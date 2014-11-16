<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>个人概况</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
</style>

<body>
	<input  data-dojo-type="dijit/form/ValidationTextBox" id="personInforId"  data-dojo-props='name:"personInforId",style:{display:"none"},value:"${personInforEntity?.id }"' />
	<table border="0" width="730" align="left">
		<tr>
		    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>姓名：</div></td>
		    <td width="250">
		    	<input id="chinaName" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"chinaName",trim:true,required:true,${fieldAcl.isReadOnly("chinaName")},
							value:"${personInforEntity?.chinaName}"
	                '/>
		    </td>
		    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>状态：</div></td>
		  	<td width="240">
		    	<select id="status" data-dojo-type="dijit/form/ComboBox" 
                 	data-dojo-props='name:"status",trim:true,${fieldAcl.isReadOnly("status")},
                 		<g:if test="${statusNotWrite }">disabled:true,</g:if>
						value:"${personInforEntity?.status}"
                '>
	                <g:each in="${statusList}" var="item">
	                	<option value="${item}">${item}</option>
	                </g:each>
	            </select>
		    </td>
		</tr>
		<tr>
			<td><div align="right"><span style="color:red">*&nbsp;</span>用户类型：</div></td>
			<td>
		    	<select id="userTypeName" data-dojo-type="dijit/form/ComboBox"
             		data-dojo-props='name:"userTypeName",
             			autoComplete:true,required:true,
             			${fieldAcl.isReadOnly("userTypeName")},
						value:"${personInforEntity?.getUserTypeName()?personInforEntity.getUserTypeName():userTypeEntity?.typeName }"
                '>
                <g:each in="${userTypeList}" var="item">
                	<option value="${item.id }">${item.typeName }</option>
                </g:each>	
                
                </select>
		    </td>
		    <td><div align="right"><span style="color:red">*&nbsp;</span>所属部门：</div></td>
		    <td>
		    	<input id="allowdepartsName" data-dojo-type="dijit/form/ValidationTextBox" 
	               	data-dojo-props='name:"allowdepartsName",readOnly:true,
	               		trim:true,required:true,
						value:"${departName}"
	          	'/>
	          	<g:if test="${!onlyShow }">
		         	<g:hiddenField name="allowdepartsId" value="${departId}" />
					<button data-dojo-type="dijit.form.Button" data-dojo-props='onClick:function(){selectDepart("${createLink(controller:'system',action:'departTreeDataStore',params:[companyId:company?.id])}")}'>选择</button>
           		</g:if>
           	</td>
		</tr>
		<tr>
		    <td><div align="right"><span style="color:red">*&nbsp;</span>性别：</div></td>
		  	<td>
		  		<input id="sex1" data-dojo-type="dijit/form/RadioButton"
	           		data-dojo-props='name:"sex",type:"radio",${fieldAcl.isReadOnly("sex")},
	           			<g:if test="${personInforEntity?.sex=="男" }">checked:true,</g:if>
						value:"男"
              	'/>
				<label for="sex1">男</label>
			
              	<input id="sex2" data-dojo-type="dijit/form/RadioButton"
           			data-dojo-props='name:"sex",type:"radio",${fieldAcl.isReadOnly("sex")},
           			<g:if test="${personInforEntity?.sex=="女" }">checked:true,</g:if>
					value:"女"
              	'/>
				<label for="sex2">女</label>
		    </td>
		    <td><div align="right"><span style="color:red">*&nbsp;</span>身份证号：</div></td>
		  	<td>
		    	<input id="idCard" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"idCard",trim:true,required:true,${fieldAcl.isReadOnly("idCard")},
							value:"${personInforEntity?.idCard}"
	                '/>
		    </td>
		</tr>
		<tr>
		 	<td><div align="right">出生日期：</div></td>
		  	<td>
		    	<input id="birthday" data-dojo-type="dijit/form/DateTextBox" 
	                 	data-dojo-props='name:"birthday",trim:true,${fieldAcl.isReadOnly("birthday")},
							value:"${personInforEntity?.getFormatteBirthday()}"
	                '/>
		    </td>
		    <td><div align="right">国籍：</div></td>
		  	<td>
<%--		    	<input id="city" data-dojo-type="dijit/form/ValidationTextBox" --%>
<%--	                 	data-dojo-props='name:"city",trim:true,${fieldAcl.isReadOnly("city")},--%>
<%--							value:"${personInforEntity?.city}"--%>
<%--	                '/>--%>
	                
	            <select id="city" data-dojo-type="dijit/form/ComboBox" 
                 	data-dojo-props='name:"city",trim:true,${fieldAcl.isReadOnly("city")},
						value:"${personInforEntity?.city}"
                '>
	                <g:each in="${countryList}" var="item">
	                	<option value="${item.code }">${item.name }</option>
	                </g:each>
                </select>
		    </td>
		     
		</tr>
		<tr>
			<td><div align="right">民族：</div></td>
		  	<td>
