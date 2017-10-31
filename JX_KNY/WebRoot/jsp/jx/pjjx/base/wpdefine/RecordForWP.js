

Ext.onReady(function() {
	Ext.namespace('RecordForWP');
	
	/** ************* 定义全局变量开始 ************* */
	RecordForWP.searchParam = {};
	RecordForWP.wPIDX = "###";										// 作业流程主键
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义作业流程所用记录单表格开始 ************* */
	RecordForWP.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/record!findPageListForWP.action',           //装载列表数据的请求URL
	    saveURL: ctx + '/wPUnionRecord!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/wPUnionRecord!logicDelete.action',            //删除数据的请求URL
	    tbar:['<B>检修记录单</B>', '-', 'add', 'delete'],
	    searchFormColNum: 2,
	    page:false,
	    saveWin: RecordForWPSelect.win,								// 作业流程所用记录单新增窗口
	    
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden: true, width: 15
		},{
			header:'编号', dataIndex:'recordNo', width: 20
		},{
			header:'名称', dataIndex:'recordName', width: 30
		},{
			header:'描述', dataIndex:'recordDesc', width: 50
		}],
		storeAutoLoad: false,
		// 重新查询方法，在查询条件中要额外附加“作业流程主键”进行限定查询
		searchFn: function(searchParam){
	    	this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	        this.store.load({
	            params: { 
	            	entityJson: Ext.util.JSON.encode(searchParam),
	            	wPIDX: RecordForWP.wPIDX
            	}       
	        });	
	    },
	    beforeAddButtonFn: function() {
	    	if ("###" == RecordForWP.wPIDX) {
	    		alert("系统错误：作业流程主键赋值出错");
	    		return false;
	    	}
	    	RecordForWPSelect.wPIDX = RecordForWP.wPIDX;
	    	return true;
	    },
	    afterShowSaveWin: function() {
	    	RecordForWPSelect.grid.store.load();
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
	            	wPIDX: RecordForWP.wPIDX
            	}
	        });
	    }
	});
	RecordForWP.grid.un('rowdblclick', RecordForWP.grid.toEditFn, RecordForWP.grid);
	RecordForWP.grid.store.setDefaultSort("recordNo", "ASC");
	//查询前添加过滤条件
	RecordForWP.grid.store.on('beforeload' , function(){
		var searchParam = RecordForWP.searchParam;
		this.baseParams.wPIDX = RecordForWP.wPIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	/** ************* 定义作业流程所用记录单表格结束 ************* */
	
});