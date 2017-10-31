Ext.onReady(function(){
Ext.namespace('Gzforeman');                       //定义命名空间
Gzforeman.searchParam = {};						//全局查询参数集
Gzforeman.win = new Ext.Window({
    title:"默认施修人员设置", maximizable:true, width:600, height:280, 
    plain:true, closeAction:"hide", modal:true,layout:"fit",
    items:WorkStationWorker.grid
});
Gzforeman.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/workStation!workStationPageList.action',                 //装载列表数据的请求URL
    singleSelect: true,
    tbar:['search',{
    	text:"设置默认作业人员", iconCls:"addIcon",
        handler: function(){
        		//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(Gzforeman.grid)) return;
        		var data = Gzforeman.grid.selModel.getSelections();
        		workStationIDX = data[0].get("idx");
        		orgseq = data[0].get("teamOrgSeq");
        		Gzforeman.win.show();
        		WorkStationWorker.grid.store.load();
        	}
    	},'refresh'],
	fields: [{ 
		header:'工位idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'流水线编码', dataIndex:'repairLineCode', editor:{ xtype: 'hidden' }
	},{
		header:'流水线名称', dataIndex:'repairLineName'
	},{
		header:'工位编码', dataIndex:'workStationCode'
	},{
		header:'工位名称', dataIndex:'workStationName'
	},{
		header:'所属台位', dataIndex:'deskName', editor:{ xtype: 'hidden' }
	},{
		header:'默认施修人员', dataIndex:'persons', editor:{ xtype: 'hidden' }
	},{
		header:'工作班组', dataIndex:'teamOrgId', hidden:true
	},{
		header:'工作班组名称', dataIndex:'teamOrgName', hidden:true
	},{
		header:'工作班组序列', dataIndex:'teamOrgSeq', hidden:true
	}],
	searchFn: function(searchParam){
    	Gzforeman.searchParam = searchParam ;
        this.store.load();
    }
});
Gzforeman.grid.un('rowdblclick', Gzforeman.grid.toEditFn, Gzforeman.grid);
Gzforeman.grid.store.on("beforeload", function(){
	var searchParam = Gzforeman.searchParam;
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:Gzforeman.grid });
});