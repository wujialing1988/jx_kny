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
		header:i18n.WhMatQuota.idx, dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:i18n.WhMatQuota.workplaceCode, dataIndex:'workplaceCode', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:i18n.WhMatQuota.workplaceName, dataIndex:'workplaceName',hidden:true, editor:{  disabled: true,maxLength:100 }
	},{
		header:i18n.WhMatQuota.whIdx, dataIndex:'whIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:i18n.WhMatQuota.whName, dataIndex:'whName',hidden:true, editor:{  disabled: true,maxLength:50 }
	},{
		header:i18n.WhMatQuota.matCode, dataIndex:'matCode', editor:{  disabled: true,maxLength:50 }
	},{
		header:i18n.WhMatQuota.matDesc, dataIndex:'matDesc', editor:{ disabled: true, maxLength:100 }
	},{
		header:i18n.WhMatQuota.matType, dataIndex:'matType', editor:{ disabled: true, maxLength:100 },searcher:{disabled:true}
	},{
		header:i18n.WhMatQuota.unit, dataIndex:'unit', editor:{ disabled: true, maxLength:20 },searcher:{disabled:true}
	},{
		header:i18n.WhMatQuota.minQty, dataIndex:'minQty', editor:{ xtype:'numberfield' },searcher:{disabled:true}
	},{
		header:i18n.WhMatQuota.maxQty, dataIndex:'maxQty',hidden:true, editor:{ xtype:'numberfield' },searcher:{disabled:true}
	},{
		header:i18n.WhMatQuota.currentQty, dataIndex:'currentQty', editor:{ xtype:'numberfield' },searcher:{disabled:true}
	},{
		header:i18n.WhMatQuota.useRate, dataIndex:'useRate',hidden:true, editor:{  maxLength:20 },searcher:{disabled:true}
	},{
		header:i18n.WhMatQuota.remarks, dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:200 },searcher:{disabled:true}
	},{
		header:i18n.WhMatQuota.maintainEmp, dataIndex:'maintainEmp',hidden:true, editor:{ disabled: true, maxLength:25 },searcher:{disabled:true}
	},{
		header:i18n.WhMatQuota.maintainDate, dataIndex:'maintainDate', hidden:true, xtype:'datecolumn', editor:{ disabled: true,xtype:'my97date' },searcher:{disabled:true}
	}],
	tbar: ["search",{ text:i18n.WhMatQuota.AddInBatches, iconCls:'chart_attributeConfigIcon', handler:function(){
//			        			PartsAccount.manageDeptId = Ext.getCmp("PartsAboardExWh_whIdx").getValue();
					        	if(WhMatQuota.workplaceCode == ""){
						    		MyExt.Msg.alert(i18n.WhMatQuota.choiceWP);
						    		return ;
						    	}
			           	        MatTypeList.selectWin.show();
			           	        MatTypeList.grid.store.load();
						
			        }},"delete",{
			    	text: i18n.WhMatQuota.refresh, iconCls: "refreshIcon", handler: function(){self.location.reload();}
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
		    	MyExt.Msg.alert(i18n.WhMatQuota.maxValue);
				return;
		    }
		}
		if("" != currentQty){
			currentQty = parseInt(currentQty);
			if(currentQty > 9999999){
		    	MyExt.Msg.alert(i18n.WhMatQuota.maxValue);
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