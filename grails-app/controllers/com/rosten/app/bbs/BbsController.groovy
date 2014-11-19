package com.rosten.app.bbs

import grails.converters.JSON
import com.rosten.app.util.FieldAcl
import com.rosten.app.util.SystemUtil
import com.rosten.app.util.Util
import com.rosten.app.gtask.Gtask
import com.rosten.app.system.Depart
import com.rosten.app.system.Attachment
import com.rosten.app.system.Model
import com.rosten.app.system.Authorize
import com.rosten.app.system.Company
import com.rosten.app.system.User

import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.runtime.ProcessInstanceQuery
import org.activiti.engine.task.Task
import org.activiti.engine.task.TaskQuery
import com.rosten.app.workflow.FlowBusiness

import com.rosten.app.system.SystemService
import com.rosten.app.share.ShareService
import com.rosten.app.start.StartService
import com.rosten.app.workflow.WorkFlowService

class BbsController {
	def springSecurityService
	def bbsService
	def startService
	def workFlowService
	def taskService
	def systemService
	def shareService
	
	
	def bbsSearchView ={
		def model =[:]
		render(view:'/bbs/bbsSearch',model:model)
	}
	
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
		attachment.size1 = f.size
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
	def publishBbs ={
		
		def user = User.get(params.userId)
		def company = Company.get(params.companyId)
		def c = Bbs.createCriteria()
		
		//获取配置文档
		def bbsConfig = BbsConfig.first()
		def today= new Date()
		
		//取最前面9条数据
		def bbsList = []
		def cResult =c.list{
			eq("company",company)
			
			//defaultReaders为：all或者【角色】或者readers中包含当前用户的均有权访问
			like("defaultReaders", "%all%")
			
//			or{
//				
//				readers{
//					eq("id",user?.id)
//				}
//			}
			or{
				eq("status","已发布")
				eq("status","已归档")
				eq("status","已结束")
			}
			
			order("publishDate", "desc")
		}.unique()
		
		def _resultCount = cResult.size()
		if(cResult.size() > 6){
			cResult = cResult[0..5]
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
			
			if(bbsConfig){
				def _num = today - it.publishDate
				if(_num < bbsConfig.showDays){
					smap["isnew"] = true
				}
			}else{
				//默认全部显示
				smap["isnew"] = true
			}
			bbsList << smap
		}
		
		def json=[:]
		json.dataList = bbsList
		json.dataCount = _resultCount
		render json as JSON
		
	}
	
	def bbsFlowBack ={
		def json=[:]
		def bbs = Bbs.get(params.id)
		def currentUser = springSecurityService.getCurrentUser()
		def frontStatus = bbs.status
		
		try{
			//获取上一处理任务
			def frontTaskList = workFlowService.findBackAvtivity(bbs.taskId)
			if(frontTaskList && frontTaskList.size()>0){
				//简单的取最近的一个节点
				def activityEntity = frontTaskList[frontTaskList.size()-1]
				def activityId = activityEntity.getId();
				
				//流程跳转
				workFlowService.backProcess(bbs.taskId, activityId, null)
				
				//获取下一节点任务，目前处理串行情况
				def nextStatus
				def tasks = workFlowService.getTasksByFlow(bbs.processInstanceId)
				def task = tasks[0]
				if(task.getDescription() && !"".equals(task.getDescription())){
					nextStatus = task.getDescription()
				}else{
					nextStatus = task.getName()
				}
				bbs.taskId = task.getId()
				
				//获取对应节点的处理人员以及相关状态
				def historyActivity = workFlowService.getHistrotyActivityByActivity(bbs.taskId,activityId)
				def user = User.findByUsername(historyActivity.getAssignee())
				
				/*
				def taskEntity = workFlowService.getTaskEntityByActivity(activityEntity)
				def nextStatus = historyActivity.getActivityName()
				
				*/
				
				//任务指派给当前拟稿人
				taskService.claim(bbs.taskId, user.username)
				
				//增加待办事项
				def args = [:]
				args["type"] = "【公告】"
				args["content"] = "名称为  【" + bbs.topic +  "】 的公告被退回，请查看！"
				args["contentStatus"] = nextStatus
				args["contentId"] = bbs.id
				args["user"] = user
				args["company"] = user.company
				
				startService.addGtask(args)
					
				//修改当前公告信息
				bbs.currentUser = user
				bbs.currentDepart = user.getDepartName()
				bbs.currentDealDate = new Date()
				bbs.status = nextStatus
				
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
					contentStatus:frontStatus,
					status:"0"
				)
				if(gtask!=null){
					gtask.dealDate = new Date()
					gtask.status = "1"
					gtask.save(flush:true)
				}
				
				bbs.save(flush:true)
				
				//添加日志
				def logContent = "退回【" + user.getFormattedName() + "】"
				shareService.addFlowLog(bbs.id,"bbs",currentUser,logContent)
			}
				
			json["result"] = true
		}catch(Exception e){
			json["result"] = false
		}
		render json as JSON
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
		def map =[:]
		if(params.conditionName){
			map[params.conditionName] = params.conditionValue
		}
		taskService.complete(bbs.taskId,map)	//结束当前任务
		
		ProcessInstance processInstance = workFlowService.getProcessIntance(bbs.processInstanceId)
		if(!processInstance || processInstance.isEnded()){
			//流程已结束
			nextStatus = "已结束"
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
						shareService.addFlowLog(bbs.id,"bbs",nextUser,"委托授权给【" + authorize.beAuthorizerDepart + ":" + authorize.getFormattedAuthorizer() + "】")
						
						nextUser = authorize.beAuthorizer
						nextDepart = authorize.beAuthorizerDepart
					}
					//-------------------------------------------------------------------------
					
