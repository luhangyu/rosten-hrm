package com.rosten.app.bbs

import grails.converters.JSON
import com.rosten.app.util.FieldAcl
import com.rosten.app.util.SystemUtil
import com.rosten.app.system.Company
import com.rosten.app.util.Util
import com.rosten.app.system.User
import com.rosten.app.start.StartService
import com.rosten.app.gtask.Gtask
import com.rosten.app.system.Depart
import com.rosten.app.system.Attachment
import com.rosten.app.system.Model
import com.rosten.app.system.SystemService
import com.rosten.app.system.Authorize

import com.rosten.app.workflow.WorkFlowService
import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.runtime.ProcessInstanceQuery
import org.activiti.engine.task.Task
import org.activiti.engine.task.TaskQuery

class BbsController {
	def springSecurityService
	def bbsService
	def startService
	def workFlowService
	def taskService
	def systemService
	
	def getFileUpload ={
		def model =[:]
		model["docEntity"] = "bbs"
		model["isShowFile"] = false
		if(params.id){
			//已经保存过
			def bbs = Bbs.get(params.id)
			model["docEntityId"] = params.id
			//获取附件信息
			model["attachFiles"] = Attachment.findAllByBeUseId(params.id)
			
			def user = springSecurityService.getCurrentUser()
			if("admin".equals(user.getUserType())){
				model["isShowFile"] = true
			}else if(user.equals(bbs.currentUser) && !"已发布".equals(bbs.status) ){
				model["isShowFile"] = true
			}
		}else{
			//尚未保存
			model["newDoc"] = true
		}
		render(view:'/share/fileUpload',model:model)
	}
	
	def uploadFile = {
		def json=[:]
		SystemUtil sysUtil = new SystemUtil()
		
//		def uploadPath = sysUtil.getUploadPath("bbs")
		def uploadPath
		def currentUser = (User) springSecurityService.getCurrentUser()
		def companyPath = currentUser.company?.shortName
		if(companyPath == null){
			uploadPath = sysUtil.getUploadPath("bbs")
		}else{
			uploadPath = sysUtil.getUploadPath(currentUser.company.shortName + "/bbs")
		}
		
		def f = request.getFile("uploadedfile")
		if (f.empty) {
			json["result"] = "blank"
			render json as JSON
			return
		}
		
		def uploadSize = sysUtil.getUploadSize()
		if(uploadSize!=null){
			//控制附件上传大小
			def maxSize = uploadSize * 1024 * 1024
			if(f.size>=maxSize){
				json["result"] = "big"
				render json as JSON
				return
			}
		}
		String name = f.getOriginalFilename()//获得文件原始的名称
		def realName = sysUtil.getRandName(name)
		f.transferTo(new File(uploadPath,realName))
		
		def attachment = new Attachment()
		attachment.name = name
		attachment.realName = realName
		attachment.type = "bbs"
		attachment.url = uploadPath
		attachment.size = f.size
		attachment.beUseId = params.id
		attachment.upUser = (User) springSecurityService.getCurrentUser()
		attachment.save(flush:true)
		
		json["result"] = "true"
		json["fileId"] = attachment.id
		json["fileName"] = name
		
		if("yes".equals(params.isIE)){
			def resultStr  = '{"result":"true", "fileId":"' + json["fileId"]  + '","fileName":"' + json["fileName"] +'"}'
			render "<textarea>" + resultStr +  "</textarea>"
			return
		}else{
			render json as JSON
		}
		
	}
	def bbsAddComment ={
		def json=[:]
		def bbs = Bbs.get(params.id)
		def user = User.get(params.userId)
		if(bbs){
			def bbsComment = new BbsComment()
			bbsComment.user = user
			bbsComment.status = bbs.status
			bbsComment.content = params.dataStr
			bbsComment.bbs = bbs
			
			if(bbsComment.save(flush:true)){
				json["result"] = true
			}else{
				bbsComment.errors.each{
					println it
				}
				json["result"] = false
			}
			
		}else{
			json["result"] = false
		}
		
		render json as JSON
	}
	def getCommentLog ={
		def model =[:]
		def bbs = Bbs.get(params.id)
		if(bbs){
			def logs = BbsComment.findAllByBbs(bbs,[ sort: "createDate", order: "desc"])
			model["log"] = logs
		}
		
		render(view:'/share/commentLog',model:model)
	}
	def getFlowLog={
		def model =[:]
		def bbs = Bbs.get(params.id)
		if(bbs){
			def bbsLogs = BbsLog.findAllByBbs(bbs,[ sort: "createDate", order: "asc"])
			model["log"] = bbsLogs
			
			model["logEntityId"] = params.id
			model["logEntityName"] = "bbs"
		}
		
		render(view:'/share/flowLog',model:model)
	}
	
