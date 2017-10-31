/**
 * 机车组成-设置组成 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainPlaceBuildUp');                       //定义命名空间
TrainPlaceBuildUp.labelWidth = 80;
TrainPlaceBuildUp.fieldWidth = 140;
//结构位置信息tab上form表单
TrainPlaceBuildUp.structurePlaceForm = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: TrainPlaceBuildUp.labelWidth, plain: true,
    baseCls: "x-plain", defaultType: "textfield", height:600,  frame: false, style: "padding:10px", buttonAlign: "center",
    
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
            		{ id:"structureCode_Id", name: "buildUpPlaceCode", fieldLabel: "结构位置编码", allowBlank: false, disabled:true, width: TrainPlaceBuildUp.fieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: TrainPlaceBuildUp.fieldWidth, maxLength:50, disabled: true  },
            		{ 
            		  id: "ProfessionalType_form_SId", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型", disabled: true,
					  hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_form_SId",propertyName:"text"}], 
					  selectNodeModel: "all",  width: TrainPlaceBuildUp.fieldWidth
            		},
            		{id:"professionalTypeName_form_SId", name:"professionalTypeName", xtype: 'hidden' },
            		{ name: "buildUpPlaceSEQ", fieldLabel:"序号", maxLength: 8, xtype:"numberfield", disabled: true, width: TrainPlaceBuildUp.fieldWidth} 
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id: "fixPlaceName_SId", name: "buildUpPlaceName", fieldLabel: "结构位置名称", disabled: true, allowBlank: false, width: TrainPlaceBuildUp.fieldWidth, maxLength: 50 },
            		{ name: "buildUpPlaceShortName", fieldLabel: "结构位置简称",  width: TrainPlaceBuildUp.fieldWidth, maxLength:20, disabled: true  },
            		{ id: "partID_form_SId", xtype: "Base_combo",	fieldLabel: "位置",
						  hiddenName: "partID", returnField: [{widgetId:"partName_form_SId",propertyName:"partName"}],
						  displayField: "partName", valueField: "partId",
						  entity: "com.yunda.jx.component.entity.EquipPart", 
						  fields: ["partId","partName"],
						  pageSize: 20, minListWidth: 200, width: TrainPlaceBuildUp.fieldWidth, editable: true, disabled: true },
				    { id:"partName_form_SId", name:"partName", xtype: "hidden" }
            		           		
            	]
    		},
    		{ name: "idx",xtype: "hidden"},
    		{ name: "remarks",xtype: "hidden"},
    		{ name: "placeType",xtype: "hidden", value:structure_place},
    		{ name: "trainTypeIDX",xtype: "hidden", value:trainTypeIDX}
    	]}     
    ]
});
//安装位置名称修改联动修改安装位置全名
Ext.getCmp("fixPlaceName_SId").on("change",function(field,newValue,oldValue){
	var oldFullName = Ext.getCmp("buildUpPlaceFullName_Id").getValue();
	var index = oldFullName.lastIndexOf(oldValue);	
	Ext.getCmp("buildUpPlaceFullName_Id").setValue(oldFullName.substr(0,index) + newValue);
});
//配件位置信息
TrainPlaceBuildUp.partsPlaceForm = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: TrainPlaceBuildUp.labelWidth, plain: true,
    baseCls: "x-plain", defaultType: "textfield", height:600,  frame: false, style: "padding:10px", buttonAlign: "center",
    
    items: [
    	{
	    	xtype: "panel", border: false,  baseCls: "x-plain", layout: "form", align: "center", defaultType: "textfield",
	    	items : [{
	    		name: "buildUpPlaceFullName", fieldLabel: "配件位置全名",  disabled: true, width: 380 
	    	}]
	    },
    	{
    	xtype: "panel", border: false, anchor:"90%", baseCls: "x-plain", layout: "column", align: "center", plain: true,
    	items : [
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ name: "buildUpPlaceCode", fieldLabel: "配件位置编码", allowBlank: false, disabled:true, width: TrainPlaceBuildUp.fieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: TrainPlaceBuildUp.fieldWidth, disabled:true  },
            		{name:"professionalTypeName", fieldLabel: "专业类型", disabled:true, width: TrainPlaceBuildUp.fieldWidth },
            		{ name: "buildUpPlaceSEQ", fieldLabel:"序号", disabled:true, width: TrainPlaceBuildUp.fieldWidth} 
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ name: "buildUpPlaceName", fieldLabel: "配件位置名称", disabled:true, width: TrainPlaceBuildUp.fieldWidth},
            		{ name: "buildUpPlaceShortName", fieldLabel: "配件位置简称", disabled:true, width: TrainPlaceBuildUp.fieldWidth },
				    { name:"partName", fieldLabel: "位置", disabled:true, width: TrainPlaceBuildUp.fieldWidth }
            	]
    		},
    		{ name: "idx",xtype: "hidden"},
    		{ name: "remarks",xtype: "hidden"},
    		{ name: "placeType",xtype: "hidden", value:fix_place}
    	]}     
    ]
});
//虚拟位置信息
TrainPlaceBuildUp.virtualPlaceForm = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: TrainPlaceBuildUp.labelWidth, plain: true,
    baseCls: "x-plain", defaultType: "textfield", height:600,  frame: false, style: "padding:10px", buttonAlign: "center",
    
    items: [
    	{
	    	xtype: "panel", border: false,  baseCls: "x-plain", layout: "form", align: "center", defaultType: "textfield",
	    	items : [{
	    		name: "buildUpPlaceFullName", fieldLabel: "虚拟位置全名",  disabled: true, width: 380 
	    	}]
	    },
    	{
    	xtype: "panel", border: false, anchor:"90%", baseCls: "x-plain", layout: "column", align: "center", plain: true,
    	items : [
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ name: "buildUpPlaceCode", fieldLabel: "虚拟位置编码", allowBlank: false, disabled:true, width: TrainPlaceBuildUp.fieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: TrainPlaceBuildUp.fieldWidth, disabled:true  },
            		{ name:"professionalTypeName", fieldLabel: "专业类型", disabled:true, width: TrainPlaceBuildUp.fieldWidth }
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ name: "buildUpPlaceName", fieldLabel: "虚拟位置名称", disabled:true, width: TrainPlaceBuildUp.fieldWidth},
            		{ name: "buildUpPlaceShortName", fieldLabel: "虚拟位置简称", disabled:true, width: TrainPlaceBuildUp.fieldWidth },
				    { name:"partName", fieldLabel: "位置", disabled:true, width: TrainPlaceBuildUp.fieldWidth },
            		{ name: "buildUpPlaceSEQ", fieldLabel:"序号", disabled:true, width: TrainPlaceBuildUp.fieldWidth}            		
            	]
    		},
    		{ name: "idx",xtype: "hidden"},
    		{ name: "remarks",xtype: "hidden"},
    		{ name: "placeType",xtype: "hidden", value:virtual_place}
    	]}     
    ]
});
//根据业务编码生成规则生成结构位置编码
TrainPlaceBuildUp.getStructurePlaceCode = function(){
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
TrainPlaceBuildUp.childTrainBuildUpGrid =  new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpPlace!childPlaceList.action',                 //装载列表数据的请求URL
    saveFormColNum: 2,
    storeAutoLoad: false,
    //remoteSort: false,
    singleSelect: true,
    tbar: [],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'结构位置编码', dataIndex:'buildUpPlaceCode', editor:{ id:'buildUpPlaceCode_SId', disabled: true, maxLength:10 }, sortable: true
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
		header:'序号', dataIndex:'buildUpPlaceSEQ',  editor:{ xtype:'numberfield', maxLength:8, vtype: 'positiveInt' }
	},{
		header:'车型', dataIndex:'trainTypeIDX', hidden:true, editor:{ xtype:'hidden', value: trainTypeIDX }
	}],
	/**
     * 显示新增窗口后触发该函数，该函数依赖saveFn，若默认saveFn被覆盖则失效
     */
    afterShowSaveWin: function(){
    	TrainPlaceBuildUp.getStructurePlaceCode();
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
        data.trainTypeIDX = trainTypeIDX;
        if(self.loadMask)   self.loadMask.show();
        var cfg = {
            scope: this, url: this.saveURL, jsonData: data,
            params:{parentIdx:parentIdx,buildUpTypeIDX:buildUpTypeIdx},
            success: function(response, options){
                if(self.loadMask)   self.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    this.afterSaveSuccessFn(result, response, options);
                    TrainBuild.expandNode(nodeId, parentIdx);
                } else {
                    this.afterSaveFailFn(result, response, options);
                }
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
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
    	TrainBuild.expandNode(nodeId, parentIdx);
    }
});
//移除侦听器
TrainPlaceBuildUp.childTrainBuildUpGrid.un('rowdblclick', TrainPlaceBuildUp.childTrainBuildUpGrid.toEditFn, TrainPlaceBuildUp.childTrainBuildUpGrid);
//新增编辑窗口设为模态
TrainPlaceBuildUp.childTrainBuildUpGrid.createSaveWin();
TrainPlaceBuildUp.childTrainBuildUpGrid.saveWin.modal = true;
//选择安装位置窗口
TrainPlaceBuildUp.buildUpPlaceWin = new Ext.Window({
    title: "安装位置选择", width: 700, height: 400, layout: "fit", plain: true, closeAction: "hide", modal: true,
    items: [BuildUpPlace.selectGrid]
});
//下级安装位置
TrainPlaceBuildUp.childTrainFixGrid =  new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpPlace!childPlaceList.action',                 //装载列表数据的请求URL
    saveFormColNum: 2,
    //remoteSort: false,
    singleSelect: true,
    tbar: [],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'安装位置编码', dataIndex:'buildUpPlaceCode', editor:{  maxLength:10 }, sortable: true
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
    	TrainBuild.expandNode(nodeId, parentIdx);
    }
});
//移除侦听器
TrainPlaceBuildUp.childTrainFixGrid.un('rowdblclick', TrainPlaceBuildUp.childTrainFixGrid.toEditFn, TrainPlaceBuildUp.childTrainFixGrid);	
//tab选项卡布局
TrainPlaceBuildUp.tabs = new Ext.TabPanel({
    activeTab: 0,
    frame:true,
    singleSelect: true,
    items:[{  
           id: "structurePlaceTab",
           title: '结构位置信息',
           layout:'fit',
           frame:true,
           items: [TrainPlaceBuildUp.structurePlaceForm]
        },{  
           id: "partsPlaceTab",
           title: '配件位置信息',
           layout:'fit',
           frame:true,
           items: [TrainPlaceBuildUp.partsPlaceForm]
        },{  
           id: "virtualPlaceTab",
           title: '虚拟位置信息',
           layout:'fit',
           frame:true,
           items: [TrainPlaceBuildUp.virtualPlaceForm]
        },{ 
          id: "childTrainBuildUpTab",
          title: '下级结构位置',
          layout: 'fit' ,
          items: [TrainPlaceBuildUp.childTrainBuildUpGrid]
       },{ 
          id: "childTrainFixTab",
          title: '下级安装位置',
          layout: 'fit' ,
          items: [TrainPlaceBuildUp.childTrainFixGrid]
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
          items: [PlaceFault.grid]
       }]
});
//页面布局
TrainPlaceBuildUp.viewport = new Ext.Panel( {
    layout : 'border',
    items : [ {
        title : '<span style="font-weight:normal"></span>',
        iconCls : 'chart_organisationIcon',
        tools : [ {
            id : 'refresh',
            handler : function() {
                TrainBuild.tree.root.reload();
                TrainBuild.tree.getRootNode().expand();
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
        items : [ TrainBuild.tree ]
    }, {
        region : 'center',
        layout : 'fit',
        bodyBorder: false,
        items : [ TrainPlaceBuildUp.tabs ]
    } ]
});
//机车组成维护窗口
TrainPlaceBuildUp.win =  new Ext.Window({
    title: "机车组成维护", maximizable: true, width: 600, height: 400, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center',
    items: [TrainPlaceBuildUp.viewport],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ TrainPlaceBuildUp.win.hide(); }
            }]
});

});