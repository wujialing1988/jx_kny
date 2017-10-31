Ext.namespace("handled")
/*** 查询表单 start ***/
handled.searchLabelWidth = 90;
handled.searchAnchor = '95%';
handled.searchFieldWidth = 270;
handled.searchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: handled.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.33,
			fieldWidth: handled.searchFieldWidth, labelWidth: handled.searchLabelWidth, defaults:{anchor:handled.searchAnchor},
			items:[{ 
				fieldLabel: "车型",
				hiddenName: "trainSortName",
				displayField: "shortName", valueField: "shortName",
				pageSize: 0, minListWidth: 200,
				editable:true,
				forceSelection: true,
				xtype: "Base_combo",
	        	business: 'trainType',													
				fields:['typeID','shortName'],
				queryParams: {'isCx':'yes'},
				entity:'com.yunda.jx.base.jcgy.entity.TrainType',
				listeners : {   
		        	"select" : function() {   
		            	//重新加载车号下拉数据
		                var trainNo_comb = handled.searchForm.getForm().findField("trainNo");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.trainTypeShortName = this.getValue();
		                trainNo_comb.cascadeStore();	
		        	}   
		    	}
			},{
				fieldLabel:'作业工单名称',
				xtype: "textfield",
				name: "workCardName"
			}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.33,
			fieldWidth: handled.searchFieldWidth, labelWidth: handled.searchLabelWidth, defaults:{anchor:handled.searchAnchor},
			items:[{
				id: "trainNo_comb_search",
				fieldLabel: "车号",
				hiddenName: "trainNo", 
				displayField: "trainNo", valueField: "trainNo",
				pageSize: 20, minListWidth: 200,
				minChars : 1,
				maxLength : 5,
				anchor: "95%", 				
				xtype: "Base_combo",
				business: 'trainNo',
				entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',
				fields:["trainNo","makeFactoryIDX","makeFactoryName",
				{name:"leaveDate", type:"date", dateFormat: 'time'},
				"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse","trainUseName",
				"bId","dId","bName","dName","bShortName","dShortName"],
				queryParams:{'isCx':'no','isIn':'false','isRemoveRun':'false'},
				isAll: 'yes',
				editable:true
			}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.34,
			fieldWidth: handled.searchFieldWidth, labelWidth: handled.searchLabelWidth, defaults:{anchor:handled.searchAnchor},
			items:[{
				id:"rc_comb",
				xtype: "Base_combo",
				fieldLabel: "修程",
				hiddenName: "repairClassName",
				business: 'trainRC',
				fields:['xcID','xcName'],
				displayField: "xcName",
				valueField: "xcName",
				pageSize: 0, minListWidth: 200
			}]
		}]
	}],
	buttons:[{
			text:'查询', iconCls:'searchIcon', 
			handler: function(){ 
				var form = handled.searchForm.getForm();
		        var searchParam = form.getValues();
		        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
					searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
				}
                searchParam = MyJson.deleteBlankProp(searchParam);
				handled.grid.searchFn(searchParam); 
			}
		},{
            text: "重置", iconCls: "resetIcon", handler: function(){ 
            	var form = handled.searchForm;
            	form.getForm().reset();
            	//清空自定义组件的值
                var componentArray = ["Base_combo"];
                for (var j = 0; j < componentArray.length; j++) {
                	var component = form.findByType(componentArray[j]);
                	if (!Ext.isEmpty(component) && Ext.isArray(component)) {
						for (var i = 0; i < component.length; i++) {
							component[i].clearValue();
						}						
					}	                    
                }
                var trainNo_comb = handled.searchForm.getForm().findField("trainNo");   
                delete trainNo_comb.queryParams.trainTypeShortName;
                trainNo_comb.cascadeStore();
            	searchParam = {};
            	handled.grid.searchFn(searchParam);
            }
		}]
});
/*** 查询表单 end ***/
/* ***********
 * 已处理作业卡
 */

