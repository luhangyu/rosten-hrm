package com.rosten.app.salary

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company

/*
 * 档位
 */
class Gear {
	String id
	
	//名称
	@GridColumn(name="档位名称",formatter="gear_formatTopic",colIdx=1,width="150px")
	String gearName
	
	//系数
	@GridColumn(name="档位系数",colIdx=2)
	String gearNumber
	
	static belongsTo = [company:Company]
	
    static constraints = {
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "RS_SA_GEAR"
	}
}
