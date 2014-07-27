package com.rosten.app.attendance

import java.util.Date;
import com.rosten.app.system.User
import java.text.SimpleDateFormat
/**
 * 请假申请
 * @author xucy
 *
 */
class Vacate {

	String id
	
	//开始时间
	Date startDate
	
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
	
	def getFormatteEndDate(){
		if(endDate!=null){
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd")
			return sd.format(endDate)
		}else{
			return ""
		}
	}
	
	//请假数量
	double number
	
	String unitType = "天"//小时或者天
	
	//请假类型
	String vacateType = "事假"
	
	String remark
	
    static constraints = {
		remark nullable:true,blank:true
    }
	static belongsTo = [user:User]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_Attendance_vacate"
		remark sqlType:"text"
	}
	
}
