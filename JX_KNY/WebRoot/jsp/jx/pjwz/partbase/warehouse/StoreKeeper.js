/**
 * 库管员 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
//定义命名空间
Ext.namespace("StoreKeeper");
Ext.namespace("Employee");
//数据容器
Employee.store = new Ext.data.JsonStore({
	id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:true,
    url: ctx + "/omEmployeeSelect!pageListForKeeper.action",
    fields: [ "empid","empcode","empname" ]
});
//选择模式，勾选框可多选
Employee.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
//分页工具
Employee.pagingToolbar = Ext.yunda.createPagingToolbar({store: Employee.store});
//人员列表
Employee.grid = new Ext.grid.GridPanel({
    border: false,
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
    selModel: Employee.sm,
    colModel: new Ext.grid.ColumnModel([
        Employee.sm,
        new Ext.grid.RowNumberer(),
//        { sortable:true, header:"库房主键", dataIndex:"warehouseIDX" },			
//        { sortable:true, header:"库房代码", dataIndex:"warehouseID" },			
//        { sortable:true, header:"库房名称", dataIndex:"warehouseName" },			
//        { sortable:true, header:"组织人员主键", dataIndex:"empID" },			
        { sortable:true, header:"人员编码", dataIndex:"empcode" },			
        { sortable:true, header:"人员名称", dataIndex:"empname" }			
    ]),
    store: Employee.store,
    //工具栏
    tbar: [{
        xtype:"label", text:"人员名称"
    },{
         xtype: "textfield" , id:"empname" , name:"empname"
    },{
        text:"查询", iconCls:"searchIcon",
        handler: function(){
           Employee.store.load();
        }
    },"-",{
       text:"确定", iconCls:"saveIcon",
        handler: function(){
        	var sm = Employee.grid.getSelectionModel();
            if (sm.getCount() < 1) {
                MyExt.Msg.alert("尚未选择一条记录！");
                return;
            }
            var empData =  sm.getSelections();
            var dataAry = new Array();
            for (var i = 0; i < empData.length; i++){
            	var data = {};
                data.empID = empData[ i ].get("empid");
                data.empCode = empData[ i ].get("empcode");
                data.empName = empData[ i ].get("empname");
                data.warehouseIDX = WarehouseLocation.wareHouseIDX ; //前台组织数据
				data.warehouseID = WarehouseLocation.wareHouseID ;
				data.warehouseName = WarehouseLocation.wareHouseName ;
                dataAry.push(data);
            }
            StoreKeeper.loadMask.show();
            Ext.Ajax.request({
                url:  ctx + "/storeKeeper!saveOrUpdateBatch.action",
                jsonData: dataAry,
                success: function(response, options){
                  	StoreKeeper.loadMask.hide();
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        alertSuccess();
                        StoreKeeper.store.reload();
                        Employee.store.reload();
                    } else {
                        alertFail(result.errMsg);
                    }
                },
                failure: function(response, options){
                    StoreKeeper.loadMask.hide();
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
        }
    }],
    bbar: Employee.pagingToolbar
});
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
StoreKeeper.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
//表单组件高宽等设置
StoreKeeper.labelWidth = 100;
StoreKeeper.fieldWidth = 180;
//信息表单
StoreKeeper.form = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,		style: "padding:10px",		labelWidth: StoreKeeper.labelWidth,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:StoreKeeper.labelWidth,
            columnWidth: 1, 
            items: [
            	{id:"emp", xtype:'OmEmployee_SelectWin',editable:false,fieldLabel : "选择人员",hiddenName:'empID',width:StoreKeeper.fieldWidth,
            		returnField:[{widgetId:"empCode",propertyName:"empcode"},{widgetId:"empName",propertyName:"empname"}]
            	},
//				{ name:"empID", fieldLabel:"人员IDX", xtype: "numberfield" , maxLength:10,width:StoreKeeper.fieldWidth },
				{ name:"empCode", fieldLabel:"人员代码",maxLength:15,width:StoreKeeper.fieldWidth },
				{ id:"empName",name:"empName", fieldLabel:"人员名称",maxLength:25,width:StoreKeeper.fieldWidth }
            ]
        },
        {xtype:"hidden", name:"idx"}
        ]
    }]
});
//新增编辑窗口
StoreKeeper.win = new Ext.Window({
    title:"选择人员信息", maximizable:true, width:520, height:280, 
    plain:true, closeAction:"hide", modal:true,layout:"fit",
//    items:StoreKeeper.form,
    items:Employee.grid
//    buttons: [{
//        text:"保存", iconCls:"saveIcon",
//        handler:function(){
//            var form = StoreKeeper.form.getForm();
//            if (!form.isValid()) return;
//            var url = ctx + "/storeKeeper!saveOrUpdate.action";
//            var data = form.getValues();
//            data.warehouseIDX = Warehouse.wareHouseIDX ; //前台组织数据
//			data.warehouseID = Warehouse.wareHouseID ;
//			data.warehouseName = Warehouse.wareHouseName ;
//            StoreKeeper.loadMask.show();
//            Ext.Ajax.request({
//                url: url,
//                jsonData: data,
//                success: function(response, options){
//                  	StoreKeeper.loadMask.hide();
//                    var result = Ext.util.JSON.decode(response.responseText);
//                    if (result.errMsg == null) {
//                        alertSuccess();
//                        StoreKeeper.store.reload();
//                    } else {
//                        alertFail(result.errMsg);
//                    }
//                },
//                failure: function(response, options){
//                    StoreKeeper.loadMask.hide();
//                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
//                }
//            });
//        }
//    }
//    ]
});
//数据容器
StoreKeeper.store = new Ext.data.JsonStore({
	id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:true,
    url: ctx + "/storeKeeper!list.action",
    fields: [ "warehouseIDX","warehouseID","warehouseName","empID","empCode","empName","idx" ]
});
//选择模式，勾选框可多选
StoreKeeper.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
//分页工具
StoreKeeper.pagingToolbar = Ext.yunda.createPagingToolbar({store: StoreKeeper.store});
//人员列表
StoreKeeper.grid = new Ext.grid.GridPanel({
    border: false,
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
    selModel: StoreKeeper.sm,
    colModel: new Ext.grid.ColumnModel([
        StoreKeeper.sm,
        new Ext.grid.RowNumberer(),
//        { sortable:true, header:"库房主键", dataIndex:"warehouseIDX" },			
//        { sortable:true, header:"库房代码", dataIndex:"warehouseID" },			
//        { sortable:true, header:"库房名称", dataIndex:"warehouseName" },			
//        { sortable:true, header:"组织人员主键", dataIndex:"empID" },			
        { sortable:true, header:"人员编码", dataIndex:"empCode" },			
        { sortable:true, header:"人员名称", dataIndex:"empName" }			
    ]),
    store: StoreKeeper.store,
    //工具栏
    tbar: [{
        text:"新增", iconCls:"addIcon",
        handler: function(){
//        	StoreKeeper.searchWin.hide();
            StoreKeeper.form.getForm().reset();
            StoreKeeper.win.setIconClass("addIcon");
            StoreKeeper.win.setTitle("新增");
            var node = {empid: 0, empname: " "};
            var p_record = new Ext.data.Record(node);
            Ext.getCmp("emp").setValue(p_record);
            StoreKeeper.win.show();
            Employee.store.load();
            
        }
    },"-",{
        text:"删除", iconCls:"deleteIcon",
        handler: function(){
            var sm = StoreKeeper.grid.getSelectionModel();
            if (sm.getCount() < 1) {
                MyExt.Msg.alert("尚未选择一条记录！");
                return;
            }
            StoreKeeper.win.hide();
            Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
                if(btn == "yes"){
                    var data = sm.getSelections();
                    var ids = new Array();
                    for (var i = 0; i < data.length; i++){
                        ids.push(data[ i ].get("idx"));
                    }
                    StoreKeeper.win.hide();
                    Ext.Ajax.request({
                        url: ctx + "/storeKeeper!logicDelete.action",
                        params: {ids: ids},
                        success: function(response, options){
                            var result = Ext.util.JSON.decode(response.responseText);
                            if (result.errMsg == null) {
                                alertSuccess();
                                StoreKeeper.store.reload();    
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
//    },"-",{
//        text:"查询", iconCls:"searchIcon", handler: function(){
//        	StoreKeeper.win.hide();
//        	StoreKeeper.searchWin.show(); 
//       	}
//    },"-",{
//		text:"刷新", iconCls:"refreshIcon", handler:function(){self.location.reload();}
    }],
    bbar: StoreKeeper.pagingToolbar
//    listeners: {
//        "rowdblclick": {
//            fn: function(grid, idx, e){
////            	StoreKeeper.searchWin.hide();
//                var r = grid.store.getAt(idx);
//                StoreKeeper.win.setIconClass("edit1");
//                StoreKeeper.win.setTitle("编辑");
//                StoreKeeper.win.show();
//                StoreKeeper.form.getForm().reset();
//                //回显人员选择控件的值
//                var node = {empid: 0, empname: " "};
//	            if (r.get("empID") != null)    node = {empid: r.get("empID"), empname: r.get("empName")};
//	            var p_record = new Ext.data.Record(node);
//	            Ext.getCmp("emp").setValue(p_record);
//	            StoreKeeper.form.getForm().loadRecord(r);
////                Ext.getCmp("empname").setValue(r.get("empname"));//返回隐藏域值
//            }
//        }
//    }       
});

//store载入前查询
StoreKeeper.store.on("beforeload", function(){
	//定义查询条件
	var searchParam = {};
	searchParam = {warehouseIDX:WarehouseLocation.wareHouseIDX}; //获取库房IDX作为库位查询条件
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

Employee.store.on("beforeload", function(){
	//定义查询条件
	var empname = Ext.getCmp("empname").getValue();
	this.baseParams.emp = empname;
	this.baseParams.wareHouseIDX = WarehouseLocation.wareHouseIDX; //库房
	this.baseParams.keeper ='1';
});

});