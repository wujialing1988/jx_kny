/** 配件规格型号选择公用js-按承修部门panel */
Ext.onReady(function(){
	Ext.namespace('PartsRepairListTree');                       //定义命名空间
	PartsRepairListTree.searchParams = {};
	PartsRepairListTree.tree = new Ext.tree.TreePanel( {
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/partsRepairList!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '承修部门',
	        id: 'ROOT_0',
	        leaf: false,
	        expanded :true
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    animate : false,
	    useArrows : false,
	    border : false,
	    listeners: {
	        click: function(node, e) {
	        	jx.pjwz.partbase.component.PartsTypeTree.searchParams = {};
	        	var partsTypeTree = PartsRepairListTree.orgPanel.items.itemAt(1).items.itemAt(0);
	        	if(node != PartsRepairListTree.tree.getRootNode())
	        		jx.pjwz.partbase.component.PartsTypeTree.searchParams.repairOrgID = node.id;
	        	partsTypeTree.root.reload();
	        	partsTypeTree.root.expand();
	        	jx.pjwz.partbase.component.PartsTypeTree.searchParams = {};
	        }
	    }    
	});
	PartsRepairListTree.tree.on('beforeload', function(node){
	    this.loader.dataUrl = ctx + '/partsRepairList!tree.action';
	});
	PartsRepairListTree.tree.getRootNode().expand();
	
	PartsRepairListTree.orgPanel = new Ext.Panel({ 
		   title : '', 
		   layout:'border',//表格布局
		   items: [ 
			   { 
			    region: 'north',
			    height: 150,		// 修改布局高度（何涛2014-09-01）
			    autoScroll : true,	// 修改根据容器内容自动出现滚动条（何涛2014-09-01）
			    items: PartsRepairListTree.tree
			   },
			   { 
			    region: 'center',
			    layout: 'fit',
			    items: new jx.pjwz.partbase.component.PartsTypeTree.tree()
			   }
			]

	});
});