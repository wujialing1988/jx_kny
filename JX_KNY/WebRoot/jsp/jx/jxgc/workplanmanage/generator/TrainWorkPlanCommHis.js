/**机车检修作业计划详细信息公用界面*/
Ext.onReady(function(){
	Ext.namespace('TrainWorkPlanCommHis');                       //定义命名空间
	TrainWorkPlanCommHis.rpdIdx = "" ;						//兑现单全局变量
	/**
	 * Form定义
	 */
	TrainWorkPlanCommHis.titleForm =  new Ext.form.FormPanel({
		baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,	labelWidth: 50,
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:120,
	            columnWidth: 0.33, 
	            items: [
	        		{ fieldLabel:"车型车号", name:"trainTypeTrainNo", width:150, style:"border:none;background:none;", readOnly:true, id:"train_Id"},
	        		{ fieldLabel:"车型", name:"trainTypeIdx", id:"train_Type", xtype:"hidden"},
	        		{ fieldLabel:"车号", name:"trainNo", id:"train_no", xtype:"hidden"},
	        		{ fieldLabel:"车型简称", name:"trainTypeShortName", id:"train_sort_Type", xtype:"hidden"},
	            	{ fieldLabel:"修程", name:"repairClassIDX", id:"repairClass_Idx", xtype:"hidden"},
	        		{ fieldLabel:"修次", name:"repairTimeIdx", id:"repairTime_Idx", xtype:"hidden"},
	                { fieldLabel:"修程修次", name:"repairClassRepairTime", id:"xcxc", width:150, style:"border:none;background:none;", readOnly:true}	        		
	            ]
	        }, {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:120,
	            columnWidth: 0.33, 
	            items: [
	            	
	                { fieldLabel:"计划开始", id:'planBeginTime_Id', name:"planBeginTime", width:150, style:"border:none;background:none;", readOnly:true},
	                { fieldLabel:"计划结束", id:'planEndTime_Id',name:"planEndTime", width:150, style:"border:none;background:none;", readOnly:true}
	            ]
	        }, {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:120,
	            columnWidth: 0.33, 
	            items: [
	            
	                { fieldLabel:"实际开始", id:'beginTime_Id', name:"beginTime", width:150, style:"border:none;background:none;", readOnly:true},
	                { fieldLabel:"实际结束", id:'endTime_Id',name:"endTime", width:150, style:"border:none;background:none;", readOnly:true}
	            ]
	        }]
	    }]
	});
	
	
	//质量记录单查询结果窗口
	TrainWorkPlanCommHis.saveWin = new Ext.Window({
		title: "作业计划单查看", maximizable:false, layout: "fit", 
		closeAction: "hide", modal: true, maximized: true , buttonAlign:"center",
		items: {
		layout: "border", frame:true,
			items:[{
	            region: 'north', layout: "fit",bodyStyle:'padding-left:20px; padding-top:10px;',collapsible:true,title: "车辆检修信息", 
	            height: 110, bodyBorder: false,items:[TrainWorkPlanCommHis.titleForm]
	        },{
	            region : 'center', xtype:"tabpanel",activeTab:0, bodyBorder: false, 
	            items : [{            	
	            	title:"检修计划",
					layout:"fit",
					items:[TrainWorkPlanGanttHis.ganttPanel],
					listeners: {
						activate: function() {
							WorkPlanGanttHis.loadFn();
						}
					}
	            }, {
	            	title:"检修记录单",
					layout:"fit",
					items:[RQWorkCard.tab],
					listeners: {
						activate: function() {
							RQWorkCard.rpdIdx = TrainWorkPlanCommHis.rpdIdx;
							RQWorkCard.reloadTree();
						}
					}
	            }/*,{
	            	title:"上下车计划",
					layout:"fit",
					items:[PartsZzjhQuery.grid],
					listeners: {
						activate: function() {
							PartsZzjhQuery.workPlanIdx = TrainWorkPlanCommHis.rpdIdx;
							PartsZzjhQuery.grid.store.reload();					
						}
					}	
				},{
					title:"上车配件",
					layout:"fit",
					items:[{
						border:false, layout:'fit',
						items:PartsFixRegisterHis.grid
					}],
					listeners: {
						activate: function() {
							PartsFixRegisterHis.grid.store.reload();
						}
					}
	            }, {
					title:"下车配件",
					layout:"fit",
					items:[{
						border:false, layout:'fit',
						items:PartsUnloadRegisterHis.grid		
					}],
					listeners: {
						activate: function() {
							PartsUnloadRegisterHis.grid.store.reload();
						}
					}
				},{
				
					title:"车辆提票",
					layout:"fit",
					items:[TrainWorkTicket.grid],
					listeners: {
						activate: function() {
							TrainWorkTicket.rpdIdx = TrainWorkPlanCommHis.rpdIdx ;
							TrainWorkTicket.grid.store.reload();
						}
					}
				}*/]
	        }]
		},
		listeners: {
			beforeshow: function() {				
				WorkPlanGanttHis.loadFn();  // 加载甘特图数据
				if(RQWorkCard.isRender){
					RQWorkCard.reloadTree();
				}
				RQWorkCard.grid.store.load();  //刷新作业工单信息
				PartsZzjhQuery.grid.store.load();  //刷新上下车配件信息
				PartsUnloadRegisterHis.grid.store.load();  //刷新下车配件信息
				PartsFixRegisterHis.grid.store.load();  //刷新上车配件信息
				TrainWorkTicket.grid.store.load();  //刷新超范围或返工修情况信息				
			},
			render: function() {
				setTimeout(WorkPlanGanttHis.initFn, 50);
			}
		},
		buttons:[{
			text: "关闭", iconCls: "closeIcon", handler: function(){TrainWorkPlanCommHis.saveWin.hide();}
		}]
	});
	
	//机车检修TrainWorkPlan
	TrainWorkPlanCommHis.setParams = function(record){
		TrainWorkPlanCommHis.rpdIdx = record.idx;  //施修任务兑现单主键
		TrainWorkPlanCommHis.workPlanStatus =  record.transLogStatus; // 判断是否查历史数据
		TrainWorkCardNew.workPlanStatus = 	TrainWorkPlanCommHis.workPlanStatus; // 记录卡
		WorkPlanGanttHis.workPlanIDX = record.idx;
		RQWorkCard.grid.store.load();  //刷新作业工单信息
		PartsUnloadRegisterHis.grid.store.load();  //刷新下车配件信息
		PartsFixRegisterHis.grid.store.load();  //刷新上车配件信息
	    TrainWorkPlanCommHis.titleForm.getForm().reset();
	    TrainWorkPlanCommHis.titleForm.getForm().loadRecord(record);
	    Ext.getCmp("planBeginTime_Id").setValue(new Date(record.planBeginTime).format('Y-m-d H:i'));
	    Ext.getCmp("planEndTime_Id").setValue(new Date(record.planEndTime).format('Y-m-d H:i'));
	    Ext.getCmp("beginTime_Id").setValue(new Date(record.beginTime).format('Y-m-d H:i'));
	    if(null != record.endTime){
	   		Ext.getCmp("endTime_Id").setValue(new Date(record.endTime).format('Y-m-d H:i'));
	   	}
	    var cxch = record.trainTypeShortName +" | " +record.trainNo ; //车型|车号
	    var xcxc = (record.repairClassName == null ? "" : record.repairClassName) +" | " +(record.repairtimeName==null ? "" : record.repairtimeName); //车型|车号
	    Ext.getCmp("train_Id").setValue(cxch); 
	    Ext.getCmp("xcxc").setValue(xcxc); 
	    
	    //为机车检修记录单传递值
	    //车型车号
		RQWorkCard.cxch = cxch;
		TrainWorkTicketDetail.cxch = cxch;
		//修程修次
		RQWorkCard.xcxc = xcxc;
		TrainWorkTicketDetail.xcxc = xcxc;
		//开始时间
		RQWorkCard.planBeginTime = new Date(record.planBeginTime).format('Y-m-d H:i');
		//结束时间
		RQWorkCard.planEndTime = new Date(record.planEndTime).format('Y-m-d H:i');
		//开始时间
		TrainWorkTicketDetail.planBeginTime = new Date(record.planBeginTime).format('Y-m-d H:i');
		//结束时间
		TrainWorkTicketDetail.planEndTime = new Date(record.planEndTime).format('Y-m-d H:i');    
		//作业工单界面显示信息    
	    RQWorkCard.titleForm.getForm().reset();
	    RQWorkCard.titleForm.getForm().loadRecord(record);
	    RQWorkCard.titleForm.find("name","trainTypeTrainNo")[0].setValue(cxch);
	    RQWorkCard.titleForm.find("name","repairClassRepairTime")[0].setValue(xcxc);
	    RQWorkCard.titleForm.find("name","planBeginTime")[0].setValue(new Date(record.planBeginTime).format('Y-m-d H:i'));
	    RQWorkCard.titleForm.find("name","planEndTime")[0].setValue(new Date(record.planEndTime).format('Y-m-d H:i'));
	}
	
	//机车检修作业编辑TrainWorkPlanEdit.js
	TrainWorkPlanCommHis.showWin = function(data) {	
		if(this.searchWin != null)  this.searchWin.hide(); 	
		var dataArray = data.split(",");
		WorkPlanGanttHis.workPlanIDX = dataArray[0];
		RQWorkCard.rpdIdx = dataArray[0];	
	    TrainWorkPlanCommHis.rpdIdx = dataArray[0];  //施修任务兑现单主键
		TrainWorkPlanCommHis.workPlanStatus =  dataArray[7];
		RQWorkCard.workPlanStatus = 	TrainWorkPlanCommHis.workPlanStatus; // 记录单;		
		TrainWorkCardNew.workPlanStatus = 	TrainWorkPlanCommHis.workPlanStatus; // 记录卡;	
		Ext.getCmp("train_Id").setValue(dataArray[1]); 
		Ext.getCmp("xcxc").setValue(dataArray[2]);
		Ext.getCmp("planBeginTime_Id").setValue(dataArray[3]);
	    Ext.getCmp("planEndTime_Id").setValue(dataArray[4]);
	    Ext.getCmp("beginTime_Id").setValue(dataArray[5]);
	   	Ext.getCmp("endTime_Id").setValue(dataArray[6]);
	
	    //为机车检修记录单传递值
	    //车型车号
		RQWorkCard.cxch = dataArray[1];
		TrainWorkTicketDetail.cxch = dataArray[1];
		//修程修次
		RQWorkCard.xcxc = dataArray[2];
		TrainWorkTicketDetail.xcxc = dataArray[2];
		//开始时间
		RQWorkCard.planBeginTime = dataArray[3];
		//结束时间
		RQWorkCard.planEndTime = dataArray[4];
		//开始时间
		TrainWorkTicketDetail.planBeginTime = dataArray[5];
		//结束时间
		TrainWorkTicketDetail.planEndTime = dataArray[6];
	    	    
		//作业工单界面显示信息    
	    RQWorkCard.titleForm.getForm().reset();
	    RQWorkCard.titleForm.find("name","trainTypeTrainNo")[0].setValue(dataArray[1]);
	    RQWorkCard.titleForm.find("name","repairClassRepairTime")[0].setValue(dataArray[2]);
	    RQWorkCard.titleForm.find("name","planBeginTime")[0].setValue(dataArray[3]);
	    RQWorkCard.titleForm.find("name","planEndTime")[0].setValue(dataArray[4]);
	
	    TrainWorkPlanCommHis.saveWin.show();
	}
	
	//查看配件检修任务详情
	TrainWorkPlanCommHis.showPartsRdpInfoFn = function(partsRdpIDX,  partsAccountIDX) {		
		if (!partsRdpIDX || "null"==partsRdpIDX){
				Ext.Ajax.request({
					url: ctx + '/partsAccount!getModel.action',
					params:{id: partsAccountIDX},
					//请求成功后的回调函数
				    success: function(response, options){
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {       //操作成功 
				        	PartsDetail.record = result.entity;
				        	PartsDetail.win.show();
				        } else {                           //操作失败
				            alertFail(result.errMsg);
				        }
				    },
				    //请求失败后的回调函数
				    failure: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				    }
				});
			}	
	   else{
			// Ajax请求
			Ext.Ajax.request({
				url: ctx + '/partsRdp!getModel.action',
				params:{id: partsRdpIDX},
				//请求成功后的回调函数
			    success: function(response, options){
			        var result = Ext.util.JSON.decode(response.responseText);
			        if (result.errMsg == null) {       //操作成功   		        	
			        	PartsRdpDetail.record = result.entity;
			        	PartsRdpDetail.win.show();
			        } else {                           //操作失败
			            alertFail(result.errMsg);
			        }
			    },
			    //请求失败后的回调函数
			    failure: function(response, options){
			        if(self.loadMask)    self.loadMask.hide();
			        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			    }
			});
		}
					
	}
});