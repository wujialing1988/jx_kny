/**
 * 检修作业流程 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace('PartsRdpProcess'); 
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpProcess.labelWidth = 100;
	PartsRdpProcess.fieldWidth = 140;
	PartsRdpProcess.rdpIdx = null;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 【修竣提交】【无法修复】按钮触发的函数处理
	PartsRdpProcess.finishPartsRdpFn = function(id, status, repairResultDesc) {
		self.loadMask.show();
		Ext.Ajax.request({
			url: ctx + '/partsRdp!finishPartsRdp.action',
			params: {
				id: id,
				status: status,
				repairResultDesc: repairResultDesc
			},
			//请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            // 重新加载“检修配件信息树”
		            PartsRdpTree.reload();
		            // 如果“无法修复处理窗口”处于可视状态，则进行隐藏
		            if (PartsRdpProcess.processWin.isVisible()) {
		            	PartsRdpProcess.processWin.hide();
		            }
		            // 清空工单列表
		            PartsRdpRecordCard.grid.store.removeAll();					// 记录工单
		            PartsRdpTecCard.grid.store.removeAll();						// 工艺工单
		            PartsRdpNotice.grid.store.removeAll();						// 提票工单
		            PartsRdpExpendMatQuery.grid.store.removeAll();				// 物料消耗清空
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义无法修复处理窗口开始 ************** */
	PartsRdpProcess.processWin = new Ext.Window({
		title:"提交",
		width:525,
		height:170,
		layout:"fit",
		closeAction:"hide",
		plain: true, modal: true,
		items:[{
			xtype:"form", border: false,
			baseCls:"x-plain",
			labelWidth:80,
			padding: "15px;",
			labelAlign:"left",
			layout:"form",
			items:[
				{
					xtype:"textarea",
					name:"repairResultDesc", allowBlank: false,
					maxLength: 500,
					fieldLabel:"情况描述",
					anchor:"100%"
				}
			]
		}],
		buttonAlign: 'center',
		buttons: [{
			text:'确认', iconCls: 'yesIcon', handler: function() {
				var form = PartsRdpProcess.processWin.findByType('form')[0].getForm();
				if (!form.isValid()) {
					return;
				}
				var repairResultDesc = PartsRdpProcess.processWin.find('name', 'repairResultDesc')[0].getValue();
		        PartsRdpProcess.finishPartsRdpFn(PartsRdpProcess.rdpIdx, RDP_STATUS_WFXF, repairResultDesc);
			}	
		}, {
			text:'取消', iconCls: 'cancelIcon', handler: function() {
				PartsRdpProcess.processWin.hide();
			}	
		}],
		
		listeners: {
			// 每次显示时，清空表单数据
			show: function(window) {
				window.find('xtype', 'form')[0].getForm().reset();
			}
		}
	})
	/** ************** 定义无法修复处理窗口结束 ************** */
	
	/** ************** 定义配件检修作业计划单信息表单开始 ************** */
	// 通过设置组件的defaults属性,大大减少了重复的代码量
	PartsRdpProcess.baseForm = new Ext.form.FormPanel({
		labelWidth:PartsRdpProcess.labelWidth,
		border: false,
		labelAlign:"left",
		layout:"column",
		bodyStyle:"padding:10px;",
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.25, 
			defaults:{
				style: 'border:none; background:none;', 
				xtype:"textfield", readOnly: true,
				anchor:"100%"
			}
		},
		items:[{
			items:[{
				fieldLabel:"配件编号", name:"partsNo"
			}, {
				fieldLabel:"下车车型号", name:"trainType"
			}]
		}, {
			items:[{
				fieldLabel:"配件名称", name:"partsName"
			}, {
				fieldLabel:"下车修程", name:"repair"
			}]
		}, {
			items:[{
				fieldLabel:"规格型号", name:"specificationModel"
			}, {
				fieldLabel:"计划开始时间", name:"planStartTime"
			}]
		}, {
			items:[{
				fieldLabel:"扩展编号", name:"extendNo"
			}, {
				fieldLabel:"计划结束时间", name:"planEndTime"
			}]
		}]
	})
	/** ************** 定义配件检修作业计划单信息表单结束 ************** */

	// 页面自适应布局
	new Ext.Viewport({
		layout:"border",
		// ------------- 页面左边 ------------ //
		items:[{
			title : '<span style="font-weight:normal">检修配件信息树</span>',
	        iconCls : 'icon-expand-all',
	        tools : [ {
	            id : 'refresh',
	            handler: function() {
	            	PartsRdpTree.reload();
	            }
	        } ],
			region:"west",
			collapsible: true,
			width:330,
			layout:"fit",
			items: PartsRdpTree.tree
		// ------------- 页面右边 ------------ //
		}, {
			region:"center",
			layout:"border",
			// ------ 页面右边 - 上半部分 ------ //
			items:[{
				title:"配件检修作业单信息",
				xtype:"panel", region:"north", frame: true, collapsible:true,
				height:105,
				items: PartsRdpProcess.baseForm
			// ------ 页面右边 - 下半部分 ------ //
			}, {
				xtype:"panel", region:"center", layout:"fit",
				tbar:[{
					text:'修竣提交', iconCls:'yesIcon', handler: function(){
						if (Ext.isEmpty(PartsRdpProcess.rdpIdx)) {
							MyExt.Msg.alert('尚未选择一条记录！');
							return;
						}
						Ext.Msg.confirm("提示", "是否确认提交？", function(btn) {
						if ("yes" == btn) {
							PartsRdpProcess.finishPartsRdpFn(PartsRdpProcess.rdpIdx, RDP_STATUS_JXDYS);
						}
					});
					}
				}, {
					text:'无法修复', iconCls:'deleteIcon', handler: function(){
						if (Ext.isEmpty(PartsRdpProcess.rdpIdx)) {
							MyExt.Msg.alert('尚未选择一条记录！');
							return;
						}
						// 无法修复需要填写无法修复原因，因此弹出一个窗口进行处理
						PartsRdpProcess.processWin.show();
				}
				}, '-', {
					text:'刷新', iconCls:'refreshIcon', handler: function() {
						 self.location.reload();
					}
				}],
				items:[{
					xtype:"tabpanel", activeTab:0, id:"tabpanel_base",
					defaults:{layout:"fit"},
					items:[{
						title:"检修记录工单",
						items: PartsRdpRecordCard.grid
					}, {
						title:"检修工艺工单",
						items: PartsRdpTecCard.grid
					}, {
						title:"提票工单",
						items: PartsRdpNotice.grid
					}, {
						title:"物料消耗情况",
						items: PartsRdpExpendMatQuery.grid,
						listeners: {
							activate: function() {
								PartsRdpExpendMatQuery.grid.store.reload();
							}
						}
					}]
				}]
			}]
		}]
	})

});