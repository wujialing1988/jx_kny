/**
 * 作业人员 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('QRWorker');                       //定义命名空间
QRWorker.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/worker!pageQuery.action',                 //装载列表数据的请求URL
    singleSelect: true,
    storeAutoLoad:false,
    tbar:null,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'作业卡主键', dataIndex:'workCardIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'作业人员ID', dataIndex:'workerID',hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'人员代码', dataIndex:'workerCode',editor:{  maxLength:30 }
	},{
		header:'人员名称', dataIndex:'workerName', editor:{  maxLength:25 }
	},{
		header:'人员所在班组', dataIndex:'workerTreamIDX',hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'人员所在班组', dataIndex:'workerTreamName', editor:{  maxLength:300 }
	},{
		header:'人员所在班组序列', dataIndex:'workerTreamSeq',hidden:true, editor:{  maxLength:512 }
	}]
});

//作业人员信息查询
QRWorker.grid.store.on('beforeload' , function(){
	var whereList = [] ;
	whereList.push({propName:'workCardIDX', propValue: RQWorkCard.workCardIDX, stringLike: false}) ;
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

QRWorker.grid.un('rowdblclick', QRWorker.grid.toEditFn, QRWorker.grid); //取消编辑监听

});