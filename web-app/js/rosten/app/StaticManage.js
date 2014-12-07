/**
 * @author rosten
 */
define([ "dojo/_base/connect", "dojo/_base/lang","dijit/registry", "dojo/_base/kernel",
         "rosten/kernel/behavior" ], function(
		connect, lang,registry,kernel) {
	
	//员工按用工性质统计
	more_staffByCategory = function(){
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("more_staffByCategory", rosten.webPath + "/statistics/more_staffByCategory?companyId=" + companyId);
	};
	
	//员工按年龄段统计
	more_staffByAge = function(){
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("more_staffByAge", rosten.webPath + "/statistics/more_staffByAge?companyId=" + companyId);
	};
	
	//员工人数按部门统计
	more_staffByDepart = function(){
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("more_staffByDepart", rosten.webPath + "/statistics/more_staffByDepart?companyId=" + companyId);
	};
	
	/*
	 * 此功能默认必须存在
	 */
	show_staticNaviEntity = function(oString) {
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		var userid = rosten.kernel.getUserInforByKey("idnumber");
		
		switch (oString) {
		case "static":
            rosten.kernel.setHref(rosten.webPath + "/statistics/payweal?companyId=" + companyId, oString);
            break;
		case "staticDesign":
			demo_static(oString);
            break;
		
		}
		
	}
	connect.connect("show_naviEntity", show_staticNaviEntity);
});
