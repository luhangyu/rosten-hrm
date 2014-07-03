<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>${logoname}</title>
<meta name="layout" content="rosten" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
	.personSearch table.tab_css {
		font-size:11px;
		color:#333333;
		border-width: 1px;
		border-color: #b5bcc7;
		border-collapse: collapse;
	}
	.personSearch table.tab_css th {
		border-width: 1px;
		padding: 4px;
		border-style: solid;
		border-color: #b5bcc7;
		/*background-color: #cad9ea;*/
	}
	.personSearch table.tab_css td{
		border-width: 1px;
		padding: 4px;
		border-style: solid;
		border-color: #b5bcc7;
		/*background-color: #ffffff;*/
	}
	#home_bbs,#home_personMail,#home_gtask,#home_download{overflow:auto;}
	#home_bbs ul,#home_personMail ul,#home_gtask ul,#home_download ul{list-style:none;padding:0px;overflow:hidden;margin:0px}
	#home_bbs li,#home_personMail li,#home_gtask li,#home_download li{list-style:none;height:25px;line-height:25px;border-bottom:1px dashed #c9c9c9}
	#home_bbs ul li a, #home_download ul li a{
		display:inline;
		color:#404040!important;
		float:left;
		text-decoration:none;
		max-width:65%;
		overflow:hidden;
		text-overflow:ellipsis;
		white-space:nowrap;
	}
	#home_bbs ul li .new{
		background:url(images/rosten/share/icon_new.gif) no-repeat left 5px;
		width:18px;
		height:25px;
		display:block;
		float:left;
		padding-left:5px;
		margin-right:10px;
		margin-left:5px;
	}
	#home_personMail ul li a,#home_gtask ul li a{
		margin-left:5px;
		display:inline;
		color:#404040!important;
		float:left;
		text-decoration:none;
		max-width:65%;
		overflow:hidden;
		text-overflow:ellipsis;
		white-space:nowrap;
	}
	#home_bbs ul li .time,#home_personMail ul li .time,#home_gtask ul li .time,#home_download ul li .time{
		float:right;
		color:#404040!important;
		/*padding-right:10px;*/
	}
	#home_personMail ul li .level,#home_gtask ul li .type{
		float:left;
		display:inline;
	}
	#information ul{
		padding:8px 0px 0px 0px;
		width: 100%;
		margin: 0px auto;
		overflow: hidden;
		text-align:center;
	}
	#information ul li{
		position: relative;
		float: left;
		margin-top: 2px;
		margin-bottom: 10px;
		display: inline;
		width: 45%;
		text-align: cneter;
	}
	#information ul li a{
		cursor:pointer;
		display:inline;
		text-decoration: none;
		max-width:65%;
		overflow:hidden;
		text-overflow:ellipsis;
		white-space:nowrap;
	}
	#information ul li a:link,#information ul li a:active,#information ul li a:hover,#information ul li a:visited{
		text-decoration: none;
	}
	#information ul li h5{
		color: #142E65;
		height: 24px;
		font-weight:normal;
		margin-top:0px;
	}
	#information .dijitTitlePaneTitle ,#information .dijitTitlePaneContentOuter{
		border:0px;
	}
	.rosten .dijitTitlePaneCountNode{
		margin-left:3px;
		color:#ff0000;
		vertical-align:text-top;
	}
