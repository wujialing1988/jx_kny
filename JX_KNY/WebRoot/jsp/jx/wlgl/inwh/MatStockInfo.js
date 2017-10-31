/**
 * 查看库位信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('MatStockInfo');                       //定义命名空间
MatStockInfo.searchParam = {} ;
MatStockInfo.whIdx = "" ;
MatStockInfo.matType = "" ;
MatStockInfo.searchForm=new Ext.form.FormPanel({
	  layout:'column',baeCls:'x-plain',labelWidth:60,
			items:[{												
				columnWidth: .35,
				layout: 'form',
				items: [{
					name: 'locationName',
					fieldLabel: '库位',
					xtype: 'textfield',
					width: 80
				}]
			}, {													
				columnWidth: .5,
				layout: 'form',
				items: [{
					name: 'matDesc',
					fieldLabel: '物料描述',
					xtype: 'textfield',
					anchor: '95%'
				}]
			}, {												
				columnWidth: .15,
				layout: 'form',
				items: [{
					xtype:'button',text:'查询',handler:function(){
			        MatStockInfo.searchParam = MatStockInfo.searchForm.getForm().getValues();
			        MatStockInfo.grid.store.load();
//			     	MatStockInfo.grid.searchFn(MatStockInfo.searchParam);
		     }
				}]
			}]
	 });
	MatStockInfo.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matStock!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matStock!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matStock!logicDelete.action',            //删除数据的请求URL
	    storeAutoLoad : false ,singleSelect:true,
	    tbar:[],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'库房主键', dataIndex:'whIdx', hidden:true, editor:{ xtype:'hidden'  }
		},{
			header:'库房名称', dataIndex:'whName', hidden:true, editor:{ xtype:'hidden'  }
		},{
			header:'库位', dataIndex:'locationName', editor:{  maxLength:50 }
		},{
			header:'物料编码', dataIndex:'matCode', editor:{  maxLength:50 }
		},{
			header:'物料描述', dataIndex:'matDesc', editor:{  maxLength:100 }
		},{
			header:'单位', dataIndex:'unit', editor:{  maxLength:20 }
		},{
			header:'数量', dataIndex:'qty', editor:{ xtype:'numberfield' }
		}]
	});
	MatStockInfo.grid.un("rowdblclick",MatStockInfo.grid.toEditFn,MatStockInfo.grid);
	MatStockInfo.grid.store.on('beforeload', function() {
		MatStockInfo.searchParam = MatStockInfo.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(MatStockInfo.searchParam);
		searchParams.whIdx = MatStockInfo.whIdx ;
		searchParams.matType = MatStockInfo.matType ;
		MatStockInfo.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	//库位存放情况查看窗口
	MatStockInfo.stockInfoWin = new Ext.Window({
		title: "库位存放情况", maximizable:false, layout: "fit", width:600,height:400,
		closeAction: "hide", modal: true, maximized: false , buttonAlign:"center",
		items: {
			xtype: "panel", layout: "border",
			items:[{
	            region: 'north', layout: "fit",frame:true,baeCls:'x-plain',height: 60, bodyBorder: false,items:[MatStockInfo.searchForm]
	        },{
	            region : 'center', layout : 'fit', bodyBorder: false, items : [ MatStockInfo.grid]
	        }]
		},
		buttons:[{
			text: "关闭", iconCls: "closeIcon", handler: function(){MatStockInfo.stockInfoWin.hide();}
		}]
	});
});