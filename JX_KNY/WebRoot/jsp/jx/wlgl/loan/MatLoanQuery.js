 Ext.onReady(function() {
	Ext.namespace('MatLoanQuery');


	/** ************** 定义全局变量开始 ************** */
	MatLoanQuery.searchParams = {};
	MatLoanQuery.labelWidth = 70;
	MatLoanQuery.fieldWidth = 140;
	/** ************** 定义全局变量结束 ************** */
	
 	/** ************** 定义查询表单开始 ************** */
	MatLoanQuery.searchForm = new Ext.form.FormPanel({
		labelWidth: MatLoanQuery.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 15px',
		items:[{
			layout: 'column',
			items:[{										
				columnWidth: .3,
				layout: 'form',
				items: [{
					xtype: 'textfield',
					name: 'loanOrg',
					fieldLabel: '借出单位',
					width: MatLoanQuery.fieldWidth										
				}]
			}, {												
				columnWidth: .3,
				layout: 'form',
				items: [{
					xtype: 'textfield',
					name: 'matCode',
					fieldLabel: '物料编码',
					width: MatLoanQuery.fieldWidth-30
				}]
			}, {									
				columnWidth: .4,
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
						var form = MatLoanQuery.searchForm.getForm();
						if (form.isValid()) {
							MatLoanQuery.grid.store.load();
						}
					}
				}, {
					xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
						MatLoanQuery.searchForm.getForm().reset();
						// 重新加载表格
						MatLoanQuery.grid.store.load();
					}
				}],
		buttonAlign: 'center'
	});
	/** ************** 定义查询表单结束 ************** */
 
 /** ************ 定义表格开始 ************ */	
	MatLoanQuery.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/matLoan!pageList.action',
		storeId: 'idx',
		tbar:['refresh'],
		fields: [{
			dataIndex: 'matType', header: '领料类型'
		}, {
			dataIndex: 'matCode', header: '物料编码'
		}, {
			dataIndex: 'matDesc', header: '物料描述'
		}, {
			dataIndex: 'loanOrg', header: '借出单位'
		}, {
			dataIndex: 'loanQty', header: '借出数量'
		}, {
			dataIndex: 'unit', header: '单位'
		}, {
			dataIndex: 'idx', header: '主键', hidden:true
		}],
		toEditFn: Ext.emptyFn //覆盖双击编辑事件
	});
	/** ************ 定义表格结束 ************ */	
	MatLoanQuery.grid.store.on('beforeload', function() {
		MatLoanQuery.searchParams = MatLoanQuery.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(MatLoanQuery.searchParams);
		MatLoanQuery.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	})
	
	MatLoanQuery.grid.store.sort({field: 'matType', direction: 'ASC'});
	
	/** ************ 布局 ************ */	
	new Ext.Viewport({
		layout: 'border',
		defaults: {layout: 'fit'},
		items: [{
			region: 'north',
			frame:true,
			height: 100,
			items: MatLoanQuery.searchForm
		}, {
			region: 'center',
			frame:true,
			items: MatLoanQuery.grid
		}]
	});
 });
	