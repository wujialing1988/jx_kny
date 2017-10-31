/**
 * 巡检下设备确认 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	// 定义命名空间
	Ext.namespace('InspectPlanRecord');  
	
	/** **************** 定义全局函数开始 **************** */
	// 巡检计划信息展示
	InspectPlanRecord.initFn = function() {
		InspectPlanRecord.tpl.overwrite(Ext.get('equipment_base_info2'), InspectPlanRecord.record.data);
		var entityJson = { planIdx : InspectPlanRecord.record.id };
		InspectPlanRecord.grid.store.load({
			params: {
				entityJson: Ext.encode(entityJson)
			}
		});
		Ext.getCmp('equipmentCode_combo').whereList = [{sql: "idx IN (Select equipmentIdx From InspectPlanEquipment Where planIdx = '"+ InspectPlanRecord.record.id +"' And useWorker is null And checkResult = '"+ CHECK_RESULT_YXJ +"')", compare: Condition.SQL}];
		Ext.getCmp('equipmentCode_combo').reload();
	}
	/** **************** 定义全局函数结束 **************** */
	
	// 巡检计划基本信息
	InspectPlanRecord.tpl = new Ext.XTemplate(
		'<table border="0" cellpadding="0" cellspacing="0">',
			'<tr>',
				'<td><span>巡检线路名称：</span>{routeName}</td>',
				'<td><span>巡检周期：</span>{periodType}</td>',
				'<td><span>巡检人：</span>{partrolWorker}</td>',
			'</tr>',
			'<tr>',
			 '<td><span>计划开始时间：</span>{[fm.date(new Date(values.planStartDate), "Y-m-d")]}</td>',
			 '<td><span>计划结束时间：</span>{[fm.date(new Date(values.planEndDate), "Y-m-d")]}</td>',
			'</tr>',
		'</table>'
	);
	
	// 巡检设备列表
	InspectPlanRecord.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/inspectPlanEquipment!queryPageList2User.action',
		storeAutoLoad: false,
		tbar : [{
			text: '确认', iconCls: 'wrenchIcon', tooltip: '确认当前巡检计划下已勾选的巡检设备！', handler: function() {
	    		var data = InspectPlanRecord.grid.selModel.getSelections();
	    		if (Ext.isEmpty(data)) {
	    			MyExt.Msg.alert('尚未选择任何记录！');
	    			return;
	    		}
	    	    var ids = [];
	    	    for (var i = 0; i < data.length; i++){
	    	        ids.push(data[ i ].get("idx"));
	    	    }
	    	    // Modified by hetao on 2017-02-22 取消操作前的确认提示框
	    	    $yd.request({
	    	    	url: ctx + '/inspectPlanEquipment!confirm.action',
	    	    	params: {
	    	    		ids: Ext.encode(ids)
	    	    	}
	    	    }, function (result) {
	    	    	alertSuccess();
	    	    	InspectPlanRecord.grid.store.reload();
	    	    	InspectPlanConfirm.grid.store.reload();
	    	    });
//				Ext.Msg.confirm('提示', '确认所选的已巡检设备，是否确认继续？', function(btn) {
//					if ('yes' == btn) {
//					}
//				});
			}
		}, '->', {
			id: 'equipmentCode_combo',
			xtype: 'singlefieldcombo',
			entity: 'com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo', 
			xfield: 'equipmentCode',
			enableKeyEvents: true,
			width: 140, emptyText: '输入设备编码查询...',
			listeners: {
				select: function() {
					InspectPlanRecord.grid.store.load();
				},
				change: function() {
					InspectPlanRecord.grid.store.load();
				},
				keyup: function(me, e) {
					if (e.keyCode === e.ENTER) {
						InspectPlanRecord.grid.store.load();
					}
				}
			}
		}, {
	    	text: '重置', iconCls: 'resetIcon', handler: function() {
	    		var tbar = this.findParentByType('toolbar');
	    		tbar.findByType('singlefieldcombo')[0].reset();
	    		InspectPlanRecord.grid.store.load();
	    	}
	    }],
		fields: [{
			header: 'idx主键', dataIndex:'idx', hidden: true
		},{
			header: '巡检周期计划idx主键', dataIndex:'planIdx', hidden: true
		},{
			header: '设备主键', dataIndex:'equipmentIdx', hidden: true
		},{
			header: '设备名称', dataIndex:'equipmentName'
		},{
			header: '设备编码', dataIndex:'equipmentCode'
		},{
			header: '型号', dataIndex:'model'
		},{
			header: '设置地点', dataIndex:'usePlace'
		}],
		// 取消行双击事件监听
		toEditFn: Ext.emptyFn
	});
	
	// 巡检设备列表加载前过滤条件
	InspectPlanRecord.grid.store.on('beforeload', function() {
		var equipmentCode = InspectPlanRecord.grid.getTopToolbar().find('xtype', 'singlefieldcombo')[0].getRawValue();
		var entityJson = {
			planIdx : InspectPlanRecord.record.id,
			equipmentCode: equipmentCode
		};
		this.baseParams.entityJson = Ext.encode(MyJson.deleteBlankProp(entityJson));
	});
	
	// 选择首行
	InspectPlanRecord.grid.store.on('load', function(store, records, options ) {
		if (0 < store.getCount()) {
			InspectPlanRecord.grid.getSelectionModel().selectFirstRow();
		} else {
			// 如果该巡检计划下所有设备已被确认，则自动跳转到下一条巡检计划
			InspectPlanRecord.planEquipmentIdx = null;
			var record = InspectPlanConfirm.grid.getStore().getAt(++InspectPlanRecord.rowIndex);
			// 若其后没有更多的巡检计划，则隐藏“使用人确认窗口”
			if (Ext.isEmpty(record)) {
				InspectPlanRecord.win.hide();
				return;
			}
			InspectPlanRecord.recordGrid.getTopToolbar().findByType('radio')[0].reset();
			InspectPlanRecord.record = record;
			InspectPlanRecord.initFn();
		}
	});
	
	// 选择传递设备idx
	InspectPlanRecord.grid.getSelectionModel().on('rowselect', function(sm, rowIndex, r) {
		if (sm.getCount() != 1) {
			return;
		}
		InspectPlanRecord.planEquipmentIdx = r.id;
		InspectPlanRecord.repairType = REPAIR_TYPE_JX;
		InspectPlanRecord.recordGrid.store.load();
	});
	
	// 选择多行移除所有项目详情
	InspectPlanRecord.grid.getSelectionModel().on('selectionchange', function(sm){
		if (sm.getCount() != 1) {
			InspectPlanRecord.recordGrid.getStore().removeAll();
		}
	});
	
	// 设备巡检项目列表
	InspectPlanRecord.recordGrid = new Ext.yunda.Grid({
		loadURL: ctx + '/inspectRecord!queryPageList.action',
		storeAutoLoad: true,
		singleSelect: true,
		tbar : [{
			xtype: 'radio', name: 'repairType', inputValue: REPAIR_TYPE_JX, boxLabel: '机械类型', checked: true,
			listeners: {
				check: function(me, checked) {
					if (checked) {
						InspectPlanRecord.repairType = me.getRawValue();
						InspectPlanRecord.recordGrid.store.load();
					}
				}
			}
		}, {
			xtype: 'radio', name: 'repairType', inputValue: REPAIR_TYPE_DQ, boxLabel: '电气类型',
			listeners: {
				check: function(me, checked) {
					if (checked) {
						InspectPlanRecord.repairType = me.getRawValue();
						InspectPlanRecord.recordGrid.store.load();
					}
				}
			}
		}],
		fields: [{
				header: 'idx主键', dataIndex:'idx', hidden: true
			},{
				header: '巡检设备主键idx', dataIndex:'planEquipmentIdx', hidden: true
			},{
				header: '检查项目', dataIndex:'checkItem', width: 300
			},{
				header: '检查结果', dataIndex:'checkResult', width: 50, renderer: function(v, m, r, rowIndex) {
					if ("合格" === v) {
						return '<div style="background:#0f0;width:50px;height:18px;line-height:18px;text-align:center;margin-left:10px;"><p>' + v + '</p></div>';
					}else {
						return '<div style="background:#FF0000;width:50px;height:18px;line-height:18px;text-align:center;margin-left:10px;"><p>' + v + '</p></div>';
					}
				}
		}],
		// 取消行双击事件监听
		toEditFn: Ext.emptyFn
	});
	
	// 设备巡检项目列表加载前过滤条件
	InspectPlanRecord.recordGrid.store.on('beforeload', function() {
		if (Ext.isEmpty(InspectPlanRecord.planEquipmentIdx)) {
			return false;
		}
		var entityJson = {
			planEquipmentIdx: InspectPlanRecord.planEquipmentIdx,
			repairType: InspectPlanRecord.repairType
		};
		this.baseParams.entityJson = Ext.encode(entityJson);
	});
	
	// 详情窗口
	InspectPlanRecord.win = new Ext.Window({
		title: '设备巡检使用人确认',
		width: 1000, height: 550,
		closeAction: 'hide', plain: true, modal:true, layout: 'border',
		tbar: [{
        	text: '返回', iconCls: 'backIcon', tooltip: '点击返回！', handler: function() {
        		this.findParentByType('window').hide();
        	}
		},  '->', '-', {
			text: '上一条', iconCls:'moveUpIcon', handler: function() {
				var record = InspectPlanConfirm.grid.getStore().getAt(--InspectPlanRecord.rowIndex);
				if (Ext.isEmpty(record)) {
					InspectPlanRecord.rowIndex++;
					MyExt.Msg.alert('已经是第一条记录！');
					return;
				}
				InspectPlanRecord.recordGrid.getTopToolbar().findByType('radio')[0].reset();
				InspectPlanRecord.record = record;
				InspectPlanRecord.initFn();
			}
		}, {
			text: '下一条', iconCls:'moveDownIcon', handler: function() {
				var record = InspectPlanConfirm.grid.getStore().getAt(++InspectPlanRecord.rowIndex);
				if (Ext.isEmpty(record)) {
					InspectPlanRecord.rowIndex--;
					MyExt.Msg.alert('已经是最后一条记录！');
					return;
				}
				InspectPlanRecord.recordGrid.getTopToolbar().findByType('radio')[0].reset();
				InspectPlanRecord.record = record;
				InspectPlanRecord.initFn();
			}
		}],
		defaults: {	region: 'center', layout: 'fit'	},
		items: [{
			region: 'north', height: 80, baseCls: 'plain',
			items: [{
				xtype: 'fieldset', title: '检修信息', html: '<div id="equipment_base_info2" class="tpl-3col-table"></div>'
			}]
		}, {
			region: 'west', width: 500,
			items:  InspectPlanRecord.grid
		}, {
			items: InspectPlanRecord.recordGrid
		}],
		buttonAlign: 'center',
		buttons: [{
			// Modified by hetao on 2017-02-22 增加批量确认巡检设备的功能
			text: '确认', iconCls: 'wrenchIcon', tooltip: '批量确认当前巡检计划下所有巡检设备！', handler: function() {
			 	var ids = [];
			 	InspectPlanRecord.grid.store.each(function(r){
			 		ids.push(r.id);
			 	}, ids);
	    	    $yd.request({
	    	    	url: ctx + '/inspectPlanEquipment!confirm.action',
	    	    	params: {
	    	    		ids: Ext.encode(ids)
	    	    	}
	    	    }, function (result) {
	    	    	alertSuccess();
	    	    	InspectPlanRecord.grid.store.reload();
	    	    	InspectPlanConfirm.grid.store.reload();
	    	    });
			}
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				InspectPlanRecord.win.hide();
			}
		}],
		listeners: {
			show: function() {
				InspectPlanRecord.initFn();
			}
		}
	})
});