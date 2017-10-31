/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {
	Ext.namespace('WorkStationSearcher1');
	
	/** ************* 定义全局变量开始 ************* */
	WorkStationSearcher1.searchParams = {};
	WorkStationSearcher1.nodeIDX = "";											// 检修作业流程节点主键
	WorkStationSearcher1.labelWidth = 80,
	WorkStationSearcher1.fieldWidth = 120,
	WorkStationSearcher1.repairLineIdx = "",
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 【确定】按钮触发的函数处理
	WorkStationSearcher1.addFn = function() {
		var ids = [];
		RepairLine1.getHasChkedNode(RepairLine1.tree.getRootNode(), ids);
		if (Ext.isEmpty(ids)) {
			MyExt.Msg.alert('尚未选择任何记录！');
			return;
		}
		
		var datas = [];
		
		for (var i = 0; i < ids.length; i++) {
			var jobNodeProjectDef = {};
			jobNodeProjectDef.nodeIDX = WorkStationSearcher1.nodeIDX;
			jobNodeProjectDef.workStationIDX = ids[i];
			datas.push(jobNodeProjectDef);
		}
    	// Ajax后台数据处理
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
            url: ctx + '/jobNodeStationDef!save.action',
            jsonData: datas,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	alertSuccess();
                	TecnodeUnionStation.grid.store.reload();
                	// 一个作业节点只能关联一条流水线上的一个工位，因为添加了一个工位后，从树上移除已经添加了的工位的流水线
                	for (var i = 0; i < ids.length; i++) {
                		var node = RepairLine1.tree.getNodeById(ids[i]);
                		var repairLineIdx = node.attributes.repairLineIdx;
                		// 移除流水线
                		RepairLine1.tree.getRootNode().removeChild(RepairLine1.tree.getNodeById(repairLineIdx));
                	}
                } else {
                    alertFail(result.errMsg);
                }
            }
        }));
		
	}
	/** ************* 定义全局函数结束 ************* */
		
	WorkStationSearcher1.win = new Ext.Window({
		title:"选择工位",
		width: 300,
		height: 600,
		modal: true,
		layout: 'fit',
		closeAction: 'hide',
		items:[RepairLine1.tree],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '添加', iconCls: 'yesIcon', handler: WorkStationSearcher1.addFn
		}, {
			xtype: "button", text: '关闭', iconCls: 'closeIcon', handler: function() {
				WorkStationSearcher1.win.hide();
			}
		}], 
		listeners: {
			show: function(window){
				// 加载流水线树
				RepairLine1.tree.getRootNode().reload();
				RepairLine1.tree.getRootNode().expand();
			},
			hide: function(){
			}
		}
	});
	
});