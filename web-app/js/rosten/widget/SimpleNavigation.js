define(["dojo/_base/declare","dojo/_base/lang","dojo/_base/xhr","dijit/_WidgetBase","dijit/_TemplatedMixin","dojox/collections/SortedList","rosten/util/general"],function(_1,_2,_3,_4,_5,_6,_7){
return _1("rosten.widget.SimpleNavigation",[_4,_5],{general:new _7(),id:"",url:"",urlArgs:null,templateString:"<div class=\"simpleNavigation\""+"\t><div  data-dojo-attach-point=\"containerNode\""+"\t>Loading...</div>"+"</div>",navigationData:null,constructor:function(){
},postCreate:function(){
this.id=this.id!=""?this.id:this.widgetId;
this.navigationData=new _6();
if(this.url!=""){
this.refreshNavigation(this.url);
}
},refreshNavigation:function(_8){
this.containerNode.innerHTML="";
if(this.url!=_8){
this.url=_8;
}
var _9={url:this.url,sync:true,preventCache:true,handleAs:"json",load:_2.hitch(this,function(_a){
this._setData(_a);
}),error:_2.hitch(this,function(_b){
console.log("get Navigation data is error: ",_b);
this.onDownloadError(_b);
})};
if(this.urlArgs!=null){
_9.content=this.urlArgs;
}
_3.post(_9);
},onDownloadError:function(_c){
},_setData:function(_d){
console.log("navigation set data start");
if(this.navigationData!=null){
this.navigationData.clear();
}else{
this.navigationData=new _6();
}
var _e=document.createElement("ul");
for(var i=0;i<_d.length;i++){
var _f=_d[i]["name"];
var img=_d[i]["img"];
var _10=_d[i]["href"];
var _11=document.createElement("li");
var _12=document.createElement("A");
_12.setAttribute("href",_10);
var _13=document.createElement("img");
var _14=document.createAttribute("src");
_14.nodeValue=img;
_13.setAttributeNode(_14);
_12.appendChild(_13);
_12.appendChild(document.createTextNode("  "+_f));
_11.appendChild(_12);
_e.appendChild(_11);
this.navigationData.add(this.general.stringLeft(this.general.stringRight(_10,"\""),"\""),_f);
}
this.containerNode.appendChild(_e);
console.log("navigation set data end");
},getShowName:function(key){
if(this.navigationData.contains(key)){
var _15=this.navigationData.item(key);
return _15;
}else{
return "";
}
}});
});

