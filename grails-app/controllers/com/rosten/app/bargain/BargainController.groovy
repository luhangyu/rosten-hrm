package com.rosten.app.bargain

import com.rosten.app.staff.Bargain
import com.rosten.app.staff.BargainConfig
import com.rosten.app.util.FieldAcl
import com.rosten.app.system.Company
import grails.converters.JSON

class BargainController {
	def springSecurityService
	
	def bargainConfigSave ={
		def json=[:]
		def config = new BargainConfig()
		if(params.id && !"".equals(params.id)){
			config = BargainConfig.get(params.id)
		}
		config.properties = params
		config.clearErrors()
		config.company = Company.get(params.companyId)
		
		config.nowCancel = params.config_nowCancel
		config.frontCancel = params.config_frontCancel
		
		if(config.save(flush:true)){
			json["result"] = true
			json["coonfigId"] = config.id
			json["companyId"] = config.company.id
		}else{
			config.errors.each{
				println it
			}
			json["result"] = false
		}
		render json as JSON
	}
	def bargainConfig ={
		def model = [:]
		def user = springSecurityService.getCurrentUser()
		
		def config = BargainConfig.findWhere(company:user.company)
		if(config==null) {
			config = new BargainConfig()
			
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
		
		render(view:'/bargain/bargainConfig',model:model)
	}	
   	def getBargainPerson={
		def model =[:]
		def currentUser = springSecurityService.getCurrentUser()
		def company = currentUser.company
		model["company"] = company
		
		def bargain = Bargain.get(params.id)
		model["bargain"] = bargain
		
		render(view:'/staff/bargainPerson',model:model)
		
	}
	def bargainSearchView ={
		def model =[:]
		render(view:'/bargain/bargainSearch',model:model)
	}
}
