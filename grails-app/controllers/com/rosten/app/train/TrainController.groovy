package com.rosten.app.train

import grails.converters.JSON

import com.rosten.app.util.FieldAcl
import com.rosten.app.util.Util
import com.rosten.app.system.Company
import com.rosten.app.system.User

class TrainController {
	def trainService
	
	def trainCourseDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def trainCourse = TrainCourse.get(it)
				if(trainCourse){
					trainCourse.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	def trainCourseSave ={
		def json=[:]
		def trainCourse = new TrainCourse()
		if(params.id && !"".equals(params.id)){
			trainCourse = TrainCourse.get(params.id)
		}else{
			if(params.companyId){
				trainCourse.company = Company.get(params.companyId)
			}
		}
		trainCourse.properties = params
		trainCourse.clearErrors()
		
		if(trainCourse.save(flush:true)){
			json["result"] = "true"
		}else{
			trainCourse.errors.each{
				println it
			}
			json["result"] = "false"
		}
		render json as JSON
	}
	def trainCourseAdd ={
		redirect(action:"trainCourseShow",params:params)
	}
	def trainCourseShow ={
		def model =[:]
		
		def user = User.get(params.userid)
		def company = Company.get(params.companyId)
		def trainCourse = new TrainCourse()
		if(params.id){
			trainCourse = TrainCourse.get(params.id)
		}
		model["user"]=user
		model["company"] = company
		model["trainCourse"] = trainCourse
		
		FieldAcl fa = new FieldAcl()
		if("normal".equals(user.getUserType())){
			//普通用户
//			fa.readOnly += ["description"]
		}
		model["fieldAcl"] = fa
		
		render(view:'/train/trainCourse',model:model)
	}
	def trainCourseGrid ={
		def json=[:]
		def company = Company.get(params.companyId)
		if(params.refreshHeader){
			json["gridHeader"] = trainService.getTrainCourseListLayout()
		}
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			json["gridData"] = trainService.getTrainCourseListDataStore(args)
			
		}
		if(params.refreshPageControl){
			def total = trainService.getTrainCourseCount(company)
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
	}
    
}
