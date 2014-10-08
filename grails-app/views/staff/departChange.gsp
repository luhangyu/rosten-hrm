<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>部门调动</title>
    <style type="text/css">
		body{
			overflow:auto;
		}
    </style>
	<script type="text/javascript">
		require(["dojo/parser",
		 		"dojo/_base/kernel",
		 		"dijit/registry",
		 		"dojo/_base/xhr",
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
		     	"rosten/kernel/behavior"],
			function(parser,kernel,registry,xhr,datestamp,DepartUserDialog){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}"});
					rosten.cssinit();
				});
				
			departChange_add = function(object){
				var chenkids = ["personInforName","changeType","allowdepartsName"];
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
				var changeDate = registry.byId("changeDate");
				content.changeDate = datestamp.toISOString(changeDate.attr("value"),{selector: "date"});

				//流程相关信息
				<g:if test='${flowCode}'>
					content.flowCode = "${flowCode}";
					content.relationFlow = "${relationFlow}";
				</g:if>
				
				//增加对多次单击的次数----2014-9-4
				var buttonWidget = object.target;
				rosten.toggleAction(buttonWidget,true);
				
				rosten.readSync(rosten.webPath + "/staff/staffDepartChangeSave",content,function(data){
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
			departChange_addComment = function(){
				//flowCode为是否需要走流程，如需要，则flowCode为业务流程代码
				var commentDialog = rosten.addCommentDialog({type:"departChange"});
				commentDialog.callback = function(_data){
					var content = {dataStr:_data.content,userId:"${user?.id}",status:"${departChange?.status}",flowCode:"${flowCode}"};
					rosten.readSync(rosten.webPath + "/share/addComment/${departChange?.id}",content,function(data){
						if(data.result=="true" || data.result == true){
							rosten.alert("成功！");
						}else{
							rosten.alert("失败!");
						}	
					});
				};
			};
			departChange_submit = function(object,conditionObj){
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
				//2014-10-8针对当前流程，对调出调入部门进行特殊控制
				if("${departChange.status}"=="领导审核"){
					//下一节点为调出部门审核
					content.selectDepart = registry.byId("outDepartName").get("value");
				}else if("${departChange.status}" == "调出部门审核"){
					//下一节点为调入部门审核
					content.selectDepart = registry.byId("allowdepartsName").get("value");
				}
				
				rosten.readSync("${createLink(controller:'share',action:'getSelectFlowUser',params:[userId:user?.id,taskId:departChange?.taskId,drafterUsername:departChange?.drafter?.username])}",content,function(data){
					if(data.dealFlow==false){
						//流程无下一节点
						departChange_deal("submit",null,buttonWidget,conditionObj);
						return;
					}
					var url = "${createLink(controller:'system',action:'userTreeDataStore',params:[companyId:company?.id])}";
					if(data.dealType=="user"){
						//人员处理
						if(data.showDialog==false){
							//单一处理人
							var _data = [];
							_data.push(data.userId + ":" + data.userDepart);
							departChange_deal("submit",_data,buttonWidget,conditionObj);
						}else{
							//多人，多部门处理
							url += "&type=user&user=" + data.user;
							departChange_select(url,buttonWidget,conditionObj);
						}
					}else{
						//群组处理
						url += "&type=group&groupIds=" + data.groupIds;
						if(data.limitDepart){
							url += "&limitDepart="+data.limitDepart;
						}
						departChange_select(encodeURI(url),buttonWidget,conditionObj);
					}

				},function(error){
					rosten.alert("系统错误，请通知管理员！");
					rosten.toggleAction(buttonWidget,false);
				});
			};
			departChange_select = function(url,buttonWidget,conditionObj){
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
		            	departChange_deal("submit",_data,buttonWidget,conditionObj);
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
			departChange_deal = function(type,readArray,buttonWidget,conditionObj){
				var content = {};
				content.id = "${departChange?.id}";
				content.deal = type;
				if(readArray){
					content.dealUser = readArray.join(",");
				}
				if(conditionObj){
					lang.mixin(content,conditionObj);
				}
				rosten.readSync(rosten.webPath + "/staff/staffDepartChangeFlowDeal",content,function(data){
					if(data.result=="true" || data.result == true){
						rosten.alert("成功！").queryDlgClose= function(){
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
			departChange_back = function(object,conditionObj){
				//增加对多次单击的控制
				var buttonWidget = object.target;
				rosten.toggleAction(buttonWidget,true);
				
				var content = {};
				rosten.readSync("${createLink(controller:'staff',action:'staffDepartChangeFlowBack',params:[id:departChange?.id])}",content,function(data){
					if(data.result=="true" || data.result == true){
						rosten.alert("成功！").queryDlgClose= function(){
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
			page_quit = function(){
				rosten.pagequit();
			};
		});
		
		
    </script>
</head>
<body>
	<div class="rosten_action">
		<div data-dojo-type="rosten/widget/ActionBar" id="rosten_actionBar" data-dojo-props='actionBarSrc:"${createLink(controller:'staffAction',action:'departChangeForm',id:departChange?.id,params:[userid:user?.id])}"'></div>
	</div>
	<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
	  	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props='style:{height:"480px"}'>
        	<form class="rosten_form" id="rosten_form" onsubmit="return false;" style="padding:0px">
        		<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${departChange?.id }"' />
        		<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        	  	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"基本信息",toggleable:false,moreText:"",height:"400px",marginBottom:"2px"'>
        	  
                <table class="tableData" style="width:740px;margin:0px">
                    <tbody>
                       <tr>
						    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>调动人员：</div></td>
						    <td width="250">
						    	<input  data-dojo-type="dijit/form/ValidationTextBox" id="personInforId"  data-dojo-props='name:"personInforId",style:{display:"none"},value:"${departChange?.personInfor?.id }"' />
						    	<input id="personInforName" data-dojo-type="dijit/form/ValidationTextBox" 
				                 	data-dojo-props='trim:true,required:true,value:"${departChange?.getPersonInforName() }"
				                '/>
						    </td>
						    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>调动类型：</div></td>
						    <td width="250">
						    	<select id="changeType" data-dojo-type="dijit/form/FilteringSelect" 
					                data-dojo-props='name:"changeType",trim:true,required:true,
					      			value:"${departChange.changeType?departChange.changeType:"人员调动"}"
					            '>
								<option value="人员调动">人员调动</option>
								<option value="人员下派">人员下派</option>
								<option value="人员借用">人员借用</option>
					    	</select>
				           </td>
						</tr>
						<tr>
							<td>
						    	<div align="right"><span style="color:red">*&nbsp;</span>调出部门：</div>
				            </td>
				            <td>
				            	<input id="outDepartName" data-dojo-type="dijit/form/ValidationTextBox" 
				                 	data-dojo-props='trim:true,required:true,readOnly:true,
										value:"${departChange?.getOutDepartName()}"
				                '/>
				            </td>
						    <td><div align="right"><span style="color:red">*&nbsp;</span>调入部门：</div></td>
						    <td>
						    	<input id="allowdepartsName" data-dojo-type="dijit/form/ValidationTextBox" 
					               	data-dojo-props='name:"allowdepartsName",readOnly:true,
					               		trim:true,required:true,
										value:"${departChange?.getInDepartName()}"
					          	'/>
					         	<g:hiddenField name="allowdepartsId" value="${departChange?.inDepart?.id}" />
								<button data-dojo-type="dijit.form.Button" data-dojo-props='onClick:function(){selectDepart("${createLink(controller:'system',action:'departTreeDataStore',params:[companyId:company?.id])}")}'>选择</button>
						    
						   </td>
						        
						</tr>
						<tr>
						    <td><div align="right"><span style="color:red">*&nbsp;</span>调动时间：</div></td>
						    <td colspan=3>
				                <input id="changeDate" data-dojo-type="dijit/form/DateTextBox" 
	                 				data-dojo-props='name:"changeDate",trim:true,required:true,
	                 					value:"${departChange?.getShowChangeDate() }"
	                 				'/>
						    
						    </td>    
						</tr>
						
						<tr>
						    <td><div align="right"><span style="color:red">*&nbsp;</span>申请理由：</div></td>
						    <td colspan=3>
				                <textarea id="changeReason" data-dojo-type="dijit/form/SimpleTextarea" 
									data-dojo-props='name:"changeReason",
					                    style:{width:"620px",height:"300px"},
					                    trim:true,value:"${departChange?.changeReason}"
					            '>
								</textarea>
						    
						    </td>    
						</tr>
                    </tbody>
                </table>
                </div>
			</form>
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"附件信息",toggleable:false,moreText:"",
				height:"60px",href:"${createLink(controller:'share',action:'getFileUpload',id:departChange?.id,params:[uploadPath:'staff',isShowFile:isShowFile])}"'>
			</div>
			
		</div>
		<g:if test="${departChange?.id}">
			<div data-dojo-type="dijit/layout/ContentPane" id="flowComment" title="流转意见" data-dojo-props='refreshOnShow:true,
				href:"${createLink(controller:'share',action:'getCommentLog',id:departChange?.id)}"
			'>	
			</div>
			<div data-dojo-type="dijit/layout/ContentPane" id="flowLog" title="流程跟踪" data-dojo-props='refreshOnShow:true,
				href:"${createLink(controller:'share',action:'getFlowLog',id:departChange?.id,params:[processDefinitionId:departChange?.processDefinitionId,taskId:departChange?.taskId])}"
			'>	
			</div>
		</g:if>
	</div>
</body>
</html>