</style>
<script type="text/javascript">
   	logout = function(){
    	var url = "${createLink(controller:'j_spring_security_logout')}";
		window.location = url;
	};
	require(["dojo/parser", 
	     	"dojo/_base/kernel", 
	     	"dojo/json",
	     	"dojo/_base/lang",
	     	"dojo/query",
	     	"dojo/_base/connect",
	     	"dojo/_base/window",
	     	"dojo/dom",
	     	"dojo/dom-style",
	     	"dojo/dom-class",
	     	"dojo/dom-construct",
	     	"dijit/registry",
	     	"dojox/layout/ContentPane",
	     	"dijit/Dialog",
	     	"dijit/layout/BorderContainer",
	     	"dijit/layout/AccordionContainer",
	     	"dijit/form/ValidationTextBox",
	     	"dijit/form/Button",
	     	"rosten/widget/TitlePane",
	     	"rosten/app/Main"],
		function(parser, kernel,JSON,lang,query,connect,win,dom,domStyle,domClass,domConstruct,registry,ContentPane,Dialog) {
			rosten.init({webpath : "${request.getContextPath()}",dojogridcss : true});
			kernel.addOnLoad(function() {
				var n = dom.byId("preLoader");
		        kernel.fadeOut({
	                node:n,
	                duration:720,
	                onEnd:function(){
                        domStyle.set(n,"display","none");
	                }
		        }).play();
				
				var data = JSON.parse('${userinfor}');
	   	   		var naviJson = {
		            naviMenuSrc: "${createLink(controller:'system',action:'naviMenu',id:user.id)}",
		            type: "stand"
		        };
				initInstance(naviJson,data);
				connect.connect(registry.byId("personSearchInput"),"onKeyPress",searchPersonByKeyPress);
			});
			lang.extend(ContentPane,{
	   			onDownloadError:function(error){
	   				if(error.status=="401"){
	   					var dlg
	   					var nl = query(".logindlg",win.body())
	   					if(nl.length>0){
	   						dlg = registry.getEnclosingWidget(nl[0]);
	   					}else{
	   						dlg = new Dialog({title:"用户登录"},document.createElement("div"));
	   						domConstruct.place(dlg.domNode,win.body());
	   						domStyle.set(dlg.domNode,"height","185px");
	   						domStyle.set(dlg.domNode,"width","280px");
	   						domClass.add(dlg.domNode,"logindlg");
	   						var div = document.createElement("div");
	   						domConstruct.place(div,dlg.containerNode);
	   						var cp1 = new ContentPane({executeScripts:true,renderStyles:true},div)
	   						cp1.attr("href","${createLink(controller:'login',action:'dlgauth')}");
	   					}
	   					dlg.cp = this;
	   					dlg.show();
	   					return "<span class='dijitContentPaneError'>页面失效，请刷新页面并重新登录！</span>";
	   				}else return this.errorMessage;
	   			}
			});
		});
