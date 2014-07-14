package com.rosten.app.train

import com.rosten.app.system.User
import java.text.SimpleDateFormat
import com.rosten.app.system.Company

import com.rosten.app.annotation.GridColumn

class TrainMessage {

	String id
	
	//培训用户
	User user
	
	@GridColumn(name="用户名称",formatter="trainMessage_formatTopic",colIdx=1,width="60px")
	def getUserName(){
		return user?.getFormattedName()
	}
	
	//培训班
	TrainCourse trainCourse
	
	//培训考试结果
	@GridColumn(name="培训考试结果")
	String trainResult
	
	//培训证书发放结果
	boolean isSendCert = false
	@GridColumn(name="培训证书是否发放")
	def getIsSendCertValue(){
		if(isSendCert)return "是"
		else return "否"
	}
	
	//备注
	String remark
	
	//培训班名称
	@GridColumn(name="培训班",colIdx=2)
	def getCourseName(){
		if(trainCourse) return trainCourse.courseName
		else return ""
	}
	
	//培训时间
	@GridColumn(name="培训时间",colIdx=3)
	def getTrainDate(){
		if(trainCourse && trainCourse.trainDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(trainCourse.trainDate)
		}else{
			return ""
		}
	}
	
	static belongsTo = [company:Company]
	
    static constraints = {
		trainResult nullable:true,blank:true
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_TRAIN_MESSAGE"
		
		remark sqlType:"text"
		
	}
	
}
