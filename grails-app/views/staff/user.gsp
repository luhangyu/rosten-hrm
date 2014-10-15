<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>人员信息</title>
    <style type="text/css">
    	body{
			overflow:auto;
		}
		.rosten .rostenFamilyTitleGrid .dijitTitlePaneContentInner{
			padding:2px 1px 1px 1px;
			height:160px;
		}
		.rosten .rostenDegreeTitleGrid .dijitTitlePaneContentInner{
			padding:2px 1px 1px 1px;
			height:280px;
		}
		.rosten .rostenWorkResumeTitleGrid .dijitTitlePaneContentInner{
			padding:2px 1px 1px 1px;
			height:280px;
		}
		/*
 * 附件上传css样式
 */
.dijitFileInput {
	position:relative;
	height:1.3em;
}
.dijitFileInputReal {
	position:absolute;
	z-index:2;
	filter:alpha(opacity:0);
	opacity:0;
	cursor:pointer;
	width:350px;
}
.dijitFileInputRealBlind {
	right:0;
}
.dijitFileInputReal:hover { cursor:pointer; } 

.dijitFileInputButton,
.dijitFileInputText {
	border:1px solid #333;
	padding:2px 12px 2px 12px; 
	cursor:pointer;
}

.dijitFileInputButton {
	z-index:3;
	visibility:hidden;
}
.dijitFakeInput { position:absolute; top:0; left:0; z-index:1; white-space: nowrap; }

.rosten .upload .dijitFakeInput input {
	border: 1px solid #bcc8dd;
	background-color: #fff;
	background-repeat: repeat-x;
	background-position: top left;
	background-image: -moz-linear-gradient(rgba(127, 127, 127, 0.2) 0%, rgba(127, 127, 127, 0) 2px);
	background-image: -webkit-linear-gradient(rgba(127, 127, 127, 0.2) 0%, rgba(127, 127, 127, 0) 2px);
	background-image: linear-gradient(rgba(127, 127, 127, 0.2) 0%, rgba(127, 127, 127, 0) 2px);
	line-height:normal;
	padding:0.2em 0.3em;
	width:280px;
}

