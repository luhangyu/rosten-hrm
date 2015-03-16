package com.rosten.app.travel

import com.rosten.app.util.GridUtil

class TravelService {
	def getTravelCountByUser ={company,user,searchArgs->
		def c = TravelApp.createCriteria()
		def query = {
			eq("company",company)
//			eq("currentUser",user)
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
//			eq("currentUser",user)
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
		def propertyList = getAllTravel(offset,max,params.company,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	def getAllTravel ={offset,max,company,searchArgs->
		def c = TravelApp.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
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
	def getTravelCount ={company,searchArgs->
		def c = TravelApp.createCriteria()
		def query = {
			eq("company",company)
			
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
