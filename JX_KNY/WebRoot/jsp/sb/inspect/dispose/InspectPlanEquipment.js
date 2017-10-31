Ext.onReady(function() {
	
	Ext.ns('InspectPlanEquipment');
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 显示巡检记录详情
	 */
	InspectPlanEquipment.showInspectRecord = function(grid, rowIndex, e) {
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
		//console.debug(record);
		// 缓存正在处理的巡检设备列表的行索引，用于重新加载列表后，能再次选中该设备记录所在行
		InspectPlanEquipment.rowIndex = rowIndex;
		InspectRecord.planEquipmentIdx = record.id;
		InspectRecord.planIdx = record.data.planIdx;
		if (InspectRecord.win.hidden) {
			InspectRecord.win.show();
		} else {
			// 初始化，加载巡检记录列表数据源，渲染巡检基本情况展示信息
			InspectRecord.initFn();
		}
	}
	/**
	 * 查看照片，查看该巡检任务下所有工单关联的照片
	 */
	InspectPlanEquipment.showImage = function() {
		ImageView.show(InspectPlanEquipment.grid, {
			title: '设备巡检照片',
			businessName: 'inspectPlanEquipmentManager'
		});
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义巡检设备列表开始 **************** */
	InspectPlanEquipment.grid = new Ext.yunda.Grid({
		loadURL : ctx + '/inspectPlanEquipment!queryPageList.action',
		storeAutoLoad : false, viewConfig : null,
		tbar : [{
			text: '查看照片', iconCls: 'imageIcon', disabled: false, id: 'id_image_view_btn', handler: InspectPlanEquipment.showImage
		}, {
			text: '刷新', iconCls: 'refreshIcon', handler: function() {
				self.location.reload();
			}
		}, {
			text: '巡检处理', iconCls: 'wrenchIcon', handler: function() {
				var sm = InspectPlanEquipment.grid.getSelectionModel();
				if(0 >= sm.getCount()) {
					MyExt.Msg.alert("尚未选择任何记录");
					return;
				}
				Ext.Msg.confirm('提示', '处理设备下所有巡检项目状态为：<span style="color:red">合格</span>，是否确认继续？？', function(btn) {
					if ('yes' == btn) {
						var ids = [];
						var records = sm.getSelections();
						for(var i = 0; i < records.length; i++) {
							ids.push(records[i].id);
						}
						$yd.request({
		        			url: ctx + '/inspectPlanEquipment!updateFinished.action',
		        			params: {
		        				ids: Ext.encode(ids)
		        			}
		        		}, function () {
		        			alertSuccess();
		        			InspectDispose.tree.root.reload();
		        		});
					}
				});
			}
		},'->', '<span style="color:gray;">双击一条记录查看巡检情况详情！</span>'],
		fields : [{
			header: "idx", dataIndex: "idx",  hidden: true
		}, {
			header: "planIdx", dataIndex: "planIdx",  hidden: true
		}, {
			header: "顺序号", dataIndex: "seqNo", hidden: true
		}, {
			header: "管理类别", dataIndex: "manageClass", width: 60, align: 'center', renderer: function(value, metaData, record) {
				return '<span style="font-weight:bold;">' + value + '</span>';
			}
		}, {
			header: "设备编号", dataIndex: "equipmentCode"
		}, {
			header: "设备名称", dataIndex: "equipmentName", width: 220
		}, {
			header: '设置地点', dataIndex: 'usePlace'
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
			header: "项目检查进度", dataIndex: "yclCount", width: 110, align: 'center', renderer: com.yunda.Common.yclCountRenderer2XJ
		}, {
			header: "照片数量", dataIndex: "imageCount", width: 60, renderer: com.yunda.Common.imageCountRenderer2XJ
		}, {
			dataIndex : 'planStartDate', header : '计划开工时间', width: 85, renderer: function(v) {
				return v ? new Date(v).format('Y-m-d') : '';
			}
		}, {
			dataIndex : 'planEndDate', header : '计划完工时间', width: 85, renderer: com.yunda.Common.planEndDatetRenderer2XJ
		}, {
			header: "机械巡检人员", dataIndex: "macInspectEmp", renderer: com.yunda.Common.empNameRenderer
		}, {
			header: "机械巡检人员id", dataIndex: "macInspectEmpid", hidden: true
		}, {
			header: "电气巡检人员", dataIndex: "elcInspectEmp", renderer: com.yunda.Common.empNameRenderer
		}, {
			header: "电气巡检人员id", dataIndex: "elcInspectEmpid", hidden: true
		}, {
			header: "委托机械巡检人员", dataIndex: "entrustMacInspectEmp", width: 120, renderer: com.yunda.Common.empNameRenderer
		}, {
			header: "委托机械巡检人员id", dataIndex: "entrustMacInspectEmpid", hidden: true
		}, {
			header: "委托电气巡检人员", dataIndex: "entrustElcInspectEmp", width: 120, renderer: com.yunda.Common.empNameRenderer
		}, {
			header: "委托电气巡检人员id", dataIndex: "entrustElcInspectEmpid", hidden: true
		}, {
			header: "项目检查进度", dataIndex: "wclCount", hidden: true
		}, {
			header: "使用人确认", dataIndex: "useWorker", width: 73, renderer:function(v, m, r) {
				m.css = "unEditCell";
				return v;
			}
		}, {
			header: "使用人id", dataIndex: "useWorkerId", hidden: true
		}, {
			header: "巡检情况描述", dataIndex: "checkResultDesc", width: 250
		}, {
			header: "类别(编号)", dataIndex: "className", renderer: function(value, metaData, record) {
				if (Ext.isEmpty(record.get('classCode'))) {
					return value;
				}
				return value + "(" + record.get('classCode') + ")";
			}, width: 180, hidden: true
		}, {
			header: "类别编码", dataIndex: "classCode", hidden: true
		}, {
			header: "型号", dataIndex: "model", hidden: true
		}, {
			header: "规格", dataIndex: "specification", hidden: true
		}, {
			header: "机械系数", dataIndex: "mechanicalCoefficient", width: 60, align: 'center', hidden: true
		}, {
			header: "电气系数", dataIndex: "electricCoefficient", width: 60, align: 'center', hidden: true
		}, {
			header: "固资编号", dataIndex: "fixedAssetNo", hidden: true
		}, {
			header: "固资原值", dataIndex: "fixedAssetValue", hidden: true
		}, {
			header: "使用年月", dataIndex: "useDate", xtype: "datecolumn", format: 'Y-m', hidden: true
		}, {
			header: "设置地点", dataIndex: "usePlace", hidden: true
		}, {
			header: "制造工厂", dataIndex: "makeFactory", hidden: true
		}, {
			header: "制造年月", dataIndex: "makeDate", xtype: "datecolumn", format: 'Y-m', hidden: true
		}],
		toEditFn : InspectPlanEquipment.showInspectRecord
	});
	InspectPlanEquipment.grid.store.on('beforeload', function() {
		var entityJson = {
			planIdx : InspectPlanEquipment.planIdx
		};
		this.baseParams.entityJson = Ext.encode(entityJson);
		// Modified by hetao on 2016-10-17 默认使用设备设置地点进行排序
		this.baseParams.sort = "equipmentCode";
		this.baseParams.dir = "ASC";
	});
	InspectPlanEquipment.grid.store.on('load', function() {
		// 巡检设备列表加载完成后，如果InspectPlanEquipment.rowIndex存在，则重新选择之前的记录行
		if (InspectPlanEquipment.rowIndex && InspectPlanEquipment.rowIndex >= 0) {
			InspectPlanEquipment.grid.getSelectionModel().selectRow(InspectPlanEquipment.rowIndex);
		}
	});
	/** **************** 定义巡检设备列表结束 **************** */


	
});