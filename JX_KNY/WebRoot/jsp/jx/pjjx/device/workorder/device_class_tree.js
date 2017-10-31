Ext.onReady(function(){
	
	Ext.ns("DeviceClass");
	
	var nodeClickEvents = [];
	
	var tree = new Ext.tree.TreePanel({
		root: new Ext.tree.AsyncTreeNode({
	       	text: '设备分类',
	        id: "12",
	        leaf: false,
	        iconCls: 'folderIcon'
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    animate : true,
	    useArrows : true,
	    border : false,
	    listeners: {
	    	render: function() {
	    		tree.root.expand()
	    	},
	    	beforeload: function(node) {
			    tree.loader.dataUrl = ctx + '/deviceType!tree.action?parentIdx=' + node.id
	    	},
	        click: function(node, e) {
	        	for(var i = 0; i < nodeClickEvents.length; i++){
	        		nodeClickEvents[i](node);
	        	}
			}
	    }    
	});
	
	DeviceClass.classTree = tree;
	
	DeviceClass.regTreeNodeClick = function(callback){
		nodeClickEvents.push(callback);
	}
});