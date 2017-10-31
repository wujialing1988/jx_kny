/**
 * 设备检修周期 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {
	
	Ext.ns('RepairPeriod');		// 自定义命名空间
	
	var labelWidth = 130, fieldWidth = 160;
	
	/** **************** 定义表格开始 **************** */
	RepairPeriod.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/repairPeriod!pageList.action',
		saveURL: ctx + '/repairPeriod!saveOrUpdate.action',
		deleteURL: ctx + '/repairPeriod!logicDelete.action',
		labelWidth: labelWidth,
		saveFormColNum: 2,
		viewConfig: false,
		tbar: ['add', 'delete', 'refresh'],
		plugins: [new Ext.ux.grid.ColumnHeaderGroup({
			rows: [[{
				header: "设备类别", colspan: 5, align: 'center'
			}, {
				header: "一般", colspan: 3, align: 'center'
			}, {
				header: "A类", colspan: 3, align: 'center'
			}]],
			hierarchicalColMenu: true
		})],
		fields: [{
			dataIndex: 'idx', header: 'idx主键', hidden: true, editor: {
				xtype: 'hidden'
			}
		}, {
			dataIndex: 'classCode', header: '设备类别', width: fieldWidth, editor: {
				xtype: 'classificationSelect',  hiddenName: 'classCode', allowBlank: false,
				// 除根结点，其它结点都可选
				selectNodeModel: Ext.yunda.snm.exceptRoot,
				returnField: [{
					widgetId: 'className', propertyName: 'className'
				}]
			}
		}, {
			dataIndex: 'className', header: '设备类别名称', width: fieldWidth + 40, editor: {
				id: 'className', disabled: true, allowBlank: false
			}
		}, {
			dataIndex: 'dx', header: '大修(年)', width: fieldWidth, editor: {
				xtype: 'numberfield', length: 2, vtype: 'positiveInt', maxLength: 2, allowBlank: false
			}
		}, {
			dataIndex: 'zx', header: '中修间隔(小修次)', width: fieldWidth, editor: {
				xtype: 'numberfield', length: 2, vtype: 'positiveInt', maxLength: 2, allowBlank: false
			}
		}, {
			dataIndex: 'xx', header: '小修周期(月)', width: fieldWidth, editor: {
				xtype: 'numberfield', length: 2, vtype: 'positiveInt', maxLength: 2, allowBlank: false
			}
		}, {
			dataIndex: 'adx', header: '大修(年)', width: fieldWidth, editor: {
				xtype: 'numberfield', length: 2, vtype: 'positiveInt', maxLength: 2, fieldLabel: 'A类大修(年)', allowBlank: false
			}
		}, {
			dataIndex: 'azx', header: '中修间隔(小修次)', width: fieldWidth, editor: {
				xtype: 'numberfield', length: 2, vtype: 'positiveInt', maxLength: 2, fieldLabel: 'A中修间隔(小修次)', allowBlank: false
			}
		}, {
			dataIndex: 'axx', header: '小修周期(月)', width: fieldWidth, editor: {
				xtype: 'numberfield', length: 2, vtype: 'positiveInt', maxLength: 2, fieldLabel: 'A类小修周期(月)', allowBlank: false
			}
		}],
		
		editOrder: ['classCode', 'className', 'dx', 'adx', 'zx', 'azx', 'xx', 'axx'],
		
		afterShowSaveWin: function() {
			this.saveForm.findByType('classificationSelect')[0].clearValue();
		}, 
		
		afterShowEditWin: function(record, rowIndex) {
			this.saveForm.findByType('classificationSelect')[0].setDisplayValue(record.get('classCode'), record.get('className'));
		},
		
		beforeGetFormData: function() {
			this.saveForm.find('id', 'className')[0].enable();
		},
		afterGetFormData: function() {
			this.saveForm.find('id', 'className')[0].disable();
		}
	});
	RepairPeriod.grid.store.on('beforeload', function() {
		this.baseParams.sort = 'classCode';
		this.baseParams.dir = 'ASC';
	});
	/** **************** 定义表格结束 **************** */
	
	// 页面自适应布局
	new Ext.Viewport({
		layout: 'fit',
		items: RepairPeriod.grid
	});
	
});