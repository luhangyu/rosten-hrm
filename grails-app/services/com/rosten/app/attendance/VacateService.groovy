package com.rosten.app.attendance

import com.rosten.app.share.ShareService;
import com.rosten.app.util.GridUtil

class VacateService {
	def shareService
	
	//2015-3-16------------------增加考勤记录----------------------------------------------------
	def getWorkCheckListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new WorkCheck())
	}
	
	def getWorkCheckDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllWorkCheck(offset,max,params.company,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	
	def getAllWorkCheck ={offset,max,company,searchArgs->
		def c = WorkCheck.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
			order("createDate", "desc")
		}
		return c.list(pa,query)
	}
	
	def getWorkCheckCount ={company,searchArgs->
		def c = WorkCheck.createCriteria()
		def query = {
			 eq("company",company)
			 
			 searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
		}
		return c.count(query)
	}
	
	//2015-3-13----------------增加出勤解释单-----------------------------------------------------
	def getVacateExplainListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new VacateExplain())
	}
	
	def getVacateExplainDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllVacateExplain(offset,max,params.company,params.user,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	
	def getAllVacateExplain ={offset,max,company,user,searchArgs->
		def c = VacateExplain.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				if(k.equals("month")){
					Calendar calendar = Calendar.getInstance()
					def month1 = new GregorianCalendar(calendar.get(Calendar.YEAR),v,1)
					def month2 = new GregorianCalendar(calendar.get(Calendar.YEAR),v + 1,1)
					
					between("explainDate", month1.time, month2.time)
					
				}else{
					like(k,"%" + v + "%")
				}
			}
			order("createDate", "desc")
		}
		return c.list(pa,query)
	}
	
	def getVacateExplainCount ={company,user,searchArgs->
		def c = VacateExplain.createCriteria()
		def query = {
			 eq("company",company)
			 
			 searchArgs.each{k,v->
				if(k.equals("month")){
					Calendar calendar = Calendar.getInstance()
					def month1 = new GregorianCalendar(calendar.get(Calendar.YEAR),v,1)
					def month2 = new GregorianCalendar(calendar.get(Calendar.YEAR),v + 1,1)
					
					between("explainDate", month1.time, month2.time)
					
				}else{
					like(k,"%" + v + "%")
				}
			}
		}
		return c.count(query)
	}
	//-------------------------------------------------------------------------------
	
	def getVacateListLayout ={
		def gridUtil = new GridUtil()
		return gridUtil.buildLayoutJSON(new Vacate())
	}
	
	def getVacateDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllVacate(offset,max,params.company,params.user,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	
	def getAllVacate ={offset,max,company,user,searchArgs->
		def c = Vacate.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
			//2015-4-12--------------只显示当前处理者本人的信息
			eq("currentUser",user)
			
//			if(!user.getAllRolesValue().contains("管理员") && !"admin".equals(user.getUserType())){
//				readers{
//					eq("id",user.id)
//				}
//			}
			order("createDate", "desc")
		}
		return c.list(pa,query)
	}
	
	def getVacateCount ={company,user,searchArgs->
		def c = Vacate.createCriteria()
		def query = {
			 eq("company",company)
			 
			 //2015-4-12--------------只显示当前处理者本人的信息
			 eq("currentUser",user)
			 
//			 if(!user.getAllRolesValue().contains("管理员") && !"admin".equals(user.getUserType())){
//				 readers{
//					 eq("id",user.id)
//				 }
//			 }
			 searchArgs.each{k,v->
				 like(k,"%" + v + "%")
			 }
		}
		return c.count(query)
	}
	
	
	def getAllAskForDataStore ={params,searchArgs->
		Integer offset = (params.offset)?params.offset.toInteger():0
		Integer max = (params.max)?params.max.toInteger():15
		def propertyList = getAllAskFor(offset,max,params.company,params.user,params.departs,searchArgs)

		def gridUtil = new GridUtil()
		return gridUtil.buildDataList("id","title",propertyList,offset)
	}
	
	def getAllVacateCount ={company,user,departs,searchArgs->
		def c = Vacate.createCriteria()
		def query = {
			 eq("company",company)
			 searchArgs.each{k,v->
				 like(k,"%" + v + "%")
			 }
			 
			 if(!shareService.checkAdmin(user,"应用管理员") && !shareService.checkAdmin(user,"请假管理员")){	//管理员可以查看所有数据
				 if(departs && departs.size()>0){
					 'in'("drafterDepart",departs)
				 }else{
					 readers{
						 eq("id",user.id)
					 }
				 }
			 }
			 
		 }
		return c.count(query)
	}
	
	def getAllAskFor ={offset,max,company,user,departs,searchArgs->
		def c = Vacate.createCriteria()
		def pa=[max:max,offset:offset]
		def query = {
			eq("company",company)
			searchArgs.each{k,v->
				like(k,"%" + v + "%")
			}
			
			if(!shareService.checkAdmin(user,"应用管理员") && !shareService.checkAdmin(user,"请假管理员")){	//管理员可以查看所有数据
				if(departs && departs.size()>0){
					'in'("drafterDepart",departs)
				}else{
					readers{
						eq("id",user.id)
					}
				}
			}
			
			order("createDate", "desc")
		}
		return c.list(pa,query)
	}
	
}
