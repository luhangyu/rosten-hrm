package com.rosten.app.publicc

import grails.converters.JSON
import com.rosten.app.system.User

class PubliccActionController {
	def imgPath ="images/rosten/actionbar/"
	
	def downloadFileForm ={
		def webPath = request.getContextPath() + "/"
		def actionList = []
		actionList << createAction("返回",webPath + imgPath + "quit_1.gif","page_quit")
		
		def user = User.get(params.userid)
		if(params.id){
			def downloadFile = DownLoadFile.get(params.id)
			if(user.equals(downloadFile.publisher)){
				//当前处理人
				actionList << createAction("保存",webPath + imgPath + "Save.gif","downloadFile_add")
			}
		}else{
			//新建
			actionList << createAction("保存",webPath + imgPath + "Save.gif","downloadFile_add")
		}
		render actionList as JSON
	}
	def downloadFileView ={
		def actionList =[]
		def strname = "downloadFile"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		
		def user = User.get(params.userId)
		if("admin".equals(user.getUserType())){
			actionList << createAction("添加",imgPath + "add.png","add_" + strname)
			actionList << createAction("查看",imgPath + "read.gif","read_" + strname)
			actionList << createAction("修改",imgPath + "ok.png","change_" + strname)
			actionList << createAction("删除",imgPath + "delete.png","delete_" + strname)
		}else{
			actionList << createAction("查看",imgPath + "read.gif","read_" + strname)
		}
		
		actionList << createAction("刷新",imgPath + "fresh.gif","freshGrid")
		
		render actionList as JSON
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
