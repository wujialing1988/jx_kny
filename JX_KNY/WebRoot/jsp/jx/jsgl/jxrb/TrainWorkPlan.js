/**
 * 生产调度—机车检修作业计划 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
 
var now = new Date();
now.setMonth(now.getMonth() - 1);
var lastMonth = now.format('Y-m-d');

Ext.onReady(function(){
 	Ext.ns('TrainWorkPlan');
 	
 	TrainWorkPlan.searchForm = new Ext.form.FormPanel({
 		layout: 'column', region: 'north', height: 50, baseCls: 'x-plain',
 		padding: 10,
 		defaults: {
 			baseCls: 'x-plain', layout: 'form'
 		},
 		items: [{
 			columnWidth: .6, 
 			items: [{
				xtype: 'compositefield', fieldLabel: '作业计划日期', combineErrors: false, anchor:'100%',
				items: [{
					xtype:'my97date', id: 'startDate', format:'Y-m-d', value:lastMonth, width: 100,
					// 日期校验器
					vtype:'dateRange', allowBlank: false,
					dateRange:{startDate: 'startDate', endDate: 'endDate'}
				}, {
					xtype: 'label',
					text: '至',
					style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}, {							
					xtype:'my97date', id: 'endDate', format:'Y-m-d', width: 100,
					// 日期校验器
					vtype:'dateRange', allowBlank: false,
					dateRange:{startDate: 'startDate', endDate: 'endDate'}
				}]
			
 			}]
 		}, {
 			columnWidth: .4, 
 			items: [{
 				xtype: 'button', text: '查询', iconCls: 'searchIcon', handler: function() {
 					var form = TrainWorkPlan.searchForm.getForm();
 					if (!form.isValid()) {
 						return;
 					}
 					TrainWorkPlan.grid.store.load();
 				}, width: 80
 			}]
 		}]
 	});
 	
 	TrainWorkPlan.grid = new Ext.yunda.Grid({
 		storeAutoLoad: false, region: 'center',
 		loadURL: ctx + '/trainWorkPlan!pageQuery.action',
 		viewConfig: null,
 		tbar: ['-', {
 			xtype: 'textfield', emptyText: '根据车型车号检索作业计划', listeners: {
 				keyup: function(me, e) {
 					if (e.ENTER === e.keyCode) {
 						TrainWorkPlan.grid.store.load();
 					}
 				}
 			}, enableKeyEvents: true, width: 160
 		},'<span style="color:gray;">输入完成后按回车键查询</span>', '->', '计划状态：', {
 			xtype: 'checkbox', boxLabel: '处理中&nbsp;&nbsp;', inputValue: ONGOING, listeners: {
 				check: function() {
 					TrainWorkPlan.grid.store.load();
 				}
 			}, checked: true
 		}, {
 			xtype: 'checkbox', boxLabel: '已处理', inputValue: COMPLETE, listeners: {
 				check: function() {
 					TrainWorkPlan.grid.store.load();
 				}
 			}
 		}],
 		fields: [{
 			dataIndex: 'idx', header: 'idx主键', hidden: true
 		}, {
 			dataIndex: 'trainTypeShortName', header: '车型', renderer: function(v, metaData, record) {
 				return v + '&nbsp;' + record.get('trainNo');
 			}, width: 100
 		}, {
 			dataIndex: 'trainNo', header: '车号', hidden: true
 		}, {
 			dataIndex: 'repairClassName', header: '修程', renderer: function(v, metaData, record) {
 				if (!Ext.isEmpty(record.get('repairtimeName'))) {
	 				return v + '&nbsp;' + record.get('repairtimeName');
 				}
 				return v;
 			}, width: 100
 		}, {
 			dataIndex: 'repairtimeName', header: '修次', hidden: true
 		}, {
 			dataIndex: 'processName', header: '作业流程名称', width: 180
 		}, {
 			dataIndex: 'planBeginTime', header: '计划开始时间', xtype: "datecolumn", format: 'Y-m-d'
 		}, {
 			dataIndex: 'planEndTime', header: '计划结束时间', xtype: "datecolumn", format: 'Y-m-d'
 		}, {
 			dataIndex: 'dNAME', header: '配属段'
 		}, {
 			dataIndex: 'delegateDName', header: '维修段'
 		}, {
 			dataIndex: 'workPlanStatus', header: '计划状态', renderer: function(v) {
 				if (INITIALIZE == v) return '初始化';
 				if (ONGOING == v) return '处理中';
 				if (COMPLETE == v) return '已处理';
 				if (TERMINATED == v) return '终止';
 				return '错误！未知状态';
 			}
 		}],
 		toEditFn: function(grid, rowIndex, e) {
 			Ext.Msg.confirm('提示', '是否确认生成机车检修日报？', function(btn){
 				if ('yes' == btn) {
					var record = this.store.getAt(rowIndex);
 					 // 根据机车检修作业计划生成机车检修日报
	 				DailyReport.saveFn([record.id], function(){
	 					TrainWorkPlan.grid.store.reload();
	 				});
 				}
 			}, this);
 		}
 	});
 	TrainWorkPlan.grid.store.on('beforeload', function(){
 		var whereList = [];
 		// 查询“处理中”“已处理”的机车检修作业计划
 		var tbar = TrainWorkPlan.grid.getTopToolbar();
 		var checkFields = tbar.findByType('checkbox');
 		var propValues = [-1];
 		for (var i = 0; i < checkFields.length; i++) {
 			if (checkFields[i].checked) {
 				propValues.push(checkFields[i].getRawValue());
 			}
 		}
 		whereList.push({compare: Condition.IN, propName: 'workPlanStatus', propValues: propValues});
 		
 		// 过滤已经生成了机车检修日报的机车检修作业计划
 		var sql = "IDX NOT IN (SELECT WORK_PLAN_ID FROM JSGL_JXRB WHERE RECORD_STATUS = 0 AND WORK_PLAN_ID IS NOT NULL)";
 		whereList.push({compare: Condition.SQL, sql: sql});
 		
 		// 根据车型车号过滤数据源
 		var trainTypeNo = tbar.findByType('textfield')[0].getValue();
 		if (!Ext.isEmpty(trainTypeNo)) trainTypeNo = trainTypeNo.toUpperCase();
 		sql = "(TRAIN_TYPE_SHORTNAME LIKE '%"+ trainTypeNo + "%' OR TRAIN_NO LIKE '%"+ trainTypeNo + "%')";
 		whereList.push({compare: Condition.SQL, sql: sql});
 		
 		// 根据机车检修作业计划时间进行过滤
 		var form = TrainWorkPlan.searchForm.getForm();
 		var startDate = form.getValues().startDate + " 00:00:00";
 		var endDate = form.getValues().endDate + " 23:59:59";
 		sql = "(PLAN_BEGIN_TIME >= TO_DATE('"+ startDate +"', 'yyyy-MM-dd HH24:MI:SS') AND PLAN_BEGIN_TIME <= TO_DATE('"+ endDate +"', 'yyyy-MM-dd HH24:MI:SS'))";
 		whereList.push({compare: Condition.SQL, sql: sql});
 		
 		this.baseParams.whereListJson = Ext.encode(whereList);
 	});
 	
 	TrainWorkPlan.win = new Ext.Window({
 		title: '生成机车检修日报',
 		modal: true,
 		closeAction: 'hide',
 		height: 500, width: 1000,
 		layout: 'border',
 		items: [TrainWorkPlan.searchForm, TrainWorkPlan.grid],
 		buttonAlign: 'center',
 		buttons: [{
 			text: '生成日报', iconCls: 'saveIcon', handler: function() {
 				var sm = TrainWorkPlan.grid.getSelectionModel();
 				var selections = sm.getSelections();
 				if (selections.length <= 0) {
 					MyExt.Msg.alert('尚未选择任何记录！');
 					return;
 				}
 				var workPlanIDs = [];
 				for (var i = 0; i < selections.length; i++) {
 					workPlanIDs.push(selections[i].id);
 				}
 				// 根据机车检修作业计划生成机车检修日报
 				DailyReport.saveFn(workPlanIDs, function(){
 					TrainWorkPlan.grid.store.reload();
 				});
 			}
 		}, {
 			text: '关闭', iconCls: 'closeIcon', handler: function() {
 				this.findParentByType('window').hide();
 			}
 		}],
 		listeners: {
 			show: function() {
 				TrainWorkPlan.grid.store.load();
 			}
 		}
 	});
 });