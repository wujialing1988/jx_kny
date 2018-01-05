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
	    	{xtype:"label", text:i18n.OperatorCtl.operatorType},
	    	{xtype:"radio",name:"statuschoose", boxLabel:i18n.OperatorCtl.Enabled+"&nbsp;&nbsp;&nbsp;&nbsp;", checked:true,
	    		handler:function(radio, checked){
	    			if(checked){
	    				Operator.searchParams = {status:'running'};
	    				Operator.grid.store.load();
	    			}
	    		}
	    	},
	    	{xtype:"radio", name:"statuschoose", boxLabel:i18n.OperatorCtl.Disabled+"&nbsp;&nbsp;&nbsp;&nbsp;",
      	        handler:function(radio, checked){
      	        	if(checked){
      	        		Operator.searchParams = {status:'stop'};
      	        		Operator.grid.store.load();
      	        	}
      	        }
	    	}
	    ],
	    	
		fields: [
			{header:i18n.OperatorCtl.operatorid, dataIndex:'operatorid', hidden:true, editor: { xtype:'hidden' }},
			{header:i18n.OperatorCtl.userid, dataIndex:'userid', hidden:false, editor: {allowBlank:false,maxLength:32}},
			{header:i18n.OperatorCtl.password, dataIndex:'password', hidden:true, editor: { inputType:'password', allowBlank:false, maxLength:32 }},
			{header:i18n.OperatorCtl.invaldate, dataIndex:'invaldate', hidden:true, editor: { xtype:'hidden' }},
			{header:i18n.OperatorCtl.operatorname, dataIndex:'operatorname', hidden:false, editor: { allowBlank:false, maxLength:32}},
			{header:i18n.OperatorCtl.authmode, dataIndex:'authmode', hidden:true, editor: { xtype:'hidden' }},
			{header:i18n.OperatorCtl.status, dataIndex:'status', hidden:false, 
			 editor: {
			 	id:'_status_1',allowBlank:false,xtype: 'combo',dataColumn :"status", hiddenName: "status",  mode: 'local' ,valueField: "value", displayField: "text", triggerAction: "all",   		 	
            		editable: false, forceSelection: true, store:[["running",i18n.OperatorCtl.Enabled],["stop",i18n.OperatorCtl.Disabled]]
			 },
			  renderer:function(v){ if(v == 'running'){return i18n.OperatorCtl.Enabled;} else if(v == 'stop'){return i18n.OperatorCtl.Disabled;} else {return i18n.OperatorCtl.Disabled;}}
			},
			{header:i18n.OperatorCtl.unlocktime, dataIndex:'unlocktime', hidden:true, editor: { xtype:'hidden' }},
			{header:i18n.OperatorCtl.menutype, dataIndex:'menutype', hidden:true, editor: { xtype:'hidden' }},
			{header:i18n.OperatorCtl.lastlogin, dataIndex:'lastlogin', hidden:true, editor: { xtype:'hidden' }},
			{header:i18n.OperatorCtl.errcount, dataIndex:'errcount', hidden:true, editor: { xtype:'hidden' }},
			{header:i18n.OperatorCtl.startdate, dataIndex:'startdate', hidden:true, editor: { xtype:'hidden' }},
			{header:i18n.OperatorCtl.enddate, dataIndex:'enddate', hidden:true, editor: { xtype:'hidden' }},
			{header:i18n.OperatorCtl.validtime, dataIndex:'validtime', hidden:true, editor: { xtype:'hidden' }},
			{header:i18n.OperatorCtl.maccode, dataIndex:'maccode', hidden:true, editor: { xtype:'hidden' }},
			{header:i18n.OperatorCtl.ipaddress, dataIndex:'ipaddress', hidden:true, editor: { xtype:'hidden' }},
			{header:i18n.OperatorCtl.email, dataIndex:'email', hidden:true, editor: { xtype:'hidden' }}
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