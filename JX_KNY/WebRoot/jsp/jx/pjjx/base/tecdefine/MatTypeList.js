

Ext.onReady(function() {
	Ext.namespace('MatTypeList');
	
	/** ************* 定义全局变量开始 ************* */
	MatTypeList.tecCardIDX = "";
	MatTypeList.searchParam = {};
	/** ************* 定义全局变量结束 ************* */
	
	MatTypeList.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matTypeList!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matTypeList!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matTypeList!logicDelete.action',            //删除数据的请求URL
	    tbar:null,
		fields: [{
			header:'物料编码', dataIndex:'matCode'
		},{
			header:'物料描述', dataIndex:'matDesc', width: 200
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
		var whereList = [];
		var sql = "MAT_CODE NOT IN (SELECT MAT_CODE FROM PJJX_TEC_CARD_MAT WHERE RECORD_STATUS = 0 AND TEC_CARD_IDX = '" + MatTypeList.tecCardIDX + "')";
		whereList.push({sql: sql, compare:Condition.SQL});
		for (var prop in MatTypeList.searchParam) {
			  whereList.push({propName:prop, compare:Condition.LIKE, propValue:MatTypeList.searchParam[ prop ]});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
});