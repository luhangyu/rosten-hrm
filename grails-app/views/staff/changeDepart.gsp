<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>部门变更</title>
	<script type="text/javascript">

		require([
				"dijit/registry",
				"dojo/dom-style",
				"dojo/dom",
				"dijit/form/SimpleTextarea",
		 		"dijit/form/ValidationTextBox",
		 		"dijit/form/RadioButton",
		 		"rosten/widget/ActionBar"
		     	],function(registry,domStyle,dom){
	     	
			searchStaff = function(){
				var username = registry.byId("username");
		    	if(!username.isValid()){
		    		rosten.alert("员工名称不正确！").queryDlgClose = function(){
		    			username.focus();
					};
					return;
		    	}
		    	var content ={
		    		companyId:"${companyId}",
		    		serchInput:username.attr("value")

				};
		    	rosten.readSync(rosten.webPath + "/staff/serachPerson", content, function(data) {
		    		registry.byId("userId").attr("value",data.userId);
		    		registry.byId("userDepartId").attr("value",data.userDepartId);
		    		registry.byId("userDepart").attr("value",data.userDepart);
		    		registry.byId("sex").attr("value",data.sex);
		    		registry.byId("idCard").attr("value",data.idCard);
		    		registry.byId("birthday").attr("value",data.birthday);
		    		registry.byId("city").attr("value",data.city);
		    		registry.byId("nationality").attr("value",data.nationality);
		    		registry.byId("birthAddress").attr("value",data.birthAddress);
		    		registry.byId("nativeAddress").attr("value",data.nativeAddress);
		    		registry.byId("politicsStatus").attr("value",data.politicsStatus);
		    		registry.byId("marriage").attr("value",data.marriage);
		    		registry.byId("religion").attr("value",data.religion);
				});
			};
		});	

		
    </script>
</head>
<body>
<div class="rosten_action">
	<div data-dojo-type="rosten/widget/ActionBar" 
		data-dojo-props='actionBarSrc:"${createLink(controller:'staffAction',action:'staffChangeDepartForm')}"'>
	</div>
</div>
<div style="text-Align:center">
	
<g:form id="rosten_form" name="rosten_form" onsubmit="return false;" class="rosten_form" style="text-align:left">
	<fieldset class="fieldset-form">
	<legend class="tableHeader">员工信息</legend>
        <input id="companyId" data-dojo-type="dijit/form/ValidationTextBox"  data-dojo-props='name:"companyId",style:{display:"none"},value:"${companyId }"' />
        <input id="userId" data-dojo-type="dijit/form/ValidationTextBox"  data-dojo-props='name:"userId",style:{display:"none"}' />
        <input id="userDepartId" data-dojo-type="dijit/form/ValidationTextBox"  data-dojo-props='name:"userDepartId",style:{display:"none"},trim:true,required:true' />
		<table border="0" align="left" style="margin:0 auto;width:740px">
			<tr>
			    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>员工名称：</div></td>
			    <td width="260">
			    	<input id="username" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='trim:true,required:true,placeHolder:"员工账号/姓名/电话"
	                '/>
	                <button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){searchStaff()}'>查询验证</button>
			    </td>
			    <td width="120"><div align="right">所属部门：</div></td>
			    <td width="250">
			    	<input id="userDepart" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='readOnly:true'/>
	           </td>
			</tr>
			<tr>
			    <td width="120"><div align="right">性别：</div></td>
			  	<td width="260">
			  		<input id="sex" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='readOnly:true'/>
			    </td>
			    <td width="120"><div align="right">身份证号：</div></td>
			  	<td width="250">
			    	<input id="idCard" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='readOnly:true'/>
			    </td>
			</tr>
			<tr>
			 	<td width="120"><div align="right">出生日期：</div></td>
			  	<td width="260">
			    	<input id="birthday" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='readOnly:true'/>
			    </td>
			    <td width="120"><div align="right">国籍：</div></td>
			  	<td width="250">
			    	<input id="city" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='readOnly:true'/>
			    </td>
			     
			</tr>
			<tr>
				<td width="120"><div align="right">民族：</div></td>
			  	<td width="260">
			    	<input id="nationality" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='readOnly:true'/>
			    </td>
			 	<td width="120"><div align="right">出生地：</div></td>
			  	<td width="250">
			    	<input id="birthAddress" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='readOnly:true'/>
			    </td>
			</tr>
			
			<tr>
			 	<td width="120"><div align="right">籍贯：</div></td>
				<td width="260">
			    	<input id="nativeAddress" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='readOnly:true'/>
				</td>
				<td width="120"><div align="right">政治面貌：</div></td>
			 	<td width="250">
			    	<input id="politicsStatus" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='readOnly:true'/>
			    </td>
			</tr>
			
			<tr>
				<td width="120"><div align="right">婚姻状况：</div></td>
				  <td width="260">
				  		<input id="marriage" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='readOnly:true'/>
				    </td>
				 <td width="120"><div align="right">宗教信仰：</div></td>
				  <td width="250">
				  		<input id="religion" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='readOnly:true'/>
				    </td>
			</tr>
		</table>
		</fieldset>
		<fieldset class="fieldset-form">
			<legend class="tableHeader">调入部门</legend>
			<table border="0" align="left" style="margin:0 auto;width:740px">
				<tr>
				    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>调入部门：</div></td>
				    <td width="260">
		                <input id="newdepartName" data-dojo-type="dijit/form/ValidationTextBox" 
			               	data-dojo-props='name:"newdepartName",readOnly:true,
			               		trim:true,required:true
			          	'/>
			         	<input id="newDepartId" data-dojo-type="dijit/form/ValidationTextBox"  data-dojo-props='style:{display:"none"},trim:true,required:true' />
						<button data-dojo-type="dijit/form/Button" 
							data-dojo-props='onClick:function(){
								rosten.selectDepart("${createLink(controller:'system',action:'departTreeDataStore',params:[companyId:companyId])}",false,"newdepartName","newDepartId")}'>选择</button>
		           </td>
		           <td width="120"><div align="right"></div></td>
				   <td width="250">
				    	
		           </td>
				</tr>
			</table>
		</fieldset>
</g:form>
</div>
</body>
</html>