/**
 * 机车检修作业计划 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){	
Ext.namespace('WorkPlanGanttSearch'); 	
var project = new Edo.project.Project();
WorkPlanGanttSearch.loadFn = function(displayMode, nodeIdx) {
	if (!displayMode)
		displayMode = 'default';
	var url = ctx + '/trainWorkPlan!planOrderGantt.action?workPlanIDX=' + TrainWorkPlanGanttSearch.workPlanIDX + '&displayMode=' + displayMode ;
	if (nodeIdx)
		url += '&nodeIdx=' + nodeIdx;
	loadJSON(url, project);
}
WorkPlanGanttSearch.initFn = function() {
	project.set({
		width : document.getElementById('view').scrollWidth,
		height : document.getElementById('view').offsetHeight,
	    enableMenu:true, readOnly:true,
	    render: document.getElementById('view')
	});
	Edo.managers.ResizeManager.reg({
	    target: project
	});
	//默认只显示任务树
	project.gantt.parent.set('visible', false);    
	project.tree.set('verticalScrollPolicy', 'auto');        
	project.tree.parent.set('visible', true);
		
	//获得任务树已有定义的列配置对象
	var columns = project.tree.groupColumns;
	columns[0].header = '  机车检修计划甘特图';
	project.tree.groupColumns[0].columns[2].width = 200;
	columns[0].columns.removeAt(1);
	columns[0].columns.insert(1,{
	    header: 'WBS',
	    dataIndex: 'OutlineNumber',
	    width: 50
	});
	columns[0].columns.removeAt(3);
	columns[0].columns.insert(3,{
	    header: '工期',
	    dataIndex: 'workDate',
	    width: 80
	});
	
	var workDate_column = project.tree.groupColumns[0].columns[3];
	workDate_column.renderer = function(v, r){
		if(v != null && v != '' && v != 'null'){
			var hours = v.toString().split(".")[0];
			var minutes = (v*60 - hours*60).toFixed(0);
			if(hours > 0){
				if(minutes > 0){
	    			return hours + "小时" + minutes + "分钟";
				}else{
					return hours + "小时";
				}
			}else{
				return minutes + "分钟"	;	
			}
		}else{
			return "0小时";
		}
	}
	//获取开始时间列
	var start_column = project.tree.groupColumns[0].columns[5];
	//获取结束时间列
	var end_column = project.tree.groupColumns[0].columns[6];
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
//	var preTask_column = project.tree.groupColumns[0].columns[7];
//	preTask_column.width = 100;
//	columns[0].columns.removeAt(8);
	columns[0].columns.removeAt(7);	
	columns[0].columns.insert(7,{
	    header: '检修班组',
	    dataIndex: 'workTeam',
	    width: 130
	});
	columns[0].columns.removeAt(8);
	columns[0].columns.insert(8,{
	    header: '工位',
	    dataIndex: 'workStationName'
	});
	columns[0].columns.add({
	    header: '实际开始时间',
	    dataIndex: 'realStart',
	    width: 130
	});
	columns[0].columns.add({
	    header:'实际完成时间',
	    dataIndex: 'realFinish',
	    width: 130
	});
	columns[0].columns.add({
	    header: '处理情况',
	    dataIndex: 'ProcessInfo'
	});
	columns[0].columns.add({
	    header: '计划模式',
	    dataIndex: 'planMode',
	    width: 70
	});
	columns[0].columns.add({
	    header: '工作日历',
	    dataIndex: 'workCalendar',
	    width: 160
	});
	columns[0].columns.add({
	    header: '前置任务',
	    dataIndex: 'PredecessorLinkStr',
	    width: 160
	});
	
	//获得条形图对象
	var gantt = project.gantt;
	//•年/季：year-quarter
	//•年/月：year-month
	//•年/周：year-week
	//•年/日：year-day
	//•季/月：quarter-month
	//•季/周：quarter-week
	//•季/日：quarter-day
	//•月/周：month-week
	//•月/日：month-day
	//•周/日：week-day
	//•日/时：day-hour
	//•时/分：hour-minute
	gantt.set('dateView', 'day-hour');
	gantt.set('taskNameVisible', false);
	gantt.set('viewMode', 'track');
//	gantt.taskTipRenderer = function(task, gantt, isTrack){
//	    //是否移动到"比较基准"下方的条形图上        
//	    if(isTrack){
//	        //获得任务的当前比较基准数据
//	        var bl = task.Baseline ? task.Baseline[0] : null;
//	        var str = task.Name+"<br/>开始时间:"+task.Start.format('Y-m-d H:i')+"<br />完成时间:"+task.Finish.format('Y-m-d H:i');
//	        if (bl != null)
//	        	str += "<br/>实际开始时间:"+bl.Start.format('Y-m-d H:i')+"<br />实际完成时间:"+bl.Finish.format('Y-m-d H:i');
//	        return str;
//	    }else{            
//	        return task.Name+"<br/>开始时间:"+task.Start.format('Y-m-d H:i')+"<br />完成时间:"+task.Finish.format('Y-m-d H:i');
//	    }
//	}
	
	//将修改后的列配置对象, 设置给任务树的columns属性
	project.tree.set('columns', columns);
	//获得甘特图右键菜单对象
	var menu = project.getMenu();
	////隐藏掉"编辑"菜单项
	for (var i = 0; i < 14; i++) {
		menu.getChildAt(i).set('visible', false);
	}
	menu.addChildAt(1, {
	    type: 'button', icon: 'e-icon-refresh', text: '刷新',
	    onclick: function(e){
	    	WorkPlanGanttSearch.loadFn();
	    }
	});
	menu.addChildAt(3, {
	    type: 'button', icon: 'e-icon-search', text: '查看',
	    onclick: function(e){
	    	WorkPlanGanttSearch.editNode();
	    }
	});
}

var processTips;
function hide(tip){
	if(tip){
		alertSuccess(tip);
	}else{
		alertSuccess();
	}
	if(processTips){	//如果有遮罩，调用一次 隐藏
		hidetip();
	}	
}
/*
 * 显示处理遮罩
 */
