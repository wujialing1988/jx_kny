/**
 * 机车检修流水线 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('RepairLine1');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	RepairLine1.nodeIDX = "";							// 检修作业流程节点主键
	RepairLine1.repairLineName = "";						// 流水线名称 
	/** **************** 定义全局变量结束 **************** */
	
	// 机车检修作业流程节点树型列表
	// Modified by hetao at 2015-09-08 调整台位以父子节点方式显示
	RepairLine1.tree =  new Ext.tree.TreePanel({
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/repairLine!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '流水线',
	        id: "ROOT_0",
	        leaf: false
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    autoShow: true,
	    useArrows : true,
	    border : false,
	    listeners: {
	    	render: function() {
	    	},
	        beforeload:  function(node){
			    RepairLine1.tree.loader.dataUrl = ctx + '/repairLine!tree.action?parentIDX=' + node.id;
			},
			load: function(node) {
				if (node.getDepth() != 0) {
					return;
				}
				// 过滤树节点，移除已经添加到工位组的工位
				if (!Ext.isEmpty(WorkStationSearcher1.selectedIds)) {
					for (var i in WorkStationSearcher1.selectedIds) {
						var childNodes = node.childNodes;
						for (var j in childNodes) {
							if (WorkStationSearcher1.selectedIds[i] == childNodes[j].id) {
								node.removeChild(childNodes[j]);
							}
						}
					}
				}
			}
	    }    
	});
	
	// 递归获取勾选的树节点
	RepairLine1.getHasChkedNode = function(node, array) {
		if(typeof(node) == 'undefined') return false;
		if(node.attributes.checked == true && node.disabled != true) {
			array.push(node.id);
		}
		var childs = node.childNodes; //找到该节点的所有子节点
		for(var i = 0; i < childs.length; i++){
			arguments.callee(childs[i], array);
		}
	}
});