/**
 * 处理方法 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace('FaultMethod');                       //定义命名空间
	
	FaultMethod.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/faultMethod!pageList.action',              //装载列表数据的请求URL
	    saveURL: ctx + '/faultMethod!saveOrUpdate.action',          //保存数据的请求URL
	    deleteURL: ctx + '/faultMethod!delete.action',        	    //删除数据的请求URL
	    // 重新设置表格主键字段（默认为idx)
	    storeId: "methodID",
		fields: [{
			header:'处理方法编码', dataIndex:'methodID', width: 30, editor: { allowBlank:false, vtype:'numberInt', maxLength: '10' }
		},{
			header:'处理方法名称', dataIndex:'methodName', width: 70, editor:{ allowBlank:false, xtype:'textfield', maxLength: '100' }
		},{
			// 用于标识保存对象是“新增”还是“更新”的冗余字段
			header:'flag', dataIndex:'flag', hidden: true, editor:{ xtype:'hidden' }
		}],
		// 启用编辑时被禁用的字段
		afterShowSaveWin: function(){
			this.enableColumns(['methodID']);
			this.saveForm.find('name', 'flag')[0].setValue(FLAG_INSERT);
		},  
		// 编辑时，禁用【处理方法编码】字段，因为methodID是数据表主键，不允许修改
		afterShowEditWin: function(record, rowIndex){
			this.disableColumns(['methodID']);
			this.saveForm.find('name', 'flag')[0].setValue(FLAG_UPDATE);
		},
		// 获取数据前启用被禁用的字段
		beforeGetFormData: function(){
			this.enableColumns(['methodID']);
		},
		// 获取数据后禁用被启用的字段
    	afterGetFormData: function(){
    		var flagField = this.saveForm.find('name', 'flag')[0];
    		var flagValue = flagField.getValue();
    		if (flagValue == FLAG_UPDATE) {
				this.disableColumns(['methodID']);
    		}
    	}, 
    	// 保存成功后隐藏编辑端口
    	afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        // 保存成功后隐藏编辑窗口
	        this.saveWin.hide();
	    }
	});
	// 设置表格的默认排序字段为【处理方法编码】字段
	FaultMethod.grid.store.setDefaultSort('methodID', 'ASC');
	
	//页面自适应布局
	new Ext.Viewport({ layout:'fit', items:FaultMethod.grid });
	
});