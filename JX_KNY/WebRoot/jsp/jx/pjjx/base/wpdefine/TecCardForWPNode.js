

Ext.onReady(function() {
	Ext.namespace('TecCardForWPNode');
	
	/** ************* 定义全局变量开始 ************* */
	TecCardForWPNode.searchParam = {};
	TecCardForWPNode.wPIDX = "###";										// 作业流程主键
	TecCardForWPNode.wPNodeIDX = "###";									// 作业流程节点主键
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义作业流程所用工艺卡表格开始 ************* */
	TecCardForWPNode.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/tecCard!findPageListForWPNode.action',           //装载列表数据的请求URL
	    saveURL: ctx + '/wPNodeUnionTecCard!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/wPNodeUnionTecCard!logicDelete.action',            //删除数据的请求URL
	    tbar:['add', 'delete'],
	    searchFormColNum: 2,
	    
	    saveWin: TecCardForWPNodeSelect.win,								// 作业流程所用工艺卡新增窗口
	    
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden: true, width: 15
		},{
			header:'工艺名称 - 顺序', dataIndex:'seqNo', width: 40, hidden: true, renderer: function(value, metadata, record, rowIndex, colIndex, store) {
				return record.data.tecName + " - " + value;
			}
		},{
			header:'工艺卡编号', dataIndex:'tecCardNo', width: 10
		},{
			header:'工艺卡描述', dataIndex:'tecCardDesc', width: 50
		},{
			header:'工艺名称', dataIndex:'tecName', width: 40, hidden: false
		}],
		storeAutoLoad: false,
		// 重新查询方法，在查询条件中要额外附加“作业流程主键”进行限定查询
		searchFn: function(searchParam){
	    	this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	        this.store.load({
	            params: { 
	            	entityJson: Ext.util.JSON.encode(searchParam),
	            	wPIDX: TecCardForWPNode.wPIDX,
	            	wPNodeIDX: TecCardForWPNode.wPNodeIDX
            	}       
	        });	
	    },
	    beforeAddButtonFn: function() {
	    	if ("###" == TecCardForWPNode.wPIDX) {
	    		alert("系统错误：作业流程主键赋值出错");
	    		return false;
	    	}
	    	if ("###" == TecCardForWPNode.wPNodeIDX) {
	    		alert("系统错误：作业流程节点主键赋值出错");
	    		return false;
	    	}
	    	TecCardForWPNodeSelect.wPIDX = TecCardForWPNode.wPIDX;
	    	TecCardForWPNodeSelect.wPNodeIDX = TecCardForWPNode.wPNodeIDX;
	    	return true;
	    },
	    afterShowSaveWin: function() {
	    	TecCardForWPNodeSelect.grid.store.load();
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
	            	wPNodeIDX: TecCardForWPNode.wPNodeIDX
            	}
	        });
	    }
	});
	TecCardForWPNode.grid.un('rowdblclick', TecCardForWPNode.grid.toEditFn, TecCardForWPNode.grid);
	TecCardForWPNode.grid.store.setDefaultSort("seqNo", "ASC");
	//查询前添加过滤条件
	TecCardForWPNode.grid.store.on('beforeload' , function(){
		var searchParam = TecCardForWPNode.searchParam;
		this.baseParams.wPIDX = TecCardForWPNode.wPIDX;
		this.baseParams.wPNodeIDX = TecCardForWPNode.wPNodeIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	/** ************* 定义作业流程所用工艺卡表格结束 ************* */
	
});