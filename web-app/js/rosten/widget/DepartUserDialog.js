define(["dojo/_base/declare","dojo/_base/lang","dojo/_base/kernel","dojo/data/ItemFileWriteStore","dojo/_base/xhr","dojo/dom-style","dojo/dom-class","dojo/_base/connect","dojo/_base/window","dojo/dom-construct","dijit/form/ComboBox","dijit/form/TextBox","dijit/form/Button","dijit/form/CheckBox","dijit/form/MultiSelect","rosten/widget/_Dialog","rosten/widget/CheckBoxTree","rosten/util/general"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e,_f,_10,_11,_12){
return _1("rosten.widget.DepartUserDialog",[_10],{general:new _12(),title:"人员选择",url:null,rootLabel:"Rosten",showCheckBox:true,showRoot:false,query:{parentId:null},folderClass:null,type:"multile",isSelected:true,height:"565px",width:"600px",returnData:[],treeLable:null,connectArray:[],buildContent:function(_13){
_7.add(_13,"departUser");
if(this.type!="multile"){
this.showCheckBox=false;
this.height="530px";
_6.set(this._dialog.domNode,"height",this.height);
}
this.selectUtil=new this.general.selectObject();
this.treeStore=new _4({url:this.url});
this._buildSearchTable(_13);
this._buildTreeTable(_13);
},_buildSearchTable:function(_14){
var _15=document.createElement("table");
_7.add(_15,"fieldlist");
var tr=document.createElement("tr");
_15.appendChild(tr);
var td1=document.createElement("td");
var _16=document.createElement("span");
_16.innerHTML="部门名称";
td1.appendChild(_16);
this.searchDepart=new _b({style:{width:"150px",marginLeft:"5px"},store:this.treeStore,query:this.query,searchAttr:"name"},document.createElement("div"));
td1.appendChild(this.searchDepart.domNode);
tr.appendChild(td1);
var td2=document.createElement("td");
var _17=document.createElement("span");
_17.innerHTML="人员名称";
td2.appendChild(_17);
this.searchUser=new _c({style:{width:"150px",marginLeft:"5px"}});
td2.appendChild(this.searchUser.domNode);
tr.appendChild(td2);
var td3=document.createElement("td");
this.searchButton=new _d({label:"查询"});
td3.appendChild(this.searchButton.domNode);
this.connectArray.push(_8.connect(this.searchButton,"onClick",this,"searchFresh"));
tr.appendChild(td3);
_14.appendChild(_15);
},searchFresh:function(){
var _18=this.searchDepart.attr("value");
var _19=this.searchUser.attr("value");
var url;
if(_18!=""||_19!=""){
url=this.url+"&depart="+_18+"&user="+_19;
}else{
url=this.url;
}
this.treeStore=new _4({url:url});
this.treenode.innerHTML="";
this._buildTree(this.treenode);
if(_18!=""||_19!=""){
this.tree.expandAll();
}
this.searchDepart.attr("value","");
this.searchUser.attr("value","");
},_buildTreeTable:function(_1a){
var _1b=document.createElement("table");
_7.add(_1b,"fieldlist");
var _1c=document.createElement("tbody");
var tr=document.createElement("tr");
var td1=document.createElement("td");
td1.setAttribute("width","45%");
var _1d=document.createElement("div");
_7.add(_1d,"tab_box");
_6.set(_1d,"height","400px");
td1.appendChild(_1d);
if(this.treeLable!=null){
var h3=document.createElement("h3");
h3.innerHTML=this.treeLable;
_1d.appendChild(h3);
}
this.treenode=document.createElement("div");
_7.add(this.treenode,"titlelist");
if(this.folderClass!=null){
_7.add(this.treenode,this.folderClass);
}
this._buildTree(this.treenode);
_1d.appendChild(this.treenode);
tr.appendChild(td1);
var td2=document.createElement("td");
td2.setAttribute("width","10%");
var _1e=document.createElement("div");
_7.add(_1e,"btn_choose");
if(this.type=="multile"){
var _1f=document.createElement("button");
_7.add(_1f,"allRight");
_1e.appendChild(_1f);
this.connectArray.push(_8.connect(_1f,"onclick",this,function(){
this._getStoreItem(this.treeStore,{},function(_20){
for(var i=0;i<_20.length;i++){
var _21=_20[i];
if(_21.type=="user"&&!this.selectUtil.selectIsExitItem(this.searchResult.domNode,_21.id)){
this._addOption(this.searchResult.domNode,_21);
}
this.tree.model.updateCheckbox(_21,false);
}
});
}));
_1e.appendChild(document.createElement("br"));
}
var _22=document.createElement("button");
_7.add(_22,"right");
_1e.appendChild(_22);
this.connectArray.push(_8.connect(_22,"onclick",this,function(){
if(this.type=="multile"){
this._getStoreItem(this.treeStore,{checkbox:true},function(_23){
for(var i=0;i<_23.length;i++){
var _24=_23[i];
if(_24.type=="user"&&!this.selectUtil.selectIsExitItem(this.searchResult.domNode,_24.id)){
this._addOption(this.searchResult.domNode,_24);
}
this.tree.model.updateCheckbox(_24,false);
}
});
}else{
if(this.returnData.length>0){
_a.empty(this.searchResult.domNode);
this._addOption(this.searchResult.domNode,this.returnData[0]);
}
}
}));
_1e.appendChild(document.createElement("br"));
var _25=document.createElement("button");
_7.add(_25,"left");
_1e.appendChild(_25);
this.connectArray.push(_8.connect(_25,"onclick",this,function(){
var _26=this.searchResult.domNode;
for(var i=_26.length-1;i>=0;i--){
if(_26.options[i].value==this.searchResult.get("value")){
_26.remove(i);
}
}
}));
_1e.appendChild(document.createElement("br"));
if(this.type=="multile"){
var _27=document.createElement("button");
_7.add(_27,"allLeft");
_1e.appendChild(_27);
this.connectArray.push(_8.connect(_27,"onclick",this,function(){
_a.empty(this.searchResult.domNode);
}));
_1e.appendChild(document.createElement("br"));
}
td2.appendChild(_1e);
tr.appendChild(td2);
var td3=document.createElement("td");
td3.setAttribute("width","45%");
var _28=document.createElement("div");
_7.add(_28,"tab_box");
_6.set(_28,"height","400px");
this.searchResult=new _f({style:{height:"400px",width:"100%",border:"none"}},document.createElement("div"));
_28.appendChild(this.searchResult.domNode);
td3.appendChild(_28);
tr.appendChild(td3);
_1c.appendChild(tr);
_1b.appendChild(_1c);
if(this.type=="multile"){
var _29=document.createElement("tfoot");
var _2a=document.createElement("tr");
var _2b=document.createElement("td");
_2b.setAttribute("colspan","3");
this.allselectnode=new _e({},document.createElement("div"));
_2b.appendChild(this.allselectnode.domNode);
var _2c=document.createElement("span");
_2c.innerHTML="全选";
_2b.appendChild(_2c);
this.noselectnode=new _e({},document.createElement("div"));
_2b.appendChild(this.noselectnode.domNode);
var _2d=document.createElement("span");
_2d.innerHTML="反选";
_2b.appendChild(_2d);
this.connectArray.push(_8.connect(this.allselectnode,"onClick",this,function(){
this.allselectnode.set("checked",true);
this.noselectnode.set("checked",false);
this._getStoreItem(this.treeStore,this.query,function(_2e){
for(var i=0;i<_2e.length;i++){
var _2f=_2e[i];
this.tree.model.updateCheckbox(_2f,true);
}
});
}));
this.connectArray.push(_8.connect(this.noselectnode,"onClick",this,function(){
this.noselectnode.set("checked",true);
this.allselectnode.set("checked",false);
this._getStoreItem(this.treeStore,this.query,function(_30){
for(var i=0;i<_30.length;i++){
var _31=_30[i];
this.tree.model.updateCheckbox(_31,false);
}
});
}));
_2a.appendChild(_2b);
_29.appendChild(_2a);
_1b.appendChild(_29);
}
_1a.appendChild(_1b);
},_buildTree:function(_32){
var _33=document.createElement("div");
_32.appendChild(_33);
var _34=new _11.model({store:this.treeStore,query:this.query,childrenAttrs:["children"],rootLabel:this.rootLabel,checkboxAll:this.showCheckBox,checkboxRoot:false,checkboxState:false});
var _35=true;
if(!this.showCheckBox){
}
var _36={model:_34,openOnClick:_35,autoExpand:true,persist:false,showRoot:this.showRoot};
this.tree=new _11.tree(_36,_33);
if(!this.showCheckBox){
this.connectArray.push(_8.connect(this.tree,"onClick",this,"onclick"));
}
if(this.type!="multile"&&this.isSelected){
this.connectArray.push(_8.connect(this.tree,"onLoad",this,function(){
this._getStoreItem(this.treeStore,{type:"user"},function(_37){
if(_37.length==1){
var _38=_37[0];
this.onclick(_38);
}
this.afterLoad();
});
}));
}
this.tree.startup();
},refresh:function(){
this.contentPane.innerHTML="";
this.buildContent(this.contentPane);
},onclick:function(_39,_3a){
if(_39.type=="user"){
_a.empty(this.searchResult.domNode);
this._addOption(this.searchResult.domNode,_39);
this.returnData.length=0;
this.returnData.push(_39);
}else{
this.returnData.length=0;
}
},afterLoad:function(){
},destroyConnect:function(){
_3.forEach(this.connectArray,_8.disconnect);
},_addOption:function(_3b,_3c){
var c=_9.doc.createElement("option");
c.innerHTML=_3c.name;
c.value=_3c.id;
c.departId=_3c.departId;
c.departName=_3c.departName;
_3b.appendChild(c);
},getData:function(){
var _3d=this.selectUtil.getAllValue(this.searchResult.domNode);
return _3d;
},_getStoreItem:function(_3e,_3f,_40){
_3e.fetch({query:_3f,queryOptions:{deep:true},onComplete:_2.hitch(this,_40)});
}});
});

