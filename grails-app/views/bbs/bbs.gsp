<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>通知公告</title>
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
		 		"dijit/Editor",
				"dijit/_editor/plugins/FontChoice",
		     	"rosten/widget/ActionBar",
		     	"rosten/app/Application",
		     	"rosten/kernel/behavior"],
			function(parser,kernel,registry,xhr,dom,lang,datestamp,DepartUserDialog){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}"});
					rosten.cssinit();

					<g:if test="${bbs.id && bbs.id!=null && !"".equals(bbs.id)}">
					
						var ioArgs = {
							url : rosten.webPath + "/bbs/bbsGetContent/${bbs?.id}",
							sync : true,
							handleAs : "text",
							preventCache : true,
							encoding : "utf-8",
							load : function(data) {
								registry.byId("content").set("value",data);
							}
						};
						xhr.get(ioArgs);
					</g:if>
				});
				
			bbs_add = function(object){
				var chenkids = ["level1","category","publishDate","topic"];
				if(!rosten.checkData(chenkids)) return;
				
				var content = {};
				var publishDate = registry.byId("publishDate");
				content.publishDate = datestamp.toISOString(publishDate.attr("value"),{selector: "date"});
				content.content = registry.byId("content").get("value");
				
				//流程相关信息
				<g:if test='${flowCode}'>
					content.flowCode = "${flowCode}";
					content.relationFlow = "${relationFlow}";
				</g:if>

				//添加新增时添加附件功能
				<g:if test="${!bbs?.id}">
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
				
				rosten.readSync(rosten.webPath + "/bbs/bbsSave",content,function(data){
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
			bbs_back = function(object,conditionObj){
				//增加对多次单击的次数----2014-9-4
				var buttonWidget = object.target;
				rosten.toggleAction(buttonWidget,true);
				
				var content = {};
				rosten.readSync("${createLink(controller:'bbs',action:'bbsFlowBack',params:[id:bbs?.id])}",content,function(data){
					if(data.result=="true" || data.result == true){
						rosten.alert("成功！").queryDlgClose= function(){
							//刷新首页bbs内容
							window.opener.showStartBbs("${user?.id}","${company?.id }");
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
			bbs_deal = function(type,readArray,buttonWidget,conditionObj){
				var content = {};
				content.id = registry.byId("id").attr("value");
				content.deal = type;
				if(readArray){
					content.dealUser = readArray.join(",");
				}
				if(conditionObj){
					lang.mixin(content,conditionObj);
				}
				rosten.readSync(rosten.webPath + "/bbs/bbsFlowDeal",content,function(data){
					if(data.result=="true" || data.result == true){
						rosten.alert("成功！").queryDlgClose= function(){
							//刷新首页bbs内容
							window.opener.showStartBbs("${user?.id}","${company?.id }");
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
			bbs_submit_select = function(url,buttonWidget,conditionObj){
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
		            	bbs_deal("submit",_data,buttonWidget,conditionObj);
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
			bbs_submit = function(object,conditionObj){
				//从后台获取下一处理人
				
				//增加对多次单击的次数----2014-9-4
				var buttonWidget = object.target;
				rosten.toggleAction(buttonWidget,true);
				
				var content = {};

				//增加对排他分支的控制
				if(conditionObj){
					lang.mixin(content,conditionObj);
				}
				
				rosten.readSync("${createLink(controller:'share',action:'getSelectFlowUser',params:[userId:user?.id,taskId:bbs?.taskId,drafterUsername:bbs?.drafter?.username])}",content,function(data){
					if(data.dealFlow==false){
						//流程无下一节点
						bbs_deal("submit",null,buttonWidget,conditionObj);
						return;
					}
					var url = "${createLink(controller:'system',action:'userTreeDataStore',params:[companyId:company?.id])}";
					if(data.dealType=="user"){
						//人员处理
						if(data.showDialog==false){
							//单一处理人
							var _data = [];
							_data.push(data.userId + ":" + data.userDepart);
							bbs_deal("submit",_data,buttonWidget,conditionObj);
						}else{
							//多人，多部门处理
							url += "&type=user&user=" + data.user;
							bbs_submit_select(url,buttonWidget,conditionObj);
						}
					}else{
						//群组处理
						url += "&type=group&groupIds=" + data.groupIds;
						if(data.limitDepart){
							url += "&limitDepart="+data.limitDepart;
						}
						bbs_submit_select(encodeURI(url),buttonWidget,conditionObj);
					}

				},function(error){
					rosten.alert("系统错误，请通知管理员！");
					rosten.toggleAction(buttonWidget,false);
				});
			};
			bbs_addComment = function(){
				//flowCode为是否需要走流程，如需要，则flowCode为业务流程代码
				var commentDialog = rosten.addCommentDialog({type:"bbs"});
				commentDialog.callback = function(_data){
					var content = {dataStr:_data.content,userId:"${user?.id}",status:"${bbs?.status}",flowCode:"${flowCode}"};
					rosten.readSync(rosten.webPath + "/share/addComment/${bbs?.id}",content,function(data){
						if(data.result=="true" || data.result == true){
							rosten.alert("成功！");
						}else{
							rosten.alert("失败!");
						}	
					});
				};
				
			};
			page_quit = function(){
				rosten.pagequit();
			};
		});
		
		
    </script>
</head>
<body>
	<div class="rosten_action">
		<div data-dojo-type="rosten/widget/ActionBar" id="rosten_actionBar" data-dojo-props='actionBarSrc:"${createLink(controller:'bbsAction',action:'bbsForm',id:bbs?.id,params:[userid:user?.id])}"'></div>
	</div>
	<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
		
	  	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props='style:{height:"650px"}'>
        <form class="rosten_form" id="rosten_form" onsubmit="return false;" style="padding:0px">
        		<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${bbs?.id }"' />
        		<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        	 <div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"基本信息",toggleable:false,moreText:"",height:"460px",marginBottom:"2px"'>
        	  
                <table class="tableData" style="width:740px;margin:0px">
                    <tbody>
                       <tr>
						    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>流水号：</div></td>
						    <td width="250">
						    	<input id="serialNo" data-dojo-type="dijit/form/ValidationTextBox" 
				                 	data-dojo-props='readOnly:true,trim:true,placeHolder:"领导发布后自动生成",
										value:"${bbs?.serialNo}"
				                '/>
						    </td>
						    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>类别：</div></td>
						    <td width="250">
						    	<select id="category" data-dojo-type="dijit/form/ComboBox" 
					                data-dojo-props='name:"category",${fieldAcl.isReadOnly("category")},
					                trim:true,required:true,missingMessage:"请选择类别！",invalidMessage:"请选择类别！",
					      			value:"${bbs?.category}"
					            '>
								<option value="公告">公告</option>
								<option value="公文">公文</option>
					    	</select>
				           </td>
						</tr>
						<tr>
							<td>
						    	<div align="right"><span style="color:red">*&nbsp;</span>紧急程度：</div>
				            </td>
				            <td>
				            	<select id="level1" data-dojo-type="dijit/form/ComboBox"
					                data-dojo-props='name:"level1",${fieldAcl.isReadOnly("level1")},
					               	trim:true,required:true,missingMessage:"请选择紧急程度！",invalidMessage:"请选择紧急程度！",
					      			value:"${bbs?.level1}"
					            '>
									<option value="普通">普通</option>
									<option value="紧急">紧急</option>
									<option value="特急">特急</option>
					    		</select>
				            </td>
						    <td><div align="right"><span style="color:red">*&nbsp;</span>发布时间：</div></td>
						    <td>
						    	<input id="publishDate" data-dojo-type="dijit/form/DateTextBox" 
				                	data-dojo-props='name:"publishDate",${fieldAcl.isReadOnly("publishDate")},
				                	trim:true,required:true,missingMessage:"请正确填写发布时间！",invalidMessage:"请正确填写发布时间！",
				                	value:"${bbs?.getFormattedPublishDate("date")}"
				               '/>
						    
						   </td>
						        
						</tr>
						<tr>
						    <td><div align="right"><span style="color:red">*&nbsp;</span>标题：</div></td>
						    <td colspan=3>
						    	<input id="topic" data-dojo-type="dijit/form/ValidationTextBox" 
				                 	data-dojo-props='name:"topic",${fieldAcl.isReadOnly("topic")},
				                 		trim:true,required:true,missingMessage:"请正确填写标题！",invalidMessage:"请正确填写标题！",
				                 		style:{width:"490px"},
										value:"${bbs?.topic}"
				                '/>
						    
						    </td>    
						</tr>
						<tr>
						    <td><div align="right">内容：</td>
						    <td colspan=3>
						    	
						    	<div data-dojo-type="dijit/Editor" style="overflow:hidden;width:620px" id="content"
									extraPlugins="[{name:'dijit/_editor/plugins/FontChoice', command: 'fontName', generic: true},'fontSize']"
									data-dojo-props='name:"content"
				            		<g:if test="${fieldAcl.readOnly.contains('content')}">,disabled:true</g:if>
					            '>
									
								</div>
						    						    
						    </td>    
						</tr>
						
                    </tbody>
                </table>
            </div>
			
			
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"附件信息",toggleable:false,moreText:"",
				href:"${createLink(controller:'share',action:'getFileUploadNew',id:bbs?.id,params:[uploadPath:'bbs',isShowFile:isShowFile])}"'>
			</div>
		
		</form>
		</div>
		
		<g:if test="${bbs?.id && bbs?.processDefinitionId}">
			<div data-dojo-type="dijit/layout/ContentPane" id="bbsComment" title="流转意见" data-dojo-props='refreshOnShow:true,
				href:"${createLink(controller:'share',action:'getCommentLog',id:bbs?.id)}"
			'>	
			</div>
			<div data-dojo-type="dijit/layout/ContentPane" id="bbsFlowLog" title="流程跟踪" data-dojo-props='refreshOnShow:true,
				href:"${createLink(controller:'share',action:'getFlowLog',id:bbs?.id,params:[processDefinitionId:bbs?.processDefinitionId,taskId:bbs?.taskId])}"
			'>	
			</div>
		
		</g:if>
	</div>
</body>
</html>