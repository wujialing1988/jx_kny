/**
 * 机车检修作业编辑超链接中的下级节点grid
 */
Ext.onReady(function(){
Ext.namespace('ChildNodeListGrid'); 	
ChildNodeListGrid.searchParam = {}
ChildNodeListGrid.nodeIDX = '';
ChildNodeListGrid.ids = [];
ChildNodeListGrid.workPlanStatus = '';
//初始化状态和对应的中文名
ChildNodeListGrid.stateName = {"NOTSTARTED":"未启动","RUNNING":"运行","COMPLETED":"完成","TERMINATED":"终止"};

var processTips;

/*
 * 显示处理遮罩
 */
function showtip(msg){
	processTips = new Ext.LoadMask(TrainWorkPlanEditWin.win.getEl()||Ext.getBody(),{
		msg: msg || "正在处理你的操作，请稍后...",
		removeMask:true
	});
	processTips.show();
}

/*
 * 隐藏处理遮罩
 */
function hidetip(){
	processTips.hide();
}

ChildNodeListGrid.deleteBatchFn = function() {
	var grid = ChildNodeListGrid.grid;
	if(!$yd.isSelectedRecord(grid)) return;
	var ids = $yd.getSelectedIdx(grid);
	
	ChildNodeListGrid.deleteOpFn(ids);
}

ChildNodeListGrid.deleteFn = function(v) {
	var ids = [];
	ids.push(v);
	ChildNodeListGrid.deleteOpFn(ids);
}
ChildNodeListGrid.deleteOpFn = function() {
	var grid = ChildNodeListGrid.grid;
	var ids = $yd.getSelectedIdx(grid);	
	//不能选择多条数据启动
	if(ids.length > 1){
		MyExt.Msg.alert("不能同时启动多个节点，请选择一条任务！");
		return;
	}

	var idValue = ids;
	var record = ChildNodeListGrid.grid.store.getById(idValue);
	var status = record.get("status");
	
	if(status == PROCESSTYPE){
		MyExt.Msg.alert("请选择非流程任务的节点！");
		return;			
	}
	switch(status){
		case NODE_STATUS_GOING:
			 MyExt.Msg.alert("不能删除【运行中】的作业工单！");
			 return;
			 break;
		case NODE_STATUS_COMPLETE:
			 MyExt.Msg.alert("不能删除【已完工】的作业工单！");
			 return;
			 break;
	    case NODE_STATUS_STOP:
			 MyExt.Msg.alert("不能删除【已终止】的作业工单！");
			 return;
			 break;
		default:							 					 
			 break;
	}	
				
	var cfg = {
        url: ctx + "/jobProcessNode!deleteNode.action", 
		params: {id: idValue},
        timeout: 600000,
        success: function(response, options){
        	if(processTips) hidetip();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null && result.success == true) {
                alertSuccess();
                ChildNodeListGrid.grid.store.load();
                TrainWorkPlanEditWin.tree.root.reload();
				TrainWorkPlanEditWin.tree.getRootNode().expand();
            } else {
                alertFail(result.errMsg);
            }
        },
        failure: function(response, options){
        	if(processTips) hidetip();
	        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	    }
    };
    Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
        if(btn != 'yes')    return;
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    });
}

ChildNodeListGrid.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/jobProcessNodeQuery!getChildNodeList.action',                 
    page : false,
    isEdit : false,
    singleSelect: true,
    storeAutoLoad:true,
    tbar: [{
    	text: "启动节点任务" ,iconCls:"beginIcon", handler: function(){
    		ChildNodeListGrid.startNode();
    	}
    },{
    	text: "删除",iconCls:"deleteIcon", handler: function(){
    		ChildNodeListGrid.deleteBatchFn();
    	}
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true
	},{
		header:'名称', dataIndex:'nodeName',width: 150
	},{
		header:'工期', dataIndex:'ratedWorkMinutes'
	},{
		header:'前置节点', dataIndex:'lastChildNodeNames'
	},{
		header:'开始时间', dataIndex:'planBeginTime', xtype:'datecolumn', format: "Y-m-d"
	},{
		header:'完成时间', dataIndex:'planEndTime', xtype:'datecolumn', format: "Y-m-d"
	},{
		header:'作业工位', dataIndex:'workStationName'
	},{
		header:'作业班组', dataIndex:'workStationBelongTeamName'
	},{
		header:'节点状态', dataIndex:'status',
		renderer :function(a,b,c,d){
			return ChildNodeListGrid.stateName[a];
		}
	}],
	//不需要双击显示
	toEditFn : function(){}
});
ChildNodeListGrid.grid.store.on('beforeload', function() {
	this.baseParams.nodeIDX = ChildNodeListGrid.nodeIDX;
});


ChildNodeListGrid.startNode = function() {
	
	//判断是否选择了数据
	var grid = ChildNodeListGrid.grid;
	if(!$yd.isSelectedRecord(grid)) {
		MyExt.Msg.alert("请选择一条任务！");
		return;
	}
	var ids = $yd.getSelectedIdx(grid);	
	
	//不能选择多条数据启动
	if(ids.length > 1){
		MyExt.Msg.alert("不能同时启动多个节点，请选择一条任务！");
		return;
	}
	
	var record = ChildNodeListGrid.grid.store.getById(ids[0]);
	var idx = record.get("idx");
	
	if(ChildNodeListGrid.workPlanStatus != PLAN_STATUS_HANDLING){
		MyExt.Msg.alert("请选择【已启动生产】状态的节点！");
		return;
	}
	
	var status = record.get("status");
	if(status != NODE_STATUS_UNSTART){
		MyExt.Msg.alert("请选择【未启动】状态的节点！");
		return;			
	}
	
	var cfg = {
        url: ctx + "/jobProcessNode!startNode.action", 
		params: {id: idx},
        timeout: 600000,
        success: function(response, options){
        	if(processTips) hidetip();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null && result.success == true) {
                alertSuccess();
                ChildNodeListGrid.grid.store.load();
            } else {
                alertFail(result.errMsg);
            }
        },
        failure: function(response, options){
        	if(processTips) hidetip();
	        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	    }
    };
    Ext.Msg.confirm("提示  ", "此操作不能恢复，确定启动此节点任务？  ", function(btn){
        if(btn != 'yes')    return;
        showtip();
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    });
}

});