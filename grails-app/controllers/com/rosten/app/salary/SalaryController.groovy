package com.rosten.app.salary
import grails.converters.JSON
import com.rosten.app.system.Company
import com.rosten.app.util.Util
import com.rosten.app.staff.PersonInfor
import com.rosten.app.util.SystemUtil
import com.rosten.app.system.User
import groovy.sql.Sql
import com.rosten.app.export.ExcelImport
class SalaryController {

 def salaryService
 def springSecurityService
 def dataSource
 
 def quartersGrid ={
	 def json=[:]
	 def company = Company.get(params.companyId)
	 if(params.refreshHeader){
		 json["gridHeader"] =salaryService.getQuartersListLayout()
	 }
	 //增加查询条件
	 def searchArgs =[:]
	if(params.quaName && !"".equals(params.quaName)) searchArgs["quaName"] = params.quaName
	 if(params.refreshData){
		 def args =[:]
		 int perPageNum = Util.str2int(params.perPageNum)
		 int nowPage =  Util.str2int(params.showPageNum)
		 
		 args["offset"] = (nowPage-1) * perPageNum
		 args["max"] = perPageNum
		 args["company"] = company
		 json["gridData"] = salaryService.getQuartersListDataStore(args,searchArgs)
		 
	 }
	 if(params.refreshPageControl){
		 def total = salaryService.getQuarters(company,searchArgs)
		 json["pageControl"] = ["total":total.toString()]
	 }
	 render json as JSON
	}
 
 def quartersSearchView ={
	 def model =[:]
	 render(view:'/salary/quartersSearch',model:model)
 }
 
 /**
  * 增加
  */
 def quartersAdd ={
	 redirect(action:"quartersShow",params:params)
 }
 
 def quartersShow ={
	 def model =[:]
	 
	 def company = Company.get(params.companyId)
	 def quarters = new Quarters()
	 if(params.id){
		 quarters = Quarters.get(params.id)
	 }
	 
	 model["company"] = company
	 model["quarters"] = quarters
	 
	 render(view:'/salary/quarters',model:model)
 }
 
 def quartersSave ={
	 def json=[:]
	 def quarters = new Quarters()
	 if(params.id && !"".equals(params.id)){
		 quarters = Quarters.get(params.id)
	 }else{
		 if(params.companyId){
			 quarters.company = Company.get(params.companyId)
		 }
	 }
	 quarters.properties = params
	 quarters.clearErrors()
	 
	 if(quarters.save(flush:true)){
		 json["result"] = "true"
	 }else{
		 quarters.errors.each{
			 println it
		 }
		 json["result"] = "false"
	 }
	 render json as JSON
 }
 
 //删除
 def quartersDelete={
	 def ids = params.id.split(",")
	 def json
	 try{
		 ids.each{
			 def quarters = Quarters.get(it)
			 if(quarters){
				 quarters.delete(flush: true)
			 }
		 }
		 json = [result:'true']
	 }catch(Exception e){
		 json = [result:'error']
	 }
	 render json as JSON
 }
 
 
 def gearGrid ={
	 def json=[:]
	 def company = Company.get(params.companyId)
	 if(params.refreshHeader){
		 json["gridHeader"] =salaryService.getGearListLayout()
	 }
	 //增加查询条件
	 def searchArgs =[:]
	if(params.gearName && !"".equals(params.gearName)) searchArgs["gearName"] = params.gearName
	 if(params.refreshData){
		 def args =[:]
		 int perPageNum = Util.str2int(params.perPageNum)
		 int nowPage =  Util.str2int(params.showPageNum)
		 
		 args["offset"] = (nowPage-1) * perPageNum
		 args["max"] = perPageNum
		 args["company"] = company
		 json["gridData"] = salaryService.getGearListDataStore(args,searchArgs)
		 
	 }
	 if(params.refreshPageControl){
		 def total = salaryService.getGear(company,searchArgs)
		 json["pageControl"] = ["total":total.toString()]
	 }
	 render json as JSON
	}
 
 def gearSearchView ={
	 def model =[:]
	 render(view:'/salary/gearSearch',model:model)
 }
 
