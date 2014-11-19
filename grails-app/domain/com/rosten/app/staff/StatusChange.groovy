package com.rosten.app.staff

import java.text.SimpleDateFormat
import java.util.Date

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Depart
import com.rosten.app.system.Company
import com.rosten.app.system.User
import com.rosten.app.util.SystemUtil
import com.rosten.app.system.Attachment
import com.rosten.app.gtask.Gtask
import com.rosten.app.share.*

class StatusChange {
	String id
	
	//申请人,有可能申请人与调动员工并非同一人，一般情况下为同一人
	User applayUser
	
	@GridColumn(name="员工名称",colIdx=1,width="60px",formatter="staffStatusChange_formatTopic")
	def getApplayPersonInforName(){
		return personInfor?.chinaName
	}
	
	@GridColumn(name="所属部门",colIdx=2,width="80px")
	def getOutDepartName(){
		def departs = personInfor?.departs
		if(departs && departs.size()>0){
			return departs[0].departName
		}
		return ""
	}
	
	//申请类型:离职/退休
	String changeType
	
	//申请理由
	@GridColumn(name="申请理由",colIdx=4)
	String changeReason
	
	//申请时间
	Date changeDate = new Date()
	
	@GridColumn(name="申请时间",width="106px",colIdx=3)
	def getFormattedChangeDate(){
		if(changeDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(changeDate)
		}else{
			return ""
		}
	}
	
	//创建时间
	Date createDate = new Date()
	
	@GridColumn(name="创建时间",width="106px",colIdx=8)
	def getFormattedCreatedDate(){
		if(createDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(createDate)
		}else{
			return ""
		}
	}
	
	//流程相关字段信息----------------------------------------------------------
	//增加已阅读人员,读者
	static hasMany=[hasReaders:User,readers:User]
	
	//当前处理人
	User currentUser

	@GridColumn(name="当前处理人",width="60px",colIdx=6)
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
	@GridColumn(name="状态",width="60px",colIdx=7)
	String status = "新增"
	
	//--------------------------------------------------------------------------
	
	static belongsTo = [personInfor:PersonInfor,company:Company]
	
    static constraints = {
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
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_STACH"
		
		//兼容mysql与oracle
		def systemUtil = new SystemUtil()
		if(systemUtil.getDatabaseType().equals("oracle")){
			changeReason sqlType:"clob"
		}else{
			changeReason sqlType:"text"
		}
		
	}
	def beforeDelete(){
		StatusChange.withNewSession{session ->
			Gtask.findAllByContentId(this.id).each{item->
				item.delete()
			}
			FlowComment.findAllByBelongToId(this.id).each{item->
				item.delete()
			}
			FlowLog.findAllByBelongToId(this.id).each{item->
				item.delete()
			}
			Attachment.findAllByBeUseId(this.id).each{item->
				item.delete()
			}
			session.flush()
		}
	}
}
