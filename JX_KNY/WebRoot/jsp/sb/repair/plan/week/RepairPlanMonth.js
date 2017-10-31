Ext.onReady(function() {
	
	Ext.ns('RepairPlanMonth');
	
	/** **************** 定义私有变量开始 **************** */
	var sm = new Ext.grid.CheckboxSelectionModel(),
		cm, store;
	var labelWidth = 100, fieldWidth = 140;
	/** **************** 定义私有变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 添加设备检修年计划
	 * @param planYear 计划年份
	 * @param equipmentIds 设备信息idx主键数组
	 */
	RepairPlanMonth.saveFn = function(planYear, equipmentIds, callback) {
		if(self.loadMask)    self.loadMask.show();
		// 提交后台进行数据存储
		$yd.request({
			url : ctx + '/repairPlanYear!save.action',
			params : {planYear : planYear, equipmentIds : Ext.encode(equipmentIds)},
			scope : RepairPlanMonth.grid
		},
		// Ajax请求成功后的回调函数
		function(result) {
			this.store.reload({
				callback: callback
			});
		});
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	RepairPlanMonth.searchForm = new Ext.form.FormPanel({
		padding: 10,
		labelWidth: labelWidth,
		layout: 'column', 
		buttonAlign: 'center',
		defaults: {
			columnWidth: .25, layout: 'form', defaults: {
				 
				xtype: 'singlefieldcombo', 
				// 指定实体的全路径类名称
				entity: 'com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo', 
				// 指定的查询的字段名称
				xfield: 'equipmentName',
				whereList: [{
					propName: 'orgId', propValue: teamOrgId
				}, {
					sql: 'idx In (Select Distinct equipmentIdx From RepairPlanMonth Where recordStatus = 0 And planYear = '+ new Date().getFullYear() +' And planMonth = '+ (new Date().getMonth() + 1) +')', compare: Condition.SQL
				}],
				width: fieldWidth, enableKeyEvents: true, 
				listeners: {
					keyup: function( me, e ) {
						if (e.ENTER == e.keyCode) {
							RepairPlanMonth.grid.store.load();
						}
					},
					select: function() {
						RepairPlanMonth.grid.store.load();
					}
				}
			}
		},
		items: [{
			items: [{
				 xtype: 'OmOrganizationCustom_comboTree', fieldLabel: '单位',
				 hiddenName: 'orgId', selectNodeModel:'exceptRoot' , width: 140, editable: false,
				 listeners : {
					select: function () {
						RepairPlanMonth.grid.store.load();
						// 重新加载查询条件控件数据源
						var cmps = RepairPlanMonth.searchForm.findByType('singlefieldcombo');
						for (var i = 0; i < cmps.length; i++) {
							cmps[i].reset();
							cmps[i].whereList[0].propValue = this.getValue();
							cmps[i].reload();
						}
					}
       			 }
			}]
		}, {
			items: [{
				xtype: 'compositefield', fieldLabel: '计划年月', combineErrors: false, anchor:'100%',
				defaults: {
					width: 60
				},
				items: [{
					hiddenName: 'planYear', id: 'plan_year_combo', xtype: 'combo',
			        store: function() {
			        	var data = [];
			        	var year = new Date().getFullYear();
			        	data.push([year + 1, year + 1]);			// 下一年
			        	for (var i = 0; i < 2; i++) {
			        		data.push([year - i, year - i]);
			        	}
			        	return new Ext.data.SimpleStore({
						    fields: ['k', 'v'],
							data: data
						})
			        }(),
					valueField:'k', displayField:'v',
					value: new Date().getFullYear(),
					editable: false,
					triggerAction:'all',
					mode:'local',
					listeners: {
						select: function( combo, record, index ) {
							var year = Ext.getCmp('plan_year_combo').getValue();
							var month = Ext.getCmp('plan_month_combo').getValue();
							RepairPlanMonth.grid.store.load();
							// 重新加载查询条件控件数据源
							var cmps = RepairPlanMonth.searchForm.findByType('singlefieldcombo');
							for (var i = 0; i < cmps.length; i++) {
								cmps[i].reset();
								if (!Ext.isEmpty(month)) {
									cmps[i].whereList[1].sql = 'idx In (Select Distinct equipmentIdx From RepairPlanMonth Where recordStatus = 0 And planYear = '+ year +' And planMonth = '+ month +')';
								} else {
									cmps[i].whereList[1].sql = 'idx In (Select Distinct equipmentIdx From RepairPlanMonth Where recordStatus = 0 And planYear = '+ year +')';
								}
								cmps[i].reload();
							}
						}
					}
				}, {
					width: 15, xtype: 'label', text: '年', style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}, {							
					hiddenName: 'planMonth', id: 'plan_month_combo', xtype: 'combo',
					store: function() {
						var data = [];
						var year = new Date().getFullYear();
						data.push(['', '全部']);							// 全部
						for (var i = 1; i <= 12; i++) {
							data.push([i, i]);
						}
						return new Ext.data.SimpleStore({
							fields: ['k', 'v'],
							data: data
						})
					}(),
					valueField:'k', displayField:'v',
					value: new Date().getMonth() + 1,
					editable: false,
					triggerAction:'all',
					mode:'local',
					listeners: {
						select: function( combo, record, index ) {
							var year = Ext.getCmp('plan_year_combo').getValue();
							var month = Ext.getCmp('plan_month_combo').getValue();
							RepairPlanMonth.grid.store.load();
							// 重新加载查询条件控件数据源
							var cmps = RepairPlanMonth.searchForm.findByType('singlefieldcombo');
							for (var i = 0; i < cmps.length; i++) {
								cmps[i].reset();
								if (!Ext.isEmpty(month)) {
									cmps[i].whereList[1].sql = 'idx In (Select Distinct equipmentIdx From RepairPlanMonth Where recordStatus = 0 And planYear = '+ year +' And planMonth = '+ month +')';
								} else {
									cmps[i].whereList[1].sql = 'idx In (Select Distinct equipmentIdx From RepairPlanMonth Where recordStatus = 0 And planYear = '+ year +')';
								}
								cmps[i].reload();
							}
						}
					}
				}, {
					width: 15, xtype: 'label', text: '月', style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}]
			}]
		}, {
			items: [{
				fieldLabel: '设备名称'
			}]
		}, {
			items: [{
				fieldLabel: '设备编码', xfield: 'equipmentCode'
			}]
		}],
		buttons: [{
			text: '查询', iconCls: 'searchIcon', handler: function() {
				RepairPlanMonth.grid.store.load();
			}
		}, {
			text: '重置', iconCls: 'resetIcon', handler: function() {
				RepairPlanMonth.searchForm.getForm().reset();
				RepairPlanMonth.searchForm.findByType('OmOrganizationCustom_comboTree')[0].clearValue();
				// 重新加载查询条件控件数据源
				var cmps = RepairPlanMonth.searchForm.findByType('singlefieldcombo');
				for (var i = 0; i < cmps.length; i++) {
					cmps[i].reset();
					cmps[i].whereList[0].propValue = teamOrgId;
					cmps[i].whereList[1].sql = 'idx In (Select Distinct equipmentIdx From RepairPlanMonth Where recordStatus = 0 And planYear = '+ new Date().getFullYear() +' And planMonth = '+ (new Date().getMonth() + 1) +')';
					cmps[i].reload();
				}
				RepairPlanMonth.grid.store.load();
			}
		}]
	});
	/** **************** 定义查询表单结束 **************** */
	
	/**
	 * 表格列模型
	 */
	var cm = new Ext.grid.ColumnModel({
		columns: [new Ext.grid.RowNumberer(), sm, {
			dataIndex : 'idx', header : 'idx主键', hidden : true
		}, {
			dataIndex : 'equipmentIdx', header : '设备主键', hidden : true
		}, {
			dataIndex : 'orgName', header : '单位名称', width: 160
		}, {
			dataIndex : 'classCode', header : '类别编码', hidden:true
		}, {
			dataIndex : 'className', header : '设备类别', hidden:true, renderer: function(v, m, r) {
				return [v, "(", r.get('classCode'), ")"].join('');
			}, width: 140
		}, {
			header:'设备编码', dataIndex:'equipmentCode'
		}, {
			header:'设备名称', dataIndex:'equipmentName', width: 240
		}, {
			dataIndex : 'planYear', header : '年度', width: 55, align: 'center'
		}, {
			dataIndex : 'planMonth', header : '月份', width: 55, align: 'center'
		}, {
			dataIndex : 'repairClass', header : '修程', width: 55, align: 'center', renderer: function(v) {
				if (REPAIR_CLASS_SMALL == v) return '小';
				if (REPAIR_CLASS_MEDIUM == v) return '中';
				if (REPAIR_CLASS_SUBJECT == v) return '项';
			}
		}, {
			dataIndex : 'beginTime', header: '开工时间', /*xtype: 'datecolumn', format: 'Y-m-d',*/ editor: {	
				xtype: 'my97date'
			}, renderer: function(value, metaData, record) {
				if (PLAN_STATUS_WXF == record.get('planStatus')) {
					metaData.css = 'editCell';
				}
				return value ? new Date(value).format('Y-m-d') : '';
			}
		}, {
			dataIndex : 'endTime', header : '完工时间', /*xtype: 'datecolumn', format: 'Y-m-d', */editor: {
				xtype: 'my97date'
			}, renderer: function(value, metaData, record) {
				if (PLAN_STATUS_WXF == record.get('planStatus')) {
					metaData.css = 'editCell';
				}
				return value ? new Date(value).format('Y-m-d') : '';
			}
		}, {
			dataIndex : 'planStatus', header : '计划状态', renderer: function(value) {
				if (PLAN_STATUS_WXF == value) return "未下发";
				if (PLAN_STATUS_YXF == value) return "已下发";
			}
		}, {
			dataIndex : 'model', header : '型号'
		}, {
			dataIndex : 'specification', header : '规格'
		}, {
			dataIndex : 'mechanicalCoefficient', header : '机械系数', width: 65
		}, {
			dataIndex : 'electricCoefficient', header : '电气系数', width: 65
		}, {
			dataIndex : 'usePlace', header : '设置地点'
		}, {
			dataIndex : 'mechanicalRepairTeam', header : '机械维修班组'
		}, {
			dataIndex : 'electricRepairTeam', header : '电气维修班组'
		}]
	});
	
	/**
	 * 数据源
	 */
	store = new Ext.data.JsonStore({
        id: 'idx', 
        root: "root", 
        totalProperty: "totalProperty", 
        autoLoad: true,
        remoteSort: true,
        url: ctx + '/repairPlanMonth!queryPageList.action',
        fields: ['idx', 'equipmentIdx', 'planYear', 'planMonth', 'repairClass', 'beginTime', 'endTime', 'planStatus', 'classCode', 'className', 'model', 'specification', 'usePlace', 'mechanicalCoefficient', 'electricCoefficient', 'mechanicalRepairTeam', 'electricRepairTeam', 'equipmentName', 'equipmentCode', 'orgName'],
        listeners: {
        	beforeload: function() {
        		var searchParams = RepairPlanMonth.searchForm.getForm().getValues();
        		MyJson.deleteBlankProp(searchParams);
        		// 计划状态快速过滤
        		var tbar = RepairPlanMonth.grid.getTopToolbar();
        		var checkboxs = tbar.findByType('checkbox');
        		var propValues = [-1];
        		for (var i = 0; i < checkboxs.length; i++) {
        			if (checkboxs[i].checked) {
        				propValues.push(checkboxs[i].getRawValue())
        			}
        		}
        		this.baseParams.planStatus = propValues.join(',');
        		this.baseParams.entityJson = Ext.encode(searchParams);
        	},
        	load: function() {
        		for (var i = 0; i < this.getCount(); i++) {
        			if (PLAN_STATUS_YXF == this.getAt(i).get('planStatus')) {
        				var row = RepairPlanMonth.grid.getView().getRow(i);
        				row.childNodes[0].style['background-color'] = '#00FF00';
        			}
        		}
        	}
        }
    }); 

	/**
	 * 表格
	 */
	RepairPlanMonth.grid = new Ext.grid.EditorGridPanel({
		border: false,
		entity: 'com.yunda.sb.repair.plan.entity.RepairPlanMonth',
		deleteURL: ctx + '/repairPlanMonth!logicDelete.action',
		sm: sm,
		cm: cm,
		storeId: 'idx',
        store: store,
        stripeRows: true,
        loadMask: new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." }),
        clicksToEdit: 1,
        tbar: ['-', {
        	text: '下发计划', iconCls: 'addIcon', handler: function() {
        		var sm = RepairPlanMonth.grid.getSelectionModel();
        		if (0 >= sm.getCount()) {
        			MyExt.Msg.alert('尚未选择任何记录！');
        			return;
        		}
        		var selections = sm.getSelections();
        		var planMonthIdxs = [];
        		// 该标识用于设置下发计划时，是否需要验收字段，默认小修的检修任务不需要验收处理
        		RepairPlanPublish.repairClass = 1;			// 小修
        		// 以下两个标识字段，用于标识计划下发界面是否加载“机械”或者“电气”班组，需求：如果设备的“机械系数”为0，则没有机械检修项，如果设备的“电气系数”为0，则没有电气检修项
        		RepairPlanPublish.mac = null;
        		RepairPlanPublish.elc = null;
        		var id, errMsg;					// 不能下发的月检修计划记录id
        		for (var i = 0; i < selections.length; i++) {
        			var planStatus = selections[i].get('planStatus'),			// 计划状态
        				beginTime = selections[i].get('beginTime'),				// 开工时间
        				endTime = selections[i].get('endTime');					// 完工时间
        			if (null == id && PLAN_STATUS_YXF == planStatus) {
        				errMsg = "月计划已经下发！";
        				// 选中不能下发的月检修计划记录
        				id = selections[i].id;
        				
        			}
        			if (null == id && Ext.isEmpty(beginTime)) {
        				errMsg = "开工时间为空！";
        				// 选中不能下发的月检修计划记录
        				id = selections[i].id;
        			}
        			if (null == id && Ext.isEmpty(endTime)) {
        				errMsg = "的完工时间为空！";
        				// 选中不能下发的月检修计划记录
        				id = selections[i].id;
        			}
        			if (null == id && new Date(beginTime).getTime() > new Date(endTime).getTime()) {
        				errMsg = "开工时间不能大于完工时间！";
        				// 选中不能下发的月检修计划记录
        				id = selections[i].id;
        				
        			}
        			if (null != id) {
        				alertFail(selections[i].get('equipmentName') + "(编号：" + selections[i].get('equipmentCode') + ")的" + errMsg);
        				sm.selectRow(store.indexOf(store.getById(id)));
        				return;
        			}
        			planMonthIdxs.push(selections[i].id);
        			// 如果多选中含有不是“小修”的检修任务，则不进行默认设置
        			if (null != RepairPlanPublish.repairClass && 1 != selections[i].get('repairClass')) {
        				RepairPlanPublish.repairClass = null;
        			}
        			if (null == RepairPlanPublish.mac && 0 < selections[i].get('mechanicalCoefficient')) {
        				RepairPlanPublish.mac = true;
        			}
        			if (null == RepairPlanPublish.elc && 0 < selections[i].get('electricCoefficient')) {
        				RepairPlanPublish.elc = true;
        			}
        		}
        		RepairPlanPublish.planMonthIdxs = planMonthIdxs;
        		RepairPlanPublish.win.show();
        	}
        }, {
        	text: '撤销计划', iconCls: 'deleteIcon', id: 'id_rescind_btn', hidden: true, handler: function() {
        		var sm = RepairPlanMonth.grid.getSelectionModel();
        		if (0 >= sm.getCount()) {
        			MyExt.Msg.alert('尚未选择任何记录！');
        			return;
        		}
        		var selections = sm.getSelections();
        		var ids = [];
        		for (var i = 0; i < selections.length; i++) {
        			ids.push(selections[i].id);
        		}
        		Ext.Msg.confirm('提示', '该操作将不能恢复是否继续？', function(btn) {
        			if ('yes' == btn) {
        				$yd.request({
        					url: ctx + '/repairPlanMonth!updateForRescind.action',
        					jsonData: ids
        				}, function() {
        					store.reload();
        					alertSuccess();
        					// 隐藏“撤销计划”操作按钮
        					Ext.getCmp('id_rescind_btn').hide();
        				});
        			}
        		});
        	}
        }, {
        	text: '刷新', iconCls: 'refreshIcon', handler: function() {
        		self.location.reload();
        	}
        }, '->', '-','计划状态：', {
			xtype: 'checkbox', checked: true, boxLabel: '未下发&nbsp;&nbsp;', inputValue: PLAN_STATUS_WXF,
			listeners: {
				check: function(me) {
					RepairPlanMonth.grid.store.load();
				}
			}
		}, {
			xtype: 'checkbox', checked: false, boxLabel: '<span style="background-color:#00FF00;">已下发</span>&nbsp;&nbsp;', inputValue: PLAN_STATUS_YXF,
			listeners: {
				check: function(me) {
					RepairPlanMonth.grid.store.load();
				}
			}
		}],
        bbar: $yd.createPagingToolbar({pageSize:20, store: store}),
        listeners: {
        	beforeedit: function(e) {
        		// 已下发的月检修计划，不可以编辑开始、结束时间
        		var record = e.record;
        		if (PLAN_STATUS_YXF == record.get('planStatus')) {
        			e.cancel = true
        		}
        	},
        	afteredit: function(e) {
        		// Modified by hetao on 2017-01-09 增加开工时间、完工时间的有效性验证
        		var isValid = true;
        		if ("endTime" === e.field) {
        			var beginTime = new Date(e.record.get('beginTime'));
        			if (e.value < beginTime) {
        				isValid = false;
        			}
        		} else {
        			var endTime = new Date(e.record.get('endTime'));
        			if (e.value > endTime) {
        				isValid = false;
        			}
        		}
        		if (!isValid) {
        			MyExt.Msg.alert('“开工时间”不能晚于“完工时间”，请重新输入！');
        			e.grid.store.reload();
        			return;
        		}
        		e.grid.fireEvent('update', this, e.record);
        	},
        	update: function( me, record, operation ) {
        		var values = record.getChanges();
        		// 获取最后一次编辑的字段名称
        		var fieldName;
        		for (var prop in values) {
        			fieldName = prop;
        		}
        		values.idx = record.id;
        			
        		// 提交后台进行数据存储
        		$yd.request({
        			scope: this,
        			url: ctx + '/sbCommon!saveChange.action',
        			params: {
        				className: this.entity,
        				fieldName:  fieldName,
        				entityJson: Ext.encode(values)	
        			}},
        			function(result) {
        				// 如果不提交，record.getChanges()方法会始终记录所有的记录变更
//        				record.commit();
        			}
        		);
        	}
        }
    });
	
	RepairPlanMonth.grid.getSelectionModel().on('selectionchange', function(sm) {
		var rescindBtn = Ext.getCmp('id_rescind_btn');
		if (sm.getCount() <= 0 && !rescindBtn.hidden) {
			rescindBtn.hide();
			return;
		}
		var selections = sm.getSelections();
		var showable = false;
		for (var i = 0; i < selections.length; i++) {
			if (PLAN_STATUS_YXF == selections[i].get('planStatus')) {
				showable = true;
				break;
			}
		}
		if (showable) {
			rescindBtn.show();
		} else {
			rescindBtn.hide();
		}
	});
	
	new Ext.Viewport({
		layout: 'border',
		defaults: { layout: 'fit', border: false },
		items: [{
			region: 'north', height: 110, title: '查询&nbsp;<span style="color:gray;font-weight:normal;">输入完成后按回车键查询</span>',
			collapsible: true, frame: true,
			items: [RepairPlanMonth.searchForm]
		}, {
			region: 'center',
			items: [RepairPlanMonth.grid]
		}]
	});
	
});