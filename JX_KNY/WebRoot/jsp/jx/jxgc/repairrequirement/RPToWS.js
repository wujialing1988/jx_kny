/**
 * 检修项目对应作业工单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainQR');                       //定义命名空间
TrainQR.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/workSeq!findWorkSeqByProject.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/workSeq!saveWorkSeqForQrKey.action',             //保存数据的请求URL
    deleteURL: ctx + '/workSeq!deleteWorkSeq.action',            //删除数据的请求URL
    saveFormColNum: 2 ,fieldWidth: 180,
	searchFormColNum: 2,
    tbar: [{
	    	text:"新增机车检修记录卡",iconCls:"addIcon" ,handler: function(){
	           //调用新增公用方法
	            QREdit.addFn(TrainQR.grid, TrainQR.editTabs, TrainQR.saveWin);
            	Ext.Ajax.request({
			        url: ctx + "/codeRuleConfig!getConfigRule.action",
			        params: {ruleFunction : "JXGC_WORK_SEQ_WORK_SEQ_CODE"},
			        success: function(response, options){
			            var result = Ext.util.JSON.decode(response.responseText);
			            Ext.getCmp("workSeqCodeId").setValue(result.rule);
			        }
				});
				Ext.getCmp("trainType_comb").setDisplayValue(RepairProjectEdit.pTrainTypeIdx,RepairProjectEdit.pTrainTypeName);
				Ext.getCmp("trainTypeShortName_Id").setValue(RepairProjectEdit.pTrainTypeName);
				Ext.getCmp("trainType_comb").disable();
	    	}
	    },'delete',{
    	text:"关闭",iconCls:"closeIcon" ,handler: function(){
    			RepairProject.saveWin.hide();
    	}
    }], 
	fields: [{
			header:'机车检修记录卡idx主键', dataIndex:'idx', hidden:true, editor: {id:"entityID", xtype:'hidden' }
		},{
			header:'机车检修记录单idx主键', dataIndex:'recordIDX', hidden:true, editor: {id:"recordIDX", xtype:'hidden' }
		},{
			header:'编号', dataIndex:'workSeqCode',width:120, editor:{ id:"workSeqCodeId",  maxLength:50 , allowBlank: false }
		},{
			header:'检修车型', dataIndex:'pTrainTypeIDX', hidden: true, 
			editor:{  
				id:"trainType_comb",xtype: "Base_combo",	fieldLabel: "检修车型",
				business: 'trainType',													
				entity:'com.yunda.jx.base.jcgy.entity.TrainType',
				fields:['typeID','shortName'],
				queryParams: {'isCx':'yes'},
				forceSelection: true,
				hiddenName: "pTrainTypeIDX", 
				returnField: [{widgetId:"trainTypeShortName_Id",propertyName:"shortName"}],
				displayField: "shortName", valueField: "typeID",
				pageSize: 0, minListWidth: 200,
				editable:true,
				allowBlank: false,
				listeners : {   
		        	"select" : function() {   
		            	Ext.getCmp("PartsTypeByBuild_SelectWin_Id").clearValue(); 
	                	Ext.getCmp("isVirtual_Id").setValue("");
		            	//根据车型过滤可安装的组成型号
				        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.grid.store.proxy = new Ext.data.HttpProxy({
				        												url: ctx + "/partsTypeByBuildSelect!buildUpTypeList.action" +
				        													"?typeIDX=" + Ext.getCmp("trainType_comb").getValue()
				        											        + "&type=0"});
				        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.grid.store.baseParams = {};
				        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.grid.store.baseParams.typeIDX = Ext.getCmp("trainType_comb").getValue();
				        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.grid.store.baseParams.type = 0;
				        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.grid.store.load();
		        	}   
		    	}}	    
		},{
			header:'车型', dataIndex:'pTrainTypeShortName',hidden: true,  editor:{id: 'trainTypeShortName_Id', xtype: 'hidden' },
			searcher:{xtype: 'textfield'}, width: 60
		},{
			header:'记录卡名称', dataIndex:'workSeqName',width:200, editor:{ allowBlank:false, maxLength:50 }
		},{
			header:'记录卡序号', dataIndex:'seq', editor:{  maxLength:2 ,vtype: "positiveInt"},searcher: {disabled: true}
		},{
			header:'描述', dataIndex:'repairScope',hidden: false,  width:200, editor:{xtype:'textarea',  maxLength:1000 },searcher: {disabled: true}
		},{
			header:'安全注意事项', dataIndex:'safeAnnouncements',hidden:true, editor:{ xtype:'textarea', maxLength:1000 }
		},{
			header:'isVirtual', dataIndex:'isVirtual',hidden:true, editor:{ xtype:'hidden', id:"isVirtual_Id" }
		},{
			header:'rpToWsIdx', dataIndex:'rpToWsIdx',hidden:true, editor:{ xtype:'hidden' }
		}],
	    searchFn: function(searchParam){ 
			TrainQR.searchParam = searchParam ;
	        this.store.load();
		},
		toEditFn: function(grid, rowIndex, e){
            var record = this.store.getAt(rowIndex);
            QREdit.clearQC();//清空质量选择项
            QREdit.SetWorkSeqQC(record.get("idx"), TrainQR.grid);//设置质量控制选择项
	        QREdit.toEditFn(grid, rowIndex, e, TrainQR.saveWin, TrainQR.editTabs);
	        Ext.getCmp("trainType_comb").disable();
	    	PartsList.grid.store.reload();
	    },
		afterSaveSuccessFn: function(result, response, options){
			TrainQR.grid.store.load();//刷新

			Ext.getCmp("trainType_comb").disable();
	    },
	    afterSaveFailFn: function(result, response, options){
	    	TrainQR.grid.store.reload();
	        alertFail(result.errMsg);
	        Ext.getCmp("trainType_comb").disable();
	    },
	    editOrder: ['workSeqCode','workSeqName','pTrainTypeIDX','ratedWorkHours','buildUpTypeCode','buildUpTypeName','repairScope','safeAnnouncements']
	});
//初始化表单
	TrainQR.grid.createSaveForm(); 
	QREdit.createSaveForm();
	//定义'fieldset'列表
	TrainQR.fieldset = [{
		bodyStyle: 'padding-left:10px;',
		items:{
			xtype: "fieldset",
			title: "基本信息编辑",
			autoHeight: true,
			width: 720,
			items: [
                TrainQR.grid.saveForm
            ]
		}
	},{
		bodyStyle: 'padding-left:10px;',
		items:{
			xtype: "fieldset",
			title: "质量检查项",
			width: 720,  //宽度设置
			items: QREdit.saveForm
		}
	}];
	//编辑选项卡列表
	TrainQR.editTabs = new Ext.TabPanel({
	    activeTab: 0, frame:true,
	    items:[{
	            title: "基本信息编辑", layout: "fit", frame: true, border: false, items: {
	                buttonAlign:"center",
	                autoScroll: true, //滚动条
	                defaults: {
	                    border: false
	                },            
	                items: TrainQR.fieldset,
	            	buttons: [{
	            		id:"trainWorkSeqSaveId", text: "保存", iconCls:"saveIcon", handler:function(){
	            			QREdit.recordIDX = RepairProjectEdit.idx;
							QREdit.saveFn(TrainQR.grid, TrainQR.editTabs) ; //调用新增方法（QREdit.js）中            			
	            		}
	            	},{
						text: "关闭", iconCls:"closeIcon", handler:function(){TrainQR.saveWin.hide();}
					}]
	            }
	        },{
	            title: "检测/检修项目", layout: "fit", border: false, items: [ WorkStep.grid ]
	        }/*,{
	            title: "配件清单2", layout: "fit", border: false, items: [ PartsList.grid ]
	        }*/]
	});
	//作业工单编辑窗口
	TrainQR.saveWin = new Ext.Window({
		title: "机车检修记录卡编辑", maximizable:false, layout: "fit", 
		closeAction: "hide", modal: true, maximized: true , buttonAlign:"center",
		items: [TrainQR.editTabs] 
	});
//查询前添加过滤条件
TrainQR.grid.store.on('beforeload',function(){
	var searchParam = {} ;
	searchParam.recordIDX = RepairProjectEdit.idx ; //检修项目主键
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
});