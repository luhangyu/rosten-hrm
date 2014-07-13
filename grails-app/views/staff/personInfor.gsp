<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>个人概况</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
</style>

<body>
	<table border="0" width="740" align="left">
		<tr>
		    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>姓名：</div></td>
		    <td width="250">
		    	<input id="chinaName" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"chinaName",trim:true,${fieldAcl.isReadOnly("chinaName")},
							value:"${personInforEntity?.user?.chinaName}"
	                '/>
		    </td>
		    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>所属部门：</div></td>
		    <td width="250">
		    	<input id="allowdepartsName" data-dojo-type="dijit/form/ValidationTextBox" 
	               	data-dojo-props='name:"allowdepartsName",${fieldAcl.isReadOnly("allowdepartsName")},
	               		trim:true,
	               		required:true,
						value:"${personInforEntity?.user?.getDepartName()}"
	          	'/>
	         	<g:hiddenField name="allowdepartsId" value="${personInforEntity?.user?.getDepartEntity()?.id }" />
				<button data-dojo-type="dijit.form.Button" data-dojo-props='onClick:function(){selectDepart("${createLink(controller:'system',action:'departTreeDataStore',params:[companyId:company?.id])}")}'>选择</button>
           </td>
		</tr>
	</table>
</body>
</html>
