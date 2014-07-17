<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>图表</title>
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
        height: 200px;
        margin:0 auto;
	}
	.chart {
		width:  800px;
		height: 168px;
	}
	
	.chart-area-pie {
		/*border: 1px solid #ccc;*/
        height: 190px;
        width:400px;
        margin:0 auto;
	}
	.chart-pie {
		width:400px;
		height: 190px;
	}
	.chart-area-cols {
		/*border: 1px solid #ccc;*/
        height: 200px;
        width:500px;
        float:left;
	}
	.chart-cols {
		width:500px;
		height: 190px;
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
     	"dojo/data/ItemFileWriteStore",
     	"dojox/charting/Chart",
     	"dojox/charting/DataSeries",
     	"dojox/charting/themes/ThreeD",
     	"dojox/charting/widget/Legend",
     	"dojox/charting/axis2d/Default",
     	"dojox/charting/plot2d/Markers",
     	"dojox/charting/action2d/Tooltip",
     	"dojox/charting/action2d/Magnify",
     	"dojox/charting/plot2d/Grid",
     	"dojox/charting/action2d/MoveSlice",
     	"dojox/charting/plot2d/Pie",
     	"dojox/charting/action2d/Shake",
     	"dojox/charting/plot2d/ClusteredColumns",
     	"dojox/charting/plot2d/Columns"
     	],
	function( kernel,JSON,lang,query,domStyle,domClass,domConstruct,registry,ItemFileWriteStore,
		Chart,DataSeries,ThreeD,Legend,Default,Markers,Tooltip,Magnify,Grid,MoveSlice,Pie,Shake,ClusteredColumns,Columns
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
	        	title: "金额(万元)",
	        	titleGap: 20, 
	        	max:50,
	        	titleFontColor: "green",
	            titleOrientation: "axis",
	        	fixLower: "major", fixUpper: "major", natural: true,includeZero: true, vertical: true
	        	
	        	});
	        
	        // chartL.addSeries("技术中心", new DataSeries(store, {query: {depart :"a"}}, "price"));      
	        chartL.addSeries("技术中心",[
				{ x: 1, y: 20},{ x: 2, y: 30},{ x: 3, y: 40},{ x: 4, y: 35},{ x: 5, y: 30},{ x: 6, y: 26},
	        	{ x: 7, y: 29},{ x: 8, y: 30},{ x: 9, y: 35},{ x: 10, y: 20},{ x: 11, y: 25},{ x: 12, y: 40}
	        ]); 
	        chartL.addSeries("网络中心",[
	        	{ x: 1, y: 10},{ x: 2, y: 20},{ x: 3, y: 30},{ x: 4, y: 25},{ x: 5, y: 36},{ x: 6, y: 16},
	        	{ x: 7, y: 19},{ x: 8, y: 20},{ x: 9, y: 25},{ x: 10, y: 30},{ x: 11, y: 35},{ x: 12, y: 20}
	        ]);
	        chartL.addSeries("财务部",[
   	        	{ x: 1, y: 11},{ x: 2, y: 26},{ x: 3, y: 45},{ x: 4, y: 35},{ x: 5, y: 27},{ x: 6, y: 30},
	        	{ x: 7, y: 23},{ x: 8, y: 25},{ x: 9, y: 28},{ x: 10, y: 40},{ x: 11, y: 15},{ x: 12, y: 25}
   	        ]);
	        
	        new Magnify(chartL);
	        new Tooltip(chartL);
	        
	        chartL.render();
	        
	        addLegend(chartL, "lines_legend");

			//添加员工部门人数分布图
			var store = new ItemFileWriteStore({url: "${createLink(controller:'statistics',action:'getDepartUsers',id:company?.id)}"});
	        var chartP = new Chart("pie");
            chartP.setTheme(ThreeD);
            chartP.addPlot("default", {type: Pie, radius: 80});
            chartP.addSeries("Price", new DataSeries(store, {query: {id: "*"}}, {y: "number", text: "name", tooltip: "number"}));
            chartP.render();
            
            new Tooltip(chartP);
    		new MoveSlice(chartP);
            
   		 	addLegend(chartP, "pie_legend");

			//添加员工编制统计
			
   			//柱状图
   			var groupList = JSON.parse('${groupList}');
   			var departList = JSON.parse('${departList}');
   			
   			var store1 = new ItemFileWriteStore({url: "${createLink(controller:'statistics',action:'getDepartUsersByType',id:company?.id,params:[departIds:departIds,groupNames:groupNames])}"});
   			
	        var chartC = new Chart("cols");
            chartC.setTheme(ThreeD);
            chartC.addAxis("x", {
            	natural: true,
            	labelFunc: function(value){
            		return departList[value-1];
				}
            });
            chartC.addAxis("y", {vertical: true, fixLower: "major", fixUpper: "major", includeZero: true,
                title: "员工人数",
	        	titleGap: 20,
	        	titleFontColor: "green",
	            titleOrientation: "axis"
	        });
            chartC.addPlot("default", {type: ClusteredColumns,gap: 30, labels: true});

            for (var i = 0; i < groupList.length; i++) {
            	chartC.addSeries(groupList[i], new DataSeries(store1, {query: {group: groupList[i]}}, dojo.hitch(null, valTrans, "number")));
            }
            
            new Shake(chartC, "default", {shiftY: 0});
    		new Tooltip(chartC);
            
            chartC.render();
    		addLegend(chartC, "cols_legend");

			//增加员工职称统计
    		var pieChart3 = new Chart("pie1");
	        pieChart3.setTheme(ThreeD);
	        pieChart3.addPlot("default", {
	            type:       "Pie",
	        	labelStyle: "columns",
	        	omitLabels: true,
	        	radius:     80
	        });
	        pieChart3.addSeries("test",new DataSeries(store, {query: {id: "*"}}, "number"));
	        pieChart3.render();


    		
	    };
	    function valTrans(value, store, item){
	        return {
	            y: store.getValue(item, value),
	            tooltip: "员工人数:" + store.getValue(item, value)
	        };
	    };
		function addLegend(chart, node){
	        var legend = new Legend({chart: chart}, node);
	        dojo.connect(chart, "render", legend, "refresh");
	    }
	
	});
	
