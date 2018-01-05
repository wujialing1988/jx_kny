/** 应用功能树 */
Ext.onReady(function() {
	Ext.namespace('AllotPowerTree');
	AllotPowerTree.searchParams = {}; 
	var dataAry = new Array();
	
	function setChkBox (node, isChk){
		//向上遍历
		setParentNode(node, isChk);
		//向下遍历
		setChildNode(node, isChk);
	}
	
	//设置父节点的勾选状态
	function setParentNode(node, isChk){
		if(node.attributes.realid == '0') return false;
		if(node.parentNode.id == 'ROOT_0') return;
		if(isChk) {
			node.parentNode.getUI().checkbox.checked = isChk;
			node.parentNode.attributes.checked = isChk;
			setParentNode(node.parentNode, isChk);
		} else {
			var childs = node.parentNode.childNodes; //找到与之同级的所有节点
			var hasChked = false;
			for(var i = 0; i<childs.length; i++){
				if(childs[i].getUI().checkbox.checked){
					hasChked = true;
					break;
				}
			}
			if(hasChked){
				return false;
			} else {
				node.parentNode.getUI().checkbox.checked = isChk;
				node.parentNode.attributes.checked = isChk;
				setParentNode(node.parentNode, isChk);
			}
		}
	}
	//设置子节点的勾选状态
	function setChildNode(node, isChk){
		if(typeof(node) == 'undefined') {
			return false;
		}
		else if(node.leaf) {
			return false;
		}
		else {
			var childs = node.childNodes; //找到该节点的所有子节点
			for(var i = 0; i<childs.length; i++){
				childs[i].getUI().checkbox.checked = isChk;
				childs[i].attributes.checked = isChk;
				setChildNode(childs[i],isChk);
			}
		}
	}
	//获取树中被选中的节点
	function getHasChkedNode(node){
		if(typeof(node) == 'undefined') {
			return false;
		}
		else if(node.leaf) {
			if(node.attributes.checked == true && node.attributes.nodetype == 'func'){
				var data = {};
				data.roleid = SysRole.grid.selModel.getSelections()[0].get("roleid");
				data.appid = node.attributes.appid;
				data.funcgroupid = node.attributes.funcgroupid;
				data.funccode = node.attributes.funccode;
				dataAry.push(data);
			}
		}
		else {
			var childs = node.childNodes; //找到该节点的所有子节点
			for(var i = 0; i<childs.length; i++){
				getHasChkedNode(childs[i]);
			}
		}
	}
	
	function confirmAndSave (cfg){
	    Ext.Msg.confirm(i18n.AllotPowerTree.prompt, i18n.AllotPowerTree.confirmToSave, function(btn){
	        if(btn != 'yes')    return;
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });    
	}
	
	//权限树
	AllotPowerTree.tree = new Ext.tree.TreePanel({
		tbar : ['-',{
	    	text:i18n.AllotPowerTree.save, iconCls:"saveIcon", handler: function(){
	    		dataAry = new Array();
	    		var root = AllotPowerTree.tree.getRootNode();
	    		getHasChkedNode(root);
	    		var cfg = {
		            scope: this, url: ctx + '/sysRoleFunc!saveRoleAndFunc.action', jsonData: dataAry,
		            params : {roleid:SysRole.grid.selModel.getSelections()[0].get("roleid")},
		            success: function(response, options){
//		                if(self.loadMask)   self.loadMask.hide();
		                var result = Ext.util.JSON.decode(response.responseText);
		                if (result.errMsg == null) {
							alertSuccess();
		                } else {
		                    alertFail(result.errMsg);
		                }
		                AllotPowerTree.tree.root.reload();
	    				/* 加载并出同步树的所有叶子节点 */
	    				AllotPowerTree.tree.expandAll(); //全部展开
		            }
		        };
		        confirmAndSave(cfg);
	    	}
		}],
		plugins : ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/sysApp!tree.action"
		}),
		root: new Ext.tree.TreeLoader({
			text :i18n.AllotPowerTree.funcPermissions,
			disabled : false,
			id : 'ROOT_0',
			leaf : false,
			realid : 'ROOT_0',
			iconCls : 'chart_organisationIcon'
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : true,
    	collapsed : false,
    	listeners: {
    		render : function() {
//			    AllotPowerTree.tree.root.reload();
//			    AllotPowerTree.tree.getRootNode().expand();
//    			AllotPowerTree.tree.expandAll(); //展开所有节点
    		},
    		//树checkbox的勾选和取消事件
    		checkchange : function(node, e){
				setChkBox(node, e);
    		}
    	}
	});
	
	AllotPowerTree.tree.on('beforeload', function(node){
		var roleid = SysRole.grid.selModel.getSelections()[0].get("roleid");
    	AllotPowerTree.tree.loader.dataUrl = ctx + '/sysApp!tree.action?&roleid='+roleid;
	});
	
	/** 权限分配窗口 */
	AllotPowerTree.win = new Ext.Window({
		title: i18n.AllotPowerTree.funcPermissions,
		width: 350, height: 500, 
		layout: "fit", 
		plain: true, border:false, maximizable: false,  
		closeAction: "hide",
		items : [AllotPowerTree.tree],
		buttonAlign: 'center', 
		buttons:[{
			text:i18n.AllotPowerTree.close, iconCls:'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
});