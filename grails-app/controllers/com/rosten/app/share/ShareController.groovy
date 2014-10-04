package com.rosten.app.share

import com.rosten.app.workflow.WorkFlowService
import com.rosten.app.system.User
import grails.converters.JSON
import com.rosten.app.util.Util

class ShareController {
	def workFlowService
	
	/*
	 * 获取下个处理人员
	 */
	def getSelectFlowUser ={
		def json=[:]
		json.dealFlow = true
		
		def currentUser = User.get(params.userId)
		
		def defEntity = workFlowService.getNextTaskDefinition(params.taskId,[name:params.conditionName,value:params.conditionValue]);
		
		if(!defEntity){
			//流程处于最后一个节点
			json.dealFlow = false
			render json as JSON
			return
		}
		
		//存在处理人员的情况
		def expEntity = defEntity.getAssigneeExpression()
		if(expEntity){
			def expEntityText = expEntity.getExpressionText()
			if(expEntityText.contains("{")){
				json.user = params.drafterUsername
			}else{
				json.user = expEntity.getExpressionText()
			}
			
			//判断下一处理人是否有多部门情况，如果有，则弹出对话框选择，如果没有，直接进入下一步
			def userEntity = User.findByUsername(json.user)
			def userDeparts = userEntity.getAllDepartEntity()
			if(userDeparts && userDeparts.size()>1){
				//一个人存在多个部门的情况，这种情况比较少
				json.showDialog = true
			}else{
				json.showDialog = false
				json.userDepart = userDeparts[0].departName
				json.userId = userEntity.id
			}
			
			json.dealType = "user"
			render json as JSON
			return
		}
		
		//处理部门群组的情况
		def groupEntity = defEntity.getCandidateGroupIdExpressions()
		if(groupEntity.size()>0){
			//默认有一组group的方式为true，则整组均为true;true:严格控制本部门权限
			def groupIds = []
			def limit = false
			groupEntity.each{
				groupIds << Util.strLeft(it.getExpressionText(), ":")
				if(!limit && "true".equals(Util.strRight(it.getExpressionText(), ":"))){
					limit = true
				}
			}
			json.groupIds = groupIds.unique().join("-")
			if(limit){
				json.limitDepart = currentUser.getDepartEntityTrueName()
			}
			
			json.dealType = "group"
			render json as JSON
			return
		}
	}
	def getCommentLog ={
		def model =[:]
		def logs = FlowComment.findAllByBelongToId(params.id,[ sort: "createDate", order: "desc"])
		model["log"] = logs
		
		render(view:'/share/commentLog',model:model)
	}
	def getFlowLog ={
		def model =[:]
		def logs = FlowLog.findAllByBelongToId(params.id,[ sort: "createDate", order: "asc"])
		model["log"] = logs
		
		model["logEntityName"] = "share"
		model["processDefinitionId"] = params.processDefinitionId
		model["taskId"] = params.taskId
		
		render(view:'/share/flowLog',model:model)
	}
	def flowActiveExport ={
		InputStream imageStream = workFlowService.getflowActiveStream(params.processDefinitionId,params.taskId)
		
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
		  response.outputStream.write(b, 0, len);
		}
		response.outputStream.flush()
		response.outputStream.close()
	}
	def addComment ={
		def json=[:]
		def user = User.get(params.userId)
		
		def comment = new FlowComment()
		comment.user = user
		comment.status = params.status
		comment.content = params.dataStr
		comment.belongToId = params.id
		comment.belongToObject = params.flowCode
		comment.company = user.company
		
		if(comment.save(flush:true)){
			json["result"] = true
		}else{
			comment.errors.each{
				println it
			}
			json["result"] = false
		}
		render json as JSON
	}
}
