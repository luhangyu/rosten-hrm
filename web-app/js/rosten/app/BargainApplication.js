/**
 * @author rosten
 */
define(["dojo/dom",
        "dijit/registry",
        "rosten/app/Application",
        "rosten/kernel/behavior"], function(dom,registry) {
	
	bargainItem_formatFile = function(value,rowIndex){
		return "<a href=\"javascript:bargainItem_onFileMessageOpen(" + rowIndex + ");\">" + value+ "</a>";
	};
	bargainItem_onFileMessageOpen = function(rowIndex){
		//打开附件信息
		var fileId = rosten.getGridItemValue(bargainItemGrid,rowIndex,"bargainFileId");
		if(fileId!=""){
			rosten.openNewWindow("bargainFile", rosten.webPath + "/system/downloadFile/"+fileId);
		}
    };
	bargainItem_formatTopic = function(value,rowIndex){
		return value;
//		return "<a href=\"javascript:bargainItem_onMessageOpen(" + rowIndex + ");\">" + value+ "</a>";
	};
	bargainItem_onMessageOpen = function(rowIndex){
		//打开systemCodeItem信息
    	rosten.createRostenShowDialog(rosten.webPath + "/bargain/bargainItemShow", {
            onLoadFunction : function() {
            	var itemId = rosten.getGridItemValue(bargainItemGrid,rowIndex,"id");
            	var code = rosten.getGridItemValue(bargainItemGrid,rowIndex,"code");
            	var name = rosten.getGridItemValue(bargainItemGrid,rowIndex,"name");

	        }
        });
    };
    bargain_action = function(value,rowIndex){
    	return "<a href=\"javascript:bargain_onDelete(" + rowIndex + ");\">" + "删除" + "</a>";
	};
	bargain_onDelete = function(rowIndex){
		//删除item信息
		var store = bargainItemGrid.getStore();
	    var item = rosten.getGridItem(bargainItemGrid,rowIndex);
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
