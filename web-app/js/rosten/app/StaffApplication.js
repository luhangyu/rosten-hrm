/**
 * @author rosten
 */
define(["dojo/dom",
        "dijit/registry",
        "dojo/date/stamp",
        "rosten/widget/PickTreeDialog",
        "rosten/app/Application",
        "rosten/kernel/behavior"], function(dom,registry,stamp,PickTreeDialog) {
	
	
	degree_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:degree_onMessageOpen(" + rowIndex + ");\">" + value+ "</a>";
	};
	degree_onMessageOpen = function(rowIndex){
		//打开systemCodeItem信息
    	rosten.createRostenShowDialog(rosten.webPath + "/staff/degreeInforShow", {
            onLoadFunction : function() {
            	var id = rosten.getGridItemValue(degreeGrid,rowIndex,"id");
            	var name = rosten.getGridItemValue(degreeGrid,rowIndex,"degreeName");
            	var major = rosten.getGridItemValue(degreeGrid,rowIndex,"major");
            	var degree = rosten.getGridItemValue(degreeGrid,rowIndex,"degree");
            	var startDate = rosten.getGridItemValue(degreeGrid,rowIndex,"getFormatteStartDate");
            	var endDate = rosten.getGridItemValue(degreeGrid,rowIndex,"getFormatteEndDate");
            	
            	registry.byId("degree_id").set("value",id)
            	registry.byId("degree_name").set("value",name)
            	registry.byId("degree_major").set("value",major)
            	registry.byId("degree_degree").set("value",degree)
            	registry.byId("degree_startDate").set("value",startDate)
            	registry.byId("degree_endDate").set("value",endDate)
	        }
        });
	};
	degree_delete = function(value,rowIndex){
		return "<a href=\"javascript:degree_onDelete(" + rowIndex + ");\">" + "删除" + "</a>";
	};
	degree_onDelete = function(rowIndex){
		grid_onDeleteCommon(degreeGrid,rowIndex);
	};
	staff_addDegree = function(){
		rosten.createRostenShowDialog(rosten.webPath + "/staff/degreeInforShow", {
            onLoadFunction : function() {}
        });
	};
	degree_Submit = function(){
		if(!degree_form.validate()) return;
		var degreeId = registry.byId("degree_id").get("value");

		function gotAll(items,request){
			var node;
			for(var i=0;i < items.length;i++){
				var id = store.getValue(items[i], "id");
				if(id==degreeId){
					node = items[i];
					break;
				}
			}
			
			if(node){
				store.setValue(node,"degreeName",registry.byId("degree_name").get("value"));
				store.setValue(node,"major",registry.byId("degree_major").get("value"));
				store.setValue(node,"degree",registry.byId("degree_degree").get("value"));
				store.setValue(node,"getFormatteStartDate",datestamp.toISOString(registry.byId("degree_startDate").get("value"),{selector: "date"}));
				store.setValue(node,"getFormatteEndDate",datestamp.toISOString(registry.byId("degree_endDate").get("value"),{selector: "date"}));
			}else{
				var randId = Math.random();
				store.newItem({
					id:randId,
					actionId:randId,
					rowIndex:items.length+1,
					degreeName:registry.byId("degree_name").get("value"),
					major:registry.byId("degree_major").get("value"),
					degree:registry.byId("degree_degree").get("value"),
					getFormatteStartDate:registry.byId("degree_startDate").get("value"),
					getFormatteEndDate:registry.byId("degree_endDate").get("value")
				});
			}
		}
		
		var store = degreeGrid.getStore();
		store.fetch({
			query:{id:"*"},onComplete:gotAll,queryOptions:{deep:true}
		});
		rosten.hideRostenShowDialog();
	};
	
	workResume_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:workResume_onMessageOpen(" + rowIndex + ");\">" + value+ "</a>";
	};
	workResume_onMessageOpen = function(rowIndex){
		//打开systemCodeItem信息
    	rosten.createRostenShowDialog(rosten.webPath + "/staff/workResumeInforShow", {
            onLoadFunction : function() {
            	var id = rosten.getGridItemValue(workResumeGrid,rowIndex,"id");
            	var workCompany = rosten.getGridItemValue(workResumeGrid,rowIndex,"workCompany");
            	var workContent = rosten.getGridItemValue(workResumeGrid,rowIndex,"workContent");
            	var getFormatteStartDate = rosten.getGridItemValue(workResumeGrid,rowIndex,"getFormatteStartDate");
            	var getFormatteEndDate = rosten.getGridItemValue(workResumeGrid,rowIndex,"getFormatteEndDate");
            	var duty = rosten.getGridItemValue(workResumeGrid,rowIndex,"duty");
            	var proveName = rosten.getGridItemValue(workResumeGrid,rowIndex,"proveName");
            	var remark = rosten.getGridItemValue(workResumeGrid,rowIndex,"remark");
            	
            	registry.byId("workResume_id").set("value",id)
            	registry.byId("workResume_workCompany").set("value",workCompany)
            	registry.byId("workResume_workContent").set("value",workContent)
            	registry.byId("workResume_startDate").set("value",getFormatteStartDate)
            	registry.byId("workResume_endDate").set("value",getFormatteEndDate)
            	registry.byId("workResume_duty").set("value",duty)
            	registry.byId("workResume_proveName").set("value",proveName)
            	registry.byId("workResume_remark").set("value",remark)
	        }
        });
	};
	workResume_delete = function(value,rowIndex){
		return "<a href=\"javascript:workResume_onDelete(" + rowIndex + ");\">" + "删除" + "</a>";
	};
	workResume_onDelete = function(rowIndex){
		grid_onDeleteCommon(workResumeGrid,rowIndex);
	};
	staff_addWorkResume = function(){
		rosten.createRostenShowDialog(rosten.webPath + "/staff/workResumeInforShow", {
            onLoadFunction : function() {}
        });
	};
	workResume_Submit = function(){
		if(!workResume_form.validate()) return;
		var workResumeId = registry.byId("workResume_id").get("value");

		function gotAll(items,request){
			var node;
			for(var i=0;i < items.length;i++){
				var id = store.getValue(items[i], "id");
				if(id==workResumeId){
					node = items[i];
					break;
				}
			}
			
			if(node){
				store.setValue(node,"workCompany",registry.byId("workResume_workCompany").get("value"));
				store.setValue(node,"workContent",registry.byId("workResume_workContent").get("value"));
				store.setValue(node,"getFormatteStartDate",registry.byId("workResume_startDate").get("value"));
				store.setValue(node,"getFormatteEndDate",registry.byId("workResume_endDate").get("value"));
				store.setValue(node,"duty",registry.byId("workResume_duty").get("value"));
				store.setValue(node,"proveName",registry.byId("workResume_proveName").get("value"));
				store.setValue(node,"remark",registry.byId("workResume_remark").get("value"));
			}else{
				var randId = Math.random();
				store.newItem({
					id:randId,
					actionId:randId,
					rowIndex:items.length+1,
					workCompany:registry.byId("workResume_workCompany").get("value"),
					workContent:registry.byId("workResume_workContent").get("value"),
					getFormatteStartDate:registry.byId("workResume_startDate").get("value"),
					getFormatteEndDate:registry.byId("workResume_endDate").get("value"),
					duty:registry.byId("workResume_duty").get("value"),
					proveName:registry.byId("workResume_proveName").get("value"),
					remark:registry.byId("workResume_remark").get("value")
				});
			}
		}
		
		var store = workResumeGrid.getStore();
		store.fetch({
			query:{id:"*"},onComplete:gotAll,queryOptions:{deep:true}
		});
		rosten.hideRostenShowDialog();
	};
	
	familyInfor_formatTopic = function(value,rowIndex){
		return "<a href=\"javascript:familyInfor_onMessageOpen(" + rowIndex + ");\">" + value+ "</a>";
	};
	familyInfor_onMessageOpen = function(rowIndex){
		//打开systemCodeItem信息
    	rosten.createRostenShowDialog(rosten.webPath + "/staff/familyInforShow", {
            onLoadFunction : function() {
            	var id = rosten.getGridItemValue(staffFamilyGrid,rowIndex,"id");
            	var name = rosten.getGridItemValue(staffFamilyGrid,rowIndex,"name");
            	var relation = rosten.getGridItemValue(staffFamilyGrid,rowIndex,"relation");
            	var mobile = rosten.getGridItemValue(staffFamilyGrid,rowIndex,"mobile");
            	var workUnit = rosten.getGridItemValue(staffFamilyGrid,rowIndex,"workUnit");
            	var duties = rosten.getGridItemValue(staffFamilyGrid,rowIndex,"duties");
            	var politicsStatus = rosten.getGridItemValue(staffFamilyGrid,rowIndex,"politicsStatus");
            	
            	registry.byId("family_id").set("value",id)
            	registry.byId("family_name").set("value",name)
            	registry.byId("family_relation").set("value",relation)
            	registry.byId("family_mobile").set("value",mobile)
            	registry.byId("family_workUnit").set("value",workUnit)
            	registry.byId("family_duties").set("value",duties)
            	registry.byId("family_politicsStatus").set("value",politicsStatus)
	        }
        });
	};
	familyInfor_delete = function(value,rowIndex){
		return "<a href=\"javascript:familyInfor_onDelete(" + rowIndex + ");\">" + "删除" + "</a>";
	};
	familyInfor_onDelete = function(rowIndex){
		//删除item信息
		var store = staffFamilyGrid.getStore();
	    var item = rosten.getGridItem(staffFamilyGrid,rowIndex);
		store.deleteItem(item);
		//更新store中的rowIndex号
		store.fetch({
			query:{id:"*"},onComplete:function(items){
				for(var i=0;i < items.length;i++){
					var _item = items[i];
					store.setValue(_item,"rowIndex",i+1);
				}
			},queryOptions:{deep:true}
		});
	};
	staff_addFamily = function(){
		rosten.createRostenShowDialog(rosten.webPath + "/staff/familyInforShow", {
            onLoadFunction : function() {}
        });
	};
	familyInfor_Submit = function(){
		var chenkids = ["family_name","family_relation","family_mobile"];
		if(!rosten.checkData(chenkids)) return;
		
		var familyId = registry.byId("family_id").get("value");
		
		function gotAll(items,request){
			var node;
			for(var i=0;i < items.length;i++){
				var id = store.getValue(items[i], "id");
				if(id==familyId){
					node = items[i];
					break;
				}
			}
			
			if(node){
				store.setValue(node,"name",registry.byId("family_name").get("value"));
				store.setValue(node,"relation",registry.byId("family_relation").get("value"));
				store.setValue(node,"mobile",registry.byId("family_mobile").get("value"));
				store.setValue(node,"workUnit",registry.byId("family_workUnit").get("value"));
				store.setValue(node,"duties",registry.byId("family_duties").get("value"));
				store.setValue(node,"politicsStatus",registry.byId("family_politicsStatus").get("value"));
			}else{
				var randId = Math.random();
				store.newItem({
					id:randId,
					familyInforId:randId,
					rowIndex:items.length+1,
					name:registry.byId("family_name").get("value"),
					relation:registry.byId("family_relation").get("value"),
					mobile:registry.byId("family_mobile").get("value"),
					workUnit:registry.byId("family_workUnit").get("value"),
					duties:registry.byId("family_duties").get("value"),
					politicsStatus:registry.byId("family_politicsStatus").get("value")
				});
			}
		}
		
		var store = staffFamilyGrid.getStore();
		store.fetch({
			query:{id:"*"},onComplete:gotAll,queryOptions:{deep:true}
		});
		rosten.hideRostenShowDialog();
	};
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
    
    /*
     * 抽取出来的公共函数---------------------------------------------------
     */
    
    grid_onDeleteCommon = function(grid,rowIndex){
    	//删除item信息
		var store = grid.getStore();
	    var item = rosten.getGridItem(grid,rowIndex);
		store.deleteItem(item);
		//更新store中的rowIndex号
		store.fetch({
			query:{id:"*"},onComplete:function(items){
				for(var i=0;i < items.length;i++){
					var _item = items[i];
					store.setValue(_item,"rowIndex",i+1);
				}
			},queryOptions:{deep:true}
		});
    };
});
