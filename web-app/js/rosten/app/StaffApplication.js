/**
 * @author rosten
 */
define(["dojo/dom",
        "dijit/registry",
        "rosten/widget/PickTreeDialog",
        "rosten/app/Application",
        "rosten/kernel/behavior"], function(dom,registry,PickTreeDialog) {
	
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

		function gotAll(items,request){
			if(items && items.length>0){
				store.setValue(items[0],"name",registry.byId("family_name").get("value"));
				store.setValue(items[0],"relation",registry.byId("family_relation").get("value"));
				store.setValue(items[0],"mobile",registry.byId("family_mobile").get("value"));
				store.setValue(items[0],"workUnit",registry.byId("family_workUnit").get("value"));
				store.setValue(items[0],"duties",registry.byId("family_duties").get("value"));
				store.setValue(items[0],"politicsStatus",registry.byId("family_politicsStatus").get("value"));
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
			query:{id:registry.byId("family_id").get("value")},onComplete:gotAll,queryOptions:{deep:true}
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
    
});
