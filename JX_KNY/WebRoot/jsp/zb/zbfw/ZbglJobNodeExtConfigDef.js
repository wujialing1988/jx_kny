/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('ZbglJobNodeExtConfigDef'); 
	
	/** **************** 定义全局变量开始 **************** */
	ZbglJobNodeExtConfigDef.labelWidth = 120;
	ZbglJobNodeExtConfigDef.fieldWidth = 140;
	ZbglJobNodeExtConfigDef.nodeIDX = "";
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 保存
	ZbglJobNodeExtConfigDef.saveFn = function() {
    	var array = [];
    	// 机车状态设置
    	var zbglJobNodeExtConfigDef = null;
		var idx = Ext.getCmp('ext_train_status_idx').getValue();
    	var ext_train_status = Ext.getCmp('ext_train_status_value').getValue();
		if (!Ext.isEmpty(idx) ||　!Ext.isEmpty(ext_train_status)){
			zbglJobNodeExtConfigDef = {};
    		zbglJobNodeExtConfigDef.idx = idx;
			zbglJobNodeExtConfigDef.nodeIDX = ZbglJobNodeExtConfigDef.nodeIDX;
			zbglJobNodeExtConfigDef.configName = EXT_TRAIN_STATUS;
			zbglJobNodeExtConfigDef.configDesc = '机车状态设置';
			zbglJobNodeExtConfigDef.configValue = ext_train_status;
			array.push(zbglJobNodeExtConfigDef);
		}
    	// 机车状态设置
		idx = Ext.getCmp('ext_check_control_idx').getValue();
    	var ext_check_control = Ext.getCmp('ext_check_control_value').getValue();
    	if (!Ext.isEmpty(idx) ||　!Ext.isEmpty(ext_check_control)){
			zbglJobNodeExtConfigDef = {};
    		zbglJobNodeExtConfigDef.idx = idx;
			zbglJobNodeExtConfigDef.nodeIDX = ZbglJobNodeExtConfigDef.nodeIDX;
			zbglJobNodeExtConfigDef.configName = EXT_DICT_TYPE;
			zbglJobNodeExtConfigDef.configDesc = '整备范围数据字典';
			zbglJobNodeExtConfigDef.configValue = ext_check_control;
			array.push(zbglJobNodeExtConfigDef);
    	}
    	if (array.length <= 0) {
    		MyExt.Msg.alert('没有可以保存的项目！');
    		return;
    	}
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
			url: ctx + '/zbglJobNodeExtConfigDef!save.action',
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
		            	} else if (entity.configName == EXT_DICT_TYPE) {
		            		Ext.getCmp('ext_check_control_idx').setValue(entity.idx);
		            	}
		            }
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
		}));
	}
	
	// 加载数据
	ZbglJobNodeExtConfigDef.loadFn = function(nodeIDX) {
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
			url: ctx + '/zbglJobNodeExtConfigDef!list.action',
			params: {
				entityJson: Ext.util.JSON.encode({nodeIDX: nodeIDX})
			},
			success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		        	ZbglJobNodeExtConfigDef.resetFn();
		            var list = result.root;
		            for (var i = 0; i < list.length; i++) {
		            	var entity = list[i];
		            	if (entity.configName == EXT_TRAIN_STATUS) {
		            		Ext.getCmp('ext_train_status_idx').setValue(entity.idx);
		            		Ext.getCmp('ext_train_status_value').setValue(entity.configValue);
		            	} else if (entity.configName == EXT_DICT_TYPE) {
		            		Ext.getCmp('ext_check_control_idx').setValue(entity.idx);
		            		Ext.getCmp('ext_check_control_value').setValue(entity.configValue);
		            	}
		            }
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
		}));
	}
	
	// 重置扩展配置保存表单
	ZbglJobNodeExtConfigDef.resetFn = function() {
		Ext.getCmp('ext_train_status_idx').setValue("");
    	Ext.getCmp('ext_train_status_value').clearValue();
		Ext.getCmp('ext_check_control_idx').setValue("");
    	Ext.getCmp('ext_check_control_value').clearValue();
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义保存表单开始 **************** */
	ZbglJobNodeExtConfigDef.form = new Ext.form.FormPanel({
		frame: true, labelWidth: ZbglJobNodeExtConfigDef.labelWidth, padding: 10,
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
			xtype: 'compositefield', fieldLabel: '整备范围数据字典', combineErrors: false,
			items: [
		        { id: 'ext_check_control_value',xtype: 'EosDictEntry_combo', hiddenName: EXT_DICT_TYPE,
						  displayField:'dictname',valueField:'dictname',dicttypeid: 'JCZB_ZBFW'}, 
			    {
					xtype: 'button', text: '重置', iconCls:'resetIcon', handler: function() {
						Ext.getCmp('ext_check_control_value').clearValue();
					}
				}]
		}, {
			xtype:'container', defaultType:'hidden', layout: 'form', 
			defaults: {anchor:'100%'},
			items: [{
				fieldLabel: '机车状态设置主键', id: 'ext_train_status_idx'
			}, {
				fieldLabel: '质量检查卡控主键', id: 'ext_check_control_idx'
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '保存', iconCls: 'saveIcon', handler: ZbglJobNodeExtConfigDef.saveFn
		}, {
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	/** **************** 定义保存表单结束 **************** */
})