

Ext.onReady(function() {
	Ext.namespace('RecordCardForWPNode');
	
	/** ************* 定义全局变量开始 ************* */
	RecordCardForWPNode.searchParam = {};
	RecordCardForWPNode.wPIDX = "###";										// 作业流程主键
	RecordCardForWPNode.wPNodeIDX = "###";									// 作业流程节点主键
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义作业流程所用记录卡表格开始 ************* */
	RecordCardForWPNode.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/recordCard!findPageListForWPNode.action',           //装载列表数据的请求URL
	    saveURL: ctx + '/wPNodeUnionRecordCard!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/wPNodeUnionRecordCard!logicDelete.action',            //删除数据的请求URL
	    tbar:['add', 'delete'],
	    searchFormColNum: 2,
	    
	    saveWin: RecordCardForWPNodeSelect.win,								// 作业流程所用记录卡新增窗口
	    
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden: true, width: 15
		},{
			header:'记录单名称 - 顺序', dataIndex:'seqNo', width: 40, hidden: true, renderer: function(value, metadata, record, rowIndex, colIndex, store) {
				return record.data.recordName + " - " + value;
			}
		},{
			header:'记录卡编号', dataIndex:'recordCardNo', width: 15
		},{
			header:'记录卡描述', dataIndex:'recordCardDesc', width: 40
		},{
			header:'记录单名称', dataIndex:'recordName', width: 40, hidden: false
		}],
		storeAutoLoad: false,
		// 重新查询方法，在查询条件中要额外附加“作业流程主键”进行限定查询
		searchFn: function(searchParam){
	    	this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	        this.store.load({
	            params: { 
	            	entityJson: Ext.util.JSON.encode(searchParam),
	            	wPIDX: RecordCardForWPNode.wPIDX,
	            	wPNodeIDX: RecordCardForWPNode.wPNodeIDX
            	}       
	        });	
	    },
	    beforeAddButtonFn: function() {
	    	if ("###" == RecordCardForWPNode.wPIDX) {
	    		alert("系统错误：作业流程主键赋值出错");
	    		return false;
	    	}
	    	if ("###" == RecordCardForWPNode.wPNodeIDX) {
	    		alert("系统错误：作业流程节点主键赋值出错");
	    		return false;
	    	}
	    	RecordCardForWPNodeSelect.wPIDX = RecordCardForWPNode.wPIDX;
	    	RecordCardForWPNodeSelect.wPNodeIDX = RecordCardForWPNode.wPNodeIDX;
	    	return true;
	    },
	    afterShowSaveWin: function() {
	    	RecordCardForWPNodeSelect.grid.store.load();
	    },
	    addButtonFn: function() {
	    	if(this.beforeAddButtonFn() == false)   return;
	        //判断新增删除窗体是否为null，如果为null则自动创建后显示
	        if(this.saveWin == null)  this.createSaveWin();
        	if(this.searchWin)  this.searchWin.hide();
        	if(this.saveWin.isVisible())    this.saveWin.hide();
        	if(this.beforeShowSaveWin() == false)   return;
	        this.saveWin.show();
      		this.afterShowSaveWin();
	    },
	    deleteButtonFn: function(){                         //点击删除按钮触发的函数，默认执行删除操作
	        if(this.saveWin)    this.saveWin.hide();
	        if(this.searchWin)  this.searchWin.hide();        
	        //未选择记录，直接返回
	        if(!$yd.isSelectedRecord(this)) return;
	        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	        if(!this.beforeDeleteFn()) return;
	        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
	        $yd.confirmAndDelete({
	            scope: this, url: this.deleteURL, params: {
	            	ids: $yd.getSelectedIdx(this, this.storeId),
	            	wPNodeIDX: RecordCardForWPNode.wPNodeIDX
            	}
	        });
	    }
	});
	RecordCardForWPNode.grid.un('rowdblclick', RecordCardForWPNode.grid.toEditFn, RecordCardForWPNode.grid);
	RecordCardForWPNode.grid.store.setDefaultSort("seqNo", "ASC");
	//查询前添加过滤条件
	RecordCardForWPNode.grid.store.on('beforeload' , function(){
		var searchParam = RecordCardForWPNode.searchParam;
		this.baseParams.wPIDX = RecordCardForWPNode.wPIDX;
		this.baseParams.wPNodeIDX = RecordCardForWPNode.wPNodeIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	/** ************* 定义作业流程所用记录卡表格结束 ************* */
	
});