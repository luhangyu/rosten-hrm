package com.rosten.app.staff

import grails.converters.JSON
import com.rosten.app.system.User

class StaffActionController {
	def imgPath ="images/rosten/actionbar/"
	
	def bargainForm ={
		def webPath = request.getContextPath() + "/"
		def actionList = []
		def strname = "bargain"
		actionList << createAction("返回",webPath + imgPath + "quit_1.gif","page_quit")
		actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_add")
		render actionList as JSON
	}
	def statusChangeForm ={
		def webPath = request.getContextPath() + "/"
		def actionList = []
		def strname = "statusChange"
		actionList << createAction("返回",webPath + imgPath + "quit_1.gif","page_quit")
		actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_add")
		render actionList as JSON
	}
	def bargainView ={
		render createCommonAction(null,"bargain",true) as JSON
	}
	def departChangeForm ={
		def webPath = request.getContextPath() + "/"
		def actionList = []
		def strname = "departChange"
		actionList << createAction("返回",webPath + imgPath + "quit_1.gif","page_quit")
		
		if(params.id){
			def entity = DepartChange.get(params.id)
			def user = User.get(params.userid)
			if(user.equals(entity.currentUser)){
				//当前处理人
				switch (true){
					case entity.status.contains("审核") || entity.status.contains("审批"):
						actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_add")
						actionList << createAction("填写意见",webPath +imgPath + "sign.png",strname + "_addComment")
						actionList << createAction("同意",webPath +imgPath + "ok.png",strname + "_submit")
						actionList << createAction("退回",webPath +imgPath + "back.png",strname + "_back")
						break;
					default :
						actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_add")
						actionList << createAction("填写意见",webPath +imgPath + "sign.png",strname + "_addComment")
						actionList << createAction("提交",webPath +imgPath + "submit.png",strname + "_submit")
						break;
				}
			}
		}else{
			actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_add")
		}
		render actionList as JSON
	}
	def staffStatusChangeView ={
		def actionList =[]
		def strname = "staffStatusChange"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		if("leave".equals(params.type)){
			actionList << createAction("离职申请",imgPath + "add.png","add_" + strname)
		}else{
			actionList << createAction("退休申请",imgPath + "add.png","add_" + strname)
		}
		actionList << createAction("打印通知单",imgPath + "word_print.png",strname + "_print_tzd")
		actionList << createAction("打印交接清单",imgPath + "word_print.png",strname + "_print_qd")
		
		actionList << createAction("删除",imgPath + "delete.png","delete_" + strname)
		actionList << createAction("刷新",imgPath + "fresh.gif","freshGrid")
		render actionList as JSON
	}
	
