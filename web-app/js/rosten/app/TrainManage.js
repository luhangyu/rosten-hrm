/**
 * @author rosten
 */
define([ "dojo/_base/connect", "dojo/_base/lang","dijit/registry", "dojo/_base/kernel","rosten/kernel/behavior" ], function(
		connect, lang,registry,kernel) {
	
	trainMessage_add = function(){
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("trainMessage", rosten.webPath + "/train/trainMessageAdd?companyId=" + companyId + "&userid=" + userid+ "&flowCode=trainCourse");
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
		rosten.openNewWindow("trainCourse", rosten.webPath + "/train/trainMessageShow/" + unid + "?userid=" + userid + "&companyId=" + companyId + "&flowCode=trainCourse");
		rosten.kernel.getGrid().clearSelected();
	};
	trainCourse_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:trainCourse_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	trainCourse_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("trainCourse", rosten.webPath + "/train/trainCourseShow/" + unid + "?userid=" + userid + "&companyId=" + companyId+ "&flowCode=trainCourse");
		rosten.kernel.getGrid().clearSelected();
	};
	trainCourse_add = function(){
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("trainCourse", rosten.webPath + "/train/trainCourseAdd?companyId=" + companyId + "&userid=" + userid + "&flowCode=trainCourse");
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
		rosten.openNewWindow("trainCourse", rosten.webPath + "/train/trainCourseShow/" + unid + "?userid=" + userid + "&companyId=" + companyId+ "&flowCode=trainCourse");
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
	
	degreeStudy_add = function(){
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("DegreeStudy", rosten.webPath + "/train/degreeStudyAdd?companyId=" + companyId + "&userid=" + userid+ "&flowCode=degree");
	};
	degreeStudy_delete = function(){
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.read(rosten.webPath + "/train/degreeStudyDelete", content,rosten.deleteCallback);
		};
	};
	degreeStudy_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:degreeStudy_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	degreeStudy_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("degreeStudy", rosten.webPath + "/train/degreeStudyShow/" + unid + "?userid=" + userid + "&companyId=" + companyId+ "&flowCode=degree");
		rosten.kernel.getGrid().clearSelected();
	};
	
	
	forgeinStudy_add = function(){
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("forgeinStudy", rosten.webPath + "/train/forgeinStudyAdd?companyId=" + companyId + "&userid=" + userid+ "&flowCode=forgeinStudy");
	};
	
	forgeinStudy_delete = function(){
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.read(rosten.webPath + "/train/forgeinStudyDelete", content,rosten.deleteCallback);
		};
	};
	
	forgeinStudy_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:forgeinStudy_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	
	forgeinStudy_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("forgeinStudy", rosten.webPath + "/train/forgeinStudyShow/" + unid + "?userid=" + userid + "&companyId=" + companyId+ "&flowCode=forgeinStudy");
		rosten.kernel.getGrid().clearSelected();
	};
	
	//培训班
	trainCourse_search = function(){
		var content = {};
		
		switch(rosten.kernel.navigationEntity) {
		default:
			var courseName = registry.byId("s_courseName");
			if(courseName.get("value")!=""){
				content.courseName = courseName.get("value");
			}
			break;
		}
		
		rosten.kernel.refreshGrid(rosten.kernel.getGrid().defaultUrl, content);
	};
	
	trainCourse_resetSearch = function(){
		switch(rosten.kernel.navigationEntity) {
		default:
			registry.byId("s_courseName").set("value","");
			break;
		}	
		
		rosten.kernel.refreshGrid();
	};
	
	//出国进修
	forgeinStudy_search = function(){
		var content = {};
		
		switch(rosten.kernel.navigationEntity) {
		default:
			var appYear = registry.byId("s_appYear");
			if(appYear.get("value")!=""){
				content.appYear = appYear.get("value");
			}
			break;
		}
		
		rosten.kernel.refreshGrid(rosten.kernel.getGrid().defaultUrl, content);
	};
	
	forgeinStudy_resetSearch = function(){
		switch(rosten.kernel.navigationEntity) {
		default:
			registry.byId("s_appYear").set("value","");
			break;
		}	
		
		rosten.kernel.refreshGrid();
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
                searchSrc:rosten.webPath + "/train/trainCourseSearchView",
                gridSrc : rosten.webPath + "/train/trainCourseGrid?companyId=" + companyId
            };
            rosten.kernel.addRightContent(naviJson);

            var rostenGrid = rosten.kernel.getGrid();
            break;
		case "trainMessage":
            var naviJson = {
                identifier : oString,
                actionBarSrc : rosten.webPath + "/trainAction/trainMessageView?userId=" + userid,
                gridSrc : rosten.webPath + "/train/trainMessageGrid?companyId=" + companyId
            };
            rosten.kernel.addRightContent(naviJson);

            var rostenGrid = rosten.kernel.getGrid();
            break;
		case "degreeStudy":
            var naviJson = {
                identifier : oString,
                actionBarSrc : rosten.webPath + "/trainAction/degreeStudyView?userId=" + userid,
                gridSrc : rosten.webPath + "/train/degreeStudyGrid?companyId=" + companyId
            };
            rosten.kernel.addRightContent(naviJson);

            var rostenGrid = rosten.kernel.getGrid();
            break;
		case "forgeinStudy":
	        var naviJson = {
	            identifier : oString,
	            actionBarSrc : rosten.webPath + "/trainAction/forgeinStudyView?userId=" + userid,
	            searchSrc:rosten.webPath + "/train/forgeinStudySearchView",
	            gridSrc : rosten.webPath + "/train/forgeinStudyGrid?companyId=" + companyId
	        };
	        rosten.kernel.addRightContent(naviJson);
	
	        var rostenGrid = rosten.kernel.getGrid();
	        break;
		}
		
	}
	connect.connect("show_naviEntity", show_trainNaviEntity);
});
