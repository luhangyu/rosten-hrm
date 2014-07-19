/**
 * @author rosten
 */
define([ "dojo/_base/connect", "dojo/_base/lang","dijit/registry", "dojo/_base/kernel",
         "rosten/kernel/behavior" ], function(
		connect, lang,registry,kernel) {
	
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
		}
		
	}
	connect.connect("show_naviEntity", show_staticNaviEntity);
});
