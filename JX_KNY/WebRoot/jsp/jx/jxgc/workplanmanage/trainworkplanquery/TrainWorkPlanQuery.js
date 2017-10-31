/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('TrainWorkPlanQuery');
	
	/** **************** 定义全局变量开始 **************** */
	TrainWorkPlanQuery.plans = new vis.DataSet();		// 机车检修作业流程计划数据集对象
	TrainWorkPlanQuery.nodes = new vis.DataSet();		// 机车检修作业流程节点数据集对象
	TrainWorkPlanQuery.timeline = null;					// 时间轴timeline对象
	TrainWorkPlanQuery.planSuffixChar = '_';			// 计划对比数据主键字符后缀
	
	TrainWorkPlanQuery.status = PLAN_STATUS_NEW + "," + PLAN_STATUS_HANDLING;
	
	TrainWorkPlanQuery.options = {
		editable: false,					// 设置timeline不可编辑
		stack: true,						// 相邻项目时间有重叠时，以堆栈形式显示
  		align: 'left',						// 设置时间轴上条目的文本内容以“居中”方式显示
		zoomMin: 1000 * 60 * 50 * 12,             					// 时间轴缩小的最小精度（五分钟）
   		orientation: 'bottom',
  		
  		// 时间轴可显示的最小、最大时间
		min: new Date(nowYear - 1, nowMonth, nowDay),   // 时间轴范围最小值(以当前时间为截止的过去一年)
		max: getYearEndDate(),        					// 时间轴范围最大值(本年末)
  		
		// 时间轴初始化时显示的起、止时间
  		start: new Date(),				// 昨天	
  		end: getDay(1)					// 明天
	};
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	//停时图
	TrainWorkPlanQuery.showStopAnasys = function(idx, startTime, lastTime,trainTypeAndNo){
		var reportUrl = "/jxgc/TrainsTopAnasys.cpt?ctx=" + ctx.substring(1);
		var dataUrl = reportUrl + "&workPlanIdx=" + idx + "&startTime=" + startTime +"&lastTime=" + lastTime + "&trainTypeAndNo=" + trainTypeAndNo;
        window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI("停时图"));
	}
	
	
	// 清空数据集
	TrainWorkPlanQuery.clearFn = function() {
		TrainWorkPlanQuery.plans.clear();
		TrainWorkPlanQuery.nodes.clear();
	}
	
	//打开检修明细窗口
	TrainWorkPlanQuery.trainWin = function(agrs) {	
        TrainWorkPlanCommHis.showWin(agrs);
	}
	
	// 创建分页工具栏
	TrainWorkPlanQuery.createPagingToolbarFn = function(){
	 	cfg = {pageSize:10, store: TrainWorkPlanQuery.store};
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
		    		TrainWorkPlanQuery.clearFn();
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
					TrainWorkPlanQuery.timeline.fit();
				}
			}, {
				text: '选择居中', iconCls: 'centerIcon', handler: function() {
					var selections = TrainWorkPlanQuery.timeline.getSelection();
					if (selections.length <= 0) {
						MyExt.Msg.alert('尚未选择任何记录！');
						return;
					}
					TrainWorkPlanQuery.timeline.focus(selections);
				}
			}, {
				text: '当前日期', iconCls: 'returnIcon', handler: function() {
					// 时间轴显示到当前日期
					TrainWorkPlanQuery.timeline.setWindow(getDayStartTime(), getDayEndTime());
				}
			}, '-', {
				xtype: 'label', text: '显示到：', 
				style: [
					'padding-left:18px;',
					'background-positionX:5px;',
					'background-repeat: no-repeat;',
					'background-image: url("location.png");'
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
							TrainWorkPlanQuery.timeline.setWindow(getWeekStartDate(), getWeekEndDate());
		        		}
		        		if ('month' == comboBox.getValue()) {
							TrainWorkPlanQuery.timeline.setWindow(getMonthStartDate(), getMonthEndDate());
		        		}
		        		if ('quarter' == comboBox.getValue()) {
							TrainWorkPlanQuery.timeline.setWindow(getQuarterStartDate(), getQuarterEndDate());
		        		}
		        		if ('year' == comboBox.getValue()) {
							TrainWorkPlanQuery.timeline.setWindow(getYearStartDate(), getYearEndDate());
		        		}
		        	}
		        }
			}],
	        listeners: {
	        	beforechange : function() {
	        		// 清空数据集
					TrainWorkPlanQuery.clearFn();
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
	TrainWorkPlanQuery.formatPlanContentFn = function(plan, isPlan) {
		var content = plan.get('trainTypeShortName') 			// 车型简称
						+ plan.get('trainNo') 					// 车号
			content += " " + plan.get('repairClassName') 			// 修程
//		if (!Ext.isEmpty(plan.get('repairtimeName'))) {
//			content += plan.get('repairtimeName'); 				// 修次
//		}
		
		// 计划
		var planBeginTime = new Date(plan.get('planBeginTime'));
		var planEndTime = new Date(plan.get('planEndTime'));
		// 实际
		var beginTime, endTime;
		if (plan.get('beginTime')) beginTime = new Date(plan.get('beginTime'));
		if (plan.get('endTime')) endTime = new Date(plan.get('endTime')); 
		else endTime = new Date();
		
		// 与chengr接口对接的传入参数
		var args = [];
		args.push(plan.get('idx'));
		args.push(plan.get('trainTypeShortName') + "|" + plan.get('trainNo'));
		var xcxc = plan.get('repairClassName') ;
		if (!Ext.isEmpty(plan.get('repairtimeName')))
			xcxc+= "|" + plan.get('repairtimeName');
		args.push(xcxc);
		args.push(planBeginTime.format('Y-m-d H:i'));
		args.push(planEndTime.format('Y-m-d H:i'));
		content = "<a href='#' class='train_pic_bg' onclick='TrainWorkPlanWinQuery.showWin(\""+ args.join(",") + "\")'>" + content + "</a>";
		content += "<span class='smallFont'>";
		// 计划时间显示
	 	if (beginTime && endTime) {
			content += "<br/>" + beginTime.format('m-d H:i');
			content += "~" + endTime.format('m-d H:i');	
		}
		content += "<br/>"
		content += "</span>";
//		content += "<a href='#'  onclick='TrainWorkPlanQuery.showPartsZzjhWin(\""+ args.join(',') + "\")'>" + " 大部件周转计划 " + "</a>";
//		content += "<a href='#'  onclick='TrainWorkPlanQuery.showWin1(\""+ plan.get('idx') + "\")'>" + " 流程图 " + "</a>";
		var trainTypeAndNo =plan.get('trainTypeShortName')+"|"+ plan.get('trainNo');
		var startTime = planBeginTime; 
		var lastTime = planEndTime;
		if(plan.get('minRealTime')){
			var minRealTime = new Date(plan.get('minRealTime'));
			startTime = planBeginTime <= minRealTime? planBeginTime:minRealTime;
		}
		if(plan.get('maxRealTime')){
			var maxRealTime = new Date(plan.get('maxRealTime'));
			lastTime = planEndTime<= maxRealTime? maxRealTime:planEndTime;
		}
		content += "<a href='#'  onclick='TrainWorkPlanQuery.showStopAnasys(\""+ plan.get('idx') + "\",\""+ startTime.format('Y-m-d') +									 	// [1]
				"\",\""+ lastTime.format('Y-m-d') +  			// 车型简称
						"\",\""+ trainTypeAndNo + "\")'>" + " 停时图 " + "</a>";	
		args.push(plan.get('dNAME'));
		args.push(plan.get('delegateDName'));
		if(plan.get('beginTime')){
			args.push(new Date(plan.get('beginTime')).format('Y-m-d H:i'));
		}else {args.push("");}
		if(plan.get('endTime')){
			args.push(new Date(plan.get('endTime')).format('Y-m-d H:i'));
		}else {args.push("");}
		args.push(plan.get('workPlanStatus'));
		content += "<a href='#' onclick='TrainWorkPlanQuery.trainWin(\""+  args.join(",") + "\")'>" + " 检修明细 " + "</a>";
		return content;
	}
	
	// 格式化机车检修计划基本信息
	TrainWorkPlanQuery.getPlanInfoFn = function(plan) {
		var info = [];
		info.push("机车信息：");
		info.push(plan.get('trainTypeShortName'));			// 车型简称
		info.push(plan.get('trainNo'));						// 车号
		info.push(plan.get('repairClassName') );			// 修程
		info.push(plan.get('repairtimeName')); 				// 修次
		return info.join(" ");
	}
	
	// 格式化分组信息title的显示内容
	TrainWorkPlanQuery.formatPlanTitleFn = function(plan) {
		var planBeginTime = new Date(plan.get('planBeginTime'));		// 计划开始时间
		var planEndTime = new Date(plan.get('planEndTime'));			// 计划结束时间
		var title = [];
		title.push(TrainWorkPlanQuery.getPlanInfoFn(plan));
		title.push("耗时：" + formatTime((planEndTime - planBeginTime) / 60 / 1000 , 'm'));
		title.push("开始时间：" + planBeginTime.format('Y-m-d H:i'));
		title.push("结束时间：" + planEndTime.format('Y-m-d H:i'));
		return title.join('\r\n');
	}
	
	// 格式化机车检修作业计划-流程节点在timeline上的title信息
	TrainWorkPlanQuery.formatNodeTitleFn = function(plan, node) {
		var title = [];
		// 机车信息
		title.push(TrainWorkPlanQuery.getPlanInfoFn(plan));
		// 名称
		title.push("名称：" + node.nodeName);
		// 工期
		title.push("工期：" + formatTime(node.ratedWorkMinutes, 'm'));
		// 完成百分比
		title.push("完成百分比：" + node.completePercent);
		// 计划
		title.push("计划：" + new Date(node.planBeginTime).format('m-d H:i') + " ~ " + new Date(node.planEndTime).format('m-d H:i'));
		// 实际
		if (!Ext.isEmpty(node.realBeginTime)) {
			var realTime = "实际：" + new Date(node.realBeginTime).format('m-d H:i') + " ~ ";
			if (!Ext.isEmpty(node.realEndTime)) {
				realTime += new Date(node.realEndTime).format('m-d H:i');
			}
			title.push(realTime);
		}
		// 延期
		if (!Ext.isEmpty(node.delayTime)) {
			title.push("延期：" + formatTime(node.delayTime, 'm'));
		}
		// 状态
		if (node.status == NODE_STATUS_WKG) {
			title.push("状态：未开工");
		} else if (node.status == NODE_STATUS_YKG) {
			title.push("状态：已开工");
		} else if (node.status == NODE_STATUS_YWG){
			title.push("状态：已完工");
		}
		// 延期原因
		if (!Ext.isEmpty(node.delayReason)) {
			title.push("延期原因：" + node.delayReason);
		}
		return title.join('\r\n');
	}
	
	/**
	 * 格式化机车检修作业计划-流程节点在timeline上的显示信息
	 * @param node 机车检修作业流程节点数据实体
	 * @param isPlan 是否显示为计划对比样式
	 * @param plan 机车检修作业计划数据实体
	 * @param className 根据显示样式来确定任务完成状态
	 */
	TrainWorkPlanQuery.formatNodeContentFn = function(node, isPlan, plan, className) {
		// 1 根据“计划开始时间”和“计划结束时间”计算单个作业流程节点的作业时长（分钟）
		var planBeginDate = new Date(node.planBeginTime);			// 计划开始时间的Date对象
		var planEndDate = new Date(node.planEndTime);				// 计划结束时间的Date对象
		
		var planBeginTime = planBeginDate.getTime();
		var planEndTime = planEndDate.getTime();
		/**
		 * // TODO 完善编辑机车检修作业节点
		 * 编辑机车检修作业节点
		 */
		//方法参数
		var idxValue = plan.get('idx');
		
		// 与chengr接口对接的传入参数
		var args = [];
		args.push(plan.get('idx'));
		args.push(plan.get('trainTypeShortName') + "|" + plan.get('trainNo'));
		var xcxc = plan.get('repairClassName') ;
		if (!Ext.isEmpty(plan.get('repairtimeName')))
			xcxc+= "|" + plan.get('repairtimeName');
		args.push(xcxc);
		
		// 计划
		var planBeginTime1 = new Date(plan.get('planBeginTime'));
		var planEndTime1 = new Date(plan.get('planEndTime'));
		
		args.push(planBeginTime1.format('Y-m-d H:i'));
		args.push(planEndTime1.format('Y-m-d H:i'));			
		var workPlanStatus = plan.get('workPlanStatus');
		
		var content = "";
		if (Ext.isEmpty(node.nodeName)) {
			content += "子节点信息";
		} else {
			content += node.nodeName;
		}
		if(!Ext.isEmpty(node.ratedWorkMinutes)&& 0 != node.ratedWorkMinutes){
			content += "<span class='smallFont'>[" + formatTime(node.ratedWorkMinutes, 'm') + "]</span>"
		}	

			content += "<br/><span class='smallFont'>" + planBeginDate.format('m-d H') + "~" + planEndDate.format('m-d H') + "</span>";
			if (!Ext.isEmpty(node.realBeginTime)) {
				content += "&nbsp&nbsp<span class='smallFont'>" + new Date(node.realBeginTime).format('m-d H');
				
			if (!Ext.isEmpty(node.realEndTime)) {
				content += "~"+ "<span class='smallFont'>" + new Date(node.realEndTime).format('m-d H');
			}
			
		}
		// 如果任务状态为“已延期”，则要提供填写任务延期原因的链接
		if (node.status !== NODE_STATUS_WKG) {
			content += "<br><a>" 
			if (Ext.isEmpty(node.delayReason)) {
				content += "";
			} else {
				content += node.delayReason;
			}
			"</a>";
		}
		return '<div style="height:68px;">' + content + '</div>';
	}
	
	// 复选框多选查询函数处理
	TrainWorkPlanQuery.checkQueryFn = function() {
		// 清空数据集
		TrainWorkPlanQuery.clearFn();
		
		TrainWorkPlanQuery.status = "-1";
		if(Ext.getCmp("status_dx").checked){
			TrainWorkPlanQuery.status += "," + PLAN_STATUS_NEW;
		} 
		if(Ext.getCmp("status_zx").checked){
			TrainWorkPlanQuery.status += "," + PLAN_STATUS_HANDLING;
		} 
		if(Ext.getCmp("status_xj").checked){
			TrainWorkPlanQuery.status += "," + SPLAN_TATUS_HANDLED;
		} 
		TrainWorkPlanQuery.store.load();
	}
	
	// 如果作业流程节点未设置计划开始时间或者计划结束时间的提示信息
	TrainWorkPlanQuery.errorInfo = function(plan, node) {
		var trainInfo = [];
		trainInfo.push(plan.get('trainTypeShortName'));			// 车型简称
		trainInfo.push(plan.get('trainNo'));					// 车号
		trainInfo.push(plan.get('repairClassName') );			// 修程
		trainInfo.push(plan.get('repairtimeName')); 			// 修次
		return trainInfo.join('') + " - " + node.nodeName;
	}
	
	// 数据加载完成后的函数处理
	TrainWorkPlanQuery.storeLoadFn = function(store, records, options ) {
		var count = this.getCount();
		for (var i = 0; i < count; i++) {
			var plan = this.getAt(i);
			var groupId = plan.get('idx')
			// 添加分组信息【计划】
			TrainWorkPlanQuery.plans.update({
				id : groupId + TrainWorkPlanQuery.planSuffixChar,
				title :  TrainWorkPlanQuery.formatPlanTitleFn(plan),
				content :  i + 1 + "、" + TrainWorkPlanQuery.formatPlanContentFn(plan, true)
			});

			var items = plan.get('jobProcessNodes');
			for (var j = 0; j < items.length; j++) {
				var node = items[j];
				if (Ext.isEmpty(node.planBeginTime)) {
					MyExt.Msg.alert("检修作业计划<span style='font-weight:bold;color:red;'>" + TrainWorkPlanQuery.errorInfo(plan, node) + "</span>开始时间设置有误，导致该作业节点未能正常显示！");
					continue;
				} else if (Ext.isEmpty(node.planEndTime)) {
					MyExt.Msg.alert("检修作业计划<span style='font-weight:bold;color:red;'>" + TrainWorkPlanQuery.errorInfo(plan, node) + "</span>结束时间设置有误，导致该作业节点未能正常显示！");
					continue;
				}
				
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
				
				// 添加检修作业流程节点信息【计划】
				TrainWorkPlanQuery.nodes.update({
					id : node.idx + TrainWorkPlanQuery.planSuffixChar,
					title : TrainWorkPlanQuery.formatNodeTitleFn(plan, node),
					className : className,
					group : groupId + TrainWorkPlanQuery.planSuffixChar,
					content : TrainWorkPlanQuery.formatNodeContentFn(node, true, plan),
					// 计划开始时间
					start : new Date((new Date()).valueOf() + 1000 * 60 * 60 * 5 *j),
					// 计划结束时间
					end : new Date((new Date()).valueOf() + 1000 * 60 * 60* 5 * j + 1000 * 60 * 5 *55)
				});
			}

		}
		// 声明一个timeline对象实例
		if (!TrainWorkPlanQuery.timeline) {
			TrainWorkPlanQuery.timeline = new vis.Timeline(
				document.getElementById('visualization'), 
				TrainWorkPlanQuery.nodes, 
				TrainWorkPlanQuery.plans, 
				Ext.apply({}, TrainWorkPlanQuery.options, VisUtil.options)
			);
		}

	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义数据容器开始 **************** */
	TrainWorkPlanQuery.store = new Ext.data.JsonStore({
		id : 'idx',
		root : "root",
		totalProperty : "totalProperty",
		autoLoad : true,
		remoteSort : false,
		url : ctx + '/vTrainWorkPlan!queryWorkPlanList.action',
		fields : [
			"idx", 
			"trainTypeIDX", "trainTypeShortName", "trainNo",
			"repairClassIDX", "repairClassName", 
			"repairtimeIDX", "repairtimeName", 
			"planBeginTime", "planEndTime", 
			"beginTime", "endTime", "dID","dNAME","delegateDID","delegateDName",
			"jobProcessNodes","workPlanStatus",'inTime','minRealTime','maxRealTime'
		],
		sortInfo : {
			field : 'trainTypeShortName',
			direction : 'ASC'
		},
		listeners : {
			// 数据加载完成后的函数处理
			load : TrainWorkPlanQuery.storeLoadFn,
			
			// 数据加载异常处理
			exception: function() {
				var response = arguments[4];
	        	var result = Ext.util.JSON.decode(response.responseText);
				if (!Ext.isEmpty(result.errMsg)) {
					Ext.Msg.alert("数据错误", result.errMsg);
				}
			},
			
			beforeload: function(store, options) {
	       	 	if(!TrainWorkPlanQuery.loadMask)   
	       	 		TrainWorkPlanQuery.loadMask = new Ext.LoadMask(Ext.getBody(), {
	       	 			msg: '正在加载，请稍候...',
	       	 			store: TrainWorkPlanQuery.store
	       	 		});
       	 		TrainWorkPlanQuery.loadMask.show();
				var searchParams = {};
				// 根据检修计划状态过滤
				searchParams.workPlanStatus = TrainWorkPlanQuery.status;
				searchParams.trainNo = Ext.getCmp('train_type_no').getValue();
				searchParams = MyJson.deleteBlankProp(searchParams);
				store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
			}
		}
	});
	/** **************** 定义数据容器结束 **************** */
	
	//页面适应布局
	TrainWorkPlanQuery.viewport = new Ext.Viewport({
		layout: 'border',
		items: [{
			region: 'center',
			border: false, autoScroll : true,
			tbar: [{		
		    	xtype:"checkbox", id:"status_dx", boxLabel:"待修&nbsp;&nbsp;&nbsp;&nbsp;", checked:true , handler: TrainWorkPlanQuery.checkQueryFn
		    }, {
		    	xtype:"checkbox", id:"status_zx", boxLabel:"在修&nbsp;&nbsp;&nbsp;&nbsp;", checked:true , handler: TrainWorkPlanQuery.checkQueryFn
	    	}, {
		    	xtype:"checkbox", id:"status_xj", boxLabel:"修竣", checked:false , handler: TrainWorkPlanQuery.checkQueryFn
			}, '-',  {
				text : '刷新',
				iconCls : 'refreshIcon',
				handler : function() {
					self.location.reload();
				}	
			}, '->', {
				xtype:'textfield', id:'train_type_no', enableKeyEvents:true, emptyText:'输入车型车号快速检索', listeners: {
		    		keyup: function(filed, e) {
						// 如果敲下Enter（回车键），则触发添加按钮的函数处理
						if (e.getKey() == e.ENTER){
							// 清空数据集
							TrainWorkPlanQuery.clearFn();
							TrainWorkPlanQuery.store.load();
						}
		    		}
	    		}
			}, {
				text:'查询', iconCls:'searchIcon', handler: function(){
					// 清空数据集
					TrainWorkPlanQuery.clearFn();
					TrainWorkPlanQuery.store.load();
				}
			}, {
				text:'重置', iconCls:'resetIcon', handler: function(){
					Ext.getCmp('train_type_no').reset();
					// 清空数据集
					TrainWorkPlanQuery.clearFn();
					TrainWorkPlanQuery.store.load();
				}
			}, '-', '图例：', '-', {
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
			
			html : [
				'<div id="visualization">',
				'</div>'
			].join(""),
				
			bbar: TrainWorkPlanQuery.createPagingToolbarFn()
		}, VTrainAccessAccountQuery.panel]
	});
	
	/** **************** 更新时间轴的定时器定义开始 **************** */
	// 页面初始化完成后延迟3秒启动定时器
	var timerTask = new Ext.util.DelayedTask(function() {
		Ext.TaskMgr.start({
			run : function() {
				// 加载数据
				TrainWorkPlanQuery.store.reload();
				// 加载在修机车面板数据
				VTrainAccessAccountQuery.showStatisticsInfoFn();
			},
			interval : 1000 * 60 * 2		// 每60秒更新一次
		});
	});
	// 延迟30秒
	timerTask.delay(1000 * 30);
	/** **************** 更新时间轴的定时器定义结束 **************** */

});