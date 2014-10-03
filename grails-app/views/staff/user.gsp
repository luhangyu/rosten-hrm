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
		.rosten .rostenTitleGrid .dijitTitlePaneContentInner{
			padding:2px 1px 1px 1px;
			height:160px;
		}
    </style>
	<script type="text/javascript">
	require(["dojo/parser",
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
		     	"dijit/form/Button",
		     	"dijit/Dialog",
				"dojox/grid/DataGrid",
		     	"dijit/form/RadioButton",
		     	"dijit/form/FilteringSelect",
		     	"dijit/form/ComboBox",
		     	"rosten/app/SystemApplication",
		     	"rosten/app/StaffApplication"],
			function(parser,kernel,registry,ActionBar){
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
			user_submit = function(object){
				
			};
			page_quit = function(){
				if(window.opener.rosten.kernel.navigationEntity!="userManage"){
					window.opener.rosten.kernel.refreshGrid();
				}else{
					window.opener.dom_rostenGrid.refresh();
				}
		        window.close();
			};
	});
    </script>
</head>
<body>
	<div class="rosten_action">
		<div data-dojo-type="rosten/widget/ActionBar" id="rosten_actionBar" 
			data-dojo-props='actionBarSrc:"${createLink(controller:'staffAction',action:'staffForm',id:personInfor?.id,params:[userId:loginUser?.id,type:type])}"'></div>
	</div>
	
	
	<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
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
			
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"个人概况  <span style=\"color:red;margin-left:5px\">(必填信息)</span>",toggleable:false,moreText:"",height:"315px",marginBottom:"2px",
				href:"${createLink(controller:'staff',action:'getPersonInfor',id:personInfor?.id,params:[departId:departId])}"
			'>
			</div>
			
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"通讯方式 <span style=\"color:red;margin-left:5px\">(必填信息)</span>",toggleable:false,moreText:"",height:"150px",marginBottom:"2px",
				href:"${createLink(controller:'staff',action:'getContactInfor',id:personInfor?.id)}"'>
			</div>

			<div data-dojo-type="rosten/widget/TitlePane" 
				data-dojo-props='"class":"rostenTitleGrid",title:"家庭成员",toggleable:true,_moreClick:staff_addFamily,moreText:"<span style=\"color:#108ac6\">增加成员</span>",marginBottom:"2px"'>
				<div data-dojo-type="rosten/widget/RostenGrid" id="staffFamilyGrid" data-dojo-id="staffFamilyGrid"
					data-dojo-props='showPageControl:false,url:"${createLink(controller:'staff',action:'getFamily',id:personInfor?.id)}"'></div>
			</div>
			
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"学历学位",toggleable:false,moreText:"",height:"100px",marginBottom:"2px",
				href:"${createLink(controller:'staff',action:'getDegree',id:user?.id)}"'>
			</div>
			
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"工作经历",toggleable:false,moreText:"",height:"260px",marginBottom:"2px",
				href:"${createLink(controller:'staff',action:'getWorkResume',id:user?.id)}"'>
			</div>
		</form>
        </div>
        
        <g:if test='${"staffAdd".equals(type)}'>
        	<div data-dojo-type="dijit/layout/ContentPane" title="面试结果" data-dojo-props=''>
	        	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"面试结果",toggleable:false,moreText:"",height:"480px",marginBottom:"2px"'>
	        		<table border="0" width="770" align="left">
							<tr>
							    <td width="70"><div align="right"><span style="color:red">*&nbsp;</span>面试结果：</div></td>
						  		<td>
							  		<textarea id="msResult" data-dojo-type="dijit/form/SimpleTextarea" 
										data-dojo-props='name:"msResult",
						                    style:{width:"680px",height:"470px"},
						                    trim:true,value:"${}"
						            '>
									</textarea>
						  			
							    </td>
							</tr>
						</table>
	        	
				</div>
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
		<g:else>
			<div data-dojo-type="dijit/layout/ContentPane" title="合同信息" data-dojo-props=''>
		
			</div>
			<div data-dojo-type="dijit/layout/ContentPane" title="劳资福利" data-dojo-props=''>
			
			</div>
		</g:else>
	</div>
	
	
</body>
</html>