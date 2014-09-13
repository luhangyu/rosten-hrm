define(["dojo/_base/window","dojo/_base/kernel","dojo/_base/lang","dojo/_base/connect","dojo/_base/xhr","dojo/dom-construct","dojo/dom","dojo/has","dojo/dom-class","dojo/dom-style","dijit/registry"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b){
var _c={version:"V1.0",variable:{},dojoPath:"js",dojothemecss:"claro",rostenthemecss:"normal",webPath:"/web",connectArray:[]};
_c.addConnect=function(_d){
if(_d!=undefined){
_c.connectArray.push(_d);
}
};
_c.destroyConnect=function(){
_2.forEach(_c.connectArray,_4.disconnect);
};
_c.setDojoVersion=function(_e){
_c.dojoVersion=_e;
};
_c.getDojoVersion=function(){
return _c.dojoVersion;
};
_c.setDojoPath=function(_f){
_c.dojoPath=_f;
};
_c.getDojoPath=function(){
return _c.dojoPath;
};
_c.setDojoThemeCss=function(_10){
_c.dojothemecss=_10;
};
_c.setRostenThemeCss=function(_11){
_c.rostenthemecss=_11;
};
_c.readNoTime=_c._readNoTime=function(url,_12,_13,_14,_15){
var _16={url:url,handleAs:"json",preventCache:true,content:_12,encoding:"utf-8"};
if(_15){
_16.form=_15;
}
if(_3.isFunction(_13)){
_16.load=_13;
}
if(_3.isFunction(_14)){
_16.error=_14;
}else{
_16.error=function(_17,_18){
console.log(_17);
};
}
_5.post(_16);
};
_c.read=_c._read=function(url,_19,_1a,_1b,_1c){
var _1d={url:url,timeout:3000,handleAs:"json",preventCache:true,content:_19,encoding:"utf-8"};
if(_1c){
_1d.form=_1c;
}
if(_3.isFunction(_1a)){
_1d.load=_1a;
}
if(_3.isFunction(_1b)){
_1d.error=_1b;
}else{
_1d.error=function(_1e,_1f){
console.log(_1e);
};
}
_5.post(_1d);
};
_c.readSync=_c._readSync=function(url,_20,_21,_22,_23){
var _24={url:url,timeout:3000,sync:true,handleAs:"json",preventCache:true,encoding:"utf-8",content:_20};
if(_23){
_24.form=_23;
}
if(_3.isFunction(_21)){
_24.load=_21;
}
if(_3.isFunction(_22)){
_24.error=_22;
}else{
_24.error=function(_25,_26){
console.log(_25);
};
}
_5.post(_24);
};
_c.readSyncNoTime=_c._readeSyncNoTime=function(url,_27,_28,_29,_2a){
var _2b={url:url,sync:true,handleAs:"json",preventCache:true,content:_27,encoding:"utf-8"};
if(_2a){
_2b.form=_2a;
}
if(_3.isFunction(_28)){
_2b.load=_28;
}
if(_3.isFunction(_29)){
_2b.error=_29;
}else{
_2b.error=function(_2c,_2d){
console.log(_2c);
};
}
_5.post(_2b);
};
_c.openBigWindow=function(url,_2e){
if(url!==undefined&&url!==null&&url!==""){
var _2f=screen.availWidth;
var _30=screen.availHeight;
if(!_2e){
_2e="_blankwindow";
}
var _31=window.open(url,_2e,"resizable=yes,toolbar=yes, menubar=yes,   scrollbars=yes,location=yes, status=yes,top=0,left=0,width="+_2f+",height="+_30);
_31.resizeTo(_2f,_30);
_31.moveTo(0,0);
_31.focus();
return false;
}
};
_c.openNewWindow=function(_32,url){
var _33=screen.availWidth-10;
var _34=screen.availHeight-15;
var _35="";
if(!_32){
_32="_blankwindow";
}
_c.closeChildWin();
_c.variable.childWindow=window.open(url,_32,_35);
return _c.variable.childWindow;
};
_c.closeChildWin=function(){
if(_c.variable.childWindow!=null&&_c.variable.childWindow.open){
_c.variable.childWindow.close();
_c.variable.childWindow=null;
}
};
_c.expandTreeNode=function(_36){
if(!_36.isExpanded){
_36.rootNode.expand();
}
var _37=_36.rootNode.getChildren();
_c._expandChildNode(_37,_36);
};
_c._expandChildNode=function(_38,_39){
for(var i=0;i<_38.length;i++){
var _3a=_38[i];
if(_3a.isExpandable&&!_3a.isExpanded){
_39._expandNode(_3a);
}
var _3b=_3a.getChildren();
if(_3b.length>0){
_c._expandChildNode(_3b,_39);
}
}
};
_c.replaceRostenTheme=function(_3c){
if(!_9.contains(_1.body(),"rosten")){
_9.add(_1.body(),"rosten");
}
_c.addRostenCss(_3c);
};
_c.addRostenCss=function(_3d){
if(_3d){
_c.setRostenThemeCss(_3d);
}
var _3e=_7.byId("rostenThemeCss");
if(_3e){
_6.destroy(_3e);
}
var _3f=_c.webPath+"/css/rosten/themes/"+_c.rostenthemecss+"/css/"+_c.rostenthemecss+".css";
_c.addCSSFile(_3f,"rostenThemeCss");
};
_c.addCSSFile=function(_40,_41){
var _42=document.getElementsByTagName("head").item(0);
var _43=document.createElement("link");
if(_41){
_43.setAttribute("id",_41);
}
_43.setAttribute("rel","stylesheet");
_43.setAttribute("type","text/css");
if(_8("ie")){
_43.href=_40;
}else{
_43.setAttribute("href",_40);
}
_42.appendChild(_43);
};
_c.replaceDojoTheme=function(_44,_45){
var _46=_c.dojothemecss;
if(_9.contains(_1.body(),_46)){
_9.remove(_1.body(),_46);
}
if(!_9.contains(_1.body(),_44)){
_9.add(_1.body(),_44);
}
_c.addDojoTheme(_44);
if(_45&&_45==true){
_c.addDojoGridCss();
}
};
_c.addDojoTheme=function(_47){
if(_47){
_c.setDojoThemeCss(_47);
}
var _48=_7.byId("themeCss");
if(_48){
_6.destroy(_48);
}
var _49=_2.moduleUrl("dijit.themes",_c.dojothemecss+"/"+_c.dojothemecss+".css");
_c.addCSSFile(_49,"themeCss");
};
_c.addDojoGridCss=function(){
var _4a=_7.byId("gridCss");
if(_4a){
_6.destroy(_4a);
}
var _4b=_2.moduleUrl("dojox","grid/resources/Grid.css");
_c.addCSSFile(_4b,"gridCss");
var _4c=_7.byId("themeGridCss");
if(_4c){
_6.destroy(_4c);
}
var _4d=_2.moduleUrl("dojox","grid/resources/"+_c.dojothemecss+"Grid.css");
_c.addCSSFile(_4d,"themeGridCss");
};
_c.init=function(_4e){
if(_4e.webpath){
_c.webPath=_4e.webpath;
}
var _4f;
if(_4e.dojocss){
_4f=_4e.dojocss;
}else{
_4f=_c.dojothemecss;
}
if(_4e.dojogridcss){
_c.replaceDojoTheme(_4f,true);
}else{
_c.replaceDojoTheme(_4f,false);
}
if(_4e.rostencss){
_c.replaceRostenTheme(_4e.rostencss);
}
};
_c.windowclose=function(){
window.close();
};
_c.errordeal=function(_50,_51){
_50.innerHTML="";
var div=document.createElement("div");
_a.set(div,{"fontSize":"18px","color":"#FF0000","border":"1px solid #c8c8c8","textAlign":"center","height":"35px","lineHeight":"35px","margin":"5px"});
_9.add(div,"verticalAlign");
_50.appendChild(div);
var _52=document.createElement("img");
var _53=_2.moduleUrl("rosten","widget/templates/alert_1.gif");
if(_2.isIE){
_52.src=_53;
}else{
_52.setAttribute("src",_53);
}
div.appendChild(_52);
div.appendChild(document.createTextNode(" "+_51));
};
_c.validatyById=function(_54){
var _55=_b.byId(_54);
if(_55){
_55.destroy();
}
};
_c.validatyByArr=function(arr){
if((arr&&arr instanceof Array||typeof arr=="array")){
for(var i=0;i<arr.length;i++){
_c.validatyById(arr[i]);
}
}
};
_c.addNaviMenu=function(_56,_57){
_56.innerHTML="";
if(_57.size()>0){
for(var i=0;i<_57.size();i++){
var div=document.createElement("div");
var li=document.createElement("li");
var a=document.createElement("a");
div.innerHTML=">";
a.innerHTML="";
li.appendChild(div);
li.appendChild(a);
_56.appendChild(li);
}
}
};
_c.logout=function(){
var url=_c.webPath+"/j_spring_security_logout";
window.location=url;
};
return _c;
});

