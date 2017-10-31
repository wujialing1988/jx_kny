/**
 * 机车检修作业计划 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){	
	Ext.namespace('JobProcessFirstNodeApplyWin'); 	
	JobProcessFirstNodeApplyWin.processTips = null;
	JobProcessFirstNodeApplyWin.item = {};
	 JobProcessFirstNodeApplyWin.chossForm = null;
	/*
	 * 显示处理遮罩
	 */
	JobProcessFirstNodeApplyWin.showtip = function(win,msg){
		JobProcessFirstNodeApplyWin.processTips = new Ext.LoadMask(win.getEl()||Ext.getBody(),{
			msg: msg || "正在处理你的操作，请稍后...",
			removeMask:true
		});
		JobProcessFirstNodeApplyWin.processTips.show();
	}
	/*
	 * 隐藏处理遮罩
	 */
	JobProcessFirstNodeApplyWin.hidetip = function(){
		JobProcessFirstNodeApplyWin.processTips.hide();
	}
	
	// 申请调整节点
	JobProcessFirstNodeApplyWin.applyChangeNode = function() {
		var applyForm = JobProcessFirstNodeApplyWin.applyForm.getForm();	
		if (!applyForm.isValid()) {
			MyExt.Msg.alert("请填写申请原因！");
			return;    
		}
		var data = applyForm.getValues();
		var cfg = {
	        url: ctx + "/jobProcessNodeUpdateApply!updateLeafNodeTimeApply.action", jsonData: data,
	        timeout: 600000,
	        success: function(response, options){
				if(JobProcessFirstNodeApplyWin.processTips) JobProcessFirstNodeApplyWin.hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	            	TrainWorkPlanEdit.store.reload(); // 重新加载vis节点
	                JobProcessFirstNodeApplyWin.applyWin.hide();
	                TrainWorkPlanFirstNodeEditWin.win.hide();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){	
	        	if(JobProcessFirstNodeApplyWin.processTips) JobProcessFirstNodeApplyWin.hidetip();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    JobProcessFirstNodeApplyWin.showtip(JobProcessFirstNodeApplyWin.applyWin);
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	}
	
	
	JobProcessFirstNodeApplyWin.applyForm = new Ext.form.FormPanel({
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
					fieldLabel: '原计划开始时间', name: 'planBeginTime',readOnly:true
				},{
					fieldLabel: '原计划结束时间', name: 'planEndTime',readOnly:true
				}]
		},{
			defaults: { 
				 xtype: 'my97date',format: "Y-m-d H:i",
				 width: 140 
			},
			items: [{
					
					fieldLabel: '现开始时间', name: 'newPlanBeginTime',readOnly:true
				},{
					fieldLabel: '现结束时间', name: 'newPlanEndTime',readOnly:true							
				}]
		},{
			columnWidth: 1,
			items: [{							
					fieldLabel: '申请说明', xtype: 'textarea',name: 'reason', allowBlank:false, width: 360, hight: 100
				}]
		 }],
		buttonAlign: 'center',
		buttons: [{
			text: "申请延期", iconCls: "saveIcon", scope: this, handler: function(){ JobProcessFirstNodeApplyWin.applyChangeNode(); }
		}, {
			text: "提交", iconCls: "addIcon", scope: this, handler: function(){ JobProcessFirstNodeApplyWin.applyWin.hide();
				TrainWorkPlanFirstNodeEditWin.saveFn(); }
		}, {
   			text: "取消", iconCls: "closeIcon", scope: this, handler: function(){ JobProcessFirstNodeApplyWin.applyWin.hide(); }
		}]
	});
	
	// 定义节点申请调整窗口
	JobProcessFirstNodeApplyWin.applyWin  = new Ext.Window({
			height: 260,
			width: 600,
			buttonAlign: 'center',
			closeAction: 'hide',
			layout: 'fit',
			title: "时间调整申请",
			items: [JobProcessFirstNodeApplyWin.applyForm],
			listeners: {
				beforeshow:function(window){
					var item = null;
					if( 1== JobProcessFirstNodeApplyWin.chossForm){
						 item = JobProcessNodeEditWin.form.getForm().getValues();
					}else if(2 ==  JobProcessFirstNodeApplyWin.chossForm){
						 item = JobProcessNode.form.getForm().getValues();
					}
					if(item){
						var nodeIDX = item.idx;
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
						// 编辑前的开始及结束时间
						window.find('name', 'planBeginTime')[0].setValue(item.newPlanBeginTime);
						window.find('name', 'planEndTime')[0].setValue(item.newPlanEndTime);
						// 显示编辑后的计划的开始及结束时间
						window.find('name', 'newPlanBeginTime')[0].setValue(item.planBeginTime);
						window.find('name', 'newPlanEndTime')[0].setValue(item.planEndTime);						
					}  
					return true;
				}
			}
		});
});