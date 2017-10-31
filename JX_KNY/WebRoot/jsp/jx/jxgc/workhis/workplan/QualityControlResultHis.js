/**
 * 质量检查信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('QualityControlResult');                       //定义命名空间
QualityControlResult.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/qCResultHis!findQCResultList.action',                 //装载列表数据的请求URL
    singleSelect: true,
    storeAutoLoad:false,
    tbar:[],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'检查项', dataIndex:'checkItemName', editor:{  maxLength:25 },sortable:false
	},{
		header:'检查人', dataIndex:'qcEmpName',sortable:false
	},{
		header:'检查日期', dataIndex:'qcTime', xtype:'datecolumn',format:'Y-m-d', editor:{ xtype:'my97date' },sortable:false
	},{
		header:'备注', dataIndex:'remarks',sortable:false
	},{
		header:'状态', dataIndex:'status',sortable:false,
		renderer: function(v) {
			switch(v){
				case 0:
					return "未开放";
				case 1:
					return "待处理";
				case 2:
					return "已处理";
				case 3:
					return "已终止";	
				default:
					return v;
			}
		}
	}]
});

QualityControlResult.grid.store.on('beforeload' , function(){
	var searchParam = {};
	searchParam = MyJson.deleteBlankProp(searchParam);
	searchParam.workCardIDX = RQWorkCard.workCardIDX; //作业工单主键
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

QualityControlResult.grid.un('rowdblclick', QualityControlResult.grid.toEditFn, QualityControlResult.grid); //取消编辑监听

});