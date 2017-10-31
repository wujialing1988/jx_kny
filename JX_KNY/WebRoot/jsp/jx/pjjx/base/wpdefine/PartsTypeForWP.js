

Ext.onReady(function() {
	Ext.namespace('PartsTypeForWP');
	
	/** ************* 定义全局变量开始 ************* */
	PartsTypeForWP.searchParam = {};
	PartsTypeForWP.wPIDX = "###";										// 作业流程主键
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义作业流程适用配件表格开始 ************* */
	PartsTypeForWP.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsType!findPageListForWP.action',           //装载列表数据的请求URL
	    saveURL: ctx + '/wPUnionParts!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/wPUnionParts!logicDelete.action',            //删除数据的请求URL
	    tbar:['search', 'add', 'delete'],
	    searchFormColNum: 2,
	    
	    saveWin: PartsTypeForWPSelect.win,								// 作业流程适用配件新增窗口
	    
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden: true, width: 15
		},{
			header:'编号', dataIndex:'specificationModelCode', width: 15
		},{
			header:'配件名称', dataIndex:'partsName', width: 30
		},{
			header:'规格型号', dataIndex:'specificationModel', width: 40
		},{
			header:'物料编码', dataIndex:'matCode', width: 25
		}],
		storeAutoLoad: false,
		// 重新查询方法，在查询条件中要额外附加“作业流程主键”进行限定查询
		searchFn: function(searchParam){
	    	this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	        this.store.load({
	            params: { 
	            	entityJson: Ext.util.JSON.encode(searchParam),
	            	wPIDX: PartsTypeForWP.wPIDX
            	}       
	        });	
	    },
	    beforeAddButtonFn: function() {
	    	if ("###" == PartsTypeForWP.wPIDX) {
	    		alert("系统错误：作业流程主键赋值出错");
	    		return false;
	    	}
	    	PartsTypeForWPSelect.wPIDX = PartsTypeForWP.wPIDX;
	    	return true;
	    },
	    afterShowSaveWin: function() {
	    	PartsTypeForWPSelect.grid.store.load();
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
	            	wPIDX: PartsTypeForWP.wPIDX
            	}
	        });
	    }
	});
	PartsTypeForWP.grid.un('rowdblclick', PartsTypeForWP.grid.toEditFn, PartsTypeForWP.grid);
	PartsTypeForWP.grid.store.setDefaultSort("specificationModelCode", "ASC");
	//查询前添加过滤条件
	PartsTypeForWP.grid.store.on('beforeload' , function(){
		var searchParam = PartsTypeForWP.searchParam;
		this.baseParams.wPIDX = PartsTypeForWP.wPIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	/** ************* 定义作业流程适用配件表格结束 ************* */
	
});