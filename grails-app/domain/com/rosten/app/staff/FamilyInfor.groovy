package com.rosten.app.staff

import com.rosten.app.annotation.GridColumn

//家庭成员
class FamilyInfor {
	
	String id
	
	//姓名
	@GridColumn(name="姓名",colIdx=1,formatter="familyInfor_formatTopic")
	String name
	
	//成员关系
	@GridColumn(name="成员关系",colIdx=2)
	String relation
	
	//移动电话
	@GridColumn(name="移动电话",colIdx=3)
	String mobile
	
	//工作单位
	@GridColumn(name="工作单位",colIdx=4)
	String workUnit
	
	//职务
	@GridColumn(name="职务",colIdx=5)
	String duties
	
	//政治面貌
	@GridColumn(name="政治面貌",colIdx=6)
	String politicsStatus
	
	@GridColumn(name="操作",width="60px",colIdx=7,formatter="familyInfor_delete")
	def familyInforId(){
		return id
	}
	
    static constraints = {
		workUnit nullable:true,blank:true
		mobile nullable:true,blank:true
		duties nullable:true,blank:true
		politicsStatus nullable:true,blank:true
    }
	
	static belongsTo = [personInfor:PersonInfor]
	
	static mapping = {
		id generator:'uuid.hex',params:[separator:'-']
		table "ROSTEN_STAFF_FAMILYINFOR"
	}
}
