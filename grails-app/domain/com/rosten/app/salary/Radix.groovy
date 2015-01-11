package com.rosten.app.salary

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company

/*
 * 基础基数
 */
class Radix {
	String id
	
    //类型
	String category
	
	//基础
	String radixNumber
	
	
	static belongsTo = [company:Company]
	
    static constraints = {
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "RS_SA_RADIX"
	}
}
