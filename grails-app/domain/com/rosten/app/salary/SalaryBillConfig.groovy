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
	
	//基础类型：绩效工资-岗位工资-五险一金
	Radix radix
	
	//岗位级别
	Quarters quarters
	
	//档位
	Gear gear
	
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
	
    static constraints = {
		
		quarters nullable:true,blank:true
		gear nullable:true,blank:true
		
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
