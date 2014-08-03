package com.rosten.app.publicc

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Attachment;
import com.rosten.app.system.Company;
import com.rosten.app.system.User;

import java.text.SimpleDateFormat

class DownLoadFile {
	String id
	
	//标题
	@GridColumn(name="标题",width="auto",formatter="formatterDownloadFileSubject")
	String subject
	
	//发布人
	User publisher
	
	@GridColumn(name="发布人",width="60px")
	def getPublisherName(){
		if(publisher!=null){
			return publisher.getFormattedName()
		}else{
			return ""
		}
	}
	
	//发布时间
	Date publishDate = new Date()
	
	@GridColumn(name="发布时间",width="120px")
	def getFormattedPublishDate(){
		if(publishDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(publishDate)
		}else{
			return ""
		}
	}
	
	//显示顺序
	int number
	
	//是否显示首页
	boolean isShow = true
	
	@GridColumn(name="显示首页",width="50px")
	def getFormattedIsShow(){
		if(isShow){
			return "是"
		}else{
			return "否"
		}
	}
	
	//描述
	String description
	
	//附件
	Attachment attachment
	
	@GridColumn(name="操作",width="80px",formatter="formatterDownloadFile")
	def getAttachmentId(){
		if(attachment){
			return attachment.id
		}else{
			return ""
		}
	}
	
	static belongsTo = [company:Company]
	
    static constraints = {
		number nullable:true,blank:true
		attachment nullable:true,blank:true
		description nullable:true,blank:true
    }
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_DOWNLOADFILE"
		description sqlType:"longtext"
	}
}
