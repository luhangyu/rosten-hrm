define(["dojo/_base/declare","dojo/_base/connect","dijit/_WidgetBase","dijit/_TemplatedMixin","rosten/widget/RostenDialog"],function(_1,_2,_3,_4,_5){
return _1("rosten.widget.ShowDialog",[_3,_4],{templateString:"<div dojoAttachPoint=\"containerNode\"></div>",dialog:null,src:"",onLoadFunction:null,postCreate:function(){
this.dialog=new _5({id:"rosten_showDialog",renderStyles:true},this.containerNode.domNode);
if(this.src==""){
this.dialog.setContent("error:can not get the data......");
}else{
this.dialog.setHref(this.src);
if(this.onLoadFunction!=null){
_2.connect(this.dialog,"onLoad",this.onLoadFunction);
}
}
this.show();
},hide:function(){
this.dialog.hide();
},show:function(){
this.dialog.show();
},getGrid:function(){
return this.dialog;
},destroy:function(){
this.dialog.destroyRecursive();
}});
});

