<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>人员信息</title>
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
		<div style="text-Align:center">
        <form class="rosten_form" id="rosten_form" onsubmit="return false;" style="text-align:left;">
			
        	<input id="id" data-dojo-type="dijit/form/ValidationTextBox" data-dojo-props='name:"id",value:"${user?.id }",style:{display:"none"}'
        	></input>
            <fieldset class="fieldset-form">
                <legend class="tableHeader">人员配置</legend>
                <table class="tableData">
                    <tbody>
                        <tr>
                            <td width="100">
                                <div align="right" >
                                    <span style="color:red">*&nbsp;</span>用户名：
                                </div>
                            </td>
                            <td>
                            	<g:if test='${userType.equals("admin") }'>
                            		<input id="userNameFront" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"userNameFront","class":"input",style:{width:"50px"},
              							value:"${company?.shortName}-",disabled:true
                                	'/>
                                	
                                	<input id="username" data-dojo-type="dijit/form/ValidationTextBox" 
	                                	data-dojo-props='name:"username",${fieldAcl.isReadOnly("username")},
	                                		"class":"input",
	                                		trim:true,
	                                		required:true,
	                                		promptMessage:"请正确输入用户名...",
	                                		style:{width:"140px"},
	                                		<g:if test="${username && !"".equals(username)}">disabled:true,</g:if>
	              							value:"${username}"
	                                '/>
                            	</g:if>
                            	<g:else>
                            		<input id="username" data-dojo-type="dijit/form/ValidationTextBox" 
	                                	data-dojo-props='name:"username",${fieldAcl.isReadOnly("username")},
	                                		"class":"input",
	                                		trim:true,
	                                		required:true,
	                                		promptMessage:"请正确输入用户名...",
	              							value:"${user?.username}"
	                                '/>
                            	</g:else>
                            	
                            </td>
                        </tr>
                        <tr>
                            <td width="100">
                                <div align="right" >中文名：</div>
                            </td>
                            <td>
                            	<input id="chinaName" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"chinaName",${fieldAcl.isReadOnly("chinaName")},
                                		"class":"input",
                                		trim:true,
              							value:"${user?.chinaName}"
                                '/>
                            </td>
                        </tr>
                        <g:if test='${userType.equals("admin") }'>
	                        <tr>
		                        <td>
		                        	 <div align="right"><span style="color:red">*&nbsp;</span>用户类型：</div>
		                        </td>
		                        <td>
		                        	<select id="userTypeName" data-dojo-type="dijit/form/ComboBox"
	                             		data-dojo-props='name:"userTypeName",
	                             			autoComplete:true,
	                             			style:{fontSize:"14px",width:"194px"},
	                             			${fieldAcl.isReadOnly("userTypeName")},
	              							value:"${user?.userTypeEntity?.typeName }"
	                                '>
	                                <g:each in="${userTypeList}" var="item">
	                                	<option value="${item.id }">${item.typeName }</option>
	                                </g:each>	
	                                
	                                </select>
									
		                        </td>
		                    </tr>
                        </g:if>
						<tr>
                            <td>
                                <div align="right">
                                    <span style="color:red">*&nbsp;</span>用户密码：
                                </div>
                            </td>
                            <td>
                            	<input id="password" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"password",${fieldAcl.isReadOnly("password")},
                                		"class":"input",
                                		type:"password",
                                		trim:true,
                                		required:true,
                                		promptMessage:"请正确输入密码...",
              							value:"${user?.password}"
                                '/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div align="right">
                                    <span style="color:red">*&nbsp;</span>密码确认：
                                </div>
                            </td>
                            <td>
                            	<input id="passwordcheck" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"passwordcheck",${fieldAcl.isReadOnly("password")},
                                		"class":"input",
                                		type:"password",
                                		trim:true,
                                		required:true,
                                		promptMessage:"请正确输入密码...",
              							value:"${user?.password}"
                                '/>
                            </td>
                        </tr>
                        <g:if test='${userType.equals("super") }'>
	                        <tr>
	                           <td>
	                                <div align="right" >
	                                    <span style="color:red">*&nbsp;</span>所属机构：
	                                </div>
	                            </td>
	                            <td>
	                            	<input id="companyName" data-dojo-type="dijit/form/ValidationTextBox" 
	                                	data-dojo-props='name:"companyName",${fieldAcl.isReadOnly("companyName")},
	                                		"class":"input",
	                                		trim:true,
	                                		required:true,
	                                		disabled:true,
	              							value:"${user?.company?.companyName}"
	                                '/>
	                                <input id="companyId" data-dojo-type="dijit/form/ValidationTextBox" data-dojo-props='name:"companyId",value:"${user?.company?.id }",style:{display:"none"}'/>
									<button data-dojo-type="dijit.form.Button" data-dojo-props='onClick:function(){selectCompany()}'>选择</button>
	                            </td>
	                        </tr>
                        </g:if>
                        <g:if test='${userType.equals("admin") }'>
                        	<tr>
	                           <td>
	                                <div align="right"><span style="color:red">*&nbsp;</span>所属部门：</div>
	                            </td>
	                            <td>
	                            	<input id="allowdepartsName" data-dojo-type="dijit/form/ValidationTextBox" 
	                                	data-dojo-props='name:"allowdepartsName",${fieldAcl.isReadOnly("allowdepartsName")},
	                                		"class":"input",
	                                		trim:true,
	                                		required:true,
	                                		style:{width:"400px"},
	              							value:"${user?.getDepartName()}"
	                                '/>
	                                <g:hiddenField name="allowdepartsId" value="${user?.getDepartEntity()?.id }" />
									<button data-dojo-type="dijit.form.Button" data-dojo-props='onClick:function(){selectDepart("${createLink(controller:'system',action:'departTreeDataStore',params:[companyId:company?.id])}")}'>选择</button>
	                            </td>
	                        </tr>
							<tr>
	                           <td>
	                                <div align="right">具有角色：</div>
	                            </td>
	                            <td>
	                            	<input id="allowrolesName" data-dojo-type="dijit/form/ValidationTextBox"
	                   					data-dojo-props='"class":"input",
	                   						trim:true,
	                   						style:{width:"400px"},
	                   						${fieldAcl.isReadOnly("allowrolesName")},
	                   						value:"${allowrolesName }"
	                   				'/>
	                   				<g:hiddenField name="allowrolesId" value="${allowrolesId }" />
									<button data-dojo-type="dijit.form.Button" 
										data-dojo-props = 'onClick:function(){selectRole("${createLink(controller:'system',action:'roleSelect',params:[companyId:company?.id])}")}'
									>选择</button>
								
	                            </td>
	                        </tr>
                        </g:if>
                        <g:if test='${userType.equals("super") }'>
	                        <tr>
		                        <td>
		                        	 <div align="right"><span style="color:red">*&nbsp;</span>是否管理员：</div>
		                        </td>
		                        <td>
		                        	<input id="admin1" data-dojo-type="dijit/form/RadioButton"
	                             		data-dojo-props='name:"sysFlag",
	                             			type:"radio",
	                             			${fieldAcl.isReadOnly("sysFlag")},
	                             			<g:if test="${!user?.sysFlag || user.sysFlag==true }">checked:true,</g:if>
	              							value:"true"
	                                '/>
									<label for="admin1">是</label>
									
	                                <input id="admin2" data-dojo-type="dijit/form/RadioButton"
	                             		data-dojo-props='name:"sysFlag",
	                             			type:"radio",
	                             			${fieldAcl.isReadOnly("sysFlag")},
	                             			<g:if test="${user?.sysFlag && user.sysFlag==false }">checked:true,</g:if>
	              							value:"false"
	                                '/>
									<label for="admin2">否</label>
									
		                        </td>
		                    </tr>
	                    </g:if>
	                    <tr>
	                        <td>
	                        	 <div align="right"><span style="color:red">*&nbsp;</span>CSS样式表：</div>
	                        </td>
	                        <td>
	                        	<select id="cssStyle" data-dojo-type="dijit/form/FilteringSelect"
                             		data-dojo-props='name:"cssStyle",
                             			style:{fontSize:"14px",width:"194px"},
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
	                    </tr>
	                    <tr>
                           <td>
                                <div align="right" >手机号码：</div>
                            </td>
                            <td>
                            	<input id="telephone" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"telephone",${fieldAcl.isReadOnly("telephone")},
                                		"class":"input",
                                		trim:true,
              							value:"${user?.telephone}"
                                '/>
                            </td>
                        </tr>
                        <tr>
                           <td>
                                <div align="right" >身份证号码：</div>
                            </td>
                            <td>
                            	<input id="idCard" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"idCard",${fieldAcl.isReadOnly("idCard")},
                                		"class":"input",
                                		trim:true,
              							value:"${user?.idCard}"
                                '/>
                            </td>
                        </tr>
                        <tr>
                           <td>
                                <div align="right" >邮箱地址：</div>
                            </td>
                            <td>
                            	<input id="email" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"email",${fieldAcl.isReadOnly("email")},
                                		"class":"input",
                                		trim:true,
              							value:"${user?.email}"
                                '/>
                            </td>
                        </tr>
                        <tr>
                           <td>
                                <div align="right" >联系地址：</div>
                            </td>
                            <td>
                            	<input id="address" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"address",${fieldAcl.isReadOnly("address")},
                                		"class":"input",
                                		trim:true,
                                		style:{width:"400px"},
              							value:"${user?.address}"
                                '/>
                            </td>
                        </tr>
                        <tr>
                        	<td>
                                <div align="right" >内容描述：</div>
                            </td>
                             <td colspan="3">
                             	<textarea id="description" data-dojo-type="dijit/form/SimpleTextarea"
                             		data-dojo-props='name:"description",${fieldAcl.isReadOnly("description")},
                                		"class":"input",
                                		style:{width:"400px",marginLeft:"1px"},
                                		trim:true,
              							value:"${user?.description}"
                                '></textarea>
    						</td>
                        </tr>
                    </tbody>
                </table>
            </fieldset>
	</form>
	</div>
</body>
</html>