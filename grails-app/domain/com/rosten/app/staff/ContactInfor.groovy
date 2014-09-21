package com.rosten.app.staff

import com.rosten.app.system.User

//通讯方式
class ContactInfor {
	
	String id
	
	//固定电话
	String phone
	
	//移动电话
	String mobile
	
	//移动电话2
	String mobile2
	
	//通讯地址
	String address
	
	//通讯地址邮编
	String addressPostcode

	//家庭地址
	String homeAddress
	
	//邮编
	String postcode
	
	//电子邮件
	String email
	
	//qq
	String qq
	
	//微信
	String wechat
	
    static constraints = {
		phone nullable:true,blank:true
		mobile nullable:true,blank:true
		mobile2 nullable:true,blank:true
		address nullable:true,blank:true
		addressPostcode nullable:true,blank:true
		homeAddress nullable:true,blank:true
		postcode nullable:true,blank:true
		email nullable:true,blank:true
    }
	
	static belongsTo = [personInfor:PersonInfor]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_CONTACTINFOR"
	}
}
