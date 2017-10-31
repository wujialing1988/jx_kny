/**
 * 機構信息表 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	/*********************************************************/
	/*                     下級工作組                             */
	/*********************************************************/
	Ext.namespace('WGList');                //工作組列表命名空間
	WGList.searchParams = {}; 
	WGList.grouptypeStore = []; //工作组类型
	WGList.groupstatusStore = []; //工作组状态
	
	//机构信息列表
	WGList.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workGroup!findGroupList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/workGroup!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/workGroup!delete.action',            //删除数据的请求URL
	    saveFormColNum:2,	searchFormColNum:1,
	    storeAutoLoad: false,
	    storeId:'groupid',
	    tbar:['search','add','delete'],
	    fields: [
	    	//列表中隐藏
	    	{header:'工作组ID', dataIndex:'groupid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:'父工作组',dataIndex:'parentgroupid',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'隶属机构ID',dataIndex:'orgid',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'工作组层次',dataIndex:'grouplevel',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'工作组路径序列', dataIndex:'groupseq', hidden:true, editor: {maxLength:255,xtype:'hidden'}},
	    	{header:'负责人(虚拟字段)', dataIndex:'empname', hidden:true, editor: {id:'_empname_list0',xtype:'hidden'}},
	    	{header:'创建时间', dataIndex:'createtime', hidden:true, editor: {xtype:'hidden'}},
	    	{header:'最近更新时间', dataIndex:'lastupdate', hidden:true, editor: {xtype:'hidden'}},
	    	{header:'最近更新人员', dataIndex:'updator', hidden:true, editor: {xtype:'hidden'}},
	    	{header:'是否叶子节点', dataIndex:'isleaf', hidden:true, editor: {xtype:'hidden'}},
	    	{header:'子节点数', dataIndex:'subcount', hidden:true, editor: {xtype:'hidden'}},
	    	//列表中显示
	    	{header:'工作组名称', dataIndex:'groupname', hidden:false, editor: {maxLength:25,allowBlank:false,vtype:'validChar'}},
	    	{header:'负责人', dataIndex:'manager', hidden:true, editor: {id:'_manager_list0',xtype: "OmEmployee_SelectWin",
	    		hiddenName: "manager",displayField:"empname",valueField: "empid",
	    		returnField :[{widgetId: '_empname_list0',propertyName:'empname'}],editable: false }},
	    	{header:'工作组类型', dataIndex:'grouptype', editor: {xtype: 'EosDictEntry_combo', id:'_grouptype_list0',
	    		hiddenName: 'grouptype', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_GROUPTYPE'},
	    		renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_GROUPTYPE',v);
				}},
	    	{header:'工作组状态', dataIndex:'groupstatus', editor: {xtype: 'EosDictEntry_combo',  id:'_groupstatus_list0',
	    		hiddenName: 'groupstatus', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_GROUPSTATUS'},
	    		renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_GROUPSTATUS',v);
				}},
	    	{header:'有效开始日期', dataIndex:'startdate', xtype:'datecolumn',format: 'Y-m-d', editor: {xtype:'my97date', format: 'Y-m-d',initNow:false}},
	    	{header:'有效截止日期', dataIndex:'enddate', xtype:'datecolumn',format: 'Y-m-d', editor: {xtype:'my97date', format: 'Y-m-d',initNow:false}},
	    	{header:'工作组描述', dataIndex:'groupdesc', editor: {maxLength:256,xtype:'textarea'}}
	    ],
	    editOrder:['groupid','parentgroupid','orgid','grouplevel','groupseq','createtime','lastupdate','updator','isleaf','subcount',
	    			'groupname','manager','grouptype','groupstatus','startdate','enddate','groupdesc'],
	    searchOrder:['groupname'],
	     //默认信息
	    afterShowSaveWin: function(){
	    	if(WGList.grouptypeStore!=null && WGList.grouptypeStore.length>0)
	    	WGList.grid.saveForm.getForm().findField('_grouptype_list0').setDisplayValue(WGList.grouptypeStore[0].dictid,WGList.grouptypeStore[0].dictname);//工作组类型
	    	if(WGList.groupstatusStore!=null && WGList.groupstatusStore.length>0)
	    	WGList.grid.saveForm.getForm().findField('_groupstatus_list0').setDisplayValue(WGList.groupstatusStore[0].dictid,WGList.groupstatusStore[0].dictname);//工作组状态
	    },
	    //回显负责人控件
	    afterShowEditWin: function(record, rowIndex){
	    	WGList.grid.saveForm.getForm().findField('_manager_list0').setDisplayValue(record.data.manager,record.data.empname);
	    	
	    	//回显工作组类型
	    	for(var i=0; i<WGList.grouptypeStore.length; i++){
	    		if(WGList.grouptypeStore[i].dictid == record.data.grouptype){
	    			WGList.grid.saveForm.getForm().findField('_grouptype_list0').setDisplayValue(WGList.grouptypeStore[i].dictid,WGList.grouptypeStore[i].dictname);
	    			break;
	    		}
	    	}
	    	//回显工作组状态
	    	for(var i=0; i<WGList.groupstatusStore.length; i++){
	    		if(WGList.groupstatusStore[i].dictid == record.data.groupstatus){
	    			WGList.grid.saveForm.getForm().findField('_groupstatus_list0').setDisplayValue(WGList.groupstatusStore[i].dictid,WGList.groupstatusStore[i].dictname);
	    			break;
	    		}
	    	}
	    },
	    beforeSaveFn: function(data){
	    	delete data.empname;
	    	if(WGTab.GroupNodeId!=null&&WGTab.GroupNodeId!='')
	    		data.parentgroupid = WGTab.GroupNodeId;
	    	else
	    		delete data.parentgroupid;
	    	return true; 
	    },
	    searchFn : function(searchParam) {
			WGList.searchParams = searchParam;
			this.store.load();
		},
		afterSaveSuccessFn: function(result, response, options){
        	this.store.reload();
        	alertSuccess();
        	WGTree.tree.root.reload();
		    WGTree.tree.getRootNode().expand();
//        	var node = dutytree.tree.getSelectionModel().getSelectedNode();
//        	if(node == null||node.id == 'ROOT_0'){
//        		dutytree.tree.root.reload();
//		        dutytree.tree.getRootNode().expand();  
//        	} else {
//        		var path = node.getPath('id');
//        		dutytree.tree.getLoader().load(dutytree.tree.getRootNode(),
//        		function(treeNode){
//        			dutytree.tree.expandPath(path,'id',function(bSucess,oLastNode){
//        				dutytree.tree.getSelectionModel().select(oLastNode);
//        			});
//        		},this);
//        	}
    	},
    	afterDeleteFn: function(){
    		WGTree.tree.root.reload();
		    WGTree.tree.getRootNode().expand();
    	}
	});
	
	//记录数据字典项-工作组类型
	Ext.getCmp("_grouptype_list0").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			WGList.grouptypeStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	//记录数据字典项-工作组状态
	Ext.getCmp("_groupstatus_list0").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			WGList.groupstatusStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	WGList.grid.store.setDefaultSort('createtime', 'desc');

	WGList.grid.store.on('beforeload', function() {
		var searchParam = WGList.searchParams;
		var whereList = [];
		if(WGTab.GroupNodeId==null||WGTab.GroupNodeId==''){
			whereList.push({propName : 'parentgroupid', propValue : '', compare:21});
		} else {
			whereList.push({propName : 'parentgroupid', propValue : WGTab.GroupNodeId, compare:1});
		}
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:8});
		}
	    var params = {
	    	start : 0,
			limit : WGList.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.OmGroup",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	/*********************************************************/
	/*                         岗位                               */
	/*********************************************************/
	Ext.namespace('positionlist');                       //岗位命名空间
	positionlist.searchParams = {}; 
	positionlist.statusStore = [];//岗位状态
	//机构信息列表
	positionlist.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/position!findPositionList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/position!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/position!delete.action',            //删除数据的请求URL
	    saveFormColNum:2,	searchFormColNum:2,
	    storeAutoLoad: false,
	    storeId:'positionid',
	    tbar:['search','add','delete'],
	    fields: [
	    	//列表中隐藏
	    	{header:'岗位编码', dataIndex:'positionid', hidden:true, editor:{xtype:'hidden' }},
	    	{header:'上级岗位', dataIndex:'manaposi',	hidden:true, editor:{xtype:'hidden'}},
	    	{header:'岗位层次',	dataIndex:'posilevel',	hidden:true, editor:{xtype:'hidden'}},
	    	{header:'岗位序列',	dataIndex:'positionseq',	hidden:true, editor:{xtype:'hidden'}},
	    	{header:'创建时间', dataIndex:'createtime', hidden:true, 	editor: {xtype:'hidden'}},
			{header:'最后一次修改时间', dataIndex:'lastupdate', hidden:true, 	editor: {xtype:'hidden'}},
			{header:'最近更新人员', dataIndex:'updator', hidden:true, 	editor: {xtype:'hidden'}},
			{header:'是否是叶子节点', dataIndex:'isleaf', hidden:true, 	editor: {xtype:'hidden'}},
			{header:'子节点数', dataIndex:'subcount', hidden:true, 	editor: {xtype:'hidden'}},
	    	//列表中显示
	    	{header:'岗位代码', dataIndex:'posicode', hidden:false, editor: {allowBlank:false, maxLength:20, vtype:'alphanum2'}},
	    	{header:'岗位名称', dataIndex:'posiname', hidden:false, editor: {allowBlank:false, maxLength:40, vtype:'validChar'}},
	    	{header:'所属机构',	dataIndex:'orgid',	hidden:true, editor:{xtype:'hidden'}},
	    	{header:'所属职务', dataIndex:'dutyid',	hidden:true, editor:{
	    		id:'_dutyid_list0',
	    		xtype: "WorkDuty_comboTree",
	    		hiddenName: "dutyid", disabled:false,
	    		selectNodeModel: "exceptRoot" ,
		    	allowBlank: false
	    	}},
	    	{header:'所属职务', dataIndex:'dutyname', hidden:false, editor: {xtype:'hidden'}},
	    	{header:'岗位类别', dataIndex : 'positype',  hidden:false,  editor:{xtype:'hidden'},
				renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_POSITYPE',v);
				}
			},
			{header:'岗位状态', dataIndex : 'status',  hidden:false, 
				editor: { 
					id:'_status_list4',xtype: 'EosDictEntry_combo', hiddenName: 'status', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_POSISTATUS'
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_POSISTATUS',v);
				}
			},
			{header:'有效开始时间', dataIndex:'startdate', hidden:false, xtype:'datecolumn',	editor: {xtype:'my97date', format: 'Y-m-d',initNow:false}},
			{header:'有效结束时间', dataIndex:'enddate', hidden:false, xtype:'datecolumn', 	editor: {xtype:'my97date', format: 'Y-m-d',initNow:false}}
			
	    ],
	    editOrder:['posicode','posiname','startdate','enddate','dutyid','status'],
	    searchOrder:['posicode','posiname'],
	    //回显职务控件
	    afterShowEditWin: function(record, rowIndex){
	    	positionlist.grid.saveForm.getForm().findField('_dutyid_list0').setDisplayValue(record.data.dutyid,record.data.dutyname);
	    	//回显岗位状态
	    	for(var i=0; i<positionlist.statusStore.length; i++){
	    		if(positionlist.statusStore[i].dictid == record.data.status){
	    			positionlist.grid.saveForm.getForm().findField('_status_list4').setDisplayValue(positionlist.statusStore[i].dictid, positionlist.statusStore[i].dictname);
	    			break;
	    		}
	    	}
	    },
	    afterShowSaveWin: function(){
	    	var obj = positionlist.grid.saveForm.getForm().findField('_dutyid_list0')
	    	if(obj.getValue()!=null&&obj.getValue()!=''){
	    		obj.clearValue();
	    	}
	    	//设置岗位状态默认值
	    	if(positionlist.statusStore!=null && positionlist.statusStore.length>0)
	    	positionlist.grid.saveForm.getForm().findField('_status_list4').setDisplayValue(positionlist.statusStore[0].dictid,positionlist.statusStore[0].dictname);
	    	return true;
	    },
	    beforeSaveFn: function(data){
	    	if(WGTab.currentNodeType!=null&&WGTab.currentNodeType=='gop'){
	    		data.orgid = WGTab.GroupNodeId; //对于工作组下的直接岗位，需要后台存储工作组与岗位的关联信息，利用orgid字段临时传递groupid参数的值
	    	}
	    	data.positype = 'workgroup'; //岗位类别
	    	if(WGTab.PosNodeId!=null&&WGTab.PosNodeId!=''){
	    		data.manaposi = WGTab.PosNodeId; //设置上级岗位
	    	}
	    	delete data.dutyname;
	    	return true; 
	    },
	    searchFn : function(searchParam) {
			positionlist.searchParams = searchParam;
			this.store.load();
		},
		afterSaveSuccessFn: function(result, response, options){
        	this.store.reload();
        	alertSuccess();
        	WGTree.tree.root.reload();
		    WGTree.tree.getRootNode().expand();
//        	var node = dutytree.tree.getSelectionModel().getSelectedNode();
//        	if(node == null||node.id == 'ROOT_0'){
//        		dutytree.tree.root.reload();
//		        dutytree.tree.getRootNode().expand();  
//        	} else {
//        		var path = node.getPath('id');
//        		dutytree.tree.getLoader().load(dutytree.tree.getRootNode(),
//        		function(treeNode){
//        			dutytree.tree.expandPath(path,'id',function(bSucess,oLastNode){
//        				dutytree.tree.getSelectionModel().select(oLastNode);
//        			});
//        		},this);
//        	}
    	},
    	afterDeleteFn: function(){
    		WGTree.tree.root.reload();
		    WGTree.tree.getRootNode().expand();
    	}
    	
	});
	
	//记录数据字典项-岗位状态
	Ext.getCmp("_status_list4").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			positionlist.statusStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	positionlist.grid.store.setDefaultSort('createtime', 'desc');

	positionlist.grid.store.on('beforeload', function() {
		var searchParam = positionlist.searchParams;
		var whereList = [];
		if(WGTab.PosNodeId!=null&&WGTab.PosNodeId!=''){
			whereList.push({propName : 'manaposi', propValue : WGTab.PosNodeId, compare:1});//设置上级岗位条件
		} else {
			whereList.push({propName : 'manaposi', propValue : '', compare:21}); //设置上级岗位条件 is null
			whereList.push({propName : 'groupid', propValue : WGTab.GroupNodeId, compare:1});//设置工作组条件
		}
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop],compare:8});
		}
	    var params = {
	    	start : 0,
			limit : positionlist.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.OmPosition",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	/*********************************************************/
	/*                     人员选择列表                            */
	/*********************************************************/
	Ext.namespace('AddEmpList');
	AddEmpList.searchParams = {};
	
	//机构信息列表
	AddEmpList.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/employee!findAddEmpList2.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/employee!addEmpGroupOrPosition.action',             //保存数据的请求URL
	    saveFormColNum:2,	searchFormColNum:1,
	    height:326,
	    storeAutoLoad: false,
	    storeId:'empid',
	    tbar:['search',{
	    	text:"新增", iconCls:"addIcon", handler: function(){
	    		if(AddEmpList.grid.searchWin)  AddEmpList.grid.searchWin.hide();   
        		if(!$yd.isSelectedRecord(AddEmpList.grid)) return;//未选择记录，直接返回
        		//如果当前执行的是工作组下的人员新增操作
        		if(WGTab.currentNodeType != null && WGTab.currentNodeType != '' && WGTab.currentNodeType == 'gop'){
	        		//执行保存的AJAX请求
	        		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
	            		scope: AddEmpList.grid, url: AddEmpList.grid.saveURL, params: {groupid:WGTab.GroupNodeId, ids: $yd.getSelectedIdx(AddEmpList.grid, AddEmpList.grid.storeId)}
	        		}));
        		}
        		//如果当前执行的是岗位下的人员新增操作
        		else if(WGTab.currentNodeType != null && WGTab.currentNodeType != '' && WGTab.currentNodeType == 'pos'){
        			//执行保存的AJAX请求
	        		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
	            		scope: AddEmpList.grid, url: AddEmpList.grid.saveURL, params: {positionid:WGTab.PosNodeId, ids: $yd.getSelectedIdx(AddEmpList.grid, AddEmpList.grid.storeId)}
	        		}));
        		}
	    	}
	    }],
	    fields: [
	    	{header:'人员ID', dataIndex:'empid', hidden:true, editor: {xtype:'hidden'}},
	    	{header:'操作员登录号', dataIndex:'userid',hidden:false, editor:{xtype:'hidden'},searcher:{}},  //显示
	    	{header:'人员代码', dataIndex:'empcode', hidden:false, editor:{xtype:'hidden'}},    //显示
	    	{header:'人员姓名', dataIndex:'empname', hidden:false, editor:{xtype:'hidden'},searcher:{}},    //显示
			{header:'性别', dataIndex:'gender', hidden:false, editor:{xtype:'hidden'},
				renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_GENDER',v);
				}
			},        //显示
			{header:'人员状态', dataIndex : 'empstatus',  hidden:false, editor:{xtype:'hidden'},
				renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_EMPSTATUS',v);
				}
			} //显示
	    ],
	    editOrder:[],
	    searchOrder:['userid','empname'],
	    searchFn : function(searchParam) {
			AddEmpList.searchParams = searchParam;
			this.store.load();
		}
	});
	
	AddEmpList.grid.store.setDefaultSort('empname', 'desc');
	//移除侦听器
	AddEmpList.grid.un('rowdblclick', AddEmpList.grid.toEditFn, AddEmpList.grid); //初始默认为显示已入场的,此时对双击行编辑事件禁用

	AddEmpList.grid.store.on('beforeload', function() {
		var searchParam = AddEmpList.searchParams;
		var whereList = [];
		//如当前选中树节点类型为工作组
		if(WGTab.currentNodeType != null && WGTab.currentNodeType != '' && WGTab.currentNodeType == 'gop'){
			whereList.push({propName : 'groupid', propValue : WGTab.GroupNodeId, compare:1}); //设置查询条件：查询范围为当前工作组groupid
		}
		else if(WGTab.currentNodeType != null && WGTab.currentNodeType != '' && WGTab.currentNodeType == 'pos'){
			whereList.push({propName : 'positionid', propValue : WGTab.PosNodeId, compare:1}); //设置查询条件：查询范围为当前岗位positionid
		}
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop],compare:8});
		}
	    var params = {
	    	start : 0,
			limit : AddEmpList.grid.pagingToolbar.pageSize,
			nodetype : WGTab.currentNodeType, //当前选中数节点的节点类型（gop-工作组、pos-岗位）
			queryClassName : "com.yunda.frame.yhgl.entity.OmEmployee",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	AddEmpList.win = new Ext.Window({
		title:"新增", 
		width:556, 
		height:356, 
		plain:true, 
		closeAction:"hide", 
		buttonAlign:'center', 
		maximizable:false, 
		items:[
			new Ext.Panel({
//				layout : 'fit',
		    	border : false,
		    	items : [AddEmpList.grid]
			})
		],
		listeners : {
			//关闭人员选择窗口时，刷新父表格内容
			beforehide : function (){
				emplist.grid.store.load();
				WGTree.tree.root.reload();
		    	WGTree.tree.getRootNode().expand();
			}
		}
	});
	
	/*********************************************************/
	/*                    工作组下人员                             */
	/*********************************************************/
	Ext.namespace('emplist');  //人员命名空间
	emplist.searchParams = {}; 
	
	//机构信息列表
	emplist.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/employee!findEmpListByNode.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/employee!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/employee!delEmpGroupOrPosition.action',            //删除数据的请求URL
	    saveFormColNum:2,	searchFormColNum:2,
	    storeAutoLoad: false,
	    storeId:'empid',
	    tbar:['search',{
	    	text:"新增", iconCls:"addIcon", handler: function(){
	    		AddEmpList.win.show();
	    		AddEmpList.grid.store.load();
	    	}
	    },{
	    	text:"删除", iconCls:"deleteIcon", handler: function(){
	    		if(emplist.grid.searchWin)  AddEmpList.grid.searchWin.hide();   
        		if(!$yd.isSelectedRecord(emplist.grid)) return;//未选择记录，直接返回
        		//如果当前执行的是工作组下的人员删除操作
        		if(WGTab.currentNodeType != null && WGTab.currentNodeType != '' && WGTab.currentNodeType == 'gop'){
	        		//执行保存的AJAX请求
	        		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
	            		scope: emplist.grid, url: emplist.grid.deleteURL, params: {groupid:WGTab.GroupNodeId, ids: $yd.getSelectedIdx(emplist.grid, emplist.grid.storeId)}
	        		}));
        		}
        		//如果当前执行的是岗位下的人员删除操作
        		else if(WGTab.currentNodeType != null && WGTab.currentNodeType != '' && WGTab.currentNodeType == 'pos'){
        			//执行保存的AJAX请求
	        		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
	            		scope: emplist.grid, url: emplist.grid.deleteURL, params: {positionid:WGTab.PosNodeId, ids: $yd.getSelectedIdx(emplist.grid, emplist.grid.storeId)}
	        		}));
        		}
	    	}	
	    }],
	    fields: [
	    	{header:'人员ID', 	dataIndex:'empid', 		hidden:true, 	editor: {xtype:'hidden'}},
	    	{header:'登录帐号',	dataIndex:'userid',		hidden:false,	editor:	{xtype:'hidden'},	searcher:{}},
	    	{header:'人员姓名', 	dataIndex:'empname', 	hidden:false, 	editor: {xtype:'hidden'},	searcher:{}},
	    	{header:'人员状态', 	dataIndex:'empstatus', 	hidden:false, 	editor: {xtype:'hidden'},
	    		renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_EMPSTATUS',v);
				}
	    	},
	    	{header:'所属机构', 	dataIndex:'orgname', 	hidden:false, 	editor: {xtype:'hidden'}},
	    	{header:'所属工作组', dataIndex:'groupname', 	hidden:false, 	editor: {xtype:'hidden'}}
	    ],
	    editOrder:[],
	    searchOrder:['userid','empname'],
	    searchFn : function(searchParam) {
			emplist.searchParams = searchParam;
			this.store.load();
		}
	});
	
	emplist.grid.store.setDefaultSort('emp.empname', 'asc');
	//移除侦听器
	emplist.grid.un('rowdblclick', emplist.grid.toEditFn, emplist.grid); //初始默认为显示已入场的,此时对双击行编辑事件禁用

	emplist.grid.store.on('beforeload', function() {
		var searchParam = emplist.searchParams;
		var whereList = [];
		//如当前选中树节点类型为工作组
		if(WGTab.currentNodeType != null && WGTab.currentNodeType != '' && WGTab.currentNodeType == 'gop'){
			whereList.push({propName : 'gop.groupid', propValue : WGTab.GroupNodeId, compare:1}); //设置查询条件：查询范围为当前工作组groupid
		}
		else if(WGTab.currentNodeType != null && WGTab.currentNodeType != '' && WGTab.currentNodeType == 'pos'){
			whereList.push({propName : 'pos.positionid', propValue : WGTab.PosNodeId, compare:1}); //设置查询条件：查询范围为当前岗位positionid
		}
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop],compare:8});
		}
	    var params = {
	    	start : 0,
			limit : emplist.grid.pagingToolbar.pageSize,
			nodetype : WGTab.currentNodeType, //当前选中数节点的节点类型（gop-工作组、pos-岗位）
			queryClassName : "com.yunda.frame.yhgl.entity.OmEmployee",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
});