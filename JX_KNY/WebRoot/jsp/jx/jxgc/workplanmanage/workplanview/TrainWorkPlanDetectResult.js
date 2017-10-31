/**
 * 检测结果 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('QRDetectResult');                       //定义命名空间
QRDetectResult.searchParams = {} ;                     //全局查询条件
QRDetectResult.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/detectResult!pageList.action',                 //装载列表数据的请求URL
    singleSelect: true,
    tbar:null,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'作业任务主键', dataIndex:'workTaskIDX',hidden:true, editor:{  maxLength:50 }
		},{
			header:'检测项编码', dataIndex:'detectItemCode',hidden:true, editor:{  maxLength:64 }
		},{
			header:'检测结果数据类型', dataIndex:'detectResulttype',hidden:true, editor:{  maxLength:50 }
		},{
			header:'检测内容', dataIndex:'detectItemContent', editor:{  maxLength:500 }
		},{
			header:'检测项标准', dataIndex:'detectItemStandard', editor:{  maxLength:1000 }
		},{
			header:'检测结果', dataIndex:'detectResult', editor:{  maxLength:100 }
		},{
			header:'字典项编码', dataIndex:'dictItemCode',hidden:true, editor:{  maxLength:128 }
	}],
	searchFn: function(searchParam){
		QRDetectResult.searchParams = searchParam ;
		QRDetectResult.grid.store.load();
	}
});
//检测结果信息
QRDetectResult.grid.store.on('beforeload' , function(){
	var searchParam = QRDetectResult.searchParams;
	searchParam = MyJson.deleteBlankProp(searchParam);
	searchParam.workTaskIDX = RQWorkTask.workTaskIDX ; //作业任务主键
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

QRDetectResult.grid.un('rowdblclick', QRDetectResult.grid.toEditFn, QRDetectResult.grid); //取消编辑监听

});