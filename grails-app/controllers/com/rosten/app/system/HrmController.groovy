package com.rosten.app.system

import grails.converters.JSON

class HrmController {
	def systemService
	
	def modelInit ={
		def json
		def company = Company.get(params.id)
		def path = request.contextPath
		
		try{
			//删除当前单位下面的所有模块信息（除系统管理等基础模块）
			def modelCodes = ["system","workflow","public","sms","question","personconfig"]
			Model.findAllByCompany(company).each{
				if(!modelCodes.contains(it.modelCode)){
					it.delete()
				}
			}
			//增加人事系统特有的功能模块
			def model = new Model(company:company)
			model.modelName = "人事异动"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "staffChange"
			model.description ="人事异动"
			model.save()
			
			model = new Model(company:company)
			model.modelName = "人才招聘"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "staffEmploy"
			model.description ="人才招聘"
			model.save()
			
			model = new Model(company:company)
			model.modelName = "人才培养"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "staffDevelop"
			model.description ="人才培养"
			model.save()
			
			model = new Model(company:company)
			model.modelName = "合同管理"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "bargain"
			model.description ="合同管理"
			model.save()
			
			model = new Model(company:company)
			model.modelName = "劳资福利"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "payweal"
			model.description ="劳资福利"
			model.save()
			
			model = new Model(company:company)
			model.modelName = "考勤管理"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "workAttendance"
			model.description ="考勤管理"
			model.save()
			
			model = new Model(company:company)
			model.modelName = "统计分析"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "static"
			model.description ="统计分析"
			
			def resource = new Resource()
			resource.resourceName = "统计分析"
			resource.url = "static"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			model.save()
			
			//增加人事系统特有的服务列表信息
			NormalService.findAllByCompany(company).each{
				it.delete()
			}
			
			systemService.initData_service(path,company)
			
			json = [result:'true']
		}catch(Exception e){
			log.debug(e);
			json = [result:'error']
		}
		render json as JSON
	}
}
