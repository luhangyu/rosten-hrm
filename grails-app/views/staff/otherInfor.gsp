<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>其他信息</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
<style type="text/css">
	.personSearch table.tab_css {
		font-size:11px;
		color:#333333;
		border-width: 1px;
		border-color: #b5bcc7;
		border-collapse: collapse;
	}
	.personSearch table.tab_css th {
		border-width: 1px;
		padding: 4px;
		border-style: solid;
		border-color: #b5bcc7;
		/*background-color: #cad9ea;*/
	}
	.personSearch table.tab_css td{
		border-width: 1px;
		padding: 4px;
		border-style: solid;
		border-color: #b5bcc7;
	}
</style>

<body>
	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"岗位调动信息",toggleable:false,moreText:"",marginBottom:"2px"'>
	
		  <div class="personSearch">
			<table width="98%" class="tab_css">
				<THEAD> 
					<tr>
							<td>调出部门</td>
							<th>调入部门</th>
	   						<th>调入岗位</th>
	   						<th>调入编制</th>
	   						<td>调动类型</td>  
	   						<td>调动时间</td>  
	   					</tr>
	   					</THEAD>
	   				<tbody>	
					<g:each in="${departChangList}">
						<tr>
	   						<td>${it.getOutDepartName()}</td>
	   						<th>${it.getInDepartName()}</th>
	   						<th>${it.inDuty}</th>
	   						<th>${it.inPersonType}</th>
	   						<td>${it.changeType}</td>
	   						<td>${it.getShowChangeDate()}</td>
	   					</tr>
					</g:each>
				</tbody>
			</table>
			
		</div>
	</div>
	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"离(退)休信息",toggleable:false,moreText:"",marginBottom:"2px"'>
		
		  <div class="personSearch">
			<table width="98%" class="tab_css">
				<THEAD> 
					<tr>
							<th>申请类型</th>
	   						<td>申请时间</td>
	   					</tr>
	   					</THEAD>
	   					<tbody>
					<g:each in="${statusChangList}">
						<tr>
							<th>${it.changeType}</th>
	   						<td>${it.getFormattedChangeDate()}</td>
	   					</tr>
					</g:each>
				</tbody>
			</table>
			</div>
	</div>
	
	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"培训信息",toggleable:false,moreText:"",marginBottom:"2px"'>
		
	</div>
	
</body>
</html>