					//任务指派给当前拟稿人
					taskService.claim(bbs.taskId, nextUser.username)
					
					def args = [:]
					args["type"] = "【公告】"
					args["content"] = "请您审核名称为  【" + bbs.topic +  "】 的公告"
					args["contentStatus"] = nextStatus
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
			contentStatus:frontStatus,
			status:"0"
		)
		if(gtask!=null){
			gtask.dealDate = new Date()
			gtask.status = "1"
			gtask.save(flush:true)
		}
		
		//当前文档特殊字段处理
		def isPublish = false	//判断是否发布公告，此状态只存在状态为已结束的情况
		if((nextStatus.equals("已发布") || nextStatus.equals("已结束")) && bbs.serialNo==null){
			bbs.publisher = currentUser
			bbs.publisherDepart = currentUser.getDepartName()
			
			bbs.publishDate = new Date()
			bbs.addDefaultReader("all")
			
			bbs.serialNo = bbsConfig.nowYear + bbsConfig.nowSN.toString().padLeft(4,"0")
			
			//修改配置文档中的流水号
			bbsConfig.nowSN += 1
			bbsConfig.save()
			
			isPublish = true
		}
		
		if(bbs.save(flush:true)){
			//添加日志
			def logContent
			switch (true){
				case bbs.status.contains("已发布"):
					logContent = "发布公告【" + nextUsers.join("、") + "】"
					break
				case bbs.status.contains("已结束"):
					if(isPublish){
						logContent = "发布公告"
					}else{
						logContent = "结束流程"
					}
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
			shareService.addFlowLog(bbs.id,"bbs",currentUser,logContent)
						
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
			bbs.publishDate = Util.convertToTimestamp(params.publishDate)
			
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
			def _processInstance = workFlowService.getProcessDefinition(params.relationFlow)
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
				shareService.addFlowLog(bbs.id,params.flowCode,user,"起草公告")
				
				//修改配置文档中的流水号，改为发布后产生流水号
//				bbsConfig.nowSN += 1
//				bbsConfig.save(flush:true) 
			}
			
			//增加附件功能
			if(params.attachmentIds){
				params.attachmentIds.split(",").each{
					def attachment = Attachment.get(it)
					attachment.beUseId = bbs.id
					attachment.save(flush:true)
				}
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
		if(params.flowCode){
			//需要走流程
			def company = Company.get(params.companyId)
			def flowBusiness = FlowBusiness.findByFlowCodeAndCompany(params.flowCode,company)
			if(flowBusiness && !"".equals(flowBusiness.relationFlow)){
				params.relationFlow = flowBusiness.relationFlow
				redirect(action:"bbsShow",params:params)
			}else{
				//不存在流程引擎关联数据
				render '<h2 style="color:red;width:660px;margin:0 auto;margin-top:60px">当前业务不存在流程设置，无法创建，请联系管理员！</h2>'
			}
		}else{
			redirect(action:"bbsShow",params:params)
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
		json["level1"] = bbs.level1
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
		def currentUser = springSecurityService.getCurrentUser()
		
		def user = User.get(params.userid)
		def company = Company.get(params.companyId)
		def bbs = new Bbs()
		if(params.id){
			bbs = Bbs.get(params.id)
			//判断是否为当前处理人
			if(currentUser.equals(bbs.currentUser)){
				if(!"已结束".equals(bbs.status) && !"已发布".equals(bbs.status)){
					model["isShowFile"] = true
				}
			}
		}else{
			bbs.currentUser = user
			model["isShowFile"] = true
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
						fa.readOnly = ["level1","category","publishDate","topic","content"]
						break;
				}
			}else{
				fa.readOnly = ["level1","category","publishDate","topic","content"]
			}
		}
		model["fieldAcl"] = fa
		
		//流程相关信息----------------------------------------------
		model["relationFlow"] = params.relationFlow
		model["flowCode"] = params.flowCode
		//------------------------------------------------------
		
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
		
		//2014-9-1 增加搜索功能
		def searchArgs =[:]
		
		if(params.serialNo && !"".equals(params.serialNo)) searchArgs["serialNo"] = params.serialNo
		if(params.topic && !"".equals(params.topic)) searchArgs["topic"] = params.topic
		if(params.status && !"".equals(params.status)) searchArgs["status"] = params.status
		
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
				gridData = bbsService.getBbsListDataStoreByUser(args,searchArgs)
			}else if("all".equals(params.type)){
				//所有文档
				gridData = bbsService.getBbsListDataStore(args,searchArgs)
			}else if("new".equals(params.type)){
				//最新文档
				args["user"] = user
				args["showDays"] = bbsConfig.showDays;
				gridData = bbsService.getBbsListDataStoreByNew(args,searchArgs)
			}
			
			//处理format中的内容
			gridData.items.each{
				switch(it.level1){
				case "普通":
					it.level1 = ""
					break
				case "紧急":
					it.level1 = "images/rosten/share/alert_1.gif"
					break
				case "特急":
					it.level1 = "images/rosten/share/alert_1.gif"
					break
				}
			}
			
			json["gridData"] = gridData
			
		}
		if(params.refreshPageControl){
			
			def total
			if("person".equals(params.type)){
				//个人待办
				total = bbsService.getBbsCountByUser(company,user,searchArgs)
			}else if("all".equals(params.type)){
				//所有文档
				total = bbsService.getBbsCount(company,searchArgs)
			}else if("new".equals(params.type)){
				//最新文档
				total = bbsService.getBbsCountByNew(company,user,bbsConfig.showDays,searchArgs)
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
			//fa.readOnly = ["nowYear","nowSN","nowCancel","frontYear","frontSN","frontCancel"]
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
