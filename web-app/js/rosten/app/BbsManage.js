/**
 * @author rosten
 */
define([ "dojo/_base/connect", "dijit/registry","rosten/util/general", "rosten/kernel/behavior" ], function(
		connect, registry,General) {
	
	var general = new General();
	
	bbs_changeStatus = function(){
			
			
	};
	bbs_changeUser = function(){
			
	};
	formatBbsLevel = function(value){
		if(value && value!=""){
			var imgs = general.splitString(value,",");
			var _values=""
			for(var i = 0; i < imgs.length; i ++){
				if(_values==""){
					_values = "<img style=\"margin-left:4px\" src=\"" + rosten.webPath + "/" + imgs[i] + "\" />";
				}else{
					_values += "<img style=\"margin-left:4px\" src=\"" + rosten.webPath + "/" + imgs[i] + "\" />";
				}
			}
			return _values;
		}else{
			return "";
		}
	};
	bbs_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:bbs_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	bbs_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("bbs", rosten.webPath + "/bbs/bbsShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
		rosten.kernel.getGrid().clearSelected();
	};
	add_bbs = function() {
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("bbs", rosten.webPath + "/bbs/bbsAdd?companyId=" + companyId + "&userid=" + userid);
    };
	change_bbs = function() {
		var unid = rosten.getGridUnid("single");
		if (unid == "")
			return;
		var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("bbs", rosten.webPath + "/bbs/bbsShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
	};
	read_bbs = function() {
		change_bbs();
	};
	delete_bbs = function() {
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.read(rosten.webPath + "/bbs/bbsDelete", content,
					rosten.deleteCallback);
		};
	};
    
	/*
	 * 此功能默认必须存在
	 */
	show_bbsNaviEntity = function(oString) {
		
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		var userid = rosten.kernel.getUserInforByKey("idnumber");
		
		switch (oString) {
		case "allbbsManage":
			var naviJson = {
				identifier : oString,
				actionBarSrc : rosten.webPath + "/bbsAction/allbbsView?userId=" + userid,
				gridSrc : rosten.webPath + "/bbs/bbsGrid?companyId=" + companyId + "&userId=" + userid + "&type=all"
			};
			rosten.kernel.addRightContent(naviJson);
			break;
		case "newbbsManage":
			var naviJson = {
				identifier : oString,
				actionBarSrc : rosten.webPath + "/bbsAction/newbbsView",
				gridSrc : rosten.webPath + "/bbs/bbsGrid?companyId=" + companyId + "&userId=" + userid + "&type=new"
			};
			rosten.kernel.addRightContent(naviJson);
			break;
		case "mybbsManage":
			var naviJson = {
				identifier : oString,
				actionBarSrc : rosten.webPath + "/bbsAction/bbsView",
				gridSrc : rosten.webPath + "/bbs/bbsGrid?companyId=" + companyId + "&userId=" + userid + "&type=person"
			};
			rosten.kernel.addRightContent(naviJson);
			break;
			
		case "bbsConfigManage":
			rosten.kernel.setHref(rosten.webPath + "/bbs/bbsConfig", oString);
            break;
		}
	}
	connect.connect("show_naviEntity", show_bbsNaviEntity);
});
