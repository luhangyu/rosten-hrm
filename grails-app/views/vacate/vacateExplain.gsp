<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <link rel="stylesheet" href="${createLinkTo(dir:'js/dojox/widget/Wizard',file:'Wizard.css') }"></link>
    <title>出勤解释单</title>
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
		 		"dojo/_base/kernel",
		 		"dijit/registry",
		 		"dojo/dom",
		 		"dojo/_base/lang",
		 		"dijit/layout/TabContainer",
		 		"dijit/layout/ContentPane",
		 		"dijit/form/ValidationTextBox",
		 		"dijit/form/RadioButton",
		 		"dijit/form/DateTextBox",
		 		"dijit/form/SimpleTextarea",
		 		"dijit/form/NumberTextBox",
		 		"dijit/form/Button",
		     	"rosten/widget/ActionBar",
		     	"rosten/widget/TitlePane",
		     	"rosten/app/Application",
		     	"rosten/kernel/behavior"],
			function(parser,kernel,registry,dom,lang){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}",dojogridcss : true});
					rosten.cssinit();
				});
				vacateExplain_save = function(object){
					var chenkids = ["applyName","applyDepart","explainDate","dateStage"];
					if(rosten.checkData(chenkids)){
						var content = {};
						
						//增加对多次单击的次数----2014-9-4
						var buttonWidget = object.target;
						rosten.toggleAction(buttonWidget,true);

						rosten.readSync(rosten.webPath + "/vacate/vacateExplainSave",content,function(data){
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
					}
				};
				
				page_quit = function(){
					rosten.pagequit();
				};
				addPersonInfor = function(){
			        registry.byId("chooseDialog").show();
			    };
			    addPersonInforNext = function(){
			        var content = {};
			        
			        var username = registry.byId("s_username");
			        if(username.get("value")!=""){
			            content.username = username.get("value");
			        }
			        
			        var chinaName = registry.byId("s_chinaName");
			        if(chinaName.get("value")!=""){
			            content.chinaName = chinaName.get("value");
			        }
			        
			        var departName = registry.byId("s_departName");
			        if(departName.get("value")!=""){
			            content.departName = departName.get("value");
			        }
			        
			        var idCard = registry.byId("s_idCard");
			        if(idCard.get("value")!=""){
			            content.idCard = idCard.get("value");
			        }
			        var sex = registry.byId("s_sex");
			        if(sex.get("value")!=""){
			            content.sex = sex.get("value");
			        }
			        var politicsStatus = registry.byId("s_politicsStatus");
			        if(politicsStatus.get("value")!=""){
			            content.politicsStatus = politicsStatus.get("value");
			        }
			        var nativeAddress = registry.byId("s_nativeAddress");
			        if(nativeAddress.get("value")!=""){
			            content.nativeAddress = nativeAddress.get("value");
			        }
			        var city = registry.byId("s_city");
			        if(city.get("value")!=""){
			            content.city = city.get("value");
			        }
			        var status = registry.byId("s_status");
			        if(status.get("value")!=""){
			            content.status = status.get("value");
			        }
			        
			        chooseListGrid.refresh(null,content);
			    };
			    addPersonInforDone = function(){
			        var chinaName = rosten.getGridItemValue1(chooseListGrid,"chinaName");
			        var departName = rosten.getGridItemValue1(chooseListGrid,"departName");

			        registry.byId("applyName").set("value",chinaName);
			        registry.byId("applyDepart").set("value",departName);
			        
			        registry.byId("chooseDialog").hide();
			    };
			    personInfor_formatTopic_normal = function(value,rowIndex){
					return "<a href=\"javascript:personInfor_normal_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
				};
				personInfor_normal_onMessageOpen = function(rowIndex){
					
				};
				
		});
    </script>
</head>
<body>
<div class="rosten_action">
	<div data-dojo-type="rosten/widget/ActionBar" data-dojo-id="rosten_actionBar" 
		data-dojo-props='actionBarSrc:"${createLink(controller:'vacateAction',action:'vacateExplainForm',id:vacateExplain?.id,params:[userid:user?.id])}"'>
	</div>
</div>

<div data-dojo-id="rosten_tabContainer" data-dojo-type="dijit/layout/TabContainer" data-dojo-props='doLayout:false,persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props='doLayout:false'>
		<form id="rosten_form" name="rosten_form" url='[controller:"assetConfig",action:"assetCategorySave"]' onsubmit="return false;" class="rosten_form" style="padding:0px">
			<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${vacateExplain?.id }"' />
        	<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        	
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"请假申请",toggleable:false,moreText:"",height:"320px",marginBottom:"2px"'>
				<table border="0" width="740" align="left">
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>申请人：</div></td>
					    <td >
					    	<input id="applyName" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"applyName",trim:true,readOnly:true,
									value:"${vacateExplain?.applyName}"
			                '/>
			                <button data-dojo-type='dijit/form/Button' 
								data-dojo-props="label:'选择',iconClass:'docAddIcon'">
								<script type="dojo/method" data-dojo-event="onClick">
										addPersonInfor();
									</script>
							</button>
					    </td>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>申请部门：</div></td>
					    <td >
					    	<input id="applyDepart" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"applyDepart",trim:true,readOnly:true,
									value:"${vacateExplain?.applyDepart}"
			                '/>
					    </td>
					     
					</tr>

					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>未出勤时间：</div></td>
					    <td>
					    	<div id="explainDate" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"explainDate",${fieldAcl.isReadOnly("explainDate")},
			                	trim:true,required:true,missingMessage:"请正确填写未出勤时间！",invalidMessage:"请正确填写未出勤时间！",
			                	value:"${vacateExplain?.getFormatteStartDate()}"
			               '>
			               </div>
			            </td>
						<td><div align="right"><span style="color:red">*&nbsp;</span>请假区间：</div></td>
					    <td>
					    	<select id="dateStage" data-dojo-type="dijit/form/FilteringSelect"
                           		data-dojo-props='name:"dateStage",required:true,
                           			autoComplete:false,${fieldAcl.isReadOnly("dateStage")},
            						value:"${vacateExplain?.dateStage?vacateExplain.dateStage:"上午"}"
                            '>
	                            <option value="上午">上午</option>
								<option value="下午">下午</option>
                           	</select>
			            </td>
		            </tr>
					<tr>
					    <td><div align="right">未 出勤理由：</div></td>
					    <td  colspan=3>
					    	<textarea id="remark" data-dojo-type="dijit/form/SimpleTextarea" 
    							data-dojo-props='name:"remark","class":"input",
                               		style:{width:"586px"},rows:"10",${fieldAcl.isReadOnly("remark")},
                               		trim:true,value:"${vacateExplain?.remark}"
                           '>
    						</textarea>
					    </td>
					</tr>
					
				</table>
				
			</div>
			
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