<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>员工转正</title>
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
		 		"dojo/_base/lang",
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
		 		"dojox/widget/Wizard",
				"dojox/widget/WizardPane",
		     	"rosten/widget/ActionBar",
		     	"rosten/app/Application",
		     	"rosten/app/SystemApplication",
		     	"rosten/app/StaffApplication",
		     	"rosten/kernel/behavior"],
			function(parser,kernel,registry,xhr,dom,lang,datestamp,DepartUserDialog){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}"});
					rosten.cssinit();
				});
				
			officialApply_add = function(object){

				var formWidget = registry.byId("rosten_form");
				if(!formWidget.validate()){
					rosten.alert("请正确填写相关信息！");
					return;
				}

				var applyReasonDom = registry.byId("applyReason");
				var applyReason = applyReasonDom.get("value");
				if(applyReason==""){
					rosten.alert("申请理由不能为空！").queryDlgClose = function(){
						applyReasonDom.focus();
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
				<g:if test="${!officialApply?.id}">
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
				
				rosten.readSync(rosten.webPath + "/staff/officialApplySave",content,function(data){
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
			officialApply_addComment = function(){
				//flowCode为是否需要走流程，如需要，则flowCode为业务流程代码
				var commentDialog = rosten.addCommentDialog({type:"officialApply"});
				commentDialog.callback = function(_data){
					var content = {dataStr:_data.content,userId:"${user?.id}",status:"${officialApply?.status}",flowCode:"${flowCode}"};
					rosten.readSync(rosten.webPath + "/share/addComment/${officialApply?.id}",content,function(data){
						if(data.result=="true" || data.result == true){
							rosten.alert("成功！");
						}else{
							rosten.alert("失败!");
						}	
					});
				};
			};
			officialApply_submit = function(object,conditionObj){
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
				rosten.readSync("${createLink(controller:'share',action:'getSelectFlowUser',params:[userId:user?.id,taskId:officialApply?.taskId,drafterUsername:officialApply?.drafter?.username])}",content,function(data){
					if(data.dealFlow==false){
						//流程无下一节点
						officialApply_deal("submit",null,buttonWidget,conditionObj);
						return;
					}
					var url = "${createLink(controller:'system',action:'userTreeDataStore',params:[companyId:company?.id])}";
					if(data.dealType=="user"){
						//人员处理
						if(data.showDialog==false){
							//单一处理人
							var _data = [];
							_data.push(data.userId + ":" + data.userDepart);
							officialApply_deal("submit",_data,buttonWidget,conditionObj);
						}else{
							//多人，多部门处理
							url += "&type=user&user=" + data.user;
							officialApply_select(url,buttonWidget,conditionObj);
						}
					}else{
						//群组处理
						url += "&type=group&groupIds=" + data.groupIds;
						if(data.limitDepart){
							url += "&limitDepart="+data.limitDepart;
						}
						officialApply_select(encodeURI(url),buttonWidget,conditionObj);
					}

				},function(error){
					rosten.alert("系统错误，请通知管理员！");
					rosten.toggleAction(buttonWidget,false);
				});
			};
			officialApply_select = function(url,buttonWidget,conditionObj){
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
		            	officialApply_deal("submit",_data,buttonWidget,conditionObj);
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
			officialApply_deal = function(type,readArray,buttonWidget,conditionObj){
				var content = {};
				content.id = "${officialApply?.id}";
				content.deal = type;
				if(readArray){
					content.dealUser = readArray.join(",");
				}
				if(conditionObj){
					lang.mixin(content,conditionObj);
				}
				rosten.readSync(rosten.webPath + "/staff/officialApplyFlowDeal",content,function(data){
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
			officialApply_back = function(object,conditionObj){
				//增加对多次单击的控制
				var buttonWidget = object.target;
				rosten.toggleAction(buttonWidget,true);
				
				var content = {};
				rosten.readSync("${createLink(controller:'staff',action:'officialApplyFlowBack',params:[id:officialApply?.id])}",content,function(data){
					if(data.result=="true" || data.result == true){
						rosten.alert("成功！下一处理人<" + data.nextUserName +">").queryDlgClose= function(){
							//刷新待办事项内容
							window.opener.showStartGtask("${user}","${company?.id }");
							
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
			officialApply_print_zzkhb = function(){
				
			};
			page_quit = function(){
				rosten.pagequit();
			};
		});
		
		
    </script>
</head>
<body>
	<div class="rosten_action">
		<div data-dojo-type="rosten/widget/ActionBar" id="rosten_actionBar" data-dojo-props='actionBarSrc:"${createLink(controller:'staffAction',action:'officialApplyForm',id:officialApply?.id,params:[userid:user?.id])}"'></div>
	</div>
	<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
	  	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props='style:{height:"600px"}'>
	  		<form method="post" class="rosten_form" id="rosten_form" data-dojo-type="dijit/form/Form"
				onsubmit="return false;" style="padding:0px" enctype="multipart/form-data">
        		<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${officialApply?.id }"' />
        		<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        		<input  data-dojo-type="dijit/form/ValidationTextBox" id="personInforId"  data-dojo-props='name:"personInforId",style:{display:"none"},value:"${officialApply?.personInfor?.id }"' />
        	  <div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"基本信息",toggleable:false,moreText:"",marginBottom:"2px"'>
        	  
                <table class="tableData" style="width:740px;margin:0px">
                    <tbody>
                       <tr>
						    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>申请人：</div></td>
						    <td width="250">
						    	<input data-dojo-type="dijit/form/ValidationTextBox" 
				                 	data-dojo-props='name:"applyName",trim:true,required:true,readOnly:true,value:"${officialApply?.getPersonInforName() }"
				                '/>
						    </td>
						    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>申请部门：</div></td>
						    <td width="250">
						    	<input data-dojo-type="dijit/form/ValidationTextBox" 
					                data-dojo-props='trim:true,readOnly:true,
					      			value:"${officialApply?.getApplyDepartName() }"
					            '/>
								
				           </td>
						</tr>
						
						<tr>
						    <td><div align="right"><span style="color:red">*&nbsp;</span>试用期时间：</div></td>
						    <td colspan=3>
				                <input id="startDate" data-dojo-type="dijit/form/DateTextBox" 
	                 				data-dojo-props='name:"startDate",trim:true,required:true,
	                 					value:"${officialApply?.getFormattedStartDate() }"
	                 				'/> ~ 
						    	
						    	<input id="endDate" data-dojo-type="dijit/form/DateTextBox" 
	                 				data-dojo-props='name:"endDate",trim:true,required:true,
	                 					value:"${officialApply?.getFormattedEndDate() }"
	                 				'/>
	                 				
						    </td>    
						</tr>
						
						<tr>
						    <td><div align="right"><span style="color:red">*&nbsp;</span>申请理由：</div></td>
						    <td colspan=3>
				                <textarea id="applyReason" data-dojo-type="dijit/form/SimpleTextarea" 
									data-dojo-props='name:"applyReason",
					                    style:{width:"548px",height:"150px"},
					                    trim:true,value:"${officialApply?.applyReason}"
					            '>
								</textarea>
						    
						    </td>    
						</tr>
                    </tbody>
                </table>
                
            </div>
			
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"附件信息",toggleable:false,moreText:"",
				href:"${createLink(controller:'share',action:'getFileUploadNew',id:officialApply?.id,params:[uploadPath:'staff',isShowFile:isShowFile])}"'>
			</div>
			</form>
		</div>
		
		
		<g:if test="${officialApply?.id}">
			<div data-dojo-type="dijit/layout/ContentPane" id="flowComment" title="流转意见" data-dojo-props='refreshOnShow:true,
				href:"${createLink(controller:'share',action:'getCommentLog',id:officialApply?.id)}"
			'>	
			</div>
			<div data-dojo-type="dijit/layout/ContentPane" id="flowLog" title="流程跟踪" data-dojo-props='refreshOnShow:true,
				href:"${createLink(controller:'share',action:'getFlowLog',id:officialApply?.id,params:[processDefinitionId:officialApply?.processDefinitionId,taskId:officialApply?.taskId])}"
			'>	
			</div>
		</g:if>
	</div>
</body>
</html>