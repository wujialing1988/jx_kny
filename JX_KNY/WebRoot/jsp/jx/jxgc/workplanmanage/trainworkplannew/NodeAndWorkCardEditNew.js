Ext.onReady(function(){
Ext.namespace('NodeAndWorkCardEdit'); 
	NodeAndWorkCardEdit.processTips = null;
	/*
	 * 显示处理遮罩
	 */
	NodeAndWorkCardEdit.showtip = function(win,msg){
		NodeAndWorkCardEdit.processTips = new Ext.LoadMask(win.getEl(),{
			msg: msg || "正在处理你的操作，请稍后...",
			removeMask:true
		});
		NodeAndWorkCardEdit.processTips.show();
	}
	/*
	 * 隐藏处理遮罩
	 */
	NodeAndWorkCardEdit.hidetip = function(){
		NodeAndWorkCardEdit.processTips.hide();
	}
	NodeAndWorkCardEdit.reloadParent = function(nodeIdx) {
		WorkPlanGantt.loadFn("expanded", nodeIdx);
	}
	// 保存节点信息
	NodeAndWorkCardEdit.saveFn = function() {
		var nodeForm = JobProcessNode.form.getForm();
		if (!nodeForm.isValid()) return; 
		nodeForm.findField("planBeginTime").enable();
		nodeForm.findField("planEndTime").enable();
		nodeForm.findField("workHours").enable();
		nodeForm.findField("workMinutes").enable();
		var data = nodeForm.getValues();
    	if(data.editStatus == EDIT_STATUS_WAIT){
			nodeForm.findField("planBeginTime").disable();
			nodeForm.findField("planEndTime").disable();
			nodeForm.findField("workHours").disable();
			nodeForm.findField("workMinutes").disable();	
    	}
	    var msg = "前置节点均不影响此节点的计划开始时间,计划开始时间以界面设置为准,";
	    if(data.status == NODE_STATUS_GOING){
			nodeForm.findField("planBeginTime").disable();
			msg = "节点已启动，节点工期将根据节点结束时间重新推算，";
	    }    
	    data.ratedWorkMinutes = nodeForm.findField("workHours").getValue()*60 + nodeForm.findField("workMinutes").getValue();
	    delete data.workHours;
	    delete data.workMinutes;
	    var datas = new Array();
		for (var i = 0; i < JobProcessNodeRel.grid.store.getCount(); i++) {
			var relData = {} ;
			relData = JobProcessNodeRel.grid.store.getAt(i).data;
			if(Ext.isEmpty(relData.preNodeIDX)){
				continue;
			}
			relData.nodeIDX = JobProcessNode.idx;
			datas.push(relData);
		}
		for (var i = 0; i < datas.length; i++) {
			var preNodeIDX = datas[i].preNodeIDX;
			for (var j = (i + 1); j < datas.length; j++) {
				var preNodeIDX1 = datas[j].preNodeIDX;
				if (preNodeIDX == preNodeIDX1) {
					MyExt.Msg.alert("不能重复选择同一节点为前置节点");
					return;
				}
			}
		}
	    var cfg = {
	        url: ctx + "/jobProcessNodeNew!saveOrUpdateFirstNodeAndRel.action", jsonData: data,
	        timeout: 600000,
	        params: {rels: Ext.util.JSON.encode(datas)},
	        success: function(response, options){
				if(NodeAndWorkCardEdit.processTips) NodeAndWorkCardEdit.hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                NodeAndWorkCardEdit.win.hide();
	                 TrainWorkPlanEdit.store.reload();
	                if (data.idx)
	                	NodeAndWorkCardEdit.reloadParent(data.idx);
	                else if (data.parentIDX)
	                	NodeAndWorkCardEdit.reloadParent(data.parentIDX);
	                WorkPlanGantt.loadFn();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){	
	        	if(NodeAndWorkCardEdit.processTips) NodeAndWorkCardEdit.hidetip();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Msg.confirm("提示  ", msg + "确定操作？  ", function(btn){
	        if(btn != 'yes')    return;
	        NodeAndWorkCardEdit.showtip(NodeAndWorkCardEdit.win);
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });
	}
	// 点击保存时判断是申请还是直接提交
	NodeAndWorkCardEdit.saveOrApplyFn = function() {
		var nodeForm = JobProcessNode.form.getForm();
		if (!nodeForm.isValid()) return; 
		nodeForm.findField("planEndTime").enable();
	    var data = nodeForm.getValues();
	    var oldPlanEndTime = new Date(NodeAndWorkCardEdit.planEndTime).format('Y-m-d');
	    // 判断是否延期申请
	    if( oldPlanEndTime < data.planEndTime.substring(0,10)){
	    	JobProcessFirstNodeApplyWin.applyWin.show();
    	}else {
	       NodeAndWorkCardEdit.saveFn();
    	}
	}
	
	NodeAndWorkCardEdit.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
	            title: "流程节点", border: false, layout: "border", buttonAlign : "center",
	            items: [{
		            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
		            height: 200, bodyBorder: false,
		            items:[JobProcessNode.form], frame: true
		        },{
		            region : 'center', layout : 'fit', bodyBorder: false, items : [ JobProcessNodeRel.grid ]
		        }],
		        buttons: [{
			        	id: "nodeSaveBtn", text: "保存", iconCls: "saveIcon", handler: function() {
				        	NodeAndWorkCardEdit.saveOrApplyFn();
				        }
				    }, {
				        text: "关闭", iconCls: "closeIcon", handler: function(){ NodeAndWorkCardEdit.win.hide(); }
				    }]
	        },{
	            id: "WorkCardEditTab", title: "作业工单", border: false, layout: "fit", 
	            items:[WorkCardEdit.grid]
	        },{
	            id: "NodeExtConfigTab", title: "扩展配置", border: false, layout: "fit", 
	            items:[JobNodeExtConfig.form]
	        }]
	});
	NodeAndWorkCardEdit.win = new Ext.Window({
	    title:"节点编辑", width: 800, maximized : true, layout: 'fit',
	    plain:true, closeAction:"hide", buttonAlign:'center',  
	    items:NodeAndWorkCardEdit.tabs
	});
	
	NodeAndWorkCardEdit.showAddWin = function() {
		var nodeForm = JobProcessNode.form.getForm();
		nodeForm.reset();
		JobProcessNodeRel.grid.store.load();
		NodeAndWorkCardEdit.win.show();
		nodeForm.findField("workPlanIDX").setValue(JobProcessNode.workPlanIDX); 
	    nodeForm.findField("processIDX").setValue(JobProcessNode.processIDX);
	    nodeForm.findField("parentIDX").setValue(JobProcessNode.parentIDX);
	    nodeForm.findField("workHours").setValue(0);
	    nodeForm.findField("workMinutes").setValue(0);
	    nodeForm.findField("workStationBelongTeam").clearValue();
		nodeForm.findField("workStationBelongTeamName").setValue("");
		nodeForm.findField("workStationBelongTeam").enable();
		nodeForm.findField("workStationName").enable();
	    JobProcessNode.planBeginTime = null;
	    NodeAndWorkCardEdit.enableNodeForm();
		NodeAndWorkCardEdit.enableNodeRelGrid();
	}
	// 显示编制窗口
	NodeAndWorkCardEdit.showEditWin = function() {
		var nodeForm = JobProcessNode.form.getForm();
		nodeForm.reset();
		nodeForm.findField("workStationBelongTeam").clearValue();
		nodeForm.findField("workStationBelongTeamName").setValue("");
		var cfg = {
	        scope: this, url: ctx + '/jobProcessNode!getEntityByIDX.action',
	        params: {nodeIDX: JobProcessNode.idx},
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.entity != null) {
	            	var record = new Ext.data.Record();
	                var entity = result.entity;
	                for(var i in entity){
	                	if(i == 'planBeginTime'){
	                		record.set(i, new Date(entity[i]));
	                		JobProcessNode.planBeginTime = new Date(entity[i]).format('Y-m-d H:i');
	                		continue;	                    		
	                	}
	                	if(i == 'planEndTime'){
	                		record.set(i, new Date(entity[i]));
	                		continue;	                    		
	                	}
	                	record.set(i, entity[i]);
	                }           	
	            	nodeForm.loadRecord(record);
	            	NodeAndWorkCardEdit.planEndTime = entity.planEndTime;
	            	// newPlanBeginTime,newPlanEndTime记录编辑时 修改之前的开始及结束时间
					nodeForm.findField("newPlanBeginTime").setValue(new Date(entity.planBeginTime).format('Y-m-d H:i'));
					nodeForm.findField("newPlanEndTime").setValue(new Date(entity.planEndTime).format('Y-m-d H:i'));
	                var hours = (entity.ratedWorkMinutes / 60).toString().split(".")[0];
					var minutes = entity.ratedWorkMinutes - hours * 60;
					nodeForm.findField("workHours").setValue(hours);
					nodeForm.findField("workMinutes").setValue(minutes);
					nodeForm.findField("workStationBelongTeam").setDisplayValue(entity.workStationBelongTeam, entity.workStationBelongTeamName);
					nodeForm.findField("workStationBelongTeamName").setValue(entity.workStationBelongTeamName);
					nodeForm.findField("workStationName").setValue(entity.workStationName);
					JobProcessNode.nodeIDX = entity.nodeIDX;
					nodeForm.findField("workStationName").nodeIDX = JobProcessNode.nodeIDX;
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
					if(entity.editStatus == EDIT_STATUS_WAIT){
            			nodeForm.findField("planBeginTime").disable();
						nodeForm.findField("planEndTime").disable();
						nodeForm.findField("workHours").disable();
						nodeForm.findField("workMinutes").disable();
	            	}else{
	            		nodeForm.findField("planBeginTime").enable();
						nodeForm.findField("planEndTime").enable();
						nodeForm.findField("workHours").enable();
						nodeForm.findField("workMinutes").enable();
	            	}
	//				if (entity.status == NODE_STATUS_GOING) {
	//					NodeAndWorkCardEdit.disableNodeForm();
	//					NodeAndWorkCardEdit.disableNodeRelGrid();
	//				} else if (entity.status == NODE_STATUS_UNSTART) {
	//					NodeAndWorkCardEdit.enableNodeForm();
	//					NodeAndWorkCardEdit.enableNodeRelGrid();
	//				}
					if (entity.isLeaf == CONST_INT_IS_LEAF_NO) {
						nodeForm.findField("workStationBelongTeam").disable();
						nodeForm.findField("workStationName").disable();
					} else if (entity.status == NODE_STATUS_UNSTART) {
						nodeForm.findField("workStationBelongTeam").enable();
						nodeForm.findField("workStationName").enable();
					}
					if(entity.status == NODE_STATUS_GOING){
						nodeForm.findField("planBeginTime").disable();
					}
	            }
	        }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    JobProcessFirstNodeApplyWin.chossForm = 2;
		JobProcessNodeRel.grid.store.load();
		NodeAndWorkCardEdit.win.show();
	}
	
	NodeAndWorkCardEdit.disableNodeForm = function() {
		var nodeForm = JobProcessNode.form.getForm();
		nodeForm.findField("nodeName").disable();
		nodeForm.findField("ratedWorkMinutes").disable();
		nodeForm.findField("workHours").disable();
		nodeForm.findField("workMinutes").disable();
		nodeForm.findField("nodeDesc").disable();
		nodeForm.findField("planBeginTime").disable();
		nodeForm.findField("workStationName").disable();
		nodeForm.findField("workCalendarIDX").disable();
		nodeForm.findField("workStationBelongTeam").disable();
		nodeForm.findField("planMode").disable();
		Ext.getCmp("nodeSaveBtn").setVisible(false);
	}
	NodeAndWorkCardEdit.disableNodeRelGrid = function() {
		JobProcessNodeRel.grid.disable();
	}
	NodeAndWorkCardEdit.enableNodeForm = function() {
		var nodeForm = JobProcessNode.form.getForm();
		nodeForm.findField("nodeName").enable();
		nodeForm.findField("ratedWorkMinutes").enable();
		nodeForm.findField("workHours").enable();
		nodeForm.findField("workMinutes").enable();
		nodeForm.findField("nodeDesc").enable();
		nodeForm.findField("planBeginTime").enable();
		nodeForm.findField("workStationName").enable();
		nodeForm.findField("workCalendarIDX").enable();
		nodeForm.findField("workStationBelongTeam").enable();
		nodeForm.findField("planMode").enable();
		Ext.getCmp("nodeSaveBtn").setVisible(true);
	}
	NodeAndWorkCardEdit.enableNodeRelGrid = function() {
		JobProcessNodeRel.grid.enable();
	}
});