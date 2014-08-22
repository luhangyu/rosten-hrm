package com.rosten.app.system

import grails.converters.JSON

class HrmController {
	def systemService
	
	def modelInit ={
		def json,model,resource
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
			model = new Model(company:company)
			model.modelName = "通知公告"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "bbs"
			model.serialNo = 5
			
			resource = new Resource()
			resource.resourceName = "配置文档"
			resource.url = "bbsConfigManage"
			resource.imgUrl = "images/rosten/navigation/config.png"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "各人待办"
			resource.url = "mybbsManage"
			resource.imgUrl = "images/rosten/navigation/bbs_my.gif"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "最新公告"
			resource.url = "newbbsManage"
			resource.imgUrl = "images/rosten/navigation/bbs_new.gif"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "所有公告"
			resource.url = "allbbsManage"
			resource.imgUrl = "images/rosten/navigation/bbs_all.gif"
			model.addToResources(resource)
			
			model.save()
			
			model = new Model(company:company)
			model.modelName = "员工管理"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "staffManage"
			model.serialNo = 4
			
			resource = new Resource()
			resource.resourceName = "员工登记"
			resource.url = "newStaffAdd"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "人事异动"
			resource.url = "staffDepartChange"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			model.save()
			
			model = new Model(company:company)
			model.modelName = "人才招聘"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "staffEmploy"
			model.serialNo = 6
			
			resource = new Resource()
			resource.resourceName = "招聘计划管理"
			resource.url = "staffEmployPlan"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "招聘岗位发布"
			resource.url = "staffEmployPublic"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "招聘人员审核"
			resource.url = "staffEmployAudit"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "人才报到"
			resource.url = "staffEmployCheck"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			model.save()
			
			model = new Model(company:company)
			model.modelName = "人才培养"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "staffDevelop"
			model.serialNo = 7
			
			resource = new Resource()
			resource.resourceName = "学历学位进修"
			resource.url = "degreeStudy"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "出国进修"
			resource.url = "forgeinStudy"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			model.save()
			
			model = new Model(company:company)
			model.modelName = "合同管理"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "bargain"
			model.serialNo = 8
			
			resource = new Resource()
			resource.resourceName = "合同档案管理"
			resource.url = "bargainManage"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			model.save()
			
			model = new Model(company:company)
			model.modelName = "劳资福利"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "payweal"
			model.serialNo = 9
			
			resource = new Resource()
			resource.resourceName = "岗位工资标准"
			resource.url = "positionPayweal"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "薪级工资标准"
			resource.url = "salaryPayweal"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "绩效工资标准"
			resource.url = "perforPayweal"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "员工工资管理"
			resource.url = "staffPayweal"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			model.save()
			
			model = new Model(company:company)
			model.modelName = "考勤管理"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "workAttendance"
			model.serialNo = 10
			
			resource = new Resource()
			resource.resourceName = "员工请假"
			resource.url = "staffAskFor"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "所有请假"
			resource.url = "allAskFor"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "请假汇总"
			resource.url = "askForStatic"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			model.save()
			
			model = new Model(company:company)
			model.modelName = "培训管理"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "trainManage"
			model.serialNo = 11
			
			resource = new Resource()
			resource.resourceName = "培训班管理"
			resource.url = "trainCourse"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "培训管理"
			resource.url = "trainMessage"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			model.save()
			
			model = new Model(company:company)
			model.modelName = "统计分析"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "static"
			model.serialNo = 12
			
			
			resource = new Resource()
			resource.resourceName = "报表设计"
			resource.url = "staticDesign"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "统计分析"
			resource.url = "static"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			model.save()
			
			//完善个人配置中的个人工作日志
			model = Model.findByModelCodeAndCompany("personconfig",company)
			resource = Resource.findByUrlAndModel("personWorkLog",model)
			if(!resource){
				resource = new Resource()
				resource.resourceName = "工作日志"
				resource.url = "personWorkLog"
				resource.imgUrl = "images/rosten/navigation/rosten.png"
				model.addToResources(resource)
				
				model.save()
			}
			
			//增加人事系统特有的服务列表信息
			NormalService.findAllByCompany(company).each{
				it.delete()
			}
			
			systemService.initData_service(path,company)
			
			def _service = new NormalService()
			_service.serviceName = "协同办公系统"
			_service.company = company
			_service.functionUrl = "http://oa.html"
			_service.imgUrl = "images/rosten/service/oa.gif"
			_service.save(flush:true)
			
			_service = new NormalService()
			_service.serviceName = "资产管理系统"
			_service.company = company
			_service.functionUrl = "http://zcxt.html"
			_service.imgUrl = "images/rosten/service/zcxt.gif"
			_service.save(flush:true)
			
			json = [result:'true']
		}catch(Exception e){
			log.debug(e);
			json = [result:'error']
		}
		render json as JSON
	}
}
