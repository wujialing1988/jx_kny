Ext.onReady(function() {
	
	Ext.ns('RepairPlanYear');
	
	/** **************** 定义私有变量开始 **************** */
	var sm = new Ext.grid.CheckboxSelectionModel(),
		cm, store;
	var labelWidth = 100, fieldWidth = 140;
	/** **************** 定义私有变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 打开设备检修范围维护页面
	 */
	RepairPlanYear.openRepairPeriodTab = function() {
		var id = '50106', 
			nodeUrl = '/jsp/sb/repair/period/RepairPeriod.jsp', 
			tabTitle = '维修周期', 
			allowClose = true;
		top.MainFrame.openNewTab(id, nodeUrl, tabTitle, allowClose);
	};
	
	/**
	 * 添加设备检修年计划
	 * @param planYear 计划年份
	 * @param equipmentIds 设备信息idx主键数组
	 */
	RepairPlanYear.saveFn = function(planYear, equipmentIds, callback) {
		if(self.loadMask)    self.loadMask.show();
		// 提交后台进行数据存储
		$yd.request({
			url : ctx + '/repairPlanYear!save.action',
			params : {planYear : planYear, equipmentIds : Ext.encode(equipmentIds)},
			scope : RepairPlanYear.grid
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
	RepairPlanYear.searchForm = new Ext.form.FormPanel({
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
					sql: 'idx In (Select equipmentIdx From RepairPlanYear Where recordStatus = 0 And planYear = '+ new Date().getFullYear() +')', compare: Condition.SQL
				}],
				width: fieldWidth, enableKeyEvents: true, 
				listeners: {
					keyup: function( me, e ) {
						if (e.ENTER == e.keyCode) {
							RepairPlanYear.grid.store.load();
						}
					},
					select: function() {
						RepairPlanYear.grid.store.load();
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
						RepairPlanYear.grid.store.load();
						// 重新加载查询条件控件数据源
						var cmps = RepairPlanYear.searchForm.findByType('singlefieldcombo');
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
				fieldLabel: '年度',
				hiddenName: 'planYear',
				xtype: 'combo',
		        store: function() {
		        	var data = [];
		        	var year = new Date().getFullYear();
		        	data.push([year + 1, year + 1 + '年']);			// 下一年
		        	for (var i = 0; i < 2; i++) {
		        		data.push([year - i, year - i + '年']);
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
						RepairPlanYear.grid.store.load();
						// 重新加载查询条件控件数据源
						var cmps = RepairPlanYear.searchForm.findByType('singlefieldcombo');
						for (var i = 0; i < cmps.length; i++) {
							cmps[i].reset();
							cmps[i].whereList[0].propValue = teamOrgId;
							cmps[i].whereList[1].sql = 'idx In (Select equipmentIdx From RepairPlanYear Where recordStatus = 0 And planYear = '+ this.getValue() +')';
							cmps[i].reload();
						}
					}
				}
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
				RepairPlanYear.grid.store.load();
			}
		}, {
			text: '重置', iconCls: 'resetIcon', handler: function() {
				RepairPlanYear.searchForm.getForm().reset();
				RepairPlanYear.searchForm.findByType('OmOrganizationCustom_comboTree')[0].clearValue();
				// 重新加载查询条件控件数据源
				var cmps = RepairPlanYear.searchForm.findByType('singlefieldcombo');
				for (var i = 0; i < cmps.length; i++) {
					cmps[i].reset();
					cmps[i].whereList[0].propValue = teamOrgId;
					cmps[i].whereList[1].sql = 'idx In (Select equipmentIdx From RepairPlanYear Where recordStatus = 0 And planYear = '+ new Date().getFullYear() +')';
					cmps[i].reload();
				}
				RepairPlanYear.grid.store.load();
			}
		}]
	});
	/** **************** 定义查询表单结束 **************** */
	
	/**
	 * 创建月修程下拉控件
	 */
	function createMonthEditor(cfg) {
		var editor = {
			xtype: 'combo',
	        store:new Ext.data.SimpleStore({
			    fields: ['k', 'v'],
				data: [[REPAIR_CLASS_NOSET, '无'], [REPAIR_CLASS_SMALL, '小'],[REPAIR_CLASS_MEDIUM, '中'],[REPAIR_CLASS_SUBJECT, '项']]
			}),
			valueField:'k', displayField:'v',
			value: REPAIR_CLASS_NOSET,
			editable: false,
			triggerAction:'all',
			mode:'local'
		}
		Ext.apply(editor, cfg);
		return editor;
	}
	
	/**
	 * 月修程渲染
	 */
	function monthRenderer(value, meta, record) {
		if (-1 == value.toString().indexOf('0')) {
			meta.css = "editCell";
		} else {
			meta.css = "unEditCell";
		}
		var v = value.toString().substr(0, 1);
		if (REPAIR_CLASS_SMALL == v) return '小';
		if (REPAIR_CLASS_MEDIUM == v) return '中';
		if (REPAIR_CLASS_SUBJECT == v) return '项';
	}
	
	/**
	 * 表格列模型
	 */
	var cm = new Ext.grid.ColumnModel({
//		defaults: { sortable: true },
		columns: [new Ext.grid.RowNumberer(), sm, {
			dataIndex : 'idx', header : 'idx主键', hidden: true
		}, {
			dataIndex : 'equipmentIdx', header : '设备主键', hidden: true
		}, {
			dataIndex : 'orgName', header : '单位名称', width: 120
		}, {
			header:'设备编码', dataIndex:'equipmentCode'
		}, {
			header:'设备名称', dataIndex:'equipmentName', width: 240
		}, {
			dataIndex : 'planYear', header : '年度', width: 55, align: 'center'
		}, {
			dataIndex : 'month1', header : '1月', renderer: monthRenderer, editor: createMonthEditor(), width: 38, align: 'center'
		}, {
			dataIndex : 'month2', header : '2月', renderer: monthRenderer, editor: createMonthEditor(), width: 38, align: 'center'
		}, {
			dataIndex : 'month3', header : '3月', renderer: monthRenderer, editor: createMonthEditor(), width: 38, align: 'center'
		}, {
			dataIndex : 'month4', header : '4月', renderer: monthRenderer, editor: createMonthEditor(), width: 38, align: 'center'
		}, {
			dataIndex : 'month5', header : '5月', renderer: monthRenderer, editor: createMonthEditor(), width: 38, align: 'center'
		}, {
			dataIndex : 'month6', header : '6月', renderer: monthRenderer, editor: createMonthEditor(), width: 38, align: 'center'
		}, {
			dataIndex : 'month7', header : '7月', renderer: monthRenderer, editor: createMonthEditor(), width: 38, align: 'center'
		}, {
			dataIndex : 'month8', header : '8月', renderer: monthRenderer, editor: createMonthEditor(), width: 38, align: 'center'
		}, {
			dataIndex : 'month9', header : '9月', renderer: monthRenderer, editor: createMonthEditor(), width: 38, align: 'center'
		}, {
			dataIndex : 'month10', header : '10月', renderer: monthRenderer, editor: createMonthEditor(), width: 38, align: 'center'
		}, {
			dataIndex : 'month11', header : '11月', renderer: monthRenderer, editor: createMonthEditor(), width: 38, align: 'center'
		}, {
			dataIndex : 'month12', header : '12月', renderer: monthRenderer, editor: createMonthEditor(), width: 38, align: 'center'
		}, {
			dataIndex : 'classCode', header : '类别编码', hidden: true
		}, {
			dataIndex : 'className', header : '设备类别', hidden: true, width: 180
		}, {
			dataIndex : 'model', header : '型号'
		}, {
			dataIndex : 'specification', header : '规格'
		}, {
			dataIndex : 'mechanicalCoefficient', header : '机械系数', width: 62, align: 'center'
		}, {
			dataIndex : 'electricCoefficient', header : '电气系数', width: 62, align: 'center'
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
        url: ctx + '/repairPlanYear!queryPageList.action',
        fields: ['idx', 'equipmentIdx', 'planYear', 'month1', 'month2', 'month3', 'month4', 'month5', 'month6', 'month7', 'month8', 'month9', 'month10', 'month11', 'month12', 'classCode', 'className', 'model', 'specification', 'usePlace', 'mechanicalCoefficient', 'electricCoefficient', 'mechanicalRepairTeam', 'electricRepairTeam', 'equipmentName', 'equipmentCode', 'orgName'],
        listeners: {
        	beforeload: function() {
        		var searchParams = RepairPlanYear.searchForm.getForm().getValues();
        		MyJson.deleteBlankProp(searchParams);
        		this.baseParams.entityJson = Ext.encode(searchParams);
        	}
        }
    }); 

	/**
	 * 表格
	 */
	RepairPlanYear.grid = new Ext.grid.EditorGridPanel({
		border: false,
		entity: 'com.yunda.sb.repair.plan.entity.RepairPlanYear',
		deleteURL: ctx + '/repairPlanYear!logicDelete.action',
		sm: sm,
		cm: cm,
		stripeRows: true,
        store: store,
        loadMask: new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." }),
        clicksToEdit: 1,
        tbar: ['-', {
        	text: '添加设备年计划', iconCls: 'addIcon', handler: function() {
        		var yearField = RepairPlanYear.searchForm.find('hiddenName', 'planYear')[0];
        		var yearValue = yearField.getValue();
        		if (Ext.isEmpty(yearValue)) {
        			MyExt.Msg.alert('请先选择计划年份！');
        			yearField.focus();
        			return;
        		}
        		// 显示待修设备选择窗口
        		EquipmentPrimaryInfoSelect.planYear = yearValue;
        		EquipmentPrimaryInfoSelect.win.show();
        	}
        }, {
        	// Modified by hetao on 2017-02-21 增加设备检修年度计划报表
        	text: '打印', iconCls: 'printerIcon', handler: function() {
        		var sp = {};
        		sp.planYear = RepairPlanYear.searchForm.find('hiddenName', 'planYear')[0].getValue();
        		sp.orgName = purviewOrgName;
	        	report.view("/repair/repairPlanYear.cpt", document.title, sp)
        	}
        }, {
        	text: '删除', iconCls: 'deleteIcon', handler: function() {
        		var me = RepairPlanYear.grid;
        		if(!$yd.isSelectedRecord(me)) return;
        		$yd.confirmAndDelete({
                    scope: me, url: me.deleteURL, params: {ids: $yd.getSelectedIdx(me, me.storeId)}
                }, function() {
                	this.store.reload({
        				callback: alertSuccess('操作成功！')
                	});
                })
        	}
        }, {
        	text: '刷新', iconCls: 'refreshIcon', handler: function() {
        		self.location.reload();
        	}
        }, '-', com.yunda.Common.INFO('若要实现设备检修计划根据历史计划自动生成，请先维护设备<a href="#" onclick="RepairPlanYear.openRepairPeriodTab()" title="点击跳转到设备维修周期页面！">维修周期</a>！'), '->', {
			xtype : 'label',
			html : '此色块表示已生成检修月计划',
			style : 'background-color:#00FF00;'
		}],
        bbar: $yd.createPagingToolbar({pageSize:20, store: store}),
        listeners: {
        	beforeedit: function(e) {
        		if (-1 != e.value.toString().indexOf(0)) e.cancel = true;
        	},
        	afteredit: function(e) {
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
	
	new Ext.Viewport({
		layout: 'border',
		defaults: { layout: 'fit', border: false },
		items: [{
			region: 'north', height: 110, title: '查询&nbsp;<span style="color:gray;font-weight:normal;">输入完成后按回车键查询</span>',
			collapsible: true, frame: true,
			items: [RepairPlanYear.searchForm]
		}, {
			region: 'center',
			items: [RepairPlanYear.grid]
		}]
	});
	
});