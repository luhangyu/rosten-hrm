define(["dojo/_base/declare","dojo/_base/connect","dijit/_WidgetBase","dijit/_TemplatedMixin","dojox/layout/ContentPane","rosten/widget/RostenDialog"],function(_1,_2,_3,_4,_5,_6){
return _1("rosten.widget.ShowDialog1",[_3,_4],{templateString:"<div dojoAttachPoint=\"containerNode\"></div>",dialog:null,dlgContentPane:null,src:"",position_absolute_x:200,position_absolute_y:200,callback:null,callbackargs:null,onLoadFunction:null,title:"系统对话框",postCreate:function(){
this.dialog=new _6({id:"rosten_showDialog",title:this.title,renderStyles:true},this.containerNode.domNode);
if(this.src==""){
this.dialog.setContent("error:can not get the data......");
}else{
this.dlgContentPane=new _5({id:"rosten_dialogContent",renderStyles:true});
this.dlgContentPane.setHref(this.src);
this.dialog.setContent(this.dlgContentPane.domNode);
this.dlgContentPane.startup();
if(this.onLoadFunction!=null){
_2.connect(this.dlgContentPane,"onLoad",this.onLoadFunction);
}
}
this.show();
},hide:function(){
this.dialog.hide();
},show:function(){
this.dialog.show();
this.dialog.domNode.style.left=this.position_absolute_x+"px";
this.dialog.domNode.style.top=this.position_absolute_y+"px";
},getGrid:function(){
return this.dialog;
},changePosition:function(x,y){
this.position_absolute_x=x;
this.position_absolute_y=y;
this.dialog.domNode.style.left=this.position_absolute_x+"px";
this.dialog.domNode.style.top=this.position_absolute_y+"px";
},destroy:function(){
this.dialog.destroyRecursive();
this.dlgContentPane.destroyRecursive();
}});
});

