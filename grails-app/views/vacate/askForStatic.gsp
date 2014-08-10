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
		.charts {
			clear: both;
		}
		.chart-area-pie {
			/*border: 1px solid #ccc;*/
	        height: 240px;
	        width:520px;
	        margin:0 auto;
		}
		.chart-pie {
			width:520px;
			height: 230px;
		}
		
   	</style>
	<script type="text/javascript">
		require(["dijit/registry",
		         "dojo/json",
		         "rosten/app/Application",
		     "rosten/widget/ActionBar"],
			function(registry,JSON){
				vacate_print = function(){
					
				};
				
				rosten.variable.tempAskFor = JSON.parse('${json}');
				rosten.variable.titleName = '${titleName}';
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

<table border="0" align="left" style="margin:0 auto;width:740px">
				<tr>
				    <td width="120"><div align="right">选择部门：</div></td>
				    <td width="260">
		                <input id="departName" data-dojo-type="dijit/form/ValidationTextBox" 
			               	data-dojo-props='name:"departName",readOnly:true,
			               		trim:true,required:true,value:"${departName}"
			          	'/>
			         	<input id="departId" data-dojo-type="dijit/form/ValidationTextBox"  data-dojo-props='style:{display:"none"},trim:true,required:true' />
						<button data-dojo-type="dijit/form/Button" 
							data-dojo-props='onClick:function(){
								selecWorktDepart("${createLink(controller:'system',action:'departTreeDataStore',params:[companyId:companyId])}",false)}'>选择</button>
		           </td>
		           <td width="120"><div align="right"></div></td>
				   <td width="250">
				    	
		           </td>
				</tr>
			</table>

	<div class="charts">
		<div id="askFor_pie_legend"></div>
		<div class="chart-area-pie">
			<div id="askFor_pie" class="chart-pie"></div>
		</div>
	</div>
	
	<table width="100%" class="tab_css">
		<THEAD> 
			<tr class="tableBackGround">
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