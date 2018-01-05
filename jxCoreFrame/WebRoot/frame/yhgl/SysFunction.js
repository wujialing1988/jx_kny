/** 系统应用功能 */
Ext.onReady(function() {
	/** 系统应用 */
	Ext.namespace('Sys.App');
	Sys.App.searchParams = {}; 
	
	Sys.App.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/sysApp!pageQuery.action',                 //装载列表数据的请求URL
		saveURL: ctx + '/sysApp!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/sysApp!delete.action',            //删除数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    storeAutoLoad: true,
	    singleSelect: false,
	    loadMask: {msg: null},
	    storeId:'appid',
	    tbar:['search','add','delete','-',{
	    	text:i18n.SysFuncion.funcMaintain, iconCls:"pluginIcon", handler: function(){
	    		if(Sys.App.grid.searchWin)  Sys.App.grid.searchWin.hide();   
        		if(!$yd.isSelectedRecord(Sys.App.grid)) return;//未选择记录，直接返回
        		if(Sys.App.grid.selModel.getSelections().length>1){ MyExt.Msg.alert("只能选择一个应用！"); return;} //
        		functionWin.show();
        		Sys.Funcgroup.tree.root.reload();
			    Sys.Funcgroup.tree.getRootNode().expand();
	    	}
	    }],
	    fields: [
	    	{header:i18n.SysFuncion.AppId, dataIndex:'appid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.appcode, dataIndex:'appcode', hidden:false, editor: { maxLength:10, allowBlank:false, vtype:'alphanum2'}},
	    	{header:i18n.SysFuncion.appname, dataIndex:'appname', hidden:false, editor: { maxLength:25, allowBlank:false, vtype:'validChar' }},
	    	{header:i18n.SysFuncion.apptype, dataIndex:'apptype', hidden:true, editor: { xtype:'hidden',value:'0' }},
	    	{header:i18n.SysFuncion.isopen, dataIndex:'isopen', hidden:true, editor: { xtype:'hidden',value:'y' }},
	    	{header:i18n.SysFuncion.opendate, dataIndex:'opendate', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.url, dataIndex:'url', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.appdesc, dataIndex:'appdesc', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.maintenance, dataIndex:'maintenance', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.manarole, dataIndex:'manarole', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.remark, dataIndex:'demo', hidden:false, editor: {xtype:'textarea',maxLength:400}},
	    	{header:i18n.SysFuncion.iniwp, dataIndex:'iniwp', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.intaskcenter, dataIndex:'intaskcenter', hidden:true, editor: { xtype:'hidden' }},
	    	{header:'IP', dataIndex:'ipaddr', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.ipport, dataIndex:'ipport', hidden:true, editor: { xtype:'hidden' }}
	    ],
	    editOrder:['appid','appcode','appname','demo'],
	    searchOrder:['appcode','appname'],
	    searchFn : function(searchParam) {
			Sys.App.searchParams = searchParam;
			this.store.load();
		}
	});
	
	Sys.App.grid.store.setDefaultSort('appcode', 'asc');

	Sys.App.grid.store.on('beforeload', function() {
		var searchParam = Sys.App.searchParams;
		var whereList = [];
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});
		}
	    var params = {
	    	start : 0,
			limit : Sys.App.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.AcApplication",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	
	/** 系统功能组 */
	Ext.namespace('Sys.Funcgroup');
	Sys.Funcgroup.searchParams = {}; 
	
	/* 功能组树 */
	Sys.Funcgroup.tree = new Ext.tree.TreePanel({
		tbar : new Ext.Toolbar(),
		plugins : ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/sysFuncGroup!tree.action"
		}),
		root: new Ext.tree.TreeLoader({
			text : i18n.SysFuncion.AppFuncGroup,
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
    	collapsed : false,
    	listeners: {
    		render : function() {
    		},
    		click : function (node, e) {
    			if(node.id == 'ROOT_0'){
    				Ext.getCmp('_tabPanel').hideTabStripItem(0);   //隐藏 功能标签
    				Ext.getCmp('_tabPanel').setActiveTab(1);
    				Sys.Funcgroup.findClkTreeNode(node); //加载【功能组】列表数据
    			} else {
    				Ext.getCmp('_tabPanel').unhideTabStripItem(0); //显示 功能标签
    				var title = Ext.getCmp('_tabPanel').getActiveTab().title;
    				if(title == i18n.SysFuncion.Sub_funcGroupList) Sys.Funcgroup.findClkTreeNode(node);
    				else if(title == i18n.SysFuncion.functionList) Sys.Func.findClkTreeNode(node);
//    				Ext.getCmp('_tabPanel').setActiveTab(0);
//    				Sys.Func.findClkTreeNode(node);      //加载【功能】列表数据
    			}
    		}
    	}
	});
	
	Sys.Funcgroup.tree.on('beforeload', function(node){
		var appid = Sys.App.grid.selModel.getSelections()[0].get("appid");
		var funcgroupid = null;
		if(typeof(node.attributes.funcgroupid) == 'undefined') funcgroupid = null;
		else funcgroupid = node.attributes.funcgroupid;
		if(node!=null) Sys.Funcgroup.tree.loader.dataUrl = ctx + '/sysFuncGroup!tree.action?&appid='+appid+'&funcgroupid='+funcgroupid;
	});
	
	Sys.Funcgroup.findClkTreeNode = function (node) {
		Sys.Funcgroup.searchParams = {};
		var clknode;
		if(node){
			clknode = node;
		} else {
			clknode = Sys.Funcgroup.tree.getSelectionModel().getSelectedNode();
		}
		if(typeof(clknode) != 'undefined'&&clknode != null && clknode != ''){
			Sys.Funcgroup.searchParams.parentgroup = clknode.attributes.funcgroupid;
		} else {
			Sys.Funcgroup.searchParams.parentgroup = null;
		}
		Sys.Funcgroup.searchParams.appid = Sys.App.grid.selModel.getSelections()[0].get("appid");
		Sys.Funcgroup.grid.store.load();
	};
	
	Sys.Funcgroup.isAdd = false; //save or update sign
	
	/* 功能组列表 */
	Sys.Funcgroup.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/sysFuncGroup!pageQuery.action',           //装载列表数据的请求URL
		saveURL: ctx + '/sysFuncGroup!saveOrUpdate.action',        //保存数据的请求URL
	    deleteURL: ctx + '/sysFuncGroup!delete.action',            //删除数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    storeAutoLoad: false,
	    singleSelect: false,
	    loadMask: {msg: null},
	    storeId:'funcgroupid',
	    tbar:['search','add','delete'],
	    fields: [
	    	{header:i18n.SysFuncion.funcgroupid, dataIndex:'funcgroupid', hidden:false, editor: {id:'_funcgroupid_1',vtype:'positiveInt',maxLength:8, allowBlank:false}},
	    	{header:i18n.SysFuncion.appid, dataIndex:'appid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.funcgroupname, dataIndex:'funcgroupname', hidden:false, editor: {maxLength:30, allowBlank:false}},
	    	{header:i18n.SysFuncion.parentgroup, dataIndex:'parentgroup', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.grouplevel, dataIndex:'grouplevel', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.funcgroupseq, dataIndex:'funcgroupseq', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.isleaf, dataIndex:'isleaf', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.subcount, dataIndex:'subcount', hidden:true, editor: { xtype:'hidden' }}
	    ],
	    editOrder:['funcgroupid','funcgroupname'],
	    searchOrder:['funcgroupname'],
	    //新增时， 解锁功能组编号的写操作状态
	    beforeAddButtonFn: function(){
	    	Ext.getCmp('_funcgroupid_1').setDisabled(false);
	    	Sys.Funcgroup.isAdd = true;
	    	return true;
	    },
	    //编辑时， 锁定功能组编号的写操作状态
	    afterShowEditWin: function(record, rowIndex){
	    	Ext.getCmp('_funcgroupid_1').setDisabled(true);
	    	Sys.Funcgroup.isAdd = false;
	    },
	    //保存前，解锁功能组编号的写操作状态
	    beforeGetFormData: function(){
	    	Ext.getCmp('_funcgroupid_1').setDisabled(false);
	    },
	    //保存后， 锁定功能组编号的写操作状态
	    afterGetFormData: function(){
	    	Ext.getCmp('_funcgroupid_1').setDisabled(true);
	    },
	    //保存前，构造默认的数据内容
	    beforeSaveFn: function(data){ 	
	    	var clknode = Sys.Funcgroup.tree.getSelectionModel().getSelectedNode(); //当前选中的功能组树节点
	    	//保存前，设置默认值
	    	data.appid = Sys.Funcgroup.searchParams.appid;
	    	data.parentgroup = Sys.Funcgroup.searchParams.parentgroup;
	    	if(data.isleaf == null || data.isleaf == '') data.isleaf = 'y';
	    	if(data.subcount == null || data.subcount == '') data.subcount = 0;
	    	if(typeof(clknode) != 'undefined' && clknode != null){ 
	    		//如果当前选中了某一个功能组树节点
	    		var grouplevel = clknode.attributes.grouplevel; //从树节点属性中获取level
	    		var funcgroupseq = clknode.attributes.funcgroupseq; //从树节点属性中获取funcgroupseq
	    		
	    		if(grouplevel != null && grouplevel != '')  data.grouplevel = parseInt(clknode.attributes.grouplevel)+1; //当前层级 = 上级 + 1
	    			else data.grouplevel = 1; //初始从1开始
	    		if(funcgroupseq != null && funcgroupseq != '')  data.funcgroupseq = funcgroupseq + data.funcgroupid + '.'; //当前seq = 上级seq + 当前功能组编号 + '.'
	    			else data.funcgroupseq = '.'+data.funcgroupid+'.'; //seq为当前功能组编号前后加'.'
	    	} else {  
	    		//如果当前未选中任何一个功能组树节点
	    		data.grouplevel = 1; //初始从1开始
	    		data.funcgroupseq = '.'+data.funcgroupid+'.'; //seq为当前功能组编号前后加'.'
	    	}
	    	return true; 
	    },
	    saveFn: function(){
	        //表单验证是否通过
	        var form = Sys.Funcgroup.grid.saveForm.getForm(); 
	        if (!form.isValid()) return;
	        
	        //获取表单数据前触发函数
	        Sys.Funcgroup.grid.beforeGetFormData();
	        var data = form.getValues();
	        //获取表单数据后触发函数
	        Sys.Funcgroup.grid.afterGetFormData();
	        
	        //调用保存前触发函数，如果返回fasle将不保存记录
	        if(!Sys.Funcgroup.grid.beforeSaveFn(data)) return;
	        
	        if(self.loadMask)   self.loadMask.show();
	        var cfg = {
	            scope: Sys.Funcgroup.grid, url: Sys.Funcgroup.grid.saveURL, jsonData: data,
	            params:{'isAdd':Sys.Funcgroup.isAdd},
	            success: function(response, options){
	                if(self.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null) {
	                    this.afterSaveSuccessFn(result, response, options);
	                } else {
	                    this.afterSaveFailFn(result, response, options);
	                }
	            }
	        };
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    },
	    searchFn : function(searchParam) {
	    	var appid = Sys.Funcgroup.searchParams.appid;   //取出appid
	    	var parentgroup = Sys.Funcgroup.searchParams.parentgroup; //取出parentgroup
			Sys.Funcgroup.searchParams = searchParam; //查询条件覆盖
			Sys.Funcgroup.searchParams.appid = appid;   //重新设置appid作为查询条件
			Sys.Funcgroup.searchParams.parentgroup = parentgroup; //重新设置parentgroup作为查询条件
			this.store.load();
		},
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        Sys.Funcgroup.tree.root.reload();
			Sys.Funcgroup.tree.getRootNode().expand();
	    },
	    afterSaveFailFn: function(result, response, options){
	    	Sys.Funcgroup.grid.store.reload();
	        alertFail(result.errMsg);
	        Ext.getCmp('_funcgroupid_1').setDisabled(false);
	    },
	    afterDeleteFn: function(){
    		Sys.Funcgroup.tree.root.reload();
			Sys.Funcgroup.tree.getRootNode().expand();
    	}
	});
	
	Sys.Funcgroup.grid.store.setDefaultSort('funcgroupid', 'asc');

	Sys.Funcgroup.grid.store.on('beforeload', function() {
		var searchParam = Sys.Funcgroup.searchParams;
		var whereList = [];
		for (prop in searchParam) {
			switch (prop) {
				case 'parentgroup':
				var paramVal = searchParam[prop];
				if(typeof(paramVal)=='undefined' || paramVal == null || (paramVal == '' && paramVal != 0)){
					whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.IS_NULL});
				} else {
					whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.EQ, stringLike:false});
				}
				break;
				
				case 'appid':
					whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.EQ, stringLike:false});
				break;
				
				default :
				whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});
			}
		}
	    var params = {
	    	start : 0,
			limit : Sys.Funcgroup.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.AcFuncgroup",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	/** 系统功能 */
	Ext.namespace('Sys.Func');
	Sys.Func.searchParams = {};
	Sys.Func.isAdd = true;
	
	Sys.Func.findClkTreeNode = function (node) {
		Sys.Func.searchParams = {};
		var clknode;
		if(node){
			clknode = node;
		} else {
			clknode = Sys.Funcgroup.tree.getSelectionModel().getSelectedNode();
		}
		if(typeof(clknode) != 'undefined'&&clknode != null && clknode != ''){
			Sys.Func.searchParams.funcgroupid = clknode.attributes.funcgroupid;
		}
		Sys.Func.grid.store.load();
	}
	
	Sys.Func.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/sysFunction!pageQuery.action',                 //装载列表数据的请求URL
		saveURL: ctx + '/sysFunction!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/sysFunction!delete.action',            //删除数据的请求URL
	    saveFormColNum:1,	searchFormColNum:1,
	    fieldWidth: 250,
	    storeAutoLoad: false,
	    singleSelect: false,
	    loadMask: {msg: null},
	    storeId:'funccode',
	    tbar:['search','add','delete'],
	    fields: [
	    	{header:i18n.SysFuncion.funccode, dataIndex:'funccode', hidden:false, editor: {id:'_funccode_0', maxLength:20, allowBlank:false }},//xtype:'hidden' }},
	    	{header:i18n.SysFuncion.funcgroupid, dataIndex:'funcgroupid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.funcname, dataIndex:'funcname', hidden:false, editor: { maxLength:20, allowBlank:false }},
	    	{header:i18n.SysFuncion.funcdesc, dataIndex:'funcdesc', hidden:true, editor: { xtype:'textarea',maxLength:400 }},
	    	{header:i18n.SysFuncion.funcaction, dataIndex:'funcaction', hidden:false, editor: { xtype:'textarea',maxLength:200 }},
	    	{header:i18n.SysFuncion.parainfo, dataIndex:'parainfo', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.ischeck, dataIndex:'ischeck', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.SysFuncion.functype, dataIndex:'functype', hidden:false, editor: { 
	    		xtype:'combo',
			    triggerAction: 'all',
			    mode: 'local',
			    store: new Ext.data.ArrayStore({
			        fields: [ 'id', 'text' ],
			        data: [
			        	[FUNC_TYPE_PAGE, FUNC_TYPE_PAGE], 			// '前端功能'
			        	[FUNC_TYPE_REPORT, FUNC_TYPE_REPORT],		// '报表功能'
			        	[FUNC_TYPE_SERVICE, FUNC_TYPE_SERVICE]		// '后台服务'
		        	]
			    }),
			    valueField: 'text',
			    displayField: 'text'
    		}},
	    	{header:i18n.SysFuncion.ismenu, dataIndex:'ismenu', hidden:false, 
	    	  editor: {
	    	  	allowBlank:false,
	    		id : '_ismenu_1', 
	    		xtype : 'combo', 
	    		dataColumn : "ismenu", 
	    		hiddenName : "ismenu",  
	    		mode : 'local' ,
	    		valueField : "value", 
	    		displayField : "text", 
	    		triggerAction : "all",   		 	
            	editable : false, 
            	forceSelection : true, 
            	store : [["y",i18n.SysFuncion.Y],["n",i18n.SysFuncion.N]]
              },renderer:function(v){
	    			if(v == 'y'){return i18n.SysFuncion.Y;}
	    			else if(v == 'n'){return i18n.SysFuncion.N;}
	    			else {return i18n.SysFuncion.N;}
				}
              }
	    ],
	    editOrder:['funccode','funcname','ismenu','functype','funcaction','funcdesc'],
	    searchOrder:['funccode','funcname'],
	    afterShowSaveWin: function(){
	    	Sys.Func.isAdd = true;
	    	Ext.getCmp('_funccode_0').setDisabled(false);
	    },
	    afterShowEditWin: function(record, rowIndex){
	    	Ext.getCmp('_funccode_0').setDisabled(true);
	    	Sys.Func.isAdd = false;
	    },
	     beforeGetFormData: function(){
	    	Ext.getCmp('_funccode_0').setDisabled(false);
	    },
	    //保存后， 锁定功能编号的写操作状态
	    afterGetFormData: function(){
	    	Ext.getCmp('_funccode_0').setDisabled(true);
	    },
	    //保存前，构造默认的数据内容
	    beforeSaveFn: function(data){ 
	    	var clknode = Sys.Funcgroup.tree.getSelectionModel().getSelectedNode(); //当前选中的功能组树节点
	    	//保存前，设置默认值
//	    	data.functype = 0;
	    	if(typeof(clknode) != 'undefined' && clknode != null){ 
	    		//如果当前选中了某一个功能组树节点
	    		var funcgroupid = clknode.attributes.funcgroupid; //从树节点属性中获取funcgroupid
	    		if(funcgroupid != null && (funcgroupid != ''||funcgroupid==0))  data.funcgroupid = funcgroupid; //设置当前功能所属的功能组id
	    			else return false; //没有获取到当前功能所属的功能组id， 不能进行保存操作
	    	} else {  
	    		return false; //没有获取到当前功能所属的功能组id， 不能进行保存操作
	    	}
	    	return true; 
	    },
	    searchFn : function(searchParam) {
	    	var funcgroupid = Sys.Func.searchParams.funcgroupid; //取出funcgroupid
			Sys.Func.searchParams = searchParam; //查询条件覆盖
			Sys.Func.searchParams.funcgroupid = funcgroupid; //重新设置funcgroupid作为查询条件
			this.store.load();
		},
		saveFn: function(){
	        //表单验证是否通过
	        var form = Sys.Func.grid.saveForm.getForm(); 
	        if (!form.isValid()) return;
	        
	        //获取表单数据前触发函数
	        Sys.Func.grid.beforeGetFormData();
	        var data = form.getValues();
	        //获取表单数据后触发函数
	        Sys.Func.grid.afterGetFormData();
	        
	        //调用保存前触发函数，如果返回fasle将不保存记录
	        if(!Sys.Func.grid.beforeSaveFn(data)) return;
	        
	        if(self.loadMask)   self.loadMask.show();
	        var cfg = {
	            scope: Sys.Func.grid, url: Sys.Func.grid.saveURL, jsonData: data,
	            params : {'isAdd': Sys.Func.isAdd},
	            success: function(response, options){
	                if(self.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null) {
	                    Sys.Func.grid.afterSaveSuccessFn(result, response, options);
	                } else {
	                    Sys.Func.grid.afterSaveFailFn(result, response, options);
	                }
	            }
	        };
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    },
	    afterSaveFailFn: function(result, response, options){
    		Sys.Func.grid.store.reload();
        	alertFail(result.errMsg);
        	Ext.getCmp('_funccode_0').setDisabled(false);
        	Ext.getCmp('_funccode_0').selectText();
    	}
	});
	
	Sys.Func.grid.store.setDefaultSort('funccode', 'asc');

	Sys.Func.grid.store.on('beforeload', function() {
		var searchParam = Sys.Func.searchParams;
		var whereList = [];
		for (prop in searchParam) {
			switch (prop) {
				case 'funcgroupid':
				whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.EQ, stringLike:false});
				break;
				
				default :
				whereList.push({propName : prop, propValue : searchParam[prop], compare:Condition.LIKE});
			}
		}
	    var params = {
	    	start : 0,
			limit : Sys.Func.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.AcFunction",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	
	/***************************************************框架***********************************************************/
	
	/** 选项卡页面 */
	var funcationTab = new Ext.Panel({
		layout : 'fit', border : false, activeItem : 0, baseCls: "x-plain",
		items : [{
			layout : 'fit', border : true, height : 450, 
			items : {
				id:'_tabPanel', xtype : "tabpanel", activeTab : 0, enableTabScroll : true, border : false,
				items : [{
					title : i18n.SysFuncion.functionList, order : false, layout : "fit", items : [Sys.Func.grid],
					listeners : {
				 		"activate" : function() {
				 			Sys.Func.findClkTreeNode();
					 	}
					}
				}, {
					title : i18n.SysFuncion.Sub_funcGroupList, border : false, layout : "fit", items : [Sys.Funcgroup.grid],
					listeners : {
						"activate" : function() {
							Sys.Funcgroup.findClkTreeNode();
					 	}
					}
				}]
			}
		}]
	});
	
	/** 权限分配窗口 */
	var functionWin = new Ext.Window({
		title: i18n.SysFuncion.funcMaintain, maximizable: false, width: 750, height: 500, layout: "border", 
		plain: true, closeAction: "hide", buttonAlign: 'center', border:false,
		items : [{
			title : i18n.SysFuncion.AppFuncGroup,
			region : 'west',
			xtype : 'panel',
			width : 200,
			border : true,
			layout : 'fit',
			items : [Sys.Funcgroup.tree]
		},{
			id: 'censusPanel',
			region:'center',
			layout:'fit',
			xtype:'panel',
			border:true,
			autoScroll:true,
			items:[funcationTab]
		}],
		listeners : {
			beforehide : function (){
				if(Sys.App.grid.loadMask)   Sys.App.grid.loadMask.hide(); //隐藏遮罩
			},
			beforeshow : function (){
				if(Sys.App.grid.loadMask)   Sys.App.grid.loadMask.show(); //显示遮罩
				Ext.getCmp('_tabPanel').hideTabStripItem(0);   //隐藏 功能标签
    			Ext.getCmp('_tabPanel').setActiveTab(1);
    			Sys.Funcgroup.findClkTreeNode(); //加载【功能组】列表数据
			}
		}
	
	});
	
	/** 主框架 */
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items: Sys.App.grid });
});