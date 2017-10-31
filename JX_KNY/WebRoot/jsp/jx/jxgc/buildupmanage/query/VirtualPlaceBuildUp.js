/**
 * 虚拟组成-设置组成 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('VirtualPlaceBuildUp');                       //定义命名空间
VirtualPlaceBuildUp.labelWidth = 80;
VirtualPlaceBuildUp.fieldWidth = 140;
//结构位置信息tab上form表单
VirtualPlaceBuildUp.structurePlaceForm = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: VirtualPlaceBuildUp.labelWidth, plain: true,
    baseCls: "x-plain", defaultType: "textfield", height:600,  frame: false, style: "padding:10px", buttonAlign: "center",
    
    items: [
    	{
	    	xtype: "panel", border: false,  baseCls: "x-plain", layout: "form", align: "center", defaultType: "textfield",
	    	items : [{
	    		id:"buildUpPlaceFullName_VId", name: "buildUpPlaceFullName", fieldLabel: "结构位置全名",  disabled: true, width: 380 
	    	}]
	    },
    	{
    	xtype: "panel", border: false, anchor:"90%", baseCls: "x-plain", layout: "column", align: "center", plain: true,
    	items : [
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id:"structureCode_VId", name: "buildUpPlaceCode", fieldLabel: "结构位置编码", allowBlank: false, disabled:true, width: VirtualPlaceBuildUp.fieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: VirtualPlaceBuildUp.fieldWidth, maxLength:50  },
            		{ 
            		  id: "ProfessionalType_form_VSId", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型", 
					  hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_form_VSId",propertyName:"text"}], 
					  selectNodeModel: "all",  width: VirtualPlaceBuildUp.fieldWidth
            		},
            		{id:"professionalTypeName_form_VSId", name:"professionalTypeName", xtype: 'hidden' },
            		{ name: "buildUpPlaceSEQ", fieldLabel:"序号", maxLength: 8, xtype:"numberfield", width: VirtualPlaceBuildUp.fieldWidth} 
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id: "fixPlaceName_VSId", name: "buildUpPlaceName", fieldLabel: "结构位置名称", allowBlank: false, width: VirtualPlaceBuildUp.fieldWidth, maxLength: 50 },
            		{ name: "buildUpPlaceShortName", fieldLabel: "结构位置简称",  width: VirtualPlaceBuildUp.fieldWidth, maxLength:20  },
            		{ id: "partID_form_VSId", xtype: "Base_combo",	fieldLabel: "位置",
						  hiddenName: "partID", returnField: [{widgetId:"partName_form_VSId",propertyName:"partName"}],
						  displayField: "partName", valueField: "partId",
						  entity: "com.yunda.jx.component.entity.EquipPart", 
						  fields: ["partId","partName"],
						  pageSize: 20, minListWidth: 200, width: VirtualPlaceBuildUp.fieldWidth, editable: true },
				    { id:"partName_form_VSId", name:"partName", xtype: "hidden" }
            		           		
            	]
    		},
    		{ name: "idx",xtype: "hidden"},
    		{ name: "remarks",xtype: "hidden"},
    		{ name: "placeType",xtype: "hidden", value:structure_place},
    		{ name: "trainTypeIDX",xtype: "hidden", value:trainTypeIDX},
    		{ name: "partsTypeIDX",xtype: "hidden", value:partsTypeIDX}
    	]}     
    ]
});
//安装位置名称修改联动修改安装位置全名
Ext.getCmp("fixPlaceName_VSId").on("change",function(field,newValue,oldValue){
	var oldFullName = Ext.getCmp("buildUpPlaceFullName_VId").getValue();
	var index = oldFullName.lastIndexOf(oldValue);	
	Ext.getCmp("buildUpPlaceFullName_VId").setValue(oldFullName.substr(0,index) + newValue);
});
//配件位置信息
VirtualPlaceBuildUp.partsPlaceForm = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: VirtualPlaceBuildUp.labelWidth, plain: true,
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
            		{ name: "buildUpPlaceCode", fieldLabel: "配件位置编码", allowBlank: false, disabled:true, width: VirtualPlaceBuildUp.fieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: VirtualPlaceBuildUp.fieldWidth, disabled:true  },
            		{name:"professionalTypeName", fieldLabel: "专业类型", disabled:true, width: VirtualPlaceBuildUp.fieldWidth },
            		{ name: "buildUpPlaceSEQ", fieldLabel:"序号", disabled:true, width: VirtualPlaceBuildUp.fieldWidth} 
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ name: "buildUpPlaceName", fieldLabel: "配件位置名称", disabled:true, width: VirtualPlaceBuildUp.fieldWidth},
            		{ name: "buildUpPlaceShortName", fieldLabel: "配件位置简称", disabled:true, width: VirtualPlaceBuildUp.fieldWidth },
				    { name:"partName", fieldLabel: "位置", disabled:true, width: VirtualPlaceBuildUp.fieldWidth }
            	]
    		},
    		{ name: "idx",xtype: "hidden"},
    		{ name: "remarks",xtype: "hidden"},
    		{ name: "placeType",xtype: "hidden", value:fix_place}
    	]}     
    ]
});
//虚拟位置信息
VirtualPlaceBuildUp.virtualPlaceForm = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: VirtualPlaceBuildUp.labelWidth, plain: true,
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
            		{ name: "buildUpPlaceCode", fieldLabel: "虚拟位置编码", allowBlank: false, disabled:true, width: VirtualPlaceBuildUp.fieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: VirtualPlaceBuildUp.fieldWidth, disabled:true  },
            		{ name:"professionalTypeName", fieldLabel: "专业类型", disabled:true, width: VirtualPlaceBuildUp.fieldWidth }
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ name: "buildUpPlaceName", fieldLabel: "虚拟位置名称", disabled:true, width: VirtualPlaceBuildUp.fieldWidth},
            		{ name: "buildUpPlaceShortName", fieldLabel: "虚拟位置简称", disabled:true, width: VirtualPlaceBuildUp.fieldWidth },
				    { name:"partName", fieldLabel: "位置", disabled:true, width: VirtualPlaceBuildUp.fieldWidth },
            		{ name: "buildUpPlaceSEQ", fieldLabel:"序号", disabled:true, width: VirtualPlaceBuildUp.fieldWidth}            		
            	]
    		},
    		{ name: "idx",xtype: "hidden"},
    		{ name: "remarks",xtype: "hidden"},
    		{ name: "placeType",xtype: "hidden", value:virtual_place}
    	]}     
    ]
});
//根据业务编码生成规则生成结构位置编码
VirtualPlaceBuildUp.getStructurePlaceCode = function(){
	var url = ctx + "/codeRuleConfig!getConfigRule.action";
	Ext.Ajax.request({
                url: url,
                params: {ruleFunction: "JXGC_FIX_PLACE_Code"},
                success: function(response, options){
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        Ext.getCmp("buildUpPlaceCode_VSId").setValue(result.rule);
                    }
                },
                failure: function(response, options){
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
}
//下级结构位置列表
VirtualPlaceBuildUp.childTrainBuildUpGrid =  new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpPlace!childPlaceList.action',                 //装载列表数据的请求URL
    saveFormColNum: 2,
    storeAutoLoad: false,
    //remoteSort: false,
    singleSelect: true,
    tbar: [],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'结构位置编码', dataIndex:'buildUpPlaceCode', editor:{ id:'buildUpPlaceCode_VSId', disabled: true, maxLength:10 }, sortable: true
	},{
		header:'结构位置名称', dataIndex:'buildUpPlaceName', editor:{  maxLength:50 }
	},{
		header:'位置简称', dataIndex:'buildUpPlaceShortName', editor:{  maxLength:20 }
	},{
		header:'图号', dataIndex:'chartNo', editor:{  maxLength:50 }
	},{
		header:'配件专业类型表主键', dataIndex:'professionalTypeIDX', hidden: true, 
		editor:{
			id: "ProfessionalType_VSId", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型", 
			hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_VSId",propertyName:"text"}], 
			selectNodeModel: "all"
		}
	},{
		header:'专业类型名称', dataIndex:'professionalTypeName', editor:{ id:'professionalTypeName_VSId', xtype:'hidden' }
	},{
		header:'位置编码', dataIndex:'partID', hidden:true, 
		editor:{
			  id: "partID_VSId", xtype: "Base_combo",	fieldLabel: "位置",
			  hiddenName: "partID", returnField: [{widgetId:"partName_SId",propertyName:"partName"}],
			  displayField: "partName", valueField: "partId",
			  entity: "com.yunda.jx.component.entity.EquipPart", 
			  fields: ["partId","partName"],
			  pageSize: 20, minListWidth: 200, editable: true
		}
	},{
		header:'位置', dataIndex:'partName', 
		editor:{
			id:"partName_VSId", xtype:"hidden"
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
		header:'序号', dataIndex:'buildUpPlaceSEQ',  editor:{ xtype:'numberfield', maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeIDX', hidden:true, editor:{ xtype:'hidden', value: trainTypeIDX }
	},{
		header:'配件型号', dataIndex:'partsTypeIDX', hidden:true, editor:{ xtype:'hidden', value: partsTypeIDX }
	}],
	/**
     * 显示新增窗口后触发该函数，该函数依赖saveFn，若默认saveFn被覆盖则失效
     */
    afterShowSaveWin: function(){
    	VirtualPlaceBuildUp.getStructurePlaceCode();
    },	
    /**
     * 获取表单数据前触发(覆盖该函数可用于修改表单字段enable、disabled等)，
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     */
    beforeGetFormData: function(){
    	Ext.getCmp("buildUpPlaceCode_VSId").enable();
    },
    /**
     * 获取表单数据后触发(覆盖该函数可用于修改表单字段enable、disabled等)，
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     */    
    afterGetFormData: function(){
    	Ext.getCmp("buildUpPlaceCode_VSId").disable();
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
        data.partsTypeIDX = partsTypeIDX;
        if(self.loadMask)   self.loadMask.show();
        var cfg = {
            scope: this, url: this.saveURL, jsonData: data,
            params:{parentIdx:parentIdx,buildUpTypeIDX:buildUpTypeIdx},
            success: function(response, options){
                if(self.loadMask)   self.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    this.afterSaveSuccessFn(result, response, options);
                    //VirtualBuild.expandNode(nodeId, parentIdx);
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
    	Ext.getCmp("ProfessionalType_VSId").setDisplayValue(record.get("professionalTypeIDX"),record.get("professionalTypeName"));
    	Ext.getCmp("partID_VSId").setDisplayValue(record.get("partID"),record.get("partName"));
    },
    /**
     * 执行删除操作后触发的函数，
     * 该函数依赖deleteButtonFn，如果deleteButtonFn被覆盖则失效
     * @return {Boolean}
     */
    afterDeleteFn: function(){ 
    	//VirtualBuild.expandNode(nodeId, parentIdx);
    }
});
//移除侦听器
VirtualPlaceBuildUp.childTrainBuildUpGrid.un('rowdblclick', VirtualPlaceBuildUp.childTrainBuildUpGrid.toEditFn, VirtualPlaceBuildUp.childTrainBuildUpGrid);
//新增编辑窗口设为模态
VirtualPlaceBuildUp.childTrainBuildUpGrid.createSaveWin();
VirtualPlaceBuildUp.childTrainBuildUpGrid.saveWin.modal = true;
//选择安装位置窗口
VirtualPlaceBuildUp.buildUpPlaceWin = new Ext.Window({
    title: "安装位置选择", width: 700, height: 400, layout: "fit", plain: true, closeAction: "hide", modal: true,
    items: [BuildUpPlace.v_selectGrid]
});
//下级安装位置
VirtualPlaceBuildUp.childTrainFixGrid =  new Ext.yunda.Grid({
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
    	//VirtualBuild.expandNode(nodeId, parentIdx);
    }
});
//移除侦听器
VirtualPlaceBuildUp.childTrainFixGrid.un('rowdblclick', VirtualPlaceBuildUp.childTrainFixGrid.toEditFn, VirtualPlaceBuildUp.childTrainFixGrid);	
//tab选项卡布局
VirtualPlaceBuildUp.tabs = new Ext.TabPanel({
    activeTab: 0,
    frame:true,
    singleSelect: true,
    items:[{  
           id: "v_structurePlaceTab",
           title: '结构位置信息',
           layout:'fit',
           frame:true,
           items: [VirtualPlaceBuildUp.structurePlaceForm]
        },{  
           id: "v_partsPlaceTab",
           title: '配件位置信息',
           layout:'fit',
           frame:true,
           items: [VirtualPlaceBuildUp.partsPlaceForm]
        },{  
           id: "v_virtualPlaceTab",
           title: '虚拟位置信息',
           layout:'fit',
           frame:true,
           items: [VirtualPlaceBuildUp.virtualPlaceForm]
        },{ 
          id: "v_childTrainBuildUpTab",
          title: '下级结构位置',
          layout: 'fit' ,
          items: [VirtualPlaceBuildUp.childTrainBuildUpGrid]
       },{ 
          id: "v_childTrainFixTab",
          title: '下级安装位置',
          layout: 'fit' ,
          items: [VirtualPlaceBuildUp.childTrainFixGrid]
       },{ 
          id: "v_fixBuildUpTypeTab",
          title: '可安装组成型号',
          frame:true,
          layout: 'fit' ,
          items: [FixBuildUpType.v_fixBuildUpTypeQueryGrid]
       },{ 
          id: "v_faultTab",
          title: '故障现象',
          frame:true,
          layout: 'fit' ,
          items: [PlaceFault.v_grid]
       }]
});
//页面布局
VirtualPlaceBuildUp.viewport = new Ext.Panel( {
    layout : 'border',
    items : [ {
        title : '<span style="font-weight:normal"></span>',
        iconCls : 'chart_organisationIcon',
        tools : [ {
            id : 'refresh',
            handler : function() {
                VirtualBuild.tree.root.reload();
                VirtualBuild.tree.getRootNode().expand();
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
        items : [ VirtualBuild.tree ]
    }, {
        region : 'center',
        layout : 'fit',
        bodyBorder: false,
        items : [ VirtualPlaceBuildUp.tabs ]
    } ]
});
//虚拟组成维护窗口
VirtualPlaceBuildUp.win =  new Ext.Window({
    title: "虚拟组成维护", maximizable :true, width: 600, height: 400, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center',
    items: [VirtualPlaceBuildUp.viewport],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ VirtualPlaceBuildUp.win.hide(); }
            }]
});
});