function showtip(msg){
	processTips = new Ext.LoadMask(TrainWorkPlanWinSearch.win.getEl()||Ext.getBody(),{
		msg: msg || "正在处理你的操作，请稍后...",
		removeMask:true
	});
	processTips.show();
}
/*
 * 隐藏处理遮罩
 */
function hidetip(){
	processTips.hide();
}

WorkPlanGanttSearch.editNode = function(idx,parentIdx,isLeaf,status,idxValue) {
	var task = project.tree.getSelected();
	if(idx && parentIdx && isLeaf && status && idxValue){
		task.nodeCaseIdx = idx;
		task.parentIdx = parentIdx;
		task.isLastLevel = isLeaf;
		task.status = status;
		
		var workPlanIDX = idxValue;
		if (!Ext.isEmpty(idxValue)) {
			var cfg = {
		        scope: this, url: ctx + '/trainWorkPlan!getEntityByIDX.action',
		        params: {workPlanIDX: idxValue},
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.entity != null) {		            	
		                workPlanEntity = result.entity;
		            }
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
		
	}else{
//		var task = project.tree.getSelected();
		if(Ext.isEmpty(task)) {
			MyExt.Msg.alert("请选择一条任务！");
			return;
		}
		if(task.NodeType == PROCESSTYPE){
			MyExt.Msg.alert("请选择非流程任务的节点！");
			return;			
		}
	}
	
	JobProcessNodeSearch.idx = task.nodeCaseIdx;
	JobProcessNodeRelSearch.nodeIDX = task.nodeCaseIdx;
	JobProcessNodeRelSearch.parentIDX = task.parentIdx;
	
	NodeAndWorkCardEditSearch.showEditWin();
	if (task.isLastLevel == CONST_INT_IS_LEAF_YES) {
		NodeAndWorkCardEditSearch.tabs.unhideTabStripItem("WorkCardEditTabSearch");
		WorkCardEditSearch.nodeCaseIDX = task.nodeCaseIdx;
		WorkCardEditSearch.grid.store.load();
		if (task.status == NODE_STATUS_UNSTART)
			TrainWorkSearch.status = WORKCARD_STATUS_NEW;
		else if (task.status == NODE_STATUS_GOING)
			TrainWorkSearch.status = WORKCARD_STATUS_HANDLING;
		TrainWorkSearch.nodeCaseIDX = task.nodeCaseIdx;
		TrainWorkSearch.trainTypeIDX = workPlanEntity.trainTypeIDX;				
	} else if (task.isLastLevel == CONST_INT_IS_LEAF_NO) {		
		NodeAndWorkCardEditSearch.tabs.hideTabStripItem("WorkCardEditTabSearch");
	}			
	NodeAndWorkCardEditSearch.tabs.unhideTabStripItem("NodeExtConfigTabSearch");
	JobNodeExtConfigSearch.loadFn(task.nodeCaseIdx);
	JobProcessNodeTreeWin.processName = workPlanEntity.processName;
	JobProcessNodeTreeWin.workPlanIDX = workPlanIDX;
	JobProcessNodeTreeWin.nodeIDX = task.nodeCaseIdx;
}

Ext.namespace('TrainWorkPlanWinSearch');                       //定义命名空间
TrainWorkPlanWinSearch.fieldWidth = 120;
TrainWorkPlanWinSearch.labelWidth = 80;
TrainWorkPlanWinSearch.infoForm = new Ext.form.FormPanel({
	labelWidth:TrainWorkPlanWinSearch.labelWidth, border: false,
	labelAlign:"left", layout:"column",
	bodyStyle:"padding:10px;",
	defaults:{
		xtype:"container", autoEl:"div", layout:"form", columnWidth:0.25, 
		defaults:{
			style: 'border:none; background:none;', 
			xtype:"textfield", readOnly: true,
			anchor:"100%"
		}
	},
	items:[{
        items: [
        	{ fieldLabel:"车型车号", name:"trainTypeAndNo"}
        ]
	},{
        items: [
        	{ fieldLabel:"修程修次", name:"xcxc"}
        ]
	},{
        items: [
        	{ fieldLabel:"计划开始时间", name:"planBeginTime"}
        ]
	},{
        items: [
        	{ fieldLabel:"计划完成时间", name:"planEndTime"}
        ]
	}]
});
TrainWorkPlanWinSearch.ganttPanel = new Ext.Panel({
	border:false, layout:'fit',
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
		},{
		iconCls:"searchIcon",
    	text:"查看",
    	handler: function(){
    		WorkPlanGanttSearch.editNode();
    	}
    },{
    	iconCls:"refreshIcon", text:"刷新", handler: function(){WorkPlanGanttSearch.loadFn(); }
    },"-",{
    	id: 'closeWorkPlanWinButton', text: "关闭", iconCls: "closeIcon", 
    	handler: function(){
    		// Modefied by hetao at 2015-09-17 15:00 '【机车检修台位占用情况】点击进入计划编辑页面时，“关闭”按钮不起作用
			if (typeof VTrainWorkPlan != 'undefined') {
	    		VTrainWorkPlan.clearFn();
				VTrainWorkPlan.store.load();
			}
    		TrainWorkPlanWinSearch.win.hide(); 
    	}
    }],
		html:[
		'<div id="view" style="height:100%;width:100%;"></div>'
		].join('')
	}]
});
TrainWorkPlanWinSearch.panel = new Ext.Panel({
    layout: "border", 
    defaults: {
    	border: false, layout: "fit"
    },
    items: [{
        region: 'north', collapsible:true, collapsed: false,  height: 70, 
        items:[TrainWorkPlanWinSearch.infoForm], frame: true, title: "检修基本信息"
    },{
        id:"gantt", region : 'center', items: TrainWorkPlanWinSearch.ganttPanel
    }]
});
TrainWorkPlanWinSearch.win = new Ext.Window({
    title:"机车检修作业计划查看", 
    closeAction:"hide", maximized : true, layout: 'fit',
    items:TrainWorkPlanWinSearch.panel,
	listeners: {
		beforeshow: function() {
			// 加载甘特图数据
			WorkPlanGanttSearch.loadFn();
		},
		render: function() {
			setTimeout(WorkPlanGanttSearch.initFn, 50);
		}
	}
});
TrainWorkPlanWinSearch.showWin = function(data) {
	var dataArray = data.split(",");
	var form = TrainWorkPlanWinSearch.infoForm.getForm();
	form.reset();
	form.findField("trainTypeAndNo").setValue(dataArray[1]);
	form.findField("xcxc").setValue(dataArray[2]);
	form.findField("planBeginTime").setValue(dataArray[3]);
	form.findField("planEndTime").setValue(dataArray[4]);	
	TrainWorkPlanGanttSearch.workPlanIDX = dataArray[0];
	workPlanIDX = TrainWorkPlanGanttSearch.workPlanIDX;
	if (!Ext.isEmpty(TrainWorkPlanGanttSearch.workPlanIDX)) {
		var cfg = {
	        scope: this, url: ctx + '/trainWorkPlan!getEntityByIDX.action',
	        params: {workPlanIDX: TrainWorkPlanGanttSearch.workPlanIDX},
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.entity != null) {		            	
	                workPlanEntity = result.entity;
	                TrainWorkPlanWinSearch.win.show();
	            }
	        }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	}
}
});