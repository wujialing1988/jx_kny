/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('JobNodeExtConfigSearch'); 
	
	/** **************** 定义全局变量开始 **************** */
	JobNodeExtConfigSearch.labelWidth = 100;
	JobNodeExtConfigSearch.fieldWidth = 140;
	JobNodeExtConfigSearch.trainWorkPlanEditWin = null;
//	JobNodeExtConfigSearch.nodeIDX = "";
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 重置扩展配置保存表单
	JobNodeExtConfigSearch.resetFn = function() {
		Ext.getCmp('ext_train_status_idx_Search').setValue("");
    	Ext.getCmp('ext_train_status_value_Search').clearValue();
		Ext.getCmp('ext_check_control_idx_Search').setValue("");
    	Ext.getCmp('ext_check_control_value_Search').clearValue();
		Ext.getCmp('ext_check_ticket_idx_Search').setValue("");
    	Ext.getCmp('ext_check_ticket_value_Search').clearValue();
	}
	// 加载数据
	JobNodeExtConfigSearch.loadFn = function(nodeIDX) {
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
			url: ctx + '/jobNodeExtConfig!list.action',
			params: {
				entityJson: Ext.util.JSON.encode({nodeIDX: nodeIDX})
			},
			success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		        	JobNodeExtConfigSearch.resetFn(); 
		            var list = result.root;
		            for (var i = 0; i < list.length; i++) {
		            	var entity = list[i];
		            	if (entity.configName ==  EXT_TRAIN_STATUS) {
		            		Ext.getCmp('ext_train_status_idx_Search').setValue(entity.idx);
		            		Ext.getCmp('ext_train_status_value_Search').enable();
		            		Ext.getCmp('ext_train_status_value_Search').setValue(entity.configValue, entity.configValue);
		            		Ext.getCmp('ext_train_status_value_Search').disable();
		            	} else if (entity.configName == EXT_CHECK_CONTROL) {
		            		Ext.getCmp('ext_check_control_idx_Search').setValue(entity.idx);
		            		Ext.getCmp('ext_check_control_value_Search').setValue(entity.configValue);
		            	} else if (entity.configName == EXT_CHECK_TICKET) {
		            		Ext.getCmp('ext_check_ticket_idx_Search').setValue(entity.idx);
		            		Ext.getCmp('ext_check_ticket_value_Search').setValue(entity.configValue);
		            	}
		            }
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
		}));
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义保存表单开始 **************** */
	JobNodeExtConfigSearch.form = new Ext.form.FormPanel({
		frame: true, labelWidth: JobNodeExtConfigSearch.labelWidth, padding: 10,
		items: [{
			id: 'ext_train_status_value_Search',
			fieldLabel: '机车状态', allowBlank: true, disabled: true,
			xtype: 'Base_combo', hiddenName: EXT_TRAIN_STATUS,
 			entity: 'com.yunda.twt.twtinfo.entity.TrainStatusColors', 
 			displayField:'status', valueField: 'status', 
 			queryHql:'', fields: ["idx", "status"], disabled: true
		}, {
			id: 'ext_check_control_value_Search',
			fieldLabel: '质量检查卡控', disabled: true,
			xtype: 'combo', hiddenName: EXT_CHECK_CONTROL,  mode: 'local' ,
			valueField: "k", displayField: "v", triggerAction: "all",   		 	
        	editable: false, forceSelection: true, 
        	fields:['k','v'],
        	store:[[EXT_CHECK_CONTROL_NONE,"不卡控"],[EXT_CHECK_CONTROL_CURRENT,"卡控当前节点"],[EXT_CHECK_CONTROL_ALL,"卡控所有节点"]]	, disabled: true
		}, {
			id: 'ext_check_ticket_value_Search',
			fieldLabel: '检查提票卡控', disabled: true,
			xtype: 'combo', hiddenName: EXT_CHECK_TICKET,  mode: 'local' ,
			valueField: "k", displayField: "v", triggerAction: "all",   		 	
        	editable: false, forceSelection: true, 
        	fields:['k','v'],
        	store:[[EXT_CHECK_TICKET_NONE,"不卡控"],[EXT_CHECK_TICKET_CURRENT,"卡控当前节点"]], disabled: true
        }, {
			xtype:'container', defaultType:'hidden', layout: 'form', items: [{
				fieldLabel: '机车状态设置主键', id: 'ext_train_status_idx_Search'
			}, {
				fieldLabel: '机车状态设置名称', id: 'ext_train_status_name_Search', value: EXT_CHECK_CONTROL
			}, {
				fieldLabel: '机车状态设置描述', id: 'ext_train_status_desc_Search', value: '机车状态设置'
			}, {
				fieldLabel: '质量检查卡控主键', id: 'ext_check_control_idx_Search'
			}, {
				fieldLabel: '质量检查卡控名称', id: 'ext_check_control_name_Search', value: EXT_TRAIN_STATUS
			}, {
				fieldLabel: '质量检查卡控描述', id: 'ext_check_control_desc_Search', value: '质量检查卡控'
			}, {
				fieldLabel: '检查提票卡控主键', id: 'ext_check_ticket_idx_Search'
			}, {
				fieldLabel: '检查提票卡控名称', id: 'ext_check_ticket_name_Search', value: EXT_CHECK_TICKET
			}, {
				fieldLabel: '检查提票卡控描述', id: 'ext_check_ticket_desc_Search', value: '检查提票卡控'
			}]
		}],
		
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				if(JobNodeExtConfigSearch.trainWorkPlanEditWin){
	    			JobNodeExtConfigSearch.trainWorkPlanEditWin.win.hide();
	    		}
	    		if(typeof(NodeAndWorkCardEditSearch)!='undefined' && NodeAndWorkCardEditSearch.win){
					NodeAndWorkCardEditSearch.win.hide();
	    		}
			}
		}]
	});
	/** **************** 定义保存表单结束 **************** */
})