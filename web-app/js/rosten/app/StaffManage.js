/**
 * @author rosten
 */
define(["dojo/_base/connect", "dijit/registry","rosten/util/general", "rosten/kernel/behavior"], function(
		connect, registry,General) {
	
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
		case "staffRegi":
			var naviJson = {
				identifier : oString,
				actionBarSrc : rosten.webPath + "/staffAction/staffView?userId=" + userid,
				gridSrc : rosten.webPath + "/staff/staffGrid?companyId=" + companyId + "&userId=" + userid + "&type=normal"
			};
			rosten.kernel.addRightContent(naviJson);
			break;
		case "staffDepartChange":
			require(["rosten/app/Application"],function(){
				rosten.kernel.setHref(rosten.webPath + "/staff/staffDepartChange?companyId=" + companyId, oString);
			});
			break;
		}
	}
	connect.connect("show_naviEntity", show_staffNaviEntity);
});
