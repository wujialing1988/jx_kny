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

		var formPanel = Dispatch.win.findByType('form')[0];
		// 回显主修班组
		formPanel.find('hiddenName', 'repairTeamId')[0].setDisplayValue(record.get('repairTeamId'), record.get('repairTeam'));
		formPanel.find('name', 'repairTeam')[0].setValue(record.get('repairTeam'));
		// 回显辅修班组
		formPanel.find('hiddenName', 'assistRepairTeamId')[0].setDisplayValue(record.get('assistRepairTeamId'), record.get('assistRepairTeam'));
		formPanel.find('name', 'assistRepairTeam')[0].setValue(record.get('assistRepairTeam'));
	}
	
	/**
	 * 退回
	 * @param backReason 默认的退回原因（自修故障）
	 */
	FaultOrder.updateBacked = function(ids, backReason) {
		if (Ext.isEmpty(ids)) {
			alertFail('请您先选择需要回退的故障提票单！');
			return;
		}
		Ext.Msg.prompt('退回', '请输入退回原因...', function(btn, backReason, obj) {
			if ('ok' === btn) {
				if (backReason.length > 200) {
					MyExt.Msg.alert('回退原因的最大输入长度为200！');
					FaultOrder.updateBacked(backReason);
					return;
				}
				if(self.loadMask)    self.loadMask.show();
				Ext.Ajax.request({
					url : ctx + '/faultOrder!updateBacked.action',
					params: {ids : Ext.encode(ids), backReason: backReason},
					scope: this,
				    success: function(response, options) {
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功     
				        	alertSuccess();
				        	this.store.reload();
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
		}, FaultOrder.grid, false, backReason)
	
		
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
		}],
		listeners: {/*
			render: function(me) {
				if (!Ext.isEmpty(equipmentCode)) {
					me.find('name', 'equipmentName')[0].setValue(equipmentCode);
				}
			}
		*/}
	});
	/** **************** 定义查询表单结束 **************** */
	
	/**
	 * 表格右键菜单
	 */
	var rightMenu = new Ext.ux.grid.RightMenu({
	    items:[{
	        text : '退回',
	        iconCls : 'deleteIcon',
	        recHandler:function(record, rowIndex, grid) {
	        	if (STATE_TH === record.get('state')) {
	        		alertFail('该提票单已经被退回！');
	        		return;
	        	}
	        	if (STATE_YPG === record.get('state')) {
					Ext.Msg.confirm('提示', '提票单已经派工，是否继续执行退回？', function(btn) {
						if ('yes' === btn) {
							FaultOrder.updateBacked([record.id], '自修故障');
						}
					});
				} else {
					FaultOrder.updateBacked([record.id], '自修故障');
				}
	        }
	    }]
	});
	/** **************** 定义提票单表格开始 **************** */
	FaultOrder.grid = new Ext.yunda.Grid({
		plugins:[ rightMenu ],
		region: 'center',
		loadURL: ctx + '/faultOrder!pageQuery.action',
		saveAndAdd: true, viewConfig: false, modalWindow: true,
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
				var selections = sm.getSelections();
				// 验证选择的记录中包含已退回的提票单
				var hasBacked = false;
				for (var i = 0; i < selections.length; i++) {
					if (STATE_TH === selections[i].get('state')) {
						hasBacked = true;
						break;
					}
				}
				if (hasBacked) {
					Ext.Msg.confirm('提示', '您选择的记录中包含已退回的提票单，是否继续执行派工？', function(btn) {
						if ('yes' === btn) {
							MultiDispatch.win.show();
						}
					});
				} else {
					MultiDispatch.win.show();
				}
			}
		}, {
			text: '退回', iconCls: 'deleteIcon', handler: function() {
				var sm = FaultOrder.grid.getSelectionModel();
				if (sm.getCount() <= 0) {
					MyExt.Msg.alert('尚未选择任何记录！');
					return;
				}
				var selections = sm.getSelections();
				// 验证选择的记录中包含已派工的提票单
				var hasDispatched = false;
				var ids = [];
				for (var i = 0; i < selections.length; i++) {
					if (STATE_TH === selections[i].get('state')) {
						continue;
					}
					ids.push(selections[i].id);
					if (STATE_YPG === selections[i].get('state') && !hasDispatched) {
						hasDispatched = true;
					}
				}
				if (hasDispatched) {
					Ext.Msg.confirm('提示', '您选择的记录中包含已派工的提票单，是否继续执行退回？', function(btn) {
						if ('yes' === btn) {
							FaultOrder.updateBacked(ids, '自修故障');
						}
					});
				} else {
					FaultOrder.updateBacked(ids, '自修故障');
				}
				
			}
		}, 'refresh', '-', '<span style="color:red;font-weight:bold;">提票状态：</span>', {
			xtype: 'checkbox', checked: true, boxLabel: '新建&nbsp;&nbsp;', inputValue: STATE_XJ,
			listeners: {
				check: function(me) {
					FaultOrder.grid.store.load();
				}
			}
		}, {
			xtype: 'checkbox', checked: false, boxLabel: '已派工&nbsp;&nbsp;', inputValue: STATE_YPG,
			listeners: {
				check: function(me) {
					FaultOrder.grid.store.load();
				}
			}
		}, {
			xtype: 'checkbox', checked: false, boxLabel: '退回&nbsp;&nbsp;', inputValue: STATE_TH,
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
			dataIndex: 'state', header: '提票状态'
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
			dataIndex: 'repairEmp', header: '修理人', hidden: true
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
		}, {
			dataIndex: 'backReason', header: '退回原因', width: 150
		}],
		toEditFn: function(grid, rowIndex, e) {
			var record = this.store.getAt(rowIndex);
			FaultOrder.dispatchFn(record);
		},
		listeners: {
			rowcontextmenu: function(me, rowIndex, e) {
				this.getSelectionModel().selectRow(rowIndex);
				var r = this.getStore().getAt(rowIndex);
				if (STATE_XJ != r.get('state')) {
					return false;
				}
				return true;
			}
		}
	});
	FaultOrder.grid.store.on('beforeload', function() {
		whereList = [];
		// 提票状态快速过滤
		var tbar = FaultOrder.grid.getTopToolbar();
		var checkboxs = tbar.findByType('checkbox');
		var propValues = [-1];
		for (var i = 0; i < 3; i++) {
			if (checkboxs[i].checked) {
				propValues.push(checkboxs[i].getRawValue())
			}
		}
		whereList.push({compare: Condition.IN, propName: 'state', propValues: propValues});
		
		// 故障等级快速过滤
		var tbar = FaultOrder.grid.getTopToolbar();
		var checkboxs = tbar.findByType('checkbox');
		var propValues = [-1];
		for (var i = 3; i < checkboxs.length; i++) {
			if (checkboxs[i].checked) {
				propValues.push(checkboxs[i].getRawValue())
			}
		}
		whereList.push({compare: Condition.IN, propName: 'faultLevel', propValues: propValues});
		// 其它查询条件
		var searchParams = FaultOrder.searchForm.getForm().getValues();
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
			if (STATE_TH === store.getAt(i).get('state')) {
				var row = FaultOrder.grid.getView().getRow(i);
				row.childNodes[0].style.color = 'red';
			} else if (STATE_YPG === store.getAt(i).get('state')) {
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