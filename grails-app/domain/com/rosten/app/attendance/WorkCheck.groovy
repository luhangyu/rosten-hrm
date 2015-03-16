package com.rosten.app.attendance

import java.text.SimpleDateFormat
import java.util.Date;

import com.rosten.app.system.Company
import com.rosten.app.annotation.GridColumn

/*
 * 上下班考勤记录
 */
class WorkCheck {
	String id
	
	@GridColumn(name="员工名称",colIdx=1)
	String staffName
	
	//部门名称
	@GridColumn(name="所属部门",colIdx=2)
	String staffDepart
	
	@GridColumn(name="考勤日期",colIdx=3)
	String checkDate
	
	@GridColumn(name="上班时间",colIdx=4)
	String startDate
	
	@GridColumn(name="下班时间",colIdx=5)
	String endDate

	//创建时间
	Date createDate = new Date()
	def getFormattedCreatedDate(){
		if(createDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(createDate)
		}else{
			return ""
		}
	}
	
    static constraints = {
		startDate nullable:true,blank:true
		endDate nullable:true,blank:true
    }
	
	static belongsTo = [company:Company]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "RS_VACATE_CHECK"
		
	}
}
