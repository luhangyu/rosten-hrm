package com.rosten.app.train

import com.rosten.app.system.User;
import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company
import java.text.SimpleDateFormat
import java.util.Date;

class ForgeinStudy {

	String id
	
	//用户
	User user
	
	@GridColumn(name="姓名",formatter="forgeinStudy_formatTopic",colIdx=1,width="60px")
	def getUserName(){
		return user?.getFormattedName()
	}
	
	//部门名称
	@GridColumn(name="部门",colIdx=2)
	def getFormattedDepartName(){
		if(user!=null){
			return user.getDepartName()
		}else{
			return ""
		}
	}
	
	@GridColumn(name="留学项目",colIdx=7)
	def program//留学项目
	
	@GridColumn(name="申请年份",colIdx=3)
	def appYear//申请年份
	
	@GridColumn(name="留学层次",colIdx=4)
	def studyLevel//留学层次
	
	@GridColumn(name="留学身份",colIdx=5)
	def studyIdentity//学习身份
	
	@GridColumn(name="学历层次",colIdx=6)
	def educationLevel//学历层次
	
	@GridColumn(name="学科",colIdx=8)
	def discipline//学科
	
	@GridColumn(name="专业",colIdx=9)
	def major//专业
	
	def abroadDate //出国时间
	
	@GridColumn(name="出国时间",width="106px",colIdx=7)
	def getFormatteAbroadDate(){
		if(abroadDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(abroadDate)
		}else{
			return ""
		}
	}
	
	def returneDate//回国时间
	
	@GridColumn(name="回国时间",width="106px",colIdx=7)
	def getFormatteReturneDate(){
		if(returneDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(returneDate)
		}else{
			return ""
		}
	}
	
	@GridColumn(name="留学国家",colIdx=10)
	def country//留学国家
	
	def cumulativeTime//出国累计时间
	
	def researchStatus//科研情况
	
	def patentStatus//专利情况
	
	def paperStatus//论文情况
	
	def remark//备注
	
     static belongsTo = [user:User,company:Company]
	
    static constraints = {
		
    }
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_TRAIN_FORGEINSTUDY"
	}
	
}