handled.grid = new Ext.yunda.Grid({
	loadURL: ctx + '/workCard!findFinishedWorkCard.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/workCard!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/workCard!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:3, searchFormColNum:2,
    viewConfig: {forceFit: false, markDirty: false}, 
    tbar:['refresh'],
    fields: [
    	Attachment.createColModeJson({ attachmentKeyName:'JXGC_Work_Card',disableButton:['删除',"新增"] }),
	{
		header:'IDX', dataIndex:'idx', editor: { xtype:"hidden"}, hidden:true
	},{
		header:'作业编码', dataIndex:'workCardCode', editor: { }, searcher:{disabled:true}, hidden:true
	},{
		header:'作业名称', dataIndex:'workCardName', width:300, editor: { }, searcher:{ anchor:'98%' }
	},{
		header:'车型', dataIndex:'trainSortName', editor: { } ,hidden:true
	},{
		header:'车型', dataIndex:'trainTypeIdx', width:50, editor: { },hidden:true
	},{
		header:'车号', dataIndex:'trainNo', editor: { } ,hidden:true
	},{
		header:'车型车号', dataIndex:'trainTypeTrainNo',editor: { }
	},{
		header:'修程 | 修次', dataIndex:'repairClassRepairTime', editor: { }, width:85
	},{
		header:'修程', dataIndex:'repairClassName', editor: { }, width:50, hidden:true, searcher: { }
	},{	
		header:'修次IDX', dataIndex:'repairTimeIdx', editor: { }, hidden:true
	},{
		header:'修程IDX', dataIndex:'repairClassIdx', editor: { }, hidden:true
	},{
		header:'作业内容', dataIndex:'workScope', width:250, editor: { }, searcher:{disabled:true}, hidden:true
	},{
		header:'流程节点', dataIndex:'nodeCaseName', editor: { }, searcher:{ anchor:'98%' }
	},{
		header:'状态', dataIndex:'status', hidden:true, editor: { },searcher:{disabled:true}
	},{
		header:'备注', dataIndex:'remarks', hidden:true, editor: { }, searcher:{disabled:true}
	},{
		header:'转入时间', dataIndex:'transinTimeStr', hidden:true, editor: { }
	},{
		header:'计划交车时间', dataIndex:'planTrainTimeStr', hidden:true, editor: { }
	},{
		header:'工位名称', dataIndex:'workStationName', hidden:true, editor: { }
	},{
		header:'工位负责人名称', dataIndex:'dutyPersonName', hidden:true, editor: { }
	},{
		header:'注意事项', dataIndex:'safeAnnouncements', hidden:true, editor: { }
	},{
		header:'互换配件信息主键', dataIndex:'partsAccountIDX', hidden:true, editor: { }
	},{
		header:'互换配件型号主键', dataIndex:'partsTypeIDX', hidden:true, editor: { }
	},{
		header:'计划开工时间', dataIndex:'planBeginTimeStr', hidden:true, editor: { }
	},{
		header:'计划完工时间', dataIndex:'planEndTimeStr', hidden:true, editor: { }
	},{
		header:'rdpIdx', dataIndex:'rdpIDX', hidden:true, editor: { }
	},{
		header:'开工时间', dataIndex:'realBeginTimeStr', 
		editor:{ xtype:'hidden' }, hidden:true
	},{
		header:'完工时间', dataIndex:'realEndTimeStr', 
		editor:{ xtype:'hidden' }, hidden:true
	}],
	searchFn:function(searchParam){
    	handled.searchParam = searchParam ;
        this.store.load();
	},
    toEditFn:function(grid, ri, e){
    	var taskForm = form.taskForm.getForm();
//    	taskForm.findField("repairResult").disable();
    	form.createWorkerForm();
    	var baseForm = form.baseForm.getForm();
    	baseForm.findField("remarks").readOnly = true;
    	var record = grid.store.getAt(ri);
    	
    	form.titleForm.show();
    	form.titleForm.getForm().reset();
    	form.titleForm.getForm().loadRecord(record);
    	
    	form.baseForm.getForm().reset();
    	form.baseForm.getForm().loadRecord(record);
    	win.createHandlerWin();
    	win.handlerWin.show();//显示操作界面
    	handler.currentIdx = record.get("idx");//设置唯一操作主键 
    	taskItem.grid.store.load();//刷新作业任务
    	Worker.grid.store.load();//刷新作业人员
    	
    	baseForm.findField("realBeginTime").setValue(record.get("realBeginTimeStr"));
    	baseForm.findField("realEndTime").setValue(record.get("realEndTimeStr"));
    	
		
		Ext.getCmp("saveWorkTask").setVisible(false);
		Ext.getCmp("saveParts").setVisible(false);

    }
});

handled.grid.store.on("beforeload",function(){	
	var searchParam = handled.searchParam||{};
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});