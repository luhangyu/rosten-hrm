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
    </style>
	<script type="text/javascript">
	require(["dojo/parser",
		 		"dojo/_base/kernel",
		 		"dijit/registry",
		     	"rosten/widget/ActionBar",
		     	"dijit/form/TextBox",
		     	"dijit/form/RadioButton",
		     	"dijit/form/ValidationTextBox",
		     	"dijit/form/SimpleTextarea",
		     	"dijit/form/Button",
		     	"dijit/form/RadioButton",
		     	"dijit/form/FilteringSelect",
		     	"dijit/form/ComboBox",
		     	"rosten/app/SystemApplication"],
			function(parser,kernel,registry,ActionBar){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}"});
					rosten.cssinit();
				});
			user_add_check = function(){
				var username = registry.byId("username");
				if(!username.isValid()){
					rosten.alert("人员名称不正确！").queryDlgClose = function(){
						username.focus();
					};
					return false;
				}
				var password = registry.byId("password");
				if(!password.isValid()){
					rosten.alert("密码不正确！").queryDlgClose = function(){
						password.focus();
					};
					return false;
				}
				var passwordcheck = registry.byId("passwordcheck");
				if(!passwordcheck.isValid()){
					rosten.alert("确认密码不正确！").queryDlgClose = function(){
						passwordcheck.focus();
					};
					return false;
				}
				if(password.attr("value")!=passwordcheck.attr("value")){
					rosten.alert("密码不一致！").queryDlgClose = function(){
						password.attr("value","");
						passwordcheck.attr("value","");
						password.focus();
					};
					return false;
				}
				<g:if test='${userType.equals("super") }'>
					var company = registry.byId("companyName");
					if(company && !company.isValid()){
						rosten.alert("所属机构不正确！");
						return false;
					}
					return true
				</g:if>
				<g:elseif test='${userType.equals("admin")}'>
					var allowdepartsName = registry.byId("allowdepartsName");
					if(allowdepartsName && !allowdepartsName.isValid()){
						rosten.alert("所属部门不正确！");
						return false;
					}
					return true
				
				</g:elseif>
			}
			user_add = function(){
				if(user_add_check()==false) return;
				var content = {};
				<g:if test='${!userType.equals("super") }'>
					content.companyId = "${company?.id}";
					content.userNameFront = registry.byId("userNameFront").attr("value");
				</g:if>
				rosten.readSync(rosten.webPath + "/system/userSave",content,function(data){
					if(data.result=="true"){
						rosten.alert("保存成功！").queryDlgClose= function(){
							page_quit();	
						};
					}else if(data.result=="repeat"){
						rosten.alert("人员名称冲突，保存失败!");
					}else{
						rosten.alert("保存失败!");
					}
				},null,"rosten_form");
			}
	});
    </script>
</head>
<body>
	<div class="rosten_action">
		<div data-dojo-type="rosten/widget/ActionBar" id="rosten_actionBar" data-dojo-props='actionBarSrc:"${createLink(controller:'systemAction',action:'administratorForm',params:[userId:loginUser?.id])}"'></div>
	</div>
	<form class="rosten_form" id="rosten_form" onsubmit="return false;" style="text-align:left;">
	<div>
		<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${user?.id }"' />
        <input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
	</div>
	<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
        <div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props=''>
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"账号信息",toggleable:false,moreText:"",height:"100px",marginBottom:"2px"'>
				<table border="0" width="740" align="left">
					<tr>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>账号：</div></td>
					    <td width="250">
					    	<input id="userNameFront" data-dojo-type="dijit/form/ValidationTextBox" 
                               	data-dojo-props='name:"userNameFront",style:{width:"50px"},
             						value:"${company?.shortName}-",disabled:true
                            '/>
					    	<g:if test='${userType.equals("admin") }'>
					    		<input id="username" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"username",${fieldAcl.isReadOnly("username")},
                                		trim:true,
                                		required:true,
                                		promptMessage:"请正确输入账号...",
                                		style:{width:"100px"},
                                		<g:if test="${username && !"".equals(username)}">disabled:true,</g:if>
              							value:"${username}"
                                '/>
					    	</g:if>
					    	<g:else>
                           		<input id="username" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"username",${fieldAcl.isReadOnly("username")},
                                		trim:true,
                                		required:true,
                                		promptMessage:"请正确输入账号...",
              							value:"${user?.username}"
                                '/>
                           	</g:else>	
					    </td>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>具有角色：</div></td>
					    <td width="250">
					    	<input id="allowrolesName" data-dojo-type="dijit/form/ValidationTextBox"
                					data-dojo-props='trim:true,${fieldAcl.isReadOnly("allowrolesName")},
                						value:"${allowrolesName }"
                				'/>
                 				<g:if test='${userType.equals("admin") }'>
                 					<g:hiddenField name="allowrolesId" value="${allowrolesId }" />
									<button data-dojo-type="dijit.form.Button" 
										data-dojo-props = 'onClick:function(){selectRole("${createLink(controller:'system',action:'roleSelect',params:[companyId:company?.id])}")}'
									>选择</button>
                 				</g:if>
			           </td>
					</tr>
					<g:if test='${userType.equals("admin") }'>
						<tr>
						    <td><div align="right"><span style="color:red">*&nbsp;</span>密码：</div></td>
						    <td>
						    	<input id="password" data-dojo-type="dijit/form/ValidationTextBox" 
	                               	data-dojo-props='name:"password",${fieldAcl.isReadOnly("password")},
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
                                	data-dojo-props='name:"passwordcheck",${fieldAcl.isReadOnly("password")},
                                		type:"password",
                                		trim:true,
                                		required:true,
                                		promptMessage:"请正确输入密码...",
              							value:"${user?.password}"
                                '/>
				            </td>    
						</tr>
					
					</g:if>
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>CSS样式表：</div></td>
					    <td>
					    	<select id="cssStyle" data-dojo-type="dijit/form/FilteringSelect"
                           		data-dojo-props='name:"cssStyle",
                           			autoComplete:false,
                           			${fieldAcl.isReadOnly("cssStyle")},
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
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"个人概况",toggleable:false,moreText:"",height:"300px",marginBottom:"2px",
				href:"${createLink(controller:'staff',action:'getPersonInfor',id:user?.id)}"
			'>
			</div>
			
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"通讯方式",toggleable:false,moreText:"",height:"300px",marginBottom:"2px"'>
				通讯方式
			</div>
			
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"学历学位",toggleable:false,moreText:"",height:"300px",marginBottom:"2px"'>
				学历学位
			</div>
			
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"工作经历",toggleable:false,moreText:"",height:"300px",marginBottom:"2px"'>
				工作经历
			</div>
			
        </div>
		<div data-dojo-type="dijit/layout/ContentPane" title="合同信息" data-dojo-props=''>
		
	
		</div>
	</div>
	
	</form>
</body>
</html>