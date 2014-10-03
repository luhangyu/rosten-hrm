package com.rosten.app.share

import com.rosten.app.workflow.WorkFlowService
import com.rosten.app.system.User
import grails.converters.JSON

class ShareController {
	def workFlowService
	
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
