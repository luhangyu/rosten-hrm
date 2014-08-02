package com.rosten.app.publicc

import com.rosten.app.util.GridUtil

class PubliccService {
	
	def getDownloadFileListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new DownLoadFile())
	}
	
	def getDownloadFileListDataStore ={params->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllDownloadFile(offset,max,params.company)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllDownloadFile={offset,max,company->
		def c = DownLoadFile.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			order("number", "asc")
		}
		return c.list(pa,query)
	}
	def getDownloadFileCount ={company->
		def c = DownLoadFile.createCriteria()
		def query = {
			eq("company",company)
			order("number", "asc")
		}
		return c.count(query)
	}
	
    def serviceMethod() {

    }
}
