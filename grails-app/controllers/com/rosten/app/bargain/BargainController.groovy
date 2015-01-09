package com.rosten.app.bargain

import com.rosten.app.staff.Bargain
import com.rosten.app.staff.BargainConfig
import com.rosten.app.util.FieldAcl
import com.rosten.app.util.Util
import com.rosten.app.system.Company
import grails.converters.JSON
import com.rosten.app.system.Attachment

class BargainController {
	def springSecurityService
	
	def bargainItemGrid ={
		def json=[:]
		def bargain = Bargain.get(params.id)
		def personInfor = bargain?.personInfor
		def company = personInfor?.company
		
		if(params.refreshHeader){
			def _gridHeader =[]

			_gridHeader << ["name":"序号","width":"26px","colIdx":0,"field":"rowIndex"]
			_gridHeader << ["name":"合同编号","width":"auto","colIdx":1,"field":"bargainSerialNo","formatter":"bargainItem_formatTopic"]
			_gridHeader << ["name":"合同类别","width":"auto","colIdx":2,"field":"bargainType"]
			_gridHeader << ["name":"起聘日期","width":"auto","colIdx":3,"field":"startDate"]
			_gridHeader << ["name":"终聘日期","width":"auto","colIdx":4,"field":"endDate"]
			_gridHeader << ["name":"合同附件","width":"auto","colIdx":5,"field":"bargainFile","formatter":"bargainItem_formatFile"]
			//暂时不提供删除功能
//			_gridHeader << ["name":"操作","width":"80px","colIdx":6,"field":"actionId"]

			json["gridHeader"] = _gridHeader
		}
		if(params.refreshData){
			def _json = [identifier:'id',label:'name',items:[]]
			
			def c = Bargain.createCriteria()
			def _list = c.list{
				eq("company",company)
				eq("personInfor",personInfor)
				not {'in'("id",[bargain?.id])}
				order("bargainSerialNo", "desc")
			}
			def idx = 0
			_list.each{
				def sMap =[:]
				def _attachment = Attachment.findByBeUseId(it.id)
				
				sMap["rowIndex"] = idx+1
				sMap["id"] = it.id
				sMap["bargainSerialNo"] = it.bargainSerialNo
				sMap["bargainType"] = it.bargainType
				sMap["startDate"] = it.getFormatteStartDate()
				sMap["endDate"] = it.getFormatteEndDate()
				
				sMap["bargainFile"] = _attachment?_attachment.name:""
				sMap["bargainFileId"] = _attachment?_attachment.id:""
				sMap["actionId"] = it.id
				
				_json.items+=sMap
				
				idx += 1
			}

			json["gridData"] = _json
		}
		
		if(params.refreshPageControl){
			
			def c = Bargain.createCriteria()
			def query = {
				eq("company",company)
				eq("personInfor",personInfor)
				not {'in'("id",[bargain?.id])}
			}
			def totalNum = c.count(query)
			json["pageControl"] = ["total":totalNum.toString()]
		}
		render json as JSON
	}
	
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
