package com.rosten.app.bbs

import com.rosten.app.util.GridUtil

class BbsService {
	
	def getBbsListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new Bbs())
	}
	def getBbsListDataStoreByUser ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllBbsByUser(offset,max,params.company,params.user,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllBbsByUser={offset,max,company,user,searchArgs->
		def c = Bbs.createCriteria()
		def pa=[max:max,offset:offset]
		def query = { 
			eq("company",company) 
//			eq("currentUser",user)
			
			readers{
				eq("id",user.id)
			}
			
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
			
			order("createDate", "desc")
		}
		return c.list(pa,query)
	}
	def getBbsCountByUser ={company,user,searchArgs->
		def c = Bbs.createCriteria()
		def query = { 
			eq("company",company) 
//			eq("currentUser",user)
			
			readers{
				eq("id",user.id)
			}
			
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
			
			order("createDate", "desc")
		}
		return c.count(query)
	}
	def getBbsListDataStoreByNew ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllBbsByNew(offset,max,params.company,params.user,params.showDays,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllBbsByNew={offset,max,company,user,showDays,searchArgs->
		def c = Bbs.createCriteria()
		def pa=[max:max,offset:offset]
		def now = new Date()
		def query = {
			eq("company",company)
			or{
				//defaultReaders为：*或者【角色】或者readers中包含当前用户的均有权访问
//				readers{
//					eq("id",user.id)
//				}
				like("defaultReaders", "%all%")
			}
//			between("publishDate",now-showDays,now)
			order("createDate", "desc")
			
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.list(pa,query).unique()
	}
	def getBbsCountByNew ={company,user,showDays,searchArgs->
		def c = Bbs.createCriteria()
		def now = new Date()
		def query = {
			eq("company",company)
			or{
				//defaultReaders为：*或者【角色】或者readers中包含当前用户的均有权访问
//				readers{
//					eq("id",user.id)
//				}
				like("defaultReaders", "%all%")
			}
//			between("publishDate",now-showDays,now)
			order("createDate", "desc")
			
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.count(query)
	}
	def getBbsListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllBbs(offset,max,params.company,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllBbs={offset,max,company,searchArgs->
		def c = Bbs.createCriteria()
		def pa=[max:max,offset:offset]
		def query = { 
			eq("company",company)
			order("createDate", "desc")
			
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.list(pa,query)
	}
	def getBbsCount ={company,searchArgs->
		def c = Bbs.createCriteria()
		def query = {
			eq("company",company)
			order("createDate", "desc")
			
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.count(query)
	}
    def serviceMethod() {

    }
}
