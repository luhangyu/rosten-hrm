<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>工作经历</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
</style>

<body>
	<table border="0" width="740" align="left">
		<tr>
		    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>工作单位：</div></td>
	  		<td width="250">
		    	<input id="workCompany" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"workCompany",trim:true,required:true,${fieldAcl.isReadOnly("workCompany")},
							value:"${workResumeEntity?.workCompany}"
	                '/>
		    </td>
		    
	       	<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>工作内容：</div></td>
		  	<td width="250">
		    	<input id="workContent" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"workContent",trim:true,required:true,${fieldAcl.isReadOnly("workContent")},
							value:"${workResumeEntity?.workContent}"
	                '/>
		    </td>
		</tr>
		<tr>
		    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>开始时间：</div></td>
		    <td width="250">
		    	<input id="workStartDate" data-dojo-type="dijit/form/DateTextBox" 
	               	data-dojo-props='name:"workStartDate",${fieldAcl.isReadOnly("startDate")},
	               		trim:true,required:true,
						value:"${workResumeEntity?.getFormatteStartDate()}"
	          	'/>
           </td>
           
           <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>结束时间：</div></td>
		  <td width="250">
		    	<input id="workEndDate" data-dojo-type="dijit/form/DateTextBox" 
	                 	data-dojo-props='name:"workEndDate",trim:true,required:true,${fieldAcl.isReadOnly("endDate")},
							value:"${workResumeEntity?.getFormatteEndDate()}"
	                '/>
		    </td>
           
		</tr>
		<tr>
		 <td width="120"><div align="right">担任职务：</div></td>
		  <td width="250">
		    	<input id="duty" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"duty",trim:true,${fieldAcl.isReadOnly("duty")},
							value:"${workResumeEntity?.duty}"
	                '/>
		    </td>
		    
		    <td width="120"><div align="right">证明人：</div></td>
		  <td width="250">
		    	<input id="proveName" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"proveName",trim:true,${fieldAcl.isReadOnly("proveName")},
							value:"${workResumeEntity?.proveName}"
	                '/>
		    </td>
		</tr>
		
		<tr>
		 	<td width="120"><div align="right">备注：</div></td>
		  	<td  colspan="3">
	    	<textarea id="remark" data-dojo-type="dijit/form/SimpleTextarea" 
				data-dojo-props='name:"remark",
                    style:{width:"550px",height:"150px"},
                    trim:true,value:"${workResumeEntity?.remark}"
            '>
			</textarea>
	    	</td>
		</tr>
		
	</table>
</body>
</html>
