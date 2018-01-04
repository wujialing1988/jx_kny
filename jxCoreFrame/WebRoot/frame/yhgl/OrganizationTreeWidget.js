Ext.onReady(function() {
	Ext.namespace('OrgTreeWidget');
	
	OrgTreeWidget.widgetType;//树控件的数据查询类别，1-机构 2-机构+岗位
	//机构树
	OrgTreeWidget.tree = new Ext.tree.TreePanel({
		tbar : new Ext.Toolbar(),
		plugins : ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/organization!treeWidget.action"
		}),
		root: new Ext.tree.TreeLoader({
			text : i18n.OrganizationTreeWidget.orgEmp,
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
    	height : 330,
    	collapsed : false,
    	listeners: {
    		render : function() {
    			OrgTreeWidget.tree.root.reload();
			    OrgTreeWidget.tree.getRootNode().expand();
			     Ext.getCmp('OrgTreeWidgetSaveBtn').setDisabled(true);
    		},
    		click: function(node, e) {
    			Ext.getCmp('OrgTreeWidgetSaveBtn').setDisabled(false);
    		}
    	}
	});
	
	OrgTreeWidget.tree.on('beforeload', function(node){
		var tempid;
		if(node.id=='ROOT_0') tempid = node.id;
		else tempid = node.id.substring(2,node.id.length);
    	OrgTreeWidget.tree.loader.dataUrl = ctx + '/organization!treeWidget.action?nodeid='+tempid+'&nodetype='+node.attributes.nodetype+'&widgetType='+OrgTreeWidget.widgetType;
	});
	
	OrgTreeWidget.transferWin = new Ext.Window({
            title:i18n.OrganizationTreeWidget.orgMobiliz, 
            width:300, 
            height:400, 
            plain:true, 
            autoScroll : true,
            closeAction:"hide", 
            buttonAlign:'center', 
            maximizable:false, 
            items:[OrgTreeWidget.tree], 
            buttons: [{
                id:'OrgTreeWidgetSaveBtn',text: i18n.OrganizationTreeWidget.confirm, iconCls: "saveIcon", scope: this, handler: function(){ 
                   var empids = $yd.getSelectedIdx(EmpCtl.grid, EmpCtl.grid.storeId);
                   var node = OrgTreeWidget.tree.getSelectionModel().getSelectedNode(); //获取选中的机构
                   
                   var tempid;
				   if(node.id=='ROOT_0') tempid = node.id;
				   else tempid = node.id.substring(2,node.id.length);
                   
                   var saveUrl = '';
                   if(OrgTreeWidget.widgetType!=null && OrgTreeWidget.widgetType=='1'){
                   		saveUrl = ctx + '/employee!updateEmpOrg.action'; //更新人员-机构关系
                   		//执行保存的AJAX请求
		        	   Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		               	scope: EmpCtl.grid, url: saveUrl, params: {orgid:tempid, ids: $yd.getSelectedIdx(EmpCtl.grid, EmpCtl.grid.storeId)}
		        	   }));
                   } else if(OrgTreeWidget.widgetType!=null && OrgTreeWidget.widgetType=='2'){
                   	   saveUrl = ctx + '/employee!updateEmpPosi.action'; //更新人员-岗位关系
                   		//执行保存的AJAX请求
		        	   Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		               	scope: EmpCtl.grid, url: saveUrl, params: {positionid:tempid, ismain:'y', ids: $yd.getSelectedIdx(EmpCtl.grid, EmpCtl.grid.storeId)}
		        	   }));
                   }
				   OrgTreeWidget.transferWin.hide();
                }
            }],
           listeners: {
           		beforehide : function (){
           			Ext.getCmp('OrgTreeWidgetSaveBtn').setDisabled(true);
           			EmpCtl.ZZMask.hide();
           		}
           }
	});
});