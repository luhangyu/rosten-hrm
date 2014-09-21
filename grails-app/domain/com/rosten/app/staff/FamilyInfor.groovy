package com.rosten.app.staff

import com.rosten.app.system.User

//家庭成员
class FamilyInfor {
	
	String id
	
	//姓名
	String name
	
	String relation//成员关系
	
	//移动电话
	String mobile
	
	//工作单位
	String workUnit
	
    static constraints = {
		workUnit nullable:true,blank:true
		mobile nullable:true,blank:true
    }
	
	static belongsTo = [personInfor:PersonInfor]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_FAMILYINFOR"
	}
}
