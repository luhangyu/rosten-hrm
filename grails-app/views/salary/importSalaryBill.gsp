<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>批量导入</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <style type="text/css">
    	.rosten #file_form table tr{
    		height:50px;
    	}
    </style>
  </head>
  
<body>
	<div style="text-Align:center">
           <form data-dojo-type="dijit/form/Form" method="post" class="rosten_form" id="file_form" style="width:560px;text-align:left" 
        	target="upload_iframe" enctype="multipart/form-data" action="${createLink(controller:'salary',action:'importSalary',params:[])}">
            <fieldset class="fieldset-form">
                <legend class="tableHeader">工资单批量导入</legend>
                <table class="tableData upload"  style="margin:2px;">
                    <tbody>
                    <tr>
                     <td width="80">
                        <div align="right" style="margin-top:10px"><span style="color:red">*</span>年份：</div>
                     </td>
                     <td width="100">
                         	<select id="importYear" data-dojo-type="dijit/form/ComboBox"
		                			data-dojo-props='name:"year",required:true,trim:true,value:"",style:{width:"100px"}
					            '>
									<option value="2014">2014</option>
									<option value="2015">2015</option>
									<option value="2016">2016</option>
									<option value="2017">2017</option>
									<option value="2018">2018</option>
					    		</select>
                     </td>
                     <td width="80">
                     	<div align="right" style="margin-top:10px"><span style="color:red">*</span>月份：</div>
                     </td>
                     <td>
                     	<select id="importMonth" data-dojo-type="dijit/form/ComboBox"
				                data-dojo-props='name:"month",required:true,trim:true,value:"",style:{width:"100px"}
				            '>
								<option value="1">1</option>
								<option value="2">2</option>
								<option value="3">3</option>
								<option value="4">4</option>
								<option value="5">5</option>
								<option value="6">6</option>
								<option value="7">7</option>
								<option value="8">8</option>
								<option value="9">9</option>
								<option value="10">10</option>
								<option value="11">11</option>
								<option value="12">12</option>
				    		</select>
                     </td>       
                    </tr>
						<tr>
                            <td width="80">
                                <div align="right" style="margin-top:10px"><span style="color:red">*</span>文件地址：</div>
                            </td>
                             <td width="400" colspan="3">
                            	<input data-dojo-type="dojox/form/FileInput" data-dojo-props= 'label:"浏览...",cancelText:"清空",required:true' name="uploadedfile" />
                            </td>
                        </tr>
                        
						<tr style="text-align:center">
							<td colspan="4">
								<div style="margin-top:10px">
								
								<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(object){import_salaryBill_submit(object)}'>确定</button>
								<button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){rosten.kernel.hideRostenShowDialog()}'>取消</button>
								</div>
							</td>
						</tr>
                    </tbody>
                </table>
				
				
            </fieldset>
            </form>
            <iframe name="upload_iframe" style="display:none"></iframe>
	</div>
</body>
</html>
