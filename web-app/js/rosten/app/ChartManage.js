/**
 * @author rosten
 */
define([ "dijit/registry",
         "dojo/_base/connect",
         "dojo/_base/lang", 
      	"dojo/data/ItemFileWriteStore",
      	"dojox/charting/Chart",
      	"dojox/charting/DataSeries",
      	"dojox/charting/themes/ThreeD",
      	"dojox/charting/widget/Legend",
      	"dojox/charting/axis2d/Default",
      	"dojox/charting/action2d/Tooltip",
      	"dojox/charting/action2d/MoveSlice",
      	"dojox/charting/plot2d/Pie"], function(
  			registry,connect,lang,ItemFileWriteStore,Chart,DataSeries,ThreeD,Legend,Default,Tooltip,MoveSlice,Pie) {
	
	var chartObject = {};
	
	chartObject.addAskForChart = function(){
		var companyId = rosten.kernel.getUserInforByKey("companyid");
		var store = new ItemFileWriteStore({url:rosten.webPath + "/vacate/getAskForChartData/" + companyId});
		
        var chartP = new Chart("askFor_pie",{title: "承保运营部"});
        chartP.setTheme(ThreeD);
        chartP.addPlot("default", {type: Pie, radius: 80});
        chartP.addSeries("number", new DataSeries(store, {query: {id: "*"}},lang.hitch(null, chartObject.valTrans, "number")));
        chartP.render();
        
        new Tooltip(chartP);
		new MoveSlice(chartP);
        
//		chartObject.addLegend(chartP, "askFor_pie_legend");
	}
	chartObject.addLegend = function(chart, node){
        var legend = new Legend({chart: chart}, node);
        connect.connect(chart, "render", legend, "refresh");
    }
	chartObject.valTrans = function(value, store, item){
        return {
            y: store.getValue(item, value),
            text:store.getValue(item,"name"),
            tooltip: "总天数:" + store.getValue(item, value)
        };
    };
    return chartObject;
});
