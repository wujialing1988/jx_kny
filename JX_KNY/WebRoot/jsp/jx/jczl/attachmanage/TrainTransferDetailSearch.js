/**
 * 机车调拨明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 * 引用此JS文件需在页面上定义变量search=false，履历引用此JS文件search=true。
 */
Ext.onReady(function(){
Ext.namespace('TrainTransferDetail');                       //定义命名空间

TrainTransferDetail.searchParam = {};

TrainTransferDetail.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainTransferDetail!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainTransferDetail!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainTransferDetail!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:2,  searchFormColNum:2, singleSelect:true,
    tbar:["search","refresh"],
    viewConfig: null,    
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'trainTypeIDX', dataIndex:'trainTypeIDX', width:0, hidden:search,
		editor:{
			allowBlank:false ,
			id:"trainType_comb_transfer",xtype: "TrainType_combo",	fieldLabel: "车型",
			hiddenName: "trainTypeIDX", 
			returnField: [{widgetId:"trainTypeShortName",propertyName:"shortName"}],
			displayField: "shortName", valueField: "typeID",
			pageSize: 0, minListWidth: 200, 
			width: TrainTransferDetail.fieldWidth,
			editable:true,
			listeners : {   
	        	"select" : function() {   
	            	//重新加载车号下拉数据
	                var trainNo_comb = Ext.getCmp("trainNo_comb");   
	                trainNo_comb.reset();  
	                Ext.getCmp("trainNo_comb").clearValue(); 
	                trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
	                trainNo_comb.cascadeStore();
	        	}   
	    	}
		},
		searchor : { xtype:'textfield' }

	},{
		header:'车型', dataIndex:'trainTypeName', hidden:search, editor:{ 			
			xtype:"hidden"
		}
	},{
		header:'车号', dataIndex:'trainNo', hidden:search,
		editor:{ 
			allowBlank:false ,
			id:"trainNo_comb_transfer",xtype: "TrainNo_combo",	fieldLabel: "车号",
			hiddenName: "trainNo", 
			displayField: "trainNo", valueField: "trainNo",
			pageSize: 20, minListWidth: 200, editable:true,
			returnField: [{widgetId:"holdOrgId",propertyName:"holdOrgId"},
			              {widgetId:"holdOrgSeq",propertyName:"holdOrgSeq"},
			              {widgetId:"holdOrgName",propertyName:"holdOrgName"}],
			width: TrainTransferDetail.fieldWidth
		}
	},{
		header:'使用别', dataIndex:'trainUse', hidden:true,
		editor:{
			id:"train_use_transfer",
			xtype: 'Base_combo',
			hiddenName: 'trainUse',
			entity:'com.yunda.jx.base.jcgy.entity.TrainUse',
			returnField:[{widgetId:"trainUseName",propertyName:"useName"}],
			fields: ["useID","useName"],displayField:'useName',valueField:'useID'
		}
	},{
		header:'使用别', dataIndex:'trainUseName',  hidden:search,
		editor:{
			xtype: 'hidden'			
		}
	},{
		header:'新造后走行公里', dataIndex:'achieveKM',  hidden:search, editor:{ xtype:'numberfield', 
		maxLength:6,
		validator:function(v){
			if(v < 0){
				return "不能为负数";
			}		
		}}
	},{
		header:'调拨类型', dataIndex:'transferType',  hidden:search, editor:{ 
			xtype: 'combo',	        	        
	        hiddenName:'transferType',
	        store:new Ext.data.SimpleStore({
			    fields: ['v', 't'],
				data : [[0,"调入"],[1,"调出"]]
			}),
			valueField:'v',
			displayField:'t',
			triggerAction:'all',
			value:0,
			mode:'local'
		},
		renderer:function(v){
			switch(v){
				case 0:
					return "调入";
				case 1:
					return "调出";
				default:
					return v;
			}
		},searcher:{disabled:true }
	},{
		header:'部命令号', dataIndex:'ministryOrder',width:150, editor:{  maxLength:20 }
	},{
		header:'局命令号', dataIndex:'bureauOrder', width:150,editor:{  maxLength:20 }
	},{
		header:'配属日期', dataIndex:'transferDate',width:150, xtype:'datecolumn', editor:{ xtype:'my97date', initNow:false},
		searcher:{ initNow:false }
	},{
		header:'原配属单位ID', dataIndex:'oldHoldOrgId', hidden:true, 
		editor:{ 
			id:"OldHoldOrg_Idx",
			fieldLabel:'原配属单位',
			xtype: 'OmOrganizationCustom_comboTree',
			hiddenName: 'oldHoldOrgId',	
			orgid:'0',
			orgname:orgRoot,
			returnField:[{widgetId:"oldHoldOrgName",propertyName:"orgname"},{widgetId:"oldHoldOrgSeq",propertyName:"orgseq"}]
		}
	},{
		header:'原配属单位部门序列', dataIndex:'oldHoldOrgSeq', hidden:true, editor:{  xtype:'hidden' }
	},{
		header:'原配属单位名称', dataIndex:'oldHoldOrgName', hidden:search,
		editor:{  			
			xtype: 'hidden'
		}
	},{
		header:'配属单位ID', dataIndex:'holdOrgId', hidden:true, 
		editor:{ 
			id:"HoldOrg_Idx",
			fieldLabel:'配属单位',
			xtype: 'OmOrganizationCustom_comboTree',
			hiddenName: 'holdOrgId',	
			orgid:'0',
			orgname:orgRoot,
			returnField:[{widgetId:"holdOrgName",propertyName:"orgname"},{widgetId:"holdOrgSeq",propertyName:"orgseq"}]
		}
	},{
		header:'配属单位部门序列', dataIndex:'holdOrgSeq', hidden:true, editor:{  xtype:'hidden' }
	},{
		header:'配属单位名称', dataIndex:'holdOrgName',width:200, 
		editor:{  			
			xtype: 'hidden'
		}
	},{
		header:'备注', dataIndex:'remarks',  hidden:search, editor:{ xtype:'textarea', maxLength:1000 }
	},{
		header:'申请人', dataIndex:'applyPerson', hidden:true,
		editor:{ 
			id:"apply_Person",
			xtype: 'OmEmployee_SelectWin',
			hiddenName: 'applyPerson',
			returnField:[{widgetId:"applyPersonName",propertyName:"empname"}],
			displayField:'empname',valueField:'empid', 
			editable:false
		}
	},{
		header:'申请人名称', dataIndex:'applyPersonName',  hidden:search, editor:{  xtype:"hidden" }
	},{
		header:'申请时间', dataIndex:'applyTime',  hidden:search, xtype:'datecolumn', editor:{ xtype:'my97date' ,initNow:false }
	}],
	searchFn: function(searchParam){ 
		TrainTransferDetail.searchParam = searchParam ;
		TrainTransferDetail.grid.store.load();
	},
	//处理编辑打开之后的事件
	afterShowEditWin: function(record, rowIndex){
		Ext.getCmp("OldHoldOrg_Idx").setDisplayValue(record.get("oldHoldOrgId"), record.get("oldHoldOrgName"));
		Ext.getCmp("HoldOrg_Idx").setDisplayValue(record.get("holdOrgId"), record.get("holdOrgName"));
		Ext.getCmp("apply_Person").setDisplayValue(record.get("applyPerson"), record.get("applyPersonName"));		
		//车型
		Ext.getCmp("trainType_comb_transfer").setDisplayValue(record.get("trainTypeIDX"),record.get("trainTypeName"));
		//车号
		Ext.getCmp("trainNo_comb_transfer").setDisplayValue(record.get("trainNo"),record.get("trainNo"));
		//使用别
		Ext.getCmp("train_use_transfer").setDisplayValue(record.get('trainUse'), record.get("trainUseName"));	
		
		TrainTransferDetail.grid.saveWin.setTitle('查看');
		TrainTransferDetail.grid.saveWin.buttons[0].setVisible(false);
		TrainTransferDetail.grid.disableAllColumns();		
	}
});
//查询前添加过滤条件
TrainTransferDetail.grid.store.on('beforeload' , function(){		
	var searchParam = TrainTransferDetail.searchParam;
	if(search){
		searchParam.trainTypeIDX = JczlTrain.trainTypeIdx;
		searchParam.trainNo = JczlTrain.trainNo;
		searchParam.transferType = 0;
		
	}
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
});