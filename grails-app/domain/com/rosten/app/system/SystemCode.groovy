package com.rosten.app.system

import com.rosten.app.annotation.GridColumn
import com.rosten.app.util.Util

class SystemCode {
	String id
	
	//代码编号
	@GridColumn(name="代码编号",colIdx=1,width="150px")
	String code
	
	//代码名称
	@GridColumn(name="代码名称",colIdx=2,width="150px",formatter="systemCode_formatTopic")
	String name
	
	//显示序号
	//@GridColumn(name="显示顺序",colIdx=3)
	Integer serialNo
	
	@GridColumn(name="包含条目",colIdx=3)
	def getSystemCodeItems(){
		if(items){
			def itemList = items.collect{elem->
				elem.name
			}
			return Util.getLimitLengthString(itemList.join(","),100,"...")
		}else{
			return ""
		}
	}
	
	//上级代码
	SystemCode parent
	
	@GridColumn(name="上级代码",colIdx=4,width="150px")
	def getParentName(){
		return parent?parent.name:""
	}	
	//拥有的条目
	List items
	static hasMany=[items:SystemCodeItem,children:SystemCode]
	
	static belongsTo = [company:Company]
	
    static constraints = {
		serialNo nullable:true,blank:true
		parent nullable:true
    }
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_SYSTEMCODE"
	}
	
}
