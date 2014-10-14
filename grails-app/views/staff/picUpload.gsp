<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>上传</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
  </head>
  
<body>
	<div style="text-Align:center">
        <form data-dojo-type="dijit/form/Form" method="post" class="rosten_form" id="file_form" style="width:560px;text-align:left" 
        	target="upload_iframe" enctype="multipart/form-data" action="${createLink(controller:'staff',action:'uploadPic',params:[personId:uploadPic])}">
            <fieldset class="fieldset-form">
                <legend class="tableHeader">图片上传</legend>
                <table class="tableData upload" style="margin:2px;">
                    <tbody>
						<tr>
                            <td width="80">
                                <div align="right" style="margin-top:10px"><span style="color:red">*</span>文件地址：</div>
                            </td>
                            <td width="220">
                            	<input data-dojo-type="dojox/form/FileInput" data-dojo-props= 'label:"浏览...",cancelText:"清空"' name="uploadedfile" />
                            </td>
                        </tr>                       
                    </tbody>
                </table>
				<div style="text-align:center;margin-top:10px">
					<button data-dojo-type="dijit/form/Button" type="submit" data-dojo-props=''>确定</button>
					<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){rosten.hideRostenShowDialog()}'>取消</button>
				</div>
				
            </fieldset>
		</form>
		<iframe name="upload_iframe" style="display:none">
		</iframe>
	</div>
</body>
</html>
