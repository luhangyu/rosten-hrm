define(["dijit/Dialog","dojo/_base/declare","dojo/_base/kernel","dojo/dom-style"],function(_1,_2,_3,_4){
return _2("rosten.widget.RostenDialog",[_1],{disableCloseButton:true,postCreate:function(){
this.inherited(arguments);
this._updateCloseButtonState();
},_onKey:function(_5){
if(this.disableCloseButton&&_5.charOrCode==_3.keys.ESCAPE){
return;
}
this.inherited(arguments);
},setCloseButtonDisabled:function(_6){
this.disableCloseButton=_6;
this._updateCloseButtonState();
},_updateCloseButtonState:function(){
_4.set(this.closeButtonNode,"display",this.disableCloseButton?"none":"block");
}});
});

