<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>合同管理</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
</style>

<body>
	<div style="margin-top:10px">
		<fieldset class="fieldset-form">
			<legend class="tableHeader">附件查看</legend>
			<div id="fileUpload_show" style="padding:5px;margin:8px;font-size:14px">
				<g:each in="${attachFiles}">
					<div style="height:30px;width:50%;float:left">
						<a href="${createLink(controller:'system',action:'downloadFile',id:it.id)}" style="margin-right:20px" dealId="${it.id }">${it.name }</a>
					</div>
				</g:each>
			</div>
		</fieldset>
	</div>
</body>
</html>
