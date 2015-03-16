package com.rosten.app.travel

import grails.converters.JSON
import com.rosten.app.system.User
import com.rosten.app.share.ShareService

class TravelActionController {
	def imgPath ="images/rosten/actionbar/"
	def strname = "travel"
	def shareService
	
	def travelConfigView ={
		def actionList =[]
		def strname = "travelConfig"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("保存",imgPath + "Save.gif",strname + "_save")
		
		render actionList as JSON
	}
	
	def travelForm ={
		def webPath = request.getContextPath() + "/"
		def actionList =[]
		
		actionList << createAction("返回",webPath + imgPath + "quit_1.gif","page_quit")
		
		def user = User.get(params.userid)
		if(params.id){
			def entity = TravelApp.get(params.id)
			
			//判断是否出现保存功能
			def isChange = shareService.checkPemission(user,entity,"管理员","员工申请")
			if(isChange){
				actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_add")
			}
			
			if(user.equals(entity.currentUser)){
				//当前处理人
				switch (true){
					case entity.status.contains("拟稿"):
						actionList << createAction("提交",webPath +imgPath + "submit.png",strname + "_submit")
						break;   
					case entity.status.contains("审核") || entity.status.contains("审批"):
						actionList << createAction("填写意见",webPath +imgPath + "sign.png","addComment")
						actionList << createAction("同意",webPath +imgPath + "ok.png",strname + "_submit")
						actionList << createAction("退回",webPath +imgPath + "back.png",strname + "_back")
						break;
					case entity.status.contains("已签发")|| entity.status.contains("已发布"):
						actionList << createAction("填写意见",webPath +imgPath + "sign.png","addComment")
						actionList << createAction("提交",webPath +imgPath + "submit.png",strname +"_submit")
						break;
					case entity.status.contains("归档"):
						actionList << createAction("填写意见",webPath +imgPath + "sign.png","addComment")
						actionList << createAction("归档",webPath +imgPath + "gd.png",strname +"_submit")
						break;
					default :
						actionList << createAction("提交",webPath +imgPath + "submit.png",strname + "_submit")
						break;
						
				}
			}
		}else{
			//新增
			actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_add")
		}
		
		render actionList as JSON
	}
	
	def allTravelView ={
		def actionList =[]
		def strname = "travel"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
//		actionList << createAction("查看出差通知",imgPath + "read.gif","read_" + strname)
		
		def user = User.get(params.userId)
		if(shareService.checkAdmin(user,"管理员")){
			actionList << createAction("删除出差申请",imgPath + "delete.png","delete_" + strname)
			actionList << createAction("状态迁移",imgPath + "changeStatus.gif",strname + "_changeStatus")
			actionList << createAction("用户迁移",imgPath + "changeUser.gif",strname + "_changeUser")
		}
		
		actionList << createAction("刷新",imgPath + "fresh.gif","freshGrid")
		
		render actionList as JSON
	}
	
	def travelView = {
		def actionList =[]
		
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("新增出差申请",imgPath + "add.png","add_"+ strname)
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
