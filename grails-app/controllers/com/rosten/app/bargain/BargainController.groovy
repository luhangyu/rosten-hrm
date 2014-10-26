package com.rosten.app.bargain

class BargainController {
	def springSecurityService
	
   	def getBargainPerson={
		def model =[:]
		def currentUser = springSecurityService.getCurrentUser()
		def company = currentUser.company
		model["company"] = company
		
		render(view:'/staff/bargainPerson',model:model)
		
	}
}
