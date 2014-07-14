package com.rosten.app.train

import grails.converters.JSON
import com.rosten.app.system.User

class TrainActionController {

	def imgPath ="images/rosten/actionbar/"
	
	def trainMessageView ={
		def actionList =[]
		def strname = "trainMessage"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("新增",imgPath + "add.png",strname + "_add")
		actionList << createAction("删除",imgPath + "read.gif",strname + "_delete")
		actionList << createAction("刷新",imgPath + "fresh.gif","freshGrid")
		
		render actionList as JSON
	}
	
	def trainMessageForm ={
		def webPath = request.getContextPath() + "/"
		def strname = "trainMessage"
		def actionList = []
		
		actionList << createAction("返回",webPath + imgPath + "quit_1.gif","page_quit")
		actionList << createAction("保存",webPath + imgPath + "Save.gif",strname + "_save")
		
		render actionList as JSON
	}
	
	def trainCourseForm ={
		def webPath = request.getContextPath() + "/"
		def strname = "trainCourse"
		def actionList = []
		
		actionList << createAction("返回",webPath + imgPath + "quit_1.gif","page_quit")
		actionList << createAction("保存",webPath + imgPath + "Save.gif",strname + "_save")
		
		render actionList as JSON
	}
	def trainCourseView ={
		def actionList =[]
		def strname = "trainCourse"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("新增培训班",imgPath + "add.png",strname + "_add")
		actionList << createAction("删除培训班",imgPath + "read.gif",strname + "_delete")
		actionList << createAction("刷新",imgPath + "fresh.gif","freshGrid")
		
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
