Ext.onReady(function() {
	
	Ext.namespace('Operator');                       //操作员管理命名空间
	Operator.searchParams = {status:'running'};      //初始化查询状态为正常的用户
	
	Operator.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/operator!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/operator!updateOperatorInfo.action',             //保存数据的请求URL
	    deleteURL: ctx + '/operator!delete.action',            //删除数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    loadMask: null,
	    storeAutoLoad: true,
	    storeId:'operatorid',
	    tbar:['search','delete','refresh','-',
	    	{xtype:"label", text:"操作员状态:"},
	    	{xtype:"radio",name:"statuschoose", boxLabel:"启用&nbsp;&nbsp;&nbsp;&nbsp;", checked:true,
	    		handler:function(radio, checked){
	    			if(checked){
	    				Operator.searchParams = {status:'running'};
	    				Operator.grid.store.load();
	    			}
	    		}
	    	},
	    	{xtype:"radio", name:"statuschoose", boxLabel:"禁用&nbsp;&nbsp;&nbsp;&nbsp;",
      	        handler:function(radio, checked){
      	        	if(checked){
      	        		Operator.searchParams = {status:'stop'};
      	        		Operator.grid.store.load();
      	        	}
      	        }
	    	}
	    ],
	    	
		fields: [
			{header:'操作员ID', dataIndex:'operatorid', hidden:true, editor: { xtype:'hidden' }},
			{header:'登录用户名', dataIndex:'userid', hidden:false, editor: {allowBlank:false,maxLength:32}},
			{header:'密码', dataIndex:'password', hidden:true, editor: { inputType:'password', allowBlank:false, maxLength:32 }},
			{header:'密码失效日期', dataIndex:'invaldate', hidden:true, editor: { xtype:'hidden' }},
			{header:'操作员名称', dataIndex:'operatorname', hidden:false, editor: { allowBlank:false, maxLength:32}},
			{header:'认证模式', dataIndex:'authmode', hidden:true, editor: { xtype:'hidden' }},
			{header:'操作员状态', dataIndex:'status', hidden:false, 
			 editor: {
			 	id:'_status_1',allowBlank:false,xtype: 'combo',dataColumn :"status", hiddenName: "status",  mode: 'local' ,valueField: "value", displayField: "text", triggerAction: "all",   		 	
            		editable: false, forceSelection: true, store:[["running","启用"],["stop","禁用"]]
			 },
			  renderer:function(v){ if(v == 'running'){return '启用';} else if(v == 'stop'){return '禁用';} else {return '禁用';}}
			},
			{header:'解锁时间', dataIndex:'unlocktime', hidden:true, editor: { xtype:'hidden' }},
			{header:'菜单风格', dataIndex:'menutype', hidden:true, editor: { xtype:'hidden' }},
			{header:'最后一次登录时间', dataIndex:'lastlogin', hidden:true, editor: { xtype:'hidden' }},
			{header:'最新登录次数', dataIndex:'errcount', hidden:true, editor: { xtype:'hidden' }},
			{header:'有效开始时间', dataIndex:'startdate', hidden:true, editor: { xtype:'hidden' }},
			{header:'有效结束时间', dataIndex:'enddate', hidden:true, editor: { xtype:'hidden' }},
			{header:'有效时间范围', dataIndex:'validtime', hidden:true, editor: { xtype:'hidden' }},
			{header:'MAC码', dataIndex:'maccode', hidden:true, editor: { xtype:'hidden' }},
			{header:'iP地址', dataIndex:'ipaddress', hidden:true, editor: { xtype:'hidden' }},
			{header:'邮箱', dataIndex:'email', hidden:true, editor: { xtype:'hidden' }}
		],
		editOrder:[],
		searchOrder:['userid'],
		//回显操作员状态控件
		afterShowEditWin: function(record, rowIndex){
	    	Ext.getCmp('_status_1').setValue(record.data.status);
		},
		searchFn : function(searchParam) {
			Operator.searchParams = searchParam;
			this.store.load();
		}
	});

	Operator.grid.store.setDefaultSort('userid', 'asc');
	
	Operator.grid.store.on('beforeload', function() {
		var searchParam = Operator.searchParams;
		var whereList = [];
		
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop],compare:8});
		}
	    var params = {
	    	start : 0,
			limit : Operator.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.AcOperator",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:Operator.grid });
});