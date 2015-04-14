package com.rosten.app.travel

import com.rosten.app.util.GridUtil
import com.rosten.app.share.ShareService

class TravelService {
	def shareService
	def getTravelCountByUser ={company,user,searchArgs->
		def c = TravelApp.createCriteria()
		def query = {
			eq("company",company)
			eq("currentUser",user)
			order("createDate", "desc")
			
			searchArgs.each{k,v->
				if(k.equals("travelDate")){
					and{
						le("startDate",v)
						ge("endDate",v)
					}
				}else{
					like(k,"%" + v + "%")
				}
			}
			
		}
		return c.count(query)
	}
	def getTravelListDataStoreByUser ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllTravelByUser(offset,max,params.company,params.user,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	def getAllTravelByUser ={offset,max,company,user,searchArgs->
		def c = TravelApp.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			eq("currentUser",user)
			order("createDate", "desc")
			
			searchArgs.each{k,v->
				if(k.equals("travelDate")){
					and{
						le("startDate",v)
						ge("endDate",v)
					}
				}else{
					like(k,"%" + v + "%")
				}
			}
		}
		return c.list(pa,query)
	}
	
	def getTravelListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new TravelApp())
	}
	def getTravelListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllTravel(offset,max,params.company,params.user,params.departs,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	def getAllTravel ={offset,max,company,user,departs,searchArgs->
		def c = TravelApp.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			order("createDate", "desc")
			
			if(!shareService.checkAdmin(user,"应用管理员") && !shareService.checkAdmin(user,"出差管理员")){	//管理员可以查看所有数据
				if(departs && departs.size()>0){
					'in'("drafterDepart",departs)
				}else{
					readers{
						eq("id",user.id)
					}
				}
			}
			
			searchArgs.each{k,v->
				if(k.equals("travelDate")){
					and{
						le("startDate",v)
						ge("endDate",v)
					}
				}else{
					like(k,"%" + v + "%")
				}
			}
		}
		return c.list(pa,query)
	}
	def getTravelCount ={company,user,departs,searchArgs->
		def c = TravelApp.createCriteria()
		def query = {
			eq("company",company)
			
			if(!shareService.checkAdmin(user,"应用管理员") && !shareService.checkAdmin(user,"出差管理员")){	//管理员可以查看所有数据
				if(departs && departs.size()>0){
					'in'("drafterDepart",departs)
				}else{
					readers{
						eq("id",user.id)
					}
				}
			}
			searchArgs.each{k,v->
				if(k.equals("travelDate")){
					and{
						le("startDate",v)
						ge("endDate",v)
					}
				}else{
					like(k,"%" + v + "%")
				}
			}
			
		}
		return c.count(query)
	}
}
