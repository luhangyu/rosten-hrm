package com.rosten.app.salary

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company
import com.rosten.app.system.User;
import com.rosten.app.staff.PersonInfor
class SalarySlip {

	String id
	
	//员工
	PersonInfor personInfor
	
	@GridColumn(name="年度",colIdx=2)
	String year
	
	@GridColumn(name="月份",colIdx=3)
	String month
	
	@GridColumn(name="岗位工资",colIdx=5,width="50px")
	Double ygwgz
	
	//月绩效工资
	@GridColumn(name="绩效工资",colIdx=6,width="50px")
	Double yjxgz
	
	//工龄补贴
	@GridColumn(name="工龄补贴",colIdx=7,width="50px")
	Double glbt
	
	//工资小计
	@GridColumn(name="工资小计",colIdx=8,width="50px")
	Double gzxj
	
	//租房补贴
	@GridColumn(name="租房补贴",colIdx=9,width="50px")
	Double zfbt
	
	//考核奖
	@GridColumn(name="考核奖",colIdx=10)
	Double khj
	
	//应发金额
	@GridColumn(name="应发",colIdx=11,width="60px")
	Double yfje
	
	//个税
	@GridColumn(name="个税",colIdx=12)
	Double grss
	
	//公积金
	@GridColumn(name="公积金",colIdx=13)
	Double gjj
	
	//失业保险
	@GridColumn(name="失业",colIdx=14,width="50px")
	Double sybx
	
	//养老保险
	@GridColumn(name="养老",colIdx=15,width="50px")
	Double ylaobx

	//医疗保险
	@GridColumn(name="医疗",colIdx=16,width="50px")
	Double ylbx
	
	//重补
	@GridColumn(name="重补",colIdx=18)
	Double cb
	
	//五险一金小计
	@GridColumn(name="社保小计",colIdx=19)
	Double wxyjxj

	
	//实发小计
	@GridColumn(name="实发",colIdx=20,width="60px")
	Double sfje
		
	@GridColumn(name="姓名",colIdx=1,width="50px")
	def getPersonInforName(){
		return personInfor?.chinaName
	}
	
	static constraints = {
		
		yjxgz nullable:true,blank:true
		ygwgz nullable:true,blank:true
		glbt nullable:true,blank:true
		
		gzxj nullable:true,blank:true
		zfbt nullable:true,blank:true
		khj nullable:true,blank:true
		
		yfje nullable:true,blank:true
		grss nullable:true,blank:true
		gjj nullable:true,blank:true
		
		sybx nullable:true,blank:true
		ylaobx nullable:true,blank:true
		ylbx nullable:true,blank:true
		
		cb nullable:true,blank:true
		wxyjxj nullable:true,blank:true
		sfje nullable:true,blank:true
		
		year nullable:true,blank:true
		month nullable:true,blank:true
		
	}
	
	static belongsTo = [company:Company]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "RS_SA_SASlip"
	}
}
