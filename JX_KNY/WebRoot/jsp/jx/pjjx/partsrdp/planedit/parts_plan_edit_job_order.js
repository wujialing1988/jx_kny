Ext.onReady(function(){
	
	if(typeof(PartsPlanEdit) === 'undefined')
		Ext.ns("PartsPlanEdit");
	
	var tree, grid, card, nodeIdx, wpDesc, rdpIdx, reload = false;
	
	function createTree(){
		tree = new Ext.tree.TreePanel({
			loader : new Ext.tree.TreeLoader( {
		        dataUrl : ctx + "/partsRdpNodeQuery!tree.action"
		    }),
			root: new Ext.tree.AsyncTreeNode({
				text: wpDesc,
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
						if(card.items.length == 1){
							PartsPlanEdit.loadNodeGrid(node.id, false);
							card.add(PartsPlanEdit.createNodeTabs());
						}else{
							PartsPlanEdit.loadNodeGrid(node.id, true);
						}
						card.getLayout().setActiveItem(1);
					}else{
						nodeIdx = node.id;
						card.getLayout().setActiveItem(0);
						grid.store.reload();
					}
				},
				beforeload:  function(node){
					if (node == tree.getRootNode())
		        		tree.loader.dataUrl = ctx + '/partsRdpNodeQuery!tree.action?rdpIDX=' + rdpIdx + '&parentIDX=' + node.id;
		        	else
				    	tree.loader.dataUrl = ctx + '/partsRdpNodeQuery!tree.action?rdpIDX=' + rdpIdx + '&parentIDX=' + node.attributes["wpNodeIDX"];
				}
			}
		});
	}
	
	function createGrid(){
		grid = new Ext.yunda.Grid({
			loadURL: ctx + "/partsRdpNode!pageQuery.action",
			storeAutoLoad: false,
			tbar:[],
			fields: [{
				dataIndex: "idx", hidden:true
			},{
				header: "名称", dataIndex:"wpNodeName"
			},{
				header: "工期", dataIndex:"patedPeriod"
			}],
			toEditFn: notEidt
		});
		grid.store.on("beforeload", function(){
			var whereList = [];
			whereList.push({propName:"parentWPNodeIDX", propValue: nodeIdx});
			whereList.push({propName:"rdpIDX", propValue: rdpIdx});
			this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
			this.baseParams.sort = "seqNo";
			this.baseParams.dir = "asc";
		});
		
		grid.on("render", function(){
			this.tbar.dom.parentNode.removeChild(this.tbar.dom);
			this.doLayout();
		});
	}
	function createCard(){
		card = new Ext.Panel({
			region: "center",
			layout: "card",
			activeItem: 0,
			items: [grid]
		});
	}
	
	
	function createPanel(){
		createTree();
		createGrid();
		createCard();
		
		return new Ext.Panel({
			layout: "border",
			border: false,
			items: [{
				region: "west",
				width: 200,				
				collapsible: true,
				autoScroll: true,
				title: "流程节点",
				items: tree,
				layout: "fit"
			}, card]
		});
	}
	
	function loadTree(){
		if(tree){
			if(reload)
				tree.root.reload();
			tree.root.expand();
			tree.root.setText(wpDesc);
		}
	}
	
	PartsPlanEdit.jobOrderPanel = createPanel;
	
	PartsPlanEdit.setJobOrder = function(_record){
		if(rdpIdx != undefined)
			reload = true;
		wpDesc = _record.get("wpDesc");
		rdpIdx = _record.get("idx");
		loadTree();
	}
});