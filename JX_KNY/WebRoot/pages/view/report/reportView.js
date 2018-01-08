Ext.onReady(function(){
	if(typeof(ReportView) === 'undefined')
		Ext.ns("ReportView");
	
	var tree = new Ext.tree.TreePanel({
		root: new Ext.tree.AsyncTreeNode({
			text: "报表",
	        id: "ROOT_0",
	        expanded: true
		}),
		rootVisible: true,
		autoScroll:true,
		animate:true,
		border: false,
		frame:true,
		listeners:{
			"click": function(node){
				if(node.leaf && !node.attributes.url){
					MyExt.Msg.alert("未配置报表路径，请先配置!");
					return ;
				}
				url = node.attributes.url;
				var reportUrl = getReportEffectivePath(url);
				document.getElementById("report").innerHTML = "<iframe style='width:100%;height:100%;overflow:auto;' frameborder='0' src=" + reportUrl + "></iframe>";
			},
			beforeload:  function(node){
			    tree.loader.dataUrl = ctx + '/reportView!findReportViewForTree.action?reportType='+reportType;
			}
		}
	});
	
	tree.getSelectionModel().on('beforeselect', function(me, newNode, oldNode) {
		if(!newNode.leaf){
			return ;
		}
		newNode.setIconCls('tickIcon');
		if (oldNode && oldNode.leaf) {
			oldNode.setIconCls('groupCheckedIcon');
		}
	});
	
	ReportView.tree = tree;
	

	
	new Ext.Viewport({
		layout: "border",
		items: [{
			region: "west",
			width: 200,
			layout: "fit",
			items: ReportView.tree
		},{
			id:'report',
			region: "center",
			border:false,
			layout: "fit"
		}]
		
	});
});