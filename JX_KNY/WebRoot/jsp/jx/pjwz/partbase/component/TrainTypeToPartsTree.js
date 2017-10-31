/** 配件规格型号选择公用js-按车型panel */
Ext.onReady(function(){
	Ext.namespace('TrainTypeToPartsTree');                       //定义命名空间
	TrainTypeToPartsTree.tree = new Ext.tree.TreePanel( {
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/trainTypeToParts!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '车型',
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
	        	var partsTypeTree = TrainTypeToPartsTree.trainPanel.items.itemAt(1).items.itemAt(0);
	        	if(node != TrainTypeToPartsTree.tree.getRootNode()){
		        	if(node.id.indexOf("X_")==0){ //选中了车型
		        		jx.pjwz.partbase.component.PartsTypeTree.searchParams.trainTypeIDX = node.attributes.trainTypeIDX;	        		
		        	}
		        	if(node.id.indexOf("P_")==0){ //选择修程，两个条件过滤（车型，修程）
						jx.pjwz.partbase.component.PartsTypeTree.searchParams.trainTypeIDX = node.parentNode.attributes.trainTypeIDX;
						jx.pjwz.partbase.component.PartsTypeTree.searchParams.repairClassIDX = node.attributes.xcID;
		        	}
	        	}
	        	partsTypeTree.root.reload();
	        	partsTypeTree.root.expand();
	        	jx.pjwz.partbase.component.PartsTypeTree.searchParams = {};
	        }
	    }    
	});
	TrainTypeToPartsTree.tree.on('beforeload', function(node){
	    this.loader.dataUrl = ctx + '/trainTypeToParts!tree.action?parentIDX=' + node.id;
	});	
	TrainTypeToPartsTree.tree.getRootNode().expand();
	TrainTypeToPartsTree.trainPanel = new Ext.Panel({ 
		   title : '', 
		   layout:'border',//表格布局
		   items: [ 
			   { 
			    region: 'north', 
			    height: 150,		// 修改布局高度（何涛2014-09-01）
			    autoScroll : true,	// 修改根据容器内容自动出现滚动条（何涛2014-09-01）
			    items: TrainTypeToPartsTree.tree
			   },
			   { 
			    region: 'center',
			    layout: 'fit',
				// 修改在控件的【按车型】选择Tab页不能正常显示规格型号树组件的错误（何涛2014-09-10）
			    items: new jx.pjwz.partbase.component.PartsTypeTree.tree()
			   }
			]

	});
});