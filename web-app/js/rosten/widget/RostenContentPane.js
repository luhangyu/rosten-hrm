define(["dojo/_base/declare","dijit/_WidgetBase","dijit/_TemplatedMixin","dojox/layout/ContentPane"],function(_1,_2,_3,_4){
return _1("rosten.widget.RostenContentPane",[_2,_3],{templateString:"<div data-dojo-attach-point=\"containerNode\"></div>",contentPane:null,src:"",postCreate:function(){
this.contentPane=new _4({renderStyles:true,executeScripts:true,style:{"padding":"2px","paddingLeft":"0px","paddingRight":"0px"}});
this.contentPane.set("href",this.src);
this.containerNode.appendChild(this.contentPane.domNode);
},destroy:function(){
this.contentPane.destroyRecursive();
}});
});

