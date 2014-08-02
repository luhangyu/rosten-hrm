package com.rosten.app.publicc

import grails.converters.JSON
import com.rosten.app.util.FieldAcl
import com.rosten.app.system.Company
import com.rosten.app.system.User
import com.rosten.app.system.Attachment
import com.rosten.app.util.Util
import com.rosten.app.util.SystemUtil

class PubliccController {
	def springSecurityService
	def publiccService
	
	def publishDownloadFile ={
		def user = User.get(params.userId)
		def company = Company.get(params.companyId)
		
		def max = 5
		def offset = 0
		
		def c = DownLoadFile.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			eq("isShow",true)
			order("number", "asc")
		}
		
		//获取配置文档
		def today= new Date()
		
		//取最前面5条数据
		def _list = []
		def cResult =c.list(pa,query).unique()
		if(cResult.size() > 5){
			cResult = cResult[0..4]
		}
		cResult.each{
			def smap =[:]
			smap["topic"] = Util.getLimitLengthString(it.subject,40,"...")
			smap["id"] = it.id
			smap["date"] = it.getFormattedPublishDate()
			
			_list << smap
		}
		render _list as JSON
	}
	def getFileUpload ={
		def model =[:]
		model["docEntity"] = "publicc"
		model["isShowFile"] = false
		if(params.id){
			//已经保存过
			def downloadFile = DownLoadFile.get(params.id)
			model["docEntityId"] = params.id
			//获取附件信息
			model["attachFiles"] = Attachment.findAllByBeUseId(params.id)
			
			def user = springSecurityService.getCurrentUser()
			if("admin".equals(user.getUserType())){
				model["isShowFile"] = true
			}
		}else{
			//尚未保存
			model["newDoc"] = true
		}
		render(view:'/share/fileUpload',model:model)
	}
	
	def uploadFile = {
		def json=[:]
		SystemUtil sysUtil = new SystemUtil()
		
		def uploadPath
		def currentUser = (User) springSecurityService.getCurrentUser()
		def companyPath = currentUser.company?.shortName
		if(companyPath == null){
			uploadPath = sysUtil.getUploadPath("downloadFile")
		}else{
			uploadPath = sysUtil.getUploadPath(currentUser.company.shortName + "/downloadFile")
		}
		
		def f = request.getFile("uploadedfile")
		if (f.empty) {
			json["result"] = "blank"
			render json as JSON
			return
		}
		
		def uploadSize = sysUtil.getUploadSize()
		if(uploadSize!=null){
			//控制附件上传大小
			def maxSize = uploadSize * 1024 * 1024
			if(f.size>=maxSize){
				json["result"] = "big"
				render json as JSON
				return
			}
		}
		String name = f.getOriginalFilename()//获得文件原始的名称
		def realName = sysUtil.getRandName(name)
		f.transferTo(new File(uploadPath,realName))
		
		def attachment = new Attachment()
		attachment.name = name
		attachment.realName = realName
		attachment.type = "downloadFile"
		attachment.url = uploadPath
		attachment.size = f.size
		attachment.beUseId = params.id
		attachment.upUser = (User) springSecurityService.getCurrentUser()
		attachment.save(flush:true)
		
		def downloadFile = DownLoadFile.get(params.id)
		downloadFile.attachment = attachment
		downloadFile.save(flush:true)
		
		json["result"] = "true"
		json["fileId"] = attachment.id
		json["fileName"] = name
		
		if("yes".equals(params.isIE)){
			def resultStr  = '{"result":"true", "fileId":"' + json["fileId"]  + '","fileName":"' + json["fileName"] +'"}'
			render "<textarea>" + resultStr +  "</textarea>"
			return
		}else{
			render json as JSON
		}
		
	}
	
	def downloadFileGrid ={
		def json=[:]
		def company = Company.get(params.companyId)
		def user = User.get(params.userId)
		
		if(params.refreshHeader){
			def layout = publiccService.getDownloadFileListLayout()
			json["gridHeader"] = layout
		}
		
		if(params.refreshData){
			def args =[:]
			int perPageNum = Util.str2int(params.perPageNum)
			int nowPage =  Util.str2int(params.showPageNum)
			
			args["offset"] = (nowPage-1) * perPageNum
			args["max"] = perPageNum
			args["company"] = company
			
			def gridData = publiccService.getDownloadFileListDataStore(args)
			json["gridData"] = gridData
			
		}
		if(params.refreshPageControl){
			def total = publiccService.getDownloadFileCount(company)
			json["pageControl"] = ["total":total.toString()]
		}
		render json as JSON
	}
	
	def downloadFileAdd ={
		redirect(action:"downloadFileShow",params:params)
	}
	
	def downloadFileShow ={
		def model =[:]
		
		def user = User.get(params.userid)
		def company = Company.get(params.companyId)
		def downloadFile = new DownLoadFile()
		if(params.id){
			downloadFile = DownLoadFile.get(params.id)
		}
		
		if(!downloadFile){
			render '<h2 style="color:red;width:500px;margin:0 auto">此文件已过期或删除，请联系管理员！</h2>'
			return
		}
		
		model["downloadFile"] = downloadFile
		model["user"]=user
		model["company"] = company
		
		FieldAcl fa = new FieldAcl()
		if("normal".equals(user.getUserType())){
			//普通用户
			fa.readOnly = ["isShow","number","subject","description"]
		}
		model["fieldAcl"] = fa
		
		render(view:'/publicc/downloadFile',model:model)
	}
	def downloadFileDelete ={
		def ids = params.id.split(",")
		def json
		try{
			ids.each{
				def downloadFile = DownLoadFile.get(it)
				if(downloadFile){
					downloadFile.delete(flush: true)
				}
			}
			json = [result:'true']
		}catch(Exception e){
			json = [result:'error']
		}
		render json as JSON
	}
	def downloadFileSave = {
		def json=[:]
		
		def user = springSecurityService.getCurrentUser()
		
		def downloadFile
		if(params.id && !"".equals(params.id)){
			downloadFile = DownLoadFile.get(params.id)
			downloadFile.properties = params
			downloadFile.clearErrors()
		}else{
			downloadFile = new DownLoadFile()
			downloadFile.properties = params
			downloadFile.clearErrors()
			
			downloadFile.company = Company.get(params.companyId)
			downloadFile.publisher = user
		}
		
		if(downloadFile.save(flush:true)){
			json["result"] = true
			json["id"] = downloadFile.id
			json["companyId"] = downloadFile.company.id
			
		}else{
			downloadFile.errors.each{
				println it
			}
			json["result"] = false
		}
		render json as JSON
	}
	
    def index() { }
}
