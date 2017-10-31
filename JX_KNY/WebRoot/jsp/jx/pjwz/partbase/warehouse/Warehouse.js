/**
 * 库房 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
//定义命名空间
Ext.namespace("Warehouse");
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
Warehouse.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
//表单组件高宽等设置
Warehouse.labelWidth = 180;
Warehouse.fieldWidth = 260;
//信息表单
Warehouse.form = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,		style: "padding:10px",		labelWidth: Warehouse.labelWidth,
    buttonAlign: "center",
    buttons: [{
        id:"submitBtn", text:"保存", iconCls:"saveIcon",
        handler:function(){
            var form = Warehouse.form.getForm();
            if (!form.isValid()) return;
            var url = ctx + "/warehouse!saveOrUpdate.action";
            var data = form.getValues();
            if(data.status == ""){
            	data.status = 0 ;
            }
            Warehouse.loadMask.show();
            Ext.Ajax.request({
                url: url,
                jsonData: data,
                success: function(response, options){
                    Warehouse.loadMask.hide();
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        alertSuccess();
                        Warehouse.store.reload();
                        Warehouse.tabs.activate(1);
		                Warehouse.tabs.unhideTabStripItem(1);
						Warehouse.tabs.unhideTabStripItem(2); 
                        WarehouseLocation.wareHouseIDX = result.entity.idx ;
						WarehouseLocation.wareHouseID = result.entity.wareHouseID ;
						WarehouseLocation.wareHouseName = result.entity.wareHouseName ;
						WarehouseLocation.grid.store.load();
                    } else {
                        alertFail(result.errMsg);
                    }
                },
                failure: function(response, options){
                    Warehouse.loadMask.hide();
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
        }
    },{
    	 text:"关闭", iconCls:"closeIcon",handler:function(){
    	 	Warehouse.win.hide();
    	 }
    }],    
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:Warehouse.labelWidth,
            columnWidth: 1, 
            items: [
				{id:"wareHouseID", name:"wareHouseID", fieldLabel:"库房编码",maxLength:50, allowBlank:false,width:Warehouse.fieldWidth },
				{id:"wareHouseName", name:"wareHouseName", fieldLabel:"库房名称", maxLength:50,allowBlank:false,width:Warehouse.fieldWidth },
				{id:"wareHouseAddress", name:"wareHouseAddress", fieldLabel:"库房地点",maxLength:100,width:Warehouse.fieldWidth },
				/*{id:"orgID", name:"orgID", fieldLabel:"所属部门的组织主键", xtype: "numberfield" , maxLength:10,allowBlank:false,width:Warehouse.fieldWidth },
				{id:"orgCode", name:"orgCode", fieldLabel:"所属部门的组织代码",maxLength:15,width:Warehouse.fieldWidth },
				{id:"orgName", name:"orgName", fieldLabel:"所属部门的组织名称",maxLength:25,width:Warehouse.fieldWidth }*/
				
				{ id:"org",xtype: 'OmOrganizationCustom_comboTree',hiddenName: 'orgID',allowBlank:false,
					returnField:[{widgetId:"orgCode",propertyName:"orgcode"}, 
					{widgetId:"orgName",propertyName:"orgname"}],
					fullNameDegree: "plant",
					selectNodeModel:'exceptRoot',width:Warehouse.fieldWidth
				}

            ]
        },
        {xtype:"hidden", name:"status", value:"0"},
        {xtype:"hidden", id:"idx", name:"idx"},
        {xtype:"hidden",id:"orgCode", name:"orgCode"},
		{xtype:"hidden",id:"orgName", name:"orgName"}

        ]
    }]
});

