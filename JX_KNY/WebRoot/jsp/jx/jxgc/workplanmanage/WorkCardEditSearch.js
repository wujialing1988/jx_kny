/**
 * 机车检修作业计划-作业工单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('WorkCardEditSearch'); 	
WorkCardEditSearch.searchParam = {}
WorkCardEditSearch.nodeCaseIDX = '';
WorkCardEditSearch.ids = [];
WorkCardEditSearch.trainWorkPlanEditWin = null;
WorkCardEditSearch.qcWin = null;

WorkCardEditSearch.fillData = function(workCardEntity) {
	var r = new Ext.data.Record();
	for(var i in workCardEntity){
		r.set(i, workCardEntity[i]);			
	}
	TrainWorkSearch.showEditWin(r);
	TrainWorkSearch.idx = workCardEntity.idx;
}

WorkCardEditSearch.loadWorkRecord = function(idx) {
	var cfg = {
        scope: this, url: ctx + '/workCard!getEntityByIDX.action',
        params: {id: idx},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.entity != null) {		            	
                 var workCardEntity = result.entity;
                 WorkCardEditSearch.fillData(workCardEntity);
                 WorkStepSearch.grid.store.load();
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
}

	WorkCardEditSearch.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workCardQuery!getWorkCardListByNode.action',                 
	    saveFormColNum:1,	searchFormColNum:1,
	    singleSelect:true,
	    storeAutoLoad: false,
	    tbar: [{
	        xtype:"label", text:"  作业名称： " 
	    },{
	        xtype: "textfield" ,maxLength: 100,vtype:"validChar"
	    },{
	    	text: "查询", iconCls:"searchIcon", handler: function(){
	    		var grid = WorkCardEditSearch.grid;
	    		var searchParam = WorkCardEditSearch.searchParam;
	    		var workCardName = grid.getTopToolbar().get(1).getValue();
	    		searchParam.workCardName = workCardName;
	    		grid.store.load();
	    	}
	    },{
	    	text: "刷新", iconCls:"refreshIcon", handler: function(){
	    		WorkCardEditSearch.grid.store.load();
	    	}
//	    },{
//	    	text: "关闭", iconCls:"closeIcon", handler: function(){
//	    		if(WorkCardEditSearch.trainWorkPlanEditWin){
//	    			WorkCardEditSearch.trainWorkPlanEditWin.win.hide();
//	    		}
//	    	
//	    		NodeAndWorkCardEditSearch.win.hide();
//	    	}
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
		}],
		toEditFn: function(grid, rowIndex, e){
			var record = grid.store.getAt(rowIndex);
			var status = record.get("status");
			WorkCardEditSearch.loadWorkRecord(record.get("idx"));
			if(Ext.getCmp("checkItem_id"))
				Ext.getCmp("checkItem_id").enable();
		},
	    searchFn: function(searchParam){
	    	WorkCardEditSearch.searchParam = searchParam;
	    	this.store.load();
	    }
	});
	WorkCardEditSearch.grid.store.on('beforeload', function() {
		WorkCardEditSearch.searchParam.nodeCaseIDX = WorkCardEditSearch.nodeCaseIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(WorkCardEditSearch.searchParam);
	});
	
	//查看质量检测人员方法
	// FIXME 代码评审点
	WorkCardEditSearch.showQCNameListWin = function(workCardIDX){
		var qcWin = null;
		if(WorkCardEditSearch.qcWin){
			qcWin = WorkCardEditSearch.qcWin;
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
						WorkCardEditSearch.qcWin = null;
					}
				}]
			});  
			
			WorkCardEditSearch.qcWin = qcWin;
		}
		qcWin.show(); 
		
	};
});