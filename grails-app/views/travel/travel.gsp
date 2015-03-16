<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>出差申请</title>
    <style type="text/css">
    	.rosten .travel_form table tr{
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
		 		"dojo/_base/xhr",
		 		"dojo/has",
		 		"dojo/dom",
		 		"dojo/_base/lang",
		 		"dojo/date/stamp",
		 		"rosten/widget/DepartUserDialog",
		 		"dijit/layout/TabContainer",
		 		"dijit/layout/ContentPane",
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
		     	"rosten/widget/TitlePane",
		     	"rosten/app/Application",
		     	"rosten/kernel/behavior"],
			function(parser,kernel,registry,xhr,has,dom,lang,datestamp,DepartUserDialog){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}"});
					rosten.cssinit();
				});
				//保存功能
				travel_add = function(object){
					
					var formWidget = registry.byId("travel_form");
					if(!formWidget.validate()){
						rosten.alert("请正确填写相关信息！");
						return;
					}

					var travelUsers = registry.byId("travelUsers");
					if(travelUsers.get("value")==""){
						rosten.alert("出差人员不正确！").queryDlgClose = function(){
							travelUsers.focus();
						};
						return;
					}

					var content = {};
					
					//流程相关信息
					<g:if test='${flowCode}'>
						content.flowCode = "${flowCode}";
						content.relationFlow = "${relationFlow}";
					</g:if>

					//添加新增时添加附件功能
					<g:if test="${!travel?.id}">
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
					
					rosten.readSync(rosten.webPath + "/travel/travelSave",content,function(data){
						if(data.result=="true" || data.result == true){
							rosten.alert("保存成功！").queryDlgClose= function(){
								//2015-3-16-----根据用户要求，自动关闭页面
								page_quit();

								/*
								if(window.location.href.indexOf(data.id)==-1){
									window.location.replace(window.location.href + "&id=" + data.id);
								}else{
									window.location.reload();
								}*/
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
					},"travel_form");
				};
				travel_deal = function(type,readArray,buttonWidget,conditionObj){
					var content = {};
					content.id = registry.byId("id").attr("value");
					content.deal = type;
					if(readArray){
						content.dealUser = readArray.join(",");
					}
					rosten.readSync(rosten.webPath + "/travel/travelFlowDeal",content,function(data){
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
				travel_submit_select = function(url,buttonWidget,conditionObj){
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
			            	travel_deal("submit",_data,buttonWidget,conditionObj);	
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
				travel_submit = function(object,conditionObj){
					//从后台获取下一处理人
					
					//增加对多次单击的次数----2014-9-4
					var buttonWidget = object.target;
					rosten.toggleAction(buttonWidget,true);
				
					var content = {};
					
					//增加对排他分支的控制
					if(conditionObj){
						lang.mixin(content,conditionObj);
					}
					
					rosten.readSyncNoTime("${createLink(controller:'share',action:'getSelectFlowUser',params:[userId:user?.id,taskId:travel?.taskId,drafterUsername:travel?.drafter?.username])}",content,function(data){
						if(data.dealFlow==false){
							//流程无下一节点
							travel_deal("submit",null,buttonWidget,conditionObj);
							return;
						}
						var url = "${createLink(controller:'system',action:'userTreeDataStore',params:[companyId:company?.id])}";
						if(data.dealType=="user"){
							//人员处理
							if(data.showDialog==false){
								//单一处理人
								var _data = [];
								_data.push(data.userId + ":" + data.userDepart);
								travel_deal("submit",_data,buttonWidget,conditionObj);
							}else{
								//多人，多部门处理
								url += "&type=user&user=" + data.user;
								travel_submit_select(url,buttonWidget,conditionObj);
							}
						}else{
							//群组处理
							url += "&type=group&groupIds=" + data.groupIds;
							if(data.limitDepart){
								url += "&limitDepart="+data.limitDepart;
							}
							travel_submit_select(encodeURI(url),buttonWidget,conditionObj);
						}

					},function(error){
						rosten.alert("系统错误，请通知管理员！");
						rosten.toggleAction(buttonWidget,false);
					});
				};
				travel_back = function(object,conditionObj){
					//增加对多次单击的控制
					var buttonWidget = object.target;
					rosten.toggleAction(buttonWidget,true);
					
					var content = {};
					rosten.readSyncNoTime("${createLink(controller:'travel',action:'travelFlowBack',params:[id:travel?.id])}",content,function(data){
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
				addComment = function(){
					//flowCode为是否需要走流程，如需要，则flowCode为业务流程代码
					var commentDialog = rosten.addCommentDialog({type:"travel"});
					commentDialog.callback = function(_data){
						var content = {dataStr:_data.content,userId:"${user?.id}",status:"${travel?.status}",flowCode:"${flowCode}"};
						rosten.readSync(rosten.webPath + "/share/addComment/${travel?.id}",content,function(data){
							if(data.result=="true" || data.result == true){
								rosten.alert("成功！").queryDlgClose= function(){
									var selectWidget = rosten_tabContainer.selectedChildWidget;
									if(selectWidget.get("id")=="share_Comment"){
										rosten_tabContainer.selectedChildWidget.refresh();
									}
								};
							}else{
								rosten.alert("失败!");
							}	
						});
					};
				};
				page_quit = function(){
					rosten.pagequit();
				};
				//自动计算出差天数
				computeDays = function(ostr){
					var startDate = registry.byId("startDate");
					var endDate = registry.byId("endDate");
					var newdate = new Date() 
					
					if(endDate.get("value") < startDate.get("value")){
						rosten.alert("开始时间不能小于结束时间！").queryDlgClose= function(){
							if(ostr=="startDate"){
								startDate.set("value",new Date());
							}else{
								newdate.setDate(newdate.getDate()+1);
								endDate.set("value",newdate);
							}	
						}
					}else{
						registry.byId("days").set("value",(endDate.get("value")-startDate.get("value"))/1000/60/60/24);
					}
				};
				//自动计算出差人数
				selectUser = function(){
					var _dialog = rosten.selectUser("${createLink(controller:'staff',action:'staffTreeDataStore',params:[companyId:company?.id])}","multile","travelUsers","travelUserIds");
					
					_dialog.callback = function(data) {
			            var _data = [];
			            var _data_1 = [];
			            for (var k = 0; k < data.length; k++) {
			                var item = data[k];
			                _data.push(item.name);
			                _data_1.push(item.value);
			            }
			            registry.byId("travelUsers").set("value", _data.join(","));
			            registry.byId("travelUserIds").set("value",_data_1.join(","));
			            registry.byId("travelNum").set("value",data.length);
			        }; 
                     
				}
		});
    </script>
</head>
<body>
<div class="rosten_action">
	<div data-dojo-type="rosten/widget/ActionBar" data-dojo-id="travel_actionBar" 
		data-dojo-props='actionBarSrc:"${createLink(controller:'travelAction',action:'travelForm',id:travel?.id,params:[userid:user?.id])}"'>
	</div>
</div>

<div data-dojo-id="rosten_tabContainer" data-dojo-type="dijit/layout/TabContainer" 
	data-dojo-props='doLayout:false,persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props='doLayout:false'>
		<form id="travel_form" name="travel_form" data-dojo-type="dijit/form/Form"
			onsubmit="return false;" class="rosten_form" style="padding:0px">
			
			<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${travel?.id }"' />
        	<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        	
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"基本信息",toggleable:false,moreText:"",height:"370px",marginBottom:"2px"'>
				<table border="0" width="740" align="left">
					<tr>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>申请单编号：</div></td>
					    <td width="250">
					    	<input id="applyNum" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='readOnly:true,
			                 		trim:true,placeHolder:"保存后自动生成",
									value:"${travel?.applyNum}"
			                '/>
					    </td>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>申请日期：</div></td>
					    <td width="250">
					    	<input id="time" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"time",readOnly:true,
			                	trim:true,required:true,
			                	value:"${travel?.getFormattedDate1()}"
			               '/>
			           </td>
					</tr>
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>申请人：</div></td>
					    <td>
					    	<input id="drafter" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='trim:true,readOnly:true,
									value:"${travel?.drafter?.getFormattedName()}"
			                '/>
			            </td>
					</tr>
					
					<tr>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>开始时间：</div></td>
					    <td width="250">
					    	<input id="startDate" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"startDate",${fieldAcl.isReadOnly("startDate")},
			                	trim:true,required:true,missingMessage:"请正确填写日期！",invalidMessage:"请正确填写日期！",
			                	onChange:function(){computeDays("startDate")},
			                	value:"${travel?.getFormattedStartDate()}"
			               '/>
			            </td>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>结束时间：</div></td>
					    <td>
					    	<input id="endDate" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"endDate",${fieldAcl.isReadOnly("endDate")},
			                	trim:true,required:true,missingMessage:"请正确填写日期！",invalidMessage:"请正确填写日期！",
			                	onChange:function(){computeDays("endDate")},
			                	value:"${travel?.getFormattedEndDate()}"
			               '/>
			            </td>    
					</tr>
					
					<tr>
					  <!--
					  <td><div align="right"><span style="color:red">*&nbsp;</span>出差负责人：</div></td>
					    <td>
					    	<input id="chargePerson" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"chargePerson",trim:true,required:true,
			                 	invalidMessage:"请正确填写！",
									value:"${travel?.chargePerson}"
			                '/>
			            </td> 
		              -->
		              
		              	<td><div align="right"><span style="color:red">*&nbsp;</span>工作部门：</div></td>
					    <td>
					    	<input id="drafterDepart" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"drafterDepart",trim:true,required:true,${fieldAcl.isReadOnly("drafterDepart")},
									value:"${travel?.drafterDepart}"
			                '/>
			                <g:if test="${isShowFile}">
				                <button data-dojo-type="dijit/form/Button" 
									data-dojo-props = 'onClick:function(){rosten.selectDepart("${createLink(controller:'system',action:'departTreeDataStore',params:[companyId:company?.id])}",false,"drafterDepart")}'
								>选择</button>
							</g:if>
			            </td>   
		              
					    <td><div align="right"><span style="color:red">*&nbsp;</span>出差地点：</div></td>
					    <td>
					    	<input id="travelAddress" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"travelAddress",trim:true,required:true,invalidMessage:"请正确填写！",
									value:"${travel?.travelAddress}"
			                '/>
			            </td>    
					</tr>
					
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>出差人员：</div></td>
					    <td colspan=3>
					  <input  data-dojo-type="dijit/form/ValidationTextBox" id="travelUserIds" data-dojo-props='name:"travelUserIds",style:{display:"none"},value:"${travel.getTravelUsersValue()}"' />
					    	<textarea id="travelUsers" data-dojo-type="dijit/form/SimpleTextarea" 
    							data-dojo-props='readOnly:true,"class":"input",required:true,name:"travelUsers",
                                		style:{width:"550px"},readOnly:true,
                                		trim:true,
                                		value:"${travel?.getTravelUsersValue()}"
                           '>
    						</textarea>
    						<g:if test="${isShowFile}">
	    						<button data-dojo-type="dijit/form/Button" 
									data-dojo-props = 'onClick:function(){selectUser()}'
								>选择</button>
							</g:if>
			            </td>    
					</tr>
						
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>出差人数：</div></td>
					    <td>
					    	<input id="travelNum" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='trim:true,readOnly:true,required:true,name:"travelNum",
									value:${travel?.travelNum}
			                '/>
			            </td>
			               <td><div align="right"><span style="color:red">*&nbsp;</span>出差天数：</div></td>
					    <td>
					    	<input id="days" data-dojo-type="dijit/form/NumberTextBox" 
			                 	data-dojo-props='trim:true,readOnly:true,required:true,name:"days",
									value:${travel?.days}
			                '/>
			            </td>
					      
					</tr>
					<tr>
					    <td><div align="right">出差事由：</div></td>
					    <td  colspan=3>
					    	<textarea id="travelReason" data-dojo-type="dijit/form/SimpleTextarea" 
    							data-dojo-props='name:"travelReason","class":"input",
                               		style:{width:"550px"},rows:"8",${fieldAcl.isReadOnly("travelReason")},
                               		trim:true,value:"${travel?.travelReason }"
                           '>
    						</textarea>
					    </td>
					</tr>
					
				</table>
			</div>
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"附件信息",toggleable:false,moreText:"",
				href:"${createLink(controller:'share',action:'getFileUploadNew',id:travel?.id,params:[uploadPath:'travel',isShowFile:isShowFile])}"'>
			</div>
		</form>
		
	</div>
	
	<g:if test="${travel?.id && travel?.processDefinitionId}">
		<div data-dojo-type="dijit/layout/ContentPane" id="share_Comment" title="流转意见" data-dojo-props='refreshOnShow:true,
			href:"${createLink(controller:'share',action:'getCommentLog',id:travel?.id)}"
		'>	
		</div>
		<div data-dojo-type="dijit/layout/ContentPane" id="share_FlowLog" title="流程跟踪" data-dojo-props='refreshOnShow:true,
			href:"${createLink(controller:'share',action:'getFlowLog',id:travel?.id,params:[processDefinitionId:travel?.processDefinitionId,taskId:travel?.taskId])}"
		'>	
		</div>
	
	</g:if>
</div>
</body>