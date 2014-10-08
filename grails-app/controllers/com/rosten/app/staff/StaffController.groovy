package com.rosten.app.staff

import com.rosten.app.system.Company
import com.rosten.app.system.User
import com.rosten.app.system.Model
import com.rosten.app.share.ShareService
import com.rosten.app.util.FieldAcl
import com.rosten.app.system.Depart
import com.rosten.app.util.Util
import com.rosten.app.system.UserDepart

import grails.converters.JSON

import com.rosten.app.system.UserRole
import com.rosten.app.system.UserType
import com.rosten.app.system.SystemService
import com.rosten.app.system.Role

import java.io.OutputStream

import com.rosten.app.export.ExcelExport
import com.rosten.app.export.WordExport;
import com.rosten.app.workflow.FlowBusiness
import com.rosten.app.workflow.WorkFlowService

import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.runtime.ProcessInstanceQuery
import org.activiti.engine.task.Task
import org.activiti.engine.task.TaskQuery

import com.rosten.app.share.FlowLog
import com.rosten.app.start.StartService
import com.rosten.app.gtask.Gtask

class StaffController {
	def springSecurityService
	def systemService
	def shareService
	def staffService
	def workFlowService
	def taskService
	def startService
	
	def bargainAdd ={
		redirect(action:"bargainShow",params:params)
	}
	def bargainShow ={
		def model =[:]
		model["company"] = Company.get(params.companyId)
		
		def bargain
		if(params.id){
			bargain = Bargain.get(params.id)
		}else{
			bargain = new Bargain()
		}
		model["bargain"] = bargain
		
		FieldAcl fa = new FieldAcl()
		model["fieldAcl"] = fa
		render(view:'/staff/bargainShow',model:model)
	}
	def bargainSave ={
		def model=[:]
		
		def company = Company.get(params.companyId)
		def bargain = new Bargain()
		if(params.id && !"".equals(params.id)){
			bargain = Bargain.get(params.id)
		}else{
			bargain.company = company
		}
		
		bargain.properties = params
		bargain.clearErrors()
		bargain.changeDate = Util.convertToTimestamp(params.changeDate)
		
		def personInfor = PersonInfor.get(params.personInforId)
		bargain.personInfor = personInfor
		
		if(bargain.save(flush:true)){
			model["result"] = "true"
		}else{
			bargain.errors.each{
				println it
			}
			model["result"] = "false"
		}
		render model as JSON
	}
	def bargainDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def bargain = Bargain.get(it)
				if(bargain){
					bargain.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	def bargainGrid ={
		def model=[:]
		def company = Company.get(params.companyId)
		if(params.refreshHeader){
			model["gridHeader"] = staffService.getBargainListLayout()
		}
		
		def searchArgs =[:]
		
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			model["gridData"] = staffService.getBargainListDataStore(args,searchArgs)
			
		}
		if(params.refreshPageControl){
			def total = staffService.getBargainCount(company,searchArgs)
			model["pageControl"] = ["total":total.toString()]
		}
		render model as JSON
	}
	
	def staffStatusChangeAdd ={
		redirect(action:"staffStatusChangeShow",params:params)
	}
	def staffStatusChangeShow ={
		def model =[:]
		model["company"] = Company.get(params.companyId)
		
		def statusChange
		if(params.id){
			statusChange = StatusChange.get(params.id)
		}else{
			statusChange = new StatusChange()
			if("leave".equals(params.type)){
				statusChange.changeType = "离职"
			}else{
				statusChange.changeType = "退休"
			}
		}
		model["statusChange"] = statusChange
		model["personInfor"] = statusChange.personInfor
		
		FieldAcl fa = new FieldAcl()
		model["fieldAcl"] = fa
		render(view:'/staff/statusChange',model:model)
	}
	def staffStatusChangeSave ={
		def model=[:]
		
		def currentUser = springSecurityService.getCurrentUser()
		def company = Company.get(params.companyId)
		def statusChange = new StatusChange()
		if(params.id && !"".equals(params.id)){
			statusChange = StatusChange.get(params.id)
		}else{
			statusChange.company = company
			statusChange.applayUser = currentUser
		}
		
		statusChange.properties = params
		statusChange.clearErrors()
		statusChange.changeDate = Util.convertToTimestamp(params.changeDate)
		
		def personInfor = PersonInfor.get(params.personInforId)
		statusChange.personInfor = personInfor
		
		if(statusChange.save(flush:true)){
			personInfor.status = statusChange.changeType
			personInfor.save(flush:true)
			
			model["result"] = "true"
		}else{
			statusChange.errors.each{
				println it
			}
			model["result"] = "false"
		}
		render model as JSON
	}
	def staffStatusChangeDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def statusChange = StatusChange.get(it)
				if(statusChange){
					statusChange.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	def staffStatusChangeGrid ={
		def model=[:]
		def company = Company.get(params.companyId)
		if(params.refreshHeader){
			model["gridHeader"] = staffService.getStaffStatusChangeListLayout()
		}
		
		def searchArgs =[:]
		if("leave".equals(params.type)){
			searchArgs.changeType = "离职"
		}else{
			searchArgs.changeType = "退休"
		}
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			model["gridData"] = staffService.getStaffStatusChangeListDataStore(args,searchArgs)
			
		}
		if(params.refreshPageControl){
			def total = staffService.getStaffStatusChangeCount(company,searchArgs)
			model["pageControl"] = ["total":total.toString()]
		}
		render model as JSON
	}
	
