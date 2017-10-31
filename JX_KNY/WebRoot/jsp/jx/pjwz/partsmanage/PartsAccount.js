/**
 * 互换配件信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
//定义命名空间
Ext.namespace("PartsAccount");
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
PartsAccount.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
//表单组件高宽等设置
PartsAccount.labelWidth = 100;
PartsAccount.fieldWidth = 190;
PartsAccount.textareaWidth = 540;
//信息表单
PartsAccount.form = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "99%" }, autoScroll:true,
    layout: "form",		border: false,		style: "padding:15px",		labelWidth: PartsAccount.labelWidth,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:PartsAccount.labelWidth,
            columnWidth: 0.5, 
            items: [
            	{ id:"specificationModel_combo",xtype:'PartsTypeAndQuota_SelectWin',editable:false,
            	fieldLabel : "规格型号",hiddenName:"specificationModel",valueField : 'specificationModel',displayField : 'specificationModel',
            	returnField:[{widgetId:"PartsAccount_partsTypeIDX",propertyName:"idx"},
						{widgetId:"PartsAccount_partsName",propertyName:"partsName"},
						{widgetId:"PartsAccount_professionalTypeIdx",propertyName:"professionalTypeIdx"},
						{widgetId:"PartsAccount_professionalTypeName",propertyName:"professionalTypeName"}],
						width:PartsAccount.fieldWidth},
        		{id:"PartsAccount_professionalTypeName",fieldLabel:"专业类型", name:"professionalTypeName",width:PartsAccount.fieldWidth},
				{ id:"nameplateNo",name:"nameplateNo", fieldLabel:"配件铭牌号",allowBlank:false,maxLength:50 ,width:PartsAccount.fieldWidth },
//				{ name:"useUnitName", fieldLabel:"使用单位名称",allowBlank:false,width:PartsAccount.fieldWidth },
				{id:"orgown",
				xtype: 'OmOrganizationCustom_comboTree',
				hiddenName: 'ownerUnit',allowBlank:false, 
				fieldLabel:"配属单位",//orgid:branch,orgname:branchName,
				queryHql:"from OmOrganization where 1=1 and orgdegree='oversea'",
				returnField:[{widgetId:"PartsAccount_ownerUnitName",propertyName:"orgname"}
				],
				selectNodeModel:'all',width:PartsAccount.fieldWidth},
				
				{ id:"Factory_Select",xtype:'GyjcFactory_SelectWin',editable:false,fieldLabel : "生产厂家",hiddenName:"madeFactoryIdx",
				queryHql:"From GyjcFactory where fcID='B'",
				returnField:[{widgetId:"PartsAccount_madeFactoryName",propertyName:"fName"}],width:PartsAccount.fieldWidth},
//				{id:"assetsStaus_combo",xtype:"EosDictEntry_combo",fieldLabel:"资产状态",hiddenName:"assetsStaus",
//				 displayField:"dictname",valueField:"dictid",dicttypeid:"PJWZ_Parts_Account_Assets_Staus",
//				 width:PartsAccount.fieldWidth,queryWhere:" id.dictid in (10,12)" },
//				{id:"installPartsStatus_combo",xtype:"EosDictEntry_combo",fieldLabel:"上配件状态",hiddenName:"installPartsStatus",
//				 displayField:"dictname",valueField:"dictid",dicttypeid:"PJWZ_Parts_Account_Install_Parts_Status",width:PartsAccount.fieldWidth },
//				{id:"warehouseStatus_combo",xtype:"EosDictEntry_combo",fieldLabel:"库存状态",hiddenName:"warehouseStatus", 
//				 displayField:"dictname",valueField:"dictid",dicttypeid:"PJWZ_Parts_Account_WareHouse_Status",width:PartsAccount.fieldWidth },
				{ id:"useDate",name:"useDate", xtype:"my97date", fieldLabel:"交付使用日期",allowBlank:false, width:PartsAccount.fieldWidth },
				{ id:"fixedAssetsNo",name:"fixedAssetsNo", fieldLabel:"固定资产编号",maxLength:50 , width:PartsAccount.fieldWidth },
				{ id:"fixedAssetsCardno",name:"fixedAssetsCardno", fieldLabel:"固定资产卡片号",maxLength:50 ,width:PartsAccount.fieldWidth }
            ]
        },
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:PartsAccount.labelWidth,
            columnWidth: 0.5, 
            items: [
            	{ id:"PartsAccount_partsName",name:"partsName", fieldLabel:"配件名称",maxLength:100 , width:PartsAccount.fieldWidth },
				{ id:"partsNo",name:"partsNo", fieldLabel:"配件编号", allowBlank:false,maxLength:50 ,width:PartsAccount.fieldWidth },
				{id:"orguse",
				xtype: 'OmOrganizationCustom_comboTree',
				hiddenName: 'useUnit',allowBlank:false, 
				fieldLabel:"使用单位",//orgid:branch,orgname:branchName,
				queryHql:"from OmOrganization where 1=1 and orgdegree='oversea'",
				returnField:[{widgetId:"PartsAccount_useUnitName",propertyName:"orgname"}
				],
				selectNodeModel:'all',width:PartsAccount.fieldWidth},
				{ id:"leaveDate",name:"leaveDate", xtype:"my97date", fieldLabel:"出厂日期",width:PartsAccount.fieldWidth },
				{ id:"price",name:"price", xtype:"numberfield", fieldLabel:"价格(元)",allowBlank:false,vtype: "nonNegativeFloat",width:PartsAccount.fieldWidth },
//				{id:"installTrainStatus_combo",xtype:"EosDictEntry_combo",fieldLabel:"上车状态",hiddenName:"installTrainStatus",
//				 displayField:"dictname",valueField:"dictid",dicttypeid:"PJWZ_Parts_Account_Install_Train_Status",width:PartsAccount.fieldWidth },
//				{id:"healthStatus_combo",xtype:"EosDictEntry_combo",fieldLabel:"健康状态",hiddenName:"healthStatus",
//				 displayField:"dictname",valueField:"dictid",dicttypeid:"PJWZ_Parts_Account_Health_Status",width:PartsAccount.fieldWidth },
//				{id:"turnoverStatus_combo",xtype:"EosDictEntry_combo",fieldLabel:"周转状态",hiddenName:"turnoverStatus",
//				 displayField:"dictname",valueField:"dictid",dicttypeid:"PJWZ_Parts_Account_Turnover_Status",
//				 width:PartsAccount.fieldWidth,queryWhere:" id.dictid in (30,40)" },
				{ id:"registerTime",name:"registerTime", xtype:"my97date",my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"},fieldLabel:"登记时间",  
				format: "Y-m-d H:i",allowBlank:false,width:PartsAccount.fieldWidth },
				{ id:"fixedAssetsName",name:"fixedAssetsName", fieldLabel:"固定资产名称",maxLength:100 ,width:PartsAccount.fieldWidth }
            ]
        },
        {xtype:"hidden", name:"idx"},
        {id:"PartsAccount_partsTypeIDX",xtype:"hidden", name:"partsTypeIDX"},
        {id:"PartsAccount_empid",xtype:"hidden", name:"empid"},
        {id:"PartsAccount_empname",xtype:"hidden", name:"empname"},
        {id:"PartsAccount_madeFactoryName",xtype:"hidden", name:"madeFactoryName"},
        {id:"PartsAccount_useUnitName",xtype:"hidden", name:"useUnitName"},
//        {id:"PartsAccount_useUnit",xtype:"hidden", name:"useUnit"},
        {id:"PartsAccount_ownerUnitName",xtype:"hidden", name:"ownerUnitName"},
        {id:"PartsAccount_professionalTypeIdx",xtype:"hidden", name:"professionalTypeIdx"}
        ]
    },{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:PartsAccount.labelWidth,
            columnWidth: 1, 
            items: [
				{ id:"remarks",name:"remarks", fieldLabel:"备注", xtype:"textarea",maxLength:1000 , width:PartsAccount.textareaWidth }
            ]
        }
        ]
    }
    ]
});
//新增编辑窗口
PartsAccount.win = new Ext.Window({
    title:"新增", maximizable:true, width:820, height:430, 
    plain:true, closeAction:"hide", items:PartsAccount.form,
    buttonAlign: "center",
    buttons: [{
        id:"accountsave",text:"保存", iconCls:"saveIcon",
        handler:function(){
            var form = PartsAccount.form.getForm();
            if (!form.isValid()) return;
            PartsAccount.enableForm();
            var data = form.getValues();
            data.registerTime = Date.parseDate(data.registerTime, "Y-m-d H:i");
            Ext.Ajax.request({
                url: ctx + "/partsAccount!isHas.action",
                jsonData: data,
                success: function(response, options){
                  	PartsAccount.loadMask.hide();
                    var result_v = Ext.util.JSON.decode(response.responseText);
                    if(result_v.flag==true){
                    	Ext.Msg.confirm("提示  ", "已存储铭牌号为【"+data.nameplateNo+"】的配件，是否继续保存？  ", function(btn){
			                if(btn == "yes"){
					            PartsAccount.save(data);
			                }
			            });
                    }else PartsAccount.save(data);
                },
                failure: function(response, options){
                    PartsAccount.loadMask.hide();
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
            
        }
    },{
    	 text:"关闭", iconCls:"closeIcon",handler:function(){
    	 	PartsAccount.win.hide();
    	 }
    }
    ]
});
PartsAccount.save = function(data){
	var url = ctx + "/partsAccount!saveOrUpdate.action";
    PartsAccount.disableForm();
    PartsAccount.loadMask.show();
    Ext.Ajax.request({
        url: url,
        jsonData: data,
        success: function(response, options){
          	PartsAccount.loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                alertSuccess();
//                PartsAccount.win.hide();
                PartsAccount.store.reload();
                PartsAccount.getPartsNo();
            } else {
                alertFail(result.errMsg);
            }
        },
        failure: function(response, options){
            PartsAccount.loadMask.hide();
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    });
}
//根据业务编码生成规则生成配件编号
PartsAccount.getPartsNo = function(){
	var url = ctx + "/codeRuleConfig!getConfigRule.action";
	Ext.Ajax.request({
                url: url,
                params: {ruleFunction: "PJWZ_PARTS_ACCOUNT_PARTS_NO"},
                success: function(response, options){
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        Ext.getCmp("partsNo").setValue(result.rule);
                    }
                },
                failure: function(response, options){
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
}
//数据容器
PartsAccount.store = new Ext.data.JsonStore({
	id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:true,
    url: ctx + "/partsAccount!pageList.action",
    fields: [ "partsTypeIDX","partsName","specificationModel","nameplateNo","partsNo","fixedAssetsNo","fixedAssetsName",
        "fixedAssetsCardno","assetsStaus","installPartsStatus","installTrainStatus","warehouseStatus","healthStatus",
        "turnoverStatus","ownerUnit","ownerUnitName","useUnit","useUnitName","madeFactoryName","madeFactoryIdx",
        {name:"leaveDate", type:"date", dateFormat: 'time'},
        {name:"useDate", type:"date", dateFormat: 'time'},
        {name:"registerTime", type:"date", dateFormat: 'time'},
        "price","buildupTypeIdx","buildupTypeCode","buildupTypeName","empid","empname","achieveKM","achieveYEAR","remarks","idx","professionalTypeIdx","professionalTypeName" ]
});
//选择模式，勾选框可多选
PartsAccount.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
//分页工具
PartsAccount.pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsAccount.store});
//人员列表
PartsAccount.grid = new Ext.grid.GridPanel({
    border: false,
    loadMask:true,
    //随着窗口（父容器）大小自动调整,true表示不出现滚动条，列宽会自动压缩
    viewConfig: {forceFit: true},
    //该高度设置在IE、Google浏览器显示正常，在Opera显示不正常
//    height: document.documentElement.scrollHeight,
    //可移动列
    enableColumnMove: true,
//    loadMask: {msg:"正在加载表格数据，请稍等..."},
    //偶数行变色
    stripeRows: true,
    //多选行
    selModel: PartsAccount.sm,
    colModel: new Ext.grid.ColumnModel([
        PartsAccount.sm,
        new Ext.grid.RowNumberer(),
        { sortable:true, header:"配件名称", dataIndex:"partsName" },			
        { sortable:true, header:"规格型号", dataIndex:"specificationModel" },				
        { sortable:true, header:"专业类型", dataIndex:"professionalTypeName" },			
        { sortable:true, header:"配件铭牌号", dataIndex:"nameplateNo" },			
        { sortable:true, header:"配件编号", dataIndex:"partsNo" },			
//        { sortable:true, header:"资产状态", dataIndex:"assetsStaus",renderer: function(v){ return EosDictEntry.getDictname('PJWZ_Parts_Account_Assets_Staus',v)} },			
        { sortable:true, header:"配属单位", dataIndex:"ownerUnitName" },			
        { sortable:true, header:"使用单位", dataIndex:"useUnitName" },			
        { sortable:true, header:"登记人名称", dataIndex:"empname" },			
        { sortable:true, header:"登记时间", dataIndex:"registerTime", xtype: 'datecolumn', format: 'Y-m-d H:i' }		
    ]),
    store: PartsAccount.store,
    //工具栏
    tbar: [{
        text:"查询", iconCls:"searchIcon", handler: function(){
        	PartsAccount.win.hide();
        	PartsAccount.searchWin.show(); 
       	}
    },{
        text:"新增", iconCls:"addIcon",
        handler: function(){
        	PartsAccount.searchWin.hide();
            PartsAccount.form.getForm().reset();
            PartsAccount.win.setTitle("新增");
            PartsAccount.win.show();
            PartsAccount.getPartsNo();
            //为登记时间赋默认值
            Ext.getCmp("registerTime").setValue(new Date().format('Y-m-d H:i'));
            //为交付使用日期赋默认值
            Ext.getCmp("useDate").setValue(new Date().format('Y-m-d'));
            //设置单位默认值,如果段级单位没有值，就取操作员所属单位
            var node = {};
            if(oversea==""){
            	node = {id: orgId, text: orgName};
            }else {
            	node = {id: oversea, text: overseaName};
            }
	        Ext.getCmp("orgown").setValue(node);
	        Ext.getCmp("PartsAccount_ownerUnitName").setValue(overseaName);
	        Ext.getCmp("orguse").setValue(node);
	        Ext.getCmp("PartsAccount_useUnitName").setValue(overseaName);
	        //清空规格型号控件的值
	        Ext.getCmp("specificationModel_combo").clear("specificationModel_combo");
	        //清空厂家选择控件的值
	        Ext.getCmp("Factory_Select").clear("Factory_Select");
//            Ext.getCmp("assetsStaus_combo").loadRecord('',Assets_Staus_add,'新购','assetsStaus_combo');//资产状态【新购】
//            Ext.getCmp("warehouseStatus_combo").loadRecord('',WH_STATUS_ALREADY_OUT,'已出库','warehouseStatus_combo');//库存状态【已出库】
//            Ext.getCmp("healthStatus_combo").loadRecord('',Health_Status_OK,'完好','healthStatus_combo');//健康状态【完好】
//            Ext.getCmp("installPartsStatus_combo").loadRecord('',IP_Status_DOWN_PARTS,'已下配件','installPartsStatus_combo');//上配件状态【已下配件】
//            Ext.getCmp("installTrainStatus_combo").loadRecord('',IT_Status_DOWN_CAR,'已下车','installTrainStatus_combo');//上车状态【已下车】
//            Ext.getCmp("turnoverStatus_combo").loadRecord('',Turnover_STATUS_IN_PLANT,'在车间','turnoverStatus_combo');//周转状态【在车间】
            Ext.getCmp("PartsAccount_empid").setValue(empId);//登记人取当前用户信息
            Ext.getCmp("PartsAccount_empname").setValue(empName);
            PartsAccount.enableForm_all();
//           	PartsAccount.disableForm();
        }
    },{
        text:"删除", iconCls:"deleteIcon",
        handler: function(){
            var sm = PartsAccount.grid.getSelectionModel();
            if (sm.getCount() < 1) {
                MyExt.Msg.alert("尚未选择一条记录！");
                return;
            }
            PartsAccount.win.hide();
            Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
                if(btn == "yes"){
                    var data = sm.getSelections();
                    var ids = new Array();
                    for (var i = 0; i < data.length; i++){
                        ids.push(data[ i ].get("idx"));
                    }
                    PartsAccount.win.hide();
                    Ext.Ajax.request({
                        url: ctx + "/partsAccount!logicDelete.action",
                        params: {ids: ids},
                        success: function(response, options){
                            var result = Ext.util.JSON.decode(response.responseText);
                            if (result.errMsg == null) {
                                alertSuccess();
                                PartsAccount.store.reload();    
                            } else {
                                alertFail(result.errMsg);
                            }
                        },
                        failure: function(response, options){
                            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                        }
                    });
                }
            });
        }
    },{
		text:"刷新", iconCls:"refreshIcon", handler:function(){self.location.reload();}
    }],        
    bbar: PartsAccount.pagingToolbar,
    listeners: {
        "rowdblclick": {
            fn: function(grid, idx, e){
            	PartsAccount.searchWin.hide();
                var r = grid.store.getAt(idx);
                PartsAccount.win.setTitle("编辑");
                PartsAccount.win.show();
                PartsAccount.form.getForm().reset();
                PartsAccount.form.getForm().loadRecord(r);
                //回显单位选择控件的值
                var node = {id: 0, text: " "};
	            if (r.get("useUnit") != null)    node = {id: r.get("useUnit"), text: r.get("useUnitName")};
	            Ext.getCmp("orguse").setValue(node);
                Ext.getCmp("PartsAccount_useUnitName").setValue(r.get("useUnitName"));
                var ownerUnitnode = {id: 0, text: " "};
                if (r.get("ownerUnit") != null)    ownerUnitnode = {id: r.get("ownerUnit"), text: r.get("ownerUnitName")};
	            Ext.getCmp("orgown").setValue(ownerUnitnode);
                Ext.getCmp("PartsAccount_ownerUnitName").setValue(r.get("ownerUnitName"));
                //回显规格型号选择控件的值
                Ext.getCmp("specificationModel_combo").loadRecord(null,r.get("partsTypeIDX"),r.get("specificationModel"),'specificationModel_combo');
                Ext.getCmp("PartsAccount_partsTypeIDX").setValue(r.get("partsTypeIDX"));
                Ext.getCmp("PartsAccount_partsName").setValue(r.get("partsName"));
                Ext.getCmp("PartsAccount_professionalTypeName").setValue(r.get("professionalTypeName"));
                Ext.getCmp("PartsAccount_professionalTypeIdx").setValue(r.get("professionalTypeIdx"));
                //回显厂家选择控件的值
                Ext.getCmp("Factory_Select").loadRecord(null,r.get("madeFactoryIdx"),r.get("madeFactoryName"),"Factory_Select");
                Ext.getCmp("PartsAccount_madeFactoryName").setValue(r.get("madeFactoryName"));
                //回显几种状态值
//                var dictName1 = EosDictEntry.getDictname("PJWZ_Parts_Account_Assets_Staus",r.get("assetsStaus"));
//				Ext.getCmp("assetsStaus_combo").loadRecord(null,r.get("assetsStaus"),dictName1,'assetsStaus_combo');//资产状态
//				var dictName2 = EosDictEntry.getDictname("PJWZ_Parts_Account_WareHouse_Status",r.get("warehouseStatus"));
//	            Ext.getCmp("warehouseStatus_combo").loadRecord(null,r.get("warehouseStatus"),dictName2,'warehouseStatus_combo');//库存状态
//	            var dictName3 = EosDictEntry.getDictname("PJWZ_Parts_Account_Health_Status",r.get("healthStatus"));
//	            Ext.getCmp("healthStatus_combo").loadRecord(null,r.get("healthStatus"),dictName3,'healthStatus_combo');//健康状态
//	            var dictName4 = EosDictEntry.getDictname("PJWZ_Parts_Account_Install_Parts_Status",r.get("installPartsStatus"));
//	            Ext.getCmp("installPartsStatus_combo").loadRecord(null,r.get("installPartsStatus"),dictName4,'installPartsStatus_combo');//上配件状态
//	            var dictName5 = EosDictEntry.getDictname("PJWZ_Parts_Account_Install_Train_Status",r.get("installTrainStatus"));
//	            Ext.getCmp("installTrainStatus_combo").loadRecord(null,r.get("installTrainStatus"),dictName5,'installTrainStatus_combo');//上车状态
//	            var dictName6 = EosDictEntry.getDictname("PJWZ_Parts_Account_Turnover_Status",r.get("turnoverStatus"));
//	            Ext.getCmp("turnoverStatus_combo").loadRecord(null,r.get("turnoverStatus"),dictName6,'turnoverStatus_combo');//周转状态
           		//资产状态为调出和报废的，不能编辑值
//           		if(r.get("assetsStaus")==Assets_Staus_SCRAP||r.get("assetsStaus")==Assets_Staus_OUT){
//           			PartsAccount.disableForm_all();
//           		}else {
//           			PartsAccount.enableForm_all();
//           			PartsAccount.disableForm();
//           		}
	        }
        }
    }       
});
//表单字段控件失效
PartsAccount.disableForm = function(){
    Ext.getCmp("PartsAccount_partsName").disable();
    Ext.getCmp("PartsAccount_professionalTypeName").disable();
//    Ext.getCmp("warehouseStatus_combo").disable();
//    Ext.getCmp("healthStatus_combo").disable();
//    Ext.getCmp("installPartsStatus_combo").disable();
//    Ext.getCmp("installTrainStatus_combo").disable();
    Ext.getCmp("partsNo").disable();
    Ext.getCmp("registerTime").disable();
}
//表单字段控件生效
PartsAccount.enableForm = function(){
    Ext.getCmp("PartsAccount_partsName").enable();
    Ext.getCmp("PartsAccount_professionalTypeName").enable();
//    Ext.getCmp("warehouseStatus_combo").enable();
//    Ext.getCmp("healthStatus_combo").enable();
//    Ext.getCmp("installPartsStatus_combo").enable();
//    Ext.getCmp("installTrainStatus_combo").enable();
    Ext.getCmp("partsNo").enable();
    Ext.getCmp("registerTime").enable();
}
//表单所有字段控件失效
PartsAccount.disableForm_all = function(){
	PartsAccount.disableForm();
//    Ext.getCmp("assetsStaus_combo").disable();
//    Ext.getCmp("turnoverStatus_combo").disable();
    Ext.getCmp("specificationModel_combo").disable();
    Ext.getCmp("nameplateNo").disable();
    Ext.getCmp("orgown").disable();
    Ext.getCmp("Factory_Select").disable();
    Ext.getCmp("price").disable();
    Ext.getCmp("useDate").disable();
    Ext.getCmp("fixedAssetsNo").disable();
    Ext.getCmp("fixedAssetsCardno").disable();
    Ext.getCmp("orguse").disable();
    Ext.getCmp("leaveDate").disable();
    Ext.getCmp("registerTime").disable();
    Ext.getCmp("fixedAssetsName").disable();
    Ext.getCmp("remarks").disable();
    Ext.getCmp("accountsave").setVisible(false);
}
//表单所有字段控件生效
PartsAccount.enableForm_all = function(){
	PartsAccount.enableForm();
//    Ext.getCmp("assetsStaus_combo").enable();
//    Ext.getCmp("turnoverStatus_combo").enable();
    Ext.getCmp("specificationModel_combo").enable();
    Ext.getCmp("nameplateNo").enable();
    Ext.getCmp("orgown").enable();
    Ext.getCmp("Factory_Select").enable();
    Ext.getCmp("price").enable();
    Ext.getCmp("useDate").enable();
    Ext.getCmp("fixedAssetsNo").enable();
    Ext.getCmp("fixedAssetsCardno").enable();
    Ext.getCmp("orguse").enable();
    Ext.getCmp("leaveDate").enable();
    Ext.getCmp("registerTime").enable();
    Ext.getCmp("fixedAssetsName").enable();
    Ext.getCmp("remarks").enable();
    Ext.getCmp("accountsave").setVisible(true);
}
//查询参数表单
PartsAccount.searchForm = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "99%" }, autoScroll:true,
    layout: "form",		border: false,		style: "padding:15px",		labelWidth: PartsAccount.labelWidth,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:PartsAccount.labelWidth,
            columnWidth: 1, 
            items: [
				{ name:"partsName",vtype:"validChar", fieldLabel:"配件名称",width: PartsAccount.fieldWidth },
				{ name:"specificationModel",vtype:"validChar",  fieldLabel:"规格型号",width: PartsAccount.fieldWidth },
				{ name:"partsNo",vtype:"validChar",  fieldLabel:"配件编号",width: PartsAccount.fieldWidth },
				{ name:"nameplateNo",vtype:"validChar",  fieldLabel:"铭牌号",width: PartsAccount.fieldWidth }
            ]
        }
        ]
    }]
});
//查询参数对象
PartsAccount.searchParam = {};
//查询窗口
PartsAccount.searchWin = new Ext.Window({
    title:"查询", items:PartsAccount.searchForm,
    width:360, height:240, plain: true, closeAction: "hide",
    buttonAlign:"center",
    buttons: [{
        id: "searchBtn", text: "查询", iconCls: "searchIcon",
        handler: function(){  
		    PartsAccount.searchParam = PartsAccount.searchForm.getForm().getValues();
		    var searchParam = PartsAccount.searchForm.getForm().getValues();
            searchParam = MyJson.deleteBlankProp(searchParam);
		    PartsAccount.store.load({
		        params: {
                    start: 0,    limit: PartsAccount.pagingToolbar.pageSize,
                    entityJson: Ext.util.JSON.encode(searchParam)
                }		    
		    });
        }
    }, {
        text:"重置", iconCls:"resetIcon",
        handler:function(){	
           PartsAccount.searchForm.getForm().reset();
           PartsAccount.grid.store.load();
        //清空资产状态选择控件
       // Ext.getCmp("assetsStaus_search").clear("assetsStaus_search");
        }
    }, {
        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ PartsAccount.searchWin.hide(); }                
    }]
});

//页面自适应布局
var viewport = new Ext.Viewport({ layout:"fit", items:PartsAccount.grid });
});