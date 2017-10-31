/**
 * 检修作业流程 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace('TecCardProcess'); 
	
	
	/** ************** 定义全局变量开始 ************** */
	TecCardProcess.labelWidth = 100;
	TecCardProcess.fieldWidth = 140;
	TecCardProcess.index = -1;				// 用以记录当前正在处理的【检修工艺工单】索引
	TecCardProcess.idx = "####";				// 用以记录当前正在处理的【检修工艺工单】主键
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全函数量开始 ************** */
	
	// 设置【检修工艺工单处理】窗口的表单数据
	TecCardProcess.initFn = function(record) {
    	// 设置【工单编号】、【工单描述】字段值
        TecCardProcess.baseForm.getForm().loadRecord(record);
    	
    	
    	// 设置当前正在处理的【检修工艺工单】主键
    	TecCardProcess.idx = record.get('idx');
    	
    	var rdpIDX = record.get('rdpIDX');						// 作业主键
    	var rdpNodeIDX = record.get('rdpNodeIDX');				// 作业节点主键
    	var tecCardIDX = record.get('tecCardIDX');				// 工艺卡主键
    	
    	// 设置【作业任务】表格加载数据的过滤参数（PartsRdpTecWS.js）
//    	PartsRdpTecWS.rdpIDX = rdpIDX;							// 作业主键
//		PartsRdpTecWS.rdpNodeIDX = rdpNodeIDX;					// 作业节点主键
		PartsRdpTecWS.rdpTecCardIDX = TecCardProcess.idx;		// 工艺工单主键
		PartsRdpTecWS.wsParentIDX = "ROOT_0";
		
		PartsRdpTecWS.grid.store.load();
        
	}
	
	// 【上一工单】工具栏按钮触发的函数处理，如果还有上一条工单，则返回true，否则返回false
	TecCardProcess.previousTecCardFn = function() {
		if (TecCardProcess.index <= 0) {
			return false;
		} 
		TecCardProcess.index = parseInt(TecCardProcess.index) - 1;
		var record = PartsRdpTecCard.grid.store.getAt(TecCardProcess.index);
		TecCardProcess.initFn(record);
		return true;
	}
	
	// 【上一工单】工具栏按钮触发的函数处理，如果还有下一条工单，则返回true，否则返回false
	TecCardProcess.nextTecCardFn = function() {
		if (TecCardProcess.index == PartsRdpTecCard.grid.store.getCount() - 1) {
			return false;
		}
		TecCardProcess.index = parseInt(TecCardProcess.index) + 1;
		var record = PartsRdpTecCard.grid.store.getAt(TecCardProcess.index);
		TecCardProcess.initFn(record);
		return true;
	}
	
	/** ************** 定义全函数量结束 ************** */

	// 【检修工艺工单处理】窗口 - 【检修工艺工单】基本信息表单
	TecCardProcess.baseForm = new Ext.form.FormPanel({
		labelWidth:TecCardProcess.labelWidth,
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
				fieldLabel:"工单编号", name:'tecCardNo'
			}]
		}, {
			columnWidth:0.75,
			items:[{
				fieldLabel:"工单描述", name:'tecCardDesc'
			}]
		}]
							
	});
	
	// 【检修工艺工单处理】窗口
	TecCardProcess.win = new Ext.Window({
		title:"检修工艺工单处理",
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
					if (!TecCardProcess.previousTecCardFn()) {
						MyExt.Msg.alert('已经是第一条工单');
					}
				}
			}, {
				text:'下一工单', iconCls:'moveDownIcon', handler:function(){
					if (!TecCardProcess.nextTecCardFn()) {
						MyExt.Msg.alert('已经是最后一条工单');
					}
				}
			}],
			items:[{
				 title:"工艺工单基本信息",frame:true,
				 region:"north", height:100, layout:"fit",
				 items:[ TecCardProcess.baseForm ]
			}, {
				title:"作业任务",
				region:"center",layout:'fit',border: false,
				items:[PartsRdpTecWS.grid]
			}]
		}],
		listeners: {
			hide: function(p) {
				PartsRdpTecCard.grid.store.reload();
			}
		}
	})
})