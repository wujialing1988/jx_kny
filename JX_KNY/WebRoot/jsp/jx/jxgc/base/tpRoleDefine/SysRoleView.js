/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('SysRoleView');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	SysRoleView.searchParams = {};
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义角色表格开始 ************** */
	SysRoleView.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/sysRole!pageQuery.action',                 //装载列表数据的请求URL	
	    storeAutoLoad: true,
	    singleSelect: false,
	    tbar:['search'],
	    storeId:'roleid',
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
	     searchOrder:['rolename'],
         searchFn : function(searchParam) {
			SysRoleView.searchParams = searchParam;
			this.store.load();
		}
	});
	// 取消表格双击进行编辑的事件监听
	SysRoleView.grid.un('rowdblclick', SysRoleView.grid.toEditFn, SysRoleView.grid);
	// 数据加载时的参数设置
	SysRoleView.grid.store.on('beforeload', function(){
		var searchParam = SysRoleView.searchParams;
		var whereList = [];
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});
		}
		if(!Ext.isEmpty(FaultTicketRoleDefineView.faultTicketType) && "###" != FaultTicketRoleDefineView.faultTicketType)	     
		{
        	var sqlStr1 = "  not exists(SELECT R.ROLE_ID FROM JXGC_FAULT_TICKET_ROLE R WHERE  R.ROLE_ID = ROLEID AND R.RECORD_STATUS = 0 AND R.FAULT_TICKET_TYPE = '"+ FaultTicketRoleDefineView.faultTicketType +"')" ;		
        }		
		whereList.push({sql: sqlStr1, compare: Condition.SQL});	
	    var params = {
	    	start : 0,
			limit : SysRoleView.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.AcRole",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	/** ************** 角色表格结束 ************** */
	
	
	
	/** ************** 定义角色维护窗口开始 ************** */
	SysRoleView.win =new Ext.Window({
		title:"系统角色",
		width:560,
		height:402,
		layout:"fit",
		closeAction: 'hide',
		modal: true,
		items:[{
				xtype:"panel",
				layout:"fit",
				items: SysRoleView.grid			
			}
		],
		buttonAlign: 'center',
		buttons:[{
			text:'添加',iconCls:'addIcon',handler:function(){
	    		SysRoleView.submit();
			}
    	},{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	})
	/** ************** 定义角色维护窗口结束 ************** */
	//  确认提交方法，后面可覆盖此方法完成查询
	SysRoleView.submit = function(){alert("请覆盖，MatInforList.submit 方法完成自己操作业务！");}
});