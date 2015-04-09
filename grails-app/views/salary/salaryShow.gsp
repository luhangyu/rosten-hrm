<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>工资信息表</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="layout" content="rosten" />
<style type="text/css">
		body{
			overflow:auto;
		}
		
		table thead tr , table thead tr td,table thead tr th{
			background-color: #dedede;
			text-align:center;
			font-weight:blod;
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
	<div data-dojo-type="dijit/layout/ContentPane" data-dojo-props='style:{padding:"1px",width:"80%",margin:"5px auto"}' class="static" >
	<table width="100%" class="tab_css simpleNavigation" style="text-align:center">
		<THEAD> 
		
			<tr>
				<th>年度</th>
				<th>月份</th>
				<th>岗位工资</th>
				<th>绩效工资</th>
				<th>工龄补贴</th>
				<th>工资小计</th>
				<th>租房补贴</th>
				<!--<th>考核奖</th>  -->
				<th>应发</th>
				<th>个税</th>
				<th>公积金</th>
				<th>失业</th>
				<th>养老</th>
				<th>医疗</th>
				<th>重补</th>
				<th>社保小计</th>
				<th>实发</th>
			</tr>
		</THEAD>
		<tbody >
			<g:each in="${tableItem}">
				<tr>
					<td>${it.year }</td>
					<td>${it.month }</td>
					<td>${it.ygwgz }</td>
					<td>${it.yjxgz }</td>
					<td>${it.glbt }</td>
					<td>${it.gzxj }</td>
					<td>${it.zfbt }</td>
					<td>${it.yfje }</td>
					<td>${it.grss }</td>
					<td>${it.gjj }</td>
					<td>${it.sybx }</td>
					<td>${it.ylaobx }</td>
					<td>${it.ylbx }</td>
					<td>${it.cb }</td>
					<td>${it.wxyjxj }</td>
					<td>${it.sfje }</td>
				</tr>
			</g:each>
		</tbody>
		
	</table>
	</div>
</body>
</html>
