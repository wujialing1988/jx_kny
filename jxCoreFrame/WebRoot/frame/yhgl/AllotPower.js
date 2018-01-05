/** 系统角色权限分配页面各选择控件及列表 */
Ext.onReady(function() {
	/** 操作员列表 */
	Ext.namespace('AllotPower.Operator');
	AllotPower.Operator.searchParams = {}; 
	
	AllotPower.Operator.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/operator!findOperatorListByRole.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/sysRole!deleteOperatorToRole.action',            		//删除数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    storeAutoLoad: false,
	    singleSelect: false,
	    storeId:'operatorid',
	    tbar:['search',{
	    	text:i18n.AllotPower.add, iconCls:"addIcon", handler: function(){
	    		AllotPower.SelectWin.Operator.show();
	    		AllotPower.AddOperator.grid.store.load();
	    	}
	    },'delete'],
	    fields: [
	    	{header:i18n.AllotPower.operatorid, dataIndex:'operatorid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.AllotPower.userid, dataIndex:'userid', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.operatorname, dataIndex:'operatorname', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.status, dataIndex:'status', hidden:false, editor: { xtype:'hidden' },
	    	renderer:function(v){ if(v == 'running'){return i18n.AllotPower.enable;} else if(v == 'stop'){return i18n.AllotPower.disable;} else {return i18n.AllotPower.disable;}}}
	    ],
	    editOrder:[],
	    searchOrder:['userid','operatorname'],
	    searchFn : function(searchParam) {
			AllotPower.Operator.searchParams = searchParam;
			this.store.load();
		},
		deleteButtonFn: function(){                         
	       	if(!$yd.isSelectedRecord(AllotPower.Operator.grid)) return;//未选择记录，直接返回
	    	if(AllotPower.Operator.grid.searchWin)  AllotPower.Operator.grid.searchWin.hide();   
	        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	        if(!AllotPower.Operator.grid.beforeDeleteFn()) return;
	        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
	        $yd.confirmAndDelete({
	            scope: AllotPower.Operator.grid, 
	            url: AllotPower.Operator.grid.deleteURL, 
	            params: {
	            	roleid : SysRole.grid.selModel.getSelections()[0].get("roleid"),
	            	ids: $yd.getSelectedIdx(AllotPower.Operator.grid, AllotPower.Operator.grid.storeId)
	            }
	        });
	    }
	});
	
	//移除侦听器
	AllotPower.Operator.grid.un('rowdblclick', AllotPower.Operator.grid.toEditFn, AllotPower.Operator.grid); //对双击行编辑事件禁用
	
	AllotPower.Operator.grid.store.setDefaultSort('userid', 'asc');

	AllotPower.Operator.grid.store.on('beforeload', function() {
		var searchParam = AllotPower.Operator.searchParams;
		var whereList = [];
		whereList.push({propName : 'roleid', propValue : SysRole.grid.selModel.getSelections()[0].get("roleid"), compare:1});
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});
		}
	    var params = {
	    	start : 0,
			limit : AllotPower.Operator.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.AcRole",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	/** 机构列表 */
	Ext.namespace('AllotPower.Organization');
	AllotPower.Organization.searchParams = {}; 
	
	AllotPower.Organization.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/organization!findOrganizationListByRole.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/sysRole!deleteOrganizationToRole.action',            		//删除数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    storeAutoLoad: false,
	    singleSelect: false,
	    storeId:'orgid',
	    tbar:['search',{
	    	text:i18n.AllotPower.add, iconCls:"addIcon", handler: function(){
	    		AllotPower.SelectWin.Organization.show();
	    		AllotPower.AddOrganization.grid.store.load();
	    	}
	    },'delete'],
	    fields: [
	    	{header:i18n.AllotPower.orgid, dataIndex:'orgid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.AllotPower.orgcode, dataIndex:'orgcode', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.orgname, dataIndex:'orgname', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.orgtype, dataIndex:'orgtype', hidden:false, editor: { xtype:'hidden' },
	    	 renderer: function(v,metadata, record,rowIndex, colIndex, store){ return EosDictEntry.getDictname('ABF_ORGTYPE',v); }
	    	},
	    	{header:i18n.AllotPower.status, dataIndex:'status', hidden:false, editor: { xtype:'hidden' },
	    	 renderer: function(v,metadata, record,rowIndex, colIndex, store){ return EosDictEntry.getDictname('ABF_ORGSTATUS',v);}
	    	}
	    ],
	    editOrder:[],
	    searchOrder:['orgcode','orgname'],
	    searchFn : function(searchParam) {
			AllotPower.Organization.searchParams = searchParam;
			this.store.load();
		},
		deleteButtonFn: function(){                         
	       	if(!$yd.isSelectedRecord(AllotPower.Organization.grid)) return;//未选择记录，直接返回
	    	if(AllotPower.Organization.grid.searchWin)  AllotPower.Organization.grid.searchWin.hide();   
	        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	        if(!AllotPower.Organization.grid.beforeDeleteFn()) return;
	        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
	        $yd.confirmAndDelete({
	            scope: AllotPower.Organization.grid, 
	            url: AllotPower.Organization.grid.deleteURL, 
	            params: {
	            	roleid : SysRole.grid.selModel.getSelections()[0].get("roleid"),
	            	ids: $yd.getSelectedIdx(AllotPower.Organization.grid, AllotPower.Organization.grid.storeId)
	            }
	        });
	    }
	});
	
	//移除侦听器
	AllotPower.Organization.grid.un('rowdblclick', AllotPower.Organization.grid.toEditFn, AllotPower.Organization.grid); //对双击行编辑事件禁用
	
	AllotPower.Organization.grid.store.setDefaultSort('orgcode', 'asc');

	AllotPower.Organization.grid.store.on('beforeload', function() {
		var searchParam = AllotPower.Organization.searchParams;
		var whereList = [];
		whereList.push({propName : 'roleid', propValue : SysRole.grid.selModel.getSelections()[0].get("roleid"), compare:1});
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});
		}
	    var params = {
	    	start : 0,
			limit : AllotPower.Organization.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.AcRole",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	/** 工作组列表 */
	Ext.namespace('AllotPower.Group');
	AllotPower.Group.searchParams = {}; 
	
	AllotPower.Group.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/workGroup!findGroupListByRole.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/sysRole!deleteGroupToRole.action',            		//删除数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    storeAutoLoad: false,
	    singleSelect: false,
	    storeId:'groupid',
	    tbar:['search',{
	    	text:i18n.AllotPower.add, iconCls:"addIcon", handler: function(){
	    		AllotPower.SelectWin.Group.show();
	    		AllotPower.AddGroup.grid.store.load();
	    	}
	    },'delete'],
	    fields: [
	    	{header:i18n.AllotPower.groupid, dataIndex:'groupid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.AllotPower.groupname, dataIndex:'groupname', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.grouptype, dataIndex:'grouptype', hidden:false, editor: { xtype:'hidden' },
	    	 renderer: function(v,metadata, record,rowIndex, colIndex, store){ return EosDictEntry.getDictname('ABF_GROUPTYPE',v); }},
	    	{header:i18n.AllotPower.groupstatus, dataIndex:'groupstatus', hidden:false, editor: { xtype:'hidden' },
	    	 renderer: function(v,metadata, record,rowIndex, colIndex, store){ return EosDictEntry.getDictname('ABF_GROUPSTATUS',v);}}
	    ],
	    editOrder:[],
	    searchOrder:['groupname'],
	    searchFn : function(searchParam) {
			AllotPower.Group.searchParams = searchParam;
			this.store.load();
		},
		deleteButtonFn: function(){                         
	       	if(!$yd.isSelectedRecord(AllotPower.Group.grid)) return;//未选择记录，直接返回
	    	if(AllotPower.Group.grid.searchWin)  AllotPower.Group.grid.searchWin.hide();   
	        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	        if(!AllotPower.Group.grid.beforeDeleteFn()) return;
	        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
	        $yd.confirmAndDelete({
	            scope: AllotPower.Group.grid, 
	            url: AllotPower.Group.grid.deleteURL, 
	            params: {
	            	roleid : SysRole.grid.selModel.getSelections()[0].get("roleid"),
	            	ids: $yd.getSelectedIdx(AllotPower.Group.grid, AllotPower.Group.grid.storeId)
	            }
	        });
	    }
	});
	
	//移除侦听器
	AllotPower.Group.grid.un('rowdblclick', AllotPower.Group.grid.toEditFn, AllotPower.Group.grid); //对双击行编辑事件禁用
	
	AllotPower.Group.grid.store.setDefaultSort('groupname', 'asc');

	AllotPower.Group.grid.store.on('beforeload', function() {
		var searchParam = AllotPower.Group.searchParams;
		var whereList = [];
		whereList.push({propName : 'roleid', propValue : SysRole.grid.selModel.getSelections()[0].get("roleid"), compare:1});
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});
		}
	    var params = {
	    	start : 0,
			limit : AllotPower.Group.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.AcRole",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	/** 岗位列表 */
	Ext.namespace('AllotPower.Position');
	AllotPower.Position.searchParams = {}; 
	
	AllotPower.Position.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/position!findPositionListByRole.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/sysRole!deletePositionToRole.action',            		//删除数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    storeAutoLoad: false,
	    singleSelect: false,
	    storeId:'positionid',
	    tbar:['search',{
	    	text:i18n.AllotPower.add, iconCls:"addIcon", handler: function(){
	    		AllotPower.SelectWin.Position.show();
	    		AllotPower.AddPosition.grid.store.load();
	    	}
	    },'delete'],
	    fields: [
	    	{header:i18n.AllotPower.positionid, dataIndex:'positionid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.AllotPower.posicode, dataIndex:'posicode', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.posiname, dataIndex:'posiname', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.positype, dataIndex:'positype', hidden:false, editor: { xtype:'hidden' },
	    	 renderer: function(v,metadata, record,rowIndex, colIndex, store){ return EosDictEntry.getDictname('ABF_POSITYPE',v);}},
	    	{header:i18n.AllotPower.status, dataIndex:'status',   hidden:false, editor: { xtype:'hidden' },
	    	 renderer: function(v,metadata, record,rowIndex, colIndex, store){ return EosDictEntry.getDictname('ABF_POSISTATUS',v);}}
	    	
	    ],
	    editOrder:[],
	    searchOrder:['posicode','posiname'],
	    searchFn : function(searchParam) {
			AllotPower.Position.searchParams = searchParam;
			this.store.load();
		},
		deleteButtonFn: function(){
	       	if(!$yd.isSelectedRecord(AllotPower.Position.grid)) return;//未选择记录，直接返回
	    	if(AllotPower.Position.grid.searchWin)  AllotPower.Position.grid.searchWin.hide();   
	        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	        if(!AllotPower.Position.grid.beforeDeleteFn()) return;
	        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
	        $yd.confirmAndDelete({
	            scope: AllotPower.Position.grid, 
	            url: AllotPower.Position.grid.deleteURL, 
	            params: {
	            	roleid : SysRole.grid.selModel.getSelections()[0].get("roleid"),
	            	ids: $yd.getSelectedIdx(AllotPower.Position.grid, AllotPower.Position.grid.storeId)
	            }
	        });
	    }
	});
	
	//移除侦听器
	AllotPower.Position.grid.un('rowdblclick', AllotPower.Position.grid.toEditFn, AllotPower.Position.grid); //对双击行编辑事件禁用
	
	AllotPower.Position.grid.store.setDefaultSort('posicode', 'asc');

	AllotPower.Position.grid.store.on('beforeload', function() {
		var searchParam = AllotPower.Position.searchParams;
		var whereList = [];
		whereList.push({propName : 'roleid', propValue : SysRole.grid.selModel.getSelections()[0].get("roleid"), compare:1});
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});
		}
	    var params = {
	    	start : 0,
			limit : AllotPower.Position.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.AcRole",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	/** 职务列表 */
	Ext.namespace('AllotPower.Duty');
	AllotPower.Duty.searchParams = {}; 
	
	AllotPower.Duty.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/workDuty!findDutyListByRole.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/sysRole!deleteDutyToRole.action',            		//删除数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    storeAutoLoad: false,
	    singleSelect: false,
	    storeId:'dutyid',
	    tbar:['search',{
	    	text:i18n.AllotPower.add, iconCls:"addIcon", handler: function(){
	    		AllotPower.SelectWin.Duty.show();
	    		AllotPower.AddDuty.grid.store.load();
	    	}
	    },'delete'],
	    fields: [
	    	{header:i18n.AllotPower.dutyid,  dataIndex:'dutyid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.AllotPower.dutycode, dataIndex:'dutycode', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.dutyname, dataIndex:'dutyname', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.dutytype, dataIndex:'dutytype', hidden:false, editor: { xtype:'hidden' },
	    	 renderer: function(v,metadata, record,rowIndex, colIndex, store){ return EosDictEntry.getDictname('ABF_DUTYTYPE',v);}}
	    ],
	    editOrder:[],
	    searchOrder:['dutycode','dutyname'],
	    searchFn : function(searchParam) {
			AllotPower.Duty.searchParams = searchParam;
			this.store.load();
		},
		deleteButtonFn: function(){
	       	if(!$yd.isSelectedRecord(AllotPower.Duty.grid)) return;//未选择记录，直接返回
	    	if(AllotPower.Duty.grid.searchWin)  AllotPower.Duty.grid.searchWin.hide();   
	        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	        if(!AllotPower.Duty.grid.beforeDeleteFn()) return;
	        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
	        $yd.confirmAndDelete({
	            scope: AllotPower.Duty.grid, 
	            url: AllotPower.Duty.grid.deleteURL, 
	            params: {
	            	roleid : SysRole.grid.selModel.getSelections()[0].get("roleid"),
	            	ids: $yd.getSelectedIdx(AllotPower.Duty.grid, AllotPower.Duty.grid.storeId)
	            }
	        });
	    }
	});
	
	//移除侦听器
	AllotPower.Duty.grid.un('rowdblclick', AllotPower.Duty.grid.toEditFn, AllotPower.Duty.grid); //对双击行编辑事件禁用
	
	AllotPower.Duty.grid.store.setDefaultSort('dutycode', 'asc');

	AllotPower.Duty.grid.store.on('beforeload', function() {
		var searchParam = AllotPower.Duty.searchParams;
		var whereList = [];
		whereList.push({propName : 'roleid', propValue : SysRole.grid.selModel.getSelections()[0].get("roleid"), compare:1});
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});
		}
	    var params = {
	    	start : 0,
			limit : AllotPower.Duty.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.AcRole",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	
	/***************************************************************************************************/
	/** 未配置角色的操作员列表 */
	Ext.namespace('AllotPower.AddOperator');
	AllotPower.AddOperator.searchParams = {}; 
	
	AllotPower.AddOperator.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/operator!findOperatorListByRole.action',           //装载列表数据的请求URL
		saveURL: ctx + '/sysRole!saveOperatorToRole.action',             	//保存数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    storeAutoLoad: false,
	    singleSelect: false,
	    storeId:'operatorid',
	    tbar:['search',{
	    	text:i18n.AllotPower.save, iconCls:"saveIcon", handler: function(){
	    		if(!$yd.isSelectedRecord(AllotPower.AddOperator.grid)) return;//未选择记录，直接返回
	    		if(AllotPower.AddOperator.grid.searchWin)  AllotPower.AddOperator.grid.searchWin.hide();   
	    		Ext.Ajax.request(Ext.apply(AllotPower.SelectWin.cfgAjaxRequest2(AllotPower.Operator.grid), {
		            scope: AllotPower.AddOperator.grid, 
		            url: AllotPower.AddOperator.grid.saveURL, 
		            params: {
		            	roleid : SysRole.grid.selModel.getSelections()[0].get("roleid"),
		            	ids    : $yd.getSelectedIdx(AllotPower.AddOperator.grid, AllotPower.AddOperator.grid.storeId)}
		        }));
	    	}
	    }],
	    fields: [
	    	{header:i18n.AllotPower.operatorid, dataIndex:'operatorid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.AllotPower.userid, dataIndex:'userid', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.operatorname, dataIndex:'operatorname', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.status, dataIndex:'status', hidden:false, editor: { xtype:'hidden' },
	    	renderer:function(v){ if(v == 'running'){return i18n.AllotPower.enable;} else if(v == 'stop'){return i18n.AllotPower.disable;} else {return i18n.AllotPower.disable;}}}
	    ],
	    editOrder:[],
	    searchOrder:['userid','operatorname'],
	    searchFn : function(searchParam) {
			AllotPower.AddOperator.searchParams = searchParam;
			this.store.load();
		}
	});
	
	//移除侦听器
	AllotPower.AddOperator.grid.un('rowdblclick', AllotPower.AddOperator.grid.toEditFn, AllotPower.AddOperator.grid); //对双击行编辑事件禁用
	
	AllotPower.AddOperator.grid.store.setDefaultSort('userid', 'asc');

	AllotPower.AddOperator.grid.store.on('beforeload', function() {
		var searchParam = AllotPower.AddOperator.searchParams;
		var whereList = [];
		whereList.push({propName : 'roleid', propValue : SysRole.grid.selModel.getSelections()[0].get("roleid"), compare:1});
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});
		}
	    var params = {
	    	start : 0,
			limit : AllotPower.AddOperator.grid.pagingToolbar.pageSize,
			relevance : true, //标识，代表查询未关联角色的操作员
			queryClassName : "com.yunda.frame.yhgl.entity.AcRole",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	/** 未配置角色的机构列表 */
	Ext.namespace('AllotPower.AddOrganization');
	AllotPower.AddOrganization.searchParams = {}; 
	
	AllotPower.AddOrganization.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/organization!findOrganizationListByRole.action',                 //装载列表数据的请求URL
		saveURL: ctx + '/sysRole!saveOrganizationToRole.action',             	//保存数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    storeAutoLoad: false,
	    singleSelect: false,
	    storeId:'orgid',
	    tbar:['search',{
	    	text:i18n.AllotPower.save, iconCls:"saveIcon", handler: function(){
	    		if(!$yd.isSelectedRecord(AllotPower.AddOrganization.grid)) return;//未选择记录，直接返回
	    		if(AllotPower.AddOrganization.grid.searchWin)  AllotPower.AddOrganization.grid.searchWin.hide();   
	    		Ext.Ajax.request(Ext.apply(AllotPower.SelectWin.cfgAjaxRequest2(AllotPower.Organization.grid), {
		            scope: AllotPower.AddOrganization.grid, 
		            url: AllotPower.AddOrganization.grid.saveURL, 
		            params: {
		            	roleid : SysRole.grid.selModel.getSelections()[0].get("roleid"),
		            	ids    : $yd.getSelectedIdx(AllotPower.AddOrganization.grid, AllotPower.AddOrganization.grid.storeId)}
		        }));
	    	}
	    }],
	    fields: [
	    	{header:i18n.AllotPower.orgid, dataIndex:'orgid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.AllotPower.orgcode, dataIndex:'orgcode', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.orgname, dataIndex:'orgname', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.orgtype, dataIndex:'orgtype', hidden:false, editor: { xtype:'hidden' },
	    	 renderer: function(v,metadata, record,rowIndex, colIndex, store){ return EosDictEntry.getDictname('ABF_ORGTYPE',v); }
	    	},
	    	{header:i18n.AllotPower.status, dataIndex:'status', hidden:false, editor: { xtype:'hidden' },
	    	 renderer: function(v,metadata, record,rowIndex, colIndex, store){ return EosDictEntry.getDictname('ABF_ORGSTATUS',v);}
	    	}
	    ],
	    editOrder:[],
	    searchOrder:['orgcode','orgname'],
	    searchFn : function(searchParam) {
			AllotPower.AddOrganization.searchParams = searchParam;
			this.store.load();
		}
	});
	
	//移除侦听器
	AllotPower.AddOrganization.grid.un('rowdblclick', AllotPower.AddOrganization.grid.toEditFn, AllotPower.AddOrganization.grid); //对双击行编辑事件禁用
	
	AllotPower.AddOrganization.grid.store.setDefaultSort('orgcode', 'asc');

	AllotPower.AddOrganization.grid.store.on('beforeload', function() {
		var searchParam = AllotPower.AddOrganization.searchParams;
		var whereList = [];
		whereList.push({propName : 'roleid', propValue : SysRole.grid.selModel.getSelections()[0].get("roleid"), compare:1});
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});
		}
	    var params = {
	    	start : 0,
			limit : AllotPower.AddOrganization.grid.pagingToolbar.pageSize,
			relevance : true, //标识，代表查询未关联角色的机构
			queryClassName : "com.yunda.frame.yhgl.entity.AcRole",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	/** 未配置角色的工作组列表 */
	Ext.namespace('AllotPower.AddGroup');
	AllotPower.AddGroup.searchParams = {}; 
	
	AllotPower.AddGroup.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/workGroup!findGroupListByRole.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/sysRole!saveGroupToRole.action',             	//保存数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    storeAutoLoad: false,
	    singleSelect: false,
	    storeId:'groupid',
	    tbar:['search',{
	    	text:i18n.AllotPower.save, iconCls:"saveIcon", handler: function(){
	    		if(!$yd.isSelectedRecord(AllotPower.AddGroup.grid)) return;//未选择记录，直接返回
	    		if(AllotPower.AddGroup.grid.searchWin)  AllotPower.AddGroup.grid.searchWin.hide();   
	    		Ext.Ajax.request(Ext.apply(AllotPower.SelectWin.cfgAjaxRequest2(AllotPower.Group.grid), {
		            scope: AllotPower.AddGroup.grid, 
		            url: AllotPower.AddGroup.grid.saveURL, 
		            params: {
		            	roleid : SysRole.grid.selModel.getSelections()[0].get("roleid"),
		            	ids    : $yd.getSelectedIdx(AllotPower.AddGroup.grid, AllotPower.AddGroup.grid.storeId)}
		        }));
	    	}
	    }],
	    fields: [
	    	{header:i18n.AllotPower.groupid, dataIndex:'groupid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.AllotPower.groupname, dataIndex:'groupname', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.grouptype, dataIndex:'grouptype', hidden:false, editor: { xtype:'hidden' },
	    	 renderer: function(v,metadata, record,rowIndex, colIndex, store){ return EosDictEntry.getDictname('ABF_GROUPTYPE',v); }},
	    	{header:i18n.AllotPower.groupstatus, dataIndex:'groupstatus', hidden:false, editor: { xtype:'hidden' },
	    	 renderer: function(v,metadata, record,rowIndex, colIndex, store){ return EosDictEntry.getDictname('ABF_GROUPSTATUS',v);}}
	    ],
	    editOrder:[],
	    searchOrder:['groupname'],
	    searchFn : function(searchParam) {
			AllotPower.AddGroup.searchParams = searchParam;
			this.store.load();
		}
	});
	
	//移除侦听器
	AllotPower.AddGroup.grid.un('rowdblclick', AllotPower.AddGroup.grid.toEditFn, AllotPower.AddGroup.grid); //对双击行编辑事件禁用
	
	AllotPower.AddGroup.grid.store.setDefaultSort('groupname', 'asc');

	AllotPower.AddGroup.grid.store.on('beforeload', function() {
		var searchParam = AllotPower.AddGroup.searchParams;
		var whereList = [];
		whereList.push({propName : 'roleid', propValue : SysRole.grid.selModel.getSelections()[0].get("roleid"), compare:1});
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});
		}
	    var params = {
	    	start : 0,
			limit : AllotPower.AddGroup.grid.pagingToolbar.pageSize,
			relevance : true, //标识，代表查询未关联角色的工作组
			queryClassName : "com.yunda.frame.yhgl.entity.AcRole",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	/** 未配置角色的岗位列表 */
	Ext.namespace('AllotPower.AddPosition');
	AllotPower.AddPosition.searchParams = {}; 
	
	AllotPower.AddPosition.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/position!findPositionListByRole.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/sysRole!savePositionToRole.action',             	//保存数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    storeAutoLoad: false,
	    singleSelect: false,
	    storeId:'positionid',
	    tbar:['search',{
	    	text:i18n.AllotPower.save, iconCls:"saveIcon", handler: function(){
	    		if(!$yd.isSelectedRecord(AllotPower.AddPosition.grid)) return;//未选择记录，直接返回
	    		if(AllotPower.AddPosition.grid.searchWin)  AllotPower.AddPosition.grid.searchWin.hide();   
	    		Ext.Ajax.request(Ext.apply(AllotPower.SelectWin.cfgAjaxRequest2(AllotPower.Position.grid), {
		            scope: AllotPower.AddPosition.grid, 
		            url: AllotPower.AddPosition.grid.saveURL, 
		            params: {
		            	roleid : SysRole.grid.selModel.getSelections()[0].get("roleid"),
		            	ids    : $yd.getSelectedIdx(AllotPower.AddPosition.grid, AllotPower.AddPosition.grid.storeId)}
		        }));
	    	}
	    }],
	    fields: [
	    	{header:i18n.AllotPower.positionid, dataIndex:'positionid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.AllotPower.posicode, dataIndex:'posicode', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.posiname, dataIndex:'posiname', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.positype, dataIndex:'positype', hidden:false, editor: { xtype:'hidden' },
	    	 renderer: function(v,metadata, record,rowIndex, colIndex, store){ return EosDictEntry.getDictname('ABF_POSITYPE',v);}},
	    	{header:i18n.AllotPower.status, dataIndex:'status',   hidden:false, editor: { xtype:'hidden' },
	    	 renderer: function(v,metadata, record,rowIndex, colIndex, store){ return EosDictEntry.getDictname('ABF_POSISTATUS',v);}}
	    	
	    ],
	    editOrder:[],
	    searchOrder:['posicode','posiname'],
	    searchFn : function(searchParam) {
			AllotPower.AddPosition.searchParams = searchParam;
			this.store.load();
		}
	});
	
	//移除侦听器
	AllotPower.AddPosition.grid.un('rowdblclick', AllotPower.AddPosition.grid.toEditFn, AllotPower.AddPosition.grid); //对双击行编辑事件禁用
	
	AllotPower.AddPosition.grid.store.setDefaultSort('posicode', 'asc');

	AllotPower.AddPosition.grid.store.on('beforeload', function() {
		var searchParam = AllotPower.AddPosition.searchParams;
		var whereList = [];
		whereList.push({propName : 'roleid', propValue : SysRole.grid.selModel.getSelections()[0].get("roleid"), compare:1});
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});
		}
	    var params = {
	    	start : 0,
			limit : AllotPower.AddPosition.grid.pagingToolbar.pageSize,
			relevance : true, //标识，代表查询未关联角色的岗位
			queryClassName : "com.yunda.frame.yhgl.entity.AcRole",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	/** 未配置角色的职务列表 */
	Ext.namespace('AllotPower.AddDuty');
	AllotPower.AddDuty.searchParams = {}; 
	
	AllotPower.AddDuty.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/workDuty!findDutyListByRole.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/sysRole!saveDutyToRole.action',             	//保存数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    storeAutoLoad: false,
	    singleSelect: false,
	    storeId:'dutyid',
	    tbar:['search',{
	    	text:i18n.AllotPower.save, iconCls:"saveIcon", handler: function(){
	    		if(!$yd.isSelectedRecord(AllotPower.AddDuty.grid)) return;//未选择记录，直接返回
	    		if(AllotPower.AddDuty.grid.searchWin)  AllotPower.AddDuty.grid.searchWin.hide();   
	    		Ext.Ajax.request(Ext.apply(AllotPower.SelectWin.cfgAjaxRequest2(AllotPower.Duty.grid), {
		            scope: AllotPower.AddDuty.grid, 
		            url: AllotPower.AddDuty.grid.saveURL, 
		            params: {
		            	roleid : SysRole.grid.selModel.getSelections()[0].get("roleid"),
		            	ids    : $yd.getSelectedIdx(AllotPower.AddDuty.grid, AllotPower.AddDuty.grid.storeId)}
		        }));
	    	}
	    }],
	    fields: [
	    	{header:i18n.AllotPower.dutyid,  dataIndex:'dutyid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.AllotPower.dutycode, dataIndex:'dutycode', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.dutyname, dataIndex:'dutyname', hidden:false, editor: { xtype:'hidden' }, searcher:{}},
	    	{header:i18n.AllotPower.dutytype, dataIndex:'dutytype', hidden:false, editor: { xtype:'hidden' },
	    	 renderer: function(v,metadata, record,rowIndex, colIndex, store){ return EosDictEntry.getDictname('ABF_DUTYTYPE',v);}}
	    ],
	    editOrder:[],
	    searchOrder:['dutycode','dutyname'],
	    searchFn : function(searchParam) {
			AllotPower.AddDuty.searchParams = searchParam;
			this.store.load();
		}
	});
	
	//移除侦听器
	AllotPower.AddDuty.grid.un('rowdblclick', AllotPower.AddDuty.grid.toEditFn, AllotPower.AddDuty.grid); //对双击行编辑事件禁用
	
	AllotPower.AddDuty.grid.store.setDefaultSort('dutycode', 'asc');

	AllotPower.AddDuty.grid.store.on('beforeload', function() {
		var searchParam = AllotPower.AddDuty.searchParams;
		var whereList = [];
		whereList.push({propName : 'roleid', propValue : SysRole.grid.selModel.getSelections()[0].get("roleid"), compare:1});
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});
		}
	    var params = {
	    	start : 0,
			limit : AllotPower.AddDuty.grid.pagingToolbar.pageSize,
			relevance : true, //标识，代表查询未关联角色的职务
			queryClassName : "com.yunda.frame.yhgl.entity.AcRole",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	/***************************************************************************************************/
	/** 选择窗口 */
	Ext.namespace('AllotPower.SelectWin');
	AllotPower.SelectWin.searchParams = {};
	
	AllotPower.SelectWin.cfgAjaxRequest2 = function(scope){
		var cfg = {
		    //请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            this.store.reload(); 
		            this.afterDeleteFn();
		            scope.store.reload();
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert(i18n.AllotPower.prompt,i18n.AllotPower.false + response.status + "\n" + response.responseText);
		    }
		};
	    return cfg;
	}
	
	AllotPower.SelectWin.Operator = new Ext.Window({
		title:i18n.AllotPower.operatorChoice, maximizable: false, width: 650, height: 400, layout: "fit", plain: true, closeAction: "hide", buttonAlign: 'center', border:false,
		items : [{xtype: 'panel',	border:true,  align:'center',  buttonAlign: "center", baseCls: "x-plain", layout: "fit", items: [AllotPower.AddOperator.grid]}]
	});
	
	AllotPower.SelectWin.Organization = new Ext.Window({
		title: i18n.AllotPower.orgChoice, maximizable: false, width: 650, height: 400, layout: "fit", plain: true, closeAction: "hide", buttonAlign: 'center', border:false,
		items : [{xtype: 'panel',	border:true,  layout:'fit',	align:'center',  buttonAlign: "center", baseCls: "x-plain", items: [AllotPower.AddOrganization.grid]}]
	});
	
	AllotPower.SelectWin.Group = new Ext.Window({
		title: i18n.AllotPower.WorkGroupCho, maximizable: false, width: 650, height: 400, layout: "fit", plain: true, closeAction: "hide", buttonAlign: 'center', border:false,
		items : [{xtype: 'panel',	border:true,  layout:'fit',	align:'center',  buttonAlign: "center", baseCls: "x-plain", items: [AllotPower.AddGroup.grid]}]
	});
	
	AllotPower.SelectWin.Position = new Ext.Window({
		title: i18n.AllotPower.positChoice, maximizable: false, width: 650, height: 400, layout: "fit", plain: true, closeAction: "hide", buttonAlign: 'center', border:false,
		items : [{xtype: 'panel',	border:true,  layout:'fit',	align:'center',  buttonAlign: "center", baseCls: "x-plain", items: [AllotPower.AddPosition.grid]}]
	});
	
	AllotPower.SelectWin.Duty = new Ext.Window({
		title:i18n.AllotPower.jobChoice, maximizable: false, width: 650, height: 400, layout: "fit", plain: true, closeAction: "hide", buttonAlign: 'center', border:false,
		items : [{xtype: 'panel',	border:true,  layout:'fit',	align:'center',  buttonAlign: "center", baseCls: "x-plain", items: [AllotPower.AddDuty.grid]}]
	});
});