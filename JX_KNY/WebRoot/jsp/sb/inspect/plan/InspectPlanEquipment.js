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
	
	/**
	 * @param planIdx 巡检目录idx主键
	 * @param equipmentIds 设备信息idx主键数组
	 */
	InspectPlanEquipment.saveFn = function(planIdx, equipmentIds, callback) {
		if(self.loadMask)    self.loadMask.show();
		Ext.Ajax.request({
			url : ctx + '/inspectPlanEquipment!save.action',
			params : {planIdx : planIdx, equipmentIds : Ext.encode(equipmentIds)},
		    success: function(response, options) {
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            InspectPlan.tree.root.reload(callback);
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options) {
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + ('' || response.responseText));
		    }
		});
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
		saveURL : ctx + '/inspectPlanEquipment!saveOrUpdate.action',
		deleteURL : ctx + '/inspectPlanEquipment!logicDelete.action',
		storeAutoLoad : false, viewConfig : null,
		tbar : [{
			text: '查看照片', iconCls: 'imageIcon', disabled: false, id: 'id_image_view_btn', handler: InspectPlanEquipment.showImage
		}, '-', 'add', 'delete', 'refresh', '-', {
			text: '委托巡检', iconCls: 'configIcon', handler: function() {
				var sm = InspectPlanEquipment.grid.getSelectionModel();
				var selections = sm.getSelections();
				if (0 >= selections.length) {
					MyExt.Msg.alert('尚未选择任何记录！');
					return;
				}
				// 以下两个标识字段，用于标识计划下发界面是否加载“机械”或者“电气”班组，需求：如果设备的“机械系数”为0，则没有机械检修项，如果设备的“电气系数”为0，则没有电气检修项
				InspectEmp.mac = null;
				InspectEmp.elc = null;
				// 如果选择了一条记录，则对该设备的巡检人员进行回显
				if (1 == selections.length) {
					InspectEmp.record = selections[0];
				} else {
					InspectEmp.record = null;
				}
				// 缓存已选择的巡检目录明细idx主键
				var ids = [];
				for (var i = 0; i < selections.length; i++) {
					ids.push(selections[i].id);
					if (null == InspectEmp.mac && 0 < selections[i].get('mechanicalCoefficient')) {
						InspectEmp.mac = true;
					}
					if (null == InspectEmp.elc && 0 < selections[i].get('electricCoefficient')) {
						InspectEmp.elc = true;
					}
				}
				InspectEmp.ids = ids;
				InspectEmp.win.show();
			}
		}, '->', '<span style="color:gray;">双击一条记录查看巡检情况详情！</span>'],
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
		
		// 表格行编辑事件
		toEditFn : InspectPlanEquipment.showInspectRecord,
		
		// 新增按钮点击前事件
	    beforeAddButtonFn: function() {
	    	var sm = InspectPlan.tree.getSelectionModel();
	    	if (null == sm.getSelectedNode()) {
	    		MyExt.Msg.alert('请在左侧列表中选择一个设备巡检计划！');
	    		return false;
	    	}
	    	return true;
	    },
	    
	    // 新增按钮事件
		addButtonFn: function() {   
	    	if(!this.beforeAddButtonFn())   return;
	    	EquipmentPrimaryInfoSelect.win.show();
	    },
	    
	    // 删除成功后重新加载设备巡检计划树
	    afterDeleteFn: function() {
	    	InspectPlan.tree.root.reload();
	    }
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
	/** **************** 定义巡检设备列表结束 **************** */


	
});