<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>flowLog</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
</style>

<body>

	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"流程图",toggleable:false,moreText:"",height:"300px",marginBottom:"2px",
		style:{overflow:"auto"}
	'>
		<g:if test="${logEntityName}">
			<div style="margin:0 auto;text-align:center">
				<img src="${createLink(controller:logEntityName,action:'flowActiveExport',id:logEntityId)}" style="left:0px; top:0px;">
			</div>
		</g:if>
	</div>
	
	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"流转信息",toggleable:false,moreText:""'>
		<div class="flowLog">
			<table>
				<tbody>
					<g:each in="${log}">
						<tr>
							<th>${it.getFormattedCreatedDate()}</th>
	   						<td>${it.user.getFormattedName() + " —> " + it.content}</td>
	   					</tr>
					</g:each>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
