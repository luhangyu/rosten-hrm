define(["dojo/_base/declare","dojo/_base/kernel","dojo/_base/lang","dojo/_base/xhr","dojo/dom-style","dojo/dom-class","dojo/data/ItemFileWriteStore","dojo/_base/connect","dojo/number","dijit/_WidgetBase","dijit/_TemplatedMixin","dijit/_WidgetsInTemplateMixin","dojo/text!./templates/RostenGrid.html","dijit/form/TextBox","dojox/collections/SortedList","dojox/grid/_CheckBoxSelector","dojox/grid/DataGrid","rosten/kernel/_kernel","rosten/kernel/behavior","rosten/util/gen-dialog","rosten/util/general"],function(_1,_2,_3,_4,_5,_6,_7,_8,_9,_a,_b,_c,_d,_e,_f,_10,_11,_12,_13,_14,_15){
return _1("rosten.widget.RostenGrid",[_a,_b,_c],{templateString:_d,id:"",url:"",defaultUrl:"",urlContent:null,store:null,structure:null,grid:null,query:"",sortStatus:false,showRowSelector:"false",rowSelector:"20px",gridHeight:-1,autoGridRow:-1,pageSize:15,showPageControl:true,emptymsg:"目前暂无数据！",imgSrc:"images/rosten/share/wait.gif",pageControl:{page:1,total:0,totalpages:0,start:0,count:0},connectArray:[],_gridUtil:null,constructor:function(){
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
},refresh:function(url,_16){
console.log("grid refresh is start");
if(url&&url!=""){
this.url=url;
}
if(_16){
console.log("change refresh content...");
this.pageControl.page=1;
this.gotoPage(this.pageControl.page,_16);
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
},_gotoPage:function(_17){
var _18={refreshHeader:false,refreshPageControl:false,refreshData:true};
this.gotoPage(_17,false,_18);
},gotoPage:function(_19){
var _1a={};
if(this.showPageControl){
_1a.showAllData=false;
_5.set(this.loadingimg,"display","inline");
this.pageControl.page=_19;
this.pageControl.start=(this.pageControl.page-1)*this.pageControl.count+1;
_1a.showPageNum=this.pageControl.page;
_1a.perPageNum=this.pageControl.count;
this._renderLink();
}else{
_5.set(this.pageControl,"display","none");
_1a.showAllData=true;
}
if(arguments[1]!=false){
this.urlContent=arguments[1];
}
if(arguments[2]==undefined){
_1a.refreshHeader=this.refreshHeader;
_1a.refreshData=this.refreshData;
_1a.refreshPageControl=this.refreshPageControl;
}else{
if(_1a.refreshHeader==undefined){
_1a.refreshHeader=this.refreshHeader;
}
if(_1a.refreshData==undefined){
_1a.refreshData=this.refreshData;
}
if(_1a.refreshPageControl==undefined){
_1a.refreshPageControl=this.refreshPageControl;
}
}
if(this.urlContent!=null){
_3.mixin(_1a,this.urlContent);
}
var _1b={url:this.url,handleAs:"json",preventCache:true,content:_1a,encoding:"utf-8",load:_3.hitch(this,function(_1c,_1d){
this._parseData(_1c);
_8.publish("closeUnderlay",[this]);
}),error:_3.hitch(this,function(_1e,_1f){
_12.errordeal(this.containerNode,"无法初始化表格内容数据...");
this.onDownloadError(_1e);
})};
_4.post(_1b);
},onDownloadError:function(_20){
},_parseData:function(_21){
if(_21.gridHeader){
this._parseGridHeader(_21.gridHeader);
}
if(_21.gridData){
this._praseGridData(_21.gridData);
}
if(_21.pageControl){
this._prasePageControl(_21.pageControl);
}
},_prasePageControl:function(_22){
if(!_22||!_22.total){
return;
}
this.pageControl.total=parseInt(_22.total);
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
},_parseGridHeader:function(_23){
var _24=this._checkMobile();
_2.forEach(_23,function(_25){
if(_25.formatter){
_25.formatter=eval(_25.formatter);
}
if(_24&&_25.width=="auto"){
_25.width="180px";
}
});
if(this.showRowSelector=="new"){
var _26=[];
_26.push({type:"dojox.grid._CheckBoxSelector"});
var _27={};
_27.cells=[];
_27.cells.push(_23);
_26.push(_27);
this.structure=_26;
}else{
this.structure=_23;
}
},_praseGridData:function(_28){
this.store=new _7({data:_28});
if(this.grid==null){
var _29={store:this.store,structure:this.structure,query:this.query,canSort:_3.hitch(this,this.sortFunc),autoRender:true,autoHeight:true,loadingMessage:"系统正在载入数据，请稍候！"};
if(this.showRowSelector=="true"){
_29.rowSelector=this.rowSelector;
}
if(this.autoGridRow!=-1){
_29.autoHeight=this.autoGridRow;
}
this.grid=new _11(_29,this._gridNode);
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
this._gridUtil=new _14();
}
this._gridUtil.showWaitDialog_1(this.containerNode,"rosten_gridDialog");
},_closeLoading:function(){
_5.set(this._gridData,"display","block");
if(this._gridUtil==null){
this._gridUtil=new _14();
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
},pageKeyPress:function(_2a){
if(_2a.keyCode==13){
var _2b=_9.format(this.gotopage.get("value"));
if(_2b!=null){
if(_2b>this.pageControl.totalpages){
_2b=this.pageControl.totalpages;
}
if(_2b<1){
_2b=1;
}
this.pageControl.page=parseInt(_2b);
this._gotoPage(this.pageControl.page);
}else{
this.gotopage.setAttribute("value",this.pageControl.page);
_13.alert("请正确输入页码数字！");
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

