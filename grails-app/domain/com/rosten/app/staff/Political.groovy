package com.rosten.app.staff

import com.rosten.app.system.User
import java.text.SimpleDateFormat

//政治面貌
class Political {
	String id
	
	//政治面貌
	String political = "党员"
	
	//参加所在单位
	String addCompany
	
	//介绍人
	String introducer
	
	//转正日期
	Date trueDate
	def getFormatteTrueDate(){
		if(trueDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(trueDate)
		}else{
			return ""
		}
	}
	
	//参加时间
	Date addDate
	
	//转入单位
	String inCompany
	
	//转入时间
	Date inDate
	def getFormatteInDate(){
		if(inDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(inDate)
		}else{
			return ""
		}
	}
	
	//转入原因
	String inReson
	
	//转出日期
	Date outDate
	def getFormatteOutDate(){
		if(outDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(outDate)
		}else{
			return ""
		}
	}
	
	//转出原因
	String outReson
	
    static constraints = {
		addCompany nullable:true,blank:true
		introducer nullable:true,blank:true
		trueDate nullable:true,blank:true
		addDate nullable:true,blank:true
		inCompany nullable:true,blank:true
		inDate nullable:true,blank:true
		inReson nullable:true,blank:true
		outDate nullable:true,blank:true
		outReson nullable:true,blank:true
    }
	static belongsTo = [use:User]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_POLITICAL"
		
		inReson sqlType:"text"
		outReson sqlType:"text"
	}
}
