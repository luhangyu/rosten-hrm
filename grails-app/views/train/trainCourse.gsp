<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>培训班管理</title>
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
				trainCourse_save = function(){
					var courseName = registry.byId("courseName");
					if(!courseName.isValid()){
						rosten.alert("培训班名称不正确！").queryDlgClose = function(){
							courseName.focus();
						};
						return;
					}
					var organizeName = registry.byId("organizeName");
					if(!organizeName.isValid()){
						rosten.alert("组织者名称不正确！").queryDlgClose = function(){
							organizeName.focus();
						};
						return;
					}
					var trainDate = registry.byId("trainDate");
					if(!trainDate.isValid()){
						rosten.alert("培训时间不正确！").queryDlgClose = function(){
							trainDate.focus();
						};
						return;
					}
					var trainAddress = registry.byId("trainAddress");
					if(!trainAddress.isValid()){
						rosten.alert("培训地点不正确！").queryDlgClose = function(){
							trainAddress.focus();
						};
						return;
					}
					rosten.readSync(rosten.webPath + "/train/trainCourseSave",{},function(data){
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
		data-dojo-props='actionBarSrc:"${createLink(controller:'trainAction',action:'trainCourseForm',id:trainCourse?.id,params:[userid:user?.id])}"'>
	</div>
</div>

<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props=''>
		<form id="rosten_form" name="rosten_form" url='[controller:"assetConfig",action:"assetCategorySave"]' onsubmit="return false;" class="rosten_form" style="padding:0px">
			<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${trainCourse?.id }"' />
        	<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        	
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"基本信息",toggleable:false,moreText:"",height:"300px",marginBottom:"2px"'>
				<table border="0" width="740" align="left">
					<tr>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>培训班名称：</div></td>
					    <td width="250">
					    	<input id="courseName" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"courseName",${fieldAcl.isReadOnly("courseName")},
			                 		trim:true,required:true,"class":"input",
			                 		missingMessage:"请正确填写培训班名称！",invalidMessage:"请正确填写培训班名称！",
									value:"${trainCourse?.courseName}"
			                '/>
					    </td>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>组织者名称：</div></td>
					    <td width="250">
					    	<input id="organizeName" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"organizeName",${fieldAcl.isReadOnly("organizeName")},
			                 		trim:true,required:true,"class":"input",
			                 		missingMessage:"请正确填写组织者名称！",invalidMessage:"请正确填写组织者名称！",
									value:"${trainCourse?.organizeName}"
			                '/>
			           </td>
					</tr>
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>培训时间：</div></td>
					    <td>
					    	<input id="trainDate" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"trainDate",${fieldAcl.isReadOnly("trainDate")},
			                	trim:true,required:true,missingMessage:"请正确填写培训时间！",invalidMessage:"请正确填写培训时间！",
			                	value:"${trainCourse?.getFormatteTrainDate()}"
			               '/>
			            </td>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>培训地点：</div></td>
					    <td>
					    	<input id="trainAddress" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"trainAddress",${fieldAcl.isReadOnly("trainAddress")},
			                 		trim:true,required:true,"class":"input",
			                 		missingMessage:"请正确填写培训地点！",invalidMessage:"请正确填写培训地点！",
									value:"${trainCourse?.trainAddress}"
			                '/>
			            </td>    
					</tr>
					<tr>
					    <td><div align="right">培训对象：</div></td>
					    <td colspan=3>
					    	<input id="trainObject" data-dojo-type="dijit/form/ValidationTextBox" 
			                	data-dojo-props='name:"trainObject",${fieldAcl.isReadOnly("trainObject")},
			                	trim:true,
			                	style:{width:"550px"},
			                	value:"${trainCourse?.trainObject}"
			               '/>
			            </td>
					       
					</tr>
					<tr>
					    <td><div align="right">备注：</div></td>
					    <td  colspan=3>
					    	<textarea id="description" data-dojo-type="dijit/form/SimpleTextarea" 
    							data-dojo-props='name:"description","class":"input",
                               		style:{width:"550px"},rows:"10",
                               		trim:true
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