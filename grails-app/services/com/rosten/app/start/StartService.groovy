package com.rosten.app.start
import com.rosten.app.util.GridUtil
import com.rosten.app.gtask.Gtask
import com.rosten.app.sms.*
import com.rosten.app.staff.PersonInfor
import com.rosten.app.staff.FamilyInfor

class StartService {
	
	//增加待办事项
	def addGtask ={params->
		def gtask = new Gtask()
		gtask.properties = params
		gtask.clearErrors()
		gtask.save()
		
		//增加短消息发送功能，只发单个人信息
		if(params.company.isSmsOn){
			def telephone = params.user.telephone
			def personInfor = PersonInfor.findByUser(params.user)
			if(personInfor){
				def familyInfor = FamilyInfor.findByPersonInfor(personInfor)
				if(familyInfor && familyInfor.mobile){
					telephone = familyInfor.mobile
				}
			}
			if(telephone){
				
				//2015-4-13--------------测试时期，暂时关闭短消息发送
//				def smsClient = new AxisClient()
//				smsClient.sendPhoneInfo(telephone, "【人事系统】" + params.content)
			}
		}
	}
	
	def getGtaskListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new Gtask())
	}
	def getGtaskListDataStore ={params->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllGtask(offset,max,params.company,params.user)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	def getAllGtask ={offset,max,company,user->
		def c = Gtask.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			eq("user",user)
			eq("status","0")
			order("createDate", "desc")
		}
		return c.list(pa,query)
	}
	def getGtaskCount ={company,user->
		def c = Gtask.createCriteria()
		def query = { 
			eq("company",company)
			eq("user",user)
			eq("status","0")
			order("createDate", "desc")
		}
		return c.count(query)
	}
    def serviceMethod() {

    }
}
