define(["dojo/_base/kernel","dojo/has","dojo/dom-style","dojo/dom-class","dojo/_base/lang","dijit/registry","rosten/kernel/_kernel","rosten/widget/ConfirmDialog","rosten/widget/AlertDialog","rosten/widget/ActionBar","rosten/widget/RostenDialog","rosten/widget/ShowDialog","rosten/widget/CommentDialog"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d){
var _e={};
_e.addAttachShow=function(_f,_10){
var a=document.createElement("a");
if(_2("ie")){
a.href=rosten.webPath+"/system/downloadFile/"+_10.fileId;
}else{
a.setAttribute("href",rosten.webPath+"/system/downloadFile/"+_10.fileId);
}
a.setAttribute("style","margin-right:20px");
a.setAttribute("dealId",_10.fileId);
a.innerHTML=_10.fileName;
_f.appendChild(a);
};
_e.addCommentDialog=function(obj){
if(!_7.sys_commentDialog||!_6.byId("sys_commentDialog")){
_7.sys_commentDialog=new _d({title:"填写意见",id:"CommentDialog"});
}
_7.sys_commentDialog.queryDlgClose=function(){
};
_7.sys_commentDialog.open();
_7.sys_commentDialog.refresh(obj);
return _7.sys_commentDialog;
};
_e.addRostenBar=function(_11,_12){
var _13=_1.create("div",null,_11,"first");
_4.add(_13,"rosten_action");
var _14=new _a({id:"rosten_actionBar",actionBarSrc:_12});
_13.appendChild(_14.domNode);
};
_e.confirm=function(_15){
if(!_7.sys_confirmDialog||!_6.byId("sys_confirmDialog")){
_7.sys_confirmDialog=new _8({title:"系统对话框",id:"sys_confirmDialog"});
}
_7.sys_confirmDialog.callback=function(){
};
_7.sys_confirmDialog.queryDlgClose=function(){
};
_7.sys_confirmDialog.open();
_7.sys_confirmDialog.refresh(_15);
return _7.sys_confirmDialog;
};
_e.alert=function(_16){
if(!_7.sys_alertDialog||!_6.byId("sys_alertDialog")){
_7.sys_alertDialog=new _9({title:"系统对话框",id:"sys_alertDialog"});
}
_7.sys_alertDialog.queryDlgClose=function(){
};
_7.sys_alertDialog.open();
_7.sys_alertDialog.refresh(_16);
return _7.sys_alertDialog;
};
_e.getStoreItem=function(_17,_18,_19){
_17.fetch({query:_18,queryOptions:{deep:true},onComplete:_19});
};
_e.toggleAction=function(obj,_1a){
var _1b=_6.getEnclosingWidget(obj);
if(_1b){
_1b.set("disabled",_1a);
}
};
_5.mixin(_7,_e);
return _e;
});

