<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>代码条目</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
  </head>
  
<body>
	<div style="text-Align:center">
        <div class="rosten_form" style="width:400px;text-align:left">
            <fieldset class="fieldset-form">
                <legend class="tableHeader">代码条目</legend>
                <table class="tableData">
                    <tbody>
						<tr>
                            <td width="80">
                                <div align="right"><span style="color:red">*&nbsp;</span>条目编号：</div>
                            </td>
                           <td  width="220">
                                <input id="itemCode" data-dojo-type="dijit/form/ValidationTextBox"
                                	data-dojo-props='name:"itemCode",
                                		"class":"input",
                                		trim:true,
                                		required:true,
                                		promptMessage:"请正确输入条目编号..."
                                '/>
                            </td>
                        </tr>
						<tr>
                            <td>
                                <div align="right"><span style="color:red">*&nbsp;</span>条目名称：</div>
                            </td>
                            <td>
                            	<input id="itemName" data-dojo-type="dijit/form/ValidationTextBox"
                                	data-dojo-props='name:"itemName",
                                		"class":"input",
                                		trim:true,
                                		required:true,
                                		promptMessage:"请正确输入条目名称..."
                                '/>
                            </td>
                        </tr>
						<tr>
							<td></td>
							<td>
								<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){systemCodeItem_Submit()}'>确定</button>
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
