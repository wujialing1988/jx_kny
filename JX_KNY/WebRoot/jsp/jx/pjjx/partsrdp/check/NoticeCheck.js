/**
 * 检修作业流程 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace('NoticeProcess'); 
	
	
	/** ************** 定义全局变量开始 ************** */
	NoticeProcess.labelWidth = 80;
	NoticeProcess.fieldWidth = 140;
	NoticeProcess.index = -1;				// 用以记录当前正在处理的【检修提票单】索引
	NoticeProcess.idx = "####";				// 用以记录当前正在处理的【检修提票单】主键
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全函数量开始 ************** */
	
	// 设置【检修提票单处理】窗口的表单数据
	NoticeProcess.initFn = function(record) {
    	// 设置【工单编号】、【工单描述】字段值
        NoticeProcess.baseForm.getForm().loadRecord(record);
    	// 设置【开工时间】、【完工时间】字段值
        NoticeProcess.saveForm.getForm().loadRecord(record);
    	
    	
    	// 设置当前正在处理的【检修提票单】主键
    	NoticeProcess.idx = record.get('idx');
    	
    	var rdpIDX = record.get('rdpIDX');						// 作业主键
    	var rdpNodeIDX = record.get('rdpNodeIDX');				// 作业节点主键
    	
    	// 加载【检修检测项】表格数据
    	PartsRdpRecordRI.rdpNoticeIDX = NoticeProcess.idx;
    	PartsRdpRecordRI.grid.store.load();
    	
		// 根据记录状态激活（禁用）相应的功能处理按钮
    	NoticeProcess.saveForm.find('name', 'solution')[0].disable();
		NoticeProcess.saveForm.find('name', 'workStartTime')[0].disable();
		NoticeProcess.saveForm.find('name', 'workEmpName')[0].disable();
    	
	}
	
	
	// 【上一工单】工具栏按钮触发的函数处理，如果还有上一条工单，则返回true，否则返回false
	NoticeProcess.previousNoticeFn = function() {
		if (NoticeProcess.index <= 0) {
			return false;
		} 
		NoticeProcess.index = parseInt(NoticeProcess.index) - 1;
		var record = PartsRdpNotice.grid.store.getAt(NoticeProcess.index);
		NoticeProcess.initFn(record);
		return true;
	}
	
	// 【上一工单】工具栏按钮触发的函数处理，如果还有下一条工单，则返回true，否则返回false
	NoticeProcess.nextNoticeFn = function() {
		if (NoticeProcess.index == PartsRdpNotice.grid.store.getCount() - 1) {
			return false;
		}
		NoticeProcess.index = parseInt(NoticeProcess.index) + 1;
		var record = PartsRdpNotice.grid.store.getAt(NoticeProcess.index);
		NoticeProcess.initFn(record);
		return true;
	}
	
	/** ************** 定义全函数量结束 ************** */

	// 【检修提票单处理】窗口 - 【检修提票单】基本信息表单
	NoticeProcess.baseForm = new Ext.form.FormPanel({
		labelWidth:NoticeProcess.labelWidth,
		border:false, baseCls:'x-plain',
		labelAlign:"left",
		layout:"column", 
//		bodyStyle:'padding: 0 10px;',
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.33,
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
				fieldLabel:"提票单编号", name:'noticeNo'
			}]
		}, {
			items:[{
				fieldLabel:"提报人", name:'noticeEmpName'
			}]
		}, {
			columnWidth:1,
			items:[{
				fieldLabel:"问题描述", name:'noticeDesc'
			}]
		}]
							
	});
	
	// 【检修提票单处理】窗口 - 【检修提票单】保存表单
	NoticeProcess.saveForm = new Ext.form.FormPanel({
		layout:"column", style:'padding: 10px 10px;', labelWidth:NoticeProcess.labelWidth,
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.5,
			defaults: {
				width:NoticeProcess.fieldWidth
			}
		},
		items:[{
			columnWidth:1,
			items:[{
				name:'solution', fieldLabel:"处理结果描述", xtype:'textarea', anchor: '90%', height: 65, allowBlank: true, maxLength: 500
			}]
		}, {
			items:[{
				name:'workEmpName', xtype:"textfield",fieldLabel:"处理人"
			}]
		}, {
			items:[{
				name:'workStartTime', xtype:"my97date", format:'Y-m-d H:i', fieldLabel:"处理时间"
			}]
		}]
	})
	
	// 【检修提票单处理】窗口
	NoticeProcess.win = new Ext.Window({
		title:"检修提票单处理",
		width:900, height:400, layout:"fit",
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
					if (!NoticeProcess.previousNoticeFn()) {
						MyExt.Msg.alert('已经是第一条工单');
					}
				}
			}, {
				text:'下一工单', iconCls:'moveDownIcon', handler:function(){
					if (!NoticeProcess.nextNoticeFn()) {
						MyExt.Msg.alert('已经是最后一条工单');
					}
				}
			}],
			defaults:{layout:'fit'},
			items:[
				{
					title:"提票工单基本信息",
					region:"north",
					height:125,
					frame:true,
//					margins:"10px 0 0 0",
					collapseFirst: false, collapsible: true,
					items:[NoticeProcess.baseForm]
				}, {
					region:"center", height:185, 
					frame: true,
					items:NoticeProcess.saveForm
				}]
		}],
		listeners: {
			hide: function(p) {
				PartsRdpNotice.grid.store.reload();
			}
		}
	})
})