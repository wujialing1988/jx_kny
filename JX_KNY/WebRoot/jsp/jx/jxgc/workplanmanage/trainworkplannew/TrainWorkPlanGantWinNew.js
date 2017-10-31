/**
 * 机车检修作业计划 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){	
	Ext.namespace('WorkPlanGantt'); 	
	var projectNew = new Edo.project.Project();
	WorkPlanGantt.loadFn = function(displayMode, nodeIdx) {
		if (!displayMode)
			displayMode = 'default';
		var url = ctx + '/trainWorkPlan!planOrderGantt.action?workPlanIDX=' + TrainWorkPlanGantt.workPlanIDX + '&displayMode=' + displayMode ;
		if (nodeIdx)
			url += '&nodeIdx=' + nodeIdx;
		loadJSON(url, projectNew);
	}
	WorkPlanGantt.initFn = function() {
		projectNew.set({
			width : document.getElementById('viewNew1').scrollWidth,
			height : document.getElementById('viewNew1').offsetHeight,
		    enableMenu:true, readOnly:true,
		    render: document.getElementById('viewNew1')
		});
		Edo.managers.ResizeManager.reg({
		    target: projectNew
		});
		//默认只显示任务树
		projectNew.gantt.parent.set('visible', false);    
		projectNew.tree.set('verticalScrollPolicy', 'auto');        
		projectNew.tree.parent.set('visible', true);
			
		//获得任务树已有定义的列配置对象
		var columns = projectNew.tree.groupColumns;
		columns[0].header = '  机车检修计划甘特图';
		projectNew.tree.groupColumns[0].columns[2].width = 200;
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
		
		var workDate_column = projectNew.tree.groupColumns[0].columns[3];
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
		var start_column = projectNew.tree.groupColumns[0].columns[5];
		//获取结束时间列
		var end_column = projectNew.tree.groupColumns[0].columns[6];
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
		//去掉组件自带的【前置任务显示】
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
		// 设置工位
		var workStationName_column = projectNew.tree.groupColumns[0].columns[8];
		workStationName_column.renderer = function(v, r){
			var isLastLevel = r.isLastLevel ;
			var isWsComplete = r.isWsComplete ;
			if(isLastLevel == 0 && isWsComplete == 0){
				return '<span style="color:red;">未完成分配</span>' ;		
			}else{
				return v ;
			}
			
		}
		
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
		var gantt = projectNew.gantt;
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
		projectNew.tree.set('columns', columns);
		//获得甘特图右键菜单对象
		var menu = projectNew.getMenu();
		////隐藏掉"编辑"菜单项
		for (var i = 0; i < 14; i++) {
			menu.getChildAt(i).set('visible', false);
		}
		menu.addChildAt(1, {
		    type: 'button', icon: 'e-icon-refresh', text: '刷新',
		    onclick: function(e){
		    	WorkPlanGantt.loadFn();
		    }
		});
		menu.addChildAt(2, {
		    type: 'button', icon: 'e-icon-add', text: '新增',
		    onclick: function(e){
		    	WorkPlanGantt.addNode();
		    }
		});
		menu.addChildAt(3, {
		    type: 'button', icon: 'e-icon-edit', text: '编辑',
		    onclick: function(e){
		    	WorkPlanGantt.editNode(null,null,null,null,null,null);
		    }
		});
		menu.addChildAt(4, {
		    type: 'button', icon: 'e-icon-gototask', text: '调整节点层级关系',
		    onclick: function(e){
		    	WorkPlanGantt.changeNode();
		    }
		});
		menu.addChildAt(5, {
		    type: 'button', icon: 'e-icon-delete', text: '删除',
		    onclick: function(e){
		    	WorkPlanGantt.deleteNode();
		    }
		});
	//	menu.addChildAt(6, {
	//	    type: 'button', icon: 'e-icon-edit', text: '流水线排程',
	//	    onclick: function(e){
	//	    	WorkPlanGantt.updateWorkCardForDispatchByRepairLine();
	//	    }
	//	});
		if (Ext.getCmp("startNodeButton").isVisible()) {
			menu.addChildAt(7, {
		    type: 'button', icon: 'e-gantt-arrowhead-right', text: '启动节点任务',
		    onclick: function(e){
		    	WorkPlanGantt.startNode();
		    }
		});
		}
	};
	// 点击事件隐藏按钮
	projectNew.on('click',function(){
		var task = projectNew.tree.getSelected();
	 	if(Ext.isEmpty(task)) {
	 		return;
	 	}
	 	// 选中流程
 		if(task.NodeType == PROCESSTYPE){
			 Ext.getCmp("startNodeButton").setVisible(false);
			 Ext.getCmp("deleteNodeButton").setVisible(false);
			 Ext.getCmp("editButton").setVisible(true);
			 Ext.getCmp("andNodeButton").setVisible(true);
		}else{ // 选中节点
			Ext.getCmp("andNodeButton").setVisible(false);
			if(null == task.parentIdx ||  "ROOT_0" == task.parentIdx){
				Ext.getCmp("editButton").setVisible(true);
				if(task.status != NODE_STATUS_UNSTART){
					 Ext.getCmp("startNodeButton").setVisible(false);
					 Ext.getCmp("deleteNodeButton").setVisible(false);
				}else{
					 Ext.getCmp("startNodeButton").setVisible(true);
					 Ext.getCmp("deleteNodeButton").setVisible(true);

				}
			}else{
				Ext.getCmp("deleteNodeButton").setVisible(false);
				Ext.getCmp("startNodeButton").setVisible(false);
				Ext.getCmp("editButton").setVisible(false);
			}
		}

	});
	
	//修改处理中和已完成的节点的实际开完工时间
	WorkPlanGantt.updateRealTime = function() {
		var task = projectNew.tree.getSelected();
		if(Ext.isEmpty(task)) {
			MyExt.Msg.alert("请选择一条任务！");
			return;
		}
		if(task.NodeType == PROCESSTYPE){
			MyExt.Msg.alert("请选择非流程任务的节点！");
			return;			
		}
		if(task.status == NODE_STATUS_UNSTART ){
			MyExt.Msg.alert("不能修改【未启动】的节点！");
			return;			
		}
		if(task.status == NODE_STATUS_STOP ){
			MyExt.Msg.alert("不能修改【已终止】的节点！");
			return;			
		}
		AdjustNodeForm.showWin(task.nodeCaseIdx);
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
		processTips = new Ext.LoadMask(ParentNodeTreeWin.win.getEl()||Ext.getBody(),{
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
	
	// 新增
	WorkPlanGantt.addNode = function() {
		var task = projectNew.tree.getSelected();
		if(Ext.isEmpty(task)) {
			MyExt.Msg.alert("请选择一条任务！");
			return;
		}
		var form = JobProcessNode.form.getForm();
		if(task.NodeType == PROCESSTYPE){		
			if(task.status == WORKPLAN_STATUS_HANDLED ){
				MyExt.Msg.alert("不能在【已完成】的节点下新增节点！");
				return;			
			}
			if(task.status == WORKPLAN_STATUS_NULLIFY ){
				MyExt.Msg.alert("不能在【已终止】的节点下新增节点！");
				return;			
			}
			JobProcessNode.parentIDX = '';				
			JobProcessNodeRel.parentIDX = '';
			JobProcessNodeRel.nodeIDX = '';
			JobProcessNode.workPlanIDX = workPlanIDX;
			JobProcessNode.processIDX = workPlanEntity.processIDX;	
			form.findField("planEndTime").disable();
			NodeAndWorkCardEdit.showAddWin();
			NodeAndWorkCardEdit.tabs.hideTabStripItem("WorkCardEditTab");
			NodeAndWorkCardEdit.tabs.hideTabStripItem("NodeExtConfigTab");
			NodeAndWorkCardEdit.tabs.activate(0);
		}			
		else if(task.NodeType == NODETYPE){
			MyExt.Msg.alert("只能在作业计划下新增节点！");
				return;			
	//		if(task.status == NODE_STATUS_COMPLETE ){
	//			MyExt.Msg.alert("不能在【已完成】的节点下新增节点！");
	//			return;			
	//		}
	//		if(task.status == NODE_STATUS_STOP ){
	//			MyExt.Msg.alert("不能在【已终止】的节点下新增节点！");
	//			return;			
	//		}
	//		JobProcessNode.parentIDX = task.nodeCaseIdx;
	//		JobProcessNodeRel.parentIDX = task.nodeCaseIdx;
	//		JobProcessNodeRel.nodeIDX = '';
	//		var cfg = {
	//	        scope: this, url: ctx + '/jobProcessNodeQuery!hasWorkCardOrActivity.action',
	//	        params: {nodeIDX: task.nodeCaseIdx},
	//	        success: function(response, options){
	//	            var result = Ext.util.JSON.decode(response.responseText);
	//	            var hasWorkCardOrActivity = result.hasWorkCardOrActivity;
	//	            if ( hasWorkCardOrActivity != null && hasWorkCardOrActivity == true) {		            	
	//	                MyExt.Msg.alert("此节点已关联有工单，不能新增子节点！");
	//					return;
	//	            } else if (hasWorkCardOrActivity != null && hasWorkCardOrActivity == false){
	//	            	JobProcessNode.workPlanIDX = workPlanIDX;
	//					JobProcessNode.processIDX = workPlanEntity.processIDX;
	//
	//					form.findField("planEndTime").enable();
	//		    		NodeAndWorkCardEdit.showAddWin();
	//		    		NodeAndWorkCardEdit.tabs.hideTabStripItem("WorkCardEditTab");
	//		    		NodeAndWorkCardEdit.tabs.hideTabStripItem("NodeExtConfigTab");
	//		    		NodeAndWorkCardEdit.tabs.activate(0);
	//	            }
	//	        }
	//	    };
	//	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
	}
	
	WorkPlanGantt.editNode = function(idx,parentIdx,isLeaf,status,idxValue) {
		if(idx && isLeaf && status && idxValue){
			var task = {};
			task.nodeCaseIdx = idx;
			task.parentIdx = parentIdx;
			task.isLastLevel = isLeaf;
			task.status = status;
			
			if(status == NODE_STATUS_COMPLETE ){
				MyExt.Msg.alert("不能编辑【已完成】的节点！");
				return;			
			}
			if(status == NODE_STATUS_STOP ){
				MyExt.Msg.alert("不能编辑【已终止】的节点！");
				return;			
			}
			
			workPlanIDX = idxValue;
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
			var task = projectNew.tree.getSelected();
			if(Ext.isEmpty(task)) {
				MyExt.Msg.alert("请选择一条任务！");
				return;
			}
			
			if(task.status == NODE_STATUS_COMPLETE ){
				MyExt.Msg.alert("不能编辑【已完成】的节点！");
				return;			
			}
			if(task.status == NODE_STATUS_STOP ){
				MyExt.Msg.alert("不能编辑【已终止】的节点！");
				return;			
			}
		}
		JobProcessNode.idx = task.nodeCaseIdx;
		JobProcessNodeRel.nodeIDX = task.nodeCaseIdx;
		JobProcessNodeRel.parentIDX = task.parentIdx;
		 /// 如果选择的是作业计划，则编制计划详情，否则编制第一级节点
		if(task.NodeType == PROCESSTYPE){  
				TrainWorkPlanEditFormNew.showWin();
		}else{				
			
			if (null != JobProcessNodeRel.parentIDX &&  "ROOT_0" != JobProcessNodeRel.parentIDX) {
				MyExt.Msg.alert("只能编辑一级节点！");
				return;	
	//			NodeAndWorkCardEdit.tabs.unhideTabStripItem("WorkCardEditTab");
	//			RepairProjectSelect.pTrainTypeIdx = workPlanEntity.trainTypeIDX;
	//			RepairProjectSelect.nodeCaseIDX = task.nodeCaseIdx;
	//			WorkCardEdit.nodeCaseIDX = task.nodeCaseIdx;
	//			WorkCardEdit.grid.store.load();
	//			if (task.status == NODE_STATUS_UNSTART)
	//				TrainWork.status = WORKCARD_STATUS_NEW;
	//			else if (task.status == NODE_STATUS_GOING)
	//				TrainWork.status = WORKCARD_STATUS_HANDLING;
	//			TrainWork.nodeCaseIDX = task.nodeCaseIdx;
	//			TrainWork.trainTypeIDX = workPlanEntity.trainTypeIDX;				
			} else {	
				NodeAndWorkCardEdit.showEditWin();
				NodeAndWorkCardEdit.tabs.hideTabStripItem("WorkCardEditTab");
			}			
			NodeAndWorkCardEdit.tabs.unhideTabStripItem("NodeExtConfigTab");
			JobNodeExtConfig.loadFn(task.nodeCaseIdx);
			JobProcessNodeTreeWin.processName = workPlanEntity.processName;
			JobProcessNodeTreeWin.workPlanIDX = workPlanIDX;
			JobProcessNodeTreeWin.nodeIDX = task.nodeCaseIdx;
		}
	}
	
	 // 调整节点层级关系 点击返回事件
	ParentNodeTreeWin.returnFn = function(nodeIDX, changeNode){	
		var cfg = {
	        url: ctx + '/jobProcessNodeNew!changeNode.action',
	        params: {id: nodeIDX, changeNodeId: changeNode.id},
	        success: function(response, options){
	        	if(processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            WorkPlanGantt.loadFn("expanded", nodeIDX);
		            ParentNodeTreeWin.win.hide();
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
	        }
	    };
	    Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
	        if(btn != 'yes')    return;
	        processTips = new Ext.LoadMask(ParentNodeTreeWin.win.getEl(),{
				msg: "正在处理你的操作，请稍后...",
				removeMask:true
			});
			processTips.show();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });
	}
	
	 // 调整节点层级关系
	WorkPlanGantt.changeNode = function() {
		var task = projectNew.tree.getSelected();
		if(Ext.isEmpty(task)) {
			MyExt.Msg.alert("请选择一条任务！");
			return;
		}
		if(task.NodeType == PROCESSTYPE){
			MyExt.Msg.alert("请选择非流程任务的节点！");
			return;			
		}
		if(task.status != NODE_STATUS_UNSTART){
			MyExt.Msg.alert("请选择【未启动】状态的节点！");
			return;			
		}
		ParentNodeTreeWin.processName = workPlanEntity.processName;
		ParentNodeTreeWin.workPlanIDX = workPlanIDX;
		ParentNodeTreeWin.nodeIDX = task.nodeCaseIdx;
		ParentNodeTreeWin.parentNodeIDX = task.parentIdx;
		if (task.parentIdx == null) {
			ParentNodeTreeWin.selectNodeModel = 'exceptRoot';
		} else {
			ParentNodeTreeWin.selectNodeModel = 'all';
		}
		ParentNodeTreeWin.win.show();
		ParentNodeTreeWin.tree.root.reload();
	}
	// 删除
	WorkPlanGantt.deleteNode = function() {
		var task = projectNew.tree.getSelected();
		if(Ext.isEmpty(task)) {
			MyExt.Msg.alert("请选择一条任务！");
			return;
		}
		if(task.NodeType == PROCESSTYPE){
			MyExt.Msg.alert("请选择非流程任务的节点！");
			return;			
		}
		if(task.status != NODE_STATUS_UNSTART){
			MyExt.Msg.alert("请选择【未启动】状态的节点！");
			return;			
		}
		var cfg = {
	        url: ctx + "/jobProcessNodeNew!deleteNode.action", 
			params: {id: task.nodeCaseIdx},
	        timeout: 600000,
	        success: function(response, options){
	        	if(processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                WorkPlanGantt.loadFn("expanded");
	                TrainWorkPlanEdit.store.load();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){
	        	if(processTips) hidetip();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Msg.confirm("提示  ", "此操作不能恢复，确定删除此节点及其子节点及所有关联数据？  ", function(btn){
	        if(btn != 'yes')    return;
	        showtip();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });
	}
	
	// 启动生产计划
	WorkPlanGantt.startPlan = function() {
		if (workPlanEntity.workPlanStatus == WORKPLAN_STATUS_NULLIFY) {
			MyExt.Msg.alert("此作业计划已终止！");
			return;
		}
		if (workPlanEntity.workPlanStatus != WORKPLAN_STATUS_NEW) {
			MyExt.Msg.alert("此作业计划已启动！");
			return;
		}		
		var cfg = {
	        url: ctx + "/trainWorkPlan!startPlan.action", 
			params: {id: workPlanIDX},
	        timeout: 600000,
	        success: function(response, options){
	        	if(processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                WorkPlanGantt.loadFn();
	                Ext.getCmp("startNodeButton").setVisible(true);
	                Ext.getCmp("startPlanButton").setVisible(false);
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){
	        	if(processTips) hidetip();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Msg.confirm("提示  ", "此操作不能恢复，确定启动生产？  ", function(btn){
	        if(btn != 'yes')    return;
	        showtip();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });
	}
	
	WorkPlanGantt.startNode = function() {
		var task = projectNew.tree.getSelected();
		if(Ext.isEmpty(task)) {
			MyExt.Msg.alert("请选择一条任务！");
			return;
		}
		if(task.NodeType == PROCESSTYPE){
			MyExt.Msg.alert("请选择非流程任务的节点！");
			return;			
		}	
		if(task.status != NODE_STATUS_UNSTART){
			MyExt.Msg.alert("请选择【未启动】状态的节点！");
			return;			
		}
		if(task.isLastLevel == CONST_INT_IS_LEAF_YES){
			MyExt.Msg.alert("请选择一级【未启动】状态的节点！");
			return;	
		}
		var cfg = {
	        url: ctx + "/jobProcessNodeNew!startNode.action", 
			params: {id: task.nodeCaseIdx},
	        timeout: 600000,
	        success: function(response, options){
	        	if(processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                WorkPlanGantt.loadFn("expanded", task.nodeCaseIdx);
	                TrainWorkPlanEdit.store.load();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){
	        	if(processTips) hidetip();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Msg.confirm("提示  ", "此操作不能恢复，确定启动此节点任务？  ", function(btn){
	        if(btn != 'yes')    return;
	        showtip();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });
	}
	
	WorkPlanGantt.terminatePlan = function() {	
		if (workPlanEntity.workPlanStatus == WORKPLAN_STATUS_NULLIFY) {
			MyExt.Msg.alert("此作业计划已终止！");
			return;
		}
		var cfg = {
	        url: ctx + "/trainWorkPlan!updateForTerminatePlan.action", 
			params: {id: workPlanIDX},
	        timeout: 600000,
	        success: function(response, options){
	        	if(processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                TrainWorkPlanWinNew.win.hide();
	                self.location.reload();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){
	        	if(processTips) hidetip();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Msg.confirm("提示  ", "此操作不能恢复，确定终止此作业计划？  ", function(btn){
	        if(btn != 'yes')    return;
	        showtip();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });    	
	}
	
	
   //  定义命名空间
	Ext.namespace('TrainWorkPlanWinNew');                     
	TrainWorkPlanWinNew.fieldWidth = 120;
	TrainWorkPlanWinNew.labelWidth = 80;
	TrainWorkPlanWinNew.infoForm = new Ext.form.FormPanel({
		labelWidth:TrainWorkPlanWinNew.labelWidth, border: false,
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
	TrainWorkPlanWinNew.ganttPanel = new Ext.Panel({
		border:false, layout:'fit',
		items:[{
			tbar:[{
	        	text: '显示方式：只显示任务树', id:'dateviewBtnNew',
	       		menu: [{
	            	text: '只显示任务树', handler: function(){ 
				        projectNew.gantt.parent.set('visible', false);    
				        projectNew.tree.set('verticalScrollPolicy', 'auto');						        
				        projectNew.tree.parent.set('visible', true);
				        Ext.getCmp('dateviewBtnNew').hide();
				        Ext.getCmp('dateviewBtnNew').setText('显示方式：'+this.text);
	                }
	        	}, {
	            	text: '只显示条形图', handler: function(){ 
	                    projectNew.tree.parent.set('visible', false);        
				        projectNew.gantt.parent.set('visible', true);    
				        projectNew.tree.set('verticalScrollPolicy', 'off');
				        Ext.getCmp('dateviewBtnNew').show();
				        Ext.getCmp('dateviewBtnNew').setText('显示方式：'+this.text);
	                }
	        	}, {
	            	text: '全部显示', handler: function(){ 
				        projectNew.gantt.parent.set('visible', true);  						        
				        projectNew.tree.parent.set('visible', true);
				        Ext.getCmp('dateviewBtnNew').show();
				        Ext.getCmp('dateviewBtnNew').setText('显示方式：'+this.text);
	                }
	        	}]
			}, {
	            text: '日期 ：天/时', id:'dateviewBtnNew1', hidden:true,
	            menu: [{
	        		text: '年/季', handler: function(){ 
	                    projectNew.gantt.set('dateView', 'year-quarter');
	                    Ext.getCmp('dateviewBtnNew1').setText('日期 : '+this.text);
	                }
	        	}, {
	            	text: '年/月', handler: function(){ 
	                    projectNew.gantt.set('dateView', 'year-month');
	                    Ext.getCmp('dateviewBtnNew1').setText('日期 : '+this.text);
	                }
	        	}, {
	            	text: '年/天', handler: function(){ 
	                    projectNew.gantt.set('dateView', 'year-day');
	                    Ext.getCmp('dateviewBtnNew1').setText('日期 : '+this.text);
	                }
	        	}, {
	            	text: '月/天', handler: function(){ 
	                    projectNew.gantt.set('dateView', 'month-day');
	                    Ext.getCmp('dateviewBtnNew1').setText('日期 : '+this.text);
	                }
	        	}, {
	            	text: '周/天', handler: function(){ 
	                    projectNew.gantt.set('dateView', 'week-day');
	                    Ext.getCmp('dateviewBtnNew').setText('日期 : '+this.text);
	            	}
	        	}, {
	            	text: '天/时', handler: function(){ 
	                    projectNew.gantt.set('dateView', 'day-hour');
	                    Ext.getCmp('dateviewBtnNew1').setText('日期 : '+this.text);
	            	}
	        	}]
	//		},"-",{
	//		iconCls:"configIcon",
	//    	text:"作业计划基本信息",
	//    	handler: function(){    		
	//    		TrainWorkPlanEditForm.showWin();
	//    	}
	    },"-",{
	//    	id: 'startPlanButton',
	//		iconCls:"beginIcon",
	//    	text:"启动生产",
	//    	hidden: true,
	//    	handler: function(){
	//    		WorkPlanGantt.startPlan();
	//    	}
	//    },{
	    	id: 'startNodeButton',
			iconCls:"beginIcon",
	    	text:"启动",
	    	handler: function(){
	    		WorkPlanGantt.startNode();
	    	}
	    },"-",{
	    	id: 'deletePlanButton',
			iconCls:"deleteIcon",
	    	text:"终止计划",
	    	handler: function(){
	    		WorkPlanGantt.terminatePlan();
	    	}
	    },"-",{
	    	id: 'andNodeButton',
			iconCls:"addIcon",
	    	text:"新增",
	    	handler: function(){
	    		WorkPlanGantt.addNode();
	    	}
	    },"-",{
	    	id: 'editButton',
			iconCls:"editIcon",
	    	text:"编辑",
	    	handler: function(){
	    		WorkPlanGantt.editNode(null,null,null,null,null,null);
	    	}
//	    },"-",{
//	    	id: 'applicationButton',
//			iconCls:"application_goIcon",
//	    	text:"调整节点层级关系",
//	    	handler: function(){
//	    		WorkPlanGantt.changeNode();
//	    	}
	    },"-",{
	    	id: 'deleteNodeButton',
			iconCls:"deleteIcon",
	    	text:"删除",
	    	handler: function(){
	    		WorkPlanGantt.deleteNode();
	    	}
	//    },"-",{
	//		iconCls:"chart_organisationIcon",
	//    	text:"流水线排程",
	//    	handler: function(){
	//    		WorkPlanGantt.updateWorkCardForDispatchByRepairLine();
	//    	}
	//    },"-",{
	//		iconCls:"editIcon",
	//    	text:"调整实际时间",
	//    	handler: function(){
	//    		WorkPlanGantt.updateRealTime();
	//    	}
	    },"-",{
	    	iconCls:"refreshIcon", text:"刷新", handler: function(){WorkPlanGantt.loadFn(); }
	    },"-",{
	    	id: 'closeWorkPlanWinButton', text: "关闭", iconCls: "closeIcon", 
	    	handler: function(){
	    		// Modefied by hetao at 2015-09-17 15:00 '【机车检修台位占用情况】点击进入计划编辑页面时，“关闭”按钮不起作用
				if (typeof VTrainWorkPlan != 'undefined') {
		    		VTrainWorkPlan.clearFn();
					VTrainWorkPlan.store.load();
				}
	    		TrainWorkPlanWinNew.win.hide(); 
	    	}
	    }],
			html:[
			'<div id="viewNew1" style="height:100%;width:100%;"></div>'
			].join('')
		}]
	});
	TrainWorkPlanWinNew.panel = new Ext.Panel({
	    layout: "border", 
	    defaults: {
	    	border: false, layout: "fit"
	    },
	    items: [{
	        region: 'north', collapsible:true, collapsed: false,  height: 70, 
	        items:[TrainWorkPlanWinNew.infoForm], frame: true, title: "检修基本信息"
	    },{
	        id:"gantt", region : 'center', items: TrainWorkPlanWinNew.ganttPanel
	    }]
	});
	TrainWorkPlanWinNew.win = new Ext.Window({
	    title:"车辆检修作业计划编辑", 
	    closeAction:"hide", maximized : true, layout: 'fit',
	    items:TrainWorkPlanWinNew.panel,
		listeners: {
			beforeshow: function() {
				// 加载甘特图数据
				WorkPlanGantt.loadFn();
			},
			render: function() {
				setTimeout(WorkPlanGantt.initFn, 50);
			}
		}
	});
	
	TrainWorkPlanWinNew.showWin = function(data) {
		var dataArray = data.split(",");
		var form = TrainWorkPlanWinNew.infoForm.getForm();
		form.reset();
		form.findField("trainTypeAndNo").setValue(dataArray[1]);
		form.findField("xcxc").setValue(dataArray[2]);
		form.findField("planBeginTime").setValue(dataArray[3]);
		form.findField("planEndTime").setValue(dataArray[4]);	
		TrainWorkPlanGantt.workPlanIDX = dataArray[0];
		workPlanIDX = TrainWorkPlanGantt.workPlanIDX;
		if (!Ext.isEmpty(TrainWorkPlanGantt.workPlanIDX)) {
			var cfg = {
		        scope: this, url: ctx + '/trainWorkPlan!getEntityByIDX.action',
		        params: {workPlanIDX: TrainWorkPlanGantt.workPlanIDX},
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.entity != null) {		            	
		                workPlanEntity = result.entity;
		                TrainWorkPlanWinNew.win.show();
		                switch(workPlanEntity.workPlanStatus){
							case WORKPLAN_STATUS_NEW:
								 Ext.getCmp("startNodeButton").setVisible(true);
								 break;
							case WORKPLAN_STATUS_HANDLING:
								 Ext.getCmp("startNodeButton").setVisible(true);
								 break;
							case WORKPLAN_STATUS_HANDLED:
								 Ext.getCmp("startNodeButton").setVisible(false);
								 break;
						    case WORKPLAN_STATUS_NULLIFY:
								 Ext.getCmp("startNodeButton").setVisible(false);
								 break;
							default:
								
								 Ext.getCmp("startNodeButton").setVisible(false);							 
								 break;
						}					
		                
		            }
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
	}
});