package com.rosten.app.staff

import java.util.Map;

import com.rosten.app.util.GridUtil
import com.rosten.app.system.Company
import com.rosten.app.system.Depart
import com.rosten.app.system.User
import com.rosten.app.system.Attachment
import com.rosten.app.system.UserType
import com.rosten.app.share.FlowComment

class StaffService {
	
	
	//2015-3-16-------------------------------------------------
	def getAllDepartId ={departIdList,depart->
		departIdList << depart.id
		if(depart.children){
			depart.children.each{
				return this.getAllDepartId(departIdList,it)
			}
		}else{
			return departIdList
		}
	}
	
	//2014-11-18增加员工聘任------------------------------------------
	def getEngageListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new Engage())
	}
	def getEngageListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllEngage(offset,max,params.company,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllEngage={offset,max,company,searchArgs->
		def c = Engage.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			order("createDate", "desc")
			
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.list(pa,query)
	}
	def getEngageCount ={company,searchArgs->
		def c = Engage.createCriteria()
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.count(query)
	}
	//------------------------------------------------------------
	
	//2014-11-17增加员工转正-------------------------------------------
	
	def getOfficialApplyListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new OfficialApply())
	}
	def getOfficialApplyListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllOfficialApply(offset,max,params.company,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllOfficialApply={offset,max,company,searchArgs->
		def c = OfficialApply.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			order("createDate", "desc")
			
			searchArgs.each{k,v->
				if(k.equals("chinaName")){
					createAlias('personInfor', 'a')
					like("a.chinaName","%" + v + "%")
				}else{
					like(k,"%" + v + "%")
				}
			}
		}
		return c.list(pa,query)
	}
	def getOfficialApplyCount ={company,searchArgs->
		def c = OfficialApply.createCriteria()
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				if(k.equals("chinaName")){
					createAlias('personInfor', 'a')
					like("a.chinaName","%" + v + "%")
				}else{
					like(k,"%" + v + "%")
				}
			}
		}
		return c.count(query)
	}
	
	//------------------------------------------------------------
	def getPersonInforByDepart(Depart depart){
		def c = PersonInfor.createCriteria()
		def resultList = c.list{
			departs{
				eq("id",depart?.id)
			}
		}
		return resultList
	}
	
	public List<Map<String, Object>> getCommentByStatus(String dealId,String status){
		def _list =[]
		def commentList = FlowComment.findAllByBelongToIdAndStatus(dealId,status)
		if(commentList && commentList.size()>0){
			commentList.each{
				def map =[:]
				map["name"] = it.user.getChinaName()
				map["content"]= it.content
				map["date"]= it.getFormattedCreatedDate()
				_list << map
			}
			
		}
		return _list
	}
	
	public PersonInfor getPersonInfor(String id) {
		return PersonInfor.get(id)
	}
	
	//通讯方式
	public ContactInfor getContactInfor(PersonInfor person){
		return ContactInfor.findByPersonInfor(person)
	}
	
	//工作经历
	public WorkResume getWorkResumeByPersonInfor(PersonInfor person){
		def _list = WorkResume.findAllByPersonInfor(person,[max: 1, sort: "startDate", order: "desc"])
		if(_list && _list.size()>0){
			return _list[0]
		}else{
			return null
		}
	}
	
	//工作经历
	public String getWorkResume(PersonInfor person){
		def _list = WorkResume.findAllByPersonInfor(person)
		if(_list && _list.size()>0){
			String con="";
			for(int i=0;i<_list.size();i++){
				def gzdw = _list[i].workCompany
				def kssj = _list[i].getFormatteStartDate()
				def jssj = _list[i].getFormatteEndDate()
				def zw = (null==_list[i].duty||"".equals(_list[i].duty))?"":_list[i].duty
				def zmr =(null==_list[i].proveName||"".equals(_list[i].proveName))?"": _list[i].proveName
				
				con+="工作单位："+gzdw+" 时间："+kssj+"—"+jssj+" 职务："+zw+" 证明人："+zmr+"&#x000D;";
			}
			return con;
		}else{
			return ""
		}
	}
	
	//家庭成员
	public String geFamilyInfor(PersonInfor person){
		def _list = FamilyInfor.findAllByPersonInfor(person)
		if(_list && _list.size()>0){
			String con="";
			for(int i=0;i<_list.size();i++){
				con+="关系："+_list[i].relation+" 姓名："+_list[i].name+" 移动电话："+_list[i].mobile+"&#x000D;";
			}
			return con;
		}else{
			return ""
		}
	}
	
	//最高学历（毕业学校，毕业时间）
	public Degree getDegreeByPersonInfor(PersonInfor person){
		def _list = Degree.findAllByPersonInfor(person,[max: 1, sort: "endDate", order: "desc"])
		if(_list && _list.size()>0){
			return _list[0]
		}else{
			return null
		}
	}
	
	//附件信息
	public Attachment getAttachment(String ids){  
		return Attachment.findByBeUseIdAndType(ids,"staff")
	}
	
	//部门调动信息
	public DepartChange getDepartChange(String id){
		return DepartChange.get(id);
	}
	
	
	public PersonInfor getPersonByDepaCh(String id){
		DepartChange depa =  DepartChange.get(id);
		return depa.personInfor;
	}
	
	//离职，退休
	public StatusChange getStatusChange(String id){
		return StatusChange.get(id);
	}
	
	public PersonInfor getPersonByStatuCh(String id){
		StatusChange depa =  StatusChange.get(id);
		return depa.personInfor;
	}
	
	def getBargainListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new Bargain())
	}
	def getBargainListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllBargain(offset,max,params.company,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllBargain={offset,max,company,searchArgs->
		def c = Bargain.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			order("bargainSerialNo", "desc")
			
			searchArgs.each{k,v->
				if(k.equals("chinaName")){
					createAlias('personInfor', 'a')
					like("a.chinaName","%" + v + "%")
				}else if(k.equals("bargainTime")){
					//lt("startDate",v)
					ge("endDate",v)
				}else{
					like(k,"%" + v + "%")
				}
			}
		}
		return c.list(pa,query)
	}
	def getBargainCount ={company,searchArgs->
		def c = Bargain.createCriteria()
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				if(k.equals("chinaName")){
					createAlias('personInfor', 'a')
					like("a.chinaName","%" + v + "%")
				}else if(k.equals("bargainTime")){
					lt("startDate",v)
					ge("endDate",v)
				}else{
					like(k,"%" + v + "%")
				}
			}
		}
		return c.count(query)
	}
	
	def getStaffStatusChangeListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new StatusChange())
	}
	def getStaffStatusChangeListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllStaffStatusChange(offset,max,params.company,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllStaffStatusChange={offset,max,company,searchArgs->
		def c = StatusChange.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			order("createDate", "desc")
			searchArgs.each{k,v->
				if(k.equals("chinaName")){
					createAlias('personInfor', 'a')
					like("a.chinaName","%" + v + "%")
					
				}else{
					like(k,"%" + v + "%")
				}
				
			}
		}
		return c.list(pa,query)
	}
	
	def getStaffStatusChangeCount ={company,searchArgs->
		def c = StatusChange.createCriteria()
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				if(k.equals("chinaName")){
					createAlias('personInfor', 'a')
					like("a.chinaName","%" + v + "%")
					
				}else{
					like(k,"%" + v + "%")
				}
			}
		}
		return c.count(query)
	}
	
	def getStaffDepartChangeListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new DepartChange())
	}
	def getStaffDepartChangeListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllStaffDepartChange(offset,max,params.company,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllStaffDepartChange={offset,max,company,searchArgs->
		def c = DepartChange.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			order("personInfor", "asc")
			
			searchArgs.each{k,v->
				if(k.equals("chinaName")){
					createAlias('personInfor', 'a')
					like("a.chinaName","%" + v + "%")
					
				}else if(k.equals("inDepart")){
					createAlias('inDepart', 'a')
					like("a.departName","%" + v + "%")
					
				}else{
					like(k,"%" + v + "%")
				}
			}
		}
		return c.list(pa,query)
	}
	def getStaffDepartChangeCount ={company,searchArgs->
		def c = DepartChange.createCriteria()
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				if(k.equals("chinaName")){
					createAlias('personInfor', 'a')
					like("a.chinaName","%" + v + "%")
					
				}else if(k.equals("inDepart")){
					createAlias('inDepart', 'a')
					like("a.departName","%" + v + "%")
					
				}else{
					like(k,"%" + v + "%")
				}
			}
		}
		return c.count(query)
	}
	
    def getFamilyInforListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new FamilyInfor())
	}
	def getFamilyInforListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllFamilyInfor(offset,max,params.personInfor,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllFamilyInfor={offset,max,personInfor,searchArgs->
		def c = FamilyInfor.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("personInfor",personInfor)
//			order("serialNo", "asc")
			
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.list(pa,query)
	}
	def getFamilyInforCount ={personInfor,searchArgs->
		def c = FamilyInfor.createCriteria()
		def query = {
			eq("personInfor",personInfor)
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.count(query)
	}
	def getDegreeInforListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new Degree())
	}
	def getDegreeInforListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllDegreeInfor(offset,max,params.personInfor,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllDegreeInfor={offset,max,personInfor,searchArgs->
		def c = Degree.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("personInfor",personInfor)
			
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.list(pa,query)
	}
	def getDegreeInforCount ={personInfor,searchArgs->
		def c = Degree.createCriteria()
		def query = {
			eq("personInfor",personInfor)
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.count(query)
	}
	def getWorkResumeInforListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new WorkResume())
	}
	def getWorkResumeInforListDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllWorkResumeInfor(offset,max,params.personInfor,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	private def getAllWorkResumeInfor={offset,max,personInfor,searchArgs->
		def c = WorkResume.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("personInfor",personInfor)
			
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.list(pa,query)
	}
	def getWorkResumeInforCount ={personInfor,searchArgs->
		def c = WorkResume.createCriteria()
		def query = {
			eq("personInfor",personInfor)
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.count(query)
	}
	
	//后期需要更新改造
	
	public boolean commonSave(PersonInfor entity,ContactInfor contactInfor,User userEntity,String departName,String userTypeName) {
		
		def departEntity = Depart.findByDepartName(departName);
		if(departEntity){
			entity.addToDeparts(departEntity)
		}
		
		def userType = UserType.findByTypeName(userTypeName)
		if(!userType){
			userType = UserType.first()
		}
		
		entity.userTypeEntity = userType
		entity.company = userEntity.company
		
		if(entity.save(flush:true)){
			contactInfor.personInfor = entity
			contactInfor.save(flush:true)
			return true
		}else{
			entity.errors.each{
				println it
			}
			return false
		}
	}
	
	//
	public boolean saveBargain(Bargain bar,String sfzh,User userEntity) {
		
		def personInfor = PersonInfor.findByIdCard(sfzh);
		bar.personInfor = personInfor
		bar.company = userEntity.company
		if(bar.save(flush:true)){
			
			return true
		}else{
			bar.errors.each{
				println it
			}
			return false
		}
	}
	
}
