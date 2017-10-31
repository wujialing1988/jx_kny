/**
 * 人员分组
 */
Ext.onReady(function(){
	
Ext.namespace('ClassOrganizationUser');                       //定义命名空间

// 班组
ClassOrganizationUser.orgIdx = "" ; // 班组

ClassOrganizationUser.classOrgIdx = "" ; // 班组班次对应ID


// 选择分队
ClassOrganizationUser.tree = new Ext.tree.TreePanel({
	loader : new Ext.tree.TreeLoader({
		dataUrl : ctx + "/sysEosDictEntry!tree.action?dicttypeid=CLASS_ORG_QUEUE"
	}),
	root: new Ext.tree.TreeLoader({
		disabled : false,
		id : 'ROOT_0',
		text : '分队列表',
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
		click: function(node, e) {
				if(node.leaf){
					var datas = new Array();
					var queueCode = node.attributes.dictid ;
					var queueName = node.attributes.dictname ;
					var records = ClassOrganizationUser.grid.getSelectionModel().getSelections();					
					for (var i = 0; i < records.length; i++) {
						var data = {} ;
						data = records[i].data;	
						data.idx = data.orgUserIdx ;
						data.orgIdx = ClassOrganizationUser.orgIdx ;
						data.classOrgIdx = ClassOrganizationUser.classOrgIdx ;
						data.queueCode = queueCode ;
						data.queueName = queueName ;
						delete data.orgUserIdx ;
						datas.push(data);
					}
			        Ext.Ajax.request({
			            url: ctx + '/classOrganization!saveOrganizationUsers.action',
			            jsonData: datas,
			            success: function(response, options){
			                var result = Ext.util.JSON.decode(response.responseText);
			                if (result.errMsg == null) {
			                	ClassOrganizationUser.selectwin.hide();
			                	ClassOrganizationUser.grid.store.reload();
			                } else {
			                    alertFail(result.errMsg);
			                }
			            },
			            failure: function(response, options){
			                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			            }
			        });					
				}
			}
    	}
	});
	
ClassOrganizationUser.selectwin = new Ext.Window({
	title: "选择分队", maximizable: false, width: 350, height: 400, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center',modal:true,
	items : [ {
        collapsible : false,
        width : 180,
        split : false,
        bodyBorder: false,
        autoScroll : true,
        items : [ClassOrganizationUser.tree]
    }],
    listeners: {
    	show:function(me){
    		ClassOrganizationUser.tree.getRootNode().expand();
    	}
    }
});	
	

//定义全局变量保存查询条件
ClassOrganizationUser.searchParam = {} ;
ClassOrganizationUser.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/classOrganization!findOrganizationUsers.action',                 //装载列表数据的请求URL
    singleSelect: false, saveFormColNum:1,
    labelWidth: 60,                                     //查询表单中的标签宽度
    fieldWidth: 130,
    isEdit: false,
    page: false,
	tbar : [
	{
		text : '选择分队',		
		disabled:false,
		iconCls:"addIcon",
    	handler: function(){
			var records = ClassOrganizationUser.grid.getSelectionModel().getSelections();
			if(records.length == 0){
				MyExt.Msg.alert("请先选择人员！");
				return;
			}    		
    		ClassOrganizationUser.selectwin.show();
    	}
	}
	],
	fields: [
     	{
		header:'班组班次对应ID', dataIndex:'classOrgIdx',hidden:true,width: 120,editor: {}
	},
     	{
		header:'班组ID', dataIndex:'orgIdx',width: 120,hidden:true,editor: {}
	},
     	{
		header:'分队ID', dataIndex:'orgUserIdx',width: 120,hidden:true,editor: {}
	},	
     	{
		header:'人员名称', dataIndex:'workPersonName',width: 120,editor: {}
	},
     	{
		header:'人员ID', dataIndex:'workPersonIdx',width: 120,hidden:true,editor: {}
	},
     	{
		header:'分队', dataIndex:'queueName',width: 120,editor: {}
	},
     	{
		header:'分队编码', dataIndex:'queueCode',width: 120,hidden:true,editor: {}
	},	
		{
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}],
	searchFn: function(searchParam){ 
		ClassOrganizationUser.searchParam = searchParam ;
        ClassOrganizationUser.grid.store.load();
	},
	beforeShowEditWin: function(record, rowIndex){ 
		return false ;
	}	
});

//查询前添加过滤条件
ClassOrganizationUser.grid.store.on('beforeload' , function(){
		this.baseParams.orgIdx = ClassOrganizationUser.orgIdx ;
		this.baseParams.classOrgIdx = ClassOrganizationUser.classOrgIdx ;
});

});