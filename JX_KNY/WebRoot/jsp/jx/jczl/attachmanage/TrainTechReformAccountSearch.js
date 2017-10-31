/**
 * 机车技术改造台账 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 * 引用此JS文件需在页面上定义变量search=false，履历引用此JS文件search=true
 */
Ext.onReady(function(){
Ext.namespace('TrainTechReformAccount');                       //定义命名空间
TrainTechReformAccount.searchParam = {};
TrainTechReformAccount.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainTechReformAccount!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainTechReformAccount!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainTechReformAccount!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:2, searchFormColNum:2,singleSelect:true,
    tbar:["search","refresh"],
    viewConfig: null,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'trainTypeIDX', dataIndex:'trainTypeIDX', width:0, hidden:search, 
		editor:{
			allowBlank:false ,
			id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "车型",
			hiddenName: "trainTypeIDX", 
			returnField: [{widgetId:"trainTypeShortName",propertyName:"shortName"}],
			displayField: "shortName", valueField: "typeID",
			pageSize: 0, minListWidth: 200, 
			width: TrainTechReformAccount.fieldWidth,
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
		}
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{ 	xtype:"hidden" } ,hidden:search
	},{
		header:'车号', dataIndex:'trainNo', hidden:search,
		editor:{ 
			allowBlank:false ,
			id:"trainNo_comb",xtype: "TrainNo_combo",	fieldLabel: "车号",
			hiddenName: "trainNo", 
			displayField: "trainNo", valueField: "trainNo",
			pageSize: 20, minListWidth: 200, editable:true,
			returnField: [{widgetId:"holdOrgId",propertyName:"holdOrgId"},
			              {widgetId:"holdOrgSeq",propertyName:"holdOrgSeq"},
			              {widgetId:"holdOrgName",propertyName:"holdOrgName"}],
			width: TrainTechReformAccount.fieldWidth
		}
	},{
		header:'改造开始日期', dataIndex:'reformStartTime', xtype:'datecolumn',
		editor:{ xtype:'my97date' },searcher:{ initNow:false }
	},{
		header:'改造结束日期', dataIndex:'reformEndTime', xtype:'datecolumn',
		editor:{ xtype:'my97date' }
	},{
		header:'技术改造名称', dataIndex:'techReformName',  editor:{  maxLength:200 }
	},{
//		header:'改造项目', dataIndex:'techReformItemName', width:150, editor:{  maxLength:200 }
		header:'项目负责人', dataIndex:'empName',hidden:true,  width:150, editor:{  maxLength:200 }
	},{
		header:'改造依据', dataIndex:'techReformReason', width:200, editor:{  maxLength:200 }
	},{
		header:'承改单位ID', dataIndex:'reformOrgId', hidden:true, 
		editor:{ 
			allowBlank:false,
			id: 'reformOrg_Id',
			fieldLabel:'承改单位',
			xtype: 'OmOrganizationCustom_comboTree',
			hiddenName: 'reformOrgId',	
			orgid:'0',
			orgname:orgRoot,
			returnField:[{widgetId:"reformOrgName",propertyName:"orgname"},{widgetId:"reformOrgSeq",propertyName:"orgseq"}]
		}
	},{
		header:'承改单位部门序列', dataIndex:'reformOrgSeq', hidden:true, editor:{ xtype:'hidden' }
	},{
		header:'承改单位名称', dataIndex:'reformOrgName', 
		editor:{  
			xtype: 'hidden'
			
		
		}
	},{
		header:'配属单位ID', dataIndex:'holdOrgId', hidden:true, 
		editor:{			
			xtype:'hidden'
		}
	},{
		header:'配属单位部门序列', dataIndex:'holdOrgSeq', hidden:true, 
		editor:{ 
			xtype:'hidden'
		}
	},{
		header:'配属单位名称', dataIndex:'holdOrgName', hidden:search, 
		editor:{ 
			allowBlank:false,
			xtype: 'textfield',
			readOnly:true
		},
		searcher:{disabled:true }
	},{
		header:'上报状态', dataIndex:'reportStatus', hidden:search, 
		editor:{
			id:"report_Status_tech",
			xtype: 'EosDictEntry_combo',
			hiddenName: 'reportStatus',
			dicttypeid:'JCJX_COMMON_DIC_REPORT_STATUS',			
			displayField:'dictname',valueField:'dictid'		
		},
		renderer:function(v){
			return EosDictEntry.getDictname('JCJX_COMMON_DIC_REPORT_STATUS',v);
		}
	}],
	searchFn: function(searchParam){ 
		TrainTechReformAccount.searchParam = searchParam ;
		TrainTechReformAccount.grid.store.load();
	},
	//处理编辑打开之后的事件
	afterShowEditWin: function(record, rowIndex){
		var rsName = EosDictEntry.getDictname('JCJX_COMMON_DIC_REPORT_STATUS',record.get('reportStatus'));		
		Ext.getCmp('report_Status_tech').setDisplayValue(record.get('reportStatus'),rsName);
		Ext.getCmp('reformOrg_Id').setDisplayValue(record.get('reformOrgId'),record.get('reformOrgName'));
		
		//车型
		Ext.getCmp("trainType_comb").setDisplayValue(record.get("trainTypeIDX"),record.get("trainTypeShortName"));
		//车号
		Ext.getCmp("trainNo_comb").setDisplayValue(record.get("trainNo"),record.get("trainNo"));
		
		
		TrainTechReformAccount.grid.saveWin.setTitle('查看');
		TrainTechReformAccount.grid.saveWin.buttons[0].setVisible(false);
		TrainTechReformAccount.grid.disableAllColumns();	
		
	}
});
if(search){
	//查询前添加过滤条件
	TrainTechReformAccount.grid.store.on('beforeload' , function(){		
		var searchParam = TrainTechReformAccount.searchParam;
		searchParam.trainTypeIDX = JczlTrain.trainTypeIdx;
		searchParam.trainNo = JczlTrain.trainNo;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
}
if(!search){//来自机车履历
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:TrainTechReformAccount.grid });
}
});