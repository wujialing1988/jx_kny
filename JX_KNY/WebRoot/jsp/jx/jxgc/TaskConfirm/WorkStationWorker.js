/**
 * 工位作业人员 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('WorkStationWorker');                       //定义命名空间
WorkStationWorker.searchParams = {};                     //全局查询参数集

//作业人员列表
WorkStationWorker.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/workStationWorker!pageList.action',                 //装载列表数据的请求URL
    storeAutoLoad: false,
    singleSelect: true,
    tbar: [{
	    	text:"刷新",iconCls:"refreshIcon" ,handler: function(){
	    		WorkStationWorker.grid.store.reload();
	    	}
	    },{
    	text:"关闭", iconCls:"closeIcon", handler:function(){
    		WorkStation.grid.saveWin.hide();
    	}
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'工位主键', dataIndex:'workStationIDX', hidden:true, editor:{  xtype:'hidden' }
	},{
		header:'工位名称', dataIndex:'workStationName', hidden:true, editor:{  xtype:'hidden' }
	},{
		header:'工作人员ID', dataIndex:'workerID', hidden:true, editor:{ xtype:'hidden' }
	},{
		header:'人员编码', dataIndex:'workerCode', editor:{  }
	},{
		header:'人员名称', dataIndex:'workerName', editor:{  }
	},{
		header:'状态', dataIndex:'status', hidden: true,
		editor:{ xtype:'hidden', value: workerStatus_new }
	}]
});

WorkStationWorker.grid.un('rowdblclick', WorkStationWorker.grid.toEditFn, WorkStationWorker.grid);//移除侦听器
WorkStationWorker.grid.store.on("beforeload", function(){
	WorkStationWorker.searchParams.workStationIDX = WorkStation.idx;
	this.baseParams.entityJson = Ext.util.JSON.encode(WorkStationWorker.searchParams);
});
});