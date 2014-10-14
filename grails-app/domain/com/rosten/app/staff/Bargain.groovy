package com.rosten.app.staff

import java.text.SimpleDateFormat
import java.util.Date

import com.rosten.app.system.Attachment;
import com.rosten.app.system.Company
import com.rosten.app.annotation.GridColumn

class Bargain {
	String id
	
	//合同编号
	@GridColumn(name="合同编号",colIdx=1)
	String bargainSerialNo
	
	//合同类别
	@GridColumn(name="合同类别",colIdx=2)
	String bargainType
	
	//起聘时间
	Date startDate = new Date()
	
	@GridColumn(name="起聘日期",colIdx=3)
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
	
	@GridColumn(name="终聘日期",colIdx=4)
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
	
    static constraints = {
		attachment nullable:true,blank:true
    }
	static belongsTo = [personInfor:PersonInfor,company:Company]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_BARGAIN"
	}
}
