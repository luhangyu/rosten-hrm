package com.rosten.app.train

import grails.converters.JSON

import com.rosten.app.util.FieldAcl
import com.rosten.app.util.Util
import com.rosten.app.system.Company
import com.rosten.app.system.User

class TrainController {
	def trainService
	def springSecurityService
	
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
	
	/**
	 * 员工培训信息
	 */
	def trainMessageGrid ={
		def json=[:]
		def company = Company.get(params.companyId)
		if(params.refreshHeader){
			json["gridHeader"] = trainService.getTrainMessageListLayout()
		}
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			json["gridData"] = trainService.getTrainMessageDataStore(args)
			
		}
		if(params.refreshPageControl){
			def total = trainService.getTrainMessageCount(company)
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
		
	}
	
	/**
	 * 培训信息增加
	 */
	def trainMessageAdd ={
		redirect(action:"trainMessageShow",params:params)
	}
	
	def trainMessageShow ={
		def model =[:]
		def currentUser = springSecurityService.getCurrentUser()
		
		def user = User.get(params.userid)
		def company = Company.get(params.companyId)
		def trainMessage = new TrainMessage()
		if(params.id){
			trainMessage = TrainMessage.get(params.id)
		}else{
			trainMessage.user = currentUser
		}
		
		model["company"] = company
		model["trainMessage"] = trainMessage
		
		FieldAcl fa = new FieldAcl()
		if("normal".equals(user.getUserType())){
			//普通用户
//			fa.readOnly += ["description"]
		}
//		fa.readOnly += ["trainAddress"]
		model["fieldAcl"] = fa
		
		render(view:'/train/trainMessage',model:model)
	}
	
	/**
	 * 培训班选择
	 */
	def courseSelect ={
		def courseList =[]
		def company = Company.get(params.companyId)
		TrainCourse.findAllByCompany(company).each{
			def json=[:]
			json["id"] = it.id
			json["name"] = it.courseName
			courseList << json
		}
		render courseList as JSON
	}
	
	/**
	 * 培训信息保存
	 */
	def trainMessageSave ={
		def json=[:]
		def trainMessage = new TrainMessage()
		if(params.id && !"".equals(params.id)){
			trainMessage = TrainMessage.get(params.id)
		}else{
			if(params.companyId){
				trainMessage.company = Company.get(params.companyId)
			}
		}
		trainMessage.properties = params
		trainMessage.clearErrors()
		
		trainMessage.trainCourse = TrainCourse.get(params.courseId)
		trainMessage.user = User.get(params.userId)
		
		if(trainMessage.save(flush:true)){
			json["result"] = "true"
		}else{
			trainMessage.errors.each{
				println it
			}
			json["result"] = "false"
		}
		render json as JSON
	}
	
	/**
	 * 删除
	 */
	def trainMessageDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def trainMessage = TrainMessage.get(it)
				if(trainMessage){
					trainMessage.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
    
}
