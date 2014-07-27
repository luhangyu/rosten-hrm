package com.rosten.app.attendance

import java.text.SimpleDateFormat
import java.util.Date
import com.rosten.app.system.User

class VacateComment {

   String id
	
	User user
	
	String status
	
	String content
	
	//创建时间
	Date createDate = new Date()
	
	def getFormattedCreatedDate(){
		if(createDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(createDate)
		}else{
			return ""
		}
	}
	
	static belongsTo = [vacate:Vacate]
	
    static constraints = {
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_VACATE_COMMENT"
		content sqlType:"longtext"
	}
}
