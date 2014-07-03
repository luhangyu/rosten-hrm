define(["dojo/_base/declare","dojo/_base/kernel","dojo/dom-style","dojo/_base/connect","dojo/_base/lang","dojo/query!css3","dijit/registry","dijit/form/Button","rosten/widget/RostenDialog"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9){
return _1("rosten.widget._Dialog",null,{_dialog:null,height:"320px",width:"350px",id:"system_dialog",contentPane:null,controlPane:null,title:"Dialog_Title",callback:null,queryDlgClose:null,mode:"OKCANCEL",initialized:false,constructor:function(_a){
for(var _b in _a){
this[_b]=_a[_b];
}
this._dialog=_7.byId(this.id);
if(!this._dialog){
this.contentPane=document.createElement("div");
this.contentPane.setAttribute("id","system_dialog_contentpane");
this.controlPane=document.createElement("div");
this.controlPane.setAttribute("id","system_dialog_controlpane");
this._dialog=new _9({id:this.id,refocus:false,title:this.title},document.createElement("div"));
_3.set(this._dialog.domNode,"width",this.width);
_3.set(this._dialog.domNode,"height",this.height);
this._dialog.containerNode.appendChild(this.contentPane);
this._dialog.containerNode.appendChild(this.controlPane);
_4.connect(this._dialog,"hide",this,function(){
this._dialog.destroy();
});
}else{
this.contentPane=_2.query("#system_dialog_contentpane",this._dialog.domNode)[0];
this.controlPane=_2.query("#system_dialog_controlpane",this._dialog.domNode)[0];
this.initialized=true;
}
},open:function(){
if(this.initialized==false){
this.buildContent(this.contentPane);
this.buildControl(this.controlPane);
this.initialized=true;
}
this._dialog.show();
if(this.mode=="CLOSE"){
setTimeout(_5.hitch(this,"close"),800);
}
},close:function(){
this._dialog.hide();
this.queryDlgClose();
},destroy:function(){
if(this._dialog){
this._dialog.destroy();
}
},doAction:function(){
var _c=this.getData();
this._dialog.hide();
this.callback(_c);
},buildControl:function(_d){
if(this.mode=="OKCANCEL"){
var _e=new _8({label:"确定",showLabel:true,iconClass:"okIcon"},document.createElement("div"));
var _f=new _8({label:"取消",showLabel:true,iconClass:"cancelIcon"},document.createElement("div"));
_4.connect(_f,"onClick",this,"close");
_4.connect(_e,"onClick",this,"doAction");
_d.appendChild(_e.domNode);
_d.appendChild(_f.domNode);
_3.set(_d,"marginRight","auto");
_3.set(_d,"marginLeft","auto");
_3.set(_d,"textAlign","center");
_3.set(_d,"marginTop","5px");
}else{
if(this.mode=="CLOSE"){
var _e=new _8({label:"关闭",showLabel:true,iconClass:"okIcon"},document.createElement("div"));
_4.connect(_e,"onClick",this,"close");
_d.appendChild(_e.domNode);
_3.set(_d,"marginRight","auto");
_3.set(_d,"marginLeft","auto");
_3.set(_d,"textAlign","center");
}
}
},getContentPane:function(){
return this.contentPane;
},getControlPane:function(){
return this.controlPane;
},buildContent:function(_10){
_10.innerHTML="Loading....";
},getData:function(){
},callback:function(){
},queryDlgClose:function(){
},refresh:function(){
this.contentPane=_6("#system_dialog_contentpane",this._dialog.domNode)[0];
this.contentPane.innerHTML="";
this.buildContent(this.contentPane);
}});
});