 /**
  * 增加
  */
 def gearAdd ={
	 redirect(action:"gearShow",params:params)
 }
 
 def gearShow ={
	 def model =[:]
	 
	 def company = Company.get(params.companyId)
	 def gear = new Gear()
	 if(params.id){
		 gear = Gear.get(params.id)
	 }
	 
	 model["company"] = company
	 model["gear"] = gear
	 
	 render(view:'/salary/gear',model:model)
 }
 
 def gearSave ={
	 def json=[:]
	 def gear = new Gear()
	 if(params.id && !"".equals(params.id)){
		 gear = Gear.get(params.id)
	 }else{
		 if(params.companyId){
			 gear.company = Company.get(params.companyId)
		 }
	 }
	 gear.properties = params
	 gear.clearErrors()
	 
	 if(gear.save(flush:true)){
		 json["result"] = "true"
	 }else{
		 gear.errors.each{
			 println it
		 }
		 json["result"] = "false"
	 }
	 render json as JSON
 }
 
 //删除
 def gearDelete={
	 def ids = params.id.split(",")
	 def json
	 try{
		 ids.each{
			 def gear =Gear.get(it)
			 if(gear){
				 gear.delete(flush: true)
			 }
		 }
		 json = [result:'true']
	 }catch(Exception e){
		 json = [result:'error']
	 }
	 render json as JSON
 }
 
 def radixGrid ={
	 def json=[:]
	 def company = Company.get(params.companyId)
	 if(params.refreshHeader){
		 json["gridHeader"] =salaryService.getRadixListLayout()
	 }
	 //增加查询条件
	 def searchArgs =[:]
	if(params.category && !"".equals(params.category)) searchArgs["category"] = params.category
	 if(params.refreshData){
		 def args =[:]
		 int perPageNum = Util.str2int(params.perPageNum)
		 int nowPage =  Util.str2int(params.showPageNum)
		 
		 args["offset"] = (nowPage-1) * perPageNum
		 args["max"] = perPageNum
		 args["company"] = company
		 json["gridData"] = salaryService.getRadixListDataStore(args,searchArgs)
		 
	 }
	 if(params.refreshPageControl){
		 def total = salaryService.getRadix(company,searchArgs)
		 json["pageControl"] = ["total":total.toString()]
	 }
	 render json as JSON
	}
 
 def radixSearchView ={
	 def model =[:]
	 render(view:'/salary/radixSearch',model:model)
 }
 
 /**
  * 增加
  */
 def radixAdd ={
	 redirect(action:"radixShow",params:params)
 }
 
 def radixShow ={
	 def model =[:]
	 
	 def company = Company.get(params.companyId)
	 def radix = new Radix()
	 if(params.id){
		 radix = Radix.get(params.id)
	 }
	 
	 model["company"] = company
	 model["radix"] = radix
	 
	 render(view:'/salary/radix',model:model)
 }
 
 def radixSave ={
	 def json=[:]
	 def radix = new Radix()
	 if(params.id && !"".equals(params.id)){
		 radix = Radix.get(params.id)
	 }else{
		 if(params.companyId){
			 radix.company = Company.get(params.companyId)
		 }
	 }
	 radix.properties = params
	 radix.clearErrors()
	 
	 if(radix.save(flush:true)){
		 json["result"] = "true"
	 }else{
		 radix.errors.each{
			 println it
		 }
		 json["result"] = "false"
	 }
	 render json as JSON
 }
 
 //删除
 def radixDelete={
	 def ids = params.id.split(",")
	 def json
	 try{
		 ids.each{
			 def radix =Radix.get(it)
			 if(radix){
				 radix.delete(flush: true)
			 }
		 }
		 json = [result:'true']
	 }catch(Exception e){
		 json = [result:'error']
	 }
	 render json as JSON
 }
 
