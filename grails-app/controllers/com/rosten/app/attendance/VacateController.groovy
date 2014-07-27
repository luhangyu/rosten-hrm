package com.rosten.app.attendance

import grails.converters.JSON

import com.rosten.app.util.FieldAcl
import com.rosten.app.util.Util
import com.rosten.app.system.Company
import com.rosten.app.system.User

class VacateController {
	def vacateService
	
	def getCommentLog ={
		def model =[:]
		def vacate = Vacate.get(params.id)
		if(vacate){
			def logs = VacateComment.findAllByVacate(vacate,[ sort: "createDate", order: "desc"])
			model["log"] = logs
		}
		
		render(view:'/share/commentLog',model:model)
	}
	def getFlowLog={
		def model =[:]
		def vacate = Vacate.get(params.id)
		if(vacate){
			def logs = VacateLog.findAllByVacate(vacate,[ sort: "createDate", order: "asc"])
			model["log"] = logs
			
			model["logEntityId"] = params.id
			model["logEntityName"] = "vacate"
		}
		
		render(view:'/share/flowLog',model:model)
	}
	
    def vacateGrid ={
		def json=[:]
		def company = Company.get(params.companyId)
		if(params.refreshHeader){
			json["gridHeader"] = vacateService.getVacateListLayout()
		}
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			json["gridData"] = vacateService.getVacateDataStore(args)
			
		}
		if(params.refreshPageControl){
			def total = vacateService.getVacateCount(company)
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
	}
		
		
	def vacateAdd ={
		redirect(action:"vacateShow",params:params)
	}
		
	def vacateShow ={
		def model =[:]
		
		def user = User.get(params.userid)
		def company = Company.get(params.companyId)
		def vacate = new Vacate()
		if(params.id){
			vacate = Vacate.get(params.id)
		}else{
			vacate.user = user
		}
		model["user"]=user
		model["company"] = company
		model["vacate"] = vacate
		
		FieldAcl fa = new FieldAcl()
		if("normal".equals(user.getUserType())){
			//普通用户
//			fa.readOnly += ["description"]
		}
		model["fieldAcl"] = fa
		
		render(view:'/vacate/vacate',model:model)
	}
	
	def vacateSave ={
		def json=[:]
		def vacate = new Vacate()
		if(params.id && !"".equals(params.id)){
			vacate = Vacate.get(params.id)
		}else{
			if(params.companyId){
				vacate.company = Company.get(params.companyId)
			}
		}
		vacate.properties = params
		vacate.clearErrors()
		
		if(vacate.save(flush:true)){
			json["result"] = "true"
		}else{
			vacate.errors.each{
				println it
			}
			json["result"] = "false"
		}
		render json as JSON
	}
}
