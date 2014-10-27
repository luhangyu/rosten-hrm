package com.rosten.app.bargain

import grails.converters.JSON
import com.rosten.app.system.User

class BargainActionController {
	def imgPath ="images/rosten/actionbar/"
	
    def bargainConfigView = {
		def actionList =[]
		def strname = "bargainConfig"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("保存",imgPath + "Save.gif",strname + "_save")
		
		render actionList as JSON
	}
	private def createAction={name,img,action->
		def model =[:]
		model["name"] = name
		model["img"] = img
		model["action"] = action
		return model
	}
}
