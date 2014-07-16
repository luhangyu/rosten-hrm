package com.rosten.app.stat

import com.rosten.app.system.Company
import com.rosten.app.util.Util
import grails.converters.JSON

class StatisticsController {

    def payweal ={
		def model =[:]
		model["company"] = Company.get(params.companyId)
		render(view:'/statistics/chart',model:model)
	}
}
