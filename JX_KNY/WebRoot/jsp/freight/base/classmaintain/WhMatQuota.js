/**
 * 站点物料维护
 */
Ext.onReady(function(){
	
Ext.namespace('WhMatQuota');                       //定义命名空间

WhMatQuota.workplaceCode = "";// 站点标识
WhMatQuota.workplaceName = "";// 站点名称

WhMatQuota.searchParam = {};

WhMatQuota.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/whMatQuota!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/whMatQuota!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/whMatQuota!delete.action',            //删除数据的请求URL
    storeAutoLoad : false,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'站点编码', dataIndex:'workplaceCode', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:'站点名称', dataIndex:'workplaceName',hidden:true, editor:{  disabled: true,maxLength:100 }
	},{
		header:'库房主键', dataIndex:'whIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:'库房名称', dataIndex:'whName',hidden:true, editor:{  disabled: true,maxLength:50 }
	},{
		header:'物料编码', dataIndex:'matCode', editor:{  disabled: true,maxLength:50 }
	},{
		header:'物料描述', dataIndex:'matDesc', editor:{ disabled: true, maxLength:100 }
	},{
		header:'物料类型', dataIndex:'matType', editor:{ disabled: true, maxLength:100 },searcher:{disabled:true}
	},{
		header:'计量单位', dataIndex:'unit', editor:{ disabled: true, maxLength:20 },searcher:{disabled:true}
	},{
		header:'最低保有量', dataIndex:'minQty', editor:{ xtype:'numberfield' },searcher:{disabled:true}
	},{
		header:'最大量', dataIndex:'maxQty',hidden:true, editor:{ xtype:'numberfield' },searcher:{disabled:true}
	},{
		header:'当前保有量', dataIndex:'currentQty', editor:{ xtype:'numberfield' },searcher:{disabled:true}
	},{
		header:'使用频率', dataIndex:'useRate',hidden:true, editor:{  maxLength:20 },searcher:{disabled:true}
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:200 },searcher:{disabled:true}
	},{
		header:'维护人', dataIndex:'maintainEmp',hidden:true, editor:{ disabled: true, maxLength:25 },searcher:{disabled:true}
	},{
		header:'维护日期', dataIndex:'maintainDate', hidden:true, xtype:'datecolumn', editor:{ disabled: true,xtype:'my97date' },searcher:{disabled:true}
	}],
	tbar: ["search",{ text:'批量添加', iconCls:'chart_attributeConfigIcon', handler:function(){
//			        			PartsAccount.manageDeptId = Ext.getCmp("PartsAboardExWh_whIdx").getValue();
					        	if(WhMatQuota.workplaceCode == ""){
						    		MyExt.Msg.alert("请选择站点！");
						    		return ;
						    	}
			           	        MatTypeList.selectWin.show();
			           	        MatTypeList.grid.store.load();
						
			        }},"delete",{
			    	text: "刷新", iconCls: "refreshIcon", handler: function(){self.location.reload();}
			    }],
	searchFn: function(searchParam){
			WhMatQuota.searchParam = searchParam ;
			this.store.load();
		},
	beforeSaveFn: function(rowEditor, changes, record, rowIndex){
		var minQty = record.data.minQty ;
		var currentQty = record.data.currentQty ;
		if("" != minQty){
			minQty = parseInt(minQty);
			if(minQty > 9999999){
		    	MyExt.Msg.alert("输入的数量超过了系统最大值 9999999 ！请重新输入");
				return;
		    }
		}
		if("" != currentQty){
			currentQty = parseInt(currentQty);
			if(currentQty > 9999999){
		    	MyExt.Msg.alert("输入的数量超过了系统最大值 9999999 ！请重新输入");
				return;
		    }
		}
        return true;
    }
	 
});

 WhMatQuota.grid.store.on('beforeload',function(){
	var searchParam = {};
	if(WhMatQuota.workplaceCode == ""){
		searchParam.workplaceCode = "####";
	}else{
		searchParam.workplaceCode = WhMatQuota.workplaceCode ;
	} 
	searchParam=MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

});