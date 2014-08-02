package com.rosten.app.bbs

import java.util.List;

import com.rosten.app.system.Company

class BbsConfig {
	String id
	
	//今年年代
	Integer nowYear
	
	//今年流水号
	Integer nowSN = 1
	
	//今年保留号或者废号
	String nowCancel
	
	//去年年代
	Integer frontYear
	
	//去年流水号
	Integer frontSN = 1
	
	//去年保留号或者废号
	String frontCancel
	
	//最新公告保留时间
	Integer showDays = 7
	
	static transients = ["addCancelSN","removeCancelSN"]
	
	def addCancelSN ={type,sn->
		if("now".equals(type)){
			//今年
			if("".equals(nowCancel)){
				nowCancel = sn
			}else{
				nowCancel += "," + sn 
			}
		}else{
			//去年
			if("".equals(frontCancel)){
				frontCancel = sn
			}else{
				frontCancel += "," + sn 
			}
		}
	}
	
	//获取保留号或者废弃号
	def removeCancelSN ={type,sn->
		if("now".equals(type)){
			return nowCancel.split(",").remove(sn).join(",")
		}else{
			return frontCancel.split(",").remove(sn).join(",")
		}
	}
	
	static belongsTo = [company:Company]
	
    static constraints = {
		nowCancel nullable:true,blank:true
		frontCancel nullable:true,blank:true
    }
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_BBS_CONFIG"
	}
}
