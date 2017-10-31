/**
 * 机车调拨明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainTransferDetail');                       //定义命名空间
TrainTransferDetail.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainTransferDetail!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainTransferDetail!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainTransferDetail!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:2,  searchFormColNum:2,    
    viewConfig: null,    
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型名称', dataIndex:'trainTypeName', editor:{  maxLength:20 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},{
		header:'使用别', dataIndex:'trainUse', 
		editor:{
			xtype: 'Base_combo',
			hiddenName: 'trainUse',
			entity:'com.yunda.jx.base.jcgy.entity.TrainUse',			
			fields: ["useID","useName"],displayField:'useName',valueField:'useID'
		}
	},{
		header:'新造后走行公里', dataIndex:'achieveKM', editor:{ xtype:'numberfield', 
		maxLength:6,
		validator:function(v){
			if(v < 0){
				return "不能为负数";
			}		
		}}
	},{
		header:'调拨类型', dataIndex:'transferType', editor:{ 
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
		header:'部命令号', dataIndex:'ministryOrder', editor:{  maxLength:20 }
	},{
		header:'局命令号', dataIndex:'bureauOrder', editor:{  maxLength:20 }
	},{
		header:'调拨日期', dataIndex:'transferDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'原配属单位ID', dataIndex:'oldHoldOrgId', hidden:true, 
		editor:{ 
			id:"OldHoldOrg_Idx",
			fieldLabel:'原配属单位',
			xtype: 'OmOrganizationCustom_comboTree',
			hiddenName: 'oldHoldOrgId',	
			orgid:'0',
			orgname:orgRootName,
			returnField:[{widgetId:"oldHoldOrgName",propertyName:"orgname"},{widgetId:"oldHoldOrgSeq",propertyName:"orgseq"}]
		}
	},{
		header:'原配属单位部门序列', dataIndex:'oldHoldOrgSeq', hidden:true, editor:{  xtype:'hidden' }
	},{
		header:'原配属单位名称', dataIndex:'oldHoldOrgName', 
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
			orgname:orgRootName,
			returnField:[{widgetId:"holdOrgName",propertyName:"orgname"},{widgetId:"holdOrgSeq",propertyName:"orgseq"}]
		}
	},{
		header:'配属单位部门序列', dataIndex:'holdOrgSeq', hidden:true, editor:{  xtype:'hidden' }
	},{
		header:'配属单位名称', dataIndex:'holdOrgName', 
		editor:{  			
			xtype: 'hidden'
		}
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 }
	},{
		header:'申请人', dataIndex:'applyPerson', 
		editor:{ 
			id:"apply_Person",
			xtype: 'OmEmployee_SelectWin',
			hiddenName: 'applyPerson',
			returnField:[{widgetId:"applyPersonName",propertyName:"empname"}],
			displayField:'empname',valueField:'empid', 
			editable:false
		}
	},{
		header:'申请人名称', dataIndex:'applyPersonName', hidden:true, editor:{  xtype:"hidden" }
	},{
		header:'申请时间', dataIndex:'applyTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	}],
	//处理编辑打开之后的事件
	afterShowEditWin: function(record, rowIndex){
		Ext.getCmp("OldHoldOrg_Idx").setDisplayValue(record.get("oldHoldOrgId"), record.get("oldHoldOrgName"));
		Ext.getCmp("HoldOrg_Idx").setDisplayValue(record.get("holdOrgId"), record.get("holdOrgName"));
		Ext.getCmp("apply_Person").setDisplayValue(record.get("applyPerson"), record.get("applyPersonName"));
		
		if(search){
			TrainTransferDetail.grid.saveWin.setTitle('查看');
			TrainTransferDetail.grid.saveWin.buttons[0].setVisible(false);
			TrainTransferDetail.grid.disableAllColumns();
		}		
	},
	//处理新增打开之后的事件
	afterShowSaveWin: function(){
		Ext.getCmp("OldHoldOrg_Idx").clearValue();
		Ext.getCmp("HoldOrg_Idx").clearValue();
		Ext.getCmp("apply_Person").clearValue();
	}
});
//页面自适应布局
});