 def riskGoldGrid ={
	 def json=[:]
	 def company = Company.get(params.companyId)
	 if(params.refreshHeader){
		 json["gridHeader"] =salaryService.getRiskGoldListLayout()
	 }
	 //增加查询条件
	 def searchArgs =[:]
	if(params.chinaName && !"".equals(params.chinaName)) searchArgs["chinaName"] = params.chinaName
	 if(params.refreshData){
		 def args =[:]
		 int perPageNum = Util.str2int(params.perPageNum)
		 int nowPage =  Util.str2int(params.showPageNum)
		 
		 args["offset"] = (nowPage-1) * perPageNum
		 args["max"] = perPageNum
		 args["company"] = company
		 json["gridData"] = salaryService.getRiskGoldListDataStore(args,searchArgs)
		 
	 }
	 if(params.refreshPageControl){
		 def total = salaryService.getRiskGoldCount(company,searchArgs)
		 json["pageControl"] = ["total":total.toString()]
	 }
	 render json as JSON
	}
 
 def riskGoldSearchView ={
	 def model =[:]
	 render(view:'/salary/riskGoldSearch',model:model)
 }
 
 /**
  * 增加
  */
 def riskGoldAdd ={
	 redirect(action:"riskGoldShow",params:params)
 }
 
 def riskGoldShow ={
	 def model =[:]
	 
	 def company = Company.get(params.companyId)
	 def riskGold = new RiskGold()
	 if(params.id){
		 riskGold = RiskGold.get(params.id)
	 }
	 
	 model["company"] = company
	 model["riskGold"] = riskGold
	 
	 render(view:'/salary/riskGold',model:model)
 }
 
 def riskGoldConfig ={
	 def model = [:]
	 def user = springSecurityService.getCurrentUser()
	 
	 def riskGold = RiskGold.findWhere(company:user.company)
	 if(riskGold==null) {
		 riskGold = new RiskGold()
	 }
	
	 model["company"] = user.company
	 model["riskGold"] = riskGold
	 
	 render(view:'/salary/riskGoldConfig',model:model)
 }
 
 def riskGoldSave ={
	 def json=[:]
	 def riskGold = new RiskGold()
	 if(params.id && !"".equals(params.id)){
		 riskGold = RiskGold.get(params.id)
	 }
	 riskGold.properties = params
	 riskGold.clearErrors()
	 
	 riskGold.gjjBl = Util.str2int(params.gjjBl)
	 riskGold.sybxBl = Util.str2int(params.sybxBl)
	 riskGold.ylbxBl = Util.str2int(params.ylbxBl)
	 riskGold.syubxBl = Util.str2int(params.syubxBl)
	 riskGold.ylaobxBl = Util.str2int(params.ylaobxBl)
	 
	 if(riskGold.save(flush:true)){
		 json["result"] = true
		 json["coonfigId"] = riskGold.id
		 json["companyId"] = riskGold.company.id
	 }else{
		 riskGold.errors.each{
			 println it
		 }
		 json["result"] = false
	 }
	 render json as JSON
 }
 
 //删除
 def riskGoldDelete={
	 def ids = params.id.split(",")
	 def json
	 try{
		 ids.each{
			 def riskGold =RiskGold.get(it)
			 if(riskGold){
				 riskGold.delete(flush: true)
			 }
		 }
		 json = [result:'true']
	 }catch(Exception e){
		 json = [result:'error']
	 }
	 render json as JSON
 }
 
 def billConfigGrid ={
	 def json=[:]
	 def company = Company.get(params.companyId)
	 if(params.refreshHeader){
		 json["gridHeader"] =salaryService.getBillConfigListLayout()
	 }
	 //增加查询条件
	 def searchArgs =[:]
	if(params.chinaName && !"".equals(params.chinaName)) searchArgs["chinaName"] = params.chinaName
	 if(params.refreshData){
		 def args =[:]
		 int perPageNum = Util.str2int(params.perPageNum)
		 int nowPage =  Util.str2int(params.showPageNum)
		 
		 args["offset"] = (nowPage-1) * perPageNum
		 args["max"] = perPageNum
		 args["company"] = company
		 json["gridData"] = salaryService.getBillConfigListDataStore(args,searchArgs)
		 
	 }
	 if(params.refreshPageControl){
		 def total = salaryService.getBillConfigCount(company,searchArgs)
		 json["pageControl"] = ["total":total.toString()]
	 }
	 render json as JSON
	}
 
