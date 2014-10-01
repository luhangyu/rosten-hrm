<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>代码配置</title>
    <style type="text/css">
		body{
			overflow:auto;
		}
		.rosten .rostenTitleGrid .dijitTitlePaneContentInner{
			padding:2px 1px 1px 1px;
			height:330px;
		}
    </style>
	<script type="text/javascript">
		require(["dojo/parser",
		 		"dojo/_base/kernel",
		 		"dijit/registry",
		 		"dojo/json",
		 		"dijit/form/ValidationTextBox",
		 		"dijit/form/Button",
		 		"dijit/form/Form",
		 		"rosten/widget/TitlePane",
		 		"rosten/widget/RostenGrid",
		     	"rosten/widget/ActionBar",
		     	"rosten/app/Application",
		     	"rosten/kernel/behavior"],
			function(parser,kernel,registry,JSON){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}",dojogridcss : true});
					rosten.cssinit();
				});
				
			systemCode_add = function(object){
				var chenkids = ["code","name"];
				if(!rosten.checkData(chenkids)) return;
				
				var content = {};
				
				//获取表格中的数据-------------------------------------
				var gridContent=[]
				
				var store = systemCodeItemGrid.getStore();
				store.fetch({
					query:{id:"*"},onComplete:function(items){
						for(var i=0;i < items.length;i++){
							var _item = items[i];
							gridContent.push({code:store.getValue(_item, "code"),name:store.getValue(_item, "name")});
						}
					},queryOptions:{deep:true}
				});
				content.systemCodeItems = JSON.stringify(gridContent);
				//-----------------------------------------------
				
				//增加对多次单击的次数----2014-9-4
				var buttonWidget = object.target;
				rosten.toggleAction(buttonWidget,true);
				
				rosten.readSync(rosten.webPath + "/systemExtend/systemCodeSave",content,function(data){
					if(data.result=="true" || data.result == true){
						rosten.alert("保存成功！").queryDlgClose= function(){
							page_quit();
						};
					}else{
						rosten.alert("保存失败!");
					}
					rosten.toggleAction(buttonWidget,false);
				},function(error){
					rosten.alert("系统错误，请通知管理员！");
					rosten.toggleAction(buttonWidget,false);
				},"rosten_form");
			};
			page_quit = function(){
				rosten.pagequit();
			};
			systemCode_addItem = function(){
				rosten.createRostenShowDialog(rosten.webPath + "/systemExtend/systemCodeItemShow", {
		            onLoadFunction : function() {}
		        });
			};
			systemCodeItem_Submit = function(){
				var chenkids = ["itemCode","itemName"];
				if(!rosten.checkData(chenkids)) return;

				function gotAll(items,request){
					if(items && items.length>0){
						store.setValue(items[0],"code",registry.byId("itemCode").get("value"));
						store.setValue(items[0],"name",registry.byId("itemName").get("value"));
					}else{
						var randId = Math.random();
						store.newItem({id:randId,systemCodeItemId:randId,rowIndex:items.length+1,code:registry.byId("itemCode").get("value"),name:registry.byId("itemName").get("value")});
					}
				}
				
				var store = systemCodeItemGrid.getStore();
				store.fetch({
					query:{id:registry.byId("itemId").get("value")},onComplete:gotAll,queryOptions:{deep:true}
				});
				rosten.hideRostenShowDialog();
			};
			systemCodeItem_formatTopic = function(value,rowIndex){
				return "<a href=\"javascript:systemCodeItem_onMessageOpen(" + rowIndex + ");\">" + value+ "</a>";
			};
			systemCodeItem_onMessageOpen = function(rowIndex){
				//打开systemCodeItem信息
		    	rosten.createRostenShowDialog(rosten.webPath + "/systemExtend/systemCodeItemShow", {
		            onLoadFunction : function() {
		            	var itemId = rosten.getGridItemValue(systemCodeItemGrid,rowIndex,"id");
		            	var code = rosten.getGridItemValue(systemCodeItemGrid,rowIndex,"code");
		            	var name = rosten.getGridItemValue(systemCodeItemGrid,rowIndex,"name");

		            	registry.byId("itemId").set("value",itemId);
		            	registry.byId("itemCode").set("value",code);
		            	registry.byId("itemName").set("value",name);
			        }
		        });
		    };
		    systemCodeItem_action = function(value,rowIndex){
		    	return "<a href=\"javascript:systemCodeItem_onDelete(" + rowIndex + ");\">" + "删除" + "</a>";
			};
			systemCodeItem_onDelete = function(rowIndex){
				//删除item信息
				var store = systemCodeItemGrid.getStore();
			    var item = rosten.getGridItem(systemCodeItemGrid,rowIndex);
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
		
		
    </script>
</head>
<body>
	<div class="rosten_action">
		<div data-dojo-type="rosten/widget/ActionBar" id="rosten_actionBar" data-dojo-props='actionBarSrc:"${createLink(controller:'systemExtendAction',action:'systemCodeForm',id:systemCode?.id,params:[userid:user?.id])}"'></div>
	</div>
	<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
	  	<div data-dojo-type="dijit/layout/ContentPane" title="代码配置" data-dojo-props='style:{height:"355px"}'>
        	<form class="rosten_form" id="rosten_form" onsubmit="return false;" style="padding:0px">
        		<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${systemCode?.id }"' />
        		<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        	  	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"基本信息",toggleable:false,moreText:"",marginBottom:"2px"'>
	                <table class="tableData" style="width:740px;margin:0px">
	                   <tr>
					    	<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>代码编号：</div></td>
					    	<td width="250">
						    	<input id="code" data-dojo-type="dijit/form/ValidationTextBox" 
				                 	data-dojo-props='name:"code",trim:true,required:true,missingMessage:"请填写代码编号！",invalidMessage:"请填写代码编号！",
										value:"${systemCode?.code}"
				                '/>
						    </td>
						    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>代码名称：</div></td>
						    <td width="250">
						    	<input id="name" data-dojo-type="dijit/form/ValidationTextBox" 
					                data-dojo-props='name:"name",trim:true,required:true,missingMessage:"请填写代码名称！",invalidMessage:"请填写代码名称！",
					      			value:"${systemCode?.name}"
					            '/>
				           </td>
						</tr>
	                </table>
                </div>
                <div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='"class":"rostenTitleGrid",title:"条目信息",toggleable:false,_moreClick:systemCode_addItem,moreText:"<span style=\"color:#108ac6\">增加条目</span>",marginBottom:"2px"'>
                	<div data-dojo-type="rosten/widget/RostenGrid" id="systemCodeItemGrid" data-dojo-id="systemCodeItemGrid"
						data-dojo-props='showPageControl:false,url:"${createLink(controller:'systemExtend',action:'systemCodeItemGrid',id:systemCode?.id)}"'></div>
                </div>
			</form>
		</div>
		
	</div>
</body>
</html>