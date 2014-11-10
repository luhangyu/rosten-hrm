package com.rosten.app.staff

import java.text.SimpleDateFormat

class NoticeBill {
	/*
	 * 各种类型的通知单
	 */
	
	String id
	
	//通知单类型：入职通知单,借用通知单,下派通知单
	String type
	
	//通知单编号
	String noticeSerialNo
	
	//通知单存放地址url
	String noticeUrl
	
	//通知单时间
	Date noticeDate
	
	def getFormatteOutDate(){
		if(noticeDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(noticeDate)
		}else{
			return ""
		}
	}
	
	static belongsTo = [personInfor:PersonInfor]
	
	static constraints = {
		noticeUrl nullable:true,blank:true
	}
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_NOBI"
	}
}