	def staffDepartChangeView ={
		def actionList =[]
		def strname = "staffDepartChange"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("员工调动",imgPath + "add.png","add_" + strname)
		actionList << createAction("打印调动通知单",imgPath + "word_print.png",strname + "_print_tzd")
		actionList << createAction("打印交接清单",imgPath + "word_print.png",strname + "_print_qd")
		actionList << createAction("删除",imgPath + "delete.png","delete_" + strname)
		actionList << createAction("刷新",imgPath + "fresh.gif","freshGrid")
		render actionList as JSON
	}
	def staffAddView ={
		def actionList =[]
		def strname = "personInfor"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("员工入职",imgPath + "add.png",strname + "_rz")
		actionList << createAction("打印登记表",imgPath + "word_print.png",strname + "_print")
		actionList << createAction("打印入职清单",imgPath + "word_print.png",strname + "_print_rzqd")
		actionList << createAction("打印入职通知书",imgPath + "word_print.png",strname + "_print_rztzs")
		actionList << createAction("删除",imgPath + "delete.png","delete_" + strname)
		actionList << createAction("刷新",imgPath + "fresh.gif","freshGrid")
		render actionList as JSON
	}
	def staffForm ={
		def webPath = request.getContextPath() + "/"
		def actionList = []
		def strname = "user"
		actionList << createAction("返回",webPath + imgPath + "quit_1.gif","page_quit")
		
		if(params.id && "staffAdd".equals(params.type)){
			def entity = PersonInfor.get(params.id)
			def user = User.get(params.userId)
			if(user.equals(entity.currentUser)){
				//当前处理人
				switch (true){
					case entity.status.contains("审核") || entity.status.contains("审批"):
						actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_add")
						actionList << createAction("填写意见",webPath +imgPath + "sign.png",strname + "_addComment")
						actionList << createAction("同意",webPath +imgPath + "ok.png",strname + "_submit")
						actionList << createAction("退回",webPath +imgPath + "back.png",strname + "_back")
						break;
					case entity.status.contains("已签发"):
						actionList << createAction("保存",webPath +imgPath + "Save.gif",strname +"_add")
						actionList << createAction("填写意见",webPath +imgPath + "sign.png",strname + "_addComment")
						actionList << createAction("录入合同",webPath +imgPath + "bargain.gif",strname + "_addBargain")
						actionList << createAction("生成登记表",webPath +imgPath + "word_print.png",strname +"_print_djb")
						actionList << createAction("生成录用通知书",webPath +imgPath + "word_print.png",strname +"_print_tzs")
						actionList << createAction("打印入职清单",webPath +imgPath + "word_print.png",strname +"_print_rzqd")
						actionList << createAction("结束流程",webPath +imgPath + "submit.png",strname +"_submit")
						break;
					case entity.status.contains("面试中"):
						actionList << createAction("保存",webPath +imgPath + "Save.gif",strname +"_add")
						actionList << createAction("填写意见",webPath +imgPath + "sign.png",strname + "_addComment")
						actionList << createAction("提交领导审核",webPath +imgPath + "submit.png",strname +"_ok")
						actionList << createAction("不通过",webPath +imgPath + "qx.png",strname +"_cancel")
						break;
					default :
						actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_add")
						actionList << createAction("填写意见",webPath +imgPath + "sign.png",strname + "_addComment")
						actionList << createAction("提交",webPath +imgPath + "submit.png",strname + "_submit")
						break;
				}
			}
		}else{
			actionList << createAction("保存",webPath +imgPath + "Save.gif",strname + "_add")
		}
		render actionList as JSON
	}
	def staffChangeDepartForm ={
		def actionList =[]
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("部门变更",imgPath + "add.png","staffChangeDepart")
		actionList << createAction("离职",imgPath + "add.png","staffLeave")
		actionList << createAction("退休",imgPath + "add.png","staffRetire")
		render actionList as JSON
	}
	
	def staffView ={
		def actionList =[]
		def strname = "personInfor"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("员工登记",imgPath + "add.png",strname + "_dj")
		actionList << createAction("批量导入",imgPath + "add.png","import_" + strname)
		actionList << createAction("导出员工信息",imgPath + "send.png","export_" + strname)
		actionList << createAction("打印登记表",imgPath + "word_print.png",strname + "_print")
		actionList << createAction("删除",imgPath + "delete.png","delete_" + strname)
		actionList << createAction("刷新",imgPath + "fresh.gif","freshGrid")
		render actionList as JSON
	}
    def userView ={
		def actionList =[]
		def strname = "personInfor"
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("新增",imgPath + "add.png",strname + "_add")
		actionList << createAction("查看",imgPath + "read.gif","read_" + strname)
		actionList << createAction("删除",imgPath + "delete.png","delete_" + strname)
		actionList << createAction("刷新",imgPath + "fresh.gif",strname + "_freshGrid")
		actionList << createAction("更改密码",imgPath + "changePassword.gif",strname + "_changePassword" )
		actionList << createAction("分配账号",imgPath + "asignAccount.gif",strname + "_asignAccount" )
		render actionList as JSON
	}
	private def createCommonAction={actionList,strname,args->
		if(!(actionList && actionList instanceof List)){
			actionList =[]
		}
		actionList << createAction("退出",imgPath + "quit_1.gif","returnToMain")
		actionList << createAction("新增",imgPath + "add.png","add_" + strname)
		actionList << createAction("查看",imgPath + "read.gif","read_" + strname)
		
		if(args){
			//允许修改，删除操作
//			actionList << createAction("修改",imgPath + "ok.png","change_" + strname)
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
