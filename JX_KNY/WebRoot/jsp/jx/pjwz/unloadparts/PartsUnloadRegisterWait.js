/**
 * 下车配件登记单--未登记 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsUnloadRegisterWait');                       //定义命名空间
PartsUnloadRegisterWait.fieldWidth = 160;
PartsUnloadRegisterWait.labelWidth = 90;
PartsUnloadRegisterWait.searchParam = {};
//规格型号选择控件赋值函数
PartsUnloadRegisterWait.callReturnFn=function(node,e){
  PartsUnloadRegisterWait.saveForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
  PartsUnloadRegisterWait.saveForm.find("name","partsName")[0].setValue(node.attributes["partsName"]);
  PartsUnloadRegisterWait.saveForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
}
PartsUnloadRegisterWait.saveForm = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: PartsUnloadRegisterWait.labelWidth,
	    buttonAlign:"center",
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:PartsUnloadRegisterWait.labelWidth,
	            columnWidth: 0.5, 
	            items: [
		                    {xtype:'textfield',fieldLabel:'id',name:"idx",hidden:true},
		                    {xtype:'textfield',fieldLabel:'车型',name:"unloadTrainType", readOnly: true, style: 'background:none; border: none;'},
		                    {id:"unloadTrainTypeIdx",xtype:'textfield',fieldLabel:'车型id',name:"unloadTrainTypeIdx",hidden:true},
		                    {xtype:'textfield',fieldLabel:'修程',name:"unloadRepairClass", readOnly: true, style: 'background:none; border: none;'},
		            		{xtype:'textfield',fieldLabel:'修程id',name:"unloadRepairClassIdx",hidden:true},
		            		{
		                     xtype:"PartsTypeTreeSelect",
		                     id:'PartsTypeTreeSelect_select',
		                     fieldLabel: '配件规格型号',
		                     allowBlank:false,
						  	 hiddenName: 'specificationModel', 
						  	 editable:false,
						  	 onTriggerClick: function() {
							        if(this.disabled)  return;
							        //选择配件规格型号前先选车型
			                		var trainTypeId =  Ext.getCmp("unloadTrainTypeIdx").getValue();
							        jx.pjwz.PartsTypeSelect.returnFn = this.returnFn;
							        jx.pjwz.PartsTypeSelect.init();
							        if(jx.pjwz.PartsTypeSelect.win == null)  
							        jx.pjwz.PartsTypeSelect.createWin();
							        jx.pjwz.PartsTypeSelect.win.show();
							    },
							  	returnFn: PartsUnloadRegisterWait.callReturnFn,
							  	width: PartsUnloadRegisterWait.fieldWidth
			                  },{
								id:"PartsUnloadRegister_partsNo",fieldLabel: "配件编号", allowBlank:false,maxLength:25, name:"partsNo",width: PartsUnloadRegisterWait.fieldWidth
							  },{
					        	id:"orgDic_comb",
					        	xtype: "Base_combo",
					        	fieldLabel: "接收部门",
								fields:['orgid','orgSeq','orgName'],
								hiddenName: "takeOverDeptId",
								business: 'orgDicItem',
								queryParams: {dictTypeId: 'accountorg'},
								returnField:[
									 {widgetId:"PartsUnloadRegister_takeOverDept",propertyName:"orgName"},
							  		 {widgetId:"PartsUnloadRegister_takeOverDeptOrgseq",propertyName:"orgSeq"}
							  	], 
							  	idProperty: 'orgid',
								displayField: "orgName", 
								valueField: "orgid",
								pageSize: 20, 
								minListWidth: 200, 
								editable:false,
								isAll:true
				            },{
								id:"PartsUnloadRegister_takeOverDept",xtype:"hidden", name:"takeOverDept"
							},{
								id:"PartsUnloadRegister_takeOverDeptOrgseq",xtype:"hidden", name:"takeOverDeptOrgseq"
							},
							{id:"unloadReason_v",xtype: 'Base_combo',hiddenName: "unloadReason",fieldLabel:"下车原因",
			 				 entity:"com.yunda.jxpz.phrasedic.entity.PhraseDicItem",
			 				 queryParams: {'dictTypeId':'reasoncode'}, 
			 				 displayField:"dictItemDesc",valueField:"dictItemDesc",fields:["dictItemDesc"],			 
			 				 width: PartsUnloadRegisterWait.fieldWidth},
			 				 {
								fieldLabel:"下车位置", name:"unloadPlace",maxLength:50 ,width: PartsUnloadRegisterWait.fieldWidth
							},
			 				 { id:"factory_comb", editable:true,forceSelection:false,selectOnFocus:false,typeAhead:false,
			    	    		 xtype: 'Base_combo',hiddenName: "madeFactoryName",fieldLabel:"生产厂家",
				 				 entity:"com.yunda.jx.pjwz.partsBase.madefactory.entity.PartsMadeFactory",
				 				 displayField:"madeFactoryName",valueField:"madeFactoryName",fields:["id","madeFactoryName"],			 
				 				 returnField:[{widgetId:"madeFactoryIdx_id",propertyName:"id"}], 
				 				 width: PartsUnloadRegisterWait.fieldWidth
				 				 },{
								id:"madeFactoryIdx_id",xtype:"hidden", name:"madeFactoryIdx"
							}
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:PartsUnloadRegisterWait.labelWidth,
	            columnWidth: 0.5, 
	            items: [
					  {xtype:'textfield',fieldLabel:'车号',name:"unloadTrainNo", readOnly: true, style: 'background:none; border: none;'},
					  {xtype:'textfield',fieldLabel:'修次',name:"unloadRepairTime", readOnly: true, style: 'background:none; border: none;'},
		              {xtype:'textfield',fieldLabel:'修次id',name:"unloadRepairTimeIdx",hidden:true},
		              { 
	            		id:"partsName", name:"partsName", fieldLabel:"配件名称",maxLength:50 ,allowBlank:false,width: PartsUnloadRegisterWait.fieldWidth
	            	},{ 
	            		id:"partsTypeIDX", name:"partsTypeIDX", fieldLabel:"规格型号id",hidden:true 
	            	},
	            	{ 
		            	id:"takeOver_select",
		            	xtype:'OmEmployee_SelectWin',
		            	editable:false,
		            	fieldLabel : "接收人",
		            	hiddenName:'takeOverEmpId',
						returnField:[{widgetId:"PartsUnloadRegister_takeOverEmp",propertyName:"empname"}],
						width: PartsUnloadRegisterWait.fieldWidth
					},{
						id:"PartsUnloadRegister_takeOverEmp",xtype:"hidden", name:"takeOverEmp"
					},
	            	{ 
		            	id:"handOver_select",
		            	xtype:'OmEmployee_SelectWin',
		            	editable:false,
		            	fieldLabel : "交件人",
		            	hiddenName:'handOverEmpId',
						returnField:[{widgetId:"PartsUnloadRegister_handOverEmp",propertyName:"empname"}],
						width: PartsUnloadRegisterWait.fieldWidth
					},{
						id:"PartsUnloadRegister_handOverEmp",xtype:"hidden", name:"handOverEmp"
					},
					{ 
		            	id:"unloadDate",name: "unloadDate", 
		            	fieldLabel: "下车日期",
		            	xtype: "my97date",
		            	format: "Y-m-d" ,width: PartsUnloadRegisterWait.fieldWidth 
					},
					{xtype: 'Base_combo',hiddenName: "location",fieldLabel:"存放位置",
		 				 entity:"com.yunda.jxpz.phrasedic.entity.PhraseDicItem",
		 				 queryParams: {'dictTypeId':'locationcode'}, 
		 				 displayField:"dictItemDesc",valueField:"dictItemDesc",fields:["dictItemDesc"],			 
		 				 width: PartsUnloadRegisterWait.fieldWidth,maxLength:100},
					{ 
		            	id:"factoryDate",name: "factoryDate", 
		            	fieldLabel: "出厂日期",
		            	xtype: "my97date",
		            	format: "Y-m-d"  ,width: PartsUnloadRegisterWait.fieldWidth
					}
	            ]
	        },
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:PartsUnloadRegisterWait.labelWidth,
	            columnWidth: 1, 
	            items: [
	            	{
						xtype:"textfield",
						fieldLabel:"详细配置",
						anchor:"95%",
						maxLength:100,
						name:"configDetail",
						xtype:"textarea",
						height: 70
					}
	            ]
	        }
	        ]
	    }]
	});
PartsUnloadRegisterWait.saveWin = new Ext.Window({title:"下车配件登记情况",
				width:800, height:400,
				plain: true, maximized:false, modal:true,
				closeAction: 'hide', 
//				layout:"border",
				items:[PartsUnloadRegisterWait.saveForm],
				buttonAlign: 'center',
	            buttons: [{
                text: "保存", iconCls: "saveIcon", scope: this, handler: function(){ PartsUnloadRegisterWait.grid.saveFn(); }
           		 },{
	                text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ PartsUnloadRegisterWait.saveWin.hide(); }
	            }]
	})
PartsUnloadRegisterWait.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsUnloadRegister!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsUnloadRegister!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsUnloadRegister!logicDelete.action',            //删除数据的请求URL
    storeAutoLoad : false,
	saveForm: PartsUnloadRegisterWait.saveForm,
	saveWin: PartsUnloadRegisterWait.saveWin,
    viewConfig : [],
    tbar: ['add'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'单据编号', dataIndex:'billNo', hidden:true, editor:{ maxLength:50 }
	},{
		header:'规格型号', dataIndex:'specificationModel', width:150,editor:{  maxLength:100 }
	},{
		header:'配件名称', dataIndex:'partsName', editor:{  maxLength:100 }
	},{
		header:'配件编号', dataIndex:'partsNo', width:120,editor:{  maxLength:50 }
	},{
		header:'走行公里', dataIndex:'runingKM',width:100, editor:{ xtype:'numberfield', maxLength:8 }
	},{
		header:'下车车型', dataIndex:'unloadTrainType', width:80,editor:{  maxLength:50 }
	},{
		header:'下车车型编码', dataIndex:'unloadTrainTypeIdx',hidden:true, editor:{  maxLength:8 }
	},{
		header:'下车车号', dataIndex:'unloadTrainNo', width:80,editor:{  maxLength:50 }
	},{
		header:'下车修程编码', dataIndex:'unloadRepairClassIdx', hidden:true,editor:{  maxLength:8 }
	},{
		header:'下车修程', dataIndex:'unloadRepairClass',width:80, editor:{  maxLength:50 }
	},{
		header:'下车修次编码', dataIndex:'unloadRepairTimeIdx',hidden:true, editor:{  maxLength:8 }
	},{
		header:'下车修次', dataIndex:'unloadRepairTime', width:80,editor:{  maxLength:50 }
	},{
		header:'接收部门主键', dataIndex:'takeOverDeptId',hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'接收部门', dataIndex:'takeOverDept', width:150,editor:{  maxLength:100 }
	},{
		header:'接收部门序列', dataIndex:'takeOverDeptOrgseq',hidden:true, editor:{  maxLength:512 }
	},{
		header:'接收人主键', dataIndex:'takeOverEmpId', hidden:true,editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'接收人', dataIndex:'takeOverEmp', width:80,width:80,editor:{  maxLength:50 }
	},{
		header:'交件人主键', dataIndex:'handOverEmpId',hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'交件人', dataIndex:'handOverEmp', width:80,width:80,editor:{  maxLength:25 }
	},{
		header:'存放地点', dataIndex:'location',width:150, editor:{  maxLength:100 }
	},{
		header:'下车原因', dataIndex:'unloadReason',width:150, editor:{  maxLength:100 }
	},{
		header:'下车位置', dataIndex:'unloadPlace',width:150, editor:{  maxLength:100 }
	},{
		header:'下车日期', dataIndex:'unloadDate', width:150,xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'出厂日期', dataIndex:'factoryDate',width:150, xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'接收部门类型', dataIndex:'takeOverDeptType',hidden:true, editor:{ xtype:'numberfield', maxLength:1 }
	},{
		header:'配件信息主键', dataIndex:'partsAccountIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'配件规格型号主键', dataIndex:'partsTypeIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'生产厂家主键', dataIndex:'madeFactoryIdx',hidden:true, editor:{  maxLength:5 }
	},{
		header:'生产厂家', dataIndex:'madeFactoryName', width:150,editor:{  maxLength:50 }
	},{
		header:'详细配置', dataIndex:'configDetail', width:150,editor:{  maxLength:200 }
	},{
		header:'单据状态', dataIndex:'status',hidden:true, editor:{  maxLength:20 }
	},{
		header:'创建人名称', dataIndex:'creatorName', hidden:true,editor:{  maxLength:50 }
	}],
	afterShowSaveWin: function(){
		var record = TrainWorkPlanView.grid.store.getById(TrainWorkPlanView.rdpIdx);
		PartsUnloadRegisterWait.saveForm.find("name","unloadTrainTypeIdx")[0].setValue(record.get("trainTypeIdx"));
		PartsUnloadRegisterWait.saveForm.find("name","unloadTrainType")[0].setValue(record.get("trainTypeShortName"));
		PartsUnloadRegisterWait.saveForm.find("name","unloadTrainNo")[0].setValue(record.get("trainNo"));
		PartsUnloadRegisterWait.saveForm.find("name","unloadRepairClass")[0].setValue(record.get("repairClassName"));
		PartsUnloadRegisterWait.saveForm.find("name","unloadRepairClassIdx")[0].setValue(record.get("repairClassIdx"));
		PartsUnloadRegisterWait.saveForm.find("name","unloadRepairTimeIdx")[0].setValue(record.get("repairtimeIdx"));
		PartsUnloadRegisterWait.saveForm.find("name","unloadRepairTime")[0].setValue(record.get("repairtimeName"));
		
	},
	afterShowEditWin: function(record, rowIndex){
		Ext.getCmp("PartsTypeTreeSelect_select").setValue(record.get("specificationModel"));
		Ext.getCmp("takeOver_select").setDisplayValue(record.get("takeOverEmpId"),record.get("takeOverEmp"));
		Ext.getCmp("handOver_select").setDisplayValue(record.get("handOverEmpId"),record.get("handOverEmp"));
		Ext.getCmp("orgDic_comb").setDisplayValue(record.get("takeOverDeptId"),record.get("takeOverDept"));
	},
	beforeSaveFn: function(data){ 
		data.specificationModel = Ext.getCmp("PartsTypeTreeSelect_select").getValue();
		data.status = STATUS_WAIT ;//状态为未登记
		data.isInRange = IS_IN_RANGE_NO ;//范围外下车
		data.rdpIdx = TrainWorkPlanView.rdpIdx ;
		delete data.PartsTypeTreeSelect_select ;
		return true; 
	},
	afterSaveSuccessFn: function(result, response, options){
        PartsUnloadRegisterWait.grid.store.reload();
        PartsUnloadRegisterWait.saveWin.hide();
        alertSuccess();
    },
    afterSaveFailFn: function(result, response, options){
    	PartsUnloadRegisterWait.grid.store.reload();
//    	PartsUnloadRegisterWait.saveWin.hide();
        alertFail(result.errMsg);
    }
});
//查询前添加过滤条件
PartsUnloadRegisterWait.grid.store.on('beforeload',function(){
	var searchParam = PartsUnloadRegisterWait.searchParam;
	searchParam.status = STATUS_WAIT;  //状态--未登记
	searchParam.rdpIdx = TrainWorkPlanView.rdpIdx;  
    this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
});