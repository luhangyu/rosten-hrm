package com.rosten.app.staff

import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.User
import com.rosten.app.system.Company
import com.rosten.app.system.Attachment
import com.rosten.app.util.SystemUtil
import com.rosten.app.gtask.Gtask
import com.rosten.app.share.*

import java.text.SimpleDateFormat
import java.util.Date

class OfficialApply {
	String id
	
	//申请人
	@GridColumn(name="申请人",colIdx=1,width="60px",formatter="officialApply_formatTopic")
	def getPersonInforName(){
		return personInfor?.chinaName
	}
	
	@GridColumn(name="申请部门",colIdx=2,width="80px")
	def getApplyDepartName(){
		def departs = personInfor?.departs
		if(departs && departs.size()>0){
			return departs[0].departName
		}
		return ""
	}
	
	//申请理由
	@GridColumn(name="申请理由",colIdx=3)
	String applyReason
	
	//试用期开始时间
	Date startDate
	
	@GridColumn(name="开始时间",width="106px",colIdx=4)
	def getFormattedStartDate(){
		if(startDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(startDate)
		}else{
			return ""
		}
	}
	
	//试用期结束时间
	Date endDate
	
	@GridColumn(name="结束时间",width="106px",colIdx=5)
	def getFormattedEndDate(){
		if(endDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(endDate)
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
	
	//创建时间
	Date createDate = new Date()
	
	@GridColumn(name="创建时间",width="106px",colIdx=8)
	def getFormattedCreatedDate(){
		if(createDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(createDate)
		}else{
			return ""
		}
	}
	
	static belongsTo = [personInfor:PersonInfor,company:Company]
	
    static constraints = {
		applyReason nullable:true,blank:true
		startDate nullable:true,blank:true
		endDate nullable:true,blank:true
		
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
		table "RS_STAFF_OffApply"
		
		//兼容mysql与oracle
		def systemUtil = new SystemUtil()
		if(systemUtil.getDatabaseType().equals("oracle")){
			applyReason sqlType:"clob"
		}else{
			applyReason sqlType:"text"
		}
	}
	def beforeDelete(){
		OfficialApply.withNewSession{session ->
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
