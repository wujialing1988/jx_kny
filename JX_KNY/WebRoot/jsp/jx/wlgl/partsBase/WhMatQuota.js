/**
 * 库房保有量 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('WhMatQuota');                       //定义命名空间
WhMatQuota.whIdx = "";//库房id
WhMatQuota.searchParam = {};
//库房列表
WhMatQuota.whGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/warehouse!pageList.action',                 //装载列表数据的请求URL
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'库房名称', dataIndex:'wareHouseName', editor:{  maxLength:50 }
	}]
});
//库房列表点击事件
WhMatQuota.whGrid.on("rowclick", function(grid, idx, e){
        var r = grid.store.getAt(idx);
        WhMatQuota.whIdx = r.get("idx");
        //取消行编辑
    	WhMatQuota.grid.rowEditor.stopEditing(false);
		WhMatQuota.grid.store.load();
})
//只查询已启用的库房记录
WhMatQuota.whGrid.store.on('beforeload', function() {
	this.baseParams.status = STATUS_USE;
})
WhMatQuota.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/whMatQuota!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/whMatQuota!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/whMatQuota!delete.action',            //删除数据的请求URL
    storeAutoLoad : false,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
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
		header:'最大量', dataIndex:'maxQty', editor:{ xtype:'numberfield' },searcher:{disabled:true}
	},{
		header:'使用频率', dataIndex:'useRate', editor:{  maxLength:20 },searcher:{disabled:true}
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:200 },searcher:{disabled:true}
	},{
		header:'维护人', dataIndex:'maintainEmp', editor:{ disabled: true, maxLength:25 },searcher:{disabled:true}
	},{
		header:'维护日期', dataIndex:'maintainDate', xtype:'datecolumn', editor:{ disabled: true,xtype:'my97date' },searcher:{disabled:true}
	}],
	tbar: ["search",{ text:'批量添加', iconCls:'chart_attributeConfigIcon', handler:function(){
//			        			PartsAccount.manageDeptId = Ext.getCmp("PartsAboardExWh_whIdx").getValue();
					        	if(WhMatQuota.whIdx == ""){
						    		MyExt.Msg.alert("请选择库房！");
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
		var maxQty = record.data.maxQty ;
		if("" != minQty && "" != maxQty && minQty > maxQty){
			MyExt.Msg.alert("最低保有量不能大于最高保有量！");
			return ;
		}
		if("" != minQty){
			minQty = parseInt(minQty);
			if(minQty > 2147483647){
		    	MyExt.Msg.alert("输入的数量超过了系统最大值 2147483647 ！请重新输入");
				return;
		    }
		}
		if("" != maxQty){
			maxQty = parseInt(maxQty);
			if(maxQty > 2147483647){
		    	MyExt.Msg.alert("输入的数量超过了系统最大值 2147483647 ！请重新输入");
				return;
		    }
		}
        return true;
    }
	 
});
 WhMatQuota.grid.store.on('beforeload',function(){
	var searchParam = WhMatQuota.searchParam;
	if(WhMatQuota.whIdx == ""){
		searchParam.whIdx = "####";
	}else searchParam.whIdx = WhMatQuota.whIdx ;
	searchParam=MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'border', 
	items:[{
			layout: "fit",
			width:'300',
			region : 'west',
	        bodyBorder: false,
	        autoScroll : true,
//	        collapseMode:'mini',
	        items : [ WhMatQuota.whGrid ]
	},{
			layout: "fit",
			region : 'center',
	        bodyBorder: false,
	        autoScroll : true,
	        items : [ WhMatQuota.grid ]
	}]});
});