/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {
	Ext.namespace('ZbWorkStationSearcher');
	
	/** ************* 定义全局变量开始 ************* */
	ZbWorkStationSearcher.searchParams = {};
	ZbWorkStationSearcher.nodeIDX = "";											// 检修作业流程节点主键
	ZbWorkStationSearcher.labelWidth = 80,
	ZbWorkStationSearcher.fieldWidth = 120,
	ZbWorkStationSearcher.repairLineIdx = "",
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 【确定】按钮触发的函数处理
	ZbWorkStationSearcher.addFn = function() {
		var ids = [];
//		ZbRepairLine.getHasChkedNode(ZbRepairLine.tree.getRootNode(), ids);
		// Modified by hetao at 2015-09-17 修改单次操作只能添加一个工位
		var selectedNodeIdx = ZbRepairLine.getSelectedNode();
		if (!Ext.isEmpty(selectedNodeIdx)) {
			ids.push(selectedNodeIdx);
		}
		
		if (Ext.isEmpty(ids)) {
			MyExt.Msg.alert('尚未选择任何记录！');
			return;
		}
		
		//将选择的数据放入到form中
		var form = ZbglJobProcessNodeDef.saveForm;
		
	}
	/** ************* 定义全局函数结束 ************* */
	
	ZbWorkStationSearcher.win = new Ext.Window({
		title:"选择工位",
		width: 300,
		height: 600,
		modal: true,
		layout: 'fit',
		closeAction: 'hide',
		items:[ZbRepairLine.tree],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '添加', iconCls: 'yesIcon', handler: ZbWorkStationSearcher.addFn
		}, {
			xtype: "button", text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}], 
		listeners: {
			show: function(window){
				// Modified by hetao at 2015-09-08 调整台位以父子节点方式显示
				// 加载流水线树
				ZbRepairLine.tree.getRootNode().reload();
				ZbRepairLine.tree.getRootNode().expand();
				// 加载工位表格
//				ZbWorkStationSearcher.grid.store.load();
			},
			hide: function(){
			}
		}
	});
	
});