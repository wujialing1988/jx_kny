/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('ZbglRdpPlanRecord');
	
	ZbglRdpPlanRecord.rdpPlanIdx ;  // 列检计划ID
	
	ZbglRdpPlanRecord.statusArrays = {"TODO":"未处理","COMPLETE":"已完成"} ;
	ZbglRdpPlanRecord.statusColorArrays = {"TODO":"#999999","COMPLETE":"#00BFFF"} ;
	
	
	ZbglRdpPlanRecord.store = new Ext.data.GroupingStore({
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
		       {name: 'seqNum'}
			]
		}),
		sortInfo:{field: 'wiStatus', direction: "DESC"},
		groupField:'wiName'
	});
	
    
    ZbglRdpPlanRecord.grid = new Ext.grid.GridPanel({
        store: ZbglRdpPlanRecord.store,
        loadMask:true,
        columns: [
            {id:'wiName',header: i18n.PassengerInspectionPlan.professionType, width: 20, sortable: true, dataIndex: 'wiName'},
            {header: i18n.PassengerInspectionPlan.trainType, width: 20, sortable: true,  dataIndex: 'trainTypeCode'},
            {header: i18n.PassengerInspectionPlan.trainNum, width: 20, sortable: true,  dataIndex: 'trainNo'},
            {header: i18n.PassengerInspectionPlan.handlePerson, width: 20, sortable: true,  dataIndex: 'handlePersonName'},
            {header: i18n.PassengerInspectionPlan.handleTime, width: 20, sortable: true,dataIndex: 'handleTime'},
            {header: i18n.PassengerInspectionPlan.status, width: 20, sortable: true,  dataIndex: 'wiStatus',renderer: function(value, metaData, record, rowIndex, colIndex, store) {
            		return '<div style="background:'+ ZbglRdpPlanRecord.statusColorArrays[value] +';color:white;width:48px;height:18px;line-height:18px;text-align:center;border-radius:8px;margin-left:10px;">' + ZbglRdpPlanRecord.statusArrays[value] + '</div>';
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
            text:i18n.PassengerInspectionPlan.expand,
            handler : function(){
            	ZbglRdpPlanRecord.grid.getView().expandAllGroups();
            }
        	} ,{
        	iconCls : 'icon-collapse-all',
            text:i18n.PassengerInspectionPlan.collapse,
            handler : function(){
                ZbglRdpPlanRecord.grid.getView().collapseAllGroups();
            }
        }]
    });
    
    
    ZbglRdpPlanRecord.grid.getStore().addListener('beforeload',function(me, options){
		this.baseParams.rdpPlanIdx = ZbglRdpPlanRecord.rdpPlanIdx;
    });
    
    ZbglRdpPlanRecord.grid.getStore().addListener('load',function(me, options){
    });

});