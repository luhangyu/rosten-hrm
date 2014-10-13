<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>学习经历</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
</style>

<body>
<form class="rosten_form" data-dojo-type="dijit/form/Form"  data-dojo-id="degree_form" id="degree_form" onsubmit="return false;" style="text-align:left;width:400px;">
	<input  data-dojo-type="dijit/form/ValidationTextBox" id="degree_id"  data-dojo-props='name:"degree_id",style:{display:"none"}' />
	<fieldset class="fieldset-form">
		<legend class="tableHeader">学习经历</legend>
		<table class="tableData">
			<tr>
	            <td width="80">
	                <div align="right"><span style="color:red">*&nbsp;</span>学校名称：</div>
	            </td>
	            <td  width="220">
	                <input id="degree_name" data-dojo-type="dijit/form/ValidationTextBox"
	                	data-dojo-props='name:"degree_name",
	                		"class":"input",
	                		trim:true,
	                		required:true
	                '/>
	             </td>
        	</tr>
			<tr>
               <td>
                   <div align="right"><span style="color:red">*&nbsp;</span>专业：</div>
               </td>
               <td>
               	<input id="degree_major" data-dojo-type="dijit/form/ValidationTextBox"
                   	data-dojo-props='name:"degree_major",
                   		"class":"input",
                   		trim:true,
                   		required:true
                   '/>
               </td>
           	</tr>
			<tr>
               <td>
                   <div align="right"><span style="color:red">*&nbsp;</span>学历：</div>
               </td>
               <td>
               	<input id="degree_degree" data-dojo-type="dijit/form/ValidationTextBox"
                   	data-dojo-props='name:"degree_degree",
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
               	<input id="degree_startDate" data-dojo-type="dijit/form/DateTextBox"
                   	data-dojo-props='name:"degree_startDate",
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
               	<input id="degree_endDate" data-dojo-type="dijit/form/DateTextBox"
                   	data-dojo-props='name:"degree_endDate",
                   		trim:true,
                   		required:true
                   '/>
               </td>
           </tr>
           <tr>
				<td></td>
				<td>
					<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){degree_Submit()}'>确定</button>
					<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){rosten.hideRostenShowDialog()}'>取消</button>
					
				</td>
			</tr>
		</table>
	</fieldset>
	
</form>	
</body>
</html>
