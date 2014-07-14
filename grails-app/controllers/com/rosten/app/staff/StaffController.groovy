package com.rosten.app.staff

import com.rosten.app.system.Company
import com.rosten.app.system.User
import com.rosten.app.util.FieldAcl
import com.rosten.app.system.Depart
import com.rosten.app.util.Util
import com.rosten.app.system.UserDepart
import grails.converters.JSON

class StaffController {
	def springSecurityService
	
	def userGrid ={
		def departEntity = Depart.get(params.departId)
		def json=[:]
		if(params.refreshHeader){
			def _gridHeader =[]

			_gridHeader << ["name":"序号","width":"26px","colIdx":0,"field":"rowIndex"]
			_gridHeader << ["name":"姓名","width":"auto","colIdx":1,"field":"userName","formatter":"personInfor_formatTopic"]
			_gridHeader << ["name":"部门","width":"auto","colIdx":2,"field":"departName"]
			_gridHeader << ["name":"编制类别","width":"auto","colIdx":3,"field":"type"]
			_gridHeader << ["name":"性别","width":"auto","colIdx":4,"field":"sex"]
			_gridHeader << ["name":"出生年月","width":"auto","colIdx":5,"field":"birthday"]
			_gridHeader << ["name":"身份证号","width":"auto","colIdx":6,"field":"idCard"]
			_gridHeader << ["name":"手机号码","width":"auto","colIdx":7,"field":"mobile"]
			_gridHeader << ["name":"民族","width":"auto","colIdx":8,"field":"nationality"]
			_gridHeader << ["name":"政治面貌","width":"auto","colIdx":9,"field":"politicsStatus"]
			_gridHeader << ["name":"状态","width":"auto","colIdx":10,"field":"status"]

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
				sMap["userName"] = _user.getFormattedName()
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
		
		def currentUser = springSecurityService.getCurrentUser()
		def user = User.get(params.userId)
		if(user){
			entity = PersonInfor.findByUser(user)
		}else{
			entity = new PersonInfor()
		}
		
		model["personInforEntity"] = entity
		
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
		
		render(view:'/staff/degree',model:model)
	}
    def index() { }
}
