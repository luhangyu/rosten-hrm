<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>员工聘任</title>
    <style type="text/css">
		body{
			overflow:auto;
		}
		.rosten .rostenGridView .pagecontrol{
			text-align:left;
		}
    </style>
	<script type="text/javascript">
		require(["dojo/parser",
		 		"dojo/_base/kernel",
		 		"dijit/registry",
		 		"dojo/_base/xhr",
		 		"dojo/dom",
		 		"dojo/_base/lang",
		 		"dojo/date/stamp",
		 		"rosten/widget/DepartUserDialog",
		 		"dijit/form/ValidationTextBox",
		 		"dijit/form/SimpleTextarea",
		 		"dijit/form/Button",
		 		"dijit/form/DateTextBox",
		 		"dijit/form/FilteringSelect",
		 		"dijit/form/DropDownButton",
		 		"dijit/form/Form",
		 		"dojox/form/Uploader",
		 		"dojox/form/uploader/FileList",
		 		"dojox/widget/Wizard",
				"dojox/widget/WizardPane",
		     	"rosten/widget/ActionBar",
		     	"rosten/app/Application",
		     	"rosten/app/SystemApplication",
		     	"rosten/app/StaffApplication",
		     	"rosten/kernel/behavior"],
			function(parser,kernel,registry,xhr,dom,lang,datestamp,DepartUserDialog){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}"});
					rosten.cssinit();
				});
				
			engage_add = function(object){

				var formWidget = registry.byId("rosten_form");
				if(!formWidget.validate()){
					rosten.alert("请正确填写相关信息！");
					return;
				}

				var reasonDom = registry.byId("reason");
				var reason = reasonDom.get("value");
				if(reason==""){
					rosten.alert("聘任理由不能为空！").queryDlgClose = function(){
						reasonDom.focus();
					};
					return false;
				}
				
				var content = {};

				//添加新增时添加附件功能
				<g:if test="${!engage?.id}">
					var filenode = dom.byId("fileUpload_show");
					var fileIds = [];

			       	var node=filenode.firstChild;
			       	while(node!=null){
			            node=node.nextSibling;
			            if(node!=null){
			            	fileIds.push(node.getAttribute("id"));
				        }
			        }
					content.attachmentIds = fileIds.join(",");
				</g:if>
				
				//增加对多次单击的次数----2014-9-4
				var buttonWidget = object.target;
				rosten.toggleAction(buttonWidget,true);
				
				rosten.readSync(rosten.webPath + "/staff/engageSave",content,function(data){
					if(data.result=="true" || data.result == true){
						rosten.alert("保存成功！").queryDlgClose= function(){
							if(window.location.href.indexOf(data.id)==-1){
								window.location.replace(window.location.href + "&id=" + data.id);
							}else{
								window.location.reload();
							}
						};
					}else if(data.result=="noConfig"){
						rosten.alert("系统不存在配置文档，请通知管理员！");
					}else{
						rosten.alert("保存失败!");
					}
					rosten.toggleAction(buttonWidget,false);
				},function(error){
					rosten.alert("系统错误，请通知管理员！");
					rosten.toggleAction(buttonWidget,false);
				},"rosten_form");
			};
			engage_public = function(){
				rosten.readSync(rosten.webPath + "/staff/engagePublish",{id:"${engage?.id}"},function(data){
					if(data.result==true || data.result=="true"){
						rosten.alert("发布成功，请在首页查看！").queryDlgClose = function(){
							//刷新首页bbs内容
							window.opener.showStartBbs("${user?.id}","${company?.id }");
							rosten.pagequit();
						};
					}else{
						rosten.alert("发布失败，请通知管理员！");
					}
				});
			};
			page_quit = function(){
				rosten.pagequit();
			};
		});
		
		
    </script>
</head>
<body>
	<div class="rosten_action">
		<div data-dojo-type="rosten/widget/ActionBar" id="rosten_actionBar" data-dojo-props='actionBarSrc:"${createLink(controller:'staffAction',action:'engageForm',id:engage?.id,params:[userid:user?.id])}"'></div>
	</div>
	<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
	  	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props='style:{height:"600px"}'>
	  		<form method="post" class="rosten_form" id="rosten_form" data-dojo-type="dijit/form/Form"
				onsubmit="return false;" style="padding:0px" enctype="multipart/form-data">
        		<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${engage?.id }"' />
        		<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        	  <div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"基本信息",toggleable:false,moreText:"",marginBottom:"2px"'>
        	  
                <table class="tableData" style="width:740px;margin:0px">
                    <tbody>
                       <tr>
						    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>聘任人员：</div></td>
						    <td width="250">
						    	<input data-dojo-type="dijit/form/ValidationTextBox" 
				                 	data-dojo-props='name:"engageName",trim:true,required:true,value:"${engage?.engageName }"
				                '/>
						    </td>
						    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>聘任部门：</div></td>
						    <td width="250">
						    	<input id="allowdepartsName" data-dojo-type="dijit/form/ValidationTextBox" 
					                data-dojo-props='name:"engageDepart",trim:true,required:true,
					      			value:"${engage?.engageDepart }"
					            '/>
								<g:hiddenField name="allowdepartsId" value="${departId}" />
								<button data-dojo-type="dijit.form.Button" 
									data-dojo-props='onClick:function(){selectDepart("${createLink(controller:'system',action:'departTreeDataStore',params:[companyId:company?.id])}")}'>选择</button>
				           </td>
						</tr>
						
						<tr>
						    <td><div align="right"><span style="color:red">*&nbsp;</span>聘任时间：</div></td>
						    <td>
				                <input id="startDate" data-dojo-type="dijit/form/DateTextBox" 
	                 				data-dojo-props='name:"engageDate",trim:true,required:true,
	                 					value:"${engage?.getFormattedEngageDate()}"
	                 				'/>
						    </td>
						    <td><div align="right">是否已发布：</div></td>
						    <td>
						    	<input data-dojo-type="dijit/form/ValidationTextBox" 
				                 	data-dojo-props='disabled:true,value:"${engage?.getIsPublishFormat() }"
				                '/>
						    </td>    
						</tr>
						
						<tr>
						    <td><div align="right"><span style="color:red">*&nbsp;</span>聘任理由：</div></td>
						    <td colspan=3>
				                <textarea id="reason" data-dojo-type="dijit/form/SimpleTextarea" 
									data-dojo-props='name:"reason",
					                    style:{width:"548px",height:"150px"},
					                    trim:true,value:"${engage?.reason}"
					            '>
								</textarea>
						    
						    </td>    
						</tr>
                    </tbody>
                </table>
                
            </div>
			
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"附件信息",toggleable:false,moreText:"",
				href:"${createLink(controller:'share',action:'getFileUploadNew',id:engage?.id,params:[uploadPath:'staff',isShowFile:isShowFile])}"'>
			</div>
			</form>
		</div>
	</div>
</body>
</html>