/**
 * 机车检修作业计划-前置节点 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('JobProcessNodeRelEditWin');                       //定义命名空间
JobProcessNodeRelEditWin.nodeIDX = "";
JobProcessNodeRelEditWin.parentIDX = '';
JobProcessNodeRelEditWin.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
JobProcessNodeRelEditWin.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
JobProcessNodeRelEditWin.store = new Ext.data.JsonStore({
	id: "idx", root: "root", totalProperty: "totalProperty", autoLoad: false,
    url: ctx + "/jobProcessNodeRel!getList.action",
    fields: [ "idx","preNodeIDX", "preNodeName", "seqClass","delayTime", "seqClassName"]
});

JobProcessNodeRelEditWin.deleteFn = function(v) {
	var ids = [];
	ids.push(v);
	JobProcessNodeRelEditWin.deleteOpFn(ids);
}
JobProcessNodeRelEditWin.deleteOpFn = function(ids) {
	var cfg = {
        scope: this, url: ctx + '/jobProcessNodeRel!updateForDelete.action',
        params: {ids: ids},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {       //操作成功     
	            alertSuccess();
	            JobProcessNodeRelEditWin.grid.store.reload(); 
	        } else {                           //操作失败
	            alertFail(result.errMsg);
	        }
        }
    };
    Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
        if(btn != 'yes')    return;
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    });
}
JobProcessNodeRelEditWin.clearFn = function(rowIndex) {
	var record = JobProcessNodeRelEditWin.grid.getStore().getAt(rowIndex);
	record.data.preNodeName = "";
	record.data.seqClassName = "";
	record.data.delayTime = "";
	record.data.preNodeIDX = "";
	record.data.seqClass = "";
	JobProcessNodeRelEditWin.grid.getView().refreshRow(record);
}
JobProcessNodeRelEditWin.grid = new Ext.grid.EditorGridPanel({	
	border: false, enableColumnMove: true, stripeRows: true, 
    viewConfig: {forceFit: true}, selModel: JobProcessNodeRelEditWin.sm,
    clicksToEdit: 1,
    store: JobProcessNodeRelEditWin.store, loadMask: JobProcessNodeRelEditWin.loadMask,
	hideRowNumberer: true,
	tbar:[{
    	text: "刷新", iconCls:"refreshIcon", handler: function(){
    		JobProcessNodeRelEditWin.grid.store.load();
    	}
    }],
	colModel: new Ext.grid.ColumnModel([
		{ header:'idx', dataIndex:'idx',hidden: true},
		{ header:'前置节点名称', dataIndex:'preNodeName',
		  editor: {
		  	//同一父节点下的节点选择控件
		  	  id: 'preNodeName_ComboxIDX1',
			  xtype: 'Base_combo',hiddenName: 'preNodeName', 
			  entity:'com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode', business: 'jobProcessNode', allowBlank: false,
			  fields: ["idx","nodeName"],displayField:'nodeName',valueField:'nodeName',
			  returnField:[{widgetId:"preNodeIDX_Id1",propertyName:"idx"}]
		  }
		},{ header: "类型",  dataIndex: "seqClassName",width:150,hidden: true,
		  editor: {
		  		xtype: 'EosDictEntry_combo', 
				hiddenName: 'seqClass', 
				dicttypeid: 'PJJX_WP_NODE_SEQ_TYPE',
				allowBlank:false,
				displayField:'dictname',valueField:'dictname',
				returnField:[{widgetId:"seqClass_Id1",propertyName:"dictid"}]
		  	
		  }
	    },
		{ header: "操作",  dataIndex: "idx",width:50,
		  renderer:function(value, metaData, record, rowIndex, colIndex, store){		
		  	if (Ext.isEmpty(value))
		  		return "<img src='" + ctx + "/frame/resources/images/toolbar/delete.gif' alt='清空' style='cursor:pointer'  onclick='JobProcessNodeRelEditWin.clearFn(\"" + rowIndex + "\")'/>";
			return "<img src='" + ctx + "/frame/resources/images/toolbar/delete1.gif' alt='删除' style='cursor:pointer' onclick='JobProcessNodeRelEditWin.deleteFn(\"" + value + "\")'/>";
		  }
	    },{	    	
	    	hidden: true, dataIndex: "preNodeIDX", editor:{ id: "preNodeIDX_Id1", name: "preNodeIDX"}
	    },{
	    	hidden: true, dataIndex: "seqClass", editor:{ id: "seqClass_Id1", name: "seqClass"}
	    }
	]),
	listeners: {
			cellclick:function(grid, rowIndex, columnIndex, e) {
			    var record = grid.getStore().getAt(rowIndex);  // 返回当前行Record对象
			    if (columnIndex == 1) {
				    var preNodeName_Combox =  Ext.getCmp("preNodeName_ComboxIDX1");
				    var queryParams = {"workPlanIDX": workPlanIDX, "parentIDX": JobProcessNodeRelEditWin.parentIDX, "nodeIDX": JobProcessNodeRelEditWin.nodeIDX};
				    preNodeName_Combox.queryParams = queryParams;
				    preNodeName_Combox.store.proxy = new Ext.data.HttpProxy( {   
			            url : ctx + '/baseCombo!pageList.action?queryParams='+Ext.util.JSON.encode(preNodeName_Combox.queryParams) + '&manager='+preNodeName_Combox.business  
			        });
			        preNodeName_Combox.store.load();
			    }
			},
			afteredit:function(e){
				var preNodeIDX = Ext.getCmp("preNodeIDX_Id1").getValue();
				if (preNodeIDX != "undefined" && !Ext.isEmpty(preNodeIDX))
					e.record.data.preNodeIDX = preNodeIDX;
				var seqClass = Ext.getCmp("seqClass_Id1").getValue();
				if (seqClass != "undefined" && !Ext.isEmpty(seqClass))
					e.record.data.seqClass = seqClass;
			}
		}
});
JobProcessNodeRelEditWin.grid.store.on("beforeload", function() {
	this.baseParams.nodeIDX = JobProcessNodeRelEditWin.nodeIDX;	
})
});