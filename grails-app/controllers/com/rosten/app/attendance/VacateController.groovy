package com.rosten.app.attendance

import grails.converters.JSON
import groovy.sql.Sql

import com.rosten.app.util.FieldAcl
import com.rosten.app.util.Util
import com.rosten.app.system.Company
import com.rosten.app.system.User
import com.rosten.app.system.Model
import com.rosten.app.system.Authorize
import com.rosten.app.gtask.Gtask

import com.rosten.app.system.SystemService
import com.rosten.app.start.StartService

import com.rosten.app.workflow.WorkFlowService
import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.runtime.ProcessInstanceQuery
import org.activiti.engine.task.Task
import org.activiti.engine.task.TaskQuery

class VacateController {
	def springSecurityService
	def vacateService
	
	def systemService
	def workFlowService
	def taskService
	def startService
	def dataSource
	
	def getDealWithUser ={
		if("user".equals(params.type)){
			//选择人员，默认获取选择人员人数为大于一人，params中必须具备参数params.user：用户登录名
			redirect controller: "system",action:'userTreeDataStore', params: params
			return
		}else if("group".equals(params.type)){
			/*
			 * 通过群组选择人员
			 * 默认有一组group的方式为true，则整组均为true;true:严格控制本部门权限 
			 * 参数格式为：params.groupIds(depart-leader),params.limitDepart
			 */
			redirect controller: "system",action:'userTreeDataStore', params: params
			return
		}
	}
	
