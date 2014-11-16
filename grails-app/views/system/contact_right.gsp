<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.rosten.app.staff.StaffService"%>
<%
	StaffService utilService = new StaffService();
 %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>通讯录导航</title>
    <style type="text/css">
    	.contactDetail { clear:both; margin-top:2px;height:100% }
    	.contactDetail h3 { height:27px; line-height:27px; text-align:center; background:#e3effb;margin:0px }
		.contactDetail h3 span { color:#155fbe; }
		.contactDetail h3 em { float:right; padding-right:10px; font-weight:normal; *margin-top:-28px;}
		.contactDetail .demo_data { width:180px; height:79px; background:url(../images/rosten/share/czsj_bg.gif) left top repeat-x; padding:6px; float:left; display:inline; margin:5px 2px 0 2px; border:#dedede 1px solid; }
		.contactDetail .demo_data p { line-height:24px; color:#666666; margin:0px}
		.contactDetail .demo_data h4 { border-bottom:#cecece 1px dashed; line-height:30px; color:#155fbe; font-weight:normal; margin:0px}
		.rosten .contactDetail .dijitTitlePaneContentOuter{
			border:0px;
		}
		.rosten .contactDetail .con {
			text-align:left;
		}
	</style>
</head>
<body>
	<div class="contactDetail">
		<div data-dojo-type="rosten/widget/TitlePane"
			data-dojo-props='title:"${departEntity?.departName  + (departEntity.departPhone?"  [" +departEntity.departPhone + "]":"")  }",toggleable:false,moreText:"",style:{textAlign:"center"}'>
			
			<g:each in="${utilService.getPersonInforByDepart(departEntity)}">
				<div class="con">
					<div class="demo_data">
						<h4>${it.chinaName }</h4>
						<p>工作岗位：${it?.workJob }</p>
						<p>手机号码：${utilService.getContactInfor(it)?.mobile }</p>
					</div>
				</div>
			</g:each>
		</div>
		<g:each var="item" in="${departEntity?.children }">
			<div data-dojo-type="rosten/widget/TitlePane"
				data-dojo-props='title:"${item?.departName + (item.departPhone?"  [" +item.departPhone + "]":"") }",toggleable:false,moreText:"",style:{textAlign:"center",marginTop:"15px"}'>
				
				<g:each in="${utilService.getPersonInforByDepart(item)}">
					<div class="con">
						<div class="demo_data">
							<h4>${it.chinaName }</h4>
							<p>工作岗位：${it?.workJob }</p>
							<p>手机号码：${utilService.getContactInfor(it)?.mobile }</p>
						</div>
					</div>
				</g:each>
			</div>
		</g:each>
	</div>
</body>
</html>