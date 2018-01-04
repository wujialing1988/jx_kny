/**
 * 机构人员树 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('orgtree');                       //机构人员树命名空间
	orgtree.searchParams = {}; 
	
	//机构人员树
	orgtree.tree = new Ext.tree.TreePanel({
		tbar : new Ext.Toolbar(),
		plugins : ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/organization!tree.action"
		}),
		root: new Ext.tree.TreeLoader({
			text : i18n.Organization_Tree.InstitutionalStaff,
			disabled : false,
			id : 'ROOT_0',
			nodetype : 'org',
			leaf : false,
			iconCls : 'chart_organisationIcon'
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
    	collapsed : false,
//    	enableDrag:true,
    	listeners: {
    		render : function() {
    			orgtree.tree.root.reload();
			    orgtree.tree.getRootNode().expand();
    		},
    		click: function(node, e) {
    			if(node.id=='ROOT_0'){
    				OrgTab.OrgNodeId = null; //将当前所选树节点Id作为参数传递给tab页
    				OrgTab.PosNodeId = '';//设置上级岗位
    				OrgTab.hideTabPanelMethod(0);
    				orglist.grid.store.load();
    				OrgTab.currentNodeType = node.nodetype;
    				return;
    			}
    			OrgTab.currentNodeType = node.attributes.nodetype; //将当前所选树节点类型（org,emp等）作为参数传递给tab页
    			OrgTab.parentTreeNode = node.parentNode;
    			
    			// 记录组织机构树的展开路径，用于再重新加载树时能展开树节点
    			OrgTab.orgPath = node.getPath();
    			
    			//点击机构级节点时
    			if(node.attributes.nodetype=='org'){
    				OrgTab.OrgNodeId = node.attributes.orgid; //将当前所选树节点Id作为参数传递给tab页
    				OrgTab.OrgNodeName = node.text; //当前所选树节点的机构名称
    				OrgTab.PosNodeId = '';//设置上级岗位
    				OrgTab.hideTabPanelMethod(1); //调用tabPanel显示与隐藏方法
    				orgForm.currentInfoForm.getForm().reset();  //重置表单
    				var my97Ary = orgForm.currentInfoForm.findByType('my97date');
                    if(!Ext.isEmpty(my97Ary) && Ext.isArray(my97Ary)){
                    	for(var i = 0; i < my97Ary.length; i++){
	                        my97Ary[ i ].setValue('');
	                    }
                    }
                    var componentArray = ["EosDictEntry_combo","OmEmployee_SelectWin","OmPosition_SelectWin","WorkDuty_comboTree"];
                    for(var j = 0; j < componentArray.length; j++){
                    	var component = orgForm.currentInfoForm.findByType(componentArray[j]); //获取页面中所有控件
                    	if(Ext.isEmpty(component) || !Ext.isArray(component)){
                    		continue;
                    	}else{
                    		for(var i = 0; i < component.length; i++){
		                        component[ i ].clearValue();
		                    }
                    	}	                    
                    }
    				orgForm.findCurrentOrgInfo(); //调用js函数查询数据并填充至表单当中
    				orglist.grid.store.load();    //加载下级组织机构列表
    				positionlist.grid.store.load(); //加载下级岗位列表
    				emplist.grid.store.load();    //加载直属人员列表
    			} 
    			//点击岗位级节点时
    			else if(node.attributes.nodetype == 'pos'){
    				OrgTab.OrgNodeId = node.attributes.orgid; //将当前所选树节点Id作为参数传递给tab页
    				OrgTab.PosNodeId = node.attributes.positionid; //设置上级岗位
					OrgTab.hideTabPanelMethod(2);//调用tabPanel显示与隐藏方法
					posForm.cPosForm.getForm().reset();  //重置表单
    				var my97Ary = posForm.cPosForm.findByType('my97date');
                    if(!Ext.isEmpty(my97Ary) && Ext.isArray(my97Ary)){
                    	for(var i = 0; i < my97Ary.length; i++){
	                        my97Ary[ i ].setValue('');
	                    }
                    }
                    var componentArray = ["EosDictEntry_combo","OmEmployee_SelectWin","OmPosition_SelectWin","WorkDuty_comboTree"];
                    for(var j = 0; j < componentArray.length; j++){
                    	var component = posForm.cPosForm.findByType(componentArray[j]); //获取页面中所有控件
                    	if(Ext.isEmpty(component) || !Ext.isArray(component)){
                    		continue;
                    	}else{
                    		for(var i = 0; i < component.length; i++){
		                        component[ i ].clearValue();
		                    }
                    	}	                    
                    }
    				posForm.findCPosInfo(); //调用js函数查询数据并填充至表单当中
    				positionlist.grid.store.load(); //加载下级岗位列表
    				emplist.grid.store.load();    //加载直属人员列表
					
    			} else if (node.attributes.nodetype == 'emp'){
    				OrgTab.OrgNodeId = node.attributes.orgid; //将当前所选树节点Id作为参数传递给tab页
    				OrgTab.PosNodeId = node.attributes.positionid; //设置上级岗位
    				OrgTab.hideTabPanelMethod(3);//调用tabPanel显示与隐藏方法
    				empForm.cEmpForm.getForm().reset();  //重置表单
    				var my97Ary = empForm.cEmpForm.findByType('my97date');
                    if(!Ext.isEmpty(my97Ary) && Ext.isArray(my97Ary)){
                    	for(var i = 0; i < my97Ary.length; i++){
	                        my97Ary[ i ].setValue('');
	                    }
                    }
                    var componentArray = ["EosDictEntry_combo","OmEmployee_SelectWin","OmPosition_SelectWin","WorkDuty_comboTree"];
                    for(var j = 0; j < componentArray.length; j++){
                    	var component = empForm.cEmpForm.findByType(componentArray[j]); //获取页面中所有控件
                    	if(Ext.isEmpty(component) || !Ext.isArray(component)){
                    		continue;
                    	}else{
                    		for(var i = 0; i < component.length; i++){
		                        component[ i ].clearValue();
		                    }
                    	}	                    
                    }
					empForm.findCEmpInfo(node.attributes.empid);//调用js函数查询数据并填充至表单当中
    			}
    		}
    	}
	});
	
	orgtree.tree.on('beforeload', function(node){
		var tempid;
		if(node.id=='ROOT_0') tempid = node.id;
		else tempid = node.id.substring(2,node.id.length);
    	orgtree.tree.loader.dataUrl = ctx + '/organization!tree.action?nodeid='+tempid+'&nodetype='+node.attributes.nodetype;
	});
	
	// 树数据源加载完成后的事件监听
	orgtree.tree.on('load', function(node) {
		// 重新选择上一次编辑时选择的组织机构节点
		if (node.id == 'ROOT_0' && !Ext.isEmpty(OrgTab.orgPath)) {
			orgtree.tree.expandPath(OrgTab.orgPath);
			orgtree.tree.selectPath(OrgTab.orgPath);
		}
	});
});