define(["dojo/_base/declare","dojo/_base/kernel","dojo/_base/lang","dojo/_base/xhr","dojo/dom-style","dijit/_WidgetBase","dijit/_TemplatedMixin","dijit/form/Button","dijit/Toolbar","dojo/_base/connect","rosten/util/general","rosten/kernel/_kernel"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c){
return _1("rosten.widget.ActionBar",[_6,_7],{id:"",actionBarSrc:"",actionTextHeight:12,templateString:"<div data-dojo-attach-point=\"containerNode\" class=\"ActionBarOuter\">actionBar is Loading...</div>",constructor:function(){
},postCreate:function(){
this.id=this.id!=""?this.id:this.widgetId;
this.containerNode.innerHTML="";
this.toolBar=new _9({});
this.containerNode.appendChild(this.toolBar.domNode);
if(this.actionBarSrc==""){
this.containerNode.innerHTML="remote actionBar data source required...";
}else{
this._getData(this.actionBarSrc);
}
},refresh:function(_d){
console.log("ActionBar refresh");
if(_d&&_d!=""){
this.actionBarSrc=_d;
}
this.toolBar.destroyDescendants();
this._getData(this.actionBarSrc);
console.log("ActionBar refresh is end");
},_getData:function(_e){
var _f={url:_e,handleAs:"json",load:_3.hitch(this,function(_10){
this._setListData(_10);
}),error:_3.hitch(this,function(_11){
console.log("ActionBar error occurred: ",_11);
_c.errordeal(this.containerNode,"无法初始化操作条数据...");
this.onDownloadError(_11);
})};
_4.get(_f);
},onDownloadError:function(_12){
},_setListData:function(_13){
for(var i=0;i<_13.length;i++){
var _14=_13[i];
var _15=new _8({label:_14.name,iconClass:"actionBarIcon"});
_5.set(_15.domNode,"fontSize",this.actionTextHeight+"px");
_5.set(_15.iconNode,{backgroundImage:"url('"+(_14.img)+"')"});
if(_14.action.indexOf(".")!=-1){
_a.connect(_15,"onClick",rosten,_b.stringRight(_14.action,"."));
}else{
_a.connect(_15,"onClick",_14.action);
}
this.toolBar.addChild(_15);
}
}});
});

