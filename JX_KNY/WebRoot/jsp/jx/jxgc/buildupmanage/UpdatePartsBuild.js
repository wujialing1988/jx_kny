/**
 *   已作废-配件组成维护信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
//定义命名空间
Ext.namespace("UpdatePartsBuild");
UpdatePartsBuild.labelWidth = 80;
UpdatePartsBuild.fieldWidth = 140;

UpdatePartsBuild.bigFieldWidth = 140;
UpdatePartsBuild.bigLabelWidth = 80;
//根据业务编码生成规则生成安装位置编码
UpdatePartsBuild.getFixPlaceCode = function(){
	var url = ctx + "/codeRuleConfig!getConfigRule.action";
	Ext.Ajax.request({
                url: url,
                params: {ruleFunction: "JXGC_FIX_PLACE_Code"},
                success: function(response, options){
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        Ext.getCmp("fixPlaceCode_newId").setValue(result.rule);
                    }
                },
                failure: function(response, options){
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
}
//组成型号选择列表
UpdatePartsBuild.buildUpTypeGrid = new Ext.yunda.Grid({
	loadURL: ctx + "/buildUpType!fixBuildUpTypeList.action",
	tbar: [{
        	xtype:"label", text:"  组成型号名称：" 
    	},{			
            xtype: "textfield",    
            id: "buildUpTypeName_searchId"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var typeName = Ext.getCmp("buildUpTypeName_searchId").getValue();
				var searchParam = {};
				searchParam.buildUpTypeName = typeName;
				searchParam.type = type_parts;
    			searchParam.status = status_use;	
				UpdatePartsBuild.buildUpTypeGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam),
						fixPlaceIdx:fixPlaceIdx
					}																
				});				
			},			
			width : 40
		},{
			text : "确定",
			iconCls : "saveIcon",
			handler : function(){
				//未选择记录，直接返回
        		if(!$yd.isSelectedRecord(UpdatePartsBuild.buildUpTypeGrid)) return;
        		var tempData = UpdatePartsBuild.buildUpTypeGrid.selModel.getSelections();
        		var dataAry = new Array();
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.fixPlaceIdx = fixPlaceIdx;
        			data.buildUpTypeIdx = tempData[ i ].get("idx");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/fixBuildUpType!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        UpdatePartsBuild.buildUpTypeGrid.getStore().reload();
	                        UpdatePartsBuild.fixBuildUpTypeGrid.getStore().reload();
	                        UpdatePartsBuild.buildUpTypeWin.hide();
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
					header:'组成型号编码', dataIndex:'buildUpTypeCode' 
				},{
					header:'组成型号名称', dataIndex:'buildUpTypeName' 
				},{
					header:'配件规格型号', dataIndex:'specificationModel'
				},{
					header:'配件名称', dataIndex:'partsName'
				}]
});
//移除侦听器
UpdatePartsBuild.buildUpTypeGrid.un('rowdblclick', UpdatePartsBuild.buildUpTypeGrid.toEditFn, UpdatePartsBuild.buildUpTypeGrid);
//组成型号选择窗口
UpdatePartsBuild.buildUpTypeWin =  new Ext.Window({
    title: "组成型号选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [UpdatePartsBuild.buildUpTypeGrid],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ UpdatePartsBuild.buildUpTypeWin.hide(); }
            }]
});
//设为缺省可安装组成型号
UpdatePartsBuild.setDefaultFixBuildType = function(id,grid){
	var cfg = Ext.apply($yd.cfgAjaxRequest(), {
        params: {'id': id},
        url: ctx + '/fixBuildUpType!setIsDefault.action', 
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                //操作成功            
                alertSuccess();
                grid.store.reload(); 
            } else {
                //操作失败
                alertFail(result.errMsg);
            }
        }               
    }); 
    Ext.Ajax.request(cfg);
}
//可安装组成型号tab
UpdatePartsBuild.fixBuildUpTypeGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!list.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/fixBuildUpType!logicDelete.action',            //删除数据的请求URL
    tbar: [{
	    	text: '添加可安装组成型号',
	    	iconCls: 'addIcon',
	    	handler: function(){
	    		
	    		UpdatePartsBuild.buildUpTypeWin.show();
	    	}
	    },{
	    	text: '设为缺省',
	    	iconCls: 'configIcon',
	    	handler: function(){
	    		if(!$yd.isSelectedRecord(UpdatePartsBuild.fixBuildUpTypeGrid)) return;
	    		var ids = $yd.getSelectedIdx(UpdatePartsBuild.fixBuildUpTypeGrid);
	    		UpdatePartsBuild.setDefaultFixBuildType(ids[0],UpdatePartsBuild.fixBuildUpTypeGrid);
	    	}
	    },'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'组成型号名称', dataIndex:'buildUpTypeName', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'规格型号', dataIndex:'specificationModel', editor:{ xtype: "hidden",  maxLength:50, allowBlank: false }
	},{
		header:'缺省', dataIndex:'isDefault', 
		renderer:function(v){
			switch(v){
				case isDefault:
					return isDefaultMean;
					break;
				case noDefault:
					return noDefaultMean;
					break;
				default :
					return "";
					break;
			}
		},
		editor:{ }
	}]
});
//移除侦听器
UpdatePartsBuild.fixBuildUpTypeGrid.un('rowdblclick', UpdatePartsBuild.fixBuildUpTypeGrid.toEditFn, UpdatePartsBuild.fixBuildUpTypeGrid);
//安装位置信息tab上form表单
UpdatePartsBuild.PartsBuildUpForm = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: UpdatePartsBuild.bigLabelWidth, plain: true,
    baseCls: "x-plain", defaultType: "textfield", height: 600,  frame: false, style: "padding:10px", buttonAlign: "center",
    buttons:[{
    	text:"保存", iconCls:"saveIcon",
        handler:function(){
        	//表单验证是否通过
        var form = UpdatePartsBuild.PartsBuildUpForm.getForm(); 
        if (!form.isValid()) return; 
        Ext.getCmp("fixPlaceFullName_Id").enable();
        Ext.getCmp("fixPlaceCode_Id").enable();
        var data = form.getValues();
        Ext.getCmp("fixPlaceFullName_Id").disable();
        Ext.getCmp("fixPlaceCode_Id").disable();
        if(self.loadMask)   self.loadMask.show();
        var cfg = {
            scope: this, url: ctx + '/fixPlace!saveOrUpdate.action', jsonData: data,
            success: function(response, options){
                if(self.loadMask)   self.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null && result.entity != null) {
                    alertSuccess();
                    PartsBuild.tree.root.reload();
            		PartsBuild.tree.getRootNode().expand();
            		if(result.entity.isVirtual == isVirtual){
            			UpdatePartsBuild.tabs.hideTabStripItem("fixBuildUpTypeTab");
		        		UpdatePartsBuild.tabs.unhideTabStripItem("PartsBuildUpTab");
		        		UpdatePartsBuild.tabs.unhideTabStripItem("childPartsBuildUpTab");
						UpdatePartsBuild.tabs.activate("PartsBuildUpTab");
            		}else if(result.entity.isVirtual == noVirtual){
            			UpdatePartsBuild.tabs.unhideTabStripItem("fixBuildUpTypeTab");
		        		UpdatePartsBuild.tabs.unhideTabStripItem("PartsBuildUpTab");
		        		UpdatePartsBuild.tabs.hideTabStripItem("childPartsBuildUpTab");
						UpdatePartsBuild.tabs.activate("PartsBuildUpTab");
            		}
                } else {
                    alertFail(result.errMsg);
                }
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
        }
    }],
    items: [
    	{
	    	xtype: "panel", border: false,  baseCls: "x-plain", layout: "form", align: "center", defaultType: "textfield",
	    	items : [{
	    		id:"fixPlaceFullName_Id", name: "fixPlaceFullName", fieldLabel: "安装位置全名",  disabled: true, width: 380 
	    	}]
	    },
    	{
    	xtype: "panel", border: false, anchor:"90%", baseCls: "x-plain", layout: "column", align: "center", plain: true,
    	items : [
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{id:"fixPlaceCode_Id", name: "fixPlaceCode", fieldLabel: "安装位置编码", allowBlank: false, disabled: true, width: UpdatePartsBuild.bigFieldWidth },
            		{ name: "chartNo", fieldLabel: "图号",  width: UpdatePartsBuild.bigFieldWidth, maxLength:50  },
            		{ 
            		  id: "ProfessionalType_form_Id", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型", allowBlank: false,
					  hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_formId",propertyName:"text"}], 
					  selectNodeModel: "all",  width: UpdatePartsBuild.bigFieldWidth
            		},
            		{id:"professionalTypeName_formId", name:"professionalTypeName", xtype: 'hidden' },
            		{ 
						fieldLabel:"是否虚拟位置",
            		  	xtype: 'combo',
						hiddenName: 'isVirtual',
						store:new Ext.data.SimpleStore({
						    fields: ['v', 't'],
							data : [[isVirtual,'是'],[noVirtual,'否']]
						}),
						valueField:'v',
						displayField:'t',
						triggerAction:'all',
						mode:'local',
			 			value: isVirtual,
			 			allowBlank: false,
			 			width: UpdatePartsBuild.bigFieldWidth
            		}
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id: "fixPlaceName_Id", name: "fixPlaceName", fieldLabel: "安装位置名称", allowBlank: false, width: UpdatePartsBuild.bigFieldWidth },
            		{ name: "fixPlaceShortName", fieldLabel: "安装位置简称",  width: UpdatePartsBuild.bigFieldWidth, maxLength:20  },
            		{ id: "partID_formId", xtype: "Base_combo",	fieldLabel: "位置",
						  hiddenName: "partID", returnField: [{widgetId:"partName_formId",propertyName:"partName"}],
						  displayField: "partName", valueField: "partId",
						  entity: "com.yunda.jx.component.entity.EquipPart", 
						  fields: ["partId","partName"],
						  pageSize: 20, minListWidth: 200, width: UpdatePartsBuild.bigFieldWidth, editable: true },
				    { id:"partName_formId", name:"partName", xtype: "hidden" },
            		{ name: "fixPlaceSEQ", fieldLabel:"序号", width: UpdatePartsBuild.bigFieldWidth}            		
            	]
    		},
    		{ name: "idx",xtype: "hidden"},
    		{ name: "fixPlaceFullCode",xtype: "hidden"},
    		{ name: "parentIdx",xtype: "hidden"},
    		{ name: "partsBuildUpTypeIdx",xtype: "hidden"},
    		{ name: "remarks",xtype: "hidden"}
    		
    	]}     
    ]
});
//安装位置名称修改联动修改安装位置全名
Ext.getCmp("fixPlaceName_Id").on("change",function(field,newValue,oldValue){
	var oldFullName = Ext.getCmp("fixPlaceFullName_Id").getValue();
	var index = oldFullName.lastIndexOf(oldValue);	
	Ext.getCmp("fixPlaceFullName_Id").setValue(oldFullName.substr(0,index) + newValue);
});
//机车组成型号下级配置情况tab页上grid列表
UpdatePartsBuild.childPartsBuildUpGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/fixPlace!childPlaceList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/fixPlace!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/fixPlace!logicDelete.action',            //删除数据的请求URL
    saveFormColNum: 2,
    //searchFormColNum: 2,
    viewConfig: {forceFit: false, markDirty: false},
    tbar:['add','delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'安装位置编码', dataIndex:'fixPlaceCode', editor:{ id:'fixPlaceCode_newId', disabled:true, allowBlank:false, maxLength:10 }
	},{
		header:'安装位置编码全名', dataIndex:'fixPlaceFullCode', editor:{ xtype:"hidden",  maxLength:200 }, searcher : {disabled : true}
	},{
		header:'安装位置名称', dataIndex:'fixPlaceName', editor:{ allowBlank:false,  maxLength:50, allowBlank: false }
	},{
		header:'安装位置名称全名', dataIndex:'fixPlaceFullName', editor:{ xtype:"hidden", maxLength:500 }, searcher : {disabled : true}
	},{
		header:'安装位置序号', dataIndex:'fixPlaceSEQ', editor:{ xtype:'numberfield', maxLength:8, vtype:'positiveInt' },searcher : {disabled : true}
	},{
		header:'图号', dataIndex:'chartNo', editor:{  maxLength:50 },searcher : {disabled : true}
	},{
		header:'配件专业类型', dataIndex:'professionalTypeIDX', hidden: true,
		editor:{
			id: "ProfessionalType_comboTree_Id", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型", allowBlank: false,
			hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_Id",propertyName:"text"}], selectNodeModel: "all"
		},searcher : {disabled : true}
	},{
		header:'专业类型名称', dataIndex:'professionalTypeName', editor:{id:"professionalTypeName_Id", xtype: 'hidden' },searcher : {disabled : true}
	},{
		header:'上级安装位置', dataIndex:'parentIdx', hidden:true, editor:{id:"parentIdx_id", xtype: 'hidden', maxLength:50 }
	},{
		header:'位置编码', dataIndex:'partID', hidden:true,
		editor:{ 
				maxLength:4, 
				id: "partID_Id", xtype: "Base_combo",	fieldLabel: "位置",
				 hiddenName: "partID", returnField: [{widgetId:"partName_Id",propertyName:"partName"}],
				  displayField: "partName", valueField: "partId",
				  entity: "com.yunda.jx.component.entity.EquipPart", 
				  fields: ["partId","partName"],
				  pageSize: 20, minListWidth: 200, editable: true }
	},{
		header:'位置名称', dataIndex:'partName', editor:{ id:"partName_Id", maxLength:100, xtype:"hidden" }
	},{
		header:'所属组成型号', dataIndex:'partsBuildUpTypeIdx', hidden:true, editor:{id:"partsBuildUpTypeIdx_Id", xtype:"hidden", maxLength:50 },searcher : {disabled : true}
	},{
		header:'是否虚拟位置', dataIndex:'isVirtual', 
		renderer:function(v){
			switch(v){
				case isVirtual:
					return "是";
					break;
				case noVirtual:
					return "否";
					break;
				default :
					return "";
					break;
			}
		},
		editor:{
			fieldLabel:"是否虚拟位置",
		  	xtype: 'combo',
			hiddenName: 'isVirtual',
			store:new Ext.data.SimpleStore({
			    fields: ['v', 't'],
				data : [[isVirtual,'是'],[noVirtual,'否']]
			}),
			valueField:'v',
			displayField:'t',
			triggerAction:'all',
			mode:'local',
 			value: isVirtual,
 			allowBlank: false
		}
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 },searcher : {disabled : true}
	}],
	afterShowEditWin: function(record, rowIndex){
		Ext.getCmp("partID_Id").setDisplayValue(record.get("partID"),record.get("partName"));
		Ext.getCmp("ProfessionalType_comboTree_Id").setDisplayValue(record.get("professionalTypeIDX"),record.get("professionalTypeName"));
	},
	afterSaveSuccessFn: function(result, response, options){
		UpdatePartsBuild.getFixPlaceCode();
		this.store.reload();
		alertSuccess();        
        PartsBuild.tree.root.reload();
        PartsBuild.tree.getRootNode().expand();
    },
    afterDeleteFn: function(){ 
    	//删除成功后刷新节点
        var path = PartsBuild.tree.getNodeById(nodeId).getPath('id');
        PartsBuild.tree.getLoader().load(PartsBuild.tree.getNodeById(nodeId),function(treeNode){  
        //展开路径,并在回调函数里面选择该节点  
        PartsBuild.tree.expandPath(path,'id',function(bSuccess,oLastNode){        	
	        if(!bSuccess){
	          	return;				              	
	        }
			//focus 节点，并选中节点！
			oLastNode.ensureVisible();
			oLastNode.select();
			oLastNode.fireEvent('click', oLastNode);
	    }); 
        },this);
    },
    beforeShowSaveWin: function(){  
    	Ext.getCmp("ProfessionalType_comboTree_Id").clearValue();	
    	UpdatePartsBuild.getFixPlaceCode();
    	return true; 
    },
    /**
     * 获取表单数据前触发(覆盖该函数可用于修改表单字段enable、disabled等)，
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     */
    beforeGetFormData: function(){
		this.enableColumns(['fixPlaceCode']);
	},
    /**
     * 获取表单数据后触发(覆盖该函数可用于修改表单字段enable、disabled等)，
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     */    
    afterGetFormData: function(){
		this.disableColumns(['fixPlaceCode']);
	 }
});

//tab选项卡布局
UpdatePartsBuild.tabs = new Ext.TabPanel({
    activeTab: 0,
    frame:true,
    singleSelect: true,
    items:[{  
           id: "PartsBuildUpTab",
           title: '安装位置信息',
           layout:'fit',
           frame:true,
           items: [UpdatePartsBuild.PartsBuildUpForm]
        },{ 
          id: "childPartsBuildUpTab",
          title: '下级安装位置',
          layout: 'fit' ,
          items: [UpdatePartsBuild.childPartsBuildUpGrid]
       },
       	{ 
          id: "fixBuildUpTypeTab",
          title: '可安装组成型号',
          frame:true,
          layout: 'fit' ,
          items: [UpdatePartsBuild.fixBuildUpTypeGrid]
       }]
});
//页面布局
UpdatePartsBuild.viewport = new Ext.Panel( {
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
        minSize : 160,
        maxSize : 280,
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
        items : [ UpdatePartsBuild.tabs ]
    } ]
});

//配件组成维护
UpdatePartsBuild.win =  new Ext.Window({
    title: "配件组成维护", maximized :true, width: 800, height: 400, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center',
    items: [UpdatePartsBuild.viewport],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ UpdatePartsBuild.win.hide(); }
            }]
});
});