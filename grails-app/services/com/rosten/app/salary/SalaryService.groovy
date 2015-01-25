package com.rosten.app.salary

import com.rosten.app.util.GridUtil

class SalaryService {
	
	def getQuartersListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new Quarters())
	}
	def getQuartersListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllQuarters(offset,max,params.company,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllQuarters={offset,max,company,searchArgs->
		def c = Quarters.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.list(pa,query)
	}
	
	def getQuarters ={company,searchArgs->
		def c = Quarters.createCriteria()
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.count(query)
	}
	
	
	def getGearListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new Gear())
	}
	def getGearListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllGear(offset,max,params.company,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllGear={offset,max,company,searchArgs->
		def c = Gear.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.list(pa,query)
	}
	
	def getGear ={company,searchArgs->
		def c = Gear.createCriteria()
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.count(query)
	}
	
	def getRadixListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new Radix())
	}
	def getRadixListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllRadix(offset,max,params.company,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllRadix={offset,max,company,searchArgs->
		def c = Radix.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.list(pa,query)
	}
	
	def getRadix ={company,searchArgs->
		def c = Radix.createCriteria()
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.count(query)
	}
	
	def getRiskGoldListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new RiskGold())
	}
	def getRiskGoldListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllRiskGold(offset,max,params.company,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllRiskGold={offset,max,company,searchArgs->
		def c = RiskGold.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				if(k.equals("chinaName")){
					createAlias('personInfor', 'a')
					like("a.chinaName","%" + v + "%")
					
				}else{
					like(k,"%" + v + "%")
				}
			}
		}
		return c.list(pa,query)
	}
	
	def getRiskGoldCount ={company,searchArgs->
		def c = RiskGold.createCriteria()
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				if(k.equals("chinaName")){
					createAlias('personInfor', 'a')
					like("a.chinaName","%" + v + "%")
					
				}else{
					like(k,"%" + v + "%")
				}
			}
		}
		return c.count(query)
	}
	
	
	def getBillConfigListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new SalaryBillConfig())
	}
	
	def getBillConfigListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAlltBillConfig(offset,max,params.company,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	
	private def getAlltBillConfig={offset,max,company,searchArgs->
		def c = SalaryBillConfig.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				if(k.equals("chinaName")){
					createAlias('personInfor', 'a')
					like("a.chinaName","%" + v + "%")
					
				}else{
					like(k,"%" + v + "%")
				}
			}
		}
		return c.list(pa,query)
	}
	
	def getBillConfigCount ={company,searchArgs->
		def c = SalaryBillConfig.createCriteria()
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				if(k.equals("chinaName")){
					createAlias('personInfor', 'a')
					like("a.chinaName","%" + v + "%")
					
				}else{
					like(k,"%" + v + "%")
				}
			}
		}
		return c.count(query)
	}
}
