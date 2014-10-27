package com.rosten.app.staff

import java.text.SimpleDateFormat
import java.util.Date

import com.rosten.app.system.Attachment;
import com.rosten.app.system.Company
import com.rosten.app.annotation.GridColumn

class Bargain {
	String id
	
	//员工
	@GridColumn(name="姓名",colIdx=1,width="60px",formatter="bargain_formatTopic")
	def getPersonInforName(){
		return personInfor?.chinaName
	}
	
	//合同编号
	@GridColumn(name="合同编号",colIdx=2)
	String bargainSerialNo
	
	//合同类别
	@GridColumn(name="合同类别",colIdx=3)
	String bargainType
	
	//起聘时间
	Date startDate = new Date()
	
	@GridColumn(name="起聘日期",colIdx=4)
	def getFormatteStartDate(){
		if(startDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(startDate)
		}else{
			return ""
		}
	}
	
	//终聘时间
	Date endDate = new Date() + 360
	
	@GridColumn(name="终聘日期",colIdx=5)
	def getFormatteEndDate(){
		if(endDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(endDate)
		}else{
			return ""
		}
	}
	//附件
	Attachment attachment
	
	
	//创建时间
	Date createDate = new Date()

	@GridColumn(name="创建时间",width="106px",colIdx=6)
	def getFormattedCreatedDate(){
		if(createDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(createDate)
		}else{
			return ""
		}
	}
	
    static constraints = {
		attachment nullable:true,blank:true
    }
	static belongsTo = [personInfor:PersonInfor,company:Company]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_BARGAIN"
	}
}
