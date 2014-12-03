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
							if(window.location.href.indexOf(data.id)==-1){
								window.location.replace(window.location.href + "&id=" + data.id);
							}else{
								window.location.reload();
							}
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
			statusChange_addComment = function(){
				//flowCode为是否需要走流程，如需要，则flowCode为业务流程代码
				var commentDialog = rosten.addCommentDialog({type:"statusChange"});
				commentDialog.callback = function(_data){
					var content = {dataStr:_data.content,userId:"${user?.id}",status:"${statusChange?.status}",flowCode:"${flowCode}"};
					rosten.readSync(rosten.webPath + "/share/addComment/${statusChange?.id}",content,function(data){
						if(data.result=="true" || data.result == true){
							rosten.alert("成功！").queryDlgClose= function(){
								var selectWidget = rosten_tabContainer.selectedChildWidget;
								if(selectWidget.get("id")=="flowComment"){
									rosten_tabContainer.selectedChildWidget.refresh();
								}
							};
						}else{
							rosten.alert("失败!");
						}	
					});
				};
			};
			statusChange_submit = function(object,conditionObj){
				/*
				 * 从后台获取下一处理人;conditionObj为流程中排他分支使用
				 */
				//增加对多次单击的控制
				var buttonWidget = object.target;
				rosten.toggleAction(buttonWidget,true);
				
				var content = {};

				//增加对排他分支的控制
				if(conditionObj){
					lang.mixin(content,conditionObj);
				}
				rosten.readSync("${createLink(controller:'share',action:'getSelectFlowUser',params:[userId:user?.id,taskId:statusChange?.taskId,drafterUsername:statusChange?.drafter?.username])}",content,function(data){
					if(data.dealFlow==false){
						//流程无下一节点
						statusChange_deal("submit",null,buttonWidget,conditionObj);
						return;
					}
					var url = "${createLink(controller:'system',action:'userTreeDataStore',params:[companyId:company?.id])}";
					if(data.dealType=="user"){
						//人员处理
						if(data.showDialog==false){
							//单一处理人
							var _data = [];
							_data.push(data.userId + ":" + data.userDepart);
							statusChange_deal("submit",_data,buttonWidget,conditionObj);
						}else{
							//多人，多部门处理
							url += "&type=user&user=" + data.user;
							statusChange_select(url,buttonWidget,conditionObj);
						}
					}else{
						//群组处理
						url += "&type=group&groupIds=" + data.groupIds;
						if(data.limitDepart){
							url += "&limitDepart="+data.limitDepart;
						}
						statusChange_select(encodeURI(url),buttonWidget,conditionObj);
					}

				},function(error){
					rosten.alert("系统错误，请通知管理员！");
					rosten.toggleAction(buttonWidget,false);
				});
			};
			statusChange_select = function(url,buttonWidget,conditionObj){
				var rostenShowDialog = rosten.selectFlowUser(url,"single");
	            rostenShowDialog.callback = function(data) {
	            	if(data.length==0){
		            	rosten.alert("请正确选择人员！");
	            		rosten.toggleAction(buttonWidget,false);
		            }else{
		            	var _data = [];
		            	for (var k = 0; k < data.length; k++) {
		            		var item = data[k];
		            		_data.push(item.value + ":" + item.departId);
		            	};
		            	statusChange_deal("submit",_data,buttonWidget,conditionObj);
		            }
	            };
				rostenShowDialog.afterLoad = function(){
					var _data = rostenShowDialog.getData();
		            if(_data && _data.length==1){
			            //直接调用
		            	rostenShowDialog.doAction();
			        }else{
						//显示对话框
						rostenShowDialog.open();
				    }
				};
				rostenShowDialog.queryDlgClose = function(){
					rosten.toggleAction(buttonWidget,false);
				};	
			};
			statusChange_deal = function(type,readArray,buttonWidget,conditionObj){
				var content = {};
				content.id = "${statusChange?.id}";
				content.deal = type;
				if(readArray){
					content.dealUser = readArray.join(",");
				}
				if(conditionObj){
					lang.mixin(content,conditionObj);
				}
				rosten.readSync(rosten.webPath + "/staff/staffStatusChangeFlowDeal",content,function(data){
					if(data.result=="true" || data.result == true){
						var ostr = "成功！";
						if(data.nextUserName && data.nextUserName!=""){
							ostr += "下一处理人<" + data.nextUserName +">";
						}
						rosten.alert(ostr).queryDlgClose= function(){
							//刷新待办事项内容
							window.opener.showStartGtask("${user?.id}","${company?.id }");
							
							if(data.refresh=="true" || data.refresh==true){
								window.location.reload();
							}else{
								rosten.pagequit();
							}
						}
					}else{
						rosten.alert("失败!");
						rosten.toggleAction(buttonWidget,false);
					}	
				},function(error){
					rosten.alert("系统错误，请通知管理员！");
					rosten.toggleAction(buttonWidget,false);
				});
			};
			statusChange_back = function(object,conditionObj){
				//增加对多次单击的控制
				var buttonWidget = object.target;
				rosten.toggleAction(buttonWidget,true);
				
				var content = {};
				rosten.readSync("${createLink(controller:'staff',action:'staffStatusChangeFlowBack',params:[id:statusChange?.id])}",content,function(data){
					if(data.result=="true" || data.result == true){
						rosten.alert("成功！下一处理人<" + data.nextUserName +">").queryDlgClose= function(){
							//刷新待办事项内容
							window.opener.showStartGtask("${user?.id}","${company?.id }");
							
							if(data.refresh=="true" || data.refresh==true){
								window.location.reload();
							}else{
								rosten.pagequit();
							}
						}
					}else{
						rosten.alert("失败!");
						rosten.toggleAction(buttonWidget,false);
					}
					
				},function(error){
					rosten.alert("系统错误，请通知管理员！");
					rosten.toggleAction(buttonWidget,false);
				});
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
		data-dojo-props='actionBarSrc:"${createLink(controller:'staffAction',action:'statusChangeForm',id:statusChange?.id,params:[userid:user?.id])}"'>
	</div>
</div>
<div data-dojo-id="rosten_tabContainer" data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
  	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props='style:{height:"520px"}'>
       	<form class="rosten_form" id="rosten_form" onsubmit="return false;" style="padding:0px">
       		<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${statusChange?.id }"' />
       		<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
       		<input id="personInforId" data-dojo-type="dijit/form/ValidationTextBox"  data-dojo-props='name:"personInforId",style:{display:"none"},value:"${personInfor?.id }"' />
       		
       	  	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"员工信息",toggleable:false,moreText:"",height:"220px",marginBottom:"2px"'>
       	  		<table border="0" align="left" style="margin:0 auto;width:740px">
					<tr>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>离职人员：</div></td>
					    <td width="260">
					    	<input id="personInforName" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='trim:true,required:true,readOnly:true,value:"${statusChange?.getApplayPersonInforName()}"
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
			                 	data-dojo-props='readOnly:true,value:"${statusChange?.getOutDepartName()}"'/>
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
				href:"${createLink(controller:'share',action:'getFileUploadNew',id:statusChange?.id,params:[uploadPath:'staff',isShowFile:isShowFile])}"'>
			</div>
            
		</form>
	</div>
	<g:if test="${statusChange?.id}">
		<div data-dojo-type="dijit/layout/ContentPane" id="flowComment" title="流转意见" data-dojo-props='refreshOnShow:true,
			href:"${createLink(controller:'share',action:'getCommentLog',id:statusChange?.id)}"
		'>	
		</div>
		<div data-dojo-type="dijit/layout/ContentPane" id="flowLog" title="流程跟踪" data-dojo-props='refreshOnShow:true,
			href:"${createLink(controller:'share',action:'getFlowLog',id:statusChange?.id,params:[processDefinitionId:statusChange?.processDefinitionId,taskId:statusChange?.taskId])}"
		'>	
		</div>
	</g:if>
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
</html>