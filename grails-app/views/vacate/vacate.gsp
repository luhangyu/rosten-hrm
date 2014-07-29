<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>请假申请</title>
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
		 		"dijit/form/RadioButton",
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
				vacate_save = function(){
					var courseName = registry.byId("courseName");
					
					rosten.readSync(rosten.webPath + "/vacate/vacateSave",{},function(data){
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
				};
				vacate_addComment = function(){
					var id = registry.byId("id").get("value");
					var commentDialog = rosten.addCommentDialog({type:"vacate"});
					commentDialog.callback = function(_data){
						rosten.readSync(rosten.webPath + "/vacate/addComment/" + id,{dataStr:_data.content,userId:"${user?.id}"},function(data){
							if(data.result=="true" || data.result == true){
								rosten.alert("成功！");
							}else{
								rosten.alert("失败!");
							}	
						});
					};
				};
				vacate_deal = function(type,readArray){
					var content = {};
					content.id = registry.byId("id").attr("value");
					content.deal = type;
					if(readArray){
						content.dealUser = readArray.join(",");
					}
					rosten.readSync(rosten.webPath + "/vacate/vacateFlowDeal",content,function(data){
						if(data.result=="true" || data.result == true){
							rosten.alert("成功！").queryDlgClose= function(){
								//刷新待办事项内容
								window.opener.showStartGtask("${user?.id}","${company?.id }");
								
								if(data.refresh=="true" || data.refresh==true){
									window.location.reload();
								}else{
									rosten.pagequit();
								}
							}
						}else{
							rosten.alert("失败!");
						}	
					});
				};
				vacate_submit = function(){
					var rostenShowDialog = rosten.selectFlowUser("${createLink(controller:'vacate',action:'getDealWithUser',params:[companyId:company?.id,id:vacate?.id])}","single");
		            rostenShowDialog.callback = function(data) {
		            	var _data = [];
		            	for (var k = 0; k < data.length; k++) {
		            		var item = data[k];
		            		_data.push(item.value + ":" + item.departId);
		            	};
		            	vacate_deal("submit",_data);	
		            }
					rostenShowDialog.afterLoad = function(){
						var _data = rostenShowDialog.getData();
			            if(_data && _data.length==1){
				            //直接调用
			            	rostenShowDialog.doAction();
				        }else{
							//显示对话框
							rostenShowDialog.open();
					    }
					}   
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
		data-dojo-props='actionBarSrc:"${createLink(controller:'vacateAction',action:'vacateForm',id:vacate?.id,params:[userid:user?.id])}"'>
	</div>
</div>

<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props=''>
		<form id="rosten_form" name="rosten_form" url='[controller:"assetConfig",action:"assetCategorySave"]' onsubmit="return false;" class="rosten_form" style="padding:0px">
			<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${vacate?.id }"' />
        	<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        	
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"请假申请",toggleable:false,moreText:"",height:"300px",marginBottom:"2px"'>
				<table border="0" width="740" align="left">

					<tr>
					    <td><div align="right">拟稿人：</div></td>
					    <td >
					    	<input id="userName" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='trim:true,readOnly:true,
									value:"${vacate?.getFormattedDrafter()}"
			                '/>
					    </td>
					      <td><div align="right">请假类型：</div></td>
					   <td>
					    	<select id="vacateType" data-dojo-type="dijit/form/FilteringSelect"
                           		data-dojo-props='name:"vacateType",
                           			autoComplete:false,${fieldAcl.isReadOnly("cssStyle")},
            						value:"${vacate?.vacateType}"
                            '>
	                            <option value="事假">事假</option>
								<option value="病假">病假</option>
								<option value="年休假">年休假</option>
								<option value="婚假">婚假</option>	
								<option value="丧假">丧假</option>
                              	<option value="其他">其他</option>
                           	</select>
			            </td>
					</tr>

					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>开始时间：</div></td>
					    <td>
					    	<input id="startDate" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"startDate",${fieldAcl.isReadOnly("startDate")},
			                	trim:true,required:true,missingMessage:"请正确填写开始时间！",invalidMessage:"请正确填写培训时间！",
			                	value:"${vacate?.getFormatteStartDate()}"
			               '/>
			            </td>
					<td><div align="right"><span style="color:red">*&nbsp;</span>结束时间：</div></td>
					    <td>
					    	<input id="endDate" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"endDate",${fieldAcl.isReadOnly("endDate")},
			                	trim:true,required:true,missingMessage:"请正确填写结束时间！",invalidMessage:"请正确填写培训时间！",
			                	value:"${vacate?.getFormatteEndDate()}"
			               '/>
			            </td>
			            </tr>
			            
			            <tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>请假时长：</div></td>
					    <td>
					    	<input id="numbers" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='trim:true,required:true,name:"numbers",
									value:"${vacate?.numbers}"
			                '/>
			            </td>
					<td><div align="right"><span style="color:red">*&nbsp;</span>单位：</div></td>
					   <td width="250">
					  		<input id="unitType1" data-dojo-type="dijit/form/RadioButton"
				           		data-dojo-props='name:"unitType",type:"radio",
				           			<g:if test="${vacate?.unitType=="小时" }">checked:true,</g:if>
									value:"小时"
			              	'/>
							<label for="unitType1">小时</label>
						
			              	<input id="unitType2" data-dojo-type="dijit/form/RadioButton"
			           			data-dojo-props='name:"unitType",type:"radio",
			           			<g:if test="${vacate?.unitType=="天" }">checked:true,</g:if>
								value:"天"
			              	'/>
							<label for="unitType2">天</label>
					    </td>
			            </tr>
			            
					<tr>
					    <td><div align="right">备注：</div></td>
					    <td  colspan=3>
					    	<textarea id="remark" data-dojo-type="dijit/form/SimpleTextarea" 
    							data-dojo-props='name:"remark","class":"input",
                               		style:{width:"550px"},rows:"10",
                               		trim:true,value:"${vacate?.remark}"
                           '>
    						</textarea>
					    </td>
					</tr>
					
				</table>
			</div>
			
		</form>
	</div>
	<div data-dojo-type="dijit/layout/ContentPane" id="Comment" title="流转意见" data-dojo-props='refreshOnShow:true,
		href:"${createLink(controller:'vacate',action:'getCommentLog',id:vacate?.id)}"
	'>	
	</div>
	<div data-dojo-type="dijit/layout/ContentPane" id="FlowLog" title="流程跟踪" data-dojo-props='refreshOnShow:true,
		href:"${createLink(controller:'vacate',action:'getFlowLog',id:vacate?.id)}"
	'>	
	</div>
</div>
</body>