/**机车检修作业计划详细信息公用界面*/
Ext.onReady(function(){
	Ext.namespace('TrainRecordRdpPlan');                       //定义命名空间
	TrainRecordRdpPlan.rpdIdx = "" ;						//兑现单全局变量
	/**
	 * Form定义
	 */
	TrainRecordRdpPlan.titleForm =  new Ext.form.FormPanel({
		baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,	labelWidth: 50,
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.25, 
	            items: [
	        		{ fieldLabel:"车型车号", name:"trainTypeTrainNo", width:150, style:"border:none;background:none;", readOnly:true, id:"train_Id"},
	        		{ fieldLabel:"车型", name:"trainTypeIdx", id:"train_Type", xtype:"hidden"},
	        		{ fieldLabel:"车号", name:"trainNo", id:"train_no", xtype:"hidden"},
	        		{ fieldLabel:"车型简称", name:"trainTypeShortName", id:"train_sort_Type", xtype:"hidden"}      		
	            ]
	        }, {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.25, 
	            items: [
	            	{ fieldLabel:"修程", name:"repairClassIDX", id:"repairClass_Idx", xtype:"hidden"},
	        		{ fieldLabel:"修次", name:"repairTimeIdx", id:"repairTime_Idx", xtype:"hidden"},
	                { fieldLabel:"修程修次", name:"repairClassRepairTime", id:"xcxc", width:150, style:"border:none;background:none;", readOnly:true}
	                
	            ]
	     
	        }, {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.25, 
	            items: [	           
	                { fieldLabel:"修峻时间", id:'endTime_Id',name:"endTime", width:150, style:"border:none;background:none;", readOnly:true}
	            ]
	        }]
	    }]
	});
	
	
	//质量记录单查询结果窗口
	TrainRecordRdpPlan.saveWin = new Ext.Window({
		title: "车辆检修履历", maximizable:false, layout: "fit", 
		closeAction: "hide", modal: true, maximized: true , buttonAlign:"center",
		items: {
			layout: "border", frame:true,
			items:[{
	            region: 'north', layout: "fit",bodyStyle:'padding-left:20px; padding-top:10px;',collapsible:true,title: "车辆信息", 
	            height: 75, bodyBorder: false,items:[TrainRecordRdpPlan.titleForm]
	        },{
	            region : 'center', xtype:"tabpanel",activeTab:0, bodyBorder: false, 
	            items : [{            		            
	            	title:"车辆检修记录",
					layout:"fit",
					items:[TrainWorkPlanWorkRecord.panel],
					listeners: {
						activate: function() {
							TrainWorkPlanWorkRecord.reloadTree();
						}
					}
	            },{
					title:"上下车配件",
					layout:"fit",
					items:[{
						border:false, layout:'fit',
						items:PartsFixUnloadList.grid		
					}],
					listeners: {
						activate: function() {
							PartsFixUnloadList.grid.store.reload();
						}
					}
				},{
					title:"车辆提票",
					layout:"fit",
					items:[TrainWorkTicket.grid],
					listeners: {
						activate: function() {
							TrainWorkTicket.grid.store.reload();
						}
					}
				}]
	        }]
		},
		listeners: {
			beforeshow: function() {	
				if(TrainWorkPlanWorkRecord.isRender){
					TrainWorkPlanWorkRecord.reloadTree();
				}
				TrainWorkPlanWorkRecord.grid.store.load();  //刷新检修记录单信息
		
			}
		},
		buttons:[{
			text: "关闭", iconCls: "closeIcon", handler: function(){TrainRecordRdpPlan.saveWin.hide();}
		}]
	});
	
	//机车检修TrainWorkPlan
	TrainRecordRdpPlan.setParams = function(record){
		TrainRecordRdpPlan.rpdIdx = record.idx;  //施修任务兑现单主键

	    TrainRecordRdpPlan.titleForm.getForm().reset();
	    TrainRecordRdpPlan.titleForm.getForm().loadRecord(record);	 
	    if(null != record.endTime){
	   		Ext.getCmp("endTime_Id").setValue(new Date(record.endTime).format('Y-m-d H:i'));
	   	}
	    var cxch = record.trainTypeShortName +" | " +record.trainNo ; //车型|车号
	    var xcxc = (record.repairClassName == null ? "" : record.repairClassName) +" | " +(record.repairtimeName==null ? "" : record.repairtimeName); //车型|车号
	    Ext.getCmp("train_Id").setValue(cxch); 
	    Ext.getCmp("xcxc").setValue(xcxc); 
	    
	    // 为机车上下车配件传递值
	   PartsFixUnloadList.rpdIdx = TrainRecordRdpPlan.rpdIdx; 
	    // 为机车检修记录单传递值
	    TrainWorkPlanWorkRecord.rpdIdx = TrainRecordRdpPlan.rpdIdx;
	    
	    // 提票列表
	    TrainWorkTicket.rpdIdx = TrainRecordRdpPlan.rpdIdx;
	   
	    // 车型车号
		RQWorkCard.cxch = cxch;
		// 修程修次
		RQWorkCard.xcxc = xcxc;
		// 开始时间
		RQWorkCard.planBeginTime = new Date(record.planBeginTime).format('Y-m-d H:i');
		// 结束时间
		RQWorkCard.planEndTime = new Date(record.planEndTime).format('Y-m-d H:i');
		
		TrainWorkTicketDetail.cxch = cxch;
		TrainWorkTicketDetail.xcxc = xcxc;
		TrainWorkTicketDetail.planBeginTime = new Date(record.planBeginTime).format('Y-m-d H:i');
		TrainWorkTicketDetail.planEndTime = new Date(record.planEndTime).format('Y-m-d H:i');    
	 
		// 作业工单界面显示信息    
	    RQWorkCard.titleForm.getForm().reset();
	    RQWorkCard.titleForm.getForm().loadRecord(record);
	    RQWorkCard.titleForm.find("name","trainTypeTrainNo")[0].setValue(cxch);
	    RQWorkCard.titleForm.find("name","repairClassRepairTime")[0].setValue(xcxc);
	    RQWorkCard.titleForm.find("name","planBeginTime")[0].setValue(new Date(record.planBeginTime).format('Y-m-d H:i'));
	    RQWorkCard.titleForm.find("name","planEndTime")[0].setValue(new Date(record.planEndTime).format('Y-m-d H:i'));
	    TrainWorkPlanWorkRecord.reloadTree();  //刷新节点信息
		PartsFixUnloadList.grid.store.load();  //刷新下车配件信息
	}
	

});