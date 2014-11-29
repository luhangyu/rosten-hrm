package com.rosten.app.share

import com.rosten.app.system.SystemCode
import com.rosten.app.system.User
import com.rosten.app.system.Model
import com.rosten.app.util.Util

import com.rosten.app.workflow.WorkFlowService
import com.rosten.app.system.SystemService

import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.task.Task
import org.activiti.engine.task.TaskQuery

class ShareService {
	def workFlowService
	def systemService
	def taskService
	
	def businessFlowBack ={entity ->
		def nextMap = [:]
		//获取上一处理任务
		def frontTaskList = workFlowService.findBackAvtivity(entity.taskId)
		if(frontTaskList && frontTaskList.size()>0){
			//简单的取最近的一个节点
			def activityEntity = frontTaskList[frontTaskList.size()-1]
			def activityId = activityEntity.getId();
			
			//流程跳转
			workFlowService.backProcess(entity.taskId, activityId, null)
			
			//获取下一节点任务，目前处理串行情况
			def nextStatus
			def tasks = workFlowService.getTasksByFlow(entity.processInstanceId)
			def task = tasks[0]
			if(task.getDescription() && !"".equals(task.getDescription())){
				nextStatus = task.getDescription()
			}else{
				nextStatus = task.getName()
			}
			entity.taskId = task.getId()
			
			//获取对应节点的处理人员以及相关状态
			def historyActivity = workFlowService.getHistrotyActivityByActivity(entity.taskId,activityId)
			def user = User.findByUsername(historyActivity.getAssignee())
			
			//任务指派给当前拟稿人
			taskService.claim(entity.taskId, user.username)
			
			//修改相关信息
			entity.currentUser = user
			entity.currentDepart = user.getDepartName()
			entity.currentDealDate = new Date()
			entity.status = nextStatus
			
			nextMap.nextUser = user
			nextMap.nextDepart = user.getDepartName()
			nextMap.nextStatus = nextStatus
		}
		
		return nextMap
	}
	
	def businessFlowDeal ={company,entity,dealObject,map,modelName,entityStr ->
		//entity:处理domain;entityStr:处理参数,dealObject:处理人员信息,modelName:模块名称，供授权查询使用,map:流程处理的条件参数
		//目前不支持并发功能
		def nextMap = [:]
		def nextStatus,nextUsers=[],nextDeparts=[]
		
		taskService.complete(entity.taskId,map)	//结束当前任务
		ProcessInstance processInstance = workFlowService.getProcessIntance(entity.processInstanceId)
		if(!processInstance || processInstance.isEnded()){
			//流程已结束
			nextStatus = "已结束"
			entity.currentUser = null
			entity.currentDepart = null
			entity.taskId = null
			entity.status = nextStatus
			entity.currentDealDate = new Date()
		}else{
			//获取下一节点任务，目前处理串行情况
			def tasks = workFlowService.getTasksByFlow(entity.processInstanceId)
			def task = tasks[0]
			if(task.getDescription() && !"".equals(task.getDescription())){
				nextStatus = task.getDescription()
			}else{
				nextStatus = task.getName()
			}
			entity.taskId = task.getId()
		
			if(dealObject){
				//下一步相关信息处理
				def dealUsers = dealObject.split(",")
				if(dealUsers.size() >1){
					//并发
				}else{
					//串行
					def nextUser = User.get(Util.strLeft(dealObject,":"))
					def nextDepart = Util.strRight(dealObject, ":")
					
					//判断是否有公务授权------------------------------------------------------------
					def _model = Model.findByModelCodeAndCompany(modelName,company)
					def authorize = systemService.checkIsAuthorizer(nextUser,_model,new Date())
					if(authorize){
						this.addFlowLog(entity.id,entityStr,nextUser,"委托授权给【" + authorize.beAuthorizerDepart + ":" + authorize.getFormattedAuthorizer() + "】")
						nextUser = authorize.beAuthorizer
						nextDepart = authorize.beAuthorizerDepart
					}
					//-------------------------------------------------------------------------
					
					//任务指派给当前拟稿人
					taskService.claim(entity.taskId, nextUser.username)
										
					entity.currentUser = nextUser
					entity.currentDepart = nextDepart
					
					if(!entity.readers.find{ item->
						item.id.equals(nextUser.id)
					}){
						entity.addToReaders(nextUser)
					}
					
					entity.status = nextStatus
					entity.currentDealDate = new Date()
					
					//添加到返回信息中
					nextUsers << nextUser
					nextDeparts << nextDepart
					
				}
			}
		}
		nextMap.nextUser = nextUsers
		nextMap.nextDepart = nextDeparts
		nextMap.nextStatus = nextStatus
		return nextMap
	}
	def businessFlowSave ={entity,currentUser,relationFlow ->
		//entity:需要处理的domain,dealUser:处理人员
		if(!entity.id){
			//尚未保存
			entity.currentUser = currentUser
			entity.currentDepart = currentUser.getDepartName()
			entity.currentDealDate = new Date()
			
			entity.drafter = currentUser
			entity.drafterDepart = currentUser.getDepartName()
		}
		
		//增加读者域
		if(!entity.readers.find{ it.id.equals(currentUser.id) }){
			entity.addToReaders(currentUser)
		}
		
		//流程引擎相关信息处理-------------------------------------------------------------------------------------
		if(!entity.processInstanceId){
			//启动流程实例
			def _processInstance = workFlowService.getProcessDefinition(relationFlow)
			Map<String, Object> variables = new HashMap<String, Object>();
			ProcessInstance processInstance = workFlowService.addFlowInstance(_processInstance.key, currentUser.username,entity.id, variables);
			entity.processInstanceId = processInstance.getProcessInstanceId()
			entity.processDefinitionId = processInstance.getProcessDefinitionId()
			
			//获取下一节点任务
			def task = workFlowService.getTasksByFlow(processInstance.getProcessInstanceId())[0]
			entity.taskId = task.getId()
		}
		//--------------------------------------------------------------------------------------------------
	}
	
	def addFlowLog ={ belongToId,flowCode,currentUser,content ->
		//添加日志
		def _log = new FlowLog()
		_log.user = currentUser
		_log.belongToId = belongToId
		_log.belongToObject = flowCode
		_log.content = content
		_log.company = currentUser.company
		_log.save(flush:true)
	}
	def getSystemCodeItems={company,code ->
		def systemCode = SystemCode.findByCompanyAndCode(company,code)
		if(systemCode){
			return systemCode.items
		}else{
			return []
		}
	}
	
  def getAllDepartByChild ={departList,depart->
		departList << depart
		if(depart.children){
			depart.children.each{
				return getAllDepartByChild(departList,it)
			}
		}else{
			return departList
		}
	}
  
  
}
