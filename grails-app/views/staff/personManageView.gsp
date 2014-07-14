<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>员工列表</title>
    <script type="text/javascript">
    </script>
</head>
<body>
	<div data-dojo-type="rosten/widget/ActionBar" id="rosten_actionBar" 
		data-dojo-props='actionBarSrc:"${createLink(controller:'staffAction',action:'userView')}"'></div>
	
	<div data-dojo-type="rosten/widget/RostenGrid" id="rosten_rostenGrid" 
		data-dojo-props='url:"${createLink(controller:'staff',action:'userGrid',params:[departId:departId])}",showRowSelector:"new"'></div>
</body>
</html>