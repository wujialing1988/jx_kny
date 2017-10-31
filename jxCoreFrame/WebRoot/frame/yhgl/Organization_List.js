/**
 * 機構信息表 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	/*********************************************************/
	/*                          机构                             */
	/*********************************************************/
	Ext.namespace('orglist');                       //机构人员树命名空间
	orglist.searchParams = {}; 
	
	//数据字典store
	orglist.statusStore = [];  //机构状态
	orglist.degreeStore = [];  //机构等级
	orglist.orgtypeStore = [];  //机构类型
	
	//机构信息列表
	orglist.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/organization!overPageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/organization!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/organization!delete.action',            //删除数据的请求URL
	    saveFormColNum:2,	searchFormColNum:2,
	    storeAutoLoad: false,
	    storeId:'orgid',
	    tbar:['search','add','delete',{
	    	text:"机构调整", iconCls:"chart_organisationIcon", handler: function(){
	    		if(!$yd.isSelectedRecord(orglist.grid)) return;//未选择记录，直接返回
//	    		if(orglist.grid.selModel.getCount() > 1){
//	    			MyExt.Msg.alert("请只针对一个机构进行操作！");
//	    			return;
//	    		}
	    		if(orglist.grid.searchWin)  orglist.grid.searchWin.hide(); 
	    		OrgTreeAdjust.transferWin.show();
	    		OrgTreeAdjust.tree.root.reload();
			    OrgTreeAdjust.tree.getRootNode().expand();
	    	}
	    }],
	    fields: [
	    	//列表中隐藏
	    	{header:'主键', dataIndex:'orgid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:'上级机构',dataIndex:'parentorgid',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'创建时间', dataIndex:'createtime', hidden:true, editor: { xtype:'hidden' }},
	    	{header:'机构层次',dataIndex:'orglevel',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'机构序列',dataIndex:'orgseq',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'机构管理员',dataIndex:'orgmanager',hidden:true,editor:{id:'_orgmanager_list0', xtype:'hidden'}},
	    	{header:'是否是叶子节点',dataIndex:'isleaf',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'子节点数',dataIndex:'subcount',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'机构主管岗位',dataIndex:'posiname',hidden:true,editor:{xtype:'hidden'}},
	    	//列表中显示
	    	{header:'机构代码', dataIndex:'orgcode', hidden:false, editor: {maxLength:32,allowBlank:false, vtype:'alphanum2',tabIndex :1}},
	    	{header:'机构名称', dataIndex:'orgname', hidden:false, editor: {maxLength:30,allowBlank:false, tabIndex :2}},
			{header:'机构等级', dataIndex:'orgdegree', hidden:false, 
				editor: {
					allowBlank:false, id:'_orgdegree_list0',tabIndex :3,
					xtype: 'EosDictEntry_combo', hiddenName: 'orgdegree', displayField: 'dictname', valueField: 'dictid',status:'1',dicttypeid:'ABF_ORGDEGREE'
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_ORGDEGREE',v);
				}
			},
			{header:'机构状态', dataIndex : 'status',  hidden:false, 
				editor: {
					allowBlank:false, id:'_status_list0', tabIndex :4,
					xtype: 'EosDictEntry_combo', hiddenName: 'status', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_ORGSTATUS'
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_ORGSTATUS',v);
				}
			},
			{header:'机构类型', dataIndex : 'orgtype',  hidden:false, 
				editor: { 
					id:'_orgtype_list0',tabIndex :5,
					xtype: 'EosDictEntry_combo', hiddenName: 'orgtype', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_ORGTYPE'
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_ORGTYPE',v);
				}
			},
			{header:'排列顺序', dataIndex:'sortno', hidden:true, editor: {vtype:'positiveInt',maxLength:4, tabIndex :6}},
			{header:'主页', dataIndex:'weburl', hidden:true, editor: {maxLength:40, tabIndex :7}},
			{header:'机构主管人员', dataIndex:'managerid', hidden:true, editor: {
				xtype: "OmEmployee_SelectWin", id:'_managerid_list0',tabIndex :11,
				hiddenName: "managerid", displayField:"empname", valueField: "empid",
				returnField :[{widgetId: '_orgmanager_list0',propertyName:'empname'}],
				editable: false 
			}},
			{header:'联系人', dataIndex:'linkman', hidden:true, editor: {maxLength:15, tabIndex :9}},
			{header:'电子邮件', dataIndex:'email', hidden:true, editor: {maxLength:30,tabIndex :13 }},
			{header:'生效日期', dataIndex:'startdate', hidden:true, editor: {xtype:'my97date', format: 'Y-m-d',initNow:false, tabIndex :15}},
			{header:'机构地址', dataIndex:'orgaddr', hidden:true, editor: { xtype:'textarea', maxLength:30,tabIndex :17}},
		    {header:'所属地域', dataIndex:'area',  hidden:true, editor: {maxLength:15, tabIndex :8}},
			{header:'邮政编码', dataIndex:'zipcode',  hidden:true, editor: {vtype:'postalcode',maxLength:10,tabIndex :10}},
			{header:'机构主管岗位', dataIndex:'manaposition',  hidden:true, editor: {
				xtype: "OmPosition_SelectWin", id:'_manaposition_list0',tabIndex :12,
				hiddenName: "manaposition", displayField:"posiname", valueField: "positionid", 
				returnField :[{widgetId: '_temp',propertyName:'posiname'}],
				editable: false 
			}},
			{header:'联系电话', dataIndex:'linktel',  hidden:true, editor: {vtype:'mobile',tabIndex :14}},
			{header:'失效时期', dataIndex:'enddate',  hidden:true, editor: {xtype:'my97date', format: 'Y-m-d',initNow:false, tabIndex :16}},
			{header:'备注', dataIndex:'remark',  hidden:true, editor: {xtype:'textarea',maxLength:200,tabIndex :18}}
	    ],
	    editOrder:['orgid','parentorgid','orglevel','orgseq','orgmanager','isleaf','subcount',
	    'orgcode','orgname','orgdegree','status','orgtype','sortno','weburl','area','linkman',
	    'zipcode','managerid','manaposition','email','linktel','startdate','enddate','orgaddr','remark'],
	    searchOrder:['orgcode','orgname'],
	    //默认信息
	    afterShowSaveWin: function(){
	    	if(orglist.statusStore!=null && orglist.statusStore.length>0)
	    	orglist.grid.saveForm.getForm().findField('_status_list0').setDisplayValue(orglist.statusStore[0].dictid,orglist.statusStore[0].dictname);
	    	if(orglist.degreeStore!=null && orglist.degreeStore.length>0)
	    	orglist.grid.saveForm.getForm().findField('_orgdegree_list0').setDisplayValue(orglist.degreeStore[0].dictid,orglist.degreeStore[0].dictname);
	    	if(orglist.orgtypeStore!=null && orglist.orgtypeStore.length>0)
	    	orglist.grid.saveForm.getForm().findField('_orgtype_list0').setDisplayValue(orglist.orgtypeStore[0].dictid,orglist.orgtypeStore[0].dictname);
	    }, 
	    //回显控件信息
	    afterShowEditWin: function(record, rowIndex){
	    	//回显机构状态
	    	for(var i=0; i<orglist.statusStore.length; i++){
	    		if(orglist.statusStore[i].dictid == record.data.status){
	    			orglist.grid.saveForm.getForm().findField('_status_list0').setDisplayValue(orglist.statusStore[i].dictid,orglist.statusStore[i].dictname);
	    			break;
	    		}
	    	}
	    	//回显机构等级
	    	for(var i=0; i<orglist.degreeStore.length; i++){
	    		if(orglist.degreeStore[i].dictid == record.data.orgdegree){
	    			orglist.grid.saveForm.getForm().findField('_orgdegree_list0').setDisplayValue(orglist.degreeStore[i].dictid,orglist.degreeStore[i].dictname);
	    			break;
	    		}
	    	}
	    	//回显机构类型
	    	for(var i=0; i<orglist.orgtypeStore.length; i++){
	    		if(orglist.orgtypeStore[i].dictid == record.data.orgtype){
	    			orglist.grid.saveForm.getForm().findField('_orgtype_list0').setDisplayValue(orglist.orgtypeStore[i].dictid,orglist.orgtypeStore[i].dictname);
	    			break;
	    		}
	    	}
	    	
	    	//回显人员控件
	    	orglist.grid.saveForm.getForm().findField('_managerid_list0').setDisplayValue(record.data.managerid,record.data.orgmanager);
	    	orglist.grid.saveForm.getForm().findField('_manaposition_list0').setDisplayValue(record.data.manaposition,record.data.posiname);
	    },
	    beforeSaveFn: function(data){
	    	data.parentorgid = OrgTab.OrgNodeId;
	    	delete data.posiname;
	    	return true; 
	    },
	    searchFn : function(searchParam) {
			orglist.searchParams = searchParam;
			this.store.load();
		},
		afterSaveSuccessFn: function(result, response, options){
        	this.store.reload();
        	alertSuccess();
//        	var node = orgtree.tree.getSelectionModel().getSelectedNode();
//        	if(node == null||node.id == 'ROOT_0'){
//        		orgtree.tree.root.reload();
//		        orgtree.tree.getRootNode().expand();  
//        	} else {
//        		var path = node.getPath('id');
//        		orgtree.tree.getLoader().load(OrgTab.parentTreeNode,
//        		function(treeNode){
//        			orgtree.tree.expandPath(path,'id',function(bSucess,oLastNode){
//        				orgtree.tree.getSelectionModel().select(oLastNode);
//        			});
//        		},this);
//        	}
			orgtree.tree.root.reload();
		    orgtree.tree.getRootNode().expand();
    	},
    	afterDeleteFn: function(){
    		orgtree.tree.root.reload();
		    orgtree.tree.getRootNode().expand();
    	}
	});
	
	//记录数据字典项-机构状态
	Ext.getCmp("_status_list0").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			orglist.statusStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	
	//记录数据字典项-机构等级
	Ext.getCmp("_orgdegree_list0").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			orglist.degreeStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	//记录数据字典项-机构类型
	Ext.getCmp("_orgtype_list0").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			orglist.orgtypeStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	
	orglist.grid.store.setDefaultSort('sortno', 'asc');

	orglist.grid.store.on('beforeload', function() {
		var searchParam = orglist.searchParams;
		var whereList = [];
		if(OrgTab.OrgNodeId==null||OrgTab.OrgNodeId==''){
			whereList.push({propName : 'parentorgid', propValue : '', compare:21});
		} else {
			whereList.push({propName : 'parentorgid', propValue : OrgTab.OrgNodeId, compare:1});
		}
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:8});
		}
	    var params = {
	    	start : 0,
			limit : orglist.grid.pagingToolbar.pageSize,
			queryClassName : "com.yunda.frame.yhgl.entity.OmOrganization",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	/*********************************************************/
	/*                        机构下人员                          */
	/*********************************************************/
	Ext.namespace('emplist');  //人员命名空间
	emplist.searchParams = {}; 
	var saveFnEmpName = "";    //新增人员成功后，将人员姓名记录
	var isOperator = false;    //是否是操作员
	
	emplist.genderStore = [];   //性别
	emplist.empstatusStore = []; //人员状态
	emplist.partyStore = [];    //政治面貌
	emplist.degreeStore = [];   //职级
	emplist.cardtypeStore = []; //证件类型
	
	//机构信息列表
	emplist.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/employee!findEmpListByOrg.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/employee!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/employee!delete.action',            //删除数据的请求URL
	    saveFormColNum:2,	searchFormColNum:2,
	    storeAutoLoad: false,
	    storeId:'empid',
	    tbar:['search','add','delete'],
	    fields: [
	    	//列表中隐藏
	    	{header:'人员ID', dataIndex:'empid', hidden:true, editor: { xtype:'hidden' }},
	    	{header:'操作员ID',dataIndex:'operatorid',hidden:true,editor:{xtype:'hidden',id:'_operatorid_0'}},
	    	{header:'操作员登录号',dataIndex:'userid',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'人员全名',dataIndex:'realname',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'基本岗位',dataIndex:'position',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'员工IC卡号',dataIndex:'cardNum',hidden:true,editor:{xtype:'hidden'}},
	    	{header:'工资代码',dataIndex:'payId',hidden:true,editor:{xtype:'hidden'}},
	    	//列表中显示
	    	{header:'人员代码', dataIndex:'empcode', hidden:false, 
	    	 editor: {maxLength:30,allowBlank:false, id:'_empcode_Id', vtype:'alphanum2',
	    	  listeners : {
	    	  	change : function(){
	    	  		if(OrgTab.OrgNodeId!=null){
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
	    	  }
	    	},searcher:{maxLength:30}},
	    	{header:'人员姓名', dataIndex:'empname', hidden:false, editor: {maxLength:25,allowBlank:false,id:'_empname_0',vtype:'chinese'}},
			{header:'性别', dataIndex:'gender', hidden:false, 
				editor: {
					id: '_gender_list4', xtype: 'EosDictEntry_combo', hiddenName: 'gender', displayField: 'dictname', valueField: 'dictid',status:'1',dicttypeid:'ABF_GENDER'
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_GENDER',v);
				}
			},
			{header:'人员状态', dataIndex : 'empstatus',  hidden:false, 
				editor: {
					id:'_empstatus_list4', xtype: 'EosDictEntry_combo', hiddenName: 'empstatus', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_EMPSTATUS'
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_EMPSTATUS',v);
				}
			},
			
			{header:'证件类型', dataIndex : 'cardtype',  hidden:false, 
				editor: {
					id:'_cardtype_list4', xtype: 'EosDictEntry_combo', hiddenName: 'cardtype', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_CARDTYPE'
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_CARDTYPE',v);
				}
			},
			{header:'政治面貌', dataIndex : 'party',  hidden:false, 
				editor: { 
					id:'_party_list4', xtype: 'EosDictEntry_combo', hiddenName: 'party', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_PARTYVISAGE'
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_PARTYVISAGE',v);
				}
			},
			{header:'职级', dataIndex : 'degree',  hidden:false, 
				editor: { 
					id:'_degree_list4', xtype: 'EosDictEntry_combo', hiddenName: 'degree', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_EMPZC'
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_EMPZC',v);
				}
			},
			{header:'出生日期', dataIndex:'birthdate',  hidden:true, editor: {xtype:'my97date', format: 'Y-m-d',initNow:false}},
			{header:'证件号码', dataIndex:'cardno', hidden:true, editor: {maxLength:20}},
			{header:'入职时间', dataIndex:'indate', hidden:true, editor: {xtype:'my97date', format: 'Y-m-d',initNow:false}},
			{header:'离职时间', dataIndex:'outdate', hidden:true, editor: {xtype:'my97date', format: 'Y-m-d',initNow:false}},
			{header:'办公室电话', dataIndex:'otel', hidden:true, editor: {vtype:'telphone',maxLength:12}},
			{header:'办公室地址', dataIndex:'oaddress', hidden:true, editor: {maxLength:255}},
			{header:'办公邮编', dataIndex:'ozipcode', hidden:true, editor: {vtype:'postalcode',maxLength:10}},
			{header:'办公邮箱', dataIndex:'oemail', hidden:true, editor: {maxLength:128}},
		    {header:'传真号码', dataIndex:'faxno', hidden:true, editor: {maxLength:14,vtype:'telphone'}},
			{header:'手机号码', dataIndex:'mobileno', hidden:true, editor: {vtype:'mobile',maxLength:14}},
			{header:'IM号码', dataIndex:'msn', hidden:true, editor: {maxLength:16,xtype:'hidden'}},
			{header:'家庭电话', dataIndex:'htel', hidden:true, editor: {vtype:'telphone',maxLength:12}},
			{header:'家庭地址', dataIndex:'haddress', hidden:true, editor: {maxLength:128}},
			{header:'家庭邮编', dataIndex:'hzipcode', hidden:true, editor: {vtype:'postalcode',maxLength:10}},
			{header:'个人邮箱', dataIndex:'pemail', hidden:true, editor: {maxLength:128}},
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
	    createSaveWin: function(){
	        if(this.saveForm == null) this.createSaveForm();
	        //计算查询窗体宽度
	        if(this.saveWinWidth == null)   this.saveWinWidth = (this.labelWidth + this.fieldWidth + 8) * this.saveFormColNum + 60;
	        this.saveWin = new Ext.Window({
	            title:"新增", width:this.saveWinWidth, height:this.saveWinHeight, plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, items:this.saveForm, 
	            buttons: [{
	            	id: 'operatorBtn', text: "登录设置", iconCls: "cogIcon", hidden:false, scope: this, handler: function(){
	            		this.saveWin.hide();
		            	operator.win.show();
		            	/*
		            	 * 重置控件
		            	 */
		            	operator.operatorForm.getForm().reset();  //重置表单
	    				var my97Ary = operator.operatorForm.findByType('my97date');
	                    if(!Ext.isEmpty(my97Ary) && Ext.isArray(my97Ary)){
	                    	for(var i = 0; i < my97Ary.length; i++){
		                        my97Ary[ i ].setValue('');
		                    }
	                    }
	    				var component = operator.operatorForm.findByType("EosDictEntry_combo"); //获取页面中所有控件
	    				if(!Ext.isEmpty(component) && Ext.isArray(component)){
	                    	for(var i = 0; i < component.length; i++){
			            	    component[ i ].clearValue(); //重置控件
			                }
	                    }
	    				if(operator.statusStore!=null && operator.statusStore.length>0)
	                    operator.operatorForm.getForm().findField('_status_1').setDisplayValue(operator.statusStore[0].dictid, operator.statusStore[0].dictname); //设置新增页操作员状态默认值
	    				if(operator.authmodeStore!=null && operator.authmodeStore.length>0)
	                    operator.operatorForm.getForm().findField('_authmode_1').setDisplayValue(operator.authmodeStore[0].dictid, operator.authmodeStore[0].dictname); //设置新增页认证模式默认值
	    				if(operator.menutypeStore!=null && operator.menutypeStore.length>0)
	                    operator.operatorForm.getForm().findField('_menutype_1').setDisplayValue(operator.menutypeStore[0].dictid, operator.menutypeStore[0].dictname); //设置新增页菜单风格默认值
	                    
		            	//如果当前表单中操作人员id不为空，则说明已添加该人员的登录信息，执行Ajax查询方法
	            		if(Ext.getCmp('_operatorid_0').getValue()!=null&&Ext.getCmp('_operatorid_0').getValue()!=''){
		            		Ext.Ajax.request({
								url: ctx + '/operator!findOperatorInfo.action', //数据源路径
								params: {'operatorid' : Ext.getCmp('_operatorid_0').getValue()}, //操作员Id
								success: function(response, options){
								       var result = Ext.util.JSON.decode(response.responseText);
								       if (result != null && result.errMsg == null &&result.acOperator != null) {
							           		operator.operatorForm.getForm().findField('_operatorid_1').setValue(result.acOperator.operatorid); //操作员ID
							           		operator.operatorForm.getForm().findField('_operatorname_1').setValue(result.acOperator.operatorname);//操作员名称
							           		operator.operatorForm.getForm().findField('_unlocktime_1').setValue(result.acOperator.unlocktime);//解锁时间
							           		operator.operatorForm.getForm().findField('_lastlogin_1').setValue(result.acOperator.lastlogin);//最后一次登录时间
							           		operator.operatorForm.getForm().findField('_errcount_1').setValue(result.acOperator.errcount);//最新登录次数
							           		operator.operatorForm.getForm().findField('_validtime_1').setValue(result.acOperator.validtime);//有效时间范围
							           		operator.operatorForm.getForm().findField('_maccode_1').setValue(result.acOperator.maccode);//MAC码
							           		operator.operatorForm.getForm().findField('_ipaddress_1').setValue(result.acOperator.ipaddress);//iP地址
							           		operator.operatorForm.getForm().findField('_email_1').setValue(result.acOperator.email);//邮箱
							           		operator.operatorForm.getForm().findField('_userid_1').setValue(result.acOperator.userid);//登录名

							           		//回显操作员状态
									    	for(var i=0; i<operator.statusStore.length; i++){
									    		if(operator.statusStore[i].dictid == result.acOperator.status){
									    			operator.operatorForm.getForm().findField('_status_1').setDisplayValue(operator.statusStore[i].dictid,operator.statusStore[i].dictname);
									    			break;
									    		}
									    	}
									    	//回显菜单风格
									    	for(var i=0; i<operator.menutypeStore.length; i++){
									    		if(operator.menutypeStore[i].dictid == result.acOperator.menutype){
									    			operator.operatorForm.getForm().findField('_menutype_1').setDisplayValue(operator.menutypeStore[i].dictid,operator.menutypeStore[i].dictname);
									    			break;
									    		}
									    	}
							           		if(result.acOperator.startdate!=null&&result.acOperator.startdate!='')
							           		operator.operatorForm.getForm().findField('_startdate_1').setValue(new Date(result.acOperator.startdate).format('Y-m-d'));//生效日期
							           		operator.operatorForm.getForm().findField('_password_1').setValue(result.acOperator.password);//登录密码
							           		//回显认证模式
									    	for(var i=0; i<operator.authmodeStore.length; i++){
									    		if(operator.authmodeStore[i].dictid == result.acOperator.authmode){
									    			operator.operatorForm.getForm().findField('_authmode_1').setDisplayValue(operator.authmodeStore[i].dictid,operator.authmodeStore[i].dictname);
									    			break;
									    		}
									    	}
							           		
							           		if(result.acOperator.invaldate!=null&&result.acOperator.invaldate!='')
							           		operator.operatorForm.getForm().findField('_invaldate_1').setValue(new Date(result.acOperator.invaldate).format('Y-m-d'));//密码失效日期
							           		if(result.acOperator.enddate!=null&&result.acOperator.enddate!='')
							           		operator.operatorForm.getForm().findField('_enddate_1').setValue(new Date(result.acOperator.enddate).format('Y-m-d'));//失效日期
								       } else {
								       		alertFail(result.errMsg);
								       }
								},
								failure: function(response, options){
								       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
								}
							});
	            		} 
	            		//当前表单中操作员id为空，未曾设置该人员的登录信息
	            		else {
	            			Ext.getCmp('_operatorname_1').setValue(Ext.getCmp('_empname_0').getValue()); //登录信息表单中设置操作员姓名
	            			
	            		}
	            	}
	            },{
	                text: "保存", iconCls: "saveIcon", scope: this, handler: this.saveFn
	            }, {
	                text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ this.saveWin.hide(); }
	            }]
	        });
	    },
	    afterShowSaveWin: function(){
	    	this.saveWin.buttons[0].setVisible(false); //不显示登录设置按钮
	    	if(emplist.genderStore!=null && emplist.genderStore.length>0)
	    	emplist.grid.saveForm.getForm().findField('_gender_list4').setDisplayValue(emplist.genderStore[0].dictid, emplist.genderStore[0].dictname); //设置新增页性别默认值
	    	if(emplist.empstatusStore!=null && emplist.empstatusStore.length>0)
	    	emplist.grid.saveForm.getForm().findField('_empstatus_list4').setDisplayValue(emplist.empstatusStore[0].dictid, emplist.empstatusStore[0].dictname); //设置新增页人员状态默认值
	    	if(emplist.partyStore!=null && emplist.partyStore.length>0)
	    	emplist.grid.saveForm.getForm().findField('_party_list4').setDisplayValue(emplist.partyStore[0].dictid, emplist.partyStore[0].dictname); //设置新增页政治面貌默认值
	    	isOperator = false;
	    },
    	afterShowEditWin: function(record, rowIndex){
    		saveFnEmpId = record.data.empid;
    		//当打开编辑窗口时， 人员的操作员信息为空，则说明该人员已经注册了人员信息，但尚没有操作员信息注册
    		if(record.data.operatorid == null && record.data.empid!=null) {
    			saveFnEmpName = record.data.empname; 
    		}
    		if(record.data.operatorid !=null && record.data.operatorid != ''){ //已经是操作员了
    			isOperator = true;
    		}
    		this.saveWin.buttons[0].setVisible(true); //显示登录设置按钮
    		//回显人员性别
	    	for(var i=0; i<emplist.genderStore.length; i++){
	    		if(emplist.genderStore[i].dictid == record.data.gender){
	    			emplist.grid.saveForm.getForm().findField('_gender_list4').setDisplayValue(emplist.genderStore[i].dictid,emplist.genderStore[i].dictname);
	    			break;
	    		}
	    	}
	    	//回显人员状态
	    	for(var i=0; i<emplist.empstatusStore.length; i++){
	    		if(emplist.empstatusStore[i].dictid == record.data.empstatus){
	    			emplist.grid.saveForm.getForm().findField('_empstatus_list4').setDisplayValue(emplist.empstatusStore[i].dictid, emplist.empstatusStore[i].dictname);
	    			break;
	    		}
	    	}
	    	//回显政治面貌
	    	for(var i=0; i<emplist.partyStore.length; i++){
	    		if(emplist.partyStore[i].dictid == record.data.party){
	    			emplist.grid.saveForm.getForm().findField('_party_list4').setDisplayValue(emplist.partyStore[i].dictid, emplist.partyStore[i].dictname);
	    			break;
	    		}
	    	}
	    	//回显职级
	    	for(var i=0; i<emplist.degreeStore.length; i++){
	    		if(emplist.degreeStore[i].dictid == record.data.degree){
	    			emplist.grid.saveForm.getForm().findField('_degree_list4').setDisplayValue(emplist.degreeStore[i].dictid, emplist.degreeStore[i].dictname);
	    			break;
	    		}
	    	}
	    	//回显证件类型
	    	for(var i=0; i<emplist.cardtypeStore.length; i++){
	    		if(emplist.cardtypeStore[i].dictid == record.data.cardtype){
	    			emplist.grid.saveForm.getForm().findField('_cardtype_list4').setDisplayValue(emplist.cardtypeStore[i].dictid, emplist.cardtypeStore[i].dictname);
	    			break;
	    		}
	    	}
    	},
	    searchOrder:['empcode','empname'],
	    beforeSaveFn: function(data){
	    	data.orgid = OrgTab.OrgNodeId;
	    	data.realname = data.empname;
	    	if(OrgTab.currentNodeType=='pos'&&OrgTab.PosNodeId!=null&&OrgTab.PosNodeId!='') //如果当前树节点是岗位，则设置岗位id
	    		data.position = OrgTab.PosNodeId;
	    	if(data.empid==null||data.empid==""){
	    		saveFnEmpName = data.empname; //如果当前是新增操作，则记录当前保存的人员姓名
	    	} else {
	    		saveFnEmpName = data.empname; //如果当前不是新增操作，则记录当前保存的人员姓名
	    		saveFnEmpId = data.empid; //如果当前不是新增操作，则记录当前保存的人员id
	    	}
	    	return true; 
	    },
	    searchFn : function(searchParam) {
			emplist.searchParams = searchParam;
			this.store.load();
		},
    	afterDeleteFn: function(){
    		orgtree.tree.root.reload();
		    orgtree.tree.getRootNode().expand();
    	},
    	saveFn: function(){
	        //表单验证是否通过
	        var form = this.saveForm.getForm(); 
	        if (!form.isValid()) return;
	        
	        //获取表单数据前触发函数
	        this.beforeGetFormData();
	        var data = form.getValues();
	        //获取表单数据后触发函数
	        this.afterGetFormData();
	        
	        //调用保存前触发函数，如果返回fasle将不保存记录
	        if(!this.beforeSaveFn(data)) return;
	        
	        if(self.loadMask)   self.loadMask.show();
	        var cfg = {
	            scope: this, url: this.saveURL, jsonData: data,
	            success: function(response, options){
	                if(self.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null) {
	                	saveFnEmpId = result.entity.empid;
	                    this.afterSaveSuccessFn(result, response, options);
	                } else {
	                    this.afterSaveFailFn(result, response, options);
	                }
	            }
	        };
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    },
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        orgtree.tree.root.reload();
		    orgtree.tree.getRootNode().expand();
	        this.saveWin.hide(); //隐藏人员表单
	        if(!isOperator&&saveFnEmpName!=null&&saveFnEmpName!=""){
		        Ext.MessageBox.confirm('提示','是否将【'+saveFnEmpName+'】设为操作员?',function(btn){
		        	if(btn=="yes"){
		        		operator.win.show();
		        		/*
		            	 * 重置控件
		            	 */
		            	operator.operatorForm.getForm().reset();  //重置表单
	    				var my97Ary = operator.operatorForm.findByType('my97date');
	                    if(!Ext.isEmpty(my97Ary) && Ext.isArray(my97Ary)){
	                    	for(var i = 0; i < my97Ary.length; i++){
		                        my97Ary[ i ].setValue('');
		                    }
	                    }
	    				var component = operator.operatorForm.findByType("EosDictEntry_combo"); //获取页面中所有控件
	    				if(!Ext.isEmpty(component) && Ext.isArray(component)){
	                    	for(var i = 0; i < component.length; i++){
			            	    component[ i ].clearValue(); //重置控件
			                }
	                    }
						operator.operatorForm.getForm().findField('_operatorname_1').setValue(saveFnEmpName);
						if(operator.statusStore!=null && operator.statusStore.length>0)
	                    operator.operatorForm.getForm().findField('_status_1').setDisplayValue(operator.statusStore[0].dictid, operator.statusStore[0].dictname); //设置新增页操作员状态默认值
						if(operator.authmodeStore!=null && operator.authmodeStore.length>0)
	                    operator.operatorForm.getForm().findField('_authmode_1').setDisplayValue(operator.authmodeStore[0].dictid, operator.authmodeStore[0].dictname); //设置新增页认证模式默认值
						if(operator.menutypeStore!=null && operator.menutypeStore.length>0)
	                    operator.operatorForm.getForm().findField('_menutype_1').setDisplayValue(operator.menutypeStore[0].dictid, operator.menutypeStore[0].dictname); //设置新增页菜单风格默认值
		        	}
		        });
	        }
    	}
	});
	
	//记录数据字典项-人员性别
	Ext.getCmp("_gender_list4").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			emplist.genderStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	//记录数据字典项-人员状态
	Ext.getCmp("_empstatus_list4").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			emplist.empstatusStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	//记录数据字典项-政治面貌
	Ext.getCmp("_party_list4").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			emplist.partyStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	//记录数据字典项-职级
	Ext.getCmp("_degree_list4").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			emplist.degreeStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	//记录数据字典项-证件类型
	Ext.getCmp("_cardtype_list4").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			emplist.cardtypeStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	emplist.grid.store.setDefaultSort('degree', 'desc');

	emplist.grid.store.on('beforeload', function() {
		var searchParam = emplist.searchParams;
		var whereList = [];
		if(OrgTab.PosNodeId!=null&&OrgTab.PosNodeId!=''){
			whereList.push({propName : 'positionid', propValue : OrgTab.PosNodeId, compare:1});
		}		
		whereList.push({propName : 'orgid', propValue : OrgTab.OrgNodeId, compare:1}); //机构ID
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop],compare:8});
		}
	    var params = {
	    	start : 0,
			limit : emplist.grid.pagingToolbar.pageSize,
			nodetype : OrgTab.currentNodeType,
			queryClassName : "com.yunda.frame.yhgl.entity.OmEmployee",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	/*********************************************************/
	/*                          操作员                            */
	/*********************************************************/
	Ext.namespace('operator'); 
	
	operator.labelWidth = 80;
	operator.anchor = '85%';
	operator.fieldWidth = 160;
	
	operator.statusStore = []; //操作员状态
	operator.authmodeStore = []; //认证模式
	operator.menutypeStore = []; //菜单风格
	
	operator.operatorForm = new Ext.form.FormPanel({
		layout:"form", border:true, style:"padding:10px" , baseCls: "x-plain", 
		labelWidth: operator.labelWidth, align:'center',  width: 550, hegith:200,
		defaultType:'textfield',defaults:{anchor:"100%"}, buttonAlign:'center',
		items:[{
			xtype: 'panel',	border:true,  layout:'column',	align:'center', baseCls: "x-plain", 
			items:[{
				align:'center',	defaultType:'textfield', border:false,  layout:"form",
				labelWidth: operator.labelWidth,	columnWidth:0.5, baseCls: "x-plain", 
				items:[
					//隐藏部分
					{ fieldLabel:'操作员ID', id:'_operatorid_1', name:'operatorid', hidden:true },
					{ fieldLabel:'操作员名称', id:'_operatorname_1',name:'operatorname', hidden:true},
					{ fieldLabel:'解锁时间', id:'_unlocktime_1', name:'unlocktime', hidden:true, xtype:'my97date', format: 'Y-m-d',initNow:false },
					{ fieldLabel:'最后一次登录时间', id:'_lastlogin_1', name:'lastlogin', hidden:true, xtype:'my97date', format: 'Y-m-d',initNow:false },
					{ fieldLabel:'最新登录次数', id:'_errcount_1', name:'errcount', hidden:true },
					{ fieldLabel:'有效时间范围', id:'_validtime_1', name:'validtime', hidden:true },
					{ fieldLabel:'MAC码', id:'_maccode_1', name:'maccode', hidden:true },
					{ fieldLabel:'iP地址', id:'_ipaddress_1', name:'ipaddress', hidden:true },
					{ fieldLabel:'邮箱', id:'_email_1', name:'email', hidden:true },
					//显示部分
					{ id:"_userid_1",	fieldLabel:'登录名',	name:'userid', allowBlank:false, width:operator.fieldWidth, maxLength:30},
					{ fieldLabel:'操作员状态', id:'_status_1', name:'status',
					  xtype: 'EosDictEntry_combo', hiddenName: 'status', status:'1', displayField: 'dictname', 
					  valueField: 'dictid', dicttypeid:'ABF_OPERSTATUS'},
					{ fieldLabel:'菜单风格', id:'_menutype_1', name:'menutype',
					  xtype: 'EosDictEntry_combo', hiddenName: 'menutype', status:'1', displayField: 'dictname', 
					  valueField: 'dictid', dicttypeid:'ABF_LAYOUTSTYLE'},
					{ fieldLabel:'生效日期', id:'_startdate_1', name:'startdate', xtype:'my97date', format: 'Y-m-d',initNow:false, width:operator.fieldWidth }
				]
			},{
				align:'center',	defaultType:'textfield', border:false, baseCls: "x-plain", layout:"form",
				labelWidth: operator.labelWidth,	columnWidth:0.5,
				items:[
					{ id:"_password_1", fieldLabel:'登录密码', name:'password', inputType:'password', allowBlank:false, width:operator.fieldWidth, maxLength:30},
					{ fieldLabel:'认证模式', id:'_authmode_1', name:'authmode',
					  xtype: 'EosDictEntry_combo', hiddenName: 'authmode', status:'1', displayField: 'dictname', 
					  valueField: 'dictid', dicttypeid:'ABF_AUTHMODE'},
					{ fieldLabel:'密码失效日期', id:'_invaldate_1', name:'invaldate', xtype:'my97date', format: 'Y-m-d',initNow:false, width:operator.fieldWidth},
					{ fieldLabel:'失效日期', id:'_enddate_1', name:'enddate', xtype:'my97date', format: 'Y-m-d',initNow:false, width:operator.fieldWidth }
					
				]
			}]
		}]
	});
	
	//记录数据字典项-操作员状态
	Ext.getCmp("_status_1").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			operator.statusStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	//记录数据字典项-认证模式
	Ext.getCmp("_authmode_1").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			operator.authmodeStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	//记录数据字典项-菜单风格
	Ext.getCmp("_menutype_1").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			operator.menutypeStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	operator.win = new Ext.Window({
		title: "操作员设置", maximizable: false, width: 550, height: 200, layout: "fit", 
		plain: true, closeAction: "hide", buttonAlign: 'center', border:false,
		items : [{
			xtype: 'panel',	border:true,  layout:'column',	align:'center',  buttonAlign: "center", baseCls: "x-plain", 
			items: [operator.operatorForm],
			buttons: [{
            	text: "保存", iconCls: "saveIcon", scope: this, handler: function(){
            		var form = operator.operatorForm.getForm();
            		var data = form.getValues();
			        var cfg = {
			            scope: this, url: ctx+"/operator!saveOrUpdate.action", jsonData: data,
			            params: {'empid' : saveFnEmpId}, //用户ID
			            success: function(response, options){
//			                if(self.loadMask)   self.loadMask.hide();
			                var result = Ext.util.JSON.decode(response.responseText);
			                if (result.errMsg == null) {
			                    emplist.grid.store.load();
			                    alertSuccess();
			                    saveFnEmpName = "";
			                    operator.win.hide();
			                } else {
			                    alertFail(result.errMsg);
			                }
			            }
			        };
			        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
            	}
	        },{
		 	 text: "关闭", iconCls: "closeIcon",handler: function(){ 
		 	 	operator.win.hide(); 
		 	 }
		 }]
		}]
	});
	/*********************************************************/
	/*                         岗位                               */
	/*********************************************************/
	Ext.namespace('positionlist');                       //岗位命名空间
	positionlist.searchParams = {}; 
	positionlist.positypeStore = [];   //岗位类别
	positionlist.statusStore = [];     //岗位状态
	
	//机构信息列表
	positionlist.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/position!posiQuery.action',                 //装载列表数据的请求URL
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
			{header:'所属机构',	dataIndex:'orgid',	hidden:true, editor:{xtype:'hidden'}},
	    	//列表中显示
	    	{header:'岗位代码', dataIndex:'posicode', hidden:false, editor: {allowBlank:false, maxLength:8}},
	    	{header:'岗位名称', dataIndex:'posiname', hidden:false, editor: {allowBlank:false, maxLength:15}},
	    	{header:'所属机构',	dataIndex:'orgname', hidden:false, editor:{id:'_orgname_list0',disabled:true}},
	    	{header:'所属职务', dataIndex:'dutyid',	hidden:true, editor:{
	    		id:'_dutyid_list0',
	    		xtype: "WorkDuty_comboTree",
	    		hiddenName: "dutyid", disabled:false,
	    		selectNodeModel: "exceptRoot" ,
		    	allowBlank: false
	    	}},
	    	{header:'所属职务', dataIndex:'dutyname', hidden:false, editor: {xtype:'hidden'}},
	    	{header:'岗位类别', dataIndex : 'positype',  hidden:false, 
				editor: { 
					id:'_positype_list3',xtype: 'EosDictEntry_combo', hiddenName: 'positype', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_POSITYPE', disabled:true, allowBlank:false
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_POSITYPE',v);
				}
			},
			{header:'岗位状态', dataIndex : 'status',  hidden:false, 
				editor: { 
					id:'_status_list3',xtype: 'EosDictEntry_combo', hiddenName: 'status', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_POSISTATUS'
				},renderer: function(v,metadata, record,rowIndex, colIndex, store){ 
					return EosDictEntry.getDictname('ABF_POSISTATUS',v);
				}
			},
			{header:'有效开始时间', dataIndex:'startdate', hidden:false, xtype:'datecolumn',	editor: {xtype:'my97date', format: 'Y-m-d',initNow:false}},
			{header:'有效结束时间', dataIndex:'enddate', hidden:false, xtype:'datecolumn', 	editor: {xtype:'my97date', format: 'Y-m-d',initNow:false}}
			
	    ],
	    editOrder:['posicode','posiname','orgname','dutyid','startdate','enddate'],
	    searchOrder:['posicode','posiname'],
	     //回显职务控件
	    afterShowEditWin: function(record, rowIndex){
	    	positionlist.grid.saveForm.getForm().findField('_dutyid_list0').setDisplayValue(record.data.dutyid,record.data.dutyname);
	    	
	    	if(positionlist.positypeStore == null||positionlist.positypeStore.length<1){
	    		Ext.getCmp('_positype_list3').setDisabled(false);
	    	} 
	    	
	    	//回显岗位类型
	    	for(var i=0; i<positionlist.positypeStore.length; i++){
	    		if(positionlist.positypeStore[i].dictid == record.data.positype){
	    			positionlist.grid.saveForm.getForm().findField('_positype_list3').setDisplayValue(positionlist.positypeStore[i].dictid,positionlist.positypeStore[i].dictname);
	    			break;
	    		}
	    	}
	    	
	    	//回显岗位状态
	    	for(var i=0; i<positionlist.statusStore.length; i++){
	    		if(positionlist.statusStore[i].dictid == record.data.status){
	    			positionlist.grid.saveForm.getForm().findField('_status_list3').setDisplayValue(positionlist.statusStore[i].dictid, positionlist.statusStore[i].dictname);
	    			break;
	    		}
	    	}
	    },
	    afterShowSaveWin: function(){
	    	var obj = positionlist.grid.saveForm.getForm().findField('_dutyid_list0');
	    	if(obj.getValue()!=null&&obj.getValue()!=''){
	    		obj.clearValue();
	    	}
	    	positionlist.grid.saveForm.getForm().findField('_orgname_list0').setValue(OrgTab.OrgNodeName); //新增时，填充机构名称到表单
	    	
	    	//设置岗位类别默认值
	    	if(positionlist.positypeStore!=null && positionlist.positypeStore.length>0){
	    		positionlist.grid.saveForm.getForm().findField('_positype_list3').setDisplayValue(positionlist.positypeStore[0].dictid,positionlist.positypeStore[0].dictname);
	    	}else{
	    		Ext.getCmp('_positype_list3').setDisabled(false);
	    	} 
	    	
	    	//设置岗位状态默认值
	    	if(positionlist.statusStore!=null && positionlist.statusStore.length>0)
	    	positionlist.grid.saveForm.getForm().findField('_status_list3').setDisplayValue(positionlist.statusStore[0].dictid,positionlist.statusStore[0].dictname);
	    	return true;
	    },
	    beforeGetFormData: function(){
	    	Ext.getCmp('_positype_list3').setDisabled(false);
	    },
	    afterGetFormData: function(){
	    	Ext.getCmp('_positype_list3').setDisabled(true);
	    },    
	    beforeSaveFn: function(data){
	    	data.orgid = OrgTab.OrgNodeId; //设置机构归属
	    	if(OrgTab.PosNodeId!=null&&OrgTab.PosNodeId!=''){
	    		data.manaposi = OrgTab.PosNodeId; //设置上级岗位
	    	}
	    	delete data.dutyname;
	    	delete data.orgname;
	    	return true; 
	    },
	    searchFn : function(searchParam) {
			positionlist.searchParams = searchParam;
			this.store.load();
		},
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        orgtree.tree.root.reload();
		    orgtree.tree.getRootNode().expand();
    	},
    	afterDeleteFn: function(){
    		orgtree.tree.root.reload();
		    orgtree.tree.getRootNode().expand();
    	}
	});
	
	//记录数据字典项-岗位类别
	Ext.getCmp("_positype_list3").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			positionlist.positypeStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	//记录数据字典项-岗位状态
	Ext.getCmp("_status_list3").getStore().on("load", function(store, records){
		for(var i=0;i<records.length;i++){
			if(typeof(records[i].data.dictname) != 'undefined' && records[i].data.dictname == '请选择..') continue;
			positionlist.statusStore.push({dictid:records[i].data.dictid,dictname:records[i].data.dictname});
		}
	});
	
	positionlist.grid.store.setDefaultSort('createtime', 'desc');

	positionlist.grid.store.on('beforeload', function() {
		var searchParam = positionlist.searchParams;
		var whereList = [];
		if(OrgTab.PosNodeId!=null&&OrgTab.PosNodeId!=''){
			whereList.push({propName : 'manaposi', propValue : OrgTab.PosNodeId, compare:1});//设置上级岗位条件
		} else {
			whereList.push({propName : 'manaposi', propValue : '', compare:21});//设置上级岗位条件 is null
		}
		whereList.push({propName : 'orgid', propValue : OrgTab.OrgNodeId, compare:1});//设置机构条件
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:8});
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
	    loadURL: ctx + '/employee!findAddEmpList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/employee!addEmpGroupOrPosition.action',             //保存数据的请求URL
	    saveFormColNum:2,	searchFormColNum:1,
	    height:326,
	    storeAutoLoad: false,
	    storeId:'empid',
	    tbar:['search',{
	    	text:"新增", iconCls:"addIcon", handler: function(){
	    		if(AddEmpList.grid.searchWin)  AddEmpList.grid.searchWin.hide();   
        		if(!$yd.isSelectedRecord(AddEmpList.grid)) return;//未选择记录，直接返回
        		//如果当前执行的是岗位下的人员新增操作
        		if(OrgTab.currentNodeType != null && OrgTab.currentNodeType != '' && OrgTab.currentNodeType == 'pos'){
        			//执行保存的AJAX请求
	        		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
	            		scope: AddEmpList.grid, url: AddEmpList.grid.saveURL, params: {positionid:OrgTab.PosNodeId, ismain:'y', ids: $yd.getSelectedIdx(AddEmpList.grid, AddEmpList.grid.storeId)}
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
	AddEmpList.grid.un('rowdblclick', AddEmpList.grid.toEditFn, AddEmpList.grid); 

	AddEmpList.grid.store.on('beforeload', function() {
		var searchParam = AddEmpList.searchParams;
		var whereList = [];
		if(OrgTab.currentNodeType != null && OrgTab.currentNodeType != '' && OrgTab.currentNodeType == 'pos'){
			whereList.push({propName : 'positionid', propValue : OrgTab.PosNodeId, compare:1}); //设置查询条件：查询范围为当前岗位positionid
		}
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop],compare:8});
		}
	    var params = {
	    	start : 0,
			limit : AddEmpList.grid.pagingToolbar.pageSize,
			nodetype : OrgTab.currentNodeType, //当前选中数节点的节点类型（gop-工作组、pos-岗位）
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
				emplist2.grid.store.load();
				orgtree.tree.root.reload();
		    	orgtree.tree.getRootNode().expand();
			}
		}
	});
	
	/*********************************************************/
	/*                        岗位下人员                          */
	/*********************************************************/
	Ext.namespace('emplist2');  //岗位下人员命名空间
	emplist2.searchParams = {}; 
	
	//机构信息列表
	emplist2.grid = new Ext.yunda.Grid({
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
	    		if(emplist2.grid.searchWin)  emplist2.grid.searchWin.hide();   
        		if(!$yd.isSelectedRecord(emplist2.grid)) return;//未选择记录，直接返回
        		//如果当前执行的是岗位下的人员删除操作
        		if(OrgTab.currentNodeType != null && OrgTab.currentNodeType != '' && OrgTab.currentNodeType == 'pos'){
        			//执行保存的AJAX请求
	        		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
	            		scope: emplist2.grid, url: emplist2.grid.deleteURL, params: {positionid:OrgTab.PosNodeId, ids: $yd.getSelectedIdx(emplist2.grid, emplist2.grid.storeId)}
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
	    	{header:'所属岗位', dataIndex:'groupname', 	hidden:false, 	editor: {xtype:'hidden'}}
	    ],
	    editOrder:[],
	    searchOrder:['userid','empname'],
	    searchFn : function(searchParam) {
			emplist2.searchParams = searchParam;
			this.store.load();
		},
		afterDeleteFn: function(){
    		orgtree.tree.root.reload();
		    orgtree.tree.getRootNode().expand();
    	}
	});
	
	emplist2.grid.store.setDefaultSort('emp.empname', 'asc');
	//移除侦听器
	emplist2.grid.un('rowdblclick', emplist2.grid.toEditFn, emplist2.grid); 

	emplist2.grid.store.on('beforeload', function() {
		var searchParam = emplist2.searchParams;
		var whereList = [];
		//如当前选中树节点类型为岗位
	    if(OrgTab.currentNodeType != null && OrgTab.currentNodeType != '' && OrgTab.currentNodeType == 'pos'){
			whereList.push({propName : 'pos.positionid', propValue : OrgTab.PosNodeId, compare:1}); //设置查询条件：查询范围为当前岗位positionid
		}
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop],compare:8});
		}
	    var params = {
	    	start : 0,
			limit : emplist2.grid.pagingToolbar.pageSize,
			nodetype : OrgTab.currentNodeType, //当前选中数节点的节点类型（gop-工作组、pos-岗位）
			queryClassName : "com.yunda.frame.yhgl.entity.OmEmployee",
	        whereListJson: Ext.util.JSON.encode(whereList)
	    };    
	    this.baseParams = params;
	});
	
	
});