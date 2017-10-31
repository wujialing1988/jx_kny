//TODO 已作废，待删除
Ext.onReady(function(){
	Ext.namespace('PartsQR');                       //定义命名空间
	PartsQR.searchParam = {} ;                    //定义查询条件
	//查看列表界面
	PartsQR.gridView = new Ext.yunda.Grid({
	    loadURL: ctx + '/workSeq!findWorkStationForWorkSeq.action',                 //装载列表数据的请求URL
	    storeAutoLoad:false,  //是否自动加载store
	    searchFormColNum: 2,
	    storeId:'workStationSetIDX', 
	    viewConfig: null,
	    singleSelect: true,
	    tbar:[{
	    	text:"刷新",iconCls:"refreshIcon" ,handler: function(){
	    		PartsQR.gridView.store.reload();
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
			header:'编号', dataIndex:'workSeqCode',width:100, editor:{ maxLength:50 , disabled: true, allowBlank: false }
		},{
			header:'配件型号', dataIndex:'pPartsTypeIDX', hidden: true,editor:{}
		},{
			header:'规格型号', dataIndex:'pSpecificationModel', editor:{xtype: "hidden" },
			searcher:{xtype: 'textfield'}, width: 100
		},{
			header:'配件名称', dataIndex:'pPartsName', editor:{disabled: true },
			searcher:{xtype: 'textfield'}, width: 90
		},{
			header:'检修工序', dataIndex:'workSeqName',width:200, editor:{ allowBlank:false, maxLength:50 }
		},{
			header:'额定工时(分)', dataIndex:'ratedWorkHours', hidden: true, editor:{ maxLength:8 },searcher: {disabled: true}
		},{
			header:'组成型号主键', dataIndex:'buildUpTypeIDX',hidden:true, editor:{ xtype:'hidden', maxLength:50 }
		},{
			header:'零部件名称', dataIndex:'buildUpTypeName',editor:{  disabled: true  }
		},{
			header:'零部件型号', dataIndex:'buildUpTypeCode', editor:{}, searcher: { xtype: 'textfield'}
		},{
			header:'零部件图号', dataIndex:'chartNo', width: 130,editor:{ disabled: true  }
		},{
			header:'分类', dataIndex:'workClass',width:90, editor:{
			},renderer: function(v){ return EosDictEntry.getDictname('JXGC_WORKSEQ_WORKCLASS',v)},searcher: {disabled: true}
		},{
			header:'检修类型', dataIndex:'workSeqType', editor:{},
			renderer: function(v){ return EosDictEntry.getDictname('JXGC_WORK_STEP_REPAIR_TYPE',v)},searcher: {disabled: true}
		},{
			header:'描述', dataIndex:'repairScope', hidden: true, editor:{ xtype:'textarea',  maxLength:1000 },searcher: {disabled: true}
		},{
			header:'安全注意事项', dataIndex:'safeAnnouncements',hidden:true, editor:{ xtype:'textarea', maxLength:1000 }
		}]
	});
	
	//grid查询过滤
	PartsQR.gridView.store.on("beforeload", function(){	
		var searchParam = {} ;
		searchParam.workStationIDX = WorkStation.idx; //过滤工位
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	
	PartsQR.gridView.un('rowdblclick', PartsQR.gridView.toEditFn, PartsQR.gridView); //取消编辑监听
});