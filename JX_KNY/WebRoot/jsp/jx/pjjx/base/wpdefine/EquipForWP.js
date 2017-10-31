

Ext.onReady(function() {
	Ext.namespace('EquipForWP');
	
	/** ************* 定义全局变量开始 ************* */
	EquipForWP.searchParam = {};
	EquipForWP.wPIDX = "###";										// 作业流程主键
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义作业流程所用工艺表格开始 ************* */
	EquipForWP.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/wPNodeUnionEquipCard!findPageQuery.action',           //装载列表数据的请求URL
	    saveURL: ctx + '/wPNodeUnionEquipCard!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/wPNodeUnionEquipCard!logicDelete.action',            //删除数据的请求URL
	    tbar:['<B>机务设备作业工单</B>', '-', 'add', 'delete'],
	    searchFormColNum: 2,
	    page:false,
	    saveWin: EquipForWPSelect.win,								// 作业流程所用工艺新增窗口
	    
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'工单编码', dataIndex:'equipCardNo', editor:{  maxLength:30 }
		},{
			header:'设备类别', dataIndex:'deviceTypeName', editor:{  maxLength:50 }
		},{
			header:'工单描述', dataIndex:'equipCardDesc', editor:{  maxLength:500 }
		}],
		storeAutoLoad: false,
		// 重新查询方法，在查询条件中要额外附加“作业流程主键”进行限定查询
		searchFn: function(searchParam){
	    	this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	        this.store.load({
	            params: { 
	            	entityJson: Ext.util.JSON.encode(searchParam),
	            	wPIDX: EquipForWP.wPIDX
            	}       
	        });	
	    },
	    beforeAddButtonFn: function() {
	    	if ("###" == EquipForWP.wPIDX) {
	    		alert("系统错误：作业流程主键赋值出错");
	    		return false;
	    	}
	    	EquipForWPSelect.wPIDX = EquipForWP.wPIDX;
	    	return true;
	    },
	    afterShowSaveWin: function() {
	    	EquipForWPSelect.grid.store.load();
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
	            	wPIDX: EquipForWP.wPIDX
            	}
	        });
	    }
	});
	EquipForWP.grid.un('rowdblclick', EquipForWP.grid.toEditFn, EquipForWP.grid);
	//查询前添加过滤条件
	EquipForWP.grid.store.on('beforeload' , function(){
		var searchParam = EquipForWP.searchParam;
		this.baseParams.wPIDX = EquipForWP.wPIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	/** ************* 定义作业流程所用工艺表格结束 ************* */
	
});