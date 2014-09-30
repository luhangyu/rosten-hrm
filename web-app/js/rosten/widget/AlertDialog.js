define(["dojo/_base/declare","dojo/_base/kernel","dojo/dom-style","rosten/widget/_Dialog"],function(_1,_2,_3,_4){
return _1("rosten.widget.AlertDialog",rosten.widget._Dialog,{height:"100px",width:"300px",title:"RostenDialog",mode:"CLOSE",message:"",msgNode:null,buildContent:function(_5){
var _6=document.createElement("div");
_3.set(_6,{"height":"24px","fontSize":"12px"});
_5.appendChild(_6);
var _7=document.createElement("img");
var _8=_2.moduleUrl("rosten","widget/templates/alert_1.gif");
if(dojo.isIE){
_7.src=_8;
}else{
_7.setAttribute("src",_8);
}
_3.set(_7,{"float":"left"});
_6.appendChild(_7);
this.msgNode=document.createElement("div");
_6.appendChild(this.msgNode);
if(this.message!=""){
this.msgNode.innerHTML="&nbsp;&nbsp;"+this.message;
}else{
this.msgNode.innerHTML="&nbsp;&nbsp;hello welcome!";
}
},refresh:function(_9){
this.message=_9;
this.msgNode.innerHTML="&nbsp;&nbsp;"+_9;
}});
});

