package com.rosten.app.system

import com.rosten.app.annotation.GridColumn

class SystemCodeItem {
	String id
	
	//代码编号
	@GridColumn(name="条目编号",colIdx=1)
	String code
	
	@GridColumn(name="条目名称",colIdx=2)
	String name
	
	//显示序号
	@GridColumn(name="显示顺序",colIdx=3)
	Integer serialNo
	
	static belongsTo = [systemCode:SystemCode]
	
    static constraints = {
		serialNo nullable:true,blank:true
    }
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_SYSTEMCODE_ITEM"
	}
}
