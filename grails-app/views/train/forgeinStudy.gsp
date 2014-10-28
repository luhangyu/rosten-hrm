<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>出国进修</title>
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
					
					rosten.readSync(rosten.webPath + "/train/forgeinStudySave",{},function(data){
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
        	
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"基本信息",toggleable:false,moreText:"",height:"720px",marginBottom:"2px"'>
				<table border="0" width="740" align="left">
					<tr>
				 		<td width="20%"><div align="right"><span style="color:red">*&nbsp;</span>姓名：</div></td>
					    <td width="30%">
					    	<input id="userName" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='trim:true,readOnly:true,
									value:"${forgeinStudy?.getUserName()}"
			                '/>
						</td>
						
						<td width="20%"><div align="right"><span style="color:red">*&nbsp;</span>部门：</div></td>
					    <td  width="30%">
					    	<input id="departName" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='trim:true,readOnly:true,
									value:"${forgeinStudy?.getFormattedDepartName()}"
			                '/>
						</td>
						
					</tr>
					<tr>
					 		<td><div align="right"><span style="color:red">*&nbsp;</span>申请年度：</div></td>
						    <td >
						    	<input id="appYear" data-dojo-type="dijit/form/DateTextBox" 
                               		data-dojo-props='name:"appYear",trim:true,required:true,
             							value:"${forgeinStudy?.appYear}"
                               	'/>
							</td>
				
					 		<td><div align="right">出国国别：</div></td>
								    <td >
								    	<input id="country" data-dojo-type="dijit/form/ValidationTextBox" 
			                                	data-dojo-props='name:"country",
			              							value:"${forgeinStudy?.country}"
			                                	'/>
						</td>
					</tr>
					
					<tr>
					 		<td><div align="right">留学层次：</div></td>
								    <td >
								    	<input id="studyLevel" data-dojo-type="dijit/form/ValidationTextBox" 
			                                	data-dojo-props='name:"studyLevel",
			              							value:"${forgeinStudy?.studyLevel}"
			                                	'/>
						</td>
					
					 		<td><div align="right">留学身份：</div></td>
								    <td >
								    	<input id="studyIdentity" data-dojo-type="dijit/form/ValidationTextBox" 
			                                	data-dojo-props='name:"studyIdentity",
			              							value:"${forgeinStudy?.studyIdentity}"
			                                	'/>
						</td>
					</tr>
					
					<tr>
					 		<td><div align="right">学历层次：</div></td>
								    <td >
								    	<input id="educationLevel" data-dojo-type="dijit/form/ValidationTextBox" 
			                                	data-dojo-props='name:"educationLevel",
			              							value:"${forgeinStudy?.educationLevel}"
			                                	'/>
						</td>
					
					 		<td><div align="right">留学项目：</div></td>
								    <td >
								    	<input id="program" data-dojo-type="dijit/form/ValidationTextBox" 
			                                	data-dojo-props='name:"program",
			              							value:"${forgeinStudy?.program}"
			                                	'/>
						</td>
					</tr>
					
						<tr>
					 		<td><div align="right">学科：</div></td>
								    <td >
								    	<input id="discipline" data-dojo-type="dijit/form/ValidationTextBox" 
			                                	data-dojo-props='name:"discipline",
			              							value:"${forgeinStudy?.discipline}"
			                                	'/>
						</td>
					
					 		<td><div align="right">专业：</div></td>
								    <td >
								    	<input id="major" data-dojo-type="dijit/form/ValidationTextBox" 
			                                	data-dojo-props='name:"major",
			              							value:"${forgeinStudy?.major}"
			                                	'/>
						</td>
					</tr>
					
					<tr>
					    <td><div align="right">出国时间：</div></td>
					    <td>
					    	<input id="abroadDate" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"abroadDate",${fieldAcl.isReadOnly("abroadDate")},
			                	trim:true,required:true,missingMessage:"请正确填写开始时间！",invalidMessage:"请正确填写开始时间！",
			                	value:"${forgeinStudy?.getFormatteAbroadDate()}"
			               '/>
			            </td>
			       
					<td><div align="right">回国时间：</div></td>
					    <td>
					    	<input id="returneDate" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"returneDate",${fieldAcl.isReadOnly("returneDate")},
			                	trim:true,required:true,missingMessage:"请正确填写结束时间！",invalidMessage:"请正确填写结束时间！",
			                	value:"${forgeinStudy?.getFormatteReturneDate()}"
			               '/>
			            </td>
			            </tr>
			            
			            <tr>
					    <td><div align="right">出国累计时间：</div></td>
					    <td>
					    <input id="cumulativeTime" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"cumulativeTime",
              							value:"${forgeinStudy?.cumulativeTime}"
                                	'/>
					    </td>
					    </tr>
			            
			       <tr>
					<td><div align="right">科研情况：</div></td>
					    <td colspan="3">
					     <textarea id="researchStatus" data-dojo-type="dijit/form/SimpleTextarea" 
    							data-dojo-props='name:"researchStatus","class":"input",
                               		style:{width:"550px"},rows:"6",
                               		trim:true,value:"${forgeinStudy?.researchStatus}"
                           '>
					    </textarea>
					    </td>
			       </tr>
		
		 			<tr>
					<td><div align="right">专利情况：</div></td>
					    <td colspan="3">
					     <textarea id="patentStatus" data-dojo-type="dijit/form/SimpleTextarea" 
    							data-dojo-props='name:"patentStatus","class":"input",
                               		style:{width:"550px"},rows:"6",
                               		trim:true,value:"${forgeinStudy?.patentStatus}"
                           '>
					    </textarea>
					    </td>
			       </tr>
		
		
				 <tr>
					<td><div align="right">论文情况：</div></td>
					    <td colspan="3">
					     <textarea id="paperStatus" data-dojo-type="dijit/form/SimpleTextarea" 
    							data-dojo-props='name:"paperStatus","class":"input",
                               		style:{width:"550px"},rows:"6",
                               		trim:true,value:"${forgeinStudy?.paperStatus}"
                           '>
					    </textarea>
					    
					    </td>
			       </tr>
		
		 		<tr>
					<td><div align="right">备注：</div></td>
					    <td colspan="3">
					    <textarea id="remark" data-dojo-type="dijit/form/SimpleTextarea" 
    							data-dojo-props='name:"remark","class":"input",
                               		style:{width:"550px"},rows:"6",
                               		trim:true,value:"${forgeinStudy?.remark}"
                           '>
					    </textarea>
					    </td>
			       </tr>
		
				
					
				</table>
			</div>
			
		</form>
	</div>
</div>
</body>