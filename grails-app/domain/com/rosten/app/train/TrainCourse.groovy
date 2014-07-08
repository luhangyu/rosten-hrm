package com.rosten.app.train

import java.text.SimpleDateFormat
import java.util.Date;

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company

class TrainCourse {
	
	String id
	
	//培训班名称
	@GridColumn(name="培训班名称",formatter="trainCourse_formatTopic")
	String courseName
	
	//培训组织名称
	@GridColumn(name="组织者名称")
	String organizeName
	
	//培训时间
	Date trainDate = new Date()

	@GridColumn(name="培训时间")
	def getFormatteTrainDate(){
		if(trainDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(trainDate)
		}else{
			return ""
		}
	}
	
	//培训地点
	@GridColumn(name="培训地点")
	String trainAddress
	
	//培训对象
	String trainObject
	
	//创建时间
	Date createDate = new Date()
	
	@GridColumn(name="创建时间",width="106px")
	def getFormattedCreatedDate(){
		if(createDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(createDate)
		}else{
			return ""
		}
	}
	
	//描述
	String description
	
    static constraints = {
		trainObject nullable:true,blank:true
		description nullable:true,blank:true
    }
	
	static belongsTo = [company:Company]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_TRAIN_COURSE"
		
		description sqlType:"text"
	}
}
