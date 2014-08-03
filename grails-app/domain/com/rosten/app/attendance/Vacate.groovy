package com.rosten.app.attendance

import java.util.Date;

import com.rosten.app.system.User
import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company

import java.text.SimpleDateFormat

/**
 * 请假申请
 * @author xucy
 *
 */
class Vacate {

	String id
	
	@GridColumn(name="拟稿人",formatter="vacate_formatTopic",colIdx=1)
	def getFormattedDrafter(){
		if(user!=null){
			return user.getFormattedName()
		}else{
			return ""
		}
	}
	
	//部门名称
	def getFormattedDepartName(){
		if(user!=null){
			return user.getDepartName()
		}else{
			return ""
		}
	}
	
	//开始时间
	Date startDate = new Date()
	
	@GridColumn(name="开始时间",width="106px",colIdx=2)
	def getFormatteStartDate(){
		if(startDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(startDate)
		}else{
			return ""
		}
	}
	
	//结束时间
	Date endDate = new Date()
	
	@GridColumn(name="结束时间",width="106px",colIdx=3)
	def getFormatteEndDate(){
		if(endDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, 1);
			return sd.format(c.getTime())
		}else{
			return ""
		}
	}
	
	//请假数量
	int numbers
	
	String unitType = "天"//小时或者天
	
	//请假类型
	@GridColumn(name="类型",colIdx=4)
	String vacateType = "事假"
	
	//请假内容
	String remark
	
	@GridColumn(name="状态",colIdx=6)
	String status = "新建"
	
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
	
	//前处理人
	User frontUser
	
	//前处理部门
	String frontDepart
	
	//前处理时间
	Date frontDealDate
	
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
	//@GridColumn(name="处理部门",width="60px",colIdx=7)
	String currentDepart

	//当前处理时间
	Date currentDealDate
	
	//已阅读人员,读者
	static hasMany=[hasReaders:User,readers:User]
	
	//缺省读者；*:允许所有人查看,[角色名称]:允许角色,user:普通人员查看
	String defaultReaders="[普通人员],[请假管理员]"
	def addDefaultReader(String userRole){
		if(defaultReaders==null || "".equals(defaultReaders)){
			defaultReaders = userRole
		}else{
			defaultReaders += "," + userRole
		}
	}
	
	//-------------------------流程引擎----------
	//流程定义id
	String processDefinitionId
	
	//流程id
	String processInstanceId
	
	//任务id
	String taskId
	
	//-------------------------流程引擎----------
	
    static constraints = {
		remark nullable:true,blank:true
		numbers nullable:true,blank:true
		
		frontUser nullable:true,blank:true
		frontDepart nullable:true,blank:true
		frontDealDate nullable:true,blank:true
		
		currentUser nullable:true,blank:true
		currentDepart nullable:true,blank:true
		currentDealDate nullable:true,blank:true
		
		processDefinitionId nullable:true,blank:true
		processInstanceId nullable:true,blank:true
		taskId nullable:true,blank:true
    }
	
	static belongsTo = [user:User,company:Company]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_VACATE"
		remark sqlType:"text"
	}
	
}
