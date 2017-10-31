Ext.onReady(function() {
	Ext.ns('RepairWorkOrderStuff');
	
	/** **************** 定义全局变量开始 **************** */
	RepairWorkOrderStuff.repairWorkOrderIdx = null;
	/** **************** 定义全局变量结束 **************** */
	
	RepairWorkOrderStuff.grid = new Ext.yunda.RowEditorGrid({
		loadURL: ctx + '/repairWorkOrderStuff!pageList.action',
		saveURL: ctx + '/repairWorkOrderStuff!saveOrUpdate.action',
		deleteURL: ctx + '/repairWorkOrderStuff!logicDelete.action',
		tbar: ['<span style="color:red;font-weight:bold;">检修用料：</span>', 'add', 'delete'],
		
		fields: [{
			dataIndex: 'idx', header: 'idx主键', hidden: true
		}, {
			dataIndex: 'repairWorkOrderIdx', header: '设备检修工单idx主键', hidden: true
		}, {
			dataIndex: 'stuffName', header: '材料名称规格', editor: {
				id: 'stuffName',
				maxLength: 32, allowBlank: false, forceSelection: false,
				xtype: 'simpleentitycombo',
				// 指定实体的全路径类名称
				entity: 'com.yunda.sb.repair.material.entity.StuffClass', 
				hiddenName: 'certificateIdx',
				valueField: 'stuffName', displayField: 'stuffName',
				fields: ['idx', 'stuffName', 'stuffNamePY', 'stuffUnitPrice'],
				returnField: [{
					widgetId: 'stuffUnitPrice', propertyName: 'stuffUnitPrice'
				}]
			}
		}, {
			dataIndex: 'stuffUnitPrice', header: '单价（元）', editor: {
				id: 'stuffUnitPrice', maxLength: 6, vtype: 'positiveFloat', allowBlank: false, listeners: {
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
				if (v) return '&yen;&nbsp;' + parseInt(v).toFixed(2);
			}
		}, {
			dataIndex: 'stuffNumber', header: '数量', editor: {
				id: 'stuffNumber', maxLength: 5, vtype: 'positiveFloat', allowBlank: false, enableKeyEvents: true, listeners: {
					keyup: function( me, e ) {
						var stuffNumber = this.getValue();
						var stuffUnitPrice = Ext.getCmp('stuffUnitPrice').getValue();
						if (stuffNumber && stuffUnitPrice) {
							Ext.getCmp('stuffTotalMoney').setValue(Math.round(stuffNumber * stuffUnitPrice * 1000) / 1000);
						}
					}
//					change: function() {
//						var stuffNumber = this.getValue();
//						var stuffUnitPrice = Ext.getCmp('stuffUnitPrice').getValue();
//						if (stuffNumber && stuffUnitPrice) {
//							Ext.getCmp('stuffTotalMoney').setValue(Math.round(stuffNumber * stuffUnitPrice * 1000) / 1000);
//						}
//					}
				}
			}
		}, {
			dataIndex: 'stuffUnit', header: '计量单位', hidden: true
		}, {
			dataIndex: 'stuffTotalMoney', header: '金额 （元）', editor: {
				id: 'stuffTotalMoney', maxLength: 13, vtype: 'positiveFloat', allowBlank: false
			}, renderer: function(v) {
				// 小数点后保留两位
				if (v) return '&yen;&nbsp;' + parseInt(v).toFixed(2);
			}
		}],
		
		beforeSaveFn: function(rowEditor, changes, record, rowIndex) {
			record.data.stuffNamePY = pinyin.getCamelChars(record.data.stuffName);
	        return true;
	    },
	    
		beforeAddButtonFn: function() {
			if (RepairWorkOrder.grid && 0 >= RepairWorkOrder.grid.store.getCount()) {
				MyExt.Msg.alert('请先选择要进行维护的检修作业工单！');
				return false;
			}
			this.defaultData = {
				repairWorkOrderIdx: RepairWorkOrderStuff.repairWorkOrderIdx
			}
			return true;
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
	RepairWorkOrderStuff.grid.rowEditor.on('hide', function() {
		Ext.getCmp('stuffName').clearValue();
	});
});