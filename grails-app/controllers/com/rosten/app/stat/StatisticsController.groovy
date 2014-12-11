package com.rosten.app.stat

import com.rosten.app.system.Company
import com.rosten.app.system.Depart
import com.rosten.app.util.Util
import grails.converters.JSON
import groovy.sql.Sql
import com.rosten.app.system.UserType
import com.rosten.app.staff.PersonInfor

class StatisticsController {
	def imgPath ="images/rosten/actionbar/"
	def dataSource
	
	def staticShow ={
		def webPath = request.getContextPath() + "/"
		def actionList = []
		
		actionList << createAction("返回",webPath + imgPath + "quit_1.gif","page_quit")
		render actionList as JSON
	}
	
	//2014-12-07增加统计报表中的详情展示-----------------------------------
	def more_staffByCategory ={
		//员工按用工性质统计
		def model=[:]
		def company = Company.get(params.companyId)
		Sql sql = new Sql(dataSource)
		def items = []
		def seleSql = "select * from ygxztj"
		def vacateList = sql.eachRow(seleSql){
			def item = ["departName":it["depart_name"],"xhpy":it["xhpy"],"fp":it["fp"],"jy":it["jy"],"jpzz":it["jpzz"],"jpjz":it["jpjz"],
				"jhtg":it["jhtg"],"gk":it["gk"],"sx":it["sx"],"lwpq":it["lwpq"],"sj":it["sj"],"jlsg":it["jlsg"],"hjrs":it["hjrs"]]
			items<<item
		}
		
		model["tableItem"] = items
		
		render(view:'/statistics/designMore',model:model)
	}
	
	def more_staffByAge ={
		//员工按年龄段统计
		def model=[:]
		def company = Company.get(params.companyId)
		Sql sql = new Sql(dataSource)
		def items = []
		def seleSql = "select * from nldtj"
		def vacateList = sql.eachRow(seleSql){
			def item = ["num":it["num"],"age":it["age"]]
			items<<item
		}
		
		model["tableItem"] = items
		render(view:'/statistics/statisticsByAge',model:model)
		
	}
	
	def more_staffByDepart ={
		//员工人数按部门统计
		def model=[:]
		def company = Company.get(params.companyId)
		Sql sql = new Sql(dataSource)
		def items = []
		def seleSql = "select * from bmrstj"
		def vacateList = sql.eachRow(seleSql){
			def item = ["departName":it["depart_name"],"num":it["num"]]
			items<<item
		}
		
		model["tableItem"] = items
		render(view:'/statistics/statisticsByDepart',model:model)
		
	}
	
	//------------------------------------------------------------
	def getDepartUsersByType ={
		def company = Company.get(params.id)
		def json = [identifier:'id',label:'name',items:[]]
		
		def index = 1
		def groupNames = params.groupNames.split(",")
		
		params.departIds.split(",").each{
			def depart = Depart.get(it)
			
			groupNames.each{item ->
				def lastIndex = Util.obj2str(index).padLeft(3,"0")
				def _number = 0
				
				//获取所有相关部门下的员工信息
				def c = PersonInfor.createCriteria()
				c.list{
					departs{
						eq("id",depart.id)
					}
				}.each{ _user ->
					if(item.equals(_user.userTypeEntity.typeName)){
						_number += 1
					}
				}
				
				if(_number!=0){
					def sMap = ["id":lastIndex,"name":depart.departName,"group":item,"number":_number]
					
					index += 1
					
					json.items+=sMap
				}
			}
		}
		render json as JSON
	}
	
	def getDepartUsers ={
		def company = Company.get(params.id)
		def json = [identifier:'id',label:'name',items:[]]
		Depart.findAllByCompany(company).each{
			
			if(it.parent){
				
				def _depart = it
				def c = PersonInfor.createCriteria()
				def cResult = c.list{
					departs{
						eq("id",_depart.id)
					}
				}
				
				def number = cResult.size()
				if(number && number > 0){
					def sMap = ["id":it.id,"name":it.departName,"parentId":it.parent?.id,"number":number]
					json.items+=sMap
				}
			}
			
		}
		render json as JSON
	}
	
    def payweal ={
		def model =[:]
		def company = Company.get(params.companyId)
		model["company"] = company
		
		//员工编制统计图表中相关信息获取
		def groupList =[]
		UserType.findAllByCompany(company).each{
			groupList << it.typeName
		}
		model["groupList"] = groupList as JSON
		model["groupNames"] = groupList.join(",")
		
		def departList =[]
		def departIds =[]
		
		Depart.findAllByCompany(company).each{
			departList << it.departName
			departIds << it.id
		}
		model["departList"] = departList as JSON
		model["departIds"] = departIds.join(",")
		model["ageChartData"] = this.getUserAgeStatic(company) as JSON
		
		render(view:'/statistics/chart',model:model)
	}
	private def getUserAgeStatic ={company->
		
		def model=[]
		def index = 0,_0_30 = 0,_30_40=0,_40_50=0,_50_60=0,_60_100=0
		
		PersonInfor.findAllByCompany(company).each{
			if(it.idCard){
				def age = this.computeAge(it.idCard)
				
				if(age<=30){
					_0_30 += 1
				}else if(age>30 && age<=40){
					_30_40 += 1
				}else if(age>40 && age<=50){
					_40_50 += 1
				}else if(age>50 && age<=60){
					_50_60 += 1
				}else if(age>60){
					_60_100 += 1
				}
				
				index += 1
			}
		}
		
		def a = [y:_0_30,text:"30(含)以下   " + Util.DoubleToFormat(_0_30/index,2) + "%"]
		def b = [y:_30_40,text:"30-40(含) " + Util.DoubleToFormat(_30_40/index,2) + "%"]
		def c = [y:_40_50,text:"40-50(含) " + Util.DoubleToFormat(_40_50/index,2) + "%"]
		def d = [y:_50_60,text:"50-60(含) " + Util.DoubleToFormat(_50_60/index,2) + "%"]
		def e = [y:_60_100,text:"60以上  " + Util.DoubleToFormat(_60_100/index,2) + "%"]
		
		model +=[a,b,c,d,e]
		
		return model
	}
	private def computeAge ={idCardNumber ->
		if(idCardNumber && (idCardNumber.length()==15 || idCardNumber.length()==18)){
			def year = idCardNumber.substring(6,idCardNumber.length()-8)
			
			Calendar calendar = Calendar.getInstance()
			def currentYear = calendar.get(Calendar.YEAR)
			
			return Util.obj2int(currentYear)-Util.obj2int(year)
		}else{
			return null
		}
	}
	private def createAction={name,img,action->
		def model =[:]
		model["name"] = name
		model["img"] = img
		model["action"] = action
		return model
	}
}
