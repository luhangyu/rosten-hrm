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
		
		def user = User.get(params.userid)
		if(params.id){
			def vacate = Vacate.get(params.id)
			if(user.equals(vacate.currentUser)){
				//当前处理人
				switch (true){
					case vacate.status.contains("新建"):
						actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_save")
						actionList << createAction("提交",webPath +imgPath + "submit.png",strname + "_submit")
						break;
					case vacate.status.contains("审核") || vacate.status.contains("审批"):
						actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_save")
						actionList << createAction("填写意见",webPath +imgPath + "sign.png",strname + "_addComment")
						actionList << createAction("同意",webPath +imgPath + "ok.png",strname + "_submit")
						actionList << createAction("不同意",webPath +imgPath + "back.png",strname + "_submit")
						break;
					case vacate.status.contains("归档"):
						actionList << createAction("保存",webPath +imgPath + "Save.gif",strname +"_save")
						actionList << createAction("填写意见",webPath +imgPath + "sign.png",strname + "_addComment")
						actionList << createAction("归档",webPath +imgPath + "gd.png",strname +"_submit")
						break;
					default :
						actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_save")
						actionList << createAction("提交",webPath +imgPath + "submit.png",strname + "_submit")
						break;
				}
			}
		}else{
			actionList << createAction("保存",webPath + imgPath + "Save.gif",strname + "_save")
		}
		
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
