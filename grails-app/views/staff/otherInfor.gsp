<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>其他信息</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
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
</head>
<body>
	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"岗位调动信息",toggleable:false,moreText:"",marginBottom:"2px"'>
		<g:if test="${departChangList && departChangList.size()>0}">
		  <div class="personSearch simpleNavigation">
			<table width="98%" class="tab_css">
				<THEAD> 
					<tr class="bgClass">
							<td>调出部门</td>
							<th>调入部门</th>
	   						<th>调入岗位</th>
	   						<th>调入类型</th>
	   						<th>调动理由</th>  
	   						<th>调动时间</th>  
	   					</tr>
	   					</THEAD>
	   				<tbody>	
					<g:each in="${departChangList}">
						<tr>
	   						<td>${it.getOutDepartName()}</td>
	   						<td>${it.getInDepartName()}</td>
	   						<td>${it.inDuty}</td>
	   						<td>${it.changeType}</td>
	   						<td>${it.changeReason}</td>
	   						<td>${it.getShowChangeDate()}</td>
	   					</tr>
					</g:each>
				</tbody>
			</table>
		</div>
		</g:if>
	</div>
	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"升职聘任信息",toggleable:false,moreText:"",marginBottom:"2px"'>
		<g:if test="${officialApplyList && officialApplyList.size()>0}">
		<div class="personSearch simpleNavigation">
			<table width="98%" class="tab_css ">
				<THEAD> 
					<tr class="bgClass">
							<th>聘任开始时间</th>
							<th>聘任结束时间</th>
	   						<th>申请理由</th>
	   					</tr>
	   					</THEAD>
	   					<tbody>
					<g:each in="${officialApplyList}">
						<tr>
							<td>${it.getFormattedStartDate()}</td>
							<td>${it.getFormattedEndDate}</td>
	   						<td>${it.applyReason}</td>
	   					</tr>
					</g:each>
				</tbody>
			</table>
		</div>
		</g:if>
	</div>
	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"离(退)休信息",toggleable:false,moreText:"",marginBottom:"2px"'>
		<g:if test="${statusChangList && statusChangList.size()>0}">
		  <div class="personSearch simpleNavigation">
			<table width="98%" class="tab_css ">
				<THEAD> 
					<tr class="bgClass">
							<th>申请类型</th>
							<th>申请理由</th>
	   						<th>申请时间</th>
	   					</tr>
	   					</THEAD>
	   					<tbody>
					<g:each in="${statusChangList}">
						<tr>
							<td>${it.changeType}</td>
							<td>${it.changeReason}</td>
	   						<td>${it.getFormattedChangeDate()}</td>
	   					</tr>
					</g:each>
				</tbody>
			</table>
			</div>
		</g:if>
	</div>
	
	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"培训信息",toggleable:false,moreText:"",marginBottom:"2px"'>
		<g:if test="${trainMessagegList && trainMessagegList.size()>0}">
		  <div class="personSearch simpleNavigation">
			<table width="98%" class="tab_css ">
				<THEAD> 
					<tr class="bgClass">
							<th>培训班名称</th>
							<th>培训时间</th>
	   						<th>培训费用</th>
	   						<th>培训考试结果</th>
	   						<th>是否发放证书</th>
	   					</tr>
	   					</THEAD>
	   					<tbody>
					<g:each in="${trainMessagegList}">
						<tr>
							<td>${it.getCourseName()}</td>
							<td>${it.getTrainDate()}</td>
	   						<td>${it.userMoney}</td>
	   						<td>${it.trainResult}</td>
	   						<td>${it.trainCert}</td>
	   					</tr>
					</g:each>
				</tbody>
			</table>
			</div>
		</g:if>
	</div>
	
</body>
</html>
