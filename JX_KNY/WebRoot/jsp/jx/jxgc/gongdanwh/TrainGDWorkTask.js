/**
 * 作业任务 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('RQWorkTask');                       //定义命名空间
RQWorkTask.searchParams = {} ;                     //全局查询条件
RQWorkTask.workTaskIDX = "-kill007" ;                      //作业任务主键
RQWorkTask.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/workTaskResultView!pageListForTaskQR.action',                 //装载列表数据的请求URL
    singleSelect: true,
    storeAutoLoad:false,
    tbar:null,
	fields: fields, //jsp页面上动态组装
	searchFn: function(searchParam){
		RQWorkTask.searchParams = searchParam ;
		RQWorkTask.grid.store.load();
	}
});


RQWorkTask.grid.store.on('beforeload' , function(){
	var searchParam = RQWorkTask.searchParams;
	searchParam = MyJson.deleteBlankProp(searchParam);
	searchParam.workCardIDX = RQWorkCard.workCardIDX ; //作业工单主键
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

RQWorkTask.grid.un('rowdblclick', RQWorkTask.grid.toEditFn, RQWorkTask.grid); //取消编辑监听
//添加单击事件
RQWorkTask.grid.on('rowclick',function(grid, index, e){
	var record = grid.store.getAt(index);
	RQWorkTask.workTaskIDX = record.get("idx"); //检测结果
	QRDetectResult.grid.store.load();   //刷新检测结果信息
});

});