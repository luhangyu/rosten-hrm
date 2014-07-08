package com.rosten.app.train

import com.rosten.app.util.GridUtil

class TrainService {

	def getTrainCourseListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new TrainCourse())
	}
	def getTrainCourseListDataStore ={params->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllTrainCourse(offset,max,params.company)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	def getAllTrainCourse ={offset,max,company->
		def c = TrainCourse.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			order("createDate", "desc")
		}
		return c.list(pa,query)
	}
	def getTrainCourseCount ={company->
		def c = TrainCourse.createCriteria()
		def query = { eq("company",company) }
		return c.count(query)
	}
	
}
