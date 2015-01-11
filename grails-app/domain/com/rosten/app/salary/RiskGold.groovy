package com.rosten.app.salary

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company

/*
 * 五险一金
 */
class RiskGold {
	String id
	
	//公积金基数
	Double gjj
	
	//公积金比例
	Number gjjBl
	
	//失业保险基数
	Double sybx
	
	//失业保险比例
	Number sybxBl
	
	//养老保险基数
	Double ylbx
	
	//养老保险比例
	Number ylbxBl
	
	//生育保险基数
	Double syubx
	
	//生育保险比例
	Number syubxBl
	
	//医疗保险基数
	Double ylaobx
	
	//医疗保险比例
	Number ylaobxBl
	
	
	static belongsTo = [company:Company]
	
    static constraints = {
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "RS_SA_RISKGOLD"
	}
}
