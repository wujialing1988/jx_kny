Ext.onReady(function(){
//配件台账定义
Ext.namespace('PartsAccount');     
PartsAccount.searchParam = {} ;  //定义全局查询条件
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
PartsAccount.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
PartsAccount.grid = new Ext.yunda.Grid({ 
    loadURL: ctx + '/partsAccount!pageQuery.action',                 //装载列表数据的请求URL
     viewConfig: null,
    tbar: ['search',{
    	text: "确定", iconCls: "saveIcon", handler: function(){
    		PartsAccount.submit();    
    	}
    }],
	fields: [{
		header:'主键', dataIndex:'idx', hidden:true, editor:{  maxLength:8 } 
	},{
		header:'配件型号表主键', dataIndex:'partsTypeIDX', hidden:true, editor:{  maxLength:8 } 
	},{
		header:'配件编号', dataIndex:'partsNo',width:180, editor:{ xtype:'hidden' }
	},{
		header:'配件名称', dataIndex:'partsName',width:180, editor:{  maxLength:8 } 
	},{
		header:'规格型号', dataIndex:'specificationModel',width:160, editor:{  maxLength:8 }
	},{
		header:'配件铭牌号', dataIndex:'nameplateNo',width:130, editor:{  maxLength:50 }
	},{
		header:'配属单位', dataIndex:'ownerUnitName', editor:{  maxLength:50 },searcher:{disabled: true}
	},{
		header:'制造厂家', dataIndex:'madeFactoryName', editor:{  maxLength:50 },searcher:{disabled: true}
	},{
		header:'出厂日期', dataIndex:'leaveDate', xtype:'datecolumn', editor:{ xtype:'my97date' },searcher:{disabled: true}
	}],
	searchFn: function(searchParam){ 
		PartsAccount.searchParam = searchParam ;
        PartsAccount.grid.store.load();
	}
});
PartsAccount.selectWin = new Ext.Window({
	title:"选择配件台账", maximizable:true,width:600, height:415, closeAction:"hide", modal:true, layout:"fit", items:PartsAccount.grid
});
//移除监听
PartsAccount.grid.un('rowdblclick', PartsAccount.grid.toEditFn, PartsAccount.grid);
//确认提交方法，后面可覆盖此方法完成查询
PartsAccount.submit = function(){alert("请覆盖，PartsAccount.submit 方法完成自己操作业务！");};

});