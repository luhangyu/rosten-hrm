/**
 * @author rosten
 */
define(["dojo/_base/lang",
		"dijit/registry",
		"dojo/has",
		"dojo/dom",
		"dojo/json",
		"rosten/widget/MultiSelectDialog",
		"rosten/widget/PickTreeDialog",
		"rosten/widget/DepartUserDialog",
		"rosten/widget/ShowDialog",
		"rosten/kernel/_kernel"], function(lang,registry,has,dom,JSON,MultiSelectDialog,PickTreeDialog,DepartUserDialog,ShowDialog) {
			
	var application = {};
	
	/*
	 * 获取表格中的数据集合，并返回jsonString类型
	 * grid:表格widget;dealArray:需要处理的字段名称集合;query:查询参数json类型
	 */
	application.getGridDataCollect =function(grid,dealArray,query){
		var gridContent=[]
		
		var searchQuery = {id:"*"};
		if(query) searchQuery = query;
		
		var store = grid.getStore();
		store.fetch({
			query:searchQuery,onComplete:function(items){
				for(var i=0;i < items.length;i++){
					var _item = items[i];
					var jsonObj = {};
					
					for(var j = 0 ;j<dealArray.length;j++){
						jsonObj[dealArray[j]] = store.getValue(_item, dealArray[j]);
					}
					gridContent.push(jsonObj);
				}
			},queryOptions:{deep:true}
		});
		return JSON.stringify(gridContent);
	};
	
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
	};
    
    application.check_common = function(fieldStr,alertStr,isFocus){
		var _dom = registry.byId(fieldStr);
		if(_dom && !_dom.isValid()){
			if(isFocus){
				rosten.alert(alertStr).queryDlgClose = function(){
					_dom.focus();
				};
			}else{
				rosten.alert(alertStr);
			}
			return false;
		}
		return true;
	};
    
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
    application.selectBaseDialog = function(title,url,type,inputName,inputId,initValues) {
        /*
         * url:http://.....
         * type:是否单选
         * initValue:"单位1,单位2...",inputName:页面input显示id，inputId：页面input隐藏Id
         */
  
        var id = "base_contactCorpDialog";
        var initValue =[];
        if(initValues){
            initValue = initValues.split(",");
        }
        if(title==null){
            title = "系统选择框";
        }
        rosten.selectDialog(title, id, url, type, initValue);
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
                registry.byId(inputId).attr("value",_data_1.join(","));
            }
        };
        return rosten[id];
    };
    application.selectBaseTreeDialog = function(title,url,type,inputName,inputId,showRootName) {
        var id = "sys_base_treeDialog";
        
        if(title==null){
            title = "系统选择框";
        }
        
        if (rosten[id] && registry.byId(id)) {
            rosten[id].open();
            rosten[id].refresh();
        } else {
            var args = {
                url : url,
                showCheckBox : type,
                title:title,
                folderClass : "departTree"
            };
            if(showRootName==undefined || showRootName!=""){
                args.showRoot=false;
            }else{
                args.showRoot=true;
                args.rootLabel = showRootName;
            }
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
                if(registry.byId(inputId)){
                    registry.byId(inputId).attr("value", _data_1.join(","));
                }else{
                    dom.byId(inputId).value = _data_1.join(",");
                }
                
            }
        };
        return rosten[id];
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
    application.getGridItemValue1 = function(rostenGrid,name){
        var selectitems = rostenGrid.getSelected();
		
		if(selectitems.length<=0){
			rosten.alert("请先选择条目！");
			return "";
		}
		var gridStore = rostenGrid.getStore();
		var item;
		var idArgs;
		var getName = "id";
		if(name) getName = name;
    	item = selectitems[0];
    	idArgs = gridStore.getValue(item, getName);
       
		return idArgs;
	};
	application.addAttachShowNew = function(node,jsonObj){
		var div = document.createElement("div");
		div.setAttribute("style","height:30px;width:50%;float:left");
		div.setAttribute("id",jsonObj.fileId);
		
		var a = document.createElement("a");
		if (has("ie")) {
			a.href = rosten.webPath + "/system/downloadFile/" + jsonObj.fileId;
		}else{
			a.setAttribute("href", rosten.webPath + "/system/downloadFile/" + jsonObj.fileId);
		}
		a.setAttribute("style","margin-right:20px");
		a.setAttribute("dealId",jsonObj.fileId);
		a.innerHTML = jsonObj.fileName;
		div.appendChild(a);
		
		var deleteA = document.createElement("a");
		deleteA.setAttribute("style","color:green");
		if (has("ie")) {
			deleteA.href = "javascript:rosten.deleteFile('" + node.getAttribute("id") + "','" + jsonObj.fileId + "')";
		}else{
			deleteA.setAttribute("href", "javascript:rosten.deleteFile('" + node.getAttribute("id")+"','" + jsonObj.fileId + "')");
		}
		deleteA.innerHTML = "删除";
		div.appendChild(deleteA);
		
		node.appendChild(div);
		
	};
	application.deleteFile = function(objId,attachmentId){
		
		rosten.readNoTime(rosten.webPath + "/share/deleteAttachmentFile/"+attachmentId, {},function(data){
			if(data.result==true || data.result=="true"){
				var node = dom.byId(objId);
				var item = dom.byId(attachmentId);
				node.removeChild(item);
				rosten.alert("删除成功！");
			}else{
				rosten.alert("删除失败！");
			}
		});
	};
    lang.mixin(rosten,application);
    
    return application;
});
