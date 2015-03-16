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
            <th width="5%">员工名称</th>
            <td width="18%">
            	<input id="s_chinaName" data-dojo-type="dijit/form/ValidationTextBox" 
                	data-dojo-props='trim:true
               '/>
            </td>
            <th width="10%">所属部门</th>
            <td width="18%">
            	<div id="s_departName" data-dojo-type="dijit/form/ComboBox"
	                data-dojo-props='trim:true,value:""
	            '>
	            	<g:each in="${departList}" var="item">
	                	<option value="${item.departName }">${item.departName }</option>
	                </g:each>
	            </div>
            </td>
            <th width="5%">考勤日期</th>
            <td width="18%">
            	<input id="s_workCheckDate"  data-dojo-type="dijit/form/DateTextBox" 
                	data-dojo-props='trim:true
               '/>
            </td>
            <td>
            	<div class="btn">
                	<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){workCheck_search()}'>查询</button>
                	<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){workCheck_resetSearch()}'>重置条件</button>
              	</div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
	
</body>
</html>
