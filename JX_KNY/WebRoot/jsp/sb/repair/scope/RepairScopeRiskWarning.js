Ext.onReady(function() {
	
	Ext.ns('RepairScopeRiskWarning');
	
	/** **************** 定义私有变量开始 **************** */
	var grid;
	/** **************** 定义私有变量结束 **************** */
	
	/**
	 * 安全风险点列表
	 */
	grid = new Ext.yunda.RowEditorGrid({
		loadURL: ctx + '/repairScopeRiskWarning!pageQuery.action',
		saveURL: ctx + '/repairScopeRiskWarning!saveOrUpdate.action',
		deleteURL: ctx + '/repairScopeRiskWarning!logicDelete.action',
		tbar: ['add', 'delete'],
		storeAutoLoad: false, singleSelect: true,
		fields: [{
			dataIndex: 'idx', header: 'idx主键', hidden: true
		}, {
			dataIndex: 'scopeIdx', header: '检修范围主键', hidden: true
		}, {
			dataIndex: 'riskItem', header: '安全风险点', editor: {
				xtype: 'textarea', maxLength: 300, allowBlank: false
			}
		}],
		beforeSaveFn: function(rowEditor, changes, record, rowIndex) {
			record.data.scopeIdx = RepairScopeRiskWarning.scopeIdx;
	        return true;
	    }
	});
	grid.store.on('beforeload', function() {
		var whereList = [];
		whereList.push({propName: 'scopeIdx', propValue: RepairScopeRiskWarning.scopeIdx, stringLike: false});
		this.baseParams.whereListJson = Ext.encode(whereList);
	});
	
	/**
	 * 安全风险点维护窗口
	 */
	RepairScopeRiskWarning.win = new Ext.Window({
		title: '安全风险点维护', iconCls: 'edit1Icon',
		height: 300, width: 550,
		closeAction: 'hide',
		modal: true,
		layout: 'fit',
		items: [ grid ],
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}],
		listeners: {
			show: function() {
				grid.store.load();
			},
			hide: function() {
				grid.store.removeAll();
			}
		}
	});
	
});