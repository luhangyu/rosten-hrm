define(["dojo/_base/kernel","dojo/_base/declare","dojo/dom","dijit/registry","dojo/_base/lang","dojo/_base/xhr","dojo/dom-style","dojo/dom-class","dojo/_base/connect","dojox/collections/SortedList","dijit/DialogUnderlay","rosten/kernel/_kernel","rosten/widget/SimpleNavigation","rosten/widget/ActionBar","rosten/widget/RostenGrid","rosten/widget/RostenTree","rosten/util/general","rosten/widget/ShowDialog"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e,_f,_10,_11,_12){
return _2("rosten.kernel.kernel",null,{constructor:function(_13){
this.general=new _11();
this.navigationContainer=_4.byId("navigationContainer");
this.navigation=_4.byId("navigation");
this.contentBody=_4.byId("contentBody");
this.rostenNavigation=null;
this.rostenActionBar=null;
this.rostenGrid=null;
this.rostenShowDialog=null;
this.contentBodyDefault="";
this.navigationMenuSrc="",this.navigationMenu="";
this.navigationMenuData=new _a();
this.navigationSrc="",this.navigationEntity="";
this.navigationType="stand";
this.userInfo=new _a();
this.connectArray=[];
this.connectNaviArray=[];
this.underlay=new _b();
if(_13!=undefined&&_13!=""){
if(_13.type){
this.navigationType=_13.type;
}
if(_13.naviMenuSrc){
this.createNaviMenu(_13.naviMenuSrc);
}
}
_9.subscribe("closeUnderlay",this,"closeUnderlay");
},closeUnderlay:function(obj){
if(this.underlay){
this.underlay.node.style.display="none";
}
},getUserInforByKey:function(key){
if(this.userInfo.contains(key)){
var _14=this.userInfo.item(key);
return _14;
}else{
return "";
}
},getMenuValue:function(key){
if(this.navigationMenuData.contains(key)){
var _15=this.navigationMenuData.item(key);
return this.general.stringRight(this.general.stringLeft(_15,"&"),":");
}else{
return "";
}
},getMenuValueCode:function(key){
if(this.navigationMenuData.contains(key)){
var _16=this.navigationMenuData.item(key);
return this.general.stringLeft(this.general.stringLeft(_16,"&"),":");
}else{
return "";
}
},getMenuKeyByCode:function(_17){
var obj=this.navigationMenuData;
for(var i=0;i<obj.count;i++){
var _18=obj.getKey(i);
var _19=obj.item(_18);
if(_18=="default"){
break;
}
if(_17==this.general.stringLeft(_19,":")){
return _18;
}
}
return null;
},getMenuName:function(){
return this.navigationMenu;
},getMenuPath:function(key){
if(this.navigationMenuData.contains(key)){
var _1a=this.navigationMenuData.item(key);
if(_1a.indexOf("@")!=-1){
return this.general.stringLeft(this.general.stringRight(_1a,"&"),"@");
}else{
return this.general.stringRight(_1a,"&");
}
}else{
return "";
}
},getMenuType:function(key){
if(this.navigationMenuData.contains(key)){
var _1b=this.navigationMenuData.item(key);
if(_1b.indexOf("@")!=-1){
return this.general.stringRightBack(_1b,"@");
}else{
return this.navigationType;
}
}else{
return this.navigationType;
}
},addUserInfo:function(_1c){
this.userInfo.clear();
for(var _1d in _1c){
this.userInfo.add(_1d,_1c[_1d]);
}
},addRightContent:function(_1e){
if(!_1e.identifier){
return;
}
if(this.navigationEntity==_1e.identifier){
return;
}
this.destory();
if(!_1e.actionBarSrc){
return;
}
this.createActionBar({actionBarSrc:_1e.actionBarSrc});
this.createGridNavi(_1e.identifier);
if(!_1e.gridSrc){
return;
}
this.createGrid({url:_1e.gridSrc,showRowSelector:"new"});
this.navigationEntity=_1e.identifier;
},_addMenu:function(obj){
var ul=_3.byId("naviMenuUL");
ul.innerHTML="";
if(obj.count>0){
for(var i=0;i<obj.count;i++){
var _1f=obj.getKey(i);
var _20=obj.item(_1f);
if(_20=="default"){
break;
}
var _21=this.getMenuValue(_20);
var li=document.createElement("li");
var a=document.createElement("a");
var _22=document.createElement("span");
_22.innerHTML=_21;
a.appendChild(_22);
a.setAttribute("href","javascript:rosten.kernel._naviMenuShow('"+_20+"')");
li.appendChild(a);
ul.appendChild(li);
}
}
},_naviMenuCallback:function(_23){
this.navigationMenuData.clear();
var _24=new _a();
for(var _25 in _23){
var _26=this.general.stringLeft(_25,"&");
var _27=this.general.stringRight(_25,"&");
this.navigationMenuData.add(_26,_23[_25]);
_24.add(_27,_26);
}
this._addMenu(_24);
if(this.navigationMenuData.containsKey("default")){
var _28=this.navigationMenuData.entry("default");
this._naviMenuShow(_28);
}
},_naviMenuShow:function(_29){
var _2a=this.getMenuPath(_29);
if(_2a.indexOf("js:")!=-1){
var _2b=[];
_2b.push(this.general.stringRight(_2a,":"));
_9.publish("loadspecmenu",_2b);
return;
}
if(this.navigationMenu!=_29){
var _2c=this.getMenuType(_29);
if(_2c=="stand"){
this.createSimpleNavi({url:this.getMenuPath(_29),urlArgs:{id:_29}},_29);
}else{
if(_2c=="tree"){
this.underlay.node.style.display="";
this.underlay.show();
this.createNavigation({url:this.getMenuPath(_29),treeLabel:this.getMenuValue(_29),defaultentry:"yes"},_29);
}
}
var _2b=[];
_2b.push(this.getMenuValueCode(this.navigationMenu));
_9.publish("loadjsfile",_2b);
}else{
console.log("当前不需要变换导航条.......");
}
},createSimpleNavi:function(src,_2d){
this.createNavigationTitle(this.getMenuValue(_2d));
this.navigationMenu=_2d;
this.destoryNaviConnect();
if(this.rostenNavigation&&this.rostenNavigation!=null){
this.rostenNavigation.destroy();
}
src.id="rosten_navigation";
this.rostenNavigation=new _d(src);
this.navigation.attr("content",this.rostenNavigation.domNode);
},createNavigationTitle:function(_2e){
this.navigation.attr("title",_2e);
},createNaviMenu:function(src){
this.navigationMenuSrc=src;
var _2f={url:src,timeout:3000,handleAs:"json",preventCache:true,sync:true,load:_5.hitch(this,function(_30){
if(_30=="error"){
return;
}
this._naviMenuCallback(_30);
}),error:function(_31,_32){
console.log(_31);
}};
_6.get(_2f);
},createGridNavi:function(_33){
var _34=document.createElement("div");
_8.add(_34,"verticalAlign");
_7.set(_34,{"marginTop":"5px","marginBottom":"5px","height":"25px"});
var _35=document.createElement("table");
_34.appendChild(_35);
var _36=document.createElement("tbody");
_35.appendChild(_36);
var tr=document.createElement("tr");
_36.appendChild(tr);
var td=document.createElement("td");
tr.appendChild(td);
var _37=document.createElement("img");
_37.src="images/rosten/share/icon_line.gif";
td.appendChild(_37);
var _38=document.createElement("img");
_38.src="images/rosten/share/icon_navigator.gif";
td.appendChild(_38);
var td2=document.createElement("td");
tr.appendChild(td2);
var div=document.createElement("div");
div.innerHTML=this.getMenuValue(this.navigationMenu)+" >> "+this.rostenNavigation.getShowName(_33);
_7.set(div,{"marginLeft":"5px"});
td2.appendChild(div);
this.contentBody.domNode.appendChild(_34);
},createNavigation:function(src,_39){
this.destoryNaviConnect();
this.createNavigationTitle(this.getMenuValue(_39));
this.navigationMenu=_39;
if(this.rostenNavigation&&this.rostenNavigation!=null){
this.rostenNavigation.destroy();
}
src.id="rosten_navigation";
this.rostenNavigation=new _10(src);
this.navigation.attr("content",this.rostenNavigation.domNode);
},createActionBar:function(src){
if(this.rostenActionBar&&this.rostenActionBar!=null){
this.rostenActionBar.destroyRecursive();
}
src.id="rosten_actionBar";
this.rostenActionBar=new _e(src);
this.contentBody.domNode.appendChild(this.rostenActionBar.domNode);
},createGrid:function(src){
if(this.rostenGrid&&this.rostenGrid!=null){
this.rostenGrid.destroyRecursive();
}
src.id="rosten_grid";
this.rostenGrid=new _f(src);
this.contentBody.domNode.appendChild(this.rostenGrid.domNode);
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
},getGridItemValue:function(_3a,_3b){
var _3c=this.rostenGrid.getGrid();
var _3d=_3c.getItem(_3a);
var _3e=this.rostenGrid.getStore();
return _3e.getValue(_3d,_3b);
},refreshActionBar:function(src){
this.rostenActionBar.refresh(src);
},refreshGrid:function(src,_3f){
if(this.rostenGrid&&this.rostenGrid!=null){
this.rostenGrid.refresh(src,_3f);
}
},refresh:function(obj){
if(obj&&obj!=""){
this.refreshActionBar(obj.actionUrl);
this.refreshGrid(obj.gridUrl);
}else{
this.refreshActionBar();
this.refreshGrid();
}
},destoryActionBar:function(){
if(this.rostenActionBar){
this.rostenActionBar.destory();
}
},destoryNavigation:function(){
if(this.rostenNavigation&&this.rostenNavigation!=null){
this.destoryNaviConnect();
this.rostenNavigation.destory();
this.navigationEntity="";
}
},destoryGrid:function(){
if(this.rostenGrid){
this.rostenGrid.destory();
}
},destory:function(){
this.destoryConnect();
this.contentBody.destroyDescendants();
this.navigationEntity="";
},returnToMain:function(_40){
this.destory();
if(_40){
this.contentBodyDefault=_40;
}else{
var _41=this.getUserInforByKey("logoname");
if(_41==""){
_41=_c.variable.logoname;
}
this.contentBodyDefault="&nbsp;&nbsp;欢迎进入"+_41+"，当前您已成功登录！......";
}
var div=document.createElement("div");
_7.set(div,{"marginLeft":"10px","marginTop":"5px"});
div.innerHTML=this.contentBodyDefault;
this.contentBody.attr("content",div);
this.navigationEntity="";
},createRostenShowDialog:function(src,_42){
var obj={src:src};
if(_42){
if(_42.callback){
obj.callback=_42.callback;
}
if(_42.callbackargs){
obj.callbackargs=_42.callbackargs;
}
if(_42.onLoadFunction){
obj.onLoadFunction=_42.onLoadFunction;
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
},addConnect:function(_43){
if(_43!=undefined){
this.connectArray.push(_43);
}
},destoryConnect:function(){
_1.forEach(this.connectArray,_9.disconnect);
},addNaviConnect:function(_44){
if(_44){
this.connectNaviArray.push(_9.connect(this.navigation,"onLoad",_44));
}
},destoryNaviConnect:function(){
_1.forEach(this.connectNaviArray,_9.disconnect);
},setNaviContent:function(_45,_46,_47){
this.destoryNaviConnect();
this.createNavigationTitle(_47);
this.navigationMenu=_46;
if(this.rostenNavigation&&this.rostenNavigation!=null){
this.rostenNavigation.destroy();
}
this.navigation.attr("content",_45);
},setNaviHref:function(_48,_49,_4a,_4b){
this.destoryNaviConnect();
this.createNavigationTitle(_4a);
this.navigationMenu=_49;
if(this.rostenNavigation&&this.rostenNavigation!=null){
this.rostenNavigation.destroy();
}
this.navigation.attr("href",_48);
if(_4b){
this.connectNaviArray.push(_9.connect(this.navigation,"onLoad",_4b));
}
},setRightContent:function(_4c,_4d){
this.destoryConnect();
this.contentBody.attr("content",_4c);
if(_4d){
if(_4d!=""&&_4d!=this.navigationEntity){
this.navigationEntity=_4d;
}
}
},setHref:function(_4e,_4f,_50){
this.destoryConnect();
this.contentBody.attr("href",_4e);
if(_4f){
if(_4f!=""&&_4f!=this.navigationEntity){
this.navigationEntity=_4f;
}
}
if(_50){
this.connectArray.push(_9.connect(this.contentBody,"onLoad",_50));
}
},onDownloadError:function(){
}});
});

