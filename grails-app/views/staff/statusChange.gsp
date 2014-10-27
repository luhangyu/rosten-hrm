<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <link rel="stylesheet" href="${createLinkTo(dir:'js/dojox/widget/Wizard',file:'Wizard.css') }"></link>
    <title>离职/退休</title>
    <style type="text/css">
		body{
			overflow:auto;
		}
		.rosten .rostenGridView .pagecontrol{
			text-align:left;
		}
    </style>
	<script type="text/javascript">
		require(["dojo/parser",
		 		"dojo/_base/kernel",
		 		"dijit/registry",
		 		"dojo/_base/xhr",
		 		"dojo/dom",
		 		"dojo/date/stamp",
		 		"rosten/widget/DepartUserDialog",
		 		"dijit/form/ValidationTextBox",
		 		"dijit/form/SimpleTextarea",
		 		"dijit/form/Button",
		 		"dijit/form/DateTextBox",
		 		"dijit/form/FilteringSelect",
		 		"dijit/form/DropDownButton",
		 		"dijit/form/Form",
		 		"dojox/form/Uploader",
		 		"dojox/form/uploader/FileList",
		     	"rosten/widget/ActionBar",
		     	"rosten/app/Application",
		     	"rosten/app/SystemApplication",
		     	"rosten/app/StaffApplication",
		     	"rosten/kernel/behavior"],
			function(parser,kernel,registry,xhr,dom,datestamp,DepartUserDialog){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}",dojogridcss : true});
					rosten.cssinit();
				});

			searchStaff = function(){
				var username = registry.byId("username");
		    	if(!username.isValid()){
		    		rosten.alert("员工名称不正确！").queryDlgClose = function(){
		    			username.focus();
					};
					return;
		    	}
		    	var content ={
		    		companyId:"${company?.id}",
		    		serchInput:username.attr("value")

				};
		    	rosten.readSync(rosten.webPath + "/staff/serachPerson", content, function(data) {
			    	
				    var jsonLength = 0
			    	for(var key in data){
			    		jsonLength += 1;
				    	var widget = registry.byId(key);
				    	if(widget){
				    		widget.set("value",data[key]);
					    }
		    		}

				    if(jsonLength==0){
						rosten.alert("未找到当前用户信息！");
						registry.byId("username").set("value","");
						return;
				    }
		    		
				});
			};	
			statusChange_add = function(object){
				var chenkids = ["personInforName","changeType","changeDate"];
				if(!rosten.checkData(chenkids)) return;

				var changeReasonDom = registry.byId("changeReason");
				var changeReason = changeReasonDom.get("value");
				if(changeReason==""){
					rosten.alert("申请理由不能为空！").queryDlgClose = function(){
						changeReasonDom.focus();
					};
					return false;
				}
				
				var content = {};

				//流程相关信息
				<g:if test='${flowCode}'>
					content.flowCode = "${flowCode}";
					content.relationFlow = "${relationFlow}";
				</g:if>

				//添加新增时添加附件功能
				<g:if test="${!statusChange?.id}">
					var filenode = dom.byId("fileUpload_show");
					var fileIds = [];

			       	var node=filenode.firstChild;
			       	while(node!=null){
			            node=node.nextSibling;
			            if(node!=null){
			            	fileIds.push(node.getAttribute("id"));
				        }
			        }
					content.attachmentIds = fileIds.join(",");
				</g:if>
				
				//增加对多次单击的次数----2014-9-4
				var buttonWidget = object.target;
				rosten.toggleAction(buttonWidget,true);
				
				rosten.readSync(rosten.webPath + "/staff/staffStatusChangeSave",content,function(data){
					if(data.result=="true" || data.result == true){
						rosten.alert("保存成功！").queryDlgClose= function(){
							page_quit();
						};
					}else if(data.result=="noConfig"){
						rosten.alert("系统不存在配置文档，请通知管理员！");
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
			
			addPersonInforDone = function(){
				var id = rosten.getGridItemValue1(chooseListGrid,"id");
				var chinaName = rosten.getGridItemValue1(chooseListGrid,"chinaName");
				var departName = rosten.getGridItemValue1(chooseListGrid,"departName");

				registry.byId("personInforId").set("value",id);
				registry.byId("personInforName").set("value",chinaName);
				registry.byId("personInforDepartName").set("value",departName);
				
				registry.byId("chooseDialog").hide();
			};
			
		});
		
		
    </script>
</head>
<body>
<div class="rosten_action">
	<div data-dojo-type="rosten/widget/ActionBar" 
		data-dojo-props='actionBarSrc:"${createLink(controller:'staffAction',action:'statusChangeForm')}"'>
	</div>
</div>
<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
  	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props='style:{height:"520px"}'>
       	<form class="rosten_form" id="rosten_form" onsubmit="return false;" style="padding:0px">
       		<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${statusChange?.id }"' />
       		<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
       		<input id="personInforId" data-dojo-type="dijit/form/ValidationTextBox"  data-dojo-props='name:"personInforId",style:{display:"none"},value:"${personInfor?.id }"' />
       		
       	  	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"员工信息",toggleable:false,moreText:"",height:"220px",marginBottom:"2px"'>
       	  		<table border="0" align="left" style="margin:0 auto;width:740px">
					<tr>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>员工名称：</div></td>
					    <td width="260">
					    	<input id="personInforName" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='trim:true,required:true,readOnly:true
			                '/>
			                <button data-dojo-type='dijit/form/Button' 
								data-dojo-props="label:'选择',iconClass:'docAddIcon'">
								<script type="dojo/method" data-dojo-event="onClick">
										addPersonInfor();
									</script>
							</button>
					    </td>
					    <td width="120"><div align="right">所属部门：</div></td>
					    <td width="250">
					    	<input id="personInforDepartName" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='readOnly:true'/>
			           </td>
					</tr>
					<tr>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>申请类型：</div></td>
					    <td width="260">
					    	<input id="changeType" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"changeType",readOnly:true,trim:true,required:true,value:"${statusChange?.changeType }"
			                '/>
					    </td>
					    <td width="120"><div align="right">申请时间：</div></td>
					    <td width="250">
					    	<input id="changeDate" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"changeDate",${fieldAcl.isReadOnly("changeDate")},
			                	trim:true,required:true,missingMessage:"请正确填写申请时间！",invalidMessage:"请正确填写申请时间！",
			                	value:"${statusChange?.getFormattedChangeDate()}"
			               '/>
			           </td>
					</tr>
            		<tr>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>申请理由：</div></td>
				  		<td colspan=3>
					  		<textarea id="changeReason" data-dojo-type="dijit/form/SimpleTextarea" 
								data-dojo-props='name:"changeReason",
				                    style:{width:"556px",height:"150px"},
				                    trim:true,value:"${statusChange?.changeReason}"
				            '>
							</textarea>
				  			
					    </td>
					</tr>
				</table>
            </div>
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"附件信息",toggleable:false,moreText:"",
				href:"${createLink(controller:'share',action:'getFileUploadNew',id:departChange?.id,params:[uploadPath:'staff',isShowFile:isShowFile])}"'>
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
						data-dojo-props='url:"${createLink(controller:'staff',action:'staffGrid',params:[companyId:company?.id])}"'></div>
				</div>
			</div>
			<script type="dojo/method" event="cancelFunction">
				dijit.byId("chooseDialog").hide();
			</script>
		</div>
	</div>
</body>
</html>