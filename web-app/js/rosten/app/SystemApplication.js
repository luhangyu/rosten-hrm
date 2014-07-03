/**
 * @author rosten
 */
define(["dojo/dom",
        "dijit/registry",
        "rosten/widget/PickTreeDialog",
        "rosten/app/Application",
        "rosten/kernel/behavior"], function(dom,registry,PickTreeDialog) {

    page_quit_1 = function(flag) {
        var parentNode = window.opener;
        window.close();
        if (flag && flag == true) {
            parentNode.refreshSystem();
        }
    };
    page_quit = function() {
        rosten.pagequit();
    };
    selectCompany = function() {
        var id = "sys_companyDialog";
        var initValue = [];
        var companyName = registry.byId("companyName");
        if (companyName.attr("value") != "") {
            initValue.push(companyName.attr("value"));
        }
        rosten.selectDialog("机构选择", id, rosten.webPath + "/system/userSelectCompany", false, initValue);
        rosten[id].callback = function(data) {
        	registry.byId("companyName").attr("value", data[0].name);
        	registry.byId("companyId").attr("value", data[0].id);
        };
    };
    selectModel = function(companyId) {
        var id = "sys_modelDialog";
        var initValue = [];
        var modelName = registry.byId("modelName");
        if (modelName.attr("value") != "") {
            initValue.push(modelName.attr("value"));
        }
        rosten.selectDialog("模块选择", id, rosten.webPath + "/system/modelSelect?companyId=" + companyId, false, initValue);
        rosten[id].callback = function(data) {
        	registry.byId("modelName").attr("value", data[0].name);
        	registry.byId("modelId").attr("value", data[0].id);
        };
    };
    selectDepart = function(url) {
        var id = "sys_departDialog";

        if (rosten[id] && registry.byId(id)) {
            rosten[id].open();
            rosten[id].refresh();
        } else {
            var args = {
                url : url,
                rootLabel : "部门层级",
                showCheckBox : false,
                folderClass : "departTree"
            };
            rosten[id] = new PickTreeDialog(args);
            rosten[id].open();
        }
        rosten[id].callback = function(data) {
            var _data = "";
            var _data_1 = "";
            for (var k = 0; k < data.length; k++) {
                var item = data[k];
                if (_data == "") {
                    _data += item.name;
                    _data_1 += item.id;
                } else {
                    _data += "," + item.name;
                    _data_1 += "," + item.id;
                }

            }
            registry.byId("allowdepartsName").attr("value", _data);
            dom.byId("allowdepartsId").value = _data_1;
        };
    };
    selectUser = function(url,inputName) {
        var id = "sys_userDialog";

        if (rosten[id] && registry.byId(id)) {
            rosten[id].open();
            rosten[id].refresh();
        } else {
            var args = {
                url : url,
                rootLabel : "用户层级",
                showCheckBox : true,
                folderClass : "departTree"
            };
            rosten[id] = new PickTreeDialog(args);
            rosten[id].open();
        }
        rosten[id].callback = function(data) {
            var _data = "";
            var _data_1 = "";
            for (var k = 0; k < data.length; k++) {
                var item = data[k];
                if (item.type == "user") {
                    if (_data == "") {
                        _data += item.name;
                        _data_1 += item.id;
                    } else {
                        _data += "," + item.name;
                        _data_1 += "," + item.id;
                    }
                }
            }
            if( inputName ==undefined){
                registry.byId("allowusersName").attr("value", _data);
                dom.byId("allowusersId").value = _data_1;
            }else{
                registry.byId(inputName).attr("value", _data);
            }
            
        };
    };
    selectGroup = function(url) {
        var id = "sys_groupDialog";
        var initValue = [];
        var allowgroupsName = registry.byId("allowgroupsName");
        if (allowgroupsName.attr("value") != "") {
            initValue.push(allowgroupsName.attr("value"));
        }
        rosten.selectDialog("群组选择", id, url, true, initValue);
        rosten[id].callback = function(data) {
            var _data = "";
            var _data_1 = "";
            for (var k = 0; k < data.length; k++) {
                var item = data[k];
                if (_data == "") {
                    _data += item.name;
                    _data_1 += item.id;
                } else {
                    _data += "," + item.name;
                    _data_1 += "," + item.id;
                }
            }
            registry.byId("allowgroupsName").attr("value", _data);
            dom.byId("allowgroupsId").value = _data_1;
        };
    };
    selectRelationFlow = function(url) {
        var id = "sys_relationFlowDialog";
        var initValue = [];
        var allowRelationFlow = registry.byId("allowRelationFlow");
        if (allowRelationFlow.attr("value") != "") {
            initValue.push(allowRelationFlow.attr("value"));
        }
        rosten.selectDialog("关联流程选择", id, url, true, initValue);
        rosten[id].callback = function(data) {
            var _data = "";
            var _data_1 = "";
            for (var k = 0; k < data.length; k++) {
                var item = data[k];
                if (_data == "") {
                    _data += item.name;
                    _data_1 += item.id;
                } else {
                    _data += "," + item.name;
                    _data_1 += "," + item.id;
                }
            }
            registry.byId("allowRelationFlow").attr("value", _data);
            dom.byId("relationFlow").value = _data_1;
        };
    };
    selectRole = function(url) {
        var id = "sys_roleDialog";
        var initValue = [];
        var allowrolesName = registry.byId("allowrolesName");
        if (allowrolesName.attr("value") != "") {
            initValue.push(allowrolesName.attr("value"));
        }
        rosten.selectDialog("角色选择", id, url, true, initValue);
        rosten[id].callback = function(data) {
            var _data = "";
            var _data_1 = "";
            for (var k = 0; k < data.length; k++) {
                var item = data[k];
                if (_data == "") {
                    _data += item.name;
                    _data_1 += item.id;
                } else {
                    _data += "," + item.name;
                    _data_1 += "," + item.id;
                }
            }
            registry.byId("allowrolesName").attr("value", _data);
            dom.byId("allowrolesId").value = _data_1;
        };
    };
    selectPermission = function(url) {
        var id = "sys_permissionDialog";
        var initValue = [];
        var allowpermissionsName = registry.byId("allowpermissionsName");
        if (allowpermissionsName.attr("value") != "") {
            initValue.push(allowpermissionsName.attr("value"));
        }
        rosten.selectDialog("权限选择", id, url, true, initValue);
        rosten[id].callback = function(data) {
            var _data = "";
            var _data_1 = "";
            for (var k = 0; k < data.length; k++) {
                var item = data[k];
                if (_data == "") {
                    _data += item.name;
                    _data_1 += item.id;
                } else {
                    _data += "," + item.name;
                    _data_1 += "," + item.id;
                }
            }
            registry.byId("allowpermissionsName").attr("value", _data);
            dom.byId("allowpermissionsId").value = _data_1;
        };
    };
    selectResource = function(url) {
        var id = "sys_resourceDialog";

        if (rosten[id] && registry.byId(id)) {
            rosten[id].open();
            rosten[id].refresh();
        } else {
            var args = {
                url : url,
                rootLabel : "资源层级",
                showCheckBox : true,
                folderClass : "departTree"
            };
            rosten[id] = new PickTreeDialog(args);
            rosten[id].open();
        }
        rosten[id].callback = function(data) {
            var _data = "";
            var _data_1 = "";
            for (var k = 0; k < data.length; k++) {
                var item = data[k];
                if (item.type == "resource") {
                    if (_data == "") {
                        _data += item.name;
                        _data_1 += item.id;
                    } else {
                        _data += "," + item.name;
                        _data_1 += "," + item.id;
                    }
                }
            }
            registry.byId("allowresourcesName").attr("value", _data);
            dom.byId("allowresourcesId").value = _data_1;
        };
    };
    selectResource1 = function(url) {
        var id = "sys_resourceDialog1";

        if (rosten[id] && registry.byId(id)) {
        	if (rosten[id].initialized == false) {
        		rosten[id].buildContent(rosten[id].contentPane);
        		rosten[id].buildControl(rosten[id].controlPane);
        		rosten[id].initialized = true;
			}
            rosten[id].refresh();
        } else {
            var args = {
                url : url,
                rootLabel : "资源层级",
                showCheckBox : true,
                folderClass : "departTree"
            };
            rosten[id] = new PickTreeDialog(args);
            if (rosten[id].initialized == false) {
        		rosten[id].buildContent(rosten[id].contentPane);
        		rosten[id].buildControl(rosten[id].controlPane);
        		rosten[id].initialized = true;
			}
        }
        rosten[id].callback = function(data) {
            var _data_1 = "";
            for (var k = 0; k < data.length; k++) {
                var item = data[k];
                if (item.type == "resource") {
                    if (_data_1 == "") {
                        _data_1 += item.id;
                    } else {
                        _data_1 += "," + item.id;
                    }
                }
            }
            dom.byId("allowresourcesId").value = _data_1;
        };
        return rosten[id];
    };
});
