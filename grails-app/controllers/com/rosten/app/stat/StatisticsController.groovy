package com.rosten.app.stat

import com.rosten.app.system.Company
import com.rosten.app.system.Depart
import com.rosten.app.util.Util
import grails.converters.JSON
import com.rosten.app.system.UserType

class StatisticsController {
	
	def getDepartUsersByType ={
		def company = Company.get(params.id)
		def json = [identifier:'id',label:'name',items:[]]
		
		def index = 1
		def groupNames = params.groupNames.split(",")
		
		params.departIds.split(",").each{
			def depart = Depart.get(it)
			
			groupNames.each{item ->
				def lastIndex = Util.obj2str(index).padLeft(3,"0")
				def _number = 0
				depart.getAllUser().each{ _user ->
					if(item.equals(_user.userTypeEntity.typeName)){
						_number += 1
					}
				}
				def sMap = ["id":lastIndex,"name":depart.departName,"group":item,"number":_number]
				
				index += 1
				
				json.items+=sMap
			}
		}
		render json as JSON
	}
	
	def getDepartUsers ={
		def company = Company.get(params.id)
		def json = [identifier:'id',label:'name',items:[]]
		Depart.findAllByCompany(company).each{
			
			if(it.parent){
				def number = it.getAllUser().size()
				if(number && number > 0){
					def sMap = ["id":it.id,"name":it.departName,"parentId":it.parent?.id,"number":it.getAllUser().size()]
					json.items+=sMap
				}
			}
			
		}
		render json as JSON
	}
	
    def payweal ={
		def model =[:]
		def company = Company.get(params.companyId)
		model["company"] = company
		
		//员工编制统计图表中相关信息获取
		def groupList =[]
		UserType.findAllByCompany(company).each{
			groupList << it.typeName
		}
		model["groupList"] = groupList as JSON
		model["groupNames"] = groupList.join(",")
		
		def departList =[]
		def departIds =[]
		
		Depart.findAllByCompany(company).each{
			departList << it.departName
			departIds << it.id
		}
		model["departList"] = departList as JSON
		model["departIds"] = departIds.join(",")
		
		render(view:'/statistics/chart',model:model)
	}
}