	def publishBbs ={
		
		def user = User.get(params.userId)
		def company = Company.get(params.companyId)
		
		def max = 18
		def offset = 0
		
		def c = Bbs.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			or{
				//defaultReaders为：all或者【角色】或者readers中包含当前用户的均有权访问
				like("defaultReaders", "%all%")
				readers{
					eq("id",user?.id)
				}
			}
			eq("status","已发布")
			order("publishDate", "desc")
		}
		
		//获取配置文档
		def bbsConfig = BbsConfig.first()
		def today= new Date()
		
		//取最前面9条数据
		def bbsList = []
		def cResult =c.list(pa,query).unique()
		if(cResult.size() > 9){
			cResult = cResult[0..8]
		}
		cResult.each{
			def smap =[:]
			smap["topic"] = Util.getLimitLengthString(it.topic,68,"...")
			smap["id"] = it.id
			smap["date"] = it.getFormattedPublishDate("datetime")
			
			//修改显示new的条件，改为与配置文件中的显示日期相比较----2014-4-12
//			if(!it.hasReaders.find{item->
//				 item.id == user.id 
//			}){
//				smap["isnew"] = true
//			}
			
			def _num = today - it.publishDate
			if(_num < bbsConfig.showDays){
				smap["isnew"] = true
			}
			
			bbsList << smap
		}
		render bbsList as JSON
	}
	def flowActiveExport ={
		def bbs = Bbs.get(params.id)
		InputStream imageStream = workFlowService.getflowActiveStream(bbs.processDefinitionId,bbs.taskId)
		
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
		  response.outputStream.write(b, 0, len);
		}
		response.outputStream.flush()
		response.outputStream.close()
		
	}
	def getDealWithUser ={
		def currentUser = springSecurityService.getCurrentUser()
		
		def bbs = Bbs.get(params.id)
		def bbsDefEntity = workFlowService.getNextTaskDefinition(bbs.taskId);
		
		def expEntity = bbsDefEntity.getAssigneeExpression()
		if(expEntity){
			def expEntityText = expEntity.getExpressionText()
			if(expEntityText.contains("{")){
				params.user = bbs.drafter.username
			}else{
				params.user = expEntity.getExpressionText()
			}
			
			redirect controller: "system",action:'userTreeDataStore', params: params
			return
		}
		
		def groupEntity = bbsDefEntity.getCandidateGroupIdExpressions()
		if(groupEntity.size()>0){
			//默认有一组group的方式为true，则整组均为true;true:严格控制本部门权限
			def groupIds = []
			def limit = false
			groupEntity.each{
				groupIds << Util.strLeft(it.getExpressionText(), ":")
				if(!limit && "true".equals(Util.strRight(it.getExpressionText(), ":"))){
					limit = true
				}
			}
			params.groupIds = groupIds.unique().join("-")
			if(limit){
				params.limitDepart = currentUser.getDepartEntityTrueName()
			}
			
			redirect controller: "system",action:'userTreeDataStore', params: params
			return
		}
		
	}
	def bbsFlowDeal = {
		def json=[:]
		
		//获取配置文档
		def bbsConfig = BbsConfig.first()
		def bbs = Bbs.get(params.id)
		
		//处理当前人的待办事项
		def currentUser = springSecurityService.getCurrentUser()
		def frontStatus = bbs.status
		def nextStatus,nextDepart,nextLogContent
		def nextUsers=[]
		
		//流程引擎相关信息处理-------------------------------------------------------------------------------------
		
		//结束当前任务，并开启下一节点任务
		taskService.complete(bbs.taskId)	//结束当前任务
		
		ProcessInstance processInstance = workFlowService.getProcessIntance(bbs.processInstanceId)
		if(!processInstance || processInstance.isEnded()){
			//流程已结束
			nextStatus = "已归档"
			bbs.currentUser = null
			bbs.currentDepart = null
			bbs.taskId = null
		}else{
			//获取下一节点任务，目前处理串行情况
			def tasks = workFlowService.getTasksByFlow(bbs.processInstanceId)
			def task = tasks[0]
			if(task.getDescription() && !"".equals(task.getDescription())){
				nextStatus = task.getDescription()
			}else{
				nextStatus = task.getName()
			}
			bbs.taskId = task.getId()
		
			if(params.dealUser){
				//下一步相关信息处理
				def dealUsers = params.dealUser.split(",")
				if(dealUsers.size() >1){
					//并发
				}else{
					//串行
					def nextUser = User.get(Util.strLeft(params.dealUser,":"))
					nextDepart = Util.strRight(params.dealUser, ":")
					
					//判断是否有公务授权------------------------------------------------------------
					def _model = Model.findByModelCodeAndCompany("bbs",bbs.company)
					def authorize = systemService.checkIsAuthorizer(nextUser,_model,new Date())
					if(authorize){
						bbsService.addFlowLog(bbs,nextUser,"委托授权给【" + authorize.beAuthorizerDepart + ":" + authorize.getFormattedAuthorizer() + "】")
						
						nextUser = authorize.beAuthorizer
						nextDepart = authorize.beAuthorizerDepart
					}
					//-------------------------------------------------------------------------
					
					//任务指派给当前拟稿人
					taskService.claim(bbs.taskId, nextUser.username)
					
					def args = [:]
					args["type"] = "【公告】"
					args["content"] = "请您审核名称为  【" + bbs.topic +  "】 的公告"
					args["contentStatus"] = bbs.status
					args["contentId"] = bbs.id
					args["user"] = nextUser
					args["company"] = nextUser.company
					
					startService.addGtask(args)
					
					bbs.currentUser = nextUser
					bbs.currentDepart = nextDepart
					
					if(!bbs.readers.find{ item->
						item.id.equals(nextUser.id)
					}){
						bbs.addToReaders(nextUser)
					}
					nextUsers << nextUser.getFormattedName()
				}
			}
		}
		bbs.status = nextStatus
		bbs.currentDealDate = new Date()
		
		//判断下一处理人是否与当前处理人员为同一人
		if(currentUser.equals(bbs.currentUser)){
			json["refresh"] = true
		}
		
		//----------------------------------------------------------------------------------------------------
		
		//修改代办事项状态
		def gtask = Gtask.findWhere(
			user:currentUser,
			company:currentUser.company,
			contentId:bbs.id,
			contentStatus:frontStatus
		)
		if(gtask!=null){
			gtask.dealDate = new Date()
			gtask.status = "1"
			gtask.save()
		}
		
		//当前文档特殊字段处理
		if(nextStatus.equals("已发布")){
			bbs.publisher = currentUser
			bbs.publisherDepart = currentUser.getDepartName()
			
			bbs.publishDate = new Date()
			bbs.addDefaultReader("all")
			
			bbs.serialNo = bbsConfig.nowYear + bbsConfig.nowSN.toString().padLeft(4,"0")
		}
		
		if(bbs.save(flush:true)){
			//添加日志
			def logContent
			switch (true){
				case bbs.status.contains("已签发"):
					logContent = "签发文件【" + nextUsers.join("、") + "】"
					
					//修改配置文档中的流水号
					bbsConfig.nowSN += 1
					bbsConfig.save(flush:true)
					
					break
				case bbs.status.contains("归档"):
					logContent = "归档"
					break
				case bbs.status.contains("不同意"):
					logContent = "不同意！"
					break
				default:
					logContent = "提交" + bbs.status + "【" + nextUsers.join("、") + "】"
					break
			}
			bbsService.addFlowLog(bbs,currentUser,logContent)
						
			json["result"] = true
		}else{
			bbs.errors.each{
				println it
			}
			json["result"] = false
		}
		render json as JSON
	}
	def bbsDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def bbs = Bbs.get(it)
				if(bbs){
					//删除相关的gtask待办事项
					Gtask.findAllByContentId(it).each{item->
						item.delete()
					}
					bbs.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	def bbsSave = {
		def json=[:]
		
		//获取配置文档
		def bbsConfig = BbsConfig.first()
		if(!bbsConfig){
			json["result"] = "noConfig"
			render json as JSON
			return
		}
		
		def user = springSecurityService.getCurrentUser()
		
		def bbsStatus = "new"
		def bbs
		if(params.id && !"".equals(params.id)){
			bbs = Bbs.get(params.id)
			bbs.properties = params
			bbs.clearErrors()
			bbsStatus = "old"
		}else{
			bbs = new Bbs()
			bbs.properties = params
			bbs.clearErrors()
			
			bbs.company = Company.get(params.companyId)
			bbs.currentUser = user
			bbs.currentDepart = user.getDepartName()
			bbs.currentDealDate = new Date()
			
			bbs.drafter = user
			bbs.drafterDepart = user.getDepartName()
			bbs.publishDate = Util.convertToTimestamp(params.publishDate)
			
			//发布后才产生流水号
//			bbs.serialNo = bbsConfig.nowYear + bbsConfig.nowSN.toString().padLeft(4,"0")
		}
		
		if(!bbs.readers.find{ it.id.equals(user.id) }){
			bbs.addToReaders(user)
		}
		
		//流程引擎相关信息处理-------------------------------------------------------------------------------------
		if(!bbs.processInstanceId){
			//启动流程实例
			def _model = Model.findByModelCodeAndCompany("bbs",bbs.company)
			def _processInstance = workFlowService.getProcessDefinition(_model.relationFlow)
			Map<String, Object> variables = new HashMap<String, Object>();
			ProcessInstance processInstance = workFlowService.addFlowInstance(_processInstance.key, user.username,bbs.id, variables);
			bbs.processInstanceId = processInstance.getProcessInstanceId()
			bbs.processDefinitionId = processInstance.getProcessDefinitionId()
			
			//获取下一节点任务
			def task = workFlowService.getTasksByFlow(processInstance.getProcessInstanceId())[0]
			bbs.taskId = task.getId()
			
			//任务指派给当前拟稿人
			//taskService.claim(task.getId(), user.username)
		}
		//-------------------------------------------------------------------------------------------------
		
		if(bbs.save(flush:true)){
			json["result"] = true
			json["id"] = bbs.id
			json["companyId"] = bbs.company.id
			
			if("new".equals(bbsStatus)){
				//添加日志
				def bbsLog = new BbsLog()
				bbsLog.user = user
				bbsLog.bbs = bbs
				bbsLog.content = "起草公告"
				bbsLog.save(flush:true)
				
				//修改配置文档中的流水号，改为发布后产生流水号
//				bbsConfig.nowSN += 1
//				bbsConfig.save(flush:true) 
			}
		}else{
			bbs.errors.each{
				println it
			}
			json["result"] = false
		}
		render json as JSON
	}
	def hasReadBbs ={
		def json=[:]
		def user = User.get(params.userId)
		def bbs = Bbs.get(params.id)
		
		//增加已读标记
		if(!bbs.hasReaders.find{ it.id == user.id }){
			bbs.addToHasReaders(user)
			if(bbs.save(flush:true)){
				json["result"] = true
			}else{
				bbs.errors.each{
					println it
				}
				json["result"] = false
			}
		}else{
			json["result"] = true
		}
		render json as JSON
	}
	def bbsAdd ={
		//判断是否关联流程引擎
		def company = Company.get(params.companyId)
		def model = Model.findByModelCodeAndCompany("bbs",company)
		if(model.relationFlow && !"".equals(model.relationFlow)){
			redirect(action:"bbsShow",params:params)
		}else{
			//不存在流程引擎关联数据
			render '<h2 style="color:red;width:660px;margin:0 auto;margin-top:60px">当前模块不存在流程设置，无法创建，请联系管理员！</h2>'
		}
	}
	def bbsGetContent ={
		def bbs = Bbs.get(params.id)
		render bbs.content
	}
	def getBbsJson = {
		def json=[:]
		def bbs = Bbs.get(params.id)
		json["topic"] = bbs.topic
		json["content"] = bbs.content
		json["level"] = bbs.level
		json["publishDate"] = bbs.getFormattedPublishDate()
		json["serialNo"] = bbs.serialNo
		json["category"] = bbs.category
		
		render json as JSON
	}
	def bbsShowStart ={
		def model =[:]
		def bbs = Bbs.get(params.id)
		model["bbs"] = bbs
		
		model["attachFiles"] = Attachment.findAllByBeUseId(bbs.id)
		
		render(view:'/bbs/bbsShow',model:model)
	}
	def bbsShow ={
		def model =[:]
		
		def user = User.get(params.userid)
		def company = Company.get(params.companyId)
		def bbs = new Bbs()
		if(params.id){
			bbs = Bbs.get(params.id)
		}else{
			bbs.currentUser = user
		}
		
		if(!bbs){
			render '<h2 style="color:red;width:500px;margin:0 auto">此公告已过期或删除，请联系管理员！</h2>'
			return
		}
		
		model["bbs"] = bbs
		model["user"]=user
		model["company"] = company
		
		FieldAcl fa = new FieldAcl()
		if("normal".equals(user.getUserType())){
			//普通用户
			if(user.equals(bbs.currentUser)){
				switch(bbs.status){
					case "起草":
						break
					case "待发布":
						break
					case "已发布":
						fa.readOnly = ["level","category","publishDate","topic","content"]
						break;
				}
			}else{
				fa.readOnly = ["level","category","publishDate","topic","content"]
			}
		}
		model["fieldAcl"] = fa
		
		render(view:'/bbs/bbs',model:model)
	}
	def bbsGrid ={
		def json=[:]
		def company = Company.get(params.companyId)
		def user = User.get(params.userId)
		def bbsConfig = BbsConfig.findByCompany(company)
		
		if(params.refreshHeader){
			json["gridHeader"] = bbsService.getBbsListLayout()
		}
		
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			
			def gridData
			if("person".equals(params.type)){
				//个人待办
				args["user"] = user
				gridData = bbsService.getBbsListDataStoreByUser(args)
			}else if("all".equals(params.type)){
				//所有文档
				gridData = bbsService.getBbsListDataStore(args)
			}else if("new".equals(params.type)){
				//最新文档
				args["user"] = user
				args["showDays"] = bbsConfig.showDays;
				gridData = bbsService.getBbsListDataStoreByNew(args)
			}
			
			//处理format中的内容
			gridData.items.each{
				switch(it.level){
				case "普通":
					it.level = ""
					break
				case "紧急":
					it.level = "images/rosten/share/alert_1.gif"
					break
				case "特急":
					it.level = "images/rosten/share/alert_1.gif"
					break
				}
			}
			
			json["gridData"] = gridData
			
		}
		if(params.refreshPageControl){
			
			def total
			if("person".equals(params.type)){
				//个人待办
				total = bbsService.getBbsCountByUser(company,user)
			}else if("all".equals(params.type)){
				//所有文档
				total = bbsService.getBbsCount(company)
			}else if("new".equals(params.type)){
				//最新文档
				total = bbsService.getBbsCountByNew(company,user,bbsConfig.showDays)
			}
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
	}
	def bbsConfig = {
		def model = [:]
		def user = springSecurityService.getCurrentUser()
		
		def bbsConfig = BbsConfig.findWhere(company:user.company)
		if(bbsConfig==null) {
			bbsConfig = new BbsConfig()
			
			Calendar cal = Calendar.getInstance();
			bbsConfig.nowYear = cal.get(Calendar.YEAR)
			bbsConfig.frontYear = bbsConfig.nowYear -1
			
			model.companyId = user.company.id
		}else{
			model.companyId = bbsConfig.company.id
		}
		model.bbsConfig = bbsConfig
		
		FieldAcl fa = new FieldAcl()
		if("normal".equals(user.getUserType())){
			//普通用户
			fa.readOnly = ["nowYear","nowSN","nowCancel","frontYear","frontSN","frontCancel"]
		}else{
//			fa.readOnly = ["nowCancel","frontCancel"]
		}
		model["fieldAcl"] = fa
		
		render(view:'/bbs/bbsConfig',model:model)
	}
	def bbsConfigSave ={
		def json=[:]
		def bbsConfig = new BbsConfig()
		if(params.id && !"".equals(params.id)){
			bbsConfig = BbsConfig.get(params.id)
		}
		bbsConfig.properties = params
		bbsConfig.clearErrors()
		bbsConfig.company = Company.get(params.companyId)
		
		bbsConfig.nowCancel = params.bbsConfig_nowCancel
		bbsConfig.frontCancel = params.bbsConfig_frontCancel
		
		if(bbsConfig.save(flush:true)){
			json["result"] = true
			json["bbsConfigId"] = bbsConfig.id
			json["companyId"] = bbsConfig.company.id
		}else{
			bbsConfig.errors.each{
				println it
			}
			json["result"] = false
		}
		render json as JSON
	}
    def index() { }
}
