define(["dojo/_base/declare","dojo/dom-style","dojo/dom-class","dojo/_base/xhr","dojo/dom-construct","dojo/_base/connect","dojo/_base/lang","dijit/_WidgetBase","dijit/Tree","dijit/_WidgetBase","dijit/_TemplatedMixin","dojo/data/ItemFileWriteStore","dijit/tree/ForestStoreModel"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_8,_a,_b,_c){
_1("rosten.widget.LinkWidget",_8,{linkName:"",buildRendering:function(){
this.domNode=document.createElement("span");
this.a=document.createElement("a");
this.a.innerHTML=this.linkName;
this.a.href="#";
_2.set(this.a,{"display":"none","marginLeft":"5px"});
this.domNode.appendChild(this.a);
this.inherited(arguments);
}});
_1("rosten.widget.LinkTreeNode",_9._TreeNode,{linkName:"",linkFunction:null,linkShowType:"",postCreate:function(){
this.inherited(arguments);
if(this.item&&!this.item.root&&(this.item.type&&this.linkShowType==this.item.type)){
this.linkWidget=new rosten.widget.LinkWidget({linkName:this.linkName,linkFunction:this.linkFunction});
_6.connect(this.linkWidget.a,"onclick",this,function(){
this.linkFunction(this.item);
});
this.contentNode.appendChild(this.linkWidget.domNode);
_5.place(this.labelNode,this.linkWidget.domNode,"first");
}
}});
_1("rosten.widget.LinkTree",_9,{linkShowType:"",linkName:"",linkFunction:null,focusNode:function(_d){
this.inherited(arguments);
this.dndController.setSelection([_d]);
},_createTreeNode:function(_e){
_e.linkName=this.linkName;
_e.linkFunction=this.linkFunction;
_e.linkShowType=this.linkShowType;
return new rosten.widget.LinkTreeNode(_e);
}});
return _1("rosten.widget.RostenLinkTree",[_8,_a],{templateString:"<div data-dojo-attach-point=\"containerNode\"></div>",id:"",store:null,url:"",linkName:"add",linkFunction:null,linkShowType:"depart",postCreate:function(){
this.id=this.id!=""?this.id:this.widgetId;
var _f=this.url;
_4.get({url:_f,handleAs:"json",preventCache:true,load:_7.hitch(this,function(_10,_11){
var _12=_5.create("div");
this.containerNode.appendChild(_12);
_3.add(this.containerNode,"tree");
if(this.store==null){
this.store=new _b({data:_10});
}
var _13=new _c({store:this.store,query:{type:"*"},childrenAttrs:["children"]});
var _14={model:_13,showRoot:false,openOnClick:true,persist:false,linkName:this.linkName,linkFunction:this.linkFunction,linkShowType:this.linkShowType};
this.tree=new rosten.widget.LinkTree(_14,_12);
_6.connect(this.tree.dndController,"onMouseOver",this,function(_15,evt){
this._addLinkWidget(_15);
if(_15.linkWidget){
_2.set(_15.linkWidget.a,"display","");
}
});
this.tree.dndController.onMouseOut=function(_16,evt){
if(_16.linkWidget){
_2.set(_16.linkWidget.a,"display","none");
}
};
_6.connect(this.tree,"onClick",this,function(_17){
this.onClick(_17,this.store);
});
_6.connect(this.tree,"onOpen",this,function(_18){
this.onOpen(_18,this.store);
});
this.tree.startup();
})});
},_addLinkWidget:function(_19){
if(_19.linkWidget){
return;
}
if(_19.item&&!_19.item.root&&(_19.item.type&&_19.linkShowType==_19.item.type)){
_19.linkWidget=new rosten.widget.LinkWidget({linkName:_19.linkName,linkFunction:_19.linkFunction});
_6.connect(_19.linkWidget.a,"onclick",_19,function(){
_19.linkFunction(_19.item);
});
_19.contentNode.appendChild(_19.linkWidget.domNode);
_5.place(_19.labelNode,_19.linkWidget.domNode,"first");
}
},onClick:function(_1a,_1b){
},onOpen:function(_1c,_1d){
}});
});

