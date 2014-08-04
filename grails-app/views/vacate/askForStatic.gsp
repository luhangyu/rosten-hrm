<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>请假申请</title>
    <style type="text/css">
    	.static table.tab_css {
			font-size:11px;
			color:#333333;
			border-width: 1px;
			border-color: #b5bcc7;
			border-collapse: collapse;
		}
		.static table.tab_css th {
			border-width: 1px;
			padding: 4px;
			border-style: solid;
			border-color: #b5bcc7;
			font-weight:blod;
			height:20px;
			line-height:20px;
			color:black;
			/*background-color: #cad9ea;*/
		}
		.static table.tab_css td{
			border-width: 1px;
			padding: 4px;
			border-style: solid;
			border-color: #b5bcc7;
			height:20px;
			line-height:20px;
			/*background-color: #ffffff;*/
		}
   	</style>
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
<div data-dojo-type="dijit/layout/ContentPane" data-dojo-props='style:{padding:"1px"}' class="static">
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