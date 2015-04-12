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
import com.rosten.app.export.ExcelExport
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

/*
 * 请假
 */
class VacateController {
	def springSecurityService
	def vacateService
	
	def systemService
	def workFlowService
	def taskService
	def startService
	def dataSource
	def shareService
	
	private def flowCode = "vacate" 	//普通员工请假，事假
	private def flowCodeDeptLeader = "vacate_deptLeader"	//部门领导请假，事假
	private def flowCodeLeader = "vacate_leader"	//分管领导请假，事假
	private def flowCodeNx	= "vacate_nx"	//普通员工年休假
	private def flowCodeNxLeader = "vacate_nx_leader"	//部门领导年休假
	
	//2015-3-16--------增加考勤记录功能--------------------------------------------------------
	def workCheckSearchView ={
		def model =[:]
		def currentUser = springSecurityService.getCurrentUser()
		def dataList = Depart.findAllByCompany(currentUser.company,[sort: "serialNo", order: "asc"])
		model["departList"] = dataList
		render(view:'/vacate/workCheckSearch',model:model)
	}
	
	def workCheckGrid ={
		def json=[:]
		def user = User.get(params.userId)
		def company = Company.get(params.companyId)
		if(params.refreshHeader){
			json["gridHeader"] = vacateService.getWorkCheckListLayout()
		}
		
		//增加查询条件
		def searchArgs =[:]
		
		if(params.chinaName && !"".equals(params.chinaName)) searchArgs["staffName"] = params.chinaName
		if(params.departName && !"".equals(params.departName)) searchArgs["staffDepart"] = params.departName
		if(params.date && !"".equals(params.date)) searchArgs["checkDate"] = params.date
		
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			
			json["gridData"] = vacateService.getWorkCheckDataStore(args,searchArgs)
			
		}
		if(params.refreshPageControl){
			def total = vacateService.getWorkCheckCount(company,searchArgs)
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
	}
	def workCheckDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def entity = WorkCheck.get(it)
				if(entity){
					entity.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	
	//2015-3-13-------------增加出勤解释单-----------------------------------------------------
	def vacateExplainAdd ={
		redirect(action:"vacateExplainShow",params:params)
	}
	def vacateExplainShow ={
		def model =[:]
		
		def user = User.get(params.userid)
		def company = Company.get(params.companyId)
		def entity = new VacateExplain()
		
		if(params.id){
			entity = VacateExplain.get(params.id)
		}
		model["user"]=user
		model["company"] = company
		model["vacateExplain"] = entity
		
		FieldAcl fa = new FieldAcl()
		model["fieldAcl"] = fa
		
		render(view:'/vacate/vacateExplain',model:model)
	}
	def vacateExplainSave ={
		def json=[:]
		def entity = new VacateExplain()
		def currentUser = springSecurityService.getCurrentUser()
		
		if(params.id && !"".equals(params.id)){
			entity = VacateExplain.get(params.id)
		}else{
			if(params.companyId){
				entity.company = Company.get(params.companyId)
			}
			
		}
		entity.properties = params
		entity.clearErrors()
		
		entity.explainDate = Util.convertToTimestamp(params.explainDate)
		
		if(entity.save(flush:true)){
			json["id"] = entity.id
			json["companyId"]=entity.company.id
			json["result"] = "true"
			
		}else{
			entity.errors.each{
				println it
			}
			json["result"] = "false"
		}
		render json as JSON
	}
	def vacateExplainDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def entity = VacateExplain.get(it)
				if(entity){
					entity.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	def vacateExplainSearchView ={
		def model =[:]
		def currentUser = springSecurityService.getCurrentUser()
		def dataList = Depart.findAllByCompany(currentUser.company,[sort: "serialNo", order: "asc"])
		model["departList"] = dataList
		
		def monthList = []
		monthList << [name:"1月份",code:1]
		monthList << [name:"2月份",code:2]
		monthList << [name:"3月份",code:3]
		monthList << [name:"4月份",code:4]
		monthList << [name:"5月份",code:5]
		monthList << [name:"6月份",code:6]
		monthList << [name:"7月份",code:7]
		monthList << [name:"8月份",code:8]
		monthList << [name:"9月份",code:9]
		monthList << [name:"10月份",code:10]
		monthList << [name:"11月份",code:11]
		monthList << [name:"12月份",code:12]
		
		model["monthList"] = monthList
		
		Calendar calendar = Calendar.getInstance()
		model["currentMonth"] = calendar.get(Calendar.MONTH) + 1
		
		render(view:'/vacate/vacateExplainSearch',model:model)
	}
	
	def vacateExplainGrid ={
		def json=[:]
		def user = User.get(params.userId)
		def company = Company.get(params.companyId)
		if(params.refreshHeader){
			json["gridHeader"] = vacateService.getVacateExplainListLayout()
		}
		
		//增加查询条件
		def searchArgs =[:]
		
		if(params.departName && !"".equals(params.departName)) searchArgs["applyDepart"] = params.departName
		if(params.chinaName && !"".equals(params.chinaName)) searchArgs["applyName"] = params.chinaName
		
		//获取查询时间------------------------------------------------------------------------------
		def month
		
		Calendar calendar = Calendar.getInstance()
		//当前查询月份
		month = calendar.get(Calendar.MONTH)
		if(params.month && !"".equals(params.month)){
			month = Util.obj2int(params.month)-1
		}
		searchArgs["month"] = month
		
		//-------------------------------------------------------------------------------------
		
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			args["user"] = user
			
			json["gridData"] = vacateService.getVacateExplainDataStore(args,searchArgs)
			
		}
		if(params.refreshPageControl){
			def total = vacateService.getVacateExplainCount(company,user,searchArgs)
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
	}
	
	//2015-2-28--------------增加按月统计功能---------------------------------------------------
	def exportStaticByMonth ={
		OutputStream os = response.outputStream
		def company = Company.get(params.companyId)
		
		Calendar calendar = Calendar.getInstance()
		def fileName = calendar.get(Calendar.YEAR) + "年职工考勤情况表"
		if(params.month){
			fileName = calendar.get(Calendar.YEAR) + "年" + params.month + "月份职工考勤情况表"
		}
		
		//获取查询时间
		def month1,month2,month
		
		//当前查询月份
		month = calendar.get(Calendar.MONTH)
		if(params.month && !"".equals(params.month)){
			month = Util.obj2int(params.month)-1
		}
		
		month1 = new GregorianCalendar(calendar.get(Calendar.YEAR),month,1)
		month2 = new GregorianCalendar(calendar.get(Calendar.YEAR),month + 1,1)
		
		response.setContentType('application/vnd.ms-excel')
		response.setHeader("Content-disposition", "attachment; filename=" + new String((fileName+".xls").getBytes("GB2312"), "ISO_8859_1"))
		
		def searchArgs =[:]
		
		if(params.applyName && !"".equals(params.applyName)) searchArgs["applyName"] = params.applyName
		if(params.applyDepart && !"".equals(params.applyDepart)) searchArgs["applyDepart"] = params.applyDepart
		
		def c = Vacate.createCriteria()

		def _List = c.list{
			eq("company",company)
			
			and{
				lt("startDate",month2.time)
				ge("startDate",month1.time)
			}
			
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
			order("applyDepart", "asc")
		}
		def excel = new ExcelExport()
		excel.vacateByMonthExport(os,fileName,_List)
	}
	def staticByMonthGrid ={
		def json=[:]
		def user = User.get(params.userId)
		def company = Company.get(params.companyId)
		if(params.refreshHeader){
			def _gridHeader =[]

			_gridHeader << ["name":"序号","width":"26px","colIdx":0,"field":"rowIndex"]
			_gridHeader << ["name":"部门（机构）","width":"100px","colIdx":1,"field":"applyDepart"]
			_gridHeader << ["name":"姓名","width":"80px","colIdx":2,"field":"applyName"]
			_gridHeader << ["name":"请假类型","width":"60px","colIdx":3,"field":"vacateType"]
			_gridHeader << ["name":"开始时间","width":"100px","colIdx":4,"field":"startDate"]
			_gridHeader << ["name":"结束时间","width":"100px","colIdx":5,"field":"endDate"]
			_gridHeader << ["name":"天数","width":"60px","colIdx":6,"field":"numbers"]
			_gridHeader << ["name":"事由","width":"auto","colIdx":7,"field":"remark"]
			
			json["gridHeader"] = _gridHeader
		}
		
		def searchArgs =[:]
		
		if(params.applyName && !"".equals(params.applyName)) searchArgs["applyName"] = params.applyName
		if(params.applyDepart && !"".equals(params.applyDepart)) searchArgs["applyDepart"] = params.applyDepart
		
		//获取查询时间
		def month1,month2,month
		
		Calendar calendar = Calendar.getInstance()
		
		//当前查询月份
		month = calendar.get(Calendar.MONTH)
		if(params.month && !"".equals(params.month)){
			month = Util.obj2int(params.month)-1
		}
		
		month1 = new GregorianCalendar(calendar.get(Calendar.YEAR),month,1)
		month2 = new GregorianCalendar(calendar.get(Calendar.YEAR),month + 1,1)
		
		if(params.refreshData){
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)

			def offset = (nowPage-1) * perPageNum
			def max  = perPageNum

			def _json = [identifier:'id',label:'name',items:[]]
			
			def c = Vacate.createCriteria()
			def pa=[max:max,offset:offset]
			def query = {
				eq("company",company)
				
//				between("startDate", month1.time, month2.time)
				
				and{
					lt("startDate",month2.time)
					ge("startDate",month1.time)
				}
				
				searchArgs.each{k,v->
					like(k,"%" + v + "%")
				}
				
				order("applyName", "asc")
			}
			def _list = c.list(pa,query)
			
			def idx = 0
			if(offset!=null) idx=offset
			_list.each{
				def sMap =[:]
				sMap["rowIndex"] = idx+1
				sMap["id"] = it.id
				sMap["applyDepart"] = it.applyDepart
				sMap["applyName"] = it.applyName
				sMap["vacateType"] = it.vacateType
				sMap["startDate"] = it.getFormatteStartDate()
				sMap["endDate"] = it.getFormatteEndDate()
				sMap["numbers"] = it.numbers
				sMap["remark"] = it.remark
				
				_json.items+=sMap
				
				idx += 1
			}

			json["gridData"] = _json
			
		}
		if(params.refreshPageControl){
			def c = Vacate.createCriteria()
			def query = {
				eq("company",company)
				
				and{
					lt("startDate",month2.time)
					ge("startDate",month1.time)
				}
				
				searchArgs.each{k,v->
					like(k,"%" + v + "%")
				}
			}
			def totalNum = c.count(query)
			json["pageControl"] = ["total":totalNum.toString()]
		}
		render json as JSON
	}
	def staticByMonthSearchView ={
		def model =[:]
		def currentUser = springSecurityService.getCurrentUser()
		def dataList = Depart.findAllByCompany(currentUser.company,[sort: "serialNo", order: "asc"])
		model["departList"] = dataList
		
		def monthList = []
		monthList << [name:"1月份",code:1]
		monthList << [name:"2月份",code:2]
		monthList << [name:"3月份",code:3]
		monthList << [name:"4月份",code:4]
		monthList << [name:"5月份",code:5]
		monthList << [name:"6月份",code:6]
		monthList << [name:"7月份",code:7]
		monthList << [name:"8月份",code:8]
		monthList << [name:"9月份",code:9]
		monthList << [name:"10月份",code:10]
		monthList << [name:"11月份",code:11]
		monthList << [name:"12月份",code:12]
		
		model["monthList"] = monthList
		
		Calendar calendar = Calendar.getInstance()
		model["currentMonth"] = calendar.get(Calendar.MONTH) + 1
		
		render(view:'/vacate/staticByMonthSearch',model:model)
	}
	
	//-------------------------------------------------------------------------------
	def searchView ={
		def model =[:]
		def currentUser = springSecurityService.getCurrentUser()
		def dataList = Depart.findAllByCompany(currentUser.company,[sort: "serialNo", order: "asc"])
		model["departList"] = dataList
		render(view:'/vacate/search',model:model)
	}
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
						shareService.addFlowLog(vacate.id,"vacate",nextUser,"委托授权给【" + authorize.beAuthorizerDepart + ":" + authorize.getFormattedAuthorizer() + "】")
						nextUser = authorize.beAuthorizer
						nextDepart = authorize.beAuthorizerDepart
					}
					//-------------------------------------------------------------------------
					