 def billConfigSearchView ={
	 def model =[:]
	 render(view:'/salary/billConfigSearch',model:model)
 }
 
 /**
  * 增加
  */
 def billConfigAdd ={
	 redirect(action:"billConfigShow",params:params)
 }
 
 def billConfigShow ={
	 def model =[:]
	 
	 def company = Company.get(params.companyId)
	 def billConfig = new SalaryBillConfig()
	 if(params.id){
		 billConfig = SalaryBillConfig.get(params.id)
	 }else{
	 def user = springSecurityService.getCurrentUser()
	 def riskGold = RiskGold.findWhere(company:user.company)
	 if(null!=riskGold) {
		billConfig.gjj = riskGold.getGjj()
		billConfig.gjjBl = riskGold.getGjjBl()
		billConfig.sybx = riskGold.getSybx()
		billConfig.sybxBl = riskGold.getSybxBl()
		billConfig.ylbx = riskGold.getYlbx()
		billConfig.ylbxBl = riskGold.getYlbxBl()
		billConfig.syubx = riskGold.getSyubx()
		billConfig.syubxBl = riskGold.getSyubxBl()
		billConfig.ylaobx = riskGold.getYlaobx()
		billConfig.ylaobxBl = riskGold.getYlaobxBl()
	 }
	 }
	 
	 def currentUser = springSecurityService.getCurrentUser()
	 
	 //级别
	 def quaList = Quarters.findAllByCompany(currentUser.company)
	 
	 //档位
	 def gearList = Gear.findAllByCompany(currentUser.company)
	 
	 model["company"] = company
	 model["billConfig"] = billConfig
	 
	 model["quaList"] = quaList
	 model["gearList"] = gearList
	 
	 render(view:'/salary/billConfig',model:model)
 }
 
 def billConfigSave ={
	 def json=[:]
	 def billConfig = new SalaryBillConfig()
	 if(params.id && !"".equals(params.id)){
		 billConfig = SalaryBillConfig.get(params.id)
	 }else{
		 if(params.companyId){
			 billConfig.company = Company.get(params.companyId)
		 }
	 }
	 billConfig.properties = params
	 billConfig.clearErrors()
	 
	 billConfig.gjjBl = Util.str2int(params.gjjBl)
	 billConfig.sybxBl = Util.str2int(params.sybxBl)
	 billConfig.ylbxBl = Util.str2int(params.ylbxBl)
	 billConfig.syubxBl = Util.str2int(params.syubxBl)
	 billConfig.ylaobxBl = Util.str2int(params.ylaobxBl)
	 
	 billConfig.personInfor = PersonInfor.get(params.personInforId)
	 
	 billConfig.quarters = Quarters.get(params.quartersId)
	 
	 billConfig.gear = Gear.get(params.gearId)
	 
	 if(billConfig.save(flush:true)){
		 json["result"] = "true"
	 }else{
		 billConfig.errors.each{
			 println it
		 }
		 json["result"] = "false"
	 }
	 render json as JSON
 }
 
 //删除
 def billConfigDelete={
	 def ids = params.id.split(",")
	 def json
	 try{
		 ids.each{
			 def billConfig =SalaryBillConfig.get(it)
			 if(billConfig){
				 billConfig.delete(flush: true)
			 }
		 }
		 json = [result:'true']
	 }catch(Exception e){
		 json = [result:'error']
	 }
	 render json as JSON
 }
 def salaryBillDelete ={
	 def ids = params.id.split(",")
	 def json
	 try{
		 ids.each{
			 def salarySlip =SalarySlip.get(it)
			 if(salarySlip){
				 salarySlip.delete(flush: true)
			 }
		 }
		 json = [result:'true']
	 }catch(Exception e){
		 json = [result:'error']
	 }
	 render json as JSON
 }
 
 def salaryBillSearchView ={
	 def model =[:]
	 render(view:'/salary/billConfigSearch',model:model)
 }
 
