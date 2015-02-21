/**
 * @author rosten
 */
define([ "dojo/_base/connect", "dojo/_base/lang","dijit/registry", "dojo/_base/kernel","rosten/kernel/behavior" ], function(
		connect, lang,registry,kernel) {
	
	bargain_search = function(){
		var content = {};
		
		var bargainSerialNo = registry.byId("s_bargainSerialNo");
		if(bargainSerialNo.get("value")!=""){
			content.bargainSerialNo = bargainSerialNo.get("value");
		}
		
		var chinaName = registry.byId("s_chinaName");
		if(chinaName.get("value")!=""){
			content.chinaName = chinaName.get("value");
		}
		
		var bargainTime = registry.byId("s_bargainTime");
		if(bargainTime.get("value")!=""){
			content.bargainTime = bargainTime.get("value");
		}
		
		switch(rosten.kernel.navigationEntity) {
		default:
			rosten.kernel.refreshGrid(rosten.kernel.getGrid().defaultUrl, content);
			break;
		}
		
		
	};
	
	bargain_resetSearch = function(){
		switch(rosten.kernel.navigationEntity) {
		default:
			registry.byId("s_bargainSerialNo").set("value","");
			registry.byId("s_chinaName").set("value","");
			registry.byId("s_bargainTime").set("value","");
			rosten.kernel.refreshGrid();
			break;
		}	
	};
	
	bargain_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:bargain_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	bargain_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("bargain", rosten.webPath + "/staff/bargainShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
		rosten.kernel.getGrid().clearSelected();
	};
	add_bargain = function() {
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("bargain", rosten.webPath + "/staff/bargainAdd?companyId=" + companyId + "&userid=" + userid + "&flowCode=staffDepartChange");
    };
	change_bargain = function() {
		var unid = rosten.getGridUnid("single");
		if (unid == "")
			return;
		var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("bargain", rosten.webPath + "/staff/bargainShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
	};
	read_bargain = function() {
		change_bargain();
	};
	delete_bargain = function() {
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.readNoTime(rosten.webPath + "/staff/bargainDelete", content,
					rosten.deleteCallback);
		};
	};
	
	import_bargain = function(){
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.kernel.createRostenShowDialog(rosten.webPath + "/staff/importBargain/"+ companyId, {
           onLoadFunction : function() {

           }
       });
	};
	
	/*
	 * 此功能默认必须存在
	 */
	show_bargainNaviEntity = function(oString) {
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		var userid = rosten.kernel.getUserInforByKey("idnumber");
		
		switch (oString) {
		case "bargainManage":
            var naviJson = {
                identifier : oString,
                actionBarSrc : rosten.webPath + "/staffAction/bargainView?userId=" + userid,
                searchSrc:rosten.webPath + "/bargain/bargainSearchView",
                gridSrc : rosten.webPath + "/staff/bargainGrid?companyId=" + companyId
            };
            rosten.kernel.addRightContent(naviJson);

            var rostenGrid = rosten.kernel.getGrid();
            break;
		case "bargainConfig":
			rosten.kernel.setHref(rosten.webPath + "/bargain/bargainConfig", oString);
            break;
		}
		
	};
	connect.connect("show_naviEntity", show_bargainNaviEntity);
});
