package com.rosten.app.attendance

import java.text.SimpleDateFormat
import java.util.Date

import com.rosten.app.util.SystemUtil
import com.rosten.app.annotation.GridColumn
import com.rosten.app.system.Company

class VacateExplain {
	String id
	
	@GridColumn(name="申请人",formatter="vacateExplain_formatTopic",colIdx=1)
	String applyName
	
	//部门名称
	@GridColumn(name="申请部门",colIdx=2)
	String applyDepart
	
	//未出勤时间
	Date explainDate = new Date()
	
	@GridColumn(name="未出勤时间",width="106px",colIdx=3)
	def getFormatteStartDate(){
		if(explainDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(explainDate)
		}else{
			return ""
		}
	}
	
	//上午/下午
	@GridColumn(name="区间",width="80px" ,colIdx=4)
	String dateStage
	
	//未出勤理由
	@GridColumn(name="理由",colIdx=5)
	String remark
	
	//创建时间
	Date createDate = new Date()

	@GridColumn(name="创建时间",width="106px",colIdx=6)
	def getFormattedCreatedDate(){
		if(createDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm")
			return sd.format(createDate)
		}else{
			return ""
		}
	}
	
    static constraints = {
    }
	static belongsTo = [company:Company]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "Rs_VACATE_EXPLAIN"
		
		//兼容mysql与oracle
		def systemUtil = new SystemUtil()
		if(systemUtil.getDatabaseType().equals("oracle")){
			remark sqlType:"clob"
		}else{
			remark sqlType:"text"
		}
	}
}
