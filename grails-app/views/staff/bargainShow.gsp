<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>合同管理</title>
    <link rel="stylesheet" href="${createLinkTo(dir:'js/dojox/widget/Wizard',file:'Wizard.css') }"></link>
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
		 		"dijit/Editor",
				"dijit/_editor/plugins/FontChoice",
				"dojox/widget/Wizard",
				"dojox/widget/WizardPane",
		     	"rosten/widget/ActionBar",
		     	"rosten/app/Application",
		     	"rosten/app/StaffApplication",
		     	"rosten/kernel/behavior"],
			function(parser,kernel,registry,xhr,dom,datestamp,DepartUserDialog){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}",dojogridcss : true});
					rosten.cssinit();
				});
				
			bargain_add = function(object){
				var content = {};
				
				//添加新增时添加附件功能
				<g:if test="${!bargain?.id}">
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
				
				rosten.readSync(rosten.webPath + "/staff/bargainSave",content,function(data){
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
			addPersonInforDone = function(){
				var id = rosten.getGridItemValue1(chooseListGrid,"id");
				var chinaName = rosten.getGridItemValue1(chooseListGrid,"chinaName");
				var sex = rosten.getGridItemValue1(chooseListGrid,"sex");
				var departName = rosten.getGridItemValue1(chooseListGrid,"departName");
				var status = rosten.getGridItemValue1(chooseListGrid,"status");
	
				registry.byId("personInforId").set("value",id);
				registry.byId("personInforName").set("value",chinaName);
				registry.byId("sex").set("value",sex);
				registry.byId("departName").set("value",departName);
				registry.byId("status").set("value",status);
				
				registry.byId("chooseDialog").hide();
			};
		});

    </script>
</head>
<body>
	<div class="rosten_action">
		<div data-dojo-type="rosten/widget/ActionBar" id="rosten_actionBar" 
			data-dojo-props='actionBarSrc:"${createLink(controller:'staffAction',action:'bargainForm',id:bargain?.id,params:[userid:user?.id])}"'></div>
	</div>
	<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"800px",margin:"0 auto"}' >
	  	<div data-dojo-type="dijit/layout/ContentPane" title="基本信息" data-dojo-props='style:{height:"600px"}'>
	  		<form class="rosten_form" id="rosten_form" onsubmit="return false;" style="text-align:left;padding:0px">	
	  		<input  data-dojo-type="dijit/form/ValidationTextBox" id="companyId" data-dojo-props='name:"companyId",style:{display:"none"},value:"${company?.id }"' />
	  			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"员工信息",toggleable:false,moreText:"",height:"80px",marginBottom:"2px",
					href:"${createLink(controller:'bargain',action:'getBargainPerson',id:bargain?.id)}"
				'></div>
	  		
		  		<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"合同基本信息",toggleable:false,moreText:"",height:"80px",marginBottom:"2px",
					href:"${createLink(controller:'staff',action:'getBargain',id:bargain?.id)}"
				'></div>
		  	
		  		<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"附件信息",toggleable:false,moreText:"",
		  			href:"${createLink(controller:'share',action:'getFileUploadNew',id:bargain?.id,params:[uploadPath:'staff',isShowFile:isShowFile])}"'>
					
				</div>
	  		</form>
		</div>
	</div>
</body>
</html>