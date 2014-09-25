define(["dojo/_base/declare","dojo/_base/kernel","dojo/_base/lang","dojo/_base/xhr","dojo/dom-style","dojo/dom-class","dojo/data/ItemFileWriteStore","dojo/_base/connect","dojo/number","dijit/_WidgetBase","dijit/_TemplatedMixin","dojo/text!./templates/RostenGrid.html","dijit/form/TextBox","dojox/collections/SortedList","dojox/grid/_CheckBoxSelector","dojox/grid/DataGrid","rosten/kernel/_kernel","rosten/kernel/behavior","rosten/util/gen-dialog","rosten/util/general"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e,_f,_10,_11,_12,_13,_14){
return _1("rosten.widget.RostenGrid",[_a,_b],{templateString:_c,id:"",url:"",defaultUrl:"",urlContent:null,store:null,structure:null,grid:null,query:"",sortStatus:false,showRowSelector:"false",rowSelector:"20px",gridHeight:-1,autoGridRow:-1,pageSize:15,showPageControl:true,emptymsg:"目前暂无数据！",pageControl:{page:1,total:0,totalpages:0,start:0,count:0},connectArray:[],_gridUtil:null,constructor:function(){
this.refreshHeader=true;
this.refreshPageControl=true;
this.refreshData=true;
},postCreate:function(){
this.pageControl.count=this.pageSize;
this.id=this.id!=""?this.id:this.widgetId;
this.defaultUrl=this.url;
if(this.gridHeight>0){
_5.set(this._gridNode,"height",this.gridHeight+"px");
}
this.query={id:"*"};
this.pageControl.start=(this.pageControl.page-1)*this.pageControl.count+1;
this.gotoPage(1);
},refresh:function(url,_15){
console.log("grid refresh is start");
if(url&&url!=""){
this.url=url;
}
if(_15){
console.log("change refresh content...");
this.pageControl.page=1;
this.gotoPage(this.pageControl.page,_15);
}else{
this.gotoPage(this.pageControl.page);
}
console.log("grid refresh is end");
},getGrid:function(){
return this.grid;
},getStore:function(){
return this.store;
},getSelection:function(){
return this.grid.selection;
},getSelected:function(){
return this.grid.selection.getSelected();
},clearSelected:function(){
this.grid.selection.clear();
},onRowDblClick:function(){
console.log("onRowDblClick");
},onCellClick:function(){
console.log("onCellClick");
},_gotoPage:function(_16){
var _17={refreshHeader:false,refreshPageControl:false,refreshData:true};
this.gotoPage(_16,false,_17);
},gotoPage:function(_18){
var _19={};
if(this.showPageControl){
_19.showAllData=false;
_5.set(this.loadingimg,"display","inline");
this.pageControl.page=_18;
this.pageControl.start=(this.pageControl.page-1)*this.pageControl.count+1;
_19.showPageNum=this.pageControl.page;
_19.perPageNum=this.pageControl.count;
this._renderLink();
}else{
_5.set(this.pageControl,"display","none");
_19.showAllData=true;
}
if(arguments[1]!=false){
this.urlContent=arguments[1];
}
if(arguments[2]==undefined){
_19.refreshHeader=this.refreshHeader;
_19.refreshData=this.refreshData;
_19.refreshPageControl=this.refreshPageControl;
}else{
if(_19.refreshHeader==undefined){
_19.refreshHeader=this.refreshHeader;
}
if(_19.refreshData==undefined){
_19.refreshData=this.refreshData;
}
if(_19.refreshPageControl==undefined){
_19.refreshPageControl=this.refreshPageControl;
}
}
if(this.urlContent!=null){
_3.mixin(_19,this.urlContent);
}
var _1a={url:this.url,handleAs:"json",preventCache:true,content:_19,encoding:"utf-8",load:_3.hitch(this,function(_1b,_1c){
this._parseData(_1b);
_8.publish("closeUnderlay",[this]);
}),error:_3.hitch(this,function(_1d,_1e){
_11.errordeal(this.containerNode,"无法初始化表格内容数据...");
this.onDownloadError(_1d);
})};
_4.post(_1a);
},onDownloadError:function(_1f){
},_parseData:function(_20){
if(_20.gridHeader){
this._parseGridHeader(_20.gridHeader);
}
if(_20.gridData){
this._praseGridData(_20.gridData);
}
if(_20.pageControl){
this._prasePageControl(_20.pageControl);
}
},_prasePageControl:function(_21){
if(!_21||!_21.total){
return;
}
this.pageControl.total=parseInt(_21.total);
if(this.pageControl.total%this.pageControl.count==0){
this.pageControl.totalpages=this.pageControl.total/this.pageControl.count;
}else{
this.pageControl.totalpages=parseInt(this.pageControl.total/this.pageControl.count)+1;
}
this._renderLink();
this.totalCount.innerHTML=this.pageControl.total;
this.totalPages.innerHTML=this.pageControl.totalpages;
this.gotopage.setAttribute("value",this.pageControl.page);
if(this.showPageControl){
_5.set(this.loadingimg,"display","none");
}
},_checkMobile:function(){
var ua=navigator.userAgent.toLowerCase();
if(ua.indexOf("windows nt")!=-1){
return false;
}else{
return true;
}
},_parseGridHeader:function(_22){
var _23=this._checkMobile();
_2.forEach(_22,function(_24){
if(_24.formatter){
_24.formatter=eval(_24.formatter);
}
if(_23&&_24.width=="auto"){
_24.width="180px";
}
});
if(this.showRowSelector=="new"){
var _25=[];
_25.push({type:"dojox.grid._CheckBoxSelector"});
var _26={};
_26.cells=[];
_26.cells.push(_22);
_25.push(_26);
this.structure=_25;
}else{
this.structure=_22;
}
},_praseGridData:function(_27){
this.store=new _7({data:_27});
if(this.grid==null){
var _28={store:this.store,structure:this.structure,query:this.query,canSort:_3.hitch(this,this.sortFunc),autoRender:true,autoHeight:true,loadingMessage:"系统正在载入数据，请稍候！"};
if(this.showRowSelector=="true"){
_28.rowSelector=this.rowSelector;
}
if(this.autoGridRow!=-1){
_28.autoHeight=this.autoGridRow;
}
this.grid=new _10(_28,this._gridNode);
this.grid.startup();
this.connectArray.push(_8.connect(this.grid,"onRowDblClick",this,"onRowDblClick"));
this.connectArray.push(_8.connect(this.grid,"onCellClick",this,"onCellClick"));
}else{
this.grid.setStore(this.store);
this.grid.selection.clear();
this.resize();
}
},destroyConnect:function(){
_2.forEach(this.connectArray,_8.disconnect);
},_openLoading:function(){
_5.set(this._gridData,"display","none");
if(this._gridUtil==null){
this._gridUtil=new _13();
}
this._gridUtil.showWaitDialog_1(this.containerNode,"rosten_gridDialog");
},_closeLoading:function(){
_5.set(this._gridData,"display","block");
if(this._gridUtil==null){
this._gridUtil=new _13();
}
this._gridUtil.hideWaitDialog_1("rosten_gridDialog");
},sortFunc:function(idx){
return this.sortStatus;
},_resize:function(){
if(this._resTimer){
clearTimeout(this._resTimer);
this._resTimer=null;
}
this._resTimer=setTimeout(_3.hitch(this,this.resize),10);
},resize:function(){
if(this.gridHeight>0){
_5.set(this._gridNode,"height",this.gridHeight+"px");
}
if(this.grid){
this.grid.resize();
}
},pageKeyPress:function(_29){
if(_29.keyCode==13){
var _2a=_9.format(this.gotopage.setAttribute("value"));
if(_2a!=null){
if(_2a>this.pageControl.totalpages){
_2a=this.pageControl.totalpages;
}
if(_2a<1){
_2a=1;
}
this.pageControl.page=parseInt(_2a);
this._gotoPage(this.pageControl.page);
}else{
this.gotopage.setAttribute("value",this.pageControl.page);
_12.alert("请正确输入页码数字！");
}
}
},prevPage:function(){
console.log("prev Page!");
if(this.pageControl.page>1){
this.pageControl.page=this.pageControl.page-1;
this._gotoPage(this.pageControl.page);
}
},nextPage:function(){
console.log("next Page!");
if(this.pageControl.page<this.pageControl.totalpages){
this.pageControl.page=this.pageControl.page+1;
this._gotoPage(this.pageControl.page);
}
},firstPage:function(){
console.log("first Page!");
if(this.pageControl.page>1){
this.pageControl.page=1;
this._gotoPage(1);
}
},lastPage:function(){
console.log("last Page!");
if(this.pageControl.totalpages>this.pageControl.page){
this.pageControl.page=this.pageControl.totalpages;
this._gotoPage(this.pageControl.page);
}
},_renderLink:function(){
if(this.pageControl.page==1){
_6.remove(this.firstButton,"firstpagelink");
_6.add(this.firstButton,"firstpagelinkdisabled");
_6.remove(this.prevButton,"prevpagelink");
_6.add(this.prevButton,"prevpagelinkdisabled");
}else{
_6.remove(this.firstButton,"firstpagelinkdisabled");
_6.add(this.firstButton,"firstpagelink");
_6.remove(this.prevButton,"prevpagelinkdisabled");
_6.add(this.prevButton,"prevpagelink");
}
if(this.pageControl.page>=this.pageControl.totalpages){
_6.remove(this.lastButton,"lastpagelink");
_6.add(this.lastButton,"lastpagelinkdisabled");
_6.remove(this.nextButton,"nextpagelink");
_6.add(this.nextButton,"nextpagelinkdisabled");
}else{
_6.remove(this.lastButton,"lastpagelinkdisabled");
_6.add(this.lastButton,"lastpagelink");
_6.remove(this.nextButton,"nextpagelinkdisabled");
_6.add(this.nextButton,"nextpagelink");
}
}});
});

