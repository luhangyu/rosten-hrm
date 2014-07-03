define(["dojo/_base/declare","dojo/_base/lang","dojo/_base/xhr","dojo/query","dojo/dom-style","dojo/_base/kernel","dojo/_base/connect","dijit/registry","dijit/form/CheckBox","dijit/form/TextBox","dijit/form/Button","rosten/widget/_Dialog","rosten/util/general"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d){
return _1("rosten.widget.MultiSelectDialog",[_c],{height:"370px",width:"350px",title:"Rosten_选择框",chkboxwidgets:[],defaultvalues:[],datasrc:"",canappend:false,extra_choice:null,single:false,choicelist:[],choicelist_node:null,general:new _d(),constructor:function(_e){
this.chkboxwidgets=[];
if(this.initialized){
var _f=_4(".dijitCheckBox",this._dialog.domNode);
for(var i=0;i<_f.length;i++){
this.chkboxwidgets.push(_8.getEnclosingWidget(_f[i]));
}
}
},buildContent:function(_10){
var _11=document.createElement("fieldset");
var _12="290px";
var h1="255px";
if(this.canappend){
_12="240px";
h1="220px";
}
_5.set(_11,{"border":"1px solid gray","margin":"2px","padding":"3px","fontSize":"12px","height":_12});
_10.appendChild(_11);
var _13=document.createElement("legend");
_5.set(_13,{color:"blue"});
_13.innerHTML="请选择以下选项：";
_11.appendChild(_13);
this.choicelist_node=document.createElement("div");
this.choicelist_node.setAttribute("id","choicelist");
_5.set(this.choicelist_node,{"height":h1,"overflow":"auto"});
_11.appendChild(this.choicelist_node);
if(this.datasrc==""){
this._render(_10);
return;
}
var _14={url:this.datasrc,handleAs:"json",timeout:2000,preventCache:true,sync:true,load:_2.hitch(this,function(_15,_16){
this.choicelist=[];
for(var i=0;i<_15.length;i++){
this.choicelist.push(_15[i].name+"|"+_15[i].id);
}
this._render(_10);
}),error:_2.hitch(this,function(_17,_18){
console.debug(_17);
})};
_3.get(_14);
},_render:function(_19){
var _1a=this.chkboxwidgets;
var _1b=this;
_6.forEach(this.choicelist,function(_1c){
if(_1c.indexOf("|")!=-1){
var arr=_1c.split("|");
var _1d="picklist_"+arr[1];
var _1e=arr[0];
}else{
var _1d="picklist_"+_1c;
var _1e=_1c;
}
if(_8.byId(_1d)){
_8.byId(_1d).destroy();
}
var _1f=document.createElement("div");
_5.set(_1f,"height","20px");
var _20={id:_1d,name:_1e};
var _21=new _9(_20,document.createElement("div"));
if(_1b.single==true){
_7.connect(_21,"onClick",_1b,"choiceCheck");
}
_1f.appendChild(_21.domNode);
var _22=document.createElement("label");
_22.innerHTML=_1e;
_22.setAttribute("for",_1d);
_1f.appendChild(_22);
_1b.choicelist_node.appendChild(_1f);
_1a.push(_21);
});
for(var i=0;i<this.defaultvalues.length;i++){
var dv=this.defaultvalues[i];
for(var j=0;j<this.chkboxwidgets.length;j++){
var w=this.chkboxwidgets[j];
if(w.attr("name")==dv){
w.attr("value","on");
}
}
}
if(this.canappend){
var _23=document.createElement("fieldset");
_5.set(_23,{"border":"1px solid gray","margin":"2px","padding":"3px","fontSize":"12px","height":"40px"});
_19.appendChild(_23);
var _24=document.createElement("legend");
_5.set(_24,{color:"blue"});
_24.innerHTML="其他选项：";
_23.appendChild(_24);
var _25=document.createElement("span");
_25.innerHTML="新值：";
_23.appendChild(_25);
var _26=document.createElement("div");
_5.set(_26,{"fontSize":"12px",height:"20px",width:"220px"});
_23.appendChild(_26);
this.extra_choice=new _a({style:{height:"18px",width:"200px"}},_26);
var w=this.extra_choice;
_7.connect(w,"onKeyPress",this,"onKeyPress");
var _27=document.createElement("div");
_23.appendChild(_27);
var _28=new _b({label:"添加"},_27);
_7.connect(_28,"onClick",this,"appendChoice");
}
},initCheckData:function(_29){
_6.forEach(this.chkboxwidgets,function(w){
var _2a=w.attr("name");
if(this.general.isInArray(_29,_2a)){
w.attr("value",true);
}
});
},getData:function(){
var _2b=[];
_6.forEach(this.chkboxwidgets,function(w){
if(w.attr("value")!=false){
var _2c={};
var tmp=w.attr("id");
var _2d=w.attr("name");
_2c.name=_2d;
if(tmp.indexOf("picklist_")!=-1){
_2c.id=tmp.split("picklist_")[1];
_2b.push(_2c);
}else{
_2c.id=tmp;
_2b.push(_2c);
}
}
});
return _2b;
},appendChoice:function(){
var _2e=this.extra_choice.attr("value");
if(_2.trim(_2e)==""){
this.extra_choice.attr("value","");
return;
}
var _2f=_4("#choicelist",this._dialog.domNode)[0];
var _30=document.createElement("div");
_5.set(_30,"height","20px");
if(_2e.indexOf("|")!=-1){
var arr=_2e.split("|");
var _31="picklist_"+arr[1];
var _32=arr[0];
}else{
var _31="picklist_"+_2e;
var _32=_2e;
}
var _33={id:_31,name:_32};
var _34=new _9(_33,document.createElement("div"));
if(this.single){
_7.connect(_34,"onClick",this,"choiceCheck");
for(var k=0;k<this.chkboxwidgets.length;k++){
this.chkboxwidgets[k].attr("value",false);
}
}
_34.attr("value","on");
_30.appendChild(_34.domNode);
var _35=document.createElement("label");
_35.innerHTML=_32;
_35.setAttribute("for",_31);
_30.appendChild(_35);
_2f.appendChild(_30);
_2f.scrollTop=_2f.scrollHeight;
this.chkboxwidgets.push(_34);
this.extra_choice.attr("value","");
},onKeyPress:function(e){
if(e.keyCode=="13"){
this.appendChoice();
}
},choiceCheck:function(e){
var w=_8.getEnclosingWidget(e.target);
if(w.attr("value")=="on"){
for(var i=0;i<this.chkboxwidgets.length;i++){
if(this.chkboxwidgets[i]!=w){
if(this.chkboxwidgets[i].attr("value")=="on"){
this.chkboxwidgets[i].attr("value",false);
}
}
}
}
},refresh:function(){
this.contentPane.innerHTML="";
this.chkboxwidgets=[];
if(this.initialized){
var _36=_4(".dijitCheckBox",this._dialog.domNode);
for(var i=0;i<_36.length;i++){
this.chkboxwidgets.push(_8.getEnclosingWidget(_36[i]));
}
}
this.buildContent(this.contentPane);
},simpleRefresh:function(){
this.choicelist_node.innerHTML="";
this.chkboxwidgets=[];
if(this.initialized){
var _37=_4(".dijitCheckBox",this._dialog.domNode);
for(var i=0;i<_37.length;i++){
this.chkboxwidgets.push(_8.getEnclosingWidget(_37[i]));
}
}
this._render(this.contentPane);
}});
});

