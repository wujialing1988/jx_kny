Ext.onReady(function(){
	Ext.namespace('LeafNodeAndWorkCardSearch'); 
	LeafNodeAndWorkCardSearch.processTips = null;
	/*
	 * 显示处理遮罩
	 */
	LeafNodeAndWorkCardSearch.showtip = function(win,msg){
		LeafNodeAndWorkCardSearch.processTips = new Ext.LoadMask(win.getEl(),{
			msg: msg || "正在处理你的操作，请稍后...",
			removeMask:true
		});
		LeafNodeAndWorkCardSearch.processTips.show();
	}
	/*
	 * 隐藏处理遮罩
	 */
//	
//	LeafNodeAndWorkCardSearch.tabs = new Ext.TabPanel({
//	    activeTab: 0,
//	    frame:true,
//	    items:[{
//	            title: "流程节点", border: false, layout: "border", buttonAlign : "center",
//	            items: [{
//		            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
//		            height: 200, bodyBorder: false,
//		            items:[JobProcessLeafNodeSearch.form], frame: true
//		        },{
//		            region : 'center', layout : 'fit', bodyBorder: false, items : [ JobProcessNodeRelSearch.grid ]
//		        }],
//		        buttons: [{
//				        text: "关闭", iconCls: "closeIcon", handler: function(){ LeafNodeAndWorkCardSearch.win.hide(); }
//				    }]
//	        },{
//	            id: "WorkCardEditTabSearch", title: "作业工单", border: false, layout: "fit", 
//	            items:[WorkCardEditSearch.grid]
//	        },{
//	            id: "NodeExtConfigTabSearch", title: "扩展配置", border: false, layout: "fit", 
//	            items:[JobNodeExtConfigSearch.form]
//	        }]
//	});
//	LeafNodeAndWorkCardSearch.win = new Ext.Window({
//	    title:"节点查看", width: 800, maximized : true, layout: 'fit',
//	    plain:true, closeAction:"hide", buttonAlign:'center',  
//	    items:LeafNodeAndWorkCardSearch.tabs
//	});
	
	LeafNodeAndWorkCardSearch.showEditWin = function() {
		var nodeForm = JobProcessLeafNodeSearch.form.getForm();
		nodeForm.reset();
		nodeForm.findField("workStationBelongTeam").clearValue();
		nodeForm.findField("workStationBelongTeamName").setValue("");
		var cfg = {
	        scope: this, url: ctx + '/jobProcessNode!getEntityByIDX.action',
	        params: {nodeIDX: JobProcessLeafNodeSearch.idx},
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.entity != null) {
	            	var record = new Ext.data.Record();
	                var entity = result.entity;
	                for(var i in entity){
	                	if(i == 'planBeginTime'){
	                		record.set(i, new Date(entity[i]));
	                		JobProcessLeafNodeSearch.planBeginTime = new Date(entity[i]).format('Y-m-d H:i');
	                		continue;	                    		
	                	}
	                	if(i == 'planEndTime'){
	                		record.set(i, new Date(entity[i]));
	                		continue;	                    		
	                	}
	                	record.set(i, entity[i]);
	                }           	
	            	nodeForm.loadRecord(record);
	                var hours = (entity.ratedWorkMinutes / 60).toString().split(".")[0];
					var minutes = entity.ratedWorkMinutes - hours * 60;
					nodeForm.findField("workHours").setValue(hours);
					nodeForm.findField("workMinutes").setValue(minutes);
					
					nodeForm.findField("workStationBelongTeam").setDisplayValue(entity.workStationBelongTeam, entity.workStationBelongTeamName);
					nodeForm.findField("workStationBelongTeamName").setValue(entity.workStationBelongTeamName);
					nodeForm.findField("workStationName").setValue(entity.workStationName);
					JobProcessLeafNodeSearch.nodeIDX = entity.nodeIDX;
					nodeForm.findField("workStationName").nodeIDX = JobProcessLeafNodeSearch.nodeIDX;
					nodeForm.findField("workCalendarIDX").clearValue();
					if (!Ext.isEmpty(entity.workCalendarIDX)) {			    
		                var cfg = {
					        scope: this, url: ctx + '/workCalendarInfo!getWorkCalendarInfo.action',
		        			params: {infoIdx: entity.workCalendarIDX},
					        success: function(response, options){
					            var result = Ext.util.JSON.decode(response.responseText);
					            if (result.defInfo != null) {
					                nodeForm.findField("workCalendarIDX").setDisplayValue(result.defInfo.idx, result.defInfo.calendarName);
					            }
					        }
					    };
		    			Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
					}
					/*
					if (entity.isLeaf == CONST_INT_IS_LEAF_NO) {
						nodeForm.findField("workStationBelongTeam").disable();
						nodeForm.findField("workStationName").disable();
					} else if (entity.status == NODE_STATUS_UNSTART) {
						nodeForm.findField("workStationBelongTeam").enable();
						nodeForm.findField("workStationName").enable();
					}*/
					nodeForm.findField("workStationBelongTeam").disable();
					nodeForm.findField("workStationName").disable();
	            }
	        }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
//		TrainWorkPlanLeafNodeWin.centerPanel.items.items[0].setWidth(0);
		TrainWorkPlanLeafNodeWin.win.show();
  
	}
	
	// 查看节点信息	
	LeafNodeAndWorkCardSearch.showNode = function(data,idx,parentIDX,isLeaf,status,workPlanIDX) {
		var task = {};		
		task.nodeCaseIdx = idx;
		task.parentIDX = parentIDX;
		task.isLastLevel = isLeaf;
		task.status = status;
		var dataArray = data.split(",");
		var workPlanIDX = dataArray[0];
		if (!Ext.isEmpty(workPlanIDX)) {
			var cfg = {
		        scope: this, url: ctx + '/trainWorkPlan!getEntityByIDX.action',
		        params: {workPlanIDX: workPlanIDX},
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.entity != null) {		            	
		                workPlanEntity = result.entity;
		            }
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
		JobProcessLeafNodeSearch.idx = task.nodeCaseIdx;
//		JobProcessNodeRelSearch.nodeIDX = task.nodeCaseIdx;
//		JobProcessNodeRelSearch.parentIDX = task.parentIdx;
		var form = TrainWorkPlanLeafNodeWin.infoForm.getForm();		
		form.reset();
		form.findField("trainTypeAndNo").setValue(dataArray[1]);
		form.findField("xcxc").setValue(dataArray[2]);
		form.findField("planBeginTime").setValue(dataArray[3]);
		form.findField("planEndTime").setValue(dataArray[4]);
		TrainWorkPlanLeafNodeWin.centerPanel.items.items[0].setWidth(0);
		LeafNodeAndWorkCardSearch.showEditWin();
//			LeafNodeAndWorkCardSearch.tabs.unhideTabStripItem("WorkCardEditTabSearch");
		WorkCardEditSearch.nodeCaseIDX = task.nodeCaseIdx;
		LeafNodeListGrid.nodeIDX = task.nodeCaseIdx;
		LeafNodeListGrid.grid.store.load();
		WorkCardEditSearch.grid.store.load();
		if (task.status == NODE_STATUS_UNSTART)
			TrainWorkSearch.status = WORKCARD_STATUS_NEW;
		else if (task.status == NODE_STATUS_GOING)
			TrainWorkSearch.status = WORKCARD_STATUS_HANDLING;
		TrainWorkSearch.nodeCaseIDX = task.nodeCaseIdx;
		TrainWorkSearch.trainTypeIDX = workPlanEntity.trainTypeIDX;				
		NodeApplyDelayList.nodeIDX = task.nodeCaseIdx;
		NodeApplyDelayList.gridSearch.store.load();
//		LeafNodeAndWorkCardSearch.tabs.unhideTabStripItem("NodeExtConfigTabSearch");
		JobNodeExtConfigSearch.loadFn(task.nodeCaseIdx);
		JobProcessNodeTreeWin.processName = workPlanEntity.processName;
		JobProcessNodeTreeWin.workPlanIDX = workPlanIDX;
		JobProcessNodeTreeWin.nodeIDX = task.nodeCaseIdx;
		
		
		var valueArray = [true,true,true,true,false,false,false];
		var trainWorkPlanLeafNodeWin = setTabPanelIsHidden(TrainWorkPlanLeafNodeWin,valueArray);
		TrainWorkPlanLeafNodeWin = trainWorkPlanLeafNodeWin;
		TrainWorkPlanLeafNodeWin.tabPanel.activate(4);
	}
});