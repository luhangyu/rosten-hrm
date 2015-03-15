/**
 * @author rosten
 */
define([ "dojo/_base/connect", "dojo/_base/lang","dijit/registry", "dojo/_base/kernel","dojo/date/stamp","rosten/kernel/behavior" ], function(
		connect, lang,registry,kernel,datestamp) {
	var flowCode = "travel";
	
	travel_search = function(){
		var content = {};
		
		switch(rosten.kernel.navigationEntity) {
		default:
			var applyNum = registry.byId("s_applyNum");
			if(applyNum.get("value")!=""){
				content.applyNum = applyNum.get("value");
			}
			
			var travelDate = registry.byId("s_travelDate");
			if(travelDate.get("value")!=""){
				content.travelDate = datestamp.toISOString(travelDate.get("value"),{selector: "date"});
			}
			
			var status = registry.byId("s_status");
			if(status.get("value")!=""){
				content.status = status.get("value");
			}
			break;
		}
		rosten.kernel.refreshGrid(rosten.kernel.getGrid().defaultUrl, content);
	};
	travel_resetSearch = function(){
		switch(rosten.kernel.navigationEntity) {
		default:
			registry.byId("s_applyNum").set("value","");
			registry.byId("s_travelDate").set("value"," ");
			registry.byId("s_status").set("value","");
			break;
		}	
		
		rosten.kernel.refreshGrid();
	};
	travel_changeStatus = function(){
		
	};
	travel_changeUser = function(){
			
	};
	travel_formatTitle = function(value,rowIndex){
		return "<a href=\"javascript:travel_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	travel_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("travel", rosten.webPath + "/travel/travelShow/" + unid + "?userid=" + userid + "&companyId=" + companyId+ "&flowCode=" + flowCode);
		rosten.kernel.getGrid().clearSelected();
	};
	
	add_travel = function() {
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("travel", rosten.webPath + "/travel/travelAdd?companyId=" + companyId + "&userid=" + userid+ "&flowCode=" + flowCode);
    };
	change_travel = function() {
		var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		var unid = rosten.getGridUnid("single");
		if (unid == "")
			return;
		rosten.openNewWindow("travel", rosten.webPath + "/travel/travelShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
	};
	read_travel = function() {
		change_travel();
	};
	delete_travel = function() {
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.readNoTime(rosten.webPath + "/travel/travelDelete", content,rosten.deleteCallback);
		};
	};
    
	/*
	 * 此功能默认必须存在
	 */
	show_travelNaviEntity = function(oString) {
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		var userid = rosten.kernel.getUserInforByKey("idnumber");
		
		switch (oString) {
		case "travelConfigManage":
			rosten.kernel.setHref(rosten.webPath + "/travel/travelConfig", oString);
            break;
		case "myTravelManage":
			var naviJson = {
				identifier : oString,
				actionBarSrc : rosten.webPath + "/travelAction/travelView",
				searchSrc:rosten.webPath + "/travel/searchView?companyId=" + companyId + "&flowCode=" + flowCode,
				gridSrc : rosten.webPath + "/travel/travelGrid?companyId=" + companyId + "&userId=" + userid + "&type=person"
			};
			rosten.kernel.addRightContent(naviJson);
			break;
		case "allTravelManage":
			var naviJson = {
				identifier : oString,
				actionBarSrc : rosten.webPath + "/travelAction/allTravelView?userId=" + userid,
				searchSrc:rosten.webPath + "/travel/searchView?companyId=" + companyId + "&flowCode=" + flowCode,
				gridSrc : rosten.webPath + "/travel/travelGrid?companyId=" + companyId + "&userId=" + userid + "&type=all"
			};
			rosten.kernel.addRightContent(naviJson);
			break;
		}
		
	};
	connect.connect("show_naviEntity", show_travelNaviEntity);
});
