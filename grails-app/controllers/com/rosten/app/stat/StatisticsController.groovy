package com.rosten.app.stat

import com.rosten.app.system.Company
import com.rosten.app.system.Depart
import com.rosten.app.util.Util
import grails.converters.JSON

class StatisticsController {
	
	def getDepartUsers ={
		def company = Company.get(params.id)
		def json = [identifier:'id',label:'name',items:[]]
		Depart.findAllByCompany(company).each{
			def sMap = ["id":it.id,"name":it.departName,"parentId":it.parent?.id,"number":it.getAllUser().size()]
			json.items+=sMap
		}
		render json as JSON
	}
	
    def payweal ={
		def model =[:]
		model["company"] = Company.get(params.companyId)
		render(view:'/statistics/chart',model:model)
	}
}
