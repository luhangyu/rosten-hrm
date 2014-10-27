package com.rosten.app.staff

import com.rosten.app.util.GridUtil
import com.rosten.app.system.Company
import com.rosten.app.system.Depart
import com.rosten.app.system.User

class StaffService {
	
	public PersonInfor getPersonInfor(String id) {
		return PersonInfor.get(id)
	}
	
	public ContactInfor getContactInfor(PersonInfor person){
		return ContactInfor.findByPersonInfor(person)
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
			order("createDate", "desc")
			
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
	
	public boolean commonSave(PersonInfor entity,String departName,User userEntity) {
		
		def departEntity = Depart.findByDepartName(departName);
		entity.addToDeparts(departEntity)
		entity.company = userEntity.company
		
		if(entity.save(flush:true)){
			return true
		}else{
			entity.errors.each{
				println it
			}
			return false
		}
	}
	
}
