<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>学习经历</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
</style>

<body>
	<table border="0" width="740" align="left">
		<tr>
		    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>学校名称：</div></td>
		    <td width="250" colspan="3">
		    	<input id="degreeName" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"degreeName",trim:true,required:true,${fieldAcl.isReadOnly("degreeName")},
							value:"${degreeEntity?.degreeName}"
	                '/>
		    </td>
		    
		</tr>
		<tr>
		 	<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>专业：</div></td>
		  	<td width="250">
		    	<input id="major" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"major",trim:true,required:true,${fieldAcl.isReadOnly("major")},
							value:"${degreeEntity?.major}"
	                '/>
		    </td>
		    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>学历：</div></td>
		  	<td width="250">
		    	<input id="degree" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"degree",trim:true,required:true,${fieldAcl.isReadOnly("degree")},
							value:"${degreeEntity?.degree}"
	                '/>
		    </td>
		</tr>
		<tr>
			<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>开始时间：</div></td>
	    	<td width="250">
		    	<input id="startDate" data-dojo-type="dijit/form/DateTextBox" 
	               	data-dojo-props='name:"startDate",${fieldAcl.isReadOnly("startDate")},
	               		trim:true,required:true,
						value:"${degreeEntity?.getFormatteStartDate()}"
	          	'/>
           	</td>
		 	<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>结束时间：</div></td>
		  	<td width="250">
		    	<input id="endDate" data-dojo-type="dijit/form/DateTextBox" 
	                 	data-dojo-props='name:"endDate",trim:true,required:true,${fieldAcl.isReadOnly("endDate")},
							value:"${degreeEntity?.getFormatteEndDate()}"
	                '/>
		    </td>
		</tr>
	</table>
</body>
</html>