<%--		    	<input id="nationality" data-dojo-type="dijit/form/ValidationTextBox" --%>
<%--	                 	data-dojo-props='name:"nationality",trim:true,${fieldAcl.isReadOnly("nationality")},--%>
<%--							value:"${personInforEntity?.nationality}"--%>
<%--	                '/>--%>
<%--	                --%>
	           <select id="nationality" data-dojo-type="dijit/form/ComboBox" 
                 	data-dojo-props='name:"nationality",trim:true,${fieldAcl.isReadOnly("nationality")},
						value:"${personInforEntity?.nationality}"
                '>
	                <g:each in="${nationList}" var="item">
	                	<option value="${item.code }">${item.name }</option>
	                </g:each>
                </select>
	                
	                
		    </td>
		 	<td><div align="right">出生地：</div></td>
		  	<td>
		    	<input id="birthAddress" data-dojo-type="dijit/form/ValidationTextBox" 
                 	data-dojo-props='name:"birthAddress",trim:true,${fieldAcl.isReadOnly("birthAddress")},
						value:"${personInforEntity?.birthAddress}"
                '/>
		    </td>
		</tr>
		
		<tr>
		 	<td><div align="right">籍贯：</div></td>
			<td>
		    	<input id="nativeAddress" data-dojo-type="dijit/form/ValidationTextBox" 
                 	data-dojo-props='name:"nativeAddress",trim:true,${fieldAcl.isReadOnly("nativeAddress")},
						value:"${personInforEntity?.nativeAddress}"
                '/>
			</td>
			<td><div align="right">政治面貌：</div></td>
		 	<td>
		    	<select id="politicsStatus" data-dojo-type="dijit/form/ComboBox" 
                 	data-dojo-props='name:"politicsStatus",trim:true,${fieldAcl.isReadOnly("politicsStatus")},
						value:"${personInforEntity?.politicsStatus}"
                '>
                	<g:each in="${politicsStatusList}" var="item">
	                	<option value="${item.code }">${item.name }</option>
	                </g:each>
                </select>
		    </td>
		</tr>
		
		<tr>
			<td><div align="right">婚姻状况：</div></td>
			  <td>
			  		<input id="marriage1" data-dojo-type="dijit/form/RadioButton"
		           		data-dojo-props='name:"marriage",type:"radio",${fieldAcl.isReadOnly("marriage")},
		           			<g:if test="${personInforEntity?.marriage=="已婚" }">checked:true,</g:if>
							value:"已婚"
	              	'/>
					<label for="marriage1">已婚</label>
				
	              	<input id="marriage2" data-dojo-type="dijit/form/RadioButton"
	           			data-dojo-props='name:"marriage",type:"radio",${fieldAcl.isReadOnly("marriage")},
	           			<g:if test="${personInforEntity?.marriage=="未婚" }">checked:true,</g:if>
						value:"未婚"
	              	'/>
					<label for="marriage2">未婚</label>
			    </td>
			 <td><div align="right">宗教信仰：</div></td>
			  <td>
			  		<input id="religion1" data-dojo-type="dijit/form/RadioButton"
		           		data-dojo-props='name:"religion",type:"radio",${fieldAcl.isReadOnly("religion")},
		           			<g:if test="${personInforEntity?.religion=="有" }">checked:true,</g:if>
							value:"有"
	              	'/>
					<label for="religion1">有</label>
				
	              	<input id="religion2" data-dojo-type="dijit/form/RadioButton"
	           			data-dojo-props='name:"religion",type:"radio",${fieldAcl.isReadOnly("religion")},
	           			<g:if test="${personInforEntity?.religion=="无" }">checked:true,</g:if>
						value:"无"
	              	'/>
					<label for="religion2">无</label>
			    </td>
		</tr>
		
		<tr>
		 	<td><div align="right">血型：</div></td>
		  	<td>
		    	<select id="blood" data-dojo-type="dijit/form/ComboBox" 
                 	data-dojo-props='name:"blood",trim:true,${fieldAcl.isReadOnly("blood")},
						value:"${personInforEntity?.blood}"
                '>
	                <g:each in="${bloodList}" var="item">
	                	<option value="${item.code }">${item.name }</option>
	                </g:each>
                </select>
                
		    </td>
	     	<td><div align="right">健康状况：</div></td>
		  	<td>
		    	<select id="health" data-dojo-type="dijit/form/ComboBox" 
                	data-dojo-props='name:"health",trim:true,${fieldAcl.isReadOnly("health")},
					value:"${personInforEntity?.health}"
               '>
              	 	<g:each in="${healthList}" var="item">
	                	<option value="${item.code }">${item.name }</option>
	                </g:each>
	            </select>
		    </td>
		</tr>
		
		<tr>
		 	<td><div align="right">户口所在地：</div></td>
		  	<td>
		    	<input id="householdRegi" data-dojo-type="dijit/form/ValidationTextBox" 
                 	data-dojo-props='name:"householdRegi",trim:true,${fieldAcl.isReadOnly("householdRegi")},
						value:"${personInforEntity?.householdRegi}"
                '/>
                
		    </td>
	     	<td><div align="right">档案转入时间：</div></td>
		  	<td>
		    	<input id="intoday" data-dojo-type="dijit/form/DateTextBox" 
                 	data-dojo-props='name:"intoday",trim:true,${fieldAcl.isReadOnly("intoday")},
						value:"${personInforEntity?.getFormatteIntoday()}"
                '/>
		    </td>
		</tr>
		
		<tr>
		 	<td><div align="right">专业技术等级：</div></td>
		  	<td>
		    	<select id="techGrade" data-dojo-type="dijit/form/ComboBox" 
                	data-dojo-props='name:"techGrade",trim:true,${fieldAcl.isReadOnly("techGrade")},
					value:"${personInforEntity?.techGrade}"
               '>
              	 	<g:each in="${techGradeList}" var="item">
	                	<option value="${item.code }">${item.name }</option>
	                </g:each>
	            </select>
		    </td>
	     	<td><div align="right">入职时间：</div></td>
		  	<td>
		    	<input id="staffOnDay" data-dojo-type="dijit/form/DateTextBox" 
                 	data-dojo-props='name:"staffOnDay",trim:true,${fieldAcl.isReadOnly("staffOnDay")},
						value:"${personInforEntity?.getFormatteStaffOnday()}"
                '/>
		    </td>
		</tr>
		<tr>
		 	<td><div align="right">毕业学校：</div></td>
			<td>
		    	<input id="schoolName" data-dojo-type="dijit/form/ValidationTextBox" 
                 	data-dojo-props='name:"schoolName",trim:true,${fieldAcl.isReadOnly("schoolName")},
						value:"${personInforEntity?.schoolName}"
                '/>
			</td>
			<td><div align="right">所学专业：</div></td>
		 	<td>
		    	<input id="major" data-dojo-type="dijit/form/ValidationTextBox" 
                 	data-dojo-props='name:"major",trim:true,${fieldAcl.isReadOnly("major")},
						value:"${personInforEntity?.major}"
                '/>
		    </td>
		</tr>
		<tr>
		 	<td><div align="right">学历：</div></td>
			<td>
		    	<input id="upDegree" data-dojo-type="dijit/form/ValidationTextBox" 
                 	data-dojo-props='name:"upDegree",trim:true,${fieldAcl.isReadOnly("upDegree")},
						value:"${personInforEntity?.upDegree}"
                '/>
			</td>
			<td><div align="right">工作岗位：</div></td>
		 	<td>
		    	<input id="workJob" data-dojo-type="dijit/form/ValidationTextBox" 
                 	data-dojo-props='name:"workJob",trim:true,${fieldAcl.isReadOnly("workJob")},
						value:"${personInforEntity?.workJob}"
                '/>
		    </td>
		</tr>
		<tr>
		 	<td><div align="right">参加工作时间：</div></td>
			<td>
		    	<input id="workJobDate" data-dojo-type="dijit/form/DateTextBox" 
                 	data-dojo-props='name:"workJobDate",trim:true,${fieldAcl.isReadOnly("workJobDate")},
						value:"${personInforEntity?.getFormatteWorkJobDate()}"
                '/>
			</td>
			<td><div align="right">备注：</div></td>
		 	<td>
		    	<input id="remark" data-dojo-type="dijit/form/ValidationTextBox" 
                 	data-dojo-props='name:"remark",trim:true,${fieldAcl.isReadOnly("remark")},
						value:"${personInforEntity?.remark}"
                '/>
		    </td>
		</tr>
	</table>
	<div>
		<img id="pic" src="${resource(dir:'images/staff',file:imgName)}" style="width:80px;height:100px"></img>
		<g:if test="${personInforEntity?.id && !onlyShow}">
		<div data-dojo-type="dijit/form/Button" data-dojo-props="label:'上传头像'" style="margin-top:10px;margin-left:5px">
			<script type="dojo/method" data-dojo-event="onClick">
				uploadPic();
			</script>
		</div>
		</g:if>
		<g:elseif test="${!personInforEntity.id}">
			<div style="margin-top:10px;margin-left:5px;color:red">保存后再上传头像</div>
		</g:elseif>
	</div>
</body>
</html>
