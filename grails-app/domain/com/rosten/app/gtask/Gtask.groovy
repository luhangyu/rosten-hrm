package com.rosten.app.gtask

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company
import com.rosten.app.system.User

import java.text.SimpleDateFormat
import java.util.Date;
import com.rosten.app.util.SystemUtil

class Gtask {
	String id
	
	//类型
	@GridColumn(name="类型",width="78px")
	String type
	
	@GridColumn(name="处理状态",width="60px",colIdx=2)
	def getGtaskStatus(){
		if("0".equals(status)){
			return "未处理"
		}else{
			return "已处理"
		}
	}
	
	@GridColumn(name="内容",formatter="gtask_formatTitle")
	String content
	
	//内容状态
	@GridColumn(name="内容状态",width="60px")
	String contentStatus
	
	//内容链接id
	@GridColumn(visible=false)
	String contentId
	
	//处理时间
	Date dealDate
	
	@GridColumn(name="处理时间",width="150px")
	def getFormattedDealDate(){
		if(dealDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(dealDate)
		}else{
			return ""
		}
	}
	
	//创建时间
	Date createDate = new Date()
	
	@GridColumn(name="接收时间",width="150px")
	def getFormattedCreatedDate(){
		if(createDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(createDate)
		}else{
			return ""
		}
	}
	
	//0:未处理;1:已处理
	String status="0"
	
	boolean openDeal = false
	
	static belongsTo = [company:Company,user:User]
	
    static constraints = {
		dealDate nullable:true,blank:true
		openDeal nullable:true,blank:true
    }
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_GTASK"
		
		//兼容mysql与oracle
		def systemUtil = new SystemUtil()
		if(systemUtil.getDatabaseType().equals("oracle")){
			content sqlType:"clob"
		}else{
			content sqlType:"text"
		}
	}
}
