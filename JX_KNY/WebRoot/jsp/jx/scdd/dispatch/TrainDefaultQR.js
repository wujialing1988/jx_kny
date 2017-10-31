//TODO 确定无用就删除
Ext.onReady(function(){
	Ext.namespace('TrainQR');                       //定义命名空间
	TrainQR.searchParam = {} ;                    //定义查询条件
	TrainQR.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workSeq!pageQuery.action',                 //装载列表数据的请求URL
	    saveFormColNum: 2 ,fieldWidth: 180,
	    searchFormColNum: 2,
	    viewConfig: null,
	    tbar:['search',{
	    	text:"派工",iconCls:"addIcon" ,handler: function(){
	           //调用新增公用方法
	    		
	    	}
	    },'refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: {id:"entityID", xtype:'hidden' }
		},{
			header:'编号', dataIndex:'workSeqCode',width:120, editor:{ id:"workSeqCodeId",  maxLength:50 , disabled: true, allowBlank: false }
		},{
			header:'检修车型', dataIndex:'pTrainTypeIDX', hidden: true, 	editor:{}	    
		},{
			header:'车型', dataIndex:'pTrainTypeShortName', editor:{id: 'trainTypeShortName_Id', xtype: 'hidden' },
			searcher:{xtype: 'textfield'}, width: 60
		},{
			header:'检修工序', dataIndex:'workSeqName',width:200, editor:{ allowBlank:false, maxLength:50 }
		},{
			header:'额定工时(分)', dataIndex:'ratedWorkHours', hidden: true, editor:{ vtype:'nonNegativeInt', maxLength:8 },searcher: {disabled: true}
		},{
			header:'检修类型', dataIndex:'workSeqType', hidden: true, editor:{},
				renderer: function(v){ return EosDictEntry.getDictname('JXGC_WORK_STEP_REPAIR_TYPE',v)}
		},{
			header:'组成型号主键', dataIndex:'buildUpTypeIDX',hidden:true, editor:{ xtype:'hidden', maxLength:50 }
		},{
			header:'零部件名称', dataIndex:'buildUpTypeName', editor:{  }
		},{
			header:'零部件型号', dataIndex:'buildUpTypeCode', editor:{}, searcher: { xtype: 'textfield'}
		},{
			header:'零部件图号', dataIndex:'chartNo', width: 130,editor:{  }
		},{
			header:'分类', dataIndex:'workClass',width:90, editor:{},
				renderer: function(v){ return EosDictEntry.getDictname('JXGC_WORKSEQ_WORKCLASS',v)},
				searcher: {disabled: true}
		},{
			header:'描述', dataIndex:'repairScope', width:200, editor:{xtype:'textarea',  maxLength:1000 },searcher: {disabled: true}
		},{
			header:'安全注意事项', dataIndex:'safeAnnouncements',hidden:true, editor:{ xtype:'textarea', maxLength:1000 }
		}],
	    searchFn: function(searchParam){ 
			TrainQR.searchParam = searchParam ;
	        this.store.load();
		},
		toEditFn: function(grid, rowIndex, e){
			
	    },
	    editOrder: ['workSeqCode','workSeqName','pTrainTypeIDX','workClass','ratedWorkHours','buildUpTypeCode','buildUpTypeName','chartNo','workSeqType','repairScope','safeAnnouncements']
	});
	//grid查询过滤
	TrainQR.grid.store.on("beforeload", function(){	
		var searchParam = TrainQR.searchParam;
//		searchParam.status = STATUS_USE;  //默认过滤启用状态 目前暂时没有使用该状态过滤
		searchParam.buildUpType = buildUpType ; //机车组成
		var whereList = [] ;
		for (prop in searchParam) {		
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:TrainQR.grid });
});