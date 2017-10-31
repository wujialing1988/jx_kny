/**
 * 机车检修作业编辑超链接中的下级节点grid
 */
Ext.onReady(function(){
	Ext.namespace('LeafNodeListGrid'); 
	LeafNodeListGrid.labelWidth = 90;
	LeafNodeListGrid.fieldWidth = 150;
	LeafNodeListGrid.searchParam = {}
	LeafNodeListGrid.nodeIDX = '';
	LeafNodeListGrid.ids = [];
	LeafNodeListGrid.workPlanStatus = '';
	LeafNodeListGrid.calendarName = '';
	LeafNodeListGrid.parentIDX =  '';
	LeafNodeListGrid.status =  '';
	LeafNodeListGrid.workPlanIDX =  '';
	LeafNodeListGrid.processIDX = '';
	LeafNodeListGrid.workPlanStatus = '';
	//初始化状态和对应的中文名
	LeafNodeListGrid.stateName = {"NOTSTARTED":"未启动","RUNNING":"运行","COMPLETED":"完成","TERMINATED":"终止"};
	//初始化状态和对应的中文名
	LeafNodeListGrid.planMode = {"AUTO":"自动","MANUAL":"手动","TIMER":"定时"};
	
	var processTips;
	
	/*
	 * 显示处理遮罩
	 */
	function showtip(msg){
		processTips = new Ext.LoadMask(Ext.getBody(),{
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
	
	//  删除节点
	LeafNodeListGrid.deleteBatchFn = function() {
		var grid = LeafNodeListGrid.grid;
		if(!$yd.isSelectedRecord(grid)) return;
		var ids = $yd.getSelectedIdx(grid);
		
		LeafNodeListGrid.deleteOpFn(ids);
	}
	
	LeafNodeListGrid.deleteFn = function(v) {
		var ids = [];
		ids.push(v);
		LeafNodeListGrid.deleteOpFn(ids);
	}
	

	LeafNodeListGrid.deleteOpFn = function() {
		var grid = LeafNodeListGrid.grid;
		var ids = $yd.getSelectedIdx(grid);	
		//不能选择多条数据启动
		if(ids.length > 1){
			MyExt.Msg.alert("不能同时启动多个节点，请选择一条任务！");
			return;
		}
	
		var idValue = ids;
		var record = LeafNodeListGrid.grid.store.getById(idValue);
		var status = record.get("status");
		
		if(status == PROCESSTYPE){
			MyExt.Msg.alert("请选择非流程任务的节点！");
			return;			
		}
		switch(status){
			case NODE_STATUS_GOING:
				 MyExt.Msg.alert("不能删除【运行中】的作业工单！");
				 return;
				 break;
			case NODE_STATUS_COMPLETE:
				 MyExt.Msg.alert("不能删除【已完工】的作业工单！");
				 return;
				 break;
		    case NODE_STATUS_STOP:
				 MyExt.Msg.alert("不能删除【已终止】的作业工单！");
				 return;
				 break;
			default:							 					 
				 break;
		}	
					
		var cfg = {
	        url: ctx + "/jobProcessNodeNew!deleteNode.action", 
			params: {id: idValue},
	        timeout: 600000,
	        success: function(response, options){
	        	if(processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                LeafNodeListGrid.grid.store.load();
					TrainWorkPlanForLeafNode.store.reload();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){
	        	if(processTips) hidetip();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
	        if(btn != 'yes')    return;
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });
	}
	
	LeafNodeListGrid.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/jobProcessNodeQuery!getAllChildNodeExceptThisList.action',                 
	    page : false,
	    isEdit : false,
	    singleSelect: true,
	    storeAutoLoad:false,
	    tbar: [{
	    	text: "启动" ,iconCls:"beginIcon", handler: function(){
	    		LeafNodeListGrid.startNode();
	    	}
	    },{
	    	text: "删除",iconCls:"deleteIcon", handler: function(){
	    		LeafNodeListGrid.deleteBatchFn();
	    	}
	    },{
	    	iconCls:"addIcon",
	    	text:"新增",
	    	handler: function(){
	    		LeafNodeListGrid.addNode();
	    	}
	    },{
			iconCls:"chart_organisationIcon",
	    	text:"派工",
	    	handler: function(){
	    		LeafNodeListGrid.updateWorkCardForDispatch();
	    	}
	    },{
			iconCls:"refreshIcon",
	    	text:"刷新",
	    	handler: function(){
	    		LeafNodeListGrid.grid.store.reload();
	    	}
	    }],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'名称', dataIndex:'nodeName',width: 130
		},{
			header:'工期', dataIndex:'ratedWorkMinutes',width: 50,
			renderer :function(v, r){
				if(v != null && v != '' && v != 'null'){
					var hours = (v/60).toFixed(0);
					var minutes = (v- hours*60).toFixed(0);
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
		},{
			header:'前置节点', dataIndex:'lastChildNodeNames', hidden:true
		},{
			header:'计划开始时间', dataIndex:'planBeginTime', xtype:'datecolumn',width: 100, format: "Y-m-d H:i"
		},{
			header:'计划完成时间', dataIndex:'planEndTime', xtype:'datecolumn',width: 100, format: "Y-m-d H:i"
		},{
			header:'作业工位IDX', dataIndex:'workStationIDX', hidden:true
		},{
			header:'作业工位', dataIndex:'workStationName',width: 80
		},{
			header:'作业班组', dataIndex:'workStationBelongTeamName',width: 80
		},{
			header:'实际开始时间', dataIndex:'realBeginTime', xtype:'datecolumn', format: "Y-m-d H:i",width: 100
		},{
			header:'实际完成时间', dataIndex:'realEndTime', xtype:'datecolumn', format: "Y-m-d H:i",width: 100
		},{
			header:'作业计划主键', dataIndex:'workPlanIDX', hidden:true
		},{
			header:'父节点idx', dataIndex:'parentIDX', hidden:true
		},{
			header:'是否是子节点', dataIndex:'isLeaf', hidden:true
		},{
			header:'定义节点id', dataIndex:'nodeIDX', hidden:true
		},{
			header:'作业流程主键', dataIndex:'processIDX', hidden:true
		},{
			header:'延期申请状态', dataIndex:'editStatus', hidden:true
		},{
			header:'处理情况', dataIndex:'status',width: 80 ,
			renderer :function(a,b,c,d){
				return LeafNodeListGrid.stateName[a];
			}
		},{
			header:'计划模式', dataIndex:'planMode',width: 40,
			renderer :function(v){
				return LeafNodeListGrid.planMode[v];
			}
		},{
			header:'日历', dataIndex:'workCalendarIDX',width: 120,
			renderer :function(v){
				return WorkCalendarInfo.getCalendarName(v);
			}
		}],
		//不需要双击显示
		toEditFn : function(){}
	});
	LeafNodeListGrid.grid.store.on('beforeload', function() {
		this.baseParams.nodeIDX = LeafNodeListGrid.nodeIDX;
	});
	
	/********* 节点启动********/
	LeafNodeListGrid.startNode = function() {		
		//判断是否选择了数据
		var grid = LeafNodeListGrid.grid;
		if(!$yd.isSelectedRecord(grid)) {
			MyExt.Msg.alert("请选择一条任务！");
			return;
		}
		var ids = $yd.getSelectedIdx(grid);	
		
		//不能选择多条数据启动
		if(ids.length > 1){
			MyExt.Msg.alert("不能同时启动多个节点，请选择一条任务！");
			return;
		}		
		var record = LeafNodeListGrid.grid.store.getById(ids[0]);
		var idx = record.get("idx");
		
//		if(LeafNodeListGrid.workPlanStatus != PLAN_STATUS_HANDLING){
//			MyExt.Msg.alert("请选择【已启动生产】状态的节点！");
//			return;
//		}		
		var status = record.get("status");
		var editStatus = record.get("editStatus");
		if(status != NODE_STATUS_UNSTART){
			MyExt.Msg.alert("请选择【未启动】状态的节点！");
			return;			
		}
		if(editStatus == EDIT_STATUS_WAIT){
			MyExt.Msg.alert("节点正在【延期申请】，请确认后再操作！");
			return;			
		}
		
		var cfg = {
	        url: ctx + "/jobProcessNodeNew!startNode.action", 
			params: {id: idx},
	        timeout: 600000,
	        success: function(response, options){
	        	if(processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                LeafNodeListGrid.grid.store.load();
	                TrainWorkPlanForLeafNode.store.reload();
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
	
	/********* 重新定义点击 派工 确定按钮的操作 ********/ 
	TecnodeUnionStation.submit = function(){
		var grid = TecnodeUnionStation.grid;
		if(!$yd.isSelectedRecord(grid)) return;
		var r = grid.selModel.getSelections();
		var workStationIdx = r[0].get("idx");
		dataParam.workStationIDX = r[0].get("idx");
		dataParam.workStationName = r[0].get("workStationName");
		dataParam.workStationCode = r[0].get("workStationCode");
		dataParam.repairLineIDX = r[0].get("repairLineIdx");
		dataParam.workStationBelongTeam = r[0].get("teamOrgId");
		dataParam.workStationBelongTeamName = r[0].get("teamOrgName");
		dataParam.workStationBelongTeamSeq = r[0].get("teamOrgSeq");
		var cfg = {				
			url: ctx + "/jobProcessNodeNew!dispatchNode.action",
			jsonData: dataParam,
		    //请求成功后的回调函数
		    success: function(response, options){
		        if(processTips)  hidetip();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null && result.success == true) {       //操作成功     
		            alertSuccess();		         
		            TecnodeUnionStation.selectWin.hide();
//		            TecnodeUnionStation.selectWin.buttons[0].setVisible(true);
		            LeafNodeListGrid.grid.store.reload();
		            TrainWorkPlanForLeafNode.store.reload();
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		            TecnodeUnionStation.selectWin.buttons[0].setVisible(true);
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(processTips)  hidetip();
//		    	TecnodeUnionStation.selectWin.buttons[0].setVisible(true);
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		};
		Ext.Msg.confirm("提示  ", "是否对该工艺节点的作业工单派工？  ", function(btn){
	        if(btn != 'yes')   {
	        	return;		        	
	        }
//        	TecnodeUnionStation.selectWin.buttons[0].setVisible(false);
	        showtip();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });    
	}
	
	/********* 派工 ********/
	LeafNodeListGrid.updateWorkCardForDispatch = function(){
	//判断是否选择了数据
		var grid = LeafNodeListGrid.grid;
		if(!$yd.isSelectedRecord(grid)) {
			MyExt.Msg.alert("请选择一条任务！");
			return;
		}
		var ids = $yd.getSelectedIdx(grid);	
		
		//不能选择多条数据启动
		if(ids.length > 1){
			MyExt.Msg.alert("不能同时对多个节点派工，请选择一条任务！");
			return;
		}
		
		var record = LeafNodeListGrid.grid.store.getById(ids[0]);
		var idx = record.get("idx");
		var status = record.get("status");
		nodeIDX = record.get("nodeIDX");
		var workPlanIDX = record.get("workPlanIDX");
					
		var type = 1;//判断是点击根节点还是非根节点或子节点:0根节点1非根节点2子节点
		var parentIDX = idx;
		
		//对子节点排程
		if(record.get("isLeaf") == CONST_INT_IS_LEAF_YES){
			if(status == NODE_STATUS_COMPLETE ){
					MyExt.Msg.alert("不能对【已完成】的节点进行派工！");
					return;			
				}
				if(status == NODE_STATUS_STOP ){
					MyExt.Msg.alert("不能对【已终止】的节点进行派工！");
					return;			
				}
			type = 2;
			dataParam = {};
			dataParam.rdpIDX = workPlanIDX;
			dataParam.nodeCaseIDX = idx;
			dataParam.nodeStatus = status;		
//			cascadingIDX = task.cascadingIDX;
			workStationIDX = record.get("workStationIDX");
			TecnodeUnionStation.grid.store.load();
			TecnodeUnionStation.selectWin.show();
		}
	}
	
	/********* 新增子节点 ********/
	LeafNodeListGrid.addNode = function(){
		var form = LeafNodeAddWin.form.getForm();
		if(LeafNodeListGrid.status == NODE_STATUS_COMPLETE ){
			MyExt.Msg.alert("不能在【已完成】的节点下新增节点！");
			return;			
		}
		if(LeafNodeListGrid.status == NODE_STATUS_STOP ){
			MyExt.Msg.alert("不能在【已终止】的节点下新增节点！");
			return;			
		}
		LeafNodeAddWin.parentIDX = LeafNodeListGrid.parentIDX;
		var cfg = {
	        scope: this, url: ctx + '/jobProcessNodeQuery!hasWorkCardOrActivity.action',
	        params: {nodeIDX: LeafNodeListGrid.parentIDX},
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            var hasWorkCardOrActivity = result.hasWorkCardOrActivity;
	            if ( hasWorkCardOrActivity != null && hasWorkCardOrActivity == true) {		            	
	                MyExt.Msg.alert("此节点已关联有工单，不能新增子节点！");
					return;
	            } else if (hasWorkCardOrActivity != null && hasWorkCardOrActivity == false){
	            	LeafNodeAddWin.workPlanIDX = LeafNodeListGrid.workPlanIDX;
					LeafNodeAddWin.processIDX = LeafNodeListGrid.processIDX;
	    			form.findField('workPlanIDX').setValue(LeafNodeAddWin.workPlanIDX );  
					form.findField("processIDX").setValue(LeafNodeAddWin.processIDX);
					form.findField("parentIDX").setValue(LeafNodeListGrid.parentIDX);
//					form.findField("planEndTime").allowBlank = false;
		    		LeafNodeAddWin.win.show();
//		    		NodeAndWorkCardEdit.tabs.hideTabStripItem("WorkCardEditTab");
//		    		NodeAndWorkCardEdit.tabs.hideTabStripItem("NodeExtConfigTab");
//		    		NodeAndWorkCardEdit.tabs.activate(0);
	            }
	        }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    LeafNodeListGrid.grid.store.reload();
	}
	
	/** 流水线排程 */
	LeafNodeListGrid.updateWorkCardForDispatchByRepairLine = function(){
		//根节点
		var type = 0;
		var parentIDX = LeafNodeListGrid.workPlanIDX;
		var nodeCaseIDX = LeafNodeListGrid.processIDX;
		if(Ext.isEmpty(parentIDX) || Ext.isEmpty(nodeCaseIDX)) {
			MyExt.Msg.alert("请选择一条检修计划！");
			return;
		}

		if(LeafNodeListGrid.workPlanStatus != WORKPLAN_STATUS_NEW){
			MyExt.Msg.alert("请选择【未启动】状态的流程进行批量派工！");
			return;			
		}
		//进行设置检修流水线
		Ext.Ajax.request({
            url:  ctx + "/workCardQuery!hasOnGoingWorkCardByNode.action",
            params:{nodeCaseIDX:nodeCaseIDX, workPlanIDX:workPlanIDX},
            success: function(response, options){              	
                var result = Ext.util.JSON.decode(response.responseText);
                //如已有作业工单在处理，则不能设置检修工位
                if (result.errMsg != null ) {
                	MyExt.TopMsg.failMsg(result.errMsg);
                    return;	
                } else {
                    RepairLineGroupSelect.tecProcessCaseIDX = LeafNodeListGrid.processIDX;
					RepairLineGroupSelect.win.show();
					RepairLineGroupSelect.grid.store.on("beforeload", function(){										
						this.baseParams.parentIDX = parentIDX;
						this.baseParams.type = type;
					});
					RepairLineGroupSelect.grid.store.removeAll();
					RepairLineGroupSelect.grid.store.load();
                }
            },
            failure: function(response, options){	                    
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	            }
   			});
		}

});