/**
 * 
 * 消耗配件库存查询js
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatStockQuery');
	
	/** ************** 定义全局变量开始 ************** */
	MatStockQuery.searchParams = {};
	MatStockQuery.labelWidth = 100;
	MatStockQuery.fieldWidth = 140;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义查询表单开始 ************** */
	MatStockQuery.searchForm = new Ext.form.FormPanel({
		labelWidth: MatStockQuery.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 15px',
		items:[{
			// 查询表单第1行
			baseCls: "x-plain",
			layout: 'column',
			items:[{												// 第1行第1列
				columnWidth: .33,
				layout: 'form',
				items: [{
					fieldLabel: '库房',
					hiddenName: 'whIdx',
					id: 'whIdx_s',
					width: MatStockQuery.fieldWidth,
					xtype:"Base_combo", 
					entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
					queryHql:"from Warehouse where recordStatus=0 and status = 1",	
					fields:["wareHouseID","wareHouseName","idx"],
					displayField:"wareHouseName",valueField:"idx"
				}]
			}, {    // 第1行第2列	
				columnWidth: .33,
				layout: 'form',
				items: [{
					xtype: 'textfield',
					name: 'matType',
					fieldLabel: '物料类型',
					width: MatStockQuery.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .33,
				layout: 'form',
				items: [{
					xtype: 'combo',	        	        
					fieldLabel : '状态',
					name : 'status',
			        hiddenName:'status',
			        store:new Ext.data.SimpleStore({
					    fields: ['K', 'V'],
						data : [[STATUS_ALL, "所有"], [STATUS_MIN, "低于最小保有量"], [STATUS_NOR, "正常"], [STATUS_MAX, "高于最大保有量"]]
					}),
					valueField:'K',
					displayField:'V',
					triggerAction:'all',
					value: STATUS_ALL,
					mode:'local',
					width: MatStockQuery.fieldWidth
				}]
			}]
		}, {
			// 查询表单第2行
			baseCls: "x-plain",
			layout: 'column',
			items:[{
				columnWidth: .33,
				layout: 'form',
				items: [{                   // 第2行第1列
					xtype: 'textfield',
					name: 'matCode',
					fieldLabel: '物料编码',
					width: MatStockQuery.fieldWidth 
					}]
				},{                        // 第2行第2列
				columnWidth: .5,
				layout: 'form',
				items: [{
					xtype: 'textfield',
					name: 'matDesc',
					fieldLabel: '物料描述',
					anchor: '95%'
				}]
			}]
		}],
		buttons: [{
					xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
						var form = MatStockQuery.searchForm.getForm();
						if (form.isValid()) {
							MatStockQuery.grid.store.load();
						}
					}
				}, {
					xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
						MatStockQuery.searchForm.getForm().reset();
						// 重置“入库库房”字段值
						Ext.getCmp('whIdx_s').clearValue();
						// 重新加载表格
						MatStockQuery.grid.store.load();
					}
				}],
		buttonAlign: 'center'
	});
	/** ************** 定义查询表单结束 ************** */
	
	/** ************** 定义主体表格开始 ************** */
	MatStockQuery.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matStockQuery!pageList.action',                 //装载列表数据的请求URL
	    tbar:null,
	    border: true,
	    singleSelect: true,
		fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true
	},{
		header:'库房主键', dataIndex:'whIdx', hidden:true
	},{
		header:'库房', dataIndex:'whName'
	},{
		header:'物料类型', dataIndex:'matType'
	},{
		header:'物料编码', dataIndex:'matCode'
	},{
		header:'物料描述', dataIndex:'matDesc', width: 300
	},{
		header:'单位', dataIndex:'unit'
	},{
		header:'库存数量', dataIndex:'qty'
	},{
		header:'最低保有量', dataIndex:'minQty'
	},{
		header:'最大保有量', dataIndex:'maxQty'
	},{
		header:'状态', renderer : function(value, metadata ,record){
			var qty = record.get('qty');			// 库存数量
			var minQty = record.get('minQty');		// 最低保有量
			var maxQty = record.get('maxQty');		// 最大保有量
			if (qty < minQty) return '低于最小保有量';
			if (qty > maxQty) return '高于最大保有量';
			return '正常';
		}
	}]
	});
	// 取消双击行出现“编辑”页面的事件监听
	MatStockQuery.grid.un('rowdblclick', MatStockQuery.grid.toEditFn, MatStockQuery.grid);
	
	MatStockQuery.grid.store.on('beforeload', function() {
		MatStockQuery.searchParams = MatStockQuery.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(MatStockQuery.searchParams);
		MatStockQuery.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	})
	/** ************** 定义主体表格结束 ************** */
	
	// 页面自适应布局
	MatStockQuery.viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 150,
			border: true,
			collapsible: true,
//			collapseMode:'mini',
			split: true,
			items: [MatStockQuery.searchForm]
		}, {
			// 结果显示列表区域
			region: 'center',
			layout: 'fit',
			height: 200,
			items: [MatStockQuery.grid]
		}]
	});
});