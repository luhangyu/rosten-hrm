<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="layout" content="rosten" />
<title>公告栏</title>
<style type="text/css">
	body{
		overflow:auto;
	}
</style>
<script type="text/javascript">
	require(["dojo/parser",
	 		"dojo/_base/kernel",
	 		"dijit/form/Button",
	     	"rosten/app/Application"],
		function(parser,kernel){
			kernel.addOnLoad(function(){
				rosten.init({webpath:"${request.getContextPath()}"});
				rosten.cssinit();
			});
	});
</script>
</head>
<body>
	<div class="bbs">
		<div class="newspage">
		 	<div class="newspagebg">
				<div class="title">
					<h3>${bbs?.topic}</h3>
					<h4>
						<span>发布人：${bbs?.publisher?.username}</span> 
						<span>发布日期：${bbs?.getFormattedPublishDate()}</span> 
						<span>浏览人数：${bbs?.hasReaders?.size()}</span>
					</h4>
				</div>
	
				<div class="newcont">
					<p>${bbs?.content}</p>
				</div>
	
				<div class="download">
					<font color="black"> 附件： </font>
						<g:each in="${attachFiles}">
							<a href="${createLink(controller:'system',action:'downloadFile',id:it.id)}" style="color: blue;margin-right:20px" dealId="${it.id }"><b>${it.name }</b></a>
						</g:each>
					</span>
				</div>
	
				<div class="newsbar">
					<button data-dojo-type="dijit/form/Button" onclick="javascript:window.window.close();" name="关闭"
						class="button">关 闭</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>