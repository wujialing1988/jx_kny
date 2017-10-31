/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('ZbglRdpPlanRecordCL');
	
	ZbglRdpPlanRecordCL.rdpPlanIdx ;  // 列检计划ID
	
	ZbglRdpPlanRecordCL.statusArrays = {"TODO":"未处理","COMPLETE":"已完成"} ;
	ZbglRdpPlanRecordCL.statusColorArrays = {"TODO":"#999999","COMPLETE":"#00BFFF"} ;
	
	
	ZbglRdpPlanRecordCL.store = new Ext.data.GroupingStore({
		proxy:new Ext.data.HttpProxy({
			url:ctx + "/zbglRdpPlan!findKCLJLIst.action"
		}),
		reader:new Ext.data.JsonReader({
			root:'root',
			id:'idx',
			fields:[
			   {name:'idx'},
		       {name: 'wiIdx'},
		       {name: 'wiName'},
		       {name: 'wiStatus'},
		       {name: 'handlePersonId'},
		       {name: 'handlePersonName'},
		       {name: 'handleTime'},
		       {name: 'recordId'},
		       {name: 'trainTypeIdx'},
		       {name: 'trainTypeCode'},
		       {name: 'trainTypeName'},
		       {name: 'trainNo'},
		       {name: 'seqNum'},
		       {name: 'trainTypeNo'}
			]
		}),
		sortInfo:{field: 'wiStatus', direction: "DESC"},
		groupField:'trainTypeNo'
	});
	
    
    ZbglRdpPlanRecordCL.grid = new Ext.grid.GridPanel({
        store: ZbglRdpPlanRecordCL.store,
        loadMask:true,
        columns: [
            {header: "车型车号", width: 20, sortable: true,  dataIndex: 'trainTypeNo'},
			{header: "专业分类", width: 20, sortable: true, dataIndex: 'wiName'},
            {header: "处理人", width: 20, sortable: true,  dataIndex: 'handlePersonName'},
            {header: "处理时间", width: 20, sortable: true,dataIndex: 'handleTime'},
            {header: "状态", width: 20, sortable: true,  dataIndex: 'wiStatus',renderer: function(value, metaData, record, rowIndex, colIndex, store) {
            		return '<div style="background:'+ ZbglRdpPlanRecordCL.statusColorArrays[value] +';color:white;width:48px;height:18px;line-height:18px;text-align:center;border-radius:8px;margin-left:10px;">' + ZbglRdpPlanRecordCL.statusArrays[value] + '</div>';
            	}
            }
        ],
        view: new Ext.grid.GroupingView({
            forceFit:true,
            enableNoGroups:false,
            groupTextTpl: '{group} (共{[values.rs.length]}项)'
        }),
        iconCls: 'icon-grid',
        frame:true,
        width: 700,
        tbar  : ['->',{
        	iconCls : 'icon-expand-all',
            text:'全部展开',
            handler : function(){
            	ZbglRdpPlanRecordCL.grid.getView().expandAllGroups();
            }
        	} ,{
        	iconCls : 'icon-collapse-all',
            text:'全部收缩',
            handler : function(){
                ZbglRdpPlanRecordCL.grid.getView().collapseAllGroups();
            }
        }]
    });
    
    
    ZbglRdpPlanRecordCL.grid.getStore().addListener('beforeload',function(me, options){
		this.baseParams.rdpPlanIdx = ZbglRdpPlanRecordCL.rdpPlanIdx;
    });
    
    ZbglRdpPlanRecordCL.grid.getStore().addListener('load',function(me, options){
    });

});