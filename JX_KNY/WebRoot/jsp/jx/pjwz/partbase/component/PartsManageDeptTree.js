Ext.onReady(function(){
	Ext.namespace('PartsManageDeptTree');                       //定义命名空间
	PartsManageDeptTree.orgid = '0';
	PartsManageDeptTree.returnFn = function(node){
		//自定义实现功能
		alert("自己写");
	}
	PartsManageDeptTree.tree = new Ext.tree.TreePanel( {
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/partsAccountUtil!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '',
	        id: 'ROOT_0',
	        leaf: false,
	        expanded :true
	    }),
	    rootVisible: false,
	    autoScroll : true,
	    animate : false,
	    useArrows : false,
	    border : false,
	    listeners: {
	        click: function(node, e) {
	        	PartsManageDeptTree.returnFn(node);
	        }
	    }    
	});
	PartsManageDeptTree.tree.on('beforeload', function(node){
	    this.loader.dataUrl = ctx + '/partsAccountUtil!tree.action?parentIDX=' + node.id + '&orgid=' + PartsManageDeptTree.orgid ;
	});	
//	PartsManageDeptTree.tree.getRootNode().expand();
	PartsManageDeptTree.tree.expandAll();
	PartsManageDeptTree.panel = new Ext.Panel({ 
		   title : '', 
		   layout:'border',//表格布局
		   items: [ 
			   { 
			    region: 'north', layout: 'fit', 
		        height: 350,			 
			    items: new jx.pjwz.partbase.component.PartsTypeTree.tree()
			   },
			   { 
			    region: 'center',
			    layout: 'fit',
			    items: PartsManageDeptTree.tree
			    
			   }
			]

	});
	//页面自适应布局
//	var viewport = new Ext.Viewport({ layout:'fit', items:PartsManageDeptTree.panel });
});