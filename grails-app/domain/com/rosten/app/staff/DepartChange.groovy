package com.rosten.app.staff

import java.text.SimpleDateFormat
import java.util.Date
import java.util.List;

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Attachment;
import com.rosten.app.system.Depart
import com.rosten.app.system.Company
import com.rosten.app.system.User;
import com.rosten.app.gtask.Gtask
import com.rosten.app.share.*

class DepartChange {
	String id

	//申请人,有可能申请人与调动员工并非同一人，一般情况下为同一人
	User applayUser
	
	//申请调动员工
	@GridColumn(name="申请人",colIdx=1,width="60px",formatter="staffDepartChange_formatTopic")
	def getPersonInforName(){
		return personInfor?.chinaName
	}
	
	//调入部门
	Depart inDepart
	@GridColumn(name="调入部门",colIdx=2,width="80px")
	def getInDepartName(){
		return inDepart?.departName
	}
	
	@GridColumn(name="调出部门",colIdx=3,width="80px")
	def getOutDepartName(){
		def departs = personInfor?.departs
		if(departs && departs.size()>0){
			return departs[0].departName
		}
		return ""
	}
	
	//调动类型:"人员调动","人员下派","人员借用"
	@GridColumn(name="调动类型",colIdx=4,width="60px")
	String changeType
	
	//申请理由
	@GridColumn(name="申请理由",colIdx=5)
	String changeReason
	
	//变更时间
	Date changeDate = new Date()
	
	@GridColumn(name="调动时间",width="106px",colIdx=6)
	def getFormattedChangeDate(){
		if(changeDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(changeDate)
		}else{
			return ""
		}
	}
	
	def getShowChangeDate(){
		if(changeDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(changeDate)
		}else{
			return ""
		}
	}
	
	//调入岗位
	String inDuty
	
	//调入编制
	String inPersonType
	
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
	//附件
	Attachment attachment
	
	//流程相关字段信息----------------------------------------------------------
	//增加已阅读人员,读者
	static hasMany=[hasReaders:User,readers:User]
	
	//当前处理人
	User currentUser

	@GridColumn(name="当前处理人",width="60px",colIdx=8)
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
	@GridColumn(name="状态",width="60px",colIdx=9)
	String status = "新增"
	
	//--------------------------------------------------------------------------
	
	static constraints = {
		changeReason nullable:true,blank:true
		inDuty nullable:true,blank:true
		inPersonType nullable:true,blank:true
		
		attachment nullable:true,blank:true
		
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
	static belongsTo = [personInfor:PersonInfor,company:Company]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_DEPARTCHANGE"
		changeReason sqlType:"text"
	}
	def beforeDelete(){
		DepartChange.withNewSession{session ->
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
