/**
 * @author rosten
 */
define([ "dojo/_base/connect", "dojo/_base/lang","dijit/registry", "dojo/_base/kernel","rosten/kernel/behavior" ], function(
		connect, lang,registry,kernel) {
	
	quarters_add = function(){
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("quarters", rosten.webPath + "/salary/quartersAdd?companyId=" + companyId + "&userid=" + userid+ "&flowCode=quarters");
	};
	quarters_delete = function(){
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.read(rosten.webPath + "/salary/quartersDelete", content,rosten.deleteCallback);
		};
	};

	
	quarters_search = function(){
		var content = {};
		
		switch(rosten.kernel.navigationEntity) {
		default:
			var quaName = registry.byId("quaName");
			if(quaName.get("value")!=""){
				content.quaName = quaName.get("value");
			}
			break;
		}
		
		rosten.kernel.refreshGrid(rosten.kernel.getGrid().defaultUrl, content);
	};
	
	quarters_resetSearch = function(){
		switch(rosten.kernel.navigationEntity) {
		default:
			registry.byId("quaName").set("value","");
			break;
		}	
		
		rosten.kernel.refreshGrid();
	};
	
	quarters_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:quarters_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	
	quarters_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("quarters", rosten.webPath + "/salary/quartersShow/" + unid + "?&companyId=" + companyId + "&flowCode=quarters");
		rosten.kernel.getGrid().clearSelected();
	};
	
	gear_add = function(){
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("gear", rosten.webPath + "/salary/gearAdd?companyId=" + companyId + "&userid=" + userid+ "&flowCode=gear");
	};
	gear_delete = function(){
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.read(rosten.webPath + "/salary/gearDelete", content,rosten.deleteCallback);
		};
	};

	
	gear_search = function(){
		var content = {};
		
		switch(rosten.kernel.navigationEntity) {
		default:
			var gearName = registry.byId("gearName");
			if(gearName.get("value")!=""){
				content.gearName = gearName.get("value");
			}
			break;
		}
		
		rosten.kernel.refreshGrid(rosten.kernel.getGrid().defaultUrl, content);
	};
	
	gear_resetSearch = function(){
		switch(rosten.kernel.navigationEntity) {
		default:
			registry.byId("gearName").set("value","");
			break;
		}	
		
		rosten.kernel.refreshGrid();
	};
	
	gear_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:gear_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	
	gear_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("gear", rosten.webPath + "/salary/gearShow/" + unid + "?&companyId=" + companyId + "&flowCode=gear");
		rosten.kernel.getGrid().clearSelected();
	};
	
	radix_add = function(){
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("radix", rosten.webPath + "/salary/radixAdd?companyId=" + companyId + "&userid=" + userid+ "&flowCode=radix");
	};
	
	radix_delete = function(){
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.readNoTime(rosten.webPath + "/salary/radixDelete", content,rosten.deleteCallback);
		};
	};

	
	radix_search = function(){
		var content = {};
		
		switch(rosten.kernel.navigationEntity) {
		default:
			var category = registry.byId("category");
			if(category.get("value")!=""){
				content.category =category.get("value");
			}
			break;
		}
		
		rosten.kernel.refreshGrid(rosten.kernel.getGrid().defaultUrl, content);
	};
	
	radix_resetSearch = function(){
		switch(rosten.kernel.navigationEntity) {
		default:
			registry.byId("category").set("value","");
			break;
		}	
		
		rosten.kernel.refreshGrid();
	};
	
	radix_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:radix_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	
	radix_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("radix", rosten.webPath + "/salary/radixShow/" + unid + "?&companyId=" + companyId + "&flowCode=radix");
		rosten.kernel.getGrid().clearSelected();
	};
	
	riskGold_add = function(){
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("riskGold", rosten.webPath + "/salary/riskGoldAdd?companyId=" + companyId + "&userid=" + userid+ "&flowCode=riskGold");
	};
	
	riskGold_delete = function(){
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.read(rosten.webPath + "/salary/riskGoldDelete", content,rosten.deleteCallback);
		};
	};

	
	riskGold_search = function(){
		var content = {};
		
		switch(rosten.kernel.navigationEntity) {
		default:
			var chinaName = registry.byId("chinaName");
			if(chinaName.get("value")!=""){
				content.chinaName =chinaName.get("value");
			}
			break;
		}
		
		rosten.kernel.refreshGrid(rosten.kernel.getGrid().defaultUrl, content);
	};
	
	riskGold_resetSearch = function(){
		switch(rosten.kernel.navigationEntity) {
		default:
			registry.byId("chinaName").set("value","");
			break;
		}	
		
		rosten.kernel.refreshGrid();
	};
	
	riskGold_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:riskGold_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	
	riskGold_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("radix", rosten.webPath + "/salary/riskGoldShow/" + unid + "?&companyId=" + companyId + "&flowCode=riskGold");
		rosten.kernel.getGrid().clearSelected();
	};
	
	
	billConfig_add = function(){
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("billConfig", rosten.webPath + "/salary/billConfigAdd?companyId=" + companyId + "&userid=" + userid+ "&flowCode=billConfig");
	};
	
	billConfig_delete = function(){
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.readNoTime(rosten.webPath + "/salary/billConfigDelete", content,rosten.deleteCallback);
		};
	};

	
	billConfig_search = function(){
		var content = {};
		
		switch(rosten.kernel.navigationEntity) {
		default:
			var chinaName = registry.byId("chinaName");
			var year = registry.byId("year");
			var month = registry.byId("month");
			if(chinaName.get("value")!=""){
				content.chinaName =chinaName.get("value");
			}
			if(year.get("value")!=""){
				content.year =year.get("value");
			}
			if(month.get("value")!=""){
				content.month =month.get("value");
			}
			break;
		}
		
		rosten.kernel.refreshGrid(rosten.kernel.getGrid().defaultUrl, content);
	};
	
	billConfig_resetSearch = function(){
		switch(rosten.kernel.navigationEntity) {
		default:
			registry.byId("year").set("value","");
			registry.byId("month").set("value","");
			registry.byId("chinaName").set("value","");
			break;
		}	
		
		rosten.kernel.refreshGrid();
	};
	
	billConfig_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:billConfig_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	
	billConfig_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("radix", rosten.webPath + "/salary/billConfigShow/" + unid + "?&companyId=" + companyId + "&flowCode=billConfig");
		rosten.kernel.getGrid().clearSelected();
	};
	
	salaryBill_delete = function(){
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.readNoTime(rosten.webPath + "/salary/salaryBillDelete", content,rosten.deleteCallback);
		};
	};
	salaryBill_search = function(){
		var content = {};
		
		switch(rosten.kernel.navigationEntity) {
		default:
			var chinaName = registry.byId("chinaName");
			if(chinaName.get("value")!=""){
				content.chinaName =chinaName.get("value");
			}
			break;
		}
		
		rosten.kernel.refreshGrid(rosten.kernel.getGrid().defaultUrl, content);
	};
	
	salaryBill_resetSearch = function(){
		switch(rosten.kernel.navigationEntity) {
		default:
			registry.byId("chinaName").set("value","");
			break;
		}	
		
		rosten.kernel.refreshGrid();
	};
	
	import_salaryBill = function(){
		 var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.kernel.createRostenShowDialog(rosten.webPath + "/salary/importSalaryBill/"+ companyId, {
           onLoadFunction : function() {

           }
       });
	};
	
	import_salaryBill_submit = function(object){
		var formWidget = registry.byId("file_form");
		if(!formWidget.validate()){
			rosten.alert("请正确填写相关信息！");
			return;
		}
		
	    rosten.alert("导入数据需要花费一定的时间，请耐心等待！").queryDlgClose= function(){
	        var buttonWidget = object.target;
            rosten.toggleAction(buttonWidget,true);
            
	        var importDom = registry.byId("file_form");
            importDom.submit();
            
            
	    };
	};
	
	/*
	 * 此功能默认必须存在
	 */
	show_salaryNaviEntity = function(oString) {
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		var userid = rosten.kernel.getUserInforByKey("idnumber");
		
		switch (oString) {
		//岗位基数
		case "quartersConfig":
            var naviJson = {
                identifier : oString,
                actionBarSrc : rosten.webPath + "/salaryAction/quartersView?userId=" + userid,
                searchSrc:rosten.webPath + "/salary/quartersSearchView",
                gridSrc : rosten.webPath + "/salary/quartersGrid?companyId=" + companyId
            };
            rosten.kernel.addRightContent(naviJson);

            var rostenGrid = rosten.kernel.getGrid();
            break;
            //档位系数
		case "gearConfig":
            var naviJson = {
                identifier : oString,
                actionBarSrc : rosten.webPath + "/salaryAction/gearView?userId=" + userid,
                searchSrc:rosten.webPath + "/salary/gearSearchView",
                gridSrc : rosten.webPath + "/salary/gearGrid?companyId=" + companyId
            };
            rosten.kernel.addRightContent(naviJson);

            var rostenGrid = rosten.kernel.getGrid();
            break;
            //参数
		case "radixConfig":
            var naviJson = {
                identifier : oString,
                actionBarSrc : rosten.webPath + "/salaryAction/radixView?userId=" + userid,
                searchSrc:rosten.webPath + "/salary/radixSearchView",
                gridSrc : rosten.webPath + "/salary/radixGrid?companyId=" + companyId
            };
            rosten.kernel.addRightContent(naviJson);

            var rostenGrid = rosten.kernel.getGrid();
            break;
            //五险一金
		case "wxyjConfig":
//			 var naviJson = {
//                identifier : oString,
//                actionBarSrc : rosten.webPath + "/salaryAction/riskGoldView?userId=" + userid,
//                searchSrc:rosten.webPath + "/salary/riskGoldSearchView",
//                gridSrc : rosten.webPath + "/salary/riskGoldGrid?companyId=" + companyId
//            };
//            rosten.kernel.addRightContent(naviJson);
//
//            var rostenGrid = rosten.kernel.getGrid();
			rosten.kernel.setHref(rosten.webPath + "/salary/riskGoldConfig", oString);
            break;
            //员工配置
		case "salaryBillConfig":
			 var naviJson = {
	                identifier : oString,
	                actionBarSrc : rosten.webPath + "/salaryAction/billConfigView?userId=" + userid,
	                searchSrc:rosten.webPath + "/salary/billConfigSearchView",
	                gridSrc : rosten.webPath + "/salary/billConfigGrid?companyId=" + companyId
	            };
	            rosten.kernel.addRightContent(naviJson);
	
	            var rostenGrid = rosten.kernel.getGrid();
	            break;
	      
	            //工资发放
		case "salaryBill":
			 var naviJson = {
	                identifier : oString,
	                actionBarSrc : rosten.webPath + "/salaryAction/salaryBillView?userId=" + userid,
	                searchSrc:rosten.webPath + "/salary/salaryBillSearchView",
	                gridSrc : rosten.webPath + "/salary/salaryBillGrid?companyId=" + companyId
	            };
	            rosten.kernel.addRightContent(naviJson);
	
	            var rostenGrid = rosten.kernel.getGrid();
	            break;


		}
		
	}
	connect.connect("show_naviEntity", show_salaryNaviEntity);
});