	def getSelectFlowUser ={
		def json=[:]
		json.dealFlow = true
		
		def currentUser = springSecurityService.getCurrentUser()
		
		def vacate = Vacate.get(params.id)
		def defEntity = workFlowService.getNextTaskDefinition(vacate.taskId);
		
		if(!defEntity){
			//流程处于最后一个节点
			json.dealFlow = false
			render json as JSON
			return
		}
		
		def expEntity = defEntity.getAssigneeExpression()
		if(expEntity){
			def expEntityText = expEntity.getExpressionText()
			if(expEntityText.contains("{")){
				json.user = vacate.user.username
			}else{
				json.user = expEntity.getExpressionText()
			}
			
			//判断下一处理人是否有多部门情况，如果有，则弹出对话框选择，如果没有，直接进入下一步
			def userEntity = User.findByUsername(json.user)
			def userDeparts = userEntity.getAllDepartEntity()
			if(userDeparts && userDeparts.size()>1){
				json.showDialog = true
			}else{
				json.showDialog = false
				json.userDepart = userDeparts[0].departName
				json.userId = userEntity.id
			}
			
			json.dealType = "user"
			render json as JSON
			return
		}
		
		def groupEntity = defEntity.getCandidateGroupIdExpressions()
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
			json.groupIds = groupIds.unique().join("-")
			if(limit){
				json.limitDepart = currentUser.getDepartEntityTrueName()
			}
			
			json.dealType = "group"
			render json as JSON
			return
		}
		
	}
	def vacateFlowDeal = {
		def json=[:]
		def vacate = Vacate.get(params.id)
		
		//处理当前人的待办事项
		def currentUser = springSecurityService.getCurrentUser()
		def frontStatus = vacate.status
		def nextStatus,nextDepart,nextLogContent
		def nextUsers=[]
		
		//流程引擎相关信息处理-------------------------------------------------------------------------------------
		
		//结束当前任务，并开启下一节点任务
		taskService.complete(vacate.taskId)	//结束当前任务
		
		ProcessInstance processInstance = workFlowService.getProcessIntance(vacate.processInstanceId)
		if(!processInstance || processInstance.isEnded()){
			//流程已结束
			nextStatus = "已归档"
			vacate.currentUser = null
			vacate.currentDepart = null
			vacate.taskId = null
		}else{
			//获取下一节点任务，目前处理串行情况
			def tasks = workFlowService.getTasksByFlow(vacate.processInstanceId)
			def task = tasks[0]
			if(task.getDescription() && !"".equals(task.getDescription())){
				nextStatus = task.getDescription()
			}else{
				nextStatus = task.getName()
			}
			vacate.taskId = task.getId()
		
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
					def _model = Model.findByModelCodeAndCompany("workAttendance",vacate.company)
					def authorize = systemService.checkIsAuthorizer(nextUser,_model,new Date())
					if(authorize){
						vacateService.addFlowLog(vacate,nextUser,"委托授权给【" + authorize.beAuthorizerDepart + ":" + authorize.getFormattedAuthorizer() + "】")
						
						nextUser = authorize.beAuthorizer
						nextDepart = authorize.beAuthorizerDepart
					}
					//-------------------------------------------------------------------------
					
					//任务指派给当前拟稿人
					taskService.claim(vacate.taskId, nextUser.username)
					
					def args = [:]
					args["type"] = "【请假】"
					args["content"] = "请您审核名称为  【" + vacate.getFormattedDrafter() +  "】 的请假申请"
					args["contentStatus"] = vacate.status
					args["contentId"] = vacate.id
					args["user"] = nextUser
					args["company"] = nextUser.company
					
					startService.addGtask(args)
					
					vacate.currentUser = nextUser
					vacate.currentDepart = nextDepart
					
					if(!vacate.readers.find{ item->
						item.id.equals(nextUser.id)
					}){
						vacate.addToReaders(nextUser)
					}
					nextUsers << nextUser.getFormattedName()
				}
			}
		}
		vacate.status = nextStatus
		vacate.currentDealDate = new Date()
		
		//判断下一处理人是否与当前处理人员为同一人
		if(currentUser.equals(vacate.currentUser)){
			json["refresh"] = true
		}
		
		//----------------------------------------------------------------------------------------------------
		
		//修改代办事项状态
		def gtask = Gtask.findWhere(
			user:currentUser,
			company:currentUser.company,
			contentId:vacate.id,
			contentStatus:frontStatus
		)
		if(gtask!=null){
			gtask.dealDate = new Date()
			gtask.status = "1"
			gtask.save()
		}
		
		if(vacate.save(flush:true)){
			//添加日志
			def logContent
			switch (true){
				case vacate.status.contains("已签发"):
					logContent = "签发文件【" + nextUsers.join("、") + "】"
					
					break
				case vacate.status.contains("归档"):
					logContent = "归档"
					break
				case vacate.status.contains("不同意"):
					logContent = "不同意！"
					break
				default:
					logContent = "提交" + vacate.status + "【" + nextUsers.join("、") + "】"
					break
			}
			vacateService.addFlowLog(vacate,currentUser,logContent)
						
			json["result"] = true
		}else{
			vacate.errors.each{
				println it
			}
			json["result"] = false
		}
		render json as JSON
	}
	def flowActiveExport ={
		def vacate = Vacate.get(params.id)
		InputStream imageStream = workFlowService.getflowActiveStream(vacate.processDefinitionId,vacate.taskId)
		
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
		  response.outputStream.write(b, 0, len);
		}
		response.outputStream.flush()
		response.outputStream.close()
		
	}
	def addComment ={
		def json=[:]
		def vacate = Vacate.get(params.id)
		def user = User.get(params.userId)
		if(vacate){
			def comment = new VacateComment()
			comment.user = user
			comment.status = vacate.status
			comment.content = params.dataStr
			comment.vacate = vacate
			
			if(comment.save(flush:true)){
				json["result"] = true
			}else{
				comment.errors.each{
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
		def vacate = Vacate.get(params.id)
		if(vacate){
			def logs = VacateComment.findAllByVacate(vacate,[ sort: "createDate", order: "desc"])
			model["log"] = logs
		}
		
		render(view:'/share/commentLog',model:model)
	}
	def getFlowLog={
		def model =[:]
		def vacate = Vacate.get(params.id)
		if(vacate){
			def logs = VacateLog.findAllByVacate(vacate,[ sort: "createDate", order: "asc"])
			model["log"] = logs
			
			model["logEntityId"] = params.id
			model["logEntityName"] = "vacate"
		}
		
		render(view:'/share/flowLog',model:model)
	}
	
    def vacateGrid ={
		def json=[:]
		def company = Company.get(params.companyId)
		if(params.refreshHeader){
			json["gridHeader"] = vacateService.getVacateListLayout()
		}
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			json["gridData"] = vacateService.getVacateDataStore(args)
			
		}
		if(params.refreshPageControl){
			def total = vacateService.getVacateCount(company)
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
	}
		
	def vacateDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def vacate = Vacate.get(it)
				if(vacate){
					//删除相关的gtask待办事项
					Gtask.findAllByContentId(it).each{item->
						item.delete()
					}
					vacate.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	def vacateAdd ={
		//判断是否关联流程引擎
		def company = Company.get(params.companyId)
		def model = Model.findByModelCodeAndCompany("workAttendance",company)
		if(model.relationFlow && !"".equals(model.relationFlow)){
			redirect(action:"vacateShow",params:params)
		}else{
			//不存在流程引擎关联数据
			render '<h2 style="color:red;width:660px;margin:0 auto;margin-top:60px">当前模块不存在流程设置，无法创建，请联系管理员！</h2>'
		}
	}
		
	def vacateShow ={
		def model =[:]
		
		def user = User.get(params.userid)
		def company = Company.get(params.companyId)
		def vacate = new Vacate()
		if(params.id){
			vacate = Vacate.get(params.id)
		}else{
			vacate.user = user
		}
		model["user"]=user
		model["company"] = company
		model["vacate"] = vacate
		
		FieldAcl fa = new FieldAcl()
		if("normal".equals(user.getUserType())){
			//普通用户
//			fa.readOnly += ["description"]
		}
		model["fieldAcl"] = fa
		
		render(view:'/vacate/vacate',model:model)
	}
	
	def vacateSave ={
		def json=[:]
		def vacate = new Vacate()
		def currentUser = springSecurityService.getCurrentUser()
		def _status = "new"
		
		if(params.id && !"".equals(params.id)){
			vacate = Vacate.get(params.id)
			vacate.startDate = Util.convertToTimestamp(params.startDate)
			vacate.endDate = Util.convertToTimestamp(params.endDate)
			_status = "old"
		}else{
			if(params.companyId){
				vacate.company = Company.get(params.companyId)
			}
			vacate.user = currentUser
			vacate.currentUser = currentUser
			vacate.currentDepart = currentUser.getDepartName()
			vacate.currentDealDate = new Date()
			vacate.startDate = Util.convertToTimestamp(params.startDate)
			vacate.endDate = Util.convertToTimestamp(params.endDate)
		}
		vacate.properties = params
		vacate.clearErrors()
		
		//增加读者域信息
		if(!vacate.readers.find{ it.id.equals(currentUser.id) }){
			vacate.addToReaders(currentUser)
		}
		
		//流程引擎相关信息处理-------------------------------------------------------------------------------------
		if(!vacate.processInstanceId){
			//启动流程实例
			def _model = Model.findByModelCodeAndCompany("workAttendance",vacate.company)
			def _processInstance = workFlowService.getProcessDefinition(_model.relationFlow)
			Map<String, Object> variables = new HashMap<String, Object>();
			ProcessInstance processInstance = workFlowService.addFlowInstance(_processInstance.key, currentUser.username,vacate.id, variables);
			vacate.processInstanceId = processInstance.getProcessInstanceId()
			vacate.processDefinitionId = processInstance.getProcessDefinitionId()
			
			//获取下一节点任务
			def task = workFlowService.getTasksByFlow(processInstance.getProcessInstanceId())[0]
			vacate.taskId = task.getId()
			
			//任务指派给当前拟稿人
			//taskService.claim(task.getId(), user.username)
		}
		//-------------------------------------------------------------------------------------------------
		
		if(vacate.save(flush:true)){
			json["id"] = vacate.id
			json["companyId"]=vacate.company.id
			json["result"] = "true"
			
			if("new".equals(_status)){
				//添加日志
				def _log = new VacateLog()
				_log.user = currentUser
				_log.vacate = vacate
				_log.content = "新建请假申请"
				_log.save(flush:true)
			}
			
		}else{
			vacate.errors.each{
				println it
			}
			json["result"] = "false"
		}
		render json as JSON
	}
	
	def askForStatic = {
	
		def model = [:]
		def user = springSecurityService.getCurrentUser()
		model["user"]=user
		FieldAcl fa = new FieldAcl()
		if("normal".equals(user.getUserType())){
		}
		model["fieldAcl"] = fa
		
		def items = []
		Sql sql = new Sql(dataSource)
		def seleSql = " select a.*,b.china_name,c.depart_id,c.depart_name from ( select a.*,b.bjnums,c.nxjnums,d.hjnums,e.sajnums,f.qtjnums from ( select a.user_id,sum(a.numbers) sjnums from rosten_vacate a where a.vacate_type = '事假'  group by a.user_id) a left join  ";
		seleSql+=" ( select a.user_id,sum(a.numbers) bjnums from rosten_vacate a where a.vacate_type = '病假'  group by a.user_id) b on a.user_id = b.user_id left join "
		seleSql+=" ( select a.user_id,sum(a.numbers) nxjnums from rosten_vacate a where a.vacate_type = '年休假'  group by a.user_id) c on a.user_id = b.user_id left join  "
		seleSql+=" ( select a.user_id,sum(a.numbers) hjnums from rosten_vacate a where a.vacate_type = '婚假'  group by a.user_id) d on a.user_id = b.user_id left join  "
		seleSql+=" ( select a.user_id,sum(a.numbers) sajnums from rosten_vacate a where a.vacate_type = '丧假'  group by a.user_id) e on a.user_id = b.user_id left join "
		seleSql+=" ( select a.user_id,sum(a.numbers) qtjnums from rosten_vacate a where a.vacate_type = '其他'  group by a.user_id) f on a.user_id = b.user_id ) a left join rosten_user b on a.user_id = b.id left join  "
		seleSql+=" (select  a.* ,b.depart_name from rosten_user_depart  a  left join  rosten_depart  b on a.depart_id = b.id ) c on a.user_id = c.user_id "
		def vacateList = sql.eachRow(seleSql){
			def item = ["departName":it["depart_name"],"name":it["china_name"],
				"sjnums":it["sjnums"],"bjnums":it["bjnums"],"nxjnums":it["nxjnums"],
				"hjnums":it["hjnums"],"sajnums":it["sajnums"],"qtjnums":it["qtjnums"]]
			items<<item
		}
		
		model["tableItem"] = items
		
		render(view:'/vacate/askForStatic',model:model)
	}
	def getAskForChartData ={
		def company = Company.get(params.id)
		def json = [identifier:'id',label:'name',items:[]]
		
		def sMap = ["id":001,"name":"事假","number":40]
		json.items+=sMap
		
		sMap = ["id":002,"name":"病假","number":60]
		json.items+=sMap
		
		/*
		 * 后去相关数据
		 */
		
		render json as JSON
	}
	
}
