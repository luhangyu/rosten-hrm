<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <link rel="stylesheet" href="${createLinkTo(dir:'js/dojox/widget/Wizard',file:'Wizard.css') }"></link>
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
		 		"dojo/dom",
		 		"dojo/_base/lang",
		 		"dijit/layout/TabContainer",
		 		"dijit/layout/ContentPane",
		 		"dijit/form/ValidationTextBox",
		 		"dijit/form/RadioButton",
		 		"dijit/form/DateTextBox",
		 		"dijit/form/SimpleTextarea",
		 		"dijit/form/NumberTextBox",
		 		"dijit/form/Button",
		     	"rosten/widget/ActionBar",
		     	"rosten/widget/TitlePane",
		     	"rosten/app/Application",
		     	"rosten/kernel/behavior"],
			function(parser,kernel,registry,dom,lang){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}",dojogridcss : true});
					rosten.cssinit();
				});
				vacate_save = function(object){
					var courseName = registry.byId("courseName");
					var chenkids = ["startDate","endDate","numbers"];
					if(rosten.checkData(chenkids)){
						var content = {};
						
						//流程相关信息
						<g:if test='${flowCode}'>
							content.flowCode = "${flowCode}";
							content.relationFlow = "${relationFlow}";
						</g:if>

						//是否管理员直接生成，不需要走流程
						<g:if test='${notNeedFlow}'>
							content.notNeedFlow = "${notNeedFlow}";
						</g:if>

						//增加对多次单击的次数----2014-9-4
						var buttonWidget = object.target;
						rosten.toggleAction(buttonWidget,true);

						rosten.readSync(rosten.webPath + "/vacate/vacateSave",content,function(data){
							if(data.result=="true" || data.result == true){
								rosten.alert("保存成功！").queryDlgClose= function(){
									//2015-3-13-----根据用户要求，自动关闭页面
									page_quit();
									/*
									if(window.location.href.indexOf(data.id)==-1){
										window.location.replace(window.location.href + "&id=" + data.id);
									}else{
										window.location.reload();
									}*/
								};
							}else{
								rosten.alert("保存失败!");
							}
							rosten.toggleAction(buttonWidget,false);
						},function(error){
							rosten.alert("系统错误，请通知管理员！");
							rosten.toggleAction(buttonWidget,false);
						},"rosten_form");
					}
				};
				vacate_addComment = function(){
					//flowCode为是否需要走流程，如需要，则flowCode为业务流程代码
					var commentDialog = rosten.addCommentDialog({type:"vacate"});
					commentDialog.callback = function(_data){
						var content = {dataStr:_data.content,userId:"${user?.id}",status:"${vacate?.status}",flowCode:"${flowCode}"};
						rosten.readSync(rosten.webPath + "/share/addComment/${vacate?.id}",content,function(data){
							if(data.result=="true" || data.result == true){
								rosten.alert("成功！").queryDlgClose= function(){
									var selectWidget = rosten_tabContainer.selectedChildWidget;
									if(selectWidget.get("id")=="flowComment"){
										rosten_tabContainer.selectedChildWidget.refresh();
									}
								};
							}else{
								rosten.alert("失败!");
							}	
						});
					};
				};
				vacate_deal = function(type,readArray,buttonWidget,conditionObj){
					var content = {};
					content.id = registry.byId("id").attr("value");
					content.deal = type;
					if(readArray){
						content.dealUser = readArray.join(",");
					}
					if(conditionObj){
						lang.mixin(content,conditionObj);
					}
					rosten.readSync(rosten.webPath + "/vacate/vacateFlowDeal",content,function(data){
						if(data.result=="true" || data.result == true){
							var ostr = "成功！";
							if(data.nextUserName && data.nextUserName!=""){
								ostr += "下一处理人<" + data.nextUserName +">";
							}
							rosten.alert(ostr).queryDlgClose= function(){
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
							rosten.toggleAction(buttonWidget,false);
						}	
					},function(error){
						rosten.alert("系统错误，请通知管理员！");
						rosten.toggleAction(buttonWidget,false);
					});
				};
				vacate_submit = function(object,conditionObj){
					//增加对多次单击的次数----2014-9-4
					var buttonWidget = object.target;
					rosten.toggleAction(buttonWidget,true);
					
					var content = {};

					//2014-11-20 特殊处理
					if(!conditionObj){
						conditionObj ={conditionName:"numbers",conditionValue:${vacate?.numbers * 10}};
					}
					
					//增加对排他分支的控制
					if(conditionObj){
						lang.mixin(content,conditionObj);
					}
					
					rosten.readSync("${createLink(controller:'share',action:'getSelectFlowUser',
						params:[userId:user?.id,taskId:vacate?.taskId,drafterUsername:vacate?.drafter?.username])}",content,function(data){
						
						if(data.dealFlow==false){
							//流程无下一节点
							vacate_deal("submit",null,buttonWidget,conditionObj);
							return;
						}
						var url = "${createLink(controller:'system',action:'userTreeDataStore',params:[companyId:company?.id])}";
						if(data.dealType=="user"){
							//人员处理
							if(data.showDialog==false){
								//单一处理人
								var _data = [];
								_data.push(data.userId + ":" + data.userDepart);
								vacate_deal("submit",_data,buttonWidget,conditionObj);
							}else{
								//多人，多部门处理
								url += "&type=user&user=" + data.user;
								vacate_submit_select(url,buttonWidget,conditionObj);
							}
						}else{
							//群组处理
							url += "&type=group&groupIds=" + data.groupIds;
							if(data.limitDepart){
								url += "&limitDepart="+data.limitDepart;
							}
							vacate_submit_select(encodeURI(url),buttonWidget,conditionObj);
						}
					},function(error){
						rosten.alert("系统错误，请通知管理员！");
						rosten.toggleAction(buttonWidget,false);
					});
				};
				vacate_submit_select = function(url,buttonWidget,conditionObj){
					var rostenShowDialog = rosten.selectFlowUser(url,"single");
		            rostenShowDialog.callback = function(data) {
		            	if(data.length==0){
			            	rosten.alert("请正确选择人员！");
		            		rosten.toggleAction(buttonWidget,false);
			            }else{
			            	var _data = [];
			            	for (var k = 0; k < data.length; k++) {
			            		var item = data[k];
			            		_data.push(item.value + ":" + item.departId);
			            	};
			            	vacate_deal("submit",_data,buttonWidget,conditionObj);
			            }
		            };
					rostenShowDialog.afterLoad = function(){
						var _data = rostenShowDialog.getData();
			            if(_data && _data.length==1){
				            //直接调用
			            	rostenShowDialog.doAction();
				        }else{
							//显示对话框
							rostenShowDialog.open();
					    }
					};
					rostenShowDialog.queryDlgClose = function(){
						rosten.toggleAction(buttonWidget,false);
					};	
				};
				vacate_back = function(object,conditionObj){
					//增加对多次单击的次数----2014-9-4
					var buttonWidget = object.target;
					rosten.toggleAction(buttonWidget,true);
					
					var content = {};
					rosten.readSync("${createLink(controller:'vacate',action:'vacateFlowBack',params:[id:vacate?.id])}",content,function(data){
						if(data.result=="true" || data.result == true){
							rosten.alert("成功！下一处理人<" + data.nextUserName +">").queryDlgClose= function(){
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
							rosten.toggleAction(buttonWidget,false);
						}
						
					},function(error){
						rosten.alert("系统错误，请通知管理员！");
						rosten.toggleAction(buttonWidget,false);
					});
				};
				changeVacateDay = function(){
					var startDate = registry.byId("startDate").get("value");
					var endDate = registry.byId("endDate").get("value");
					var number = (endDate.getTime() - startDate.getTime())/(24 * 60 * 60 * 1000) + 1;
					if(number < 0 || number==0){
						rosten.alert("请正确输入时间！");
						registry.byId("numbers").set("value","");
					}else{
						registry.byId("numbers").set("value",number);
					}
					
				};
				changeVacateDay_stage = function(){
					var numbers = registry.byId("numbers");
					if(numbers.get("value")==0.5 || numbers.get("value")=="0.5"){
						dom.byId("dateStageTr").style.display = "";
					}else{
						dom.byId("dateStageTr").style.display = "none";
					}
					
				};
				page_quit = function(){
					rosten.pagequit();
				};
				addPersonInfor = function(){
			        registry.byId("chooseDialog").show();
			    };
			    addPersonInforNext = function(){
			        var content = {};
			        
			        var username = registry.byId("s_username");
			        if(username.get("value")!=""){
			            content.username = username.get("value");
			        }
			        
			        var chinaName = registry.byId("s_chinaName");
			        if(chinaName.get("value")!=""){
			            content.chinaName = chinaName.get("value");
			        }
			        
			        var departName = registry.byId("s_departName");
			        if(departName.get("value")!=""){
			            content.departName = departName.get("value");
			        }
			        
			        var idCard = registry.byId("s_idCard");
			        if(idCard.get("value")!=""){
			            content.idCard = idCard.get("value");
			        }
			        var sex = registry.byId("s_sex");
			        if(sex.get("value")!=""){
			            content.sex = sex.get("value");
			        }
			        var politicsStatus = registry.byId("s_politicsStatus");
			        if(politicsStatus.get("value")!=""){
			            content.politicsStatus = politicsStatus.get("value");
			        }
			        var nativeAddress = registry.byId("s_nativeAddress");
			        if(nativeAddress.get("value")!=""){
			            content.nativeAddress = nativeAddress.get("value");
			        }
			        var city = registry.byId("s_city");
			        if(city.get("value")!=""){
			            content.city = city.get("value");
			        }
			        var status = registry.byId("s_status");
			        if(status.get("value")!=""){
			            content.status = status.get("value");
			        }
			        
			        chooseListGrid.refresh(null,content);
			    };
			    addPersonInforDone = function(){
			        var chinaName = rosten.getGridItemValue1(chooseListGrid,"chinaName");
			        var departName = rosten.getGridItemValue1(chooseListGrid,"departName");

			        registry.byId("applyName").set("value",chinaName);
			        registry.byId("applyDepart").set("value",departName);
			        
			        registry.byId("chooseDialog").hide();
			    };
			    personInfor_formatTopic_normal = function(value,rowIndex){
					return "<a href=\"javascript:personInfor_normal_onMessageOpen(" + rowIndex + ");\">" + value + "</a>";
				};
				personInfor_normal_onMessageOpen = function(rowIndex){
					
				};
				
		});
    </script>
