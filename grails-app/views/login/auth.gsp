<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<link rel="shortcut icon" href="${resource(dir:'images/rosten/share',file:'rosten_logo_R.ico')}" type="image/x-icon" />
	<title><g:message code="springSecurity.login.title"/></title>
	<style type='text/css' media='screen'>
		html {
			font-family: "Microsoft YaHei";
			background:none repeat scroll 0 0 #33B5E5;
		}
		.loginbody {
			text-align: center;
		}
		.login-screen {
			position: absolute;
			top: 40%;
			left: 45%;
			margin-top:-160px;
			margin-left: -245px;
			width: 555px;
			text-align: left;
		}
		.login-icon {
			/*float: left;
			width: 160px;*/
			height: 96px;
			text-indent:-9999px;
			background: url(${resource(dir:'images/rosten/share',file:'logo.png')}) no-repeat;
		}
		.login-form{
			float: left;
			margin-left: 30px;
			padding: 20px 25px 25px 25px;
			width:310px;
			background:#ECEFF1;
			border-radius: 6px;
			text-align: center;
			-webkit-box-shadow: 2px 2px 3px #aaa;
			-moz-box-shadow: 2px 2px 3px #aaa;
			filter: progid:DXImageTransform.Microsoft.Shadow(Strength=3, Direction=135, Color='#999999');
			box-shadow: 2px 2px 0 rgba(0, 0, 0, 0.1);
		}
		.login-form h1{
			display: block;
			margin-bottom: 20px;
			border-bottom: 1px solid #DFDFDF;
			text-align: left;
			height: 40px;
			line-height: 30px;
			color: #304b65;
			font-size: 21px;
			font-weight: 300;
			font-family: 'Microsoft YaHei';
			background: url(${resource(dir:'images/rosten/share',file:'login_tit.png')}) no-repeat 0 0;
			text-indent: -9999px;
		}
		.control-group{
			position: relative;
			margin-bottom: 6px;
		}
		.login-field{
			display: inline-block;
			padding: 11px 0 11px 10px;
			width: 296px;
			height: 20px;
			line-height: 20px;
			color: #ACB6C0;
			font-size: 17px;
			background: #fff;
			border: 1px solid #E5E5E5;
			border-radius: 6px 6px 6px 6px;
			margin-bottom: 10px;
			vertical-align: middle;
		}
		.login-field-icon{
			position: absolute;
			display: block;
			top: 14px;
			right: 13px;
			width: 16px;
			height: 20px;
			cursor: pointer;
			background:url(${resource(dir:'images/rosten/share',file:'skin_icons.png')}) no-repeat;
		}
		.login-field-icon.pwd{
			background-position: -28px -137px;
		}
		.login-field-icon.user{
			background-position: -1px -137px;
		}
		.btn-login{
			display: block;
			margin: 0;
			padding: 10px 0;
			width: 100%;
			color: #fff;
			border: 2px solid #ECEFF1;
			border-radius: 6px 6px 6px 6px;
			background: #33B5E5;
			font-size: 16.5px;
			text-decoration: none;
			cursor: pointer;
		}
		.login-tips{
			display: block;
			margin: 15px auto 0 auto;
			height: 14px;
		}
		.login-tips i{
			display: inline-block;
			margin-right: 5px;
			width: 14px;
			height: 14px;
			background: url(${resource(dir:'images/rosten/share',file:'skin_icons.png')}) -56px -140px no-repeat;
			text-indent: -9999px;
			vertical-align: middle;
		}
		.login-tips b{
			display: inline-block;
			height: 14px;
			line-height: 14px;
			font-size: 12px;
			font-weight: normal;
			color: #BFC9CA;
			vertical-align: middle;
		}
		.login-screen .arrow{
			position: absolute;
			display: block;
			left: 180px;
			top: 30px;
			width: 11px;
			height: 22px;
			text-indent: -9999px;
			background: url(${resource(dir:'images/rosten/share',file:'skin_icons.png')}) -80px -356px no-repeat;
		}
		.login_message{
			margin-bottom:15px;
			color: #c33;
		}
		.login-left {
			float: left;
			width: 160px;
		}
		.login-ewm{
			margin-top:5px;
			border:0;cursor:default;text-decoration:none;
			margin-left:6px;
		}
		.login-ewm img{
			width:150px;
			height:150px;
		}
	</style>
</head>

<body>
<div class='loginbody'>
		
	<div class="login-screen">
		<div class="login-left">
			<div class="login-icon">LOGO</div>
			<div class="login-ewm">
				<img src="../images/qrcode.png" alt="二维码" />
			</div>
		</div>
		
		<form action='${postUrl}' method='POST' id='loginForm' class='login-form' autocomplete='off'>
			<h1>系统管理登录</h1>
			<g:if test='${flash.message}'>
				<div class='login_message'> ${flash.message}</div>
			</g:if>
			<div class="control-group">
				<input type='text' name='j_username' id='username' class="login-field" placeholder="用户名" title="用户名" >
				
				<label class="login-field-icon user" for="username"></label>
			</div>
			<div class="control-group">
				<input type='password'  name='j_password' id='password' class="login-field" placeholder="密码" title="密码" />
				<label class="login-field-icon pwd" for="password"></label>
			</div>
			<div>
				<input type='submit' id="submit" value='${message(code: "springSecurity.login.button")}' class="btn-login"  />
			</div>
			<span class="login-tips"><i></i><b id="msgtip">请输入用户名和密码</b></span>
			<span class="login-tips"><b><span style="color:#000000">演示账号:client  密码:password</span></b></span>
			<i class="arrow">箭头</i>
		</form>
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
