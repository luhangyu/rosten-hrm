<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>短信发送</title>
	<script type="text/javascript">

		require([
				"dijit/registry",
				"dijit/form/SimpleTextarea",
				"dijit/form/Button",
				"rosten/app/SmsManage"
		     	],function(registry){
			

		});	

		
    </script>
</head>
<body>
	<div style="text-Align:center">
	    <div class="rosten_form" style="width:550px">
	        <fieldset class="fieldset-form">
	            <legend class="tableHeader"> 短信发送
	            </legend>
	            <table width="500" border="0" align="center">
				<tr>
				    <td width="110">接收手机：
				    	<a href="javascript:sms_selectUser('${createLink(controller:'staff',action:'staffTreeDataStore',params:[userid:user?.id])}')">
				    		<img src="${resource(dir:'images/rosten/share',file:'group1.gif')}" width="16" height="16" border="0" align="absbottom">
				    	</a>
				    </td>
				    <td>
				    	<input id="sms_telephone" name="sms_telephone" type="text" rows="3" style="width: 390px;height:50px" 
					    	data-dojo-props='trim:true'
					    	data-dojo-type="dijit/form/SimpleTextarea" />
				    	
				    </td>
				  </tr>
				  <tr>
				    <td width="110">短信群组：
				    	<a href="javascript:selectSmsGroup('${createLink(controller:'system',action:'sms_group',params:[userid:user?.id])}')">
                           <img src="${resource(dir:'images/rosten/share',file:'group.gif')}" width="16" height="16" border="0" align="absbottom">
						</a>
					</td>
				    <td>
				    	<input disalbed id="sms_Group" name="sms_Group" type="text" rows="3" style="width: 390px;height:50px" 
				    		data-dojo-props='readOnly:true,trim:true'
				    		data-dojo-type="dijit/form/SimpleTextarea" />
				    </td>
				  </tr>
				  <tr>
				    <td width="110">短信内容：<span style="margin-right:16px">&nbsp;</span></td>
				    <td ><textarea id="sms_content" name="sms_content" rows="4" data-dojo-type="dijit/form/SimpleTextarea" 
				    	trim="true" style="width:390px;height:78px" ></textarea></td>
				  </tr>
 				
				  <tr>
				    <td>&nbsp;</td>
				    <td style="text-align:left"><button onClick="sendsms()" data-dojo-type="dijit/form/Button">提交</button>
				    	<button onClick="rosten.kernel.hideRostenShowDialog()" data-dojo-type="dijit/form/Button">取消</button>
				    </td>
				  </tr>
				</table>
	        </fieldset>
		</div>
	</div>
</body>
</html>