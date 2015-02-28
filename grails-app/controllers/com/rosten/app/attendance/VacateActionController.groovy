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
		actionList << createAction("删除",imgPath + "delete.png",strname + "_delete")
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
			
			//判断是否出现保存功能
			def isChange = false
			if("admin".equals(user.getUserType())){
				//管理员
				isChange = true
			}else if(user.getAllRolesValue().contains("请假管理员")){
				//拥有对应角色
				isChange = true
			}else if(user.equals(vacate.currentUser) && vacate.status.equals("新增")){
				isChange = true
			}
			
			if(isChange){
				actionList << createAction("保存",webPath + imgPath + "Save.gif",strname + "_save")
			}
			
			if(user.equals(vacate.currentUser) && !vacate.status.equals("已结束")){
				//当前处理人
				switch (true){
					case vacate.status.contains("新增"):
						actionList << createAction("提交",webPath +imgPath + "submit.png",strname + "_submit")
						break;
					case vacate.status.contains("审核") || vacate.status.contains("审批"):
						actionList << createAction("填写意见",webPath +imgPath + "sign.png",strname + "_addComment")
						actionList << createAction("同意",webPath +imgPath + "ok.png",strname + "_submit")
						actionList << createAction("退回",webPath +imgPath + "back.png",strname + "_back")
						break;
					case vacate.status.contains("已签发"):
						actionList << createAction("销假,结束流程",webPath +imgPath + "submit.png",strname +"_submit")
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
	
	//--------2015-2-28--------增加按月统计统计----------------------------------
	def staticByMonth ={
		def actionList =[]
		def strname = "vacate"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("统计分析",imgPath + "search.gif",strname + "_staticByAll")
		actionList << createAction("打印",imgPath + "word_print.png",strname + "_print")
		
		render actionList as JSON
	}
	//----------------------------------------------------------------
	def askForStatic ={
		def actionList =[]
		def strname = "vacate"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("按月统计",imgPath + "search.gif",strname + "_staticByMonth")
		
		render actionList as JSON
	}
	
	private def createAction={name,img,action->
		def model =[:]
		model["name"] = name
		model["img"] = img
		model["action"] = action
		return model
	}
	
	def allAskForView ={
		def actionList =[]
		def strname = "vacate"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		
		def user = User.get(params.userId)
		def isChange = false
		if("admin".equals(user.getUserType())){
			//管理员
			isChange = true
		}else if(user.getAllRolesValue().contains("请假管理员")){
			//拥有对应角色
			isChange = true
		}
		
		if(isChange){
			actionList << createAction("新增",imgPath + "add.png",strname + "_add_admin")
		}
		
		actionList << createAction("刷新",imgPath + "fresh.gif","freshGrid")
		
		render actionList as JSON
	}
}
