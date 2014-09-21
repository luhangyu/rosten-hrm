<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>学历学位进修</title>
    <style type="text/css">
    	.rosten .dsj_form table tr{
    		height:30px;
    		
    	}
    	body{
			overflow:auto;
		}
    </style>
	<script type="text/javascript">
	require(["dojo/parser",
		 		"dojo/_base/kernel",
		 		"dijit/registry",
		 		"dijit/layout/TabContainer",
		 		"dijit/layout/ContentPane",
		 		"dijit/form/ValidationTextBox",
		 		"dijit/form/DateTextBox",
		 		"dijit/form/SimpleTextarea",
		 		"dijit/form/Button",
		     	"rosten/widget/ActionBar",
		     	"rosten/widget/TitlePane",
		     	"rosten/app/Application",
		     	"rosten/kernel/behavior"],
			function(parser,kernel,registry){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}"});
					rosten.cssinit();
				});
				
				degreeStudy_save = function(){
					
					rosten.readSync(rosten.webPath + "/train/degreeStudySave",{},function(data){
						if(data.result=="true" || data.result == true){
							rosten.alert("保存成功！").queryDlgClose= function(){
								page_quit();
							};
						}else{
							rosten.alert("保存失败!");
						}
					},null,"rosten_form");
				};
				page_quit = function(){
					rosten.pagequit();
				};
		});
    </script>
</head>
<body>
<div class="rosten_action">
	<div data-dojo-type="rosten/widget/ActionBar" data-dojo-id="rosten_actionBar" 
		data-dojo-props='actionBarSrc:"${createLink(controller:'trainAction',action:'degreeStudyForm',id:degreeStudy?.id,params:[userid:user?.id])}"'>
	</div>
</div>

<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props=''>
		<form id="rosten_form" name="rosten_form" url='[controller:"assetConfig",action:"assetCategorySave"]' onsubmit="return false;" class="rosten_form" style="padding:0px">
			<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${degreeStudy?.id }"' />
        	<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        	
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"基本信息",toggleable:false,moreText:"",height:"300px",marginBottom:"2px"'>
				<table border="0" width="740" align="left">
		<tr>
		 		<td><div align="right">姓名：</div></td>
					    <td >
					    	<input id="userName" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='trim:true,readOnly:true,
									value:"${degreeStudy?.getUserName()}"
			                '/>
			</td>
		</tr>
		
		<tr>
		 		<td><div align="right">部门：</div></td>
					    <td >
					    	<input id="departName" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='trim:true,readOnly:true,
									value:"${degreeStudy?.getFormattedDepartName()}"
			                '/>
			</td>
		</tr>
		
		<tr>
		 		<td><div align="right">拟进修院校：</div></td>
					    <td >
					    	<input id="School" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"School",style:{width:"350px"},
              							value:"${degreeStudy?.School}"
                                	'/>
			</td>
		</tr>
		
		<tr>
		 		<td><div align="right">拟进修专业：</div></td>
					    <td >
					    	<input id="major" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"major",style:{width:"350px"},
              							value:"${degreeStudy?.major}"
                                	'/>
			</td>
		</tr>
		
		<tr>
		 		<td><div align="right">拟攻读学历：</div></td>
					    <td >
					    	<input id="Education" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"Education",style:{width:"350px"},
              							value:"${degreeStudy?.Education}"
                                	'/>
			</td>
		</tr>
		
		<tr>
		 		<td><div align="right">拟攻读学位：</div></td>
					    <td >
					    	<input id="degree" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"degree",style:{width:"350px"},
              							value:"${degreeStudy?.degree}"
                                	'/>
			</td>
		</tr>
		
		<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>开始时间：</div></td>
					    <td>
					    	<input id="startDate" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"startDate",${fieldAcl.isReadOnly("startDate")},
			                	trim:true,required:true,missingMessage:"请正确填写开始时间！",invalidMessage:"请正确填写开始时间！",
			                	value:"${degreeStudy?.getFormatteStartDate()}"
			               '/>
			            </td>
			            </tr>
			            
		
				<tr>
					<td><div align="right"><span style="color:red">*&nbsp;</span>结束时间：</div></td>
					    <td>
					    	<input id="endDate" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"endDate",${fieldAcl.isReadOnly("endDate")},
			                	trim:true,required:true,missingMessage:"请正确填写结束时间！",invalidMessage:"请正确填写结束时间！",
			                	value:"${degreeStudy?.getFormatteEndDate()}"
			               '/>
			            </td>
			            </tr>
			            
			       <tr>
					<td><div align="right"><span style="color:red">*&nbsp;</span>进修学费：</div></td>
					    <td>
					    	<input id="tuition" data-dojo-type="dijit/form/NumberTextBox" 
			                	data-dojo-props='name:"tuition",${fieldAcl.isReadOnly("endDate")},
			                	trim:true,required:true,missingMessage:"请正确填写进修学费！",invalidMessage:"请正确填写进修学费！",
			                	value:"${degreeStudy?.tuition}"
			               '/>
			            </td>
			            </tr>
		
				
					
				</table>
			</div>
			
		</form>
	</div>
</div>
</body>