<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>岗位级别信息</title>
    <link rel="stylesheet" href="${createLinkTo(dir:'js/dojox/widget/Wizard',file:'Wizard.css') }"></link>
    <style type="text/css">
    	.rosten .dsj_form table tr{
    		height:30px;
    	}
    	body{
			overflow:auto;
		}
    </style>
	<script type="text/javascript">
	require(["dojo/parser",
	         "dojo/dom",
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
		     	"rosten/app/StaffApplication",
		     	"rosten/kernel/behavior"],
			function(parser,dom,kernel,registry){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}",dojogridcss : true});
					rosten.cssinit();
				});
				billConfig_save = function(){

					rosten.readSync(rosten.webPath + "/salary/billConfigSave",{},function(data){
						if(data.result=="true" || data.result == true){
							rosten.alert("保存成功！").queryDlgClose= function(){
								page_quit();
							};
						}else{
							rosten.alert("保存失败!");
						}
					},null,"rosten_form");
				};
				page_quit = function(){
					rosten.pagequit();
				};
				setValue = function(type){
					if(type=="gearId"){
						var w = registry.byId(type);
						var store = w.store;
						registry.byId("gearHidId").set("value",store.getValue(w.item,"id"));
					}else{
						var w = registry.byId(type);
						var store = w.store;
						registry.byId("quartersHidId").set("value",store.getValue(w.item,"id"));
					}
					
				};


	addPersonInforDone = function(){
		var id = rosten.getGridItemValue1(chooseListGrid,"id");
		var chinaName = rosten.getGridItemValue1(chooseListGrid,"chinaName");
		registry.byId("personInforId").set("value",id);
		registry.byId("personInforName").set("value",chinaName);
		registry.byId("chooseDialog").hide();
	};
});

	
    </script>
</head>
<body>
<div class="rosten_action">
	<div data-dojo-type="rosten/widget/ActionBar" data-dojo-id="rosten_actionBar" 
		data-dojo-props='actionBarSrc:"${createLink(controller:'salaryAction',action:'billConfigForm',id:gear?.id,params:[userid:user?.id])}"'>
	</div>
</div>

