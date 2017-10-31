Ext.onReady(function() {
	Ext.ns('RepairWorkOrderStuff');
	
	/** **************** 定义全局变量开始 **************** */
	RepairWorkOrderStuff.repairWorkOrderIdx = null;
	/** **************** 定义全局变量结束 **************** */
	
	RepairWorkOrderStuff.grid = new Ext.yunda.RowEditorGrid({
		loadURL: ctx + '/repairWorkOrderStuff!pageList.action',
		saveURL: ctx + '/repairWorkOrderStuff!saveOrUpdate.action',
		deleteURL: ctx + '/repairWorkOrderStuff!logicDelete.action',
		tbar: ['<span style="color:red;font-weight:bold;">检修用料：</span>'],
		singleSelect: true,
		fields: [{
			dataIndex: 'idx', header: 'idx主键', hidden: true
		}, {
			dataIndex: 'repairWorkOrderIdx', header: '设备检修工单idx主键', hidden: true
		}, {
			dataIndex: 'stuffName', header: '材料名称规格', editor: {
				maxLength: 32
			}
		}, {
			dataIndex: 'stuffUnitPrice', header: '单价（元）', editor: {
				id: 'stuffUnitPrice', maxLength: 5, vtype: 'positiveFloat', allowBlank: false, listeners: {
					change: function() {
						var stuffNumber = Ext.getCmp('stuffNumber').getValue();
						var stuffUnitPrice = this.getValue();
						if (stuffNumber && stuffUnitPrice) {
							Ext.getCmp('stuffTotalMoney').setValue(Math.round(stuffNumber * stuffUnitPrice * 1000) / 1000);
						}
					}
				}
			}, renderer: function(v) {
				// 小数点后保留两位
				if(v) return '&yen;&nbsp;' + v.toFixed(2);
			}
		}, {
			dataIndex: 'stuffNumber', header: '数量', editor: {
				id: 'stuffNumber', maxLength: 5, vtype: 'positiveFloat', allowBlank: false, listeners: {
					change: function() {
						var stuffNumber = this.getValue();
						var stuffUnitPrice = Ext.getCmp('stuffUnitPrice').getValue();
						if (stuffNumber && stuffUnitPrice) {
							Ext.getCmp('stuffTotalMoney').setValue(Math.round(stuffNumber * stuffUnitPrice * 1000) / 1000);
						}
					}
				}
			}
		}, {
			dataIndex: 'stuffUnit', header: '计量单位 ', hidden: true
		}, {
			dataIndex: 'stuffTotalMoney', header: '金额（元）', editor: {
				id: 'stuffTotalMoney', maxLength: 13, vtype: 'positiveFloat', allowBlank: false
			}, renderer: function(v) {
				// 小数点后保留两位
				if(v) return '&yen;&nbsp;' + v.toFixed(2);
			}
		}],
		
		beforeAddButtonFn: function() {  
			this.defaultData = {
				repairWorkOrderIdx: RepairWorkOrderStuff.repairWorkOrderIdx
			}
			return true;
		},
		
		beforeEditFn: function(rowEditor, rowIndex) {
	        return false;
	    }
		
	});
	
	RepairWorkOrderStuff.grid.store.on('beforeload', function() {
		// 隐藏行编辑插件
		RepairWorkOrderStuff.grid.rowEditor.slideHide();
		if (0 >= RepairWorkOrder.grid.store.getCount()) {
			return false;
		}
		this.baseParams.entityJson = Ext.encode({
			repairWorkOrderIdx: RepairWorkOrderStuff.repairWorkOrderIdx
		});
	});
});