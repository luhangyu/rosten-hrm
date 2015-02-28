/**
 * @author rosten
 */
define([ "dojo/_base/connect", "dojo/_base/lang","dijit/registry", "dojo/_base/kernel","rosten/app/ChartManage","rosten/widget/PickTreeDialog","rosten/kernel/behavior" ], function(
		connect, lang,registry,kernel,ChartManage,PickTreeDialog) {
    
    
    //2015-2-28------增加按月统计功能------------------------------------------
    vacate_staticByAll = function(){
        show_attEndanceNaviEntity("staticByAll");
    };
    vacate_staticByMonth = function(){
        show_attEndanceNaviEntity("askForStatic");
    };
    vacate_print = function(){
                    
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
