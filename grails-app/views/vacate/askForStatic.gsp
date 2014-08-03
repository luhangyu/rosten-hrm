<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>请假申请</title>
	<script type="text/javascript">
		require(["dijit/registry",
		     "rosten/widget/ActionBar"],
			function(registry){
				vacate_print = function(){
					
				};
		});
    </script>
</head>
<body>
<div class="rosten_action">
	<div data-dojo-type="rosten/widget/ActionBar" data-dojo-id="rosten_actionBar" 
		data-dojo-props='actionBarSrc:"${createLink(controller:'vacateAction',action:'askForStatic',id:vacate?.id,params:[userid:user?.id])}"'>
	</div>
</div>
<div data-dojo-type="dijit/layout/ContentPane" data-dojo-props=''>
	<table width="100%" class="tab_css">
		<THEAD> 
			<tr>
				<th >部门</th>
				<th >姓名</th>
				<th>事假(天)</th>
				<th>病假(天)</th>
				<th>年休假(天)</th>
				<th>婚假(天)</th>
				<th>丧假(天)</th>
				<th>其他(天)</th>
			</tr>
		</THEAD>
		<tbody >
			<g:each in="${tableItem}">
				<tr>
					<td>${it.departName }</td>
					<td>${it.name }</td>
					<td>${it.sjnums }</td>
					<td>${it.bjnums }</td>
					<td>${it.nxjnums }</td>
					<td>${it.hjnums }</td>
					<td>${it.sajnums }</td>
					<td>${it.qtjnums }</td>
				</tr>
			</g:each>
		</tbody>
		
	</table>
</div>
						
						
</body>