

Ext.onReady(function() {
	Ext.namespace('TecForWP');
	
	/** ************* 定义全局变量开始 ************* */
	TecForWP.searchParam = {};
	TecForWP.wPIDX = "###";										// 作业流程主键
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义作业流程所用工艺表格开始 ************* */
	TecForWP.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/tec!findPageListForWP.action',           //装载列表数据的请求URL
	    saveURL: ctx + '/wPUnionTec!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/wPUnionTec!logicDelete.action',            //删除数据的请求URL
	    tbar:['<B>检修工艺</B>', '-', 'add', 'delete'],
	    searchFormColNum: 2,
	    page:false,
	    saveWin: TecForWPSelect.win,								// 作业流程所用工艺新增窗口
	    
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden: true, width: 15
		},{
			header:'编号', dataIndex:'tecNo', width: 20
		},{
			header:'名称', dataIndex:'tecName', width: 30
		},{
			header:'描述', dataIndex:'tecDesc', width: 50
		}],
		storeAutoLoad: false,
		// 重新查询方法，在查询条件中要额外附加“作业流程主键”进行限定查询
		searchFn: function(searchParam){
	    	this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	        this.store.load({
	            params: { 
	            	entityJson: Ext.util.JSON.encode(searchParam),
	            	wPIDX: TecForWP.wPIDX
            	}       
	        });	
	    },
	    beforeAddButtonFn: function() {
	    	if ("###" == TecForWP.wPIDX) {
	    		alert("系统错误：作业流程主键赋值出错");
	    		return false;
	    	}
	    	TecForWPSelect.wPIDX = TecForWP.wPIDX;
	    	return true;
	    },
	    afterShowSaveWin: function() {
	    	TecForWPSelect.grid.store.load();
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
	            	wPIDX: TecForWP.wPIDX
            	}
	        });
	    }
	});
	TecForWP.grid.un('rowdblclick', TecForWP.grid.toEditFn, TecForWP.grid);
	TecForWP.grid.store.setDefaultSort("tecNo", "ASC");
	//查询前添加过滤条件
	TecForWP.grid.store.on('beforeload' , function(){
		var searchParam = TecForWP.searchParam;
		this.baseParams.wPIDX = TecForWP.wPIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	/** ************* 定义作业流程所用工艺表格结束 ************* */
	
});