define(["dojo/_base/declare","dojo/dom-style","dijit/form/SimpleTextarea","rosten/widget/_Dialog"],function(_1,_2,_3,_4){
return _1("rosten.widget.CommentDialog",[_4],{height:"180px",width:"500px",title:"填写意见",type:"",content:"",buildContent:function(_5){
this.contentNode=new _3({style:{height:"100px",width:"480px"},maxLength:"1000",trim:true});
if(this.content!=""){
this.contentNode.set("value",this.content);
}
_5.appendChild(this.contentNode.domNode);
},refresh:function(_6){
this.contentPane.innerHTML="";
if(_6.type){
this.type=_6.type;
}
if(_6.content){
this.content=_6.content;
}
this.buildContent(this.contentPane);
},getData:function(){
var _7={};
_7.type=this.type;
_7.content=this.contentNode.get("value");
return _7;
}});
});

