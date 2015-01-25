package com.rosten.app.salary

import com.rosten.app.staff.PersonInfor
import com.rosten.app.system.Company
import com.rosten.app.annotation.GridColumn

/*
 * 工资配置表
 */
class SalaryBillConfig {
	String id
	
	//员工
	PersonInfor personInfor
	
	@GridColumn(name="姓名",colIdx=1,width="60px",formatter="billConfig_formatTopic")
	def getPersonInforName(){
		return personInfor?.chinaName
	}
	
	//基础类型：绩效工资-岗位工资-五险一金
	Radix radix
	
	//岗位级别
	Quarters quarters
	
	@GridColumn(name="职位",colIdx=2)
	def getQuaName(){
		return quarters?.quaName
	}
	
	//档位
	Gear gear
	
	@GridColumn(name="档位",colIdx=3)
	def getGearName(){
		return gear?.gearName
	}
	
	//公积金基数
	@GridColumn(name="公积金基数",colIdx=4)
	Double gjj
	
	//公积金比例
	@GridColumn(name="公积金比例",colIdx=5)
	Number gjjBl
	
	//失业保险基数
	@GridColumn(name="失业保险基数",colIdx=6)
	Double sybx
	
	//失业保险比例
	@GridColumn(name="失业保险比例",colIdx=7)
	Number sybxBl
	
	//医疗保险基数
	@GridColumn(name="医疗保险基数",colIdx=8)
	Double ylbx
	
	//医疗保险比例
	@GridColumn(name="医疗保险比例",colIdx=9)
	Number ylbxBl
	
	//生育保险基数
	@GridColumn(name="生育保险基数",colIdx=10)
	Double syubx
	
	//生育保险比例
	@GridColumn(name="生育保险比例",colIdx=11)
	Number syubxBl
	
	//养老保险基数
	@GridColumn(name="养老保险基数",colIdx=12)
	Double ylaobx
	
	//养老保险比例
	@GridColumn(name="养老保险比例",colIdx=13)
	Number ylaobxBl
	
    static constraints = {
		
		quarters nullable:true,blank:true
		gear nullable:true,blank:true
		radix nullable:true,blank:true
		gjj nullable:true,blank:true
		gjjBl nullable:true,blank:true
		sybx nullable:true,blank:true
		sybxBl nullable:true,blank:true
		ylbx nullable:true,blank:true
		ylbxBl nullable:true,blank:true
		syubx nullable:true,blank:true
		syubxBl nullable:true,blank:true
		ylaobxBl nullable:true,blank:true
		ylaobx nullable:true,blank:true
    }
	
	static belongsTo = [company:Company]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "RS_SA_BILLCONFIG"
	}
}
