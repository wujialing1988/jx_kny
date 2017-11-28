Ext.onReady(function(){
	Ext.namespace('LeafNodeAndWorkCardEditWin'); 
	LeafNodeAndWorkCardEditWin.processTips = null;
	LeafNodeAndWorkCardEditWin.labelWidth = 90;
	LeafNodeAndWorkCardEditWin.fieldWidth = 150;
	LeafNodeAndWorkCardEditWin.idx = '';
	LeafNodeAndWorkCardEditWin.nodeIDX = '';
	LeafNodeAndWorkCardEditWin.workPlanIDX = '';
	LeafNodeAndWorkCardEditWin.processIDX = '';
	LeafNodeAndWorkCardEditWin.parentIDX = '';
	LeafNodeAndWorkCardEditWin.planBeginTime = null;
	LeafNodeAndWorkCardEditWin.parentPlanEndTime = null;
	LeafNodeAndWorkCardEditWin.editStatus = 0;
	/*
	 * 显示处理遮罩
	 */
	LeafNodeAndWorkCardEditWin.showtip = function(win,msg){
		LeafNodeAndWorkCardEditWin.processTips = new Ext.LoadMask(win.getEl(),{
			msg: msg || "正在处理你的操作，请稍后...",
			removeMask:true
		});
		LeafNodeAndWorkCardEditWin.processTips.show();
	}
	/*
	 * 隐藏处理遮罩
	 */
	LeafNodeAndWorkCardEditWin.hidetip = function(){
		LeafNodeAndWorkCardEditWin.processTips.hide();
	}
	// 保存节点信息
	LeafNodeAndWorkCardEditWin.saveFn = function() {
		var nodeFormEditWin = LeafNodeAndWorkCardEditWin.form.getForm();
		if (!nodeFormEditWin.isValid()) return;    
		nodeFormEditWin.findField("planBeginTime").enable();
		nodeFormEditWin.findField("planEndTime").enable();
	    var data = nodeFormEditWin.getValues();
	    if(data.status == NODE_STATUS_GOING){
			nodeFormEditWin.findField("planBeginTime").disable();
	    }
	    var msg = '';
	    var msg1 = "计划开始时间以界面设置为准,";
	    var msg2 = "申请延期,";	
	    var planEndTime = new Date(new Date(data.planEndTime).getFullYear(),new Date(data.planEndTime).getMonth(),new Date(data.planEndTime).getDate());								
	    var parentPlanEndTime = new Date(new Date(LeafNodeAndWorkCardEditWin.parentPlanEndTime).getFullYear(),new Date(LeafNodeAndWorkCardEditWin.parentPlanEndTime).getMonth(),new Date(LeafNodeAndWorkCardEditWin.parentPlanEndTime).getDate());
        if(planEndTime > parentPlanEndTime){
        	msg = msg2;
		    data.editStatus = EDIT_STATUS_WAIT;
        }else{
        	msg = msg1;
        }	   
	    var cfg = {
	        url: ctx + "/jobProcessNodeNew!saveOrUpdateLeafNode.action", jsonData: data,
	        timeout: 600000,
	        success: function(response, options){
				if(LeafNodeAndWorkCardEditWin.processTips) LeafNodeAndWorkCardEditWin.hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
//	                LeafNodeAndWorkCardEditWin.showEditWin();
	                if(EDIT_STATUS_WAIT ==  nodeFormEditWin.findField("editStatus").getValue()){
		                nodeFormEditWin.findField("planBeginTime").disable();
		                nodeFormEditWin.findField("planEndTime").disable();
	                }
	                NodeApplyDelayList.grid.store.reload();
	                TrainWorkPlanForLeafNode.store.reload();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){	
	        	if(LeafNodeAndWorkCardEditWin.processTips) LeafNodeAndWorkCardEditWin.hidetip();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
	    };
	
	    Ext.Msg.confirm("提示  ", msg + "确定操作？  ", function(btn){
	        if(btn != 'yes')    return;
	        LeafNodeAndWorkCardEditWin.showtip(TrainWorkPlanLeafNodeWin.win);
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });
	}
	
	// 获取form时有问题，必须两个一起赋值
	LeafNodeAndWorkCardEditWin.workstationReturnFn = function(stationObject) {
		form = LeafNodeAndWorkCardEditWin.form;
		form.find("name", "workStationIDX")[0].setValue(stationObject.workStationIDX);
		form.find("name", "workStationName")[0].setValue(stationObject.workStationName);
		form.getForm().findField("workStationBelongTeam").setDisplayValue(stationObject.teamOrgId, stationObject.teamOrgName);
		form.find("name", "workStationBelongTeamName")[0].setValue(stationObject.teamOrgName);
		form = LeafNodeAddWin.form;
		form.find("name", "workStationIDX")[0].setValue(stationObject.workStationIDX);
		form.find("name", "workStationName")[0].setValue(stationObject.workStationName);
		form.getForm().findField("workStationBelongTeam").setDisplayValue(stationObject.teamOrgId, stationObject.teamOrgName);
		form.find("name", "workStationBelongTeamName")[0].setValue(stationObject.teamOrgName);
	}
	// 编辑节点form
	LeafNodeAndWorkCardEditWin.form = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",
		layout: "form",		border: false,	labelWidth: LeafNodeAndWorkCardEditWin.labelWidth,
	    defaults: {
	    	anchor: "95%",
	    	defaults: {
	    		defaults:{
	    			xtype:"textfield", 
				    labelWidth:LeafNodeAndWorkCardEditWin.labelWidth, anchor:"98%",
				    defaults: {
				    	width: LeafNodeAndWorkCardEditWin.fieldWidth
				    }
	    		}    		
	    	}
	    },
	    items: [{
	    	xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	    	items:[{
					baseCls:"x-plain", align:"center", layout:"form", labelWidth:LeafNodeAndWorkCardEditWin.labelWidth, columnWidth: 0.5,
			    	items:[{
			    			name: "nodeName", fieldLabel: "名称", maxLength: 100, allowBlank: false
			    		}]
    			},{
					baseCls:"x-plain", align:"center", layout:"form", labelWidth:LeafNodeAndWorkCardEditWin.labelWidth, columnWidth: 0.5,
			    	items:[{
	    				xtype: 'radiogroup',
			            fieldLabel: '启动模式',
			            name: 'planMode',
			            items: [
			                { boxLabel: '定时',inputValue: PLANMODE_TIMER, name: 'planMode', checked: true
			                },
			                { boxLabel: '手动',inputValue: PLANMODE_MUNAUL, name: 'planMode'			               
			                } 
    		            ]
	                }]						            
			},{
					baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:90, columnWidth: 1, 
			        items: [{
			        	name: "nodeDesc", fieldLabel: "节点描述",  xtype:"textarea", maxLength:1000  
			        }]
				},{
					baseCls:"x-plain", align:"center", layout:"form", labelWidth:LeafNodeAndWorkCardEditWin.labelWidth, columnWidth: 0.5,
			    	items:[{
			    			id: 'startDate', name: "planBeginTime", fieldLabel: "开始时间",  xtype:"my97date", format: "Y-m-d H:i", enableKeyEvents:true,
			    			vtype:'dateRange',
							dateRange:{startDate: 'startDate', endDate: 'endDate'},
				        	my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
			    		},{
			    			fieldLabel: '作业工位', name: 'workStationName', 
			    			xtype: 'JobNodeStationDefSelect', nodeIDX: LeafNodeAndWorkCardEditWin.nodeIDX,
							editable: false, returnFn: LeafNodeAndWorkCardEditWin.workstationReturnFn
						}]
				},{
					baseCls:"x-plain", align:"center", layout:"form", labelWidth:LeafNodeAndWorkCardEditWin.labelWidth, columnWidth: 0.5,
			    	items:[{
			    			id: 'endDate', name: "planEndTime", fieldLabel: "结束时间",  xtype:"my97date",format: "Y-m-d H:i",allowBlank:false,
				        	vtype:'dateRange', my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"},
							dateRange:{startDate: 'startDate', endDate: 'endDate'},
							listeners: {
								"change":function(){
									var planEndTime = new Date(new Date(this.getValue()).getFullYear(),new Date(this.getValue()).getMonth(),new Date(this.getValue()).getDate());
									var parentPlanEndTime = new Date(new Date(LeafNodeAndWorkCardEditWin.parentPlanEndTime).getFullYear(),new Date(LeafNodeAndWorkCardEditWin.parentPlanEndTime).getMonth(),new Date(LeafNodeAndWorkCardEditWin.parentPlanEndTime).getDate());
									if(planEndTime > parentPlanEndTime){
										Ext.getCmp("reason_id").show();
										Ext.getCmp("editStatus_id").setValue(EDIT_STATUS_WAIT);
										Ext.getCmp("reason_id").allowBlank = false;
									}else{
										Ext.getCmp("editStatus_id").setValue(null);
										Ext.getCmp("reason_id").hide();
										Ext.getCmp("reason_id").allowBlank = true;
									}
								}
							}
			    		},{
			    			
			    			fieldLabel:"作业班组", xtype: 'OmOrganizationCustom_comboTree',hiddenName: 'workStationBelongTeam',
			                returnField:[{widgetId:"workStationBelongTeamName_ID",propertyName:"orgname"}],
			      		    queryHql:"[degree]tream|fullName",
			      		    fullNameDegree: "plant",
				  		    selectNodeModel: 'leaf', width: 170
			    		},{    			
			               name: 'workCalendarIDX', fieldLabel: '日历'	, xtype:"hidden"
						},{
			    			id: "workStationBelongTeamName_ID", name: "workStationBelongTeamName", xtype:"hidden"
			    		},{
			    			name: "workStationIDX", xtype:"hidden"
			    		},{
			    			name: 'idx', value: LeafNodeAndWorkCardEditWin.idx , xtype:"hidden"
			    		},{
			    			name: 'workPlanIDX', xtype:"hidden"
			    		},{
			    			name: 'processIDX',  xtype:"hidden"
			    		},{
			    			name: 'parentIDX', value: LeafNodeAndWorkCardEditWin.parentIDX , xtype:"hidden"
			    		},{
			    			name: 'nodeIDX', xtype:"hidden"
			    		},{
			    			name: 'status', xtype:"hidden"
			    		},{
			    			name: 'isLeaf', xtype:"hidden"
			    		},{
			    			name: 'realBeginTime', xtype:"hidden"
			    		},{
			    			name: 'seqNo', xtype:"hidden"
		    			},{
			    			name: 'startDay', xtype:"hidden"
			    		},{
			    			name: 'startTime', xtype:"hidden"
			    		},{
			    			name: 'endDay', xtype:"hidden"
			    		},{
			    			name: 'endTime', xtype:"hidden"
			    		},{
			    			name: 'ratedWorkMinutes', xtype:"hidden"
			    		},{
			    			name: 'editStatus', id:'editStatus_id',xtype:"hidden"
			    		}]
    			},{
					baseCls:"x-plain", align:"center", layout:"form",  labelWidth:90, columnWidth: 1, 
			        items: [{
			        	name: "reason", id:"reason_id", xtype:"textarea", allowBlank: true, fieldLabel: "延期原因",  maxLength:1000  
			        }]
				}]	
	    }]
		
	});
	
//	LeafNodeAndWorkCardEditWin.tabs = new Ext.TabPanel({
//	    activeTab: 0,
//	    frame:true,
//	    items:[{
//	            title: "流程节点", border: false, layout: "border", buttonAlign : "center",
//	            items: [{
//		            region: 'center', layout: "fit",bodyStyle:'padding-left:20px;',
//		            height: 200, bodyBorder: false,
//		            items:[LeafNodeAndWorkCardEditWin.form], frame: true
//		        },{
//		            region : 'center', layout : 'fit', bodyBorder: false, items : [ JobProcessNodeRel.grid ]
//		        }],
//		        buttons: [{
//			        	id: "nodeSaveBtn", text: "保存", iconCls: "saveIcon", handler: function() {
//				        	LeafNodeAndWorkCardEditWin.saveFn();
//				        }
//				    }, {
//				        text: "关闭", iconCls: "closeIcon", handler: function(){ LeafNodeAndWorkCardEditWin.win.hide(); }
//				    }]
//	        },{
//	            id: "WorkCardEditTab", title: "作业工单", border: false, layout: "fit", 
//	            items:[WorkCardEdit.grid]
//	        },{
//	            id: "NodeExtConfigTab", title: "扩展配置", border: false, layout: "fit", 
//	            items:[JobNodeExtConfig.form]
//	        }]
//	});
//	LeafNodeAndWorkCardEditWin.win = new Ext.Window({
//	    title:"节点编辑", width: 800, maximized : true, layout: 'fit',
//	    plain:true, closeAction:"hide", buttonAlign:'center',  
//	    items:LeafNodeAndWorkCardEditWin.tabs
//	});
	
	LeafNodeAndWorkCardEditWin.showEditWin = function() {
		var nodeFormEditWin = LeafNodeAndWorkCardEditWin.form.getForm();
		nodeFormEditWin.reset();
		nodeFormEditWin.findField("workStationBelongTeam").clearValue();
		nodeFormEditWin.findField("workStationBelongTeamName").setValue("");
		var cfg = {
	        scope: this, url: ctx + '/jobProcessNode!getEntityByIDX.action',
	        params: {nodeIDX: LeafNodeAndWorkCardEditWin.idx},
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.entity != null) {
	            	var record = new Ext.data.Record();
	                var entity = result.entity;
	                for(var i in entity){
	                	if(i == 'planBeginTime'){
	                		record.set(i, new Date(entity[i]));
//	                		JobProcessLeafNodeEditWin.planBeginTime = new Date(entity[i]).format('Y-m-d H:i');
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
					LeafNodeAndWorkCardEditWin.parentIDX = entity.parentIDX;
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
	}


});