package com.rosten.app.bbs

import grails.converters.JSON
import com.rosten.app.system.User

class BbsActionController {
	
	def imgPath ="images/rosten/actionbar/"
	
	def bbsForm ={
		def webPath = request.getContextPath() + "/"
		def actionList = []
		def strname = "bbs"
		actionList << createAction("返回",webPath + imgPath + "quit_1.gif","page_quit")
		
		def user = User.get(params.userid)
		if(params.id){
			def bbs = Bbs.get(params.id)
			if(user.equals(bbs.currentUser)){
				//当前处理人
				switch (true){
					case bbs.status.contains("起草"):
						actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_add")
						actionList << createAction("提交",webPath +imgPath + "submit.png",strname + "_submit")
						break;
					case bbs.status.contains("审核") || bbs.status.contains("审批"):
						actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_add")
						actionList << createAction("填写意见",webPath +imgPath + "sign.png",strname + "_addComment")
						actionList << createAction("同意",webPath +imgPath + "ok.png",strname + "_submit")
						actionList << createAction("不同意",webPath +imgPath + "back.png",strname + "_submit")
						break;
					case bbs.status.contains("已发布"):
						actionList << createAction("保存",webPath +imgPath + "Save.gif",strname +"_add")
						actionList << createAction("填写意见",webPath +imgPath + "sign.png",strname + "_addComment")
						actionList << createAction("提交归档",webPath +imgPath + "gd.png",strname +"_submit")
						break;
					case bbs.status.contains("归档"):
						actionList << createAction("保存",webPath +imgPath + "Save.gif",strname +"_add")
						actionList << createAction("填写意见",webPath +imgPath + "sign.png",strname + "_addComment")
						actionList << createAction("归档",webPath +imgPath + "gd.png",strname +"_submit")
						break;
					default :
						actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_add")
						actionList << createAction("提交",webPath +imgPath + "submit.png",strname + "_submit")
						break;
						
				}
				
			}
		}else{
			//新建公告
			actionList << createAction("保存",webPath + imgPath + "Save.gif","bbs_add")
		}
		render actionList as JSON
	}
	
	def bbsConfigView = {
		def actionList =[]
		def strname = "bbsConfig"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("保存",imgPath + "Save.gif",strname + "_save")
		
		render actionList as JSON
	}
	def allbbsView ={
		def actionList =[]
		def strname = "bbs"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("查看公告",imgPath + "read.gif","read_" + strname)
		
		def user = User.get(params.userId)
		if("admin".equals(user.getUserType())){
			actionList << createAction("删除公告",imgPath + "delete.png","delete_" + strname)
			actionList << createAction("状态迁移",imgPath + "changeStatus.gif",strname + "_changeStatus")
			actionList << createAction("用户迁移",imgPath + "changeUser.gif",strname + "_changeUser")
		}
		
		actionList << createAction("刷新",imgPath + "fresh.gif","freshGrid")
		
		render actionList as JSON
	}
	def newbbsView ={
		def actionList =[]
		def strname = "bbs"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("查看公告",imgPath + "read.gif","read_" + strname)
		actionList << createAction("刷新",imgPath + "fresh.gif","freshGrid")
		
		render actionList as JSON
	}
	
	def bbsView = {
		def actionList =[]
		def strname = "bbs"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("新建公告",imgPath + "add.png","add_"+ strname)
		actionList << createAction("查看公告",imgPath + "read.gif","read_" + strname)
		
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

    def index() { }
}
