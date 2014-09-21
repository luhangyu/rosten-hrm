package com.rosten.app.staff

import com.rosten.app.system.User

import java.text.SimpleDateFormat
import java.util.Date
import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Depart

class DepartChange {
	String id

	//申请调动员工
	User applayUser
	
	//调入部门
	Depart inDepart
	
	//变更类型:"升职","平调","降职","其他"
	String changeType = "升职"
	
	//变更理由
	String changeReason
	
	//变更时间
	Date changeDate = new Date()
	
	//调入岗位
	String inDuty
	
	//调入编制
	String inPersonType
	
	//创建时间
	Date createDate = new Date()
	
	@GridColumn(name="创建时间",width="106px")
	def getFormattedCreatedDate(){
		if(createDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(createDate)
		}else{
			return ""
		}
	}
	
	static constraints = {
		changeReason nullable:true,blank:true
		inDuty nullable:true,blank:true
		inPersonType nullable:true,blank:true
	}
	static belongsTo = [personInfor:PersonInfor]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_DEPARTCHANGE"
	}
}
