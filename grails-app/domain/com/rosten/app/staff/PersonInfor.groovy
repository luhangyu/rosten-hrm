package com.rosten.app.staff

import com.rosten.app.system.Attachment;
import com.rosten.app.system.User
import com.rosten.app.system.UserType;
import com.rosten.app.system.Depart
import com.rosten.app.system.Company

import java.text.SimpleDateFormat
import java.util.Date;
import java.util.List;

import com.rosten.app.util.SystemUtil

//个人概况
class PersonInfor {
	
	String id
	
	String chinaName
	
	//个人头像
	String imageUrl
	
	//身份证号
	String idCard
	
	//性别
	String sex = "男"
	
	//出生日期
	Date birthday
	
	def getFormatteBirthday(){
		if(birthday!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(birthday)
		}else{
			return ""
		}
	}
	
	//出生地
	String birthAddress
	
	//籍贯
	String nativeAddress
	
	//曾用名
	String usedName
	
	//民族
	String nationality = "汉族"
	
	//国籍
	String city = "中国"
	
	//婚姻状况
	String marriage = "未婚"
	
	//健康状况
	String health = "良好"
	
	//政治面貌
	String politicsStatus = "党员"

	//宗教信仰
	String religion = "无"
	
	//血型
	String blood
	
	//户口所在地
	String householdRegi
	
	//毕业学校
	String schoolName
	
	//最高学历
	String upDegree
	
	//职称
	String technicalName
	
	//工作岗位
	String workJob
	
	//所学专业
	String major
	
	//工作时间
	Date workJobDate
	
	def getFormatteWorkJobDate(){
		if(workJobDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(workJobDate)
		}else{
			return ""
		}
	}
	
	//人事关系转入时间
	Date intoday = new Date()
	
	def getFormatteIntoday(){
		if(intoday!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(intoday)
		}else{
			return ""
		}
	}
	
	//专业技术等级
	String techGrade
	
	//入职时间
	Date staffOnDay = new Date() + 90
	def getFormatteStaffOnday(){
		if(staffOnDay!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(staffOnDay)
		}else{
			return ""
		}
	}
	
	//状态
	String status = "在职"
	
	//用户类型
	UserType userTypeEntity
	
	def getUserTypeName(){
		return userTypeEntity?userTypeEntity.typeName:null
	}
	
	//关联的账号用户信息
	User user
	
	//增加已阅读人员,读者
	List departs
	static hasMany=[departs:Depart,hasReaders:User,readers:User]
	
	def getUserDepartName(){
		if(departs && departs.size()>0){
			return departs[0].departName
		}
		return ""
	}
	
	//附件
	Attachment attachment
	
	//所属单位
	static belongsTo = [company:Company]
	
	//流程相关字段信息----------------------------------------------------------
	//当前处理人
	User currentUser

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

	//创建时间
	Date createDate = new Date()

	def getFormattedCreatedDate(){
		if(createDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(createDate)
		}else{
			return ""
		}
	}
	
	//流程定义id
	String processDefinitionId
	
	//流程id
	String processInstanceId
	
	//任务id
	String taskId
	
	//--------------------------------------------------------------------------
	
	String msResult
	
    static constraints = {
		imageUrl nullable:true,blank:true
		birthday nullable:true,blank:true
		birthAddress nullable:true,blank:true
		nativeAddress nullable:true,blank:true
		usedName nullable:true,blank:true
		blood nullable:true,blank:true
		householdRegi nullable:true,blank:true
		intoday nullable:true,blank:true
		techGrade nullable:true,blank:true
		staffOnDay nullable:true,blank:true
		user nullable:true,blank:true
		attachment nullable:true,blank:true
		msResult nullable:true,blank:true
		
		schoolName nullable:true,blank:true
		upDegree nullable:true,blank:true
		technicalName nullable:true,blank:true
		workJob nullable:true,blank:true
		workJobDate nullable:true,blank:true
		major nullable:true,blank:true
		politicsStatus nullable:true,blank:true
		health nullable:true,blank:true
		marriage nullable:true,blank:true
		
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
	
	def beforeDelete(){
		PersonInfor.withNewSession{session ->
			ContactInfor.findAllByPersonInfor(this).each{item->
				item.delete()
			}
			Bargain.findAllByPersonInfor(this).each{item->
				item.delete()
			}
			Degree.findAllByPersonInfor(this).each{item->
				item.delete()
			}
			FamilyInfor.findAllByPersonInfor(this).each{item->
				item.delete()
			}
			Political.findAllByPersonInfor(this).each{item->
				item.delete()
			}
			WorkResume.findAllByPersonInfor(this).each{item->
				item.delete()
			}
			DepartChange.findAllByPersonInfor(this).each{item->
				item.delete()
			}
			StatusChange.findAllByPersonInfor(this).each{item->
				item.delete()
			}
			NoticeBill.findAllByPersonInfor(this).each{item->
				item.delete()
			}
			
			session.flush()
		}
	}
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_PER"
		
		//兼容mysql与oracle
		def systemUtil = new SystemUtil()
		if(systemUtil.getDatabaseType().equals("oracle")){
			msResult sqlType:"clob"
		}else{
			msResult sqlType:"text"
		}
	}
}
