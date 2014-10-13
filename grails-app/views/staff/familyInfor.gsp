<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>家庭成员</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
  </head>
  
<body>
	<div style="text-Align:center">
		<input  data-dojo-type="dijit/form/ValidationTextBox" id="family_id"  data-dojo-props='name:"family_id",style:{display:"none"}' />
        <div class="rosten_form" style="width:400px;text-align:left">
            <fieldset class="fieldset-form">
                <legend class="tableHeader">家庭成员</legend>
                <table class="tableData">
                    <tbody>
						<tr>
                            <td width="80">
                                <div align="right"><span style="color:red">*&nbsp;</span>姓名：</div>
                            </td>
                           <td  width="220">
                                <input id="family_name" data-dojo-type="dijit/form/ValidationTextBox"
                                	data-dojo-props='name:"family_name",
                                		"class":"input",
                                		trim:true,
                                		required:true,
                                		promptMessage:"请正确输入姓名..."
                                '/>
                            </td>
                        </tr>
						<tr>
                            <td>
                                <div align="right"><span style="color:red">*&nbsp;</span>成员关系：</div>
                            </td>
                            <td>
                            	<input id="family_relation" data-dojo-type="dijit/form/ValidationTextBox"
                                	data-dojo-props='name:"family_relation",
                                		"class":"input",
                                		trim:true,
                                		required:true,
                                		promptMessage:"请正确输入成员关系..."
                                '/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div align="right"><span style="color:red">*&nbsp;</span>移动电话：</div>
                            </td>
                            <td>
                            	<input id="family_mobile" data-dojo-type="dijit/form/ValidationTextBox"
                                	data-dojo-props='name:"family_mobile",
                                		"class":"input",
                                		trim:true,
                                		required:true,
                                		promptMessage:"请正确输入移动电话..."
                                '/>
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                <div align="right">工作单位：</div>
                            </td>
                            <td>
                            	<input id="family_workUnit" data-dojo-type="dijit/form/ValidationTextBox"
                                	data-dojo-props='name:"family_workUnit",
                                		"class":"input",
                                		trim:true,
                                		promptMessage:"请正确输入工作单位..."
                                '/>
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                <div align="right">职务：</div>
                            </td>
                            <td>
                            	<input id="family_duties" data-dojo-type="dijit/form/ValidationTextBox"
                                	data-dojo-props='name:"family_duties",
                                		"class":"input",
                                		trim:true,
                                		promptMessage:"请正确输入职务..."
                                '/>
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                <div align="right">政治面貌：</div>
                            </td>
                            <td>
                            	<input id="family_politicsStatus" data-dojo-type="dijit/form/ValidationTextBox"
                                	data-dojo-props='name:"family_politicsStatus",
                                		"class":"input",
                                		trim:true,
                                		promptMessage:"请正确输入政治面貌..."
                                '/>
                            </td>
                        </tr>
                        
						<tr>
							<td></td>
							<td>
								<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){familyInfor_Submit()}'>确定</button>
								<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){rosten.hideRostenShowDialog()}'>取消</button>
								
							</td>
						</tr>
                    </tbody>
                </table>
				
				
            </fieldset>
		</div>
	</div>
</body>
</html>
