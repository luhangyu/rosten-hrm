package com.rosten.app.salary

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company

/*
 * 档位
 */
class Gear {
	String id
	
	//名称
	String gearName
	
	//系数
	String gearNumber
	
	static belongsTo = [company:Company]
	
    static constraints = {
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "RS_SA_GEAR"
	}
}
