
Ext.onReady(function(){

	Ext.ns('PlatformTaskItem');
	
	/** **************** 定义全局变量开始 **************** */
	PlatformTaskItem.labelWidth = 100;
	PlatformTaskItem.fieldWidth = 140;
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义保存表单开始 **************** */
	PlatformTaskItem.saveForm = new Ext.form.FormPanel({
		labelWidth:PlatformTaskItem.labelWidth,
		frame: true,
		labelAlign:"left",
		layout:"column",
		padding:"10px",
		defaults: {
			xtype:"container",
			autoEl:"div",
			layout:"form",
			columnWidth:0.5
		},
		items:[{
			defaults: {
				xtype: 'textfield', anchor:"100%"
			},
			items:[{
				allowBlank: false,
				id: 'trainType_combo', xtype: 'TrainType_combo', fieldLabel:"车型", hiddenName: 'trainTypeIdx',
				displayField: 'shortName', valueField: 'typeID', pageSize: 20,
				returnField: [{widgetId: "trainType", propertyName: "shortName"}],
				editable: true,
				listeners: {
					select: function() {
						var trainNo_combo = Ext.getCmp('trainNo_combo');
						trainNo_combo.reset();
						trainNo_combo.clearValue();
						trainNo_combo.queryParams = {"trainTypeIDX" : this.getValue};
						trainNo_combo.cascadeStore();
					}
				}
			}, {
				allowBlank: false,
				fieldLabel:"机车台位", hiddenName: 'platformTaskIdx',
				xtype: 'Base_combo',
  				entity: 'com.yunda.vis.entity.PlatformTask', fields:["idx", "taskName"],
  				displayField: 'taskName', valueField: 'idx'
			}, {
				allowBlank: false,
				xtype:"my97date", format: 'Y-m-d H:i', name: 'startTime',
				fieldLabel:"开始时间"
			}]
		}, {
			defaults: {
				xtype: 'textfield', anchor:"100%"
			},
			items:[{
				allowBlank: false,
				id: 'trainNo_combo', fieldLabel:"车号", hiddenName: 'trainNo', editable: true,
				xtype: 'TrainNo_combo', displayField: 'trainNo', valueField: 'trainNo',
				listeners: {
					beforequery: function() {
						var trainTypeIdx = Ext.getCmp('trainType_combo').getValue();
						if (Ext.isEmpty(trainTypeIdx)) {
							MyExt.Msg.alert('请先选择上车车型！');
							return false;
						}
					}
				}
			}, {
				fieldLabel:"显示信息", name: 'displayInfo'
			}, {
				xtype:"my97date", format: 'Y-m-d H:i', name: 'endTime', initNow: false,
				fieldLabel:"结束时间"
			}]
		}, {
			columnWidth:1,
			defaults: {
				xtype: 'hidden', anchor:"100%"
			},
			items:[{
				fieldLabel:"IDX主键", name: 'idx'
			}, {
				fieldLabel:"车型简称", name: 'trainType', id: 'trainType'
			}, {
				fieldLabel:"任务状态", name: 'status'
			}]
		}]
	})
	/** **************** 定义保存表单结束 **************** */
	
	PlatformTaskItem.saveFn = function(saveAndAdd) {
		var form = PlatformTaskItem.saveForm.getForm();
		if (!form.isValid()) {
			return;
		}
		var data = form.getValues();
		var cfg = {
			scope: PlatformTaskItem.saveForm,
            url: ctx + '/platformTaskItem!saveOrUpdate.action',
            jsonData: data,
            success: function(response, options){
                if(self.loadMask)   self.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
	            	VisTest.store.reload(); 
	            	if (saveAndAdd) {
	            		this.getForm().reset();
						this.find('hiddenName', 'trainTypeIdx')[0].clearValue();
						this.find('hiddenName', 'trainNo')[0].clearValue();
						this.find('name', 'status')[0].setValue(STATUS_WKG);
	            	} else {
	            		this.find('name', 'idx')[0].setValue(result.entity.idx);
	            	}
                } else {
                     alertFail(result.errMsg);
                }
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	}
	
	PlatformTaskItem.saveWin = new Ext.Window( {
		width:618,
		height:185,
		frame: true,
		layout: 'fit',
		closeAction: 'hide',
		items: [PlatformTaskItem.saveForm],
		buttonAlign: 'center',
		buttons: [{
			text:'保存', iconCls: 'saveIcon', handler: function() {
				PlatformTaskItem.saveFn(false);
				
			}
		}, {
			text:'保存并新增', iconCls: 'addIcon', handler: function() {
				PlatformTaskItem.saveFn(true);
			}
		}, {
			text:'关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}],
		listeners: {
			beforeshow: function(window) {
				var item = PlatformTaskItem.item;
				if (item) {
					window.find('name', 'idx')[0].setValue(item.id);
					window.find('name', 'status')[0].setValue(item.status);
					window.find('name', 'startTime')[0].setValue(new Date(item.start).format('Y-m-d H:i'));
					var endTimeCmp = window.find('name', 'endTime')[0];
					if (item.end) {
//						endTimeCmp.enable();
//						endTimeCmp.show();
						endTimeCmp.setValue(new Date(item.end).format('Y-m-d H:i'));
					} else {
						endTimeCmp.reset();
//						endTimeCmp.disable();
//						endTimeCmp.hide();
					}
					window.find('name', 'displayInfo')[0].setValue(item.displayInfo);
					window.find('name', 'trainType')[0].setValue(item.trainType);
					var group = VisTest.groups.get(item.group);
					window.find('hiddenName', 'platformTaskIdx')[0].setDisplayValue(group.id, group.taskName);
					window.find('hiddenName', 'trainTypeIdx')[0].setDisplayValue(item.trainTypeIdx, item.trainType);
					window.find('hiddenName', 'trainNo')[0].setDisplayValue(item.trainNo, item.trainNo);
				}
				return true;
			}
		}
	});
});