/**
 * 组成位置 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('BuildUpPlace');                       //定义命名空间
BuildUpPlace.labelWidth = 80;
BuildUpPlace.fieldWidth = 140;
BuildUpPlace.searchParams = {};
//根据业务编码生成规则生成安装位置编码
BuildUpPlace.getFixPlaceCode = function(){
	var url = ctx + "/codeRuleConfig!getConfigRule.action";
	Ext.Ajax.request({
                url: url,
                params: {ruleFunction: "JXGC_FIX_PLACE_Code"},
                success: function(response, options){
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        Ext.getCmp("buildUpPlaceCode_Id").setValue(result.rule);
                    }
                },
                failure: function(response, options){
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
}

//新增配件位置Form
BuildUpPlace.fixPlaceForm = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: BuildUpPlace.labelWidth, plain: true,
    baseCls: "x-plain", defaultType: "textfield", height:600,  frame: false, style: "padding:10px", buttonAlign: "center",
    buttons:[{
    	text:"保存", iconCls:"saveIcon",
        handler:function(){
        	//表单验证是否通过
        var form = BuildUpPlace.fixPlaceForm.getForm(); 
        if (!form.isValid()) return;
        Ext.getCmp("buildUpPlaceCode_Id").enable();
        var data = form.getValues();
        Ext.getCmp("buildUpPlaceCode_Id").disable();
        data.trainTypeIDX = trainTypeIDX;
        data.partsTypeIDX = partsTypeIDX;
        if(self.loadMask)   self.loadMask.show();
        var cfg = {
            scope: this, url: ctx + '/buildUpPlace!saveOrUpdate.action', jsonData: data,
            success: function(response, options){
                if(self.loadMask)   self.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null && result.entity != null) {
                    alertSuccess(); 
                    Ext.getCmp("fixPlace_Tabs").getItem("fixBuildUpType_tabId").enable();
					Ext.getCmp("fixPlace_Tabs").activate("fixBuildUpType_tabId");
					fixPlaceIdx = result.entity.idx;//设置位置主键全局变量
					type = type_parts;//设置组成类型全局变量-配件组成	
					partsTypeIDX = result.entity.partsTypeIDX;//设置配件主键全局变量
					//过滤组成型号选择列表
					FixBuildUpType.buildUpTypeGrid.store.baseParams.fixPlaceIdx = result.entity.idx;
					FixBuildUpType.buildUpTypeGrid.store.baseParams.type = type_parts;
					FixBuildUpType.buildUpTypeGrid.store.baseParams.partsTypeIDX = result.entity.partsTypeIDX; 
					FixBuildUpType.buildUpTypeGrid.getStore().load({
        				params: {fixPlaceIdx : result.entity.idx, type: type_parts, partsTypeIDX: result.entity.partsTypeIDX}        			
        			});
        			//过滤可安装组成型号列表
        			FixBuildUpType.fixBuildUpTypeGrid.store.baseParams.fixPlaceIdx = result.entity.idx;
					FixBuildUpType.fixBuildUpTypeGrid.getStore().load({
						params: {fixPlaceIdx : result.entity.idx} 
					});
        			BuildUpPlace.grid.getStore().reload();
                } else {
                    alertFail(result.errMsg);
                }
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
        }
    },{
        text: "关闭", iconCls: "closeIcon", handler: function(){ BuildUpPlace.fixPlaceWin.hide(); }
    }],
    items: [    	
    	{
    	xtype: "panel", border: false, anchor:"90%", baseCls: "x-plain", layout: "column", align: "center", plain: true,
    	items : [
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id:"buildUpPlaceCode_Id", name: "buildUpPlaceCode", fieldLabel: "配件位置编码", allowBlank: false, disabled:true, width: BuildUpPlace.fieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: BuildUpPlace.fieldWidth, maxLength:50  },
            		{ 
            		  id: "ProfessionalType_form_Id", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型", 
					  hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_formId",propertyName:"text"}], 
					  selectNodeModel: "all",  width: BuildUpPlace.fieldWidth
            		},
            		{id:"professionalTypeName_formId", name:"professionalTypeName", xtype: 'hidden' }
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id: "buildUpPlaceName_Id", name: "buildUpPlaceName", fieldLabel: "配件位置名称", allowBlank: false, width: BuildUpPlace.fieldWidth, maxLength: 50 },
            		{ name: "buildUpPlaceShortName", fieldLabel: "配件位置简称",  width: BuildUpPlace.fieldWidth, maxLength:20  },
            		{ id: "partID_formId", xtype: "Base_combo",	fieldLabel: "位置",
						  hiddenName: "partID", returnField: [{widgetId:"partName_formId",propertyName:"partName"}],
						  displayField: "partName", valueField: "partId",
						  entity: "com.yunda.jx.component.entity.EquipPart", 
						  fields: ["partId","partName"],
						  pageSize: 20, minListWidth: 200, width: BuildUpPlace.fieldWidth, editable: true },
				    { id:"partName_formId", name:"partName", xtype: "hidden" }
            		            		
            	]
    		},
    		{ name: "buildUpPlaceSEQ", xtype:"hidden"},
    		{ name: "idx",xtype: "hidden"},
    		{ name: "remarks",xtype: "hidden"},
    		{ name: "placeType",xtype: "hidden", value:fix_place},
    		{ name: "trainTypeIDX", xtype:'hidden', value:trainTypeIDX},
    		{ name: "partsTypeIDX", xtype:'hidden', value:partsTypeIDX}
    	]}     
    ]
});
//新增配件位置窗口
BuildUpPlace.fixPlaceWin  = new Ext.Window({
    title: "安装位置编辑", maximizable: true, width: 700, height: 350, layout: "fit", plain: true, closeAction: "hide", modal: true,
    items: [{
        xtype: "tabpanel", id:"fixPlace_Tabs",  border: false, activeTab:0, enableTabScroll:true, items:[{
            id:"fixPlace_tabId", title: "基本信息", layout: "fit", frame: true, border: false, items: BuildUpPlace.fixPlaceForm
        },{
            id:"fixBuildUpType_tabId", title: "可安装组成型号", layout: "fit", border: false, items: FixBuildUpType.fixBuildUpTypeGrid
        }]
    }]
});
//加载【安装位置列表】或【选择安装位置列表】
BuildUpPlace.loadGrid = function(grid,buildUpTypeIdx){
	grid.store.baseParams.placeTypes = fix_place + "," + virtual_place;//位置类型：安装位置和虚拟位置
	if(trainTypeIDX != ''){
		grid.store.baseParams.trainTypeIDX = trainTypeIDX;//车型
		BuildUpPlace.searchParams.trainTypeIDX = trainTypeIDX;//车型
	}
	if(partsTypeIDX != ''){
		grid.store.baseParams.partsTypeIDX = partsTypeIDX;//配件型号
		BuildUpPlace.searchParams.partsTypeIDX = partsTypeIDX;//配件型号
	}
	if(buildUpTypeIdx != ''){
		grid.store.baseParams.buildUpTypeIdx = buildUpTypeIdx;
	}
	grid.store.baseParams.entityJson = Ext.util.JSON.encode(BuildUpPlace.searchParams);
	grid.store.load();
}
//安装位置列表
BuildUpPlace.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpPlace!placeList.action',                 //装载列表数据的请求URL
    saveFormColNum: 2,
    storeAutoLoad: false,
    //remoteSort: false,
    singleSelect: true,
    tbar: [],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'安装位置编码', dataIndex:'buildUpPlaceCode', editor:{  maxLength:10 }
	},{
		header:'安装位置名称', dataIndex:'buildUpPlaceName', editor:{  maxLength:50 }
	},{
		header:'组成位置序号', dataIndex:'buildUpPlaceSEQ', hidden: true, editor:{ xtype:'numberfield', maxLength:8 }
	},{
		header:'位置简称', dataIndex:'buildUpPlaceShortName', editor:{  maxLength:20 }
	},{
		header:'图号', dataIndex:'chartNo', editor:{  maxLength:50 }
	},{
		header:'配件专业类型表主键', dataIndex:'professionalTypeIDX', hidden: true, 
		editor:{			
		}
	},{
		header:'专业类型名称', dataIndex:'professionalTypeName', editor:{ xtype:'hidden' }
	},{
		header:'位置编码', dataIndex:'partID', hidden:true, editor:{ xtype:'hidden', maxLength:4 }
	},{
		header:'位置', dataIndex:'partName', editor:{  maxLength:100 }
	},{
		header:'是否安装实物件', dataIndex:'placeType', sortable: true,
		renderer: function(v){if(v==fix_place)return "是";else return "否";},
		editor:{ xtype:'hidden' }
	},{
		header:'备注', dataIndex:'remarks', hidden:true, editor:{ xtype:'textarea', maxLength:1000 }
	},{
		header:'状态', dataIndex:'status', hidden:true, editor:{ xtype:'hidden', maxLength:2 }
	},{
		header:'车型', dataIndex:'trainTypeIDX', hidden:true, editor:{ xtype:'hidden', value: trainTypeIDX }
	},{
		header:'配件型号', dataIndex:'partsTypeIDX', hidden:true, editor:{ xtype:'hidden', value: partsTypeIDX }
	}]
});
//过滤安装位置列表
BuildUpPlace.grid.store.on("beforeload",function(){
	this.baseParams.placeTypes = fix_place + "," + virtual_place;//位置类型：安装位置和虚拟位置
	if(trainTypeIDX != ''){
		this.baseParams.trainTypeIDX = trainTypeIDX;//车型
	}
	if(partsTypeIDX != ''){
		this.baseParams.partsTypeIDX = partsTypeIDX;//配件型号
	}
	this.baseParams.entityJson = Ext.util.JSON.encode(BuildUpPlace.searchParams);
});

//安装位置选择列表
BuildUpPlace.selectGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpPlace!placeList.action',                 //装载列表数据的请求URL
    saveFormColNum: 2,
    storeAutoLoad: false,
    //remoteSort: false,
    tbar: [{
    	text : "确定",
		iconCls : "addIcon",
		handler : function(){
			//未选择记录，直接返回
        	if(!$yd.isSelectedRecord(BuildUpPlace.selectGrid)) return;
        	var tempData = BuildUpPlace.selectGrid.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.buildUpPlaceIdx = tempData[i].get("idx");
        			data.buildUpTypeIdx = buildUpTypeIdx;
        			data.parentIdx = parentIdx;
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/placeBuildUpType!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        if(pageType == 1){
	                        	TrainPlaceBuildUp.childTrainFixGrid.store.reload();
	                        	BuildUpPlace.selectGrid.store.reload();
	                        	TrainBuild.expandNode(nodeId, parentIdx);
	                        }
	                        if(pageType == 2){
	                        	PartsPlaceBuildUp.childPartsFixGrid.store.reload();
	                        	BuildUpPlace.selectGrid.store.reload();
	                        	//PartsBuild.expandNode(nodeId, parentIdx, parentNodeId);
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
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'安装位置编码', dataIndex:'buildUpPlaceCode', editor:{  maxLength:10 }
	},{
		header:'安装位置名称', dataIndex:'buildUpPlaceName', editor:{  maxLength:50 }
	},{
		header:'组成位置序号', dataIndex:'buildUpPlaceSEQ', hidden: true, editor:{ xtype:'numberfield', maxLength:8 }
	},{
		header:'位置简称', dataIndex:'buildUpPlaceShortName', editor:{  maxLength:20 }
	},{
		header:'图号', dataIndex:'chartNo', editor:{  maxLength:50 }
	},{
		header:'配件专业类型表主键', dataIndex:'professionalTypeIDX', hidden: true, 
		editor:{			
		}
	},{
		header:'专业类型名称', dataIndex:'professionalTypeName', editor:{ xtype:'hidden' }
	},{
		header:'位置编码', dataIndex:'partID', hidden:true, editor:{ xtype:'hidden', maxLength:4 }
	},{
		header:'位置', dataIndex:'partName', editor:{  maxLength:100 }
	},{
		header:'是否安装实物件', dataIndex:'placeType', sortable: true,
		renderer: function(v){if(v==fix_place)return "是";else return "否";},
		editor:{ xtype:'hidden' }
	},{
		header:'备注', dataIndex:'remarks', hidden:true, editor:{ xtype:'textarea', maxLength:1000 }
	},{
		header:'状态', dataIndex:'status', hidden:true, editor:{ xtype:'hidden', maxLength:2 }
	},{
		header:'车型', dataIndex:'trainTypeIDX', hidden:true, editor:{ xtype:'hidden', value: trainTypeIDX }
	},{
		header:'配件型号', dataIndex:'partsTypeIDX', hidden:true, editor:{ xtype:'hidden', value: partsTypeIDX }
	}]	
});
//移除侦听器
BuildUpPlace.selectGrid.un('rowdblclick', BuildUpPlace.selectGrid.toEditFn, BuildUpPlace.selectGrid);
//过滤安装位置选择列表
BuildUpPlace.selectGrid.store.on("beforeload",function(){
	this.baseParams.placeTypes = fix_place + "," + virtual_place;//位置类型：安装位置和虚拟位置
	if(trainTypeIDX != ''){
		this.baseParams.trainTypeIDX = trainTypeIDX;//车型
		BuildUpPlace.searchParams.trainTypeIDX = trainTypeIDX;//车型
	}
	if(partsTypeIDX != ''){
		this.baseParams.partsTypeIDX = partsTypeIDX;//配件型号
		BuildUpPlace.searchParams.partsTypeIDX = partsTypeIDX;//配件型号
	}
	this.baseParams.entityJson = Ext.util.JSON.encode(BuildUpPlace.searchParams);
});
//根据业务编码生成规则生成虚拟位置编码
BuildUpPlace.getVirtualPlaceCode = function(){
	var url = ctx + "/codeRuleConfig!getConfigRule.action";
	Ext.Ajax.request({
                url: url,
                params: {ruleFunction: "JXGC_FIX_PLACE_Code"},
                success: function(response, options){
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        Ext.getCmp("buildUpPlaceCode_VId").setValue(result.rule);
                    }
                },
                failure: function(response, options){
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
}
//新增虚拟位置Form
BuildUpPlace.virtualPlaceForm = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: BuildUpPlace.labelWidth, plain: true,
    baseCls: "x-plain", defaultType: "textfield", height:600,  frame: false, style: "padding:10px", buttonAlign: "center",
    buttons:[{
    	text:"保存", iconCls:"saveIcon",
        handler:function(){
        	//表单验证是否通过
        var form = BuildUpPlace.virtualPlaceForm.getForm(); 
        if (!form.isValid()) return;
        Ext.getCmp("buildUpPlaceCode_VId").enable();
        var data = form.getValues();
        Ext.getCmp("buildUpPlaceCode_VId").disable();
        data.trainTypeIDX = trainTypeIDX;
        data.partsTypeIDX = partsTypeIDX;
        if(self.loadMask)   self.loadMask.show();
        var cfg = {
            scope: this, url: ctx + '/buildUpPlace!saveOrUpdate.action', jsonData: data,
            success: function(response, options){
                if(self.loadMask)   self.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null && result.entity != null) {
                    alertSuccess();
                    Ext.getCmp("virtualPlace_Tabs").getItem("virtualBuildUpType_tabId").enable();
					Ext.getCmp("virtualPlace_Tabs").activate("virtualBuildUpType_tabId");
					fixPlaceIdx = result.entity.idx;//设置位置主键全局变量
					type = type_virtual;//设置组成类型全局变量-虚拟组成
					//过滤可安装虚拟组成型号列表
					FixBuildUpType.v_fixBuildUpTypeGrid.getStore().load({
						params: {fixPlaceIdx : result.entity.idx} 
					});		
					//过滤虚拟组成型号选择列表
					FixBuildUpType.v_buildUpTypeGrid.getStore().load({
        				params: {fixPlaceIdx : result.entity.idx, type: type_virtual, trainTypeIDX: result.entity.trainTypeIDX}        			
        			});
        			BuildUpPlace.grid.getStore().reload();
                } else {
                    alertFail(result.errMsg);
                }
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
        }
    },{
        text: "关闭", iconCls: "closeIcon", handler: function(){ BuildUpPlace.virtualPlaceWin.hide(); }
    }],
    items: [    	
    	{
    	xtype: "panel", border: false, anchor:"90%", baseCls: "x-plain", layout: "column", align: "center", plain: true,
    	items : [
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id:"buildUpPlaceCode_VId", name: "buildUpPlaceCode", fieldLabel: "虚拟位置编码", allowBlank: false, disabled:true, width: BuildUpPlace.fieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: BuildUpPlace.fieldWidth, maxLength:50  },
            		{ 
            		  id: "ProfessionalType_form_VId", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型",
					  hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_VformId",propertyName:"text"}], 
					  selectNodeModel: "all",  width: BuildUpPlace.fieldWidth
            		},
            		{id:"professionalTypeName_VformId", name:"professionalTypeName", xtype: 'hidden' }
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id: "buildUpPlaceName_VId", name: "buildUpPlaceName", fieldLabel: "虚拟位置名称", allowBlank: false, width: BuildUpPlace.fieldWidth, maxLength: 50 },
            		{ name: "buildUpPlaceShortName", fieldLabel: "虚拟位置简称",  width: BuildUpPlace.fieldWidth, maxLength:20  },
            		{ id: "partID_VformId", xtype: "Base_combo",	fieldLabel: "位置",
						  hiddenName: "partID", returnField: [{widgetId:"partName_VformId",propertyName:"partName"}],
						  displayField: "partName", valueField: "partId",
						  entity: "com.yunda.jx.component.entity.EquipPart", 
						  fields: ["partId","partName"],
						  pageSize: 20, minListWidth: 200, width: BuildUpPlace.fieldWidth, editable: true },
				    { id:"partName_VformId", name:"partName", xtype: "hidden" }
            		            		
            	]
    		},
    		{ name: "buildUpPlaceSEQ", xtype:"hidden"},
    		{ name: "idx",xtype: "hidden"},
    		{ name: "remarks",xtype: "hidden"},
    		{ name: "placeType",xtype: "hidden", value:virtual_place},
    		{ name: "trainTypeIDX", xtype:'hidden', value:trainTypeIDX},
    		{ name: "partsTypeIDX", xtype:'hidden', value:partsTypeIDX}
    	]}     
    ]
});
//新增虚拟位置窗口
BuildUpPlace.virtualPlaceWin  = new Ext.Window({
    title: "安装位置编辑", maximizable: true, width: 700, height: 350, layout: "fit", plain: true, closeAction: "hide", modal: true,
    items: [{
        xtype: "tabpanel", id:"virtualPlace_Tabs",  border: false, activeTab:0, enableTabScroll:true, items:[{
            id:"virtualPlace_tabId", title: "基本信息", layout: "fit", frame: true, border: false, items: BuildUpPlace.virtualPlaceForm
        },{
            id:"virtualBuildUpType_tabId", title: "可安装虚拟组成型号", layout: "fit", border: false, items: FixBuildUpType.v_fixBuildUpTypeGrid
        }]
    }]
});
//虚拟组成设置-安装位置选择列表
BuildUpPlace.v_selectGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpPlace!placeList.action',                 //装载列表数据的请求URL
    saveFormColNum: 2,
    //remoteSort: false,
    tbar: [{
    	text : "确定",
		iconCls : "addIcon",
		handler : function(){
			//未选择记录，直接返回
        	if(!$yd.isSelectedRecord(BuildUpPlace.v_selectGrid)) return;
        	var tempData = BuildUpPlace.v_selectGrid.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.buildUpPlaceIdx = tempData[i].get("idx");
        			data.buildUpTypeIdx = buildUpTypeIdx;
        			data.parentIdx = parentIdx;
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/placeBuildUpType!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        VirtualPlaceBuildUp.childTrainFixGrid.store.reload();
	                        BuildUpPlace.v_selectGrid.store.reload();
	                        //VirtualBuild.expandNode(nodeId, parentIdx);
	                    } else {
	                        alertFail(result.errMsg);
	                    }
	                },
	                failure: function(response, options){
	                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	                }
	            });
		}    
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'安装位置编码', dataIndex:'buildUpPlaceCode', editor:{  maxLength:10 }
	},{
		header:'安装位置名称', dataIndex:'buildUpPlaceName', editor:{  maxLength:50 }
	},{
		header:'组成位置序号', dataIndex:'buildUpPlaceSEQ', hidden: true, editor:{ xtype:'numberfield', maxLength:8 }
	},{
		header:'位置简称', dataIndex:'buildUpPlaceShortName', editor:{  maxLength:20 }
	},{
		header:'图号', dataIndex:'chartNo', editor:{  maxLength:50 }
	},{
		header:'配件专业类型表主键', dataIndex:'professionalTypeIDX', hidden: true, 
		editor:{			
		}
	},{
		header:'专业类型名称', dataIndex:'professionalTypeName', editor:{ xtype:'hidden' }
	},{
		header:'位置编码', dataIndex:'partID', hidden:true, editor:{ xtype:'hidden', maxLength:4 }
	},{
		header:'位置', dataIndex:'partName', editor:{  maxLength:100 }
	},{
		header:'是否安装实物件', dataIndex:'placeType', sortable: true,
		renderer: function(v){if(v==fix_place)return "是";else return "否";},
		editor:{ xtype:'hidden' }
	},{
		header:'备注', dataIndex:'remarks', hidden:true, editor:{ xtype:'textarea', maxLength:1000 }
	},{
		header:'状态', dataIndex:'status', hidden:true, editor:{ xtype:'hidden', maxLength:2 }
	},{
		header:'车型', dataIndex:'trainTypeIDX', hidden:true, editor:{ xtype:'hidden', value: trainTypeIDX }
	},{
		header:'配件型号', dataIndex:'partsTypeIDX', hidden:true, editor:{ xtype:'hidden', value: partsTypeIDX }
	}]	
});
//移除侦听器
BuildUpPlace.v_selectGrid.un('rowdblclick', BuildUpPlace.v_selectGrid.toEditFn, BuildUpPlace.v_selectGrid);
//过滤虚拟组成设置-安装位置选择列表
BuildUpPlace.v_selectGrid.store.on("beforeload",function(){
	this.baseParams.placeTypes = fix_place + "," + virtual_place;//位置类型：安装位置和虚拟位置
	if(trainTypeIDX != ''){
		this.baseParams.trainTypeIDX = trainTypeIDX;//车型
		BuildUpPlace.searchParams.trainTypeIDX = trainTypeIDX;//车型
	}
	if(partsTypeIDX != ''){
		this.baseParams.partsTypeIDX = partsTypeIDX;//配件型号
		BuildUpPlace.searchParams.partsTypeIDX = partsTypeIDX;//配件型号
	}
	this.baseParams.entityJson = Ext.util.JSON.encode(BuildUpPlace.searchParams);
});
});