					//任务指派给当前拟稿人
					taskService.setAssignee(vacate.taskId, nextUser.username)
//					taskService.claim(vacate.taskId, nextUser.username)
					
					def args = [:]
					args["type"] = "【请假】"
					
					if(nextStatus.equals("已签发")){
						args["content"] = "领导已审批，请您查看名称为  【" + vacate.applyName +  "】 的请假申请"
					}else{
						args["content"] = "请您审核名称为  【" + vacate.applyName +  "】 的请假申请"
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
			contentStatus:frontStatus,
			status:"0"
		)
		if(gtask!=null){
			gtask.dealDate = new Date()
			gtask.status = "1"
			gtask.save()
		}
		
		if(vacate.save(flush:true)){
			//2015-4-11------增加自动添加意见功能----------------------------------------------
			if(!"新增".equals(frontStatus)){
				//默认增加意见内容：同意
				shareService.addCommentAuto(currentUser,frontStatus,vacate.id,"vacate")
			}
			//--------------------------------------------------------------------------
			
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
		
		//增加查询条件
		def searchArgs =[:]
		
		if(params.departName && !"".equals(params.departName)) searchArgs["applyDepart"] = params.departName
		if(params.chinaName && !"".equals(params.chinaName)) searchArgs["applyName"] = params.chinaName
		
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			args["user"] = user
			
			json["gridData"] = vacateService.getVacateDataStore(args,searchArgs)
			
		}
		if(params.refreshPageControl){
			def total = vacateService.getVacateCount(company,user,searchArgs)
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
	//2015-4-11------增加同一表单，启用不同流程的功能,通过当前用户的信息获取对应的流程------------------------------------
	private def getRelationFlow ={user,type ->
		def company = user.company
		def flowCode = this.flowCode
		
		def position = shareService.getUserPosition(user,"分管领导")
		if("sj".equals(type)){
			//事假
			if("leader".equals(position)){
				//分管领导
				flowCode = this.flowCodeLeader
			}else if("deptLeader".equals(position)){
				//部门领导
				flowCode = this.flowCodeDeptLeader
			}
		}else{
			//年休假
			if("leader".equals(position) || "deptLeader".equals(position) ){
				//分管领导
				flowCode = this.flowCodeNxLeader
			}else{
				//部门领导
				flowCode = this.flowCodeNx
			}
		}
		def flowBusiness = FlowBusiness.findByFlowCodeAndCompany(flowCode,company)
		if(flowBusiness && !"".equals(flowBusiness.relationFlow)){
			return flowBusiness.relationFlow
		}else{
			return null
		}
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
			redirect(action:"vacateShow",params:params)
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
			if(!params.notNeedFlow){
				vacate.applyName=user.getFormattedName()
				vacate.applyDepart = user.getDepartName()
				vacate.currentUser = user
			}
		}
		model["user"]=user
		model["company"] = company
		model["vacate"] = vacate
		
		if(params.notNeedFlow){
			model["notNeedFlow"] = params.notNeedFlow
		}
		
		FieldAcl fa = new FieldAcl()
		
		//判断是否出现保存功能
		def isChange = false
		if("admin".equals(user.getUserType())){
			//管理员
			isChange = true
		}else if(user.getAllRolesValue().contains("请假管理员")){
			//拥有对应角色
			isChange = true
		}else if(vacate.currentUser && user.equals(vacate.currentUser) && vacate.status.equals("新增")){
		}else{
			//普通用户
			fa.readOnly += ["startDate","endDate","vacateType","numbers","remark"]
		}
		
		if(isChange){
			model["notNeedFlow"] = true
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
			vacate.currentUser = currentUser
			vacate.currentDepart = currentUser.getDepartName()
			vacate.currentDealDate = new Date()
			
			vacate.drafter = currentUser
			vacate.drafterDepart = currentUser.getDepartName()
			
			vacate.startDate = Util.convertToTimestamp(params.startDate)
			vacate.endDate = Util.convertToTimestamp(params.endDate)
		}
		vacate.properties = params
		vacate.clearErrors()
		
		//增加读者域信息
		if(!vacate.readers.find{ it.id.equals(currentUser.id) }){
			vacate.addToReaders(currentUser)
		}
		
		if(!params.notNeedFlow){
			//流程引擎相关信息处理-------------------------------------------------------------------------------------
			if(!vacate.processInstanceId){
				//2015-4-12---------------------增加对领导以及分管领导特殊流程的处理----------------------------------------
				def _type = "sj"
				if("年休假".equals(vacate.vacateType)){
					_type = "nx"
				}
				def _relationFlow = this.getRelationFlow(currentUser, _type)
				if(_relationFlow){
					params.relationFlow = _relationFlow
				}else{
					json["result"] = "noFlow"
					render json as JSON
					return
				}
				//---------------------------------------------------------------------------------------------
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
		}else{
			vacate.status="已结束"
		}
		//-------------------------------------------------------------------------------------------------
		
		if(vacate.save(flush:true)){
			json["id"] = vacate.id
			json["companyId"]=vacate.company.id
			json["result"] = "true"
			
			if("new".equals(_status) && !params.notNeedFlow ){
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
		def seleSql = " select a.*,b.china_name,c.depart_id,c.depart_name from ( select a.*,b.bjnums,c.nxjnums,d.hjnums,e.sajnums,f.qtjnums from ( select a.apply_name,sum(a.numbers) sjnums from rs_vacate a where a.vacate_type = '事假'  group by a.apply_name) a left join  ";
		seleSql+=" ( select a.apply_name,sum(a.numbers) bjnums from rs_vacate a where a.vacate_type = '病假'  group by a.apply_name) b on a.apply_name = b.apply_name left join "
		seleSql+=" ( select a.apply_name,sum(a.numbers) nxjnums from rs_vacate a where a.vacate_type = '年休假'  group by a.apply_name) c on a.apply_name = c.apply_name left join  "
		seleSql+=" ( select a.apply_name,sum(a.numbers) hjnums from rs_vacate a where a.vacate_type = '婚假'  group by a.apply_name) d on a.apply_name = d.apply_name left join  "
		seleSql+=" ( select a.apply_name,sum(a.numbers) sajnums from rs_vacate a where a.vacate_type = '丧假'  group by a.apply_name) e on a.apply_name = e.apply_name left join "
		seleSql+=" ( select a.apply_name,sum(a.numbers) qtjnums from rs_vacate a where a.vacate_type = '其他'  group by a.apply_name) f on a.apply_name = f.apply_name ) a left join rosten_staff_per b on a.apply_name = b.china_name left join  "
		seleSql+=" (select  a.* ,b.depart_name,c.china_name from rosten_staff_per_rosten_depart  a  left join  rosten_depart  b on a.depart_id = b.id left join rosten_staff_per c on a.PERSON_INFOR_DEPARTS_ID = c.id) c on a.apply_name = c.china_name "
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
		seleSql+="  select '' apply_name, sum(sjnums),sum(bjnums),sum(nxjnums),sum(hjnums),sum(sajnums),sum(qtjnums),'合计' china_name,'' depart_id, '' depart_name from  "
		seleSql+=" ( select a.*,b.bjnums,c.nxjnums,d.hjnums,e.sajnums,f.qtjnums from ( select a.apply_name,sum(a.numbers) sjnums from rs_vacate a where a.vacate_type = '事假'  group by a.apply_name) a  "
		seleSql+=" left join   ( select a.apply_name,sum(a.numbers) bjnums from rs_vacate a where a.vacate_type = '病假'  group by a.apply_name) b on a.apply_name = b.apply_name left join  "
		seleSql+="  ( select a.apply_name,sum(a.numbers) nxjnums from rs_vacate a where a.vacate_type = '年休假'  group by a.apply_name) c on a.apply_name = c.apply_name left join  "
		seleSql+="  ( select a.apply_name,sum(a.numbers) hjnums from rs_vacate a where a.vacate_type = '婚假'  group by a.apply_name) d on a.apply_name = d.apply_name left join  "
		seleSql+=" ( select a.apply_name,sum(a.numbers) sajnums from rs_vacate a where a.vacate_type = '丧假'  group by a.apply_name) e on a.apply_name = e.apply_name left join  "
		seleSql+=" ( select a.apply_name,sum(a.numbers) qtjnums from rs_vacate a where a.vacate_type = '其他'  group by a.apply_name) f on a.apply_name = f.apply_name ) a "
		seleSql+=" left join rosten_staff_per b on a.apply_name = b.china_name left join  (select  a.* ,b.depart_name,c.china_name from rosten_staff_per_rosten_depart  a  left join  rosten_depart  b on a.depart_id = b.id left join rosten_staff_per c on a.PERSON_INFOR_DEPARTS_ID = c.id) c on a.apply_name = c.china_name "
		
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
		
		//增加查询条件
		def searchArgs =[:]
		
		if(params.departName && !"".equals(params.departName)) searchArgs["applyDepart"] = params.departName
		if(params.chinaName && !"".equals(params.chinaName)) searchArgs["applyName"] = params.chinaName
		
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			args["user"] = user
			
			json["gridData"] = vacateService.getAllAskForDataStore(args,searchArgs)
			
		}
		if(params.refreshPageControl){
			def total = vacateService.getAllVacateCount(company,searchArgs)
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
	}
	
}
