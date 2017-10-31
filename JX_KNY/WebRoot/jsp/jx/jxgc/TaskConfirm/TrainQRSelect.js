//TODO 已作废，待删除
Ext.onReady(function(){
	Ext.namespace('TrainQR');                       //定义命名空间
	
	//查看列表界面
	TrainQR.gridView = new Ext.yunda.Grid({
	    loadURL: ctx + '/workSeq!findWorkStationForWorkSeq.action',                 //装载列表数据的请求URL
	    storeAutoLoad:false,  //是否自动加载store
	    searchFormColNum: 2,
	    storeId:'workStationSetIDX', 
	    viewConfig: null,
	    singleSelect: true,
	    tbar:[{
	    	text:"刷新",iconCls:"refreshIcon" ,handler: function(){
	    		TrainQR.gridView.store.reload();
	    	}
	    },{
	    	text:"关闭", iconCls:"closeIcon", handler:function(){
	    		WorkStation.grid.saveWin.hide();
	    	}
    	}],
		fields: [{
			header:'idx主键', dataIndex:'idx',hidden: true, editor: { xtype:'hidden' }
		},{
			header:'workStationSetIDX', dataIndex:'workStationSetIDX', hidden: true, editor: { xtype:'hidden' }
		},{
			header:'编号', dataIndex:'workSeqCode',width:120, editor:{  maxLength:50 , disabled: true, allowBlank: false }
		},{
			header:'检修车型', dataIndex:'pTrainTypeIDX', hidden: true, 	editor:{}	    
		},{
			header:'车型', dataIndex:'pTrainTypeShortName', editor:{ xtype: 'hidden' },
			searcher:{xtype: 'textfield'}, width: 60
		},{
			header:'检修工序', dataIndex:'workSeqName',width:200, editor:{ allowBlank:false, maxLength:50 }
		},
		/*{
			header:'额定工时(分)', dataIndex:'ratedWorkHours', hidden: true, editor:{ vtype:'nonNegativeInt', maxLength:8 },searcher: {disabled: true}
		},*/{
			header:'检修类型', dataIndex:'workSeqType', hidden: true, editor:{}
		},/*{
			header:'组成型号主键', dataIndex:'buildUpTypeIDX',hidden:true, editor:{ xtype:'hidden', maxLength:50 }
		},{
			header:'零部件名称', dataIndex:'buildUpTypeName', editor:{}
		},{
			header:'零部件型号', dataIndex:'buildUpTypeCode', editor:{},searcher: { xtype: 'textfield'}
		},{
			header:'零部件图号', dataIndex:'chartNo', width: 130,editor:{ }
		},*/{
			header:'分类', dataIndex:'workClass',width:90, editor:{},
				renderer: function(v){ return EosDictEntry.getDictname('JXGC_WORKSEQ_WORKCLASS',v)},
				searcher: {disabled: true}
		},{
			header:'描述', dataIndex:'repairScope',hidden:true, editor:{xtype:'textarea',  maxLength:1000 },searcher: {disabled: true}
		},{
			header:'安全注意事项', dataIndex:'safeAnnouncements',hidden:true, editor:{ xtype:'textarea', maxLength:1000 }
		}]
	});
	
	//grid查询过滤
	TrainQR.gridView.store.on("beforeload", function(){	
		var searchParam = {} ;
		searchParam.workStationIDX = WorkStation.idx; //过滤工位
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	
	TrainQR.gridView.un('rowdblclick', TrainQR.gridView.toEditFn, TrainQR.gridView); //取消编辑监听
	
});