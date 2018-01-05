/**
 * 职务信息表 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	/*********************************************************/
	/*                          所有职务                             */
	/*********************************************************/
	Ext.namespace('DutyList');                       //职务列表命名空间
	DutyList.searchParams = {}; 
	DutyList.dutyTypeStore = []; //职务类别
	
	//机构信息列表
	DutyList.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workDuty!overPageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/workDuty!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/workDuty!delete.action',            //删除数据的请求URL
	    saveFormColNum:2,	searchFormColNum:2,
	    storeAutoLoad: false,
	    storeId:'dutyid',
	    tbar:['search','add','delete'],
	    fields: [
	    	//列表中隐藏
	    	{header:i18n.WorkDuty_List.dutyid, dataIndex:'dutyid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:'父职务ID', dataIndex:'parentduty', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.WorkDuty_List.parentdutyname, dataIndex:'parentdutyname', hidden:true, editor: { disabled:true }},
	    	{header:i18n.WorkDuty_List.dutylevel, dataIndex:'dutylevel', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.WorkDuty_List.dutyseq, dataIndex:'dutyseq', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.WorkDuty_List.isleaf, dataIndex:'isleaf', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.WorkDuty_List.subcount, dataIndex:'subcount', hidden:true, editor: { xtype:'hidden' }},
	    	//列表中显示
	    	{header:i18n.WorkDuty_List.dutycode, dataIndex:'dutycode', hidden:false, editor: {vtype:'alphanum2', maxLength:20,allowBlank:false }},
	    	{header:i18n.WorkDuty_List.dutyname, dataIndex:'dutyname', hidden:false, editor: { maxLength:15,allowBlank:false, vtype:'validChar' }},
	    	{header:i18n.WorkDuty_List.dutytype, dataIndex:'dutytype', hidden:false, editor: { 
				id:'_dutytype_list0',xtype: 'EosDictEntry_combo', hiddenName: 'dutytype', displayField: 'dictname', valueField: 'dictid',status:'1',dicttypeid:'ABF_DUTYTYPE',allowBlank:false}, 
				renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_DUTYTYPE',v);
				}
			},
	    	{header:i18n.WorkDuty_List.remark, dataIndex:'remark', hidden:false, editor: {xtype:'textarea', maxLength:200,allowBlank:true}}
	    ],
	    editOrder:['parentdutyname','dutytype','dutycode','dutyname','remark'],
	    searchOrder:['dutycode','dutyname'],
	    //设置新增默认值
	    afterShowSaveWin: function(record, rowIndex){
	    	//设置职务类别默认值
	    	for(var i = 0; i<DutyList.dutyTypeStore.length; i++){
	    		if(DutyList.dutyTypeStore[i].dictid == DutyTab.DutyTypeNodeId){
	    			DutyList.grid.saveForm.getForm().findField('_dutytype_list0').setDisplayValue(DutyList.dutyTypeStore[i].dictid,DutyList.dutyTypeStore[i].dictname);
	    		}
	    	}
	    	
	    },
	     //回显控件信息
	    afterShowEditWin: function(record, rowIndex){
	    	//回显职务类别
	    	for(var i=0; i<DutyList.dutyTypeStore.length; i++){
	    		if(DutyList.dutyTypeStore[i].dictid == record.data.dutytype){
	    			DutyList.grid.saveForm.getForm().findField('_dutytype_list0').setDisplayValue(DutyList.dutyTypeStore[i].dictid, DutyList.dutyTypeStore[i].dictname);
	    			break;
	    		}
	    	}
	    },
	    beforeSaveFn: function(data){
	    	delete parentdutyname;
	    	return true; 
	    },
	    searchFn : function(searchParam) {
			DutyList.searchParams = searchParam;
			this.store.load();
		},
		afterSaveSuccessFn: function(result, response, options){
        	this.store.reload();
        	alertSuccess();
        	dutytree.tree.root.reload();
		    dutytree.tree.getRootNode().expand();
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
    		dutytree.tree.root.reload();
		    dutytree.tree.getRootNode().expand();
    	}
	});
	
	//记录数据字典项-职务类别
	Ext.getCmp("_dutytype_list0").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			DutyList.dutyTypeStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	DutyList.grid.store.setDefaultSort('duty.dutycode', 'asc');

	DutyList.grid.store.on('beforeload', function() {
		var searchParam = DutyList.searchParams;
		var whereList = [];
		//当点击职务树中职务类别或职务节点时（即非根节点）
		if(DutyTab.currentNodeType!=null&&DutyTab.currentNodeType!=''){
			if(DutyTab.DutyTypeNodeId!=null&&DutyTab.DutyTypeNodeId!=''){
				whereList.push({propName : 'dutytype', propValue : DutyTab.DutyTypeNodeId, compare:1});
			}
			if(DutyTab.DutyNodeId!=null&&DutyTab.DutyNodeId!=''){
				whereList.push({propName : 'parentduty', propValue : DutyTab.DutyNodeId, compare:1});
			}
		}
		
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:8});
		}
	    var params = {
	    	start : 0,
			limit : DutyList.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.OmDuty",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	/*********************************************************/
	/*                          下级职务                             */
	/*********************************************************/
	Ext.namespace('LowerDutyList');                       //职务列表命名空间
	LowerDutyList.searchParams = {}; 
	
	LowerDutyList.dutyTypeStore = []; 	//职务类别
	
	//机构信息列表
	LowerDutyList.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workDuty!overPageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/workDuty!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/workDuty!delete.action',            //删除数据的请求URL
	    saveFormColNum:2,	searchFormColNum:2,
	    storeAutoLoad: false,
	    storeId:'dutyid',
	    tbar:['search','add','delete'],
	    fields: [
	    	//列表中隐藏
	    	{header:i18n.WorkDuty_List.dutyid, dataIndex:'dutyid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.WorkDuty_List.parentduty, dataIndex:'parentduty', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.WorkDuty_List.parentdutyname, dataIndex:'parentdutyname', hidden:true, editor: { disabled:true }},
	    	{header:i18n.WorkDuty_List.dutylevel, dataIndex:'dutylevel', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.WorkDuty_List.dutyseq, dataIndex:'dutyseq', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.WorkDuty_List.isleaf, dataIndex:'isleaf', hidden:true, editor: { xtype:'hidden' }},
	    	{header:i18n.WorkDuty_List.subcount, dataIndex:'subcount', hidden:true, editor: { xtype:'hidden' }},
	    	//列表中显示
	    	{header:i18n.WorkDuty_List.dutycode, dataIndex:'dutycode', hidden:false, editor: {vtype:'positiveInt', maxLength:6,allowBlank:false }},
	    	{header:i18n.WorkDuty_List.dutyname, dataIndex:'dutyname', hidden:false, editor: { maxLength:15,allowBlank:false, vtype:'validChar' }},
	    	{header:i18n.WorkDuty_List.dutytype, dataIndex:'dutytype', hidden:false, editor: { 
				id:'_dutytype_list1',xtype: 'EosDictEntry_combo', hiddenName: 'dutytype', displayField: 'dictname', valueField: 'dictid',status:'1',dicttypeid:'ABF_DUTYTYPE',allowBlank:false}, 
				renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_DUTYTYPE',v);
				}
			},
	    	{header:i18n.WorkDuty_List.remark, dataIndex:'remark', hidden:false, editor: {xtype:'textarea', maxLength:256,allowBlank:true}}
	    ],
	    editOrder:['parentdutyname','dutytype','dutycode','dutyname','remark'],
	    searchOrder:['dutycode','dutyname'],
	    //设置新增默认值
	    afterShowSaveWin: function(record, rowIndex){
	    	//设置职务类别默认值
	    	for(var i = 0; i<LowerDutyList.dutyTypeStore.length; i++){
	    		if(LowerDutyList.dutyTypeStore[i].dictid == DutyTab.DutyTypeNodeId){
	    			LowerDutyList.grid.saveForm.getForm().findField('_dutytype_list1').setDisplayValue(LowerDutyList.dutyTypeStore[i].dictid,LowerDutyList.dutyTypeStore[i].dictname);
	    		}
	    	}
		},
		 //回显控件信息
	    afterShowEditWin: function(record, rowIndex){
	    	//回显职务类别
	    	for(var i=0; i<LowerDutyList.dutyTypeStore.length; i++){
	    		if(LowerDutyList.dutyTypeStore[i].dictid == record.data.dutytype){
	    			LowerDutyList.grid.saveForm.getForm().findField('_dutytype_list1').setDisplayValue(LowerDutyList.dutyTypeStore[i].dictid, LowerDutyList.dutyTypeStore[i].dictname);
	    			break;
	    		}
	    	}
	    },
	    beforeSaveFn: function(data){
	    	data.parentduty = DutyTab.DutyNodeId; //上级职务ID
	    	delete parentdutyname;
	    	return true; 
	    },
	    searchFn : function(searchParam) {
			LowerDutyList.searchParams = searchParam;
			this.store.load();
		},
		afterSaveSuccessFn: function(result, response, options){
        	this.store.reload();
        	alertSuccess();
			
        	dutytree.tree.root.reload();
        	dutytree.tree.getRootNode().expand(); 
//        	var node; //当前点击树的节点dutytree.tree.getSelectionModel().getSelectedNode(); //当前点中的节点，新增节点在其之下
//        	var pnode;//当前点击树节点的上级节点 = dutytree.tree.getNodeById(node.attributes.pid);
//        	var targetNode = null; 
//        	
//        	node = dutytree.tree.getSelectionModel().getSelectedNode();
//        	//上层节点不为Null
//        	if(node.attributes.pid!=null&&node.attributes.pid!='null') {
//        		pnode = dutytree.tree.getNodeById(node.attributes.pid);
//        	} else {
//        		pnode = dutytree.tree.getNodeById(DutyTab.DutyTypeNodeId);
//        	}
//        	
//        	if(node.leaf) targetNode = pnode;
//        	else targetNode = node;
//        	
//        	if(node == null||node.id == 'ROOT_0'){
//        		dutytree.tree.root.reload();
//		        dutytree.tree.getRootNode().expand();  
//        	} else {
//        		var path = node.getPath('id');  //从根节点开始，一直到当前点中节点："/ROOT_0/company/3/4/5/6"
//        		dutytree.tree.getLoader().load(targetNode,//dutytree.tree.getRootNode(),
//        		function(treeNode){
//        			dutytree.tree.expandPath(path,'id',function(bSucess,oLastNode){
//        				dutytree.tree.getSelectionModel().select(oLastNode);
//        			});
//        		},this);
//        	}
    	},
    	afterDeleteFn: function(){
    		dutytree.tree.root.reload();
		    dutytree.tree.getRootNode().expand();
    	}
	});
	
	//记录数据字典项-职务类别
	Ext.getCmp("_dutytype_list1").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			LowerDutyList.dutyTypeStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	LowerDutyList.grid.store.setDefaultSort('duty.dutycode', 'asc');

	LowerDutyList.grid.store.on('beforeload', function() {
		var searchParam = LowerDutyList.searchParams;
		var whereList = [];
		//当点击职务树中职务类别或职务节点时（即非根节点）
		if(DutyTab.currentNodeType!=null&&DutyTab.currentNodeType!=''){
			if(DutyTab.DutyTypeNodeId!=null&&DutyTab.DutyTypeNodeId!=''){
				whereList.push({propName : 'dutytype', propValue : DutyTab.DutyTypeNodeId, compare:1});
			}
			if(DutyTab.DutyNodeId!=null&&DutyTab.DutyNodeId!=''){
				whereList.push({propName : 'parentduty', propValue : DutyTab.DutyNodeId, compare:1});
			}
		}
		
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:8});
		}
	    var params = {
	    	start : 0,
			limit : LowerDutyList.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.OmDuty",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	/*********************************************************/
	/*                    职务下人员                             */
	/*********************************************************/
	Ext.namespace('emplist');  //人员命名空间
	emplist.searchParams = {}; 
	
	//机构信息列表
	emplist.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/employee!findEmpListByDuty.action',                 //装载列表数据的请求URL
	    saveFormColNum:2,	searchFormColNum:2,
	    storeAutoLoad: false,
	    storeId:'empid',
	    tbar:['search'],
	    fields: [
	    	{header:i18n.WorkDuty_List.empid, 	dataIndex:'empid', 		hidden:true, 	editor: {xtype:'hidden'}},
	    	{header:i18n.WorkDuty_List.userid,	dataIndex:'userid',		hidden:false,	editor:	{xtype:'hidden'},	searcher:{}},
	    	{header:i18n.WorkDuty_List.empname, 	dataIndex:'empname', 	hidden:false, 	editor: {xtype:'hidden'},	searcher:{}},
	    	{header:i18n.WorkDuty_List.empstatus, 	dataIndex:'empstatus', 	hidden:false, 	editor: {xtype:'hidden'},
	    		renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_EMPSTATUS',v);
				}
	    	},
	    	{header:i18n.WorkDuty_List.orgname, 	dataIndex:'orgname', 	hidden:false, 	editor: {xtype:'hidden'}}
//	    	,{header:'所属工作组', dataIndex:'groupname', 	hidden:false, 	editor: {xtype:'hidden'}}
	    ],
	    editOrder:[],
	    searchOrder:['userid','empname'],
	    searchFn : function(searchParam) {
			emplist.searchParams = searchParam;
			this.store.load();
		},
    	afterDeleteFn: function(){
    		dutytree.tree.root.reload();
		    dutytree.tree.getRootNode().expand();
    	}
	});
	
	emplist.grid.store.setDefaultSort('emp.empname', 'asc');
	//移除侦听器
	emplist.grid.un('rowdblclick', emplist.grid.toEditFn, emplist.grid); //初始默认为显示已入场的,此时对双击行编辑事件禁用

	emplist.grid.store.on('beforeload', function() {
		var searchParam = emplist.searchParams;
		var whereList = [];
		if(DutyTab.DutyNodeId != null && DutyTab.DutyNodeId != ''){
			whereList.push({propName : 'dutyid', propValue : DutyTab.DutyNodeId, compare:1}); //设置查询条件：查询范围为当前职务dutyid
		}
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop],compare:8});
		}
	    var params = {
	    	start : 0,
			limit : emplist.grid.pagingToolbar.pageSize,
			nodetype : DutyTab.DutyNodeId, 
			queryClassName : "com.yunda.frame.yhgl.entity.OmEmployee",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
});