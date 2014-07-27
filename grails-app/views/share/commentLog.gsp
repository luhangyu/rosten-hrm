<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>CommentLog</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
</style>

<body>
	<div class="commentLog">
		<g:each in="${log}">
			<div class="title-nav">
				<h3>
					<span>${it.user.getFormattedName() + "【" + it.status + "】" }</span>
				</h3>
			</div>
	
			<div class="rz-list">
				<h3>${it.getFormattedCreatedDate() }</h3>
				<div class="rz-list-con">
					${it.content }
				</div>
				
			</div>
		</g:each>
		
	</div>
</body>
</html>
