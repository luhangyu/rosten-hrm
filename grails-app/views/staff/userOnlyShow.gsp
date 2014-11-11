<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="layout" content="rosten" />
    <title>人员信息</title>
    <style type="text/css">
    	body{
			overflow:auto;
		}
		.rosten .rostenFamilyTitleGrid .dijitTitlePaneContentInner{
			padding:2px 1px 1px 1px;
			height:160px;
		}
		.rosten .rostenDegreeTitleGrid .dijitTitlePaneContentInner{
			padding:2px 1px 1px 1px;
			height:280px;
		}
		.rosten .rostenWorkResumeTitleGrid .dijitTitlePaneContentInner{
			padding:2px 1px 1px 1px;
			height:280px;
		}
		
    </style>
	<script type="text/javascript">
	require(["dojo/parser",
	        	"dojo/_base/lang",
		 		"dojo/_base/kernel",
		 		"dijit/registry",
		 		"dijit/form/Button",
		     	"rosten/widget/ActionBar",
		     	"dijit/layout/TabContainer",
				"dijit/layout/ContentPane",
		     	"dijit/form/TextBox",
		     	"dijit/form/RadioButton",
		     	"dijit/form/ValidationTextBox",
		     	"dijit/form/SimpleTextarea",
		     	"dijit/form/DropDownButton",
		     	"dijit/form/Form",
		 		"dojox/form/FileInput",
		     	"dijit/form/Button",
		     	"dijit/Dialog",
				"dojox/grid/DataGrid",
		     	"dijit/form/RadioButton",
		     	"dijit/form/FilteringSelect",
		     	"dijit/form/ComboBox",
		     	"rosten/app/SystemApplication",
		     	"rosten/app/StaffApplication"],
			function(parser,lang,kernel,registry,ActionBar){
				kernel.addOnLoad(function(){
					rosten.init({webpath:"${request.getContextPath()}",dojogridcss : true});
					rosten.cssinit();
				});
			
			page_quit = function(){
				if(window.opener.rosten.kernel.navigationEntity!="userManage"){
					window.opener.rosten.kernel.refreshGrid();
				}else{
					window.opener.dom_rostenGrid.refresh();
				}
		        window.close();
			};
	});
    </script>
</head>
<body>
	<div class="rosten_action">
		<div data-dojo-type="rosten/widget/ActionBar" id="rosten_actionBar" 
			data-dojo-props='actionBarSrc:"${createLink(controller:'staffAction',action:'staffForm',id:personInfor?.id,params:[type:type])}"'></div>
	</div>
	
	<div data-dojo-type="dijit/layout/TabContainer" data-dojo-props='persist:false, tabStrip:true,style:{width:"880px",margin:"0 auto"}' data-dojo-id="rostenTabContainer" >
        <div data-dojo-type="dijit/layout/ContentPane" class="rosten_form" title="基本信息" data-dojo-props=''>
			<div data-dojo-type="rosten/widget/TitlePane" 
				data-dojo-props='title:"个人概况  <span style=\"color:red;margin-left:5px\">(必填信息)</span>",toggleable:false,moreText:"",height:"410px",marginBottom:"2px",
				href:"${createLink(controller:'staff',action:'getPersonInfor',id:personInfor?.id,params:[departId:departId,type:type])}"
			'>
			</div>
			
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"通讯方式 <span style=\"color:red;margin-left:5px\">(必填信息)</span>",toggleable:false,moreText:"",height:"150px",marginBottom:"2px",
				href:"${createLink(controller:'staff',action:'getContactInfor',id:personInfor?.id,params:[type:type])}"
			'>
			</div>

			<div data-dojo-type="rosten/widget/TitlePane" 
				data-dojo-props='"class":"rostenFamilyTitleGrid",title:"家庭成员",toggleable:true,
					_moreClick:staff_addFamily,moreText:"<g:if test="${!type }"><span style=\"color:#108ac6\">增加成员</span></g:if>",marginBottom:"2px"'>
				
				<div data-dojo-type="rosten/widget/RostenGrid" id="staffFamilyGrid" data-dojo-id="staffFamilyGrid"
					data-dojo-props='imgSrc:"../../images/rosten/share/wait.gif",showPageControl:false,url:"${createLink(controller:'staff',action:'getFamily',id:personInfor?.id,params:[type:type])}"'></div>
			
			</div>
			
			<div data-dojo-type="rosten/widget/TitlePane" 
				data-dojo-props='"class":"rostenDegreeTitleGrid",title:"学历学位",toggleable:true,
					_moreClick:staff_addDegree,moreText:"<g:if test="${!type }"><span style=\"color:#108ac6\">增加学历学位</span></g:if>",marginBottom:"2px"'>
				
				<div data-dojo-type="rosten/widget/RostenGrid" id="degreeGrid" data-dojo-id="degreeGrid"
					data-dojo-props='imgSrc:"../../images/rosten/share/wait.gif",showPageControl:false,url:"${createLink(controller:'staff',action:'getDegree',id:personInfor?.id,params:[type:type])}"'></div>
				
			</div>
			
			<div data-dojo-type="rosten/widget/TitlePane" 
				data-dojo-props='"class":"rostenWorkResumeTitleGrid",title:"工作经历",toggleable:true,
					_moreClick:staff_addWorkResume,moreText:"<g:if test="${!type }"><span style=\"color:#108ac6\">增加工作经历</span></g:if>",marginBottom:"2px"'>
				
				<div data-dojo-type="rosten/widget/RostenGrid" id="workResumeGrid" data-dojo-id="workResumeGrid"
					data-dojo-props='imgSrc:"../../images/rosten/share/wait.gif",showPageControl:false,url:"${createLink(controller:'staff',action:'getWorkResume',id:personInfor?.id,params:[type:type])}"'></div>
				
			</div>
        </div>
        
		<div data-dojo-type="dijit/layout/ContentPane" class="rosten_form" title="合同信息" data-dojo-props=''>
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"合同基本信息",toggleable:false,moreText:"",height:"80px",marginBottom:"2px",
				href:"${createLink(controller:'staff',action:'getBargainByPersonInfor',id:personInfor?.id,params:[type:type])}"
			'></div>
			<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='title:"附件信息",toggleable:false,moreText:"",
				href:"${createLink(controller:'staff',action:'getBargainFileByPersonInfor',id:personInfor?.id,params:[type:type])}"'>
				
			</div>
		</div>
		<div data-dojo-type="dijit/layout/ContentPane" class="rosten_form" title="其他信息" data-dojo-props='refreshOnShow:true,
			href:"${createLink(controller:'staff',action:'getPersonOtherInfor',id:personInfor?.id,params:[type:type])}"
		'>
	</div>
</body>
</html>