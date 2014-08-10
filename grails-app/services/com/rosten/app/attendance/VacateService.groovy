package com.rosten.app.attendance

import com.rosten.app.util.GridUtil

class VacateService {
	
	//增加流程日志
	def addFlowLog ={ entity,currentUser,content ->
		def _log = new VacateLog()
		_log.user = currentUser
		_log.vacate = entity
		_log.content = content
		_log.save(flush:true)
	}
	
	def getVacateListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new Vacate())
	}
	
	def getVacateDataStore ={params->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllVacate(offset,max,params.company,params.user)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	
	def getAllVacate ={offset,max,company,user->
		def c = Vacate.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			or{
				readers{
					eq("id",user.id)
				}
			}
			or{
				notEqual("status","已结束")
			}
			or{
				notEqual("status","结束")
			}
			 
		}
		return c.list(pa,query)
	}
	
	def getVacateCount ={company->
		def c = Vacate.createCriteria()
		def query = { eq("company",company) }
		return c.count(query)
	}
	
	
	def getAllAskForDataStore ={params->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllAskFor(offset,max,params.company,params.user)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	
	def getAllAskFor ={offset,max,company,user->
		def c = Vacate.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
		}
		return c.list(pa,query)
	}
	
}
