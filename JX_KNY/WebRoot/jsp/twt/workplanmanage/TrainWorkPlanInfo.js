/**
 * 机务设备工单定义 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TrainWorkPlanInfo');
	/** **************** 定义全局变量开始 **************** */
	TrainWorkPlanInfo.labelWidth = 100;
	TrainWorkPlanInfo.fieldWidth = 140;
	TrainWorkPlanInfo.idx = null;
	
	var project = new Edo.project.Project();			// 甘特图
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 加载甘特图数据
	TrainWorkPlanInfo.loadFn = function (idx){
		var url = ctx + '/trainWorkPlan!planOrderGantt.action?workPlanIDX=' + idx;
		loadJSON(url, project);
	}
	// 初始化
	TrainWorkPlanInfo.initFn = function(entity, form) {
		if (Ext.isEmpty(entity)) {
        	MyExt.Msg.alert("未能查询到机车<span style='font-weight:bold;color:red;'>" + trainTypeShortName + trainNo + "</span>的检修作业计划信息！");
        	return;
        }
        // 设置作业计划主键
        TrainWorkPlanInfo.idx = entity.idx;
        
        // 设置作业计划基本信息
		form.find('fieldLabel', '车型车号')[0].setValue(entity.trainTypeShortName + entity.trainNo);
		form.find('fieldLabel', '修程修次')[0].setValue(entity.repairClassName + entity.repairtimeName);
		form.find('fieldLabel', '计划开始时间')[0].setValue(new Date(entity.planBeginTime).format('Y-m-d H:i'));
		form.find('fieldLabel', '计划结束时间')[0].setValue(new Date(entity.planEndTime).format('Y-m-d H:i'));
		// 加载甘特图数据
		TrainWorkPlanInfo.loadFn(entity.idx);
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义基本信息表单开始 **************** */
	TrainWorkPlanInfo.form = new Ext.form.FormPanel({
		style: 'padding-left:20px;',
		layout: 'column', labelWidth: TrainWorkPlanInfo.labelWidth,
		defaults: {
			xtype: 'container', columnWidth: .25, layout: 'form',
			defaults: {
				xtype: 'textfield', readOnly: true,
				width: TrainWorkPlanInfo.fieldWidth, 
				style: 'background:none; border: none;', anchor: '100%'
			}
		},
		items: [{
			items: [{ fieldLabel: '车型车号' }]
		}, {
			items: [{ fieldLabel: '修程修次' }]
		}, {
			items: [{ fieldLabel: '计划开始时间' }]
		}, {
			items: [{ fieldLabel: '计划结束时间' }]
		}]
	});
	/** **************** 定义基本信息表单结束 **************** */
	
	// 自适应页面布局
	new Ext.Viewport({
		layout: 'border', defaults: { layout: 'fit', border: false },
		items: [{
			region: 'north',
			height: 60,
			frame: true,
			title: '机车基本信息',
			collapsible: true,
			collapseMode: 'mini',
			items: TrainWorkPlanInfo.form
		}, {
			region: 'center', id:'id_gantt', autoScroll : false,
			tbar: [{
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
			}, {
				text: '查看作业工单', iconCls: 'queryIcon', handler: function(){
					MyExt.Msg.alert('功能完善中！请稍候');
				}
			}, {
				text: '刷新', iconCls:'refreshIcon', handler: function(){
					self.location.reload();
				}
			}],
			html: [
				'<div id="view" style="height:100%;width:100%;"></div>'
			].join('')
		}], 
		listeners: {
			render: function(form) {
				var cfg = {
					url: ctx + '/trainWorkPlan!getModelByEntiy.action',
					jsonData: Ext.util.JSON.encode({
						trainTypeIDX: trainTypeIDX,				// 车型主键
						trainNo: trainNo,						// 车号
						workPlanStatus: STATUS_HANDLING			// 只查询当前作业计划状态为“处理中”的机车信息
					}),
					success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        // 初始化
				        TrainWorkPlanInfo.initFn(result.entity, form);
				    }
				};
				Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
			}
		}
	});
	
	/** **************** 定义甘特图开始 **************** */
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
	columns[0].columns.removeAt(3);
	columns[0].columns.insert(3, {
				header : '工期',
				dataIndex : 'workDate',
				width : 80
			});

	var workDate_column = project.tree.groupColumns[0].columns[3];
	workDate_column.renderer = function(v, r) {
		if (v != null && v != '' && v != 'null') {
			var hours = v.toString().split(".")[0];
			var minutes = (v * 60 - hours * 60).toFixed(0);
			if (hours > 0) {
				if (minutes > 0) {
					return hours + "小时" + minutes + "分钟";
				} else {
					return hours + "小时";
				}
			} else {
				return minutes + "分钟";
			}
		} else {
			return "0小时";
		}
	}
	// columns[0].columns.removeAt(4);
	// 获取开始时间列
	var start_column = project.tree.groupColumns[0].columns[5];
	// 获取结束时间列
	var end_column = project.tree.groupColumns[0].columns[6];
	start_column.width = 130;
	end_column.width = 130;
	start_column.header = "计划开始时间";
	end_column.header = "计划完成时间";
	// 给开始日期列增加新的渲染器
	start_column.renderer = function(v, r) {
		if (v != null && v != '' && v != 'null') {
			return v.format('Y-m-d H:i');
		} else {
			return "";
		}
	}
	// 给结束日期列增加新的渲染器
	end_column.renderer = function(v, r) {
		if (v != null && v != '' && v != 'null') {
			return v.format('Y-m-d H:i');
		} else {
			return "";
		}
	}
	columns[0].columns.removeAt(8);
	columns[0].columns.add({
				header : '检修班组',
				dataIndex : 'workTeam',
				width : 130
			});
	columns[0].columns.add({
				header : '工位',
				dataIndex : 'workStationName'
			});
	columns[0].columns.add({
				header : '计划模式',
				dataIndex : 'planMode',
				width : 70
			});
	columns[0].columns.add({
				header : '工作日历',
				dataIndex : 'workCalendar',
				width : 160
			});
	columns[0].columns.add({
				header : '处理情况',
				dataIndex : 'ProcessInfo'
			});
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
//			self.location.reload();
			// 加载甘特图数据
			TrainWorkPlanInfo.loadFn(TrainWorkPlanInfo.idx);
		}
	});
	/** **************** 定义甘特图结束 **************** */
	
	// 窗口大小改变时，重新加载页面，用于解决关闭窗口后，甘特图组件不能自适应的缺陷
	window.onresize = function(){
		self.location.reload();
	};
	
})