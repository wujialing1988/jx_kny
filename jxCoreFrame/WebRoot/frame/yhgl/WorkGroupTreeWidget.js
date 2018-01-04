Ext.onReady(function() {
	Ext.namespace('WGTreeWidget');
	
	WGTreeWidget.widgetType;//树控件的数据查询类别，1-机构 2-机构+岗位
	WGTreeWidget.empid; //人员ID
	WGTreeWidget.groupid; //工作组ID
	WGTreeWidget.positionid;  //岗位ID
	//工作组树
	WGTreeWidget.tree = new Ext.tree.TreePanel({
		tbar : new Ext.Toolbar(),
		plugins : ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/workGroup!treeWidget.action"
		}),
		root: new Ext.tree.TreeLoader({
			text : i18n.WorkGroupTreeWidget.WorkGroup,
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
    	height : 330,
    	collapsed : false,
    	listeners: {
    		render : function() {
    			WGTreeWidget.tree.root.reload();
			    WGTreeWidget.tree.getRootNode().expand();
			    Ext.getCmp('WGTreeWidgetSaveBtn').setDisabled(true);
    		},
    		click: function(node, e) {
    			Ext.getCmp('WGTreeWidgetSaveBtn').setDisabled(false);
    		}
    	}
	});
	
	WGTreeWidget.tree.on('beforeload', function(node){
    	WGTreeWidget.tree.loader.dataUrl = ctx + '/workGroup!treeWidget.action?nodeid='+node.id+'&nodetype='+node.attributes.nodetype+'&widgetType='+WGTreeWidget.widgetType+'&empid='+WGTreeWidget.empid;
	});
	
	WGTreeWidget.transferWin = new Ext.Window({
            title:i18n.WorkGroupTreeWidget.WGroupMobiliz, 
            width:300, 
            height:400, 
            plain:true, 
            autoScroll : true,
            closeAction:"hide", 
            buttonAlign:'center', 
            maximizable:false, 
            items:[WGTreeWidget.tree], 
            buttons: [{
                id:'WGTreeWidgetSaveBtn',text: i18n.WorkGroupTreeWidget.confirm, iconCls: "saveIcon", scope: this, handler: function(){ 
                   if(WGTreeWidget.tree.getSelectionModel().getSelectedNode()==null){
                   		return;
                   }
                   var saveUrl = empids = node ='';
                   if(WGTreeWidget.widgetType!=null && WGTreeWidget.widgetType=='1'){
                   	    /***更新用户工作组**/
                     	node = WGTreeWidget.tree.getSelectionModel().getSelectedNode(); //获取选中的机构
                   		saveUrl = ctx + '/employee!updateEmpGroup.action'; //更新人员-工作组关系
                   		//执行保存的AJAX请求
		        	   	Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		               		scope: GroupCtl.WorkGroupGrid, url: saveUrl, params: {groupid:node.id, empid:WGTreeWidget.empid, oldgroupid:WGTreeWidget.groupid}
		        	   	}));
		        	   	
                   } else if(WGTreeWidget.widgetType!=null && WGTreeWidget.widgetType=='2'){
                   		/***更新用户岗位**/
                     	node = WGTreeWidget.tree.getSelectionModel().getSelectedNode(); //获取选中的机构
                   		saveUrl = ctx + '/employee!updateEmpPosi.action'; //更新人员-岗位关系
                   	//执行保存的AJAX请求
		        	   	Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		               		scope: PosiCtl.PosiGrid, url: saveUrl, params: {positionid:node.id, ismain:'n', empid:WGTreeWidget.empid, oldpositionid:WGTreeWidget.positionid}
		        	   	}));
                   }
                   Ext.getCmp('WGTreeWidgetSaveBtn').setDisabled(true);
                   WGTreeWidget.transferWin.hide();
                }
            }],
           listeners: {
           		beforehide : function (){
           			Ext.getCmp('WGTreeWidgetSaveBtn').setDisabled(true);
           		}
           }
	});
});