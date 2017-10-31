/**
 * 机务设备工单定义 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.ns('TrainAccessAccountOut');
	
	/** ******************** 定义全局变量开始 ******************** */
	TrainAccessAccountOut.labelWidth = 80;
	TrainAccessAccountOut.fieldWidth = 150;
	
	TrainAccessAccountOut.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });
	/** ******************** 定义全局变量结束 ******************** */
	
	/** **************** 定义机车入段保存表单开始 **************** */
	// 机车入段保存表单
	TrainAccessAccountOut.form = new Ext.form.FormPanel({
		title: '机车出段', iconCls: 'edit1Icon',
		padding:10,  frame: true, labelWidth:TrainAccessAccountOut.labelWidth,
		layout:'column', 
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.5,
			defaults: { xtype:'textfield', anchor:'95%'}
		},
		items:[{
			items:[{
    			fieldLabel: "车型", name: "trainTypeShortName", disabled: true, value: trainTypeShortName
    		},{
    			fieldLabel: "出段时间", name: "outTime", xtype: 'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, initNow: true,
    			allowBlank: false
    		},{
    			fieldLabel: "出段司机", name: "outDriver", maxLength:20
    		}]
		}, {
			items:[{
    			fieldLabel: "车号", name: "trainNo", disabled: true, value: trainNo
    		},{
    			fieldLabel: "出段车次", name: "outOrder", maxLength:20
    		}]
		}, {
			columnWidth:1,
			defaults: {
				xtype:'hidden', anchor:'95%'
			},
			items: [{fieldLabel: 'idx主键', name: 'idx', value: idx}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: "确认出段", iconCls: "saveIcon", id:'id_btn_save', handler: function() {
				TrainAccessAccountOut.saveFn();
			}
		}]
	});
	/** **************** 定义机车入段保存表单结束 **************** */
	
	/** ******************** 定义全局函数开始 ******************** */
	// 保存函数
	TrainAccessAccountOut.saveFn = function(){
		var form = TrainAccessAccountOut.form.getForm();
		if (!form.isValid()) {
			return;
		}
		var data = form.getValues();
		
		TrainAccessAccountOut.loadMask.show();
		Ext.Ajax.request({
			url: ctx + '/trainAccessAccount!saveOrUpdateOut.action',
			jsonData: Ext.util.JSON.encode(data),
	   	 	//请求成功后的回调函数
	        success: function(response, options){
	            if(TrainAccessAccountOut.loadMask)   TrainAccessAccountOut.loadMask.hide();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null) {
				    alertSuccess();
	            	// 禁止多次点击保存按钮
	            	Ext.getCmp('id_btn_save').disable();
	            	// 500ms后关闭父容器窗口
            		if (top.jDiaglog) top.jDiaglog.closeTime(1);
	            } else {
					alertFail(result.errMsg);
	            }
	        },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        TrainAccessAccountOut.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});
	}
	
	/** ******************** 定义全局函数结束 ******************** */
	// 页面自适应布局
	new Ext.Viewport({
		layout:'fit',
		items: [TrainAccessAccountOut.form]
	});
	
//	new Ext.Window({
//		title:'机车入段',
//		width:600, height: 200,
//		layout: 'fit',
//		items: [TrainAccessAccountOut.form],
//		buttonAlign: 'center',
//		buttons: [{
//			text: "保存", iconCls: "saveIcon", handler: function() {
//				TrainAccessAccountOut.isSaveAndAdd = false;
//				TrainAccessAccountOut.saveFn();
//			}
//		}, {
//			text: "保存并新增", iconCls: "addIcon", handler: function() {
//				TrainAccessAccountOut.isSaveAndAdd = true;
//				TrainAccessAccountOut.saveFn();
//			}
//		}]
//	}).show();
})