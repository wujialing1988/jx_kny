Ext.onReady(function() {
	
	Ext.ns('RepairTaskList');
	
	/** **************** 定义私有变量开始 **************** */
	var cm, store, labelWidth = 100, fieldWidth = 140;
	// row expander
    var expander = new Ext.ux.grid.RowExpander({
        tpl : new Ext.Template(
            '<p><b>Company:</b> {equipmentCode}</p><br>',
            '<p><b>Summary:</b> {equipmentName}</p>'
        )
    });
	/** **************** 定义私有变量结束 **************** */
    
    /** **************** 定义全局函数开始 **************** */
    /**
     * 显示设备检修范围明细窗口
     * @param idx 设备检修任务idx主键
     * @param rowIndex 设备检修任务列表索引
     */
    RepairTaskList.showRepairScopeCase = function(idx, rowIndex) {
    	RepairScopeCase.taskListIdx = idx;
    	RepairTaskList.rowIndex = rowIndex;
    	if (Ext.isEmpty(idx)) {
    		RepairScopeCase.taskListIdx = RepairTaskList.grid.getSelectionModel().getSelections()[0].id;
    		RepairTaskList.rowIndex = RepairTaskList.grid.store.indexOfId(RepairScopeCase.taskListIdx);
    	}
		if (!RepairScopeCase.win.hidden) {
			RepairScopeCase.grid.store.load();
		} else {
			RepairScopeCase.win.show();
		}
	}
    /**
     * 查看照片，查看该检修任务下所有工单管理的照片
     */
    RepairTaskList.showImage = function() {
    	ImageView.show(RepairTaskList.grid, {
			title: '设备检修照片',
			businessName: 'repairTaskListManager'
		});
    }
    /** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	RepairTaskList.searchForm = new Ext.form.FormPanel({
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
					sql: 'idx In (Select Distinct equipmentIdx From RepairTaskList Where recordStatus = 0 And planMonthIdx In (Select idx From RepairPlanMonth Where recordStatus = 0 And planStatus = 1 And planYear = '+ new Date().getFullYear() +' And planMonth = '+ (new Date().getMonth() + 1) +'))', compare: Condition.SQL
				}],
				width: fieldWidth, enableKeyEvents: true, 
				listeners: {
					keyup: function( me, e ) {
						if (e.ENTER == e.keyCode) {
							RepairTaskList.grid.store.load();
						}
					},
					select: function() {
						RepairTaskList.grid.store.load();
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
						RepairTaskList.grid.store.load();
						// 重新加载查询条件控件数据源
						var cmps = RepairTaskList.searchForm.findByType('singlefieldcombo');
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
					width: 60,
					valueField:'k', displayField:'v',
					editable: false,
					triggerAction:'all',
					mode:'local',
					listeners: {
						select: function( combo, record, index ) {
							var year = Ext.getCmp('plan_year_combo').getValue();
							var month = Ext.getCmp('plan_month_combo').getValue();
							RepairTaskList.grid.store.load();
							// 重新加载查询条件控件数据源
							var cmps = RepairTaskList.searchForm.findByType('singlefieldcombo');
							for (var i = 0; i < cmps.length; i++) {
								cmps[i].reset();
								if (!Ext.isEmpty(month)) {
									cmps[i].whereList[1].sql = 'idx In (Select Distinct equipmentIdx From RepairTaskList Where recordStatus = 0 And planMonthIdx In (Select idx From RepairPlanMonth Where recordStatus = 0 And planStatus = 1 And planYear = '+ year +' And planMonth = '+ month +'))';
								} else {
									cmps[i].whereList[1].sql = 'idx In (Select Distinct equipmentIdx From RepairTaskList Where recordStatus = 0 And planMonthIdx In (Select idx From RepairPlanMonth Where recordStatus = 0 And planStatus = 1 And planYear = '+ year +'))';
								}
								cmps[i].reload();
							}
						}
					}
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
					value: new Date().getFullYear()
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
					value: new Date().getMonth() + 1
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
				RepairTaskList.grid.store.load();
			}
		}, {
			text: '重置', iconCls: 'resetIcon', handler: function() {
				RepairTaskList.searchForm.getForm().reset();
				RepairTaskList.searchForm.findByType('OmOrganizationCustom_comboTree')[0].clearValue();
				// 重新加载查询条件控件数据源
				var cmps = RepairTaskList.searchForm.findByType('singlefieldcombo');
				for (var i = 0; i < cmps.length; i++) {
					cmps[i].reset();
					cmps[i].whereList[0].propValue = teamOrgId;
					cmps[i].whereList[1].sql = 'idx In (Select Distinct equipmentIdx From RepairTaskList Where recordStatus = 0 And planMonthIdx In (Select idx From RepairPlanMonth Where recordStatus = 0 And planStatus = 1 And planYear = '+ new Date().getFullYear() +' And planMonth = '+ (new Date().getMonth() + 1) +'))';
					cmps[i].reload();
				}
				RepairTaskList.grid.store.load();
			}
		}]
	});
	/** **************** 定义查询表单结束 **************** */
	
	/**
	 * 表格列模型
	 */
	var cm = new Ext.grid.ColumnModel({
		columns: [/*expander, */new Ext.grid.RowNumberer(), {
			dataIndex : 'idx', header : 'idx主键', hidden : true
		}, {
			dataIndex : 'planMonthIdx', header : '检修月计划idx主键', hidden : true
		}, {
			dataIndex : 'equipmentIdx', header : '设备idx主键', hidden : true
		}, {
			dataIndex : 'orgName', header : '单位名称'
		}, {
			dataIndex : 'equipmentCode', header : '设备编号'
		}, {
			dataIndex : 'equipmentName', header : '设备名称', width: 240
		}, {
			dataIndex : 'repairClassName', header : '修程', width: 40, align: 'center'
		}, {
			dataIndex : 'realBeginTime', header : '实际开工时间', width: 120, align: 'center', renderer: function(v) {
				return v ? new Date(v).format('Y-m-d H:i') : '';
			}
		}, {
			dataIndex : 'realEndTime', header : '实际完工时间', width: 120, align: 'center', renderer: com.yunda.Common.realEndTimeRenderer2JX
		}, {
			header: '检修任务进度', renderer: com.yunda.Common.yclCountRenderer2JX, width: 110
		}, {
			header: "照片数量", dataIndex: "imageCount", width: 60, renderer: com.yunda.Common.imageCountRenderer2JX
		}, {
			dataIndex: 'createTime', header: '计划下发日期', renderer: function(v) {
				return new Date(v).format('Y-m-d H:i');
			}, width: 110
		}, {
			dataIndex : 'beginTime', header : '计划开工时间', width: 85, align: 'center', renderer: function(v) {
				return v ? new Date(v).format('Y-m-d') : '';
			}
		}, {
			dataIndex : 'endTime', header : '计划完工时间', width: 85, align: 'center', renderer: com.yunda.Common.planEndDatetRenderer2JX
		}, {
			dataIndex : 'repairTeamMac', header : '机械维修班组', renderer: com.yunda.Common.orgNameRenderer
		}, {
			dataIndex : 'repairTeamElc', header : '电气维修班组', renderer: com.yunda.Common.orgNameRenderer
		}, {
			dataIndex : 'state', header : '任务状态'
		}, {
			dataIndex : 'gzSignMac', header : '机械工长确认<span style="color:#00FF00;font-weight:bold;">&nbsp;&gt;&gt;</span>', renderer: function(v, metaData, record) {
				if (v || Ext.isEmpty(record.get('repairTeamMac'))) {
					metaData.css = 'finshedCell';
					return v;
				} else {
					metaData.css = 'processCell';
					return '<marquee direction="right" scrollamount="1">未处理</marquee>';
				}
			}, width: 100
		}, {
			dataIndex : 'gzSignElc', header : '电气工长确认<span style="color:#00FF00;font-weight:bold;">&nbsp;&gt;&gt;</span>', renderer: function(v, metaData, record) {
				if (v || Ext.isEmpty(record.get('repairTeamElc'))) {
					metaData.css = 'finshedCell';
					return v;
				} else {
					metaData.css = 'processCell';
					return '<marquee direction="right" scrollamount="1">未处理</marquee>';
				}
			}, width: 100
		}, {
			dataIndex: 'isNeedAcceptance', header: '是否需要验收', hidden: true
		}, {
			dataIndex : 'acceptorName', header : '验收签名<span style="color:#00FF00;font-weight:bold;">&nbsp;&gt;&gt;</span>', renderer: function(v, metaData, record) {
				if (v) {
					metaData.css = 'finshedCell';
					return v;
				} else if (!record.get('isNeedAcceptance')) {
					metaData.css = 'finshedCell';
					return v;
				} else {
					metaData.css = 'processCell';
					return '<marquee direction="right" scrollamount="1">未处理</marquee>';
				}
			}, width: 100
		}, {
			dataIndex : 'userName', header : '使用人确认<span style="color:#00FF00;font-weight:bold;">&nbsp;&bull;</span>', renderer: function(v, metaData) {
				if (v) {
					metaData.css = 'finshedCell';
					return v;
				} else {
					metaData.css = 'processCell';
					return '<marquee direction="right" scrollamount="1">未处理</marquee>';
				}
			}, width: 80
		}, {
			dataIndex : 'acceptanceReviews', header : '验收评语'
		}, {
			dataIndex : 'usePerson', header : '使用人', width: 60
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
        url: ctx + '/repairTaskList!queryPageList.action',
        fields: ['idx','planMonthIdx','equipmentIdx','repairClassName', 'gzSignMac','gzSignElc','beginTime','endTime','realBeginTime','realEndTime','isNeedAcceptance','acceptorId','acceptorName','acceptanceReviews','acceptanceTime','userId','userName','state','equipmentCode','equipmentName','specification','model','usePlace','electricCoefficient','mechanicalCoefficient', 'usePerson', 'wclCount','yclCount','repairTeamMac','repairTeamElc', 'createTime', 'orgName', 'imageCount'],
        listeners: {
        	beforeload: function() {
        		var searchParams = RepairTaskList.searchForm.getForm().getValues();
        		MyJson.deleteBlankProp(searchParams);
        		// 是否只显示处理中的检修作业工单
        		var tbar = RepairTaskList.grid.getTopToolbar();
        		var checkbox = tbar.findByType('checkbox')[0];
        		this.baseParams.isHanding = checkbox.checked;
        		this.baseParams.entityJson = Ext.encode(searchParams);
        	},
        	load: function() {
        		/*for (var i = 0; i < this.getCount(); i++) {
        			if (PLAN_STATUS_YXF == this.getAt(i).get('planStatus')) {
        				var row = RepairTaskList.grid.getView().getRow(i);
        				row.childNodes[0].style['background-color'] = '#00FF00';
        			}
        		}*/
        	}
        }
    }); 

	/**
	 * 表格
	 */
	RepairTaskList.grid = new Ext.grid.GridPanel({
		border: false,
		cm: cm,
		storeId: 'idx',
        store: store,
//        plugins: [expander],
        loadMask: new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." }),
        tbar: [{
        	text: '刷新', iconCls: 'refreshIcon', handler: function() {
        		self.location.reload();
        	}
        }, {
			text: '查看照片', iconCls: 'imageIcon', disabled: false, id: 'id_image_view_btn', handler: RepairTaskList.showImage
		}, {
        	text: '打印', iconCls: 'printerIcon', tooltip: '打印设备检修记录单！', handler: function() {
        		var sm = RepairTaskList.grid.getSelectionModel();
        		if (1 !== sm.getCount()) {
        			MyExt.Msg.alert('请选择<span style="color:red;">1</span>条检修作业工单！');
        			return;
        		}
        		// 显示报表打印页面
        		var cpt = "equipmentRepair/equipmentRepairRecord.cpt";
        	   	report.view(cpt, "设备检修记录单", {taskListIdx: sm.getSelections()[0].id});
        	}
        }, '-', com.yunda.Common.INFO('维修班组中的<span style="background:#0f0;border-radius:8px;padding:1px 4px;">绿色</span>标记表示该条工单是您所在班组的责任工单，请及时处理哦！'), '->', '-', {
        	boxLabel: '只显示处理中检修作业工单', xtype: 'checkbox', checked: true, listeners: {
        		check: function( me, checked ) {
        			RepairTaskList.grid.store.load();
        		}
        	}
        }, '-', '<span style="color:gray;">双击一条记录查看检修任务处理详情！</span>'],
        bbar: $yd.createPagingToolbar({pageSize:20, store: store}),
        listeners: {
        	rowdblclick: function( me, rowIndex, e ) {
        		var record = me.store.getAt(rowIndex);
        		// 设备检修任务下有检修范围活才显示详情窗口
        		if (record.get('wclCount') + record.get('yclCount')) {
        			RepairTaskList.showRepairScopeCase(record.id, rowIndex);
        		}
        	}
        }
    });
	
	new Ext.Viewport({
		layout: 'border',
		defaults: { layout: 'fit', border: false },
		items: [{
			region: 'north', height: 110, title: '查询&nbsp;<span style="color:gray;font-weight:normal;">输入完成后按回车键查询</span>',
			collapsible: true, frame: true,
			items: [RepairTaskList.searchForm]
		}, {
			region: 'center',
			items: [RepairTaskList.grid]
		}]
	});
	
});