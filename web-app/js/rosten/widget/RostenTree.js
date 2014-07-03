define(["dojo/_base/declare","dojo/_base/kernel","dojo/_base/lang","dojo/_base/xhr","dojo/dom-construct","dojo/dom-class","dojo/_base/connect","dijit/_WidgetBase","dijit/_TemplatedMixin","dijit/Tree","dojo/data/ItemFileReadStore","dijit/tree/ForestStoreModel"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c){
return _1("rosten.widget.RostenTree",[_8,_9],{templateString:"<div data-dojo-attach-point=\"containerNode\"></div>",id:"",store:null,treeLabel:"",url:"",defaultentry:"",postCreate:function(){
this.id=this.id!=""?this.id:this.widgetId;
var _d=this.url;
_4.get({url:_d,handleAs:"json",preventCache:true,load:_3.hitch(this,function(_e,_f){
var _10=_5.create("div");
this.containerNode.appendChild(_10);
_6.add(this.containerNode,"tree");
this.store=new _b({data:_e});
var _11=new _c({rootId:"rostenTreeRoot",store:this.store,query:{type:"*"},childrenAttrs:["children"]});
var _12={model:_11,openOnClick:true,persist:false};
if(this.treeLabel!=""){
_12.showRoot=true;
_12.label=this.treeLabel;
}else{
_12.showRoot=false;
}
var _13=new _a(_12,_10);
_13.startup();
_7.connect(_13,"onClick",this,"onclick");
if(this.defaultentry!=""){
var _14={query:{expand:"yes"},queryOptions:{deep:true,ignoreCase:true},onComplete:_3.hitch(this,function(_15,_16){
if(_15.length>0){
_2.forEach(_15,function(_17){
var _18=_13.getNodesByItem(_17);
var _19=_18[0];
if(_19.isExpandable&&!_19.isExpanded){
_13._expandNode(_19);
}
});
var _1a=_15[_15.length-1];
var id=_11.getIdentity(_1a);
var _1b=_13._itemNodesMap[id][0];
_13.focusNode(_1b);
_13.set("paths",[["rostenTreeRoot",id]]);
this.onclick(_1a,_1b);
}
})};
this.store.fetch(_14);
}
}),error:_3.hitch(this,function(_1c){
this.onDownloadError(_1c);
})});
},onDownloadError:function(_1d){
},onclick:function(_1e,_1f){
var _20=this.store.getValue(_1e,"action");
var _21=this.store.getValue(_1e,"name");
var id=this.store.getValue(_1e,"id");
_3.hitch(null,_20,_21,id)();
}});
});

