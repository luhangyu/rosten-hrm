package com.rosten.app.salary

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company

/*
 * 岗位级别
 */
class Quarters {
	String id
	
	//岗位名称
	String quaName
	
	//岗位系数
	String quaNumber
	
	static belongsTo = [company:Company]
	
    static constraints = {
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "RS_SA_QUARTERS"
	}
}
