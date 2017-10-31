/**
 * 机车检修作业计划 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){	
	Ext.namespace('TrainWorkPlanForEditLeafNode'); 	
	TrainWorkPlanForEditLeafNode.processTips = null;
	TrainWorkPlanForEditLeafNode.item = {};
	/*
	 * 显示处理遮罩
	 */
	TrainWorkPlanForEditLeafNode.showtip = function(win,msg){
		TrainWorkPlanForEditLeafNode.processTips = new Ext.LoadMask(win.getEl()||Ext.getBody(),{
			msg: msg || "正在处理你的操作，请稍后...",
			removeMask:true
		});
		TrainWorkPlanForEditLeafNode.processTips.show();
	}
	/*
	 * 隐藏处理遮罩
	 */
	TrainWorkPlanForEditLeafNode.hidetip = function(){
		TrainWorkPlanForEditLeafNode.processTips.hide();
	}

  	// 显示编辑节点，作业工单编辑窗口
	TrainWorkPlanForEditLeafNode.showEditWin = function() {
		var nodeFormEditWin = LeafNodeAndWorkCardEditWin.form.getForm();
		nodeFormEditWin.reset();
		nodeFormEditWin.findField("workStationBelongTeam").clearValue();
		nodeFormEditWin.findField("workStationBelongTeamName").setValue("");
		var cfg = {
	        scope: this, url: ctx + '/jobProcessNode!getEntityByIDX.action',
	        params: {nodeIDX: LeafNodeAndWorkCardEditWin.idx },
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.entity != null) {
	            	var record = new Ext.data.Record();
	                var entity = result.entity;
	                for(var i in entity){
	                	if(i == 'planBeginTime'){
	                		record.set(i, new Date(entity[i]));
	                		TrainWorkPlanForEditLeafNode.planBeginTime = new Date(entity[i]).format('Y-m-d H:i');
	                		continue;	                    		
	                	}
	                	if(i == 'planEndTime'){
	                		record.set(i, new Date(entity[i]));
	                		continue;	                    		
	                	}
	                	record.set(i, entity[i]);
	                }           	
	            	nodeFormEditWin.loadRecord(record);
            	  	if(entity.editStatus == EDIT_STATUS_WAIT){
            			nodeFormEditWin.findField("planBeginTime").disable();
						nodeFormEditWin.findField("planEndTime").disable();
	            	}else{
	            		nodeFormEditWin.findField("planBeginTime").enable();
						nodeFormEditWin.findField("planEndTime").enable();
	            	}
	            	nodeFormEditWin.findField("reason").hide();
					nodeFormEditWin.findField("workStationBelongTeam").setDisplayValue(entity.workStationBelongTeam, entity.workStationBelongTeamName);
					nodeFormEditWin.findField("workStationBelongTeamName").setValue(entity.workStationBelongTeamName);
					nodeFormEditWin.findField("workStationName").setValue(entity.workStationName);
					LeafNodeAndWorkCardEditWin.nodeIDX = entity.nodeIDX;
					nodeFormEditWin.findField("workStationName").nodeIDX = LeafNodeAndWorkCardEditWin.nodeIDX;
	//				nodeFormEditWin.findField("workCalendarIDX").clearValue();
					if (entity.isLeaf == CONST_INT_IS_LEAF_NO) {
						nodeFormEditWin.findField("workStationBelongTeam").disable();
						nodeFormEditWin.findField("workStationName").disable();
					} else if (entity.status == NODE_STATUS_UNSTART) {
						nodeFormEditWin.findField("workStationBelongTeam").enable();
						nodeFormEditWin.findField("workStationName").enable();
					}else if(entity.status == NODE_STATUS_GOING){
						nodeFormEditWin.findField("planBeginTime").disable();
					}
	            }
	        }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    TrainWorkPlanLeafNodeWin.centerPanel.items.items[0].setWidth(0);
	    TrainWorkPlanLeafNodeWin.win.show();
	    
	    var valueArray = [true,false,false,false,true,true,true];
		var trainWorkPlanLeafNodeWin = setTabPanelIsHidden(TrainWorkPlanLeafNodeWin,valueArray);
		TrainWorkPlanLeafNodeWin = trainWorkPlanLeafNodeWin;
		TrainWorkPlanLeafNodeWin.tabPanel.activate(1);
	}
	
	// 编辑子节点
	TrainWorkPlanForEditLeafNode.editNode = function(data,idx,parentIdx,isLeaf,planEndTime,status,valueIDX) {		
		if(Ext.isEmpty(idx)) {
			MyExt.Msg.alert("请选择一条任务！");
		}		
		if(status == NODE_STATUS_COMPLETE ){
			MyExt.Msg.alert("不能编辑【已完成】的节点！");
			return;			
		}
		if(status == NODE_STATUS_STOP ){
			MyExt.Msg.alert("不能编辑【已终止】的节点！");
			return;			
		}
		workPlanIDX = valueIDX; // 保存作业工单需带入作业工单
		if (!Ext.isEmpty(workPlanIDX)) {
			var cfg = {
		        scope: this, url: ctx + '/trainWorkPlan!getEntityByIDX.action',
		        params: {workPlanIDX: workPlanIDX},
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.entity != null) {		            	
		                workPlanEntity = result.entity;
		                JobProcessNodeTreeWin.processName = workPlanEntity.processName;
		            }
		        }
		    };
		    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
		var dataArray = data.split(",");
		var form = TrainWorkPlanLeafNodeWin.infoForm.getForm();		
		form.reset();
		form.findField("trainTypeAndNo").setValue(dataArray[1]);
		form.findField("xcxc").setValue(dataArray[2]);
		form.findField("planBeginTime").setValue(dataArray[3]);
		form.findField("planEndTime").setValue(dataArray[4]);
		LeafNodeAndWorkCardEditWin.parentIDX = parentIdx; 
		LeafNodeAndWorkCardEditWin.idx = idx;
		if (isLeaf == CONST_INT_IS_LEAF_YES) {
			TrainWorkPlanForEditLeafNode.showEditWin();
//			TrainWorkPlanForEditLeafNode.tabs.unhideTabStripItem("WorkCardEditTab");
			RepairProjectSelect.pTrainTypeIdx = workPlanEntity.trainTypeIDX;
			RepairProjectSelect.nodeCaseIDX = idx;
			WorkCardEditWin.nodeCaseIDX = idx;
			NodeApplyDelayList.nodeIDX = idx;
			WorkCardEditWin.grid.store.load();
			NodeApplyDelayList.grid.store.load();
			if (status == NODE_STATUS_UNSTART)
				TrainWork.status = WORKCARD_STATUS_NEW;
			else if (status == NODE_STATUS_GOING)
				TrainWork.status = WORKCARD_STATUS_HANDLING;
			TrainWork.nodeCaseIDX = idx;
			TrainWork.trainTypeIDX = workPlanEntity.trainTypeIDX;	
		} else if (isLeaf == CONST_INT_IS_LEAF_NO) {			
			MyExt.Msg.alert("只能编辑子级节点！");
			return;	
		}			
