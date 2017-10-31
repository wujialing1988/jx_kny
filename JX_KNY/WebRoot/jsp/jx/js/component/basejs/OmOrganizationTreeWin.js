Ext.onReady(function() {
	Ext.namespace('OmOrganizationTreeWin');
	//机构树
	OmOrganizationTreeWin.tree = new Ext.tree.TreePanel({
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/omOrganizationSelect!customTree.action"
		}),
		root: new Ext.tree.AsyncTreeNode({
			text : systemOrgname,
			id : systemOrgid,
			leaf : false,
			orgseq : ''
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
    	height : 330,
    	collapsed : false,
    	listeners : {
			 beforeload: function(node, e){	        	
            	OmOrganizationTreeWin.tree.getLoader().dataUrl = ctx + '/omOrganizationSelect!customTree.action?parentIDX=' + node.id + '&isChecked=true';
	        },
	        render : function() {		        	
    			OmOrganizationTreeWin.tree.root.reload();
			    OmOrganizationTreeWin.tree.getRootNode().expand();
    		},
        	scope:this
		}
	});	
	OmOrganizationTreeWin.win = new Ext.Window({
        title:"机构选择", 
        width:300, 
        height:400, 
        plain:true, 
        autoScroll : true,
        closeAction:"hide", 
        buttonAlign:'center', 
        maximizable:false, 
        items:[OmOrganizationTreeWin.tree], 
        buttons: [{
               text: "确定", iconCls: "saveIcon", id: "btnOrgSubmit", scope: this, handler: function(){               	
               	OmOrganizationTreeWin.submit();
            }
        }]
	});
	//确认提交方法，后面可覆盖此方法完成查询
	OmOrganizationTreeWin.submit = function(){alert("请覆盖，OmOrganizationTreeWin.submit 方法完成自己操作业务！");};
});