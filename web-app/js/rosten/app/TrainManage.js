/**
 * @author rosten
 */
define([ "dojo/_base/connect", "dojo/_base/lang","dijit/registry", "dojo/_base/kernel","rosten/kernel/behavior" ], function(
		connect, lang,registry,kernel) {
	
	trainMessage_add = function(){
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("trainMessage", rosten.webPath + "/train/trainMessageAdd?companyId=" + companyId + "&userid=" + userid);
	};
	trainMessage_delete = function(){
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.read(rosten.webPath + "/train/trainMessageDelete", content,rosten.deleteCallback);
		};
	};
	trainMessage_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:trainMessage_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	trainMessage_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("trainCourse", rosten.webPath + "/train/trainMessageShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
		rosten.kernel.getGrid().clearSelected();
	};
	trainCourse_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:trainCourse_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	trainCourse_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("trainCourse", rosten.webPath + "/train/trainCourseShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
		rosten.kernel.getGrid().clearSelected();
	};
	trainCourse_add = function(){
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("trainCourse", rosten.webPath + "/train/trainCourseAdd?companyId=" + companyId + "&userid=" + userid);
	};
	trainCourse_read = function(){
		trainCourse_change();
	};
	trainCourse_change = function(){
		var unid = rosten.getGridUnid("single");
		if (unid == "")
			return;
		var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("trainCourse", rosten.webPath + "/train/trainCourseyShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
	};
	trainCourse_delete = function(){
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.read(rosten.webPath + "/train/trainCourseDelete", content,rosten.deleteCallback);
		};
	};
	
	/*
	 * 此功能默认必须存在
	 */
	show_trainNaviEntity = function(oString) {
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		var userid = rosten.kernel.getUserInforByKey("idnumber");
		
		switch (oString) {
		case "trainCourse":
            var naviJson = {
                identifier : oString,
                actionBarSrc : rosten.webPath + "/trainAction/trainCourseView?userId=" + userid,
                gridSrc : rosten.webPath + "/train/trainCourseGrid?companyId=" + companyId
            };
            rosten.kernel.addRightContent(naviJson);

            var rostenGrid = rosten.kernel.getGrid();
            break;
		}
		
	}
	connect.connect("show_naviEntity", show_trainNaviEntity);
});
