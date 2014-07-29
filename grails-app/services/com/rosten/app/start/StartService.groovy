package com.rosten.app.start
import com.rosten.app.util.GridUtil
import com.rosten.app.gtask.Gtask

class StartService {
	
	//增加待办事项
	def addGtask ={params->
		def gtask = new Gtask()
		gtask.properties = params
		gtask.clearErrors()
		gtask.save()
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
