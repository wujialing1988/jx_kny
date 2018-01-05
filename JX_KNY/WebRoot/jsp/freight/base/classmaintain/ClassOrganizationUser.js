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
		text : i18n.ClassOrganizationUser.dividedList,
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
			                MyExt.Msg.alert(i18n.ClassOrganizationUser.false+"\n" + response.status + "\n" + response.responseText);
			            }
			        });					
				}
			}
    	}
	});
	
ClassOrganizationUser.selectwin = new Ext.Window({
	title: i18n.ClassOrganizationUser.choiceDivided, maximizable: false, width: 350, height: 400, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center',modal:true,
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
	
/**
 * 数据加载store
 */
ClassOrganizationUser.store = new Ext.data.JsonStore({
	id:'idx', totalProperty:'totalProperty', 
	autoLoad:true, pruneModifiedRecords: true,
	storeId: 'idx',
	root: 'root',
	url:ctx+'/classOrganization!findOrganizationUsers.action',
	fields:['idx','workPersonIdx','workPersonName','classOrgIdx','orgUserIdx',
	        'orgIdx','queueCode','queueName','positionNo','positionName']
});

// 行选择模式
ClassOrganizationUser.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});


ClassOrganizationUser.grid = new Ext.grid.EditorGridPanel({  
  	border: true, enableColumnMove: false, stripeRows: false, selModel: ClassOrganizationUser.sm, loadMask: true,
	clicksToEdit: 1,
	viewConfig: {forceFit: true},
//	bbar: $yd.createPagingToolbar({pageSize: 50, store: ClassOrganizationUser.store}),
    store: ClassOrganizationUser.store,
	colModel:new Ext.grid.ColumnModel([ new Ext.grid.RowNumberer(),	
	 ClassOrganizationUser.sm,
	{
		header:i18n.ClassOrganizationUser.classOrgIdx, dataIndex:'classOrgIdx',hidden:true,width: 120,editor: {}
	},
     	{
		header:i18n.ClassOrganizationUser.orgIdx, dataIndex:'orgIdx',width: 120,hidden:true,editor: {}
	},
     	{
		header:i18n.ClassOrganizationUser.orgUserIdx, dataIndex:'orgUserIdx',width: 120,hidden:true,editor: {}
	},	
     	{
		header:i18n.ClassOrganizationUser.workPersonName, dataIndex:'workPersonName',width: 120,editor: {}
	},
     	{
		header:i18n.ClassOrganizationUser.workPersonIdx, dataIndex:'workPersonIdx',width: 120,hidden:true,editor: {}
	},
     	{
		header:i18n.ClassOrganizationUser.queueName, dataIndex:'queueName',width: 120,editor: {}
	},
     	{
		header:i18n.ClassOrganizationUser.queueCode, dataIndex:'queueCode',width: 120,hidden:true,editor: {}
	},	{
		header:i18n.ClassOrganizationUser.positionName, dataIndex:'positionName',width: 120, hidden:true,editor: {

		}
	},
     	{
		header:i18n.ClassOrganizationUser.positionName, dataIndex:'positionNo',width: 120,hidden:false,editor: {
			id:'positionNo_combo',
			xtype: 'EosDictEntry_combo',
			hiddenName: 'positionName',
			dicttypeid:'FREIGHT_LR',
			displayField:'dictname',valueField:'dictid',
			hasEmpty:"false",
			allowBlank: false
		},renderer: function(value, metaData, record) {
			return record.get('positionName');
		}
	},	
		{
		header:i18n.ClassOrganizationUser.idx, dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}
	]),
	tbar:[{
		text : i18n.ClassOrganizationUser.choiceDivided,		
		disabled:false,
		iconCls:"addIcon",
    	handler: function(){
			var records = ClassOrganizationUser.grid.getSelectionModel().getSelections();
			if(records.length == 0){
				MyExt.Msg.alert(i18n.ClassOrganizationUser.choicepeople);
				return;
			}    		
    		ClassOrganizationUser.selectwin.show();
    	}
	}],
    listeners: {
		cellclick:function(grid, rowIndex, columnIndex, e) {
			
		},
    	beforeedit: function(e) {
    		// 可编辑内容
			if(e.field == 'positionNo'){
				return true ;
			}
			return false ;
    	},
    	afteredit: function(e) {
    		var positionName = Ext.getCmp('positionNo_combo').lastSelectionText;
			var datas = new Array();
			var records = ClassOrganizationUser.grid.getSelectionModel().getSelections();					
			for (var i = 0; i < records.length; i++) {
				var data = {} ;
				data = records[i].data;	
				data.idx = data.orgUserIdx ;
				data.positionName = positionName ;
				delete data.orgUserIdx ;
				datas.push(data);
			}
	        Ext.Ajax.request({
	            url: ctx + '/classOrganization!saveOrganizationUsers.action',
	            jsonData: datas,
	            success: function(response, options){
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null) {
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
});

//定义全局变量保存查询条件
/*ClassOrganizationUser.searchParam = {} ;
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
});*/

//查询前添加过滤条件
ClassOrganizationUser.store.on('beforeload' , function(){
		this.baseParams.orgIdx = ClassOrganizationUser.orgIdx ;
		this.baseParams.classOrgIdx = ClassOrganizationUser.classOrgIdx ;
});

});