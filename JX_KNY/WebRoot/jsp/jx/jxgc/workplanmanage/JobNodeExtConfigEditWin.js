/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('JobNodeExtConfigEditWin'); 
	
	/** **************** 定义全局变量开始 **************** */
	JobNodeExtConfigEditWin.labelWidth = 100;
	JobNodeExtConfigEditWin.fieldWidth = 140;
//	JobNodeExtConfigEditWin.nodeIDX = "";
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	
	// 加载数据
	JobNodeExtConfigEditWin.loadFn = function(nodeIDX) {
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
			url: ctx + '/jobNodeExtConfig!list.action',
			params: {
				entityJson: Ext.util.JSON.encode({nodeIDX: nodeIDX})
			},
			success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		        	Ext.getCmp('train_status_edit_win').clearValue();
		        	Ext.getCmp('ext_check_control_value_edit_win').reset();
		        	Ext.getCmp('ext_check_ticket_value_edit_win').reset();
		        	
		            var list = result.root;
		            for (var i = 0; i < list.length; i++) {
		            	var entity = list[i];
		            	if (entity.configName ==  EXT_TRAIN_STATUS) {
		            		Ext.getCmp('train_status_idx_edit_win').setValue(entity.idx);
		            		Ext.getCmp('train_status_edit_win').enable();
		            		Ext.getCmp('train_status_edit_win').setValue(entity.configValue, entity.configValue);
		            		Ext.getCmp('train_status_edit_win').disable();
		            	} else if (entity.configName == EXT_CHECK_CONTROL) {
		            		Ext.getCmp('ext_check_control_idx_edit_win').setValue(entity.idx);
		            		Ext.getCmp('ext_check_control_value_edit_win').setValue(entity.configValue);
		            	} else if (entity.configName == EXT_CHECK_TICKET) {
		            		Ext.getCmp('ext_check_ticket_idx_edit_win').setValue(entity.idx);
		            		Ext.getCmp('ext_check_ticket_value_edit_win').setValue(entity.configValue);
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
	JobNodeExtConfigEditWin.form = new Ext.form.FormPanel({
		frame: true, labelWidth: JobNodeExtConfigEditWin.labelWidth, padding: 10,
		items: [{
			id: 'train_status_edit_win',
			fieldLabel: '机车状态', allowBlank: true, disabled: true,
			xtype: 'Base_combo', hiddenName: EXT_TRAIN_STATUS,
 			entity: 'com.yunda.twt.twtinfo.entity.TrainStatusColors', 
 			displayField:'status', valueField: 'status', 
 			queryHql:'', fields: ["idx", "status"]
		}, {
			id: 'ext_check_control_value_edit_win',
			fieldLabel: '质量检查卡控', disabled: true,
			xtype: 'combo', hiddenName: EXT_CHECK_CONTROL,  mode: 'local' ,
			valueField: "k", displayField: "v", triggerAction: "all",   		 	
        	editable: false, forceSelection: true, 
        	fields:['k','v'],
        	store:[[EXT_CHECK_CONTROL_NONE,"不卡控"],[EXT_CHECK_CONTROL_CURRENT,"卡控当前节点"],[EXT_CHECK_CONTROL_ALL,"卡控所有节点"]]	
		}, {
			id: 'ext_check_ticket_value_edit_win',
			fieldLabel: '检查提票卡控', disabled: true,
			xtype: 'combo', hiddenName: EXT_CHECK_TICKET,  mode: 'local' ,
			valueField: "k", displayField: "v", triggerAction: "all",   		 	
        	editable: false, forceSelection: true, 
        	fields:['k','v'],
        	store:[[EXT_CHECK_TICKET_NONE,"不卡控"],[EXT_CHECK_TICKET_CURRENT,"卡控当前节点"]]	
        }, {
			xtype:'container', defaultType:'hidden', layout: 'form', items: [{
				fieldLabel: '机车状态设置主键', id: 'train_status_idx_edit_win'
			}, {
				fieldLabel: '机车状态设置名称', id: 'train_status_name_edit_win', value: EXT_CHECK_CONTROL
			}, {
				fieldLabel: '机车状态设置描述', id: 'ext_train_status_desc_edit_win', value: '机车状态设置'
			}, {
				fieldLabel: '质量检查卡控主键', id: 'ext_check_control_idx_edit_win'
			}, {
				fieldLabel: '质量检查卡控名称', id: 'ext_check_control_name_edit_win', value: EXT_TRAIN_STATUS
			}, {
				fieldLabel: '质量检查卡控描述', id: 'ext_check_control_desc_edit_win', value: '质量检查卡控'
			}, {
				fieldLabel: '质量检查卡控主键', id: 'ext_check_ticket_idx_edit_win'
			}, {
				fieldLabel: '质量检查卡控名称', id: 'ext_check_ticket_name_edit_win', value: EXT_CHECK_TICKET
			}, {
				fieldLabel: '质量检查卡控描述', id: 'ext_check_ticket_desc_edit_win', value: '检查提票卡控'
			}]
		}]
	});
	/** **************** 定义保存表单结束 **************** */
})