package com.rosten.app.system

import grails.converters.JSON
import com.rosten.app.workflow.FlowBusiness

class HrmController {
	def systemService
	
	def modelInit ={
		
		/*
		 * 初始化菜单功能序号统一从7开始
		 */
		
		def json,model,resource
		def company = Company.get(params.id)
		def path = request.contextPath
		
		try{
			//删除当前单位下面的所有模块信息（除系统管理等基础模块）
			def modelCodes = ["system","workflow","public","sms","question","personconfig"]
			Model.findAllByCompany(company).each{
				if(!modelCodes.contains(it.modelCode)){
					FlowBusiness.findAllByModel(it).each{item->
						item.model = null
						item.save()
					}
					it.delete()
				}
			}
			//增加人事系统特有的功能模块
			
			//员工管理
			model = new Model(company:company)
			model.modelName = "员工管理"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "staffManage"
			model.serialNo = 7
			
			resource = new Resource()
			resource.resourceName = "代码配置"
			resource.url = "systemCodeManage"
			resource.imgUrl = "images/rosten/navigation/config.png"
			resource.serialNo = 1
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "员工入职"
			resource.url = "staffAdd"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 2
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "员工转正"
			resource.url = "staffProba"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 3
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "员工管理"
			resource.url = "staffRegi"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 4
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "员工调动"
			resource.url = "staffDepartChange"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 5
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "员工离职"
			resource.url = "staffLeave"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 6
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "员工退休"
			resource.url = "staffRetire"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 7
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "员工查询"
			resource.url = "staffSearch"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 8
			model.addToResources(resource)
			
			model.save()
			
			//通知公告
			model = new Model(company:company)
			model.modelName = "通知公告"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "bbs"
			model.serialNo = 8
			
			resource = new Resource()
			resource.resourceName = "配置文档"
			resource.url = "bbsConfigManage"
			resource.imgUrl = "images/rosten/navigation/config.png"
			resource.serialNo = 1
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "各人待办"
			resource.url = "mybbsManage"
			resource.imgUrl = "images/rosten/navigation/bbs_my.gif"
			resource.serialNo = 2
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "最新公告"
			resource.url = "newbbsManage"
			resource.imgUrl = "images/rosten/navigation/bbs_new.gif"
			resource.serialNo = 3
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "所有公告"
			resource.url = "allbbsManage"
			resource.imgUrl = "images/rosten/navigation/bbs_all.gif"
			resource.serialNo = 4
			model.addToResources(resource)
			
			model.save()
			
			//合同管理
			model = new Model(company:company)
			model.modelName = "合同管理"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "bargain"
			model.serialNo = 9
			
			resource = new Resource()
			resource.resourceName = "配置文档"
			resource.url = "bargainConfig"
			resource.imgUrl = "images/rosten/navigation/config.png"
			resource.serialNo = 1
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "合同档案管理"
			resource.url = "bargainManage"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 2
			model.addToResources(resource)
			
			model.save()
			
			//人才培养
			model = new Model(company:company)
			model.modelName = "人才培养"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "trainManage"
			model.serialNo = 10
			
			resource = new Resource()
			resource.resourceName = "培训管理"
			resource.url = "trainCourse"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 1
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "学历学位进修"
			resource.url = "degreeStudy"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 2
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "出国进修"
			resource.url = "forgeinStudy"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 3
			model.addToResources(resource)
			
			model.save()
			
			//劳资福利
			model = new Model(company:company)
			model.modelName = "劳资福利"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "payweal"
			model.serialNo = 11
			
			resource = new Resource()
			resource.resourceName = "岗位工资标准"
			resource.url = "positionPayweal"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "薪级工资标准"
			resource.url = "salaryPayweal"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 1
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "绩效工资标准"
			resource.url = "perforPayweal"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 2
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "员工工资管理"
			resource.url = "staffPayweal"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 3
			model.addToResources(resource)
			
			model.save()
			
			//考勤管理
			model = new Model(company:company)
			model.modelName = "考勤管理"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "workAttendance"
			model.serialNo = 12
			
			resource = new Resource()
			resource.resourceName = "员工请假"
			resource.url = "staffAskFor"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 1
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "所有请假"
			resource.url = "allAskFor"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 2
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "请假汇总"
			resource.url = "askForStatic"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 3
			model.addToResources(resource)
			
			model.save()
			
			//统计分析
			model = new Model(company:company)
			model.modelName = "统计分析"
			model.modelUrl = path + "/system/navigation"
			model.modelCode = "static"
			model.serialNo = 13
			
			resource = new Resource()
			resource.resourceName = "花名册"
			resource.url = "hmc"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 1
			model.addToResources(resource)
			
			resource = new Resource()
			resource.resourceName = "统计分析"
			resource.url = "static"
			resource.imgUrl = "images/rosten/navigation/rosten.png"
			resource.serialNo = 2
			model.addToResources(resource)
			
			model.save()
			
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
