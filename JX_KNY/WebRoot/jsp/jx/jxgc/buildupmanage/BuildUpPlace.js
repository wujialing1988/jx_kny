/**
 * 组成位置 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('BuildUpPlace');                       //定义命名空间
BuildUpPlace.labelWidth = 90;
BuildUpPlace.fieldWidth = 140;
BuildUpPlace.searchParams = {};
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

/*******************************1首页[安装位置]使用(1.1安装位置列表、1.2新增配件位置(基本信息form+可安装组成型号)、1.3新增虚拟位置(基本信息form+可安装组成型号))***************************************/

//1.1安装位置列表--首页[安装位置]使用
BuildUpPlace.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpPlace!placeList.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/buildUpPlace!deletePlace.action',            //删除数据的请求URL
    saveFormColNum: 2,
    storeAutoLoad: false,
    //remoteSort: false,
    tbar: [{
    	text : "新增配件位置",
		iconCls : "addIcon",
		handler : function(){
			//未选择车型记录，直接返回
			if(pageType == 1){
	        	if (TrainBuildUp.trainTypeGrid.selModel.getCount() == 0) {
	        		MyExt.Msg.alert("请选择车型！");
			        return
			    }
		    }
		    //未选择配件记录，直接返回
		    if(pageType == 2){
	        	if (PartsBuildUp.partsTypeGrid.selModel.getCount() == 0) {
	        		MyExt.Msg.alert("请选择配件型号！");
			        return
			    }
		    }
			BuildUpPlace.fixPlaceWin.show();
			Ext.getCmp("fixPlace_Tabs").getItem("fixBuildUpType_tabId").disable();
			Ext.getCmp("fixPlace_Tabs").activate("fixPlace_tabId");
			BuildUpPlace.fixPlaceForm.getForm().reset();
			Ext.getCmp("ProfessionalType_form_Id").clearValue();
			Ext.getCmp("partID_formId").clearValue();
			BuildUpPlace.getFixPlaceCode();
		}    
    },{
    	text : "新增虚拟位置",
		iconCls : "addIcon",
		id : "virtualPlace_button1",
		handler : function(){
        	//未选择车型记录，直接返回
			if(pageType == 1){
	        	if (TrainBuildUp.trainTypeGrid.selModel.getCount() == 0) {
	        		MyExt.Msg.alert("请选择车型！");
			        return
			    }
		    }
		    //未选择配件记录，直接返回
		    if(pageType == 2){
	        	if (PartsBuildUp.partsTypeGrid.selModel.getCount() == 0) {
	        		MyExt.Msg.alert("请选择配件型号！");
			        return
			    }
		    }
			BuildUpPlace.virtualPlaceWin.show();
			Ext.getCmp("virtualPlace_Tabs").getItem("virtualBuildUpType_tabId").disable();
			Ext.getCmp("virtualPlace_Tabs").activate("virtualPlace_tabId");
			BuildUpPlace.virtualPlaceForm.getForm().reset();
			Ext.getCmp("ProfessionalType_form_VId").clearValue();
			Ext.getCmp("partID_VformId").clearValue();
			BuildUpPlace.getVirtualPlaceCode();			
		}    
    },'delete','refresh'],
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
	}],
	/**
     * 在双击表格中记录时（rowdblclick）触发该函数，获取当前选中记录，并将选中记录加载到编辑表单，显示编辑窗口
     * 在加载数据显示窗口之前，将执行beforeShowEditWin函数，返回false将不显示编辑窗口，中止编辑动作
     * @param {Ext.yunda.Grid} grid 当前表格对象
     * @param {Number} rowIndex 选中行下标
     * @param {Ext.EventObject} e Ext事件对象
     */    
    toEditFn: function(grid, rowIndex, e){
        var record = this.store.getAt(rowIndex);
        //安装位置编辑
        if(record.get("placeType") == fix_place){
        	BuildUpPlace.fixPlaceWin.show();
        	Ext.getCmp("fixPlace_Tabs").getItem("fixBuildUpType_tabId").enable();
			Ext.getCmp("fixPlace_Tabs").activate("fixPlace_tabId");
			BuildUpPlace.fixPlaceForm.getForm().loadRecord(record);
			Ext.getCmp("ProfessionalType_form_Id").clearValue();
			Ext.getCmp("ProfessionalType_form_Id").setDisplayValue(record.get("professionalTypeIDX"),record.get("professionalTypeName"));
			Ext.getCmp("partID_formId").clearValue();
			Ext.getCmp("partID_formId").setDisplayValue(record.get("partID"),record.get("partName"));
			fixPlaceIdx = record.get("idx");//设置位置主键全局变量
			type = type_parts;//设置组成类型全局变量-配件组成
			partsTypeIDX = record.get("partsTypeIDX"); //设置配件主键全局变量
			//过滤组成型号选择列表
			FixBuildUpType.buildUpTypeGrid.store.baseParams.fixPlaceIdx = record.get("idx");
			FixBuildUpType.buildUpTypeGrid.store.baseParams.type = type_parts;
			FixBuildUpType.buildUpTypeGrid.store.baseParams.partsTypeIDX = record.get("partsTypeIDX"); 
			FixBuildUpType.buildUpTypeGrid.getStore().load({
				params: {fixPlaceIdx : record.get("idx"), type: type_parts, partsTypeIDX: record.get("partsTypeIDX")}        			
			});
			//过滤可安装组成型号列表
			FixBuildUpType.fixBuildUpTypeGrid.store.baseParams.fixPlaceIdx = record.get("idx");
			FixBuildUpType.fixBuildUpTypeGrid.getStore().load({
				params: {fixPlaceIdx : record.get("idx")} 
			});
        }
        //虚拟位置编辑
        if(record.get("placeType") == virtual_place){
        	BuildUpPlace.virtualPlaceWin.show();
        	Ext.getCmp("virtualPlace_Tabs").getItem("virtualBuildUpType_tabId").enable();
			Ext.getCmp("virtualPlace_Tabs").activate("virtualPlace_tabId");
			fixPlaceIdx = record.get("idx");//设置位置主键全局变量
			type = type_virtual;//设置组成类型全局变量-虚拟组成
			//过滤虚拟组成型号选择列表
			FixBuildUpType.v_buildUpTypeGrid.store.baseParams.fixPlaceIdx = record.get("idx");
			FixBuildUpType.v_buildUpTypeGrid.store.baseParams.type = type_virtual;
			FixBuildUpType.v_buildUpTypeGrid.store.baseParams.trainTypeIDX = trainTypeIDX;
			FixBuildUpType.v_buildUpTypeGrid.getStore().load({
				params: {fixPlaceIdx : record.get("idx"), type: type_virtual, trainTypeIDX: trainTypeIDX}        			
			});
			//过滤可安装虚拟组成型号列表
			FixBuildUpType.v_fixBuildUpTypeGrid.store.baseParams.fixPlaceIdx = record.get("idx");
			FixBuildUpType.v_fixBuildUpTypeGrid.getStore().load({
				params: {fixPlaceIdx : record.get("idx")} 
			});			
			BuildUpPlace.virtualPlaceForm.getForm().loadRecord(record);
			Ext.getCmp("ProfessionalType_form_VId").clearValue();
			Ext.getCmp("ProfessionalType_form_VId").setDisplayValue(record.get("professionalTypeIDX"),record.get("professionalTypeName"));
			Ext.getCmp("partID_VformId").clearValue();
			Ext.getCmp("partID_VformId").setDisplayValue(record.get("partID"),record.get("partName"));			
        }
    }
});
//1.1过滤安装位置列表
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
//1.2根据业务编码生成规则生成安装位置编码--首页[安装位置]使用
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
//1.2新增配件位置Form--首页[安装位置]使用
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
	        data.trainTypeIDX = trainTypeIDX;//trainTypeIDX值为在点击选择【车型组成】时设置
	        data.partsTypeIDX = partsTypeIDX;//partsTypeIDX值为在点击选择【配件组成】时设置
	        
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
	        //验证是否有重复位置名称
	        Ext.Ajax.request({
	        	url: ctx + "/buildUpPlace!checkReduplicateName.action",
	            jsonData: data,
	            success: function(response, options){	                    
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.success == true && result.errMsg != null) {
	                    Ext.Msg.confirm("提示  ", result.errMsg[0] + " 是否继续？  ", function(btn){
					        if(btn != 'yes')    return;
					        if(self.loadMask)   self.loadMask.show();
					        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
					    }); 
	                }else if(result.errMsg == null){
	                	if(self.loadMask)   self.loadMask.show();
					    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	                }
	            },
	            failure: function(response, options){
	                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	            }
	        });        
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
//1.2新增配件位置窗口--首页[安装位置]使用
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
//1.3根据业务编码生成规则生成虚拟位置编码--首页[安装位置]使用
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
//1.3新增虚拟位置Form--首页[安装位置]使用
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
						FixBuildUpType.v_fixBuildUpTypeGrid.store.baseParams.fixPlaceIdx = result.entity.idx;
						FixBuildUpType.v_fixBuildUpTypeGrid.getStore().load({
							params: {fixPlaceIdx : result.entity.idx} 
						});		
						//过滤虚拟组成型号选择列表
						FixBuildUpType.v_buildUpTypeGrid.store.baseParams.fixPlaceIdx = result.entity.idx;
						FixBuildUpType.v_buildUpTypeGrid.store.baseParams.type = type_virtual;
						FixBuildUpType.v_buildUpTypeGrid.store.baseParams.trainTypeIDX = result.entity.trainTypeIDX;
						FixBuildUpType.v_buildUpTypeGrid.getStore().load({
	        				params: {fixPlaceIdx : result.entity.idx, type: type_virtual, trainTypeIDX: result.entity.trainTypeIDX}        			
	        			});
	        			BuildUpPlace.grid.getStore().reload();
	                } else {
	                    alertFail(result.errMsg);
	                }
	            }
	        };
	        Ext.Ajax.request({
	        	url: ctx + "/buildUpPlace!checkReduplicateName.action",
	            jsonData: data,
	            success: function(response, options){	                    
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.success == true && result.errMsg != null) {
	                    Ext.Msg.confirm("提示  ", result.errMsg[0] + " 是否继续？  ", function(btn){
					        if(btn != 'yes')    return;
					        if(self.loadMask)   self.loadMask.show();
					        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
					    }); 
	                }else if(result.errMsg == null){
	                	if(self.loadMask)   self.loadMask.show();
					    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	                }
	            },
	            failure: function(response, options){
	                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	            }
	        });        
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
//1.3新增虚拟位置窗口--首页[安装位置]使用
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
/*************************************2【组成维护】里面使用(2.1选择安装位置列表、2.2新增配件位置(基本信息form+可安装组成型号)、2.3新增虚拟位置(基本信息form+可安装组成型号))********************************************************/
//2.1安装位置选择列表--【组成维护】里面使用
BuildUpPlace.selectGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpPlace!placeList.action',                 //装载列表数据的请求URL
    saveFormColNum: 2,
    storeAutoLoad: false,
    //remoteSort: false,
    tbar: [{
        	xtype:"label", text:"位置名称：" 
    	},{			
            xtype: "textfield",    
            id: "placeName_searchId",
            width:70
		},{
        	xtype:"label", text:"图号：" 
    	},{			
            xtype: "textfield",    
            id: "chartNo_searchId",
            width:70
		},{
        	xtype:"label", text:"是否安装实物配件：" 
    	},{
	        xtype: 'combo',
	        id: 'isFix',
            fieldLabel: '',
            width:50,
            hiddenName:'',
            store:new Ext.data.SimpleStore({
			    fields: ['v', 't'],
				data : [['','全部'],[fix_place,'是'],[virtual_place,'否']]
			}),
			valueField:'v',
			displayField:'t',
			triggerAction:'all',
			mode:'local',
			value:'',
			editable: false

	    },{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var placeName = Ext.getCmp("placeName_searchId").getValue();
				var chartNo = Ext.getCmp("chartNo_searchId").getValue();
				var isFix = Ext.getCmp("isFix").getValue();
				
				BuildUpPlace.searchParams.buildUpPlaceName = placeName;
				BuildUpPlace.searchParams.chartNo = chartNo;
				BuildUpPlace.selectGrid.store.baseParams.placeTypes = fix_place + "," + virtual_place;//位置类型：安装位置和虚拟位置
				if(trainTypeIDX != ''){
					BuildUpPlace.selectGrid.store.baseParams.trainTypeIDX = trainTypeIDX;//车型
					BuildUpPlace.searchParams.trainTypeIDX = trainTypeIDX;//车型
				}
				if(partsTypeIDX != ''){
					BuildUpPlace.selectGrid.store.baseParams.partsTypeIDX = partsTypeIDX;//配件型号
					BuildUpPlace.searchParams.partsTypeIDX = partsTypeIDX;//配件型号
				}
				if(buildUpTypeIdx != ''){
					BuildUpPlace.selectGrid.store.baseParams.buildUpTypeIdx = buildUpTypeIdx;
				}
				BuildUpPlace.selectGrid.store.baseParams.isFix = isFix;
				BuildUpPlace.selectGrid.store.baseParams.entityJson = Ext.util.JSON.encode(BuildUpPlace.searchParams);
				BuildUpPlace.selectGrid.getStore().load();
			},			
			width : 40
		},{
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
//2.1移除侦听器
BuildUpPlace.selectGrid.un('rowdblclick', BuildUpPlace.selectGrid.toEditFn, BuildUpPlace.selectGrid);
//2.1过滤安装位置选择列表
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
//2.2根据业务编码生成规则生成安装位置编码--【组成维护】里面使用
BuildUpPlace.getFixPlaceCode1 = function(){
	var url = ctx + "/codeRuleConfig!getConfigRule.action";
	Ext.Ajax.request({
        url: url,
        params: {ruleFunction: "JXGC_FIX_PLACE_Code"},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                Ext.getCmp("buildUpPlaceCode_Id1").setValue(result.rule);
            }
        },
        failure: function(response, options){
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    });
}
//2.2新增配件位置Form--【组成维护】里面使用
BuildUpPlace.fixPlaceForm1 = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: BuildUpPlace.labelWidth, plain: true,
    baseCls: "x-plain", defaultType: "textfield", height:600,  frame: false, style: "padding:10px", buttonAlign: "center",
    buttons:[{
    	text:"保存", iconCls:"saveIcon",
        handler:function(){
        	//表单验证是否通过
	        var form = BuildUpPlace.fixPlaceForm1.getForm(); 
	        if (!form.isValid()) return;
	        Ext.getCmp("buildUpPlaceCode_Id1").enable();
	        var data = form.getValues();
	        Ext.getCmp("buildUpPlaceCode_Id1").disable();
	        data.trainTypeIDX = trainTypeIDX;//trainTypeIDX值为在点击选择【车型组成】时及点击【设置组成】时设置
	        data.partsTypeIDX = partsTypeIDX;//partsTypeIDX值为在点击树节点时设置
	        
	        var cfg = {
	            scope: this, url: ctx + '/buildUpPlace!savePlace.action', jsonData: data,
	            params:{parentIdx:parentIdx,buildUpTypeIDX:buildUpTypeIdx},//parentIdx、buildUpTypeIdx值为在点击树节点时设置
	            success: function(response, options){
	                if(self.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null && result.entity != null) {
	                    alertSuccess(); 
	                    Ext.getCmp("fixPlace_Tabs1").getItem("fixBuildUpType_tabId1").enable();
						Ext.getCmp("fixPlace_Tabs1").activate("fixBuildUpType_tabId1");
						fixPlaceIdx = result.entity.idx;//设置位置主键全局变量
						type = type_parts;//设置组成类型全局变量-配件组成	
						partsTypeIDX = result.entity.partsTypeIDX;//设置配件主键全局变量
						//过滤组成型号选择列表
						FixBuildUpType.buildUpTypeGrid1.store.baseParams.fixPlaceIdx = result.entity.idx;
						FixBuildUpType.buildUpTypeGrid1.store.baseParams.type = type_parts;
						FixBuildUpType.buildUpTypeGrid1.store.baseParams.partsTypeIDX = result.entity.partsTypeIDX; 
						FixBuildUpType.buildUpTypeGrid1.getStore().load({
	        				params: {fixPlaceIdx : result.entity.idx, type: type_parts, partsTypeIDX: result.entity.partsTypeIDX}        			
	        			});
	        			//过滤可安装组成型号列表
	        			FixBuildUpType.fixBuildUpTypeGrid1.store.baseParams.fixPlaceIdx = result.entity.idx;
						FixBuildUpType.fixBuildUpTypeGrid1.getStore().load({
							params: {fixPlaceIdx : result.entity.idx} 
						});
						if(pageType == 1)//机车组成维护页面
							TrainPlaceBuildUp.childTrainFixGrid.store.load();
						else if(pageType == 2)//配件组成维护页面
							PartsPlaceBuildUp.childPartsFixGrid.store.load();
	        			//BuildUpPlace.grid.getStore().reload();
	                } else {
	                    alertFail(result.errMsg);
	                }
	            }
	        };
	        //验证是否有重复位置名称
	        Ext.Ajax.request({
	        	url: ctx + "/buildUpPlace!checkReduplicateName.action",
	            jsonData: data,
	            success: function(response, options){	                    
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.success == true && result.errMsg != null) {
	                    Ext.Msg.confirm("提示  ", result.errMsg[0] + " 是否继续？  ", function(btn){
					        if(btn != 'yes')    return;
					        if(self.loadMask)   self.loadMask.show();
					        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
					    }); 
	                }else if(result.errMsg == null){
	                	if(self.loadMask)   self.loadMask.show();
					    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	                }
	            },
	            failure: function(response, options){
	                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	            }
	        });        
        }
    },{
        text: "关闭", iconCls: "closeIcon", handler: function(){ BuildUpPlace.fixPlaceWin1.hide(); }
    }],
    items: [    	
    	{
    	xtype: "panel", border: false, anchor:"90%", baseCls: "x-plain", layout: "column", align: "center", plain: true,
    	items : [
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id:"buildUpPlaceCode_Id1", name: "buildUpPlaceCode", fieldLabel: "配件位置编码", allowBlank: false, disabled:true, width: BuildUpPlace.fieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: BuildUpPlace.fieldWidth, maxLength:50  },
            		{ 
            		  id: "ProfessionalType_form_Id1", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型", 
					  hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_formId1",propertyName:"text"}], 
					  selectNodeModel: "all",  width: BuildUpPlace.fieldWidth
            		},
            		{id:"professionalTypeName_formId1", name:"professionalTypeName", xtype: 'hidden' }
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id: "buildUpPlaceName_Id1", name: "buildUpPlaceName", fieldLabel: "配件位置名称", allowBlank: false, width: BuildUpPlace.fieldWidth, maxLength: 50 },
            		{ name: "buildUpPlaceShortName", fieldLabel: "配件位置简称",  width: BuildUpPlace.fieldWidth, maxLength:20  },
            		{ id: "partID_formId1", xtype: "Base_combo",	fieldLabel: "位置",
						  hiddenName: "partID", returnField: [{widgetId:"partName_formId1",propertyName:"partName"}],
						  displayField: "partName", valueField: "partId",
						  entity: "com.yunda.jx.component.entity.EquipPart", 
						  fields: ["partId","partName"],
						  pageSize: 20, minListWidth: 200, width: BuildUpPlace.fieldWidth, editable: true },
				    { id:"partName_formId1", name:"partName", xtype: "hidden" }
            		            		
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
//2.2新增配件位置窗口--【组成维护】里面使用
BuildUpPlace.fixPlaceWin1 = new Ext.Window({
    title: "安装位置编辑", maximizable: true, width: 700, height: 350, layout: "fit", plain: true, closeAction: "hide", modal: true,
    items: [{
        xtype: "tabpanel", id:"fixPlace_Tabs1",  border: false, activeTab:0, enableTabScroll:true, items:[{
            id:"fixPlace_tabId1", title: "基本信息", layout: "fit", frame: true, border: false, items: BuildUpPlace.fixPlaceForm1
        },{
            id:"fixBuildUpType_tabId1", title: "可安装组成型号", layout: "fit", border: false, items: FixBuildUpType.fixBuildUpTypeGrid1
        }]
    }]
});
//2.3根据业务编码生成规则生成虚拟位置编码--【组成维护】里面使用
BuildUpPlace.getVirtualPlaceCode1 = function(){
	var url = ctx + "/codeRuleConfig!getConfigRule.action";
	Ext.Ajax.request({
                url: url,
                params: {ruleFunction: "JXGC_FIX_PLACE_Code"},
                success: function(response, options){
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        Ext.getCmp("buildUpPlaceCode_VId1").setValue(result.rule);
                    }
                },
                failure: function(response, options){
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
}
//2.3新增虚拟位置Form--【组成维护】里面使用
BuildUpPlace.virtualPlaceForm1 = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: BuildUpPlace.labelWidth, plain: true,
    baseCls: "x-plain", defaultType: "textfield", height:600,  frame: false, style: "padding:10px", buttonAlign: "center",
    buttons:[{
    	text:"保存", iconCls:"saveIcon",
        handler:function(){
        	//表单验证是否通过
        var form = BuildUpPlace.virtualPlaceForm1.getForm(); 
        if (!form.isValid()) return;
        Ext.getCmp("buildUpPlaceCode_VId1").enable();
        var data = form.getValues();
        Ext.getCmp("buildUpPlaceCode_VId1").disable();
        data.trainTypeIDX = trainTypeIDX;//trainTypeIDX值为在点击选择【车型组成】时及点击【设置组成】时设置
	    data.partsTypeIDX = partsTypeIDX;//partsTypeIDX值为在点击树节点时设置      
        
        var cfg = {
	            scope: this, url: ctx + '/buildUpPlace!savePlace.action', jsonData: data,
	            params:{parentIdx:parentIdx,buildUpTypeIDX:buildUpTypeIdx},//parentIdx、buildUpTypeIdx值为在点击树节点时设置
	            success: function(response, options){
	                if(self.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null && result.entity != null) {
	                    alertSuccess();
	                    Ext.getCmp("virtualPlace_Tabs1").getItem("virtualBuildUpType_tabId1").enable();
						Ext.getCmp("virtualPlace_Tabs1").activate("virtualBuildUpType_tabId1");
						fixPlaceIdx = result.entity.idx;//设置位置主键全局变量
						type = type_virtual;//设置组成类型全局变量-虚拟组成
						//过滤可安装虚拟组成型号列表
						FixBuildUpType.v_fixBuildUpTypeGrid1.getStore().load({
							params: {fixPlaceIdx : result.entity.idx} 
						});		
						//过滤虚拟组成型号选择列表
						FixBuildUpType.v_buildUpTypeGrid1.getStore().load({
	        				params: {fixPlaceIdx : result.entity.idx, type: type_virtual, trainTypeIDX: result.entity.trainTypeIDX}        			
	        			});
	        			//BuildUpPlace.grid.getStore().reload();
	                } else {
	                    alertFail(result.errMsg);
	                }
	            }
	        };
	        Ext.Ajax.request({
	        	url: ctx + "/buildUpPlace!checkReduplicateName.action",
	            jsonData: data,
	            success: function(response, options){	                    
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.success == true && result.errMsg != null) {
	                    Ext.Msg.confirm("提示  ", result.errMsg[0] + " 是否继续？  ", function(btn){
					        if(btn != 'yes')    return;
					        if(self.loadMask)   self.loadMask.show();
					        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
					    }); 
	                }else if(result.errMsg == null){
	                	if(self.loadMask)   self.loadMask.show();
					    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	                }
	            },
	            failure: function(response, options){
	                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	            }
	        });        
        }
    },{
        text: "关闭", iconCls: "closeIcon", handler: function(){ BuildUpPlace.virtualPlaceWin1.hide(); }
    }],
    items: [    	
    	{
    	xtype: "panel", border: false, anchor:"90%", baseCls: "x-plain", layout: "column", align: "center", plain: true,
    	items : [
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id:"buildUpPlaceCode_VId1", name: "buildUpPlaceCode", fieldLabel: "虚拟位置编码", allowBlank: false, disabled:true, width: BuildUpPlace.fieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: BuildUpPlace.fieldWidth, maxLength:50  },
            		{ 
            		  id: "ProfessionalType_form_VId1", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型",
					  hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_VformId1",propertyName:"text"}], 
					  selectNodeModel: "all",  width: BuildUpPlace.fieldWidth
            		},
            		{id:"professionalTypeName_VformId1", name:"professionalTypeName", xtype: 'hidden' }
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id: "buildUpPlaceName_VId1", name: "buildUpPlaceName", fieldLabel: "虚拟位置名称", allowBlank: false, width: BuildUpPlace.fieldWidth, maxLength: 50 },
            		{ name: "buildUpPlaceShortName", fieldLabel: "虚拟位置简称",  width: BuildUpPlace.fieldWidth, maxLength:20  },
            		{ id: "partID_VformId1", xtype: "Base_combo",	fieldLabel: "位置",
						  hiddenName: "partID", returnField: [{widgetId:"partName_VformId1",propertyName:"partName"}],
						  displayField: "partName", valueField: "partId",
						  entity: "com.yunda.jx.component.entity.EquipPart", 
						  fields: ["partId","partName"],
						  pageSize: 20, minListWidth: 200, width: BuildUpPlace.fieldWidth, editable: true },
				    { id:"partName_VformId1", name:"partName", xtype: "hidden" }
            		            		
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
//2.3新增虚拟位置窗口--【组成维护】里面使用
BuildUpPlace.virtualPlaceWin1 = new Ext.Window({
    title: "安装位置编辑", maximizable: true, width: 700, height: 350, layout: "fit", plain: true, closeAction: "hide", modal: true,
    items: [{
        xtype: "tabpanel", id:"virtualPlace_Tabs1",  border: false, activeTab:0, enableTabScroll:true, items:[{
            id:"virtualPlace_tabId1", title: "基本信息", layout: "fit", frame: true, border: false, items: BuildUpPlace.virtualPlaceForm1
        },{
            id:"virtualBuildUpType_tabId1", title: "可安装虚拟组成型号", layout: "fit", border: false, items: FixBuildUpType.v_fixBuildUpTypeGrid1
        }]
    }]
});
/***********************************3【虚拟组成维护】里面使用(3.1选择安装位置列表、3.2新增配件位置(基本信息form+可安装组成型号)、3.3新增虚拟位置(基本信息form+可安装组成型号))**************************************************************************/
//3.2根据业务编码生成规则生成安装位置编码--【虚拟组成维护】里面使用
BuildUpPlace.getFixPlaceCode2 = function(){
	var url = ctx + "/codeRuleConfig!getConfigRule.action";
	Ext.Ajax.request({
        url: url,
        params: {ruleFunction: "JXGC_FIX_PLACE_Code"},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                Ext.getCmp("buildUpPlaceCode_Id2").setValue(result.rule);
            }
        },
        failure: function(response, options){
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    });
}
//3.2新增配件位置Form--【虚拟组成维护】里面使用
BuildUpPlace.fixPlaceForm2 = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: BuildUpPlace.labelWidth, plain: true,
    baseCls: "x-plain", defaultType: "textfield", height:600,  frame: false, style: "padding:10px", buttonAlign: "center",
    buttons:[{
    	text:"保存", iconCls:"saveIcon",
        handler:function(){
        	//表单验证是否通过
	        var form = BuildUpPlace.fixPlaceForm2.getForm(); 
	        if (!form.isValid()) return;
	        Ext.getCmp("buildUpPlaceCode_Id2").enable();
	        var data = form.getValues();
	        Ext.getCmp("buildUpPlaceCode_Id2").disable();
	        data.trainTypeIDX = trainTypeIDX;//trainTypeIDX值为在点击选择【车型组成】时及点击【设置组成】时设置
	        data.partsTypeIDX = partsTypeIDX;//partsTypeIDX值为在点击树节点时设置
	        
	        var cfg = {
	            scope: this, url: ctx + '/buildUpPlace!savePlace.action', jsonData: data,
	            params:{parentIdx:parentIdx,buildUpTypeIDX:buildUpTypeIdx},//parentIdx、buildUpTypeIdx值为在点击树节点时设置
	            success: function(response, options){
	                if(self.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null && result.entity != null) {
	                    alertSuccess(); 
	                    Ext.getCmp("fixPlace_Tabs2").getItem("fixBuildUpType_tabId2").enable();
						Ext.getCmp("fixPlace_Tabs2").activate("fixBuildUpType_tabId2");
						fixPlaceIdx = result.entity.idx;//设置位置主键全局变量
						type = type_parts;//设置组成类型全局变量-配件组成	
						partsTypeIDX = result.entity.partsTypeIDX;//设置配件主键全局变量
						//过滤组成型号选择列表
						FixBuildUpType.buildUpTypeGrid2.store.baseParams.fixPlaceIdx = result.entity.idx;
						FixBuildUpType.buildUpTypeGrid2.store.baseParams.type = type_parts;
						FixBuildUpType.buildUpTypeGrid2.store.baseParams.partsTypeIDX = result.entity.partsTypeIDX; 
						FixBuildUpType.buildUpTypeGrid2.getStore().load({
	        				params: {fixPlaceIdx : result.entity.idx, type: type_parts, partsTypeIDX: result.entity.partsTypeIDX}        			
	        			});
	        			//过滤可安装组成型号列表
	        			FixBuildUpType.fixBuildUpTypeGrid2.store.baseParams.fixPlaceIdx = result.entity.idx;
						FixBuildUpType.fixBuildUpTypeGrid2.getStore().load({
							params: {fixPlaceIdx : result.entity.idx} 
						});
	        			//BuildUpPlace.grid.getStore().reload();
	                } else {
	                    alertFail(result.errMsg);
	                }
	            }
	        };
	        //验证是否有重复位置名称
	        Ext.Ajax.request({
	        	url: ctx + "/buildUpPlace!checkReduplicateName.action",
	            jsonData: data,
	            success: function(response, options){	                    
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.success == true && result.errMsg != null) {
	                    Ext.Msg.confirm("提示  ", result.errMsg[0] + " 是否继续？  ", function(btn){
					        if(btn != 'yes')    return;
					        if(self.loadMask)   self.loadMask.show();
					        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
					    }); 
	                }else if(result.errMsg == null){
	                	if(self.loadMask)   self.loadMask.show();
					    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	                }
	            },
	            failure: function(response, options){
	                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	            }
	        });        
        }
    },{
        text: "关闭", iconCls: "closeIcon", handler: function(){ BuildUpPlace.fixPlaceWin2.hide(); }
    }],
    items: [    	
    	{
    	xtype: "panel", border: false, anchor:"90%", baseCls: "x-plain", layout: "column", align: "center", plain: true,
    	items : [
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id:"buildUpPlaceCode_Id2", name: "buildUpPlaceCode", fieldLabel: "配件位置编码", allowBlank: false, disabled:true, width: BuildUpPlace.fieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: BuildUpPlace.fieldWidth, maxLength:50  },
            		{ 
            		  id: "ProfessionalType_grid_Id2", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型", 
					  hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_grid_Id2",propertyName:"text"}], 
					  selectNodeModel: "all",  width: BuildUpPlace.fieldWidth
            		},
            		{id:"professionalTypeName_grid_Id2", name:"professionalTypeName", xtype: 'hidden' }
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id: "buildUpPlaceName_Id2", name: "buildUpPlaceName", fieldLabel: "配件位置名称", allowBlank: false, width: BuildUpPlace.fieldWidth, maxLength: 50 },
            		{ name: "buildUpPlaceShortName", fieldLabel: "配件位置简称",  width: BuildUpPlace.fieldWidth, maxLength:20  },
            		{ id: "partID_grid_Id2", xtype: "Base_combo",	fieldLabel: "位置",
						  hiddenName: "partID", returnField: [{widgetId:"partName_grid_Id2",propertyName:"partName"}],
						  displayField: "partName", valueField: "partId",
						  entity: "com.yunda.jx.component.entity.EquipPart", 
						  fields: ["partId","partName"],
						  pageSize: 20, minListWidth: 200, width: BuildUpPlace.fieldWidth, editable: true },
				    { id:"partName_grid_Id2", name:"partName", xtype: "hidden" }
            		            		
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
//3.2新增配件位置窗口--【虚拟组成维护】里面使用
BuildUpPlace.fixPlaceWin2 = new Ext.Window({
    title: "安装位置编辑", maximizable: true, width: 700, height: 350, layout: "fit", plain: true, closeAction: "hide", modal: true,
    items: [{
        xtype: "tabpanel", id:"fixPlace_Tabs2",  border: false, activeTab:0, enableTabScroll:true, items:[{
            id:"fixPlace_tabId2", title: "基本信息", layout: "fit", frame: true, border: false, items: BuildUpPlace.fixPlaceForm2
        },{
            id:"fixBuildUpType_tabId2", title: "可安装组成型号", layout: "fit", border: false, items: FixBuildUpType.fixBuildUpTypeGrid2
        }]
    }]
});
//3.3根据业务编码生成规则生成虚拟位置编码--【虚拟组成维护】里面使用
BuildUpPlace.getVirtualPlaceCode2 = function(){
	var url = ctx + "/codeRuleConfig!getConfigRule.action";
	Ext.Ajax.request({
                url: url,
                params: {ruleFunction: "JXGC_FIX_PLACE_Code"},
                success: function(response, options){
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        Ext.getCmp("buildUpPlaceCode_VId2").setValue(result.rule);
                    }
                },
                failure: function(response, options){
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
}
//3.3新增虚拟位置Form--【虚拟组成维护】里面使用
BuildUpPlace.virtualPlaceForm2 = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: BuildUpPlace.labelWidth, plain: true,
    baseCls: "x-plain", defaultType: "textfield", height:600,  frame: false, style: "padding:10px", buttonAlign: "center",
    buttons:[{
    	text:"保存", iconCls:"saveIcon",
        handler:function(){
        	//表单验证是否通过
        var form = BuildUpPlace.virtualPlaceForm2.getForm(); 
        if (!form.isValid()) return;
        Ext.getCmp("buildUpPlaceCode_VId2").enable();
        var data = form.getValues();
        Ext.getCmp("buildUpPlaceCode_VId2").disable();
        data.trainTypeIDX = trainTypeIDX;//trainTypeIDX值为在点击选择【车型组成】时及点击【设置组成】时设置
	    data.partsTypeIDX = partsTypeIDX;//partsTypeIDX值为在点击树节点时设置      
        
        var cfg = {
	            scope: this, url: ctx + '/buildUpPlace!savePlace.action', jsonData: data,
	            params:{parentIdx:parentIdx,buildUpTypeIDX:buildUpTypeIdx},//parentIdx、buildUpTypeIdx值为在点击树节点时设置
	            success: function(response, options){
	                if(self.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null && result.entity != null) {
	                    alertSuccess();
	                    Ext.getCmp("virtualPlace_Tabs2").getItem("virtualBuildUpType_tabId2").enable();
						Ext.getCmp("virtualPlace_Tabs2").activate("virtualBuildUpType_tabId2");
						fixPlaceIdx = result.entity.idx;//设置位置主键全局变量
						type = type_virtual;//设置组成类型全局变量-虚拟组成
						//过滤可安装虚拟组成型号列表
						FixBuildUpType.v_fixBuildUpTypeGrid12.store.baseParams.fixPlaceIdx = result.entity.idx;
						FixBuildUpType.v_fixBuildUpTypeGrid12.getStore().load({
							params: {fixPlaceIdx : result.entity.idx} 
						});		
						//过滤虚拟组成型号选择列表
						FixBuildUpType.v_buildUpTypeGrid12.store.baseParams.fixPlaceIdx = result.entity.idx;
						FixBuildUpType.v_buildUpTypeGrid12.store.baseParams.type = type_virtual;
						FixBuildUpType.v_buildUpTypeGrid12.store.baseParams.trainTypeIDX = result.entity.trainTypeIDX; 
						FixBuildUpType.v_buildUpTypeGrid12.getStore().load({
	        				params: {fixPlaceIdx : result.entity.idx, type: type_virtual, trainTypeIDX: result.entity.trainTypeIDX}        			
	        			});
	        			//BuildUpPlace.grid.getStore().reload();
	                } else {
	                    alertFail(result.errMsg);
	                }
	            }
	        };
	        Ext.Ajax.request({
	        	url: ctx + "/buildUpPlace!checkReduplicateName.action",
	            jsonData: data,
	            success: function(response, options){	                    
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.success == true && result.errMsg != null) {
	                    Ext.Msg.confirm("提示  ", result.errMsg[0] + " 是否继续？  ", function(btn){
					        if(btn != 'yes')    return;
					        if(self.loadMask)   self.loadMask.show();
					        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
					    }); 
	                }else if(result.errMsg == null){
	                	if(self.loadMask)   self.loadMask.show();
					    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	                }
	            },
	            failure: function(response, options){
	                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	            }
	        });        
        }
    },{
        text: "关闭", iconCls: "closeIcon", handler: function(){ BuildUpPlace.virtualPlaceWin2.hide(); }
    }],
    items: [    	
    	{
    	xtype: "panel", border: false, anchor:"90%", baseCls: "x-plain", layout: "column", align: "center", plain: true,
    	items : [
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id:"buildUpPlaceCode_VId2", name: "buildUpPlaceCode", fieldLabel: "虚拟位置编码", allowBlank: false, disabled:true, width: BuildUpPlace.fieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: BuildUpPlace.fieldWidth, maxLength:50  },
            		{ 
            		  id: "ProfessionalType_form_VId2", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型",
					  hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_VformId2",propertyName:"text"}], 
					  selectNodeModel: "all",  width: BuildUpPlace.fieldWidth
            		},
            		{id:"professionalTypeName_VformId2", name:"professionalTypeName", xtype: 'hidden' }
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id: "buildUpPlaceName_VId2", name: "buildUpPlaceName", fieldLabel: "虚拟位置名称", allowBlank: false, width: BuildUpPlace.fieldWidth, maxLength: 50 },
            		{ name: "buildUpPlaceShortName", fieldLabel: "虚拟位置简称",  width: BuildUpPlace.fieldWidth, maxLength:20  },
            		{ id: "partID_VformId2", xtype: "Base_combo",	fieldLabel: "位置",
						  hiddenName: "partID", returnField: [{widgetId:"partName_VformId2",propertyName:"partName"}],
						  displayField: "partName", valueField: "partId",
						  entity: "com.yunda.jx.component.entity.EquipPart", 
						  fields: ["partId","partName"],
						  pageSize: 20, minListWidth: 200, width: BuildUpPlace.fieldWidth, editable: true },
				    { id:"partName_VformId2", name:"partName", xtype: "hidden" }
            		            		
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
//3.3新增虚拟位置窗口--【虚拟组成维护】里面使用
BuildUpPlace.virtualPlaceWin2 = new Ext.Window({
    title: "安装位置编辑", maximizable: true, width: 700, height: 350, layout: "fit", plain: true, closeAction: "hide", modal: true,
    items: [{
        xtype: "tabpanel", id:"virtualPlace_Tabs2",  border: false, activeTab:0, enableTabScroll:true, items:[{
            id:"virtualPlace_tabId2", title: "基本信息", layout: "fit", frame: true, border: false, items: BuildUpPlace.virtualPlaceForm2
        },{
            id:"virtualBuildUpType_tabId2", title: "可安装虚拟组成型号", layout: "fit", border: false, items: FixBuildUpType.v_fixBuildUpTypeGrid12
        }]
    }]
});
//3.1虚拟组成设置-安装位置选择列表--【虚拟组成维护】里面使用
BuildUpPlace.v_selectGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpPlace!placeList.action',                 //装载列表数据的请求URL
    saveFormColNum: 2,
    //remoteSort: false,
    tbar: [{
        	xtype:"label", text:"位置名称：" 
    	},{			
            xtype: "textfield",    
            id: "placeName_vsearchId",
            width:70
		},{
        	xtype:"label", text:"图号：" 
    	},{			
            xtype: "textfield",    
            id: "chartNo_vsearchId",
            width:70
		},{
        	xtype:"label", text:"是否安装实物配件：" 
    	},{
	        xtype: 'combo',
	        id: 'visFix',
            fieldLabel: '',
            width:50,
            hiddenName:'',
            store:new Ext.data.SimpleStore({
			    fields: ['v', 't'],
				data : [['','全部'],[fix_place,'是'],[virtual_place,'否']]
			}),
			valueField:'v',
			displayField:'t',
			triggerAction:'all',
			mode:'local',
			value:'',
			editable: false

	    },{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var placeName = Ext.getCmp("placeName_vsearchId").getValue();
				var chartNo = Ext.getCmp("chartNo_vsearchId").getValue();
				var isFix = Ext.getCmp("visFix").getValue();
				
				BuildUpPlace.searchParams.buildUpPlaceName = placeName;
				BuildUpPlace.searchParams.chartNo = chartNo;
				BuildUpPlace.v_selectGrid.store.baseParams.placeTypes = fix_place + "," + virtual_place;//位置类型：安装位置和虚拟位置
				if(trainTypeIDX != ''){
					BuildUpPlace.v_selectGrid.store.baseParams.trainTypeIDX = trainTypeIDX;//车型
					BuildUpPlace.searchParams.trainTypeIDX = trainTypeIDX;//车型
				}
				if(partsTypeIDX != ''){
					BuildUpPlace.v_selectGrid.store.baseParams.partsTypeIDX = partsTypeIDX;//配件型号
					BuildUpPlace.searchParams.partsTypeIDX = partsTypeIDX;//配件型号
				}
				if(buildUpTypeIdx != ''){
					BuildUpPlace.v_selectGrid.store.baseParams.buildUpTypeIdx = buildUpTypeIdx;
				}
				BuildUpPlace.v_selectGrid.store.baseParams.isFix = isFix;
				BuildUpPlace.v_selectGrid.store.baseParams.entityJson = Ext.util.JSON.encode(BuildUpPlace.searchParams);
				BuildUpPlace.v_selectGrid.getStore().load();
			},			
			width : 40
		},{
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
//3.1移除侦听器
BuildUpPlace.v_selectGrid.un('rowdblclick', BuildUpPlace.v_selectGrid.toEditFn, BuildUpPlace.v_selectGrid);
//3.1过滤虚拟组成设置-安装位置选择列表
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