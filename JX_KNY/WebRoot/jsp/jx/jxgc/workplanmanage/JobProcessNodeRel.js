/**
 * 机车检修作业计划-前置节点 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('JobProcessNodeRel');                       //定义命名空间
JobProcessNodeRel.nodeIDX = "";
JobProcessNodeRel.parentIDX = '';
JobProcessNodeRel.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
JobProcessNodeRel.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
JobProcessNodeRel.store = new Ext.data.JsonStore({
	id: "idx", root: "root", totalProperty: "totalProperty", autoLoad: false,
    url: ctx + "/jobProcessNodeRel!getList.action",
    fields: [ "idx","preNodeIDX", "preNodeName", "seqClass","delayTime", "seqClassName"]
});

JobProcessNodeRel.deleteFn = function(v) {
	var ids = [];
	ids.push(v);
	JobProcessNodeRel.deleteOpFn(ids);
}
JobProcessNodeRel.deleteOpFn = function(ids) {
	var cfg = {
        scope: this, url: ctx + '/jobProcessNodeRel!updateForDelete.action',
        params: {ids: ids},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {       //操作成功     
	            alertSuccess();
	            JobProcessNodeRel.grid.store.reload(); 
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
JobProcessNodeRel.clearFn = function(rowIndex) {
	var record = JobProcessNodeRel.grid.getStore().getAt(rowIndex);
	record.data.preNodeName = "";
	record.data.seqClassName = "";
	record.data.delayTime = "";
	record.data.preNodeIDX = "";
	record.data.seqClass = "";
	JobProcessNodeRel.grid.getView().refreshRow(record);
}
JobProcessNodeRel.grid = new Ext.grid.EditorGridPanel({	
	border: false, enableColumnMove: true, stripeRows: true, 
    viewConfig: {forceFit: true}, selModel: JobProcessNodeRel.sm,
    clicksToEdit: 1,
    store: JobProcessNodeRel.store, loadMask: JobProcessNodeRel.loadMask,
	hideRowNumberer: true,
	tbar:[{
    	text: "刷新", iconCls:"refreshIcon", handler: function(){
    		JobProcessNodeRel.grid.store.load();
    	}
    }],
	colModel: new Ext.grid.ColumnModel([
//		JobProcessNodeRel.sm,
		{ header:'idx', dataIndex:'idx',hidden: true},
		{ header:'前置节点名称', dataIndex:'preNodeName',width:200,
		  editor: {
		  	//同一父节点下的节点选择控件
		  	  id: 'preNodeName_ComboxIDX',
			  xtype: 'Base_combo',hiddenName: 'preNodeName', 
			  entity:'com.yunda.jx.jxgc.workplanmanage.entity.JobProcessNode', business: 'jobProcessNode', allowBlank: false,
			  fields: ["idx","nodeName"],displayField:'nodeName',valueField:'nodeName',
			  returnField:[{widgetId:"preNodeIDX_Id",propertyName:"idx"}]
		  }
		},
//		{ header: "类型",  dataIndex: "seqClassName",width:150,
//		  editor: {
//		  		xtype: 'EosDictEntry_combo', 
//				hiddenName: 'seqClass', 
//				dicttypeid: 'PJJX_WP_NODE_SEQ_TYPE',
//				allowBlank:false,
//				displayField:'dictname',valueField:'dictname',
//				returnField:[{widgetId:"seqClass_Id",propertyName:"dictid"}]
//		  	
//		  }
//	    },
		{ header: "操作",  dataIndex: "idx",width:50,
		  renderer:function(value, metaData, record, rowIndex, colIndex, store){
		  	if (Ext.isEmpty(value)&& (Ext.isEmpty(record.get('delayTime'))|| 0 == record.get('delayTime')) && Ext.isEmpty(record.get('preNodeName')))
		  		return "";
			else if (!Ext.isEmpty(value)){
				return "<img src='" + ctx + "/frame/resources/images/toolbar/delete1.gif' alt='删除' style='cursor:pointer' onclick='JobProcessNodeRel.deleteFn(\"" + value + "\")'/>";
			}else return  "<img src='" + ctx + "/frame/resources/images/toolbar/delete.gif' alt='清空' style='cursor:pointer'  onclick='JobProcessNodeRel.clearFn(\"" + rowIndex + "\")'/>";
		  }
	    },{	    	
	    	hidden: true, dataIndex: "preNodeIDX", editor:{ id: "preNodeIDX_Id", name: "preNodeIDX"}
	    },{
	    	hidden: true, dataIndex: "seqClass", editor:{ id: "seqClass_Id", name: "seqClass"}
	    }
	]),
	listeners: {
			cellclick:function(grid, rowIndex, columnIndex, e) {
			    var record = grid.getStore().getAt(rowIndex);  // 返回当前行Record对象
			    if (columnIndex == 1) {
				    var preNodeName_Combox =  Ext.getCmp("preNodeName_ComboxIDX");
//				    var queryHql = "from JobProcessNode where recordStatus = 0 and workPlanIDX = '" + workPlanIDX + "'";
//				    if (Ext.isEmpty(JobProcessNodeRel.parentIDX))
//				    	queryHql += " and parentIDX is null ";
//				    else
//				    	queryHql += " and parentIDX = '" + JobProcessNodeRel.parentIDX + "' ";
//				    if (!Ext.isEmpty(JobProcessNodeRel.nodeIDX)) {
//				    	queryHql += " and idx != '" + JobProcessNodeRel.nodeIDX + "' ";
//				    	queryHql += " and idx not in (select nodeIDX from JobProcessNodeRel where preNodeIDX = '" + JobProcessNodeRel.nodeIDX + "' and recordStatus = 0)";
//				    	queryHql += " and idx not in (select preNodeIDX from JobProcessNodeRel where nodeIDX = '" + JobProcessNodeRel.nodeIDX + "' and recordStatus = 0)";
//				    }
				    var queryParams = {"workPlanIDX": workPlanIDX, "parentIDX": JobProcessNodeRel.parentIDX, "nodeIDX": JobProcessNodeRel.nodeIDX};
				    preNodeName_Combox.queryParams = queryParams;
				    preNodeName_Combox.store.proxy = new Ext.data.HttpProxy( {   
			            url : ctx + '/baseCombo!pageList.action?queryParams='+Ext.util.JSON.encode(preNodeName_Combox.queryParams) + '&manager='+preNodeName_Combox.business  
			        });
			        preNodeName_Combox.store.load();
			    }
			},
			afteredit:function(e){
				var preNodeIDX = Ext.getCmp("preNodeIDX_Id").getValue();
				if (preNodeIDX != "undefined" && !Ext.isEmpty(preNodeIDX))
					e.record.data.preNodeIDX = preNodeIDX;
				var seqClass = Ext.getCmp("seqClass_Id").getValue();
				if (seqClass != "undefined" && !Ext.isEmpty(seqClass))
					e.record.data.seqClass = seqClass;
			}
		}
});
JobProcessNodeRel.grid.store.on("beforeload", function() {
	this.baseParams.nodeIDX = JobProcessNodeRel.nodeIDX;	
})
});