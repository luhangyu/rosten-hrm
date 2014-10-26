<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>合同管理</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
</style>
<body>
	<table border="0" width="740" align="left">
		<tr>
		 <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>员工姓名：</div></td>
	    <td width="250">
	    	<input  data-dojo-type="dijit/form/ValidationTextBox" id="personInforId"  data-dojo-props='name:"personInforId",style:{display:"none"},value:"${personInfor?.id }"' />
	    	<input id="personInforName" data-dojo-type="dijit/form/ValidationTextBox" 
                	data-dojo-props='trim:true,required:true,value:"${bargain?.getPersonInforName() }"
               '/>
               <button data-dojo-type='dijit/form/Button' 
				data-dojo-props="label:'选择',iconClass:'docAddIcon'">
				<script type="dojo/method" data-dojo-event="onClick">
					addPersonInfor();
				</script>
			</button>
	    </td>
	    
	     <td width="120"><div align="right">性别：</div></td>
	     <td width="250">
	    	<input id="sex" data-dojo-type="dijit/form/ValidationTextBox" 
                	data-dojo-props='trim:true,required:true,value:"${personInfor?.sex }"
               '/>
	    </td>
	    
		</tr>
		
		<tr>
			 <td width="120"><div align="right">部门：</div></td>
		    <td width="250">
		    	<input id="departName" data-dojo-type="dijit/form/ValidationTextBox" 
	                	data-dojo-props='trim:true,required:true,value:"${personInfor?.currentDepart}"
	               '/>
		    </td>
		    
		     <td width="120"><div align="right">状态：</div></td>
		     <td width="250">
		    	<input id="status" data-dojo-type="dijit/form/ValidationTextBox" 
	                	data-dojo-props='trim:true,required:true,value:""
	               '/>
		    </td>
	    
		</tr>
		
	</table>
	
	<div id="chooseDialog" data-dojo-type="dijit/Dialog" class="displayLater" data-dojo-props="title:'人员选择',style:'width:800px;height:450px'">
		<div id="chooseWizard" data-dojo-type="dojox/widget/Wizard" 
			data-dojo-props='previousButtonLabel:"上一步",nextButtonLabel:"下一步"' style="width:780px; height:410px;padding:0px">
			
			<div data-dojo-type="dojox/widget/WizardPane" 
				data-dojo-props='passFunction:addPersonInforNext,style:{padding:"0px"},
					href:"${createLink(controller:'staff',action:'getChooseListSearch')}"
			'></div>
			<div data-dojo-type="dojox/widget/WizardPane" data-dojo-props='canGoBack:"true",doneFunction:addPersonInforDone,style:{padding:"0px"}' >
				<div id="chooseList" data-dojo-id="chooseList" data-dojo-type="dijit/layout/ContentPane" 
					data-dojo-props='style:{overflow:"auto",padding:"1px"}'>
					
					<div data-dojo-type="rosten/widget/RostenGrid" id="chooseListGrid" data-dojo-id="chooseListGrid"
						data-dojo-props='url:"${createLink(controller:'staff',action:'staffGrid',params:[companyId:company?.id])}"'></div>
				</div>
			</div>
			<script type="dojo/method" event="cancelFunction">
				dijit.byId("chooseDialog").hide();
			</script>
		</div>
	</div>
	
</body>
</html>
