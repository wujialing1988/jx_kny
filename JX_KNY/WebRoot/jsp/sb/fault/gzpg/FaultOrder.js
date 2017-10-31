Ext.onReady(function() {
	Ext.ns('FaultOrder');
	
	/** **************** 定义全局变量开始 **************** */
	FaultOrder.labelWidth = 100;
	FaultOrder.fieldWidth = 140;
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 自动生成提票单编号
	 */
	FaultOrder.createFaultOrderNo = function(filed) {
		if(self.loadMask)    self.loadMask.show();
		Ext.Ajax.request({
			url : ctx + '/faultOrder!createFaultOrderNo.action',
		    success: function(response, options) {
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		        	filed.setValue(result.faultOrderNo);
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
	 * 提票调度派工
	 */
	FaultOrder.dispatchFn = function(record) {
		Dispatch.record = record;
		if (Dispatch.win.hidden) {
			Dispatch.win.show();
		}
		// 回显故障提票基本信息
		var data = {};
		Ext.apply(data, record.data);
		data.faultOccurTime = new Date(data.faultOccurTime).format('Y-m-d H:i');
		Dispatch.tpl.overwrite(Ext.get('id_inspect_route_info'), data);

		// 回显主修班组信息
		var repairEmpId = record.get('repairEmpId');
		var repairEmp = record.get('repairEmp');
		if (!Ext.isEmpty(repairEmpId) && !Ext.isEmpty(repairEmp)) {
			var formPanel = Dispatch.win.findByType('form')[0];
			formPanel.find('hiddenName', 'repairEmpId')[0].setDisplayValue(repairEmpId, repairEmp);
			formPanel.find('name', 'repairEmp')[0].setValue(repairEmp);
		}
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	FaultOrder.searchForm = new Ext.form.FormPanel({
		padding: 10, 
		layout: 'column',
		defaults: {layout: 'form', columnWidth: .25, defaults: {
			width: FaultOrder.fieldWidth, xtype: 'textfield', enableKeyEvents: true,
			listeners: {
				keyup: function(me, e) {
					if (e.ENTER == e.keyCode) {
						var form = FaultOrder.searchForm.getForm();
						if (!form.isValid()) {
							return;
						}
						FaultOrder.grid.store.load();
					}
				}	
			}
		}},
		items: [{
			items: [{
				name: 'faultOrderNo', fieldLabel: '提票单号'
			}]
		}, {
			items: [{
				name: 'equipmentName', fieldLabel: '故障设备'
			}]
		}, {
			items: [{
				name: 'submitEmp', fieldLabel: '提报人'
			}]
		}, {
			items: [{
				xtype: 'compositefield', fieldLabel: '故障日期', combineErrors: false, anchor:'100%',
				items: [{
					xtype:'my97date', id: 'planStartDate', format:'Y-m-d', value:lastMonth, width: 100,
					// 日期校验器
					vtype:'dateRange',
					dateRange:{startDate: 'planStartDate', endDate: 'planEndDate'}
				}, {
					xtype: 'label',
					text: '至',
					style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}, {							
					xtype:'my97date', id: 'planEndDate', format:'Y-m-d', width: 100,
					// 日期校验器
					vtype:'dateRange',
					dateRange:{startDate: 'planStartDate', endDate: 'planEndDate'}
				}]
			}]
		}]
	});
	/** **************** 定义查询表单结束 **************** */
	
	/** **************** 定义提票单表格开始 **************** */
	FaultOrder.grid = new Ext.yunda.Grid({
		region: 'center',
		loadURL: ctx + '/faultOrder!pageQuery.action',
		saveAndAdd: true, viewConfig: false, modalWindow: true,
		saveForm: FaultOrder.saveForm,
		tbar: [{
			text: '查看照片', iconCls: 'imageIcon', disabled: false, id: 'id_image_view_btn', handler: function() {
				ImageView.show(FaultOrder.grid);
			}
		}, {
			text: '批量派工', iconCls: 'saveIcon', handler: function() {
				var sm = FaultOrder.grid.getSelectionModel();
				if (sm.getCount() <= 0) {
					MyExt.Msg.alert('尚未选择任何记录！');
					return;
				}
				MultiDispatch.win.show();
			}
		}, 'refresh', '-', '<span style="color:red;font-weight:bold;">提票状态：</span>', {
			xtype: 'checkbox', checked: true, boxLabel: '未派工&nbsp;&nbsp;', inputValue: STATE_YPG,
			listeners: {
				check: function(me) {
					FaultOrder.grid.store.load();
				}
			}
		}, {
			xtype: 'checkbox', checked: false, boxLabel: '处理中&nbsp;&nbsp;', inputValue: STATE_CLZ,
			listeners: {
				check: function(me) {
					FaultOrder.grid.store.load();
				}
			}
		}, '->', '-', '故障等级：', {
			xtype: 'checkbox', checked: true, boxLabel: '一般&nbsp;&nbsp;', inputValue: FAULT_LEVEL_YB,
			listeners: {
				check: function(me) {
					FaultOrder.grid.store.load();
				}
			}
		}, {
			xtype: 'checkbox', checked: true, boxLabel: '重大&nbsp;&nbsp;', inputValue: FAULT_LEVEL_ZD,
			listeners: {
				check: function(me) {
					FaultOrder.grid.store.load();
				}
			}
		}, {
			xtype: 'checkbox', checked: true, boxLabel: '特大&nbsp;&nbsp;', inputValue: FAULT_LEVEL_TD,
			listeners: {
				check: function(me) {
					FaultOrder.grid.store.load();
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
			dataIndex: 'equipmentCode', header: '设备编码'
		}, {
			dataIndex: 'equipmentName', header: '设备名称', width: 200
		}, {
			dataIndex: 'submitEmp', header: '提报人', width: 60
		}, {
			dataIndex: 'submitEmpId', header: '提报人id', hidden: true
		}, {
			dataIndex: 'state', header: '提票状态', renderer: function(v) {
				if (STATE_YPG == v) return "未派工";
				return v;
			}
		}, {
			dataIndex: 'faultOccurTime', header: '故障发现时间', xtype: 'datecolumn', format: 'Y-m-d H:i', width: 120
		}, {
			dataIndex: 'faultPlace', header: '故障部位及意见', width: 200
		}, {
			dataIndex: 'faultPhenomenon', header: '故障现象', width: 360
		}, {
			dataIndex: 'causeAnalysis', header: '原因分析', width: 200, hidden: true
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
			dataIndex: 'repairEmp', header: '修理人' , width: 200
		}, {
			dataIndex: 'repairEmpId', header: '修理人ID', hidden: true
		}, {
			dataIndex: 'repairContent', header: '实际修理内容', hidden: true
		}, {
			dataIndex: 'useWorker', header: '使用人（保留）', hidden: true
		}, {
			dataIndex: 'useWorkerId', header: '使用人ID（保留）', hidden: true
		}, {
			dataIndex: 'faultLevel', header: '故障等级'
		}],
		toEditFn: function(grid, rowIndex, e) {
			var record = this.store.getAt(rowIndex);
			FaultOrder.dispatchFn(record);
		}
	});
	FaultOrder.grid.store.on('beforeload', function() {
		whereList = [];
		// 提票状态快速过滤
		var tbar = FaultOrder.grid.getTopToolbar();
		var checkboxs = tbar.findByType('checkbox');
		var propValues = [-1];
		for (var i = 0; i < 2; i++) {
			if (checkboxs[i].checked) {
				propValues.push(checkboxs[i].getRawValue())
			}
		}
		whereList.push({compare: Condition.IN, propName: 'state', propValues: propValues});
		
		// 故障等级快速过滤
		var tbar = FaultOrder.grid.getTopToolbar();
		var checkboxs = tbar.findByType('checkbox');
		var propValues = [-1];
		for (var i = 2; i < checkboxs.length; i++) {
			if (checkboxs[i].checked) {
				propValues.push(checkboxs[i].getRawValue())
			}
		}
		whereList.push({compare: Condition.IN, propName: 'faultLevel', propValues: propValues});
		// 其它查询条件
		var searchParams = FaultOrder.searchForm.getForm().getValues();
		// 只查询调度派工到当前班组的故障提票
		searchParams.repairTeamId = teamOrgId;
		MyJson.deleteBlankProp(searchParams);
		
		// 针对日期条件过滤的特殊处理
		var start = searchParams.planStartDate;
		var end = searchParams.planEndDate;
		var sql = null;
		if (!Ext.isEmpty(start) && !Ext.isEmpty(end)) {
			sql = "FAULT_OCCUR_TIME >= to_date('" + start + " 00:00:00', 'YYYY-MM-DDHH24:MI:SS') AND FAULT_OCCUR_TIME <= to_date('" + end + " 23:59:59', 'YYYY-MM-DDHH24:MI:SS')";
		} else if (Ext.isEmpty(start) && !Ext.isEmpty(end)) {
			sql = "FAULT_OCCUR_TIME <= to_date('" + end + " 23:59:59', 'YYYY-MM-DDHH24:MI:SS')";
		} else if (!Ext.isEmpty(start) && Ext.isEmpty(end)) {
			sql = "FAULT_OCCUR_TIME >= to_date('" + start + " 00:00:00', 'YYYY-MM-DDHH24:MI:SS')";
		}
		if (null != sql) {
			whereList.push({compare: Condition.SQL, sql: sql});
		}
		// 其它查询条件
		for (var prop in searchParams) {
			if (prop == 'planStartDate' || prop == 'planEndDate') {
				continue;
			}
			if (prop == "equipmentName") {
				whereList.push({compare: Condition.SQL, sql: "(EQUIPMENT_NAME LIKE '%"+ searchParams[prop] +"%' OR EQUIPMENT_CODE LIKE '%"+ searchParams[prop] +"%')"});
				continue;
			}
			whereList.push({propName: prop, propValue: searchParams[prop], stringLike: true});
		}
		this.baseParams.whereListJson = Ext.encode(whereList);
	});
	// 数据源加载完成后，对退回状态的数据进行高亮显示
	FaultOrder.grid.store.on('load', function(store, records, options ) {
		for (var i = 0; i < store.getCount(); i++) {
			if (STATE_CLZ === store.getAt(i).get('state')) {
				var row = FaultOrder.grid.getView().getRow(i);
				row.childNodes[0].style.color = 'green';
			}
		}
	});
	/** **************** 定义提票单表格结束 **************** */
	
	// 页面自适应布局
	new Ext.Viewport({
		layout: 'border',
		items: [{
			region: 'north', height: 110,
			title: '查询&nbsp;<span style="color:gray;font-weight:normal;">输入完成后按回车键查询</span>',
			collapsible: true, frame: true,
			items: FaultOrder.searchForm,
			buttonAlign: 'center',
			buttons: [{
				text: '查询', iconCls: 'searchIcon', handler: function() {
					var form = FaultOrder.searchForm.getForm();
					if (!form.isValid()) {
						return;
					}
					FaultOrder.grid.store.load();
				}
			}, {
				text: '重置', iconCls: 'resetIcon', handler: function() {
					FaultOrder.searchForm.getForm().reset();
					FaultOrder.grid.store.load();
				}
			}]
		}, FaultOrder.grid]
	});
	
});