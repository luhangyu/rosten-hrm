package com.rosten.app.train

import com.rosten.app.system.User
import java.text.SimpleDateFormat
import com.rosten.app.system.Company

import com.rosten.app.annotation.GridColumn

class TrainMessage {

	String id
	
	//培训用户
	User user
	
	@GridColumn(name="培训人",formatter="trainMessage_formatTopic",colIdx=1,width="60px")
	def getUserName(){
		return user?.getFormattedName()
	}
	
	@GridColumn(name="部门名称",colIdx=2)
	def getUserDepartName(){
		return user?.getDepartName()
	}
	
	//培训班
	TrainCourse trainCourse
	
	//培训考试结果
	@GridColumn(name="培训考试结果")
	String trainResult
	
	//培训证书发放结果
	/*
	boolean isSendCert = false
	
	def getIsSendCertValue(){
		if(isSendCert)return "是"
		else return "否"
	}*/
	@GridColumn(name="培训证书是否发放")
	String trainCert
	
	
	
	
	//备注
	String remark
	
	//学员个人培训费用
	@GridColumn(name="培训费用")
	Double userMoney
	
	//培训班名称
	def getCourseName(){
		if(trainCourse) return trainCourse.courseName
		else return ""
	}
	
	//培训时间
	def getTrainDate(){
		if(trainCourse && trainCourse.trainDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(trainCourse.trainDate)
		}else{
			return ""
		}
	}
	@GridColumn(name="操作",width="80px",formatter="staffItem_action")
	def staffItemId(){
		return id
	}
	
	static belongsTo = [trainCourse:TrainCourse]
	
    static constraints = {
		trainResult nullable:true,blank:true
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_TRAIN_MESSAGE"
		
		remark sqlType:"text"
		
	}
	
}
