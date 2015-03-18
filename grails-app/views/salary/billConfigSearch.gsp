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
          
          <th width="8%">年度</th>
            <td width="10%">
            	<input id="year" data-dojo-type="dijit/form/ValidationTextBox" 
                	data-dojo-props='trim:true,
               '/>
            </td>
            
            <th width="8%">月份</th>
            <td width="10%">
            	<input id="month" data-dojo-type="dijit/form/ValidationTextBox" 
                	data-dojo-props='trim:true,
               '/>
            </td>
          
            <th width="8%">姓名</th>
            <td width="10%">
            	<input id="chinaName" data-dojo-type="dijit/form/ValidationTextBox" 
                	data-dojo-props='trim:true,
               '/>
            </td>
 
            <td>
            	<div class="btn">
                	<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){billConfig_search()}'>查询</button>
                	<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){billConfig_resetSearch()}'>重置条件</button>
              	</div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
	
</body>
</html>
