/**
 * 机车作业工单（质量控制配置在作业工单上） 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TrainQR');                       //定义命名空间
	TrainQR.searchParam = {} ;                    //定义查询条件
	TrainQR.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workSeq!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/workSeq!saveWorkSeqForQrKey.action',             //保存数据的请求URL
	    deleteURL: ctx + '/workSeq!deleteWorkSeq.action',            //删除数据的请求URL
	    saveFormColNum: 2 ,fieldWidth: 180,
	    searchFormColNum: 2,
//	    viewConfig: null,
	    tbar:['search',{
	    	text:"新增",iconCls:"addIcon" ,handler: function(){
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
	    	}
	    },'delete','refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: {id:"entityID", xtype:'hidden' }
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
				allowBlank: false
				}
		},{
			header:'车型', dataIndex:'pTrainTypeShortName', editor:{id: 'trainTypeShortName_Id', xtype: 'hidden' },
			searcher:{xtype: 'textfield'}, width: 60
		},{
			header:'检修记录卡名称', dataIndex:'workSeqName',width:200, editor:{ allowBlank:false, maxLength:50 }
		},{
			header:'额定工时(分)', dataIndex:'ratedWorkHours', hidden: false, editor:{ allowBlank:true, maxLength:50 },searcher: {disabled: true}
		},{
			header:'组成型号主键', dataIndex:'buildUpTypeIDX',hidden:true, editor:{id: 'buildUpTypeIDX_Id', xtype:'hidden', maxLength:50 }
		},{
			header:'描述', dataIndex:'repairScope', width:200, editor:{xtype:'textarea',  maxLength:1000 },searcher: {disabled: true}
		},{
			header:'安全注意事项', dataIndex:'safeAnnouncements',hidden:true, editor:{ xtype:'textarea', maxLength:1000 }
		},{
			header:'isVirtual', dataIndex:'isVirtual',hidden:true, editor:{ xtype:'hidden', id:"isVirtual_Id" }
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
	    },
	    editOrder: ['workSeqCode','workSeqName','pTrainTypeIDX','ratedWorkHours','buildUpTypeCode','buildUpTypeName','repairScope','safeAnnouncements']
	});
	
	//初始化表单
	TrainQR.grid.createSaveForm(); 
	/*
	//选择零部件型号前先选择车型
	Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.on("beforeshow",function(){
		var trainType_comb =  Ext.getCmp("trainType_comb").getValue();
		if(Ext.isEmpty(trainType_comb)){
			MyExt.Msg.alert("请先选择检修车型！");
			return false;
		}else{
			//根据车型过滤可安装的组成型号
	        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.grid.store.proxy = new Ext.data.HttpProxy({
	        												url: ctx + "/partsTypeByBuildSelect!buildUpTypeList.action" +
	        													"?typeIDX=" + Ext.getCmp("trainType_comb").getValue()
	        											        + "&type=0"});
	        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.grid.store.baseParams.typeIDX = Ext.getCmp("trainType_comb").getValue();
			Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.grid.store.baseParams.type = 0;
	        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.grid.store.load();
			return true;
		}
		
	});
	*/
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
							QREdit.saveFn(TrainQR.grid, TrainQR.editTabs) ; //调用新增方法（QREdit.js）中            			
	            		}
	            	},{
						text: "关闭", iconCls:"closeIcon", handler:function(){TrainQR.saveWin.hide();}
					}]
	            }
	        },{
	            title: "检测/检修项目", layout: "fit", border: false, items: [ WorkStep.grid ]
	        }/*,{
	            title: "配件清单", layout: "fit", border: false, items: [ PartsList.grid ]
	        }*/]
	});
	//作业工单编辑窗口
	TrainQR.saveWin = new Ext.Window({
		title: "检修记录单编辑", maximizable:false, layout: "fit", 
		closeAction: "hide", modal: true, maximized: true , buttonAlign:"center",
		items: [TrainQR.editTabs] 
	});
	TrainQR.grid.store.on("beforeload", function(){	
		var searchParam = TrainQR.searchParam;
		var whereList = [] ;
		for (prop in searchParam) {		
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:TrainQR.grid });
});