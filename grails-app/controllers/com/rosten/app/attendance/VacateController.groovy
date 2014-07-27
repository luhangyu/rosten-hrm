package com.rosten.app.attendance

import grails.converters.JSON

import com.rosten.app.util.FieldAcl
import com.rosten.app.util.Util
import com.rosten.app.system.Company
import com.rosten.app.system.User
import com.rosten.app.system.Model

import com.rosten.app.workflow.WorkFlowService
import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.runtime.ProcessInstanceQuery
import org.activiti.engine.task.Task
import org.activiti.engine.task.TaskQuery

class VacateController {
	def vacateService
	def springSecurityService
	
	def workFlowService
	def taskService
	
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
		
		
	def vacateAdd ={
		redirect(action:"vacateShow",params:params)
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
}
