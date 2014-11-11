<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>个人资料</title>
	<script type="text/javascript">

		require([
				"dijit/registry",
				"dojo/dom-style",
				"dojo/dom",
				"dojox/validate/web",
				"dijit/form/SimpleTextarea",
		 		"dijit/form/ValidationTextBox",
		 		"dijit/form/RadioButton",
		 		"rosten/widget/ActionBar"
		     	],function(registry,domStyle,dom,validate){
	     	
			userSimple_save = function(){
				var isOnMail = registry.byId("isOnMail1").attr("value");
				if(isOnMail=="true" || isOnMail==true){
					var loginName = registry.byId("loginName");
					if(!validate.isEmailAddress(loginName.attr("value"))){
						rosten.alert("登录名不正确！").queryDlgClose = function(){
							loginName.focus();
						};
						return;
					}
					var loginPassword = registry.byId("loginPassword");
					if(!loginPassword.isValid()){
						rosten.alert("密码不正确！").queryDlgClose = function(){
							loginPassword.focus();
						};
						return;
					}
				}
				
				var content = {};
				rosten.readSync("${createLink(controller:'system',action:'userSimpleSave')}",content,function(data){
					if(data.result==true || data.result =="true"){
						rosten.alert("保存成功");
					}else{
						rosten.alert("保存失败！");
					}	
				},function(error){
					rosten.alert("系统错误，请通知管理员！");
				},"userSimple_form");
			};
			userSimple_checkIsOnMail = function(args){
				if(args){
					domStyle.set(dom.byId("isOnMailDiv"),"display","");
				}else{
					domStyle.set(dom.byId("isOnMailDiv"),"display","none");
				}
			}

		});	

		
    </script>
</head>
<body>
<div class="rosten_action">
	<div data-dojo-type="rosten/widget/ActionBar" 
		data-dojo-props='actionBarSrc:"${createLink(controller:'staffAction',action:'userSimpleView')}"'>
	</div>
</div>
<div style="text-Align:center">
	
