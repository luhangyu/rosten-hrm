<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title></title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
  </head>
  
<body>
	<div style="text-Align:center">
        <div class="rosten_form" style="width:400px;text-align:left">
            <fieldset class="fieldset-form">
                <legend class="tableHeader">表头信息</legend>
                <table class="tableData">
                    <tbody>
						<tr>
                            <td width="80">
                                <div align="right"><span style="color:red">*</span>表头名称：</div>
                            </td>
                            <td width="250">
                            	<input data-dojo-type="dijit/form/ValidationTextBox" 
                            	data-dojo-props='value:"45-49岁"' />
                            	
                            </td>
                        </tr>
                        <tr>
                            <td width="80">
                                <div align="right"><span style="color:red">*</span>关联字段：</div>
                            </td>
                            <td width="250">
                            	<input data-dojo-type="dijit/form/ValidationTextBox" 
                            	data-dojo-props='value:"年龄"' />
                            	
                            	<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){}'>选择</button>
                            	
                            </td>
                        </tr>
                        
						<tr style="text-align:center;margin-top:10px">
							<td colspan="2">
								
								<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){demo_static("designResult");rosten.kernel.hideRostenShowDialog()}'>确定</button>
								<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){rosten.kernel.hideRostenShowDialog()}'>取消</button>
								
							</td>
						</tr>
                    </tbody>
                </table>
				
				
            </fieldset>
		</div>
	</div>
</body>
</html>
