package com.rosten.app.attendance

import grails.converters.JSON
import groovy.sql.Sql

import com.rosten.app.util.FieldAcl
import com.rosten.app.util.Util
import com.rosten.app.system.Company
import com.rosten.app.system.Depart;
import com.rosten.app.system.User
import com.rosten.app.system.Model
import com.rosten.app.system.Authorize
import com.rosten.app.gtask.Gtask
import com.rosten.app.system.SystemService
import com.rosten.app.start.StartService
import com.rosten.app.workflow.WorkFlowService
import com.rosten.app.share.ShareService
import com.rosten.app.workflow.FlowBusiness

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
	def shareService
	
	def vacateFlowBack ={
		def json=[:]
		def vacate = Vacate.get(params.id)
		def currentUser = springSecurityService.getCurrentUser()
		def frontStatus = vacate.status
		
		try{
			//获取上一处理任务
			def frontTaskList = workFlowService.findBackAvtivity(vacate.taskId)
			if(frontTaskList && frontTaskList.size()>0){
				//简单的取最近的一个节点
				def activityEntity = frontTaskList[frontTaskList.size()-1]
				def activityId = activityEntity.getId();
				
				//流程跳转
				workFlowService.backProcess(vacate.taskId, activityId, null)
				
				//获取下一节点任务，目前处理串行情况
				def nextStatus
				def tasks = workFlowService.getTasksByFlow(vacate.processInstanceId)
				def task = tasks[0]
				if(task.getDescription() && !"".equals(task.getDescription())){
					nextStatus = task.getDescription()
				}else{
					nextStatus = task.getName()
				}
				vacate.taskId = task.getId()
				
				//获取对应节点的处理人员以及相关状态
				def historyActivity = workFlowService.getHistrotyActivityByActivity(vacate.taskId,activityId)
				def user = User.findByUsername(historyActivity.getAssignee())
				
				//任务指派给当前拟稿人
				taskService.claim(vacate.taskId, user.username)
				
				//增加待办事项
				def args = [:]
				args["type"] = "【请假】"
				args["content"] = "申请人为  【" + vacate.getFormattedDrafter() +  "】 的请假申请被退回，请查看！"
				args["contentStatus"] = nextStatus
				args["contentId"] = vacate.id
				args["user"] = user
				args["company"] = user.company
				
				startService.addGtask(args)
					
				//修改信息
				vacate.currentUser = user
				vacate.currentDepart = user.getDepartName()
				vacate.currentDealDate = new Date()
				vacate.status = nextStatus
				
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
					contentStatus:frontStatus,
					status:"0"
				)
				if(gtask!=null){
					gtask.dealDate = new Date()
					gtask.status = "1"
					gtask.save(flush:true)
				}
				
				vacate.save(flush:true)
				
				//添加日志
				def logContent = "退回【" + user.getFormattedName() + "】"
				shareService.addFlowLog(vacate.id,"vacate",currentUser,logContent)
				
				json["nextUserName"] = user?.getFormattedName()
				json["result"] = true
			}
		}catch(Exception e){
			json["result"] = false
		}
		render json as JSON
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
		def map =[:]
		if(params.conditionName){
			map[params.conditionName] = params.conditionValue
		}
		taskService.complete(vacate.taskId,map)	//结束当前任务
		
		ProcessInstance processInstance = workFlowService.getProcessIntance(vacate.processInstanceId)
		if(!processInstance || processInstance.isEnded()){
			//流程已结束
			nextStatus = "已结束"
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
						shareService.addFlowLog(vacate.id,"bbs",nextUser,"委托授权给【" + authorize.beAuthorizerDepart + ":" + authorize.getFormattedAuthorizer() + "】")
						nextUser = authorize.beAuthorizer
						nextDepart = authorize.beAuthorizerDepart
					}
					//-------------------------------------------------------------------------
					
					//任务指派给当前拟稿人
					taskService.claim(vacate.taskId, nextUser.username)
					
					def args = [:]
					args["type"] = "【请假】"
					
					if(nextStatus.equals("已签发")){
						args["content"] = "领导已审批，请您查看名称为  【" + vacate.getFormattedUser() +  "】 的请假申请"
					}else{
						args["content"] = "请您审核名称为  【" + vacate.getFormattedUser() +  "】 的请假申请"
					}
					args["contentStatus"] = nextStatus
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
				case vacate.status.contains("已结束"):
					logContent = "结束流程"
					break
				case vacate.status.contains("已签发"):
					logContent = "审批通过【" + nextUsers.join("、") + "】"
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
			shareService.addFlowLog(vacate.id,"vacate",currentUser,logContent)
			
			json["nextUserName"] = nextUsers.join("、")
			json["result"] = true
		}else{
			vacate.errors.each{
				println it
			}
			json["result"] = false
		}
		render json as JSON
	}
    def vacateGrid ={
		def json=[:]
		def user = User.get(params.userId)
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
			args["user"] = user
			
			json["gridData"] = vacateService.getVacateDataStore(args)
			
		}
		if(params.refreshPageControl){
			def total = vacateService.getVacateCount(company,user)
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
		if(params.flowCode){
			//需要走流程
			def company = Company.get(params.companyId)
			def flowBusiness = FlowBusiness.findByFlowCodeAndCompany(params.flowCode,company)
			if(flowBusiness && !"".equals(flowBusiness.relationFlow)){
				params.relationFlow = flowBusiness.relationFlow
				redirect(action:"vacateShow",params:params)
			}else{
				//不存在流程引擎关联数据
				render '<h2 style="color:red;width:660px;margin:0 auto;margin-top:60px">当前业务不存在流程设置，无法创建，请联系管理员！</h2>'
			}
		}else{
			redirect(action:"bbsShow",params:params)
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
		if(!"新增".equals(vacate.status)){
			//普通用户
			fa.readOnly += ["startDate","endDate","vacateType","numbers","remark"]
		}
		model["fieldAcl"] = fa
		
		//流程相关信息----------------------------------------------
		model["relationFlow"] = params.relationFlow
		model["flowCode"] = params.flowCode
		//------------------------------------------------------
		
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
			def _processInstance = workFlowService.getProcessDefinition(params.relationFlow)
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
				shareService.addFlowLog(vacate.id,params.flowCode,currentUser,"新建请假申请")
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
		model.companyId = params.companyId
		model["departId"] = params.departId
		
		FieldAcl fa = new FieldAcl()
		if("normal".equals(user.getUserType())){
		}
		model["fieldAcl"] = fa
		
		def items = []
		Sql sql = new Sql(dataSource)
		def seleSql = " select a.*,b.china_name,c.depart_id,c.depart_name from ( select a.*,b.bjnums,c.nxjnums,d.hjnums,e.sajnums,f.qtjnums from ( select a.user_id,sum(a.numbers) sjnums from rosten_vacate a where a.vacate_type = '事假'  group by a.user_id) a left join  ";
		seleSql+=" ( select a.user_id,sum(a.numbers) bjnums from rosten_vacate a where a.vacate_type = '病假'  group by a.user_id) b on a.user_id = b.user_id left join "
		seleSql+=" ( select a.user_id,sum(a.numbers) nxjnums from rosten_vacate a where a.vacate_type = '年休假'  group by a.user_id) c on a.user_id = c.user_id left join  "
		seleSql+=" ( select a.user_id,sum(a.numbers) hjnums from rosten_vacate a where a.vacate_type = '婚假'  group by a.user_id) d on a.user_id = d.user_id left join  "
		seleSql+=" ( select a.user_id,sum(a.numbers) sajnums from rosten_vacate a where a.vacate_type = '丧假'  group by a.user_id) e on a.user_id = e.user_id left join "
		seleSql+=" ( select a.user_id,sum(a.numbers) qtjnums from rosten_vacate a where a.vacate_type = '其他'  group by a.user_id) f on a.user_id = f.user_id ) a left join rosten_user b on a.user_id = b.id left join  "
		seleSql+=" (select  a.* ,b.depart_name from rosten_user_depart  a  left join  rosten_depart  b on a.depart_id = b.id ) c on a.user_id = c.user_id "
		if(params.departId){
			def depart = Depart.get(params.departId)
			model["departName"] = depart.departName
			seleSql+=" where 1= 1"
			seleSql+=" and ("
			
			def searchDepart =[]
			shareService.getAllDepartByChild(searchDepart,depart)
			searchDepart.unique()
			for(def index=0;index<searchDepart.size();index++){
				if(index ==0){
					seleSql+=" depart_id ='"+searchDepart[index].id+"'"
				}else{
					seleSql+=" or depart_id ='"+searchDepart[index].id+"'"
				}
			}
			seleSql+=" )"
		}else{
			//默认取单位名称
			model["departName"] = user.company?.companyName
		}
		seleSql+=" union all ( "
		seleSql+="  select '' user_id, sum(sjnums),sum(bjnums),sum(nxjnums),sum(hjnums),sum(sajnums),sum(qtjnums),'合计' china_name,'' depart_id, '' depart_name from  "
		seleSql+=" ( select a.*,b.bjnums,c.nxjnums,d.hjnums,e.sajnums,f.qtjnums from ( select a.user_id,sum(a.numbers) sjnums from rosten_vacate a where a.vacate_type = '事假'  group by a.user_id) a  "
		seleSql+=" left join   ( select a.user_id,sum(a.numbers) bjnums from rosten_vacate a where a.vacate_type = '病假'  group by a.user_id) b on a.user_id = b.user_id left join  "
		seleSql+="  ( select a.user_id,sum(a.numbers) nxjnums from rosten_vacate a where a.vacate_type = '年休假'  group by a.user_id) c on a.user_id = c.user_id left join  "
		seleSql+="  ( select a.user_id,sum(a.numbers) hjnums from rosten_vacate a where a.vacate_type = '婚假'  group by a.user_id) d on a.user_id = d.user_id left join  "
		seleSql+=" ( select a.user_id,sum(a.numbers) sajnums from rosten_vacate a where a.vacate_type = '丧假'  group by a.user_id) e on a.user_id = e.user_id left join  "
		seleSql+=" ( select a.user_id,sum(a.numbers) qtjnums from rosten_vacate a where a.vacate_type = '其他'  group by a.user_id) f on a.user_id = f.user_id ) a "
		seleSql+=" left join rosten_user b on a.user_id = b.id left join   (select  a.* ,b.depart_name from rosten_user_depart  a  left join  rosten_depart  b on a.depart_id = b.id ) c on a.user_id = c.user_id  "
		
		if(params.departId){
			def depart = Depart.get(params.departId)
			model["departName"] = depart.departName
			model["titleName"] = depart.departName
				seleSql+=" where 1= 1"
			seleSql+=" and ("
			def searchDepart =[]
			shareService.getAllDepartByChild(searchDepart,depart)
			searchDepart.unique()
			for(def index=0;index<searchDepart.size();index++){
				if(index ==0){
					seleSql+=" depart_id ='"+searchDepart[index].id+"'"
				}else{
				seleSql+=" or depart_id ='"+searchDepart[index].id+"'"
				}
			}
			seleSql+=" )"
		}else{
		def company = Company.get(params.companyId)
		model["titleName"] = company.companyName
		}
		
		seleSql+=" ) "
		def vacateList = sql.eachRow(seleSql){
			def item = ["departName":it["depart_name"],"name":it["china_name"],
				"sjnums":it["sjnums"],"bjnums":it["bjnums"],"nxjnums":it["nxjnums"],
				"hjnums":it["hjnums"],"sajnums":it["sajnums"],"qtjnums":it["qtjnums"]]
			items<<item
		}
		
		model["tableItem"] = items
		
		def json = [identifier:'id',label:'name',items:[]]
		
		if(null!=items&&items.size()>0){
			def maps = items[items.size()-1];
			def sMap = ["id":001,"name":"事假","number":maps.sjnums]
			json.items+=sMap
			sMap = ["id":002,"name":"病假","number":maps.bjnums]
			json.items+=sMap
			sMap = ["id":003,"name":"年休假","number":maps.nxjnums]
			json.items+=sMap
			sMap = ["id":004,"name":"婚假","number":maps.hjnums]
			json.items+=sMap
			sMap = ["id":005,"name":"丧假","number":maps.sajnums]
			json.items+=sMap
			sMap = ["id":006,"name":"其他","number":maps.qtjnums]
			json.items+=sMap
			model["json"] = json as JSON
			
		}
		
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
	
	def allAskForGrid ={
		def json=[:]
		def user = User.get(params.userId)
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
			args["user"] = user
			
			json["gridData"] = vacateService.getAllAskForDataStore(args)
			
		}
		if(params.refreshPageControl){
			def total = vacateService.getAllVacateCount(company)
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
	}
	
}
