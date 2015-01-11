package com.rosten.app.salary

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company

/*
 * 各种补贴
 */
class Subsidy {
	String id
	
	//名称
	String subsidyName
	
	//金额
	Double money = 0
	
	//月份:[1,2,3]
	String months
	
	def getMonthsValue(){
		return months?months.split(","):[]
	}
	def setMonthsValue(ostr){
		def _1 = this.getMonthsValue()
		_1 << ostr
		this.months = _1.unique().join(",")
	}
	
	//计算公式
	String expression
	
    static constraints = {
		expression nullable:true,blank:true
    }
	
	static belongsTo = [company:Company]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "RS_SA_SUBSIDY"
	}
}
