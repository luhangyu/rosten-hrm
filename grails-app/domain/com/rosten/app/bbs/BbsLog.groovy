package com.rosten.app.bbs

import java.text.SimpleDateFormat
import java.util.Date;
import com.rosten.app.system.User;

class BbsLog {
	
	String id
	
	User user
	
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
	
	static belongsTo = [bbs:Bbs]
	
    static constraints = {
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_BBS_LOG"
	}
}