<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props=''>
		<form id="rosten_form" name="rosten_form" url='[controller:"assetConfig",action:"assetCategorySave"]' onsubmit="return false;" class="rosten_form" style="padding:0px">
			<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${billConfig?.id }"' />
        	<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        	
				<table border="0" width="740" align="left">
				
				<tr>
				<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>员工姓名：</div></td>
				<td colspan="4">
						    	<input  data-dojo-type="dijit/form/ValidationTextBox" id="personInforId"  data-dojo-props='name:"personInforId",style:{display:"none"},value:"${billConfig?.personInfor?.id }"' />
						    	<input id="personInforName" data-dojo-type="dijit/form/ValidationTextBox" 
				                 	data-dojo-props='trim:true,required:true,readOnly:true,value:"${billConfig?.getPersonInforName() }"
				                '/>
				                <button data-dojo-type='dijit/form/Button' 
									data-dojo-props="label:'选择',iconClass:'docAddIcon'">
									<script type="dojo/method" data-dojo-event="onClick">
										addPersonInfor();
									</script>
								</button>
						    </td>
				
				</tr>
				
				<tr>
				
				<td><div align="right">级别：</div></td>
			 	<td>
			 	
			 	<input  data-dojo-type="dijit/form/ValidationTextBox" id="quartersHidId"  
			 			data-dojo-props='name:"quartersId",style:{display:"none"},value:"${billConfig?.quarters?.id }"' />
			 	
			    	<select id="quartersId" data-dojo-type="dijit/form/ComboBox" 
	                 	data-dojo-props='trim:true,onChange:function(item){setValue("quartersId")},
							value:"${billConfig?.quarters?.quaName }"
	                '>
	                	<g:each in="${quaList}" var="item">
		                	<option value="${item.id }">${item.quaName }</option>
		                </g:each>
	                </select>
			    </td>
					
				<td><div align="right">档位：</div></td>
				
			 	<td>
			 		<input  data-dojo-type="dijit/form/ValidationTextBox" id="gearHidId"  
			 			data-dojo-props='name:"gearId",style:{display:"none"},value:"${billConfig?.gear?.id }"' />
			 			
			    	<select id="gearId" data-dojo-type="dijit/form/ComboBox" 
	                 	data-dojo-props='trim:true,onChange:function(item){setValue("gearId")},
							value:"${billConfig?.gear?.gearName }"
	                '>
	                	<g:each in="${gearList}" var="item">
		                	<option value="${item.id }">${item.gearName }</option>
		                </g:each>
	                </select>
			    </td>	
				
				</tr>
				
				<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>公积金基数：</div></td>
					    <td >
					    	<input id="gjj" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"gjj",trim:true,required:true,
									value:"${billConfig?.gjj}"
			                '/>
					    </td>
					     <td><div align="right"><span style="color:red">*&nbsp;</span>公积金比例：</div></td>
					    <td >
					    	<input id="gjjBl" data-dojo-type="dijit/form/NumberTextBox" 
			                 	data-dojo-props='name:"gjjBl",trim:true,required:true,
									value:"${billConfig?.gjjBl}"
			                '/>
					    </td>
					</tr>
					
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>失业保险基数：</div></td>
					    <td >
					    	<input id="sybx" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"sybx",trim:true,required:true,
									value:"${billConfig?.sybx}"
			                '/>
					    </td>
					     <td><div align="right"><span style="color:red">*&nbsp;</span>失业保险比例：</div></td>
					    <td >
					    	<input id="sybxBl" data-dojo-type="dijit/form/NumberTextBox" 
			                 	data-dojo-props='name:"sybxBl",trim:true,required:true,
									value:"${billConfig?.sybxBl}"
			                '/>
					    </td>
					</tr>
					
					
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>医疗保险基数：</div></td>
					    <td >
					    	<input id="ylbx" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"ylbx",trim:true,required:true,
									value:"${billConfig?.ylbx}"
			                '/>
					    </td>
					     <td><div align="right"><span style="color:red">*&nbsp;</span>医疗保险比例：</div></td>
					    <td >
					    	<input id="ylbxBl" data-dojo-type="dijit/form/NumberTextBox" 
			                 	data-dojo-props='name:"ylbxBl",trim:true,required:true,
									value:"${billConfig?.ylbxBl}"
			                '/>
					    </td>
					</tr>
					
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>生育保险基数：</div></td>
					    <td >
					    	<input id="syubx" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"syubx",trim:true,required:true,
									value:"${billConfig?.syubx}"
			                '/>
					    </td>
					     <td><div align="right"><span style="color:red">*&nbsp;</span>生育保险保险比例：</div></td>
					    <td >
					    	<input id="syubxBl" data-dojo-type="dijit/form/NumberTextBox" 
			                 	data-dojo-props='name:"syubxBl",trim:true,required:true,
									value:"${billConfig?.syubxBl}"
			                '/>
					    </td>
					</tr>
					
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>养老保险基数：</div></td>
					    <td >
					    	<input id="ylaobx" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"ylaobx",trim:true,required:true,
									value:"${billConfig?.ylaobx}"
			                '/>
					    </td>
					     <td><div align="right"><span style="color:red">*&nbsp;</span>养老保险比例：</div></td>
					    <td >
					    	<input id="ylaobxBl" data-dojo-type="dijit/form/NumberTextBox" 
			                 	data-dojo-props='name:"ylaobxBl",trim:true,required:true,
									value:"${billConfig?.ylaobxBl}"
			                '/>
					    </td>
					</tr>
					
				</table>
			
		</form>
	</div>
	
</div>

	<div id="chooseDialog" data-dojo-type="dijit/Dialog" class="displayLater" data-dojo-props="title:'人员选择',style:'width:800px;height:450px'">
		<div id="chooseWizard" data-dojo-type="dojox/widget/Wizard" 
			data-dojo-props='previousButtonLabel:"上一步",nextButtonLabel:"下一步"' style="width:780px; height:410px;padding:0px">
			
			<div data-dojo-type="dojox/widget/WizardPane" 
				data-dojo-props='passFunction:addPersonInforNext,style:{padding:"0px"},
					href:"${createLink(controller:'staff',action:'getChooseListSearch')}"
			'></div>
			<div data-dojo-type="dojox/widget/WizardPane" data-dojo-props='canGoBack:"true",doneFunction:addPersonInforDone,style:{padding:"0px"}' >
				<div id="chooseList" data-dojo-id="chooseList" data-dojo-type="dijit/layout/ContentPane" 
					data-dojo-props='style:{overflow:"auto",padding:"1px"}'>
					
					<div data-dojo-type="rosten/widget/RostenGrid" id="chooseListGrid" data-dojo-id="chooseListGrid"
						data-dojo-props='imgSrc:"${resource(dir:'images/rosten/share',file:'wait.gif')}",url:"${createLink(controller:'staff',action:'staffGrid',params:[companyId:company?.id])}"'></div>
				</div>
			</div>
			<script type="dojo/method" event="cancelFunction">
				dijit.byId("chooseDialog").hide();
			</script>
		</div>
	</div>
</body>