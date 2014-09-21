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
	String program//留学项目
	
	@GridColumn(name="申请年份",colIdx=3)
	String appYear//申请年份
	
	@GridColumn(name="留学层次",colIdx=4)
	String studyLevel//留学层次
	
	@GridColumn(name="留学身份",colIdx=5)
	String studyIdentity//学习身份
	
	@GridColumn(name="学历层次",colIdx=6)
	String educationLevel//学历层次
	
	@GridColumn(name="学科",colIdx=8)
	String discipline//学科
	
	@GridColumn(name="专业",colIdx=9)
	String major//专业
	
	Date abroadDate =new Date()//出国时间
	
	@GridColumn(name="出国时间",width="106px",colIdx=7)
	def getFormatteAbroadDate(){
		if(abroadDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(abroadDate)
		}else{
			return ""
		}
	}
	
	Date returneDate=new Date()//回国时间
	
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
	String country//留学国家
	
	String cumulativeTime//出国累计时间
	
	String researchStatus//科研情况
	
	String patentStatus//专利情况
	
	String paperStatus//论文情况
	
	String remark//备注
	
    static belongsTo = [user:User,company:Company]
	
    static constraints = {
		cumulativeTime nullable:true,blank:true
    }
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_TRAIN_FORGEINSTUDY"
	}
	
}
