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
            <th width="5%">申请单编号</th>
            <td width="18%">
            	<input id="s_applyNum" data-dojo-type="dijit/form/ValidationTextBox" 
                	data-dojo-props='trim:true
               '/>
            </td>
            <th width="5%">出差时间</th>
            <td width="18%">
            	<input id="s_travelDate"  data-dojo-type="dijit/form/DateTextBox" 
                	data-dojo-props='trim:true
               '/>
            </td>
            <!--
            <th width="5%">状态</th>
            <td width="18%">
            	<select id="s_status" data-dojo-type="dijit/form/ComboBox"
	                data-dojo-props='trim:true,value:""
	            '>
					<g:each in="${statusList}" var="item">
	            		<option value="${item }">${item }</option>
	            	</g:each>
	    		</select>
            
            </td>  -->
            <td>
            	<div class="btn">
                	<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){travel_search()}'>查询</button>
                	<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){travel_resetSearch()}'>重置条件</button>
              	</div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
	
</body>
</html>
