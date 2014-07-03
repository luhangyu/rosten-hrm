define(["dojo/_base/declare","dojo/_base/window","dojo/_base/lang","dojo/_base/kernel","dojo/dom-construct","dijit/tree/ForestStoreModel","dijit/Tree","dojo/data/api/Write"],function(_1,_2,_3,_4,_5,_6,_7,_8){
var _9=_1("rosten.widget.CheckBoxStoreModel",_6,{checkboxAll:true,checkboxState:false,checkboxRoot:false,checkboxStrict:true,checkboxIdent:"checkbox",updateCheckbox:function(_a,_b){
this._setCheckboxState(_a,_b);
if(this.checkboxStrict){
this._updateChildCheckbox(_a,_b);
this._updateParentCheckbox(_a,_b);
}
},getCheckboxState:function(_c){
var _d=undefined;
if(_c==this.root){
if(typeof (_c.checkbox)=="undefined"){
this.root.checkbox=undefined;
if(this.checkboxRoot){
_d=this.root.checkbox=this.checkboxState;
}
}else{
_d=this.root.checkbox;
}
}else{
_d=this.store.getValue(_c,this.checkboxIdent);
if(_d==undefined&&this.checkboxAll){
this._setCheckboxState(_c,this.checkboxState);
_d=this.checkboxState;
}
}
return _d;
},_setCheckboxState:function(_e,_f){
var _10=true;
if(_e!=this.root){
var _11=this.store.getValue(_e,this.checkboxIdent);
if(_11!=_f&&(_11!==undefined||this.checkboxAll)){
this.store.setValue(_e,this.checkboxIdent,_f);
}else{
_10=false;
}
}else{
if(this.root.checkbox!=_f&&(this.root.checkbox!==undefined||this.checkboxRoot)){
this.root.checkbox=_f;
}else{
_10=false;
}
}
if(_10){
this.onCheckboxChange(_e);
}
return _10;
},_updateChildCheckbox:function(_12,_13){
if(this.mayHaveChildren(_12)){
this.getChildren(_12,_3.hitch(this,function(_14){
_4.forEach(_14,function(_15){
if(this._setCheckboxState(_15,_13)){
var _16=this._getParentsItem(_15);
if(_16.length>1){
this._updateParentCheckbox(_15,_13);
}
}
if(this.mayHaveChildren(_15)){
this._updateChildCheckbox(_15,_13);
}
},this);
}),function(err){
console.error(this,": updating child checkboxes: ",err);
});
}
},_updateParentCheckbox:function(_17,_18){
var _19=this._getParentsItem(_17);
_4.forEach(_19,function(_1a){
if(_18){
this.getChildren(_1a,_3.hitch(this,function(_1b){
var _1c=true;
_4.some(_1b,function(_1d){
siblState=this.getCheckboxState(_1d);
if(siblState!==undefined&&_1c){
_1c=siblState;
}
return !(_1c);
},this);
if(_1c){
this._setCheckboxState(_1a,true);
this._updateParentCheckbox(_1a,true);
}
}),function(err){
console.error(this,": updating parent checkboxes: ",err);
});
}else{
if(this._setCheckboxState(_1a,false)){
this._updateParentCheckbox(_1a,false);
}
}
},this);
},_getParentsItem:function(_1e){
var _1f=[];
if(_1e!=this.root){
var _20=_1e[this.store._reverseRefMap];
for(itemId in _20){
_1f.push(this.store._itemsByIdentity[itemId]);
}
if(!_1f.length){
_1f.push(this.root);
}
}
return _1f;
},validateData:function(_21,_22){
if(!_22.checkboxStrict){
return;
}
_22.getChildren(_21,_3.hitch(_22,function(_23){
var _24=true;
var _25;
_4.forEach(_23,function(_26){
if(this.mayHaveChildren(_26)){
this.validateData(_26,this);
}
_25=this.getCheckboxState(_26);
if(_25!==undefined&&_24){
_24=_25;
}
},this);
if(this._setCheckboxState(_21,_24)){
this._updateParentCheckbox(_21,_24);
}
}),function(err){
console.error(this,": validating checkbox data: ",err);
});
},onCheckboxChange:function(_27){
}});
var _28=_1("rosten.widget._CheckBoxTreeNode",_7._TreeNode,{_checkbox:null,_createCheckbox:function(){
var _29=this.tree.model.getCheckboxState(this.item);
if(_29!==undefined){
this._checkbox=_2.doc.createElement("input");
this._checkbox.type="checkbox";
this._checkbox.checked=_29;
_5.place(this._checkbox,this.expandoNode,"after");
}
},postCreate:function(){
this._createCheckbox();
this.inherited(arguments);
}});
var _2a=_1("rosten.widget.CheckBoxTree",_7,{onNodeChecked:function(_2b,_2c){
},onNodeUnchecked:function(_2d,_2e){
},_onClick:function(_2f,e){
var _30=e.target;
if(_30.nodeName!="INPUT"){
return this.inherited(arguments);
}
this._publish("execute",{item:_2f.item,node:_2f});
this.model.updateCheckbox(_2f.item,_2f._checkbox.checked);
this.onClick(_2f.item,_2f,e);
if(_2f._checkbox.checked){
this.onNodeChecked(_2f.item,_2f);
}else{
this.onNodeUnchecked(_2f.item,_2f);
}
this.focusNode(_2f);
},_onCheckboxChange:function(_31){
var _32=this.model,_33=_32.getIdentity(_31),_34=this._itemNodesMap[_33];
if(_34){
_4.forEach(_34,function(_35){
if(_35._checkbox!=null){
_35._checkbox.checked=this.model.getCheckboxState(_31);
}
},this);
}
},postCreate:function(){
var _36=this.model.store;
if(!_36.getFeatures()["dojo.data.api.Write"]){
throw new Error("rosten.widget.CheckboxTree: store must support dojo.data.Write");
}
this.connect(this.model,"onCheckboxChange","_onCheckboxChange");
this.inherited(arguments);
},_createTreeNode:function(_37){
return new rosten.widget._CheckBoxTreeNode(_37);
}});
var _38={tree:_2a,model:_9,treeNode:_28};
return _38;
});

