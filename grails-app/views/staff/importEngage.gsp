<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>批量导入</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
  </head>
  
<body>
	<div style="text-Align:center">
           <form data-dojo-type="dijit/form/Form" method="post" class="rosten_form" id="file_form" style="width:560px;text-align:left" 
        	target="upload_iframe" enctype="multipart/form-data" action="${createLink(controller:'staff',action:'importEngageSubmit',params:[])}">
            <fieldset class="fieldset-form">
                <legend class="tableHeader">批量导入</legend>
                <table class="tableData upload"  style="margin:2px;">
                    <tbody>
						<tr>
                            <td width="80">
                                <div align="right" style="margin-top:10px"><span style="color:red">*</span>文件地址：</div>
                            </td>
                             <td width="400">
                            	<input data-dojo-type="dojox/form/FileInput" data-dojo-props= 'label:"浏览...",cancelText:"清空"' name="uploadedfile" />
                            </td>
                        </tr>
                        
						<tr style="text-align:center">
							<td colspan="2">
								<div style="margin-top:10px">
								
<%--								<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){rosten.openNewWindow("staffTemplate", rosten.webPath + "/templates/staffTemplate.xls");}'>模板下载</button>--%>
								<button data-dojo-type="dijit/form/Button" type="submit" data-dojo-props=''>确定</button>
								<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){rosten.kernel.hideRostenShowDialog()}'>取消</button>
								</div>
							</td>
						</tr>
                    </tbody>
                </table>
				
				
            </fieldset>
            </form>
            <iframe name="upload_iframe" style="display:none"></iframe>
	</div>
</body>
</html>
