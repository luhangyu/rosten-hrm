package com.rosten.app.staff

import com.rosten.app.util.GridUtil

class StaffService {

    def getFamilyInforListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new FamilyInfor())
	}
	def getFamilyInforListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllFamilyInfor(offset,max,params.personInfor,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllFamilyInfor={offset,max,personInfor,searchArgs->
		def c = FamilyInfor.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("personInfor",personInfor)
//			order("serialNo", "asc")
			
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.list(pa,query)
	}
	def getFamilyInforCount ={personInfor,searchArgs->
		def c = FamilyInfor.createCriteria()
		def query = {
			eq("personInfor",personInfor)
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.count(query)
	}
}
