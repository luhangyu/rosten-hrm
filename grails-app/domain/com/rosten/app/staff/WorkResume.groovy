package com.rosten.app.staff

import java.text.SimpleDateFormat
import com.rosten.app.annotation.GridColumn

//工作简历
class WorkResume {
	String id
	
	//工作单位
	@GridColumn(name="工作单位",colIdx=1,formatter="workResume_formatTopic")
	String workCompany
	
	//开始时间
	Date startDate
	
	@GridColumn(name="开始时间",colIdx=2)
	def getFormatteStartDate(){
		if(startDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(startDate)
		}else{
			return ""
		}
	}
	
	//结束时间
	Date endDate
	@GridColumn(name="结束时间",colIdx=3)
	def getFormatteEndDate(){
		if(endDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(endDate)
		}else{
			return ""
		}
	}
	
	//工作内容
	@GridColumn(name="工作内容",colIdx=4)
	String workContent
	
	//担任职务
	@GridColumn(name="担任职务",colIdx=5)
	String duty
	
	//证明人
	@GridColumn(name="证明人",colIdx=6)
	String proveName
	
	//备注
	@GridColumn(name="备注",colIdx=7)
	String remark
	
	@GridColumn(name="操作",width="60px",colIdx=8,formatter="workResume_delete")
	def actionId(){
		return id
	}
	
    static constraints = {
		duty nullable:true,blank:true
		proveName nullable:true,blank:true
		remark nullable:true,blank:true
    }
	static belongsTo = [personInfor:PersonInfor]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_WORKRESUME"
		
		workContent sqlType:"text"
		remark sqlType:"text"
	}
}
