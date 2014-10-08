package com.rosten.app.staff

import java.text.SimpleDateFormat
import java.util.Date

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Depart
import com.rosten.app.system.Company
import com.rosten.app.system.User;

class StatusChange {
	String id
	
	//申请人,有可能申请人与调动员工并非同一人，一般情况下为同一人
	User applayUser
	
	@GridColumn(name="申请人",colIdx=1,width="60px",formatter="staffStatusChange_formatTopic")
	def getApplayPersonInforName(){
		return personInfor?.chinaName
	}
	
	
	@GridColumn(name="申请部门",colIdx=2,width="80px")
	def getOutDepartName(){
		def departs = personInfor.departs
		if(departs && departs.size()>0){
			return departs[0].departName
		}
		return ""
	}
	
	//申请类型:离职/退休
	String changeType
	
	//申请理由
	@GridColumn(name="申请理由",colIdx=4)
	String changeReason
	
	//申请时间
	Date changeDate = new Date()
	
	@GridColumn(name="申请时间",width="106px",colIdx=3)
	def getFormattedChangeDate(){
		if(changeDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(changeDate)
		}else{
			return ""
		}
	}
	
	//创建时间
	Date createDate = new Date()
	
	@GridColumn(name="创建时间",width="106px",colIdx=5)
	def getFormattedCreatedDate(){
		if(createDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(createDate)
		}else{
			return ""
		}
	}
	
	static belongsTo = [personInfor:PersonInfor,company:Company]
	
    static constraints = {
    }
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_STATUSCHANGE"
		changeReason sqlType:"text"
	}
}
