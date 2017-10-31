Ext.onReady(function() {
	
	/*******************/
	/**     工作组表格  **/
	/*******************/
	Ext.namespace('GroupCtl');
	GroupCtl.searchParams = {}; 
	
	GroupCtl.WorkGroupGrid = new Ext.yunda.Grid({
		loadURL: ctx + '/workGroup!findGroupListByEmployee.action',                 //装载列表数据的请求URL
	    saveFormColNum:2,	searchFormColNum:2,
	    storeAutoLoad: false,
	    singleSelect: true, 
	    tbar:[],
	    fields: [
	    	//{header:'人员ID', dataIndex:'empid', hidden:false, editor: { xtype:'hidden' }},
	    	{header:'工作组ID', dataIndex:'groupid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:'人员姓名', dataIndex:'empname', hidden:false, width:120, editor: { xtype:'hidden' }},
	    	{header:'工作组名', dataIndex:'groupname', hidden:false, width:240, editor: { xtype:'hidden' }}
	    ],
	    editOrder:[],
	    searchOrder:['empname','groupname'],
	    searchFn : function(searchParam) {
			GroupCtl.searchParams = searchParam;
			this.store.load();
		},
		toEditFn : function(){
			WGTreeWidget.empid = GroupCtl.searchParams.empid;
			WGTreeWidget.groupid = GroupCtl.WorkGroupGrid.selModel.getSelections()[0].get("groupid");//原工作组ID
			WGTreeWidget.transferWin.show();
			WGTreeWidget.tree.root.reload();
			WGTreeWidget.tree.getRootNode().expand();
		}
	});
	
	GroupCtl.WorkGroupGrid.store.setDefaultSort('gop.groupname', 'desc');

	GroupCtl.WorkGroupGrid.store.on('beforeload', function() {
		var searchParam = GroupCtl.searchParams;
		var whereList = [];
		whereList.push({propName : 'empid', propValue : searchParam['empid'],compare:1});
	    var params = {
	    	start : 0,
			limit : GroupCtl.WorkGroupGrid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.OmGroup",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	GroupCtl.WorkGroupWin = new Ext.Window({
           title:"工作组调整", 
           width:500, 
           height:300, 
           plain:true, 
           autoScroll : true,
           closeAction:"hide", 
           buttonAlign:'center', 
           maximizable:false, 
           layout: "fit",
           items:[GroupCtl.WorkGroupGrid],
           listeners: {
           		beforehide : function (){
           			EmpCtl.grid.store.load();
           			EmpCtl.ZZMask.hide();
           		}
           }
	});
	
	/*******************/
	/**     岗位表格  **/
	/*******************/
	Ext.namespace('PosiCtl');
	PosiCtl.searchParams = {}; 
	
	PosiCtl.PosiGrid = new Ext.yunda.Grid({
		loadURL: ctx + '/position!findPosiListByEmployee.action',                 //装载列表数据的请求URL
	    saveFormColNum:2,	searchFormColNum:2,
	    storeAutoLoad: false,
	    singleSelect: true, 
	    tbar:[],
	    fields: [
	    	//{header:'人员ID', dataIndex:'empid', hidden:false, editor: { xtype:'hidden' }},
	    	{header:'岗位ID', dataIndex:'positionid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:'人员姓名', dataIndex:'empname', hidden:false, width:120, editor: { xtype:'hidden' }},
	    	{header:'岗位名称', dataIndex:'posiname', hidden:false, width:240, editor: { xtype:'hidden' }}
	    ],
	    editOrder:[],
	    searchOrder:[],
	    searchFn : function(searchParam) {
			PosiCtl.searchParams = searchParam;
			this.store.load();
		},
		toEditFn : function(){
			WGTreeWidget.empid = PosiCtl.searchParams.empid;
			WGTreeWidget.positionid = PosiCtl.PosiGrid.selModel.getSelections()[0].get("positionid");//原岗位ID
			WGTreeWidget.transferWin.show();
			WGTreeWidget.tree.root.reload();
			WGTreeWidget.tree.getRootNode().expand();
		}
	});
	
	PosiCtl.PosiGrid.store.setDefaultSort('ep.positionid', 'desc');

	PosiCtl.PosiGrid.store.on('beforeload', function() {
		var searchParam = PosiCtl.searchParams;
		var whereList = [];
		whereList.push({propName : 'empid', propValue : searchParam['empid'],compare:1});
	    var params = {
	    	start : 0,
			limit : PosiCtl.PosiGrid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.OmPosition",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	PosiCtl.PosiWin = new Ext.Window({
           title:"岗位调整", 
           width:500, 
           height:300, 
           plain:true, 
           autoScroll : true,
           closeAction:"hide", 
           buttonAlign:'center', 
           maximizable:false, 
           layout: "fit",
           items:[PosiCtl.PosiGrid],
           listeners: {
           		beforehide : function (){
           			EmpCtl.grid.store.load();
           			EmpCtl.ZZMask.hide();
           		}
           }
	});
	
	
	
	/*******************/
	/**    人员总表    **/
	/*******************/
	
	Ext.namespace('EmpCtl');
	EmpCtl.searchParams = {}; 
	
	EmpCtl.genderStore = [];   //性别
	EmpCtl.empstatusStore = []; //人员状态
	EmpCtl.partyStore = [];    //政治面貌
	EmpCtl.degreeStore = [];   //职级
	EmpCtl.cardtypeStore = []; //证件类型
	
	EmpCtl.ZZMask = new Ext.LoadMask(Ext.getBody(), {msg:null});
	//人员信息列表
	EmpCtl.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/employee!findAllEmployees.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/employee!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/employee!delete.action',            //删除数据的请求URL
	    saveFormColNum:2,	searchFormColNum:2,
	    loadMask: null,
	    storeAutoLoad: true,
	    storeId:'empid',
	    tbar:['search','-',{
	    	text:"机构调动", iconCls:"icon-expand-all", handler: function(){
				if(!$yd.isSelectedRecord(EmpCtl.grid)) return; //未选择记录，直接返回
				//判断基于当前页面的遮罩控件是否存在，若不存在创建一个便于后续操作过程中调用
        		EmpCtl.ZZMask.show();
				OrgTreeWidget.widgetType = '1';
				OrgTreeWidget.transferWin.show();
				OrgTreeWidget.tree.root.reload();
			    OrgTreeWidget.tree.getRootNode().expand();
	    	}
	    }
//	    ,{
//	    	text:"主岗位调动", iconCls:"buildingIcon", handler: function(){
//	    		if(!$yd.isSelectedRecord(EmpCtl.grid)) return; //未选择记录，直接返回
//	    		//判断基于当前页面的遮罩控件是否存在，若不存在创建一个便于后续操作过程中调用
//        		EmpCtl.ZZMask.show();
//	    		OrgTreeWidget.widgetType = '2';
//				OrgTreeWidget.transferWin.show();
//				OrgTreeWidget.tree.root.reload();
//			    OrgTreeWidget.tree.getRootNode().expand();
//	    	}
//	    }
	    ,'-',{
	    	text:"工作组调动", iconCls:"silver2Icon", handler: function(){
	    		if(!$yd.isSelectedRecord(EmpCtl.grid)) return; //未选择记录，直接返回
	    		//判断基于当前页面的遮罩控件是否存在，若不存在创建一个便于后续操作过程中调用
        		EmpCtl.ZZMask.show();
	    		GroupCtl.WorkGroupWin.show();
	    		WGTreeWidget.widgetType = '1';
	    		GroupCtl.searchParams = {'empid':EmpCtl.grid.selModel.getSelections()[0].get("empid")};
	    		GroupCtl.WorkGroupGrid.store.load();
	    	}
	    }
//	    	,{
//	    	text:"岗位调动", iconCls:"buildingIcon", handler: function(){
//				if(!$yd.isSelectedRecord(EmpCtl.grid)) return; //未选择记录，直接返回
//	    		//判断基于当前页面的遮罩控件是否存在，若不存在创建一个便于后续操作过程中调用
//        		EmpCtl.ZZMask.show();
//	    		PosiCtl.PosiWin.show();
//	    		WGTreeWidget.widgetType = '2';
//	    		PosiCtl.searchParams = {'empid':EmpCtl.grid.selModel.getSelections()[0].get("empid")};
//	    		PosiCtl.PosiGrid.store.load();
//	    	}
//	    }
	    ],
	    fields: [
	    	{header:'人员状态', dataIndex : 'empstatus',  hidden:false,  width:30,
				editor: {
					id:'_empstatus_list5',xtype: 'EosDictEntry_combo', hiddenName: 'empstatus', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_EMPSTATUS'
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_EMPSTATUS',v);
				}
			},
	    	//列表中隐藏
	    	{header:'人员ID', dataIndex:'empid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:'操作员ID',dataIndex:'operatorid',hidden:true,editor:{xtype:'hidden',id:'_operatorid_0'}},
	    	{header:'操作员登录号',dataIndex:'userid',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'人员全名',dataIndex:'realname',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'基本岗位',dataIndex:'position',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'员工IC卡号',dataIndex:'cardNum',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'工资代码',dataIndex:'payId',hidden:true,editor:{xtype:'hidden'}},
	    	//列表中显示
	    	{header:'人员代码', dataIndex:'empcode', hidden:false, width:45,
	    	 editor: {maxLength:30,allowBlank:false, id:'_empcode_Id',
	    	  listeners : {
	    	  	change : function(){
	    	  		
						Ext.Ajax.request({
							url: ctx + '/employee!checkEmpCode.action', //数据源路径
							params: {'empCode' : this.getValue()}, //查询可承修机车
							success: function(response, options){
							       var result = Ext.util.JSON.decode(response.responseText);
							       if (result != null && result.errMsg == null) {
						           		if(result.isExits == true){
						           			MyExt.Msg.alert('人员代码【' + Ext.getCmp('_empcode_Id').getValue() + '】已存在! 请重新设置。');
						           			Ext.getCmp('_empcode_Id').setValue('');
						           		}
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
	    	}},
	    	{header:'人员姓名', dataIndex:'empname', hidden:false,  width:45, editor: {maxLength:50,allowBlank:false,id:'_empname_0'}},
			{header:'性别', dataIndex:'gender', hidden:false,  width:20,
				editor: {
					id:'_gender_list5',xtype: 'EosDictEntry_combo', hiddenName: 'gender', displayField: 'dictname', valueField: 'dictid',status:'1',dicttypeid:'ABF_GENDER'
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_GENDER',v);
				}
			},
			{header:'出生日期', dataIndex:'birthdate',  hidden:false, width:40, xtype:'datecolumn', editor: {xtype:'my97date', format: 'Y-m-d',initNow:false}},
			{header:'职级', dataIndex : 'degree',  hidden:false,  width:35,
				editor: { 
					id:'_degree_list5',xtype: 'EosDictEntry_combo', hiddenName: 'degree', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_EMPZC'
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_EMPZC',v);
				}
			},
			{header:'政治面貌', dataIndex : 'party',  hidden:true,  width:35,
				editor: { 
					id:'_party_list5',xtype: 'EosDictEntry_combo', hiddenName: 'party', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_PARTYVISAGE'
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_PARTYVISAGE',v);
				}
			},
			{header:'所属机构', dataIndex:'orgname', hidden:false, width: 45, editor: {xtype:'hidden'}},
			{header:'机构内岗位', dataIndex:'posiname', hidden:false, width: 45, editor: {xtype:'hidden'}},
			{header:'机构内职务', dataIndex:'dutyname', hidden:false, width: 45, editor: {xtype:'hidden'}},
			{header:'所属工作组', dataIndex:'groupname', hidden:false, width: 45, editor: {xtype:'hidden'}},
			{header:'工作组内岗位', dataIndex:'nposiname', hidden:false, width: 45, editor: {xtype:'hidden'}},
			{header:'工作组内职务', dataIndex:'ndutyname', hidden:false, width: 45, editor: {xtype:'hidden'}},
			{header:'办公室电话', dataIndex:'otel', hidden:true, width: 45, editor: {vtype:'telphone',maxLength:12}},
			{header:'办公邮箱', dataIndex:'oemail', hidden:true, width: 45, editor: {maxLength:128}},
			{header:'手机号码', dataIndex:'mobileno', hidden:true, width: 45, editor: {vtype:'mobile',maxLength:14}},
			{header:'证件类型', dataIndex : 'cardtype',  hidden:true, 
				editor: {
					id:'_cardtype_list5',xtype: 'EosDictEntry_combo', hiddenName: 'cardtype', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_CARDTYPE'
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_CARDTYPE',v);
				}
			},
			{header:'证件号码', dataIndex:'cardno', hidden:true, editor: {maxLength:20}},
			{header:'入职时间', dataIndex:'indate', hidden:true, editor: {xtype:'my97date', format: 'Y-m-d',initNow:false}},
			{header:'离职时间', dataIndex:'outdate', hidden:true, editor: {xtype:'my97date', format: 'Y-m-d',initNow:false}},
			{header:'办公室地址', dataIndex:'oaddress', hidden:true, editor: {maxLength:255}},
			{header:'办公邮编', dataIndex:'ozipcode', hidden:true, editor: {vtype:'postalcode',maxLength:10}},
		    {header:'传真号码', dataIndex:'faxno', hidden:true, editor: {maxLength:14,vtype:'telphone'}},
			{header:'IM号码', dataIndex:'msn', hidden:true, editor: {maxLength:16,xtype:'hidden'}},
			{header:'家庭电话', dataIndex:'htel', hidden:true, editor: {vtype:'telphone',maxLength:12}},
			{header:'家庭地址', dataIndex:'haddress', hidden:true, editor: {maxLength:128}},
			{header:'家庭邮编', dataIndex:'hzipcode', hidden:true, editor: {vtype:'postalcode',maxLength:10}},
			{header:'个人邮箱', dataIndex:'pemail', hidden:true, width: 45, editor: {maxLength:128}},
			{header:'直接主管', dataIndex:'major', hidden:true, editor: {xtype:'hidden'}},
			{header:'可授权角色', dataIndex:'specialty', hidden:true, editor: {xtype:'hidden'}},
			{header:'可管理机构', dataIndex:'orgidlist', hidden:true, editor: {xtype:'hidden'}},
			{header:'工作描述', dataIndex:'workexp', hidden:true, editor: {xtype:'textarea',maxLength:512}},
			{header:'注册日期', dataIndex:'regdate', hidden:true, editor: {xtype:'my97date', format: 'Y-m-d',initNow:false}},
			{header:'创建时间', dataIndex:'createtime', hidden:true, editor: {xtype:'hidden'}},
			{header:'最后更新时间', dataIndex:'lastmodytime', hidden:true, editor: {xtype:'hidden'}},
			{header:'主机构编号', dataIndex:'orgid', hidden:true, editor: {xtype:'hidden'}},
			{header:'备注', dataIndex:'remark',  hidden:true, editor: {xtype:'textarea',maxLength:200}}
	    ],
	    editOrder:[
	    	'empid','operatorid','userid','realname','position','cardNum','payId','lastmodytime','orgid','createtime', //隐藏部分
	    	'empcode','empname','regdate','birthdate','gender','empstatus','party','degree','cardtype','cardno',
	    	'indate','outdate','otel','oaddress','ozipcode','oemail','faxno','mobileno','htel','haddress',
	    	'hzipcode','pemail','workexp','remark'
	    ],
	    searchOrder:['empcode','empname'],
	    afterShowEditWin: function(record, rowIndex){
	    	//回显人员性别
	    	for(var i=0; i<EmpCtl.genderStore.length; i++){
	    		if(EmpCtl.genderStore[i].dictid == record.data.gender){
	    			EmpCtl.grid.saveForm.getForm().findField('_gender_list5').setDisplayValue(EmpCtl.genderStore[i].dictid,EmpCtl.genderStore[i].dictname);
	    			break;
	    		}
	    	}
	    	//回显人员状态
	    	for(var i=0; i<EmpCtl.empstatusStore.length; i++){
	    		if(EmpCtl.empstatusStore[i].dictid == record.data.empstatus){
	    			EmpCtl.grid.saveForm.getForm().findField('_empstatus_list5').setDisplayValue(EmpCtl.empstatusStore[i].dictid, EmpCtl.empstatusStore[i].dictname);
	    			break;
	    		}
	    	}
	    	//回显政治面貌
	    	for(var i=0; i<EmpCtl.partyStore.length; i++){
	    		if(EmpCtl.partyStore[i].dictid == record.data.party){
	    			EmpCtl.grid.saveForm.getForm().findField('_party_list5').setDisplayValue(EmpCtl.partyStore[i].dictid, EmpCtl.partyStore[i].dictname);
	    			break;
	    		}
	    	}
	    	//回显职级
	    	for(var i=0; i<EmpCtl.degreeStore.length; i++){
	    		if(EmpCtl.degreeStore[i].dictid == record.data.party){
	    			EmpCtl.grid.saveForm.getForm().findField('_degree_list5').setDisplayValue(EmpCtl.degreeStore[i].dictid, EmpCtl.degreeStore[i].dictname);
	    			break;
	    		}
	    	}
	    	//回显证件类型
	    	for(var i=0; i<EmpCtl.cardtypeStore.length; i++){
	    		if(EmpCtl.cardtypeStore[i].dictid == record.data.empstatus){
	    			EmpCtl.grid.saveForm.getForm().findField('_cardtype_list5').setDisplayValue(EmpCtl.cardtypeStore[i].dictid, EmpCtl.cardtypeStore[i].dictname);
	    			break;
	    		}
	    	}
	    },
	    beforeSaveFn: function(data){
	    	delete data.orgname;  //机构名称
	    	delete data.posiname; //主岗位名称
	    	delete data.dutyname; //主职务名称
	    	delete data.nposiname; //次要岗位名称
	    	delete data.ndutyname; //次要职务名称
	    	delete data.groupname; //工作组名称
	    	return true; 
	    },
	    searchFn : function(searchParam) {
			EmpCtl.searchParams = searchParam;
			this.store.load();
		}
	});
	
	//记录数据字典项-人员性别
	Ext.getCmp("_gender_list5").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			EmpCtl.genderStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	//记录数据字典项-人员状态
	Ext.getCmp("_empstatus_list5").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			EmpCtl.empstatusStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	//记录数据字典项-政治面貌
	Ext.getCmp("_party_list5").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			EmpCtl.partyStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	//记录数据字典项-职级
	Ext.getCmp("_degree_list5").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			EmpCtl.degreeStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	//记录数据字典项-证件类型
	Ext.getCmp("_cardtype_list5").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			EmpCtl.cardtypeStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
//	EmpCtl.grid.store.setDefaultSort('degree', 'desc');

	EmpCtl.grid.store.on('beforeload', function() {
		var searchParam = EmpCtl.searchParams;
		var whereList = [];
		
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop],compare:8});
		}
	    var params = {
	    	start : 0,
			limit : EmpCtl.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.OmEmployee",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:EmpCtl.grid });
});