</script>
<body>
	<div data-dojo-type="rosten/widget/TitlePane" style="padding:0px"
		data-dojo-props='title:"年度部门工资金额统计    (2014年度)",toggleable:false,moreText:"",height:"200px",marginBottom:"2px"'>
		<div class="charts">
			<div class="chart-area">
	            <div id="lines_legend"></div>
				<div id="lines" class="chart"></div>
			</div>
			
		</div>
	</div>
	<div data-dojo-type="dijit/layout/BorderContainer" data-dojo-props='gutters:false,style:{height:"260px"}' >
		<div data-dojo-type="rosten/widget/TitlePane" style="margin-top:1px" 
			data-dojo-props='region:"left",title:"员工部门人数分布    (2014年度)",toggleable:false,
				height:"210px",width:"50%",style:{marginRight:"1px"},moreText:""'>
			<div class="charts">
				<div id="pie_legend"></div>
				<div class="chart-area-pie">
					<div id="pie" class="chart-pie"></div>
				</div>
			</div>	
				
		</div>
		<div data-dojo-type="rosten/widget/TitlePane"
			data-dojo-props='region:"center",title:"部门员工类型统计    (2014年度)",toggleable:false,
				height:"210px",moreText:""'>
			<div class="charts">
				<div id="cols_legend"></div>
				<div class="chart-area-cols">
					<div id="cols" class="chart-cols"></div>
				</div>
			</div>	
		</div>						
	</div>
	
	<div data-dojo-type="dijit/layout/BorderContainer" data-dojo-props='gutters:false,style:{height:"260px"}' >
		<div data-dojo-type="rosten/widget/TitlePane" style="margin-top:1px" 
			data-dojo-props='region:"left",title:"员工职称统计    (2014年度)",toggleable:false,
				height:"210px",width:"50%",style:{marginRight:"1px"},moreText:""'>
			<div class="charts">	
				<div class="chart-area-pie">
		            <div id="pie_legend1"></div>
					<div id="pie1" class="chart-pie"></div>
				</div>
			</div>	
		</div>
		<div data-dojo-type="rosten/widget/TitlePane"
			data-dojo-props='region:"center",title:"部门请假统计    (2014年度)",toggleable:false,
				height:"210px",moreText:""'>
		</div>						
	</div>
	
</body>
</html>
