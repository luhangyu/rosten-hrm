<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>离职/退休</title>
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
		     	"rosten/widget/ActionBar",
		     	"rosten/app/Application",
		     	"rosten/app/SystemApplication",
		     	"rosten/kernel/behavior"],
			function(parser,kernel,registry,xhr,datestamp,DepartUserDialog){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}"});
					rosten.cssinit();
				});

			searchStaff = function(){
				var username = registry.byId("username");
		    	if(!username.isValid()){
		    		rosten.alert("员工名称不正确！").queryDlgClose = function(){
		    			username.focus();
					};
					return;
		    	}
		    	var content ={
		    		companyId:"${company?.id}",
		    		serchInput:username.attr("value")

				};
		    	rosten.readSync(rosten.webPath + "/staff/serachPerson", content, function(data) {
			    	
				    var jsonLength = 0
			    	for(var key in data){
			    		jsonLength += 1;
				    	var widget = registry.byId(key);
				    	if(widget){
				    		widget.set("value",data[key]);
					    }
		    		}

				    if(jsonLength==0){
						rosten.alert("未找到当前用户信息！");
						registry.byId("username").set("value","");
						return;
				    }
		    		
				});
			};	
			statusChange_add = function(object){
				var chenkids = ["username","changeType","changeDate"];
				if(!rosten.checkData(chenkids)) return;

				var changeReasonDom = registry.byId("changeReason");
				var changeReason = changeReasonDom.get("value");
				if(changeReason==""){
					rosten.alert("申请理由不能为空！").queryDlgClose = function(){
						changeReasonDom.focus();
					};
					return false;
				}
				
				var content = {};

				//流程相关信息
				<g:if test='${flowCode}'>
					content.flowCode = "${flowCode}";
					content.relationFlow = "${relationFlow}";
				</g:if>
				
				//增加对多次单击的次数----2014-9-4
				var buttonWidget = object.target;
				rosten.toggleAction(buttonWidget,true);
				
				rosten.readSync(rosten.webPath + "/staff/staffStatusChangeSave",content,function(data){
					if(data.result=="true" || data.result == true){
						rosten.alert("保存成功！").queryDlgClose= function(){
							page_quit();
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
			page_quit = function(){
				rosten.pagequit();
			};
		});
		
		
    </script>
</head>
<body>
<div class="rosten_action">
	<div data-dojo-type="rosten/widget/ActionBar" 
		data-dojo-props='actionBarSrc:"${createLink(controller:'staffAction',action:'statusChangeForm')}"'>
	</div>
</div>
<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
  	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props='style:{height:"520px"}'>
       	<form class="rosten_form" id="rosten_form" onsubmit="return false;" style="padding:0px">
       		<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${statusChange?.id }"' />
       		<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
       		<input id="personInforId" data-dojo-type="dijit/form/ValidationTextBox"  data-dojo-props='name:"personInforId",style:{display:"none"},value:"${personInfor?.id }"' />
       		
       	  	<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"员工信息",toggleable:false,moreText:"",height:"200px",marginBottom:"2px"'>
       	  		<table border="0" align="left" style="margin:0 auto;width:740px">
					<tr>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>员工名称：</div></td>
					    <td width="260">
					    	<input id="username" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='trim:true,required:true,placeHolder:"员工账号/姓名/电话"
			                '/>
			                <button data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){searchStaff()}'>查询验证</button>
					    </td>
					    <td width="120"><div align="right">所属部门：</div></td>
					    <td width="250">
					    	<input id="userDepart" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='readOnly:true'/>
			           </td>
					</tr>
					<tr>
					    <td width="120"><div align="right">性别：</div></td>
					  	<td width="260">
					  		<input id="sex" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='readOnly:true'/>
					    </td>
					    <td width="120"><div align="right">身份证号：</div></td>
					  	<td width="250">
					    	<input id="idCard" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='readOnly:true'/>
					    </td>
					</tr>
					<tr>
					 	<td width="120"><div align="right">出生日期：</div></td>
					  	<td width="260">
					    	<input id="birthday" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='readOnly:true'/>
					    </td>
					    <td width="120"><div align="right">国籍：</div></td>
					  	<td width="250">
					    	<input id="city" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='readOnly:true'/>
					    </td>
					     
					</tr>
					<tr>
						<td width="120"><div align="right">民族：</div></td>
					  	<td width="260">
					    	<input id="nationality" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='readOnly:true'/>
					    </td>
					 	<td width="120"><div align="right">出生地：</div></td>
					  	<td width="250">
					    	<input id="birthAddress" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='readOnly:true'/>
					    </td>
					</tr>
					
					<tr>
					 	<td width="120"><div align="right">籍贯：</div></td>
						<td width="260">
					    	<input id="nativeAddress" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='readOnly:true'/>
						</td>
						<td width="120"><div align="right">政治面貌：</div></td>
					 	<td width="250">
					    	<input id="politicsStatus" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='readOnly:true'/>
					    </td>
					</tr>
					
					<tr>
						<td width="120"><div align="right">婚姻状况：</div></td>
						  <td width="260">
						  		<input id="marriage" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='readOnly:true'/>
						    </td>
						 <td width="120"><div align="right">宗教信仰：</div></td>
						  <td width="250">
						  		<input id="religion" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='readOnly:true'/>
						    </td>
					</tr>
				</table>
            </div>
            <div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"申请理由",toggleable:false,moreText:"",height:"200px",marginBottom:"2px"'>
            	<table border="0" align="left" style="width:740px">
            		<tr>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>申请类型：</div></td>
					    <td width="260">
					    	<input id="changeType" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"changeType",readOnly:true,trim:true,required:true,value:"${statusChange?.changeType }"
			                '/>
					    </td>
					    <td width="120"><div align="right">申请时间：</div></td>
					    <td width="250">
					    	<input id="changeDate" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"changeDate",${fieldAcl.isReadOnly("changeDate")},
			                	trim:true,required:true,missingMessage:"请正确填写申请时间！",invalidMessage:"请正确填写申请时间！",
			                	value:"${statusChange?.getFormattedChangeDate()}"
			               '/>
			           </td>
					</tr>
            		<tr>
					    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>申请理由：</div></td>
				  		<td colspan=3>
					  		<textarea id="changeReason" data-dojo-type="dijit/form/SimpleTextarea" 
								data-dojo-props='name:"changeReason",
				                    style:{width:"556px",height:"150px"},
				                    trim:true,value:"${statusChange?.changeReason}"
				            '>
							</textarea>
				  			
					    </td>
					</tr>
            	</table>
			</div>
            
            <div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"附件信息",toggleable:false,moreText:"",
				height:"60px",href:"${createLink(controller:'share',action:'getFileUpload',id:statusChange?.id,params:[uploadPath:'staff',isShowFile:true])}"'>
			</div>
            
		</form>
	</div>
</div>
</body>
</html>