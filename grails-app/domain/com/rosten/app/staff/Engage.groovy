package com.rosten.app.staff

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Attachment
import com.rosten.app.system.Company
import com.rosten.app.util.SystemUtil
import java.text.SimpleDateFormat
import java.util.Date

class Engage {
	String id
	
	//聘任人员
	@GridColumn(name="聘任人员",colIdx=1,width="80px",formatter="engage_formatTopic")
	String engageName
	
	//聘任部门
	@GridColumn(name="聘任部门",colIdx=2,width="80px")
	String engageDepart
	
	//聘任时间
	Date engageDate = new Date()
	
	@GridColumn(name="聘任时间",width="106px",colIdx=3)
	def getFormattedEngageDate(){
		if(engageDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(engageDate)
		}else{
			return ""
		}
	}
	
	//聘任理由
	@GridColumn(name="聘任理由",colIdx=4)
	String reason
	
	//是否已发布
	boolean isPublish = false
	@GridColumn(name="是否已发布",width="60px",colIdx=5)
	def getIsPublishFormat(){
		return isPublish?"是":"否 "
	}
	
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
	
	static belongsTo = [company:Company]
	
    static constraints = {
		reason nullable:true,blank:true
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "RS_STAFF_ENGAGE"
		
		//兼容mysql与oracle
		def systemUtil = new SystemUtil()
		if(systemUtil.getDatabaseType().equals("oracle")){
			reason sqlType:"clob"
		}else{
			reason sqlType:"text"
		}
	}
}
