<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>岗位级别信息</title>
	<script type="text/javascript">
	require([
				"dijit/registry",
				"dijit/form/SimpleTextarea",
		 		"dijit/form/ValidationTextBox",
		 		"rosten/widget/ActionBar"
		     	],function(registry){

		riskGold_save = function(){
			var content = {};
			
			rosten.readSync("${createLink(controller:'salary',action:'riskGoldSave')}",content,function(data){
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
			},null,"rosten_form");
		}
     	
	});		
	
    </script>
</head>
<body>
<%--<div class="rosten_action">--%>
<%--	<div data-dojo-type="rosten/widget/ActionBar" data-dojo-id="rosten_actionBar" --%>
<%--		data-dojo-props='actionBarSrc:"${createLink(controller:'salaryAction',action:'riskGoldForm')}"'>--%>
<%--	</div>--%>
<%--</div>--%>

<div class="rosten_action">
	<div data-dojo-type="rosten/widget/ActionBar" 
		data-dojo-props='actionBarSrc:"${createLink(controller:'salaryAction',action:'riskGoldForm')}"'>
	</div>
</div>

<div style="text-Align:center">
		<form id="rosten_form" name="rosten_form" url='[controller:"assetConfig",action:"assetCategorySave"]' onsubmit="return false;" class="rosten_form" style="padding:0px">
			<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${riskGold?.id }"' />
        	<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        	
				<table border="0" width="740" align="left">
				
				
				<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>公积金基数：</div></td>
					    <td >
					    	<input id="gjj" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"gjj",trim:true,required:true,
									value:"${riskGold?.gjj}"
			                '/>
					    </td>
					     <td><div align="right"><span style="color:red">*&nbsp;</span>公积金比例：</div></td>
					    <td >
					    	<input id="gjjBl" data-dojo-type="dijit/form/NumberTextBox" 
			                 	data-dojo-props='name:"gjjBl",trim:true,required:true,
									value:"${riskGold?.gjjBl}"
			                '/>
					    </td>
					</tr>
					
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>失业保险基数：</div></td>
					    <td >
					    	<input id="sybx" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"sybx",trim:true,required:true,
									value:"${riskGold?.sybx}"
			                '/>
					    </td>
					     <td><div align="right"><span style="color:red">*&nbsp;</span>失业保险比例：</div></td>
					    <td >
					    	<input id="sybxBl" data-dojo-type="dijit/form/NumberTextBox" 
			                 	data-dojo-props='name:"sybxBl",trim:true,required:true,
									value:"${riskGold?.sybxBl}"
			                '/>
					    </td>
					</tr>
					
					
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>医疗保险基数：</div></td>
					    <td >
					    	<input id="ylbx" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"ylbx",trim:true,required:true,
									value:"${riskGold?.ylbx}"
			                '/>
					    </td>
					     <td><div align="right"><span style="color:red">*&nbsp;</span>医疗保险比例：</div></td>
					    <td >
					    	<input id="ylbxBl" data-dojo-type="dijit/form/NumberTextBox" 
			                 	data-dojo-props='name:"ylbxBl",trim:true,required:true,
									value:"${riskGold?.ylbxBl}"
			                '/>
					    </td>
					</tr>
					
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>生育保险基数：</div></td>
					    <td >
					    	<input id="syubx" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"syubx",trim:true,required:true,
									value:"${riskGold?.syubx}"
			                '/>
					    </td>
					     <td><div align="right"><span style="color:red">*&nbsp;</span>生育保险保险比例：</div></td>
					    <td >
					    	<input id="syubxBl" data-dojo-type="dijit/form/NumberTextBox" 
			                 	data-dojo-props='name:"syubxBl",trim:true,required:true,
									value:"${riskGold?.syubxBl}"
			                '/>
					    </td>
					</tr>
					
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>养老保险基数：</div></td>
					    <td >
					    	<input id="ylaobx" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"ylaobx",trim:true,required:true,
									value:"${riskGold?.ylaobx}"
			                '/>
					    </td>
					     <td><div align="right"><span style="color:red">*&nbsp;</span>养老保险比例：</div></td>
					    <td >
					    	<input id="ylaobxBl" data-dojo-type="dijit/form/NumberTextBox" 
			                 	data-dojo-props='name:"ylaobxBl",trim:true,required:true,
									value:"${riskGold?.ylaobxBl}"
			                '/>
					    </td>
					</tr>
					
				</table>
			
		</form>
	</div>
	
</body>