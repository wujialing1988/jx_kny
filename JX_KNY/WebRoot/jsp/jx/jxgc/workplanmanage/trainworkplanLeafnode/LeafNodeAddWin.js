/**
 * 机车检修作业编辑超链接中的下级节点grid
 */
Ext.onReady(function(){
	Ext.namespace('LeafNodeAddWin'); 
	LeafNodeAddWin.labelWidth = 90;
	LeafNodeAddWin.fieldWidth = 150;
	LeafNodeAddWin.searchParam = {}
	LeafNodeAddWin.nodeIDX = '';
	LeafNodeAddWin.ids = [];
	LeafNodeAddWin.workPlanStatus = '';
	LeafNodeAddWin.calendarName = '';
	LeafNodeAddWin.parentIDX =  '';
	LeafNodeAddWin.status =  '';
	LeafNodeAddWin.workPlanIDX =  '';
	LeafNodeAddWin.processIDX = '';
	//初始化状态和对应的中文名
	LeafNodeAddWin.stateName = {"NOTSTARTED":"未启动","RUNNING":"运行","COMPLETED":"完成","TERMINATED":"终止"};
	//初始化状态和对应的中文名
	LeafNodeAddWin.planMode = {"AUTO":"自动","MANUAL":"手动","TIMER":"定时"};
	
	LeafNodeAddWin.processTips = null;
	
	/*
	 * 显示处理遮罩
	 */
	function showtip(msg){
		LeafNodeAddWin.processTips = new Ext.LoadMask(Ext.getBody(),{
			msg: msg || "正在处理你的操作，请稍后...",
			removeMask:true
		});
		LeafNodeAddWin.processTips.show();
	}
	
	/*
	 * 隐藏处理遮罩
	 */
	function hidetip(){
		LeafNodeAddWin.processTips.hide();
	}
	
	// 保存新增节点
	LeafNodeAddWin.saveFn = function() {
		var nodeFormEditWin = LeafNodeAddWin.form.getForm();
		if (!nodeFormEditWin.isValid()) return;       
	    var data = nodeFormEditWin.getValues();
	    var msg = '';
	    var msg1 = "计划开始及结束时间以界面设置为准,";
	    var msg2 = "申请延期,";	
	    var planBeginTime = new Date(new Date(data.planBeginTime).getFullYear(),new Date(data.planBeginTime).getMonth(),new Date(data.planBeginTime).getDate());								
	    var parentPlanEndTime = new Date(new Date(LeafNodeAddWin.parentPlanEndTime).getFullYear(),new Date(LeafNodeAddWin.parentPlanEndTime).getMonth(),new Date(LeafNodeAddWin.parentPlanEndTime).getDate());
        if(planBeginTime > parentPlanEndTime){
        	Ext.Msg.alert('提示', "计划开始时间已经超过你节点的结束时间，请重新选择时间！");
        	return;
//        	msg = msg2;
//		    data.editStatus = EDIT_STATUS_WAIT;
        }else{
        	msg = msg1;
        }	   
	    var cfg = {
	        url: ctx + "/jobProcessNodeNew!saveOrUpdateLeafNode.action", jsonData: data,
	        timeout: 600000,
	        success: function(response, options){
				if(LeafNodeAddWin.processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                LeafNodeListGrid.grid.store.reload();
	                TrainWorkPlanLeafNodeWin.tree.root.reload();
	                TrainWorkPlanForLeafNode.store.reload();
	                LeafNodeAddWin.win.hide();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){	
	        	if(LeafNodeAddWin.processTips) hidetip();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
	    };
	
	    Ext.Msg.confirm("提示  ", msg + "确定新增操作？  ", function(btn){
	        if(btn != 'yes')    return;
	        showtip();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });
	}
		// 获取form时有问题，多个方法只调用第一个
	LeafNodeAddWin.workstationReturnFn = function(stationObject) {
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
	
	/** **************** 定义form开始 **************** */  
	LeafNodeAddWin.form = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",
		layout: "form",		border: false,	labelWidth: LeafNodeAddWin.labelWidth,
	    defaults: {
	    	anchor: "95%",
	    	defaults: {
	    		defaults:{
	    			xtype:"textfield", 
				    labelWidth:LeafNodeAddWin.labelWidth, anchor:"98%",
				    defaults: {
				    	width: LeafNodeAddWin.fieldWidth
				    }
	    		}    		
	    	}
	    },
	    items: [{
	    	xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	    	items:[{
					baseCls:"x-plain", align:"center", layout:"form", labelWidth:LeafNodeAddWin.labelWidth, columnWidth: 0.5,
			    	items:[{
			    			name: "nodeName", fieldLabel: "名称", maxLength: 100, allowBlank: false
			    		}]
    			},{
					baseCls:"x-plain", align:"center", layout:"form", labelWidth:LeafNodeAddWin.labelWidth, columnWidth: 0.5,
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
					baseCls:"x-plain", align:"center", layout:"form", labelWidth:LeafNodeAddWin.labelWidth, columnWidth: 0.5,
			    	items:[{
			    			name: "planBeginTime", fieldLabel: "开始时间",  xtype:"my97date",format: "Y-m-d H:i",
				        	my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
//				        	,enableKeyEvents:true,
//							listeners: {
//								keyup: function(filed, e) {
//									var planBeginTime = new Date(new Date(this.getValue()).getFullYear(),new Date(this.getValue()).getMonth(),new Date(this.getValue()).getDate());
//									var parentPlanEndTime = new Date(new Date(LeafNodeAddWin.parentPlanEndTime).getFullYear(),new Date(LeafNodeAddWin.parentPlanEndTime).getMonth(),new Date(LeafNodeAddWin.parentPlanEndTime).getDate());
//									if(planBeginTime > parentPlanEndTime){
//										Ext.getCmp("reason_l").show();
//										Ext.getCmp("editStatus_l").setValue(EDIT_STATUS_WAIT);
//										Ext.getCmp("reason_l").allowBlank = false;
//									}else{
//										Ext.getCmp("editStatus_id").setValue(null);
//										Ext.getCmp("reason_id").hide();
//										Ext.getCmp("reason_id").allowBlank = true;
//									}
//								}
//							}
			    		},{
			    			fieldLabel: '作业工位', name: 'workStationName', 
			    			xtype: 'JobNodeStationDefSelect', nodeIDX: LeafNodeAddWin.nodeIDX,
							editable: false, returnFn: LeafNodeAddWin.workstationReturnFn
	
			    	
											
						}]
				},{
					baseCls:"x-plain", align:"center", layout:"form", labelWidth:LeafNodeAddWin.labelWidth, columnWidth: 0.5,
			    	items:[{
			    			name: "planEndTime", fieldLabel: "结束时间",  xtype:"my97date",format: "Y-m-d H:i",allowBlank:false,
				        	my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}
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
			    			name: 'idx', value: LeafNodeAddWin.idx , xtype:"hidden"
			    		},{
			    			name: 'workPlanIDX',  value: LeafNodeAddWin.workPlanIDX ,xtype:"hidden"
			    		},{
			    			name: 'processIDX',  value: LeafNodeAddWin.processIDX,  xtype:"hidden"
			    		},{
			    			name: 'parentIDX', value: LeafNodeAddWin.parentIDX , xtype:"hidden"
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
			        	name: "reason",  xtype:"hidden",allowBlank: true, fieldLabel: "延期原因",  maxLength:1000  
			        }]
				}]	
	    }]
		
	});
	
	LeafNodeAddWin.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
	            title: "流程节点", border: false, layout: "fit", buttonAlign : "center",
	            items: [{
		            layout: "fit",bodyStyle:'padding-left:20px;', frame: true, buttonAlign : "center",
		            height: 200, bodyBorder: false,
		            items:[LeafNodeAddWin.form],
			        buttons: [{
				        	id: "nodeSaveBtn", text: "保存", iconCls: "saveIcon", handler: function() {
					        	LeafNodeAddWin.saveFn();
					        }
					    }, {
					        text: "关闭", iconCls: "closeIcon", handler: function(){ LeafNodeAddWin.win.hide(); }
					    }]	
		        	}]
   			 }]
	});
	LeafNodeAddWin.win = new Ext.Window({
	    title:"节点编辑", width: 600, maximized : false, layout: 'fit',
	    plain:true, closeAction:"hide", buttonAlign : "center",
	    items:LeafNodeAddWin.tabs
	});


});