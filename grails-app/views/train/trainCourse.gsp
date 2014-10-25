<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>培训班管理</title>
    <style type="text/css">
    	.rosten .dsj_form table tr{
    		height:30px;
    		
    	}
    	body{
			overflow:auto;
		}
		.rosten .rostenTitleGrid .dijitTitlePaneContentInner{
			padding:2px 1px 1px 1px;
			height:500px;
		}
    </style>
	<script type="text/javascript">
	require(["dojo/parser",
		 		"dojo/_base/kernel",
		 		"dijit/registry",
		 		"dijit/layout/TabContainer",
		 		"dijit/layout/ContentPane",
		 		"dijit/form/ValidationTextBox",
		 		"dijit/form/DateTextBox",
		 		"dijit/form/SimpleTextarea",
		 		"dijit/form/Button",
		     	"rosten/widget/ActionBar",
		     	"rosten/widget/TitlePane",
		     	"rosten/app/Application",
		     	"rosten/kernel/behavior"],
			function(parser,kernel,registry){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}",dojogridcss : true});
					rosten.cssinit();
				});
				trainCourse_save = function(){
					var courseName = registry.byId("courseName");
					if(!courseName.isValid()){
						rosten.alert("培训班名称不正确！").queryDlgClose = function(){
							courseName.focus();
						};
						return;
					}
					var organizeName = registry.byId("organizeName");
					if(!organizeName.isValid()){
						rosten.alert("组织者名称不正确！").queryDlgClose = function(){
							organizeName.focus();
						};
						return;
					}
					var trainDate = registry.byId("trainDate");
					if(!trainDate.isValid()){
						rosten.alert("培训时间不正确！").queryDlgClose = function(){
							trainDate.focus();
						};
						return;
					}
					var trainAddress = registry.byId("trainAddress");
					if(!trainAddress.isValid()){
						rosten.alert("培训地点不正确！").queryDlgClose = function(){
							trainAddress.focus();
						};
						return;
					}
					var trainMoney = registry.byId("trainMoney");
					if(!trainMoney.isValid()){
						rosten.alert("培训费用不正确！").queryDlgClose = function(){
							trainMoney.focus();
						};
						return;
					}
					
					rosten.readSync(rosten.webPath + "/train/trainCourseSave",{},function(data){
						if(data.result=="true" || data.result == true){
							rosten.alert("保存成功！").queryDlgClose= function(){
								page_quit();
							};
						}else{
							rosten.alert("保存失败!");
						}
					},null,"rosten_form");
				};
				
				staff_addItem = function(){
					rosten.createRostenShowDialog(rosten.webPath + "/train/staffItemShow", {
			            onLoadFunction : function() {

				            }
			        });
				};
				staffItem_Submit = function(){
					var chenkids = ["itemName","itemDept","itemMoney","itemResult"];
					if(!rosten.checkData(chenkids)) return;

					function gotAll(items,request){
						//单选radio元素取值
						if (registry.byId("itemCert2").get("value")){
							var itemCertValue=registry.byId("itemCert2").get("value");
					 	}else{
							var itemCertValue=registry.byId("itemCert1").get("value");
						}

						if(items && items.length>0){
							
							store.setValue(items[0],"getUserName",registry.byId("itemName").get("value"));
							store.setValue(items[0],"getUserDepartName",registry.byId("itemDept").get("value"));
							store.setValue(items[0],"userMoney",registry.byId("itemMoney").get("value"));
							store.setValue(items[0],"trainResult",registry.byId("itemResult").get("value"));
							store.setValue(items[0],"trainCert",itemCertValue);
							
						}else{
							var randId = Math.random();
							var content ={
									id:randId,
									staffItemId:randId,
									rowIndex:items.length+1,
									getUserName:registry.byId("itemName").get("value"),
									getUserDepartName:registry.byId("itemDept").get("value"),
									userMoney:registry.byId("itemMoney").get("value"),
									trainResult:registry.byId("itemResult").get("value"),
									trainCert:itemCertValue,
							};
							store.newItem(content);
						}
					}
					
					var store = staffListGrid.getStore();
					store.fetch({
						query:{id:registry.byId("itemId").get("value")},onComplete:gotAll,queryOptions:{deep:true}
					});
					rosten.hideRostenShowDialog();
				};
				trainMessage_formatTopic = function(value,rowIndex){
					return "<a href=\"javascript:trainMessage_onMessageOpen(" + rowIndex + ");\">" + value+ "</a>";
				};
				trainMessage_onMessageOpen = function(rowIndex){
					//打开systemCodeItem信息
			    	rosten.createRostenShowDialog(rosten.webPath + "/train/staffItemShow", {
			            onLoadFunction : function() {
				            
			            	var itemId = rosten.getGridItemValue(staffListGrid,rowIndex,"id");
			            	var itemName = rosten.getGridItemValue(staffListGrid,rowIndex,"getUserName");
			            	var itemDept = rosten.getGridItemValue(staffListGrid,rowIndex,"getUserDepartName");
			            	var itemResult = rosten.getGridItemValue(staffListGrid,rowIndex,"trainResult");
			            	var itemCert = rosten.getGridItemValue(staffListGrid,rowIndex,"trainCert");
			            	var itemMoney = rosten.getGridItemValue(staffListGrid,rowIndex,"userMoney");
							//var itemCert=true;

			            	
			            	registry.byId("itemId").set("value",itemId);
			            	registry.byId("itemName").set("value",itemName);
			            	registry.byId("itemDept").set("value",itemDept);
			            	registry.byId("itemResult").set("value",itemResult);
			            	
			            	if(itemCert=="是"){
			            		registry.byId("itemCert1").set("value",itemCert);
					         }else{
					        	registry.byId("itemCert2").set("value","否");
						    }
			            	
			            	
			            	registry.byId("itemMoney").set("value",itemMoney);

				        }
			        });
			    };

			    staffItem_action = function(value,rowIndex){
			    	return "<a href=\"javascript:staffItem_onDelete(" + rowIndex + ");\">" + "删除" + "</a>";
				};
				staffItem_onDelete = function(rowIndex){
					//删除item信息
					var store = staffListGrid.getStore();
				    var item = rosten.getGridItem(staffListGrid,rowIndex);
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
			    
				page_quit = function(){
					rosten.pagequit();
				};
		});
    </script>
