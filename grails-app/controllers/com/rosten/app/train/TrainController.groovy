package com.rosten.app.train

import grails.converters.JSON

import com.rosten.app.util.FieldAcl
import com.rosten.app.util.Util
import com.rosten.app.system.Company
import com.rosten.app.system.User

import com.rosten.app.workflow.FlowBusiness
import com.rosten.app.system.Authorize
import com.rosten.app.gtask.Gtask
import org.activiti.engine.runtime.ProcessInstance
import org.activiti.engine.runtime.ProcessInstanceQuery
import org.activiti.engine.task.Task
import org.activiti.engine.task.TaskQuery
import com.rosten.app.system.Model
import com.rosten.app.staff.PersonInfor

import com.rosten.app.start.StartService
import com.rosten.app.workflow.WorkFlowService
import com.rosten.app.share.ShareService
import com.rosten.app.system.SystemService

class TrainController {
	def trainService
	def springSecurityService
	def workFlowService
	def taskService
	def startService
	def shareService
	def systemService
	
	def  staffItemShow ={
		def model =[:]
		if(params.id){
			model["trainMessage"] = TrainMessage.get(params.id)
		}else{
			model["trainMessage"] = new TrainMessage()
		}
		render(view:'/train/staffItemShow',model:model)
	}
	def staffListGrid ={
		def json=[:]
		
		def trainCourse = TrainCourse.get(params.id)
		if(params.refreshHeader){
			json["gridHeader"] = trainService.getStaffListLayout()
		}
		
		//2014-9-1 增加搜索功能
		def searchArgs =[:]
		
		if(params.refreshData){
			if(!trainCourse){
				json["gridData"] = ["identifier":"id","label":"name","items":[]]
			}else{
				def args =[:]
				int perPageNum = Util.str2int(params.perPageNum)
				int nowPage =  Util.str2int(params.showPageNum)
				
				args["offset"] = (nowPage-1) * perPageNum
				args["max"] = perPageNum
				args["trainCourse"] = trainCourse
				
				def gridData = trainService.getStaffItemListDataStore(args,searchArgs)
				json["gridData"] = gridData
			}
		}
		if(params.refreshPageControl){
			if(!trainCourse){
				json["pageControl"] = ["total":"0"]
			}else{
				def total = trainService.getStaffItemCount(trainCourse,searchArgs)
				json["pageControl"] = ["total":total.toString()]
			}
			
		}
		render json as JSON
	}
	
	//培训班查询条件
	def trainCourseSearchView ={
		def model =[:]
		render(view:'/train/trainCourseSearch',model:model)
	}
	
	//出国进修查询条件
	def forgeinStudySearchView ={
		def model =[:]
		render(view:'/train/forgeinStudySearch',model:model)
	}
	
