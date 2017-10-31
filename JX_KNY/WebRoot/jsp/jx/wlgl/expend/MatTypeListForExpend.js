

Ext.onReady(function() {
	Ext.namespace('MatTypeList');
	
	/** ************* 定义全局变量开始 ************* */
	MatTypeList.searchParam = {};
	MatTypeList.orgId = "";
	/** ************* 定义全局变量结束 ************* */
	
	MatTypeList.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matTypeList!findPageListForExpend.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matTypeList!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matTypeList!logicDelete.action',            //删除数据的请求URL
	    tbar:null,
		fields: [{
			header:'物料编码', dataIndex:'matCode'
		},{
			header:'物料描述', dataIndex:'matDesc', width: 200
		},{
			header:'库存数量', dataIndex:'qty', width: 60			
		},{
			header:'计量单位', dataIndex:'unit', width: 60			
		},{
			header:'计划单价', dataIndex:'price', width: 60	
		}],
		storeId: 'matCode',
		storeAutoLoad: false
	});
	MatTypeList.grid.un('rowdblclick', MatTypeList.grid.toEditFn, MatTypeList.grid);
	//查询前添加过滤条件
	MatTypeList.grid.store.on('beforeload' , function(){
		var searchParam = MatTypeList.searchParam;
		this.baseParams.orgId = MatTypeList.orgId;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
});