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
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	FaultOrder.searchForm = new Ext.form.FormPanel({
		padding: 10, 
		layout: 'column',
		defaults: {layout: 'form', columnWidth: .33, defaults: {
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
//		}, {
//			items: [{
//				name: 'submitEmp', fieldLabel: '提报人'
//			}]
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
	
	/** **************** 定义保存表单开始 **************** */
	FaultOrder.saveForm = new Ext.form.FormPanel({
		baseCls: 'x-plain',
		labelWidth: FaultOrder.labelWidth,
		padding: 10, layout: 'column',
		defaults: {
			layout: 'form', columnWidth: .5, baseCls: 'x-plain',
			defaults: {
				xtype: 'textfield', width: FaultOrder.fieldWidth
			}
		},
		items: [{
			items: [{
				name: 'faultOrderNo', fieldLabel: '提票单号', maxLength: 20, allowBlank: false
			}, {
		    	xtype: 'OmEmployee_SelectWin', fieldLabel: '提票人',
		    	displayField:'empname', valueField: 'empid', allowBlank : false,
		    	editable: false, width: 140, hiddenName: 'submitEmpId',
		    	returnField :[{widgetId: "id_submit_emp", propertyName: "empname"}]
			}]
		}, {
			items: [{
				name: 'faultOccurTime', xtype: 'my97date', format: 'Y-m-d H:i', fieldLabel: '故障发现时间', allowBlank: false
			}, {
				hiddenName: 'faultLevel', fieldLabel: '故障等级', maxLength: 10,
				xtype: 'combo', 
				typeAhead: true,
			    triggerAction: 'all',
			    lazyRender:true,
			    mode: 'local',
			    store: new Ext.data.ArrayStore({
			    	fields: [ 'k', 'v' ],
			        data: [[
			            FAULT_LEVEL_YB, FAULT_LEVEL_YB		// 一般
	                 ],  [
	                    FAULT_LEVEL_ZD, FAULT_LEVEL_ZD		// 重大
	                 ], [
	                    FAULT_LEVEL_TD, FAULT_LEVEL_TD		// 特大
	                ]]
			    }),
			    value: FAULT_LEVEL_YB,
			    valueField: 'k', displayField: 'v'
			}]
		}, {
			columnWidth: 1,
			items: [{
				width: 265, 
				fieldLabel: '设备名称',
				xtype: 'simpleentitycombo',
				hiddenName: 'equipmentIdx',
				maxLength: 30, allowBlank: false,
				editable: true, 
				valueField: 'idx',
				displayFields: ['equipmentCode', 'equipmentName'],
				entity: 'com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo',
				fields: ['idx', 'equipmentCode', 'equipmentName', 'model', 'specification'],
				returnField: [{
					widgetId: "id_equipment_name", propertyName: "equipmentName"
				}, {
					widgetId: "id_equipment_code", propertyName: "equipmentCode"
				}, {
					widgetId: "id_model", propertyName: "model"
				}, {
					widgetId: "id_specification", propertyName: "specification"
				}]
			}]
		}, {
			columnWidth: 1,
			items: [{
				xtype: 'textarea', name: 'faultPlace', fieldLabel: '故障部位及意见', maxLength: 300, width: 402
			}]
		}, {
			columnWidth: 1, defaults: {
				xtype: 'textarea', width: 402
			},
			items: [{
				name: 'faultPhenomenon', fieldLabel: '故障现象', maxLength: 300
			}]
		}, {
			columnWidth: 1, defaults: {xtype: 'hidden'},
			items: [{
				name: 'idx', fieldLabel: 'idx主键'
			}, {
				id: 'id_equipment_name', name: 'equipmentName', fieldLabel: '设备名称'
			}, {
				id: 'id_equipment_code', name: 'equipmentCode', fieldLabel: '设备编码'
			}, {
				id: 'id_model', name: 'model', fieldLabel: '型号'
			}, {
				id: 'id_specification', name: 'specification', fieldLabel: '规格'
			}, {
				id: 'id_submit_emp', name: 'submitEmp', fieldLabel: '提报人'
			}, {
				name: 'state', value: STATE_XJ, fieldLabel: '提票状态'
			}]
		}]
	});
	/** **************** 定义保存表单结束 **************** */
	
	/** **************** 定义提票单表格开始 **************** */
	FaultOrder.grid = new Ext.yunda.Grid({
		region: 'center',
		loadURL: ctx + '/faultOrder!pageQuery.action',
		saveURL: ctx + '/faultOrder!saveOrUpdate.action',
		deleteURL: ctx + '/faultOrder!logicDelete.action',
		saveAndAdd: true, viewConfig: false, modalWindow: true,
		saveWinWidth: 600, saveForm: FaultOrder.saveForm,
		tbar: ['add', 'delete', {
			text: '查看照片', iconCls: 'imageIcon', disabled: false, id: 'id_image_view_btn', handler: function() {
				ImageView.show(FaultOrder.grid);
			}
		},'refresh', '-' ,'<span style="color:red;font-weight:bold;">提票状态：</span>', {
			xtype: 'checkbox', checked: true, boxLabel: '新建&nbsp;&nbsp;', inputValue: STATE_XJ,
			listeners: {
				check: function(me) {
					FaultOrder.grid.store.load();
				}
			}
		}, {
			xtype: 'checkbox', checked: false, boxLabel: '调度已派工&nbsp;&nbsp;', inputValue: STATE_YPG,
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
		}, {
			xtype: 'checkbox', checked: true, boxLabel: '退回&nbsp;&nbsp;', inputValue: STATE_TH,
			listeners: {
				check: function(me) {
					FaultOrder.grid.store.load();
				}
			}
		}, '-', '<span style="color:red;font-weight:bold;">快速提票：</span>', {
			xtype: 'textfield', width: 150, emptyText: '输入设备编码快速提票',
			enableKeyEvents: true, listeners: {
				keyup: function(me, e) {
					if (e.ENTER == e.keyCode) {
						var equipmentCode = this.getValue();
						if (Ext.isEmpty(equipmentCode)) {
							return;
						}
						// 使用设备编码快速提票
						QuickNotice.quickSubmit(equipmentCode);
					}
				}
			}
		}, '<span style="color:gray;">&nbsp;输入完成后按回车确认！目前仅支持精确查找</span>', '->', '-', '故障等级：', {
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
		fields: [Attachment.createColModeJson({
			attachmentKeyName:'e_fault_order'
		}),{
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
			dataIndex: 'model', header: '型号', hidden: true
		}, {
			dataIndex: 'specification', header: '规格', hidden: true
		}, {
			dataIndex: 'submitEmp', header: '提报人', width: 60
		}, {
			dataIndex: 'submitEmpId', header: '提报人id', hidden: true
		}, {
			dataIndex: 'state', header: '提票状态', renderer: function(v, metadata, record, rowIndex, collIndex, store) {
				if (STATE_YPG == v) {
					return '调度已派工';
				}
//				if (STATE_TH == v) {
//					metadata.attr = 'style=font-weight:bold;';
//				}
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
		afterShowSaveWin: function() {
			this.saveForm.findByType('OmEmployee_SelectWin')[0].setDisplayValue(empid, uname);
			this.saveForm.find('name', 'submitEmp')[0].setValue(uname);
			// Modified by hetao on 2016-11-03  显示新增窗口后重置“设备名称”控件值
			this.saveForm.findByType('simpleentitycombo')[0].clearValue();
			// 自动生成提票单编号
			FaultOrder.createFaultOrderNo(this.saveForm.find('name', 'faultOrderNo')[0]);
		},
		afterShowEditWin: function(record, rowIndex) {
			this.saveForm.findByType('OmEmployee_SelectWin')[0].setDisplayValue(record.get('submitEmpId'), record.get('submitEmp'));
			this.saveForm.findByType('simpleentitycombo')[0].setDisplayValue(record.get('equipmentIdx'), record.get('equipmentCode') + " " + record.get('equipmentName'));
		},
		afterAddSuccessFn: function(result, response, options) {
			if (this.saveForm.find('name', 'idx')[0]) this.saveForm.find('name', 'idx')[0].reset();
			// 清除设备信息
			this.saveForm.find('hiddenName', 'faultLevel')[0].reset();
			this.saveForm.find('name', 'faultPlace')[0].reset();
			this.saveForm.find('name', 'faultPhenomenon')[0].reset();
		},
		beforeShowEditWin: function(record, rowIndex) {
			if (STATE_XJ != record.get('state')) {
				MyExt.Msg.alert('处理中的提票单不可以进行编辑！');
				return false;
			}
			return true;
		},
		beforeDeleteFn: function() {
			var selections = this.getSelectionModel().getSelections();
			for (var i = 0; i < selections.length; i++) {
				if (STATE_XJ != selections[i].get('state')) {
					MyExt.Msg.alert('提票单（' + selections[i].get('faultOrderNo') + '）已经正在处理中，不能删除！');
					return false;
				}
			}
	        return true;
	    },
	});
	FaultOrder.grid.store.on('beforeload', function() {
		whereList = [];
		// 只查询提票人为当前系统操作员的提票记录
		whereList.push({propName: 'submitEmpId', propValue: empid, stringLike: false});
		
		// 提票状态快速过滤
		var tbar = FaultOrder.grid.getTopToolbar();
		var checkboxs = tbar.findByType('checkbox');
		var propValues = [-1];
		for (var i = 0; i < 4; i++) {
			if (checkboxs[i].checked) {
				propValues.push(checkboxs[i].getRawValue())
			}
		}
		whereList.push({compare: Condition.IN, propName: 'state', propValues: propValues});
		// 故障等级快速过滤
		var tbar = FaultOrder.grid.getTopToolbar();
		var checkboxs = tbar.findByType('checkbox');
		var propValues = [-1];
		for (var i = 4; i < checkboxs.length; i++) {
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
		for (var prop in searchParams) {
			if (prop == 'planStartDate' || prop == 'planEndDate') {
				continue;
			}
			if (prop == "equipmentName") {
				whereList.push({compare: Condition.SQL, sql: "(EQUIPMENT_NAME LIKE '%"+ searchParams[prop] +"%' OR EQUIPMENT_CODE LIKE '%"+ searchParams[prop] +"%')"});
				continue;
			}
			whereList.push({propName: prop, propValue: searchParams[prop]});
		}
		this.baseParams.whereListJson = Ext.encode(whereList);
	});
	
	// 数据源加载完成后，对退回状态的数据进行高亮显示
	FaultOrder.grid.store.on('load', function(store, records, options ) {
		for (var i = 0; i < store.getCount(); i++) {
			if (STATE_TH === store.getAt(i).get('state')) {
				var row = FaultOrder.grid.getView().getRow(i);
				row.childNodes[0].style.color = 'red';
			} else if (STATE_XJ !== store.getAt(i).get('state')) {
				var row = FaultOrder.grid.getView().getRow(i);
				row.childNodes[0].style['background-color'] = '#00FF00';
			}
		}
	});
	
	/*// 选择记录时，验证被选择的记录是否有照片附件
	FaultOrder.grid.getSelectionModel().on('rowselect', function(me, rowIndex, r) {
		var btn = Ext.getCmp('id_image_view_btn');
		if (1 != me.getCount()) {
			btn.disable();
			return;
		}
		// Ajax请求
		Ext.Ajax.request({
			url: ctx + '/attachment/findImages.do',
			params: {idx: r.id},
			// 请求成功后的回调函数
			success: function(response, options) {
	 	        var result = Ext.util.JSON.decode(response.responseText);
	 	        if (true === result.success) {       //操作成功     
	 	        	var list = result.list;
	 	        	if (0 >= list.length) {
	 	        		btn.disable();
	 	        	} else {
	 	        		btn.enable();
	 	        	}
	 	        } else {                           //操作失败
	 	            alertFail(result.errMsg);
	 	        }
	 	    },
	 	    // 请求失败后的回调函数
	 	    failure: function(response, options) {
	 	        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + ('' || response.responseText));
	 	    }
		});
	});*/
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