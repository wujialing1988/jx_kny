Ext.onReady(function() {
	Ext.namespace('OrgTreeAdjust');
	
	OrgTreeAdjust.widgetType;//树控件的数据查询类别，1-机构 2-机构+岗位
	//机构树
	OrgTreeAdjust.tree = new Ext.tree.TreePanel({
		tbar : new Ext.Toolbar(),
		plugins : ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/organization!orgAdjust.action"
		}),
		root: new Ext.tree.TreeLoader({
			text : i18n.OrganizationTreeAdjust.InstitutionalAdjustment,
			disabled : false,
			id : 'ROOT_0',
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
			    Ext.getCmp('OrgTreeAdjustSaveBtn').setDisabled(true);
    		},
    		beforeshow : function (){
				Ext.getCmp('OrgTreeAdjustSaveBtn').setDisabled(true);
    		},
    		click: function(node, e) {
    			Ext.getCmp('OrgTreeAdjustSaveBtn').setDisabled(false);
    		}
    	}
	});
	
	OrgTreeAdjust.tree.on('beforeload', function(node){
		var chkNodeAry = orglist.grid.selModel.getSelections();
		var orgids = new Array();
		for(var i=0; i< chkNodeAry.length; i++){
			orgids.push(chkNodeAry[i].get("orgid"));
		}
    	OrgTreeAdjust.tree.loader.dataUrl = ctx + '/organization!orgAdjust.action?chkNodeid='+orgids;
	});
	
	OrgTreeAdjust.transferWin = new Ext.Window({
            title:i18n.OrganizationTreeAdjust.InstitutionalTransfer, 
            width:300, 
            height:400, 
            plain:true, 
            autoScroll : true,
            closeAction:"hide", 
            buttonAlign:'center', 
            maximizable:false, 
            items:[OrgTreeAdjust.tree], 
            buttons: [{
                id:'OrgTreeAdjustSaveBtn',text: i18n.OrganizationTreeAdjust.confirm, iconCls: "saveIcon", scope: this, handler: function(){ 
                   	var node = OrgTreeAdjust.tree.getSelectionModel().getSelectedNode(); //获取选中的机构
                   	var saveUrl = ctx + '/organization!updateOrgAdjust.action'; //更新机构层级
                   	//执行保存的AJAX请求
					Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		            	scope: orglist.grid, url: saveUrl, params: {orgid:node.attributes.orgid, ids: $yd.getSelectedIdx(orglist.grid, orglist.grid.storeId)}
		        	}));
				   OrgTreeAdjust.transferWin.hide();
                }
            }],
           listeners: {
           		beforehide : function (){
           			Ext.getCmp('OrgTreeAdjustSaveBtn').setDisabled(true);
//           			EmpCtl.ZZMask.hide();
           		}
           }
	});
});