</script>
</head>
<body>
	<div id="preLoader"><p></p></div>
	<div data-dojo-type="dijit/layout/BorderContainer" id="container"
		data-dojo-props='gutters:false'>
		<div data-dojo-type="dijit/layout/ContentPane" id="header"
			data-dojo-props='region:"top"'>
			<div class="headerTop">
				<table width="100%" border="0" margin=0 cellpadding="0"
					cellspacing="0">
					<tr width="100%">
						<td valign="top" class="headerLogo"></td>
						<td valign="top">
							<div class="nav verticalAlign">
								<span class="nav0Icon">&nbsp;</span> <span class="nav0Div">&nbsp;<span
									id="header_username">
										${(user.chinaName!=null?user.chinaName:user.username) + "&nbsp;" + usertype}
								</span>&nbsp;&nbsp;欢迎您的到来！
								</span> <span class="nav5Icon">&nbsp;</span> <span class="nav5Div"><a
									href="javascript:changeSkin();">更换皮肤</a></span> <span class="nav4Icon">&nbsp;</span>
								<span class="nav4Div"><a href="javascript:addBookmark();">添加为书签</a></span>
								<g:if test="${normal}">
									<span class="nav3Icon">&nbsp;</span>
									<span class="nav3Div"><a
										href="javascript:chgPassword();">密码修改</a></span>
								</g:if>
								<span class="nav2Icon">&nbsp;</span> <span class="nav2Div"><a
									href="javascript:logout();">注销</a></span> <span class="nav1Icon">&nbsp;</span>
								<span class="nav1Div"><a href="javascript:quit();">退出</a></span>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div class="headerMenu">
				<div
					style="float: left; width: 216px; text-align: center; padding: 5px 0 0 0; color: #004c7e;"
					id="header_time">
					<g:formatDate format="yyyy-MM-dd ( EEE )" date="${new Date()}" />
				</div>
				<div class="naviMenuList">
					<ul id="naviMenuUL">
					</ul>
				</div>
			</div>
		</div>
		
		<div data-dojo-type="dijit/layout/BorderContainer" id="home"
			data-dojo-props='region:"center",gutters:false,style:{padding:"1px 1px 0px 1px",height:"100%",width:"100%",display:"none"}'>
			
			<div data-dojo-type="dojox/layout/ContentPane" id="information"
				data-dojo-props='region:"left",executeScripts:true,renderStyles:true,style:"padding:0px;width:206px;border:1px solid #B5BCC7;margin-top:1px;margin-bottom:1px"'>
				<div data-dojo-type="rosten/widget/TitlePane"
					data-dojo-props='title:"常用服务",toggleable:false,moreText:"",style:{textAlign:"center"}'>
					<ul>
						<g:each in="${servicesList}">
							<li>
								<a href="javascript:${it.functionName }('${it.functionArgs }')">
									<img width="39px" height="39px" src="${it.imgUrl }"></img>
									<h5>${it.serviceName }</h5>
								</a>
							</li>
						</g:each>
					</ul>
				</div>
			</div>
			
			<div data-dojo-type="dojox/layout/ContentPane" id="home_center"
				data-dojo-props='region:"center",executeScripts:true,renderStyles:true,style:"padding:1px"'>
				
					<div data-dojo-type="dijit/layout/BorderContainer" data-dojo-props='gutters:false,style:{height:"207px"}' >
					
						<div data-dojo-type="rosten/widget/TitlePane" id="home_gtask"
							data-dojo-props='region:"left",title:"待办工作",toggleable:false,
								height:"157px",width:"50%",style:{marginRight:"1px"},
								_moreClick:more_gtask,moreText:"更多"'>
						</div>
							
						<div data-dojo-type="rosten/widget/TitlePane" id="home_bbs"
							data-dojo-props='region:"center",title:"最新公告",toggleable:false,
								height:"157px",
								_moreClick:more_bbs,moreText:"更多"'>
						</div>	
						
					</div>
				
					<div data-dojo-type="dijit/layout/BorderContainer" data-dojo-props='gutters:false,style:{height:"207px"}' >
						<div data-dojo-type="rosten/widget/TitlePane" style="margin-top:1px" id ="home_personMail"
							data-dojo-props='region:"left",title:"新收邮件",toggleable:false,
								height:"157px",width:"50%",style:{marginRight:"1px"},
								_moreClick:more_mail,moreText:"更多"'>
						</div>
						<div data-dojo-type="rosten/widget/TitlePane"
							data-dojo-props='region:"center",title:"工作日志",toggleable:false,
								height:"157px",
								_moreClick:more_bbs,moreText:"更多"'>
								
							<div class="por-mod"> 
							<!--计划-->
								<div class="por-jh">
							    	<div class="por-jh-tool">
							    		<span><g:formatDate format="yyyy年MM月" date="${new Date()}" />
							    		<a href="#" class="jh-pre-w" title="上一周"></a>第<em>${weekNum }</em>周<a href="#" class="jh-next-w" title="下一周"></a></span>
							    	</div>
								    <div class="por-jh-con">
								    	<table class="por-jh-tab">
								        	<tbody><tr>
									          <td>
									          	<div <g:if test="${nowDay==2 }">class="week-today"</g:if>><h3>星期一</h3><span><g:formatDate format="dd" date="${firstDay}" /></span></div>
									          </td>
									          <td ><div <g:if test="${nowDay==3 }">class="week-today"</g:if>><h3>星期二</h3><span><g:formatDate format="dd" date="${firstDay+1 }" /></span></div></td>
									          <td ><div <g:if test="${nowDay==4 }">class="week-today"</g:if>><h3>星期三</h3><span><g:formatDate format="dd" date="${firstDay+2 }" /></span></div></td>
									          <td ><div <g:if test="${nowDay==5 }">class="week-today"</g:if>><h3>星期四</h3><span><g:formatDate format="dd" date="${firstDay+3 }" /></span></div></td>
									          <td ><div <g:if test="${nowDay==6 }">class="week-today"</g:if>><h3>星期五</h3><span><g:formatDate format="dd" date="${firstDay+4 }" /></span></div></td>
									          <td ><div <g:if test="${nowDay==7 }">class="week-today"</g:if>><h3>星期六</h3><span><g:formatDate format="dd" date="${firstDay+5 }" /></span></div></td>
									          <td ><div <g:if test="${nowDay==1 }">class="week-today"</g:if>><h3>星期日</h3><span><g:formatDate format="dd" date="${firstDay+6 }" /></span></div></td>
									        </tr>
									      </tbody></table>
								    </div>
								</div>
							</div>
						</div>
					</div>
				 	
				 	<div data-dojo-type="dijit/layout/BorderContainer" data-dojo-props='gutters:false,style:{height:"207px"}' >
						<div data-dojo-type="rosten/widget/TitlePane" style="margin-top:1px" id ="home_download"
							data-dojo-props='region:"left",title:"下载专区",toggleable:false,showTitleCount:false,
								height:"157px",width:"50%",style:{marginRight:"1px"},
								_moreClick:more_downloadFile,moreText:"更多"'>
						</div>
						<div data-dojo-type="rosten/widget/TitlePane"
							data-dojo-props='region:"center",title:"通讯录查询",toggleable:false,showTitleCount:false,
								height:"157px",
								_moreClick:more_bbs,moreText:""'>
								
								<div style="text-align:center;margin-bottom:6px">
			                      <span>关键字：</span>
			                      <input id="personSearchInput" data-dojo-type="dijit/form/ValidationTextBox" 
			                      	data-dojo-props='style:{width:"200px",marginLeft:"1px"},placeHolder:"姓名/电话/邮箱"'/>
			                      <button id="personSearchButton" data-dojo-type="dijit/form/Button" data-dojo-props='onClick:function(){searchPerson()}'>查询</button>
			                    </div>
			                    <div class="personSearch">
									<table width="98%" class="tab_css">
										<THEAD> 
											<tr>
												<th width="15%">姓名</th>
												<th width="20%">办公电话</th>
												<th width="20%">移动电话</th>
												<th>邮箱地址</th>
											</tr>
										</THEAD>
										<tbody id="personSearch">
										</tbody>
									</table>
								</div>
						</div>
					</div>
			</div>	
		</div>	
		
		<div data-dojo-type="dijit/layout/BorderContainer" id="modelMain"
			data-dojo-props='region:"center",splitter:true,style:{padding:"1px 1px 0px 1px",height:"100%"}'>
			<div data-dojo-type="dijit/layout/BorderContainer" id="sideBar"
				data-dojo-props='region:"left",minSize:"210",splitter:true,gutters:false,"class":"sideBar",style:"padding:0"'>

				<div id="navigationContainer"
					data-dojo-type="dijit/layout/AccordionContainer"
					data-dojo-props='region:"center"'>
					<div id="navigation" data-dojo-type="dojox/layout/ContentPane"
						data-dojo-props='style:"padding:0px",executeScripts:true,renderStyles:true,'>
					</div>
				</div>
			</div>

			<div data-dojo-type="dojox/layout/ContentPane" id="contentBody"
				data-dojo-props='region:"center",executeScripts:true,renderStyles:true,style:"padding:1px"'>
			</div>
		</div>
		<div data-dojo-type="dijit/layout/ContentPane" id="footer"
			data-dojo-props='region:"bottom",style:"border:1px solid #B5BCC7"'>Copyright @2012 ; rosten
			版权所有,提供技术支持</div>
	</div>
</body>
</html>
