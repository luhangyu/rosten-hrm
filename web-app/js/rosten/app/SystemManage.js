/**
 * @author rosten
 */
define(["dojo/_base/connect",
        "dijit/registry",
        "rosten/kernel/behavior"], function(connect,registry) {

    searchGridSubmit = function() {
        var url = "jsproot/sysmanage/";
        var content = {};
        switch(rosten.kernel.navigationEntity) {
            case "modelManage":

                var modelcode = registry.byId("modelcode");
                if (!modelcode.isValid()) {
                    rosten.alert("当前模块编号不正确！").queryDlgClose = function() {
                        modelcode.focus();
                    };

                    return;
                }

                var modelname = registry.byId("modelname");
                if (!modelname.isValid()) {
                    rosten.alert("当前模块名称不正确！").queryDlgClose = function() {
                        modelname.focus();
                    };

                    return;
                }
                if (modelcode.getValue() == "" && modelname.getValue() == "") {
                    rosten.alert("请正确填写搜索条件！").queryDlgClose = function() {
                        modelcode.focus();
                    };
                    return;
                }
                if (modelcode.attr("value") != "") {
                    content.modelcode = modelcode.attr("value");
                }
                if (modelname.attr("value") != "") {
                    content.modelname = modelname.attr("value");
                }
                url += "Model_Search.jsp";
                break;
            case "departManage":

                var departcode = registry.byId("departcode");
                if (!departcode.isValid()) {
                    rosten.alert("当前部门编号不正确！").queryDlgClose = function() {
                        departcode.focus();
                    };

                    return;
                }

                var departname = registry.byId("departname");
                if (!departname.isValid()) {
                    rosten.alert("当前部门名称不正确！").queryDlgClose = function() {
                        departname.focus();
                    };

                    return;
                }
                if (departcode.getValue() == "" && departname.getValue() == "") {
                    rosten.alert("请正确填写搜索条件！").queryDlgClose = function() {
                        departcode.focus();
                    };
                    return;
                }
                if (departcode.getValue() != "") {
                    content.departcode = departcode.getValue();
                }
                if (departname.getValue() != "") {
                    content.departname = departname.getValue();
                }

                url += "Depart_Search.jsp";

                break;
            case "groupManage":
                var groupcode = registry.byId("groupcode");
                if (!groupcode.isValid()) {
                    rosten.alert("当前群组编号不正确！").queryDlgClose = function() {
                        groupcode.focus();
                    };

                    return;
                }

                var groupname = registry.byId("groupname");
                if (!groupname.isValid()) {
                    rosten.alert("当前群组名称不正确！").queryDlgClose = function() {
                        groupname.focus();
                    };

                    return;
                }
                if (groupcode.getValue() == "" && groupname.getValue() == "") {
                    rosten.alert("请正确填写搜索条件！").queryDlgClose = function() {
                        groupcode.focus();
                    };
                    return;
                }
                if (groupcode.getValue() != "") {
                    content.groupcode = groupcode.getValue();
                }
                if (groupname.getValue() != "") {
                    content.groupname = groupname.getValue();
                }
                url += "Group_Search.jsp";
                break;

            case "userManage":
                var usercode = registry.byId("usercode");
                if (!usercode.isValid()) {
                    rosten.alert("当前用户编号不正确！").queryDlgClose = function() {
                        usercode.focus();
                    };

                    return;
                }

                var username = registry.byId("username");
                if (!username.isValid()) {
                    rosten.alert("当前用户名称不正确！").queryDlgClose = function() {
                        username.focus();
                    };

                    return;
                }
                if (usercode.getValue() == "" && username.getValue() == "") {
                    rosten.alert("请正确填写搜索条件！").queryDlgClose = function() {
                        usercode.focus();
                    };
                    return;
                }
                if (usercode.getValue() != "") {
                    content.usercode = usercode.getValue();
                }
                if (username.getValue() != "") {
                    content.username = username.getValue();
                }
                url += "User_Search.jsp";
                break;
            case "roleManage":
                var rolecode = registry.byId("rolecode");
                if (!rolecode.isValid()) {
                    rosten.alert("当前角色编号不正确！").queryDlgClose = function() {
                        rolecode.focus();
                    };

                    return;
                }

                var rolename = registry.byId("rolename");
                if (!rolename.isValid()) {
                    rosten.alert("当前角色名称不正确！").queryDlgClose = function() {
                        rolename.focus();
                    };

                    return;
                }
                if (rolecode.getValue() == "" && rolename.getValue() == "") {
                    rosten.alert("请正确填写搜索条件！").queryDlgClose = function() {
                        rolecode.focus();
                    };
                    return;
                }
                if (rolecode.getValue() != "") {
                    content.rolecode = rolecode.getValue();
                }
                if (rolename.getValue() != "") {
                    content.rolename = rolename.getValue();
                }
                url += "Role_Search.jsp";
                break;
            case "permissionManage":
                var permissioncode = registry.byId("permissioncode");
                if (!permissioncode.isValid()) {
                    rosten.alert("当前权限编号不正确！").queryDlgClose = function() {
                        permissioncode.focus();
                    };

                    return;
                }

                var permissionname = registry.byId("permissionname");
                if (!permissionname.isValid()) {
                    rosten.alert("当前权限名称不正确！").queryDlgClose = function() {
                        permissionname.focus();
                    };

                    return;
                }
                if (permissioncode.getValue() == "" && permissionname.getValue() == "") {
                    rosten.alert("请正确填写搜索条件！").queryDlgClose = function() {
                        permissioncode.focus();
                    };
                    return;
                }
                if (permissioncode.getValue() != "") {
                    content.permissioncode = permissioncode.getValue();
                }
                if (permissionname.getValue() != "") {
                    content.permissionname = permissionname.getValue();
                }
                url += "Permission_Search.jsp";
                break;
            case "resourceManage":
                var resourcecode = registry.byId("resourcecode");
                if (!resourcecode.isValid()) {
                    rosten.alert("当前资源编号不正确！").queryDlgClose = function() {
                        resourcecode.focus();
                    };

                    return;
                }

                var resourcename = registry.byId("resourcename");
                if (!resourcename.isValid()) {
                    rosten.alert("当前资源名称不正确！").queryDlgClose = function() {
                        resourcename.focus();
                    };

                    return;
                }
                if (resourcecode.getValue() == "" && resourcename.getValue() == "") {
                    rosten.alert("请正确填写搜索条件！").queryDlgClose = function() {
                        resourcecode.focus();
                    };
                    return;
                }
                if (resourcecode.getValue() != "") {
                    content.resourcecode = resourcecode.getValue();
                }
                if (resourcename.getValue() != "") {
                    content.resourcename = resourcename.getValue();
                }
                url += "Resource_Search.jsp";
                break;
        }

        //先关闭当前窗口，然后刷新数据
        rosten.kernel.hideRostenShowDialog();
        rosten.kernel.refreshGrid(url, content);

    };
    searchGrid = function() {

        console.log(rosten.kernel.navigationEntity);
        var url = "jslib/rosten/html/sysmanage/";
        switch(rosten.kernel.navigationEntity) {
            case "modelManage":
                url += "Model_Search.html";
                break;
            case "departManage":
                url += "Depart_Search.html";
                break;
            case "groupManage":
                url += "Group_Search.html";
                break;
            case "userManage":
                url += "User_Search.html";
                break;
            case "roleManage":
                url += "Role_Search.html";
                break;
            case "permissionManage":
                url += "Permission_Search.html";
                break;
            case "resourceManage":
                url += "Resource_Search.html";
                break;
        }

        rosten.kernel.createRostenShowDialog(url);

    };
    cancelSearch = function() {
        console.log(rosten.kernel.getGrid().defaultUrl);
        rosten.kernel.refreshGrid(rosten.kernel.getGrid().defaultUrl);
    };
    //------------------------------------------------------------------------------------------------------------------------
    changePassword = function(){
    	var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;
    	rosten.kernel.createRostenShowDialog(rosten.webPath + "/system/passwordChangeShow1/"+ unid, {
            onLoadFunction : function() {

            }
        });
    };
    changePasswordSubmit = function(){
        var newpassword = registry.byId("newpassword");
        if (!newpassword.isValid()) {
            rosten.alert("新密码不正确！").queryDlgClose = function() {
                newpassword.focus();
            };
            return;
        }
        var newpasswordcheck = registry.byId("newpasswordcheck");
        if (!newpasswordcheck.isValid()) {
            rosten.alert("确认密码不正确！").queryDlgClose = function() {
                newpasswordcheck.focus();
            };
            return;
        }
        if (newpassword.getValue() != newpasswordcheck.getValue()) {
            rosten.alert("新密码与确认密码不一致！").queryDlgClose = function() {
                newpassword.focus();
            };
            return;
        }
        var content = {};
        content.newpassword = newpassword.getValue();
        content.id = registry.byId("dealunid").getValue();

        rosten.read(rosten.webPath + "/system/passwordChangeSubmit1", content, function(data) {
            if (data.result == "true") {
                rosten.kernel.hideRostenShowDialog();
                rosten.kernel.getGrid().clearSelected();
                rosten.alert("修改密码成功!");
            } else if (data.result == "error") {
                rosten.alert("当前密码错误,修改密码失败!");
            } else {
                rosten.alert("修改密码失败!");
            }
        });
    };
    add_user = function() {
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("depart", rosten.webPath + "/system/userAdd?companyId=" + companyId + "&userid=" + userid);
    };
    read_user = function() {
        change_user();
    };
    change_user = function() {
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("user", rosten.webPath + "/system/userShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
        rosten.kernel.getGrid().clearSelected();
    };
    delete_user = function() {
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.read(rosten.webPath + "/system/userDelete", content, delete_callback);
        };
    };
    add_smsGroup = function() {
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("smsgroup", rosten.webPath + "/system/smsgroupAdd?companyId=" + companyId + "&userid=" + userid);
    };
    read_smsGroup = change_smsGroup = function() {
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("smsgroup", rosten.webPath + "/system/smsgroupShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
    };
    delete_smsGroup = function() {
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.read(rosten.webPath + "/system/smsgroupDelete", content, delete_callback);
        };
    };
    add_service = function() {
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("service", rosten.webPath + "/system/serviceAdd?companyId=" + companyId + "&userid=" + userid);
    };
    read_service = function() {
        change_service();
    };
    change_service = function() {
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("service", rosten.webPath + "/system/serviceShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
        rosten.kernel.getGrid().clearSelected();
    };
    delete_service = function() {
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.read(rosten.webPath + "/system/serviceDelete", content, delete_callback);
        };
    };
    open_service = function(){
    	var unids = rosten.getGridUnid("multi");
        if (unids == "")
            return;
        var content = {status:"是"};
        content.id = unids;
        rosten.read(rosten.webPath + "/system/serviceStatus", content, delete_callback);
    };
    close_service = function(){
    	var unids = rosten.getGridUnid("multi");
        if (unids == "")
            return;
        var content = {status:"否"};
        content.id = unids;
        rosten.read(rosten.webPath + "/system/serviceStatus", content, delete_callback);
    };
    add_resource = function() {
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("resource", rosten.webPath + "/system/resourceAdd?companyId=" + companyId + "&userid=" + userid);
    };
    read_resource = function() {
        change_resource();
    };
    change_resource = function() {
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("resource", rosten.webPath + "/system/resourceShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
        rosten.kernel.getGrid().clearSelected();
    };
    delete_resource = function() {
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.read(rosten.webPath + "/system/resourceDelete", content, delete_callback);
        };
    };
    add_permission = function() {
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("permission", rosten.webPath + "/system/permissionAdd?companyId=" + companyId + "&userid=" + userid);
    };
    read_permission = function() {
        change_permission();
    };
    change_permission = function() {
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("permission", rosten.webPath + "/system/permissionShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
        rosten.kernel.getGrid().clearSelected();
    };
    delete_permission = function() {
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.read(rosten.webPath + "/system/permissionDelete", content, delete_callback);
        };
    };
    add_role = function() {
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("role", rosten.webPath + "/system/roleAdd?companyId=" + companyId + "&userid=" + userid);
    };
    read_role = function() {
        change_role();
    };
    change_role = function() {
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;

        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("model", rosten.webPath + "/system/roleShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
        rosten.kernel.getGrid().clearSelected();
    };
    delete_role = function() {
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten._read(rosten.webPath + "/system/roleDelete", content, delete_callback);
        };
    };
    add_group = function() {
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("group", rosten.webPath + "/system/groupAdd?companyId=" + companyId + "&userid=" + userid);
    };
    read_group = function() {
        change_group();
    };
    change_group = function() {
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;

        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("model", rosten.webPath + "/system/groupShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
        rosten.kernel.getGrid().clearSelected();
    };
    delete_group = function() {
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.read(rosten.webPath + "/system/groupDelete", content, delete_callback);
        };
    };
    add_model = function() {
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("model", rosten.webPath + "/system/modelAdd?companyId=" + companyId + "&userid=" + userid);
    };
    read_model = function() {
        change_model();
    };
    change_model = function() {
        //获取参数数据
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;

        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("model", rosten.webPath + "/system/modelShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
        rosten.kernel.getGrid().clearSelected();
    };
    delete_model = function() {
        //获取参数数据
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            rosten.read(rosten.webPath + "/system/modelDelete/" + unids, content, delete_callback);
        };
    };
    delete_sms = function() {
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.read(rosten.webPath + "/system/smsDelete", content, delete_callback);
        };
    };
    change_question = function() {
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;
        rosten.openNewWindow("question", rosten.webPath + "/system/questionShow/" + unid);
        rosten.kernel.getGrid().clearSelected();
    };
    read_question = function() {
        change_question();
    };
    delete_question = function() {
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.read(rosten.webPath + "/system/questionDelete", content, delete_callback);
        };
    };
    add_administrator = function() {
        var unid = rosten.kernel.getUserInforByKey("idnumber");
        rosten.openNewWindow("administrator", rosten.webPath + "/system/userAdd?userid=" + unid);
    };
    read_administrator = function() {
        change_administrator();
    };
    change_administrator = function() {
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;

        var userid = rosten.kernel.getUserInforByKey("idnumber");
        rosten.openNewWindow("company", rosten.webPath + "/system/userShow/" + unid + "?userid=" + userid);
        rosten.kernel.getGrid().clearSelected();
    };
    delete_administrator = function() {
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.readSync(rosten.webPath + "/system/userDelete", content, delete_callback);
        };
    };
    add_company = function() {
        var unid = rosten.kernel.getUserInforByKey("idnumber");
        rosten.openNewWindow("company", rosten.webPath + "/system/companyAdd?userid=" + unid);
    };
    read_company = function() {
        change_company();
    };
    change_company = function() {
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;

        var userid = rosten.kernel.getUserInforByKey("idnumber");
        rosten.openNewWindow("company", rosten.webPath + "/system/companyShow/" + unid + "?userid=" + userid);
        rosten.kernel.getGrid().clearSelected();
    };
    delete_company = function() {
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.readSync(rosten.webPath + "/system/companyDelete", content, delete_callback);
        };
    };
    add_userType = function() {
        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("userType", rosten.webPath + "/system/userTypeAdd?companyId=" + companyId + "&userid=" + userid);
    };
    read_userType = function() {
        change_userType();
    };
    change_userType = function() {
        var unid = rosten.getGridUnid("single");
        if (unid == "")
            return;

        var userid = rosten.kernel.getUserInforByKey("idnumber");
        var companyId = rosten.kernel.getUserInforByKey("companyid");
        rosten.openNewWindow("userType", rosten.webPath + "/system/userTypeShow/" + unid + "?userid=" + userid + "&companyId=" + companyId);
        rosten.kernel.getGrid().clearSelected();
    };
    delete_userType = function() {
        var _1 = rosten.confirm("删除后将无法恢复，是否继续?");
        _1.callback = function() {
            var unids = rosten.getGridUnid("multi");
            if (unids == "")
                return;
            var content = {};
            content.id = unids;
            rosten.readSync(rosten.webPath + "/system/userTypeDelete", content, delete_callback);
        };
    };
    uploadLogo = function() {
        var upload = rosten.upLoad();
        rosten.kernel.addConnect(connect.connect(upload, 'callback', function() {
            rosten.alert("上传成功后，请使用<重新登录系统>查看变化！");
        }));
    };
    delete_callback = function(data) {
        if (data.result == "true" || data.result == true) {
            rosten.alert("成功删除!");
            rosten.kernel.refreshGrid();
        } else {
            rosten.alert("删除失败!");
        }
    };
    /*
     freshGrid = function(){
     rosten.kernel.refreshGrid();
     }*/

    /*
     * 此功能默认必须存在
     */
    show_systemNaviEntity = function(oString) {
        switch (oString) {
	        case "systemLogManage":
	            var naviJson = {
	                identifier : oString,
	                actionBarSrc : rosten.webPath + "/systemAction/systemLogView",
	                gridSrc : rosten.webPath + "/system/systemLogGrid"
	            };
	            rosten.kernel.addRightContent(naviJson);
	            break;
            case "smsManage":
                var naviJson = {
                    identifier : oString,
                    actionBarSrc : rosten.webPath + "/systemAction/smsView",
                    gridSrc : rosten.webPath + "/system/smsGrid"
                };
                rosten.kernel.addRightContent(naviJson);
                break;
            case "smsGroupManage":
                var naviJson = {
                    identifier : oString,
                    actionBarSrc : rosten.webPath + "/systemAction/smsGroupView",
                    gridSrc : rosten.webPath + "/system/smsGroupGrid"
                };
                rosten.kernel.addRightContent(naviJson);

                var rostenGrid = rosten.kernel.getGrid();
                rostenGrid.onRowDblClick = read_smsGroup;
                break;
            case "questionManage":
                var naviJson = {
                    identifier : oString,
                    actionBarSrc : rosten.webPath + "/systemAction/questionView",
                    gridSrc : rosten.webPath + "/system/questionGrid"
                };
                rosten.kernel.addRightContent(naviJson);

                var rostenGrid = rosten.kernel.getGrid();
                rostenGrid.onRowDblClick = read_question;
                break;
            case "advertiseManage":
                rosten.kernel.setHref(rosten.webPath + "/system/advertise", oString);
                break;
            case "logSet":
                rosten.kernel.setHref(rosten.webPath + "/system/logoSet", oString);
                break;
            case "systemToolManage":
                rosten.kernel.setHref(rosten.webPath + "/system/systemTool", oString);
                break;
            case "companyManage":
                var naviJson = {
                    identifier : oString,
                    actionBarSrc : rosten.webPath + "/systemAction/companyView",
                    gridSrc : rosten.webPath + "/system/companyGrid"
                };
                rosten.kernel.addRightContent(naviJson);

                var rostenGrid = rosten.kernel.getGrid();
                rostenGrid.onRowDblClick = read_company;
                break;
            case "adminManage":
                var naviJson = {
                    identifier : oString,
                    actionBarSrc : rosten.webPath + "/systemAction/administratorView",
                    gridSrc : rosten.webPath + "/system/administratorGrid"
                };
                rosten.kernel.addRightContent(naviJson);

                var rostenGrid = rosten.kernel.getGrid();
                rostenGrid.onRowDblClick = read_administrator;
                break;
            case "modelManage":
                var companyId = rosten.kernel.getUserInforByKey("companyid");
                var naviJson = {
                    identifier : oString,
                    actionBarSrc : rosten.webPath + "/systemAction/modelView",
                    gridSrc : rosten.webPath + "/system/modelGrid?companyId=" + companyId
                };
                rosten.kernel.addRightContent(naviJson);

                var rostenGrid = rosten.kernel.getGrid();
                rostenGrid.onRowDblClick = change_model;
                break;

            case "departManage" :
                var companyId = rosten.kernel.getUserInforByKey("companyid");
                rosten.kernel.setHref(rosten.webPath + "/system/depart?companyId=" + companyId, oString);
                break;
            case "groupManage" :
                var companyId = rosten.kernel.getUserInforByKey("companyid");
                var naviJson = {
                    identifier : oString,
                    actionBarSrc : rosten.webPath + "/systemAction/groupView",
                    gridSrc : rosten.webPath + "/system/groupGrid?companyId=" + companyId
                };
                rosten.kernel.addRightContent(naviJson);

                var rostenGrid = rosten.kernel.getGrid();
                rostenGrid.onRowDblClick = change_group;
                break;
            case "userManage" :
//                var companyId = rosten.kernel.getUserInforByKey("companyid");
//                var naviJson = {
//                    identifier : oString,
//                    actionBarSrc : rosten.webPath + "/systemAction/userView",
//                    gridSrc : rosten.webPath + "/system/userGrid?companyId=" + companyId
//                };
//                rosten.kernel.addRightContent(naviJson);
//
//                var rostenGrid = rosten.kernel.getGrid();
//                rostenGrid.onRowDblClick = change_user;
//                break;
                
                //2014-7-13修改为人事系统中的员工信息
                var companyId = rosten.kernel.getUserInforByKey("companyid");
                rosten.kernel.setHref(rosten.webPath + "/staff/depart?companyId=" + companyId, oString);
                break;
                
            case "userTypeManage" :
                var companyId = rosten.kernel.getUserInforByKey("companyid");
                var naviJson = {
                    identifier : oString,
                    actionBarSrc : rosten.webPath + "/systemAction/userTypeView",
                    gridSrc : rosten.webPath + "/system/userTypeGrid?companyId=" + companyId
                };
                rosten.kernel.addRightContent(naviJson);

                var rostenGrid = rosten.kernel.getGrid();
                rostenGrid.onRowDblClick = change_userType;
                break;
            case "roleManage" :
                var companyId = rosten.kernel.getUserInforByKey("companyid");
                var naviJson = {
                    identifier : oString,
                    actionBarSrc : rosten.webPath + "/systemAction/roleView",
                    gridSrc : rosten.webPath + "/system/roleGrid?companyId=" + companyId
                };
                rosten.kernel.addRightContent(naviJson);

                var rostenGrid = rosten.kernel.getGrid();
                rostenGrid.onRowDblClick = change_role;
                break;
            case "permissionManage":
                var companyId = rosten.kernel.getUserInforByKey("companyid");
                var naviJson = {
                    identifier : oString,
                    actionBarSrc : rosten.webPath + "/systemAction/permissionView",
                    gridSrc : rosten.webPath + "/system/permissionGrid?companyId=" + companyId
                };
                rosten.kernel.addRightContent(naviJson);

                var rostenGrid = rosten.kernel.getGrid();
                rostenGrid.onRowDblClick = change_permission;
                break;
            case "resourceManage":
                var companyId = rosten.kernel.getUserInforByKey("companyid");
                var naviJson = {
                    identifier : oString,
                    actionBarSrc : rosten.webPath + "/systemAction/resourceView",
                    gridSrc : rosten.webPath + "/system/resourceGrid?companyId=" + companyId
                };
                rosten.kernel.addRightContent(naviJson);

                var rostenGrid = rosten.kernel.getGrid();
                rostenGrid.onRowDblClick = change_resource;
                break;
            case "serviceManage":
                var companyId = rosten.kernel.getUserInforByKey("companyid");
                var naviJson = {
                    identifier : oString,
                    actionBarSrc : rosten.webPath + "/systemAction/serviceView",
                    gridSrc : rosten.webPath + "/system/serviceGrid?companyId=" + companyId
                };
                rosten.kernel.addRightContent(naviJson);

                var rostenGrid = rosten.kernel.getGrid();
                rostenGrid.onRowDblClick = change_service;
                break;
                
        }

    };
    connect.connect("show_naviEntity", show_systemNaviEntity);
});
