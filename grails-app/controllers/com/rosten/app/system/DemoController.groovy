package com.rosten.app.system

import grails.converters.JSON

class DemoController {
	def imgPath ="images/rosten/actionbar/"
	
	def desgine ={
		def actionList =[]
		
		actionList << createAction("返回",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("保存",imgPath + "add.png", "demo_staticDesign")
		
		render actionList as JSON
	}
	def staticDesign ={
		def actionList =[]
		
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("报表设计",imgPath + "add.png", "demo_staticDesign")
		actionList << createAction("删除",imgPath + "delete.png","demo")
		actionList << createAction("刷新",imgPath + "fresh.gif","demo")
		
		render actionList as JSON
	}
	def demo ={
		def model=[:]
		model.type = params.type
		render(view:'/demo/' + params.type,model:model)
	}
	
    def index() { }
	
	private def createAction={name,img,action->
		def model =[:]
		model["name"] = name
		model["img"] = img
		model["action"] = action
		return model
	}
}
