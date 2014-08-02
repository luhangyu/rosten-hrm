<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>附件上传</title>
    <style type="text/css">
		body{
			overflow:auto;
		}
    </style>
	<script type="text/javascript">
		require(["dojo/parser",
		 		"dojo/_base/kernel",
		 		"dijit/registry",
		 		"dojo/_base/xhr",
		 		"dijit/form/ValidationTextBox",
		 		"dijit/form/SimpleTextarea",
		 		"dijit/form/RadioButton",
		 		"dijit/form/Button",
		 		"dijit/form/DateTextBox",
		 		"dijit/form/NumberTextBox",
		 		"dijit/form/Form",
		     	"rosten/widget/ActionBar",
		     	"rosten/app/Application",
		     	"rosten/kernel/behavior"],
			function(parser,kernel,registry,xhr,datestamp,DepartUserDialog){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}"});
					rosten.cssinit();
				});
				
			downloadFile_add = function(){
				var subject = registry.byId("subject");
				if(!subject.isValid()){
					rosten.alert("标题不正确！").queryDlgClose = function(){
						subject.focus();
					};
					return;
				}
				
				rosten.readSync(rosten.webPath + "/publicc/downloadFileSave",{},function(data){
					if(data.result=="true" || data.result == true){
						rosten.alert("保存成功！").queryDlgClose= function(){
							if(window.location.href.indexOf(data.id)==-1){
								window.location.replace(window.location.href + "&id=" + data.id);
							}else{
								window.location.reload();
							}
							
						};
					}else{
						rosten.alert("保存失败!");
					}
				},null,"rosten_form");
			}
			page_quit = function(){
				rosten.pagequit();
				//showStartDownloadFile("${user?.id}","${company?.id }");
			};
		});
		
		
    </script>
</head>
<body>
	<div class="rosten_action">
		<div data-dojo-type="rosten/widget/ActionBar" id="rosten_actionBar" data-dojo-props='actionBarSrc:"${createLink(controller:'publiccAction',action:'downloadFileForm',id:downloadFile?.id,params:[userid:user?.id])}"'></div>
	</div>
	<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
	  	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props=''>
        	<form class="rosten_form" id="rosten_form" onsubmit="return false;" style="padding:0px">
        		<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${downloadFile?.id }"' />
        		<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        	  	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"基本信息",toggleable:false,moreText:"",height:"240px",marginBottom:"2px"'>
        	  
                <table class="tableData" style="width:740px;margin:0px">
                    <tbody>
                       <tr>
						    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>首页显示：</div></td>
						    <td width="250">
						    	<input id="isAnonymous1" data-dojo-type="dijit/form/RadioButton"
                             		data-dojo-props='name:"isShow",
                             			type:"radio",
                             			${fieldAcl.isReadOnly("isShow")},
                             			<g:if test="${downloadFile?.isShow }">checked:true,</g:if>
              							value:"true"
                                '/>
								<label for="isAnonymous1">是</label>
								
                                <input id="isAnonymous2" data-dojo-type="dijit/form/RadioButton"
                             		data-dojo-props='name:"isShow",
                             			type:"radio",
                             			${fieldAcl.isReadOnly("isShow")},
                             			<g:if test="${!downloadFile?.isShow }">checked:true,</g:if>
              							value:"false"
                                '/>
								<label for="isAnonymous2">否</label>
						    </td>
						    <td width="120" name="numberTd"><div align="right"><span style="color:red">*&nbsp;</span>显示顺序：</div></td>
						    <td width="250" name="numberTd">
						    	<select id="number" data-dojo-type="dijit/form/NumberTextBox" 
					                data-dojo-props='name:"number",${fieldAcl.isReadOnly("number")},
					                trim:true,
				                 	required:true,
				                 	style:{width:"60px"},
					      			value:"${downloadFile?.number}"
					            '>
					    	</select>
				           </td>
						</tr>
						
						<tr>
						    <td><div align="right"><span style="color:red">*&nbsp;</span>标题：</div></td>
						    <td colspan=3>
						    	<input id="subject" data-dojo-type="dijit/form/ValidationTextBox" 
				                 	data-dojo-props='name:"subject",${fieldAcl.isReadOnly("subject")},
				                 		trim:true,
				                 		required:true,
				                 		style:{width:"490px"},
										value:"${downloadFile?.subject}"
				                '/>
						    
						    </td>    
						</tr>
						<tr>
						    <td><div align="right">描述：</td>
						    <td colspan=3>
						    	
						    	<textarea id="description" data-dojo-type="dijit/form/SimpleTextarea" 
	    							data-dojo-props='name:"description","class":"input",
	                               		style:{width:"490px"},rows:"10",${fieldAcl.isReadOnly("description")},
	                               		trim:true,
										value:"${downloadFile?.description}"
	                           '>
	    						</textarea>
						    						    
						    </td>    
						</tr>
						
                    </tbody>
                </table>
                </div>
			</form>
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"附件信息",toggleable:false,moreText:"",
				height:"60px",href:"${createLink(controller:'publicc',action:'getFileUpload',id:downloadFile?.id)}"'>
			</div>
			
		</div>
	</div>
</body>
</html>