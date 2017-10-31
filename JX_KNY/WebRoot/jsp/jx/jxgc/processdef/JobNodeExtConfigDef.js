/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('JobNodeExtConfigDef'); 
	
	/** **************** 定义全局变量开始 **************** */
	JobNodeExtConfigDef.labelWidth = 100;
	JobNodeExtConfigDef.fieldWidth = 140;
	JobNodeExtConfigDef.nodeIDX = "";
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 保存
	JobNodeExtConfigDef.saveFn = function() {
    	var array = [];
    	// 机车状态设置
    	var jobNodeExtConfigDef = null;
		var idx = Ext.getCmp('ext_train_status_idx').getValue();
    	var ext_train_status = Ext.getCmp('ext_train_status_value').getValue();
		if (!Ext.isEmpty(idx) ||　!Ext.isEmpty(ext_train_status)){
			jobNodeExtConfigDef = {};
    		jobNodeExtConfigDef.idx = idx;
			jobNodeExtConfigDef.nodeIDX = JobNodeExtConfigDef.nodeIDX;
			jobNodeExtConfigDef.configName = EXT_TRAIN_STATUS;
			jobNodeExtConfigDef.configDesc = '机车状态设置';
			jobNodeExtConfigDef.configValue = ext_train_status;
			array.push(jobNodeExtConfigDef);
		}
    	// 质量检查卡控设置
		idx = Ext.getCmp('ext_check_control_idx').getValue();
    	var ext_check_control = Ext.getCmp('ext_check_control_value').getValue();
    	if (!Ext.isEmpty(idx) ||　!Ext.isEmpty(ext_check_control)){
			jobNodeExtConfigDef = {};
    		jobNodeExtConfigDef.idx = idx;
			jobNodeExtConfigDef.nodeIDX = JobNodeExtConfigDef.nodeIDX;
			jobNodeExtConfigDef.configName = EXT_CHECK_CONTROL;
			jobNodeExtConfigDef.configDesc = '质量检查卡控';
			jobNodeExtConfigDef.configValue = ext_check_control;
			array.push(jobNodeExtConfigDef);
    	}
    	// 检查提票卡控设置
		idx = Ext.getCmp('ext_check_ticket_idx').getValue();
    	var ext_check_ticket = Ext.getCmp('ext_check_ticket_value').getValue();
    	if (!Ext.isEmpty(idx) ||　!Ext.isEmpty(ext_check_ticket)){
			jobNodeExtConfigDef = {};
    		jobNodeExtConfigDef.idx = idx;
			jobNodeExtConfigDef.nodeIDX = JobNodeExtConfigDef.nodeIDX;
			jobNodeExtConfigDef.configName = EXT_CHECK_TICKET;
			jobNodeExtConfigDef.configDesc = '检查提票卡控';
			jobNodeExtConfigDef.configValue = ext_check_ticket;
			array.push(jobNodeExtConfigDef);
    	}
    	
    	if (array.length <= 0) {
    		MyExt.Msg.alert('没有可以保存的项目！');
    		return;
    	}
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
			url: ctx + '/jobNodeExtConfigDef!save.action',
			jsonData: Ext.util.JSON.encode(array),
			success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            var list = result.list;
		            for (var i = 0; i < list.length; i++) {
		            	var entity = list[i];
		            	if (entity.configName == EXT_TRAIN_STATUS) {
		            		Ext.getCmp('ext_train_status_idx').setValue(entity.idx);
		            	} else if (entity.configName == EXT_CHECK_CONTROL) {
		            		Ext.getCmp('ext_check_control_idx').setValue(entity.idx);
		            	} else if (entity.configName == EXT_CHECK_TICKET) {
		            		Ext.getCmp('ext_check_ticket_idx').setValue(entity.idx);
		            	}
		            }
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
		}));
	}
	
	// 加载数据
	JobNodeExtConfigDef.loadFn = function(nodeIDX) {
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
			url: ctx + '/jobNodeExtConfigDef!list.action',
			params: {
				entityJson: Ext.util.JSON.encode({nodeIDX: nodeIDX})
			},
			success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		        	JobNodeExtConfigDef.resetFn();
		            var list = result.root;
		            for (var i = 0; i < list.length; i++) {
		            	var entity = list[i];
		            	if (entity.configName == EXT_TRAIN_STATUS) {
		            		Ext.getCmp('ext_train_status_idx').setValue(entity.idx);
		            		Ext.getCmp('ext_train_status_value').setValue(entity.configValue);
		            	} else if (entity.configName == EXT_CHECK_CONTROL) {
		            		Ext.getCmp('ext_check_control_idx').setValue(entity.idx);
		            		Ext.getCmp('ext_check_control_value').setValue(entity.configValue);
		            	} else if (entity.configName == EXT_CHECK_TICKET) {
		            		Ext.getCmp('ext_check_ticket_idx').setValue(entity.idx);
		            		Ext.getCmp('ext_check_ticket_value').setValue(entity.configValue);
		            	}
		            }
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
		}));
	}
	
	// 重置扩展配置保存表单
	JobNodeExtConfigDef.resetFn = function() {
		Ext.getCmp('ext_train_status_idx').setValue("");
    	Ext.getCmp('ext_train_status_value').clearValue();
		Ext.getCmp('ext_check_control_idx').setValue("");
    	Ext.getCmp('ext_check_control_value').clearValue();
		Ext.getCmp('ext_check_ticket_idx').setValue("");
    	Ext.getCmp('ext_check_ticket_value').clearValue();
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义保存表单开始 **************** */
	JobNodeExtConfigDef.form = new Ext.form.FormPanel({
		frame: true, labelWidth: JobNodeExtConfigDef.labelWidth, padding: 10,
		items: [{
			xtype: 'compositefield', fieldLabel: '机车状态设置', combineErrors: false,
			items: [{
				id: 'ext_train_status_value',
				xtype: 'Base_combo', hiddenName: EXT_TRAIN_STATUS,
	 			entity: 'com.yunda.twt.twtinfo.entity.TrainStatusColors', 
	 			displayField:'status', valueField: 'status', 
	 			queryHql:'', fields: ["idx", "status"]
			}, {
				xtype: 'button', text: '重置', iconCls:'resetIcon', handler: function() {
					Ext.getCmp('ext_train_status_value').clearValue();
				}
			}]
		}, {
			xtype: 'compositefield', fieldLabel: '质量检查卡控', combineErrors: false,
			items: [{
				id: 'ext_check_control_value',
				xtype: 'combo', hiddenName: EXT_CHECK_CONTROL,  mode: 'local' ,
				valueField: "k", displayField: "v", triggerAction: "all",   		 	
	        	editable: false, forceSelection: true, 
	        	fields:['k','v'],
	        	store:[[EXT_CHECK_CONTROL_NONE,"不卡控"],[EXT_CHECK_CONTROL_CURRENT,"卡控当前节点"],[EXT_CHECK_CONTROL_ALL,"卡控所有节点"]]	
	        }, {
				xtype: 'button', text: '重置', iconCls:'resetIcon', handler: function() {
					Ext.getCmp('ext_check_control_value').clearValue();
				}
			}]
		}, {
			xtype: 'compositefield', fieldLabel: '检查提票卡控', combineErrors: false,
			items: [{
				id: 'ext_check_ticket_value',
				xtype: 'combo', hiddenName: EXT_CHECK_TICKET,  mode: 'local' ,
				valueField: "k", displayField: "v", triggerAction: "all",   		 	
	        	editable: false, forceSelection: true, 
	        	fields:['k','v'],
	        	store:[[EXT_CHECK_TICKET_NONE,"不卡控"],[EXT_CHECK_TICKET_CURRENT,"卡控当前节点"]]	
	        }, {
				xtype: 'button', text: '重置', iconCls:'resetIcon', handler: function() {
					Ext.getCmp('ext_check_ticket_value').clearValue();
				}
			}]
		}, {
			xtype:'container', defaultType:'hidden', layout: 'form', 
			defaults: {anchor:'100%'},
			items: [{
				fieldLabel: '机车状态设置主键', id: 'ext_train_status_idx'
			}, {
				fieldLabel: '质量检查卡控主键', id: 'ext_check_control_idx'
			}, {
				fieldLabel: '检查提票卡控主键', id: 'ext_check_ticket_idx'
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '保存', iconCls: 'saveIcon', handler: JobNodeExtConfigDef.saveFn
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	/** **************** 定义保存表单结束 **************** */
})