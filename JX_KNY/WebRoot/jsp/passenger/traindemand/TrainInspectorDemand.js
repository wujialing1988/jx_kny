/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('TrainWorkPlanEdit');	
	/** **************** 定义全局变量开始 **************** */

   	 TrainWorkPlanEdit.loadMask = new Ext.LoadMask(Ext.getBody(), {
       	 	msg: '正在加载，请稍候...',
       	 	removeMask:true
       });
  	var data1 =[];
   	for(var i=1; i<13; i++){
   		data1.push([i])
   	};
	TrainWorkPlanEdit.demands = new vis.DataSet();		// 机车检修作业流程计划数据集对象
	TrainWorkPlanEdit.nodes = new vis.DataSet();		// 机车检修作业流程节点数据集对象
	TrainWorkPlanEdit.timeline = null;					// 时间轴timeline对象
	TrainWorkPlanEdit.planSuffixChar = '_';			// 计划对比数据主键字符后缀
	TrainWorkPlanEdit.start = new Date(new Date().getFullYear(), new Date().getMonth()+1, 0);
	TrainWorkPlanEdit.end = new Date(new Date().getFullYear(), new Date().getMonth(), TrainWorkPlanEdit.start.getDate(),23);
	
	TrainWorkPlanEdit.options = {
		editable: false,					// 设置timeline不可编辑
		zoomable:false,
		stack: true,						// 相邻项目时间有重叠时，以堆栈形式显示
  		align: 'left',						// 设置时间轴上条目的文本内容以“居中”方式显示
  		verticalScroll: true,  // 垂直滚动
//  		horizontalScroll: true,  // 水平滚动
  		zoomKey: 'ctrlKey',
  		maxHeight:'100%',
//		zoomMin: 1000 * 60 * 50 * 12,             					// 时间轴缩小的最小精度（五分钟）
   	    orientation: 'top',
  		// 时间轴可显示的最小、最大时间
		min: new Date(nowYear - 1, nowMonth, nowDay),   // 时间轴范围最小值(以当前时间为截止的过去一年)
		max: getYearEndDate(),        					// 时间轴范围最大值(本年末)
  		
		// 时间轴初始化时显示的起、止时间
  		start: new Date(new Date().getFullYear(), new Date().getMonth(), 1),				// 当前天
  		end: TrainWorkPlanEdit.end				// 明天
	};
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 清空数据集
	TrainWorkPlanEdit.clearFn = function() {
		TrainWorkPlanEdit.demands.clear();
		TrainWorkPlanEdit.nodes.clear();
	}
	
	// 格式化分组信息title的显示内容
	TrainWorkPlanEdit.formatInspectorTitleFn = function(demand) {
		var empname = demand.get('empname');		
		var title = [];
		title.push("姓名：" +empname);
		var month = new Date().getMonth();
		if(!Ext.isEmpty(Ext.getCmp('monthID'))){
			month = Ext.getCmp('monthID').getValue();
		}
		title.push(month + "劳时：" + formatTimeForHours(demand.get('workHours') , 'm'));
		return title.join('\r\n');
	}
	
	// 格式化节点在timeline上的title信息
	TrainWorkPlanEdit.formatNodeTitleFn = function(demand) {
		var title = [];
		// 名称
		title.push("名称：" + demand.get('empname'));
		title.push("车次：" + demand.get('strips') + "/" + demand.get('backStrips'));
		// 时长
		title.push("时长：" + formatTimeForHours(demand.get('duration')*2, 'm'));
		title.push("时间：" + new Date(demand.get('runningDate')).format('m-d H:i') + " ~ " + new Date(demand.get('backDate')).format('m-d H:i'));
		return title.join('\r\n');
	}
	
	TrainWorkPlanEdit.formatNodeContentFn = function(demand) {
		var content = "乘";
		return '<div  style="height:28px; color:black;">' + content + '</div>';
	}
	
	// 数据加载完成后的函数处理
	TrainWorkPlanEdit.storeLoadFn = function(store, records, options ) {
		var count = this.getCount();
		for (var i = 0; i < count; i++) {
			var demand = this.getAt(i);
			var groupId = demand.get('empid');
			// 添加分组信息【计划】
			TrainWorkPlanEdit.demands.update({
				id : groupId + TrainWorkPlanEdit.planSuffixChar,
				title :  TrainWorkPlanEdit.formatInspectorTitleFn(demand),
				content : demand.get('empname')+ "<br/>"+ "劳时(时)："+  formatTimeForHours(demand.get('workHours') , 'm')
			});
		}
		for (var i = 0; i < count; i++) {
			var demand = this.getAt(i);
			var groupId = demand.get('empid');
			if (Ext.isEmpty(demand.get('runningDate'))) {
				MyExt.Msg.alert(demand.get('empname')+ "<span style='font-weight:bold;color:red;'></span>开始时间设置有误，导致未能正常显示！");
				continue;
			} else if (Ext.isEmpty(demand.get('backDate'))) {
				MyExt.Msg.alert(demand.get('empname') + "<span style='font-weight:bold;color:red;'></span>结束时间设置有误，导致未能正常显示！");
				continue;
			}
			TrainWorkPlanEdit.nodes.update({
				id : demand.get('idx') + TrainWorkPlanEdit.planSuffixChar,
				title : TrainWorkPlanEdit.formatNodeTitleFn(demand),
				group : groupId + TrainWorkPlanEdit.planSuffixChar,
				content : TrainWorkPlanEdit.formatNodeContentFn(demand),
   			    start :  new Date(demand.get('runningDate')),
				end : new Date(demand.get('backDate'))
			});
		}

		if(TrainWorkPlanEdit.loadMask) TrainWorkPlanEdit.loadMask.hide();
		// 声明一个timeline对象实例
		if (!TrainWorkPlanEdit.timeline) {		
			TrainWorkPlanEdit.timeline = new vis.Timeline(
				document.getElementById('visualization'), 
				TrainWorkPlanEdit.nodes, 
				TrainWorkPlanEdit.demands, 
				Ext.apply({}, TrainWorkPlanEdit.options, VisUtil.options)
			);
		}

	}
	/** **************** 定义全局函数结束 **************** */
	
	// 创建分页工具栏
	TrainWorkPlanEdit.createPagingToolbarFn = function(){
	 	cfg = {pageSize:10, store: TrainWorkPlanEdit.store};
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
		    		TrainWorkPlanEdit.clearFn();
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
	        items: ['-', '&nbsp;&nbsp;', pageComboBox],
	        listeners: {
	        	beforechange : function() {
	        		// 清空数据集
					TrainWorkPlanEdit.clearFn();
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
	 
	/** **************** 定义数据容器开始 **************** */
	TrainWorkPlanEdit.store = new Ext.data.JsonStore({
		id : 'idx',
		root : "root",
		totalProperty : "totalProperty",
		autoLoad : true,
		remoteSort : false,
		url : ctx + '/trainInspectorDemand!pageList.action',
		fields : [
			"idx", 
			"trainDemandIdx", "strips", "backStrips",
			"runningDate", "backDate", 
			"duration", "empid", "workHours",
			"empname"
		],
		sortInfo : {
			field : 'runningDate',
			direction : 'DESC'
		},
		listeners : {
			// 数据加载完成后的函数处理
			load : TrainWorkPlanEdit.storeLoadFn,
			
			// 数据加载异常处理
			exception: function() {
				if(TrainWorkPlanEdit.loadMask) TrainWorkPlanEdit.loadMask.hide();
				var response = arguments[4];
	        	var result = Ext.util.JSON.decode(response.responseText);
				if (!Ext.isEmpty(result.errMsg)) {
					Ext.Msg.alert("数据错误", result.errMsg);
				}
			},
			beforeload: function(store, options) {	      
       	 		TrainWorkPlanEdit.loadMask.show(); // 显示遮罩
       	 		TrainWorkPlanEdit.clearFn();  // 重新回载时清空数据集
				var searchParams = {};
				searchParams.month = new Date().format('Y-m')
				// 月份
				if(!Ext.isEmpty(Ext.getCmp('monthID').getValue())){
					searchParams.month = new Date(new Date().getFullYear(),Ext.getCmp('monthID').getValue()-1).format('Y-m');
				}
				// 根据人员
				if(!Ext.isEmpty(Ext.getCmp('empname'))){
					searchParams.empname = Ext.getCmp('empname').getValue();
				}
				searchParams = MyJson.deleteBlankProp(searchParams);
				store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
			}
		}
	});
	/** **************** 定义数据容器结束 **************** */
	
	//页面适应布局
	TrainWorkPlanEdit.viewport = new Ext.Viewport({
		layout: 'fit',
		items: [{
			border: false, autoScroll : false,
			tbar: [{
				text : '刷新',
				iconCls : 'refreshIcon',
				handler : function() {
					self.location.reload();
				}
			}, '-',  {
				emptyText:'默认为当前月',
				name:"month", id:"monthID",
				xtype: 'combo',	
				width:90,
		        store:new Ext.data.SimpleStore({
				    fields: ['K'],
					data : data1
				}),
				valueField:'K',
				displayField:'K',
				triggerAction:'all',
				mode:'local',		
				listeners: {
					keyup: function(filed, e) {
						// 如果敲下Enter（回车键），则触发添加按钮的函数处理
						if (e.getKey() == e.ENTER){
							TrainWorkPlanEdit.start = new Date(new Date().getFullYear(), this.getValue(), 0);
							TrainWorkPlanEdit.end = new Date(new Date().getFullYear(), this.getValue()-1, TrainWorkPlanEdit.start.getDate(),23);
							TrainWorkPlanEdit.timeline.setWindow({
						           	start: new Date(new Date().getFullYear(), this.getValue()-1, 1),				// 当前天
  									end: TrainWorkPlanEdit.end					// 天
						        });
					        TrainWorkPlanEdit.store.load();
						}
		    		}
	    		}
			}, '->', {
				xtype:'textfield', id:'empname', enableKeyEvents:true,width:100, emptyText:'姓名', listeners: {
		    		keyup: function(filed, e) {
						// 如果敲下Enter（回车键），则触发添加按钮的函数处理
						if (e.getKey() == e.ENTER){
							// 清空数据集
							TrainWorkPlanEdit.clearFn();
							TrainWorkPlanEdit.store.load();
						}
		    		}
	    		}
			}, {
				text:'查询', iconCls:'searchIcon', handler: function(){
					// 清空数据集
					TrainWorkPlanEdit.clearFn();
					TrainWorkPlanEdit.store.load();
				}
			}, {
				text:'重置', iconCls:'resetIcon', handler: function(){
					Ext.getCmp('empname').reset();
					// 清空数据集
					TrainWorkPlanEdit.clearFn();
					TrainWorkPlanEdit.store.load();
				}
			}],			
			html : [
				'<div id= "visualization">',
				'</div>'
			].join(""),
				
			bbar: TrainWorkPlanEdit.createPagingToolbarFn()
		}]
	});
	
	/** **************** 更新时间轴的定时器定义开始 **************** */
	// 页面初始化完成后延迟3秒启动定时器
	var timerTask = new Ext.util.DelayedTask(function() {
		Ext.TaskMgr.start({
			run : function() {
				// 加载数据
				TrainWorkPlanEdit.store.reload();
				// 加载在修机车面板数据
			},
			interval : 1000 * 60 * 10		// 每5分钟更新一次
		});
	});
	// 延迟30秒
	timerTask.delay(1000 * 30);
	/** **************** 更新时间轴的定时器定义结束 **************** */

});