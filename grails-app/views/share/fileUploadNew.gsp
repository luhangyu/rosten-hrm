<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>fileUpload</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
</style>
<script type="text/javascript">
	
</script>

<body>
<div class="rosten_form">
<g:if test="${isShowFile}">
		<div data-dojo-type="dojox/form/Uploader"  type="file" data-dojo-id="uploaderDom"
			id="fileUploader"  data-dojo-props="name:'uploadedfile',showInput:'before',multiple:false,
				uploadUrl:'${createLink(controller:'share',action:'uploadFileNew',id:docEntityId,params:[uploadPath:uploadPath])}'
			">浏览...
			
			<script type="dojo/method" data-dojo-event="onComplete" data-dojo-args="dataArray">
				if(dojo.isIE){
					dataArray = dojo.fromJson(dataArray);
				}
				uploaderActionDom.setDisabled(false);
				if(dataArray.result=="true"){
					uploaderDom.reset();
					rosten.addAttachShowNew(dojo.byId("fileUpload_show"),dataArray);	
													
				}else if(dataArray.result=="big"){
					rosten.alert("上传文件过大，请重新上传！");
				}else{rosten.alert("上传失败");}
			</script>
		</div>
		<div data-dojo-type="dijit/form/Button" data-dojo-props="label:'清空'">
			<script type="dojo/method" data-dojo-event="onClick">
			uploaderDom.reset();
		</script>
		</div>
		<div data-dojo-type="dijit/form/Button" data-dojo-props="label:'上传'" data-dojo-id="uploaderActionDom">
			<script type="dojo/method" data-dojo-event="onClick">
			var _1 = uploaderDom.get("value");
			if(_1 && _1!=""){
				uploaderDom.upload();
				this.setDisabled(true);
			}else{rosten.alert("请先选择上传文件！");}
			
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

	<fieldset class="fieldset-form" style="margin-top:5px">
		<legend class="tableHeader">附件查看</legend>
		<div id="fileUpload_show" style="padding:5px;margin:8px;font-size:14px">
			<g:each in="${attachFiles}">
				<div id="${it.id}" style="height:30px;width:50%;float:left">
					<a href="${createLink(controller:'system',action:'downloadFile',id:it.id)}" style="margin-right:20px" dealId="${it.id }">${it.name }</a>
					<g:if test="${isShowFile}">
						<a href="javascript:rosten.deleteFile('fileUpload_show','${it.id}')" style="color:green">删除</a>
					</g:if>
				</div>
			</g:each>
		</div>
	</fieldset>
</div>
</body>
</html>
