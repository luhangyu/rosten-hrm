<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>图表</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="layout" content="rosten" />
<style type="text/css">
		body{
			overflow:auto;
		}
    	table.tab_css {
			font-size:11px;
			color:#333333;
			border-width: 1px;
			border-color: #b5bcc7;
			border-collapse: collapse;
		}
		table.tab_css th {
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
		table.tab_css td{
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
require(["dojo/parser",
	 		"dojo/_base/kernel",
	 		"dijit/registry",
	 		"dojo/_base/xhr",
	 		
	 		
	     	"rosten/widget/ActionBar",
	     	"rosten/app/Application",
	     	"rosten/kernel/behavior"],
		function(parser,kernel,registry,xhr){
			kernel.addOnLoad(function(){
				rosten.init({webpath:"${request.getContextPath()}"});
				rosten.cssinit();
			});



		
		page_quit = function(){
			window.close();
		};
	
	});
	
</script>
</head>
<body>
	<div class="rosten_action">
		<div data-dojo-type="rosten/widget/ActionBar" id="rosten_actionBar" data-dojo-props='actionBarSrc:"${createLink(controller:'statistics',action:'staticShow')}"'></div>
	</div>
	<div data-dojo-type="dijit/layout/ContentPane" data-dojo-props='style:{padding:"1px"}' class="static">
	<table width="800px" class="tab_css simpleNavigation">
		<THEAD> 
		
			<tr>
				<td rowspan="2">所属部门</td>
				
				
				<td colspan="12" align="center">用工性质</td>
				</tr>
				<tr>
				<th>省局</th>
				<th >协会聘用</th>
				<th>劳务派遣</th>
				<th>返聘</th>
				<th>借用</th>
				<th>局派专职</th>
				<th>局派兼职</th>
				<th>局合同工</th>
				<th>挂靠</th>
				<th>实习</th>
				<th>局临时工</th>
				<th>小计</th>
			</tr>
		</THEAD>
		<tbody >
			<g:each in="${tableItem}">
				<tr>
					<td>${it.departName }</td>
					<td>${it.sj }</td>
					<td>${it.xhpy }</td>
					<td>${it.lwpq }</td>
					<td>${it.fp }</td>
					<td>${it.jy }</td>
					<td>${it.jpzz }</td>
					<td>${it.jpjz }</td>
					<td>${it.jhtg }</td>
					<td>${it.gk }</td>
					<td>${it.sx }</td>
					<td>${it.jlsg }</td>
					<td>${it.hjrs }</td>
				</tr>
			</g:each>
		</tbody>
		
	</table>
	</div>
</body>
</html>
