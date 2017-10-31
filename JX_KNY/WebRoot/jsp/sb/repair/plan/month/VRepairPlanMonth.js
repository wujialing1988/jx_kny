Ext.onReady(function() {
	
	Ext.ns('VRepairPlanMonth');
	
	/** **************** 定义私有变量开始 **************** */
	var labelWidth = 100, fieldWidth = 140;
	var now = new Date();
	self.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });
	var timeLineStart = new Date(now.getFullYear(), now.getMonth(), 1);
	var timeLineEnd = new Date(now.getFullYear(), now.getMonth(), getMonthDays(now.getMonth()));
	
	// 时间轴范围变化事件延迟执行计时器
	var delayTask;
	/** **************** 定义私有变量结束 **************** */
	
	/** **************** 定义全局变量开始 **************** */
	VRepairPlanMonth.items = new vis.DataSet();
	VRepairPlanMonth.groups = new vis.DataSet();
	VRepairPlanMonth.timeline = null;
	VRepairPlanMonth.options = {
			
	    start: timeLineStart,
	    end: timeLineEnd,
			
		min: new Date(new Date().getFullYear() - 1, 0, 1),      // 时间轴范围最小值
		max: new Date(new Date().getFullYear() + 1, 11, 31),    // 时间轴范围最大值
		zoomMin: 10000 * 60 * 60,             			// 时间轴缩小的最小精度（五分钟）
		zoomMax: 1000 * 60 * 60 * 24 * 30 * 12,     			// 时间轴放大的最大精度（一年）
		
		editable: {
		    add: false,         	// 允许通过双击时间轴新增一个任务项
		    updateTime: true,  		// 允许水平拖动任务项进行更新
		    updateGroup: false, 	// 不允许将选中的任务项从一个分组拖动到另一个分组
		    remove: true        	// 允许通过点击任务项右上角的删除按钮从时间轴上移除任务项
		},

		// 删除工作项的事件监听
		onRemove : function(item, callback) {
			Ext.Msg.confirm('提示', '该操作将不能恢复，是否继续？', function(btn) {
				if ('yes' != btn) {
					return;
				}
				var ids = [item.id];
				// 执行数据库删除
				$yd.request({
                    scope: VRepairPlanMonth.store, url: ctx + '/repairPlanMonth!logicDelete.action', params: {ids: ids}
                }, function() {
    				VRepairPlanMonth.store.load();
                	alertSuccess('操作成功！')
                })
			});
		}
	};
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 工作项更新时的函数处理，用于存储信息到数据库
	VRepairPlanMonth.itemsUpdateEvent = function(event, properties) {
		var items = properties.data
		var array = [];
		for (var i = 0; i < items.length; i++) {
			// 声明一个"机车台位任务"对象JSON，用于封装被更新的信息
			var repairPlanMonth = {};
			repairPlanMonth.idx = items[i].id;
			repairPlanMonth.beginTime = items[i].start;
			repairPlanMonth.endTime = items[i].end;
			array.push(repairPlanMonth);
		}
		
		// 提交后台进行数据存储
		$yd.request({
			scope: VRepairPlanMonth.store,
			url : ctx + '/repairPlanMonth!update.action',
			jsonData : Ext.util.JSON.encode(array),

	    	success: function(response, options) {
	 	        if(self.loadMask)    self.loadMask.hide();
	 	        var result = Ext.util.JSON.decode(response.responseText);
	 	        this.reload();
	 	        if (true === result.success) {       //操作成功     
	 	        	alertSuccess("更新成功");
	 	        } else {                           	 //操作失败
	 	           alertFail(result.errMsg);
	 	        }
	 	    }
		});
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	VRepairPlanMonth.searchForm = new Ext.form.FormPanel({
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
							VRepairPlanMonth.store.load();
						}
					},
					select: function() {
						VRepairPlanMonth.store.load();
					}
				}
			}
		},
		items: [{
			items: [{
				fieldLabel:"单位", 
				xtype: "OmOrganizationCustom_comboTree",
				selectNodeModel:'exceptRoot',
				hiddenName: 'orgId',
				valueField: "orgseq",
				listeners: {
					select: function() {
						VRepairPlanMonth.store.load();
						// 重新加载查询条件控件数据源
						var cmps = VRepairPlanMonth.searchForm.findByType('singlefieldcombo');
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
							VRepairPlanMonth.store.load({
								callback: function() {
									// 根据查询条件选择的年度、月份重新定位视窗
									var startDate = new Date(year, month - 1, 1);
									var endDate = new Date(year, month - 1, getMonthDays(month));
									if (Ext.isEmpty(month)) {
										startDate = new Date(year, 0, 1);
										endDate = new Date(year, 11, getMonthDays(11));
									}
									if (VRepairPlanMonth.timeline) VRepairPlanMonth.timeline.setWindow(startDate, endDate);
								}
							});
							// 重新加载查询条件控件数据源
							var cmps = VRepairPlanMonth.searchForm.findByType('singlefieldcombo');
							for (var i = 0; i < cmps.length; i++) {
								cmps[i].reset();
								if (!Ext.isEmpty(month)) {
									cmps[i].whereList[1].sql = 'idx In (Select Distinct equipmentIdx From RepairPlanMonth Where recordStatus = 0 And planYear = '+ year +' And planMonth = '+ month +')'
								} else {
									cmps[i].whereList[1].sql = 'idx In (Select Distinct equipmentIdx From RepairPlanMonth Where recordStatus = 0 And planYear = '+ year +')'
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
							VRepairPlanMonth.store.load({
								callback: function() {
									// 根据查询条件选择的年度、月份重新定位视窗
									var startDate = new Date(year, month - 1, 1);
									var endDate = new Date(year, month - 1, getMonthDays(month));
									if (Ext.isEmpty(month)) {
										startDate = new Date(year, 0, 1);
										endDate = new Date(year, 11, getMonthDays(11));
									}
									if (VRepairPlanMonth.timeline) VRepairPlanMonth.timeline.setWindow(startDate, endDate);
								}
							});
							// 重新加载查询条件控件数据源
							var cmps = VRepairPlanMonth.searchForm.findByType('singlefieldcombo');
							for (var i = 0; i < cmps.length; i++) {
								cmps[i].reset();
								if (!Ext.isEmpty(month)) {
									cmps[i].whereList[1].sql = 'idx In (Select Distinct equipmentIdx From RepairPlanMonth Where recordStatus = 0 And planYear = '+ year +' And planMonth = '+ month +')'
								} else {
									cmps[i].whereList[1].sql = 'idx In (Select Distinct equipmentIdx From RepairPlanMonth Where recordStatus = 0 And planYear = '+ year +')'
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
				VRepairPlanMonth.store.load();
			}
		}, {
			text: '重置', iconCls: 'resetIcon', handler: function() {
				VRepairPlanMonth.searchForm.getForm().reset();
				VRepairPlanMonth.searchForm.findByType('OmOrganizationCustom_comboTree')[0].clearValue();
				// 重新加载查询条件控件数据源
				var cmps = VRepairPlanMonth.searchForm.findByType('singlefieldcombo');
				for (var i = 0; i < cmps.length; i++) {
					cmps[i].reset();
					cmps[i].whereList[0].propValue = teamOrgId;
					cmps[i].whereList[1].sql = 'idx In (Select Distinct equipmentIdx From RepairPlanMonth Where recordStatus = 0 And planYear = '+ new Date().getFullYear() +' And planMonth = '+ (new Date().getMonth() + 1) +')';
					cmps[i].reload();
				}
				VRepairPlanMonth.store.load();
			}
		}]
	});
	/** **************** 定义查询表单结束 **************** */
	
	/**
	 * 数据源加载完成后的事件监听
	 */
	VRepairPlanMonth.storeLoadFn = function(me) {
		var count = me.getCount();
		for (var i = 0; i < count; i++) {
			var record = me.getAt(i);
			var groupId = record.get('equipmentIdx');
			// 添加分组信息
			VRepairPlanMonth.groups.update({
						id : groupId,
//						content :  i + 1 + "、<a href='#' onclick='VRepairPlanMonth.showDetailFn(\""+ groupId + "\")'>" + record.get('taskName') + "</a>", 
//						content : record.get('equipmentName') + '<br/><span style="font-size:small;color:gray;">编号：' + record.get('equipmentCode') + '</span><br/><span style="font-size:small;color:gray;">类别：' + record.get('className') +"（"+ record.get('classCode') + "）</span>",
						content : record.get('equipmentName') + '<br/><span style="font-size:small;color:gray;">编号：' + record.get('equipmentCode') + '</span>',
						title : ["设备名称：" + record.get('equipmentName'), "设备编号：" + record.get('equipmentCode'), "设备类别：" + record.get('className') +"（"+ record.get('classCode') + "）"].join('\r\n')
					});

			var items = record.get('items');
			for (var j = 0; j < items.length; j++) {
				var item = items[j];
				var repairClass = item.repairClass;
				if (REPAIR_CLASS_SMALL == repairClass) repairClass = "小修";
				if (REPAIR_CLASS_MEDIUM == repairClass) repairClass = "中修";
				if (REPAIR_CLASS_SUBJECT == repairClass) repairClass = "项修";
				var title = [];
				title.push("设备名称：" + item.equipmentName);
				title.push("设备编号：" + item.equipmentCode);
				title.push("设备类别：" + record.get('className') +"（"+ record.get('classCode') + "）");
				title.push("计划年月：" + item.planYear +"年"+ item.planMonth + "月");
				title.push("计划开工日期：" + new Date(item.beginTime).format('Y-m-d H:i'));
				title.push("计划完工日期：" + new Date(item.endTime).format('Y-m-d H:i'));
				title.push("修程：" + repairClass);
				title.push("计划状态：" + (PLAN_STATUS_YXF == item.planStatus ? "已下发" : "未下发"));
				VRepairPlanMonth.items.update({
							id : item.idx,
							group : groupId,
							title : title.join('\r\n'),
							className : PLAN_STATUS_YXF == item.planStatus ? "yxf" : "",
							content : item.equipmentName + "（" + item.equipmentCode + "）" + repairClass,
							start : item.beginTime,
							end : item.endTime
						});
			}

		}
		// /////////
		if (!VRepairPlanMonth.timeline) {
			VRepairPlanMonth.timeline = new vis.Timeline(
				document.getElementById('visualization'), 
				VRepairPlanMonth.items, 
				VRepairPlanMonth.groups, 
				Ext.apply({}, VRepairPlanMonth.options, VisUtil.options)
			);

			// Modified by hetao on 2016-10-24 增加按照时间轴范围查询过滤月计划，防止依次加载过多数据致使页面响应时间较长
			// 时间轴时间范围改变的事件监听
			VRepairPlanMonth.timeline.on('rangechanged', function(options) {
				// Modified by hetao on 2016-09-30
				// 如果是向更小的时间范围查询、查看设备停机记录，则不重新加载时间轴数据源
				if (timeLineStart < options.start && timeLineEnd > options.end) {
					return;
				}
				timeLineStart = options.start;
				timeLineEnd = options.end;
				if (delayTask) {
					window.clearTimeout(delayTask);
				}
				delayTask = setTimeout(function() {
					VRepairPlanMonth.store.load();
				}, 1000);
			});
		}

		// 数据加载成功后，添加工作项的更新事件监听
		VRepairPlanMonth.items.on('update', VRepairPlanMonth.itemsUpdateEvent);
	}
	
	/**
	 * 数据源
	 */
	VRepairPlanMonth.store = new Ext.data.JsonStore({
        id: 'idx', 
        root: "root", 
        totalProperty: "totalProperty", 
        autoLoad: true,
        remoteSort: true,
        url: ctx + '/vRepairPlanMonth!queryPageList.action',
        fields: ['equipmentIdx', 'equipmentName', 'equipmentCode', 'classCode', 'className', 'repairCount', 'items'],
        listeners: {
        	beforeload: function() {
        		if (self.loadMask) self.loadMask.show();
        		// 因为数据实时更新应用的是DataSet的update方法，因为在数据加载时，要撤销工作项的update事件监听
				VRepairPlanMonth.items.off('update', VRepairPlanMonth.itemsUpdateEvent);
				VRepairPlanMonth.groups.clear();
				VRepairPlanMonth.items.clear();

				// 查询条件
        		var searchParams = VRepairPlanMonth.searchForm.getForm().getValues();
        		searchParams.startTime = timeLineStart;
        		searchParams.endTime = timeLineEnd;
        		
        		MyJson.deleteBlankProp(searchParams);
        		
        		// 计划状态快速过滤
        		var tbar = VRepairPlanMonth.panel.getTopToolbar();
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
			// 数据加载完成后的函数处理
			load : function( me, records, options ) {
        		if (self.loadMask) self.loadMask.hide();
				VRepairPlanMonth.storeLoadFn(me);
			}
        }
    }); 
	
	/**
	 * vis时间轴展示面板
	 */
	VRepairPlanMonth.panel = new Ext.Panel({
		autoScroll : true, border: false,
		
		html : ['<div id="visualization"></div>'].join(""),
		
		tbar: ['-', {
        	text: '生成全年计划', iconCls: 'addIcon', handler: function() {
        		var yearField = VRepairPlanMonth.searchForm.findByType('compositefield')[0].items.items[0];
        		var planYear = yearField.getValue();
        		Ext.Msg.confirm('提示', '是否确认生成<span style="font-weight:bold;color:red;">' + planYear + '</span>全年的设备检修	计划？', function(btn) {
        			if ('yes' === btn) {
        				// 提交后台进行数据存储
        				$yd.request({
        					url : ctx + '/repairPlanMonth!insertWholeMonthPlan.action',
        					params: {planYear: planYear},
        					scope : VRepairPlanMonth.store
        				},
        				// Ajax请求成功后的回调函数
        				function(result) {
        					VRepairPlanMonth.searchForm.findByType('compositefield')[0].items.items[1].setValue('');
        					this.load({
        						callback: function() {
        							alertSuccess('操作成功！');
        							// 根据查询条件选择的年度、月份重新定位VIS视窗
        							var startDate = new Date(planYear, 0, 1);
        							var endDate = new Date(planYear, 11, 31);
        							if (VRepairPlanMonth.timeline) VRepairPlanMonth.timeline.setWindow(startDate, endDate);
        						}
        					});
        				});
        			}
        		});
        	}
        }, {
        	text: '生成月计划', iconCls: 'addIcon', handler: function() {
        		CreatePlanMonth.win.show();
        	}
        }, {
        	text: '删除', iconCls: 'deleteIcon', handler: function() {
        		var ids = VRepairPlanMonth.timeline.getSelection();
				if (ids.length <= 0) {
					MyExt.Msg.alert('尚未选择任何记录，多选请配合使用[<span style="color:red; font-weight:bold;">Ctrl</span>]或者[<span style="color:red; font-weight:bold;">Shift</span>]键！');
					return;
				}
				Ext.Msg.confirm('提示', '该操作将不能恢复，是否继续？', function(btn) {
				if ('yes' != btn) {
					return;
				}
				// 执行数据库删除
				$yd.request({
                    url: ctx + '/repairPlanMonth!logicDelete.action', params: {ids: ids}
                }, function() {
    				VRepairPlanMonth.store.load();
                	alertSuccess('操作成功！')
                })
			});
        	}
        }, {
        	text: '打印', iconCls: 'printerIcon', tooltip: '打印月度设备检修计划表！', handler: function() {
        		// 显示报表打印页面
        		var cpt = "equipmentRepair/repairPlanMonth.cpt";
        	   	report.view(cpt, "月度设备检修计划表", 
        	   			{planYear: Ext.getCmp('plan_year_combo').getValue(), 
        	   		     planMonth: Ext.getCmp('plan_month_combo').getValue()});
        	}
        }/*, {
        	text: '一键删除', iconCls: 'deleteIcon', handler: function() {
        		$yd.request({
        			url: ctx + '/repairPlanMonth/deleteByOneKey.do',
        			scope: VRepairPlanMonth.store
        		}, function() {
        			this.reload({
        				callback: alertSuccess('操作成功！')
                	});
        		});
        	}
        }*/, {
        	text: '刷新', iconCls: 'refreshIcon', handler: function() {
        		self.location.reload();
        	}
        }, '->', '-','计划状态：', {
			xtype: 'checkbox', checked: true, boxLabel: '<span style="backgound-color:rgb(213, 221, 246);">未下发</span>&nbsp;&nbsp;', inputValue: PLAN_STATUS_WXF,
			listeners: {
				check: function(me) {
					VRepairPlanMonth.store.load();
				}
			}
		}, {
			xtype: 'checkbox', checked: false, boxLabel: '<span style="background-color:#00FF00;">已下发</span>&nbsp;&nbsp;', inputValue: PLAN_STATUS_YXF,
			listeners: {
				check: function(me) {
					VRepairPlanMonth.store.load();
				}
			}
		}],
        
		// 数据分页的特殊处理
		bbar: function() {
		 	cfg = {pageSize:10, store: VRepairPlanMonth.store};
		    //配置分页工具栏，表格默认每页显示记录数
		    var pageSize = cfg.pageSize || 50;  
		    //每页显示条数下拉选择框
		    var pageComboBox = new Ext.form.ComboBox({
		        name: 'pagesize',     triggerAction: 'all',  mode : 'local',   width: 75,
		        valueField: 'value',  displayField: 'text',  value: pageSize,  editable: false,
		        store: new Ext.data.ArrayStore({
		            fields: ['value', 'text'],
		            data: [[10, '10条/页'], [20, '20条/页'], [50, '50条/页'], [100, '100条/页']]
		        })
		    });
		    // 改变每页显示条数reload数据
		    pageComboBox.on("select", function(comboBox) {
		        pagingToolbar.pageSize = parseInt(comboBox.getValue());
		        pagingToolbar.store.reload({
		            params: {
		                start: 0,    limit: pagingToolbar.pageSize
		            }
		        });
		    });
		    //一个新实例化表格的分页工具栏
		    var pagingToolbar = new Ext.PagingToolbar({
		        pageSize: pageSize,   emptyMsg: "没有符合条件的记录",
		        displayInfo: true,    displayMsg: '显示 {0} 条到 {1} 条,共 {2} 条',    
		        items: ['-', '&nbsp;&nbsp;', pageComboBox, '-', 
		        "&nbsp;时间轴控制：", {
					text: '自适应', iconCls: 'expandIcon', handler: function() {
						VRepairPlanMonth.timeline.fit();
					}
				}, {
					text: '选择居中', iconCls: 'centerIcon', handler: function() {
						var selections = VRepairPlanMonth.timeline.getSelection();
						if (selections.length <= 0) {
							MyExt.Msg.alert('尚未选择任何记录！');
							return;
						}
						VRepairPlanMonth.timeline.focus(selections);
					}
				}, {
					text: '当前日期', iconCls: 'returnIcon', handler: function() {
						// 时间轴显示到当前日期
						VRepairPlanMonth.timeline.setWindow(getDayStartTime(), getDayEndTime());
					}
				}, '-', {
					xtype: 'label', text: '显示到：', 
					style: [
						'padding-left:18px;',
						'background-positionX:5px;',
						'background-repeat: no-repeat;',
						'background-image: url("' + ctx + '/frame/resources/vis/images/location.png");'
					].join('')
				} ,{
					xtype: 'combo', editable: false,
					width: 80,
					triggerAction: 'all',  mode : 'local',   
					valueField: 'value',  displayField: 'text', 
					store: new Ext.data.ArrayStore({
			            fields: ['value', 'text'],
			            data: [['week', '本周'], ['month', '本月'], ['quarter', '本季度'], ['year', '本年']]
			        }),
			        value: 'week',
			        listeners: {
					    // 改变每页显示条数reload数据
			        	select: function(comboBox) {
			        		if ('week' == comboBox.getValue()) {
								VRepairPlanMonth.timeline.setWindow(getWeekStartDate(), getWeekEndDate());
			        		}
			        		if ('month' == comboBox.getValue()) {
								VRepairPlanMonth.timeline.setWindow(getMonthStartDate(), getMonthEndDate());
			        		}
			        		if ('quarter' == comboBox.getValue()) {
								VRepairPlanMonth.timeline.setWindow(getQuarterStartDate(), getQuarterEndDate());
			        		}
			        		if ('year' == comboBox.getValue()) {
								VRepairPlanMonth.timeline.setWindow(getYearStartDate(), getYearEndDate());
			        		}
			        	}
			        }
				}],
		        listeners: {
		        	beforechange : function() {
		        		// 数据分页的特殊处理
					 VRepairPlanMonth.groups.clear();
					 VRepairPlanMonth.items.clear();
		        	}
		        }
		    });
		    pagingToolbar.pageComboBox = pageComboBox;
		    //分页工具栏绑定数据源
		    if(cfg.store != null) {
		        pagingToolbar.bind(cfg.store);
		        cfg.store.on('beforeload', function(store, options) {
		            store.baseParams.limit = pagingToolbar.pageSize;
		        });
		    }
		    return pagingToolbar;
		 }()
	});

	new Ext.Viewport({
		layout: 'border',
		defaults: { layout: 'fit', border: false },
		items: [{
			region: 'north', height: 110, title: '查询&nbsp;<span style="color:gray;font-weight:normal;">输入完成后按回车键查询</span>',
			collapsible: true, frame: true,
			items: [VRepairPlanMonth.searchForm]
		}, {
			region: 'center',
			items: [VRepairPlanMonth.panel]
		}]
	});
	
});