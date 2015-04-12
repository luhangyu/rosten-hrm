package com.rosten.app.travel
import grails.converters.JSON
import com.rosten.app.util.FieldAcl
import com.rosten.app.util.SystemUtil
import com.rosten.app.util.Util
import com.rosten.app.system.Attachment
import com.rosten.app.system.Company
import com.rosten.app.system.User
import com.rosten.app.system.Model
import com.rosten.app.staff.PersonInfor

import com.rosten.app.gtask.Gtask
import com.rosten.app.workflow.FlowBusiness

import com.rosten.app.share.ShareService
import com.rosten.app.start.StartService

/*
 * 出差申请
 */
class TravelController {
	def springSecurityService
	def travelService
	def startService
	def shareService
	
	def searchView ={
		def model =[:]
		
		if(params.flowCode){
			//需要走流程
			def company = Company.get(params.companyId)
			def flowBusiness = FlowBusiness.findByFlowCodeAndCompany(params.flowCode,company)
			if(flowBusiness && !"".equals(flowBusiness.relationFlow)){
				def statusList = shareService.getFlowStatus(flowBusiness.relationFlow)
				model["statusList"] = statusList
			}
		}
		
		render(view:'/travel/travelSearch',model:model)
	}
	def travelDelete = {
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def travel = TravelApp.get(it)
				if(travel){
					travel.delete(flush: true)
				}
			}
			json = [result: 'true']
		}catch(Exception e){
		    json = [result: 'error']
		}
		render json as JSON
	}
	
	def travelSave = {
		def json=[:]
		
		//获取配置文档
		def config = TravelAppConfig.first()
		if(!config){
			json["result"] = "noConfig"
			render json as JSON
			return
		}
		
		def user = springSecurityService.getCurrentUser()
		
		def status = "new"
		def entity
		if(params.id && !"".equals(params.id)){
			entity = TravelApp.get(params.id)
			entity.properties = params
			entity.clearErrors()
			status = "old"
		}else{
			entity = new TravelApp()
			entity.properties = params
			entity.clearErrors()
			
			entity.company = Company.get(params.companyId)
			
			entity.applyNum = config.nowYear + config.nowSN.toString().padLeft(4,"0")
			
			entity.drafter = user
			entity.status = "已结束"
			
		}
		
		if(params.travelUserIds){
			entity.travelUsers?.clear()
			params.travelUserIds.split(",").each{
				entity.addToTravelUsers(PersonInfor.get(it))
			}
		}
		if(!entity.readers.find{ it.id.equals(user.id) }){
			entity.addToReaders(user)
		}
		
		//判断是否需要走流程-------------------------------------------------------------------------------------
		if(params.relationFlow){
			//流程引擎相关信息处理
//			shareService.businessFlowSave(entity,user,params.relationFlow)
		}
		//-------------------------------------------------------------------------------------------------
		entity.travelNum = Util.obj2int(params.travelNum)
		entity.days = Util.obj2int(params.days)
		entity.startDate = Util.convertToTimestamp(params.startDate)
		entity.endDate = Util.convertToTimestamp(params.endDate)
		
		if(entity.save(flush:true)){
			json["result"] = true
			json["id"] = entity.id
			json["companyId"] = entity.company.id
			
			if("new".equals(status)){
				//添加日志
				//shareService.addFlowLog(entity.id,params.flowCode,user,"申请出差,编号为:" + entity.applyNum)
				
				//修改配置文档中的流水号
				config.nowSN += 1
				config.save(flush:true)
			}
		}else{
			entity.errors.each{
				println it
			}
			json["result"] = false
		}
		render json as JSON
	}
	def travelAdd ={
		if(params.flowCode){
			//需要走流程
			def company = Company.get(params.companyId)
			def flowBusiness = FlowBusiness.findByFlowCodeAndCompany(params.flowCode,company)
			if(flowBusiness && !"".equals(flowBusiness.relationFlow)){
				params.relationFlow = flowBusiness.relationFlow
			}else{
				//不存在流程引擎关联数据
				render '<h2 style="color:red;width:660px;margin:0 auto;margin-top:60px">当前业务不存在流程设置，无法创建，请联系管理员！</h2>'
				return
			}
		}
		redirect(action:"travelShow",params:params)
	}
	def travelShow ={
		def model =[:]
		
		def user = User.get(params.userid)
		def company = Company.get(params.companyId)
		def entity = new TravelApp()
		if(params.id){
			entity = TravelApp.get(params.id)
		}else{
			model.companyId = params.companyId
			
			entity.drafter = user
			entity.drafterDepart = user.getDepartName()
			entity.currentUser = user
			entity.currentDepart = user.getDepartName()
			
		}
		if(!entity){
			render '<h2 style="color:red;width:500px;margin:0 auto">此出差申请已过期或删除，请联系管理员！</h2>'
			return
		}
		model["user"]=user
		model["company"] = company
		model["travel"] = entity
		
		//是否可编辑,采用附件的标记位isShowFile做判断
		def isChange = shareService.checkPemission(user,entity,"应用管理员","员工申请")
		
		FieldAcl fa = new FieldAcl()
		if(!isChange){
			fa.readOnly = ["drafterDepart","startDate","endDate"]
		}else{
			//允许所有字段都可以编辑
			model["isShowFile"] = true
		}
		model["fieldAcl"] = fa
		
		//流程相关信息----------------------------------------------
//		model["relationFlow"] = params.relationFlow
//		model["flowCode"] = params.flowCode
		//------------------------------------------------------
		
		render(view:'/travel/travel',model:model)
	}
	def travelGrid ={
		def json=[:]
		def user = User.get(params.userId)
		def company = Company.get(params.companyId)
		if(params.refreshHeader){
			json["gridHeader"] = travelService.getTravelListLayout()
		}
		
		//2014-9-3 增加搜索功能
		def searchArgs =[:]
		
		if(params.applyNum && !"".equals(params.applyNum)) searchArgs["applyNum"] = params.applyNum
		if(params.travelDate && !"".equals(params.travelDate)) searchArgs["travelDate"] = Util.convertToTimestamp(params.travelDate)
		
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
				gridData = travelService.getTravelListDataStoreByUser(args,searchArgs)
			}else if("all".equals(params.type)){
				//所有文档
				gridData = travelService.getTravelListDataStore(args,searchArgs)
			}
			
			json["gridData"] = gridData
			
		}
		if(params.refreshPageControl){
			def total
			if("person".equals(params.type)){
				//个人待办
				total = travelService.getTravelCountByUser(company,user,searchArgs)
			}else if("all".equals(params.type)){
				//所有文档
				total = travelService.getTravelCount(company,searchArgs)
			}
			
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
	}
	def travelFlowDeal = {
		def json=[:]
		
		def entity = TravelApp.get(params.id)
		def currentUser = springSecurityService.getCurrentUser()
		def company = currentUser.company
		def frontStatus = entity.status
		def nextUserName=[]
		
		//流程引擎相关信息处理-----------------------------------------------------------------------------------------------
		def map =[:]
		if(params.conditionName){
			map[params.conditionName] = params.conditionValue
		}
		def nextInfor = shareService.businessFlowDeal(company,entity,params.dealUser,map,"travel","travel")
		//--------------------------------------------------------------------------------------------------
		
		//增加待办事项
		if(nextInfor.nextUser && nextInfor.nextUser.size()>0){
			def args = [:]
			args["type"] = "【出差申请】"
			args["content"] = "请您审核【" + entity.getDrafterName() + "】的出差申请"
			args["contentStatus"] = nextInfor.nextStatus
			args["contentId"] = entity.id
			args["user"] = nextInfor.nextUser[0]
			args["company"] = company
			
			startService.addGtask(args)
			
			nextUserName << nextInfor.nextUser[0].getFormattedName()
		}
		
		//判断下一处理人是否与当前处理人员为同一人
		if(currentUser.equals(entity.currentUser)){
			json["refresh"] = true
		}
		
		//修改代办事项状态
		def gtask = Gtask.findWhere(
			user:currentUser,
			company:company,
			contentId:entity.id,
			contentStatus:frontStatus,
			status:"0"
		)
		if(gtask!=null){
			gtask.dealDate = new Date()
			gtask.status = "1"
			gtask.save(flush:true)
		}
		
		if(entity.save(flush:true)){
			//2015-4-11------增加自动添加意见功能----------------------------------------------
			if(!"新增".equals(frontStatus)){
				//默认增加意见内容：同意
				shareService.addCommentAuto(currentUser,frontStatus,entity.id,"travel")
			}
			//--------------------------------------------------------------------------
			
			//添加日志
			def logContent
			switch (true){
				case entity.status.contains("已签发"):
					logContent = "签发文件【" + nextUserName.join("、") + "】"
					
					//增加相关与会人员的待办工作任务
					def gtaskList = []
					gtaskList += entity.guesters
					gtaskList += entity.joiners
					gtaskList.unique().each{
						def args = [:]
						args["type"] = "【出差申请】"
						args["content"] = "请您审核【" + entity.getDrafterName() +  "】的出差申请"
						args["contentStatus"] = entity.status
						args["contentId"] = entity.id
						args["user"] = it
						args["company"] = company
						args["openDeal"] = true
						
						startService.addGtask(args)
					}
					
					break
				case entity.status.contains("已结束"):
					logContent = "结束流程"
					break
				case entity.status.contains("归档"):
					logContent = "归档"
					break
				case entity.status.contains("不同意"):
					logContent = "不同意！"
					break
				default:
					logContent = "提交" + entity.status + "【" + nextUserName.join("、") + "】"
					break
			}
			shareService.addFlowLog(entity.id,"travel",currentUser,logContent)
			
			json["nextUserName"] = nextUserName.join("、")
			json["result"] = true
		}else{
			entity.errors.each{
				println it
			}
			json["result"] = false
		}
		render json as JSON
		
	}
	
	def travelFlowBack ={
		def json=[:]
		def entity = TravelApp.get(params.id)
		
		def currentUser = springSecurityService.getCurrentUser()
		def company = currentUser.company
		def frontStatus = entity.status
		def nextUser
		
		try{
			
			def nextInfor = shareService.businessFlowBack(entity)
			if(nextInfor.nextUser){
				nextUser = nextInfor.nextUser
				//增加待办事项
				def args = [:]
				args["type"] = "【出差申请】"
				args["content"] = "请查看由【 " + entity.chargePerson+"】负责的申请回馈！"
				args["contentStatus"] = nextInfor.nextStatus
				args["contentId"] = entity.id
				args["user"] = nextUser
				args["company"] = company
				
				startService.addGtask(args)
			}
			
			//修改代办事项状态
			def gtask = Gtask.findWhere(
				user:currentUser,
				company:company,
				contentId:entity.id,
				contentStatus:frontStatus,
				status:"0"
			)
			if(gtask!=null){
				gtask.dealDate = new Date()
				gtask.status = "1"
				gtask.save(flush:true)
			}
			
			entity.save(flush:true)
			
			//判断下一处理人是否与当前处理人员为同一人
			if(currentUser.equals(nextUser)){
				json["refresh"] = true
			}
			
			//添加日志
			def logContent = "退回【" + nextUser?.getFormattedName() + "】"
			shareService.addFlowLog(entity.id,"travel",currentUser,logContent)
				
			json["result"] = true
			json["nextUserName"] = nextUser?.getFormattedName()
			
		}catch(Exception e){
			json["result"] = false
		}
		render json as JSON
	}
	
	def travelConfig = {
		def model = [:]
		def user = springSecurityService.getCurrentUser()
		
		def config = TravelAppConfig.findWhere(company:user.company)
		if(config==null) {
			config = new TravelAppConfig()
			
			Calendar cal = Calendar.getInstance();
			config.nowYear = cal.get(Calendar.YEAR)
			config.frontYear = config.nowYear -1
			
			model.companyId = user.company.id
		}else{
			model.companyId = config.company.id
		}
		model.config = config
		
		FieldAcl fa = new FieldAcl()
		model["fieldAcl"] = fa
		
		render(view:'/travel/travelConfig',model:model)
	}
	def travelConfigSave ={
		def json=[:]
		def config = new TravelAppConfig()
		if(params.id && !"".equals(params.id)){
			config = TravelAppConfig.get(params.id)
		}
		config.properties = params
		config.clearErrors()
		config.company = Company.get(params.companyId)
		
		config.nowCancel = params.config_nowCancel
		config.frontCancel = params.config_frontCancel
		
		if(config.save(flush:true)){
			json["result"] = true
			json["configId"] = config.id
			json["companyId"] = config.company.id
		}else{
			config.errors.each{
				println it
			}
			json["result"] = false
		}
		render json as JSON
	}
}
