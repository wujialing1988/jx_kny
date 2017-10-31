/**
 * 机车信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('JczlTrain');                       //定义命名空间
JczlTrain.searchParam = {} ;
JczlTrain.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/jczlTrain!searchPageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/jczlTrain!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/jczlTrain!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:3, searchFormColNum:2,
    viewConfig: null,
    tbar: ['search',{
    	text:"新配" ,iconCls:"addIcon" ,handler: function(){
    		TrainTransferDetailAdd.grid.addButtonFn();
    		Ext.getCmp("comboTree_holdOrgId").setDisplayValue(systemOrgid,systemOrgname); //配属单位
    		Ext.getCmp("holdOrgSeqId").setValue(systemOrgseq);
    		Ext.getCmp("holdOrgNameId").setValue(systemOrgname);
    		Ext.getCmp("comboTree_usedOrgId").setDisplayValue(systemOrgid,systemOrgname); //支配单位
    	}
	},{ 
    	text:"调入" ,iconCls:"addIcon" ,handler: function(){
    		TrainTransferDetailMoveIn.grid.addButtonFn();
    		Ext.getCmp("comboTree_holdOrgId_In").setDisplayValue(systemOrgid,systemOrgname); //配属单位
    		Ext.getCmp("holdOrgSeqId_In").setValue(systemOrgseq);
    		Ext.getCmp("holdOrgNameId_In").setValue(systemOrgname);
    		Ext.getCmp("comboTree_usedOrgId_In").setDisplayValue(systemOrgid,systemOrgname); //支配单位
    	}
    },{ 
    	text:"批量调入" ,iconCls:"addIcon" ,handler: function(){
    		BatchMoveIn.saveWin.show();
    		var cardPanel = Ext.getCmp("cardPanel");
    		cardPanel.layout.setActiveItem(0);
			cardPanel.buttons[0].setVisible(false);
			cardPanel.buttons[2].disable();
			cardPanel.buttons[3].enable();
			BatchMoveIn.grid.store.load();
			Train.grid.store.load();
			BatchMoveIn.grid.saveForm.getForm().reset();
    		Ext.getCmp("holdOrgId_batch").setDisplayValue(systemOrgid,systemOrgname); //配属单位
    		Ext.getCmp("holdOrgSeqId_batch").setValue(systemOrgseq);
    		Ext.getCmp("holdOrgNameId_batch").setValue(systemOrgname);
    		Ext.getCmp("usedOrgId_batch").setDisplayValue(systemOrgid,systemOrgname); //支配单位
    	}
    },{
    	text:"调出" ,iconCls:"application_goIcon" ,handler: function(){
    		var sm = JczlTrain.grid.getSelectionModel();
            if (sm.getCount() != 1) {
                MyExt.Msg.alert("请选择一条记录！");
                return;
            }
//            var rowIndex = JczlTrain.grid.store.indexOf(sm.getSelected());  //返回选中的行号
            var data = sm.getSelected();
            TrainTransferDetailMoveOut.grid.saveWin.setTitle('调出机车');
            TrainTransferDetailMoveOut.grid.saveWin.show();
            Ext.getCmp("trainTypeIDX_Out").setValue(data.get("trainTypeIDX"));
            Ext.getCmp("trainTypeName_Out").setValue(data.get("trainTypeShortName"));
            Ext.getCmp("trainNo_Out").setValue(data.get("trainNo"));
            Ext.getCmp("oldHoldOrgId_Out").setValue(data.get("holdOrgId"));
            Ext.getCmp("oldHoldOrgName_Out").setValue(data.get("holdOrgName"));
            Ext.getCmp("trainUseNameId_Out").setValue(data.get("trainUse")); //这里使用别显示为中文
            
    	}
    },
//    {
//    	text:"转备",iconCls:"keyIcon" ,handler: function(){}
//    },{
//    	text:"解备",iconCls:"keyIcon" ,handler: function(){}
//    },
    {
    	text:"变更使用别",iconCls:"edit1Icon" ,handler: function(){
    		var sm = JczlTrain.grid.getSelectionModel();
            if (sm.getCount() != 1) {
                MyExt.Msg.alert("请选择一条记录！");
                return;
            }
            var data = sm.getSelected();
            TrainUseChange.grid.saveWin.setTitle('变更机车使用别');
            TrainUseChange.grid.saveWin.show();
            Ext.getCmp("trainId").setValue(data.get("idx"));
            Ext.getCmp("trainTypeName_chg").setValue(data.get("trainTypeShortName"));
            Ext.getCmp("trainNo_chg").setValue(data.get("trainNo"));
            Ext.getCmp("oldTrainUseID_chg").setValue(data.get("trainUse"));
            Ext.getCmp("oldTrainUseName_chg").setValue(data.get("trainUse"));
    	}
    },{
    	text:"报废" ,iconCls: "dustbinIcon",handler: function(){
    		
    		var sm = JczlTrain.grid.getSelectionModel();
            if (sm.getCount() != 1) {
                MyExt.Msg.alert("请选择一条记录！");
                return;
            }
            var data = sm.getSelected();
            TrainScrap.grid.saveWin.setTitle('机车报废');
            TrainScrap.grid.saveWin.show();
            TrainScrap.grid.saveForm.getForm().reset();  //重置表单
            Ext.getCmp("trainId_scp").setValue(data.get("idx"));
            Ext.getCmp("trainTypeName_scp").setValue(data.get("trainTypeShortName"));
            Ext.getCmp("trainNo_scp").setValue(data.get("trainNo"));
    	}
    },
    'refresh'],
	fields: [{
			header:'idx', dataIndex:'idx', hidden:true ,editor: { xtype:"hidden" }
		},{
		header:'trainTypeIDX', dataIndex:'trainTypeIDX', width:0, hidden:true,
		editor:{
			allowBlank:false ,
			id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "车型",
			hiddenName: "trainTypeIDX", 
			returnField: [{widgetId:"trainTypeShortName",propertyName:"shortName"}],
			displayField: "shortName", valueField: "typeID",
			pageSize: 0, minListWidth: 200, 
			width: JczlTrain.fieldWidth,
			editable:true,
			listeners : {   
	        	"collapse" : function() {   
	            	//重新加载车号下拉数据
	                var trainNo_comb = Ext.getCmp("trainNo_comb");   
	                trainNo_comb.reset();  
	                Ext.getCmp("trainNo_comb").clearValue(); 
	                trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
	                trainNo_comb.cascadeStore();
	        	}   
	    	}
		}
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{ 			
			xtype:"hidden"
		},searcher:{}
	},{
		header:'车号', dataIndex:'trainNo',
		editor:{ 
			allowBlank:false ,
			id:"trainNo_comb",xtype: "TrainNo_combo",	fieldLabel: "车号",
			hiddenName: "trainNo", 
			displayField: "trainNo", valueField: "trainNo",
			pageSize: 20, minListWidth: 200, 
			width: JczlTrain.fieldWidth
		},searcher:{}
	},{
		header:'资产状态', dataIndex:'assetState', 
		editor:{ 
			xtype: 'combo',	        	        
	        hiddenName:'assetState',
	        store:new Ext.data.SimpleStore({
			    fields: ['v', 't'],
				data : [[assetStateUse,"使用中"],[assetStateScrap,"报废"]]
			}),
			valueField:'v',
			displayField:'t',
			triggerAction:'all',
			mode:'local'
		},
		renderer:function(v){
			switch(v){
				case assetStateUse:
					return "使用中";
				case assetStateScrap:
					return "报废";
				default:
					return v;
			}
		},searcher:{disabled: true}
	},{
		header:'机车状态', dataIndex:'trainState', editor:{ 		
			xtype: 'combo',	        	        
	        hiddenName:'trainState',
	        store:new Ext.data.SimpleStore({
			    fields: ['v', 't'],
				data : [[trainStateRepair,"检修"],[trainStateUse,"运用"],[trainStateSpare,"备用"]]
			}),
			valueField:'v',
			displayField:'t',
			triggerAction:'all',
			mode:'local'
		},
		renderer:function(v){
			switch(v){
				case trainStateRepair:
					return "检修";
				case trainStateUse:
					return "运用";
				case trainStateSpare:
					return "备用";
				default:
					return v;
			}
		}		
	},{
		header:'使用别', dataIndex:'trainUse', 
		editor:{
			id:"train_Use",
			xtype: 'Base_combo',
			hiddenName: 'trainUse',
			entity:'com.yunda.jx.base.jcgy.entity.TrainUse',			
			fields: ["useID","useName"],displayField:'useName',valueField:'useID'
		}
	},{
		header:'配属局', dataIndex:'bName', editor:{}
	},{
		header:'配属段', dataIndex:'dName', editor:{}
	},{
		header:'配属单位ID', dataIndex:'holdOrgId', hidden:true, editor:{ xtype:'hidden', value:systemOrgid }
	},{
		header:'配属单位', dataIndex:'holdOrgName', editor:{ xtype:'textfield', value:systemOrgname, readOnly:true },searcher:{ disabled:true }
	},{
		header:'支配单位ID', dataIndex:'usedOrgId', hidden:true,
		editor:{ 
			id: 'usedOrg_Id',
			fieldLabel:'支配单位',
			xtype: 'OmOrganizationCustom_comboTree',
			hiddenName: 'usedOrgId',
			queryHql:"[degree]oversea",
			selectNodeModel:"leaf"/*,
			orgid:'0',
			orgname:orgRoot	*/
		}
	},{
		header:'支配单位', dataIndex:'usedOrgName', editor:{ maxLength:10 },searcher:{disabled: true}
	},{
		header:'原配属单位ID', dataIndex:'oldHoldOrgId',hidden:true
	},{
		header:'原配属单位', dataIndex:'oldHoldOrgName', editor:{  maxLength:10 },searcher:{
			fieldLabel:'原配属单位',
			xtype: 'OmOrganizationCustom_comboTree',
			hiddenName: 'oldHoldOrgId',
			queryHql:"[degree]oversea",
			selectNodeModel:"leaf",
			orgid:'0',
			orgname:orgRoot	
		}
	},{
		header:'制造厂家主键', dataIndex:'makeFactoryIDX', hidden:true, editor:{  maxLength:5 },searcher:{disabled: true}
	},{
		header:'制造厂家名', dataIndex:'makeFactoryName', editor:{  maxLength:50 },searcher:{disabled: true}
	},{
		header:'出厂日期', dataIndex:'leaveDate', xtype:'datecolumn', editor:{ xtype:'my97date' },searcher:{disabled: true}
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{  maxLength:50 },searcher:{disabled: true}
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{  maxLength:50 },searcher:{disabled: true}
	},{
		header:'登记人', dataIndex:'registerPerson', hidden:true, editor:{ xtype:'numberfield', maxLength:10 },searcher:{disabled: true}
	},{
		header:'登记人', dataIndex:'registerPersonName', editor:{  maxLength:25 },searcher:{disabled: true}
	},{
		header:'登记时间', dataIndex:'registerTime', xtype:'datecolumn', editor:{ xtype:'my97date' },searcher:{disabled: true}
	},{
		header:'是否有履历', dataIndex:'isHaveResume',
		editor:{ 
			xtype: 'combo',	        	        
	        hiddenName:'assetState',
	        store:new Ext.data.SimpleStore({
			    fields: ['v', 't'],
				data : [[haveResume,"是"],[notHaveResume,"否"]]
			}),
			valueField:'v',
			displayField:'t',
			triggerAction:'all',
			mode:'local'
		},
		renderer:function(v){
			switch(v){
				case haveResume:
					return "是";
					break;
				case notHaveResume:
					return "否";
					break;
				default:
					return v;
					break;
			}
		},
		searcher:{ disabled:true }
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 },searcher:{disabled: true}
	}],
	searchFn: function(searchParam){ 
		JczlTrain.searchParam = searchParam ;
        this.store.load();
	}
});
//移除侦听器
JczlTrain.grid.un('rowdblclick', JczlTrain.grid.toEditFn, JczlTrain.grid);
//查询前添加过滤条件
JczlTrain.grid.store.on('beforeload' , function(){
	var searchParam = JczlTrain.searchParam;
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:JczlTrain.grid });

});