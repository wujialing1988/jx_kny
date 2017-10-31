/**
 * 计划检修确认 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	// 定义命名空间
	Ext.namespace('RepairRecord');  
	
	/** **************** 定义全局函数开始 **************** */
	// 设备基本信息展示
	RepairRecord.initFn = function() {
		RepairRecord.tpl.overwrite(Ext.get('equipment_base_info'), RepairRecord.record.data);
		var entityJson = { taskListIdx : RepairRecord.record.id, repairType: REPAIR_TYPE_JX};
		RepairRecord.grid.store.load({
			params: {
				entityJson: Ext.encode(entityJson)
			}
		});
	}
	/** **************** 定义全局函数结束 **************** */
	
	// 检修工单列车基本信息
	RepairRecord.tpl = new Ext.XTemplate(
		'<table border="0" cellpadding="0" cellspacing="0">',
			'<tr>',
				'<td style="width:28%;"><span>设备编码：</span>{equipmentCode}</td>',
				'<td style="width:36%;"><span>设备名称：</span>{equipmentName}</td>',
				'<td style="width:36%;"><span>型号：</span>{model}</td>',
			'</tr>',
			'<tr>',
				'<td style="width:28%;"><span>使用人：</span>{usePerson}</td>',
				'<td style="width:36%;"><span>检修开始时间：</span>{[fm.date(new Date(values.realBeginTime), "Y-m-d H:i")]}</td>',
				'<td style="width:36%;"><span>检修结束时间：</span>{[fm.date(new Date(values.realEndTime), "Y-m-d H:i")]}</td>',
			'</tr>',
		'</table>'
	);
	
	// 工单详情列表
	RepairRecord.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/repairScopeCase!queryPageList.action',
		storeAutoLoad: false,
		singleSelect: true,
		tbar : [{
			xtype: 'radio', name: 'repairType', inputValue: REPAIR_TYPE_JX, boxLabel: '机械工单', checked: true,
			listeners: {
				check: function(me, checked) {
					if (checked) {
						RepairRecord.repairType = me.getRawValue();
						RepairRecord.grid.store.load();
					}
				}
			}
		}, {
			xtype: 'radio', name: 'repairType', inputValue: REPAIR_TYPE_DQ, boxLabel: '电气工单',
			listeners: {
				check: function(me, checked) {
					if (checked) {
						RepairRecord.repairType = me.getRawValue();
						RepairRecord.grid.store.load();
					}
				}
			}
		}],
		fields: [{
			header: '范围定义主键', dataIndex:'idx', hidden: true
		},{
			header: '检修范围名称', dataIndex:'repairItemName'
		}],
		// 取消行双击事件监听
		toEditFn: Ext.emptyFn
	});
	
	// 表格加载过滤条件
	RepairRecord.grid.store.on('beforeload', function() {
		var entityJson = {
				taskListIdx : RepairRecord.record.id,
				repairType: RepairRecord.repairType
		};
		this.baseParams.entityJson = Ext.encode(entityJson);
	});
	
	// 选择首行
	RepairRecord.grid.store.on('load', function(store, records, options ) {
		if (0 < store.getCount()) {
			RepairRecord.grid.getSelectionModel().selectFirstRow();
		}
	});
	
	// 选择范围定义主键idx
	RepairRecord.grid.getSelectionModel().on('rowselect', function(sm, rowIndex, r) {
		if (sm.getCount() != 1) {
			return;
		}
		RepairRecord.scopeCaseIdx = r.id;
		RepairRecord.recordGrid.store.load();
	});
	
	// 选择多行移除所有项目详情
	RepairRecord.grid.getSelectionModel().on('selectionchange', function(sm){
		if (sm.getCount() != 1) {
			RepairRecord.recordGrid.getStore().removeAll();
		}
	});
	
	// 检修项目明细列表
	RepairRecord.recordGrid = new Ext.yunda.Grid({
		loadURL: ctx + '/repairWorkOrder!pageList.action',
		storeAutoLoad: false,
		singleSelect: true,
		tbar : null,
		fields: [{
				header: 'idx主键', dataIndex:'idx', hidden: true
			},{
				header: '作业内容', dataIndex:'workContent'
			}, {
				header: '施修人', dataIndex:'workerName', width: 30
			},{
				header: '实修记录', dataIndex:'repairRecord', width: 30, renderer: function(v, m, r, rowIndex) {
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
	RepairRecord.recordGrid.store.on('beforeload', function() {
		if (Ext.isEmpty(RepairRecord.scopeCaseIdx)) {
			return false;
		}
		var entityJson = {
				scopeCaseIdx: RepairRecord.scopeCaseIdx
		};
		this.baseParams.entityJson = Ext.encode(entityJson);
	});
	
	// 工单详情窗口
	RepairRecord.win = new Ext.Window({
		title: '设备检修使用人确认',
		width: 1000, height: 550,
		closeAction: 'hide', plain: true, modal:true, layout: 'border',
		tbar: [{
        	text: '返回', iconCls: 'backIcon', tooltip: '点击返回！', handler: function() {
        		this.findParentByType('window').hide();
        	}
		},  '->', '-', {
			text: '上一条', iconCls:'moveUpIcon', handler: function() {
				var record = RepairConfirm.grid.getStore().getAt(--RepairRecord.rowIndex);
				if (Ext.isEmpty(record)) {
					RepairRecord.rowIndex++;
					MyExt.Msg.alert('已经是第一条记录！');
					return;
				}
				RepairRecord.grid.getTopToolbar().findByType('radio')[0].reset();
				RepairRecord.record = record;
				RepairRecord.initFn();
			}
		}, {
			text: '下一条', iconCls:'moveDownIcon', handler: function() {
				var record = RepairConfirm.grid.getStore().getAt(++RepairRecord.rowIndex);
				if (Ext.isEmpty(record)) {
					RepairRecord.rowIndex--;
					MyExt.Msg.alert('已经是最后一条记录！');
					return;
				}
				RepairRecord.grid.getTopToolbar().findByType('radio')[0].reset();
				RepairRecord.record = record;
				RepairRecord.initFn();
			}
		}],
		defaults: {	region: 'center', layout: 'fit'	},
		items: [{
			region: 'north', height: 90, baseCls: 'plain',
			items: [{
				xtype: 'fieldset', title: '检修信息', html: '<div id="equipment_base_info" class="tpl-3col-table"></div>'
			}]
		}, {
			region: 'west', width: 500,
			items: RepairRecord.grid
		}, {
			items: RepairRecord.recordGrid
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '确认', iconCls: 'wrenchIcon', handler: function() {
			 	var ids = [];
	    	    ids.push(RepairRecord.record.id);
				$yd.request({
					url: ctx + '/repairTaskList!repairTaskList.action',
	        		params: {
        				ids: Ext.encode(ids)
        			},
        		}, function () {
        			alertSuccess();
        			RepairConfirm.grid.store.reload();
        			// 确认后自动跳转到下一个检修任务单
        			var record = RepairConfirm.grid.getStore().getAt(++RepairRecord.rowIndex);
        			// 若其后没有更多的检修任务单，则隐藏“使用人确认窗口”
    				if (Ext.isEmpty(record)) {
    					RepairRecord.rowIndex--;
    					RepairRecord.win.hide();
    					return;
    				}
    				RepairRecord.grid.getTopToolbar().findByType('radio')[0].reset();
    				RepairRecord.record = record;
    				RepairRecord.initFn();
        		});
			}
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				RepairRecord.win.hide();
			}
		}],
		listeners: {
			show: function() {
				RepairRecord.initFn();
			}
		}
	})
});