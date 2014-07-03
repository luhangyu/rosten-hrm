define(["dojo/_base/window","dojo/_base/kernel","dojo/_base/lang","dojo/_base/xhr","dojo/dom-construct","dojo/dom","dojo/has","dojo/dom-class","dojo/dom-style","dijit/registry"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a){
var _b={version:"V1.0",variable:{},dojoPath:"js",dojothemecss:"claro",rostenthemecss:"normal",webPath:"/web"};
_b.setDojoVersion=function(_c){
_b.dojoVersion=_c;
};
_b.getDojoVersion=function(){
return _b.dojoVersion;
};
_b.setDojoPath=function(_d){
_b.dojoPath=_d;
};
_b.getDojoPath=function(){
return _b.dojoPath;
};
_b.setDojoThemeCss=function(_e){
_b.dojothemecss=_e;
};
_b.setRostenThemeCss=function(_f){
_b.rostenthemecss=_f;
};
_b.readNoTime=_b._readNoTime=function(url,_10,_11,_12,_13){
var _14={url:url,handleAs:"json",preventCache:true,content:_10,encoding:"utf-8"};
if(_13){
_14.form=_13;
}
if(_3.isFunction(_11)){
_14.load=_11;
}
if(_3.isFunction(_12)){
_14.error=_12;
}else{
_14.error=function(_15,_16){
console.log(_15);
};
}
_4.post(_14);
};
_b.read=_b._read=function(url,_17,_18,_19,_1a){
var _1b={url:url,timeout:3000,handleAs:"json",preventCache:true,content:_17,encoding:"utf-8"};
if(_1a){
_1b.form=_1a;
}
if(_3.isFunction(_18)){
_1b.load=_18;
}
if(_3.isFunction(_19)){
_1b.error=_19;
}else{
_1b.error=function(_1c,_1d){
console.log(_1c);
};
}
_4.post(_1b);
};
_b.readSync=_b._readSync=function(url,_1e,_1f,_20,_21){
var _22={url:url,timeout:3000,sync:true,handleAs:"json",preventCache:true,encoding:"utf-8",content:_1e};
if(_21){
_22.form=_21;
}
if(_3.isFunction(_1f)){
_22.load=_1f;
}
if(_3.isFunction(_20)){
_22.error=_20;
}else{
_22.error=function(_23,_24){
console.log(_23);
};
}
_4.post(_22);
};
_b.readSyncNoTime=_b._readeSyncNoTime=function(url,_25,_26,_27,_28){
var _29={url:url,sync:true,handleAs:"json",preventCache:true,content:_25,encoding:"utf-8"};
if(_28){
_29.form=_28;
}
if(_3.isFunction(_26)){
_29.load=_26;
}
if(_3.isFunction(_27)){
_29.error=_27;
}else{
_29.error=function(_2a,_2b){
console.log(_2a);
};
}
_4.post(_29);
};
_b.openBigWindow=function(url,_2c){
if(url!==undefined&&url!==null&&url!==""){
var _2d=screen.availWidth;
var _2e=screen.availHeight;
if(!_2c){
_2c="_blankwindow";
}
var _2f=window.open(url,_2c,"resizable=yes,toolbar=yes, menubar=yes,   scrollbars=yes,location=yes, status=yes,top=0,left=0,width="+_2d+",height="+_2e);
_2f.resizeTo(_2d,_2e);
_2f.moveTo(0,0);
_2f.focus();
return false;
}
};
_b.openNewWindow=function(_30,url){
var _31=screen.availWidth-10;
var _32=screen.availHeight-15;
var _33="";
if(!_30){
_30="_blankwindow";
}
_b.closeChildWin();
_b.variable.childWindow=window.open(url,_30,_33);
return _b.variable.childWindow;
};
_b.closeChildWin=function(){
if(_b.variable.childWindow!=null&&_b.variable.childWindow.open){
_b.variable.childWindow.close();
_b.variable.childWindow=null;
}
};
_b.expandTreeNode=function(_34){
if(!_34.isExpanded){
_34.rootNode.expand();
}
var _35=_34.rootNode.getChildren();
_b._expandChildNode(_35,_34);
};
_b._expandChildNode=function(_36,_37){
for(var i=0;i<_36.length;i++){
var _38=_36[i];
if(_38.isExpandable&&!_38.isExpanded){
_37._expandNode(_38);
}
var _39=_38.getChildren();
if(_39.length>0){
_b._expandChildNode(_39,_37);
}
}
};
_b.replaceRostenTheme=function(_3a){
if(!_8.contains(_1.body(),"rosten")){
_8.add(_1.body(),"rosten");
}
_b.addRostenCss(_3a);
};
_b.addRostenCss=function(_3b){
if(_3b){
_b.setRostenThemeCss(_3b);
}
var _3c=_6.byId("rostenThemeCss");
if(_3c){
_5.destroy(_3c);
}
var _3d=_b.webPath+"/css/rosten/themes/"+_b.rostenthemecss+"/css/"+_b.rostenthemecss+".css";
_b.addCSSFile(_3d,"rostenThemeCss");
};
_b.addCSSFile=function(_3e,_3f){
var _40=document.getElementsByTagName("head").item(0);
var _41=document.createElement("link");
if(_3f){
_41.setAttribute("id",_3f);
}
_41.setAttribute("rel","stylesheet");
_41.setAttribute("type","text/css");
if(_7("ie")){
_41.href=_3e;
}else{
_41.setAttribute("href",_3e);
}
_40.appendChild(_41);
};
_b.replaceDojoTheme=function(_42,_43){
var _44=_b.dojothemecss;
if(_8.contains(_1.body(),_44)){
_8.remove(_1.body(),_44);
}
if(!_8.contains(_1.body(),_42)){
_8.add(_1.body(),_42);
}
_b.addDojoTheme(_42);
if(_43&&_43==true){
_b.addDojoGridCss();
}
};
_b.addDojoTheme=function(_45){
if(_45){
_b.setDojoThemeCss(_45);
}
var _46=_6.byId("themeCss");
if(_46){
_5.destroy(_46);
}
var _47=_2.moduleUrl("dijit.themes",_b.dojothemecss+"/"+_b.dojothemecss+".css");
_b.addCSSFile(_47,"themeCss");
};
_b.addDojoGridCss=function(){
var _48=_6.byId("gridCss");
if(_48){
_5.destroy(_48);
}
var _49=_2.moduleUrl("dojox","grid/resources/Grid.css");
_b.addCSSFile(_49,"gridCss");
var _4a=_6.byId("themeGridCss");
if(_4a){
_5.destroy(_4a);
}
var _4b=_2.moduleUrl("dojox","grid/resources/"+_b.dojothemecss+"Grid.css");
_b.addCSSFile(_4b,"themeGridCss");
};
_b.init=function(_4c){
if(_4c.webpath){
_b.webPath=_4c.webpath;
}
var _4d;
if(_4c.dojocss){
_4d=_4c.dojocss;
}else{
_4d=_b.dojothemecss;
}
if(_4c.dojogridcss){
_b.replaceDojoTheme(_4d,true);
}else{
_b.replaceDojoTheme(_4d,false);
}
if(_4c.rostencss){
_b.replaceRostenTheme(_4c.rostencss);
}
};
_b.windowclose=function(){
window.close();
};
_b.errordeal=function(_4e,_4f){
_4e.innerHTML="";
var div=document.createElement("div");
_9.set(div,{"fontSize":"18px","color":"#FF0000","border":"1px solid #c8c8c8","textAlign":"center","height":"35px","lineHeight":"35px","margin":"5px"});
_8.add(div,"verticalAlign");
_4e.appendChild(div);
var _50=document.createElement("img");
var _51=_2.moduleUrl("rosten","widget/templates/alert_1.gif");
if(_2.isIE){
_50.src=_51;
}else{
_50.setAttribute("src",_51);
}
div.appendChild(_50);
div.appendChild(document.createTextNode(" "+_4f));
};
_b.validatyById=function(_52){
var _53=_a.byId(_52);
if(_53){
_53.destroy();
}
};
_b.validatyByArr=function(arr){
if((arr&&arr instanceof Array||typeof arr=="array")){
for(var i=0;i<arr.length;i++){
_b.validatyById(arr[i]);
}
}
};
_b.addNaviMenu=function(_54,_55){
_54.innerHTML="";
if(_55.size()>0){
for(var i=0;i<_55.size();i++){
var div=document.createElement("div");
var li=document.createElement("li");
var a=document.createElement("a");
div.innerHTML=">";
a.innerHTML="";
li.appendChild(div);
li.appendChild(a);
_54.appendChild(li);
}
}
};
return _b;
});

