/**
 * 检修作业流程 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace('RecordCardProcess'); 
	
	
	/** ************** 定义全局变量开始 ************** */
	RecordCardProcess.labelWidth = 100;
	RecordCardProcess.fieldWidth = 140;
	RecordCardProcess.index = -1;				// 用以记录当前正在处理的【检修记录单】索引
	RecordCardProcess.idx = "####";				// 用以记录当前正在处理的【检修记录单】主键
	
	RecordCardProcess.idFlag = 'qcEmpID_';					// 人员ID前缀
	RecordCardProcess.nameFlag = 'qcEmpName_';				// 人员名称前缀
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全函数量开始 ************** */
	
	// 设置【检修记录单处理】窗口的表单数据
	RecordCardProcess.initFn = function(record) {
		
    	// 设置【工单编号】、【工单描述】字段值
        RecordCardProcess.baseForm.getForm().loadRecord(record);
    	// 设置【开工时间】、【完工时间】字段值
        RecordCardProcess.saveForm.getForm().reset();
        RecordCardProcess.saveForm.getForm().loadRecord(record);
        
        
        // 动态渲染需指派的质量检查人员设置组件
        // 移除动态渲染panel面板中的所有组件
        Ext.getCmp('panel_Core').removeAll();
        
        // 获取记录工单的所有质量检查项如：“互检|质检|工长检”
        var qcContent = record.get('qcContent');
        RecordCardProcess.qcItemNos = new Array();
        if (!Ext.isEmpty(qcContent)) {
        	Ext.Ajax.request({
				url: ctx + '/partsRdpQR!getModelByRdpRecordCardIDX.action',
				params: {rdpRecordCardIDX: record.get('idx')},
				success: function(response, options){
			        var result = Ext.util.JSON.decode(response.responseText);
			        if (result.errMsg == null) {
			        	var qrList = result.qrList;
			        	for (var j = 0; j < qrList.length; j++) {
				        	var panel = {};
							panel.items=[];
							var item = {};
									
							item.xtype = 'textfield';
							item.fieldLabel = qrList[j].qCItemName;
							item.value = qrList[j].qREmpName;
							item.disabled = true;
							item.id = qrList[j].qCItemNo + "_B";
							panel.items.push(item); 
		         			Ext.getCmp('panel_Core').add(panel);
			        	}
			        		
						Ext.getCmp('panel_Core').doLayout();
					}
				},
				failure: function(response, options){
					MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
        }
    	
    	// 设置当前正在处理的【检修记录单】主键
    	RecordCardProcess.idx = record.get('idx');
    	
    	// 加载【检修检测项】表格数据
    	PartsRdpRecordRI.rdpRecordCardIDX = RecordCardProcess.idx;
    	PartsRdpRecordRI.grid.store.load();
    	
		// 根据记录状态激活（禁用）相应的功能处理按钮
    	RecordCardProcess.saveForm.find('name', 'remarks')[0].disable();
    	
	}
	
	// 【上一工单】工具栏按钮触发的函数处理，如果还有上一条工单，则返回true，否则返回false
	RecordCardProcess.previousRecordCardFn = function() {
		if (RecordCardProcess.index <= 0) {
			return false;
		} 
		RecordCardProcess.index = parseInt(RecordCardProcess.index) - 1;
		var record = PartsRdpRecordCard.grid.store.getAt(RecordCardProcess.index);
		RecordCardProcess.initFn(record);
		return true;
	}
	
	// 【上一工单】工具栏按钮触发的函数处理，如果还有下一条工单，则返回true，否则返回false
	RecordCardProcess.nextRecordCardFn = function() {
		if (RecordCardProcess.index == PartsRdpRecordCard.grid.store.getCount() - 1) {
			return false;
		}
		RecordCardProcess.index = parseInt(RecordCardProcess.index) + 1;
		var record = PartsRdpRecordCard.grid.store.getAt(RecordCardProcess.index);
		RecordCardProcess.initFn(record);
		return true;
	}
	

	// 【检修记录单处理】窗口 - 【检修记录单】基本信息表单
	RecordCardProcess.baseForm = new Ext.form.FormPanel({
		labelWidth:RecordCardProcess.labelWidth,
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
			items:[{
				fieldLabel:"配件编号", name:"partsNo"
			}]
		}, {
			items:[{
				fieldLabel:"配件名称", name:"partsName"
			}]
		}, {
			items:[{
				fieldLabel:"规格型号", name:"specificationModel"
			}]
		}, {
			items:[{
				fieldLabel:"扩展编号", name:"extendNo"
			}]
		}, {
			items:[{
				fieldLabel:"工单编号", name:'recordCardNo'
			}]
		}, {
			columnWidth:0.5,
			items:[{
				fieldLabel:"工单描述", name:'recordCardDesc'
			}]
		}, {
			columnWidth:0.25,
			items:[{
				fieldLabel:"质量检查", name:'qcContent'
			}]
		}]
							
	});
	
	// 【检修记录单处理】窗口 - 【检修记录单】保存表单
	RecordCardProcess.saveForm = new Ext.form.FormPanel({
		layout:"column", style:'padding: 5 10px;', labelWidth:RecordCardProcess.labelWidth,
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.25,
			defaults: {
				width:RecordCardProcess.fieldWidth
			}
		},
		items:[{
			columnWidth:1,
			items:[{
				name:'remarks', fieldLabel:"检修情况描述", xtype:'textarea', anchor: '90%', height: 45, allowBlank: true, maxLength: 500
			}]
		}, {
			// 根据质量检查项基础配置动态生成的表单组件
			columnWidth:1,
			xtype:'panel', id:'panel_Core', layout:'column', defaults:{layout: 'form', columnWidth:.25, defaults: {width:RecordCardProcess.fieldWidth}}
//			items:RecordCardProcess.qcItems
		}]
	})
	
	// 【检修记录单处理】窗口
	RecordCardProcess.win = new Ext.Window({
		title:"检修记录单处理",
		width:1200, height:700, layout:"fit",
		closeAction:'hide',
		plain: true, modal: true,
		items:[{
			xtype:"panel",
			border: false,
			layout:"border",
			tbar:[ {
				text:'关闭', iconCls:'closeIcon', handler:function(){
					this.findParentByType('window').hide();
				}
			}, '->', {
				text:'上一工单', iconCls:'moveUpIcon', handler: function(){
					if (!RecordCardProcess.previousRecordCardFn()) {
						MyExt.Msg.alert('已经是第一条工单');
					}
				}
			}, {
				text:'下一工单', iconCls:'moveDownIcon', handler:function(){
					if (!RecordCardProcess.nextRecordCardFn()) {
						MyExt.Msg.alert('已经是最后一条工单');
					}
				}
			}],
			items:[{
				xtype:"container", autoEl:"div", region:"north", height:220, layout:"border",
				defaults:{layout:'fit'},
				items:[{
					title:"记录工单基本信息",
					region:"north",
					height:95,
					frame:true,
//					margins:"10px 0 0 0",
					collapseFirst: false, collapsible: true,
					items:[RecordCardProcess.baseForm]
				}, {
					region:"center",
					frame:true,
					items:RecordCardProcess.saveForm
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
				PartsRdpRecordCard.grid.store.reload();
			}
		}
	})
})