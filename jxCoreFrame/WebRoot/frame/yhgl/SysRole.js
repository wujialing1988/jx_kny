/** 系统角色列表 */
Ext.onReady(function() {
	Ext.namespace('SysRole');
	SysRole.searchParams = {}; 
	
	SysRole.Mask = new Ext.LoadMask(Ext.getBody(), {msg:null}); //遮罩
	
	SysRole.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/sysRole!pageQuery.action',                 //装载列表数据的请求URL
		saveURL: ctx + '/sysRole!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/sysRole!delete.action',            //删除数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    storeAutoLoad: true,
	    singleSelect: false,
	    storeId:'roleid',
	    tbar:['search','add','delete','refresh','-',{
	    	text:"权限分配", iconCls:"editIcon", handler: function(){
	    		if(SysRole.grid.searchWin)  SysRole.grid.searchWin.hide();   
        		if(!$yd.isSelectedRecord(SysRole.grid)) return;//未选择记录，直接返回
        		AllotPowerMain.win.show();
        		Ext.getCmp('_tabPanel').setActiveTab(0);
        		AllotPower.Operator.grid.store.load(); //第一个选项卡操作员表加载数据
	    	}
	    },{
	    	text:"功能权限", iconCls:"pluginIcon", handler: function(){
	    		AllotPowerTree.win.show();
	    		AllotPowerTree.tree.root.reload();
	    		/* 加载并出同步树的所有叶子节点 */
	    		AllotPowerTree.tree.expandAll(); //全部展开
	    		/* 加载并展开完毕后，收起树节点， 该功能根据需求决定是否开放 */
//	    		setTimeout("AllotPowerTree.tree.collapseAll();AllotPowerTree.tree.getRootNode().expand();",500);
	    	}
	    }],
	    fields: [
	    	{header:'角色主键', dataIndex:'roleid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:'角色名称', dataIndex:'rolename', hidden:false, editor: { maxLength:32,allowBlank:false }},
	    	{header:'角色类型', dataIndex:'roletype', hidden:false, 
	    	 editor: { id:'_roletype_list0', disabled:true,
	    		xtype: 'combo',dataColumn :"roletype", hiddenName: "roletype",  mode: 'local' ,valueField: "value", displayField: "text", triggerAction: "all",   		 	
            	editable: false, forceSelection: true, store:[["0","应用级"],["1","系统级"]]
   			 },
			 renderer:function(v){ if(v == '0'){return '应用级';} else if(v == '1'){return '系统级';} else {return '应用级';}}
			},
	    	{header:'角色描述', dataIndex:'roledesc', hidden:false, editor: { xtype:'textarea',maxLength:200 }},
	    	{header:'应用ID', dataIndex:'appid', hidden:true, editor: { xtype:'hidden' }}
	    ],
	    editOrder:['roletype','rolename','roledesc'],
	    searchOrder:['rolename'],
	    //新增時，默認
	    afterShowSaveWin: function(){
	    	Ext.getCmp('_roletype_list0').setValue(0);
	    },
	    //回显角色类型控件
		afterShowEditWin: function(record, rowIndex){
	    	Ext.getCmp('_roletype_list0').setValue(record.data.roletype);
		},
		beforeGetFormData: function(){
			Ext.getCmp('_roletype_list0').setDisabled(false);
		},
		afterGetFormData: function(){
			Ext.getCmp('_roletype_list0').setDisabled(true);
		}, 
		beforeSaveFn: function(data){ 
			if(data!=null&&data.roleid=='')
				delete data.roleid;
			return true; 
		},
	    searchFn : function(searchParam) {
			SysRole.searchParams = searchParam;
			this.store.load();
		}
	});
	
	SysRole.grid.store.setDefaultSort('rolename', 'asc');

	SysRole.grid.store.on('beforeload', function() {
		var searchParam = SysRole.searchParams;
		var whereList = [];
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});
		}
	    var params = {
	    	start : 0,
			limit : SysRole.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.AcRole",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:SysRole.grid });
});