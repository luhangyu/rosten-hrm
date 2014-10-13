/**
 * @author rosten
 */
define(["dojo/_base/connect", "dijit/registry","rosten/util/general", "rosten/kernel/behavior"], function(
		connect, registry,General) {
	
	staffStatusChange_print_tzd = function(){
		//打印通知单
	};
	
	staffStatusChange_print_qd = function(){
		//打印交接清单
	};
	
	staffStatusChange_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:staffStatusChange_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	staffStatusChange_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var type = rosten.kernel.getGridItemValue(rowIndex,"changeType");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("staffStatusChange", rosten.webPath + "/staff/staffStatusChangeShow/" + unid + "?userid=" + userid + "&companyId=" + companyId + "&type=" + type);
		rosten.kernel.getGrid().clearSelected();
	};
	add_staffStatusChange = function() {
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        
        var type="retire";
        if(rosten.kernel.navigationEntity=="staffLeave"){
        	type="leave";
        }
        rosten.openNewWindow("staffStatusChange", rosten.webPath + "/staff/staffStatusChangeAdd?companyId=" + companyId + "&userid=" + userid + "&type=" + type);
    };
	change_staffStatusChange = function() {
		var unid = rosten.getGridUnid("single");
		if (unid == "")
			return;
		var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		
		 var type="retire";
	        if(rosten.kernel.navigationEntity=="staffLeave"){
	        	type="leave";
	        }
	        
		rosten.openNewWindow("staffStatusChange", rosten.webPath + "/staff/staffStatusChangeShow/" + unid + "?userid=" + userid + "&companyId=" + companyId+ "&type=" + type);
	};
	read_staffDepartChange = function() {
		change_staffStatusChange();
	};
	delete_staffStatusChange = function() {
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.readNoTime(rosten.webPath + "/staff/staffStatusChangeDelete", content,
					rosten.deleteCallback);
		};
	};
	
	staffDepartChange_print_tzd = function(){
		//打印通知单
	};
	
	staffDepartChange_print_qd = function(){
		//打印交接清单
	};
	
	staffDepartChange_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:staffDepartChange_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	staffDepartChange_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("staffDepartChange", rosten.webPath + "/staff/staffDepartChangeShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
		rosten.kernel.getGrid().clearSelected();
	};
	add_staffDepartChange = function() {
		var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("staffDepartChange", rosten.webPath + "/staff/staffDepartChangeAdd?companyId=" + companyId + "&userid=" + userid + "&flowCode=staffDepartChange");
    };
	change_staffDepartChange = function() {
		var unid = rosten.getGridUnid("single");
		if (unid == "")
			return;
		var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("staffDepartChange", rosten.webPath + "/staff/staffDepartChangeShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
	};
	read_staffDepartChange = function() {
		change_staffDepartChange();
	};
	delete_staffDepartChange = function() {
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
		_1.callback = function() {
			var unids = rosten.getGridUnid("multi");
			if (unids == "")
				return;
			var content = {};
			content.id = unids;
			rosten.readNoTime(rosten.webPath + "/staff/staffDepartChangeDelete", content,
					rosten.deleteCallback);
		};
	};
	
	personInfor_formatTopic_normal =function(value,rowIndex){
		return "<a href=\"javascript:personInfor_normal_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	personInfor_normal_onMessageOpen =function(rowIndex){
		var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		
		var tmpArgs = "&type=" + rosten.kernel.navigationEntity;
		if(rosten.kernel.navigationEntity=="staffAdd"){
			tmpArgs +="&flowCode=" + rosten.kernel.navigationEntity;
		}
		rosten.openNewWindow("personInfor", rosten.webPath + "/staff/userShow/" + unid + "?userid=" + userid + "&companyId=" + companyId + tmpArgs);
		rosten.kernel.getGrid().clearSelected();
	};
	
	staffLeave = function(){
		var userId = registry.byId("userId");
		var userDepartId = registry.byId("userDepartId");
    	if(!userDepartId.isValid()){
    		rosten.alert("请单击<查询验证>按钮进行员工信息验证！");
			return;
    	}
    	
		var commentDialog = rosten.addCommentDialog({});
		commentDialog.title = "离职原因";
		commentDialog.callback = function(_data){
			rosten.readSync(rosten.webPath + "/staff/staffLeave/" + userId.attr("value"),{dataStr:_data.content},function(data){
				if(data.result=="true" || data.result == true){
					rosten.alert("成功！");
				}else{
					rosten.alert("失败!");
				}	
			});
		};
	};
	staffRetire = function(){
		//退休
		var userId = registry.byId("userId");
		var userDepartId = registry.byId("userDepartId");
    	if(!userDepartId.isValid()){
    		rosten.alert("请单击<查询验证>按钮进行员工信息验证！");
			return;
    	}
    	
		var commentDialog = rosten.addCommentDialog({});
		commentDialog.title = "退休原因";
		commentDialog.callback = function(_data){
			rosten.readSync(rosten.webPath + "/staff/staffRetire/" + userId.attr("value"),{dataStr:_data.content},function(data){
				if(data.result=="true" || data.result == true){
					rosten.alert("成功！");
				}else{
					rosten.alert("失败!");
				}	
			});
		};
	};
	staffChangeDepart = function(){
		var userId = registry.byId("userId");
		var userDepartId = registry.byId("userDepartId");
    	if(!userDepartId.isValid()){
    		rosten.alert("请单击<查询验证>按钮进行员工信息验证！");
			return;
    	}

    	var newDepartId = registry.byId("newDepartId");
    	if(!newDepartId.isValid()){
    		rosten.alert("请选择调入部门！").queryDlgClose = function(){
    			registry.byId("newdepartName").focus();
			};
			return;
    	}

		var content ={
			userId:userId.attr("value"),
			userDepartId:userDepartId.attr("value"),
			newDepartId:newDepartId.attr("value")	
		}
		rosten.readSync(rosten.webPath + "/staff/staffChangeDepart", content, function(data) {
			if (data.result == "true" || data.result == true) {
	            rosten.alert("变更成功!").queryDlgClose = function(){
	            	show_staffNaviEntity("staffDepartChange");
				};
	        } else {
	            rosten.alert("变更失败!");
	        }
    	
		});
	}
	
	/*
	 * 此功能默认必须存在
	 */
	show_staffNaviEntity = function(oString) {
		
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		var userid = rosten.kernel.getUserInforByKey("idnumber");
		
		switch (oString) {
		case "staffAdd":	//员工入职
			var naviJson = {
				identifier : oString,
				actionBarSrc : rosten.webPath + "/staffAction/staffAddView?userId=" + userid,
				searchSrc:rosten.webPath + "/staff/searchView",
				gridSrc : rosten.webPath + "/staff/staffGrid?companyId=" + companyId + "&userId=" + userid + "&type=" + oString
			};
			rosten.kernel.addRightContent(naviJson);
			break;
		case "staffRegi":	//员工登记
			var naviJson = {
				identifier : oString,
				actionBarSrc : rosten.webPath + "/staffAction/staffView?userId=" + userid,
				searchSrc:rosten.webPath + "/staff/searchView",
				gridSrc : rosten.webPath + "/staff/staffGrid?companyId=" + companyId + "&userId=" + userid + "&type=" + oString
			};
			rosten.kernel.addRightContent(naviJson);
			break;
		case "staffProba":	//员工转正
			rosten.alert("此功能尚未开通")
			returnToMain();
			break;
		case "staffDepartChange":	//员工调动
			var naviJson = {
				identifier : oString,
				actionBarSrc : rosten.webPath + "/staffAction/staffDepartChangeView?userId=" + userid,
				gridSrc : rosten.webPath + "/staff/staffDepartChangeGrid?companyId=" + companyId + "&userId=" + userid
			};
			rosten.kernel.addRightContent(naviJson);
			break;
		case "staffLeave":	//员工离职
			var naviJson = {
				identifier : oString,
				actionBarSrc : rosten.webPath + "/staffAction/staffStatusChangeView?userId=" + userid +"&type=leave",
				gridSrc : rosten.webPath + "/staff/staffStatusChangeGrid?companyId=" + companyId + "&userId=" + userid + "&type=leave"
			};
			rosten.kernel.addRightContent(naviJson);
			break;
		case "staffRetire":	//员工退休
			var naviJson = {
				identifier : oString,
				actionBarSrc : rosten.webPath + "/staffAction/staffStatusChangeView?userId=" + userid +"&type=retire",
				gridSrc : rosten.webPath + "/staff/staffStatusChangeGrid?companyId=" + companyId + "&userId=" + userid + "&type=retire"
			};
			rosten.kernel.addRightContent(naviJson);
			break;
		case "staffSearch":	//员工查询
			var naviJson = {
				identifier : oString,
				actionBarSrc : rosten.webPath + "/staffAction/staffView?userId=" + userid + "&type=" + oString,
				searchSrc:rosten.webPath + "/staff/searchView",
				gridSrc : rosten.webPath + "/staff/staffGrid?companyId=" + companyId + "&userId=" + userid + "&type=" + oString
			};
			rosten.kernel.addRightContent(naviJson);
			break;
		}
	}
	connect.connect("show_naviEntity", show_staffNaviEntity);
});
