/**
 * @author rosten
 */
define([ "dojo/_base/connect", "dojo/_base/lang","dijit/registry", "dojo/_base/kernel","rosten/kernel/behavior" ], function(
		connect, lang,registry,kernel) {
	
	vacate_add = function(){
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("vacate", rosten.webPath + "/vacate/vacateAdd?companyId=" + companyId + "&userid=" + userid);
	};
	vacate_delete = function(){
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.read(rosten.webPath + "/vacate/vacateDelete", content,rosten.deleteCallback);
		};
	};
	vacate_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:vacate_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	vacate_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("vacate", rosten.webPath + "/vacate/vacateShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
		rosten.kernel.getGrid().clearSelected();
	};
	
	/*
	 * 此功能默认必须存在
	 */
	show_attEndanceNaviEntity = function(oString) {
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		var userid = rosten.kernel.getUserInforByKey("idnumber");
		
		switch (oString) {
		case "staffAskFor":
            var naviJson = {
                identifier : oString,
                actionBarSrc : rosten.webPath + "/vacateAction/vacateView?userId=" + userid,
                gridSrc : rosten.webPath + "/vacate/vacateGrid?companyId=" + companyId
            };
            rosten.kernel.addRightContent(naviJson);

            var rostenGrid = rosten.kernel.getGrid();
            break;
		case "askForStatic":
			require(["rosten/app/ChartManage"],function(ChartManage){
				rosten.kernel.setHref(rosten.webPath + "/vacate/askForStatic", oString , ChartManage.addAskForChart);
        	});
			
            break;
		}
       
		
	}
	connect.connect("show_naviEntity", show_attEndanceNaviEntity);
});
