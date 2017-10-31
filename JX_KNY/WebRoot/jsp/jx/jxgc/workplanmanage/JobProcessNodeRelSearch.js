/**
 * 机车检修作业计划-前置节点 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('JobProcessNodeRelSearch');                       //定义命名空间
JobProcessNodeRelSearch.nodeIDX = "";
JobProcessNodeRelSearch.parentIDX = '';
JobProcessNodeRelSearch.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
JobProcessNodeRelSearch.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
JobProcessNodeRelSearch.store = new Ext.data.JsonStore({
	id: "idx", root: "root", totalProperty: "totalProperty", autoLoad: false,
    url: ctx + "/jobProcessNodeRel!getList.action",
    fields: [ "idx","preNodeIDX", "preNodeName", "seqClass","delayTime", "seqClassName"]
});

JobProcessNodeRelSearch.grid = new Ext.grid.EditorGridPanel({	
	border: false, enableColumnMove: true, stripeRows: true, 
    viewConfig: {forceFit: true}, selModel: JobProcessNodeRelSearch.sm,
    clicksToEdit: 1,
    store: JobProcessNodeRelSearch.store, loadMask: JobProcessNodeRelSearch.loadMask,
	hideRowNumberer: true,
	tbar:[{
    	text: "刷新", iconCls:"refreshIcon", handler: function(){
    		JobProcessNodeRelSearch.grid.store.load();
    	}
    }],
	colModel: new Ext.grid.ColumnModel([
//		JobProcessNodeRelSearch.sm,
		{ header:'idx', dataIndex:'idx',hidden: true},
		{ header:'前置节点名称', dataIndex:'preNodeName', disabled: true
		},
		{ header: "类型",  dataIndex: "seqClassName",width:150, hidden: true,disabled: true
	    },
		{ header: "延隔时间（分钟）",  dataIndex: "delayTime",width:200, disabled: true
    	  
    	},
		{	    	
	    	hidden: true, dataIndex: "preNodeIDX", editor:{ id: "preNodeIDX_Id", name: "preNodeIDX"}
	    },{
	    	hidden: true, dataIndex: "seqClass", editor:{ id: "seqClass_Id", name: "seqClass"}
	    }
	])
});
JobProcessNodeRelSearch.grid.store.on("beforeload", function() {
	this.baseParams.nodeIDX = JobProcessNodeRelSearch.nodeIDX;	
})
});