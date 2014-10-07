/**
 * @author rosten
 */
define([ "dojo/_base/connect", "dojo/_base/lang","dijit/registry", "dojo/_base/kernel","rosten/kernel/behavior" ], function(
		connect, lang,registry,kernel) {
	
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
		change_staffDepartChange();
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
                gridSrc : rosten.webPath + "/staff/bargainGrid?companyId=" + companyId
            };
            rosten.kernel.addRightContent(naviJson);

            var rostenGrid = rosten.kernel.getGrid();
            break;
		}
		
	}
	connect.connect("show_naviEntity", show_bargainNaviEntity);
});
