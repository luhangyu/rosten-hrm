<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<link rel="shortcut icon" href="${resource(dir:'images/rosten/share',file:'rosten_logo_R.ico')}" type="image/x-icon" />
	<title><g:message code="springSecurity.login.title"/></title>
	<r:cssLoad dir="css/rosten" file="login.css" />
</head>

<body>
<div class="login_body">
	<div class="login_top"></div>
	<div class="login_main">
		<form action='${postUrl}' method='POST' id='loginForm' class='login-form' autocomplete='off'>
			<div class="login_table">
				<div class='login_message'> ${flash.message?flash.message:"&nbsp;"}</div>
				<g:if test='${flash.message}'>
					
				</g:if>
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<th>用户名</th>
						<td><input type='text' name='j_username' id='username' class="login_input"  title="用户名" ></td>
					</tr>
					<tr>
						<th>密　码</th>
						<td><input type='password'  name='j_password' id='password' class="login_pwd" title="密码" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type='submit' id="submit" value='${message(code: "springSecurity.login.button")}' class="login_but"  /></td>
					</tr>
				</table>
			</div>
		</form>
		<div  id="footer" style="text-align:center;color: #908964;font-family: Microsoft YaHei;margin-top: 39px">Copyright @2014 ; Rosten 杭州恒传信息技术有限公司
			版权所有,提供技术支持</div>
	</div>
</div>
<script type='text/javascript'>
	<!--
	(function() {
		document.forms['loginForm'].elements['j_username'].focus();
	})();
	// -->
</script>
</body>
</html>
