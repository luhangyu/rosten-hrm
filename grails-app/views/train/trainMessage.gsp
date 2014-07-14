<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>员工培训信息</title>
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
	         "dojo/dom",
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
			function(parser,dom,kernel,registry){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}"});
					rosten.cssinit();
				});
				trainMessage_save = function(){
					rosten.readSync(rosten.webPath + "/train/trainMessageSave",{},function(data){
						if(data.result=="true" || data.result == true){
							rosten.alert("保存成功！").queryDlgClose= function(){
								page_quit();
								//window.location.reload();
							};
						}else{
							rosten.alert("保存失败!");
						}
					},null,"rosten_form");
				};
				page_quit = function(){
					rosten.pagequit();
				};

				selectCourse = function(url) {
			        var id = "sys_courseDialog";
			        var initValue = [];
			        var courseName = registry.byId("courseName");
			        if (courseName.attr("value") != "") {
			            initValue.push(courseName.attr("value"));
			        }
			        rosten.selectDialog("培训班选择", id, url, false, initValue);
			        rosten[id].callback = function(data) {
			            var _data = "";
			            var _data_1 = "";
			            for (var k = 0; k < data.length; k++) {
			                var item = data[k];
			                if (_data == "") {
			                    _data += item.name;
			                    _data_1 += item.id;
			                } else {
			                    _data += "," + item.name;
			                    _data_1 += "," + item.id;
			                }
			            }
			            registry.byId("courseName").attr("value", _data);
			            dom.byId("courseId").value = _data_1;
			        };
			    };
				
		});
    </script>
</head>
<body>
<div class="rosten_action">
	<div data-dojo-type="rosten/widget/ActionBar" data-dojo-id="rosten_actionBar" 
		data-dojo-props='actionBarSrc:"${createLink(controller:'trainAction',action:'trainMessageForm',id:trainMessage?.id,params:[userid:user?.id])}"'>
	</div>
</div>

<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props=''>
		<form id="rosten_form" name="rosten_form" url='[controller:"assetConfig",action:"assetCategorySave"]' onsubmit="return false;" class="rosten_form" style="padding:0px">
			<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${trainMessage?.id }"' />
        	<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        	<g:hiddenField name="userId" value="${trainMessage?.user?.id}"/>
        	
				<table border="0" width="740" align="left">
				
				<tr>
					    <td><div align="right">员工用户名：</div></td>
					    <td >
					    	<input id="userName" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='trim:true,readOnly:true,
									value:"${trainMessage?.getUserName()}"
			                '/>
					    </td>
					</tr>
					
					<tr>
					    <td><div align="right">培训班：</div></td>
					    <td >
					    			<input id="courseName" data-dojo-type="dijit/form/ValidationTextBox" 
	                                	data-dojo-props='name:"courseName",${fieldAcl.isReadOnly("courseName")},
	                                		"class":"input",
	                                		trim:true,
	                                		required:true,
	                                		style:{width:"400px"},
	              							value:"${trainMessage?.getCourseName() }"
	                                '/>
	                                <g:hiddenField name="courseId" id="courseId" value="${trainMessage?.trainCourse?.id}"/>
									<button data-dojo-type="dijit.form.Button" data-dojo-props='onClick:function(){selectCourse("${createLink(controller:'train',action:'courseSelect',params:[companyId:company?.id])}")}'>选择</button>
					    </td>
					</tr>
					
					<tr>
					 <td><div align="right">证书发放：</div></td>
					<td>
		                        	<input id="admin1" data-dojo-type="dijit/form/RadioButton"
	                             		data-dojo-props='name:"isSendCert",
	                             			type:"radio",
	                             			${fieldAcl.isReadOnly("isSendCert")},
	                             			<g:if test="${!trainMessage?.isSendCert || trainMessage?.isSendCert==true }">checked:true,</g:if>
	              							value:"true"
	                                '/>
									<label for="admin1">是</label>
									
	                                <input id="admin2" data-dojo-type="dijit/form/RadioButton"
	                             		data-dojo-props='name:"isSendCert",
	                             			type:"radio",
	                             			${fieldAcl.isReadOnly("isSendCert")},
	                             			<g:if test="${trainMessage?.isSendCert &&trainMessage?.isSendCert==false }">checked:true,</g:if>
	              							value:"false"
	                                '/>
									<label for="admin2">否</label>
									
		                        </td>
					</tr>
					
					<tr>
					    <td><div align="right">培训结果：</div></td>
					    <td >
					    		<input id="trainResult" data-dojo-type="dijit/form/ValidationTextBox" 
                                	data-dojo-props='name:"trainResult","class":"input",style:{width:"550px"},
              							value:"${trainMessage?.trainResult}"
                                	'/>
					    </td>
					</tr>
				
					<tr>
					    <td><div align="right">备注：</div></td>
					    <td  colspan=3>
					    	<textarea id="remark" data-dojo-type="dijit/form/SimpleTextarea" 
    							data-dojo-props='name:"remark",
                               		style:{width:"550px",height:"150px"},
                               		trim:true,value:"${trainMessage?.remark}"
                           '>
    						</textarea>
					    </td>
					</tr>
					
				</table>
			
		</form>
	</div>
	
</div>
</body>