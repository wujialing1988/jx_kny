Ext.onReady(function(){
//修程定义
Ext.namespace('Train');                       //定义命名空间
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
Train.searchParam={};
Train.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
Train.grid = new Ext.yunda.Grid({ 
    loadURL: ctx + '/jczlTrain!pageListTrian.action',                 //装载列表数据的请求URL
    //singleSelect: true,
    tbar: [{
        xtype:"label", text:"  车号： " 
    },{
        id:"trainNo",xtype: "textfield" ,maxLength: 100
    },{
    	text: "搜索", iconCls:"searchIcon", handler: function(){
    		var searchParam = Train.searchParam ;
			searchParam.trainNo = Ext.getCmp("trainNo").getValue();
			Train.grid.store.load({
				params: { entityJson: Ext.util.JSON.encode(searchParam) }
			});
    	}
    },'-',{
    	text: "确定", iconCls: "saveIcon", handler: function(){
    		Train.submit();
    	}
    }],
	fields: [{
		header:'主键', dataIndex:'idx', hidden:true, editor:{  maxLength:8 } 
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', hidden:true, editor:{  maxLength:8 } 
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},
//	{
//		header:'配属段', dataIndex:'holdOrgName', editor:{ xtype:'hidden' }
//	},
	{
		header:'制造厂家', dataIndex:'makeFactoryName', editor:{  maxLength:50 },searcher:{disabled: true}
	},{
		header:'出厂日期', dataIndex:'leaveDate', xtype:'datecolumn', editor:{ xtype:'my97date' },searcher:{disabled: true}
	}]
});
Train.selectWin = new Ext.Window({
	title:"选择机车", maximizable:true,width:600, height:415, closeAction:"hide", modal:true, layout:"fit", items:Train.grid
});
//移除监听
Train.grid.un('rowdblclick', Train.grid.toEditFn, Train.grid);
//确认提交方法，后面可覆盖此方法完成查询
Train.submit = function(){alert("请覆盖，Train.submit 方法完成自己操作业务！");};

});