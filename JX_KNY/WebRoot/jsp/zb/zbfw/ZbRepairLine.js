/**
 * 机车检修流水线 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('ZbRepairLine');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	ZbRepairLine.nodeIDX = "";							// 检修作业流程节点主键
	ZbRepairLine.repairLineName = "";						// 流水线名称 
	/** **************** 定义全局变量结束 **************** */
	
	// 机车检修作业流程节点树型列表
	// Modified by hetao at 2015-09-08 调整台位以父子节点方式显示
	ZbRepairLine.tree =  new Ext.tree.TreePanel({
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/RepairLine!tree.action"
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
			    ZbRepairLine.tree.loader.dataUrl = ctx + '/repairLine!tree.action?parentIDX=' + node.id + '&checkedTree=false';
			},
			load: function(node) {
				if (node.getDepth() != 0) {
					return;
				}
				// 过滤树节点，移除已经添加到工位组的工位
				if (!Ext.isEmpty(ZbWorkStationSearcher.selectedIds)) {
					for (var i in ZbWorkStationSearcher.selectedIds) {
						var childNodes = node.childNodes;
						for (var j in childNodes) {
							if (ZbWorkStationSearcher.selectedIds[i] == childNodes[j].id) {
								node.removeChild(childNodes[j]);
							}
						}
					}
				}
			},
			dblclick: function( node, e ) {
				// 通过双击进行工位关联
        		if (node.getDepth() <= 1) {
        			return;
        		}
        		ZbWorkStationSearcher.addFn();
        	}
	    }    
	});
	
	// 递归获取勾选的树节点
	ZbRepairLine.getHasChkedNode = function(node, array) {
		if(typeof(node) == 'undefined') return false;
		if(node.attributes.checked == true && node.disabled != true) {
			array.push(node.id);
		}
		var childs = node.childNodes; //找到该节点的所有子节点
		for(var i = 0; i < childs.length; i++){
			arguments.callee(childs[i], array);
		}
	}
	
	// 获取已选择的树节点
	ZbRepairLine.getSelectedNode = function() {
		var sm = ZbRepairLine.tree.getSelectionModel();
		var node = sm.getSelectedNode();
		if (Ext.isEmpty(node) || node.getDepth() <= 1) {
			return null;
		}
		return node.id;
	}
});