	def staffDepartChangeFlowDeal ={
		def json=[:]
		
		def departChange = DepartChange.get(params.id)
		
		//处理当前人的待办事项
		def currentUser = springSecurityService.getCurrentUser()
		def frontStatus = departChange.status
		def nextStatus,nextDepart,nextLogContent
		def nextUsers=[]
		
		//流程引擎相关信息处理-------------------------------------------------------------------------------------
		
		//结束当前任务，并开启下一节点任务
		def map =[:]
		if(params.conditionName){
			map[params.conditionName] = params.conditionValue
		}
		taskService.complete(departChange.taskId,map)	//结束当前任务
		
		ProcessInstance processInstance = workFlowService.getProcessIntance(departChange.processInstanceId)
		if(!processInstance || processInstance.isEnded()){
			//流程已结束
			nextStatus = "已结束"
			departChange.currentUser = null
			departChange.currentDepart = null
			departChange.taskId = null
		}else{
			//获取下一节点任务，目前处理串行情况
			def tasks = workFlowService.getTasksByFlow(departChange.processInstanceId)
			def task = tasks[0]
			if(task.getDescription() && !"".equals(task.getDescription())){
				nextStatus = task.getDescription()
			}else{
				nextStatus = task.getName()
			}
			departChange.taskId = task.getId()
		
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
					def _model = Model.findByModelCodeAndCompany("staffManage",currentUser.company)
					def authorize = systemService.checkIsAuthorizer(nextUser,_model,new Date())
					if(authorize){
						shareService.addFlowLog(departChange.id,"staffAdd",nextUser,"委托授权给【" + authorize.beAuthorizerDepart + ":" + authorize.getFormattedAuthorizer() + "】")
						nextUser = authorize.beAuthorizer
						nextDepart = authorize.beAuthorizerDepart
					}
					//-------------------------------------------------------------------------
					
					//任务指派给当前拟稿人
					taskService.claim(departChange.taskId, nextUser.username)
					
					def args = [:]
					args["type"] = "【员工调动】"
					args["content"] = "请您审核名称为  【" + departChange.getPersonInforName() +  "】 的入职人员信息"
					args["contentStatus"] = nextStatus
					args["contentId"] = departChange.id
					args["user"] = nextUser
					args["company"] = nextUser.company
					
					startService.addGtask(args)
					
					departChange.currentUser = nextUser
					departChange.currentDepart = nextDepart
					
					if(!departChange.readers.find{ item->
						item.id.equals(nextUser.id)
					}){
						departChange.addToReaders(nextUser)
					}
					nextUsers << nextUser.getFormattedName()
				}
			}
		}
		departChange.status = nextStatus
		departChange.currentDealDate = new Date()
		
		//判断下一处理人是否与当前处理人员为同一人
		if(currentUser.equals(departChange.currentUser)){
			json["refresh"] = true
		}
		
		//----------------------------------------------------------------------------------------------------
		
		//修改代办事项状态
		def gtask = Gtask.findWhere(
			user:currentUser,
			company:currentUser.company,
			contentId:departChange.id,
			contentStatus:frontStatus,
			status:"0"
		)
		if(gtask!=null){
			gtask.dealDate = new Date()
			gtask.status = "1"
			gtask.save(flush:true)
		}
		
		if(departChange.save(flush:true)){
			//添加日志
			def logContent
			switch (true){
				case departChange.status.contains("已结束"):
					logContent = "结束流程"
					break
				case departChange.status.contains("归档"):
					logContent = "归档"
					break
				case departChange.status.contains("不同意"):
					logContent = "不同意！"
					break
				default:
					logContent = "提交" + departChange.status + "【" + nextUsers.join("、") + "】"
					break
			}
			shareService.addFlowLog(departChange.id,"staffDepartChange",currentUser,logContent)
						
			json["result"] = true
		}else{
			departChange.errors.each{
				println it
			}
			json["result"] = false
		}
		render json as JSON
	}
	def staffDepartChangeFlowBack ={
		def json=[:]
		def departChange = DepartChange.get(params.id)
		
		def currentUser = springSecurityService.getCurrentUser()
		def frontStatus = departChange.status
		
		try{
			//获取上一处理任务
			def frontTaskList = workFlowService.findBackAvtivity(departChange.taskId)
			if(frontTaskList && frontTaskList.size()>0){
				//简单的取最近的一个节点
				def activityEntity = frontTaskList[frontTaskList.size()-1]
				def activityId = activityEntity.getId();
				
				//流程跳转
				workFlowService.backProcess(departChange.taskId, activityId, null)
				
				//获取下一节点任务，目前处理串行情况
				def nextStatus
				def tasks = workFlowService.getTasksByFlow(departChange.processInstanceId)
				def task = tasks[0]
				if(task.getDescription() && !"".equals(task.getDescription())){
					nextStatus = task.getDescription()
				}else{
					nextStatus = task.getName()
				}
				departChange.taskId = task.getId()
				
				//获取对应节点的处理人员以及相关状态
				def historyActivity = workFlowService.getHistrotyActivityByActivity(departChange.taskId,activityId)
				def user = User.findByUsername(historyActivity.getAssignee())
				
				//任务指派给当前拟稿人
				taskService.claim(departChange.taskId, user.username)
				
				//增加待办事项
				def args = [:]
				args["type"] = "【员工调动】"
				args["content"] = "名称为  【" + departChange.getPersonInforName() +  "】 的入职人员信息被退回，请查看！"
				args["contentStatus"] = nextStatus
				args["contentId"] = departChange.id
				args["user"] = user
				args["company"] = user.company
				
				startService.addGtask(args)
					
				//修改相关信息
				departChange.currentUser = user
				departChange.currentDepart = user.getDepartName()
				departChange.currentDealDate = new Date()
				departChange.status = nextStatus
				
				//判断下一处理人是否与当前处理人员为同一人
				if(currentUser.equals(departChange.currentUser)){
					json["refresh"] = true
				}
				
				//----------------------------------------------------------------------------------------------------
				
				//修改代办事项状态
				def gtask = Gtask.findWhere(
					user:currentUser,
					company:currentUser.company,
					contentId:departChange.id,
					contentStatus:frontStatus,
					status:"0"
				)
				if(gtask!=null){
					gtask.dealDate = new Date()
					gtask.status = "1"
					gtask.save(flush:true)
				}
				
				departChange.save(flush:true)
				
				//添加日志
				def logContent = "退回【" + user.getFormattedName() + "】"
				
				shareService.addFlowLog(departChange.id,"staffDepartChange",currentUser,logContent)
			}
				
			json["result"] = true
		}catch(Exception e){
			json["result"] = false
		}
		render json as JSON
	}
	def staffDepartChangeAdd ={
		if(params.flowCode){
			//需要走流程
			def company = Company.get(params.companyId)
			def flowBusiness = FlowBusiness.findByFlowCodeAndCompany(params.flowCode,company)
			if(flowBusiness && !"".equals(flowBusiness.relationFlow)){
				params.relationFlow = flowBusiness.relationFlow
				redirect(action:"staffDepartChangeShow",params:params)
			}else{
				//不存在流程引擎关联数据
				render '<h2 style="color:red;width:660px;margin:0 auto;margin-top:60px">当前业务不存在流程设置，无法创建，请联系管理员！</h2>'
			}
		}else{
			redirect(action:"staffDepartChangeShow",params:params)
		}
		
	}
	def staffDepartChangeShow ={
		def model =[:]
		def user = User.get(params.userid)
		model["company"] = Company.get(params.companyId)
		model["user"] = user
		
		def departChange
		if(params.id){
			departChange = DepartChange.get(params.id)
			model["isShowFile"] = true
		}else{
			departChange = new DepartChange()
			departChange.personInfor = PersonInfor.findByUser(user)
		}
		model["departChange"] = departChange
		model["personInfor"] = departChange.personInfor
		
		//流程相关信息----------------------------------------------
		model["relationFlow"] = params.relationFlow
		model["flowCode"] = params.flowCode
		//------------------------------------------------------
		
		FieldAcl fa = new FieldAcl()
		model["fieldAcl"] = fa
		render(view:'/staff/departChange',model:model)
	}
	def staffDepartChangeSave ={
		def model=[:]
		def currentUser = springSecurityService.getCurrentUser()
		def company = Company.get(params.companyId)
		
		def departChange = new DepartChange()
		if(params.id && !"".equals(params.id)){
			departChange = DepartChange.get(params.id)
		}else{
			departChange.company = company
		}
		
		departChange.properties = params
		departChange.clearErrors()
		
		departChange.personInfor = PersonInfor.get(params.personInforId)
		departChange.applayUser = currentUser
		
		departChange.changeDate = Util.convertToTimestamp(params.changeDate)
		
		def inDepart = Depart.get(params.allowdepartsId)
		departChange.inDepart = inDepart
		
		
		//判断是否需要走流程
		def _status
		if(params.relationFlow){
			//需要走流程
			if(params.id){
				_status = "old"
			}else{
				_status = "new"
				departChange.currentUser = currentUser
				departChange.currentDepart = currentUser.getDepartName()
				departChange.currentDealDate = new Date()
				
				departChange.drafter = currentUser
				departChange.drafterDepart = currentUser.getDepartName()
			}
			
			//增加读者域
			if(!departChange.readers.find{ it.id.equals(currentUser.id) }){
				departChange.addToReaders(currentUser)
			}
			
			//流程引擎相关信息处理-------------------------------------------------------------------------------------
			if(!departChange.processInstanceId){
				//启动流程实例
				def _processInstance = workFlowService.getProcessDefinition(params.relationFlow)
				Map<String, Object> variables = new HashMap<String, Object>();
				ProcessInstance processInstance = workFlowService.addFlowInstance(_processInstance.key, currentUser.username,departChange.id, variables);
				departChange.processInstanceId = processInstance.getProcessInstanceId()
				departChange.processDefinitionId = processInstance.getProcessDefinitionId()
				
				//获取下一节点任务
				def task = workFlowService.getTasksByFlow(processInstance.getProcessInstanceId())[0]
				departChange.taskId = task.getId()
			}
			//-------------------------------------------------------------------------------------------------
		}
		
		
		if(departChange.save(flush:true)){
			model["id"] = departChange.id
			model["result"] = "true"
			
			//流程引擎相关日志信息
			if("new".equals(_status)){
				//添加日志
				shareService.addFlowLog(departChange.id,params.flowCode,currentUser,"新建员工调动信息")
			}
			
		}else{
			departChange.errors.each{
				println it
			}
			model["result"] = "false"
		}
		render model as JSON
	}
	def staffDepartChangeDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def departChange = DepartChange.get(it)
				if(departChange){
					departChange.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	def staffDepartChangeGrid ={
		def model=[:]
		def company = Company.get(params.companyId)
		if(params.refreshHeader){
			model["gridHeader"] = staffService.getStaffDepartChangeListLayout()
		}
		
		def searchArgs =[:]
		
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			model["gridData"] = staffService.getStaffDepartChangeListDataStore(args,searchArgs)
			
		}
		if(params.refreshPageControl){
			def total = staffService.getStaffDepartChangeCount(company,searchArgs)
			model["pageControl"] = ["total":total.toString()]
		}
		render model as JSON
	}
	
	def searchView ={
		def model =[:]
		render(view:'/staff/search',model:model)
	}
	def staffAddFlowBack ={
		def json=[:]
		def personInfor = PersonInfor.get(params.id)
		
		def currentUser = springSecurityService.getCurrentUser()
		def frontStatus = personInfor.status
		
		try{
			//获取上一处理任务
			def frontTaskList = workFlowService.findBackAvtivity(personInfor.taskId)
			if(frontTaskList && frontTaskList.size()>0){
				//简单的取最近的一个节点
				def activityEntity = frontTaskList[frontTaskList.size()-1]
				def activityId = activityEntity.getId();
				
				//流程跳转
				workFlowService.backProcess(personInfor.taskId, activityId, null)
				
				//获取下一节点任务，目前处理串行情况
				def nextStatus
				def tasks = workFlowService.getTasksByFlow(personInfor.processInstanceId)
				def task = tasks[0]
				if(task.getDescription() && !"".equals(task.getDescription())){
					nextStatus = task.getDescription()
				}else{
					nextStatus = task.getName()
				}
				personInfor.taskId = task.getId()
				
				//获取对应节点的处理人员以及相关状态
				def historyActivity = workFlowService.getHistrotyActivityByActivity(personInfor.taskId,activityId)
				def user = User.findByUsername(historyActivity.getAssignee())
				
				//任务指派给当前拟稿人
				taskService.claim(personInfor.taskId, user.username)
				
				//增加待办事项
				def args = [:]
				args["type"] = "【公告】"
				args["content"] = "名称为  【" + personInfor.chinaName +  "】 的入职人员信息被退回，请查看！"
				args["contentStatus"] = nextStatus
				args["contentId"] = personInfor.id
				args["user"] = user
				args["company"] = user.company
				
				startService.addGtask(args)
					
				//修改当前公告信息
				personInfor.currentUser = user
				personInfor.currentDepart = user.getDepartName()
				personInfor.currentDealDate = new Date()
				personInfor.status = nextStatus
				
				//判断下一处理人是否与当前处理人员为同一人
				if(currentUser.equals(personInfor.currentUser)){
					json["refresh"] = true
				}
				
				//----------------------------------------------------------------------------------------------------
				
				//修改代办事项状态
				def gtask = Gtask.findWhere(
					user:currentUser,
					company:currentUser.company,
					contentId:personInfor.id,
					contentStatus:frontStatus,
					status:"0"
				)
				if(gtask!=null){
					gtask.dealDate = new Date()
					gtask.status = "1"
					gtask.save(flush:true)
				}
				
				personInfor.save(flush:true)
				
				//添加日志
				def logContent = "退回【" + user.getFormattedName() + "】"
				
				shareService.addFlowLog(personInfor.id,"staffAdd",currentUser,logContent)
			}
				
			json["result"] = true
		}catch(Exception e){
			json["result"] = false
		}
		render json as JSON
	}
	def staffAddFlowDeal ={
		def json=[:]
		
		def personInfor = PersonInfor.get(params.id)
		
		//默认保存当前信息
		if(params.msResult){
			//面试结果
			personInfor.msResult = params.msResult
		}
		
		//处理当前人的待办事项
		def currentUser = springSecurityService.getCurrentUser()
		def frontStatus = personInfor.status
		def nextStatus,nextDepart,nextLogContent
		def nextUsers=[]
		
		//流程引擎相关信息处理-------------------------------------------------------------------------------------
		
		//结束当前任务，并开启下一节点任务
		def map =[:]
		if(params.conditionName){
			map[params.conditionName] = params.conditionValue
		}
		taskService.complete(personInfor.taskId,map)	//结束当前任务
		
		ProcessInstance processInstance = workFlowService.getProcessIntance(personInfor.processInstanceId)
		if(!processInstance || processInstance.isEnded()){
			//流程已结束
			nextStatus = "试用"
			if(params.conditionName){
				nextStatus = "实习"	
			}
			personInfor.currentUser = null
			personInfor.currentDepart = null
			personInfor.taskId = null
		}else{
			//获取下一节点任务，目前处理串行情况
			def tasks = workFlowService.getTasksByFlow(personInfor.processInstanceId)
			def task = tasks[0]
			if(task.getDescription() && !"".equals(task.getDescription())){
				nextStatus = task.getDescription()
			}else{
				nextStatus = task.getName()
			}
			personInfor.taskId = task.getId()
		
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
					def _model = Model.findByModelCodeAndCompany("staffManage",currentUser.company)
					def authorize = systemService.checkIsAuthorizer(nextUser,_model,new Date())
					if(authorize){
						shareService.addFlowLog(personInfor.id,"staffAdd",nextUser,"委托授权给【" + authorize.beAuthorizerDepart + ":" + authorize.getFormattedAuthorizer() + "】")
						nextUser = authorize.beAuthorizer
						nextDepart = authorize.beAuthorizerDepart
					}
					//-------------------------------------------------------------------------
					
					//任务指派给当前拟稿人
					taskService.claim(personInfor.taskId, nextUser.username)
					
					def args = [:]
					args["type"] = "【员工入职】"
					args["content"] = "请您审核名称为  【" + personInfor.chinaName +  "】 的入职人员信息"
					args["contentStatus"] = nextStatus
					args["contentId"] = personInfor.id
					args["user"] = nextUser
					args["company"] = nextUser.company
					
					startService.addGtask(args)
					
					personInfor.currentUser = nextUser
					personInfor.currentDepart = nextDepart
					
					if(!personInfor.readers.find{ item->
						item.id.equals(nextUser.id)
					}){
						personInfor.addToReaders(nextUser)
					}
					nextUsers << nextUser.getFormattedName()
				}
			}
		}
		personInfor.status = nextStatus
		personInfor.currentDealDate = new Date()
		
		//判断下一处理人是否与当前处理人员为同一人
		if(currentUser.equals(personInfor.currentUser)){
			json["refresh"] = true
		}
		
		//----------------------------------------------------------------------------------------------------
		
		//修改代办事项状态
		def gtask = Gtask.findWhere(
			user:currentUser,
			company:currentUser.company,
			contentId:personInfor.id,
			contentStatus:frontStatus,
			status:"0"
		)
		if(gtask!=null){
			gtask.dealDate = new Date()
			gtask.status = "1"
			gtask.save(flush:true)
		}
		
		if(personInfor.save(flush:true)){
			//添加日志
			def logContent
			switch (true){
				case personInfor.status.contains("已结束"):
					logContent = "结束流程"
					break
				case personInfor.status.contains("归档"):
					logContent = "归档"
					break
				case personInfor.status.contains("不同意"):
					logContent = "不同意！"
					break
				default:
					logContent = "提交" + personInfor.status + "【" + nextUsers.join("、") + "】"
					break
			}
			shareService.addFlowLog(personInfor.id,"staffAdd",currentUser,logContent)
						
			json["result"] = true
		}else{
			personInfor.errors.each{
				println it
			}
			json["result"] = false
		}
		render json as JSON
	}
	
	def asignAccount ={
		def model =[:]
		model["company"] = Company.get(params.companyId)
		
		def personInfor = PersonInfor.get(params.id)
		def user = personInfor.user
		model["user"] = user
		model["personInforId"] = params.id
		
		if(user){
			//具有角色
			def allowrolesName=[]
			def allowrolesId =[]
			UserRole.findAllByUser(user).each{
				allowrolesName << it.role.authority
				allowrolesId << it.role.id
			}
			
			model["allowrolesName"] = allowrolesName.join(',')
			model["allowrolesId"] = allowrolesId.join(",")
			model["username"] = Util.strRight(user.username, "-")
		}
		
		
		render(view:'/staff/asignAccount',model:model)
	}
	def asignAccountSubmit ={
		def json
		try{
			//所属机构
			def currentUser = springSecurityService.getCurrentUser()
			def company = currentUser.company
			
			def user 
			if(params.id && !"".equals(params.id)){
				user = User.get(params.id)
			}else{
				user = new User()
				user.enabled = true
			}
			if(params.userNameFront){
				params.username = params.userNameFront + params.username
			}
			
			user.properties = params
			user.clearErrors()
			
			user.company = company
			user.save()
			
			def personInfor = PersonInfor.get(params.personInforId)
			personInfor.user = user
			
			if(personInfor.save(flush:true)){
				
				UserRole.removeAll(user)
				if(params.allowrolesId){
					params.allowrolesId.split(",").each{
						def role = Role.get(it)
						UserRole.create(user, role)
					}
				}
				
				UserDepart.removeAll(user)
				personInfor.departs.each{
					UserDepart.create(user, it)
				}
				
				json = [result:'true']
			}else{
				json = [result:'false']
			}
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	def importStaff ={
		def model =[:]
		model["company"] = Company.get(params.id)
		render(view:'/staff/importStaff',model:model)
	}
	def staffRetire ={
		//退休
		def json
		try{
			def user = User.get(params.id)
			def personInfor = PersonInfor.findByUser(user)
			if(personInfor){
				personInfor.status = "已退休"
				personInfor.save()
				
				//增加处理日志
				def staffLog = new StaffLog()
				staffLog.type = "退休"
				staffLog.dealUser = springSecurityService.getCurrentUser()
				staffLog.user = user
				staffLog.reson = params.dataStr
				
				staffLog.save(flush:true)
			}
				
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	
	def staffLeave ={
		//离职
		def json
		try{
			def user = User.get(params.id)
			def personInfor = PersonInfor.findByUser(user)
			if(personInfor){
				personInfor.status = "已离职"
				personInfor.save()
				
				//增加处理日志
				def staffLog = new StaffLog()
				staffLog.type = "离职"
				staffLog.dealUser = springSecurityService.getCurrentUser()
				staffLog.user = user
				staffLog.reson = params.dataStr
				
				staffLog.save(flush:true)
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	
	def staffChangeDepart ={
		def json,oldDepartName
		try{
			def user = User.get(params.userId)
			def departEntity = Depart.get(params.newDepartId)
			oldDepartName = user.getDepartName()
			
			if(departEntity){
				UserDepart.removeAll(user)
				UserDepart.create(user, departEntity)
				
				//增加处理日志
				def staffLog = new StaffLog()
				staffLog.type = "部门调动"
				staffLog.oldDepart = oldDepartName
				staffLog.nowDepart = departEntity.departName
				staffLog.dealUser = springSecurityService.getCurrentUser()
				staffLog.user = user
				
				staffLog.save(flush:true)
				
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
		
	}
	def serachPerson ={
		def company = Company.get(params.companyId)
		
		def c = User.createCriteria()
		def _userList = c.list({
			eq("company",company)
			or{
				like("username","%" + params.serchInput +  "%")
				like("chinaName","%" + params.serchInput +  "%")
				like("telephone","%" + params.serchInput +  "%")
			}
		})
		def smap =[:]
		if(_userList && _userList.size()>0){
			def _user = _userList[0]
			
			smap["userId"] = _user.id
			smap["username"] = _user.username
			smap["phone"] = _user.telephone
			smap["mobile"] = _user.telephone
			smap["email"] = _user.email
			
			smap["userDepartId"] = _user.getDepartEntity()?.id
			smap["userDepart"] = _user.getDepartEntity()?.departName
			
			def personInfor = PersonInfor.findByUser(_user)
			if(personInfor){
				smap["sex"] = personInfor.sex
				smap["idCard"] = personInfor.idCard
				smap["birthday"] = personInfor.birthday
				smap["city"] = personInfor.city
				smap["nationality"] = personInfor.nationality
				smap["birthAddress"] = personInfor.birthAddress
				smap["nativeAddress"] = personInfor.nativeAddress
				smap["politicsStatus"] = personInfor.politicsStatus
				smap["marriage"] = personInfor.marriage
				smap["religion"] = personInfor.religion
				smap["personInforId"] = personInfor.id
			}
		}
		render smap as JSON
	}
	def staffDepartChange ={
		def model = [:]
		model.companyId = params.companyId
		render(view:'/staff/changeDepart',model:model)
	}
	def userDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def user = User.get(it)
				if(user){
					//先刪除关联信息
					PersonInfor.findByUser(user)?.delete(flush:true)
					user.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			log.debug(e);
			json = [result:'error']
		}
		render json as JSON
		
	}
	
	def userSave ={
		def model=[:]
		def currentUser = springSecurityService.getCurrentUser()
		
		//用户类型
		def userType
		if(params.userTypeName){
			userType = UserType.findByTypeName(params.userTypeName)
		}
		
		//所属机构
		def company
		if(params.companyId){
			company = Company.get(params.companyId)
		}
		
		//账号信息
		def user
		if(params.username || params.id){
			/*
			 *保存登录用户账号信息;如果账号已经存在则登录名不变
			 */
			def username
			user = new User()
			if(params.id && !"".equals(params.id)){
				user = User.get(params.id)
				username = user.username
			}else{
				user.enabled = true
				if(params.userNameFront){
					username = params.userNameFront + params.username
				}
			}
			user.properties = params
			user.username = username
			user.clearErrors()
			
			if(params.userTypeName && !params.userTypeName.equals(user.getUserTypeName())){
				if(userType){
					user.userTypeEntity = userType
				}
			}
			
			if(params.companyId){
				user.company = company
			}
			
			user.save()
		}
		
		//添加个人概况信息
		def personInfor = new PersonInfor()
		if(params.personInforId){
			personInfor = PersonInfor.get(params.personInforId)
		}
		personInfor.properties = params
		personInfor.user = user
		
		personInfor.clearErrors()
		
		personInfor.birthday = Util.convertToTimestamp(params.birthday)
		personInfor.intoday = Util.convertToTimestamp(params.intoday)
		personInfor.staffOnDay = Util.convertToTimestamp(params.staffOnDay)
		
		if(userType && !userType.equals(personInfor.userTypeEntity)){
			personInfor.userTypeEntity = userType
		}
		personInfor.company = company
		
		//部门信息
		def depart
		if(params.allowdepartsId){
			//首先清空原有的departs
			if(personInfor.departs && personInfor.departs.size()>0){
				personInfor.departs.clear()
			}
			
			depart = Depart.get(params.allowdepartsId)
			if(depart){
				personInfor.addToDeparts(depart)
			}
		}
		
		//判断是否需要走流程
		def _status
		if(params.relationFlow){
			//需要走流程
			if(params.personInforId){
				_status = "old"
			}else{
				_status = "new"
				personInfor.currentUser = currentUser
				personInfor.currentDepart = currentUser.getDepartName()
				personInfor.currentDealDate = new Date()
				
				personInfor.drafter = currentUser
				personInfor.drafterDepart = currentUser.getDepartName()
			}
			
			//增加读者域
			if(!personInfor.readers.find{ it.id.equals(currentUser.id) }){
				personInfor.addToReaders(currentUser)
			}
			
			//流程引擎相关信息处理-------------------------------------------------------------------------------------
			if(!personInfor.processInstanceId){
				//启动流程实例
				def _processInstance = workFlowService.getProcessDefinition(params.relationFlow)
				Map<String, Object> variables = new HashMap<String, Object>();
				ProcessInstance processInstance = workFlowService.addFlowInstance(_processInstance.key, currentUser.username,personInfor.id, variables);
				personInfor.processInstanceId = processInstance.getProcessInstanceId()
				personInfor.processDefinitionId = processInstance.getProcessDefinitionId()
				
				//获取下一节点任务
				def task = workFlowService.getTasksByFlow(processInstance.getProcessInstanceId())[0]
				personInfor.taskId = task.getId()
			}
			//-------------------------------------------------------------------------------------------------
		}
		
		if(personInfor.save(flush:true)){
			if(user){
				UserDepart.removeAll(user)
				if(params.allowdepartsId){
					if(depart){
						UserDepart.create(user, depart)
					}
				}
				
				UserRole.removeAll(user)
				if(params.allowrolesId){
					params.allowrolesId.split(",").each{
						def role = Role.get(it)
						UserRole.create(user, role)
					}
				}
			}
			
			//通讯方式----------------------
			def contactInfor = ContactInfor.findByPersonInfor(personInfor)
			if(!contactInfor){
				contactInfor = new ContactInfor()
			}
			//清除params中的id号
			params.remove("id")
			
			contactInfor.properties = params
			contactInfor.clearErrors()
			
			contactInfor.personInfor = personInfor
			contactInfor.save(flush:true)
			
			//家庭成员--------------------------------------------------------------
			FamilyInfor.findAllByPersonInfor(personInfor).each{
				it.delete()
			}
			
			JSON.parse(params.staffFamily).eachWithIndex{elem, i ->
				def familyInfor = new FamilyInfor(elem)
				familyInfor.personInfor = personInfor
				familyInfor.save(flush:true)
			}
			//--------------------------------------------------------------
			
			//流程引擎相关日志信息
			if("new".equals(_status)){
				//添加日志
				shareService.addFlowLog(personInfor.id,params.flowCode,currentUser,"新建入职人员信息")
			}
			
			model["id"] = personInfor.id
			model["result"] = "true"
		}else{
			personInfor.errors.each{
				println it
			}
			model["result"] = "false"
		}
		render model as JSON
	}
	def userAdd ={
		if(params.flowCode){
			//需要走流程
			def company = Company.get(params.companyId)
			def flowBusiness = FlowBusiness.findByFlowCodeAndCompany(params.flowCode,company)
			if(flowBusiness && !"".equals(flowBusiness.relationFlow)){
				params.relationFlow = flowBusiness.relationFlow
				redirect(action:"userShow",params:params)
			}else{
				//不存在流程引擎关联数据
				render '<h2 style="color:red;width:660px;margin:0 auto;margin-top:60px">当前业务不存在流程设置，无法创建，请联系管理员！</h2>'
			}
		}else{
			redirect(action:"userShow",params:params)
		}
	}
	def userShow ={
		/*
		 * 参数中的id号修改为个人概况的id号
		 */
		def model =[:]
		
		//当前登录用户
		def loginUser = User.get(params.userid)
		model["loginUser"]= loginUser
		
		//当前选中的部门
		model["departId"] = params.currentDepartId;
		
		//显示类型
		model["type"] = params.type
		
		//个人概况
		if(params.id){
			def personInfor = PersonInfor.get(params.id)
			
			if(!personInfor){
				render '<h2 style="color:red;width:500px;margin:0 auto">此文件已过期或已被删除，请联系管理员！</h2>'
				return
			}
			
			//登录名
			def registerUser = personInfor.user
			if(registerUser){
				//存在账号信息
				model["user"] = personInfor.user
				
				//具有角色
				def allowrolesName=[]
				def allowrolesId =[]
				UserRole.findAllByUser(registerUser).each{
					allowrolesName << it.role.authority
					allowrolesId << it.role.id
				}
				
				model["allowrolesName"] = allowrolesName.join(',')
				model["allowrolesId"] = allowrolesId.join(",")
				model["username"] = Util.strRight(registerUser.username, "-")
			}else{
				model["user"] = new User()
			}
			model["personInfor"] = personInfor
		}else{
			model["user"] = new User()
			model["personInfor"] = new PersonInfor()
		}
		if(params.companyId){
			def company = Company.get(params.companyId)
			model["company"] = company
		}
		
		model["userType"] = "normal"
		
		//流程相关信息----------------------------------------------
		model["relationFlow"] = params.relationFlow
		model["flowCode"] = params.flowCode
		//------------------------------------------------------
		
		FieldAcl fa = new FieldAcl()
		fa.readOnly +=["allowdepartsName","allowrolesName"]
		if(loginUser!=null){
			if(systemService.checkIsRosten(loginUser.username)){
				model["userType"] = "super"
			}else if(loginUser.sysFlag){
				model["userType"] = "admin"
			}else{
				model["userType"] = "normal"
			}
		}
		model["fieldAcl"] = fa
		render(view:'/staff/user',model:model)
	}
	
	def userGrid ={
		def company = Company.get(params.companyId)
		def departEntity = Depart.get(params.departId)
		def json=[:]
		if(params.refreshHeader){
			def _gridHeader =[]

			_gridHeader << ["name":"序号","width":"26px","colIdx":0,"field":"rowIndex"]
			_gridHeader << ["name":"登录名","width":"auto","colIdx":1,"field":"username"]
			_gridHeader << ["name":"姓名","width":"auto","colIdx":2,"field":"chinaName","formatter":"personInfor_formatTopic"]
			_gridHeader << ["name":"部门","width":"auto","colIdx":3,"field":"departName"]
			_gridHeader << ["name":"编制类别","width":"auto","colIdx":4,"field":"type"]
			_gridHeader << ["name":"性别","width":"auto","colIdx":5,"field":"sex"]
			_gridHeader << ["name":"出生年月","width":"auto","colIdx":6,"field":"birthday"]
			_gridHeader << ["name":"身份证号","width":"auto","colIdx":7,"field":"idCard"]
			_gridHeader << ["name":"手机号码","width":"auto","colIdx":8,"field":"mobile"]
			_gridHeader << ["name":"民族","width":"auto","colIdx":9,"field":"nationality"]
			_gridHeader << ["name":"政治面貌","width":"auto","colIdx":10,"field":"politicsStatus"]
			_gridHeader << ["name":"状态","width":"auto","colIdx":11,"field":"status"]

			json["gridHeader"] = _gridHeader
		}
		
		def searchArgs =[:]
		
		if(params.username && !"".equals(params.username)) searchArgs["username"] = params.username
		if(params.chinaName && !"".equals(params.chinaName)) searchArgs["chinaName"] = params.chinaName
		if(params.departName && !"".equals(params.departName)) searchArgs["departName"] = params.departName
		
		if(params.refreshData){
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)

			def offset = (nowPage-1) * perPageNum
			def max  = perPageNum

			def _json = [identifier:'id',label:'name',items:[]]
			
			//查询搜索主体为personInfor个人概况信息
			def c = PersonInfor.createCriteria()
			def pa=[max:max,offset:offset]
			def query = {
				
				searchArgs.each{k,v->
					if(k.equals("departName")){
						departs{
							like(k,"%" + v + "%")
						}
					}else if(k.equals("username")){
						createAlias('user', 'a')
						like("a.username","%" + v + "%")
						
					}else{
						like(k,"%" + v + "%")
					}
				}
				
				departs{
					eq("id",params.departId)
				}
				order("chinaName", "asc")
				
			}
			def userList = c.list(pa,query)
			
			def idx = 0
			userList.each{
				def _user = it.user
				def contactInfor = ContactInfor.findByPersonInfor(it)
				
				def sMap =[:]
				sMap["rowIndex"] = idx+1
				sMap["id"] = it?.id
				sMap["username"] = _user?_user.username:""
				sMap["userId"] = _user?_user.id:""
				sMap["chinaName"] = it.chinaName
				sMap["departName"] = departEntity.departName
				sMap["type"] = it?.getUserTypeName()
				sMap["sex"] = it?.sex
				sMap["birthday"] = it?.getFormatteBirthday()
				sMap["idCard"] = it?.idCard
				sMap["mobile"] = contactInfor?.mobile
				sMap["nationality"] = it?.nationality
				sMap["politicsStatus"] = it?.politicsStatus
				sMap["status"] = it?.status
				
				_json.items+=sMap
				
				idx += 1
			}

			json["gridData"] = _json
		}
		
		if(params.refreshPageControl){
			
			def c = PersonInfor.createCriteria()
			def query = {
				departs{
					eq("id",params.departId)
				}
				
				searchArgs.each{k,v->
					if(k.equals("departName")){
						departs{
							like(k,"%" + v + "%")
						}
					}else if(k.equals("username")){
						createAlias('user', 'a')
						like("a.username","%" + v + "%")
					}else{
						like(k,"%" + v + "%")
					}
				}
				
			}
			def totalNum = c.count(query)
			
			json["pageControl"] = ["total":totalNum.toString()]
		}
		render json as JSON
	}
	def staffGrid ={
		def company = Company.get(params.companyId)
		def json=[:]
		if(params.refreshHeader){
			def _gridHeader =[]

			_gridHeader << ["name":"序号","width":"26px","colIdx":0,"field":"rowIndex"]
			_gridHeader << ["name":"登录名","width":"auto","colIdx":1,"field":"username"]
			_gridHeader << ["name":"姓名","width":"auto","colIdx":2,"field":"chinaName","formatter":"personInfor_formatTopic_normal"]
			_gridHeader << ["name":"部门","width":"auto","colIdx":3,"field":"departName"]
			_gridHeader << ["name":"编制类别","width":"auto","colIdx":4,"field":"type"]
			_gridHeader << ["name":"性别","width":"auto","colIdx":5,"field":"sex"]
			_gridHeader << ["name":"出生年月","width":"auto","colIdx":6,"field":"birthday"]
			_gridHeader << ["name":"身份证号","width":"auto","colIdx":7,"field":"idCard"]
			_gridHeader << ["name":"手机号码","width":"auto","colIdx":8,"field":"mobile"]
			_gridHeader << ["name":"民族","width":"auto","colIdx":9,"field":"nationality"]
			_gridHeader << ["name":"政治面貌","width":"auto","colIdx":10,"field":"politicsStatus"]
			_gridHeader << ["name":"状态","width":"auto","colIdx":11,"field":"status"]

			json["gridHeader"] = _gridHeader
		}
		
		def searchArgs =[:]
		
		if(params.username && !"".equals(params.username)) searchArgs["username"] = params.username
		if(params.chinaName && !"".equals(params.chinaName)) searchArgs["chinaName"] = params.chinaName
		if(params.departName && !"".equals(params.departName)) searchArgs["departName"] = params.departName
		
		if(params.refreshData){
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)

			def offset = (nowPage-1) * perPageNum
			def max  = perPageNum

			def _json = [identifier:'id',label:'name',items:[]]
			
			def c = PersonInfor.createCriteria()
			def pa=[max:max,offset:offset]
			def query = {
				eq("company",company)
				
				searchArgs.each{k,v->
					if(k.equals("departName")){
						departs{
							like(k,"%" + v + "%")
						}
					}else if(k.equals("username")){
						createAlias('user', 'a')
						like("a.username","%" + v + "%")
						
					}else{
						like(k,"%" + v + "%")
					}
				}
				
				if("staffAdd".equals(params.type)){
					not {'in'("status",["在职","退休","离职"])}
					order("createDate", "desc")
				}else{
					'in'("status",["在职","退休","离职"])
					//createAlias('user', 'a')
					order("createDate", "desc")
				}
			}
			def personList = c.list(pa,query)
			
			def idx = 0
			personList.each{
				def _user = it.user
				def personInfor = it
				def contactInfor = ContactInfor.findByPersonInfor(personInfor)
				
				def sMap =[:]
				sMap["rowIndex"] = idx+1
				sMap["id"] = it.id
				sMap["username"] = _user?_user.username:""
				sMap["userId"] = _user?_user.id:""
				sMap["chinaName"] = personInfor?.chinaName
				sMap["departName"] = personInfor?.getUserDepartName()
				sMap["type"] = personInfor?.getUserTypeName()
				sMap["sex"] = personInfor?.sex
				sMap["birthday"] = personInfor?.getFormatteBirthday()
				sMap["idCard"] = personInfor?.idCard
				sMap["mobile"] = contactInfor?.mobile
				sMap["nationality"] = personInfor?.nationality
				sMap["politicsStatus"] = personInfor?.politicsStatus
				sMap["status"] = personInfor?.status
				
				_json.items+=sMap
				
				idx += 1
			}

			json["gridData"] = _json
		}
		
		if(params.refreshPageControl){
			
			def c = PersonInfor.createCriteria()
			def query = {
				eq("company",company)
				
				searchArgs.each{k,v->
					if(k.equals("departName")){
						departs{
							like(k,"%" + v + "%")
						}
					}else if(k.equals("username")){
						createAlias('user', 'a')
						like("a.username","%" + v + "%")
					}else{
						like(k,"%" + v + "%")
					}
				}
				
				if("staffAdd".equals(params.type)){
					not {'in'("status",["在职","退休","离职"])}
					order("chinaName", "asc")
				}else{
					'in'("status",["在职","退休","离职"])
					//createAlias('user', 'a')
					order("chinaName", "asc")
				}
			}
			def totalNum = c.count(query)
			json["pageControl"] = ["total":totalNum.toString()]
		}
		render json as JSON
	}
	def personInforView ={
		def model =[:]
		model["departId"] = params.id
		render(view:'/staff/personManageView',model:model)
	}
	
	def depart ={
		def model =[:]
		model["company"] = Company.get(params.companyId)
		render(view:'/staff/personManage',model:model)
	}
	def getBargain ={
		def model =[:]
		
		def entity = Bargain.get(params.id)
		if(!entity){
			entity = new Bargain()
		}
		
		model["bargain"] = entity
		
		//个人概况在已签发状态打开编辑功能
		FieldAcl fa = new FieldAcl()
		if("staffAdd".equals(params.type)){
			if(!personInfor || !"已签发".equals(personInfor.status)){
				fa.readOnly = ["bargainSerialNo","bargainType","startDate","endDate"]
			}
		}
		model["fieldAcl"] = fa
		
		render(view:'/staff/bargain',model:model)
	}
	def getPersonInfor ={
		def model =[:]
		def currentUser = springSecurityService.getCurrentUser()
		def company = currentUser.company
		
		//部门信息
		def depart
		def personInfor = PersonInfor.get(params.id)
		if(!personInfor){
			personInfor = new PersonInfor()
			if(params.departId){
				depart = Depart.get(params.departId)
			}
		}else{
			depart = personInfor.departs[0]
		}
		
		if(depart){
			model["departName"] = depart.departName
			model["departId"] = depart.id
		}
		
		model["company"] = company
		model["personInforEntity"] = personInfor
		
		//用户类型
		def userTypeList = UserType.findAllByCompany(company)
		model["userTypeList"] = userTypeList
		
		if(userTypeList && userTypeList.size()>0){
			model["userTypeEntity"] = userTypeList[0]
		}
		
		//血型
		model["bloodList"] = shareService.getSystemCodeItems(company,"rs_blood")
		
		//健康状况
		model["healthList"] = shareService.getSystemCodeItems(company,"rs_health")
		
		//政治面貌
		model["politicsStatusList"] = shareService.getSystemCodeItems(company,"rs_politicsStatus")
		
		//专业技术等级
		model["techGradeList"] = shareService.getSystemCodeItems(company,"rs_techGrade")
		
		FieldAcl fa = new FieldAcl()
//		if( user && "normal".equals(user.getUserType()) && !currentUser.equals(user)){
//			//非管理员并且不是本人时，不允许修改所有相关字段
//			fa.readOnly += ["chinaName"]
//		}
		model["fieldAcl"] = fa
		
		render(view:'/staff/personInfor',model:model)
	}
	def getContactInfor ={
		def model =[:]
		def entity
		
		def user = PersonInfor.get(params.id)
		if(user){
			entity = ContactInfor.findByPersonInfor(user)
		}else{
			entity = new ContactInfor()
		}
		
		model["contactInforEntity"] = entity
		FieldAcl fa = new FieldAcl()
		
		model["fieldAcl"] = fa
		render(view:'/staff/contactInfor',model:model)
	}
	def getDegree ={
		def model =[:]
		def entity
		
		def user = PersonInfor.get(params.id)
		if(user){
			entity = Degree.findByPersonInfor(user)
		}else{
			entity = new Degree()
		}
		model["degreeEntity"] = entity
		FieldAcl fa = new FieldAcl()
	
		model["fieldAcl"] = fa
		render(view:'/staff/degree',model:model)
	}
    def getWorkResume={
		def model =[:]
		def entity
		
		def user = PersonInfor.get(params.id)
		if(user){
			entity = WorkResume.findByPersonInfor(user)
		}else{
			entity = new WorkResume()
		}
		model["workResumeEntity"] = entity
		FieldAcl fa = new FieldAcl()
		model["fieldAcl"] = fa
		render(view:'/staff/workResume',model:model)
	}
	
	def getFamily ={
		def json=[:]
		
		def personInfor = PersonInfor.get(params.id)
		if(params.refreshHeader){
			json["gridHeader"] = staffService.getFamilyInforListLayout()
		}
		
		//搜索功能
		def searchArgs =[:]
		
		if(params.refreshData){
			if(!personInfor){
				json["gridData"] = ["identifier":"id","label":"name","items":[]]
			}else{
				def args =[:]
				int perPageNum = Util.str2int(params.perPageNum)
				int nowPage =  Util.str2int(params.showPageNum)
				
				args["offset"] = (nowPage-1) * perPageNum
				args["max"] = perPageNum
				args["personInfor"] = personInfor
				
				def gridData = staffService.getFamilyInforListDataStore(args,searchArgs)
				json["gridData"] = gridData
			}
		}
		if(params.refreshPageControl){
			if(!personInfor){
				json["pageControl"] = ["total":"0"]
			}else{
				def total = staffService.getFamilyInforCount(personInfor,searchArgs)
				json["pageControl"] = ["total":total.toString()]
			}
			
		}
		render json as JSON
	}
	
	def familyInforShow ={
		def model =[:]
		if(params.id){
			model["familyInfor"] = FamilyInfor.get(params.id)
		}else{
			model["familyInfor"] = new FamilyInfor()
		}
		render(view:'/staff/familyInfor',model:model)
	}
	
	def exportPerson={
		OutputStream os = response.outputStream
		
		response.setContentType('application/vnd.ms-excel')
		response.setHeader("Content-disposition", "attachment; filename=" + new String("张三.xls".getBytes("GB2312"), "ISO_8859_1"))
		
		def excel = new ExcelExport()
		excel.mbxz(os)
	}
	
	def printPerson={
		def word = new WordExport()
		word.downloadZip(response)
	}
	
}
