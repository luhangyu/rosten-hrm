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
	<table class="tableData" style="width:740px;margin:0px">
    <g:if test="${isShowFile}">
		<tr>
		    <td width="120"><div align="right">附件：</div></td>
		    <td colspan=3>
		    	<div data-dojo-type="dijit/form/DropDownButton" >
					<span>添加附件</span>
					<div data-dojo-type="dijit/TooltipDialog" id="fileUpload_dialog" data-dojo-props="title: 'fileUpload'" style="width:380px">
						<script type="dojo/method" data-dojo-event="onOpen" data-dojo-args="dataArray">
							if (dojo.isIE) {
								dijit.byId("isIE").set("value","yes");
							}
						</script>
							<form data-dojo-type="dijit/form/Form" method="post"
								action="${createLink(controller:docEntity,action:'uploadFile',id:docEntityId)}" id="fileUpload_form" enctype="multipart/form-data">
								
								<input  data-dojo-type="dijit/form/ValidationTextBox" id="isIE"  data-dojo-props='name:"isIE",style:{display:"none"},value:"no"' />
								
								<div data-dojo-type="dojox/form/Uploader"  type="file" 
									id="fileUploader"  data-dojo-props="name:'uploadedfile'">添加
									<script type="dojo/method" data-dojo-event="onComplete" data-dojo-args="dataArray">
										if(dojo.isIE){
											dataArray = dojo.fromJson(dataArray);
										}
										if(dataArray.result=="true"){
											rosten.addAttachShow(dojo.byId("fileShow"),dataArray);
											dijit.byId("fileUpload_dialog").reset();
											dijit.byId("fileUpload_dialog").onCancel();
											
										}else if(dataArray.result=="big"){
											rosten.alert("上传文件过大，请重新上传！");
										}else{rosten.alert("上传失败");}
									</script>
								</div>
								
								<div id="fileUpload_fileList" data-dojo-type="dojox/form/uploader/FileList" 
									data-dojo-props='uploaderId:"fileUploader",headerIndex:"#",headerType:"类型",headerFilename:"文件名",headerFilesize:"大小"'></div>
								
								<div class="dijitDialogPaneActionBar">
									<button data-dojo-type="dijit/form/Button" type="reset">重置</button>
									<button data-dojo-type="dijit/form/Button" type="submit">上传
									</button>
									<button data-dojo-type="dijit/form/Button" type="button">取消
										<script type="dojo/method" data-dojo-event="onClick">
											dijit.byId("fileUpload_dialog").onCancel();
										</script>
									</button>
								</div>
							</form>
					</div>
				</div>
		    
		    <td>    
		</tr>
	</g:if>
		<tr>
			<td width="120">
				<g:if test="${!isShowFile}">
					<div align="right">附件：</div>
				</g:if>
			</td>
			<td colspan=3>
				<g:if test="${newDoc}">
					<div style="color:red">请保存后再添加附件！</div>
				</g:if>
				<g:else>
					<div id="fileShow">
						<g:each in="${attachFiles}">
							<a href="${createLink(controller:'system',action:'downloadFile',id:it.id)}" style="margin-right:20px" dealId="${it.id }">${it.name }</a>
						</g:each>
					</div>
				</g:else>
			</td>
		</tr>
    </table>
</body>
</html>
