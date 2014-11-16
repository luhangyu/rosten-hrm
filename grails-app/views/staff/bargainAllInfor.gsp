<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>合同管理</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
</style>

<body>
<form data-dojo-type="dijit/form/Form" method="post" target="bargain_iframe"
	action="${createLink(controller:'staff',action:'addBargainInfor',id:personInfor?.id)}"  id="bargain_form" enctype="multipart/form-data">

<div data-dojo-type="rosten/widget/TitlePane" 
	data-dojo-props='title:"合同基本信息",toggleable:false,moreText:"",height:"80px",marginBottom:"2px"'>
	<input  data-dojo-type="dijit/form/ValidationTextBox" id="barginId"  data-dojo-props='name:"barginId",style:{display:"none"},value:"${bargain?.id }"' />
	<table border="0" width="740" align="left">
		<tr>
			<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>合同编号：</div></td>
		  	<td width="250">
		    	<input id="bargain_serialNo" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"bargainSerialNo",trim:true,required:true,readOnly:true,
							value:"${bargain?.bargainSerialNo}"
	                '/>
		    </td>
		 	<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>合同类别：</div></td>
		  	<td width="250">
		    	<select id="bargain_bargainType" data-dojo-type="dijit/form/ComboBox"
	                 	data-dojo-props='name:"bargainType",trim:true,required:true,${fieldAcl.isReadOnly("bargainType")},
							value:"${bargain?.bargainType}"
                '>
	                
	                <g:each in="${bargainTypeList}" var="item">
	                	<option value="${item.code }">${item.name }</option>
	                </g:each>	
	            </select>
		    </td>
		</tr>
		<tr>
			<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>起聘日期：</div></td>
	    	<td width="250">
		    	<input id="bargain_startDate" data-dojo-type="dijit/form/DateTextBox" 
	               	data-dojo-props='name:"startDate",${fieldAcl.isReadOnly("startDate")},
	               		trim:true,required:true,
						value:"${bargain?.getFormatteStartDate()}"
	          	'/>
           	</td>
		 	<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>终聘日期：</div></td>
		  	<td width="250">
		    	<input id="bargain_endDate" data-dojo-type="dijit/form/DateTextBox" 
	                 	data-dojo-props='name:"endDate",trim:true,required:true,${fieldAcl.isReadOnly("endDate")},
							value:"${bargain?.getFormatteEndDate()}"
	                '/>
		    </td>
		</tr>
	</table>
</div>
<div data-dojo-type="rosten/widget/TitlePane" class="upload" data-dojo-props='title:"附件信息",toggleable:false,moreText:"",marginBottom:"2px"'>

<g:if test="${isShowFile}">
	<div data-dojo-type="dojox/form/Uploader"  type="file" data-dojo-id="uploaderDom"
		id="fileUploader"  data-dojo-props="name:'uploadedfile',showInput:'before',multiple:false">浏览...
	</div>
	<div data-dojo-type="dijit/form/Button" data-dojo-props="label:'清空'">
		<script type="dojo/method" data-dojo-event="onClick">
			uploaderDom.reset();
		</script>
	</div>
	
	<div data-dojo-type="dijit/form/Button" data-dojo-props="label:'显示详情'">
		<script type="dojo/method" data-dojo-event="onClick">
			dojo.style(uploaderListDom.domNode,"display","");
		</script>
	</div>
	
	<div data-dojo-type="dijit/form/Button" data-dojo-props="label:'隐藏详情'">
		<script type="dojo/method" data-dojo-event="onClick">
			dojo.style(uploaderListDom.domNode,"display","none");
		</script>
	</div>
	
	<div id="fileUpload_fileList" data-dojo-type="dojox/form/uploader/FileList"  data-dojo-id="uploaderListDom" style="display:none"
		data-dojo-props='uploaderId:"fileUploader",headerIndex:"序号",headerType:"类型",headerFilename:"文件名",headerFilesize:"大小"'></div>
</g:if>
	
	<div style="margin-top:10px">
		<fieldset class="fieldset-form">
			<legend class="tableHeader">附件查看</legend>
			<div id="fileUpload_show" style="padding:5px;margin:8px;font-size:14px">
				<g:each in="${attachFiles}">
					<div style="height:30px;width:50%;float:left">
						<a href="${createLink(controller:'system',action:'downloadFile',id:it.id)}" style="margin-right:20px" dealId="${it.id }">${it.name }</a>
						<g:if test="${isShowFile}">
							<a href="javascript:deleteFile('${it.id}')" style="color:green">删除</a>
						</g:if>
					</div>
				</g:each>
			</div>
		</fieldset>
	</div>
</div>
</form>

<div data-dojo-type="rosten/widget/TitlePane" data-dojo-props='"class":"rostenBargainTitleGrid",title:"历史合同信息",toggleable:false,moreText:"",marginBottom:"2px"'>
	<div data-dojo-type="rosten/widget/RostenGrid" id="bargainItemGrid" data-dojo-id="bargainItemGrid"
		data-dojo-props='imgSrc:"../../images/rosten/share/wait.gif",showPageControl:false,url:"${createLink(controller:'bargain',action:'bargainItemGrid',id:bargain?.id)}"'></div>
</div>

<g:if test="${isShowFile}">
<div style="margin:10px 0 0 5px">
	<div data-dojo-type="dijit/form/Button" data-dojo-props="label:'录入合同'">
		<script type="dojo/method" data-dojo-event="onClick">
			user_addBargain();
		</script>
	</div>
</div>
</g:if>



<iframe name="bargain_iframe" scrollbars=no menubar=no height=600 width=800 resizable=yes toolbar=no status=no style="display:none"></iframe>
</body>
</html>
