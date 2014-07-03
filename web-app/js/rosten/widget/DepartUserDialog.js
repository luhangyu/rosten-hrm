define(["dojo/_base/declare","dojo/_base/lang","dojo/data/ItemFileWriteStore","dojo/_base/xhr","dojo/dom-style","dojo/dom-class","dojo/_base/connect","dojo/_base/window","dojo/dom-construct","dijit/form/ComboBox","dijit/form/TextBox","dijit/form/Button","dijit/form/CheckBox","dijit/form/MultiSelect","rosten/widget/_Dialog","rosten/widget/CheckBoxTree","rosten/util/general"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e,_f,_10,_11){
return _1("rosten.widget.DepartUserDialog",[_f],{general:new _11(),title:"人员选择",url:null,rootLabel:"Rosten",showCheckBox:true,showRoot:false,query:{parentId:null},folderClass:null,type:"multile",isSelected:true,height:"565px",width:"600px",returnData:[],treeLable:null,buildContent:function(_12){
_6.add(_12,"departUser");
if(this.type!="multile"){
this.showCheckBox=false;
this.height="530px";
_5.set(this._dialog.domNode,"height",this.height);
}
this.selectUtil=new this.general.selectObject();
this.treeStore=new _3({url:this.url});
this._buildSearchTable(_12);
this._buildTreeTable(_12);
},_buildSearchTable:function(_13){
var _14=document.createElement("table");
_6.add(_14,"fieldlist");
var tr=document.createElement("tr");
_14.appendChild(tr);
var td1=document.createElement("td");
var _15=document.createElement("span");
_15.innerHTML="部门名称";
td1.appendChild(_15);
this.searchDepart=new _a({style:{width:"150px",marginLeft:"5px"},store:this.treeStore,query:this.query,searchAttr:"name"},document.createElement("div"));
td1.appendChild(this.searchDepart.domNode);
tr.appendChild(td1);
var td2=document.createElement("td");
var _16=document.createElement("span");
_16.innerHTML="人员名称";
td2.appendChild(_16);
this.searchUser=new _b({style:{width:"150px",marginLeft:"5px"}});
td2.appendChild(this.searchUser.domNode);
tr.appendChild(td2);
var td3=document.createElement("td");
this.searchButton=new _c({label:"查询"});
td3.appendChild(this.searchButton.domNode);
_7.connect(this.searchButton,"onClick",this,"searchFresh");
tr.appendChild(td3);
_13.appendChild(_14);
},searchFresh:function(){
var _17=this.searchDepart.attr("value");
var _18=this.searchUser.attr("value");
var url;
if(_17!=""||_18!=""){
url=this.url+"&depart="+_17+"&user="+_18;
}else{
url=this.url;
}
this.treeStore=new _3({url:url});
this.treenode.innerHTML="";
this._buildTree(this.treenode);
if(_17!=""||_18!=""){
this.tree.expandAll();
}
this.searchDepart.attr("value","");
this.searchUser.attr("value","");
},_buildTreeTable:function(_19){
var _1a=document.createElement("table");
_6.add(_1a,"fieldlist");
var _1b=document.createElement("tbody");
var tr=document.createElement("tr");
var td1=document.createElement("td");
td1.setAttribute("width","45%");
var _1c=document.createElement("div");
_6.add(_1c,"tab_box");
_5.set(_1c,"height","400px");
td1.appendChild(_1c);
if(this.treeLable!=null){
var h3=document.createElement("h3");
h3.innerHTML=this.treeLable;
_1c.appendChild(h3);
}
this.treenode=document.createElement("div");
_6.add(this.treenode,"titlelist");
if(this.folderClass!=null){
_6.add(this.treenode,this.folderClass);
}
this._buildTree(this.treenode);
_1c.appendChild(this.treenode);
tr.appendChild(td1);
var td2=document.createElement("td");
td2.setAttribute("width","10%");
var _1d=document.createElement("div");
_6.add(_1d,"btn_choose");
if(this.type=="multile"){
var _1e=document.createElement("button");
_6.add(_1e,"allRight");
_1d.appendChild(_1e);
_7.connect(_1e,"onclick",this,function(){
this._getStoreItem(this.treeStore,{},function(_1f){
for(var i=0;i<_1f.length;i++){
var _20=_1f[i];
if(_20.type=="user"&&!this.selectUtil.selectIsExitItem(this.searchResult.domNode,_20.id)){
this._addOption(this.searchResult.domNode,_20);
}
this.tree.model.updateCheckbox(_20,false);
}
});
});
_1d.appendChild(document.createElement("br"));
}
var _21=document.createElement("button");
_6.add(_21,"right");
_1d.appendChild(_21);
_7.connect(_21,"onclick",this,function(){
if(this.type=="multile"){
this._getStoreItem(this.treeStore,{checkbox:true},function(_22){
for(var i=0;i<_22.length;i++){
var _23=_22[i];
if(_23.type=="user"&&!this.selectUtil.selectIsExitItem(this.searchResult.domNode,_23.id)){
this._addOption(this.searchResult.domNode,_23);
}
this.tree.model.updateCheckbox(_23,false);
}
});
}else{
if(this.returnData.length>0){
_9.empty(this.searchResult.domNode);
this._addOption(this.searchResult.domNode,this.returnData[0]);
}
}
});
_1d.appendChild(document.createElement("br"));
var _24=document.createElement("button");
_6.add(_24,"left");
_1d.appendChild(_24);
_7.connect(_24,"onclick",this,function(){
var _25=this.searchResult.domNode;
for(var i=_25.length-1;i>=0;i--){
if(_25.options[i].value==this.searchResult.get("value")){
_25.remove(i);
}
}
});
_1d.appendChild(document.createElement("br"));
if(this.type=="multile"){
var _26=document.createElement("button");
_6.add(_26,"allLeft");
_1d.appendChild(_26);
_7.connect(_26,"onclick",this,function(){
_9.empty(this.searchResult.domNode);
});
_1d.appendChild(document.createElement("br"));
}
td2.appendChild(_1d);
tr.appendChild(td2);
var td3=document.createElement("td");
td3.setAttribute("width","45%");
var _27=document.createElement("div");
_6.add(_27,"tab_box");
_5.set(_27,"height","400px");
this.searchResult=new _e({style:{height:"400px",width:"100%",border:"none"}},document.createElement("div"));
_27.appendChild(this.searchResult.domNode);
td3.appendChild(_27);
tr.appendChild(td3);
_1b.appendChild(tr);
_1a.appendChild(_1b);
if(this.type=="multile"){
var _28=document.createElement("tfoot");
var _29=document.createElement("tr");
var _2a=document.createElement("td");
_2a.setAttribute("colspan","3");
this.allselectnode=new _d({},document.createElement("div"));
_2a.appendChild(this.allselectnode.domNode);
var _2b=document.createElement("span");
_2b.innerHTML="全选";
_2a.appendChild(_2b);
this.noselectnode=new _d({},document.createElement("div"));
_2a.appendChild(this.noselectnode.domNode);
var _2c=document.createElement("span");
_2c.innerHTML="反选";
_2a.appendChild(_2c);
_7.connect(this.allselectnode,"onClick",this,function(){
this.allselectnode.set("checked",true);
this.noselectnode.set("checked",false);
this._getStoreItem(this.treeStore,this.query,function(_2d){
for(var i=0;i<_2d.length;i++){
var _2e=_2d[i];
this.tree.model.updateCheckbox(_2e,true);
}
});
});
_7.connect(this.noselectnode,"onClick",this,function(){
this.noselectnode.set("checked",true);
this.allselectnode.set("checked",false);
this._getStoreItem(this.treeStore,this.query,function(_2f){
for(var i=0;i<_2f.length;i++){
var _30=_2f[i];
this.tree.model.updateCheckbox(_30,false);
}
});
});
_29.appendChild(_2a);
_28.appendChild(_29);
_1a.appendChild(_28);
}
_19.appendChild(_1a);
},_buildTree:function(_31){
var _32=document.createElement("div");
_31.appendChild(_32);
var _33=new _10.model({store:this.treeStore,query:this.query,childrenAttrs:["children"],rootLabel:this.rootLabel,checkboxAll:this.showCheckBox,checkboxRoot:false,checkboxState:false});
var _34=true;
if(!this.showCheckBox){
}
var _35={model:_33,openOnClick:_34,autoExpand:true,persist:false,showRoot:this.showRoot};
this.tree=new _10.tree(_35,_32);
if(!this.showCheckBox){
_7.connect(this.tree,"onClick",this,"onclick");
}
if(this.type!="multile"&&this.isSelected){
_7.connect(this.tree,"onLoad",this,function(){
this._getStoreItem(this.treeStore,{type:"user"},function(_36){
if(_36.length==1){
var _37=_36[0];
this.onclick(_37);
}
this.afterLoad();
});
});
}
this.tree.startup();
},refresh:function(){
this.contentPane.innerHTML="";
this.buildContent(this.contentPane);
},onclick:function(_38,_39){
if(_38.type=="user"){
_9.empty(this.searchResult.domNode);
this._addOption(this.searchResult.domNode,_38);
this.returnData.length=0;
this.returnData.push(_38);
}else{
this.returnData.length=0;
}
},afterLoad:function(){
},_addOption:function(_3a,_3b){
var c=_8.doc.createElement("option");
c.innerHTML=_3b.name;
c.value=_3b.id;
c.departId=_3b.departId;
c.departName=_3b.departName;
_3a.appendChild(c);
},getData:function(){
var _3c=this.selectUtil.getAllValue(this.searchResult.domNode);
return _3c;
},_getStoreItem:function(_3d,_3e,_3f){
_3d.fetch({query:_3e,queryOptions:{deep:true},onComplete:_2.hitch(this,_3f)});
}});
});

