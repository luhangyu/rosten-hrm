define(["dojo/_base/declare","dojo/_base/lang","dojo/data/ItemFileWriteStore","dojo/_base/xhr","dojo/dom-style","dojo/dom-class","dojo/_base/connect","rosten/widget/_Dialog","rosten/widget/CheckBoxTree"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9){
return _1("rosten.widget.PickTreeDialog",[_8],{title:"Rosten_Tree_Dialog",url:null,rootLabel:"Rosten",showCheckBox:false,showRoot:true,query:{parentId:null},folderClass:null,height:"380px",width:"400px",returnData:[],urltype:"JSON",buildContent:function(_a){
if(this.urltype=="XML"){
var _b={url:this.url,handleAs:"xml",timeout:5000,load:_2.hitch(this,function(_c,_d){
var _e=_c.documentElement;
var _f=_e.getElementsByTagName("valid")[0];
var _10=_e.getElementsByTagName("title")[0];
this.rootLabel=_10.textContent||_10.text;
var _11={"identifier":"id","label":"name","items":[]};
if((_f.text||_f.textContent)=="OK"){
var _12=_e.getElementsByTagName("datalist")[0];
var _13=_12.getElementsByTagName("Department");
for(var i=0;i<_13.length;i++){
var _14=_13[i];
var id=_14.getAttribute("id");
var _15=_14.getAttribute("name");
var _16={id:id,name:_15,type:"parent",children:[]};
if(this.showCheckBox){
_16.checkbox=false;
}
var arr=_14.getElementsByTagName("Person");
for(var k=0;k<arr.length;k++){
var _17=arr[k].text||arr[k].textContent;
var _18=arr[k].getAttribute("id");
var _19={id:_18,name:_17,type:"child"};
if(this.showCheckBox){
_19.checkbox=false;
}
_16.children.push(_19);
}
_11.items.push(_16);
}
console.log(_11);
this.treeStore=new _3({data:_11});
this._buildTree(_a);
}
}),error:_2.hitch(this,function(_1a,_1b){
console.error(_1a);
})};
_4.get(_b);
}else{
this.treeStore=new _3({url:this.url});
this._buildTree(_a);
}
},_buildTree:function(_1c){
var _1d=document.createElement("div");
_5.set(_1d,{height:"295px",padding:"3px",marginBottom:"5px",border:"1px solid gray",overflow:"auto"});
if(this.folderClass!=null){
_6.add(_1d,this.folderClass);
}
_1c.appendChild(_1d);
var _1e=document.createElement("div");
_1d.appendChild(_1e);
var _1f=new _9.model({store:this.treeStore,query:this.query,childrenAttrs:["children"],rootLabel:this.rootLabel,checkboxAll:this.showCheckBox,checkboxRoot:false,checkboxState:false});
var _20=true;
if(!this.showCheckBox){
_20=false;
}
var _21={model:_1f,openOnClick:_20,autoExpand:true,persist:false,showRoot:this.showRoot};
this.tree=new _9.tree(_21,_1e);
if(!this.showCheckBox){
_7.connect(this.tree,"onClick",this,"onclick");
}
_7.connect(this.tree,"onLoad",this,"afterLoad");
this.tree.startup();
},afterLoad:function(){
},selectedData:function(ids){
for(var i=0;i<ids.length;i++){
this.treeStore.fetchItemByIdentity({identity:ids[i],onItem:_2.hitch(this,function(_22){
this.tree.model.updateCheckbox(_22,true);
})});
}
},refresh:function(){
this.contentPane.innerHTML="";
this.buildContent(this.contentPane);
},onclick:function(_23,_24){
this.returnData.length=0;
this.returnData.push(_23);
},getData:function(){
if(!this.showCheckBox){
return this.returnData;
}
var _25=[];
var _26=this.tree.model.store;
var _27=this;
_26.fetch({query:{checkbox:true},queryOptions:{deep:true},onComplete:function(_28,_29){
for(var i=0;i<_28.length;i++){
var _2a=_28[i];
_25.push(_2a);
}
}});
return _25;
}});
});

