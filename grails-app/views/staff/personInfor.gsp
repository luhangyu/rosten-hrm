<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>个人概况</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
</style>

<body>
	<table border="0" width="740" align="left">
		<tr>
		    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>姓名：</div></td>
		    <td width="250">
		    	<input id="chinaName" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"chinaName",trim:true,required:true,${fieldAcl.isReadOnly("chinaName")},
							value:"${personInforEntity?.user?.getFormattedName()}"
	                '/>
		    </td>
		    <td width="120"><div align="right">曾用名：</div></td>
		  	<td width="250">
		    	<input id="usedName" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"usedName",trim:true,${fieldAcl.isReadOnly("usedName")},
							value:"${personInforEntity?.usedName}"
	                '/>
		    </td>
		</tr>
		<tr>
			<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>用户类型：</div></td>
			<td width="250">
		    	<select id="userTypeName" data-dojo-type="dijit/form/ComboBox"
             		data-dojo-props='name:"userTypeName",
             			autoComplete:true,required:true,
             			${fieldAcl.isReadOnly("userTypeName")},
						value:"${personInforEntity?.user?.userTypeEntity?.typeName }"
                '>
                <g:each in="${userTypeList}" var="item">
                	<option value="${item.id }">${item.typeName }</option>
                </g:each>	
                
                </select>
		    </td>
		    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>所属部门：</div></td>
		    <td width="250">
		    	<input id="allowdepartsName" data-dojo-type="dijit/form/ValidationTextBox" 
	               	data-dojo-props='name:"allowdepartsName",readOnly:true,
	               		trim:true,required:true,
						value:"${departName}"
	          	'/>
	         	<g:hiddenField name="allowdepartsId" value="${departId}" />
				<button data-dojo-type="dijit.form.Button" data-dojo-props='onClick:function(){selectDepart("${createLink(controller:'system',action:'departTreeDataStore',params:[companyId:company?.id])}")}'>选择</button>
           	</td>
		</tr>
		<tr>
		    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>性别：</div></td>
		  	<td width="250">
		  		<input id="sex1" data-dojo-type="dijit/form/RadioButton"
	           		data-dojo-props='name:"sex",type:"radio",
	           			<g:if test="${personInforEntity?.sex=="男" }">checked:true,</g:if>
						value:"true"
              	'/>
				<label for="sex1">男</label>
			
              	<input id="sex2" data-dojo-type="dijit/form/RadioButton"
           			data-dojo-props='name:"sex",type:"radio",
           			<g:if test="${personInforEntity?.sex=="女" }">checked:true,</g:if>
					value:"false"
              	'/>
				<label for="sex2">女</label>
		    </td>
		    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>身份证号：</div></td>
		  	<td width="250">
		    	<input id="idCard" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"idCard",trim:true,required:true,${fieldAcl.isReadOnly("idCard")},
							value:"${personInforEntity?.idCard}"
	                '/>
		    </td>
		</tr>
		<tr>
		 	<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>出生日期：</div></td>
		  	<td width="250">
		    	<input id="birthday" data-dojo-type="dijit/form/DateTextBox" 
	                 	data-dojo-props='name:"birthday",trim:true,${fieldAcl.isReadOnly("birthday")},
							value:"${personInforEntity?.getFormatteBirthday()}"
	                '/>
		    </td>
		    <td width="120"><div align="right">国籍：</div></td>
		  	<td width="250">
		    	<input id="city" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"city",trim:true,${fieldAcl.isReadOnly("city")},
							value:"${personInforEntity?.city}"
	                '/>
		    </td>
		     
		</tr>
		<tr>
			<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>民族：</div></td>
		  	<td width="250">
		    	<input id="nationality" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"nationality",trim:true,${fieldAcl.isReadOnly("nationality")},
							value:"${personInforEntity?.nationality}"
	                '/>
		    </td>
		 	<td width="120"><div align="right">出生地：</div></td>
		  	<td width="250">
		    	<input id="birthAddress" data-dojo-type="dijit/form/ValidationTextBox" 
                 	data-dojo-props='name:"birthAddress",trim:true,${fieldAcl.isReadOnly("birthAddress")},
						value:"${personInforEntity?.birthAddress}"
                '/>
		    </td>
		</tr>
		
		<tr>
		 	<td width="120"><div align="right">籍贯：</div></td>
			<td width="250">
		    	<input id="nativeAddress" data-dojo-type="dijit/form/ValidationTextBox" 
                 	data-dojo-props='name:"nativeAddress",trim:true,${fieldAcl.isReadOnly("nativeAddress")},
						value:"${personInforEntity?.nativeAddress}"
                '/>
			</td>
			<td width="120"><div align="right">政治面貌：</div></td>
		 	<td width="250">
		    	<input id="politicsStatus" data-dojo-type="dijit/form/ValidationTextBox" 
                 	data-dojo-props='name:"politicsStatus",trim:true,${fieldAcl.isReadOnly("politicsStatus")},
						value:"${personInforEntity?.politicsStatus}"
                '/>
		    </td>
		</tr>
		
		<tr>
			<td width="120"><div align="right">婚姻状况：</div></td>
			  <td width="250">
			  		<input id="marriage1" data-dojo-type="dijit/form/RadioButton"
		           		data-dojo-props='name:"marriage",type:"radio",
		           			<g:if test="${personInforEntity?.marriage=="已婚" }">checked:true,</g:if>
							value:"true"
	              	'/>
					<label for="marriage1">已婚</label>
				
	              	<input id="marriage2" data-dojo-type="dijit/form/RadioButton"
	           			data-dojo-props='name:"marriage",type:"radio",
	           			<g:if test="${personInforEntity?.marriage=="未婚" }">checked:true,</g:if>
						value:"false"
	              	'/>
					<label for="marriage2">未婚</label>
			    </td>
			 <td width="120"><div align="right">宗教信仰：</div></td>
			  <td width="250">
			  		<input id="religion1" data-dojo-type="dijit/form/RadioButton"
		           		data-dojo-props='name:"religion",type:"radio",
		           			<g:if test="${personInforEntity?.religion=="有" }">checked:true,</g:if>
							value:"true"
	              	'/>
					<label for="religion1">有</label>
				
	              	<input id="religion2" data-dojo-type="dijit/form/RadioButton"
	           			data-dojo-props='name:"religion",type:"radio",
	           			<g:if test="${personInforEntity?.religion=="无" }">checked:true,</g:if>
						value:"false"
	              	'/>
					<label for="religion2">无</label>
			    </td>
		</tr>
		
		<tr>
		 	<td width="120"><div align="right">血型：</div></td>
		  	<td width="250">
		    	<input id="blood" data-dojo-type="dijit/form/ValidationTextBox" 
                 	data-dojo-props='name:"blood",trim:true,${fieldAcl.isReadOnly("blood")},
						value:"${personInforEntity?.blood}"
                '/>
		    </td>
	     	<td width="120"><div align="right">健康状况：</div></td>
		  	<td width="250">
		    	<input id="health" data-dojo-type="dijit/form/ValidationTextBox" 
	                	data-dojo-props='name:"health",trim:true,${fieldAcl.isReadOnly("health")},
						value:"${personInforEntity?.health}"
	               '/>
		    </td>
		</tr>
		
	</table>
</body>
</html>
