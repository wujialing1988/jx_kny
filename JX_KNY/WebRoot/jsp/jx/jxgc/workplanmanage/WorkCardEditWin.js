/**
 * 机车检修作业计划-作业工单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('WorkCardEditWin'); 	
WorkCardEditWin.searchParam = {}
WorkCardEditWin.nodeCaseIDX = '';
WorkCardEditWin.ids = [];
RepairProjectSelect.pTrainTypeIdx = '';
WorkCardEditWin.qcWin = null;

WorkCardEditWin.deleteBatchFn = function() {
	var grid = WorkCardEditWin.grid;
	if(!$yd.isSelectedRecord(grid)) return;
	var ids = $yd.getSelectedIdx(grid);
	
	WorkCardEditWin.deleteOpFn(ids);
}

WorkCardEditWin.deleteFn = function(v) {
	var ids = [];
	ids.push(v);
	WorkCardEditWin.deleteOpFn(ids);
}
WorkCardEditWin.deleteOpFn = function(ids) {
	for (var i = 0; i < ids.length; i++) {
		var record = WorkCardEditWin.grid.store.getById(ids[i]);
		var status = record.get("status");
		switch(status){
			case WORKCARD_STATUS_HANDLING:
				 MyExt.Msg.alert("不能删除【处理中】的作业工单！");
				 return;
				 break;
			case WORKCARD_STATUS_HANDLED:
				 MyExt.Msg.alert("不能删除【已处理】的作业工单！");
				 return;
				 break;
			case WORKCARD_STATUS_FINISHED:
				 MyExt.Msg.alert("不能删除【质检中】的作业工单！");
				 return;
				 break;
		    case WORKCARD_STATUS_TERMINATED:
				 MyExt.Msg.alert("不能删除【已终止】的作业工单！");
				 return;
				 break;
			default:							 					 
				 break;
		}				
	}
	var cfg = {
        scope: this, url: ctx + '/workCard!deleteByWorkPlan.action',
        params: {ids: ids + ""},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {       //操作成功     
	            alertSuccess();
	            WorkCardEditWin.grid.store.reload(); 
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
WorkCardEditWin.fillData = function(workCardEntity) {
	var r = new Ext.data.Record();
	for(var i in workCardEntity){
		r.set(i, workCardEntity[i]);			
	}
	TrainWork.showEditWin(r);
	TrainWork.idx = workCardEntity.idx;
}

WorkCardEditWin.loadWorkRecord = function(idx) {
	var cfg = {
        scope: this, url: ctx + '/workCard!getEntityByIDX.action',
        params: {id: idx},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.entity != null) {		            	
                 var workCardEntity = result.entity;
                 WorkCardEditWin.fillData(workCardEntity);
                 WorkStep.grid.store.load();
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
}

JobProcessNodeTreeWin.returnFn = function(node,thisObj){
	var cfg = {
        url: ctx + '/workCard!changeNode.action',
        params: {ids: thisObj.ids, id: node.id, oldNodeIDX: thisObj.nodeCaseIDX},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {       //操作成功     
	            alertSuccess();
	            thisObj.grid.store.reload(); 
	            JobProcessNodeTreeWin.win.hide();
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

WorkCardEditWin.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/workCardQuery!getWorkCardListByNode.action',                 
    deleteURL: ctx + '/workCardQuery!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:1,	searchFormColNum:1,
    storeAutoLoad: false, 
    tbar: [{
        xtype:"label", text:"  作业名称： " 
    },{
        xtype: "textfield" ,maxLength: 100,vtype:"validChar"
    },{
    	text: "查询", iconCls:"searchIcon", handler: function(){
    		var grid = WorkCardEditWin.grid;
    		var searchParam = WorkCardEditWin.searchParam;
    		var workCardName = grid.getTopToolbar().get(1).getValue();
    		searchParam.workCardName = workCardName;
    		grid.store.load();
    	}
    },'-',/*{
    	text: "选择作业项目", iconCls:"addIcon", handler: function(){
    		RepairProjectSelect.grid.store.load();
    		RepairProjectSelect.win.show();
    	}
    },*/{
    	text: "新增自定义工单", iconCls:"addIcon", handler: function(){
    		TrainWork.showWin();
    	}
    },{
    	text: "调整所属节点", iconCls:"editIcon", handler: function(){
    		var grid = WorkCardEditWin.grid;
			if(!$yd.isSelectedRecord(grid)) return;
			WorkCardEditWin.ids = $yd.getSelectedIdx(grid);
			JobProcessNodeTreeWin.thisObj = WorkCardEditWin;
    		JobProcessNodeTreeWin.win.show();
    		JobProcessNodeTreeWin.tree.root.reload();
    	}
    },{
    	text: "删除", iconCls:"deleteIcon", handler: function(){
    		WorkCardEditWin.deleteBatchFn();
    	}
    },{
    	text: "刷新", iconCls:"refreshIcon", handler: function(){
    		WorkCardEditWin.grid.store.load();
    	}
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'作业编码', dataIndex:'workCardCode', editor: { }
	},{
		header:'作业名称', dataIndex:'workCardName', width:200, editor: { }
	},{
		header:'状态', dataIndex:'status',
        renderer: function(v){
            switch(v){
            	case WORKCARD_STATUS_NEW:
                    return '初始化';
                case WORKCARD_STATUS_OPEN:
                    return '已开放';
                case WORKCARD_STATUS_HANDLING:
                    return '处理中';
                case WORKCARD_STATUS_HANDLED:
                    return '已处理';
                case WORKCARD_STATUS_FINISHED:
                    return '质检中';
				case WORKCARD_STATUS_TERMINATED:
                    return '终止';                    
                default:
                    return v;
            }
        }
	},{
		header:'质量检查', dataIndex:'qcName', 
		renderer :function(a,b,c,d){
			if(a){
				return "<a href='#' onclick='QCResult.showWin(\""+ c.data.idx + "\")'>" + a + "</a>";
			}
			return a;
		}
	},{
		header:'工单类型', dataIndex:'extensionClass',
        renderer: function(v){
            switch(v){
            	case workDefineType:
                    return '自定义工单';               
                default:
                    return '范围工单';
            }
        }
	},{
		header:'作业人员', dataIndex:'worker', editor: { }
	},{
		header:'操作', dataIndex:'idx',
	    renderer:function(v,x,r){		
	  		if (Ext.isEmpty(v))
	  			return "<img src='" + ctx + "/frame/resources/images/toolbar/delete2.gif' alt='不能删除' style='cursor:pointer' />";
			return "<img src='" + ctx + "/frame/resources/images/toolbar/delete1.gif' alt='删除' style='cursor:pointer' onclick='WorkCardEditWin.deleteFn(\"" + v + "\")'/>";
	    }
	}],
	toEditFn: function(grid, rowIndex, e){
		var record = grid.store.getAt(rowIndex);
		if(record.get("extensionClass") != workDefineType){
			MyExt.Msg.alert("范围工单不能被编辑");
			return;
		}		
		var status = record.get("status");
		switch(status){
//			case WORKCARD_STATUS_HANDLING:
//				 MyExt.Msg.alert("不能编辑【处理中】的作业工单！");
//				 return;
//				 break;
			case WORKCARD_STATUS_HANDLED:
				 MyExt.Msg.alert("不能编辑【已处理】的作业工单！");
				 return;
				 break;
			case WORKCARD_STATUS_FINISHED:
				 MyExt.Msg.alert("不能编辑【质检中】的作业工单！");
				 return;
				 break;
		    case WORKCARD_STATUS_TERMINATED:
				 MyExt.Msg.alert("不能编辑【已终止】的作业工单！");
				 return;
				 break;
			default:							 					 
				 break;
		}
		WorkCardEditWin.loadWorkRecord(record.get("idx"));
		if(Ext.getCmp("checkItem_id"))
			Ext.getCmp("checkItem_id").enable();
	},
    searchFn: function(searchParam){
    	WorkCardEditWin.searchParam = searchParam;
    	this.store.load();
    }
});
WorkCardEditWin.grid.store.on('beforeload', function() {
	WorkCardEditWin.searchParam.nodeCaseIDX = WorkCardEditWin.nodeCaseIDX;
	this.baseParams.entityJson = Ext.util.JSON.encode(WorkCardEditWin.searchParam);
});

