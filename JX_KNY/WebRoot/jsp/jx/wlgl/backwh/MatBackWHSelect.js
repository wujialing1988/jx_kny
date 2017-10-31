

Ext.onReady(function() {
	Ext.namespace('MatBackWHSelect');
	
	/** ************* 定义全局变量开始 ************* */
	MatBackWHSelect.searchParam = {};
	MatBackWHSelect.whIdx = "";				// 退库库房idx主键
	MatBackWHSelect.getOrgId = "";			// 退库人所在班组id,即：出库时的领用人所在班组id
	/** ************* 定义全局变量结束 ************* */
	
	MatBackWHSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matBackWHSelect!pageList.action',                 //装载列表数据的请求URL
	    tbar:null,
		fields: [{
			header:'出库明细主键', dataIndex:'idx', hidden: true
		},{
			header:'出库单编号', dataIndex:'billNo'
		},{
			header:'出库单摘要', dataIndex:'billSummary', hidden: true
		},{
			header:'领用人', dataIndex:'getEmp',width: 40
		},{
			header:'领用日期', dataIndex:'getDate', xtype: 'datecolumn',width: 60
		},{
			header:'出库用途', dataIndex:'purpose'
		},{
			header:'物料编码', dataIndex:'matCode',width: 60			
		},{
			header:'物料描述', dataIndex:'matDesc'			
		},{
			header:'出库数量', dataIndex:'qty', width: 40, align: 'center'
		},{
			header:'已退库数量', dataIndex:'backQty',width: 45, align: 'center'
		},{
			header:'计量单位', dataIndex:'unit',width: 40, align: 'center', hidden: true
		}],
		storeAutoLoad: false
	});
	MatBackWHSelect.grid.un('rowdblclick', MatBackWHSelect.grid.toEditFn, MatBackWHSelect.grid);
	//查询前添加过滤条件
	MatBackWHSelect.grid.store.on('beforeload' , function(){
		MatBackWHSelect.searchParam.whIdx = MatBackWHSelect.whIdx;
		MatBackWHSelect.searchParam.getOrgId = MatBackWHSelect.getOrgId;
		this.baseParams.entityJson = Ext.util.JSON.encode(MatBackWHSelect.searchParam);
	});
});