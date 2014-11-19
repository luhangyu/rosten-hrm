define(["dojo/_base/declare","dojo/_base/kernel","dojo/_base/lang","dojo/dom-class","dojo/_base/xhr","dijit/_WidgetBase","dijit/_TemplatedMixin","dojox/collections/SortedList","rosten/util/general"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9){
return _1("rosten.widget.SimpleNavigation",[_6,_7],{general:new _9(),id:"",url:"",urlArgs:null,templateString:"<div class=\"simpleNavigation\""+"\t><div  data-dojo-attach-point=\"containerNode\""+"\t>Loading...</div>"+"</div>",navigationData:null,defaultentry:"",ulnode:null,constructor:function(){
},postCreate:function(){
this.id=this.id!=""?this.id:this.widgetId;
this.navigationData=new _8();
if(this.url!=""){
this.refreshNavigation(this.url);
}
},refreshNavigation:function(_a){
this.containerNode.innerHTML="";
if(this.url!=_a){
this.url=_a;
}
var _b={url:this.url,sync:true,preventCache:true,handleAs:"json",load:_3.hitch(this,function(_c){
this._setData(_c);
}),error:_3.hitch(this,function(_d){
console.log("get Navigation data is error: ",_d);
this.onDownloadError(_d);
})};
if(this.urlArgs!=null){
_b.content=this.urlArgs;
}
_5.post(_b);
},onDownloadError:function(_e){
},_setData:function(_f){
console.log("navigation set data start");
if(this.navigationData!=null){
this.navigationData.clear();
}else{
this.navigationData=new _8();
}
var _10=document.createElement("ul");
for(var i=0;i<_f.length;i++){
if(i==0){
this.defaultEntity=_f[i].url;
}
if(_f[i].isDefault==true||_f[i].isDefault=="true"){
this.defaultEntity=_f[i].url;
}
var _11=_f[i]["name"];
var img=_f[i]["img"];
var _12=_f[i]["href"];
var _13=this.general.stringLeft(this.general.stringRight(_12,"\""),"\"");
var _14=document.createElement("li");
_14.setAttribute("liid",_13);
var _15=document.createElement("A");
_15.setAttribute("href",_12);
var _16=document.createElement("img");
var _17=document.createAttribute("src");
_17.nodeValue=img;
_16.setAttributeNode(_17);
_15.appendChild(_16);
_15.appendChild(document.createTextNode("  "+_11));
_14.appendChild(_15);
_10.appendChild(_14);
this.navigationData.add(_13,_11);
this.ulnode=_10;
}
this.containerNode.appendChild(_10);
console.log("navigation set data end");
},rendNavigationClass:function(str){
_2.forEach(this.ulnode.childNodes,function(_18){
if(_18.getAttribute("liid")==str){
_4.add(_18,"bgClass");
}else{
_4.remove(_18,"bgClass");
}
});
},getShowName:function(key){
if(this.navigationData.contains(key)){
var _19=this.navigationData.item(key);
return _19;
}else{
return "";
}
}});
});

