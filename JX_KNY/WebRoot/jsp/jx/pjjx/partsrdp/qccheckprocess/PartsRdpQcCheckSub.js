/**
 * 检修作业流程 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace('PartsRdpQcCheckSub'); 
	
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpQcCheckSub.labelWidth = 80;
	PartsRdpQcCheckSub.fieldWidth = 140;
	PartsRdpQcCheckSub.index = -1;					// 用以记录当前正在处理的【检修记录单】索引
	PartsRdpQcCheckSub.idx = "####";				// 用以记录当前正在处理的【检修记录单】主键
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全函数量开始 ************** */
	
	// 设置【检修记录单处理】窗口的表单数据
	PartsRdpQcCheckSub.initFn = function(record) {
    	// 设置【工单编号】、【工单描述】字段值
        PartsRdpQcCheckSub.baseForm.getForm().loadRecord(record);
    	// 设置【开工时间】、【完工时间】字段值
        PartsRdpQcCheckSub.saveForm.getForm().loadRecord(record);
    	
    	// 设置当前正在处理的【检修记录单】主键
    	PartsRdpQcCheckSub.idx = record.get('idx');
    	
    	var rdpIDX = record.get('rdpIDX');						// 作业主键
    	var rdpNodeIDX = record.get('rdpNodeIDX');				// 作业节点主键
    	var recordCardIDX = record.get('recordCardIDX');		// 工艺卡主键
    	
    	// 加载【检修检测项】表格数据
    	PartsRdpRecordRI.rdpRecordCardIDX = PartsRdpQcCheckSub.idx;
    	PartsRdpRecordRI.grid.store.load();
	}
	/** ************** 定义全函数量结束 ************** */

	// 【检修记录单处理】窗口 - 【检修记录单】基本信息表单
	PartsRdpQcCheckSub.baseForm = new Ext.form.FormPanel({
		labelWidth:PartsRdpQcCheckSub.labelWidth,
		border:false, baseCls:'x-plain',
		labelAlign:"left",
		layout:"column", 
		bodyStyle:'padding: 0 10px;',
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.25,
			defaults:{
				style: 'border:none;background:none', xtype:"textfield", anchor:'100%', readOnly: true
			}
		},
		items:[{
			columnWidth:0.175,
			items:[{
				fieldLabel:"配件编号", name:"partsNo"
			}]
		}, {
			columnWidth:0.175,
			items:[{
				fieldLabel:"配件名称", name:"partsName"
			}]
		}, {
			columnWidth:0.4,
			items:[{
				fieldLabel:"规格型号", name:"specificationModel"
			}]
		}, {
			columnWidth:0.25,
			items:[{
				fieldLabel:"扩展编号", name:"extendNo"
			}]
		}, {
			columnWidth:0.175,
			items:[{
				fieldLabel:"工单编号", name:'recordCardNo'
			}]
		}, {
			columnWidth:0.75,
			items:[{
				fieldLabel:"工单描述", name:'recordCardDesc'
			}]
		}, {
			columnWidth:1,
			items:[{
				fieldLabel:"检修情况描述", name:'remarks'
			}]
		}]
							
	});
	
	// 【检修记录单处理】窗口 - 【检修记录单】保存表单
	PartsRdpQcCheckSub.saveForm = new Ext.form.FormPanel({
		layout:"column", style:'padding: 5 10px;', labelWidth:PartsRdpQcCheckSub.labelWidth,
		defaults:{
			xtype:"container", autoEl:"div", layout:"form",
			defaults: {
				width:PartsRdpQcCheckSub.fieldWidth
			}
		},
		items:[{
			columnWidth:.8,
			items:[{
				name:'qRResult', fieldLabel:"检验结果", xtype:'textarea', anchor: '90%', height: 65, allowBlank: true, maxLength: 500
			}]
		}/*, {
			columnWidth:.3,
			items:[{
				name:'qcItemName', xtype:"textfield", fieldLabel:"质量检查项", disable: true
			}]
		}*/]
	})
	
	// 【检修记录单处理】窗口
	PartsRdpQcCheckSub.win = new Ext.Window({
		title:"检修记录质量检验",
		width:1200, height:700, layout:"fit",
		closeAction:'hide',
		plain: true, modal: true,
		items:[{
			xtype:"panel",
			border: false,
			layout:"border",
			tbar:[ {
				text:'签名提交', id:'lhBtn_RecordCard',iconCls:'startIcon', handler: function() {
					Ext.Msg.confirm('提示', '是否确认提交?', function(btn){
						if (btn == 'yes') {
							// 配件检修记录单实例主键
							var rdpRecordCardIDXs = [PartsRdpQcCheckSub.idx];			
							// 检验结果
							var qRResult = PartsRdpQcCheckSub.saveForm.find('name', 'qRResult')[0].getValue();
							PartsRdpQcCheckProcess.signFn(rdpRecordCardIDXs, PartsRdpQcCheckProcess.qcItemNo, qRResult, function(index){
								PartsRdpQcCheckSub.index = index;
								PartsRdpQcCheckProcess.grid.store.reload();
								if (PartsRdpQcCheckSub.index >= PartsRdpQcCheckProcess.grid.store.getCount()) {
									PartsRdpQcCheckSub.win.hide();
								} else {
									var record = PartsRdpQcCheckProcess.grid.store.getAt(PartsRdpQcCheckSub.index);
									PartsRdpQcCheckSub.initFn(record);
								}
							});
						}
					});
				}
			},{
				text:'返修', id:'xhBtn_RecordCard', iconCls:'checkIcon', handler: function(){
					Ext.Msg.confirm('提示', '是否确认返修？', function(btn) {
						if (btn == 'yes') {
							// 配件检修记录单实例主键
							var rdpRecordCardIDXs = [PartsRdpQcCheckSub.idx];			
							PartsRdpQcCheckProcess.backFn(rdpRecordCardIDXs, PartsRdpQcCheckProcess.qcItemNo, function(index){
								PartsRdpQcCheckSub.index = index;
								PartsRdpQcCheckProcess.grid.store.reload();
								if (PartsRdpQcCheckSub.index >= PartsRdpQcCheckProcess.grid.store.getCount()) {
									PartsRdpQcCheckSub.win.hide();
								} else {
									var record = PartsRdpQcCheckProcess.grid.store.getAt(PartsRdpQcCheckSub.index);
									PartsRdpQcCheckSub.initFn(record);
								}
							});
						}
					});
				}
			}, {
				text:'提票', id:'zcBtn_RecordCard', iconCls:'saveIcon', handler:function(){
					// 显示提票处理窗口
					PartsRdpQcCheckProcess.insertNoticeWin.show();
				}
			}, {
				text:'关闭', iconCls:'closeIcon', handler:function(){
					this.findParentByType('window').hide();
				}
			}, '->', {
				text:'上一工单', iconCls:'moveUpIcon', handler: function(){
					
					PartsRdpQcCheckSub.index = parseInt(PartsRdpQcCheckSub.index) - 1;
					if (PartsRdpQcCheckSub.index < 0) {
						PartsRdpQcCheckSub.index = 0;
						MyExt.Msg.alert('已经是第一条工单');
					} else {
						var record = PartsRdpQcCheckProcess.grid.store.getAt(PartsRdpQcCheckSub.index);
						PartsRdpQcCheckSub.initFn(record);
					}
				}
			}, {
				text:'下一工单', iconCls:'moveDownIcon', handler:function(){
					PartsRdpQcCheckSub.index = 1 + parseInt(PartsRdpQcCheckSub.index);
					if (PartsRdpQcCheckSub.index >= PartsRdpQcCheckProcess.grid.store.getCount()) {
						PartsRdpQcCheckSub.index = PartsRdpQcCheckProcess.grid.store.getCount() - 1;
						MyExt.Msg.alert('已经是最后一条工单');
					} else {
						var record = PartsRdpQcCheckProcess.grid.store.getAt(PartsRdpQcCheckSub.index);
						PartsRdpQcCheckSub.initFn(record);
					}
				}
			}],
			items:[{
				xtype:"container", autoEl:"div", region:"north", height:220, layout:"border",
				defaults:{layout:'fit'},
				items:[{
					title:"<span style='font-weight:normal;'>检修记录单基本信息<span>",
					region:"north",
					height:125,
					frame:true,
//					margins:"10px 0 0 0",
					collapseFirst: false, collapsible: true,
					items:[PartsRdpQcCheckSub.baseForm]
				}, {
					region:"center", height:125, 
					frame:true,
					items:PartsRdpQcCheckSub.saveForm
				}]
			}, {
				title:'检修检测项',				
				xtype:"panel",  region:"center", layout:'border',
				defaults: {xtype:"panel", layout:'fit'},
				items:[{
					region:'west',
					width: 600,
					items:[PartsRdpRecordRI.grid]
				}, {
					region:'center',
					layout:'border',
					items:[{
						region:'north',
						layout:'fit',
						height: 150,
						frame:true,
						items:[PartsRdpRecordRI.form]
					}, {
						region:'center',
						layout:'fit',
						items:[PartsRdpRecordDI.grid]
					}]
				}]
			}]
		}],
		listeners: {
			hide: function(p) {
				PartsRdpQcCheckProcess.grid.store.reload();
			},
			show: function(win) {
				win.setTitle("质量检验<span>(" + PartsRdpQcCheckProcess.qcItemName + ")</span>");
			}
		}
	})
})