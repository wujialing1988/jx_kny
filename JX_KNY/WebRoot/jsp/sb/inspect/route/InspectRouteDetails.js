Ext.onReady(function() {
	Ext.ns('InspectRouteDetails');
	
	/** **************** 定义全局变量开始 **************** */
	InspectRouteDetails.searchParams = {};
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 保存巡检线路明细，添加已选择的设备到巡检计划中
	 * @param routeIdx 巡检目录idx主键
	 * @param equipmentIds 设备信息idx主键数组
	 */
	InspectRouteDetails.saveFn = function(routeIdx, equipmentIds, callback) {
		if(self.loadMask)    self.loadMask.show();
		Ext.Ajax.request({
			url : ctx + '/inspectRouteDetails!save.action',
			params : {routeIdx : routeIdx, equipmentIds : Ext.encode(equipmentIds)},
			scope : InspectRouteDetails.grid,
		    success: function(response, options) {
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            this.store.reload({
		            	callback: callback
		            });
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
	 * 设置巡检人员
	 */
	InspectRouteDetails.setInspectEmp = function() {
		var sm = InspectRouteDetails.grid.getSelectionModel();
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
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义巡检目录列表开始 **************** */
	InspectRouteDetails.grid = new Ext.yunda.Grid({
		loadURL : ctx + '/inspectRouteDetails!queryPageList.action',
		saveURL : ctx + '/inspectRouteDetails!saveOrUpdate.action',
		deleteURL : ctx + '/inspectRouteDetails!logicDelete.action',
		storeAutoLoad : false, /*viewConfig : null,*/
		tbar : ['add', 'delete', 'refresh', '-', {
			text: '设置巡检人员', iconCls: 'configIcon', handler: InspectRouteDetails.setInspectEmp
		}],
		fields : [{
			header: "idx", dataIndex: "idx",  hidden: true
		}, {
			header: "routeIdx", dataIndex: "routeIdx",  hidden: true
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
			header: "设置地点", dataIndex: "usePlace", width: 100
		}, {
			header: "类别(编号)", dataIndex: "className", hidden: true, renderer: function(value, metaData, record) {
				if (Ext.isEmpty(record.get('classCode'))) {
					return value;
				}
				return value + "(" + record.get('classCode') + ")";
			}, width: 180
		}, {
			header: "机械系数", dataIndex: "mechanicalCoefficient", width: 60, align: 'center'
		}, {
			header: "电气系数", dataIndex: "electricCoefficient", width: 60, align: 'center'
		}, {
			header: "机械巡检人员", dataIndex: "macInspectEmp", width: 140
		}, {
			header: "机械巡检人员id", dataIndex: "macInspectEmpid", hidden: true
		}, {
			header: "电气巡检人员", dataIndex: "elcInspectEmp", width: 140
		}, {
			header: "电气巡检人员id", dataIndex: "elcInspectEmpid", hidden: true
		}, {
			header: "类别编码", dataIndex: "classCode", hidden: true
		}, {
			header: "型号", dataIndex: "model"
		}, {
			header: "规格", dataIndex: "specification"
		}/*, {
			header: "固资编号", dataIndex: "fixedAssetNo"
		}, {
			header: "固资原值", dataIndex: "fixedAssetValue"
		}, {
			header: "使用年月", dataIndex: "useDate", xtype: "datecolumn", format: 'Y-m-d'
		}, {
			header: "设置地点", dataIndex: "usePlace", hidden: true
		}, {
			header: "制造工厂", dataIndex: "makeFactory"
		}, {
			header: "制造年月", dataIndex: "makeDate", xtype: "datecolumn", format: 'Y-m-d'
		}*/],
		toEditFn : InspectRouteDetails.setInspectEmp,
		addButtonFn: function() {   
	    	if(!this.beforeAddButtonFn())   return;
	    	EquipmentPrimaryInfoSelect.win.show();
	    },
		beforeAddButtonFn: function() {
			var sm = InspectRoute.tree.getSelectionModel();
			var node = sm.getSelectedNode();
			if (!node) {
				MyExt.Msg.alert('请选择一条巡检目录进行线路明细维护！');
				return false;
			}
	    	return true;
	    }
	});
	InspectRouteDetails.grid.store.on('beforeload', function() {
		var entityJson = {
			routeIdx : InspectRouteDetails.routeIdx
		};
		this.baseParams.entityJson = Ext.encode(entityJson);
		this.baseParams.sort = "seqNo";
		this.baseParams.dir = "ASC";
	});
	/** **************** 定义巡检目录列表结束 **************** */


	
});