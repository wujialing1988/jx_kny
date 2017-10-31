/**
 * 机构人员树 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('WGTree');                       //机构人员树命名空间
	WGTree.searchParams = {}; 
	
	//机构人员树
	WGTree.tree = new Ext.tree.TreePanel({
		tbar : new Ext.Toolbar(),
		plugins : ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/workGroup!tree.action"
		}),
		root: new Ext.tree.TreeLoader({
			text : '工作组',
			disabled : false,
			id : 'ROOT_0',
			nodetype : 'gop',
			leaf : false,
			iconCls : 'chart_organisationIcon'
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
    	collapsed : false,
//    	baseCls: "x-plain",
    	listeners: {
    		render : function() {
    			WGTree.tree.root.reload();
			    WGTree.tree.getRootNode().expand();
    		},
    		click: function(node, e) {
    			if(node.id=='ROOT_0'){
    				WGTab.GroupNodeId = null; //将当前所选树节点Id作为参数传递给tab页
    				WGTab.PosNodeId = '';//设置上级岗位
    				WGTab.hideTabPanelMethod(0);
    				WGList.grid.store.load();
    				WGTab.currentNodeType = node.nodetype;
    				return;
    			}
    			WGTab.currentNodeType = node.attributes.nodetype; //将当前所选树节点类型（org,emp等）作为参数传递给tab页
    			//点击工作组级节点时
    			if(node.attributes.nodetype=='gop'){
					WGTab.GroupNodeId = node.attributes.groupid; //将当前所选树节点Id作为参数传递给tab页
    				WGTab.PosNodeId = '';//设置上级岗位
    				WGTab.hideTabPanelMethod(1); //调用tabPanel显示与隐藏方法
    				WGForm.cGroupForm.getForm().reset();  //重置表单
    				var my97Ary = WGForm.cGroupForm.findByType('my97date');
                    if(!Ext.isEmpty(my97Ary) && Ext.isArray(my97Ary)){
                    	for(var i = 0; i < my97Ary.length; i++){
	                        my97Ary[ i ].setValue('');
	                    }
                    }
                    var componentArray = ["EosDictEntry_combo","OmEmployee_SelectWin","WorkDuty_comboTree"];
                    for(var j = 0; j < componentArray.length; j++){
                    	var component = WGForm.cGroupForm.findByType(componentArray[j]); //获取页面中所有控件
                    	if(Ext.isEmpty(component) || !Ext.isArray(component)){
                    		continue;
                    	}else{
                    		for(var i = 0; i < component.length; i++){
		                        component[ i ].clearValue();
		                    }
                    	}	                    
                    }
    				WGForm.findCurrentGroupInfo(); //调用js函数查询数据并填充至表单当中
    				WGList.grid.store.load();    //加载下级组织机构列表
    				positionlist.grid.store.load(); //加载下级岗位列表
//    				emplist.grid.store.load();    //加载直属人员列表
    			} 
    			//点击岗位级节点时
    			else if(node.attributes.nodetype == 'pos'){
//    				WGTab.GroupNodeId = node.id; //将当前所选树节点Id作为参数传递给tab页
    				WGTab.PosNodeId = node.attributes.positionid; //设置上级岗位
					WGTab.hideTabPanelMethod(2);//调用tabPanel显示与隐藏方法
					posForm.cPosForm.getForm().reset();  //重置表单
    				var my97Ary = posForm.cPosForm.findByType('my97date');
                    if(!Ext.isEmpty(my97Ary) && Ext.isArray(my97Ary)){
                    	for(var i = 0; i < my97Ary.length; i++){
	                        my97Ary[ i ].setValue('');
	                    }
                    }
                    var componentArray = ["EosDictEntry_combo","OmEmployee_SelectWin","WorkDuty_comboTree"];
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
//    				emplist.grid.store.load();    //加载直属人员列表
					
    			} else if (node.attributes.nodetype == 'emp'){
					WGTab.GroupNodeId = node.attributes.orgid; //将当前所选树节点Id作为参数传递给tab页
    				WGTab.PosNodeId = node.attributes.positionid; //设置上级岗位
    				WGTab.hideTabPanelMethod(3);//调用tabPanel显示与隐藏方法
    				empForm.cEmpForm.getForm().reset();  //重置表单
    				var my97Ary = empForm.cEmpForm.findByType('my97date');
                    if(!Ext.isEmpty(my97Ary) && Ext.isArray(my97Ary)){
                    	for(var i = 0; i < my97Ary.length; i++){
	                        my97Ary[ i ].setValue('');
	                    }
                    }
                    var componentArray = ["EosDictEntry_combo","OmEmployee_SelectWin","WorkDuty_comboTree"];
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
	
	WGTree.tree.on('beforeload', function(node){
		var tempid;
		if(node.id=='ROOT_0') tempid = node.id;
		else tempid = node.id.substring(2,node.id.length);
    	WGTree.tree.loader.dataUrl = ctx + '/workGroup!tree.action?nodeid='+tempid+'&nodetype='+node.attributes.nodetype;
	});
	
});