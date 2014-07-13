package com.rosten.app.staff

import com.rosten.app.system.User
import java.text.SimpleDateFormat

//工作简历
class WorkResume {
	String id
	
	//开始时间
	Date startDate
	
	def getFormatteStartDate(){
		if(startDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(startDate)
		}else{
			return ""
		}
	}
	
	//结束时间
	Date endDate
	
	def getFormatteEndDate(){
		if(endDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(endDate)
		}else{
			return ""
		}
	}
	
	//工作单位
	String workCompany
	
	//工作内容
	String workContent
	
	//担任职务
	String duty
	
	//证明人
	String proveName
	
	//备注
	String remark
	
    static constraints = {
		proveName nullable:true,blank:true
		remark nullable:true,blank:true
    }
	static belongsTo = [user:User]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_WORKRESUME"
		
		workContent sqlType:"text"
		remark sqlType:"text"
	}
}