</head>
<body>
<div class="rosten_action">
	<div data-dojo-type="rosten/widget/ActionBar" data-dojo-id="rosten_actionBar" 
		data-dojo-props='actionBarSrc:"${createLink(controller:'trainAction',action:'trainCourseForm',id:trainCourse?.id,params:[userid:user?.id])}"'>
	</div>
</div>

<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
	<div data-dojo-type="dijit/layout/ContentPane" title="培训班信息" data-dojo-props=''>
		<form id="rosten_form" name="rosten_form" url='[controller:"assetConfig",action:"assetCategorySave"]' onsubmit="return false;" class="rosten_form" style="padding:0px">
			<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${trainCourse?.id }"' />
        	<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        	
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"基本信息",toggleable:false,moreText:"",height:"210px",marginBottom:"2px"'>
				<table border="0" width="740" align="left">
					<tr>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>培训班名称：</div></td>
					    <td colspan=3>
					    	<input id="courseName" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"courseName",${fieldAcl.isReadOnly("courseName")},
			                 		trim:true,required:true,
			                 		style:{width:"550px"},
			                 		missingMessage:"请正确填写培训班名称！",invalidMessage:"请正确填写培训班名称！",
									value:"${trainCourse?.courseName}"
			                '/>
					    </td>
					</tr>
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>培训时间：</div></td>
					    <td>
					    	<input id="trainDate" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"trainDate",${fieldAcl.isReadOnly("trainDate")},
			                	trim:true,required:true,missingMessage:"请正确填写培训时间！",invalidMessage:"请正确填写培训时间！",
			                	value:"${trainCourse?.getFormatteTrainDate()}"
			               '/>
			            </td>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>培训地点：</div></td>
					    <td>
					    	<input id="trainAddress" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"trainAddress",${fieldAcl.isReadOnly("trainAddress")},
			                 		trim:true,required:true,
			                 		missingMessage:"请正确填写培训地点！",invalidMessage:"请正确填写培训地点！",
									value:"${trainCourse?.trainAddress}"
			                '/>
			            </td>    
					</tr>
					<tr>
						<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>培训组织单位：</div></td>
					    <td width="250">
					    	<input id="organizeName" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"organizeName",${fieldAcl.isReadOnly("organizeName")},
			                 		trim:true,required:true,
			                 		missingMessage:"请正确填写培训组织单位名称！",invalidMessage:"请正确填写培训组织单位名称！",
									value:"${trainCourse?.organizeName}"
			                '/>
			           	</td>
						<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>培训总费用：</div></td>
					    <td width="250">
					    	<input id="trainMoney" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"trainMoney",${fieldAcl.isReadOnly("trainMoney")},
			                 		trim:true,required:true,
			                 		missingMessage:"请正确填写培训费用！",invalidMessage:"请正确填写培训费用！",
									value:"${trainCourse?.trainMoney}"
			                '/>
			            </td>    
					</tr>
					<tr>
					    <td><div align="right">所属培训计划：</div></td>
					    <td>
					    	<input id="trainPlan" data-dojo-type="dijit/form/ValidationTextBox" 
			                	data-dojo-props='name:"trainPlan",${fieldAcl.isReadOnly("trainPlan")},
			                	trim:true,
			                	
			                	value:"${trainCourse?.trainPlan}"
			               '/>
			            </td>
			            <td><div align="right">培训证书发放情况：</div></td>
					    <td>

                                <input id="trainCert1" data-dojo-type="dijit/form/RadioButton"
					           		data-dojo-props='name:"trainCert",type:"radio",
					           			<g:if test="${trainCourseEntity?.trainCert=="是" }">checked:true,</g:if>
										value:"是"
				              	'/>
								<label for="trainCert1">是</label>
							
				              	<input id="trainCert2" data-dojo-type="dijit/form/RadioButton"
				           			data-dojo-props='name:"itemCert",type:"radio",
				           			<g:if test="${trainCourseEntity?.trainCert=="否" }">checked:true,</g:if>
									value:"否"
				              	'/>
								<label for="trainCert2">否</label>					    	
					    
					   
					    </td>					       
					</tr>
					
					 
					
					<tr>
					    <td><div align="right">培训内容：</div></td>
					    <td  colspan=3>
					    	<textarea id="description" data-dojo-type="dijit/form/SimpleTextarea" 
    							data-dojo-props='name:"description","class":"input",
                               		style:{width:"550px"},rows:"3",
                               		trim:true,value:""
                           '>
    						</textarea>
					    </td>
					</tr>
					
				</table>
			</div>
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='"class":"rostenTitleGrid",title:"学员名单",toggleable:false,_moreClick:staff_addItem,moreText:"<span style=\"color:#108ac6\">增加学员</span>",marginBottom:"2px"'>
            	<div data-dojo-type="rosten/widget/RostenGrid" id="staffListGrid" data-dojo-id="staffListGrid"
					data-dojo-props='showPageControl:false,url:"${createLink(controller:'train',action:'staffListGrid',id:trainCourse?.id)}"'></div>
            </div>
		</form>
	</div>
	
	
</div>
</body>