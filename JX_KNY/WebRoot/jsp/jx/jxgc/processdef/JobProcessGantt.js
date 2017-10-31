/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('JobProcessGantt');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	JobProcessGantt.processIDX = null;
	
	var project = new Edo.project.Project();			// 甘特图
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 加载甘特图数据
	JobProcessGantt.loadFn = function (){
		var url = ctx + '/jobProcessDef!planOrderGantt.action?idx=' + JobProcessGantt.processIDX;
		loadJSON(url, project);
	}
	
	// 初始化甘特图组件
	JobProcessGantt.initFn = function() {
		project.set({
			width : document.getElementById('view').scrollWidth,
			height : document.getElementById('view').offsetHeight,
			enableMenu : true,
			readOnly : true,
			render : document.getElementById('view')
		});
		Edo.managers.ResizeManager.reg({
			target : project
		});
		
		// 默认只显示任务树
	//	project.tree.set('autoColumns', false);
		project.gantt.parent.set('visible', false);
		project.tree.set('verticalScrollPolicy', 'auto');
		project.tree.parent.set('visible', true);
	
		// 获得任务树已有定义的列配置对象
		var columns = project.tree.groupColumns;
		columns[0].header = '机车检修计划甘特图';
		project.tree.groupColumns[0].columns[2].width = 200;
		columns[0].columns.removeAt(1);
		columns[0].columns.insert(1, {
				header : 'WBS',
				dataIndex : 'OutlineNumber',
				width : 50
			});
		// 工期
		columns[0].columns.removeAt(3);
		columns[0].columns.insert(3, {
			header : '工期',
			dataIndex : 'workDate',
			width : 120
		});
		columns[0].columns.removeAt(4);
		// 工期渲染
		var workDate_column = project.tree.groupColumns[0].columns[3];
		workDate_column.renderer = function(v, r) {
			return formatTime(v, 'm')
		}
		//获取开始时间列
		var start_column = project.tree.groupColumns[0].columns[4];
		//获取结束时间列
		var end_column = project.tree.groupColumns[0].columns[5];
		start_column.width = 130;
		end_column.width = 130;
		start_column.header = "计划开始时间";
		end_column.header = "计划完成时间";
		//给开始日期列增加新的渲染器
		start_column.renderer = function(v, r){
			if(v != null && v != '' && v != 'null'){
		    	return v.format('Y-m-d H:i');
			}else{
				return "";
			}
		}
		//给结束日期列增加新的渲染器
		end_column.renderer = function(v, r){
		    if(v != null && v != '' && v != 'null'){
		    	return v.format('Y-m-d H:i');
			}else{
				return "";
			}
		}
		
		columns[0].columns.removeAt(7);
		
		// 获得条形图对象
		var gantt = project.gantt;
		// •年/季：year-quarter
		// •年/月：year-month
		// •年/周：year-week
		// •年/日：year-day
		// •季/月：quarter-month
		// •季/周：quarter-week
		// •季/日：quarter-day
		// •月/周：month-week
		// •月/日：month-day
		// •周/日：week-day
		// •日/时：day-hour
		// •时/分：hour-minute
		gantt.set('dateView', 'day-hour');
	
		// 将修改后的列配置对象, 设置给任务树的columns属性
		project.tree.set('columns', columns);
	
		// 获得甘特图右键菜单对象
		var menu = project.getMenu();
		// 隐藏掉"编辑"菜单项
		for (var i = 0; i < 14; i++) {
			menu.getChildAt(i).set('visible', false);
		}
		menu.addChildAt(1, {
			type : 'button',
			icon : 'e-icon-edit',
			text : '刷新',
			onclick : function(e) {
				// 加载甘特图数据
				JobProcessGantt.loadFn();
			}
		});
	}
	/** **************** 定义全局函数结束 **************** */
	
	JobProcessGantt.win = new Ext.Window({
		closeAction: 'hide',
		title:'检车检修作业流程甘特图',
		layout:'fit',
		maximized: true,
		items:[{
			tbar:[{
            	text: '显示方式：只显示任务树', id:'dateviewBtn1',
           		menu: [{
	            	text: '只显示任务树', handler: function(){ 
				        project.gantt.parent.set('visible', false);    
				        project.tree.set('verticalScrollPolicy', 'auto');						        
				        project.tree.parent.set('visible', true);
				        Ext.getCmp('dateviewBtn').hide();
				        Ext.getCmp('dateviewBtn1').setText('显示方式：'+this.text);
                    }
            	}, {
                	text: '只显示条形图', handler: function(){ 
                        project.tree.parent.set('visible', false);        
				        project.gantt.parent.set('visible', true);    
				        project.tree.set('verticalScrollPolicy', 'off');
				        Ext.getCmp('dateviewBtn').show();
				        Ext.getCmp('dateviewBtn1').setText('显示方式：'+this.text);
                    }
            	}, {
                	text: '全部显示', handler: function(){ 
				        project.gantt.parent.set('visible', true);  						        
				        project.tree.parent.set('visible', true);
				        Ext.getCmp('dateviewBtn').show();
				        Ext.getCmp('dateviewBtn1').setText('显示方式：'+this.text);
                    }
            	}]
			}, {
	            text: '日期 ：天/时', id:'dateviewBtn', hidden:true,
	            menu: [{
            		text: '年/季', handler: function(){ 
                        project.gantt.set('dateView', 'year-quarter');
                        Ext.getCmp('dateviewBtn').setText('日期 : '+this.text);
                    }
            	}, {
                	text: '年/月', handler: function(){ 
                        project.gantt.set('dateView', 'year-month');
                        Ext.getCmp('dateviewBtn').setText('日期 : '+this.text);
                    }
            	}, {
                	text: '年/天', handler: function(){ 
                        project.gantt.set('dateView', 'year-day');
                        Ext.getCmp('dateviewBtn').setText('日期 : '+this.text);
                    }
            	}, {
                	text: '月/天', handler: function(){ 
                        project.gantt.set('dateView', 'month-day');
                        Ext.getCmp('dateviewBtn').setText('日期 : '+this.text);
                    }
            	}, {
                	text: '周/天', handler: function(){ 
                        project.gantt.set('dateView', 'week-day');
                        Ext.getCmp('dateviewBtn').setText('日期 : '+this.text);
                	}
            	}, {
                	text: '天/时', handler: function(){ 
                        project.gantt.set('dateView', 'day-hour');
                        Ext.getCmp('dateviewBtn').setText('日期 : '+this.text);
                	}
            	}]
			}],
			html:[
			'<div id="view" style="height:100%;width:100%;"></div>'
			].join('')
		}],
		buttonAlign:'center',
		buttons:[{
			text:'关闭', iconCls:'closeIcon', handler:function(){
				this.findParentByType('window').hide();
			}
		}], 
		listeners: {
			beforeshow: function() {
				// 加载甘特图数据
				JobProcessGantt.loadFn();
			},
			render: function() {
				setTimeout(JobProcessGantt.initFn, 50);
			},
			show: function() {
				// 隐藏机车检修作业流程节点编辑窗口
				if (JobProcessNodeDef.grid.saveWin) JobProcessNodeDef.grid.saveWin.hide();
			}
		}
	});
	
	/** **************** 定义甘特图开始 **************** */
	
	/** **************** 定义甘特图结束 **************** */
	
});