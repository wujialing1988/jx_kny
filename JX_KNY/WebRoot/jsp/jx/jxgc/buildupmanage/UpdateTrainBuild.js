/**
 *  已作废- 机车组成维护信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
//定义命名空间
Ext.namespace("UpdateTrainBuild");
UpdateTrainBuild.labelWidth = 80;
UpdateTrainBuild.fieldWidth = 140;

UpdateTrainBuild.bigFieldWidth = 140;
UpdateTrainBuild.bigLabelWidth = 80;
//根据业务编码生成规则生成安装位置编码
UpdateTrainBuild.getFixPlaceCode = function(){
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
UpdateTrainBuild.buildUpTypeGrid = new Ext.yunda.Grid({
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
				UpdateTrainBuild.buildUpTypeGrid.getStore().load({
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
        		if(!$yd.isSelectedRecord(UpdateTrainBuild.buildUpTypeGrid)) return;
        		var tempData = UpdateTrainBuild.buildUpTypeGrid.selModel.getSelections();
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
	                        UpdateTrainBuild.buildUpTypeGrid.getStore().reload();
	                        UpdateTrainBuild.fixBuildUpTypeGrid.getStore().reload();
	                        UpdateTrainBuild.buildUpTypeWin.hide();
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
UpdateTrainBuild.buildUpTypeGrid.un('rowdblclick', UpdateTrainBuild.buildUpTypeGrid.toEditFn, UpdateTrainBuild.buildUpTypeGrid);
//组成型号选择窗口
UpdateTrainBuild.buildUpTypeWin =  new Ext.Window({
    title: "组成型号选择", width: 500, height: 350, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center', modal:true,
    items: [UpdateTrainBuild.buildUpTypeGrid],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ UpdateTrainBuild.buildUpTypeWin.hide(); }
            }]
});
//设为缺省可安装组成型号
UpdateTrainBuild.setDefaultFixBuildType = function(id,grid){
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
UpdateTrainBuild.fixBuildUpTypeGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!list.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/fixBuildUpType!logicDelete.action',            //删除数据的请求URL
    singleSelect: true,
    tbar: [{
	    	text: '添加可安装组成型号',
	    	iconCls: 'addIcon',
	    	handler: function(){	    		
//	    		UpdateTrainBuild.buildUpTypeGrid.getStore().load({
//	    			params:{fixPlaceIdx:fixPlaceIdx}
//	    		});
	    		UpdateTrainBuild.buildUpTypeWin.show();
	    	}
	    },{
	    	text: '设为缺省',
	    	iconCls: 'configIcon',
	    	handler: function(){
	    		if(!$yd.isSelectedRecord(UpdateTrainBuild.fixBuildUpTypeGrid)) return;
	    		var ids = $yd.getSelectedIdx(UpdateTrainBuild.fixBuildUpTypeGrid);
	    		UpdateTrainBuild.setDefaultFixBuildType(ids[0],UpdateTrainBuild.fixBuildUpTypeGrid);
	    	}
	    },'delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{}
	},{
		header:'组成型号名称', dataIndex:'buildUpTypeName', editor:{}
	},{
		header:'规格型号', dataIndex:'specificationModel', editor:{}
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
UpdateTrainBuild.fixBuildUpTypeGrid.un('rowdblclick', UpdateTrainBuild.fixBuildUpTypeGrid.toEditFn, UpdateTrainBuild.fixBuildUpTypeGrid);
//安装位置信息tab上form表单
UpdateTrainBuild.trainBuildUpForm = new Ext.form.FormPanel({
    layout: "anchor",     border: false,      labelWidth: UpdateTrainBuild.bigLabelWidth, plain: true,
    baseCls: "x-plain", defaultType: "textfield", height:600,  frame: false, style: "padding:10px", buttonAlign: "center",
    buttons:[{
    	text:"保存", iconCls:"saveIcon",
        handler:function(){
        	//表单验证是否通过
        var form = UpdateTrainBuild.trainBuildUpForm.getForm(); 
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
                    TrainBuild.tree.root.reload();
            		TrainBuild.tree.getRootNode().expand();
            		if(result.entity.isVirtual == isVirtual){
            			UpdateTrainBuild.tabs.hideTabStripItem("fixBuildUpTypeTab");
		        		UpdateTrainBuild.tabs.unhideTabStripItem("trainBuildUpTab");
		        		UpdateTrainBuild.tabs.unhideTabStripItem("childTrainBuildUpTab");
						UpdateTrainBuild.tabs.activate("trainBuildUpTab");
            		}else if(result.entity.isVirtual == noVirtual){
            			UpdateTrainBuild.tabs.unhideTabStripItem("fixBuildUpTypeTab");
		        		UpdateTrainBuild.tabs.unhideTabStripItem("trainBuildUpTab");
		        		UpdateTrainBuild.tabs.hideTabStripItem("childTrainBuildUpTab");
						UpdateTrainBuild.tabs.activate("trainBuildUpTab");
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
            		{ id:"fixPlaceCode_Id", name: "fixPlaceCode", fieldLabel: "安装位置编码", allowBlank: false, disabled:true, width: UpdateTrainBuild.bigFieldWidth, maxLength:10  },
            		{ name: "chartNo", fieldLabel: "图号",  width: UpdateTrainBuild.bigFieldWidth, maxLength:50  },
            		{ 
            		  id: "ProfessionalType_form_Id", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型", allowBlank: false,
					  hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_formId",propertyName:"text"}], 
					  selectNodeModel: "all",  width: UpdateTrainBuild.bigFieldWidth
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
			 			editable: false,
			 			width: UpdateTrainBuild.bigFieldWidth
            		}
            	]
    		},
    		{
    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.5, defaultType: "textfield",
            	items : [
            		{ id: "fixPlaceName_Id", name: "fixPlaceName", fieldLabel: "安装位置名称", allowBlank: false, width: UpdateTrainBuild.bigFieldWidth, maxLength: 50 },
            		{ name: "fixPlaceShortName", fieldLabel: "安装位置简称",  width: UpdateTrainBuild.bigFieldWidth, maxLength:20  },
            		{ id: "partID_formId", xtype: "Base_combo",	fieldLabel: "位置",
						  hiddenName: "partID", returnField: [{widgetId:"partName_formId",propertyName:"partName"}],
						  displayField: "partName", valueField: "partId",
						  entity: "com.yunda.jx.component.entity.EquipPart", 
						  fields: ["partId","partName"],
						  pageSize: 20, minListWidth: 200, width: UpdateTrainBuild.bigFieldWidth, editable: true },
				    { id:"partName_formId", name:"partName", xtype: "hidden" },
            		{ name: "fixPlaceSEQ", fieldLabel:"序号", maxLength: 8, xtype:"numberfield", width: UpdateTrainBuild.bigFieldWidth}            		
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
UpdateTrainBuild.childTrainBuildUpGrid = new Ext.yunda.Grid({
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
		header:'安装位置编码', dataIndex:'fixPlaceCode', editor:{ id:'fixPlaceCode_newId' , disabled: true, allowBlank:false, maxLength:10 }
	},{
		header:'安装位置编码全名', dataIndex:'fixPlaceFullCode', editor:{ xtype:"hidden",  maxLength:200 }, searcher : {disabled : true}
	},{
		header:'安装位置名称', dataIndex:'fixPlaceName', editor:{ allowBlank:false,  maxLength:50, allowBlank: false }
	},{
		header:'安装位置名称全名', dataIndex:'fixPlaceFullName', editor:{ xtype:"hidden", maxLength:500 }, searcher : {disabled : true}
	},{
		header:'安装位置序号', dataIndex:'fixPlaceSEQ', editor:{ xtype:'numberfield', maxLength:8, vtype: 'positiveInt' },searcher : {disabled : true}
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
 			editable: false,
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
		UpdateTrainBuild.getFixPlaceCode();
		this.store.reload();
		alertSuccess();     		
        TrainBuild.tree.root.reload();
        TrainBuild.tree.getRootNode().expand();
    },
    afterDeleteFn: function(){ 
    	//删除成功后刷新节点
        var path = TrainBuild.tree.getNodeById(nodeId).getPath('id');
        TrainBuild.tree.getLoader().load(TrainBuild.tree.getNodeById(nodeId),function(treeNode){  
        //展开路径,并在回调函数里面选择该节点  
        TrainBuild.tree.expandPath(path,'id',function(bSuccess,oLastNode){        	
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
		UpdateTrainBuild.getFixPlaceCode();
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
UpdateTrainBuild.tabs = new Ext.TabPanel({
    activeTab: 0,
    frame:true,
    singleSelect: true,
    items:[{  
           id: "trainBuildUpTab",
           title: '安装位置信息',
           layout:'fit',
           frame:true,
           items: [UpdateTrainBuild.trainBuildUpForm]
        },{ 
          id: "childTrainBuildUpTab",
          title: '下级安装位置',
          layout: 'fit' ,
          items: [UpdateTrainBuild.childTrainBuildUpGrid]
       },
       	{ 
          id: "fixBuildUpTypeTab",
          title: '可安装组成型号',
          frame:true,
          layout: 'fit' ,
          items: [UpdateTrainBuild.fixBuildUpTypeGrid]
       }]
});
//页面布局
UpdateTrainBuild.viewport = new Ext.Panel( {
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
        minSize : 160,
        maxSize : 280,
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
        items : [ UpdateTrainBuild.tabs ]
    } ]
});

//机车组成维护
UpdateTrainBuild.win =  new Ext.Window({
    title: "机车组成维护", maximized :true, width: 800, height: 400, layout: "fit", plain: true, closeAction: "hide",buttonAlign: 'center',
    items: [UpdateTrainBuild.viewport],
    buttons: [{
                text: "关闭", iconCls: "closeIcon", handler: function(){ UpdateTrainBuild.win.hide(); }
            }]
});


});