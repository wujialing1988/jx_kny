Ext.onReady(function() {
	Ext.ns('ZbglRdpQuery');
	
	/** **************** 定义全局变量开始 **************** */
	ZbglRdpQuery.plans = new vis.DataSet();		
	ZbglRdpQuery.nodes = new vis.DataSet();		
	ZbglRdpQuery.timeline = null;							
	
//	ZbglRdpQuery.status = PLAN_STATUS_HANDLING + "," + PLAN_STATUS_HANDLED;
	ZbglRdpQuery.status = PLAN_STATUS_HANDLING;//默认第一次查询整备中的数据
	
	ZbglRdpQuery.options = {
		editable: false,					// 设置timeline不可编辑
		stack: true,						// 相邻项目时间有重叠时，以堆栈形式显示
  		align: 'center',						// 设置时间轴上条目的文本内容以“居中”方式显示
  		
  		// 时间轴可显示的最小、最大时间
		min: new Date(nowYear - 3, nowMonth, nowDay),   // 时间轴范围最小值(以当前时间为截止的过去一年)
		max: getYearEndDate(),        					// 时间轴范围最大值(本年末)
  		
		// 时间轴初始化时显示的起、止时间
  		start: getDay(-0.1),				// 昨天	
  		end: getDay(1)					// 明天
	};
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 清空数据集
	ZbglRdpQuery.clearFn = function() {
		ZbglRdpQuery.plans.clear();
		ZbglRdpQuery.nodes.clear();
	};
	
	// 如果作业流程节点未设置开始时间或者结束时间的提示信息
	ZbglRdpQuery.errorInfo = function(plan, node) {
		var trainInfo = [];
		trainInfo.push(plan.get('trainTypeShortName'));			// 车型简称
		trainInfo.push(plan.get('trainNo'));					// 车号
		trainInfo.push(plan.get('dID') );					// 整备站场名称
		trainInfo.push(plan.get('dName')); 			            // 配属段名称
		return trainInfo.join('') + " - " + node.nodeName;
	};
	
	// 格式化分组信息content的显示内容
	ZbglRdpQuery.formatPlanContentFn = function(plan) {
		var content = plan.get('trainTypeShortName') 			// 车型简称
						+ plan.get('trainNo');					// 车号
		// 整备时间
		var rdpStartTime, rdpEndTime;
		if (plan.get('rdpStartTime')) rdpStartTime = new Date(plan.get('rdpStartTime'));
		if (plan.get('rdpEndTime')) rdpEndTime = new Date(plan.get('rdpEndTime')); 
		content = "<a href='#' class='train_pic_bg'>" + content + "</a>";
		content += "<span style='font-size: small;'> <br/>";
		if (rdpStartTime){
			content += rdpStartTime.format('m-d H:i');
		}
		content += "&nbsp;&#8594&nbsp;";
	    if(rdpEndTime){
	    	content += rdpEndTime.format('m-d H:i');
	    }	
		content += "</span>";
	return content;
	}
	
	// 格式化机车基本信息
	ZbglRdpQuery.getPlanInfoFn = function(plan) {
		var info = [];	
		info.push("机车信息：" + plan.get('trainTypeShortName') + " " + plan.get('trainNo') );
		if (!Ext.isEmpty(plan.get('repairClass'))) {            // 检修类型
			if(plan.get('repairClass')== '10')
				info.push("检修类型：碎修");
			else if(plan.get('repairClass')== '20')
				info.push("检修类型： 临修");
		}
		info.push("配属段信息：" + plan.get('dID') + " " + plan.get('dName'));				// 配属段编号、名称
		info.push("整备站场信息："+ plan.get('siteID'));	    // 整备站场ID、名称
		return info.join("\r\n");
	}
	
	// 格式化作业工单详情信息显示（时间轴上的分组信息的标题显示内容）
	ZbglRdpQuery.formatPlanTitleFn = function(plan){
		var rdpStartTime = new Date(plan.get('rdpStartTime'));	// 整备开始时间
		var rdpEndTime = new Date(plan.get('rdpEndTime'));		// 整备结束时间
		var title = [];
		title.push(ZbglRdpQuery.getPlanInfoFn(plan));
		if(!Ext.isEmpty(plan.get('rdpStartTime'))){
			title.push("整备开始时间：" + rdpStartTime.format('Y-m-d H:i'));
		}
		if(!Ext.isEmpty(plan.get('rdpEndTime'))){
			title.push("整备结束时间：" + rdpEndTime.format('Y-m-d H:i'));
		}
//		title.push("整备后的去向：" + plan.get('toGo'));
//		title.push("交验交车人：" + plan.get('fromPersonName'));
//		title.push("交验接车人：" + plan.get('toPersonName'));
//		title.push("出段车次：" + plan.get('outOrder'));
//		title.push("交验情况描述：" + plan.get('remarks'));
		return title.join('\r\n');
	};
	
	//格式化作业工单结点信息显示（时间轴上的结点内容显示）
	ZbglRdpQuery.formatNodeContentFn = function(node){
		var content = node.nodeName;
		// 状态
		if (node.status != NODE_STATUS_UNSTART) {
			content += "<br/><span class='smallFont'>处理时间：";
			if(null != node.realBeginTime &&　null == node.realEndTime) {
				content += new Date(node.realBeginTime).format('m-d H:i');					
			}
			else if (null == node.realBeginTime && null != node.realEndTime){
				content +=  new Date(node.realEndTime).format('m-d H:i');
			}
			else{
				content += new Date(node.realBeginTime).format('m-d H:i')+ "~" + new Date(node.realEndTime).format('m-d H:i');
			}
			content += "</span>";
		}
	
		return content;
	};
	
	//格式化作业工单结点详情信息（时间轴上的详情内容显示）
	ZbglRdpQuery.formatNodeTitleFn = function(plan, node){
		var title = [];
		// 机车信息
		title.push(ZbglRdpQuery.getPlanInfoFn(plan));
		// 结点序号
		title.push("结点序号：" + node.seqNo);
		// 名称
		title.push("名称：" + node.nodeName);
        //处理时间
		if (node.status != NODE_STATUS_UNSTART) {
			var realTime = "处理时间：";
			if(null != node.realBeginTime &&　null == node.realEndTime) {
				 realTime += new Date(node.realBeginTime).format('m-d H:i');					
			}
			else if (null == node.realBeginTime && null != node.realEndTime){
				realTime += new Date(node.realEndTime).format('m-d H:i');
			}
			else {
				realTime += new Date(node.realBeginTime).format('m-d H:i')+ "~" + new Date(node.realEndTime).format('m-d H:i');
			}
			title.push(realTime);	
		}		
		
		// 状态
		if (node.status == NODE_STATUS_UNSTART) {
			title.push("状态：未处理");
		} else if (node.status == NODE_STATUS_GOING) {	
			title.push("状态：处理中");
		} else if (node.status == NODE_STATUS_COMPLETE){
			var realTime = "处理时间："
			title.push("状态：已处理");
		}
		return title.join('\r\n');
	};
	
    // 创建分页工具栏
	ZbglRdpQuery.createPagingToolbarFn = function(){
	 	cfg = {pageSize:10, store: ZbglRdpQuery.store};
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
		    		ZbglRdpQuery.clearFn();
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
					ZbglRdpQuery.timeline.fit();
				}
			}, {
				text: '选择居中', iconCls: 'centerIcon', handler: function() {
					var selections = ZbglRdpQuery.timeline.getSelection();
					if (selections.length <= 0) {
						MyExt.Msg.alert('尚未选择任何记录！');
						return;
					}
					ZbglRdpQuery.timeline.focus(selections);
				}
			}, {
				text: '当前日期', iconCls: 'returnIcon', handler: function() {
					// 时间轴显示到当前日期
					ZbglRdpQuery.timeline.setWindow(getDayStartTime(), getDayEndTime());
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
							ZbglRdpQuery.timeline.setWindow(getWeekStartDate(), getWeekEndDate());
		        		}
		        		if ('month' == comboBox.getValue()) {
							ZbglRdpQuery.timeline.setWindow(getMonthStartDate(), getMonthEndDate());
		        		}
		        		if ('quarter' == comboBox.getValue()) {
							ZbglRdpQuery.timeline.setWindow(getQuarterStartDate(), getQuarterEndDate());
		        		}
		        		if ('year' == comboBox.getValue()) {
							ZbglRdpQuery.timeline.setWindow(getYearStartDate(), getYearEndDate());
		        		}
		        	}
		        }
			}],
	        listeners: {
	        	beforechange : function() {
	        		// 清空数据集
					ZbglRdpQuery.clearFn();
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
	 
	// 数据加载完成后的函数处理
	ZbglRdpQuery.storeLoadFn = function(store, records, options ) {
		var count = this.getCount();
		for (var i = 0; i < count; i++) {
			var plan = this.getAt(i);
			var groupId = plan.get('idx');
			// 添加分组信息【整备单】
			ZbglRdpQuery.plans.update({
				id : groupId,
				title :ZbglRdpQuery.formatPlanTitleFn(plan),
				content :  i + 1 + "、" + ZbglRdpQuery.formatPlanContentFn(plan)
			});
			var noteItems = plan.get('zbglRdpNodes');
			if(null !=noteItems){
			for (var j = 0; j < noteItems.length; j++) {
				var node = noteItems[j];
				// 根据任务完成状态设置不同的显示样式
				var className = "";
				if (node.status == NODE_STATUS_UNSTART) {
					className = 'wkg';
				} else if (node.status == NODE_STATUS_GOING) {
					className = 'ykg';
				} else {
					className = 'ywg';
				}
				// 添加时间轴显示项（整备作业流程节点信息）
				ZbglRdpQuery.nodes.update({
					id : node.idx,
					title : ZbglRdpQuery.formatNodeTitleFn(plan, node),
					className : className,
					group : groupId ,
					content : ZbglRdpQuery.formatNodeContentFn(node),
					// 如果处理中（即已有实际开始时间），则起始时间为实际开始时间
					start : new Date(node.showBeginTime),
					// 如果已处理（即已有实际结束时间），否则：如果处理中（即已有实际开始时间），则结束时间为当前时间
					end : new Date(node.showEndTime)
				});	
			}
			}
		}
		// 声明一个timeline对象实例
		if (!ZbglRdpQuery.timeline) {
			ZbglRdpQuery.timeline = new vis.Timeline(
				document.getElementById('visualization'), 
				ZbglRdpQuery.nodes, 
				ZbglRdpQuery.plans, 
				Ext.apply({}, ZbglRdpQuery.options, VisUtil.options)
			);
		}
	}
	
	// 复选框多选查询函数处理
	ZbglRdpQuery.checkQueryFn = function() {
		// 清空数据集
		ZbglRdpQuery.clearFn();	
		ZbglRdpQuery.status = "-1";
		if(Ext.getCmp("status_zbz").checked){
			ZbglRdpQuery.status += "," + PLAN_STATUS_HANDLING;
		} 
		if(Ext.getCmp("status_zbwc").checked){
			ZbglRdpQuery.status += "," + PLAN_STATUS_HANDLED;
		} 
		ZbglRdpQuery.store.load();
	};
	
	/** **************** 定义数据容器开始 **************** */
	ZbglRdpQuery.store = new Ext.data.JsonStore({
		id : 'idx',
		root : "root",
		totalProperty : "totalProperty",
		autoLoad : true,
		remoteSort : false,
		url : ctx + '/zbglRdpQuery!pageList.action',
		fields : [
			"idx", 
			"trainTypeIDX", "trainTypeShortName", 
			"trainNo", "dID", "dName", 
			"siteID", "siteName", 
			"rdpStartTime", "rdpEndTime", 
			"trainAccessAccountIDX", "repairClass", 
			"fromPersonName", "toPersonName", 
			"outOrder", "remarks", 
			"toGo","rdpStatus","zbglRdpNodes"
		],
		sortInfo : {
			field : 'trainTypeShortName',
			direction : 'ASC'
		},
		listeners : {
			// 数据加载完成后的函数处理
			load : ZbglRdpQuery.storeLoadFn,
			
			// 数据加载异常处理
			exception: function() {
				var response = arguments[4];
	        	var result = Ext.util.JSON.decode(response.responseText);
				if (!Ext.isEmpty(result.errMsg)) {
					Ext.Msg.alert("数据错误", result.errMsg);
				}
			},		
			beforeload: function(store, options) {
	       	 	if(!ZbglRdpQuery.loadMask)   
	       	 		ZbglRdpQuery.loadMask = new Ext.LoadMask(Ext.getBody(), {
	       	 			msg: '正在加载，请稍候...',
	       	 			store: ZbglRdpQuery.store
	       	 		});
       	 		ZbglRdpQuery.loadMask.show();
				var searchParams = {};
				// 根据根据整备计划状态过滤
				searchParams.rdpStatus = ZbglRdpQuery.status;
				searchParams.trainNo = Ext.getCmp('txt_train_type_no').getValue();
				searchParams = MyJson.deleteBlankProp(searchParams);
				store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
			}
		}
	});
	/** **************** 定义数据容器结束 **************** */
	
	/** **************** 定义时间轴显示内容开始 **************** */
	ZbglRdpQuery.viewport = new Ext.Viewport({
		layout: 'border',
		items: [{
			region: 'center',
			border: false, autoScroll : true,
			tbar: [{
		    	xtype:"checkbox", id:"status_zbz", boxLabel:"整备中&nbsp;&nbsp;&nbsp;&nbsp;", checked: true , handler: ZbglRdpQuery.checkQueryFn
		    }, {
		    	xtype:"checkbox", id:"status_zbwc", boxLabel:"整备完成&nbsp;&nbsp;&nbsp;&nbsp;",  handler: ZbglRdpQuery.checkQueryFn
		    }, '-',  {
				text : '刷新',
				iconCls : 'refreshIcon',
				handler : function() {
					self.location.reload();
				}
			}, '->', {
				xtype:'textfield', id:'txt_train_type_no', enableKeyEvents:true, emptyText:'输入车型车号快速检索', listeners: {
		    		keyup: function(filed, e) {
						// 如果敲下Enter（回车键），则触发添加按钮的函数处理
					if (e.getKey() == e.ENTER){
						// 清空数据集
						ZbglRdpQuery.clearFn();
						ZbglRdpQuery.store.load();
						}
		    		}
	    		}
			}, {
				text:'查询', iconCls:'searchIcon', handler: function(){
					// 清空数据集
					ZbglRdpQuery.clearFn();
					ZbglRdpQuery.store.load();
				}
			}, {
				text:'重置', iconCls:'resetIcon', handler: function(){
					Ext.getCmp('txt_train_type_no').reset();
					// 清空数据集
					ZbglRdpQuery.clearFn();
					ZbglRdpQuery.store.load();
				}
			}, '-', '图例：', '-', {
				xtype : 'label',
				text : '未处理',
				style: 'font-weight:bold;',
				cls : 'status_example status_example_wkg'
			}, '-', {
				xtype : 'label',
				text : '处理中',
				style: 'font-weight:bold;',
				cls : 'status_example status_example_ykg'
			}, '-', {
				xtype : 'label',
				text : '已处理',
				style: 'font-weight:bold;',
				cls : 'status_example status_example_ywg'
			}],	
			html : [
				'<div id="visualization">',
				'</div>'
			].join(""), bbar: ZbglRdpQuery.createPagingToolbarFn()
		}
//		VTrainAccessAccount.panel
		]
	});
	/** **************** 定义时间轴显示内容结束 **************** */
	
	/** **************** 更新时间轴的定时器定义开始 **************** */
	// 页面初始化完成后延迟3秒启动定时器
	var timerTask = new Ext.util.DelayedTask(function() {
		Ext.TaskMgr.start({
			run : function() {
				// 加载数据
				ZbglRdpQuery.store.reload();
			},
			interval : 1000 * 60		// 每60秒更新一次
		});
	});
	// 延迟30秒
	timerTask.delay(1000 * 30);
	/** **************** 更新时间轴的定时器定义结束 **************** */

});