<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>合同管理</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
</style>

<body>
	<input  data-dojo-type="dijit/form/ValidationTextBox" id="barginId"  data-dojo-props='name:"barginId",style:{display:"none"},value:"${bargain?.id }"' />
	<table border="0" width="740" align="left">
		<tr>
			<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>合同编号：</div></td>
		  	<td width="250">
		    	<input id="bargainSerialNo" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"bargainSerialNo",trim:true,required:true,${fieldAcl.isReadOnly("bargainSerialNo")},
							value:"${bargain?.bargainSerialNo}"
	                '/>
		    </td>
		 	<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>合同类别：</div></td>
		  	<td width="250">
		    	<input id="bargainType" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"bargainType",trim:true,required:true,${fieldAcl.isReadOnly("bargainType")},
							value:"${bargain?.bargainType}"
	                '/>
		    </td>
		</tr>
		<tr>
			<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>起聘日期：</div></td>
	    	<td width="250">
		    	<input id="startDate" data-dojo-type="dijit/form/DateTextBox" 
	               	data-dojo-props='name:"startDate",${fieldAcl.isReadOnly("startDate")},
	               		trim:true,required:true,
						value:"${bargain?.getFormatteStartDate()}"
	          	'/>
           	</td>
		 	<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>终聘日期：</div></td>
		  	<td width="250">
		    	<input id="endDate" data-dojo-type="dijit/form/DateTextBox" 
	                 	data-dojo-props='name:"endDate",trim:true,required:true,${fieldAcl.isReadOnly("endDate")},
							value:"${bargain?.getFormatteEndDate()}"
	                '/>
		    </td>
		</tr>
	</table>
</body>
</html>
