package com.rosten.app.attendance

import java.util.Date;

import com.rosten.app.system.User
import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company
import com.rosten.app.gtask.Gtask

import java.text.SimpleDateFormat

import com.rosten.app.share.*
import com.rosten.app.util.SystemUtil
import com.rosten.app.util.Util

/**
 * 请假申请
 * @author xucy
 * 备注：当前domain中applyName不允许重名
 *
 */
class Vacate {

	String id
	
	@GridColumn(name="申请人",formatter="vacate_formatTopic",colIdx=1)
	String applyName
	
	//部门名称
	@GridColumn(name="申请部门",colIdx=2)
	String applyDepart
	
	//请假数量
	double numbers = 1
	@GridColumn(name="申请天数",width="60px",colIdx=3)
	def getFormattedNumbers(){
		return Util.DoubleToFormat(numbers,1)
	}
	
	String unitType = "天"//小时或者天
	
	//请假类型
	@GridColumn(name="类型",colIdx=4)
	String vacateType = "事假"
	
	//开始时间
	Date startDate = new Date()
	
	@GridColumn(name="开始时间",width="106px",colIdx=5)
	def getFormatteStartDate(){
		if(startDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(startDate)
		}else{
			return ""
		}
	}
	
	//结束时间
	Date endDate = new Date() + 1
	
	@GridColumn(name="结束时间",width="106px",colIdx=6)
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
	
	//请假内容
	String remark
	
	//创建时间
	Date createDate = new Date()

	@GridColumn(name="创建时间",width="106px",colIdx=9)
	def getFormattedCreatedDate(){
		if(createDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(createDate)
		}else{
			return ""
		}
	}
	
	//流程相关字段信息----------------------------------------------------------
	//已阅读人员,读者
	static hasMany=[hasReaders:User,readers:User]
	
	//当前处理人
	User currentUser

	@GridColumn(name="当前处理人",colIdx=7)
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
	
	//缺省读者；*:允许所有人查看,[角色名称]:允许角色,user:普通人员查看
	String defaultReaders="[普通人员],[应用管理员]"
	def addDefaultReader(String userRole){
		if(defaultReaders==null || "".equals(defaultReaders)){
			defaultReaders = userRole
		}else{
			defaultReaders += "," + userRole
		}
	}
	
	//起草人
	User drafter
	
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
	
	@GridColumn(name="状态",colIdx=8)
	String status = "新增"
	
	//-------------------------流程引擎----------
	
    static constraints = {
		remark nullable:true,blank:true
		numbers nullable:true,blank:true
		
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
		table "ROSTEN_VACATE"
		
		//兼容mysql与oracle
		def systemUtil = new SystemUtil()
		if(systemUtil.getDatabaseType().equals("oracle")){
			remark sqlType:"clob"
		}else{
			remark sqlType:"text"
		}
	}
	def beforeDelete(){
		Vacate.withNewSession{session ->
			Gtask.findAllByContentId(this.id).each{item->
				item.delete()
			}
			FlowComment.findAllByBelongToId(this.id).each{item->
				item.delete()
			}
			FlowLog.findAllByBelongToId(this.id).each{item->
				item.delete()
			}
			session.flush()
		}
	}
	
}
