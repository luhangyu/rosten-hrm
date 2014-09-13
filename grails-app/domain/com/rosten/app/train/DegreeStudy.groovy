package com.rosten.app.train

import com.rosten.app.system.User

import java.text.SimpleDateFormat
import java.util.Date;

import com.rosten.app.system.Company
import com.rosten.app.annotation.GridColumn

class DegreeStudy {

	String id
	
	//用户
	User user
	
	@GridColumn(name="姓名",formatter="degreeStudy_formatTopic",colIdx=1,width="60px")
	def getUserName(){
		return user?.getFormattedName()
	}
	
	//部门名称
	@GridColumn(name="部门",colIdx=2)
	def getFormattedDepartName(){
		if(user!=null){
			return user.getDepartName()
		}else{
			return ""
		}
	}
	
	@GridColumn(name="进修学校",colIdx=3)
	String School
	
	@GridColumn(name="进修专业",colIdx=4)
	String major
	
	@GridColumn(name="攻读学历",colIdx=5)
	String Education
	
	@GridColumn(name="攻读学位",colIdx=6)
	String degree
	
	//开始时间
	Date startDate = new Date()
	
	@GridColumn(name="开始时间",width="106px",colIdx=7)
	def getFormatteStartDate(){
		if(startDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(startDate)
		}else{
			return ""
		}
	}
	
	//结束时间
	Date endDate = new Date()
	
	@GridColumn(name="结束时间",width="106px",colIdx=8)
	def getFormatteEndDate(){
		if(endDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, 1);
			return sd.format(c.getTime())
		}else{
			return ""
		}
	}
	
	@GridColumn(name="进修学费",colIdx=9)
	double tuition
	
    static belongsTo = [user:User,company:Company]
	
    static constraints = {
		
    }
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_TRAIN_DEGREESTUDY"
	}
	
	
}
