<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>图标</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<style type="text/css">
	.dojoxLegendNode {border: 1px solid #ccc; margin: 5px 10px 0px;}
    .dojoxLegendText {vertical-align: text-top; padding-right: 10px}
	.charts {
		clear: both;
	}
	.chart-area {
		float: left;
		border: 1px solid #ccc;
		width:  800px;
        height: 220px;
        margin:0 auto;
	}
	.chart {
		width:  800px;
		height: 168px;
	}
</style>
<script>
	require([
     	"dojo/_base/kernel", 
     	"dojo/json",
     	"dojo/_base/lang",
     	"dojo/query",
     	"dojo/dom-style",
     	"dojo/dom-class",
     	"dojo/dom-construct",
     	"dijit/registry",
     	"dojox/charting/Chart",
     	"dojox/charting/DataSeries",
     	"dojox/charting/themes/ThreeD",
     	"dojox/charting/widget/Legend",
     	"dojox/charting/axis2d/Default",
     	"dojox/charting/plot2d/Markers",
     	"dojox/charting/action2d/Tooltip",
     	"dojox/charting/action2d/Magnify",
     	"dojox/charting/plot2d/Grid"
     	],
	function( kernel,JSON,lang,query,domStyle,domClass,domConstruct,registry,
		Chart,DataSeries,ThreeD,Legend,Default,Markers,Tooltip,Magnify,Grid
		) {
		kernel.addOnLoad(function() {
			makeCharts();
		});
		
		makeCharts = function(){
	        var chartL = new Chart("lines");
	        
	        chartL.addPlot("default", {type: Markers});
	        chartL.addPlot("grid", { type: "Grid" ,hMajorLines: true, vMajorLines: false});
	        
	        chartL.setTheme(ThreeD)
	        
	        chartL.addAxis("x", {
	        	//title: "2014年度",
	        	//titleGap: 20, 
	        	//titleFontColor: "green",
	            //titleOrientation: "away",
	            max:13,
	        	fixLower: "none", fixUpper: "none", natural: true,includeZero: true,
	        	labelFunc: function(value){
	        		if(value==0 ){
	        			return "";
	        		}else if(value ==13){
	        			return "2015年1月";
	        		}
					return value + "月";
					}
	        	});
	        	
	        chartL.addAxis("y", {
	        	title: "金额(元)",
	        	titleGap: 20, 
	        	max:20,
	        	titleFontColor: "green",
	            titleOrientation: "axis",
	        	fixLower: "major", fixUpper: "major", natural: true,includeZero: true, vertical: true
	        	
	        	});
	        
	        // chartL.addSeries("技术中心", new DataSeries(store, {query: {depart :"a"}}, "price"));      
	        chartL.addSeries("网络中心",[
	        	{ x: 7, y: 5},
	        	{ x: 8, y: 10}
	        ]); 
	        
	        new Magnify(chartL);
	        new Tooltip(chartL);
	        
	        chartL.render();
	        
	        addLegend(chartL, "lines_legend");
	    };
		function addLegend(chart, node){
	        var legend = new Legend({chart: chart}, node);
	        dojo.connect(chart, "render", legend, "refresh");
	    }
	
	});
	
</script>
<body>
	<div data-dojo-type="rosten/widget/TitlePane" style="padding:0px"
		data-dojo-props='title:"年度部门工资金额统计    (2014年度)",toggleable:false,moreText:"",height:"220px",marginBottom:"2px"'>
		<div class="charts">
			<div class="chart-area">
	            <div id="lines_legend"></div>
				<div id="lines" class="chart"></div>
			</div>
			
		</div>
	</div>
</body>
</html>
