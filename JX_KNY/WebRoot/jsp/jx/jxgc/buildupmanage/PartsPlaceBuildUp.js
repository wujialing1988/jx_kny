/**
 * 配件组成-设置组成 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsPlaceBuildUp');                       //定义命名空间
PartsPlaceBuildUp.labelWidth = 80;
PartsPlaceBuildUp.fieldWidth = 140;
//结构位置信息tab上form表单
PartsPlaceBuildUp.structurePlaceForm = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: PartsPlaceBuildUp.labelWidth, plain: true,
    baseCls: "x-plain", defaultType: "textfield", height:600,  frame: false, style: "padding:10px", buttonAlign: "center",
    buttons:[{
    	text:"保存", iconCls:"saveIcon",
        handler:function(){
        	//表单验证是否通过
	        var form = PartsPlaceBuildUp.structurePlaceForm.getForm(); 
	        if (!form.isValid()) return; 
	        Ext.getCmp("structureCode_Id").enable();
	        var data = form.getValues();
	        delete data["buildUpPlaceFullName"];
	        Ext.getCmp("structureCode_Id").disable();
	        data.partsTypeIDX = partsTypeIDX;
	        var cfg = {
	            scope: this, url: ctx + '/buildUpPlace!saveStructure.action', jsonData: data,
	            params:{parentIdx:parentIdx,buildUpTypeIDX:partsBuildUpTypeIdx},
	            success: function(response, options){
	                if(self.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null && result.entity != null) {
	                    alertSuccess();                    
	                } else {
	                    alertFail(result.errMsg);
	                }
	            }
	        };
	        Ext.Ajax.request({
	        	url: ctx + "/buildUpPlace!checkReduplicateName.action",
	            jsonData: data,
	            params: {buildUpTypeIDX:buildUpTypeIdx},
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
    }],
    items: [
    	{
	    	xtype: "panel", border: false,  baseCls: "x-plain", layout: "form", align: "center", defaultType: "textfield",
	    	items : [{
	    		id:"buildUpPlaceFullName_Id", name: "buildUpPlaceFullName", fieldLabel: "结构位置全名",  disabled: true, width: 380 
	    	}]
	    },
    	{
    	xtype: "panel", border: false, anchor:"90%", baseCls: "x-plain", layout: "column", align: "center", plain: true,
    	items : [
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id:"structureCode_Id", name: "buildUpPlaceCode", fieldLabel: "结构位置编码", allowBlank: false, disabled:true, width: PartsPlaceBuildUp.fieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: PartsPlaceBuildUp.fieldWidth, maxLength:50  },
            		{ 
            		  id: "ProfessionalType_form_SId", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型",
					  hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_form_SId",propertyName:"text"}], 
					  selectNodeModel: "all",  width: PartsPlaceBuildUp.fieldWidth
            		},
            		{id:"professionalTypeName_form_SId", name:"professionalTypeName", xtype: 'hidden' },
            		{ name: "buildUpPlaceSEQ", fieldLabel:"序号", maxLength: 8, xtype:"numberfield", vtype: "nonNegativeInt", width: PartsPlaceBuildUp.fieldWidth} 
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id: "fixPlaceName_SId", name: "buildUpPlaceName", fieldLabel: "结构位置名称", allowBlank: false, width: PartsPlaceBuildUp.fieldWidth, maxLength: 50 },
            		{ name: "buildUpPlaceShortName", fieldLabel: "结构位置简称",  width: PartsPlaceBuildUp.fieldWidth, maxLength:20  },
            		{ id: "partID_form_SId", xtype: "Base_combo",	fieldLabel: "位置",
						  hiddenName: "partID", returnField: [{widgetId:"partName_form_SId",propertyName:"partName"}],
						  displayField: "partName", valueField: "partId",
						  entity: "com.yunda.jx.component.entity.EquipPart", 
						  fields: ["partId","partName"],
						  pageSize: 20, minListWidth: 200, width: PartsPlaceBuildUp.fieldWidth, editable: true },
				    { id:"partName_form_SId", name:"partName", xtype: "hidden" }
            		           		
            	]
    		},
    		{ name: "idx",xtype: "hidden"},
    		{ name: "remarks",xtype: "hidden"},
    		{ name: "placeType",xtype: "hidden", value:structure_place},
    		{ name: "partsTypeIDX",xtype: "hidden", value:partsTypeIDX}
    	]}     
    ]
});
//配件位置信息
PartsPlaceBuildUp.partsPlaceForm = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: PartsPlaceBuildUp.labelWidth, plain: true,
    baseCls: "x-plain", defaultType: "textfield", height:600,  frame: false, style: "padding:10px", buttonAlign: "center",
    buttons:[{
    	text:"保存", iconCls:"saveIcon",
        handler:function(){
        	//表单验证是否通过
	        var form = PartsPlaceBuildUp.partsPlaceForm.getForm(); 
	        if (!form.isValid()) return; 
	        Ext.getCmp("BuildUpPlace_Code").enable();
	        var data = form.getValues();
	        delete data["buildUpPlaceFullName"];
	        Ext.getCmp("BuildUpPlace_Code").disable();
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
	                } else {
	                    alertFail(result.errMsg);
	                }
	            }
	        };
	        Ext.Ajax.request({
	        	url: ctx + "/buildUpPlace!checkReduplicateName.action",
	            jsonData: data,
	            params: {buildUpTypeIDX:buildUpTypeIdx},
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
    }],
    items: [
    	{
	    	xtype: "panel", border: false,  baseCls: "x-plain", layout: "form", align: "center", defaultType: "textfield",
	    	items : [{
	    		id: "BuildUpPlace_FullName", name: "buildUpPlaceFullName", fieldLabel: "配件位置全名",  disabled: true, width: 380 
	    	}]
	    },
    	{
    	xtype: "panel", border: false, anchor:"90%", baseCls: "x-plain", layout: "column", align: "center", plain: true,
    	items : [
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id: "BuildUpPlace_Code", name: "buildUpPlaceCode", fieldLabel: "配件位置编码", allowBlank: false, disabled:true, width: PartsPlaceBuildUp.fieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: PartsPlaceBuildUp.fieldWidth  },
            		{ 
            		  id: "ProfessionalType_form_Id2", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型", 
					  hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_formId2",propertyName:"text"}], 
					  selectNodeModel: "all",  width: PartsPlaceBuildUp.fieldWidth
            		},
            		{id:"professionalTypeName_formId2", name:"professionalTypeName", xtype: 'hidden' }
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id: "BuildUpPlace_Name", name: "buildUpPlaceName", fieldLabel: "配件位置名称", width: PartsPlaceBuildUp.fieldWidth},
            		{ name: "buildUpPlaceShortName", fieldLabel: "配件位置简称", width: PartsPlaceBuildUp.fieldWidth },
				    { id: "partID_formId2", xtype: "Base_combo",	fieldLabel: "位置",
						  hiddenName: "partID", returnField: [{widgetId:"partName_formId2",propertyName:"partName"}],
						  displayField: "partName", valueField: "partId",
						  entity: "com.yunda.jx.component.entity.EquipPart", 
						  fields: ["partId","partName"],
						  pageSize: 20, minListWidth: 200, width: PartsPlaceBuildUp.fieldWidth, editable: true },
				    { id:"partName_formId2", name:"partName", xtype: "hidden" }
            	]
    		},
    		{ name: "idx",xtype: "hidden"},
    		{ name: "remarks",xtype: "hidden"},
    		{ name: "placeType",xtype: "hidden", value:fix_place}
    	]}     
    ]
});
//安装位置名称修改联动修改安装位置全名
Ext.getCmp("BuildUpPlace_Name").on("change",function(field,newValue,oldValue){
	var oldFullName = Ext.getCmp("BuildUpPlace_FullName").getValue();
	var index = oldFullName.lastIndexOf(oldValue);	
	Ext.getCmp("BuildUpPlace_FullName").setValue(oldFullName.substr(0,index) + newValue);
});
//虚拟位置信息
PartsPlaceBuildUp.virtualPlaceForm = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: PartsPlaceBuildUp.labelWidth, plain: true,
    baseCls: "x-plain", defaultType: "textfield", height:600,  frame: false, style: "padding:10px", buttonAlign: "center",
    buttons:[{
    	text:"保存", iconCls:"saveIcon",
        handler:function(){
        	//表单验证是否通过
	        var form = PartsPlaceBuildUp.virtualPlaceForm.getForm(); 
	        if (!form.isValid()) return; 
	        Ext.getCmp("v_BuildUpPlace_Code").enable();
	        var data = form.getValues();
	        delete data["buildUpPlaceFullName"];
	        Ext.getCmp("v_BuildUpPlace_Code").disable();
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
	                } else {
	                    alertFail(result.errMsg);
	                }
	            }
	        };
	        Ext.Ajax.request({
	        	url: ctx + "/buildUpPlace!checkReduplicateName.action",
	            jsonData: data,
	            params: {buildUpTypeIDX:buildUpTypeIdx},
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
    }],
    items: [
    	{
	    	xtype: "panel", border: false,  baseCls: "x-plain", layout: "form", align: "center", defaultType: "textfield",
	    	items : [{
	    		id: "v_BuildUpPlace_FullName", name: "buildUpPlaceFullName", fieldLabel: "虚拟位置全名",  disabled: true, width: 380 
	    	}]
	    },
    	{
    	xtype: "panel", border: false, anchor:"90%", baseCls: "x-plain", layout: "column", align: "center", plain: true,
    	items : [
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id: "v_BuildUpPlace_Code", name: "buildUpPlaceCode", fieldLabel: "虚拟位置编码", allowBlank: false, disabled:true, width: PartsPlaceBuildUp.fieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: PartsPlaceBuildUp.fieldWidth  },
            		{ 
            		  id: "v_ProfessionalType_form_Id2", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型", 
					  hiddenName: "professionalTypeIDX", returnField: [{widgetId:"v_professionalTypeName_formId2",propertyName:"text"}], 
					  selectNodeModel: "all",  width: PartsPlaceBuildUp.fieldWidth
            		},
            		{id:"v_professionalTypeName_formId2", name:"professionalTypeName", xtype: 'hidden' }
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id: "v_BuildUpPlace_Name", name: "buildUpPlaceName", fieldLabel: "虚拟位置名称", width: PartsPlaceBuildUp.fieldWidth},
            		{ name: "buildUpPlaceShortName", fieldLabel: "虚拟位置简称", width: PartsPlaceBuildUp.fieldWidth },
				    { id: "v_partID_formId2", xtype: "Base_combo",	fieldLabel: "位置",
						  hiddenName: "partID", returnField: [{widgetId:"v_partName_formId2",propertyName:"partName"}],
						  displayField: "partName", valueField: "partId",
						  entity: "com.yunda.jx.component.entity.EquipPart", 
						  fields: ["partId","partName"],
						  pageSize: 20, minListWidth: 200, width: PartsPlaceBuildUp.fieldWidth, editable: true },
				    { id:"v_partName_formId2", name:"partName", xtype: "hidden" }               		
            	]
    		},
    		{ name: "idx",xtype: "hidden"},
    		{ name: "remarks",xtype: "hidden"},
    		{ name: "placeType",xtype: "hidden", value:virtual_place}
    	]}     
    ]
});
//安装位置名称修改联动修改安装位置全名
Ext.getCmp("v_BuildUpPlace_Name").on("change",function(field,newValue,oldValue){
	var oldFullName = Ext.getCmp("v_BuildUpPlace_FullName").getValue();
	var index = oldFullName.lastIndexOf(oldValue);	
	Ext.getCmp("v_BuildUpPlace_FullName").setValue(oldFullName.substr(0,index) + newValue);
});
//根据业务编码生成规则生成结构位置编码
PartsPlaceBuildUp.getStructurePlaceCode = function(){
	var url = ctx + "/codeRuleConfig!getConfigRule.action";
	Ext.Ajax.request({
                url: url,
                params: {ruleFunction: "JXGC_FIX_PLACE_Code"},
                success: function(response, options){
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        Ext.getCmp("buildUpPlaceCode_SId").setValue(result.rule);
                    }
                },
                failure: function(response, options){
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
}
//下级结构位置列表
PartsPlaceBuildUp.childPartsBuildUpGrid =  new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpPlace!childPlaceList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/buildUpPlace!saveStructure.action',             //保存数据的请求URL
    deleteURL: ctx + '/placeBuildUpType!deleteStructure.action',            //删除数据的请求URL
    saveFormColNum: 2,
    storeAutoLoad: false,
    tbar: ['add','delete','refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'结构位置编码', dataIndex:'buildUpPlaceCode', editor:{ id:'buildUpPlaceCode_SId', disabled: true, maxLength:10 }
	},{
		header:'结构位置名称', dataIndex:'buildUpPlaceName', editor:{  maxLength:50 }
	},{
		header:'位置简称', dataIndex:'buildUpPlaceShortName', editor:{  maxLength:20 }
	},{
		header:'图号', dataIndex:'chartNo', editor:{  maxLength:50 }
	},{
		header:'配件专业类型表主键', dataIndex:'professionalTypeIDX', hidden: true, 
		editor:{
			id: "ProfessionalType_SId", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型", 
			hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_SId",propertyName:"text"}], 
			selectNodeModel: "all"
		}
	},{
		header:'专业类型名称', dataIndex:'professionalTypeName', editor:{ id:'professionalTypeName_SId', xtype:'hidden' }
	},{
		header:'位置编码', dataIndex:'partID', hidden:true, 
		editor:{
			  id: "partID_SId", xtype: "Base_combo",	fieldLabel: "位置",
			  hiddenName: "partID", returnField: [{widgetId:"partName_SId",propertyName:"partName"}],
			  displayField: "partName", valueField: "partId",
			  entity: "com.yunda.jx.component.entity.EquipPart", 
			  fields: ["partId","partName"],
			  pageSize: 20, minListWidth: 200, editable: true
		}
	},{
		header:'位置', dataIndex:'partName', 
		editor:{
			id:"partName_SId", xtype:"hidden"
		}
	},{
		header:'位置类型', dataIndex:'placeType', 
		hidden: true,
		editor:{ xtype:'hidden', value: structure_place}
	},{
		header:'备注', dataIndex:'remarks', hidden:true, editor:{ xtype:'hidden' }
	},{
		header:'状态', dataIndex:'status', hidden:true, editor:{ xtype:'hidden'}
	},{
		header:'序号', dataIndex:'buildUpPlaceSEQ',  editor:{ xtype:'numberfield', maxLength:8, vtype: "nonNegativeInt" }
	},{
		header:'配件型号', dataIndex:'partsTypeIDX', hidden:true, editor:{ xtype:'hidden', value: partsTypeIDX }
	}],
	/**
     * 显示新增窗口后触发该函数，该函数依赖saveFn，若默认saveFn被覆盖则失效
     */
    afterShowSaveWin: function(){
    	PartsPlaceBuildUp.getStructurePlaceCode();
    },	
    /**
     * 获取表单数据前触发(覆盖该函数可用于修改表单字段enable、disabled等)，
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     */
    beforeGetFormData: function(){
    	Ext.getCmp("buildUpPlaceCode_SId").enable();
    },
    /**
     * 获取表单数据后触发(覆盖该函数可用于修改表单字段enable、disabled等)，
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     */    
    afterGetFormData: function(){
    	Ext.getCmp("buildUpPlaceCode_SId").disable();
    },
	/**
     * 新增编辑窗口保存按钮触发的函数，执行数据数据保存动作
     */
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
        data.partsTypeIDX = partsTypeIDX;
        var cfg = {
            scope: this, url: this.saveURL, jsonData: data,
            params:{parentIdx:parentIdx,buildUpTypeIDX:partsBuildUpTypeIdx},
            success: function(response, options){
                if(self.loadMask)   self.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    this.afterSaveSuccessFn(result, response, options);
                    //PartsBuild.expandNode(nodeId, parentIdx, parentNodeId);
                } else {
                    this.afterSaveFailFn(result, response, options);
                }
            }
        };
        Ext.Ajax.request({
        	url: ctx + "/buildUpPlace!checkReduplicateName.action",
            jsonData: data,
            params: {buildUpTypeIDX:buildUpTypeIdx},
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
        
    },
    /**
     * 选中记录加载到编辑表单，显示编辑窗口后触发该函数
     * 该函数依赖toEditFn，若默认toEditFn被覆盖则失效
     * @param {Ext.data.Record} record 当前选中记录
     * @param {Number} rowIndex 选中行下标
     */
    afterShowEditWin: function(record, rowIndex){
    	Ext.getCmp("ProfessionalType_SId").setDisplayValue(record.get("professionalTypeIDX"),record.get("professionalTypeName"));
    	Ext.getCmp("partID_SId").setDisplayValue(record.get("partID"),record.get("partName"));
    },
    /**
     * 执行删除操作后触发的函数，
     * 该函数依赖deleteButtonFn，如果deleteButtonFn被覆盖则失效
     * @return {Boolean}
     */
    afterDeleteFn: function(){ 
    	//PartsBuild.expandNode(nodeId, parentIdx, parentNodeId);
    }
});
//移除侦听器
PartsPlaceBuildUp.childPartsBuildUpGrid.un('rowdblclick', PartsPlaceBuildUp.childPartsBuildUpGrid.toEditFn, PartsPlaceBuildUp.childPartsBuildUpGrid);
//新增编辑窗口设为模态
PartsPlaceBuildUp.childPartsBuildUpGrid.createSaveWin();
PartsPlaceBuildUp.childPartsBuildUpGrid.saveWin.modal = true;
//选择安装位置窗口
PartsPlaceBuildUp.buildUpPlaceWin = new Ext.Window({
    title: "安装位置选择", width: 700, height: 400, layout: "fit", plain: true, closeAction: "hide", modal: true,
    items: [BuildUpPlace.selectGrid]
});
//下级安装位置
PartsPlaceBuildUp.childPartsFixGrid =  new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpPlace!childPlaceList.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/placeBuildUpType!deletePlace.action',            //删除数据的请求URL
    saveFormColNum: 2,
    tbar: [/*{
    	text : "选择安装位置",
		iconCls : "addIcon",
		handler : function(){
			PartsPlaceBuildUp.buildUpPlaceWin.show();
			BuildUpPlace.selectGrid.store.baseParams.placeTypes = fix_place + "," + virtual_place;//位置类型：安装位置和虚拟位置
			BuildUpPlace.searchParams.partsTypeIDX = partsTypeIDX;//配件类型
			BuildUpPlace.selectGrid.store.baseParams.entityJson = Ext.util.JSON.encode(BuildUpPlace.searchParams);
			BuildUpPlace.selectGrid.store.load();
		}    
    },*/{
    	text : "新增配件位置",
		iconCls : "addIcon",
		handler : function(){
			BuildUpPlace.fixPlaceWin1.show();
			Ext.getCmp("fixPlace_Tabs1").getItem("fixBuildUpType_tabId1").disable();
			Ext.getCmp("fixPlace_Tabs1").activate("fixPlace_tabId1");
			BuildUpPlace.fixPlaceForm1.getForm().reset();
			Ext.getCmp("ProfessionalType_form_Id1").clearValue();
			Ext.getCmp("partID_formId1").clearValue();
			BuildUpPlace.getFixPlaceCode1();
		}    
    },{
    	text : "新增虚拟位置",
		iconCls : "addIcon",
		id : "virtualPlace_button",
		handler : function(){
			BuildUpPlace.virtualPlaceWin1.show();
			Ext.getCmp("virtualPlace_Tabs1").getItem("virtualBuildUpType_tabId1").disable();
			Ext.getCmp("virtualPlace_Tabs1").activate("virtualPlace_tabId1");
			BuildUpPlace.virtualPlaceForm1.getForm().reset();
			Ext.getCmp("ProfessionalType_form_VId1").clearValue();
			Ext.getCmp("partID_VformId1").clearValue();
			BuildUpPlace.getVirtualPlaceCode1();			
		}    
    },'delete'],
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
		header:'是否安装实物件', dataIndex:'placeType', 
		renderer: function(v){if(v==fix_place)return "是";else return "否";},
		editor:{ xtype:'hidden' }
	},{
		header:'备注', dataIndex:'remarks', hidden:true, editor:{ xtype:'textarea', maxLength:1000 }
	},{
		header:'状态', dataIndex:'status', hidden:true, editor:{ xtype:'hidden', maxLength:2 }
	}],
	/**
     * 执行删除操作后触发的函数，
     * 该函数依赖deleteButtonFn，如果deleteButtonFn被覆盖则失效
     * @return {Boolean}
     */
    afterDeleteFn: function(){ 
    	//PartsBuild.expandNode(nodeId, parentIdx, parentNodeId);
    }
});
//移除侦听器
PartsPlaceBuildUp.childPartsFixGrid.un('rowdblclick', PartsPlaceBuildUp.childPartsFixGrid.toEditFn, PartsPlaceBuildUp.childPartsFixGrid);	
//tab选项卡布局
PartsPlaceBuildUp.tabs = new Ext.TabPanel({
    activeTab: 0,
    frame:true,
    singleSelect: true,
    items:[{  
           id: "structurePlaceTab",
           title: '结构位置信息',
           layout:'fit',
           frame:true,
           items: [PartsPlaceBuildUp.structurePlaceForm]
        },{  
           id: "partsPlaceTab",
           title: '配件位置信息',
           layout:'fit',
           frame:true,
           items: [PartsPlaceBuildUp.partsPlaceForm]
        },{  
           id: "virtualPlaceTab",
           title: '虚拟位置信息',
           layout:'fit',
           frame:true,
           items: [PartsPlaceBuildUp.virtualPlaceForm]
        },{ 
          id: "childPartsBuildUpTab",
          title: '下级结构位置',
          layout: 'fit' ,
          items: [PartsPlaceBuildUp.childPartsBuildUpGrid]
       },{ 
          id: "childPartsFixTab",
          title: '下级安装位置',
          layout: 'fit' ,
          items: [PartsPlaceBuildUp.childPartsFixGrid]
       },{ 
          id: "fixBuildUpTypeTab",
          title: '可安装组成型号',
          frame:true,
          layout: 'fit' ,
          items: [FixBuildUpType.fixBuildUpTypeQueryGrid]
       },{ 
          id: "faultTab",
          title: '故障现象',
          frame:true,
          layout: 'fit' ,
          items: [PlaceFault.faultPanel]
       }]
});
//页面布局
PartsPlaceBuildUp.viewport = new Ext.Panel( {
    layout : 'border',
    items : [ {
        title : '<span style="font-weight:normal"></span>',
        iconCls : 'chart_organisationIcon',
        tools : [ {
            id : 'refresh',
            handler : function() {
                PartsBuild.tree.root.reload();
                PartsBuild.tree.getRootNode().expand();
            }
        } ],
        collapsible : true,
        width : 210,
//        minSize : 160,
//        maxSize : 280,
        split : true,
        region : 'west',
        bodyBorder: false,
        autoScroll : true,
        containerScroll : true,
        items : [ PartsBuild.tree ]
    }, {
        region : 'center',
        layout : 'fit',
        bodyBorder: false,
        items : [ PartsPlaceBuildUp.tabs ]
    } ]
});
//配件组成维护窗口
PartsPlaceBuildUp.win =  new Ext.Window({
    title: "配件组成维护", maximizable :true, width: 705, height: 400, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center',
    items: [PartsPlaceBuildUp.viewport],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ PartsPlaceBuildUp.win.hide(); }
            }]
});

});