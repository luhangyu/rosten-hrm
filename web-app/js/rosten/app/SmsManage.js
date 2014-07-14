/**
 * @author rosten
 */
define(["dojo/dom",
        "dijit/registry",
        "dojo/_base/connect",
        "rosten/widget/PickTreeDialog",
        "rosten/app/Application",
        "rosten/kernel/behavior"], function(dom,registry,connect,PickTreeDialog) {
	authorize_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:authorize_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
	};
	authorize_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"id");
        var userid = rosten.kernel.getUserInforByKey("idnumber");
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		rosten.openNewWindow("authorize", rosten.webPath + "/system/authorizeShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
		rosten.kernel.getGrid().clearSelected();
	};
    authorize_start = function(){
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;
        var content = {};
        content.id = unid;
        rosten.readSync(rosten.webPath + "/system/authorizeStart", content, function(data){
            if (data.result == "true" || data.result == true) {
                rosten.alert("成功启用!");
                rosten.kernel.refreshGrid();
            } else {
                rosten.alert("删除失败!");
            }
        });
    };
    authorize_cancel = function(){
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;
        var content = {};
        content.id = unid;
        rosten.readSync(rosten.webPath + "/system/authorizeCancel", content, function(data){
            if (data.result == "true" || data.result == true) {
                rosten.alert("成功取消!");
                rosten.kernel.refreshGrid();
            } else {
                rosten.alert("删除失败!");
            }
        });
    };
    
    add_authorize = function() {
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("authorize", rosten.webPath + "/system/authorizeAdd?companyId=" + companyId + "&userid=" + userid);
    };
    read_authorize = function() {
        change_authorize();
    };
    change_authorize = function() {
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("authorize", rosten.webPath + "/system/authorizeShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
        rosten.kernel.getGrid().clearSelected();
    };
    delete_authorize = function() {
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.read(rosten.webPath + "/system/authorizeDelete", content, function(data){
            	if (data.result == "true" || data.result == true) {
                    rosten.alert("成功删除!");
                    rosten.kernel.refreshGrid();
                } else {
                    rosten.alert("删除失败!");
                }
            });
        };
    };
    gtask_formatTitle = function(value,rowIndex){
        return "<a href=\"javascript:gtask_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
    },
    gtask_onMessageOpen = function(rowIndex){
        var unid = rosten.kernel.getGridItemValue(rowIndex,"contentId");
        var type = rosten.kernel.getGridItemValue(rowIndex,"type");
        openGtask(type,unid);
        rosten.kernel.getGrid().clearSelected();
    },
	delete_gtask = function(){
		var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.readSync(rosten.webPath + "/start/gtaskDelete", content, function(data){
                if (data.result == "true" || data.result == true) {
                    rosten.alert("成功删除!");
                    rosten.kernel.refreshGrid();
                } else {
                    rosten.alert("删除失败!");
                }
            });
        };
	};
    add_smsGroup = function() {
        var unid = rosten.kernel.getUserInforByKey("idnumber");
        rosten.openNewWindow("smsGroup", rosten.webPath + "/system/smsGroupAdd?userid=" + unid);
    };
    read_smsGroup = function() {
        change_smsGroup();
    };
    change_smsGroup = function() {
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;

        var userid = rosten.kernel.getUserInforByKey("idnumber");
        rosten.openNewWindow("smsGroup", rosten.webPath + "/system/smsGroupShow/" + unid + "?userid=" + userid);
    };
    delete_smsGroup = function() {
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.readSync(rosten.webPath + "/system/smsGroupDelete", content, function(data){
                if (data.result == "true" || data.result == true) {
                    rosten.alert("成功删除!");
                    rosten.kernel.refreshGrid();
                } else {
                    rosten.alert("删除失败!");
                }
            });
        };
    };
    selectSmsGroup = function(url) {
        var id = "sys_smsGroupDialog";
        var initValue = [];
        var allowgroupsName = registry.byId("sms_Group");
        if (allowgroupsName.attr("value") != "") {
            initValue.push(allowgroupsName.attr("value"));
        }
        rosten.selectDialog("群组选择", id, url, true, initValue);
        rosten[id].callback = function(data) {
            var _data = "";
            for (var k = 0; k < data.length; k++) {
                var item = data[k];
                if (_data == "") {
                    _data += item.name;
                } else {
                    _data += "," + item.name;
                }
            }
            registry.byId("sms_Group").attr("value", _data);
        };
    };
    sendsms = function(){
        var content = {};
        var isNext = false;
        var telephone = registry.byId("sms_telephone");
        if(telephone.attr("value")!=""){
            content.telephone = telephone.attr("value");
            isNext = true;
        }
        var smsGroup = registry.byId("sms_Group");
        if(smsGroup.attr("value")!=""){
            content.smsGroup = smsGroup.attr("value");
            isNext = true;
        }
        if(isNext==false){
            rosten.alert("手机号码,短信群组不能为空！");
            return;
        }
        var contentNode = registry.byId("sms_content");
        if(contentNode.attr("value")!=""){
            content.content = contentNode.attr("value");
        }else{
            rosten.alert("发送内容不能为空！");
            return;
        }
        rosten.readSync(rosten.webPath + "/system/smsSave",content,function(data){
            if(data.result=="nouse"){
                rosten.alert("短消息功能尚未开通，请联系厂家！");
            }else if(data.result == "error"){
                rosten.alert("系统出错，请联系厂家！");
            }else{
                rosten.alert("发送成功！");
                rosten.kernel.hideRostenShowDialog();
            }
            
        });
        
    };
    show_smsNaviEntity = function(oString) {
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        switch (oString) {
	        case "personInformation":
	    		var userid = rosten.kernel.getUserInforByKey("idnumber");
	            var companyId = rosten.kernel.getUserInforByKey("companyid");
	            rosten.kernel.setHref(rosten.webPath +"/system/personInformation?companyId=" + companyId + "&userid=" + userid,oString);
	            break;
            
	        case "gtaskManage":
	            var naviJson = {
	                identifier : oString,
	                actionBarSrc : rosten.webPath + "/startAction/gtaskView?userId=" + userid,
	                gridSrc : rosten.webPath + "/start/gtaskGrid?userid=" + userid + "&companyId=" + companyId
	            };
	            rosten.kernel.addRightContent(naviJson);
	            break;
            case "smsgroup":
                var naviJson = {
                    identifier : oString,
                    actionBarSrc : rosten.webPath + "/systemAction/smsGroupView",
                    gridSrc : rosten.webPath + "/system/smsGroupGrid?userid=" + userid + "&companyId=" + companyId
                };
                rosten.kernel.addRightContent(naviJson);
                
                var rostenGrid = rosten.kernel.getGrid();
                rostenGrid.onRowDblClick = change_smsGroup;
                break;
            case "authorizeManage":
                var naviJson = {
                    identifier : oString,
                    actionBarSrc : rosten.webPath + "/systemAction/authorizeView?userId=" + userid,
                    gridSrc : rosten.webPath + "/system/authorizeGrid?userid=" + userid + "&companyId=" + companyId
                };
                rosten.kernel.addRightContent(naviJson);
                break;
        }    
    };
    connect.connect("show_naviEntity", show_smsNaviEntity);
});
