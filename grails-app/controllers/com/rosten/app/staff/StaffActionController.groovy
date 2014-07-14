package com.rosten.app.staff

import grails.converters.JSON

class StaffActionController {
	def imgPath ="images/rosten/actionbar/"
	
    def userView ={
		def actionList =[]
		def strname = "personInfor"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("添加",imgPath + "add.png","add_" + strname)
		actionList << createAction("查看",imgPath + "read.gif","read_" + strname)
		actionList << createAction("删除",imgPath + "delete.png","delete_" + strname)
		actionList << createAction("更改密码",imgPath + "changePassword.gif",strname + "_changePassword" )
		render actionList as JSON
	}
	private def createCommonAction={actionList,strname,args->
		if(!(actionList && actionList instanceof List)){
			actionList =[]
		}
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("添加",imgPath + "add.png","add_" + strname)
		actionList << createAction("查看",imgPath + "read.gif","read_" + strname)
		
		if(args){
			//允许修改，删除操作
			actionList << createAction("修改",imgPath + "ok.png","change_" + strname)
			actionList << createAction("删除",imgPath + "delete.png","delete_" + strname)
		}
		actionList << createAction("刷新",imgPath + "fresh.gif","freshGrid")
		return actionList
	}
	private def createAction={name,img,action->
		def model =[:]
		model["name"] = name
		model["img"] = img
		model["action"] = action
		return model
	}
}
