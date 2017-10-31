/**
 * 设备故障统计查询
 */
Ext.onReady(function() {
	
	// 自定义命名空间
	Ext.ns('FaultStatisticsDetail');
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 窗口组件初始化函数
	 */
	FaultStatisticsDetail.initFn = function() {
		// 渲染巡检目录明细展示信息
		FaultStatisticsDetail.tpl.overwrite(Ext.get('equipment_base_info'), FaultStatisticsDetail.record.data);
		// 设置统计日期
		Ext.getCmp('id_statistics_detail_date').setValue(Ext.getCmp('id_statistics_date').getValue());
		// 加载故障提交详情列表数据源
		FaultStatisticsDetail.grid.store.load();
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义设备基本信息模板开始 **************** */
	FaultStatisticsDetail.tpl = new Ext.XTemplate(
		'<table border="0" cellpadding="0" cellspacing="0">',
			'<tr>',
			'<td><span>设备名称：</span>{equipmentName}</td>',
			'<td><span>设备编号：</span>{equipmentCode}</td>',
			'</tr>',
			'<tr>',
			'<td><span>规格：</span>{specification}</td>',
			'<td><span>型号：</span>{model}</td>',
			'</tr>',
			'<tr>',
			'<td><span>制造工厂：</span>{makeFactory}</td>',
			'</tr>',
		'</table>'
	);
	/** **************** 定义设备基本信息模板结束 **************** */
	
	/** **************** 定义设备故障提票详情列表开始 **************** */
	FaultStatisticsDetail.grid = new Ext.yunda.Grid({
		region: 'center',
		loadURL: ctx + '/faultOrder!pageQuery.action',
		viewConfig: false,
		singleSelect: true,
		storeAutoLoad: false,
		tbar: [{
			text: '查看照片', iconCls: 'imageIcon', disabled: false, id: 'id_image_view_btn', handler: function() {
				ImageView.show(FaultStatisticsDetail.grid);
			}
		}, '-', '统计日期：', {
			xtype: 'combo',
			id: 'id_statistics_detail_date',
			store: new Ext.data.ArrayStore({
				fields: ['k', 'v'],
				data: [[0, '全部'], [1, '最近一月'], [3, '最近一季'], [6, '最近半年'], [12, '最近一年']]
			}),
			valueField: 'k', displayField: 'v',
			value: 0,
			triggerAction: 'all',
			mode: 'local',
			width: 100,
			listeners: {
				// 监听combo选择事件，重新加载表格数据源
				select: function( combo, record, index ) {
					FaultStatisticsDetail.grid.store.load();
				}
			}
		}, '->', '-', '故障等级：', {
			xtype: 'checkbox', checked: true, boxLabel: '一般&nbsp;&nbsp;', inputValue: FAULT_LEVEL_YB,
			listeners: {
				check: function(me) {
					FaultStatisticsDetail.grid.store.load();
				}
			}
		}, {
			xtype: 'checkbox', checked: true, boxLabel: '重大&nbsp;&nbsp;', inputValue: FAULT_LEVEL_ZD,
			listeners: {
				check: function(me) {
					FaultStatisticsDetail.grid.store.load();
				}
			}
		}, {
			xtype: 'checkbox', checked: true, boxLabel: '特大&nbsp;&nbsp;', inputValue: FAULT_LEVEL_TD,
			listeners: {
				check: function(me) {
					FaultStatisticsDetail.grid.store.load();
				}
			}
		}],
		fields: [{
			dataIndex: 'idx', header: 'idx主键', hidden: true
		}, {
			dataIndex: 'equipmentIdx', header: '设备idx主键', hidden: true
		}, {
			dataIndex: 'faultOrderNo', header: '提票单号'
		}, {
			dataIndex: 'equipmentName', header: '设备名称', renderer: function(value, metaData, record) {
				return value + '(' + record.get('equipmentCode') + ')';
			}, width: 200, hidden: true
		}, {
			dataIndex: 'equipmentCode', header: '设备编码', hidden: true
		}, {
			dataIndex: 'model', header: '型号', hidden: true
		}, {
			dataIndex: 'specification', header: '规格', hidden: true
		}, {
			dataIndex: 'submitEmp', header: '提报人', width: 60
		}, {
			dataIndex: 'submitEmpId', header: '提报人id', hidden: true
		}, {
			dataIndex: 'state', header: '提票状态', renderer: function(v) {
				if (STATE_YPG == v) {
					return '调度已派工';
				}
				return v;
			}
		}, {
			dataIndex: 'faultOccurTime', header: '故障发现时间', xtype: 'datecolumn', format: 'Y-m-d H:i', width: 120
		}, {
			dataIndex: 'repairContent', header: '实际修理内容', width: 200
		}, {
			dataIndex: 'faultRecoverTime', header: '故障恢复时间', xtype: 'datecolumn', format: 'Y-m-d H:i', width: 120
		}, {
			dataIndex: 'repairTeam', header: '主修班组'
		}, {
			dataIndex: 'repairTeamId', header: '主修班组ID', hidden: true
		}, {
			dataIndex: 'assistRepairTeam', header: '辅修班组', width: 140
		}, {
			dataIndex: 'assistRepairTeamId', header: '辅修班组ID', hidden: true
		}, {
			dataIndex: 'repairCost', header: '修理费用', hidden: true
		}, {
			dataIndex: 'repairEmp', header: '修理人'
		}, {
			dataIndex: 'assistRepairEmps', header: '辅修人员'
		}, {
			header: "使用人确认", dataIndex: "useWorker", width: 73, renderer:function(v, m, r) {
				m.css = "unEditCell";
				return v;
			}
		}, {
			header: "使用人id", dataIndex: "useWorkerId", hidden: true
		}, {
			dataIndex: 'faultPlace', header: '故障部位及意见'
		}, {
			dataIndex: 'faultPhenomenon', header: '故障现象', width: 200
		}, {
			dataIndex: 'causeAnalysis', header: '原因分析', width: 200
		}, {
			dataIndex: 'repairEmpId', header: '修理人ID', hidden: true
		}, {
			dataIndex: 'useWorker', header: '使用人（保留）', hidden: true
		}, {
			dataIndex: 'useWorkerId', header: '使用人ID（保留）', hidden: true
		}, {
			dataIndex: 'faultLevel', header: '故障等级'
		}],
		
		// 取消行双击事件监听
		toEditFn: Ext.emptyFn
	});
	
	// 查询条件查询传值
	FaultStatisticsDetail.grid.store.on('beforeload', function() {
		whereList = [];
		
		// 查询条件 - 指定设备的故障提票详情
		whereList.push({propName: 'equipmentIdx', propValue: FaultStatisticsDetail.record.data.equipmentIdx});
		
		// 故障等级快速过滤
		var tbar = FaultStatisticsDetail.grid.getTopToolbar();
		var checkboxs = tbar.findByType('checkbox');
		var propValues = [-1];
		for (var i = 0; i < checkboxs.length; i++) {
			if (checkboxs[i].checked) {
				propValues.push(checkboxs[i].getRawValue())
			}
		}
		whereList.push({compare: Condition.IN, propName: 'faultLevel', propValues: propValues});
		
		// 查询条件 - 故障发生日期
		// [1, '最近一月'], [3, '最近一季'], [6, '最近半年'], [12, '最近一年']
		var flag = Ext.getCmp('id_statistics_detail_date').getValue();
		var now = new Date();
		if (1 === flag) {
			now.setMonth(now.getMonth() - 1);
		} else if (3 === flag) {
			now.setMonth(now.getMonth() - 3);
		} else if (6 === flag) {
			now.setMonth(now.getMonth() - 6);
		} else if (12 === flag) {
			now.setFullYear(now.getFullYear() - 1);
		}
		if (0 !== flag) {
			whereList.push({compare: Condition.SQL, sql: "FAULT_OCCUR_TIME >= to_date('"+ now.toLocaleDateString() +" 00 : 00 : 00', 'YYYY/MM/DD HH24:MI:SS')"});
		}
		
		this.baseParams.whereListJson = Ext.encode(whereList);
	});
	/** **************** 定义设备故障提票详情列表结束 **************** */
	
	FaultStatisticsDetail.win = new Ext.Window({
		title: '设备故障明细',
		height: 600, width: 900,
		plain: true,
		closeAction: 'hide',
		layout: 'border',
		tbar: [{
			text: '返回', iconCls: 'backIcon', handler: function() {
				this.ownerCt.ownerCt.hide();
			}
		}, '->', '-', {
			text: '上一条', iconCls:'moveUpIcon', handler: function() {
				var record = FaultStatistics.grid.getStore().getAt(--FaultStatistics.rowIndex);
				if (Ext.isEmpty(record)) {
					FaultStatistics.rowIndex++;
					MyExt.Msg.alert('已经是第一条记录！');
					return;
				}
				FaultStatisticsDetail.record = record;
				FaultStatisticsDetail.initFn();
			}
		}, {
			text: '下一条', iconCls:'moveDownIcon', handler: function() {
				var record = FaultStatistics.grid.getStore().getAt(++FaultStatistics.rowIndex);
				if (Ext.isEmpty(record)) {
					FaultStatistics.rowIndex--;
					MyExt.Msg.alert('已经是最后一条记录！');
					return;
				}
				FaultStatisticsDetail.record = record;
				FaultStatisticsDetail.initFn();
			}
		}],
		defaults: {
			region: 'center', layout: 'fit'
		},
		items: [{
			region: 'north', height: 110, baseCls: 'plain',
			items: [{
				xtype: 'fieldset', title: '设备基本信息', html: '<div id="equipment_base_info"></div>'
			}]
		}, FaultStatisticsDetail.grid],
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.ownerCt.ownerCt.hide();
			}
		}],
		listeners: {
			show: function() {
				// 窗口组件初始化
				FaultStatisticsDetail.initFn();
			}
		}
	});
	
});