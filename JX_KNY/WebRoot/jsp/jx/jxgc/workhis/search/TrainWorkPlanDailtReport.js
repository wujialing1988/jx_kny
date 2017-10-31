Ext.onReady(function(){
	Ext.namespace('TrainWorkPlanDailtReport');                       //定义命名空间
	TrainWorkPlanDailtReport.time = new Date(new Date().setDate(new Date().getDate()));;//前一天或者后一天传递参数适用
	TrainWorkPlanDailtReport.showTime;
	
	var html = "<br>";
	html += "<br>";
	html += "<h1 id='searchTime' style='color:blue; text-align:center;font-size:30px'></h1>";
	html += "<br>";
	html += "<br>";
	
	
	/*** 提票明细列表***/
	TrainWorkPlanDailtReport.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/trainWorkPlan!searchTrainWorkPlanDailtReportInfo.action',//装载列表数据的请求URL
	    singleSelect: true,
	    page: false,
	    viewConfig: null,
	    tbar: [{
	        xtype:"label", text:"日期：" 
	    },{
        	id:"searchDate",name: "searchDate",  xtype: "my97date",format: "Y-m-d",width: 100
	    },'-',{
	    	text: "搜索", iconCls:"searchIcon", handler: function(){
	    		TrainWorkPlanDailtReport.time = Ext.getCmp("searchDate").getValue();
				TrainWorkPlanDailtReport.grid.store.load();
	    	}
	    },'-',{
	        text: "前一天", iconCls: "searchIcon", handler: function(){
	        	//var currentDay = new Date();//获取当前日
				var time = new Date(TrainWorkPlanDailtReport.time.setDate(TrainWorkPlanDailtReport.time.getDate() - 1));
	        	TrainWorkPlanDailtReport.time = time;
				//TrainWorkPlanDailtReport.grid.store.load();
				
				getTimeTrainWokrPlanDailt(TrainWorkPlanDailtReport.grid,time);
	    	}
	    },'-',{
	        text: "今天", iconCls: "searchIcon", handler: function(){
	        	var currentDay = new Date();//获取当前日
				var time = new Date(currentDay.setDate(currentDay.getDate()));
	        	TrainWorkPlanDailtReport.time = time;
				//TrainWorkPlanDailtReport.grid.store.load();
				
				getTimeTrainWokrPlanDailt(TrainWorkPlanDailtReport.grid,time);
	    	}
	    },'-',{
	        text: "后一天", iconCls: "searchIcon", handler: function(){
	        	//var currentDay = new Date();//获取当前日
	        	var time = new Date(TrainWorkPlanDailtReport.time.setDate(TrainWorkPlanDailtReport.time.getDate() + 1));
				TrainWorkPlanDailtReport.time = time;
				//TrainWorkPlanDailtReport.grid.store.load();
				
				getTimeTrainWokrPlanDailt(TrainWorkPlanDailtReport.grid,time);
	    	}
	    },'-',{   
            text:"打印", iconCls:"printerIcon", handler: function(){
            	var searchDate_v ;
                var searchDate ;
            	if(TrainWorkPlanDailtReport.time){
					searchDate_v = TrainWorkPlanDailtReport.time.format('m/d/Y');
					searchDate = TrainWorkPlanDailtReport.time.format('Y-m-d');
				}else{
					searchDate_v = Ext.getCmp("searchDate").getValue().format('m/d/Y');
					searchDate = Ext.getCmp("searchDate").getValue().format('Y-m-d');
				}
                var tomorrowDate = new Date(searchDate_v).add(Date.DAY, 1).format('Y-m-d');
				var reportUrl = "/jxgc/TrainWorkPlanDailtReport.cpt?ctx=" + ctx.substring(1);
				var dataUrl = reportUrl + "&searchDate=" + searchDate + "&tomorrowDate=" + tomorrowDate ;
                window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI("机车检修动态"));
            } 
        },'-',{
	            text: "EXCEL", iconCls: "resetIcon", handler: function(){ 
	            	var url = ctx + "/trainWorkPlan!exportTrainWorkPlanDTOListByParm.action";
	            	var params = [];
	                var grid = TrainWorkPlanDailtReport.grid;
	                
			        var searchDate = Ext.getCmp("searchDate").getValue();
					if(TrainWorkPlanDailtReport.time){
		                params.push({name : "searchDate",value : TrainWorkPlanDailtReport.time.format('Y-m-d')});
					}else{
		                params.push({name : "searchDate",value : searchDate.format('Y-m-d')});
					}
	                
	                params.push(getPatterns(grid,"patterns"));
	            	exportExcel(url,params);
		         }
			}],
		fields: [{
			header:'机车检修计划主键idx', dataIndex:'idx', hidden:true
		},{
			header:'车型车号', dataIndex:'trainTypeAndNo',
			renderer :function(a,b,c,d){
				// 与chengr接口对接的传入参数
				var args = [];
				args.push(c.data.idx);
				args.push(c.data.trainTypeShortName + "|" + c.data.trainNo);
				var xcxc = c.data.repairClassName;
				if (!Ext.isEmpty(c.data.repairtimeName))
					xcxc+= "|" + c.data.repairtimeName;
				args.push(xcxc);
				args.push(c.data.planBeginTime);
				args.push(c.data.planEndTime);
			
				return "<a href='#' class='train_pic_bg' onclick='TrainWorkPlanWin.showWin(\""+ args.join(",") + "\")'>" + a + "</a>";
			}
		},{
			header:'修程修次', dataIndex:'repairClassNameAndTime',width: 70
		},{
			header:'配属段', dataIndex:'deserveName'
		},{
			header:'委修段', dataIndex:'delegateDName'
		},{
			header:'入段时间', dataIndex:'inTime'
		},{
			header:'检修开始时间', dataIndex:'beginTime'
		},{
			header:'今日计划', dataIndex:'nodeName',width: 180
		},{
			header:'当前工位', dataIndex:'workStationName',width: 180
		},{
			header:'明日计划', dataIndex:'tomorrowNodeName',width: 180
		},{
			header:'计划停时', dataIndex:'planStateTime'
		},{
			header:'实际停时', dataIndex:'realStateTime',
			renderer :function(a,b,c,d){
				//如果实际停时比计划停时大，显示红色
				if(parseInt(c.data.planStateTime) < parseInt(a)){
					return "<font color='red'>"+a+"</font>";
				}else{
					return a;
				}
			}
		},{
			header:'车型拼音码', dataIndex:'trainTypeShortName', hidden:true
		},{
			header:'车号', dataIndex:'trainNo', hidden:true
		},{
			header:'修程名称', dataIndex:'repairClassName', hidden:true
		},{
			header:'修次名称', dataIndex:'repairtimeName', hidden:true
		},{
			header:'机车检修计划开始时间', dataIndex:'planBeginTime', hidden:true
		},{
			header:'机车检修计划完成时间', dataIndex:'planEndTime', hidden:true
		},{
			header:'停时图', dataIndex:'idx', width: 60,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var planBeginTime = new Date( record.get('planBeginTime')).format('Y-m-d');
	     		var planEndTime = new Date(record.get('planEndTime')).format('Y-m-d');
	     		var trainTypeAndNo =  record.get('trainTypeAndNo');
				var args = [value,planBeginTime,planEndTime,trainTypeAndNo].join(',');	
	  		    return "<img src='" + diaImg + "' alt='停时图' style='cursor:pointer' onclick='TrainWorkStopAnasys.showStopAnasys(\""+ args +"\")'/>";
			},searcher:{disabled:true}
		}],
		//不需要双击显示
		toEditFn : function(){}
	});
	
	// 表格数据加载前的参数设置
	TrainWorkPlanDailtReport.grid.store.on('beforeload', function(){
		var searchDate = Ext.getCmp("searchDate").getValue();
		if(TrainWorkPlanDailtReport.time){
			this.baseParams.searchDate = TrainWorkPlanDailtReport.time.format('Y-m-d');
			TrainWorkPlanDailtReport.showTime = TrainWorkPlanDailtReport.time;
		}else{
			this.baseParams.searchDate = searchDate.format('Y-m-d');
			TrainWorkPlanDailtReport.showTime = searchDate;
		}
		
		//获取年
		var year = TrainWorkPlanDailtReport.showTime.getFullYear();
		//获取月
		var mouth = TrainWorkPlanDailtReport.showTime.getMonth() + 1;
		//获取日
		var day = TrainWorkPlanDailtReport.showTime.getDate();
		
		Ext.get("searchTime").dom.innerHTML = year + "年" + mouth + "月" + day + "日" + "&nbsp&nbsp&nbsp" + "机车检修动态";
	});	
	
	/*** 界面布局 start ***/
	TrainWorkPlanDailtReport.panel = {
	    xtype: "panel", layout: "border", 
	    items: [{
	        region: 'north',
	        html : html
	        
	    },{
	        region : 'center', layout : 'fit', bodyBorder: false, items : [ TrainWorkPlanDailtReport.grid ]
	    }]
	};

	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items: TrainWorkPlanDailtReport.panel });
});

//根据grid和时间显示对应时间的检修日报
function getTimeTrainWokrPlanDailt(grid,time){
    TrainWorkPlanDailtReport.time = time;
	grid.store.load();
};