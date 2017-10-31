/**
 * 职务树 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('dutytree');                       //职务树命名空间
	dutytree.searchParams = {}; 
	
	//职务树
	dutytree.tree = new Ext.tree.TreePanel({
		tbar : new Ext.Toolbar(),
		plugins : ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/workDuty!tree.action"
		}),
		root: new Ext.tree.TreeLoader({
			text : '职务',
			disabled : false,
			id : 'ROOT_0',
			nodetype : 'dict',
			leaf : false,
			iconCls : 'chart_organisationIcon'
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
    	collapsed : false,
    	listeners: {
    		render : function() {
    			dutytree.tree.root.reload();
			    dutytree.tree.getRootNode().expand();
    		},
    		click: function(node, e) {
    			DutyTab.currentNode = node;
    			if(node.id=='ROOT_0'){
    				DutyTab.DutyTypeNodeId = null; //当前所点击的是根节点
    				DutyTab.DutyNodeId = null;     //当前未选中任何职务
    				DutyTab.currentNodeType = null; //当前所选节点不属于任何职务类型
    				DutyTab.hideTabPanelMethod(0); //调用函数隐藏tab
    				DutyList.grid.store.load();
    				return;
    			}
    			//点击职务列别级节点时
    			if(node.attributes.nodetype=='dict'){
    				DutyTab.DutyTypeNodeId = node.id;  //当前职务类别id
    				DutyTab.DutyNodeId = null;         //当前职务id
    				DutyTab.currentNodeType = node.attributes.nodetype; //当前节点类别
    				DutyTab.hideTabPanelMethod(1); //调用函数隐藏显示tab
    				DutyList.grid.store.load();
    			} 
    			//点击职务级节点时
    			else if(node.attributes.nodetype == 'duty'){
    				DutyTab.DutyTypeNodeId = node.attributes.dutytype;  //当前职务类别id
    				DutyTab.DutyNodeId = node.id;         //当前职务id
    				DutyTab.currentNodeType = node.attributes.nodetype; //当前节点类别
    				DutyTab.hideTabPanelMethod(2); 			//调用函数隐藏显示tab
    				DutyForm.findCurrentDutyInfo();//调用js函数查询数据并填充至表单当中
					LowerDutyList.grid.store.load();
					emplist.grid.store.load();
    			} 
    		}
    	}
	});
	
	dutytree.tree.on('beforeload', function(node){
    	dutytree.tree.loader.dataUrl = ctx + '/workDuty!tree.action?nodeid='+node.id+'&nodetype='+node.attributes.nodetype;
	});
	
});