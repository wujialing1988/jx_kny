/**
 * 设备巡检记录Ext页面，该文件依赖于InspectPlanEquipment.js文件，引用时请注意
 */
Ext.onReady(function() {
	
	Ext.ns('InspectRecord');
	
	/** **************** 定义私有变量开始 **************** */
	var dom0, dom1, myChart0, myChart1;
	var option0 = {
		series: [{
			type: 'liquidFill',
			name: '巡检进度',
			radius: '96%',
			center: ['50%', '48%'],
			outline: {
				show: false
			},
			data: [{
				name: '完成度',
				itemStyle: {
					normal: {
						color: Constants.YclColor
					}
				}
			}],
			label: {
				normal:{
					textStyle: {
						fontSize: 30,
						color: Constants.YclColor
					}
				}
			}
		}]
	};
	var option1 = {
		series: [{
			type: 'liquidFill',
			name: '巡检进度',
			radius: '96%',
			center: ['50%', '48%'],
			outline: {
				show: false
			},
			data: [{
				name: '完成度',
				itemStyle: {
					normal: {
						color: Constants.YclColor
					}
				}
			}],
			label: {
				normal:{
					textStyle: {
						fontSize: 30,
						color: Constants.YclColor
					}
				}
			}
		}]
	};
	/** **************** 定义私有变量结束 **************** */
	
	/** **************** 定义全局变量开始 **************** */
	InspectRecord.repairType = REPAIR_TYPE_JX;
	var record;
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 初始化，加载巡检记录列表数据源，渲染巡检基本情况展示信息
	 */
	InspectRecord.initFn = function() {
		if (InspectPlanEquipment) {
			record = InspectPlanEquipment.grid.store.getById(InspectRecord.planEquipmentIdx);
		}
		
		// 重新加载巡检记录列表数据源
		InspectRecord.grid.store.load();
		
		// 渲染巡检目录明细展示信息
		$yd.request({
			url: ctx + '/inspectPlanEquipmentEmp!getModelsByPlanEquipmentIdx.action',
			params: {planEquipmentIdx: InspectRecord.planEquipmentIdx}
		}, function(result) {
			var list = result.list;
			Ext.get('inspect_plan_mac').dom.innerHTML = '<div style="padding:35px 0 0 20px;">无机械巡检标准<div>';
			Ext.get('inspect_plan_elc').dom.innerHTML = '<div style="padding:35px 0 0 20px;">无电气巡检标准<div>';
			Ext.each(list, function(entity) {
				if (record) {
					entity.mechanicalCoefficient = record.get('mechanicalCoefficient');
					entity.electricCoefficient = record.get('electricCoefficient');
				}
				if (REPAIR_TYPE_JX == entity.repairType) {
					InspectRecord.tpl2Mac.overwrite(Ext.get('inspect_plan_mac'), entity);
				} else if (REPAIR_TYPE_DQ == entity.repairType) {
					InspectRecord.tpl2Elc.overwrite(Ext.get('inspect_plan_elc'), entity);
				}
			});
		});
	}
	/**
	 * 查看照片
	 */
	InspectRecord.showImage = function() {
		ImageView.show(InspectRecord.grid, {
			title: '设备巡检照片'
		});
	}
	/** **************** 定义全局函数结束 **************** */
	
	
	/** **************** 定义巡检情况展示模板开始 **************** */
	var tpl = ['<table>',
				'<tr>',
					// 后续需替换会不同检修类型的系数
					'<td><span>系数：</span><b><u>{coefficient}</u></b></td>',
				'</tr>',
				'<tr>',
					'<td><span>巡检人：</span>{inspectEmp}&nbsp;{entrustInspectEmp}</td>',
				'</tr>',
				'<tr>',
					'<td><span>巡检结果：</span>',
						'<tpl if="checkResult == \'已巡检\'">',
							'<span style="color:#000;text-align:left;width:initial;padding:0 5px 2px 5px;border-radius:8px;background:' + Constants.YclColor + '">{checkResult}</span>',
						'</tpl>',
						'<tpl if="checkResult == \'未巡检\'">',
							'<span style="color:#000;text-align:left;width:initial;padding:0 5px 2px 5px;border-radius:8px;background:' + Constants.WclColor + '">{checkResult}</span>',
						'</tpl>',
					'</td>',
				'</tr>',
				'<tr>',
					'<td><span>巡检情况描述：</span>{checkResultDesc}</td>',
				'</tr>',
			'</table>'];
	
	// 机械巡检情况基本信息显示模板
	InspectRecord.tpl2Mac = new Ext.XTemplate(tpl.join('').replace('系数', '机械系数').replace('coefficient', 'mechanicalCoefficient'));
	// 电气巡检情况基本信息显示模板
	InspectRecord.tpl2Elc = new Ext.XTemplate(tpl.join('').replace('系数', '电气系数').replace('coefficient', 'electricCoefficient'));
	/** **************** 定义巡检情况展示模板结束 **************** */
	
	/** **************** 定义巡检设备列表开始 **************** */
	InspectRecord.grid = new Ext.yunda.Grid({
		loadURL : ctx + '/inspectRecord!queryPageList.action',
		storeAutoLoad : false,
		tbar : [ '-', {
			xtype: 'radio', name: 'repairType', inputValue: REPAIR_TYPE_JX, boxLabel: '机械&nbsp;&nbsp;', checked: true,
			listeners: {
				check: function(me, checked) {
					if (checked) {
						InspectRecord.repairType = me.getRawValue();
						InspectRecord.grid.store.load();
					}
				}
			}
		}, {
			xtype: 'radio', name: 'repairType', inputValue: REPAIR_TYPE_DQ, boxLabel: '电气',
			listeners: {
				check: function(me, checked) {
					if (checked) {
						InspectRecord.repairType = me.getRawValue();
						InspectRecord.grid.store.load();
					}
				}
			}
		}, '->', {
			text: '查看照片', iconCls: 'imageIcon', disabled: false, id: 'id_image_view_btn', handler: InspectRecord.showImage
		}],
		singleSelect : true,
		fields : [{
			header: "idx主键", dataIndex: "idx", hidden: true
		}, {
			header: "顺序号", dataIndex: "seqNo", width: 50, hidden: true
		}, {
			header: "检查结果", dataIndex: "checkResult", width: 80, renderer: function(value, mataDate, record) {
				return Ext.isEmpty(value)? '<marquee direction="right" scrollamount="1">未处理</marquee>' : value;
			}
		}, {
			header: "巡检人", dataIndex: "inspectWorker"
		}, {
			header: "计划巡检人", dataIndex: "inspectEmp", width: 150, hidden: true
		}, {
			header: "计划巡检人", dataIndex: "inspectEmpid", hidden: true
		}, {
			header: "委托计划巡检人", dataIndex: "entrustInspectEmp", width: 150, hidden: true
		}, {
			header: "委托计划巡检人", dataIndex: "entrustInspectEmpid", hidden: true
		}, {
			header: "检查项目", dataIndex: "checkItem", width: 400
		}, {
			header: "巡检时间", dataIndex: "checkTime", xtype: 'datecolumn', format: 'Y-m-d H:i', width: 120
		}, {
			header: "检查标准", dataIndex: "checkStandard", width: 160, hidden: true
		}, {
			header: "巡检人id", dataIndex: "inspectWorkerId", hidden: true
		}, {
			header: "巡检结果", dataIndex: "empCheckResult", hidden: true
		}, {
			header: "照片数量", dataIndex: "imageCount", renderer: function(v, m, r) {
				if (v) {
					return '<div style="background:#0f0;width:20px;height:15px;line-height:15px;text-align:center;border-radius:8px;margin-left:10px;"><a href="#" onclick="InspectRecord.showImage()">' + v + '</a></div>';
				}
			}
		}],
		toEditFn : Ext.emptyFn
	});
	InspectRecord.grid.store.on('beforeload', function() {
		this.baseParams.entityJson = Ext.encode({
			planEquipmentIdx: InspectRecord.planEquipmentIdx,
			repairType: InspectRecord.repairType
		});
	});
	InspectRecord.grid.store.on('load', function() {
		// 加载巡检记录_机械饼图数据源
		InspectRecord.piechartStore_jx.load();
		// 加载巡检记录_电气饼图数据源
		InspectRecord.piechartStore_dq.load();
	});
	
	/** **************** 定义巡检设备列表结束 **************** */
	
	/** **************** 定义巡检记录饼图数据源开始 **************** */
	// 机械
	InspectRecord.piechartStore_jx = new Ext.data.JsonStore({
		fields: ['K', 'V'],
		idProperty: 'K',
		url: ctx + '/inspectRecord!queryChartData.action',
		listeners: {
			beforeload: function() {
				this.baseParams.repairType = REPAIR_TYPE_JX;
				this.baseParams.planEquipmentIdx = InspectRecord.planEquipmentIdx;
			},
			// 设置巡检进度“水球图”数据
			load: function(store, records) {
				var yxjCount = records[0].data.V;
				var wxjCount = records[1].data.V;
				
				if (0 === (yxjCount + wxjCount)) {
					option0.series[0].data[0].name = "无";
					delete option0.series[0].data[0].value;
				} else {
					option0.series[0].data[0].value = yxjCount / (yxjCount + wxjCount);
				}
				// 计算是否已经延期
				if (record) {
					var delayDays = com.yunda.Common.getDelayDays(new Date(record.get('planEndDate')), wxjCount);
					if (0 < delayDays) {
						option0.series[0].data[0].itemStyle.normal.color = Constants.YyqColor;	
					} else {
						option0.series[0].data[0].itemStyle.normal.color = Constants.YclColor;	
					}
				}
				if (option0 && typeof option0 === "object") {
					myChart0.setOption(option0, true);
				}
			}
		}
	})
	// 电气
	InspectRecord.piechartStore_dq = new Ext.data.JsonStore({
		fields: ['K', 'V'],
		idProperty: 'K',
		url: ctx + '/inspectRecord!queryChartData.action',
		listeners: {
			beforeload: function() {
				this.baseParams.repairType = REPAIR_TYPE_DQ;
				this.baseParams.planEquipmentIdx = InspectRecord.planEquipmentIdx;
			},
			// 设置巡检进度“水球图”数据
			load: function(store, records) {
				var yxjCount = records[0].data.V;
				var wxjCount = records[1].data.V;
				
				if (0 === (yxjCount + wxjCount)) {
					option1.series[0].data[0].name = "无";
					delete option1.series[0].data[0].value;
				} else {
					option1.series[0].data[0].value = yxjCount / (yxjCount + wxjCount);
				}
				// 计算是否已经延期
				if (record) {
					var delayDays = com.yunda.Common.getDelayDays(new Date(record.get('planEndDate')), wxjCount);
					if (0 < delayDays) {
						option1.series[0].data[0].itemStyle.normal.color = Constants.YyqColor;	
					} else {
						option1.series[0].data[0].itemStyle.normal.color = Constants.YclColor;	
					}
				}
				if (option1 && typeof option0 === "object") {
					myChart1.setOption(option1, true);
				}
			}
		}
	})
	/** **************** 定义巡检记录饼图数据源结束 **************** */

	InspectRecord.win = new Ext.Window({
		title: '设备巡检记录',
		width: 900, height: 550,
		closeAction: 'hide',
		plain: true, modal: true,
		layout: 'border',
		defaults: {layout: 'fit', border: false},
		items: [{
			region: 'north', height: 140, layout: 'hbox', baseCls: 'plain', style: 'background:rgb(223, 232, 246);', 
			defaults: {flex: 1, xtype: 'fieldset', height: 140},
			items: [{
				title: '机械',
				layout: 'border', items: [{
					region: 'center', xtype: 'container',
					html: '<div id = "inspect_plan_mac">无机械巡检标准</div>'
				}, {
					region: 'east',	width: 180, xtype: 'container',
					html: '<div id="piechart_jx" style="height:105px;width:160px;"></div>',
					listeners: {
						afterrender: function() {
							dom0 = Ext.get('piechart_jx').dom;
							myChart0 = echarts.init(dom0);
						}
					}
				}]
			}, {
				title: '电气',
				layout: 'border', items: [{
					region: 'center', xtype: 'container',
					html: '<div id = "inspect_plan_elc">无电气巡检标准</div>'
				}, {
					region: 'east',	width: 180, xtype: 'container',
					html: '<div id="piechart_dq" style="height:105px;width:160px;"></div>',
					listeners: {
						afterrender: function() {
							dom1 = Ext.get('piechart_dq').dom;
							myChart1 = echarts.init(dom1);
						}
					}
				}]
			}]
		}, {
			region: 'center',
			items: InspectRecord.grid
		}],
		listeners: {
			show: function() {
				// 初始化，加载巡检记录列表数据源，渲染巡检基本情况展示信息
				InspectRecord.initFn();
			}
		},
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
});