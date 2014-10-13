<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>工作经历</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
</style>

<body>
<form class="rosten_form" data-dojo-type="dijit/form/Form"  data-dojo-id="workResume_form"  id="workResume_form" onsubmit="return false;" style="text-align:left;width:400px;">
	<input  data-dojo-type="dijit/form/ValidationTextBox" id="workResume_id"  data-dojo-props='name:"workResume_id",style:{display:"none"}' />
	<fieldset class="fieldset-form">
		<legend class="tableHeader">工作经历</legend>
		<table class="tableData">
			<tr>
	            <td width="80">
	                <div align="right"><span style="color:red">*&nbsp;</span>工作单位：</div>
	            </td>
	            <td  width="220">
	                <input id="workResume_workCompany" data-dojo-type="dijit/form/ValidationTextBox"
	                	data-dojo-props='name:"workResume_workCompany",
	                		"class":"input",
	                		trim:true,
	                		required:true
	                '/>
	             </td>
        	</tr>
			<tr>
               <td>
                   <div align="right"><span style="color:red">*&nbsp;</span>工作内容：</div>
               </td>
               <td>
               	<input id="workResume_workContent" data-dojo-type="dijit/form/ValidationTextBox"
                   	data-dojo-props='name:"workResume_workContent",
                   		"class":"input",
                   		trim:true,
                   		required:true
                   '/>
               </td>
           	</tr>
			<tr>
               <td>
                   <div align="right"><span style="color:red">*&nbsp;</span>开始时间：</div>
               </td>
               <td>
               	<input id="workResume_startDate" data-dojo-type="dijit/form/DateTextBox"
                   	data-dojo-props='name:"workResume_startDate",
                   		trim:true,
                   		required:true
                   '/>
               </td>
           	</tr>
			<tr>
               <td>
                   <div align="right"><span style="color:red">*&nbsp;</span>结束时间：</div>
               </td>
               <td>
               	<input id="workResume_endDate" data-dojo-type="dijit/form/DateTextBox"
                   	data-dojo-props='name:"workResume_endDate",
                   		trim:true,
                   		required:true
                   '/>
               </td>
           	</tr>
			<tr>
               <td>
                   <div align="right">担任职务：</div>
               </td>
               <td>
               	<input id="workResume_duty" data-dojo-type="dijit/form/ValidationTextBox"
                   	data-dojo-props='name:"workResume_duty",
                   		"class":"input",
                   		trim:true
                   '/>
               </td>
           	</tr>
			<tr>
               <td>
                   <div align="right">证明人：</div>
               </td>
               <td>
               	<input id="workResume_proveName" data-dojo-type="dijit/form/ValidationTextBox"
                   	data-dojo-props='name:"workResume_proveName",
                   		"class":"input",
                   		trim:true
                   '/>
               </td>
           	</tr>
			<tr>
               <td>
                   <div align="right">备注：</div>
               </td>
               <td>
               	<input id="workResume_remark" data-dojo-type="dijit/form/ValidationTextBox"
                   	data-dojo-props='name:"workResume_remark",
                   		"class":"input",
                   		trim:true
                   '/>
               </td>
           	</tr>
           	<tr>
				<td></td>
				<td>
					<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){workResume_Submit()}'>确定</button>
					<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){rosten.hideRostenShowDialog()}'>取消</button>
					
				</td>
			</tr>
		</table>
	</fieldset>
</form>
</body>
</html>