.rosten .upload .dijitFileInputButton,
.rosten .upload .dijitFileInputText {
	background-image: -moz-linear-gradient(rgba(127, 127, 127, 0.2) 0%, rgba(127, 127, 127, 0) 2px);
	background-image: -webkit-linear-gradient(rgba(127, 127, 127, 0.2) 0%, rgba(127, 127, 127, 0) 2px);
	background-image: -o-linear-gradient(rgba(127, 127, 127, 0.2) 0%, rgba(127, 127, 127, 0) 2px);
	background-image: linear-gradient(rgba(127, 127, 127, 0.2) 0%, rgba(127, 127, 127, 0) 2px);
	background-color: #cde3f6;
	border: 1px solid #799ab7;
	border-radius: 4px;
	-moz-border-radius: 4px;
	-webkit-border-radius: 4px;
	box-shadow:0px 1px 1px rgba(0,0,0,0.2);
	-webkit-box-shadow:0px 1px 1px rgba(0,0,0,0.2);
	-moz-box-shadow: 0px 1px 1px rgba(0,0,0,0.2);
	font-size:12px;
	padding:2px 10px 2px 10px; 
}
    </style>
	<script type="text/javascript">
	require(["dojo/parser",
	        	"dojo/_base/lang",
		 		"dojo/_base/kernel",
		 		"dijit/registry",
		 		"dijit/form/Button",
		     	"rosten/widget/ActionBar",
		     	"dijit/layout/TabContainer",
				"dijit/layout/ContentPane",
		     	"dijit/form/TextBox",
		     	"dijit/form/RadioButton",
		     	"dijit/form/ValidationTextBox",
		     	"dijit/form/SimpleTextarea",
		     	"dijit/form/DropDownButton",
		     	"dijit/form/Form",
		 		"dojox/form/Uploader",
		 		"dojox/form/uploader/FileList",
		     	"dijit/form/Button",
		     	"dijit/Dialog",
				"dojox/grid/DataGrid",
		     	"dijit/form/RadioButton",
		     	"dijit/form/FilteringSelect",
		     	"dijit/form/ComboBox",
		     	"rosten/app/SystemApplication",
		     	"rosten/app/StaffApplication",
		     	"rosten/kernel/behavior",
		     	"rosten/app/Application"],
			function(parser,lang,kernel,registry,ActionBar){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}",dojogridcss : true});
					rosten.cssinit();
				});
			user_add_check = function(){
				<g:if test='${departId}'>
					if(rosten.check_common("username","账号不正确！",true)==false) return false;
					if(rosten.check_common("password","密码不正确！",true)==false) return false;
					if(rosten.check_common("passwordcheck","确认密码不正确！",true)==false) return false;
	
					var password = registry.byId("password");
					var passwordcheck = registry.byId("passwordcheck");
					if(password.attr("value")!=passwordcheck.attr("value")){
						rosten.alert("密码不一致！").queryDlgClose = function(){
							password.attr("value","");
							passwordcheck.attr("value","");
							password.focus();
						};
						return false;
					}
				</g:if>

				//个人概况检查
				if(rosten.check_common("chinaName","姓名不正确！",true)==false) return false;
				if(rosten.check_common("userTypeName","用户类型不正确！",true)==false) return false;
				if(rosten.check_common("allowdepartsName","所属部门不正确！")==false) return false;
				if(rosten.check_common("idCard","身份证号不正确！",true)==false) return false;

				//通讯方式
				if(rosten.check_common("mobile","移动电话不正确！",true)==false) return false;
				if(rosten.check_common("qq","QQ号码不正确！",true)==false) return false;
				if(rosten.check_common("address","通讯地址不正确！",true)==false) return false;
				if(rosten.check_common("addressPostcode","邮编不正确！",true)==false) return false;
				
				return true;
			};
			user_add = function(object){
				if(user_add_check()==false) return;

				//增加对多次单击的次数----2014-9-4
				var buttonWidget = object.target;
				rosten.toggleAction(buttonWidget,true);
				
				var content = {};
				<g:if test='${!userType.equals("super")  }'>
					content.companyId = "${company?.id}";
					<g:if test='${departId}'>
						content.userNameFront = registry.byId("userNameFront").attr("value");
					</g:if>
				</g:if>
				
				<g:if test='${"staffAdd".equals(type) && personInfor && personInfor.id==null}'>
					content.status = "新增";
				</g:if>
				
				content.staffFamily = rosten.getGridDataCollect(staffFamilyGrid,["name","relation","mobile","workUnit","duties","politicsStatus"]);
				content.degree = rosten.getGridDataCollect(degreeGrid,["degreeName","major","degree","getFormatteStartDate","getFormatteEndDate"]);
				content.workResume = rosten.getGridDataCollect(workResumeGrid,["workCompany","workContent","getFormatteStartDate","getFormatteEndDate","duty","proveName","remark"]);
				if(registry.byId("msResult")) content.msResult = registry.byId("msResult").attr("value");

				//流程相关信息
				<g:if test='${flowCode}'>
					content.flowCode = "${flowCode}";
					content.relationFlow = "${relationFlow}";
				</g:if>
				
				rosten.readSync(rosten.webPath + "/staff/userSave",content,function(data){
					if(data.result=="true"){
						rosten.alert("保存成功！").queryDlgClose= function(){
							<g:if test='${flowCode}'>
								if(window.location.href.indexOf(data.id)==-1){
									window.location.replace(window.location.href + "&id=" + data.id);
								}else{
									window.location.reload();
								}
							</g:if>
							<g:else>
								page_quit();
							</g:else>	
						};
					}else if(data.result=="repeat"){
						rosten.alert("账号冲突，保存失败!");
					}else{
						rosten.alert("保存失败!");
					}
				},function(error){
					rosten.alert("系统错误，请通知管理员！");
					rosten.toggleAction(buttonWidget,false);
				},"rosten_form");
			};
			user_addComment = function(){
				var commentDialog = rosten.addCommentDialog({type:"user"});
				commentDialog.callback = function(_data){
					var content = {dataStr:_data.content,userId:"${loginUser?.id}",status:"${personInfor?.status}",flowCode:"${flowCode}"};
					rosten.readSync(rosten.webPath + "/share/addComment/${personInfor?.id}",content,function(data){
						if(data.result=="true" || data.result == true){
							rosten.alert("成功！");
						}else{
							rosten.alert("失败!");
						}	
					});
				};
			};
			submit_select = function(url,buttonWidget,conditionObj){
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
		            	user_deal("submit",_data,buttonWidget,conditionObj);
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
			user_deal = function(type,readArray,buttonWidget,conditionObj){
				var content = {};
				content.id = "${personInfor?.id}";
				content.deal = type;
				if(readArray){
					content.dealUser = readArray.join(",");
				}
				if(conditionObj){
					lang.mixin(content,conditionObj);
				}
				rosten.readSync(rosten.webPath + "/staff/staffAddFlowDeal",content,function(data){
					if(data.result=="true" || data.result == true){
						rosten.alert("成功！").queryDlgClose= function(){
							//刷新待办事项内容
							window.opener.showStartGtask("${loginUser?.id}","${company?.id }");
							
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
			
			user_submit = function(object,conditionObj){
				//从后台获取下一处理人
				
				//增加对多次单击的控制
				var buttonWidget = object.target;
				rosten.toggleAction(buttonWidget,true);
				
				var content = {};

				//增加对排他分支的控制
				if(conditionObj){
					lang.mixin(content,conditionObj);
				}
				rosten.readSync("${createLink(controller:'share',action:'getSelectFlowUser',params:[userId:loginUser?.id,taskId:personInfor?.taskId,drafterUsername:personInfor?.drafter?.username])}",content,function(data){
					if(data.dealFlow==false){
						//流程无下一节点
						user_deal("submit",null,buttonWidget,conditionObj);
						return;
					}
					var url = "${createLink(controller:'system',action:'userTreeDataStore',params:[companyId:company?.id])}";
					if(data.dealType=="user"){
						//人员处理
						if(data.showDialog==false){
							//单一处理人
							var _data = [];
							_data.push(data.userId + ":" + data.userDepart);
							user_deal("submit",_data,buttonWidget,conditionObj);
						}else{
							//多人，多部门处理
							url += "&type=user&user=" + data.user;
							submit_select(url,buttonWidget,conditionObj);
						}
					}else{
						//群组处理
						url += "&type=group&groupIds=" + data.groupIds;
						if(data.limitDepart){
							url += "&limitDepart="+data.limitDepart;
						}
						submit_select(encodeURI(url),buttonWidget,conditionObj);
					}

				},function(error){
					rosten.alert("系统错误，请通知管理员！");
					rosten.toggleAction(buttonWidget,false);
				});
			};
			user_addBargain = function(object){
				var bargain_form = registry.byId("bargain_form");
				if(bargain_form==undefined){
					rosten.alert("请先查看合同相关信息");
					return;
				}
				if(!bargain_form.validate()) return;
				bargain_form.submit();
			};
			user_ok = function(object){
				//填写面试结果
				var msResultDom = registry.byId("msResult");
				var msResult = msResultDom.get("value");
				if(msResult==""){
					rosten.alert("面试结果不能为空！").queryDlgClose = function(){
						rostenTabContainer.selectChild(msResultContentPane); 
						msResultDom.focus();
					};
					return false;
				}
				user_submit(object,{conditionName:"flow",conditionValue:"agree",msResult:msResult});
			};
			user_end = function(object){
				//结束流程
				var bargain = registry.byId("bargainId");
				console.log(bargain);
				if(!bargain.validate()){
					rosten.alert("请先录入合同！").queryDlgClose = function(){
						barginDom.focus();
					};
					return false;
				}
				user_submit(object);
				rostenTabContainer.selectChild(barginContentPane); 
			};
			user_cancel = function(object){
				user_submit(object,{conditionName:"flow",conditionValue:"notAgree"});
			};
			user_back = function(object){
				//增加对多次单击的控制
				var buttonWidget = object.target;
				rosten.toggleAction(buttonWidget,true);
				
				var content = {};
				rosten.readSync("${createLink(controller:'staff',action:'staffAddFlowBack',params:[id:personInfor?.id])}",content,function(data){
					if(data.result=="true" || data.result == true){
						rosten.alert("成功！").queryDlgClose= function(){
							//刷新待办事项内容
							window.opener.showStartGtask("${loginUser}","${company?.id }");
							
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
				if(window.opener.rosten.kernel.navigationEntity!="userManage"){
					window.opener.rosten.kernel.refreshGrid();
				}else{
					window.opener.dom_rostenGrid.refresh();
				}
		        window.close();
			};

			//上传头像
			uploadPic=function(){
		    	rosten.createRostenShowDialog(rosten.webPath + "/staff/addUpload?personId=${personInfor?.id}", {
		            onLoadFunction : function() {
		            }
		        });
			};

			
	});
    </script>
</head>
<body>
	<div class="rosten_action">
		<div data-dojo-type="rosten/widget/ActionBar" id="rosten_actionBar" 
			data-dojo-props='actionBarSrc:"${createLink(controller:'staffAction',action:'staffForm',id:personInfor?.id,params:[userId:loginUser?.id,type:type])}"'></div>
	</div>
	
	<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"880px",margin:"0 auto"}' data-dojo-id="rostenTabContainer" >
        <div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props=''>
		<form class="rosten_form" id="rosten_form" onsubmit="return false;" style="text-align:left;padding:0px">
		
		    <input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
		    
			<g:if test='${departId}'>
				<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${user?.id }"' />
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"账号信息 <span style=\"color:red;margin-left:5px\">(必填信息)</span>",toggleable:false,moreText:"",height:"100px",marginBottom:"2px"'>
				<table border="0" width="740" align="left">
					<tr>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>账号：</div></td>
					    <td width="250">
					    	<input id="userNameFront" data-dojo-type="dijit/form/ValidationTextBox" 
                               	data-dojo-props='name:"userNameFront",style:{width:"50px"},
             						value:"${company?.shortName}-",disabled:true
                            '/>
					    	
				    		<input id="username" data-dojo-type="dijit/form/ValidationTextBox" 
                               	data-dojo-props='name:"username",${fieldAcl.isReadOnly("username")},
                               		trim:true,required:true,
                               		promptMessage:"请正确输入账号...",
                               		style:{width:"125px"},
                               		<g:if test="${username && !"".equals(username)}">disabled:true,</g:if>
             							value:"${username}"
                               '/>
					    	
					    </td>
					    <td width="120"><div align="right">具有角色：</div></td>
					    <td width="250">
					    	<input id="allowrolesName" data-dojo-type="dijit/form/ValidationTextBox"
                					data-dojo-props='trim:true,readOnly:true,
                						value:"${allowrolesName }"
                				'/>
                 				
                 					<g:hiddenField name="allowrolesId" value="${allowrolesId }" />
									<button data-dojo-type="dijit.form.Button" 
										data-dojo-props = 'onClick:function(){selectRole("${createLink(controller:'system',action:'roleSelect',params:[companyId:company?.id])}")}'
									>选择</button>
                 				
			           </td>
					</tr>
						<tr>
						    <td><div align="right"><span style="color:red">*&nbsp;</span>密码：</div></td>
						    <td>
						    	<input id="password" data-dojo-type="dijit/form/ValidationTextBox" 
	                               	data-dojo-props='name:"password",
	                               		type:"password",
	                               		trim:true,
	                               		required:true,
	                               		promptMessage:"请正确输入密码...",
	             						value:"${user?.password}"
	                           	'/>
				            </td>
						    <td><div align="right"><span style="color:red">*&nbsp;</span>密码确认：</div></td>
						    <td>
						    	<input id="passwordcheck" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"passwordcheck",
                                		type:"password",
                                		trim:true,
                                		required:true,
                                		promptMessage:"请正确输入密码...",
              							value:"${user?.password}"
                                '/>
				            </td>    
						</tr>
					
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>CSS样式表：</div></td>
					    <td>
					    	<select id="cssStyle" data-dojo-type="dijit/form/FilteringSelect"
                           		data-dojo-props='name:"cssStyle",
                           			autoComplete:false,${fieldAcl.isReadOnly("cssStyle")},
            						value:"${(user!=null && user.cssStyle!=null)?user.cssStyle:"normal" }"
                            '>
	                            <option value="normal">标准样式</option>
								<option value="cfyl">春风杨柳</option>
								<option value="hbls">环保绿色</option>
								<option value="jqsy">金秋十月</option>	
								<option value="jsnz">金色农庄</option>
								<option value="lhqh">蓝灰情怀</option>
								<option value="rose">浪漫玫瑰</option>
								<option value="shys">深红夜思</option>
                              
                           	</select>
			            </td>
					    <td></td>
					    <td></td>    
					</tr>
					
				</table>
			</div>
			</g:if>
			
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"个人概况  <span style=\"color:red;margin-left:5px\">(必填信息)</span>",toggleable:false,moreText:"",height:"345px",marginBottom:"2px",
				href:"${createLink(controller:'staff',action:'getPersonInfor',id:personInfor?.id,params:[departId:departId])}"
			'>
			</div>
			
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"通讯方式 <span style=\"color:red;margin-left:5px\">(必填信息)</span>",toggleable:false,moreText:"",height:"150px",marginBottom:"2px",
				href:"${createLink(controller:'staff',action:'getContactInfor',id:personInfor?.id)}"'>
			</div>

			<div data-dojo-type="rosten/widget/TitlePane" 
				data-dojo-props='"class":"rostenFamilyTitleGrid",title:"家庭成员",toggleable:true,_moreClick:staff_addFamily,moreText:"<span style=\"color:#108ac6\">增加成员</span>",marginBottom:"2px"'>
				
				<div data-dojo-type="rosten/widget/RostenGrid" id="staffFamilyGrid" data-dojo-id="staffFamilyGrid"
					data-dojo-props='showPageControl:false,url:"${createLink(controller:'staff',action:'getFamily',id:personInfor?.id)}"'></div>
			
			</div>
			
			<div data-dojo-type="rosten/widget/TitlePane" 
				data-dojo-props='"class":"rostenDegreeTitleGrid",title:"学历学位",toggleable:true,_moreClick:staff_addDegree,moreText:"<span style=\"color:#108ac6\">增加学历学位</span>",marginBottom:"2px"'>
				
				<div data-dojo-type="rosten/widget/RostenGrid" id="degreeGrid" data-dojo-id="degreeGrid"
					data-dojo-props='showPageControl:false,url:"${createLink(controller:'staff',action:'getDegree',id:personInfor?.id)}"'></div>
				
			</div>
			
			<div data-dojo-type="rosten/widget/TitlePane" 
				data-dojo-props='"class":"rostenWorkResumeTitleGrid",title:"工作经历",toggleable:true,_moreClick:staff_addWorkResume,moreText:"<span style=\"color:#108ac6\">增加工作经历</span>",marginBottom:"2px"'>
				
				<div data-dojo-type="rosten/widget/RostenGrid" id="workResumeGrid" data-dojo-id="workResumeGrid"
					data-dojo-props='showPageControl:false,url:"${createLink(controller:'staff',action:'getWorkResume',id:personInfor?.id)}"'></div>
				
			</div>
		</form>
        </div>
        
        <g:if test='${"staffAdd".equals(type)}'>
        	<div data-dojo-type="dijit/layout/ContentPane" title="面试结果" data-dojo-props='' data-dojo-id="msResultContentPane">
	        	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"面试结果",toggleable:false,moreText:"",height:"480px",marginBottom:"2px"'>
	        		<table border="0" width="770" align="left">
							<tr>
							    <td width="70"><div align="right"><span style="color:red">*&nbsp;</span>面试结果：</div></td>
						  		<td>
							  		<textarea id="msResult" data-dojo-type="dijit/form/SimpleTextarea" 
										data-dojo-props='name:"msResult",
						                    style:{width:"680px",height:"470px"},
						                    trim:true,value:"${personInfor?.msResult}"
						            '>
									</textarea>
						  			
							    </td>
							</tr>
						</table>
	        	
				</div>
			</div>
			<g:if test="${personInfor?.id}">
				<div data-dojo-type="dijit/layout/ContentPane"  data-dojo-id="barginContentPane" class="rosten_form" title="合同信息" data-dojo-props='refreshOnShow:true,
					href:"${createLink(controller:'staff',action:'getBargainAllInfor',id:personInfor?.id,params:[type:type,userId:loginUser?.id])}"
				'>
				</div>
				
				<div data-dojo-type="dijit/layout/ContentPane" id="flowComment" title="流转意见" data-dojo-props='refreshOnShow:true,
					href:"${createLink(controller:'share',action:'getCommentLog',id:personInfor?.id)}"
				'>	
				</div>
				<div data-dojo-type="dijit/layout/ContentPane" id="flowLog" title="流程跟踪" data-dojo-props='refreshOnShow:true,
					href:"${createLink(controller:'share',action:'getFlowLog',id:personInfor?.id,params:[processDefinitionId:personInfor?.processDefinitionId,taskId:personInfor?.taskId])}"
				'>	
				</div>
			</g:if>
		</g:if>
		<g:else>
				<div data-dojo-type="dijit/layout/ContentPane"  data-dojo-id="barginContentPane" class="rosten_form" title="合同信息" data-dojo-props='refreshOnShow:true,
					href:"${createLink(controller:'staff',action:'getBargainAllInfor',id:personInfor?.id,params:[type:type,userId:loginUser?.id])}"
				'>
				
				<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"附件信息",toggleable:false,moreText:"",
					height:"60px"'>
					
				</div>
			</div>
			<div data-dojo-type="dijit/layout/ContentPane" class="rosten_form" title="劳资福利" data-dojo-props=''>
			
			</div>
		</g:else>
	</div>
	
</body>
</html>