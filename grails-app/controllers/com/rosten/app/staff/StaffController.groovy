package com.rosten.app.staff

import com.rosten.app.system.Company
import com.rosten.app.system.User
import com.rosten.app.util.FieldAcl

class StaffController {
	def springSecurityService
	
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
