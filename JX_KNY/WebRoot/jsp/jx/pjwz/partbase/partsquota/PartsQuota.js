/**
 * 互换配件定额 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
//定义命名空间
Ext.namespace("PartsQuota");
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
PartsQuota.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
//表单组件高宽等设置
PartsQuota.labelWidth = 100;
PartsQuota.fieldWidth = 180;
//信息表单
PartsQuota.form = new Ext.form.FormPanel({
    layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: PartsQuota.labelWidth,
    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "98%" },
    items: [{
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [
        	{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: PartsQuota.labelWidth, 	 columnWidth: 1, 
	            items: [
					//{ name: "partsTypeIDX", id: "partsTypeIDX", fieldLabel: "配件型号表主键",  maxLength: 50,  width: PartsQuota.fieldWidth },
					{ name: "partsName", id: "partsName", fieldLabel: "配件名称",  maxLength: 100,  width: PartsQuota.fieldWidth },
					{ name: "specificationModel", id: "specificationModel", fieldLabel: "规格型号",  maxLength: 100,  width: PartsQuota.fieldWidth },
					{ name: "unit", id: "unit", fieldLabel: "计量单位",maxLength: 10,  width: PartsQuota.fieldWidth },
					{ name: "ownerUnitName", id: "ownerUnitName", fieldLabel: "所属单位", xtype: "hidden",  maxLength: 32,  width: PartsQuota.fieldWidth },
					{ name: "limitQuantity", fieldLabel: "定量",vtype: "positiveInt", maxLength: 8,  width: PartsQuota.fieldWidth }
	            ]
        	},
	        {xtype: "hidden", name: "idx"},
	        {xtype: "hidden", name: "partsTypeIDX"},
	        {xtype: "hidden", name: "ownerUnit",id: "ownerUnit"}
        ]
    }]
});
//编辑窗口
PartsQuota.win = new Ext.Window({
    maximizable: true, 		width: 350, 	height: 230,
    plain: true,   	closeAction: "hide", 	items: PartsQuota.form,
    buttonAlign: "center",
    buttons: [{
        text: "保存", iconCls: "saveIcon",
        handler: function(){
            var form = PartsQuota.form.getForm();
            if (!form.isValid()) return;
            var url = ctx + "/partsQuota!saveOrUpdate.action";
            PartsQuota.enableForm();
            var data = form.getValues();
            PartsQuota.disableForm();
            PartsQuota.loadMask.show();
            Ext.Ajax.request({
                url: url,
                jsonData: data,
                success: function(response, options){
                  	PartsQuota.loadMask.hide();
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        alertSuccess();
                        PartsQuota.store.reload();
                    } else {
                        alertFail(result.errMsg);
                    }
                },
                failure: function(response, options){
                    PartsQuota.loadMask.hide();
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
        }
    },{
    	 text:"关闭", iconCls:"closeIcon",handler:function(){
    	 	PartsQuota.win.hide();
    	 }
    }]
});
//批量设置定额表单
PartsQuota.setForm = new Ext.form.FormPanel({
    layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: PartsQuota.labelWidth,
    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "98%" },
    items: [{
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [
        	{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: PartsQuota.labelWidth, 	 columnWidth: 1, 
	            items: [
					{ name: "limitQuantity", fieldLabel: "定量",vtype: "positiveInt", maxLength: 8,  width: PartsQuota.fieldWidth }
	            ]
        	}
        ]
    }]
});
//批量设置定额窗口
PartsQuota.setWin = new Ext.Window({
    title: "批量设置",maximizable: true, 		width: 350, 	height: 120,
    plain: true,   	closeAction: "hide", 	items: PartsQuota.setForm,
    buttonAlign: "center",
    buttons: [{
        text: "保存", iconCls: "saveIcon",
        handler: function(){
            var form = PartsQuota.setForm.getForm();
            if (!form.isValid()) return;
            var data_set = form.getValues();
            var sm = PartsQuota.grid.getSelectionModel();
            var data = sm.getSelections();
            var dataAry = new Array();
            for (var i = 0; i < data.length; i++){
            	var data_v = data[ i ].data;
                data_v.limitQuantity = data_set.limitQuantity;
                dataAry.push(data_v);
            }
            PartsQuota.loadMask.show();
            Ext.Ajax.request({
                url: ctx + '/partsQuota!saveOrUpdateList.action',
                jsonData: dataAry,
                success: function(response, options){
                  	PartsQuota.loadMask.hide();
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        alertSuccess();
                        PartsQuota.store.load();
                    } else {
                        alertFail(result.errMsg);
                    }
                },
                failure: function(response, options){
                    PartsQuota.loadMask.hide();
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
        }
    },{
    	 text:"关闭", iconCls:"closeIcon",handler:function(){
    	 	PartsQuota.setWin.hide();
    	 }
    }]
});
//规格型号选择窗口
PartsType.selectWin = new Ext.Window({
    title: "配件规格型号选择", layout: "fit", width: 500, height: 360, modal: true, 
    closeAction: "hide", items: PartsType.grid,layout: "fit"
});
//数据容器
PartsQuota.store = new Ext.data.JsonStore({
	id: "idx", root: "root", totalProperty: "totalProperty", autoLoad: true,
    url: ctx + "/partsQuota!pageList.action",
    fields: [ "partsTypeIDX","partsName","unit","specificationModel","ownerUnit","ownerUnitName","limitQuantity","idx",{name:"updateTime", type:"date", dateFormat: 'time'},"updatorName" ]
});
//选择模式，勾选框可多选
PartsQuota.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
//分页工具
PartsQuota.pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsQuota.store});
//页面列表
PartsQuota.grid = new Ext.grid.GridPanel({
    border: false, 		enableColumnMove: true, 	stripeRows: true,
    //随着窗口（父容器）大小自动调整,true表示不出现滚动条，列宽会自动压缩
    viewConfig: {forceFit: true},
    //多选行
    selModel: PartsQuota.sm,
    colModel: new Ext.grid.ColumnModel([
        PartsQuota.sm,
        new Ext.grid.RowNumberer(),
        { sortable: true, header: "配件名称",  dataIndex: "partsName" },			
        { sortable: true, header: "规格型号",  dataIndex: "specificationModel" },	
        { sortable: true, header: "计量单位",  dataIndex: "unit" },	
        { sortable: true, header: "所属单位", hidden:true,  dataIndex: "ownerUnitName" },	
        { sortable: true, header:"定量", dataIndex:"limitQuantity"},	
        { sortable: true, header:"修改人", dataIndex:"updatorName"},	
        { sortable: true, header:"修改日期",xtype:'datecolumn', dataIndex:"updateTime", format: 'Y-m-d'}
//        { sortable: true, header: "定额",  dataIndex: "limitQuantity" }			
    ]),
    store: PartsQuota.store,					//数据容器
    tbar: [{
        text: "查询", iconCls: "searchIcon", handler: function(){
        	PartsQuota.setWin.hide();
        	PartsQuota.searchWin.show(); 
       	}
    },{								//工具栏
        text: "新增", iconCls: "addIcon", handler: function(){
        	PartsQuota.searchWin.hide();
            PartsType.selectWin.show();
//            PartsQuota.form.getForm().reset();
//            PartsQuota.win.setIconClass("addIcon");
//            PartsQuota.win.setTitle("新增");
//            PartsQuota.win.show();
//            PartsQuota.enableForm();
        }
    },{  text: "批量设置", iconCls: "configIcon", handler: function(){
        	var sm = PartsQuota.grid.getSelectionModel();
            if (sm.getCount() < 1) {
                MyExt.Msg.alert("尚未选择一条记录！");
                return;
            }
            PartsQuota.setWin.show();
        }
    },
//    	{
//    	text: "保存", iconCls: "saveIcon", handler: function(){
//    		var record = PartsQuota.store.getModifiedRecords();
//    		var datas = new Array();
//    		for (var i = 0; i < record.length; i++) {
//    			var data = {} ;
//    			data = record[i].data;
//    			datas.push(data);
//    		}
//    		PartsQuota.loadMask.show();
//            Ext.Ajax.request({
//                url: ctx + '/partsQuota!saveOrUpdateList.action',
//                jsonData: datas,
//                success: function(response, options){
//                  	PartsQuota.loadMask.hide();
//                    var result = Ext.util.JSON.decode(response.responseText);
//                    if (result.errMsg == null) {
//                        alertSuccess();
//                        PartsQuota.store.load();
//                    } else {
//                        alertFail(result.errMsg);
//                    }
//                },
//                failure: function(response, options){
//                    PartsQuota.loadMask.hide();
//                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
//                }
//            });
//    	}
//    },
    	{
        text: "删除", iconCls: "deleteIcon",
        handler: function(){
            var sm = PartsQuota.grid.getSelectionModel();
            if (sm.getCount() < 1) {
                MyExt.Msg.alert("尚未选择一条记录！");
                return;
            }
            PartsQuota.setWin.hide();
            Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
                if(btn == "yes"){
                    var data = sm.getSelections();
                    var ids = new Array();
                    for (var i = 0; i < data.length; i++){
                        ids.push(data[ i ].get("idx"));
                    }
                    PartsQuota.setWin.hide();
                    Ext.Ajax.request({
                        url: ctx + "/partsQuota!logicDelete.action",
                        params: {ids: ids},
                        success: function(response, options){
                            var result = Ext.util.JSON.decode(response.responseText);
                            if (result.errMsg == null) {
                                alertSuccess();
                                PartsQuota.store.reload();    
                                PartsType.store.reload();
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
		text: "刷新", iconCls: "refreshIcon", handler: function(){self.location.reload();}
    }],
    bbar: PartsQuota.pagingToolbar,
    listeners: {
        "rowdblclick": {
            fn: function(grid, idx, e){
            	PartsQuota.searchWin.hide();
                var r = grid.store.getAt(idx);
                PartsQuota.win.setTitle("编辑");
                PartsQuota.win.show();
                PartsQuota.form.getForm().reset();
                PartsQuota.form.getForm().loadRecord(r);
                PartsQuota.disableForm();
            }
        }
    }       
});
//表单字段控件失效
PartsQuota.disableForm = function(){
    //Ext.getCmp("partsTypeIDX").disable();
    Ext.getCmp("partsName").disable();
    Ext.getCmp("specificationModel").disable();
    Ext.getCmp("unit").disable();
    Ext.getCmp("ownerUnitName").disable();
}
//表单字段控件生效
PartsQuota.enableForm = function(){
   // Ext.getCmp("partsTypeIDX").enable();
    Ext.getCmp("partsName").enable();
    Ext.getCmp("specificationModel").enable();
    Ext.getCmp("unit").enable();
    Ext.getCmp("ownerUnitName").enable();
}
//查询参数表单
PartsQuota.searchForm = new Ext.form.FormPanel({
    layout: "form", 	border: false,  	style: "padding:10px", 		labelWidth: PartsQuota.labelWidth,
    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },    
    items: [{
        xtype: "panel", border: false, baseCls: "x-plain", layout: "column", align: "center", 
        items: [
        {
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: PartsQuota.labelWidth, 	 columnWidth: 1, 
            items: [
				{ name: "partsName",vtype:"validChar",  fieldLabel: "配件名称", width: PartsQuota.fieldWidth },
				{ name: "specificationModel",vtype:"validChar",  fieldLabel: "规格型号", width: PartsQuota.fieldWidth },
//				{ name: "ownerUnitName", fieldLabel: "所属单位名称", width: PartsQuota.fieldWidth },
				{ name: "limitQuantity",vtype:"validChar",  fieldLabel: "定量",xtype: "numberfield", width: PartsQuota.fieldWidth }
            ]
        }
        ]
    }]
});
//查询参数对象
PartsQuota.searchParam = {};
//查询窗口
PartsQuota.searchWin = new Ext.Window({
    title: "查询", 	width: 350, 	height: 200, 
    plain: true, 		closeAction: "hide", 	items: PartsQuota.searchForm, 
    buttonAlign:"center",
    buttons: [{
        id: "searchBtn", text: "查询", iconCls: "searchIcon", handler: function(){ 
		    PartsQuota.searchParam = PartsQuota.searchForm.getForm().getValues();
		    var searchParam = PartsQuota.searchForm.getForm().getValues();
		    searchParam = MyJson.deleteBlankProp(searchParam);
		    PartsQuota.store.load({
		        params: {
                    start: 0,    limit: PartsQuota.pagingToolbar.pageSize,
                    entityJson: Ext.util.JSON.encode(searchParam)
                }	    
		    });
        }
    }, {
        text: "重置", iconCls: "resetIcon", handler: function(){ 
        	PartsQuota.searchForm.getForm().reset();
        	PartsQuota.searchParam = {};
        	PartsQuota.store.load();
        }
    }, {
        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ PartsQuota.searchWin.hide(); }                
    }]
});
////配件规格型号命名空间， 数据容器
//Ext.namespace("PartsType");
////数据容器
//PartsType.store = new Ext.data.JsonStore({
//    id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:true,
//    url: ctx + "/partsType!list.action",
//    fields: [ "specificationModel","professionalTypeName","partsName","unit","timeLimit","limitKm","limitYears","status","recordStatus","siteID","creator","createTime","updator","updateTime","idx","partsClassIdx","className" ]
//});
//PartsType.pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsType.store});
////选择模式，勾选框可多选
//PartsType.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
//PartsType.grid = new Ext.grid.GridPanel({
//    border: false, enableColumnMove: true, stripeRows: true, selModel: PartsType.sm, viewConfig: {forceFit: true},
//    colModel: new Ext.grid.ColumnModel([
//        PartsType.sm,
//        new Ext.grid.RowNumberer(),
//        { sortable:true, header:"配件名称", dataIndex:"partsName" },
//        { sortable:true, header:"规格型号", dataIndex:"specificationModel" },           
//        { sortable:true, header:"配件分类", dataIndex:"className" },         
//        { sortable:true, header:"专业类型", dataIndex:"professionalTypeName" }  
//    ]),
//    store: PartsType.store,
//    tbar: [{
//        xtype:"label", text:"  配件名称： " 
//    },{
//        xtype: "textfield"
//    },{
//        text:"查询", iconCls:"searchIcon", handler: function(){
//            alert("功能未实现");
//        }        
//    },"-",{
//        text:"确定", iconCls:"saveIcon", handler: function(){
//            alert("从列表中勾选多项，点击确定按钮后，自动生成配件定额记录。");
//        }
//    }],
//    bbar: PartsType.pagingToolbar 
//});
PartsQuota.store.on("beforeload", function(){
	this.baseParams.entityJson = Ext.util.JSON.encode(PartsQuota.searchParam);  
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:"fit", items:PartsQuota.grid });
});