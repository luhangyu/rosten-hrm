package com.rosten.app.staff

import com.rosten.app.system.User
import java.text.SimpleDateFormat

//学历学位
class Degree {
	String id
	
	//学校名称
	String degreeName
	
	//入学时间
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
	
	//学历
	String degree = "本科"
	
	//专业
	String major
	
    static constraints = {
		
    }
	static belongsTo = [use:User]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_DEGREE"
	}
}
