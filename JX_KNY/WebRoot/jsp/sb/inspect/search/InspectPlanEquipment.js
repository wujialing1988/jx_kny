Ext.onReady(function() {
	
	Ext.ns('InspectPlanEquipment');
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 显示巡检记录详情
	 */
	InspectPlanEquipment.showInspectRecord = function() {
		var sm = InspectPlanEquipment.grid.getSelectionModel();
		if (0 >= sm.getCount()) {
			MyExt.Msg.alert("尚未选择任何记录！");
			return;
		}
		var record = sm.getSelections()[0];
		var count = record.get('yclCount') + record.get('wclCount');
		if (count <= 0) {
			MyExt.Msg.alert("该设备没有检查项目！");
			return;
		}
		InspectRecord.planEquipmentIdx = record.id;
		if (InspectRecord.win.hidden) {
			InspectRecord.win.show();
		} else {
			// 初始化，加载巡检记录列表数据源，渲染巡检基本情况展示信息
			InspectRecord.initFn();
		}
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义巡检设备列表开始 **************** */
	InspectPlanEquipment.grid = new Ext.yunda.Grid({
		loadURL : ctx + '/inspectPlanEquipment!queryPageList3.action',
		storeAutoLoad : false, //viewConfig : null,
		tbar : [{
			text: '查看照片', iconCls: 'imageIcon', disabled: false, id: 'id_image_view_btn', handler: function() {
				ImageView.show(InspectPlanEquipment.grid);
			}
		}, '-', '统计日期：', {
			xtype: 'combo',
			id: 'id_statistics_date',
			store: new Ext.data.ArrayStore({
				fields: ['k', 'v'],
				data: [[0, '全部'], [1, '最近一月'], [3, '最近一季'], [6, '最近半年'], [12, '最近一年']]
			}),
			valueField: 'k', displayField: 'v',
			value: 1,
			triggerAction: 'all',
			mode: 'local',
			width: 100,
			listeners: {
				// 监听combo选择事件，重新加载表格数据源
				select: function( combo, record, index ) {
					InspectPlanEquipment.grid.store.load();
				}
			}
		}, '->', '<span style="color:gray;">双击一条记录查看巡检情况详情！</span>'],
		singleSelect : true, viewConfig: null,
		fields : [{
			header: "idx", dataIndex: "idx",  hidden: true
		}, {
			header: "计划名称", dataIndex: "routeName", width: 140
		}, {
			dataIndex : 'realBeginTime', header : '实际开工时间', width: 120, align: 'center', renderer: function(v) {
				return v ? new Date(v).format('Y-m-d H:i') : '';
			}
		}, {
			dataIndex : 'realEndTime', header : '实际完工时间', width: 120, align: 'center', renderer: com.yunda.Common.realEndTimeRenderer2XJ
		}, {
			header: "巡检结果", dataIndex: "checkResult", hidden: true, renderer: function(value, metaData, record) {
				if (CHECK_RESULT_YXJ == value) {
					metaData.attr = 'style=color:green;font-weight:bold;';
				} else {
					metaData.attr = 'style=color:red;font-weight:bold;';
				}
				return value;
			}
		}, {
			header: "项目检查进度", dataIndex: "yclCount", width: 110, renderer: com.yunda.Common.yclCountRenderer2XJ
		}, {
			dataIndex : 'planStartDate', header : '计划开工时间', width: 85, renderer: function(v) {
				return v ? new Date(v).format('Y-m-d') : '';
			}
		}, {
			dataIndex : 'planEndDate', header : '计划完工时间', width: 85, renderer: com.yunda.Common.planEndDatetRenderer2XJ
		}, {
			header: "机械巡检人员", dataIndex: "macInspectEmp"//, renderer: com.yunda.Common.empNameRenderer
		}, {
			header: "电气巡检人员", dataIndex: "elcInspectEmp"//, renderer: com.yunda.Common.empNameRenderer
		}, {
			header: "项目检查进度", dataIndex: "wclCount", hidden: true
		}, {
			header: "使用人确认", dataIndex: "useWorker", width: 100, renderer:function(v, m, r) {
				m.css = "unEditCell";
				return v;
			}
		}, {
			header: "使用人id", dataIndex: "useWorkerId", hidden: true
		}, {
			header: "巡检情况描述", dataIndex: "checkResultDesc", width: 220
		}],
		toEditFn : InspectPlanEquipment.showInspectRecord
	});
	InspectPlanEquipment.grid.store.on('beforeload', function() {
		// 当前日期
		var now = new Date();
		// 统计开始日期
		var flag = Ext.getCmp('id_statistics_date').getValue();
		now.setMonth(now.getMonth() - flag);
		var planStartDate = 0 == flag ? null : now;
		var entityJson = {
			equipmentIdx : InspectPlanEquipment.equipmentIdx,
			planStartDate: planStartDate,
			planEndDate: new Date()
		};
		MyJson.deleteBlankProp(entityJson);
		this.baseParams.entityJson = Ext.encode(entityJson);
	});
	/** **************** 定义巡检设备列表结束 **************** */


	
});