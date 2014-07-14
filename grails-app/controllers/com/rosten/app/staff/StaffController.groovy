package com.rosten.app.staff

import com.rosten.app.system.Company
import com.rosten.app.system.User
import com.rosten.app.util.FieldAcl
import com.rosten.app.system.Depart
import com.rosten.app.util.Util
import com.rosten.app.system.UserDepart
import grails.converters.JSON
import com.rosten.app.system.UserRole
import com.rosten.app.system.UserType
import com.rosten.app.system.SystemService

class StaffController {
	def springSecurityService
	def systemService
	
	def userDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def user = User.get(it)
				if(user){
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
		
		def user = new User()
		if(params.id && !"".equals(params.id)){
			user = User.get(params.id)
		}else{
			user.enabled = true
			if(!params.sysFlag && params.userNameFront){
				params.username = params.userNameFront + params.username
			}
		}
		user.properties = params
		user.clearErrors()
		
		if(params.userTypeName && !params.userTypeName.equals(user.getUserTypeName())){
			def userType = UserType.findByTypeName(params.userTypeName)
			if(userType){
				user.userTypeEntity = userType
			}
		}
		
		if(params.companyId){
			def company = Company.get(params.companyId)
			user.company = company
		}
		if(user.save(flush:true)){
			
			UserDepart.removeAll(user)
			if(params.allowdepartsId){
				def depart = Depart.get(params.allowdepartsId)
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
			
			model["result"] = "true"
		}else{
			user.errors.each{
				println it
			}
			model["result"] = "false"
		}
		render model as JSON
	}
	def userAdd ={
		redirect(action:"userShow",params:params)
	}
	def userShow ={
		def model =[:]
		
		//当前登录用户
		def loginUser = User.get(params.userid)
		model["loginUser"]= loginUser
		
		//当前选中的部门
		model["departId"] = params.currentDepartId;
		
		if(params.id){
			def _user = User.get(params.id)
			model["user"] = _user
			
			def allowrolesName=[]
			def allowrolesId =[]
			UserRole.findAllByUser(_user).each{
				allowrolesName << it.role.authority
				allowrolesId << it.role.id
			}
			model["allowrolesName"] = allowrolesName.join(',')
			model["allowrolesId"] = allowrolesId.join(",")
			model["username"] = Util.strRight(_user.username, "-")
			
		}else{
			model["user"] = new User()
			
		}
		if(params.companyId){
			def company = Company.get(params.companyId)
			model["company"] = company
		}
		
		model["userType"] = "normal"
		
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
		if(loginUser==null || model["userType"].equals("normal")){
			params.each{key,value->
				fa.readOnly << key
			}
		}
		if(params.id){
			fa.readOnly << "username"
		}
		model["fieldAcl"] = fa
		render(view:'/staff/user',model:model)
	}
	
	def userGrid ={
		def departEntity = Depart.get(params.departId)
		def json=[:]
		if(params.refreshHeader){
			def _gridHeader =[]

			_gridHeader << ["name":"序号","width":"26px","colIdx":0,"field":"rowIndex"]
			_gridHeader << ["name":"登录名","width":"auto","colIdx":1,"field":"username","formatter":"personInfor_formatTopic"]
			_gridHeader << ["name":"姓名","width":"auto","colIdx":2,"field":"chinaName"]
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
		def totalNum = 0
		if(params.refreshData){
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)

			def offset = (nowPage-1) * perPageNum
			def max  = perPageNum

			def _json = [identifier:'id',label:'name',items:[]]
			
			def userList = UserDepart.findAllByDepart(departEntity,[max: max, sort: "user", order: "desc", offset: offset])
			totalNum = userList.size()
			
			def idx = 0
			userList.each{
				def _user = it.user
				def personInfor = PersonInfor.findByUser(_user)
				def contactInfor = ContactInfor.findByUser(_user)
				
				def sMap =[:]
				sMap["rowIndex"] = idx+1
				sMap["id"] = _user.id
				sMap["username"] = _user.username
				sMap["chinaName"] = _user.getFormattedName()
				sMap["departName"] = _user.getDepartName()
				sMap["type"] = _user.getUserTypeName()
				sMap["sex"] = personInfor?.sex
				sMap["birthday"] = personInfor?.getFormatteBirthday()
				sMap["idCard"] = personInfor?.idCard
				sMap["mobile"] = contactInfor?.mobile
				sMap["nationality"] = personInfor?.nativeAddress
				sMap["politicsStatus"] = personInfor?.politicsStatus
				sMap["status"] = "正常"
				
				_json.items+=sMap
				
				idx += 1
			}

			json["gridData"] = _json
		}
		
		if(params.refreshPageControl){
			json["pageControl"] = ["total":totalNum.toString()]
		}
		render json as JSON
	}
	
	def personInforView ={
		def model =[:]
//		model["depart"] = Depart.get(params.id)
		model["departId"] = params.id
		render(view:'/staff/personManageView',model:model)
	}
	
	def depart ={
		def model =[:]
		model["company"] = Company.get(params.companyId)
		render(view:'/staff/personManage',model:model)
	}
	
	def getPersonInfor ={
		def model =[:]
		def entity
		
		def depart = Depart.get(params.departId)
		
		def currentUser = springSecurityService.getCurrentUser()
		def user = User.get(params.userId)
		if(user){
			entity = PersonInfor.findByUser(user)
			model["departName"] = user.getDepartName()
			model["departId"] = user.getDepartEntity?.id
		}else{
			entity = new PersonInfor()
			model["departName"] = depart.departName
			model["departId"] = depart.id
		}
		model["company"] = currentUser.company
		model["personInforEntity"] = entity
		model["userTypeList"] = UserType.findAllByCompany(currentUser.company)
		
		
		FieldAcl fa = new FieldAcl()
		if( user && "normal".equals(user.getUserType()) && !currentUser.equals(user)){
			//非管理员并且不是本人时，不允许修改所有相关字段
			fa.readOnly += ["chinaName"]
		}
		model["fieldAcl"] = fa
		
		render(view:'/staff/personInfor',model:model)
	}
	def getContactInfor ={
		def model =[:]
		def entity
		
		def user = User.get(params.userId)
		if(user){
			entity = ContactInfor.findByUser(user)
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
		
		def user = User.get(params.userId)
		if(user){
			entity = Degree.findByUser(user)
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
		
		def user = User.get(params.userId)
		if(user){
			entity = WorkResume.findByUser(user)
		}else{
			entity = new WorkResume()
		}
		model["workResumeEntity"] = entity
		FieldAcl fa = new FieldAcl()
		model["fieldAcl"] = fa
		render(view:'/staff/workResume',model:model)
	}
}
