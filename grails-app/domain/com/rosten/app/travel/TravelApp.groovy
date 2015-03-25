package com.rosten.app.travel

import java.text.SimpleDateFormat
import java.util.Date;

import com.rosten.app.system.User;
import com.rosten.app.annotation.GridColumn
import com.rosten.app.share.*
import com.rosten.app.gtask.Gtask
import com.rosten.app.system.Attachment
import com.rosten.app.system.Company
import com.rosten.app.staff.PersonInfor

/*
 * 出差申请
 */
class TravelApp {
	
	String id
	
	//申请单编号
	@GridColumn(name="申请单编号",width="80px",formatter="travel_formatTitle",colIdx=1)
	String applyNum
	
	//出差负责人
	String chargePerson
	
	//出差人员
	
	@GridColumn(name="出差人员",colIdx=3)
	def getTravelUsersValue(){
		def _user =[]
		if(travelUsers && travelUsers.size()>0){
			_user = travelUsers.collect { elem ->
			  elem.chinaName
			}
		}
		return _user.join(",")
	}
	
	//出差地点
	@GridColumn(name="出差地点",colIdx=4)
	String travelAddress
	
	//开始时间
	Date startDate = new Date()
	
	//结束时间
	Date endDate = new Date() + 1
	
	@GridColumn(name="开始时间",width="106px",colIdx=5)
	def getFormattedStartDate(){
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
		return sd.format(startDate)
	}
	
	@GridColumn(name="结束时间",width="106px",colIdx=6)
	def getFormattedEndDate(){
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
		return sd.format(endDate)
	}
	
	//出差天数
	@GridColumn(name="出差天数",colIdx=7)
	Number days = 1
	
	//出差事由
	String travelReason
	
	//出差人数
	@GridColumn(name="出差人数",colIdx=8)
	Number travelNum = 1
	
	//流程相关字段信息----------------------------------------------------------
	//读者
	static hasMany=[travelUsers:PersonInfor,readers:User]
	
	//缺省读者；*:允许所有人查看,[角色名称]:允许角色
	String defaultReaders="[应用管理员]"
	def addDefaultReader(String userRole){
		if(defaultReaders==null || "".equals(defaultReaders)){
			defaultReaders = userRole
		}else{
			defaultReaders += "," + userRole
		}
	}
	
	//申请人员
	User drafter
	
	@GridColumn(name="申请人",width="50px",colIdx=9)
	def getDrafterName(){
		if(drafter!=null){
			return drafter.getFormattedName()
		}else{
			return ""
		}
	}
	
	//拟稿部门
	String drafterDepart
	
	//当前处理人
	User currentUser
	
	@GridColumn(name="当前处理人",width="60px",colIdx=10)
	def getCurrentUserName(){
		if(currentUser!=null){
			return currentUser.getFormattedName()
		}else{
			return ""
		}
	}
	//当前处理人部门
	String currentDepart
	
	//处理时间
	Date currentDealDate
	
	//状态
	@GridColumn(name="状态",width="50px")
	String status = "员工申请"
	
	//拟稿时间
	Date createDate = new Date()
	
	@GridColumn(name="申请时间",width="106px")
	def getFormattedDate(){
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
		return sd.format(createDate)
	}
	def getFormattedDate1(){
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
		return sd.format(createDate)
	}
	
	//流程定义id
	String processDefinitionId
	
	//流程id
	String processInstanceId
	
	//任务id
	String taskId
	
	//-----------------------------------------------------------------------------
	
    static constraints = {
		
		chargePerson nullable:true,blank:true
		travelAddress nullable:true,blank:true
		travelReason nullable:true,blank:true
		
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
		table "RS_TRA_APPLY"
	}
	static belongsTo = [company:Company]
	
	def beforeDelete(){
		TravelApp.withNewSession{_session->
			FlowComment.findAllByBelongToId(this.id).each{item->
				item.delete()
			}
			FlowLog.findAllByBelongToId(this.id).each{item->
				item.delete()
			}
			Gtask.findAllByContentId(this.id).each{item->
				item.delete()
			}
			Attachment.findAllByBeUseId(this.id).each{item->
				item.delete()
			}
			_session.flush()
		}
	}
}