</head>
<body>
<div class="rosten_action">
	<div data-dojo-type="rosten/widget/ActionBar" data-dojo-id="rosten_actionBar" 
		data-dojo-props='actionBarSrc:"${createLink(controller:'vacateAction',action:'vacateForm',id:vacate?.id,params:[userid:user?.id,notNeedFlow:notNeedFlow])}"'>
	</div>
</div>

<div data-dojo-id="rosten_tabContainer" data-dojo-type="dijit/layout/TabContainer" data-dojo-props='doLayout:false,persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props='doLayout:false'>
		<form id="rosten_form" name="rosten_form" url='[controller:"assetConfig",action:"assetCategorySave"]' onsubmit="return false;" class="rosten_form" style="padding:0px">
			<input  data-dojo-type="dijit/form/ValidationTextBox" id="id"  data-dojo-props='name:"id",style:{display:"none"},value:"${vacate?.id }"' />
        	<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
        	<input  data-dojo-type="dijit/form/ValidationTextBox" id="unitType"  data-dojo-props='name:"unitType",style:{display:"none"},value:"天"' />
        	
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"请假申请",toggleable:false,moreText:"",height:"320px",marginBottom:"2px"'>
				<table border="0" width="740" align="left">
					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>申请人：</div></td>
					    <td >
					    	<input id="applyName" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"applyName",trim:true,readOnly:true,
									value:"${vacate?.applyName}"
			                '/>
			                <g:if test='${notNeedFlow}'>
			                <button data-dojo-type='dijit/form/Button' 
								data-dojo-props="label:'选择',iconClass:'docAddIcon'">
								<script type="dojo/method" data-dojo-event="onClick">
										addPersonInfor();
									</script>
							</button>
							</g:if>
					    </td>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>申请部门：</div></td>
					    <td >
					    	<input id="applyDepart" data-dojo-type="dijit/form/ValidationTextBox" 
			                 	data-dojo-props='name:"applyDepart",trim:true,readOnly:true,
									value:"${vacate?.applyDepart}"
			                '/>
					    </td>
					     
					</tr>

					<tr>
					    <td><div align="right"><span style="color:red">*&nbsp;</span>开始时间：</div></td>
					    <td>
					    	<div id="startDate" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"startDate",${fieldAcl.isReadOnly("startDate")},
			                	trim:true,required:true,missingMessage:"请正确填写开始时间！",invalidMessage:"请正确填写开始时间！",
			                	value:"${vacate?.getFormatteStartDate()}"
			               '>
			               		<script type="dojo/method" data-dojo-event="onChange">
									changeVacateDay();
								</script>
			               </div>
			            </td>
					<td><div align="right"><span style="color:red">*&nbsp;</span>结束时间：</div></td>
					    <td>
					    	<div id="endDate" data-dojo-type="dijit/form/DateTextBox" 
			                	data-dojo-props='name:"endDate",${fieldAcl.isReadOnly("endDate")},
			                	trim:true,required:true,missingMessage:"请正确填写结束时间！",invalidMessage:"请正确填写结束时间！",
			                	value:"${vacate?.getFormatteEndDate()}"
			               '>
			               		<script type="dojo/method" data-dojo-event="onChange">
									changeVacateDay();
								</script>
			               </div>
			            </td>
		            </tr>
		            
		            <tr>
			            
		             	<td><div align="right"><span style="color:red">*&nbsp;</span>请假类型：</div></td>
					   	<td>
					    	<select id="vacateType" data-dojo-type="dijit/form/FilteringSelect"
                           		data-dojo-props='name:"vacateType",
                           			autoComplete:false,${fieldAcl.isReadOnly("vacateType")},
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
			            
					    <td><div align="right"><span style="color:red">*&nbsp;</span>请假时长：</div></td>
					    <td>
					    	<div id="numbers" data-dojo-type="dijit/form/NumberTextBox" 
			                 	data-dojo-props='trim:true,required:true,name:"numbers",
			                 		missingMessage:"请正确填写请假时长！",invalidMessage:"请正确填写请假时长！",${fieldAcl.isReadOnly("numbers")},
									value:"${vacate?.getFormattedNumbers()}"
			                '> &nbsp;天
			                	<script type="dojo/method" data-dojo-event="onChange">
									changeVacateDay_stage();
								</script>
			                </div>
			            </td>
		            </tr>
		            
		            <tr id="dateStageTr" style="<g:if test="${vacate.getFormattedNumbers()!=0.5}">display:none</g:if>">
		            	<td><div align="right"><span style="color:red">*&nbsp;</span>请假区间：</div></td>
		            	<td><select id="dateStage" data-dojo-type="dijit/form/FilteringSelect"
                           		data-dojo-props='name:"dateStage",
                           			autoComplete:false,${fieldAcl.isReadOnly("dateStage")},
            						value:"${vacate?.dateStage?vacate.dateStage:"上午"}"
                            '>
	                            <option value="上午">上午</option>
								<option value="下午">下午</option>
                           	</select>
                        </td>
                        <td></td>
                        <td></td>
		            </tr>
			            
					<tr>
					    <td><div align="right">请假理由：</div></td>
					    <td  colspan=3>
					    	<textarea id="remark" data-dojo-type="dijit/form/SimpleTextarea" 
    							data-dojo-props='name:"remark","class":"input",
                               		style:{width:"586px"},rows:"10",${fieldAcl.isReadOnly("remark")},
                               		trim:true,value:"${vacate?.remark}"
                           '>
    						</textarea>
					    </td>
					</tr>
					
				</table>
				
			</div>
			
		</form>
	</div>
	<g:if test="${vacate?.id && vacate?.processDefinitionId}">
	
		<div data-dojo-type="dijit/layout/ContentPane" id="flowComment" title="流转意见" data-dojo-props='refreshOnShow:true,
			href:"${createLink(controller:'share',action:'getCommentLog',id:vacate?.id)}"
		'>	
		</div>
		<div data-dojo-type="dijit/layout/ContentPane" id="FlowLog" title="流程跟踪" data-dojo-props='refreshOnShow:true,
			href:"${createLink(controller:'share',action:'getFlowLog',id:vacate?.id,params:[processDefinitionId:vacate?.processDefinitionId,taskId:vacate?.taskId])}"
		'>	
		</div>
	</g:if>
</div>
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
					data-dojo-props='imgSrc:"${resource(dir:'images/rosten/share',file:'wait.gif')}",url:"${createLink(controller:'staff',action:'staffGrid',params:[companyId:company?.id])}"'></div>
			</div>
		</div>
		<script type="dojo/method" event="cancelFunction">
				dijit.byId("chooseDialog").hide();
			</script>
	</div>
</div>
</body>