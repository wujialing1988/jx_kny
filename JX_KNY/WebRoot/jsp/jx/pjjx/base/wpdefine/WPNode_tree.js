/**
 * 检修作业流程 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('WPNode_tree');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	WPNode_tree.treePath = "###";				// 用于记录树展开时，当前节点的路径信息
	WPNode_tree.wPIDX = "###";					// 当前作业节点树的 - 作业流程主键
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 重新加载【作业节点树】
	WPNode_tree.reload = function(path) {
        WPNode_tree.tree.root.reload();
        if (path == undefined || path == "" || path == "###") {
			WPNode_tree.tree.root.expand();
        } else {
        	// 展开树到指定节点
	        WPNode_tree.tree.expandPath(path);
        }
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义作业节点树开始 ************** */
	WPNode_tree.tree = new Ext.tree.TreePanel( {
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/wPNode!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '',
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
	    		WPNode_tree.isRender = true;	// 增加一个数已经被渲染的标示字段，用以规避数还未渲染，就执行reload()和expand()方法导致的错误
	    		WPNode_tree.reload();
	    	},
	        click: function(node, e) {
	        	// 获取当前节点的路径信息
	        	WPNode_tree.treePath = node.getPath();	
	        	WPNode.parentWPNodeIDX = node.id;
        		// 如果是叶子节点，则仅能进行检修工艺卡和检修记录卡的设置
	        	if (node.leaf) {
	        		WP.hideTabPanelMethod(1);
	        		
	        		// 获取当前活动的Tab选项卡页
	        		var activeTab = Ext.getCmp('saveWinTab_2').getActiveTab();
	        		// 如果当前活动的Tab选项卡页是“下级作业节点”页，则设置当前活动的Tab选项卡页为“检修工艺卡”页
	        		if (activeTab.getId() == 'saveWinTab_2_0' || activeTab.getId() == 'saveWinTab_2_5') {
		        		Ext.getCmp('saveWinTab_2').setActiveTab(1);
	        		}
	        		
	        		// 设置【作业节点所挂工艺卡】基础信息
	        		TecCardForWPNode.wPIDX = WPNode_tree.wPIDX;							// 作业流程主键
					TecCardForWPNode.wPNodeIDX = node.id;								// 作业流程节点主键
					TecCardForWPNode.grid.store.load();
	        		
	        		// 设置【作业节点所挂记录卡】基础信息
	        		RecordCardForWPNode.wPIDX = WPNode_tree.wPIDX;							// 作业流程主键
					RecordCardForWPNode.wPNodeIDX = node.id;								// 作业流程节点主键
					RecordCardForWPNode.grid.store.load();
					
					// 设置【作业节点所挂工位】基础信息
					WpNodeStationDef.nodeIDX = node.id;								// 作业流程节点主键
					WpNodeStationDef.grid.store.load();
					
					// 设置【配件检修用料】基础信息
					WPMat.wPIDX = WPNode_tree.wPIDX;							// 作业流程主键
					WPMat.wPNodeIDX = node.id;								// 作业流程节点主键
					if(node.parentNode){
						WPMat.parentWPNodeIDX = node.parentNode.id;
					}
					WPMat.wPNodeName = node.text ;
					WPMat.grid.store.load();
					
					WPMatSearch.wPIDX = WPNode_tree.wPIDX;							// 作业流程主键
					if(node.parentNode){
						WPMatSearch.parentWPNodeIDX = node.parentNode.id;
					}
					WPMatSearch.grid.store.load();	
					
	        		// 重命名Tab - 只取序列号
					Ext.getCmp('saveWinTab_2_1').setTitle(node.text.substring(0, node.text.lastIndexOf(".")) + " - 所挂工艺卡");
					Ext.getCmp('saveWinTab_2_2').setTitle(node.text.substring(0, node.text.lastIndexOf(".")) + " - 所挂记录卡");
					Ext.getCmp('saveWinTab_2_3').setTitle(node.text.substring(0, node.text.lastIndexOf(".")) + " - 所挂作业工位");
					// Ext.getCmp('saveWinTab_2_4').setTitle(node.text.substring(0, node.text.lastIndexOf(".")) + " - 配件检修用料");
					
        		// 如果是树干节点，则仅能编辑下级作业节点信息
	        	} else if (!node.leaf) {
	        		WP.hideTabPanelMethod(0);
	        		
	        		// 重命名Tab - 只取序列号
	        		if (node.text.lastIndexOf(".") <= 0) {
		        		Ext.getCmp('saveWinTab_2_0').setTitle(node.text + " - 作业节点")
	        		} else {
		        		Ext.getCmp('saveWinTab_2_0').setTitle(node.text.substring(0, node.text.lastIndexOf(".")) + " - 下级作业节点")
	        		}
	        		
	        		// 重新加载【作业节点】表格
		        	WPNode.grid.store.reload();
	        	}
	        },
	        beforeload:  function(node){
			    WPNode_tree.tree.loader.dataUrl = ctx + '/wPNode!tree.action?parentIDX=' + node.id + '&wPIDX=' + WPNode_tree.wPIDX;
			}
	    }    
	});
	/** ************** 定义作业节点树结束 ************** */

});