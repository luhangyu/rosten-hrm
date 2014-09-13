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
_2e.push({naviMenu:this.getMenuValueCode(this.navigationMenu),naviRight:this.rostenNavigation.defaultEntity});
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
},createSimpleNavi:function(src,_30){
this.createNavigationTitle(this.getMenuValue(_30));
this.navigationMenu=_30;
this.destroyNaviConnect();
if(this.rostenNavigation&&this.rostenNavigation!=null){
this.rostenNavigation.destroy();
}
src.id="rosten_navigation";
this.rostenNavigation=new _d(src);
this.navigation.attr("content",this.rostenNavigation.domNode);
},createNavigationTitle:function(_31){
this.navigation.attr("title",_31);
},createNaviMenu:function(src){
this.navigationMenuSrc=src;
var _32={url:src,timeout:3000,handleAs:"json",preventCache:true,sync:true,load:_5.hitch(this,function(_33){
if(_33=="error"){
return;
}
this._naviMenuCallback(_33);
}),error:function(_34,_35){
console.log(_34);
}};
_6.get(_32);
},createGridNavi:function(_36){
var _37=document.createElement("div");
_8.add(_37,"verticalAlign");
_7.set(_37,{"marginTop":"5px","marginBottom":"5px","height":"25px"});
var _38=document.createElement("table");
_37.appendChild(_38);
var _39=document.createElement("tbody");
_38.appendChild(_39);
var tr=document.createElement("tr");
_39.appendChild(tr);
var td=document.createElement("td");
tr.appendChild(td);
var _3a=document.createElement("img");
_3a.src="images/rosten/share/icon_line.gif";
td.appendChild(_3a);
var _3b=document.createElement("img");
_3b.src="images/rosten/share/icon_navigator.gif";
td.appendChild(_3b);
var td2=document.createElement("td");
tr.appendChild(td2);
var div=document.createElement("div");
div.innerHTML=this.getMenuValue(this.navigationMenu)+" >> "+this.rostenNavigation.getShowName(_36);
_7.set(div,{"marginLeft":"5px"});
td2.appendChild(div);
this.contentBody.domNode.appendChild(_37);
},createNavigation:function(src,_3c){
this.destroyNaviConnect();
this.createNavigationTitle(this.getMenuValue(_3c));
this.navigationMenu=_3c;
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
},toggleSearch:function(_3d){
if(!this.rostenSearch){
return;
}
if(_3d){
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
},getGridItemValue:function(_3e,_3f){
var _40=this.rostenGrid.getGrid();
var _41=_40.getItem(_3e);
var _42=this.rostenGrid.getStore();
return _42.getValue(_41,_3f);
},refreshActionBar:function(src){
this.rostenActionBar.refresh(src);
},refreshGrid:function(src,_43){
if(this.rostenGrid&&this.rostenGrid!=null){
this.rostenGrid.refresh(src,_43);
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
},returnToMain:function(_44){
this.destroy();
if(_44){
this.contentBodyDefault=_44;
}else{
var _45=this.getUserInforByKey("logoname");
if(_45==""){
_45=_c.variable.logoname;
}
this.contentBodyDefault="&nbsp;&nbsp;欢迎进入"+_45+"，当前您已成功登录！......";
}
var div=document.createElement("div");
_7.set(div,{"marginLeft":"10px","marginTop":"5px"});
div.innerHTML=this.contentBodyDefault;
this.contentBody.attr("content",div);
this.navigationEntity="";
},createRostenShowDialog:function(src,_46){
var obj={src:src};
if(_46){
if(_46.callback){
obj.callback=_46.callback;
}
if(_46.callbackargs){
obj.callbackargs=_46.callbackargs;
}
if(_46.onLoadFunction){
obj.onLoadFunction=_46.onLoadFunction;
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
},addConnect:function(_47){
if(_47!=undefined){
this.connectArray.push(_47);
}
},destroyConnect:function(){
_1.forEach(this.connectArray,_9.disconnect);
},addNaviConnect:function(_48){
if(_48){
this.connectNaviArray.push(_9.connect(this.navigation,"onLoad",_48));
}
},destroyNaviConnect:function(){
_1.forEach(this.connectNaviArray,_9.disconnect);
},setNaviContent:function(_49,_4a,_4b){
this.destroyNaviConnect();
this.createNavigationTitle(_4b);
this.navigationMenu=_4a;
if(this.rostenNavigation&&this.rostenNavigation!=null){
this.rostenNavigation.destroy();
}
this.navigation.attr("content",_49);
},setNaviHref:function(_4c,_4d,_4e,_4f){
this.destroyNaviConnect();
this.createNavigationTitle(_4e);
this.navigationMenu=_4d;
if(this.rostenNavigation&&this.rostenNavigation!=null){
this.rostenNavigation.destroy();
}
this.navigation.attr("href",_4c);
if(_4f){
this.connectNaviArray.push(_9.connect(this.navigation,"onLoad",_4f));
}
},setRightContent:function(_50,_51){
this.destroy();
this.contentBody.attr("content",_50);
this.contentBody.onLoad=function(){
_9.publish("closeUnderlay",[_51]);
};
if(_51){
if(_51!=""&&_51!=this.navigationEntity){
this.navigationEntity=_51;
}
}
},setHref:function(_52,_53,_54){
this.destroy();
this.contentBody.attr("href",_52);
this.contentBody.onLoad=function(){
_9.publish("closeUnderlay",[_53]);
};
if(_53){
if(_53!=""&&_53!=this.navigationEntity){
this.navigationEntity=_53;
}
}
if(_54){
this.connectArray.push(_9.connect(this.contentBody,"onLoad",_54));
}
}});
});

