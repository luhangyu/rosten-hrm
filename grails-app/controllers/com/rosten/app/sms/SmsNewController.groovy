package com.rosten.app.sms

import grails.converters.JSON
import com.rosten.app.system.User
import com.rosten.app.system.Sms
import com.rosten.app.system.SmsGroup
import com.rosten.app.staff.PersonInfor
import com.rosten.app.staff.FamilyInfor

class SmsNewController {
	def springSecurityService
	
    def smsSave = {
		def model=[:]
		User user = springSecurityService.getCurrentUser()
		
		if(!user.company.isSmsOn){
			model["result"] = "nouse"
			render model as JSON
			return
		}
		
		def sms = new Sms()
		if(params.id && !"".equals(params.id)){
			sms = Sms.get(params.id)
		}
		sms.sender = user.username
		sms.content = params.content
		sms.company = user.company
		
		def sendtos = []
		if(params.telephone){
			params.telephone.split(",").each{
				sendtos << it
			}
		}
		
		//短消息群组,追加到sendto中
		if(params.smsGroup){
			params.smsGroup.split(",").each{
				def smsGroup = SmsGroup.findByGroupName(it)
				if(smsGroup.members && !"".equals(smsGroup.members)){
					smsGroup.members.each{item ->
						def _user = User.findByChinaName(item)
						if(_user){
							def telephone = _user.telephone
							def personInfor = PersonInfor.findByUser(_user)
							if(personInfor){
								def familyInfor = FamilyInfor.findByPersonInfor(personInfor)
								if(familyInfor && familyInfor.mobile){
									telephone = familyInfor.mobile
								}
							}
							if(telephone){
								sendtos << telephone
							}
						}
					}
				}
			}
		}
		
		sms.sendto = sendtos.join(",")
		if(sms.save(flush:true)){
			//通过短消息接口发送短消息
			if(sms.sendto){
				def smsClient = new AxisClient()
				smsClient.sendPhoneInfo(sms.sendto, "【人事系统】" + sms.content)
			}
			model["result"] = "true"
		}else{
			sms.errors.each{
				println it
			}
			model["result"] = "false"
		}
		render model as JSON
		
	}
}
