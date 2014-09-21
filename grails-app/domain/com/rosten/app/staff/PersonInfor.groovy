package com.rosten.app.staff

import com.rosten.app.system.User
import com.rosten.app.system.UserType;
import com.rosten.app.system.Depart
import com.rosten.app.system.Company

import java.text.SimpleDateFormat
import java.util.List;

//个人概况
class PersonInfor {
	
	String id
	
	String chinaName
	
	//身份证号
	String idCard
	
	//性别
	String sex = "男"
	
	//出生日期
	Date birthday
	
	def getFormatteBirthday(){
		if(birthday!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(birthday)
		}else{
			return ""
		}
	}
	
	//出生地
	String birthAddress
	
	//籍贯
	String nativeAddress
	
	//曾用名
	String usedName
	
	//民族
	String nationality = "汉族"
	
	//国籍
	String city = "中国"
	
	//婚姻状况
	String marriage = "未婚"
	
	//健康状况
	String health = "良好"
	
	//政治面貌
	String politicsStatus = "党员"

	//宗教信仰
	String religion = "无"
	
	//血型
	String blood
	
	//状态
	String status = "在职"
	
	//用户类型
	UserType userTypeEntity
	
	User user
	
	List departs
	static hasMany=[departs:Depart]
	
	//所属单位
	static belongsTo = [company:Company]
	
    static constraints = {
		birthday nullable:true,blank:true
		birthAddress nullable:true,blank:true
		nativeAddress nullable:true,blank:true
		usedName nullable:true,blank:true
		blood nullable:true,blank:true
		user nullable:true,blank:true
    }
	
	def beforeDelete(){
		
	}
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_PERSONINFOR"
	}
}
