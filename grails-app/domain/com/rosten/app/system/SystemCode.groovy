package com.rosten.app.system

import com.rosten.app.annotation.GridColumn

class SystemCode {
	String id
	
	//代码编号
	@GridColumn(name="代码编号",colIdx=1)
	String code
	
	//代码名称
	@GridColumn(name="代码名称",colIdx=2,formatter="systemCode_formatTopic")
	String name
	
	//显示序号
	@GridColumn(name="显示顺序",colIdx=3)
	Integer serialNo
	
	//上级代码
	SystemCode parent
	
	@GridColumn(name="上级代码",colIdx=4)
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
