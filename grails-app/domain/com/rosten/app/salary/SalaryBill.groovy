package com.rosten.app.salary

import java.util.List;

import com.rosten.app.annotation.GridColumn
import com.rosten.app.staff.PersonInfor
import com.rosten.app.system.Attachment
import com.rosten.app.system.Company


/*
 * 工资单
 */
class SalaryBill {
	
	String id

	//工资单名称
	String billName
	
	//制作人
	String userName
	
	//制作日期
	Date createDate = new Date()
	
	//附件
	Attachment attachment
	
	List persons
	static hasMany=[persons:PersonInfor]
	
    static constraints = {
		attachment nullable:true,blank:true
    }
	
	static belongsTo = [company:Company]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "RS_SA_SABILL"
	}
}
