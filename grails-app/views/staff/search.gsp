<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<style type="text/css">
	
</style>
<script type="text/javascript">	

</script>
</head>
<body>
	<div class="searchtab">
      <table width="100%" border="0">
        
        <tbody>
          <tr>
            <th width="8%">登录名</th>
            <td width="18%">
            	<input id="s_username" data-dojo-type="dijit/form/ValidationTextBox" 
                	data-dojo-props='trim:true
               '/>
            </td>
            <th width="8%">姓名</th>
            <td width="18%">
            	<input id="s_chinaName"  data-dojo-type="dijit/form/ValidationTextBox" 
                	data-dojo-props='trim:true
               '/>
            </td>
            <th width="8%">部门</th>
            <td width="18%">
            	<div id="s_departName" data-dojo-type="dijit/form/ComboBox"
	                data-dojo-props='trim:true,value:""
	            '>
	            	 <g:each in="${departList}" var="item">
	                	<option value="${item.departName }">${item.departName }</option>
	                </g:each>
	            </div>
            </td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <th>工作岗位</th>
            <td>
            	<input id="s_workJob" data-dojo-type="dijit/form/ValidationTextBox" 
                	data-dojo-props='trim:true
               '/>
            </td>
            <th>性别</th>
            <td>
            	<select id="s_sex"  data-dojo-type="dijit/form/ComboBox" 
                	data-dojo-props='trim:true,value:""
               '>
               		<option value="男">男</option>
	            	<option value="女">女</option>
	           </select>
            </td>
            <th>状态</th>
            <td>
            	<select id="s_status" data-dojo-type="dijit/form/ComboBox"
	                data-dojo-props='trim:true,value:""
	            '>
	            	<g:each in="${statusList}" var="item">
	                	<option value="${item}">${item}</option>
	                </g:each>
	            </select>
            </td>
            <td>
            	<div class="btn">
                	<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){staff_search()}'>查询</button>
                	<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){staff_resetSearch()}'>重置</button>
              	</div>
            </td>
          </tr>
          <tr style="display:none">
          	<th>身份证号</th>
            <td>
            	<input id="s_idCard" data-dojo-type="dijit/form/ValidationTextBox" 
                	data-dojo-props='trim:true
               '/>
            </td>
            <th>籍贯</th>
            <td>
            	<input id="s_nativeAddress" data-dojo-type="dijit/form/ValidationTextBox" 
                	data-dojo-props='trim:true
               '/>
            </td>
            <th>国籍</th>
            <td>
            	<input id="s_city"  data-dojo-type="dijit/form/ValidationTextBox" 
                	data-dojo-props='trim:true
               '/>
            </td>
            <th>政治面貌</th>
            <td>
            	<select id="s_politicsStatus" data-dojo-type="dijit/form/ComboBox"
	                data-dojo-props='trim:true,value:""
	            '>
	            	<g:each in="${politicsStatusList}" var="item">
	                	<option value="${item.code }">${item.name }</option>
	                </g:each>
	            </select>
            </td>
            <td>&nbsp;</td>
            
          </tr>
        </tbody>
      </table>
    </div>
	
</body>
</html>
