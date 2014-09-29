define(["dojo/_base/kernel","dojo/_base/declare","dojo/dom","dijit/registry","dojo/_base/lang","dojo/_base/xhr","dojo/dom-style","dojo/dom-class","dojo/_base/connect","dojox/collections/SortedList","dijit/DialogUnderlay","rosten/kernel/_kernel","rosten/widget/SimpleNavigation","rosten/widget/ActionBar","rosten/widget/RostenGrid","rosten/widget/RostenTree","rosten/util/general","rosten/widget/ShowDialog","rosten/widget/RostenContentPane"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e,_f,_10,_11,_12,_13){
return _2("rosten.kernel.kernel",null,{constructor:function(_14){
this.general=new _11();
this.navigationContainer=_4.byId("navigationContainer");
this.navigation=_4.byId("navigation");
this.contentBody=_4.byId("contentBody");
this.rostenNavigation=null;
this.rostenActionBar=null;
this.rostenGrid=null;
this.rostenShowDialog=null;
this.contentBodyDefault="";
this.rostenSearch=null;
this.navigationMenuSrc="",this.navigationMenu="";
this.navigationMenuData=new _a();
this.navigationSrc="",this.navigationEntity="";
this.navigationType="stand";
this.userInfo=new _a();
this.connectArray=[];
this.connectNaviArray=[];
this.underlay=new _b();
if(_14!=undefined&&_14!=""){
if(_14.type){
this.navigationType=_14.type;
}
if(_14.naviMenuSrc){
this.createNaviMenu(_14.naviMenuSrc);
}
}
_9.subscribe("closeUnderlay",this,"closeUnderlay");
_5.extend(_d,{onDownloadError:function(_15){
if(_15.status=="401"){
_c.logout();
}
}});
_5.extend(_e,{onDownloadError:function(_16){
if(_16.status=="401"){
_c.logout();
}
}});
},onDownloadError:function(){
},closeUnderlay:function(obj){
if(this.underlay){
this.underlay.node.style.display="none";
}
},getUserInforByKey:function(key){
if(this.userInfo.contains(key)){
var _17=this.userInfo.item(key);
return _17;
}else{
return "";
}
},getMenuValue:function(key){
if(this.navigationMenuData.contains(key)){
var _18=this.navigationMenuData.item(key);
return this.general.stringRight(this.general.stringLeft(_18,"&"),":");
}else{
return "";
}
},getMenuValueCode:function(key){
if(this.navigationMenuData.contains(key)){
var _19=this.navigationMenuData.item(key);
return this.general.stringLeft(this.general.stringLeft(_19,"&"),":");
}else{
return "";
}
},getMenuKeyByCode:function(_1a){
var obj=this.navigationMenuData;
for(var i=0;i<obj.count;i++){
var _1b=obj.getKey(i);
var _1c=obj.item(_1b);
if(_1b=="default"){
break;
}
if(_1a==this.general.stringLeft(_1c,":")){
return _1b;
}
}
return null;
},getMenuName:function(){
return this.navigationMenu;
},getMenuPath:function(key){
if(this.navigationMenuData.contains(key)){
var _1d=this.navigationMenuData.item(key);
if(_1d.indexOf("@")!=-1){
return this.general.stringLeft(this.general.stringRight(_1d,"&"),"@");
}else{
return this.general.stringRight(_1d,"&");
}
}else{
return "";
}
},getMenuType:function(key){
if(this.navigationMenuData.contains(key)){
var _1e=this.navigationMenuData.item(key);
if(_1e.indexOf("@")!=-1){
return this.general.stringRightBack(_1e,"@");
}else{
return this.navigationType;
}
}else{
return this.navigationType;
}
},addUserInfo:function(_1f){
this.userInfo.clear();
for(var _20 in _1f){
this.userInfo.add(_20,_1f[_20]);
}
},addRightContent:function(_21){
if(!_21.identifier){
return;
}
if(this.navigationEntity==_21.identifier){
return;
}
this.destroy();
if(!_21.actionBarSrc){
return;
}
this.createActionBar({actionBarSrc:_21.actionBarSrc});
this.createGridNavi(_21.identifier);
if(_21.searchSrc){
this.createSearch(_21.searchSrc);
}
if(!_21.gridSrc){
return;
}
this.createGrid({url:_21.gridSrc,showRowSelector:"new"});
this.navigationEntity=_21.identifier;
},_addMenu:function(obj){
var ul=_3.byId("naviMenuUL");
ul.innerHTML="";
if(obj.count>0){
for(var i=0;i<obj.count;i++){
var _22=obj.getKey(i);
var _23=obj.item(_22);
if(_23=="default"){
break;
}
var _24=this.getMenuValue(_23);
var li=document.createElement("li");
var a=document.createElement("a");
var _25=document.createElement("span");
_25.innerHTML=_24;
a.appendChild(_25);
a.setAttribute("href","javascript:rosten.kernel._naviMenuShow('"+_23+"')");
li.appendChild(a);
ul.appendChild(li);
}
}
},_naviMenuCallback:function(_26){
this.navigationMenuData.clear();
var _27=new _a();
for(var _28 in _26){
var _29=this.general.stringLeft(_28,"&");
var _2a=this.general.stringRight(_28,"&");
this.navigationMenuData.add(_29,_26[_28]);
_27.add(_2a,_29);
}
this._addMenu(_27);
if(this.navigationMenuData.containsKey("default")){
var _2b=this.navigationMenuData.entry("default");
this._naviMenuShow(_2b);
}
},_naviMenuShow:function(_2c){
var _2d=this.getMenuPath(_2c);
if(_2d.indexOf("js:")!=-1){
var _2e=[];
_2e.push(this.general.stringRight(_2d,":"));
_9.publish("loadspecmenu",_2e);
return;
}
if(this.navigationMenu!=_2c){
var _2e=[];
var _2f=this.getMenuType(_2c);
if(_2f=="stand"){
this.underlay.node.style.display="";
this.underlay.show();
this.createSimpleNavi({url:this.getMenuPath(_2c),urlArgs:{id:_2c}},_2c);
var _30=this.rostenNavigation.defaultEntity;
if(arguments[1]){
_30=arguments[1];
}
_2e.push({naviMenu:this.getMenuValueCode(this.navigationMenu),naviRight:_30});
}else{
if(_2f=="tree"){
this.underlay.node.style.display="";
this.underlay.show();
this.createNavigation({url:this.getMenuPath(_2c),treeLabel:this.getMenuValue(_2c),defaultentry:"yes"},_2c);
_2e.push({naviMenu:this.getMenuValueCode(this.navigationMenu)});
}
}
_9.publish("loadjsfile",_2e);
}else{
console.log("当前不需要变换导航条.......");
}
},createSimpleNavi:function(src,_31){
this.createNavigationTitle(this.getMenuValue(_31));
this.navigationMenu=_31;
this.destroyNaviConnect();
if(this.rostenNavigation&&this.rostenNavigation!=null){
this.rostenNavigation.destroy();
}
src.id="rosten_navigation";
this.rostenNavigation=new _d(src);
this.navigation.attr("content",this.rostenNavigation.domNode);
},createNavigationTitle:function(_32){
this.navigation.attr("title",_32);
},createNaviMenu:function(src){
this.navigationMenuSrc=src;
var _33={url:src,timeout:3000,handleAs:"json",preventCache:true,sync:true,load:_5.hitch(this,function(_34){
if(_34=="error"){
return;
}
this._naviMenuCallback(_34);
}),error:function(_35,_36){
console.log(_35);
}};
_6.get(_33);
},createGridNavi:function(_37){
var _38=document.createElement("div");
_8.add(_38,"verticalAlign");
_7.set(_38,{"marginTop":"5px","marginBottom":"5px","height":"25px"});
var _39=document.createElement("table");
_38.appendChild(_39);
var _3a=document.createElement("tbody");
_39.appendChild(_3a);
var tr=document.createElement("tr");
_3a.appendChild(tr);
var td=document.createElement("td");
tr.appendChild(td);
var _3b=document.createElement("img");
_3b.src="images/rosten/share/icon_line.gif";
td.appendChild(_3b);
var _3c=document.createElement("img");
_3c.src="images/rosten/share/icon_navigator.gif";
td.appendChild(_3c);
var td2=document.createElement("td");
tr.appendChild(td2);
var div=document.createElement("div");
div.innerHTML=this.getMenuValue(this.navigationMenu)+" >> "+this.rostenNavigation.getShowName(_37);
_7.set(div,{"marginLeft":"5px"});
td2.appendChild(div);
this.contentBody.domNode.appendChild(_38);
},createNavigation:function(src,_3d){
this.destroyNaviConnect();
this.createNavigationTitle(this.getMenuValue(_3d));
this.navigationMenu=_3d;
if(this.rostenNavigation&&this.rostenNavigation!=null){
this.rostenNavigation.destroy();
}
src.id="rosten_navigation";
this.rostenNavigation=new _10(src);
this.navigation.attr("content",this.rostenNavigation.domNode);
},createActionBar:function(src){
src.id="rosten_actionBar";
this.rostenActionBar=new _e(src);
this.contentBody.domNode.appendChild(this.rostenActionBar.domNode);
},createGrid:function(src){
src.id="rosten_grid";
this.rostenGrid=new _f(src);
this.contentBody.domNode.appendChild(this.rostenGrid.domNode);
},createSearch:function(src){
if(this.rostenSearch){
this.rostenSearch.destroyRecursive();
}
this.rostenSearch=new _13({src:src});
this.contentBody.addChild(this.rostenSearch);
},toggleSearch:function(_3e){
if(!this.rostenSearch){
return;
}
if(_3e){
_7.set(this.rostenSearch.domNode,{"display":""});
}else{
_7.set(this.rostenSearch.domNode,{"display":"none"});
}
},getnavigationTitle:function(){
return this.navigationTitle;
},getNavigation:function(){
return this.rostenNavigation;
},getContentBody:function(){
return this.contentBody;
},getActionBar:function(){
return this.rostenActionBar;
},getGrid:function(){
return this.rostenGrid;
},getGridItemValue:function(_3f,_40){
var _41=this.rostenGrid.getGrid();
var _42=_41.getItem(_3f);
var _43=this.rostenGrid.getStore();
return _43.getValue(_42,_40);
},refreshActionBar:function(src){
this.rostenActionBar.refresh(src);
},refreshGrid:function(src,_44){
if(this.rostenGrid&&this.rostenGrid!=null){
this.rostenGrid.refresh(src,_44);
}
},refresh:function(obj){
if(obj&&obj!=""){
this.refreshActionBar(obj.actionUrl);
this.refreshGrid(obj.gridUrl);
}else{
this.refreshActionBar();
this.refreshGrid();
}
},destroyActionBar:function(){
if(this.rostenActionBar){
this.rostenActionBar.destroyConnect();
if(this.rostenActionBar.destroyRecursive){
this.rostenActionBar.destroyRecursive();
}else{
this.rostenActionBar.destroy();
}
this.rostenActionBar.destroy();
}
},destoryNavigation:function(){
if(this.rostenNavigation){
this.destroyNaviConnect();
if(this.rostenNavigation.destroyRecursive){
this.rostenNavigation.destroyRecursive();
}else{
this.rostenNavigation.destroy();
}
this.navigationEntity="";
}
},destroyGrid:function(){
if(this.rostenGrid){
this.rostenGrid.destroyConnect();
if(this.rostenGrid.destroyRecursive){
this.rostenGrid.destroyRecursive();
}else{
this.rostenGrid.destroy();
}
}
},destroy:function(){
this.destroyConnect();
this.destroyActionBar();
this.destroyGrid();
this.contentBody.destroyDescendants();
this.navigationEntity="";
},returnToMain:function(_45){
this.destroy();
if(_45){
this.contentBodyDefault=_45;
}else{
var _46=this.getUserInforByKey("logoname");
if(_46==""){
_46=_c.variable.logoname;
}
this.contentBodyDefault="&nbsp;&nbsp;欢迎进入"+_46+"，当前您已成功登录！......";
}
var div=document.createElement("div");
_7.set(div,{"marginLeft":"10px","marginTop":"5px"});
div.innerHTML=this.contentBodyDefault;
this.contentBody.attr("content",div);
this.navigationEntity="";
},createRostenShowDialog:function(src,_47){
var obj={src:src};
if(_47){
if(_47.callback){
obj.callback=_47.callback;
}
if(_47.callbackargs){
obj.callbackargs=_47.callbackargs;
}
if(_47.onLoadFunction){
obj.onLoadFunction=_47.onLoadFunction;
}
}
if(this.rostenShowDialog){
this.rostenShowDialog.destroy();
}
this.rostenShowDialog=new _12(obj);
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
},addConnect:function(_48){
if(_48!=undefined){
this.connectArray.push(_48);
}
},destroyConnect:function(){
_1.forEach(this.connectArray,_9.disconnect);
},addNaviConnect:function(_49){
if(_49){
this.connectNaviArray.push(_9.connect(this.navigation,"onLoad",_49));
}
},destroyNaviConnect:function(){
_1.forEach(this.connectNaviArray,_9.disconnect);
},setNaviContent:function(_4a,_4b,_4c){
this.destroyNaviConnect();
this.createNavigationTitle(_4c);
this.navigationMenu=_4b;
if(this.rostenNavigation&&this.rostenNavigation!=null){
this.rostenNavigation.destroy();
}
this.navigation.attr("content",_4a);
},setNaviHref:function(_4d,_4e,_4f,_50){
this.destroyNaviConnect();
this.createNavigationTitle(_4f);
this.navigationMenu=_4e;
if(this.rostenNavigation&&this.rostenNavigation!=null){
this.rostenNavigation.destroy();
}
this.navigation.attr("href",_4d);
if(_50){
this.connectNaviArray.push(_9.connect(this.navigation,"onLoad",_50));
}
},setRightContent:function(_51,_52){
this.destroy();
this.contentBody.attr("content",_51);
this.contentBody.onLoad=function(){
_9.publish("closeUnderlay",[_52]);
};
if(_52){
if(_52!=""&&_52!=this.navigationEntity){
this.navigationEntity=_52;
}
}
},setHref:function(_53,_54,_55){
this.destroy();
this.contentBody.attr("href",_53);
this.contentBody.onLoad=function(){
_9.publish("closeUnderlay",[_54]);
};
if(_54){
if(_54!=""&&_54!=this.navigationEntity){
this.navigationEntity=_54;
}
}
if(_55){
this.connectArray.push(_9.connect(this.contentBody,"onLoad",_55));
}
}});
});

