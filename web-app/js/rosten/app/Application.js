/**
 * @author rosten
 */
define(["dojo/_base/lang",
		"dijit/registry",
		"rosten/widget/MultiSelectDialog",
		"rosten/widget/PickTreeDialog",
		"rosten/widget/DepartUserDialog",
		"rosten/widget/ShowDialog",
		"rosten/kernel/_kernel"], function(lang,registry,MultiSelectDialog,PickTreeDialog,DepartUserDialog,ShowDialog) {
			
	var application = {};
    application.cssinitcommon = function() {
        //此功能只添加css文件
        var _rosten = window.opener.rosten;
        var dojocss = _rosten.dojothemecss;
        var rostencss = _rosten.rostenthemecss;

        rosten.addDojoThemeCss(dojocss);
        rosten.addRostenCss(rostencss);
    };
    application.cssinit = function() {
        var _rosten = window.opener.rosten;
        var dojocss = _rosten.dojothemecss;
        var rostencss = _rosten.rostenthemecss;

        rosten.replaceDojoTheme(dojocss, false);
        rosten.replaceRostenTheme(rostencss);
    };
    
    application.checkData = function(chenkids){
		var flag=true;
		for(var i = 0 ;i<chenkids.length;i++){
			var obj = registry.byId(chenkids[i]);
			if(!obj.isValid()){
				rosten.alert("请正确填写信息！").queryDlgClose = function(){
					obj.focus();
				};
				flag=false;
				break;
			}
			}
		return flag;
		}
    
    /*
     * 关闭当前窗口，并刷新父文档视图
     */
    application.pagequit = function() {
    	if(window.opener.rosten.kernel){
    		window.opener.rosten.kernel.refreshGrid();
    	}
        window.close();
    };
    application.selectDialog = function(dialogTitle,id,url,flag,defaultValue,reload){
		/*
		 * dialogTitle:dialog中的titile
		 * id:dialog的id号需唯一
		 * url:url
		 * flag：是否多选，true为多选，默认为false
		 * reload:是否重新载入
		 * defaultValue：对话框中显示的值,为[]数组
		 */
		if (!(rosten[id] && registry.byId(id))) {
			rosten[id] = new MultiSelectDialog({
				title:dialogTitle,
		        id: id,
				single:!flag,
				datasrc:url
			});
			if(defaultValue!=undefined){
				rosten[id].defaultvalues = defaultValue;
			}
			rosten[id].open();
		}else{
			rosten[id].single = !flag;
			if(defaultValue!=undefined){
				rosten[id].defaultvalues = defaultValue;
			}
			rosten[id].open();
			if(reload!=undefined && reload==true){
				rosten[id].datasrc = url;
				rosten[id].refresh();
			}else{
				rosten[id].simpleRefresh();
			}
		}
		
	};
	application.selectFlowUser = function(url,type){
        var id = "sys_flowUserDialog";

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
                type:type
            };
            rosten[id] = new DepartUserDialog(args);
            if (rosten[id].initialized == false) {
        		rosten[id].buildContent(rosten[id].contentPane);
        		rosten[id].buildControl(rosten[id].controlPane);
        		rosten[id].initialized = true;
			}
        }
//        var _data = rosten[id].getData();
//        if(_data && _data.length==1){
//            //直接调用
//        	rosten[id].doAction();
//        }else{
//			//显示对话框
//        	rosten[id].open();
//	    }
        return rosten[id];
    };
    application.selectUser = function(url,type,inputName,inputId){
        var id = "sys_userDialog";

        if (rosten[id] && registry.byId(id)) {
            rosten[id].open();
            rosten[id].refresh();
        } else {
            var args = {
                url : url,
                type:type
            };
            rosten[id] = new DepartUserDialog(args);
            rosten[id].open();
        }
        rosten[id].callback = function(data) {
            var _data = [];
            var _data_1 = [];
            for (var k = 0; k < data.length; k++) {
                var item = data[k];
                _data.push(item.name);
                _data_1.push(item.value);
            }
            if( inputName !=undefined){
                registry.byId(inputName).attr("value", _data.join(","));
            }
            if( inputId !=undefined){
                registry.byId(inputId).attr("value",_data_1.join(","));
            }
        }; 
        return rosten[id];
    };
	application.selectDepart = function(url,type,inputName,inputId) {
        var id = "sys_departDialog";

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
            var _data = [];
            var _data_1 = [];
            for (var k = 0; k < data.length; k++) {
                var item = data[k];
                _data.push(item.name);
                _data_1.push(item.id);

            }
            if( inputName !=undefined){
            	registry.byId(inputName).attr("value", _data.join(","));
            }
            if( inputId !=undefined){
            	registry.byId(inputId).attr("value", _data_1.join(","));
            }
        };
    };
    application.createRostenShowDialog = function(src,args){
        var obj = {src:src};
        if(args){
            if(args.callback)obj.callback = args.callback;
            if(args.callbackargs) obj.callbackargs = args.callbackargs;
            if(args.onLoadFunction) obj.onLoadFunction = args.onLoadFunction;
        }
        if(application.rostenShowDialog) application.rostenShowDialog.destroy();
        application.rostenShowDialog = new ShowDialog(obj);
    },
    application.hideRostenShowDialog = function(){
        if (application.rostenShowDialog){
        	application.rostenShowDialog.hide();
        	application.rostenShowDialog.destroy();
        }
    },
    application.getGridItemValue = function(rostenGrid,index,name){
    	var grid = rostenGrid.getGrid();
    	var item = grid.getItem(index);
    	var store = rostenGrid.getStore();
    	return store.getValue(item, name);
    },
    application.getGridItem = function(rostenGrid,index){
    	var grid = rostenGrid.getGrid();
    	var item = grid.getItem(index);
    	return item;
    },
    lang.mixin(rosten,application);
    
    return application;
});
