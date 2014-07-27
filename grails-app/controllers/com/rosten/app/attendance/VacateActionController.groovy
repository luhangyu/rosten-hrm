package com.rosten.app.attendance

import grails.converters.JSON
import com.rosten.app.system.User

class VacateActionController {

   	def imgPath ="images/rosten/actionbar/"
	
	def vacateView ={
		def actionList =[]
		def strname = "vacate"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("新增",imgPath + "add.png",strname + "_add")
		actionList << createAction("删除",imgPath + "read.gif",strname + "_delete")
		actionList << createAction("刷新",imgPath + "fresh.gif","freshGrid")
		
		render actionList as JSON
	}
	
	def vacateForm ={
		def webPath = request.getContextPath() + "/"
		def strname = "vacate"
		def actionList = []
		
		actionList << createAction("返回",webPath + imgPath + "quit_1.gif","page_quit")
		actionList << createAction("保存",webPath + imgPath + "Save.gif",strname + "_save")
		
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
