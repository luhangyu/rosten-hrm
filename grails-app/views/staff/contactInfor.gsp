<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>通讯方式</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
</style>

<body>
	<input  data-dojo-type="dijit/form/ValidationTextBox" id="contactInforId"  data-dojo-props='name:"contactInforId",style:{display:"none"},value:"${contactInforEntity?.id }"' />
	<table border="0" width="740" align="left">
		<tr>
			<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>移动电话：</div></td>
		  	<td width="250">
		    	<input id="mobile" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"mobile",trim:true,required:true,${fieldAcl.isReadOnly("mobile")},
							value:"${contactInforEntity?.mobile}"
	                '/>
		    </td>
			<td width="120"><div align="right">固定电话：</div></td>
		  	<td width="250">
		    	<input id="phone" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"phone",trim:true,${fieldAcl.isReadOnly("phone")},
							value:"${contactInforEntity?.phone}"
	                '/>
		    </td>
		</tr>
		
		<tr>
		 	<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>QQ号码：</div></td>
		  	<td width="250" >
		    	<input id="qq" data-dojo-type="dijit/form/ValidationTextBox" 
                 	data-dojo-props='name:"qq",trim:true,required:true,${fieldAcl.isReadOnly("qq")},
						value:"${contactInforEntity?.qq}"
                '/>
		    </td>
		    
		     <td width="120"><div align="right">微信号：</div></td>
		 	 <td width="250" >
		    	<input id="wechat" data-dojo-type="dijit/form/ValidationTextBox" 
                 	data-dojo-props='name:"wechat",trim:true,${fieldAcl.isReadOnly("wechat")},
						value:"${contactInforEntity?.wechat}"
                '/>
		    </td>
		</tr>
		
		<tr>
	 		<td width="120"><div align="right"><span style="color:red">*&nbsp;</span>通讯地址：</div></td>
	    	<td width="250">
		    	<input id="address" data-dojo-type="dijit/form/ValidationTextBox" 
	               	data-dojo-props='name:"address",${fieldAcl.isReadOnly("address")},
	               		trim:true,required:true,
						value:"${contactInforEntity?.address}"
	          	'/>
           	</td>
		    <td width="120"><div align="right"><span style="color:red">*&nbsp;</span>邮编：</div></td>
		    <td width="250">
		    	<input id="addressPostcode" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"addressPostcode",trim:true,required:true,${fieldAcl.isReadOnly("addressPostcode")},
							value:"${contactInforEntity?.addressPostcode}"
	                '/>
		    </td>
		     
		</tr>
		<tr>
	 		<td width="120"><div align="right">家庭地址：</div></td>
		  	<td width="250">
		    	<input id="homeAddress" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"homeAddress",trim:true,${fieldAcl.isReadOnly("homeAddress")},
							value:"${contactInforEntity?.homeAddress}"
	                '/>
		    </td>
		    
		    <td width="120"><div align="right">家庭邮编：</div></td>
		  	<td width="250">
		    	<input id="postcode" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"postcode",trim:true,${fieldAcl.isReadOnly("postcode")},
							value:"${contactInforEntity?.postcode}"
	                '/>
		    </td>
		</tr>
		<tr>
		 	<td width="120"><div align="right">电子邮箱：</div></td>
		  	<td width="250" >
		    	<input id="email" data-dojo-type="dijit/form/ValidationTextBox" 
	                 	data-dojo-props='name:"email",trim:true,${fieldAcl.isReadOnly("email")},
							value:"${contactInforEntity?.email}"
	                '/>
		    </td>
		    <td></td>
		    <td></td>
		</tr>
		
	</table>
</body>
</html>