//查看质量检测人员方法
	// FIXME 代码评审点
	WorkCardEditWin.showQCNameListWin = function(workCardIDX){
		
		var qcWin = null;
		if(WorkCardEditWin.qcWin){
			qcWin = WorkCardEditWin.qcWin;
		}else{
			qcWin = new Ext.Window({
			    title:"质量检查人员列表", 
			    layout: 'fit',
				height: 150, width: 375,
				closeAction: 'hide',
				items: new Ext.yunda.Grid({
				    loadURL: ctx + '/workCard!findQCNameByWorkCardIDX.action?workCardIDX=' + workCardIDX,//装载列表数据的请求URL
				    page : false,
				    singleSelect: true,
				    viewConfig: null,
				    tbar:[],
				    storeAutoLoad: true,
				    isEdit: false,
					fields: [{
						header:'质量检查项', dataIndex:'checkItemCode',width: 100
					},{
						header:'质量检查人', dataIndex:'qcEmpName',width: 200
					}],
					//不需要双击显示
					toEditFn : function(){}
				}),
				buttonAlign: 'center',
				buttons: [{
					text: '关闭', iconCls: 'closeIcon', handler: function() {
						qcWin.close();
						WorkCardEditWin.qcWin = null;
					}
				}]
			});  
			
			WorkCardEditWin.qcWin = qcWin;
		}
		qcWin.show();
		
	};
});