define(["dojo/_base/declare"],function(_1){
return _1("rosten.util.general",null,{htmlEncode:function(_2){
_2=_2.Replace(">","&gt;");
_2=_2.Replace("<","&lt;");
_2=_2.Replace(" "," &nbsp;");
_2=_2.Replace(" "," &nbsp;");
_2=_2.Replace("\"","&quot;");
_2=_2.Replace("'","&#39;");
_2=_2.Replace("\n","<br/> ");
return _2;
},htmlDiscode:function(_3){
_3=_3.Replace("&gt;",">");
_3=_3.Replace("&lt;","<");
_3=_3.Replace("&nbsp;"," ");
_3=_3.Replace(" &nbsp;"," ");
_3=_3.Replace("&quot;","\"");
_3=_3.Replace("&#39;","'");
_3=_3.Replace("<br/> ","\n");
return _3;
},selectObject:_1("rosten.util.select",null,{getAllValue:function(_4){
var _5=[];
for(var i=0;i<_4.options.length;i++){
var _6={name:_4.options[i].text,value:_4.options[i].value,departId:_4.options[i].departId,departName:_4.options[i].departName};
_5.push(_6);
}
return _5;
},getSelectText:function(_7){
var _8=_7.selectedIndex;
var _9=_7.options[_8].text;
return _9;
},getSelectValue:function(_a){
var _b=_a.selectedIndex;
var _c=_a.options[_b].value;
return _c;
},selectItemByDefault:function(_d){
_d.options[0].selected=true;
},selectItemByValue:function(_e,_f){
for(var i=0;i<_e.options.length;i++){
if(_e.options[i].value==_f){
_e.options[i].selected=true;
break;
}
}
},selectItemByText:function(_10,_11){
for(var i=0;i<_10.options.length;i++){
if(_10.options[i].text==_11){
_10.options[i].selected=true;
break;
}
}
},removeItemFromSelect:function(_12,_13){
if(this.selectIsExitItem(_12,_13)){
for(var i=0;i<_12.options.length;i++){
if(_12.options[i].value==_13){
_12.options.remove(i);
break;
}
}
}
},AddItemToSelect:function(_14,_15,_16){
if(this.selectIsExitItem(_14,_16)){
}else{
var _17=new Option(_15,_16);
_14.options.add(_17);
}
},selectIsExitItem:function(_18,_19){
var _1a=false;
for(var i=0;i<_18.options.length;i++){
if(_18.options[i].value==_19){
_1a=true;
break;
}
}
return _1a;
},addSelectOption:function(_1b,_1c,_1d){
var _1e=document.createElement("option");
_1e.appendChild(document.createTextNode(_1c));
if(arguments.length==3){
_1e.setAttribute("value",_1d);
}
_1b.appendChild(_1e);
}}),setSpace:function(num){
var _1f="";
for(var i=0;i<num;i++){
_1f+="&nbsp;";
}
return _1f;
},getUrlArgs:function(){
var _20=new Object();
var _21=location.search.substring(1);
var _22=_21.split("&");
for(var i=0;i<_22.length;i++){
var pos=_22[i].indexOf("=");
if(pos==-1){
continue;
}
var _23=_22[i].substring(0,pos);
var _24=_22[i].substring(pos+1);
_24=decodeURIComponent(_24);
_20[_23]=_24;
}
return _20;
},implodeArray:function(arr,_25){
if((arr&&arr instanceof Array||typeof arr=="array")){
var _26="";
for(var i=0;i<arr.length;i++){
if(i==0){
_26+=arr[i];
}else{
_26+=_25+arr[i];
}
}
return _26;
}else{
return arr;
}
},arrayTrim:function(arr){
if(arr&&arr instanceof Array||typeof arr=="array"){
var o=[];
for(var i=0;i<arr.length;i++){
if(this.isInArray(o,arr[i])==false&&arr[i]!=""&&arr[i]!=" "){
o.push(arr[i]);
}
}
return o;
}else{
return arr;
}
},findInArray:function(arr,_27){
if(arr&&arr instanceof Array||typeof arr=="array"){
for(var i=0;i<arr.length;i++){
if(arr[i]==_27){
return i;
}
}
return -1;
}else{
if(arr&&typeof arr=="string"||arr instanceof String){
return arr.indexOf(_27);
}
}
},isInArray:function(arr,_28){
if(this.findInArray(arr,_28)==-1){
return false;
}else{
return true;
}
},stringRightBack:function(str,_29){
var _2a=str.lastIndexOf(_29);
if(_2a==-1){
return str;
}
return str.substr(_2a+1);
},stringLeftBack:function(str,_2b){
var _2c=str.lastIndexOf(_2b);
if(_2c==-1){
return str;
}
return str.substring(0,_2c);
},stringRight:function(str,_2d){
var _2e=str.indexOf(_2d);
if(_2e==-1){
return str;
}
return str.substr(_2e+1);
},stringLeft:function(str,_2f){
var _30=str.indexOf(_2f);
if(_30==-1){
return str;
}
return str.substring(0,_30);
},_stringTrim:function(str,_31){
if(!str.replace){
return str;
}else{
if(!str.length){
return str;
}else{
var re=(_31>0)?(/^\s+/):(_31<0)?(/\s+$/):(/^\s+|\s+$/g);
return str.replace(re,"");
}
}
},stringTrim:function(str,_32){
if(!_32){
var _33=this.splitString(str," ");
}else{
var _33=this.splitString(str,_32);
}
return _33.join("");
},splitString:function(str,_34){
var o=[];
if(str.indexOf(_34)==-1){
o.push(str);
return o;
}else{
return o=str.split(_34);
}
},getStrLength:function(str){
var len=0;
for(var i=0;i<str.length;i++){
var _35=str.charCodeAt(i);
if(!(_35<27||_35>126)){
len=len+1;
}else{
len=len+2;
}
}
return len;
},checkStrLength:function(str,_36){
var _37=this.getStrLength(str);
if(_37>_36){
return false;
}else{
return true;
}
},chgNumToRMB:function(_38,How){
var _38=Math.round(_38*Math.pow(10,How))/Math.pow(10,How);
return _38;
},obj2str:function(obj){
var _39=this;
switch(typeof (obj)){
case "string":
return "\""+obj.replace(/(["\\])/g,"\\$1")+"\"";
case "array":
return "["+obj.map(_39.obj2str).join(",")+"]";
case "object":
if(obj instanceof Array){
var _3a=[];
var len=obj.length;
for(var i=0;i<len;i++){
_3a.push(_39.obj2str(obj[i]));
}
return "["+_3a.join(",")+"]";
}else{
if(obj==null){
return "null";
}else{
var _3b=[];
for(var _3c in obj){
_3b.push(_39.obj2str(_3c)+":"+_39.obj2str(obj[_3c]));
}
return "{"+_3b.join(",")+"}";
}
}
case "number":
return obj;
case false:
return obj;
}
}});
});