<g:form id="userSimple_form" name="userSimple_form" onsubmit="return false;" class="rosten_form" style="text-align:left;margin:10px">
	<fieldset class="fieldset-form">
	<legend class="tableHeader">个人资料</legend>
		<input id="id" data-dojo-type="dijit/form/ValidationTextBox"  data-dojo-props='name:"id",style:{display:"none"},value:"${user?.id }"' />
        <input id="companyId" data-dojo-type="dijit/form/ValidationTextBox"  data-dojo-props='name:"companyId",style:{display:"none"},value:"${companyId }"' />
		<table border="0" align="left" style="margin:0 auto;width:740px">
			<tr>
			    <td width="130"><div align="right"><span style="color:red">*&nbsp;</span>用户名：</div></td>
			    <td width="240">
			    	<input id="username" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='readOnly:true,"class":"input",trim:true,disabled:true,
							value:"${user?.username}"
	                '/>
			    </td>
			    <td width="130"><div align="right"><span style="color:red">*&nbsp;</span>所属部门：</div></td>
			    <td width="240">
			    	<input data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='"class":"input",readOnly:true,disabled:true,
							value:"${user?.getDepartName()}"
	                '/>
	           </td>
			</tr>
			<tr>
			    <td><div align="right">中文名称：</div></td>
			    <td>
	                <input id="chinaName" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"chinaName",trim:true,"class":"input",
							value:"${user?.chinaName}"
	                '/>
			    </td>
			    <td><div align="right">具有角色：</div></td>
			    <td>
			    	<input id="allowrolesName" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='readOnly:true,"class":"input",disabled:true,
							value:"${allowrolesName}"
	                '/>
	           </td>
			</tr>
			<tr>
			    <td><div align="right">是否管理员：</div></td>
			    <td>
			    	<input id="admin1" data-dojo-type="dijit/form/RadioButton"
	               		data-dojo-props='type:"radio",readOnly:true,disabled:true,
	               			<g:if test="${user?.sysFlag }">checked:true,</g:if>
								value:"true"
	                  '/>
						<label for="admin1">是</label>
						
                      <input id="admin2" data-dojo-type="dijit/form/RadioButton"
                   		data-dojo-props='readOnly:true,type:"radio",disabled:true,
                   			<g:if test="${!user?.sysFlag }">checked:true,</g:if>
    							value:"false"
                      '/>
					<label for="admin2">否</label>
			    </td>
			    <td><div align="right">手机号码：</div></td>
			    <td>
					<input id="telephone" data-dojo-type="dijit/form/ValidationTextBox" 
	                   	data-dojo-props='name:"telephone","class":"input",trim:true,
	                   		value:"${user?.telephone}"
	                '/>			    	
			    </td>
			</tr>
			<tr>
			    <td><div align="right">邮箱地址：</div></td>
			    <td>
			    	<input id="email" data-dojo-type="dijit/form/ValidationTextBox" 
                    	data-dojo-props='name:"email","class":"input",trim:true,
  							value:"${user?.email}"
                    '/>
			    </td>
			    <td><div align="right">是否开启外网邮箱：</div></td>
			    <td>
			    	<input id="isOnMail1" data-dojo-type="dijit/form/RadioButton"
	               		data-dojo-props='name:"isOnMail",type:"radio",
	               			<g:if test="${user?.isOnMail }">checked:true,</g:if>
								value:"true",onClick:function(){userSimple_checkIsOnMail(true)}
	                  '/>
						<label for="isOnMail1">是</label>
						
                      <input id="isOnMail2" data-dojo-type="dijit/form/RadioButton"
                   		data-dojo-props='name:"isOnMail",type:"radio",
                   			<g:if test="${!user?.isOnMail }">checked:true,</g:if>
    							value:"false",onClick:function(){userSimple_checkIsOnMail(false)}
                      '/>
					<label for="isOnMail2">否</label>
			    </td>
			</tr>
			<tr>
			    <td><div align="right">联系地址：</div></td>
			    <td colspan=3>
					<input id="address" data-dojo-type="dijit/form/ValidationTextBox" 
                    	data-dojo-props='name:"address","class":"input",trim:true,
                    		style:{width:"568px"},
  							value:"${user?.address}"
                    '/>			    	
			    </td>
			</tr>
			<tr>
			    <td><div align="right">内容描述：</div></td>
			    <td colspan=3>
					<textarea id="description" data-dojo-type="dijit/form/SimpleTextarea"
                		data-dojo-props='name:"description","class":"input",
                   		style:{width:"565px"},
                   		trim:true,value:"${user?.description}"
                   '></textarea>		    	
			    </td>
			</tr>
		</table>
		</fieldset>
	
	<fieldset class="fieldset-form" id="isOnMailDiv" <g:if test="${!user?.isOnMail }">style="display:none"</g:if>>
		<legend class="tableHeader">绑定的外网邮箱</legend>
		<table border="0" align="left" style="margin:0 auto;width:740px">
			<tr>
			    <td width="130"><div align="right"><span style="color:red">*&nbsp;</span>登录名：</div></td>
			    <td width="240">
	                <input id="loginName" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"loginName",trim:true,"class":"input",required:true,
							value:"${emailConfig?emailConfig.loginName:user?.email}",
							invalidMessage:"登录名地址是非法的(格式为:example@rostensoft.com)",
							promptMessage:"登录名格式为:example@rostensoft.com",
							missingMessage:"登录名格式为:example@rostensoft.com"
	                '/>
			    </td>
			    <td width="130"><div align="right"><span style="color:red">*&nbsp;</span>密码：</div></td>
			    <td width="240">
			    	<input id="loginPassword" data-dojo-type="dijit/form/ValidationTextBox" 
	                   	data-dojo-props='name:"loginPassword",
	                   		"class":"input",
	                   		type:"password",
	                   		trim:true,
	                   		required:true,
	 						value:"${emailConfig?.loginPassword}"
	                '/>
	           </td>
			</tr>
		</table>
	</fieldset>	
</g:form>
</div>
</body>
</html>