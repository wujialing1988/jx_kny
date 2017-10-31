/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('VTrainStationPlan');
	
	/** **************** 定义全局变量开始 **************** */
	VTrainStationPlan.labelWidth = 100;
	VTrainStationPlan.fieldWidth = 260;
	VTrainStationPlan.stations = new vis.DataSet();		// 机车检修作业流程计划数据集对象
	VTrainStationPlan.nodes = new vis.DataSet();		// 机车检修作业流程节点数据集对象
	VTrainStationPlan.timeline = null;					// 时间轴timeline对象
	
	VTrainStationPlan.status = PLAN_STATUS_NEW + "," + PLAN_STATUS_HANDLING/* + "," + SPLAN_TATUS_HANDLED*/;
	
	VTrainStationPlan.options = {
		editable: false,					// 设置timeline不可编辑
		stack: true,						// 相邻项目时间有重叠时，以堆栈形式显示
  		align: 'left',						// 设置时间轴上条目的文本内容以“居右”方式显示
  		
  		// 时间轴可显示的最小、最大时间
		min: new Date(nowYear - 1, nowMonth, nowDay),   // 时间轴范围最小值(以当前时间为截止的过去一年)
		max: getYearEndDate(),        					// 时间轴范围最大值(本年末)
  		
		// 时间轴初始化时显示的起、止时间
  		start: getDay(-1),				// 昨天
  		end: getDay(1)					// 明天
	};
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 测试函数
	VTrainStationPlan.showDetailFn = function(idx) {
		MyExt.Msg.alert(idx);
	}
	
	// 清空数据集
	VTrainStationPlan.clearFn = function() {
		VTrainStationPlan.stations.clear();
		VTrainStationPlan.nodes.clear();
	}
	
	// 创建分页工具栏
	VTrainStationPlan.createPagingToolbarFn = function(){
	 	cfg = {pageSize:10, store: VTrainStationPlan.store};
	    //配置分页工具栏，表格默认每页显示记录数
	    var pageSize = cfg.pageSize || 10;  
	    //每页显示条数下拉选择框
	    var pageComboBox = new Ext.form.ComboBox({
	        name: 'pagesize',     triggerAction: 'all',  mode : 'local',   width: 75,
	        valueField: 'value',  displayField: 'text',  value: pageSize,  editable: false,
	        store: new Ext.data.ArrayStore({
	            fields: ['value', 'text'],
	            data: [[10, '10条/页'], [20, '20条/页'], [50, '50条/页'], [100, '100条/页']]
	        }),
	        listeners: {
			    // 改变每页显示条数reload数据
	        	select: function(comboBox) {
			    	// 清空数据集
		    		VTrainStationPlan.clearFn();
			        pagingToolbar.pageSize = parseInt(comboBox.getValue());
			        pagingToolbar.store.reload({
			            params: {
			                start: 0,    limit: pagingToolbar.pageSize
			            }
			        });
	        	}
	        }
	    });
	    //一个新实例化表格的分页工具栏
	    var pagingToolbar = new Ext.PagingToolbar({
	        pageSize: pageSize,   emptyMsg: "没有符合条件的记录",
	        displayInfo: true,    displayMsg: '显示 {0} 条到 {1} 条,共 {2} 条',    
	        items: ['-', '&nbsp;&nbsp;', pageComboBox, '-', "&nbsp;时间轴控制：", {
				text: '自适应', iconCls: 'expandIcon', handler: function() {
					VTrainStationPlan.timeline.fit();
				}
			}, {
				text: '选择居中', iconCls: 'centerIcon', handler: function() {
					var selections = VTrainStationPlan.timeline.getSelection();
					if (selections.length <= 0) {
						MyExt.Msg.alert('尚未选择任何记录！');
						return;
					}
					VTrainStationPlan.timeline.focus(selections);
				}
			}, {
				text: '当前日期', iconCls: 'returnIcon', handler: function() {
					// 时间轴显示到当前日期
					VTrainStationPlan.timeline.setWindow(getDayStartTime(), getDayEndTime());
				}
			}, '-', {
				xtype: 'label', text: '显示到：', 
				style: [
					'padding-left:18px;',
					'background-positionX:5px;',
					'background-repeat: no-repeat;',
					'background-image: url("images/location.png");'
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
							VTrainStationPlan.timeline.setWindow(getWeekStartDate(), getWeekEndDate());
		        		}
		        		if ('month' == comboBox.getValue()) {
							VTrainStationPlan.timeline.setWindow(getMonthStartDate(), getMonthEndDate());
		        		}
		        		if ('quarter' == comboBox.getValue()) {
							VTrainStationPlan.timeline.setWindow(getQuarterStartDate(), getQuarterEndDate());
		        		}
		        		if ('year' == comboBox.getValue()) {
							VTrainStationPlan.timeline.setWindow(getYearStartDate(), getYearEndDate());
		        		}
		        	}
		        }
			}],
	        listeners: {
	        	beforechange : function() {
	        		// 清空数据集
					VTrainStationPlan.clearFn();
	        	}
	        }
	    });
	    pagingToolbar.pageComboBox = pageComboBox;
	    //分页工具栏绑定数据源
	    if(cfg.store != null) {
	        pagingToolbar.bind(cfg.store);
	        cfg.store.on('beforeload', function(store, options){
	            store.baseParams.limit = pagingToolbar.pageSize;
	        });
	    }
	    return pagingToolbar;
	 };
	
	// 格式化分组信息content的显示内容
	VTrainStationPlan.formatStationContentFn = function(station) {
		var content = station.get('workStationName') 			// 工位名称
//		content = "<a href='#' style='font-weight: bold;'>" + content + "</a>";
		content = "<span style='font-weight: bold;'>" + content + "</span>";
		content += "<span style='font-size: small;'>";
		content += "<br/>" + station.get('repaireLineName');
		content += "</span>";
		return content;
	}
	
	// 格式化机车检修计划基本信息
	VTrainStationPlan.getPlanInfoFn = function(station) {
		var info = [];
		info.push("机车信息：");
		info.push(station.get('trainTypeShortName'));			// 车型简称
		info.push(station.get('trainNo'));						// 车号
		info.push(station.get('repairClassName') );				// 修程
		info.push(station.get('repairtimeName')); 				// 修次
		return info.join(" ");
	}
	
	// 格式化分组信息title的显示内容
	VTrainStationPlan.formatStatioinTitleFn = function(station) {
		var title = [];
		title.push(station.get('workStationName'));				// 工位名称
		title.push(station.get('repaireLineName'));				// 流水线名称
		return title.join('\r\n');
	}
	
	// 格式化机车检修作业计划-流程节点在timeline上的title信息
	VTrainStationPlan.formatNodeTitleFn = function(station, node) {
		var planBeginTime = new Date(node.planBeginTime);		// 计划开始时间
		var planEndTime = new Date(node.planEndTime);			// 计划结束时间
		var title = [];
		title.push(node.trainTypeShortName + node.trainNo);
		title.push('开始时间：' + planBeginTime.format('m-d H:i'));
		title.push('结束时间：' + planEndTime.format('m-d H:i'));
		return title.join('\r\n');
	}
	
	// 格式化机车检修作业计划-流程节点在timeline上的显示信息
	VTrainStationPlan.formatNodeContentFn = function(node) {
		var content = [];
		
		var planBeginTime = new Date(node.beginTime);		// 计划开始时间
		var planEndTime = new Date(node.endTime);			// 计划结束时间
		var args = [];
		args.push(node.workPlanIDX);
		args.push(node.trainTypeShortName + "|" + node.trainNo);
		args.push(node.repairClassName + "|" + node.repairtimeName);
		args.push(planBeginTime.format('Y-m-d H:i'));
		args.push(planEndTime.format('Y-m-d H:i'));
		
		content.push("<a href='#' style='font-size:20;' onclick='TrainWorkPlanWin.showWin(\""+ args.join(",") + "\")'>");
//		content.push("<span class='boldFont' style='font-size:24;'>" + node.trainTypeShortName + " " + node.trainNo + "</span>");
		content.push(node.trainTypeShortName + " " + node.trainNo);
		content.push('</a>');
		return content.join("");
	}
	
	// 复选框多选查询函数处理
	VTrainStationPlan.checkQueryFn = function() {
		// 清空数据集
		VTrainStationPlan.clearFn();
		
		VTrainStationPlan.status = "-1";
		if(Ext.getCmp("status_dx").checked){
			VTrainStationPlan.status += "," + PLAN_STATUS_NEW;
		} 
		if(Ext.getCmp("status_zx").checked){
			VTrainStationPlan.status += "," + PLAN_STATUS_HANDLING;
		} 
//		if(Ext.getCmp("status_xj").checked){
//			VTrainStationPlan.status += "," + SPLAN_TATUS_HANDLED;
//		} 
		// 加载数据
		VTrainStationPlan.store.load();
	}
	
	// 数据加载完成后的函数处理
	VTrainStationPlan.storeLoadFn = function() {
		var count = this.getCount();
		for (var i = 0; i < count; i++) {
			var station = this.getAt(i);
			var groupId = station.get('idx')
			// 添加分组信息
			VTrainStationPlan.stations.update({
				id : groupId,
				title :  VTrainStationPlan.formatStatioinTitleFn(station),
				content : i + 1 + "、" + VTrainStationPlan.formatStationContentFn(station)
			});

			var items = station.get('jobProcessNodes');
			for (var j = 0; j < items.length; j++) {
				var node = items[j];
				// 根据任务完成状态设置不同的显示样式
				var className = "";
				if (node.status == NODE_STATUS_WKG) {
					className = 'wkg';
				} else if (node.status == NODE_STATUS_YKG) {
					className = 'ykg';
				} else {
					className = 'ywg';
				}
				if (!Ext.isEmpty(node.delayTime) && node.status != NODE_STATUS_YWG) {
					className = 'yyq';
				}
				
				VTrainStationPlan.nodes.update({
					id : node.idx,
					title : VTrainStationPlan.formatNodeTitleFn(station, node),
					className : className,
					group : groupId,
					content : VTrainStationPlan.formatNodeContentFn(node),
					start : node.planBeginTime,
//					start : new Date(node.planBeginTime).getTime() + 10000,
					end : node.planEndTime
				});
			}

		}
		// 声明一个timeline对象实例
		if (!VTrainStationPlan.timeline) {
			VTrainStationPlan.timeline = new vis.Timeline(
				document.getElementById('visualization'), 
				VTrainStationPlan.nodes, 
				VTrainStationPlan.stations, 
				Ext.apply({}, VTrainStationPlan.options, VisUtil.options)
			);
		}

	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义数据容器开始 **************** */
	VTrainStationPlan.store = new Ext.data.JsonStore({
		id : 'idx',
		root : "root",
		totalProperty : "totalProperty",
		autoLoad : false,
		remoteSort : false,
		url : ctx + '/vWorkStation!pageList.action',
		fields : [
			"idx", 
			"workStationCode", 
			"workStationName",
			"repaireLineIDX",
			"repaireLineName",
			"jobProcessNodes",
			"seqNo"
		],
//		sortInfo : {
//			field : 'seqNo',
//			direction : 'ASC'
//		},
		listeners : {
			// 数据加载完成后的函数处理
			load : VTrainStationPlan.storeLoadFn,
			
			beforeload: function(store, options) {
	       	 	if(!VTrainStationPlan.loadMask)   
	       	 		VTrainStationPlan.loadMask = new Ext.LoadMask(Ext.getBody(), {
	       	 			msg: '正在加载，请稍候...',
	       	 			store: VTrainStationPlan.store
	       	 		});
       	 		VTrainStationPlan.loadMask.show();
				var searchParams = {};
				// 根据检修计划状态过滤
				searchParams.workPlanStatus = VTrainStationPlan.status;
				searchParams.wsGroupIDX = Ext.getCmp('WSGroup_Combo').getValue();
				searchParams.trainWorkPlanIDX = Ext.getCmp('TrainNo_Combo').getValue();
				if (searchParams.trainWorkPlanIDX == '请选择...') {
					delete searchParams.trainWorkPlanIDX;
				}
				store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
			}
		}
	});
	/** **************** 定义数据容器结束 **************** */
	
	//页面适应布局
	VTrainStationPlan.viewport = new Ext.Viewport({
		layout: 'border',
		items: [{
			title: '查询',
			frame:true,
			region: 'north', height: 80, layout: 'fit', border: false,
			collapsible: true, collapseMode: 'mini',
			items: [{
				border: false, baseCls: 'x-plain',
				xtype: 'form', padding: 10, frame: false, layout: 'column',
				labelWidth: VTrainStationPlan.labelWidth,
				defaults: {
					layout: 'form',
					border: false, baseCls: 'x-plain',
					columnWidth: .33,
					defaultType: 'textfield'
				},
				items: [{
					items: [{
						fieldLabel: '车号', 
						width: VTrainStationPlan.fieldWidth,
						id:"TrainNo_Combo", xtype: 'Base_combo', 
						entity:"com.yunda.jx.jxgc.workplanmanage.entity.TrainWorkPlan",
						displayField:"trainNo", valueField:"idx", 
						hasEmpty: true,
						queryHql:[
							"Select new TrainWorkPlan(idx, processName || ' ' || trainTypeShortName || ' ' || trainNo As trainNo) From TrainWorkPlan Where recordStatus = 0 And workPlanStatus In('",
							PLAN_STATUS_NEW,
							"','",
							PLAN_STATUS_HANDLING,
							"') Order By trainTypeShortName, trainNo"
						].join(""),
						fields:["idx", "trainNo"],
						listeners: {
							select: function(combo, record, index) {
								// 清空数据集
								VTrainStationPlan.clearFn();
								// 加载数据
								VTrainStationPlan.store.load();
							}
						}
					}]
				}, {
					items: [{
						xtype: 'compositefield',combineErrors: false,
						fieldLabel: '工位组',
						items: [{
							id: 'WSGroup_Combo',
							xtype: 'Base_combo', width: VTrainStationPlan.fieldWidth,
							entity:'com.yunda.jx.jxgc.workplanmanage.entity.WSGroup',
							hiddenName: 'idx',
							displayField:'name',valueField:'idx',
							fields: ['idx', 'name'],
							listeners: {
								// 选择工位组后自动过滤数据集
								select: function(combo, record, index) {
									// 清空数据集
									VTrainStationPlan.clearFn();
									// 加载数据
									VTrainStationPlan.store.load();
								},
								// Modified by hetao at 2015-09-16 17:30 页面加载时，默认选择一个已维护的工位组
								render: function() {
									this.store.load({
										callback: function() {
											if (this.getCount() > 0) {
												var record = this.getAt(0).data;
												// ☞ 设置【工位组】字段回显值☞
												Ext.getCmp('WSGroup_Combo').setDisplayValue(record.idx, record.name);
											}
											// 加载页面数据源
											VTrainStationPlan.store.load();
										}
									});
								}
								
							}
						}, {
							xtype: 'button', text: '设置工位组', id: 'btn_config_group', handler: function() {
								// 显示工位设置窗口
					    		WSGroup.win.show();
							}
						}]
					}]
				}, {/*
					items: [{
						xtype: 'button', text: '查看所有台位', width: 100, iconCls: 'resetIcon', handler: function() {
//							Ext.getCmp('TrainNo_Combo').clearValue();
							Ext.getCmp('WSGroup_Combo').clearValue();
							// 加载页面数据源
							VTrainStationPlan.store.load();
						}
					}]
				*/}]
			}]
		},{
			region: 'center',
			border: false, autoScroll : true,
			tbar: [{
		    	xtype:"checkbox", id:"status_dx", boxLabel:"待修&nbsp;&nbsp;&nbsp;&nbsp;", checked:true , handler: VTrainStationPlan.checkQueryFn
		    }, {
		    	xtype:"checkbox", id:"status_zx", boxLabel:"在修", checked:true , handler: VTrainStationPlan.checkQueryFn
//			}, {
//		    	xtype:"checkbox", id:"status_xj", boxLabel:"修竣", checked:true , handler: VTrainStationPlan.checkQueryFn
			}, '-', {
				text : '刷新', iconCls : 'refreshIcon', handler : function() {
					self.location.reload();
				}
			}, {
				text: '在新窗口中打开', iconCls: 'previewIcon', handler: function() {
					window.open("VTrainStationPlan.jsp");
				}
			}, '->', '图例：', '-', {
				xtype : 'label',
				text : '未开工',
				style: 'font-weight:bold;',
				cls : 'status_example status_example_wkg'
			}, '-', {
				xtype : 'label',
				text : '已开工',
				style: 'font-weight:bold;',
				cls : 'status_example status_example_ykg'
			}, '-', {
				xtype : 'label',
				text : '已延期',
				style: 'font-weight:bold;',
				cls : 'status_example status_example_yyq'
			}, '-', {
				xtype : 'label',
				text : '已完工',
				style: 'font-weight:bold;',
				cls : 'status_example status_example_ywg'
			}],
			
			html : ['<div id="visualization"></div>'].join(""),
				
			bbar: VTrainStationPlan.createPagingToolbarFn()
		},{ 		
	     	 region: 'south', height: 165, collapsible: false, collapseMode: 'mini',collapsed: true, 
	         items:[VTrainAccessAccount.panel]
         }]
	});
	
	/** **************** 更新时间轴的定时器定义开始 **************** */
	// 页面初始化完成后延迟3秒启动定时器
	var timerTask = new Ext.util.DelayedTask(function() {
		Ext.TaskMgr.start({
			run : function() {
				// 加载数据
				VTrainStationPlan.store.reload();
				// 加载在修机车面板数据
				VTrainAccessAccount.showStatisticsInfoFn();
			},
			interval : 1000 * 60		// 每60秒更新一次
		});
	});
	// 延迟30秒
	timerTask.delay(1000 * 30);
	/** **************** 更新时间轴的定时器定义结束 **************** */
	
});