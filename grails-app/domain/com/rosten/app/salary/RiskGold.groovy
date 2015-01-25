package com.rosten.app.salary

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company
import com.rosten.app.system.User;
import com.rosten.app.staff.PersonInfor

/*
 * 五险一金
 */
class RiskGold {
	String id
	
	//公积金基数
	@GridColumn(name="公积金基数",colIdx=2)
	Double gjj
	
	//公积金比例
	@GridColumn(name="公积金比例",colIdx=3)
	Number gjjBl
	
	//失业保险基数
	@GridColumn(name="失业保险基数",colIdx=4)
	Double sybx
	
	//失业保险比例
	@GridColumn(name="失业保险比例",colIdx=5)
	Number sybxBl
	
	//医疗保险基数
	@GridColumn(name="医疗保险基数",colIdx=6)
	Double ylbx
	
	//医疗保险比例
	@GridColumn(name="医疗老保险比例",colIdx=7)
	Number ylbxBl
	
	//生育保险基数
	@GridColumn(name="生育保险基数",colIdx=8)
	Double syubx
	
	//生育保险比例
	@GridColumn(name="生育保险比例",colIdx=9)
	Number syubxBl
	
	//养老保险基数
	@GridColumn(name="养老保险基数",colIdx=10)
	Double ylaobx
	
	//养老保险比例
	@GridColumn(name="养老保险比例",colIdx=11)
	Number ylaobxBl
	
	@GridColumn(name="姓名",colIdx=1,width="60px",formatter="riskGold_formatTopic")
	def getPersonInforName(){
		return personInfor?.chinaName
	}
	
	static belongsTo = [company:Company]
	
    static constraints = {
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "RS_SA_RISKGOLD"
	}
}
