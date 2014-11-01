package com.rosten.app.train

import java.text.SimpleDateFormat
import java.util.Date;
import java.util.List;

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company
import com.rosten.app.system.User
import com.rosten.app.util.SystemUtil

class TrainCourse {
	
	String id
	
	//培训班名称
	@GridColumn(name="培训班名称",formatter="trainCourse_formatTopic",colIdx=1)
	String courseName
	
	//培训组织名称
	@GridColumn(name="组织者名称",colIdx=2)
	String organizeName
	
	//培训时间
	Date trainDate = new Date()

	@GridColumn(name="培训时间")
	def getFormatteTrainDate(){
		if(trainDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(trainDate)
		}else{
			return ""
		}
	}
	
	//培训地点
	@GridColumn(name="培训地点",colIdx=3)
	String trainAddress
	
	//培训费用
	Long trainMoney
	 
	//培训所属计划
	String trainPlan
	
	//培训证书发放情况
	//String trainCert
	boolean trainCert = false
	def getIstrainCertValue(){
		if(trainCert)return "是"
		else return "否"
	}
	
	//创建时间
	Date createDate = new Date()
	
	@GridColumn(name="创建时间",width="106px",colIdx=7)
	def getFormattedCreatedDate(){
		if(createDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(createDate)
		}else{
			return ""
		}
	}
	
	//描述
	String description
	
	List items
	static hasMany=[items:TrainMessage,hasReaders:User,readers:User]
	
	//流程相关字段信息----------------------------------------------------------
	//当前处理人
	User currentUser

	@GridColumn(name="当前处理人",colIdx=5)
	def getCurrentUserName(){
		if(currentUser!=null){
			return currentUser.getFormattedName()
		}else{
			return ""
		}
	}

	//当前处理部门
	String currentDepart

	//当前处理时间
	Date currentDealDate
	
	//缺省读者；*:允许所有人查看,[角色名称]:允许角色,user:普通人员查看
	String defaultReaders="[应用管理员]"
	def addDefaultReader(String userRole){
		if(defaultReaders==null || "".equals(defaultReaders)){
			defaultReaders = userRole
		}else{
			defaultReaders += "," + userRole
		}
	}
	
	//起草人
	User drafter

	@GridColumn(name="创建人",colIdx=6)
	def getFormattedDrafter(){
		if(drafter!=null){
			return drafter.getFormattedName()
		}else{
			return ""
		}
	}

	//起草部门
	String drafterDepart

	//流程定义id
	String processDefinitionId
	
	//流程id
	String processInstanceId
	
	//任务id
	String taskId
	
	//状态
	@GridColumn(name="状态",width="60px",colIdx=4)
	String status = "新增"
	//---------------------------------------------------------------------
	
    static constraints = {
		trainAddress nullable:true,blank:true
		trainMoney nullable:true,blank:true
		trainPlan nullable:true,blank:true
		description nullable:true,blank:true
		
		//流程相关-------------------------------------------------------------
		defaultReaders nullable:true,blank:true
		currentUser nullable:true,blank:true
		currentDepart nullable:true,blank:true
		currentDealDate nullable:true,blank:true
		drafter nullable:true,blank:true
		drafterDepart nullable:true,blank:true
		
		processInstanceId nullable:true,blank:true
		taskId nullable:true,blank:true
		processDefinitionId nullable:true,blank:true
		//--------------------------------------------------------------------
    }
	
	static belongsTo = [company:Company]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_TRAIN_CO"
		
		//兼容mysql与oracle
		def systemUtil = new SystemUtil()
		if(systemUtil.getDatabaseType().equals("oracle")){
			description sqlType:"clob"
		}else{
			description sqlType:"text"
		}
	}
}
