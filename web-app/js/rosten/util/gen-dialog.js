define(["dojo/_base/declare","dojo/_base/kernel","dojo/dom-style","dojo/dom-attr","dojo/_base/window","dijit/registry","dijit/Dialog","rosten/widget/ShowDialog"],function(_1,_2,_3,_4,_5,_6,_7,_8){
return _1("rosten.util.gen-dialog",null,{createRostenShowDialog:function(_9,_a){
var _b={src:_9};
if(_a){
if(_a.callback){
_b.callback=_a.callback;
}
if(_a.callbackargs){
_b.callbackargs=_a.callbackargs;
}
if(_a.onLoadFunction){
_b.onLoadFunction=_a.onLoadFunction;
}
}
if(this.rostenShowDialog){
this.rostenShowDialog.destroy();
}
this.rostenShowDialog=new _8(_b);
},getRostenShowDialog:function(){
if(this.rostenShowDialog){
return this.rostenShowDialog;
}
},hideRostenShowDialog:function(){
if(this.rostenShowDialog){
console.log("showdialog start hide...");
this.rostenShowDialog.hide();
this.rostenShowDialog.destroy();
console.log("showdialog hide is sucessful...");
}
},showWaitDialog_1:function(_c,_d){
var _e=_2.byId(_d);
if(_e){
_3.set(_e,"display","block");
}else{
var _f=document.createElement("div");
_4.set(_f,"id",_d);
_f.innerHTML="<img align='middle' height='25px' src='jslib/rosten/src/share/wait_big.gif'>"+"<span style='font-size:20px;color:#414141;vertical-align:middle;font-weight:bold;padding:15px 20px 10px 10px;margin:5px 5px'>请稍候,正在获取内容...</span>";
_3.set(_f,{"display":"block","vertical-align":"middle","height":"100%","textAlign":"center"});
_c.appendChild(_f);
}
},hideWaitDialog_1:function(_10){
var _11=_2.byId(_10);
if(_11){
_3.set(_11,"display","none");
}
},showWaitDialog:function(_12){
if(!_12){
var _13="waitDialogId";
}else{
var _13=_12;
}
var _14=_6.byId(_13);
if(!_14){
var div=document.createElement("div");
var _15=document.createElement("div");
_15.innerHTML="<img align='middle' height='25px' src='jslib/rosten/src/share/wait_big.gif'>"+"<span style='font-size:20px;color:#414141;vertical-align:middle;font-weight:bold;padding:15px 20px 10px 10px;margin:5px 5px'>请稍候,正在获取内容...</span>";
_5.body().appendChild(div);
_14=new _7({id:_13,autofocus:false},div);
_3.set(_14.titleBar,"display","none");
_14.setContent(_15);
_14.show();
}else{
_14.show();
}
},hideWaitDialog:function(_16){
if(!_16){
var _17="waitDialogId";
}else{
var _17=_16;
}
var _18=_6.byId(_17);
if(_18){
_18.hide();
console.log("hide the dialog......");
}
}});
});

