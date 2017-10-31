Ext.onReady(function(){
//配件台账定义
Ext.namespace('PartsAccount');     
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
PartsAccount.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
PartsAccount.grid = new Ext.yunda.Grid({ 
     loadURL: ctx + '/partsAccount!pageListTestItem.action',                 //装载列表数据的请求URL
     singleSelect: true,
    tbar: [{
        xtype:"label", text:"  配件名称： " 
    },{
        id:"partsNameId",xtype: "textfield" ,maxLength: 50,width:100
    },{
        xtype:"label", text:"  规格型号： " 
    },{
        id:"specificationModelId",xtype: "textfield" ,maxLength: 8,width:100
    },{
        xtype:"label", text:"  配件铭牌号： " 
    },{
        id:"nameplateNoId",xtype: "textfield" ,maxLength: 50,width:80
    },{
        xtype:"label", text:"  配件编号： " 
    },{
        id:"partsNoId",xtype: "textfield" ,maxLength: 50,width:100
    },{
    	text: "搜索", iconCls:"searchIcon", handler: function(){
    		var searchParam = {} ;
			searchParam.partsName = Ext.getCmp("partsNameId").getValue();
			searchParam.specificationModel = Ext.getCmp("specificationModelId").getValue();
			searchParam.nameplateNo = Ext.getCmp("nameplateNoId").getValue();
			searchParam.partsNo = Ext.getCmp("partsNoId").getValue();
			searchParam = MyJson.deleteBlankProp(searchParam);
			PartsAccount.grid.store.load({
				params: { entityJson: Ext.util.JSON.encode(searchParam) }
			});
    	}
    },'-',{
    	text:"双击行选择配件", xtype:"label"
    }],
	fields: [{
		header:'主键', dataIndex:'idx', hidden:true, editor:{  maxLength:8 } 
	},{
		header:'配件型号表主键', dataIndex:'partsTypeIDX', hidden:true, editor:{  maxLength:8 } 
	},{
		header:'配件名称', dataIndex:'partsName', editor:{  maxLength:8 } 
	},{
		header:'规格型号', dataIndex:'specificationModel', editor:{  maxLength:8 }
	},{
		header:'配件铭牌号', dataIndex:'nameplateNo', editor:{  maxLength:50 }
	},{
		header:'配件编号', dataIndex:'partsNo', editor:{ xtype:'hidden' }
	},{
		header:'配属单位', dataIndex:'ownerUnitName', editor:{  maxLength:50 },searcher:{disabled: true}
	},{
		header:'制造厂家', dataIndex:'madeFactoryName', editor:{  maxLength:50 },searcher:{disabled: true}
	},{
		header:'出厂日期', dataIndex:'leaveDate', xtype:'datecolumn', editor:{ xtype:'my97date' },searcher:{disabled: true}
	}],
	listeners:{
				//双击选择机车
				"rowdblclick": {
	            	fn: function(grid, idx, e){
	            		var r = grid.store.getAt(idx);
	            		PartsAccount.submit(r);
	            	}
				}
			}
});
PartsAccount.selectWin = new Ext.Window({
	title:"选择配件台账", maximizable:true,width:800, height:415, closeAction:"hide", modal:true, layout:"fit", items:PartsAccount.grid
});
//移除监听
PartsAccount.grid.un('rowdblclick', PartsAccount.grid.toEditFn, PartsAccount.grid);
//确认提交方法，后面可覆盖此方法完成查询
PartsAccount.submit = function(r){alert("请覆盖，PartsAccount.submit 方法完成自己操作业务！");};

});