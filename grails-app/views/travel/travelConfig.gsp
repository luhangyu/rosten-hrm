<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>配置文件</title>
	<script type="text/javascript">

		require([
				"dijit/registry",
				"dijit/form/SimpleTextarea",
		 		"dijit/form/ValidationTextBox",
		 		"rosten/widget/ActionBar"
		     	],function(registry){
	     	
			travelConfig_save = function(){
				var nowYear = registry.byId("nowYear");
				if(!nowYear.isValid()){
					rosten.alert("今年年份不正确！").queryDlgClose = function(){
						nowYear.focus();
					};
					
					return;
				}
				var nowSN = registry.byId("nowSN");
				if(!nowSN.isValid()){
					rosten.alert("今年流水号不正确！").queryDlgClose = function(){
						nowSN.focus();
					};
					
					return;
				}
				var frontYear = registry.byId("frontYear");
				if(!frontYear.isValid()){
					rosten.alert("去年年份不正确！").queryDlgClose = function(){
						frontYear.focus();
					};
					
					return;
				}
				var frontSN = registry.byId("frontSN");
				if(!frontSN.isValid()){
					rosten.alert("去年流水号不正确！").queryDlgClose = function(){
						frontSN.focus();
					};
					
					return;
				}
				var content = {};
				
				rosten.readSync("${createLink(controller:'travel',action:'travelConfigSave')}",content,function(data){
					if(data.result==true){
						var configId = registry.byId("id");
						if(configId.attr("value")==""){
							configId.attr("value",data.configId);
							registry.byId("companyId").attr("value",data.companyId);
						}
						rosten.alert("保存成功");
					}else{
						rosten.alert("保存失败！");
					}	
				},null,"config_form");
			}

		});	

		
    </script>
</head>
<body>
<div class="rosten_action">
	<div data-dojo-type="rosten/widget/ActionBar" 
		data-dojo-props='actionBarSrc:"${createLink(controller:'travelAction',action:'travelConfigView')}"'>
	</div>
</div>
<div style="text-Align:center">
	
<g:form id="config_form" name="config_form" onsubmit="return false;" class="rosten_form" style="text-align:left;margin:10px">
	<fieldset class="fieldset-form">
	<legend class="tableHeader">配置文档</legend>
		<input id="id" data-dojo-type="dijit/form/ValidationTextBox"  data-dojo-props='name:"id",style:{display:"none"},value:"${config?.id }"' />
        <input id="companyId" data-dojo-type="dijit/form/ValidationTextBox"  data-dojo-props='name:"companyId",style:{display:"none"},value:"${companyId }"' />
		<table border="0" width="740" align="left">
			<tr>
			    <td width="130"><div align="right"><span style="color:red">*&nbsp;</span>今年年份：</div></td>
			    <td width="240">
			    	<input id="nowYear" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"nowYear",${fieldAcl.isReadOnly("nowYear")},
	                 		trim:true,
	                 		"class":"input",
	                 		required:true,
							value:"${config?.nowYear}"
	                '/>
			    </td>
			    <td width="130"><div align="right"><span style="color:red">*&nbsp;</span>今年流水号：</div></td>
			    <td width="240">
			    	<input id="nowSN" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"nowSN",${fieldAcl.isReadOnly("nowSN")},
	                 		trim:true,
	                 		"class":"input",
	                 		required:true,
							value:"${config?.nowSN}"
	                '/>
	           </td>
			</tr>
			<tr>
			    <td><div align="right">今年保留号或废弃号：</div></td>
			    <td colspan=3>
			    	<textarea id="bbsConfig_nowCancel" data-dojo-type="dijit/form/SimpleTextarea" 
					data-dojo-props='name:"bbsConfig_nowCancel",${fieldAcl.isReadOnly("nowCancel")},
	            		"class":"input",
	            		style:{width:"564px"},
	            		trim:true,
	            		value:"${config?.nowCancel }"
		            '>
					</textarea>
			    </td>
			</tr>
			<tr>
			    <td><div align="right"><span style="color:red">*&nbsp;</span>去年年份：</div></td>
			    <td>
			    	<input id="frontYear" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"frontYear",${fieldAcl.isReadOnly("frontYear")},
	                 		trim:true,
	                 		required:true,
	                 		"class":"input",
							value:"${config?.frontYear}"
	                '/>
			    </td>
			    <td><div align="right"><span style="color:red">*&nbsp;</span>去年流水号：</div></td>
			    <td>
			    	<input id="frontSN" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"frontSN",${fieldAcl.isReadOnly("frontSN")},
	                 		trim:true,
	                 		required:true,
	                 		"class":"input",
							value:"${config?.frontSN}"
	                '/>
	           </td>
			</tr>
			<tr>
			    <td><div align="right">去年保留号或废弃号：</div></td>
			    <td colspan=3>
			    	<textarea id="bbsConfig_frontCancel" data-dojo-type="dijit/form/SimpleTextarea" 
					data-dojo-props='name:"bbsConfig_frontCancel",${fieldAcl.isReadOnly("frontCancel")},
	            		"class":"input",
	            		trim:true,
	            		style:{width:"564px"},
	            		value:"${config?.frontCancel }"
		            '>
					</textarea>
			    </td>
			</tr>
		</table>
		</fieldset>
</g:form>
</div>
</body>
</html>