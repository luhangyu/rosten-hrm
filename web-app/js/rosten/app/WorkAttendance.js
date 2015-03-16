/**
 * @author rosten
 */
define([ "dojo/_base/connect", "dojo/_base/lang","dijit/registry", "dojo/_base/kernel","rosten/app/ChartManage","rosten/widget/PickTreeDialog","dojo/date/stamp","rosten/kernel/behavior" ], function(
		connect, lang,registry,kernel,ChartManage,PickTreeDialog,datestamp) {
    
    //2015-3-16------------增加考勤记录---------------------------------------
    workCheck_search =function(){
        var content = {};
        
        var applyName = registry.byId("s_chinaName");
        if(applyName.get("value")!=""){
            content.chinaName = applyName.get("value");
        }
        
        var applyDepart = registry.byId("s_departName");
        if(applyDepart.get("value")!=""){
            content.departName = applyDepart.get("value");
        }
        
        var date = registry.byId("s_workCheckDate");
        if(date.get("value") && date.get("value")!=""){
            content.date = datestamp.toISOString(date.get("value"),{selector: "date"});
        }
        
        switch(rosten.kernel.navigationEntity) {
        default:
            rosten.kernel.refreshGrid(rosten.kernel.getGrid().defaultUrl, content);
            break;
        }
    };
    workCheck_resetSearch = function(){
        switch(rosten.kernel.navigationEntity) {
        default:
            registry.byId("s_chinaName").set("value","");
            registry.byId("s_departName").set("value","");
            registry.byId("s_workCheckDate").set("value"," ");
            break;
        }   
        
        rosten.kernel.refreshGrid();
    };
    
    workCheck_delete = function(){
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.readNoTime(rosten.webPath + "/vacate/workCheckDelete", content,rosten.deleteCallback);
        };
    };
    
    //2015-3-13-------------增加出勤解释单-------------------------------------
    vacateExplain_search =function(){
        var content = {};
        
        var applyDepart = registry.byId("s_departName");
        if(applyDepart.get("value")!=""){
            content.departName = applyDepart.get("value");
        }
        
        var applyName = registry.byId("s_chinaName");
        if(applyName.get("value")!=""){
            content.chinaName = applyName.get("value");
        }
        
        var month = registry.byId("s_month");
        if(month.get("value")!=""){
            content.month = month.get("value");
        }
        
        switch(rosten.kernel.navigationEntity) {
        default:
            rosten.kernel.refreshGrid(rosten.kernel.getGrid().defaultUrl, content);
            break;
        }
    };
    vacateExplain_resetSearch = function(){
        switch(rosten.kernel.navigationEntity) {
        default:
            registry.byId("s_departName").set("value","");
            registry.byId("s_chinaName").set("value","");
            registry.byId("s_month").set("value","");
            break;
        }   
        
        rosten.kernel.refreshGrid();
    };
    vacateExplain_add = function(){
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("vacateExplain", rosten.webPath + "/vacate/vacateExplainAdd?companyId=" + companyId + "&userid=" + userid);
    };
    vacateExplain_delete = function(){
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.readNoTime(rosten.webPath + "/vacate/vacateExplainDelete", content,rosten.deleteCallback);
        };
    };
    vacateExplain_formatTopic = function(value,rowIndex){
        return "<a href=\"javascript:vacateExplain_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
    };
    vacateExplain_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("vacateExplain", rosten.webPath + "/vacate/vacateExplainShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
        rosten.kernel.getGrid().clearSelected();
    };
    
    //2015-2-28------增加按月统计功能------------------------------------------
    vacate_staticByAll = function(){
        show_attEndanceNaviEntity("staticByAll");
    };
    vacate_staticByMonth = function(){
        show_attEndanceNaviEntity("askForStatic");
    };
    vacate_print = function(){
        var companyId = rosten.kernel.getUserInforByKey("companyid");
         
        var content = {};
        var query = "";
        var applyName = registry.byId("s_chinaName");
        if(applyName.get("value")!=""){
            query += "&applyName="+applyName.get("value");
        }
        
        var applyDepart = registry.byId("s_departName");
        if(applyDepart.get("value")!=""){
            query += "&applyDepart="+applyDepart.get("value");
        }
        
        var month = registry.byId("s_month");
        if(month.get("value")!=""){
            query += "&month="+month.get("value");
        }
        
        rosten.openNewWindow("export", rosten.webPath + "/vacate/exportStaticByMonth?companyId="+companyId+"&type="+ rosten.kernel.navigationEntity+query );             
    };
    vacate_searchByMonth = function(){
        var content = {};
        
        var applyDepart = registry.byId("s_departName");
        if(applyDepart.get("value")!=""){
            content.applyDepart = applyDepart.get("value");
        }
        
        var applyName = registry.byId("s_chinaName");
        if(applyName.get("value")!=""){
            content.applyName = applyName.get("value");
        }
        
        var month = registry.byId("s_month");
        if(month.get("value")!=""){
            content.month = month.get("value");
        }
        
        switch(rosten.kernel.navigationEntity) {
        default:
            rosten.kernel.refreshGrid(rosten.kernel.getGrid().defaultUrl, content);
            break;
        }
    };
    vacate_resetSearchByMonth = function(){
        switch(rosten.kernel.navigationEntity) {
        default:
            registry.byId("s_departName").set("value","");
            registry.byId("s_chinaName").set("value","");
            registry.byId("s_month").set("value","");
            break;
        }   
        
        rosten.kernel.refreshGrid();
    };
    //--------------------------------------------------------------------
    		    
	vacate_search = function(){
        var content = {};
        
        var departName = registry.byId("s_departName");
        if(departName.get("value")!=""){
            content.departName = departName.get("value");
        }
        
        var chinaName = registry.byId("s_chinaName");
        if(chinaName.get("value")!=""){
            content.chinaName = chinaName.get("value");
        }
        
        switch(rosten.kernel.navigationEntity) {
        default:
            rosten.kernel.refreshGrid(rosten.kernel.getGrid().defaultUrl, content);
            break;
        }
    };
    
    vacate_resetSearch = function(){
        switch(rosten.kernel.navigationEntity) {
        default:
            registry.byId("s_departName").set("value","");
            registry.byId("s_chinaName").set("value","");
            break;
        }   
        
        rosten.kernel.refreshGrid();
    };
    
	//考勤管理选择部门统计
	selecWorktDepart = function(url,type) {
        var id = "work_departDialog";
        if (rosten[id] && registry.byId(id)) {
            rosten[id].open();
            rosten[id].refresh();
        } else {
            var args = {
                url : url,
                rootLabel : "部门层级",
                showCheckBox : type,
                folderClass : "departTree"
            };
            rosten[id] = new PickTreeDialog(args);
            rosten[id].open();
        }
        rosten[id].callback = function(data) {
        	var companyId = rosten.kernel.getUserInforByKey("companyid");
            var item = data[0];
			rosten.kernel.setHref(rosten.webPath + "/vacate/askForStatic?companyId=" + companyId+"&departId="+item.id, "askForStatic" ,ChartManage.addAskForChart);
        };
    };
    vacate_add_admin = function(){
    	var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("vacate", rosten.webPath + "/vacate/vacateAdd?companyId=" + companyId + "&userid=" + userid+ "&flowCode=vacate&notNeedFlow=true");
    };
	vacate_add = function(){
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("vacate", rosten.webPath + "/vacate/vacateAdd?companyId=" + companyId + "&userid=" + userid+ "&flowCode=vacate");
	};
	vacate_delete = function(){
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.readNoTime(rosten.webPath + "/vacate/vacateDelete", content,rosten.deleteCallback);
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
                gridSrc : rosten.webPath + "/vacate/vacateGrid?companyId=" + companyId+"&userId=" + userid
            };
            rosten.kernel.addRightContent(naviJson);

            var rostenGrid = rosten.kernel.getGrid();
            break;
		case "askForStatic":
		
		    var naviJson = {
                identifier : oString,
                actionBarSrc : rosten.webPath + "/vacateAction/staticByMonth?userId=" + userid,
                searchSrc:rosten.webPath + "/vacate/staticByMonthSearchView",
                gridSrc : rosten.webPath + "/vacate/staticByMonthGrid?companyId=" + companyId+"&userId=" + userid
            };
            rosten.kernel.addRightContent(naviJson);
            
            break;
        case "vacateExplain":
        
            var naviJson = {
                identifier : oString,
                actionBarSrc : rosten.webPath + "/vacateAction/vacateExplainView?userId=" + userid,
                searchSrc:rosten.webPath + "/vacate/vacateExplainSearchView",
                gridSrc : rosten.webPath + "/vacate/vacateExplainGrid?companyId=" + companyId+"&userId=" + userid
            };
            rosten.kernel.addRightContent(naviJson);
            
            break;
        case "workCheck":
        
            var naviJson = {
                identifier : oString,
                actionBarSrc : rosten.webPath + "/vacateAction/workCheckView?userId=" + userid,
                searchSrc:rosten.webPath + "/vacate/workCheckSearchView",
                gridSrc : rosten.webPath + "/vacate/workCheckGrid?companyId=" + companyId+"&userId=" + userid
            };
            rosten.kernel.addRightContent(naviJson);
            
            break;   
		case "allAskFor":
            var naviJson = {
                identifier : oString,
                actionBarSrc : rosten.webPath + "/vacateAction/allAskForView?userId=" + userid,
                searchSrc:rosten.webPath + "/vacate/searchView",
                gridSrc : rosten.webPath + "/vacate/allAskForGrid?companyId=" + companyId+"&userId=" + userid
            };
            rosten.kernel.addRightContent(naviJson);

            var rostenGrid = rosten.kernel.getGrid();
            break;
        case "staticByAll":
            rosten.kernel.setHref(rosten.webPath + "/vacate/askForStatic?companyId=" + companyId, oString , ChartManage.addAskForChart);
            break; 
		}
	};
	connect.connect("show_naviEntity", show_attEndanceNaviEntity);
});
