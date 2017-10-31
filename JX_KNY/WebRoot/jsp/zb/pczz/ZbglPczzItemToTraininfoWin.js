Ext.onReady(function(){
	Ext.namespace('ZbglPczzItemToTraininfoWin');                       //定义命名空间
	ZbglPczzItemToTraininfoWin.idx;//普查整治项idx
	ZbglPczzItemToTraininfoWin.searchParam = {};
	
	//应用grid
	ZbglPczzItemToTraininfoWin.grid = new Ext.yunda.Grid({
		loadURL: ctx + "/zbglPczzItemToTraininfo!findZbglPczzItemToTraininfoListByPczzItemID.action",
		deleteURL: ctx + '/zbglPczzItemToTraininfo!delete.action',            //删除数据的请求URL
		autoSubmit: false,
		storeAutoLoad: false,
		isEdit: false,
		defaultTar: false,
		hideRowNumberer: true,
		page: false,
		tbar: ['search','delete',{
    		text:'刷新', iconCls:'refreshIcon', handler:function(){
	    		ZbglPczzItemToTraininfoWin.grid.store.load();
	    	}
    	}],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'车号', dataIndex:'trainNo', width:25
		},{
			header: "车型简称", dataIndex: "trainTypeShortName", width:25
		}],
		toEditFn: function(grid, rowIndex, e){},
		searchFn:function(searchParam){
			ZbglPczzItemToTraininfoWin.searchParam = searchParam;
			this.store.load();
		}
	});
	
	// 表格数据加载前的参数设置
	ZbglPczzItemToTraininfoWin.grid.store.on('beforeload', function(){
		ZbglPczzItemToTraininfoWin.searchParam.idx = ZbglPczzItemToTraininfoWin.idx;
		this.baseParams.entityJson = Ext.util.JSON.encode(ZbglPczzItemToTraininfoWin.searchParam);
	});	
	
	ZbglPczzItemToTraininfoWin.panel = new Ext.Panel({
	    layout: "border", 
	    defaults: {
	    	border: false, layout: "fit"
	    },
	    items: [{
	        region : 'center', items: ZbglPczzItemToTraininfoWin.grid
	    }]
	});
	
	ZbglPczzItemToTraininfoWin.win = new Ext.Window({
	    title:"普查整治项关联机车信息", 
	    layout: 'fit',
		height: 300, width: 400,
		items:ZbglPczzItemToTraininfoWin.panel,
		closable : false,
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				ZbglPczzItemToTraininfoWin.win.hide();
			}
		}]
	});
	
	ZbglPczzItemToTraininfoWin.showWin = function(idx) {
		//普查整治项idx
		ZbglPczzItemToTraininfoWin.idx = idx;
		
		//加载编辑gird
		ZbglPczzItemToTraininfoWin.grid.store.load();
		
		ZbglPczzItemToTraininfoWin.win.show();
	}
	
	//var viewport = new Ext.Viewport({ layout:'fit', ZbglPczzItemToTraininfoWin.panel });
});