 def salaryBillGrid ={
	 def json=[:]
	 def company = Company.get(params.companyId)
	 if(params.refreshHeader){
		 json["gridHeader"] =salaryService.getSalaryBillListLayout()
	 }
	 //增加查询条件
	 def searchArgs =[:]
	if(params.chinaName && !"".equals(params.chinaName))
	   searchArgs["chinaName"] = params.chinaName
	if(params.year && !"".equals(params.year))
	   searchArgs["year"] = params.year
	if(params.month && !"".equals(params.month))
	   searchArgs["month"] = params.month
	
	 if(params.refreshData){
		 def args =[:]
		 int perPageNum = Util.str2int(params.perPageNum)
		 int nowPage =  Util.str2int(params.showPageNum)
		 
		 args["offset"] = (nowPage-1) * perPageNum
		 args["max"] = perPageNum
		 args["company"] = company
		 json["gridData"] = salaryService.getSalaryBillListDataStore(args,searchArgs)
		 
	 }
	 if(params.refreshPageControl){
		 def total = salaryService.getSalaryBillCount(company,searchArgs)
		 json["pageControl"] = ["total":total.toString()]
	 }
	 render json as JSON
	}
 
 def importSalaryBill ={
	 def model =[:]
	 model["company"] = Company.get(params.id)
	 render(view:'/salary/importSalaryBill',model:model)
 }
 
 def importSalary={
	 def ostr
	 
	 SystemUtil sysUtil = new SystemUtil()
	 def currentUser = (User) springSecurityService.getCurrentUser()
	 def year =params.year
	 def month = params.month
	 def f = request.getFile("uploadedfile")
	 if (!f.empty) {
		 
		 def uploadPath
		 def companyPath = currentUser.company?.shortName
		 if(companyPath == null){
			 uploadPath = sysUtil.getUploadPath("template")+"/"
		 }else{
			 uploadPath = sysUtil.getUploadPath(currentUser.company.shortName + "/template") + "/"
		 }
		 
		 String name = f.getOriginalFilename()//获得文件原始的名称
		 def realName = sysUtil.getRandName(name)
		 f.transferTo(new File(uploadPath,realName))
		 
		 def excelimp = new ExcelImport()
		 def result = excelimp.salarysjdr(uploadPath,realName,currentUser,year,month)
		 if("true".equals(result)){
			 ostr ="<script>var _parent = window.parent;_parent.rosten.alert('导入成功').queryDlgClose=function(){_parent.rosten.kernel.hideRostenShowDialog();_parent.rosten.kernel.refreshGrid();}</script>"
		 }else{
			 ostr = "<script>window.parent.rosten.alert('导入失败');</script>"
		 }
	 }
	 
	 render ostr
 }
 
 def salaryShow={
		def model=[:]
		def user = User.get(params.userid)
		def personInfor = PersonInfor.findByUser(user)
		def items = SalarySlip.findAllByPersonInfor(personInfor,[sort: "month", order: "desc"])
		
//		def year = params.year
//		if(null==year){
//			year="2015"
//		}
//		def userid = params.userid
//		Sql sql = new Sql(dataSource)
//		def items = []
//		def seleSql = "select * from rs_sa_saslip where PERSON_INFOR_ID='"+userid+"' order by year ,month desc"
//		def vacateList = sql.eachRow(seleSql){
//			def item = ["year":it["year"],"month":it["month"],"ygwgz":it["ygwgz"],"yjxgz":it["yjxgz"],"glbt":it["glbt"],"gzxj":it["gzxj"],"zfbt":it["zfbt"]
//				,"khj":it["khj"],"yfje":it["yfje"],"grss":it["grss"],"gjj":it["gjj"],"sybx":it["sybx"],"ylaobx":it["ylaobx"],"ylbx":it["ylbx"]
//				,"cb":it["cb"],"wxyjxj":it["wxyjxj"],"sfje":it["sfje"]]
//			items<<item
//		}
		
		model["tableItem"] = items
		render(view:'/salary/salaryShow',model:model)
 }
}