//数据容器
Warehouse.store = new Ext.data.JsonStore({
	id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:true,
    url: ctx + "/warehouse!pageList.action",
    fields: [ "idx","wareHouseID","wareHouseName","wareHouseAddress","orgID","orgCode","orgName","status" ]
});
//选择模式，勾选框可多选
Warehouse.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
//分页工具
Warehouse.pagingToolbar = Ext.yunda.createPagingToolbar({store: Warehouse.store});
//库房列表
Warehouse.grid = new Ext.grid.GridPanel({
    border: false,loadMask:true,
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
    selModel: Warehouse.sm,
    colModel: new Ext.grid.ColumnModel([
        Warehouse.sm,
        new Ext.grid.RowNumberer(),
        { sortable:true, header:"库房编码", dataIndex:"wareHouseID" },			
        { sortable:true, header:"库房名称", dataIndex:"wareHouseName" },			
        { sortable:true, header:"库房地点", dataIndex:"wareHouseAddress" },			
        { sortable:true, header:"所属部门", dataIndex:"orgName" },			
        { sortable:true, header:"状态", dataIndex:"status", renderer:JX.getBizStatus }			
    ]),
    store: Warehouse.store,
    //工具栏
    tbar: [{
        text:"查询", iconCls:"searchIcon", handler: function(){
        	Warehouse.win.hide();
        	Warehouse.searchWin.show(); 
       	}
    },'-',{
        text:"新增", iconCls:"addIcon", handler: function(){
        	Warehouse.searchWin.hide();
            Warehouse.form.getForm().reset();
    		//清空单位选择控件的值
            var node = {id: 0, text: " "};
	        Ext.getCmp("org").setValue(node);
//            Warehouse.win.setIconClass("addIcon");
            Warehouse.win.setTitle("新增");
            Warehouse.win.show();
            Ext.getCmp("wareHouseID").setDisabled(false);
        	Ext.getCmp("wareHouseName").setDisabled(false);
        	Ext.getCmp("wareHouseAddress").setDisabled(false);
        	Ext.getCmp("org").setDisabled(false);
        	Ext.getCmp("orgCode").setDisabled(false);
        	Ext.getCmp("orgName").setDisabled(false);
        	Ext.getCmp("submitBtn").setVisible(true);
        	var node = {id: orgId, text: orgName};
	        Ext.getCmp("org").setValue(node);
//        	Ext.getCmp("org").loadRecord(null,orgId,orgName,"org");
        	Ext.getCmp("orgCode").setValue(orgCode);
        	Ext.getCmp("orgName").setValue(orgName);
            Warehouse.tabs.activate(0); //show后隐藏
			Warehouse.tabs.hideTabStripItem(1);
			Warehouse.tabs.hideTabStripItem(2);    
        }
    },{
        text:"启用", iconCls:"acceptIcon",
        handler: function(){
        	var sm = Warehouse.grid.getSelectionModel();
            if (sm.getCount() < 1) {
                MyExt.Msg.alert("尚未选择一条记录！");
                return;
            }
            var data = sm.getSelections();
            var ids = new Array();
            var flag = new Array(); //标记选择项目
            for (var i = 0; i < data.length; i++){
            	if(data[i].data.status==0){
	                ids.push(data[ i ].get("idx"));
            	}else{
            		 flag.push(data[i]);
            	}
            }
            if(ids.length <= 0){
            	MyExt.Msg.alert("尚未选择一条可启用的记录！</br>【新增】状态记录可启用！");
            	return;
            }
			Ext.Msg.confirm('提示',Warehouse.alertOperate(flag,'是否继续【启用】，新增的项！','start'), function(btn){            	
                if(btn == "yes"){
                    Warehouse.win.hide();
                    Ext.Ajax.request({
                        url: ctx + "/warehouse!updateStart.action",
                        params: {ids: ids , flag:"start"},
                        success: function(response, options){
                            var result = Ext.util.JSON.decode(response.responseText);
                            if (result.errMsg == null) {
                                alertSuccess();
                                Warehouse.store.reload();  
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
        text:"作废", iconCls:"dustbinIcon",
        handler: function(){
        	var sm = Warehouse.grid.getSelectionModel();
            if (sm.getCount() < 1) {
                MyExt.Msg.alert("尚未选择一条记录！");
                return;
            }
            var data = sm.getSelections();
            var ids = new Array();
            var flag = new Array(); //标记选择的项目
            for (var i = 0; i < data.length; i++){
            	if(data[i].data.status==1){
	                ids.push(data[ i ].get("idx"));
            	}else{
            		 flag.push(data[i]);
            	}
            }
            if(ids.length <= 0){
            	MyExt.Msg.alert("尚未选择一条可作废的记录！</br>【启用】状态记录可作废！");
            	return;
            }
			Ext.Msg.confirm('提示',Warehouse.alertOperate(flag,'是否继续【作废】，启用的项！','invalid'), function(btn){            	
                if(btn == "yes"){
                    Warehouse.win.hide();
                    Ext.Ajax.request({
                        url: ctx + "/warehouse!updateStart.action",
                        params: {ids: ids , flag:"invalid"},
                        success: function(response, options){
                            var result = Ext.util.JSON.decode(response.responseText);
                            if (result.errMsg == null) {
                                alertSuccess();
                                Warehouse.store.reload();  
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
        text:"删除", iconCls:"deleteIcon",
        handler: function(){
             var sm = Warehouse.grid.getSelectionModel();
            if (sm.getCount() < 1) {
                MyExt.Msg.alert("尚未选择一条记录！");
                return;
            }
            var data = sm.getSelections();
            var ids = new Array();
            var flag = new Array(); //标记选择的项目
            for (var i = 0; i < data.length; i++){
            	if(data[i].data.status==0){
	                ids.push(data[ i ].get("idx"));
            	}else{
            		 flag.push(data[i]);
            	}
            }
            if(ids.length <= 0){
            	MyExt.Msg.alert("尚未选择一条可删除的记录！</br>【新增】状态记录可删除！");
            	return;
            }
			Ext.Msg.confirm('提示',Warehouse.alertOperate(flag,'是否继续【删除】，新增的项！','del'), function(btn){            	
                if(btn == "yes"){
                    Warehouse.win.hide();
                    Ext.Ajax.request({
                        url: ctx + "/warehouse!logicDelete.action",
                        params: {ids: ids},
                        success: function(response, options){
                            var result = Ext.util.JSON.decode(response.responseText);
                            if (result.errMsg == null) {
                                alertSuccess();
                                Warehouse.store.reload();  
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
        text:"刷新", iconCls:"refreshIcon",handler: function(){self.location.reload();}
    },"-","状态： ",{   
        xtype:"checkbox",id:"addStatus", boxLabel:"新增&nbsp;&nbsp;&nbsp;&nbsp;", checked:true, handler:function(){
            Warehouse.checkQuery();
    }},{   
        xtype:"checkbox",id:"startStatus", boxLabel:"启用&nbsp;&nbsp;&nbsp;&nbsp;", checked:true,handler:function(){
            Warehouse.checkQuery();
    }},{   
        xtype:"checkbox",id:"trashStatus", boxLabel:"作废",handler:function(){
            Warehouse.checkQuery();
    }}],
    bbar: Warehouse.pagingToolbar,
    listeners: {
        "rowdblclick": {
            fn: function(grid, idx, e){
            	Warehouse.searchWin.hide();
                var r = grid.store.getAt(idx);
                if(r.get("status")==2){
                	MyExt.Msg.alert("已经【作废】的项，不能再操作！");
                	return;
                }
                if(r.get("status")==1){
                	Ext.getCmp("wareHouseID").setDisabled(true);
                	Ext.getCmp("wareHouseName").setDisabled(true);
                	Ext.getCmp("wareHouseAddress").setDisabled(true);
                	Ext.getCmp("org").setDisabled(true);
                	Ext.getCmp("orgCode").setDisabled(true);
                	Ext.getCmp("orgName").setDisabled(true);
                	Ext.getCmp("submitBtn").setVisible(false);
                }else{
                	Ext.getCmp("wareHouseID").setDisabled(false);
                	Ext.getCmp("wareHouseName").setDisabled(false);
                	Ext.getCmp("wareHouseAddress").setDisabled(false);
                	Ext.getCmp("org").setDisabled(false);
                	Ext.getCmp("orgCode").setDisabled(false);
                	Ext.getCmp("orgName").setDisabled(false);
                	Ext.getCmp("submitBtn").setVisible(true);
                }
                WarehouseLocation.wareHouseIDX = r.get("idx") ;
				WarehouseLocation.wareHouseID = r.get("wareHouseID") ;
				WarehouseLocation.wareHouseName = r.get("wareHouseName") ;
//                Warehouse.win.setIconClass("edit1");
                Warehouse.win.setTitle("编辑");
                Warehouse.win.show();
				Warehouse.tabs.activate(0); //show后隐藏
                Warehouse.tabs.unhideTabStripItem(1);
				Warehouse.tabs.unhideTabStripItem(2);  
                Warehouse.form.getForm().reset();
                Warehouse.form.getForm().loadRecord(r);
                //回显单位选择控件的值
                var node = {id: 0, text: " "};
	            if (r.get("orgID") != null)    node = {id: r.get("orgID"), text: r.get("orgName")};
	            Ext.getCmp("org").setValue(node);
                Ext.getCmp("orgName").setValue(r.get("orgName"));
                Ext.getCmp("orgCode").setValue(r.get("orgCode"));
                WarehouseLocation.grid.store.load();
                StoreKeeper.store.reload();
            }
        }
    }       
});

//查询参数表单
Warehouse.searchForm = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,		style: "padding:10px",		labelWidth: Warehouse.labelWidth,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:100,
            columnWidth: 1, 
            items: [
				{ name:"wareHouseID",vtype:"validChar", fieldLabel:"库房编码",width: Warehouse.fieldWidth },
				{ name:"wareHouseName",vtype:"validChar", fieldLabel:"库房名称",width: Warehouse.fieldWidth },
				{ name:"wareHouseAddress",vtype:"validChar", fieldLabel:"库房地点",width: Warehouse.fieldWidth }
//				{ name:"orgName", fieldLabel:"所属部门",width: Warehouse.fieldWidth }
            ]
        }
        ]
    }]
});
//查询参数对象
Warehouse.searchParam = {};
//查询窗口
Warehouse.searchWin = new Ext.Window({
    title:"查询数据", items:Warehouse.searchForm, iconCls:"searchIcon",
    width: 430, height: 160, plain: true, closeAction: "hide", 
    buttonAlign:'center',//modal:true,
    buttons: [{
        text: "查询", iconCls: "searchIcon",
        handler: function(){  
		    Warehouse.searchParam = Warehouse.searchForm.getForm().getValues();
		    var searchParam = Warehouse.searchForm.getForm().getValues();
		    Warehouse.store.load({
		        params: {
                    start: 0,    limit: Warehouse.pagingToolbar.pageSize,
                    status: Warehouse.status,
                    entityJson: Ext.util.JSON.encode(searchParam)
                }
		    });
        }
    }, {
        text:"重置", iconCls:"resetIcon",
        handler:function(){	
        	Warehouse.searchForm.getForm().reset(); 
        	Warehouse.searchParam = {};
        	Warehouse.store.load();
        }
    }, {
        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ Warehouse.searchWin.hide(); }                
    }]
});
//新增编辑窗口，库房、库位、库管员
Warehouse.win = new Ext.Window({
    title:"新增", maximizable:true, width:600, height:420, layout:"fit", closeAction:"hide",
    items: {
      xtype:"tabpanel", id:"Warehouse_tabPanel", activeTab:0, enableTabScroll:true, border:false,
      items:[{
          title:"库 房",id: "warehouseTab", border:false, frame:true, layout:"fit", items:[ Warehouse.form ]
      },{
          title:"库 位",id: "locationTab", layout:"fit", items:[ WarehouseLocation.grid ]
      },{
          title:"库管员",id: "keeperTab", layout:"fit", items:[ StoreKeeper.grid ]
      }]        
    }
});
//页面自适应布局
var viewport = new Ext.Viewport({ 
    layout:"fit", 
    items:Warehouse.grid
});

//定义全局变量获取Tabs,并初始时话显示
Warehouse.tabs = Ext.getCmp("Warehouse_tabPanel") ;
//定义全局业务状态，用于过滤查询
Warehouse.status = "0,1" ;
/**
 * checkQuery()
 * 检查并获取查询数据
 * */
Warehouse.checkQuery =  function (){
	Warehouse.status = "-1";
	if(Ext.getCmp("addStatus").checked){
		Warehouse.status=Warehouse.status+',0';
	}
	if(Ext.getCmp("startStatus").checked){
		Warehouse.status=Warehouse.status+',1';
	}
	if(Ext.getCmp("trashStatus").checked){
		Warehouse.status=Warehouse.status+',2';
	}
	Warehouse.store.load({
		params: {
			entityJson: Ext.util.JSON.encode(Warehouse.searchParam)
        }
	});
}
//store载入前查询，为了分页查询时不至于出差错
Warehouse.store.on("beforeload", function(){
	this.baseParams.status = Warehouse.status;  
	this.baseParams.entityJson = Ext.util.JSON.encode(Warehouse.searchParam);
});

/**
 * @param infoArray 遍历的数组
 * @param msg 显示的字符串说明
 * */
Warehouse.alertOperate = function(infoArray,msg,status){
	var info = "";
	var msgInfo = "" ;
	var operInfo = "" ; //启用消息
	var invalidInfo = "" ; //作废消息
	if(status=='del'){
		msgInfo = "该操作将不能恢复，是否继续【删除】？";
		operInfo = "不能删除";
		invalidInfo = "不能删除";
	}
	if(status=='start'){
		msgInfo = "确定【启用】选择的项？";
		operInfo = "";
		invalidInfo = "不能启用"
	}
	if(status=='invalid'){
		msgInfo = "确定【作废】选择的项？";
		operInfo = "不能作废";
		invalidInfo = "";
	}
	var titleInfo = "";
	if(infoArray instanceof Array){
		for(var i = 0; i < infoArray.length; i++){
			if(infoArray[ i ].get("status") == 0){
				info += (i + 1) + ". 【" + infoArray[ i ].get("wareHouseName") + "】为新增"+operInfo+"!</br>";
				msgInfo = msg;
				titleInfo = "选择的记录！";
			}
			if(infoArray[ i ].get("status") == 1){
				info += (i + 1) + ". 【" + infoArray[ i ].get("wareHouseName") + "】已经启用"+operInfo+"！</br>";
				msgInfo = msg;
				titleInfo = "选择的记录！";
			}
			if(infoArray[ i ].get("status") == 2){
				info += (i + 1) + ". 【" + infoArray[ i ].get("wareHouseName") + "】已经作废"+invalidInfo+"！</br>";
				msgInfo = msg;
				titleInfo = "选择的记录！";
			}
		}
	} else {
		info = infoArray;
	}
	return   titleInfo + '</br>' + info + '</br>' + msgInfo;
}


});