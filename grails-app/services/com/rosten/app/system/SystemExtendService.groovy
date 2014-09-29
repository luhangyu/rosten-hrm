package com.rosten.app.system

import com.rosten.app.util.GridUtil

class SystemExtendService {

    def getSystemCodeListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new SystemCode())
	}
	def getSystemCodeListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllSystemCode(offset,max,params.company,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllSystemCode={offset,max,company,searchArgs->
		def c = SystemCode.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			order("code", "desc")
			
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.list(pa,query)
	}
	def getSystemCodeCount ={company,searchArgs->
		def c = SystemCode.createCriteria()
		def query = {
			eq("company",company)
			
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.count(query)
	}
}
