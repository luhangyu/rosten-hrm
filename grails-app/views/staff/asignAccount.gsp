<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>分配账号</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
  </head>
  
<body>
	<div style="text-Align:center">
        <form class="rosten_form" id="rosten_form" onsubmit="return false;" style="width:400px;text-align:left">
        	<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${user?.id}"' />
        	<input  data-dojo-type="dijit/form/ValidationTextBox" id="personInforId"  data-dojo-props='name:"personInforId",style:{display:"none"},value:"${personInforId}"' />
            <fieldset class="fieldset-form">
                <legend class="tableHeader">分配账号</legend>
                <table class="tableData">
                    <tbody>
						<tr>
                            <td width="80">
                                <div align="right"><span style="color:red">*&nbsp;</span>账号：</div>
                            </td>
                            <td  width="250">
                            <%--
                            	<input id="userNameFront" data-dojo-type="dijit/form/ValidationTextBox" 
	                               	data-dojo-props='name:"userNameFront",style:{width:"50px"},
	             						value:"${company?.shortName}-",disabled:true
	                            '/> --%>
						    	
					    		<input id="username" data-dojo-type="dijit/form/ValidationTextBox" 
	                               	data-dojo-props='name:"username",
	                               		trim:true,required:true,
	                               		promptMessage:"请正确输入账号...",
	                               		<%--style:{width:"125px"}, --%>
	                               		value:"${username}"
	                               '/>
                            </td>
                        </tr>
                        <tr>
                           <td>
                                <div align="right" >具有角色：</div>
                            </td>
                            <td>
                            	<input id="allowrolesName" data-dojo-type="dijit/form/ValidationTextBox"
                					data-dojo-props='trim:true,readOnly:true,
                						value:"${allowrolesName }"
                				'/>
                 				
                				<g:hiddenField name="allowrolesId" value="${allowrolesId }" />
								<button data-dojo-type="dijit.form.Button" 
									data-dojo-props = 'onClick:function(){selectRole("${createLink(controller:'system',action:'roleSelect',params:[companyId:company?.id])}")}'
								>选择</button>
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                <div align="right"><span style="color:red">*&nbsp;</span>密码：</div>
                            </td>
                            <td>
                            	<input id="password" data-dojo-type="dijit/form/ValidationTextBox" 
	                               	data-dojo-props='name:"password",
	                               		type:"password",
	                               		trim:true,
	                               		required:true,
	                               		promptMessage:"请正确输入密码...",
	             						value:"${user?.password}"
	                           	'/>
                            </td>
                        </tr>
                        
                        <tr>
                            <td>
                                <div align="right"><span style="color:red">*&nbsp;</span>确认密码：</div>
                            </td>
                            <td>
                            	<input id="passwordcheck" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='
                                		type:"password",
                                		trim:true,
                                		required:true,
                                		promptMessage:"请正确输入密码...",
              							value:"${user?.password}"
                                '/>
                            </td>
                        </tr>
                        
                        <tr>
                            <td><div align="right"><span style="color:red">*&nbsp;</span>CSS样式：</div></td>
					    <td>
					    	<select id="cssStyle" data-dojo-type="dijit/form/FilteringSelect"
                           		data-dojo-props='name:"cssStyle",
                           			autoComplete:false,
            						value:"${(user!=null && user.cssStyle!=null)?user.cssStyle:"normal" }"
                            '>
	                            <option value="normal">标准样式</option>
								<option value="cfyl">春风杨柳</option>
								<option value="hbls">环保绿色</option>
								<option value="jqsy">金秋十月</option>	
								<option value="jsnz">金色农庄</option>
								<option value="lhqh">蓝灰情怀</option>
								<option value="rose">浪漫玫瑰</option>
								<option value="shys">深红夜思</option>
                              
                           	</select>
			            </td>
                        </tr>
                        
						<tr>
							<td></td>
							<td>
								<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){asignAccount_Submit()}'>确定</button>
								<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){rosten.kernel.hideRostenShowDialog()}'>取消</button>
								
							</td>
						</tr>
                    </tbody>
                </table>
				
				
            </fieldset>
		</form>
	</div>
</body>
</html>
