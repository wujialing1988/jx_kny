/**
 * 机务设备工单定义 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.ns('TrainWorkPlanEdit');
	
	/** ******************** 定义全局变量开始 ******************** */
	TrainWorkPlanEdit.labelWidth = 100;
	TrainWorkPlanEdit.fieldWidth = 140;
	/** ******************** 定义全局变量结束 ******************** */
	
	/** ******************** 定义全局函数开始 ******************** */	
	// 初始化
	TrainWorkPlanEdit.initFn = function(entity, form) {
		if (Ext.isEmpty(entity)) {
        	MyExt.Msg.alert("未能查询到机车<span style='font-weight:bold;color:red;'>" + trainTypeShortName + trainNo + "</span>的检修作业计划信息！");
        	return;
        }
        var form = TrainWorkPlanWin.infoForm.getForm();
		form.reset();
		form.findField("trainTypeAndNo").setValue(entity.trainTypeShortName + entity.trainNo);
		form.findField("xcxc").setValue(entity.repairClassName + entity.repairtimeName);
		form.findField("planBeginTime").setValue(new Date(entity.planBeginTime).format('Y-m-d H:i'));
		form.findField("planEndTime").setValue(new Date(entity.planEndTime).format('Y-m-d H:i'));	
		TrainWorkPlanGantt.workPlanIDX = entity.idx;
		workPlanIDX = TrainWorkPlanGantt.workPlanIDX;
		workPlanEntity = entity;
		switch(workPlanEntity.workPlanStatus){
			case WORKPLAN_STATUS_NEW:
				 Ext.getCmp("startPlanButton").setVisible(true);
				 Ext.getCmp("startNodeButton").setVisible(false);
				 break;
			case WORKPLAN_STATUS_HANDLING:
				 Ext.getCmp("startPlanButton").setVisible(false);
				 Ext.getCmp("startNodeButton").setVisible(true);
				 break;
			case WORKPLAN_STATUS_HANDLED:
				 Ext.getCmp("startPlanButton").setVisible(false);
				 Ext.getCmp("startNodeButton").setVisible(false);
				 break;
		    case WORKPLAN_STATUS_NULLIFY:
				 Ext.getCmp("startPlanButton").setVisible(false);
				 Ext.getCmp("startNodeButton").setVisible(false);
				 break;
			default:
				 Ext.getCmp("startPlanButton").setVisible(true);
				 Ext.getCmp("startNodeButton").setVisible(false);							 
				 break;
		}	
		Ext.getCmp("closeWorkPlanWinButton").setVisible(false);	
		WorkPlanGantt.initFn();
		WorkPlanGantt.loadFn();
		
	}
	/** ******************** 定义全局函数结束 ******************** */
	
	
	// 页面自适应布局
	new Ext.Viewport({
	    layout: "fit", items: [TrainWorkPlanWin.panel],
		listeners: {
			render: function(form) {
				var cfg = {
					url: ctx + '/trainWorkPlan!getModelByEntiy.action',
					jsonData: Ext.util.JSON.encode({
						trainTypeIDX: trainTypeIDX,				// 车型主键
						trainNo: trainNo,						// 车号
						workPlanStatus: STATUS_HANDLING			// 只查询当前作业计划状态为“处理中”的机车信息
					}),
					success: function(response, options){
				        if(self.loadMask)    self.loadMask.hide();
				        var result = Ext.util.JSON.decode(response.responseText);
				        // 初始化
				        TrainWorkPlanEdit.initFn(result.entity, form);
				    }
				};
				Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
			}
		}
	});
	
	// 窗口大小改变时，重新加载页面，用于解决关闭窗口后，甘特图组件不能自适应的缺陷
	window.onresize = function(){
		self.location.reload();
	};
	
})