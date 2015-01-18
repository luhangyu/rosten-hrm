package com.rosten.app.salary

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company

/*
 * 岗位级别
 */
class Quarters {
	String id
	
	//岗位名称
	@GridColumn(name="岗位名称",formatter="quarters_formatTopic",colIdx=1,width="150px")
	String quaName
	
	//岗位系数
	@GridColumn(name="岗位系数",colIdx=2)
	String quaNumber
	
	static belongsTo = [company:Company]
	
    static constraints = {
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "RS_SA_QUARTERS"
	}
}