//		TrainWorkPlanForEditLeafNode.tabs.unhideTabStripItem("NodeExtConfigTab");
		JobNodeExtConfig.loadFn(idx);
		LeafNodeAndWorkCardEditWin.parentPlanEndTime = planEndTime;
		
		
		JobProcessNodeTreeWin.workPlanIDX = workPlanIDX;
		JobProcessNodeTreeWin.nodeIDX = idx;
	}
	
	// 调整节点开工第几天
	TrainWorkPlanForEditLeafNode.changeNode = function(item) {
		var data = {};
		data.idx = (item.id).substring(0,(item.id).length -1);
		data.planBeginTime = item.start;
		data.planEndTime = item.end;
		var cfg = {
	        url: ctx + "/jobProcessNodeNew!updateLeafNodeTime.action", jsonData: data,
	        timeout: 600000,
	        success: function(response, options){
				if(TrainWorkPlanForEditLeafNode.processTips) TrainWorkPlanForEditLeafNode.hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	            	LeafNodeListGrid.grid.store.reload();
	            	TrainWorkPlanForLeafNode.store.reload(); // 重新加载vis节点
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){	
	        	if(TrainWorkPlanForEditLeafNode.processTips) TrainWorkPlanForEditLeafNode.hidetip();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	}
	
	// 申请调整节点
	TrainWorkPlanForEditLeafNode.applyChangeNode = function() {
		var applyForm = TrainWorkPlanForEditLeafNode.applyForm.getForm();	
		if (!applyForm.isValid()) {
			MyExt.Msg.alert("请填写申请原因！");
			return;    
		}
		var data = applyForm.getValues();
		var cfg = {
	        url: ctx + "/jobProcessNodeUpdateApply!updateLeafNodeTimeApply.action", jsonData: data,
	        timeout: 600000,
	        success: function(response, options){
				if(TrainWorkPlanForEditLeafNode.processTips) TrainWorkPlanForEditLeafNode.hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                TrainWorkPlanForEditLeafNode.applyWin.hide();
	            	TrainWorkPlanForLeafNode.store.reload(); // 重新加载vis节点
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){	
	        	if(TrainWorkPlanForEditLeafNode.processTips) TrainWorkPlanForEditLeafNode.hidetip();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    TrainWorkPlanForEditLeafNode.showtip(TrainWorkPlanForEditLeafNode.applyWin);
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	}
	
	
	TrainWorkPlanForEditLeafNode.applyForm = new Ext.form.FormPanel({
		xtype: 'form',
					layout: 'column',
					frame: true,
					defaults:{
						columnWidth: .5,
							layout: 'form', 
							defaultType: 'textfield'
					},
					items: [{	
						columnWidth: 1,				
						items: [{
							  fieldLabel: '节点名称', name: 'nodeName', width: 240 ,readOnly:true
							},{
								xtype: 'hidden', name: 'nodeIDX'
							},{
								xtype: 'hidden', name: 'idx'
							}]
					},{
						defaults: { 
							 xtype: 'my97date',format: "Y-m-d H:i",
							 width: 140 
						},
						items: [{
								fieldLabel: '申请前开始时间', name: 'planBeginTime',readOnly:true
							},{
								fieldLabel: '申请前结束时间', name: 'planEndTime',readOnly:true
							}]
					},{
						defaults: { 
							 xtype: 'my97date',format: "Y-m-d H:i",
							 width: 140 
						},
						items: [{
								
								fieldLabel: '申请调整开始时间', name: 'newPlanBeginTime',readOnly:true
							},{
								fieldLabel: '申请调整结束时间', name: 'newPlanEndTime',readOnly:true							
							}]
					},{
						columnWidth: 1,
						items: [{							
								fieldLabel: '申请原因', xtype: 'textarea',name: 'reason', allowBlank:false, width: 360, hight: 100
							}]
					 }],
					buttonAlign: 'center',
					buttons: [{
            			text: "保存", iconCls: "saveIcon", scope: this, handler: function(){ TrainWorkPlanForEditLeafNode.applyChangeNode(); }
        			}, {
           				text: "取消", iconCls: "closeIcon", scope: this, handler: function(){ TrainWorkPlanForEditLeafNode.applyWin.hide(); }
        			}]
	});
	
	// 定义节点申请调整窗口
	TrainWorkPlanForEditLeafNode.applyWin  = new Ext.Window({
			height: 260,
			width: 600,
			buttonAlign: 'center',
			closeAction: 'hide',
			layout: 'fit',
			title: "时间调整申请",
			items: [TrainWorkPlanForEditLeafNode.applyForm],
			listeners: {
				beforeshow:function(window){
					var item = TrainWorkPlanForEditLeafNode.item;	
//					var nodeApplyEntity = {};
					if(item){
						var nodeIDX = (item.id).substring(0,(item.id).length -1)
							if (!Ext.isEmpty(nodeIDX)) {
							var cfg = {
						        scope: this, url: ctx + '/jobProcessNodeUpdateApply!getEntityByNodeIDX.action',
						        params: {nodeIDX: nodeIDX},
						        success: function(response, options){
						            var result = Ext.util.JSON.decode(response.responseText);
						            if (result.entity != null) {		            	
						               var  nodeApplyEntity = result.entity;
						                if(!Ext.isEmpty(nodeApplyEntity)){
											window.find('name', 'idx')[0].setValue(nodeApplyEntity.idx);
											window.find('name', 'reason')[0].setValue(nodeApplyEntity.reason);
										}else{
											window.find('name', 'idx')[0].setValue(null);
										}
						            }
						        }
						    };
						    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
						}						
						window.find('name', 'nodeIDX')[0].setValue(nodeIDX);
						window.find('name', 'nodeName')[0].setValue(item.nodeName);
						window.find('name', 'planBeginTime')[0].setValue(item.planBeginTime);
						window.find('name', 'planEndTime')[0].setValue(item.planEndTime);
						var newPlanBeginTime =  new Date(new Date(item.start).getFullYear(), new Date(item.start).getMonth(), new Date(item.start).getDate(), 
														 new Date(item.planBeginTime).getHours(), new Date(item.planBeginTime).getMinutes());
//						var days = new Date(item.planEndTime).getDate() - new Date(item.planBeginTime).getDate();
//						var newPlanEndTime =  new Date(new Date(item.start).getFullYear(), new Date(item.start).getMonth(), new Date(item.start).getDate()+ days, new Date(item.planEndTime).getHours(), new Date(item.planEndTime).getMinutes()); ;
						var newPlanEndTime =  new Date(new Date(item.end).getFullYear(), new Date(item.end).getMonth(), new Date(item.end).getDate(), 
													   new Date(item.planEndTime).getHours(), new Date(item.planEndTime).getMinutes()); ;
						window.find('name', 'newPlanBeginTime')[0].setValue(newPlanBeginTime);
						window.find('name', 'newPlanEndTime')[0].setValue(newPlanEndTime);						
					}  
					return true;
				}
			}
		});
});