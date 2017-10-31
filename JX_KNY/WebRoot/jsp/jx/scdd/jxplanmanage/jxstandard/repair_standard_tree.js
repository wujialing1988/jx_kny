Ext.onReady(function(){
	if(typeof(RepairStandard) === 'undefined')
		Ext.ns("RepairStandard");
	
	var tree = new Ext.tree.TreePanel({
		root: new Ext.tree.AsyncTreeNode({
			text: "车型",
	        id: "ROOT_0",
	        expanded: true
		}),
		rootVisible: true,
		autoScroll:true,
		animate:true,
		border: false,
		listeners:{
			"click": function(node){
				if(node.leaf){
					if(RepairStandard.trainTypeIdx != node.id){
						RepairStandard.trainTypeIdx = node.id;
						RepairStandard.grid.topToolbar.enable();
						RepairStandard.grid.store.load();
						RepairStandard.resetXC();
					}
				}else{
					RepairStandard.grid.topToolbar.disable();
					RepairStandard.grid.store.removeAll();
				}
				RepairStandard.trainTypeIdx = node.id;
			},
			beforeload:  function(node){
			    tree.loader.dataUrl = ctx + '/trainVehicleType!findTrainTypeForTree.action?vehicleType='+vehicleType;
			}
		}
	});
	
	RepairStandard.tree = tree;
});