package com.rosten.app.train

import com.rosten.app.staff.PersonInfor
import java.text.SimpleDateFormat
import com.rosten.app.system.Company

import com.rosten.app.annotation.GridColumn
import com.rosten.app.util.SystemUtil

class TrainMessage {

	String id
	
	//培训用户
	PersonInfor personInfor
	
	@GridColumn(name="培训人",formatter="trainMessage_formatTopic",colIdx=1,width="60px")
	def getUserName(){
		return personInfor?.chinaName
	}
	
	@GridColumn(name="部门名称",colIdx=2)
	def getUserDepartName(){
		return personInfor?.getUserDepartName()
	}
	
	//培训考试结果
	@GridColumn(name="培训考试结果",colIdx=5)
	String trainResult
	
	//培训证书发放结果
	/*
	boolean isSendCert = false
	
	def getIsSendCertValue(){
		if(isSendCert)return "是"
		else return "否"
	}*/
	
	@GridColumn(name="培训证书是否发放",colIdx=4)
	String trainCert = "否"
	
	//备注
	String remark
	
	//学员个人培训费用
	@GridColumn(name="培训费用",colIdx=3)
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
	def personInforId(){
		return personInfor.id
	}
	
	static belongsTo = [trainCourse:TrainCourse]
	
    static constraints = {
		userMoney nullable:true,blank:true
		trainResult nullable:true,blank:true
		remark nullable:true,blank:true
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_TRAIN_ME"
		
		//兼容mysql与oracle
		def systemUtil = new SystemUtil()
		if(systemUtil.getDatabaseType().equals("oracle")){
			remark sqlType:"clob"
		}else{
			remark sqlType:"text"
		}
	}
	
}
