package com.rosten.app.system

import grails.converters.JSON
import com.rosten.app.util.Util

class SystemExtendController {
	def systemExtendService
	
	def systemCodeDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def systemCode = SystemCode.get(it)
				if(systemCode){
					systemCode.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	def systemCodeSave ={
		def json=[:]
		def systemCode = new SystemCode()
		if(params.id && !"".equals(params.id)){
			systemCode = SystemCode.get(params.id)
			systemCode.properties = params
			systemCode.clearErrors()
		}else{
			systemCode.properties = params
			systemCode.clearErrors()
			systemCode.company = Company.get(params.companyId)
		}
		//删除systemCodeItem所有数据
		if(systemCode.items){
			def _list = []
			_list += systemCode.items
			systemCode.items.clear()
			_list.each{
				it.delete()
			}
		}
		
		JSON.parse(params.systemCodeItems).eachWithIndex{elem, i ->
			def systemCodeItem = new SystemCodeItem(serialNo:i+1,code:elem.code,name:elem.name)
			systemCode.addToItems(systemCodeItem)
		}
		
		if(systemCode.save(flush:true)){
			json["result"] = "true"
		}else{
			systemCode.errors.each{
				println it
			}
			json["result"] = "false"
		}
		render json as JSON
	}
	def systemCodeAdd ={
		redirect(action:"systemCodeShow",params:params)
	}
	def systemCodeShow ={
		def model =[:]
		
		def company = Company.get(params.companyId)
		def systemCode = new SystemCode()
		if(params.id){
			systemCode = SystemCode.get(params.id)
		}
		model["company"] = company
		model["systemCode"] = systemCode
		
		render(view:'/system/systemCode',model:model)
	}
	
	def systemCodeGrid ={
		def json=[:]
		def company = Company.get(params.companyId)
		def user = User.get(params.userId)
		
		if(params.refreshHeader){
			json["gridHeader"] = systemExtendService.getSystemCodeListLayout()
		}
		
		//2014-9-1 增加搜索功能
		def searchArgs =[:]
		
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			
			def gridData = systemExtendService.getSystemCodeListDataStore(args,searchArgs)
			json["gridData"] = gridData
			
		}
		if(params.refreshPageControl){
			def total = systemExtendService.getSystemCodeCount(company,searchArgs)
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
	}
	def systemCodeItemGrid ={
		def json=[:]
		
		def systemCode = SystemCode.get(params.id)
		if(params.refreshHeader){
			json["gridHeader"] = systemExtendService.getSystemCodeItemListLayout()
		}
		
		//2014-9-1 增加搜索功能
		def searchArgs =[:]
		
		if(params.refreshData){
			if(!systemCode){
				json["gridData"] = ["identifier":"id","label":"name","items":[]]
			}else{
				def args =[:]
				int perPageNum = Util.str2int(params.perPageNum)
				int nowPage =  Util.str2int(params.showPageNum)
				
				args["offset"] = (nowPage-1) * perPageNum
				args["max"] = perPageNum
				args["systemCode"] = systemCode
				
				def gridData = systemExtendService.getSystemCodeItemListDataStore(args,searchArgs)
				json["gridData"] = gridData
			}
		}
		if(params.refreshPageControl){
			if(!systemCode){
				json["pageControl"] = ["total":"0"]
			}else{
				def total = systemExtendService.getSystemCodeItemCount(systemCode,searchArgs)
				json["pageControl"] = ["total":total.toString()]
			}
			
		}
		render json as JSON
	}
	def systemCodeItemShow ={
		def model =[:]
		if(params.id){
			model["systemCodeItem"] = SystemCodeItem.get(params.id)
		}else{
			model["systemCodeItem"] = new SystemCodeItem()
		}
		render(view:'/system/systemCodeItem',model:model)
	}
}
