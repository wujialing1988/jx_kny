/**
 * 车辆状态变化历史
 */
Ext.onReady(function() {

	Ext.ns('ZtgzRecord');
	
	ZtgzRecord.trainTypeIDX = "###" ; // 车型主键
	
	ZtgzRecord.trainNo = "###" ; // 车号

	ZtgzRecord.parentNodes = new vis.DataSet(); // 状态变化历史

	ZtgzRecord.edges = new vis.DataSet();

	ZtgzRecord.network = null; // 时间轴network对象

	ZtgzRecord.options = {
		  	autoResize: true,
		  	height: '100%',
		  	width: '100%',
			interaction:{
			    dragNodes:false, // 是否可移动节点
			    dragView: true // 是否可移动整个图
			  },
			nodes:{
			  	    fixed: {
			      x:true,
			      y:true
			    },
			    size:10,
			    shape:'circle'
		  	},
		  	edges:{
		  		arrows: {
			      to:     {enabled: true, scaleFactor:1, type:'arrow'},
			      from:   {enabled: false, scaleFactor:1, type:'arrow'}
			    },
		  		smooth:false
		  	}
	};

	ZtgzRecord.data = {
		nodes : ZtgzRecord.parentNodes,
		edges : ZtgzRecord.edges
	};

	/**
	 * 列检计划列表
	 */
	ZtgzRecord.Grid = new Ext.yunda.Grid({
				loadURL : ctx + "/trainStatusChange!pageList.action",
				singleSelect : false,
				isEdit:true,
				tbar : [
				],
				fields : [
				{header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }},
				{header:'车辆主键idx', dataIndex:'trainIdx', hidden:true, editor: { xtype:'hidden' }},
				{header:'客货类型', dataIndex:'vehicleType', hidden:true, editor: { xtype:'hidden' }},
				{header:'操作业务ID', dataIndex:'businessIdx', hidden:true, editor: { xtype:'hidden' }},
				{
					header:'车型', dataIndex:'trainTypeShortname',sortable:false,
					editor:{xtype: "hidden"},searcher:{disabled: true}
				},
				{
					header:'车号', dataIndex:'trainNo',sortable:false,
					editor:{xtype: "hidden"},searcher:{disabled: true}
				},
				{
					header:'状态更改时间', dataIndex:'recordTime', xtype:'datecolumn',format:'Y-m-d H:i',sortable:false,
					editor:{xtype: "hidden"},searcher:{disabled: true}
				},
				{
					header:'操作类型', dataIndex:'businessName',sortable:false,
					editor:{xtype: "hidden"},searcher:{disabled: true}
				},
				{
					header:'车辆状态', dataIndex:'trainState',sortable:false,renderer: function(value, metaData, record, rowIndex, colIndex, store) {
						var trainState = record.get('trainState');
						return ZtgzRecord.getTrainStateName(trainState);
					},
					editor:{xtype: "hidden"},searcher:{disabled: true}
				}				
				],
				beforeShowEditWin: function(record, rowIndex){  
					return false;
				}
			});

	// 默认排序		
	ZtgzRecord.Grid.store.setDefaultSort("recordTime", "ASC");
			
	// 数据加载前
	ZtgzRecord.Grid.store.on('beforeload', function() {
		var searchParams = {};
		searchParams.vehicleType = vehicleType;
		searchParams.trainTypeIdx = ZtgzRecord.trainTypeIDX;
		searchParams.trainNo = ZtgzRecord.trainNo;
		searchParams = MyJson.deleteBlankProp(searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});

	// 添加加载结束事件
	ZtgzRecord.Grid.getStore().addListener('load',
			function(me, records, options) {
				if (records.length > 0) {
					ZtgzRecord.setNetwork(records);
				}else{
					// 清空节点
					ZtgzRecord.parentNodes.clear();
					ZtgzRecord.edges.clear();
				}
			});

	/**
	 * 设置车辆图
	 */
	ZtgzRecord.setNetwork = function(records) {
		
		// 清空节点
		ZtgzRecord.parentNodes.clear();
		ZtgzRecord.edges.clear();
		
		// 添加车辆
		for (var k = 0; k < records.length; k++) {
			var record = records[k];
			var trainState = Ext.isEmpty(record.data.trainState) ? "" : record.data.trainState ; // 状态
			var businessName = Ext.isEmpty(record.data.businessName) ? "" : record.data.businessName ; // 操作类型
			var trainStateName = ZtgzRecord.getTrainStateName(trainState) ;
			var recordTimeStr = Ext.isEmpty(record.data.recordTime) ? "" :new Date(record.data.recordTime).format('Y-m-d H:i');
			var title = "<span style:'color:red;'>操作时间："+recordTimeStr+"</span><br/><span>操作类型："+businessName+"</span>"
			var trainStateColor = ZtgzRecord.getTrainStateColor(trainState);
			
			ZtgzRecord.parentNodes.add({
						id : record.data.idx,
						label : trainStateName,
						size : 50,
						x : 0 + k*200,
						y : 50,
						color:trainStateColor,
						value : record.data.idx,
						title:title,
						font : {
							size : 10,
							strokeWidth : 3,
							'face' : 'Monospace',
							align : 'left'
						}
					});
					
			// 设置连线
			if(k != records.length - 1){
				ZtgzRecord.edges.add({
					from: record.data.idx, 
					to: records[k+1].data.idx,
					label:records[k+1].data.businessName
				});
			}
		}
		// 创建实例
		if(!ZtgzRecord.network){
			ZtgzRecord.network = new vis.Network(document.getElementById('visualization'),
					ZtgzRecord.data, ZtgzRecord.options);
		}
	}
	
	/**
	 * vis图
	 */
	ZtgzRecord.ZtgzRecordVis = new Ext.Panel({
				region : 'center',
				border : false,
				autoScroll : false,
				anchor : '100%',
				tbar : ['->', '图例：',{
							xtype : 'label',
							text : '运行',
							style : 'font-weight:bold;',
							cls : 'status_example status_example_yx_zb'
						}, '-', {
							xtype : 'label',
							text : '列检',
							style : 'font-weight:bold;',
							cls : 'status_example status_example_lj_zb'
						}, '-', {
							xtype : 'label',
							text : '检修',
							style : 'font-weight:bold;',
							cls : 'status_example status_example_jx_zb'
						},'-', {
							xtype : 'label',
							text : '扣车',
							style : 'font-weight:bold;',
							cls : 'status_example status_example_kc_zb'
						}],
				html : [
						'<div style="background-color:white" id= "visualization">',
						'</div>'].join(""),
				listeners : {
					afterrender : function(window) {

					}
				}
			});
			
	/**
	 * 车辆状态获取
	 */
	ZtgzRecord.getTrainStateName = function(trainState){
			switch(trainState){
				case trainStateRepair:
					return "检修";
				case trainStateUse:
					return "运用";
				case trainStateSpare:
					return "列检";
				case trainStateDetain:
					return "扣车";					
				default:
					return "";
			}
	}	
	
	/**
	 * 车辆状态颜色获取
	 */
	ZtgzRecord.getTrainStateColor = function(trainState){
			switch(trainState){
				case trainStateRepair:
					return "#33CCCC";
				case trainStateUse:
					return "#99CC00";
				case trainStateSpare:
					return "#00BFFF";
				case trainStateDetain:
					return "#FF6600";					
				default:
					return "";
			}
	}		
});