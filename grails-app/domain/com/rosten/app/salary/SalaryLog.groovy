package com.rosten.app.salary

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company

/*
 * 修改记录
 */
class SalaryLog {
	String id
	
	//修改人
	String userName
	
	//修改时间
	Date createDate = new Date()
	
	//修改字段
	String changeField
	
	//原始值
	String oldValue
	
	//新值
	String newValue
	
	//备注
	String remark
	
    static belongsTo = [company:Company]
	
    static constraints = {
		remark nullable:true,blank:true
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "RS_SA_LOG"
	}
}
