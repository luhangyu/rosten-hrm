package com.rosten.app.staff

import java.text.SimpleDateFormat
import com.rosten.app.annotation.GridColumn

//学历学位
class Degree {
	String id
	
	//学校名称
	@GridColumn(name="学校名称",colIdx=1,formatter="degree_formatTopic")
	String degreeName
	
	//入学时间
	Date startDate
	
	@GridColumn(name="入学时间",colIdx=2)
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
	
	//学历
	@GridColumn(name="学历",colIdx=4)
	String degree = "本科"
	
	//专业
	@GridColumn(name="专业",colIdx=5)
	String major
	
	@GridColumn(name="操作",width="60px",colIdx=6,formatter="degree_delete")
	def actionId(){
		return id
	}
	
    static constraints = {
		
    }
	static belongsTo = [personInfor:PersonInfor]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_DEGREE"
	}
	
}
