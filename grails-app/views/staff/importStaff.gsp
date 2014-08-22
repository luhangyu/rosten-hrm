<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>批量导入</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
  </head>
  
<body>
	<div style="text-Align:center">
        <div class="rosten_form" style="width:400px;text-align:left">
            <fieldset class="fieldset-form">
                <legend class="tableHeader">用户批量导入</legend>
                <table class="tableData">
                    <tbody>
						<tr>
                            <td width="80">
                                <div align="right"><span style="color:red">*</span>文件地址：</div>
                            </td>
                            <td width="220">
                            	<input data-dojo-type="dijit/form/ValidationTextBox" 
                            	data-dojo-props='readOnly:true' />
                            	<img src="${resource(dir:'images/rosten/navigation',file:'attach.png')}" >
                            </td>
                        </tr>
                        
						<tr style="text-align:center;margin-top:10px">
							<td colspan="2">
								<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){}'>模板下载</button>
								<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){rosten.kernel.hideRostenShowDialog()}'>确定</button>
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