	def trainCourseFlowBack ={
		def json=[:]
		def trainCourse = TrainCourse.get(params.id)
		def currentUser = springSecurityService.getCurrentUser()
		def frontStatus = trainCourse.status
		
		try{
			//获取上一处理任务
			def frontTaskList = workFlowService.findBackAvtivity(trainCourse.taskId)
			if(frontTaskList && frontTaskList.size()>0){
				//简单的取最近的一个节点
				def activityEntity = frontTaskList[frontTaskList.size()-1]
				def activityId = activityEntity.getId();
				
				//流程跳转
				workFlowService.backProcess(trainCourse.taskId, activityId, null)
				
				//获取下一节点任务，目前处理串行情况
				def nextStatus
				def tasks = workFlowService.getTasksByFlow(trainCourse.processInstanceId)
				def task = tasks[0]
				if(task.getDescription() && !"".equals(task.getDescription())){
					nextStatus = task.getDescription()
				}else{
					nextStatus = task.getName()
				}
				trainCourse.taskId = task.getId()
				
				//获取对应节点的处理人员以及相关状态
				def historyActivity = workFlowService.getHistrotyActivityByActivity(trainCourse.taskId,activityId)
				def user = User.findByUsername(historyActivity.getAssignee())
				
				//任务指派给当前拟稿人
				taskService.claim(trainCourse.taskId, user.username)
				
				//增加待办事项
				def args = [:]
				args["type"] = "【培训管理】"
				args["content"] = "培训班名称为  【" + trainCourse.courseName +  "】 的申请被退回，请查看！"
				args["contentStatus"] = nextStatus
				args["contentId"] = trainCourse.id
				args["user"] = user
				args["company"] = user.company
				
				startService.addGtask(args)
					
				//修改信息
				trainCourse.currentUser = user
				trainCourse.currentDepart = user.getDepartName()
				trainCourse.currentDealDate = new Date()
				trainCourse.status = nextStatus
				
				//判断下一处理人是否与当前处理人员为同一人
				if(currentUser.equals(trainCourse.currentUser)){
					json["refresh"] = true
				}
				
				//----------------------------------------------------------------------------------------------------
				
				//修改代办事项状态
				def gtask = Gtask.findWhere(
					user:currentUser,
					company:currentUser.company,
					contentId:trainCourse.id,
					contentStatus:frontStatus,
					status:"0"
				)
				if(gtask!=null){
					gtask.dealDate = new Date()
					gtask.status = "1"
					gtask.save(flush:true)
				}
				
				trainCourse.save(flush:true)
				
				//添加日志
				def logContent = "退回【" + user.getFormattedName() + "】"
				shareService.addFlowLog(trainCourse.id,"trainCourse",currentUser,logContent)
			}
				
			json["result"] = true
		}catch(Exception e){
			json["result"] = false
		}
		render json as JSON
	}
	def trainCourseFlowDeal = {
		def json=[:]
		def trainCourse = TrainCourse.get(params.id)
		
		//处理当前人的待办事项
		def currentUser = springSecurityService.getCurrentUser()
		def frontStatus = trainCourse.status
		def nextStatus,nextDepart,nextLogContent
		def nextUsers=[]
		
		//流程引擎相关信息处理-------------------------------------------------------------------------------------
		
		//结束当前任务，并开启下一节点任务
		def map =[:]
		if(params.conditionName){
			map[params.conditionName] = params.conditionValue
		}
		taskService.complete(trainCourse.taskId,map)	//结束当前任务
		
		ProcessInstance processInstance = workFlowService.getProcessIntance(trainCourse.processInstanceId)
		if(!processInstance || processInstance.isEnded()){
			//流程已结束
			nextStatus = "已结束"
			trainCourse.currentUser = null
			trainCourse.currentDepart = null
			trainCourse.taskId = null
		}else{
			//获取下一节点任务，目前处理串行情况
			def tasks = workFlowService.getTasksByFlow(trainCourse.processInstanceId)
			def task = tasks[0]
			if(task.getDescription() && !"".equals(task.getDescription())){
				nextStatus = task.getDescription()
			}else{
				nextStatus = task.getName()
			}
			trainCourse.taskId = task.getId()
		
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
					def _model = Model.findByModelCodeAndCompany("trainCourse",trainCourse.company)
					def authorize = systemService.checkIsAuthorizer(nextUser,_model,new Date())
					if(authorize){
						shareService.addFlowLog(trainCourse.id,"trainCourse",nextUser,"委托授权给【" + authorize.beAuthorizerDepart + ":" + authorize.getFormattedAuthorizer() + "】")
						nextUser = authorize.beAuthorizer
						nextDepart = authorize.beAuthorizerDepart
					}
					//-------------------------------------------------------------------------
					
					//任务指派给当前拟稿人
					taskService.claim(trainCourse.taskId, nextUser.username)
					
					def args = [:]
					args["type"] = "【培训管理】"
					args["content"] = "请您审核名称为  【" + trainCourse.courseName +  "】 的培训申请"
					args["contentStatus"] = nextStatus
					args["contentId"] = trainCourse.id
					args["user"] = nextUser
					args["company"] = nextUser.company
					
					startService.addGtask(args)
					
					trainCourse.currentUser = nextUser
					trainCourse.currentDepart = nextDepart
					
					if(!trainCourse.readers.find{ item->
						item.id.equals(nextUser.id)
					}){
						trainCourse.addToReaders(nextUser)
					}
					nextUsers << nextUser.getFormattedName()
				}
			}
		}
		trainCourse.status = nextStatus
		trainCourse.currentDealDate = new Date()
		
		//判断下一处理人是否与当前处理人员为同一人
		if(currentUser.equals(trainCourse.currentUser)){
			json["refresh"] = true
		}
		
		//----------------------------------------------------------------------------------------------------
		
		//修改代办事项状态
		def gtask = Gtask.findWhere(
			user:currentUser,
			company:currentUser.company,
			contentId:trainCourse.id,
			contentStatus:frontStatus
		)
		if(gtask!=null){
			gtask.dealDate = new Date()
			gtask.status = "1"
			gtask.save()
		}
		
		if(trainCourse.save(flush:true)){
			//添加日志
			def logContent
			switch (true){
				case trainCourse.status.contains("已签发"):
					logContent = "签发文件【" + nextUsers.join("、") + "】"
					
					break
				case trainCourse.status.contains("归档"):
					logContent = "归档"
					break
				case trainCourse.status.contains("不同意"):
					logContent = "不同意！"
					break
				default:
					logContent = "提交" + trainCourse.status + "【" + nextUsers.join("、") + "】"
					break
			}
			shareService.addFlowLog(trainCourse.id,"trainCourse",currentUser,logContent)
						
			json["result"] = true
		}else{
			trainCourse.errors.each{
				println it
			}
			json["result"] = false
		}
		render json as JSON
	}
	
	def trainCourseDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def trainCourse = TrainCourse.get(it)
				if(trainCourse){
					trainCourse.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	def trainCourseSave ={
		def json=[:]
		def trainCourse = new TrainCourse()
		def currentUser = springSecurityService.getCurrentUser()
		def _status = "new"
		
		if(params.id && !"".equals(params.id)){
			trainCourse = TrainCourse.get(params.id)
			_status = "old"
		}else{
			if(params.companyId){
				trainCourse.company = Company.get(params.companyId)
			}
			trainCourse.drafter = currentUser
			trainCourse.currentUser = currentUser
			trainCourse.currentDepart = currentUser.getDepartName()
			trainCourse.currentDealDate = new Date()
		}
		trainCourse.properties = params
		trainCourse.clearErrors()
		
		if(params.trainCert.equals("是")) {
			trainCourse.trainCert = true
		}else{
			trainCourse.trainCert = false
		}	
		trainCourse.trainDate = Util.convertToTimestamp(params.trainDate)
		
		//增加读者域信息
		if(!trainCourse.readers.find{ it.id.equals(currentUser.id) }){
			trainCourse.addToReaders(currentUser)
		}
		
		//流程引擎相关信息处理-------------------------------------------------------------------------------------
		if(!trainCourse.processInstanceId){
			//启动流程实例
			def _processInstance = workFlowService.getProcessDefinition(params.relationFlow)
			Map<String, Object> variables = new HashMap<String, Object>();
			ProcessInstance processInstance = workFlowService.addFlowInstance(_processInstance.key, currentUser.username,trainCourse.id, variables);
			trainCourse.processInstanceId = processInstance.getProcessInstanceId()
			trainCourse.processDefinitionId = processInstance.getProcessDefinitionId()
			
			//获取下一节点任务
			def task = workFlowService.getTasksByFlow(processInstance.getProcessInstanceId())[0]
			trainCourse.taskId = task.getId()
		}
		//-------------------------------------------------------------------------------------------------
		
		//培训成员--------------------------------------------------------------
		if(trainCourse.items){
			def _list = []
			_list += trainCourse.items
			trainCourse.items.clear()
			_list.each{
				it.delete()
			}
		}
		
		JSON.parse(params.trainMessage).eachWithIndex{elem, i ->
			def trainMessage = new TrainMessage(elem)
			trainMessage.personInfor = PersonInfor.get(elem.personInforId)
			trainCourse.addToItems(trainMessage)
		}
		//--------------------------------------------------------------------
		
		if(trainCourse.save(flush:true)){
			json["result"] = "true"
			json["id"] = trainCourse.id
			json["companyId"]=trainCourse.company.id
			
			if("new".equals(_status)){
				//添加日志
				shareService.addFlowLog(trainCourse.id,params.flowCode,currentUser,"新建培训班")
			}
		}else{
			trainCourse.errors.each{
				println it
			}
			json["result"] = "false"
		}
		render json as JSON
	}
	def trainCourseAdd ={
		if(params.flowCode){
			//需要走流程
			def company = Company.get(params.companyId)
			def flowBusiness = FlowBusiness.findByFlowCodeAndCompany(params.flowCode,company)
			if(flowBusiness && !"".equals(flowBusiness.relationFlow)){
				params.relationFlow = flowBusiness.relationFlow
				redirect(action:"trainCourseShow",params:params)
			}else{
				//不存在流程引擎关联数据
				render '<h2 style="color:red;width:660px;margin:0 auto;margin-top:60px">当前业务不存在流程设置，无法创建，请联系管理员！</h2>'
			}
		}else{
			redirect(action:"trainCourseShow",params:params)
		}
	}
	def trainCourseShow ={
		def model =[:]
		
		def user = User.get(params.userid)
		def company = Company.get(params.companyId)
		def trainCourse = new TrainCourse()
		if(params.id){
			trainCourse = TrainCourse.get(params.id)
		}
		model["user"]=user
		model["company"] = company
		model["trainCourse"] = trainCourse
		
		FieldAcl fa = new FieldAcl()
		if("normal".equals(user.getUserType())){
			//普通用户
//			fa.readOnly += ["description"]
		}
		model["fieldAcl"] = fa
		
		//流程相关信息----------------------------------------------
		model["relationFlow"] = params.relationFlow
		model["flowCode"] = params.flowCode
		//------------------------------------------------------
		
		render(view:'/train/trainCourse',model:model)
	}
	def trainCourseGrid ={
		def json=[:]
		def company = Company.get(params.companyId)
		if(params.refreshHeader){
			json["gridHeader"] = trainService.getTrainCourseListLayout()
		}
		
		//增加查询条件
		def searchArgs =[:]
		
		if(params.courseName && !"".equals(params.courseName)) searchArgs["courseName"] = params.courseName
		
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			json["gridData"] = trainService.getTrainCourseListDataStore(args,searchArgs)
			
		}
		if(params.refreshPageControl){
			def total = trainService.getTrainCourseCount(company,searchArgs)
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
	}
	
	/**
	 * 员工培训信息
	 */
	def trainMessageGrid ={
		def json=[:]
		def company = Company.get(params.companyId)
		if(params.refreshHeader){
			json["gridHeader"] = trainService.getTrainMessageListLayout()
		}
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			json["gridData"] = trainService.getTrainMessageDataStore(args)
			
		}
		if(params.refreshPageControl){
			def total = trainService.getTrainMessageCount(company)
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
		
	}
	
	/**
	 * 培训信息增加
	 */
	def trainMessageAdd ={
		redirect(action:"trainMessageShow",params:params)
	}
	
	def trainMessageShow ={
		def model =[:]
		def currentUser = springSecurityService.getCurrentUser()
		
		def user = User.get(params.userid)
		def company = Company.get(params.companyId)
		def trainMessage = new TrainMessage()
		if(params.id){
			trainMessage = TrainMessage.get(params.id)
		}else{
			trainMessage.user = currentUser
		}
		
		model["company"] = company
		model["trainMessage"] = trainMessage
		
		FieldAcl fa = new FieldAcl()
		if("normal".equals(user.getUserType())){
			//普通用户
//			fa.readOnly += ["description"]
		}
//		fa.readOnly += ["trainAddress"]
		model["fieldAcl"] = fa
		
		render(view:'/train/trainMessage',model:model)
	}
	
	/**
	 * 培训班选择
	 */
	def courseSelect ={
		def courseList =[]
		def company = Company.get(params.companyId)
		TrainCourse.findAllByCompany(company).each{
			def json=[:]
			json["id"] = it.id
			json["name"] = it.courseName
			courseList << json
		}
		render courseList as JSON
	}
	
	/**
	 * 培训信息保存
	 */
	def trainMessageSave ={
		def json=[:]
		def trainMessage = new TrainMessage()
		if(params.id && !"".equals(params.id)){
			trainMessage = TrainMessage.get(params.id)
		}else{
			if(params.companyId){
				trainMessage.company = Company.get(params.companyId)
			}
		}
		trainMessage.properties = params
		trainMessage.clearErrors()
		
		trainMessage.trainCourse = TrainCourse.get(params.courseId)
		trainMessage.user = User.get(params.userId)
		
		if(trainMessage.save(flush:true)){
			json["result"] = "true"
		}else{
			trainMessage.errors.each{
				println it
			}
			json["result"] = "false"
		}
		render json as JSON
	}
	
	/**
	 * 删除
	 */
	def trainMessageDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def trainMessage = TrainMessage.get(it)
				if(trainMessage){
					trainMessage.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	
	/**
	 * 学历学位进修信息
	 */
	def degreeStudyGrid ={
		def json=[:]
		def company = Company.get(params.companyId)
		if(params.refreshHeader){
			json["gridHeader"] = trainService.getDegreeStudyListLayout()
		}
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			json["gridData"] = trainService.getDegreeStudyDataStore(args)
			
		}
		if(params.refreshPageControl){
			def total = trainService.getDegreeStudyCount(company)
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
		
	}
	
	/**
	 * 增加
	 */
	def degreeStudyAdd ={
		redirect(action:"degreeStudyShow",params:params)
	}
	
	def degreeStudyShow ={
		def model =[:]
		def currentUser = springSecurityService.getCurrentUser()
		def user = User.get(params.userid)
		def company = Company.get(params.companyId)
		def degreeStudy = new DegreeStudy()
		if(params.id){
			degreeStudy = DegreeStudy.get(params.id)
		}else{
			degreeStudy.user = currentUser
		}
		
		model["company"] = company
		model["degreeStudy"] = degreeStudy
		
		FieldAcl fa = new FieldAcl()
		if("normal".equals(user.getUserType())){

		}
		model["fieldAcl"] = fa
		
		render(view:'/train/degreeStudy',model:model)
	}
	
	/**
	 * 保存
	 */
	def degreeStudySave ={
		def json=[:]
		def degreeStudy = new DegreeStudy()
		def currentUser = springSecurityService.getCurrentUser()
		if(params.id && !"".equals(params.id)){
			degreeStudy = DegreeStudy.get(params.id)
		}else{
			if(params.companyId){
				degreeStudy.company = Company.get(params.companyId)
			}
			degreeStudy.user = currentUser
			
			degreeStudy.startDate = Util.convertToTimestamp(params.startDate)
			degreeStudy.endDate = Util.convertToTimestamp(params.endDate)
		}
		degreeStudy.properties = params
		degreeStudy.clearErrors()
		
		if(degreeStudy.save(flush:true)){
			json["result"] = "true"
		}else{
			degreeStudy.errors.each{
				println it
			}
			json["result"] = "false"
		}
		render json as JSON
	}
	
	/**
	 * 删除
	 */
	def degreeStudyDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def degreeStudy = DegreeStudy.get(it)
				if(degreeStudy){
					degreeStudy.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	
	
	/**
	 * 出国进修信息
	 */
	def forgeinStudyGrid ={
		def json=[:]
		def company = Company.get(params.companyId)
		if(params.refreshHeader){
			json["gridHeader"] = trainService.getForgeinStudyListLayout()
		}
		
		//增加查询条件
		def searchArgs =[:]
		
		if(params.appYear && !"".equals(params.appYear)) searchArgs["appYear"] = params.appYear
		
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			json["gridData"] = trainService.getForgeinStudyDataStore(args,searchArgs)
			
		}
		if(params.refreshPageControl){
			def total = trainService.getForgeinStudyCount(company,searchArgs)
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
		
	}
	
	/**
	 * 增加
	 */
	def forgeinStudyAdd ={
		redirect(action:"forgeinStudyShow",params:params)
	}
	
	def forgeinStudyShow ={
		def model =[:]
		def currentUser = springSecurityService.getCurrentUser()
		def user = User.get(params.userid)
		def company = Company.get(params.companyId)
		def forgeinStudy = new ForgeinStudy()
		if(params.id){
			forgeinStudy = ForgeinStudy.get(params.id)
		}else{
			forgeinStudy.user = currentUser
		}
		
		model["company"] = company
		model["forgeinStudy"] = forgeinStudy
		
		FieldAcl fa = new FieldAcl()
		if("normal".equals(user.getUserType())){

		}
		model["fieldAcl"] = fa
		
		render(view:'/train/forgeinStudy',model:model)
	}
	
	/**
	 * 保存
	 */
	def forgeinStudySave ={
		def json=[:]
		def forgeinStudy = new ForgeinStudy()
		def currentUser = springSecurityService.getCurrentUser()
		if(params.id && !"".equals(params.id)){
			forgeinStudy = ForgeinStudy.get(params.id)
		}else{
			if(params.companyId){
				forgeinStudy.company = Company.get(params.companyId)
			}
			forgeinStudy.user = currentUser
			
			forgeinStudy.abroadDate = Util.convertToTimestamp(params.abroadDate)
			forgeinStudy.returneDate = Util.convertToTimestamp(params.returneDate)
		}
		forgeinStudy.properties = params
		forgeinStudy.clearErrors()
		
		if(forgeinStudy.save(flush:true)){
			json["result"] = "true"
		}else{
			forgeinStudy.errors.each{
				println it
			}
			json["result"] = "false"
		}
		render json as JSON
	}
	
	/**
	 * 删除
	 */
	def forgeinStudyDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def forgeinStudy = ForgeinStudy.get(it)
				if(forgeinStudy){
					forgeinStudy.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
    
}
