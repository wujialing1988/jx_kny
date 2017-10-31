/**
 * 作业任务 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('RQWorkTask');                       //定义命名空间
RQWorkTask.searchParams = {} ;                     //全局查询条件
RQWorkTask.workTaskIDX = "-kill007" ;                      //作业任务主键
RQWorkTask.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/workTask!pageList.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/workTask!logicDelete.action',
	    saveURL: ctx + '/workTask!saveOrUpdate.action',             //保存数据的请求URL
	    saveFormColNum:2,
	    labelWidth: 120,
	    storeAutoLoad: false,
	    fieldWidth: 240,
	    tbar:[],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { }
		},{
			header:'工序卡主键', dataIndex:'workCardIDX',hidden:true, editor:{xtype:'hidden',  maxLength:50 }
		},{
			header:'作业项编码', dataIndex:'workTaskCode', hidden:true, editor:{ xtype:'hidden'}
		},{
			header:'检测/检修项目', dataIndex:'workTaskName', editor:{ allowBlank:false,maxLength:50 }
		},{
			header:'技术要求或标准规定', dataIndex:'repairStandard', editor:{xtype:'textarea', maxLength:500 , width: 330,height:35, allowBlank:false},searcher: {disabled: true}
		},{
			header:'结果', dataIndex:'repairResult'
		}],
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