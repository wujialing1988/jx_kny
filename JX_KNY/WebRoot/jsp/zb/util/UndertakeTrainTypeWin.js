//车型定义
Ext.onReady(function(){
Ext.namespace('UndertakeTrainTypeWin'); 
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
UndertakeTrainTypeWin.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
UndertakeTrainTypeWin.searchParam = {} ;
UndertakeTrainTypeWin.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/undertakeTrainType!pageListForZB.action',                 //装载列表数据的请求URL
    storeAutoLoad: true, 
    tbar: [{
        xtype:"label", text:"  车型简称： " 
    },{
        id:"trainTypeShortName",xtype: "textfield" ,maxLength: 100,vtype:"validChar"
    },{
    	text: "搜索", iconCls:"searchIcon", handler: function(){
    		var searchParam = {} ;
			UndertakeTrainTypeWin.searchParam.trainTypeShortName = Ext.getCmp("trainTypeShortName").getValue();
			UndertakeTrainTypeWin.grid.store.load();
    	}
    },'-',{
    	text: "确定", iconCls: "saveIcon", handler: function(){
    		UndertakeTrainTypeWin.submit();
    	}
    }],
	fields: [{
		header:'车型主键', dataIndex:'trainTypeIDX' ,searcher: { hidden: true }
	},{
		header:'车型简称', dataIndex:'trainTypeShortName'
	}]
});
UndertakeTrainTypeWin.selectWin = new Ext.Window({
	title:"选择车型", maximizable:true, width:500, height:280, 
    plain:true, closeAction:"hide", modal:true, layout:"fit",
    items:UndertakeTrainTypeWin.grid
});
//移除监听
UndertakeTrainTypeWin.grid.un('rowdblclick', UndertakeTrainTypeWin.grid.toEditFn, UndertakeTrainTypeWin.grid);
//确认提交方法，后面可覆盖此方法完成查询
UndertakeTrainTypeWin.submit = function(){alert("请覆盖，UndertakeTrainTypeWin.submit 方法完成自己操作业务！");};

});