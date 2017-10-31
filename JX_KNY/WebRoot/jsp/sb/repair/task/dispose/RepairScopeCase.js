Ext.onReady(function() {
	Ext.ns('RepairScopeCase');
	/** **************** 定义私有变量开始 **************** */
	var cm, store;
	/** **************** 定义私有变量结束 **************** */
	
	/** **************** 定义全局变量开始 **************** */
	RepairScopeCase.searchParams = {
		repairType: REPAIR_TYPE_JX
	};
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义私有变量开始 **************** */
	var myChart;
	var option = {
		series: [{
//			center: ['70%', '50%'],
			type: 'liquidFill',
			name: '检修进度',
			radius: '90%',
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
						fontSize: 50,
						color: Constants.YclColor
					}
				}
			}
		}]
	};
	/** **************** 定义私有变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 定义设备检修计划基本信息显示模板
	 */
	RepairScopeCase.tpl = new Ext.XTemplate(
		'<table border="0" cellpadding="0" cellspacing="0">',
			'<tr>',
			'<td style="width:30%;"><span style="width:70px;">设备名称：</span>{equipmentName}</td>',
			'<td style="width:12%;"><span style="width:38px;">修程：</span>{repairClassName}修</td>',
			// 两种不同方式的日期格式化处理
//			'<td><span>计划开工时间：</span>{[fm.date(new Date(values.beginTime), "Y-m-d")]}</td>',
			'<td style="width:20%;"><span>实际开工时间：</span>{[this.format(values.realBeginTime, "Y-m-d H:i", "KG")]}</td>',
			'<td style="width:38%;"><span>机械维修班组：</span>{[this.formatTeam(values, "JX")]}</td>',
			'</tr>',
			'<tr>',
			'<td style="width:30%;"><span style="width:70px;">设备编号：</span>{equipmentCode}</td>',
			'<td style="width:12%;"><span style="width:38px;">状态：</span>{[this.check(values.state, values.endTime, values.wclCount, values.realBeginTime)]}</td>',
			'<td style="width:20%;"><span>实际完工时间：</span>{[this.format(values.realEndTime, "Y-m-d H:i", "WG")]}</td>',
			'<td style="width:38%;"><span>电气维修班组：</span>{[this.formatTeam(values, "DQ")]}</td>',
			'</tr>',
		'</table>', {
	        comipled: true,
	        formatTeam: function(values, repairType) {
	        	var val;
	        	if ("JX" == repairType) {
	        		val = (values.repairTeamMac || '无') + '&nbsp;-&nbsp;JF[' + values.mechanicalCoefficient + ']';
	        		if (!Ext.isEmpty(values.gzSignMac)) {
	        			val += '<span style="margin-left:5px;color:#000;width:initial;padding:0 5px;border-radius:8px;background:' + Constants.YclColor + ';">工长&nbsp;' + values.gzSignMac + '&nbsp;已确认</span>';
	        		}
	        	} else {
	        		val = (values.repairTeamElc || '无') + '&nbsp;-&nbsp;DF[' + values.electricCoefficient + ']';
	        		if (!Ext.isEmpty(values.gzSignElc)) {
	        			val += '<span style="margin-left:5px;color:#000;width:initial;padding:0 5px;border-radius:8px;background:' + Constants.YclColor + ';">工长&nbsp;' + values.gzSignElc + '&nbsp;已确认</span>';
	        		}
	        	}
	        	return val;
	        },
	        format: function(value, fmt, flag) {
				if (Ext.isEmpty(value)) return "KG" == flag ? "未开工" : "未完工";
		        var date = new Date(value);
		        return date.format(fmt || 'Y-m-d');
	        },
	        check: function(state, endTime, wclCount, realBeginTime) {
	        	var displayColor = Constants.YclColor;
	        	if (!realBeginTime) {
	        		state = '未开工';
	        		displayColor = Constants.WclColor;
	        	}
	        	var delayDays = com.yunda.Common.getDelayDays(new Date(endTime), wclCount);
	        	if (0 < delayDays) {
	        		return '<span style="color:#fff;text-align:left;width:initial;padding:0 5px 2px 5px;border-radius:8px;background:' + Constants.YyqColor + ';">已延期&nbsp;' + delayDays + '&nbsp;天</span>';
	        	} 
	        	return '<span style="color:#000;text-align:left;width:initial;padding:0 5px 2px 5px;border-radius:8px;background:' + displayColor + ';">' + state + '</span>';
	        }
	    }
	);
	/**
	 * 显示检修作业工单基本信息
	 * @param record 检修任务单（RepairTaskList）实体对象
	 */
	RepairScopeCase.showTaskBaseInfo = function(record) {
		RepairScopeCase.tpl.overwrite(Ext.get('repair_task_base_info'), record.data);
	}
	/** **************** 定义全局函数结束 **************** */
	
	/**
	 * 表格列模型
	 */
	cm = new Ext.grid.ColumnModel({
		columns: [new Ext.grid.RowNumberer(), {
			dataIndex: 'idx', header: 'idx主键', hidden: true
		}, {
			dataIndex: 'scopeDefineIdx', header: '范围定义主键', hidden: true
		}, {
			dataIndex: 'teamIdx', header: '检修任务处理班组主键', hidden: true
		}, {
			dataIndex: 'taskListIdx', header: '检修任务单主键', hidden: true
		}, {
			dataIndex: 'sortNo', header: '序号', hidden: true
		}, {
			dataIndex: 'repairType', header: '检修类型', renderer: function(v) {
				if (REPAIR_TYPE_JX == v) return '机械';
				if (REPAIR_TYPE_DQ == v) return '电气';
				if (REPAIR_TYPE_QT == v) return '其它';
			}, hidden: true
		}, {
			dataIndex: 'repairItemName', header: '检修范围名称', width: 180
		}, {
			dataIndex: 'remark', header: '备注', width: 220, hidden: true
		}, {
			dataIndex: 'state', header: '处理状态', hidden: true
		}, {
			header: '任务进度', renderer: function(value, metaData, record) {
//				var endTime = record.data.endTime;
				var jdBg = 'background:#00FF00;';
//				if (new Date().getTime() > endTime) {
//					jdBg = 'background:red;';
//				}
				var wclCount = record.data.wclCount;
				var yclCount = record.data.yclCount;
				// 使用进度条样式显示检修记录处理情况
				var total = parseInt(yclCount) + parseInt(wclCount);
				var width = 100;
				var displyValue = '';
				if (0 !== total) {
					width = 100 * parseInt(yclCount) / total;
					width = width == 0 ? 1 : width;
					displyValue = yclCount + '/' + total;
				}
				return [
			        '<div style = "background:rgb(239, 239, 239);margin-left:-5px;height:13px;width:100px;border-radius:8px;border:1px solid rgb(133, 139, 173);">',
				        '<div style = "' + jdBg + 'font-weight:bold;height:13px;text-align:right;border-radius:7px;width:'+ width +'px;">',
				        	displyValue,
				        '</div>',
			        '</div>'
		        ].join('');
			
			}, width: 70
		}, {
			dataIndex: 'wclCount', header: '未处理', hidden: true
		}, {
			dataIndex: 'yclCount', header: '已处理', hidden: true
		}]
	});
	
	/**
	 * 数据源
	 */
	store = new Ext.data.JsonStore({
        id: 'idx', 
        root: "root", 
        totalProperty: "totalProperty", 
        autoLoad: false,
        remoteSort: true,
        url: ctx + '/repairScopeCase!queryPageList.action',
        fields: ['idx', 'scopeDefineIdx', 'teamIdx', 'taskListIdx', 'sortNo', 'repairType', 'repairItemName', 'remark', 'state', 'wclCount', 'yclCount'],
        listeners: {
        	beforeload: function() {
        		RepairScopeCase.searchParams.taskListIdx = RepairScopeCase.taskListIdx;
        		MyJson.deleteBlankProp(RepairScopeCase.searchParams);
        		this.baseParams.entityJson = Ext.encode(RepairScopeCase.searchParams);
        	},
        	load: function(me, records, options) {
        		if (0 < records.length) {
        			// 设备检修范围活表格数据加载完成后，自动默认选择首行记录
        			if (RepairScopeCase.rowIndex && RepairScopeCase.rowIndex >= 0) {
        				RepairScopeCase.grid.getSelectionModel().selectRow(RepairScopeCase.rowIndex);
        			} else {
        				RepairScopeCase.grid.getSelectionModel().selectRow(0);
        			}
        		} else if (0 < RepairWorkOrder.grid.store.getCount()) {
        			// 如果范围活没有数据，则清空设备检修作业工单表格数据
    				RepairWorkOrder.grid.store.removeAll();
    				RepairWorkOrderStuff.grid.store.removeAll();
    			}
        		RepairScopeCase.piechartStore.load();
        	}
        }
    }); 
	
	/**
	 * 表格
	 */
	RepairScopeCase.grid = new Ext.grid.GridPanel({
		title: '<span style="font-weight:normal;">设备检修范围活</span>',
		region: 'center', split: true,
		viewConfig: {forceFit: true},
		cm: cm,
		storeId: 'idx',
        store: store,
        storeAutoLoad: false,
        loadMask: new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." }),
        tbar: ['-', {
			xtype: 'radio', name: 'repairType', inputValue: REPAIR_TYPE_JX, boxLabel: '机械&nbsp;&nbsp;', checked: true,
			listeners: {
				check: function(me, checked) {
					if (checked) {
						RepairScopeCase.rowIndex = -1;
						RepairScopeCase.searchParams.repairType = me.getRawValue();
						RepairScopeCase.grid.store.load();
					}
				}
			}
		}, {
			xtype: 'radio', name: 'repairType', inputValue: REPAIR_TYPE_DQ, boxLabel: '电气',
			listeners: {
				check: function(me, checked) {
					if (checked) {
						RepairScopeCase.rowIndex = -1;
						RepairScopeCase.searchParams.repairType = me.getRawValue();
						RepairScopeCase.grid.store.load();
					}
				}
			}
		}],
        bbar: $yd.createPagingToolbar({pageSize:20, store: store})
    });
	RepairScopeCase.grid.getSelectionModel().on('rowselect', function( me, rowIndex, r ) {
		var record = me.getSelected();
		if (!record) {
			return;
		}
		RepairWorkOrder.searchParams.scopeCaseIdx = record.id;
		RepairWorkOrder.grid.store.load();
		RepairScopeCase.rowIndex = rowIndex;
	});
	
	/** **************** 定义检修处理情况饼图数据源开始 **************** */
	RepairScopeCase.piechartStore = new Ext.data.JsonStore({
		fields: ['K', 'V'],
		idProperty: 'K',
		url: ctx + '/repairScopeCase!queryChartData.action',
		listeners: {
			beforeload: function() {
				this.baseParams.taskListIdx = RepairScopeCase.taskListIdx;
			},
			// 设置检修进度“水球图”数据
			load: function(store, records) {
				var yclCount = records[0].data.V;
				var wclCount = records[1].data.V;
				
				if (0 === (yclCount + wclCount)) {
					option.series[0].data[0].name = "无";
				} else {
					option.series[0].data[0].value = yclCount / (yclCount + wclCount);
				}
				// 计算是否已经延期
				var record = RepairTaskList.grid.store.getById(RepairScopeCase.taskListIdx);
				if (record) {
					var delayDays = com.yunda.Common.getDelayDays(new Date(record.get('endTime')), wclCount);
					if (0 < delayDays) {
						option.series[0].data[0].itemStyle.normal.color = Constants.YyqColor;	
					} else {
						option.series[0].data[0].itemStyle.normal.color = Constants.YclColor;	
					}
				}
				if (option && typeof option === "object") {
					myChart.setOption(option, true);
				}
			}
		}
	})
	/** **************** 定义检修处理情况饼图数据源结束 **************** */
	
	RepairScopeCase.win = new Ext.Window({
		title: '设备检修作业详情',
		width: 720, height: 420,
		maximized: true,
		closeAction: 'hide',
		buttonAlign: 'center',
		layout: 'border',
		// Modified by hetao on 2016-10-12 详情业务增加操作按钮【上一条】【下一条】，用于开始切换要查看的检修设备范围活处理情况
		tbar: [{
        	text: '返回', iconCls: 'backIcon', tooltip: '点我，点我可以返回主列表哦！', handler: function() {
        		this.findParentByType('window').hide();
        	}
		}, '-', {
			text: '打印', iconCls: 'printerIcon', tooltip: '打印设备检修记录单！', handler: function() {
        		// 显示报表打印页面
        		var cpt = "equipmentRepair/equipmentRepairRecord.cpt";
        	   	report.view(cpt, "设备检修记录单", {taskListIdx: RepairScopeCase.taskListIdx});
        	}
		}, {
			text: '隐藏基本信息', iconCls: 'switchIcon2', handler: function() {
				if ('隐藏基本信息' === this.getText()) {
					this.setText('显示基本信息');
				} else {
					this.setText('隐藏基本信息');
				}
				Ext.getCmp('id_north_region').toggleCollapse();
			}
		}, '->', '-', {
			text: '上一条', iconCls:'moveUpIcon', handler: function() {
				var record = RepairTaskList.grid.getStore().getAt(--RepairTaskList.rowIndex);
				if (Ext.isEmpty(record)) {
					RepairTaskList.rowIndex++;
					MyExt.Msg.alert('已经是第一条记录！');
					return;
				}
				RepairScopeCase.rowIndex = -1;
//				RepairScopeCase.win.setTitle('<span style="color:red;">' + record.get('equipmentName') + '(' + record.get('equipmentCode') + ')</span> 检修作业详情');
				// 显示检修作业工单基本信息
				RepairScopeCase.showTaskBaseInfo(record);
				RepairScopeCase.taskListIdx = record.id
				// 重新加载“设备检修范围活”表格数据
				RepairScopeCase.grid.store.load();
			}
		}, {
			text: '下一条', iconCls:'moveDownIcon', handler: function() {
				var record = RepairTaskList.grid.getStore().getAt(++RepairTaskList.rowIndex);
				if (Ext.isEmpty(record)) {
					RepairTaskList.rowIndex--;
					MyExt.Msg.alert('已经是最后一条记录！');
					return;
				}
				RepairScopeCase.rowIndex = -1;
//				RepairScopeCase.win.setTitle('<span style="color:red;">' + record.get('equipmentName') + '(' + record.get('equipmentCode') + ')</span> 检修作业详情');
				// 显示检修作业工单基本信息
				RepairScopeCase.showTaskBaseInfo(record);
				RepairScopeCase.taskListIdx = record.id
				// 重新加载“设备检修范围活”表格数据
				RepairScopeCase.grid.store.load();
			}
		}],
		items: [{
			border: false,
			region: 'west', width: 438, split: true,
			layout: 'border', 
			// Modified by hetao on 2016-10-12 增加：检修处理情况（机械 + 电气）
			items: [
		        RepairScopeCase.grid, {
		        	iconCls: 'theme2Icon',
		        	region: 'south', height: 260, title: '<span style="font-weight:normal;">检修处理情况（机械 + 电气）</span>',
		        	collapsible: true, frame: true,
		        	html: '<div id="piechart_equipment" style="width:438px;height:230px;"></div>',
		        	listeners: {
		        		afterrender: function() {
		        			var dom = document.getElementById("piechart_equipment");
		        			myChart = echarts.init(dom);
		        		}
		        	}
		        }
         	]
		}, {
			region: 'center', layout: 'border', title: '<span style="font-weight:normal;">检修作业工单</span>', //items: RepairWorkOrder.grid
			defaults: {
				xtype: 'container', layout: 'fit'
			},
			items: [{
				region: 'center',
				items: RepairWorkOrder.grid
			}, {
				region: 'south', height: 200,
				items: RepairWorkOrderStuff.grid
			}]
		}, {
			layout: 'fit', region: 'north', height: 85, baseCls: 'plain',
			collapseMode: 'mini', id: 'id_north_region',
			items: [{
				xtype: 'fieldset', title: '基本信息', html: '<div id="repair_task_base_info"></div>',
				autoScroll: true
			}]
		}],
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}],
		listeners: {
			show: function() {
				// 重新加载“设备检修范围活”表格数据
				RepairScopeCase.grid.store.load();
				// 显示检修作业工单基本信息
				RepairScopeCase.showTaskBaseInfo(RepairTaskList.grid.store.getAt(RepairTaskList.rowIndex));
			},
			hide: function() {
				// 隐藏时移除“设备检修范围活”、“设备检修作业工单”表格数据
				RepairScopeCase.grid.store.removeAll();
				RepairWorkOrder.grid.store.removeAll();
				RepairWorkOrderStuff.grid.store.removeAll();
				
				// 如果手动处理了设备检修作业工单，则重新加载首页表格数据源
				if (RepairScopeCase.hasChanged) {
					RepairTaskList.grid.store.reload();
				}
			}
		}
	});
});