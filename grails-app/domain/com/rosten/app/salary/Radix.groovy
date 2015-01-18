package com.rosten.app.salary

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company

/*
 * 基础基数
 */
class Radix {
	String id
	
    //类型
	@GridColumn(name="类型名称",formatter="radix_formatTopic",colIdx=1,width="150px")
	String category
	
	//基础
	@GridColumn(name="基数",colIdx=2)
	String radixNumber
	
	
	static belongsTo = [company:Company]
	
    static constraints = {
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "RS_SA_RADIX"
	}
}
