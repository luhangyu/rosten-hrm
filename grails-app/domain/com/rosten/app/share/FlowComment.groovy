package com.rosten.app.share

import java.text.SimpleDateFormat
import java.util.Date

import com.rosten.app.system.User
import com.rosten.app.system.Company
import com.rosten.app.util.SystemUtil

class FlowComment {
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
	
	//所属domain的id号
	String belongToId
	
	//所属流程模块；例如:员工入职菜单流程则belongToObject为staffAdd
	String belongToObject
	
	static belongsTo = [company:Company]
	
    static constraints = {
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_FLOW_COMMENT"
		
		//兼容mysql与oracle
		def systemUtil = new SystemUtil()
		if(systemUtil.getDatabaseType().equals("oracle")){
			content sqlType:"clob"
		}else{
			content sqlType:"longtext"
		}
	}
}
