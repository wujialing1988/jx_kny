/**
 * 工位作业人员 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('WorkStationWorker');                       //定义命名空间
WorkStationWorker.searchParams = {};                     //全局查询参数集
//人员选择列表
WorkStationWorker.empGrid = new Ext.yunda.Grid({
	loadURL: ctx + "/omEmployeeSelect!pageListForWorkStation.action",
	storeAutoLoad: false,
	tbar: [{
        xtype:"label", text:"人员名称"
    },{
         xtype: "textfield" , id:"empname_Id" , name:"empname"
    },{
        text:"查询", iconCls:"searchIcon",
        handler: function(){        	
           WorkStationWorker.empGrid.store.load();
        }
    },"-",{
       text:"确定", iconCls:"saveIcon",
        handler: function(){
        	//未选择记录，直接返回
    		if(!$yd.isSelectedRecord(WorkStationWorker.empGrid)) return;
    		var empData = WorkStationWorker.empGrid.selModel.getSelections();
    		var dataAry = new Array();
            for (var i = 0; i < empData.length; i++){
            	var data = {};
                data.workerID = empData[ i ].get("empid");
                data.workerCode = empData[ i ].get("empcode");
                data.workerName = empData[ i ].get("empname");
                data.workStationIDX = workStationIDX ;
				data.status = workerStatus_new;
                dataAry.push(data);
            }
            Ext.Ajax.request({
                url:  ctx + "/workStationWorker!saveOrUpdateBatch.action",
                jsonData: dataAry,
                success: function(response, options){
                  	WorkStationWorker.empGrid.loadMask.hide();
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.success == true) {
                        alertSuccess();
                        WorkStationWorker.win.hide();
                        WorkStationWorker.grid.store.reload();
                        Gzforeman.grid.store.reload();
                    } else {
                        alertFail(result.errMsg);
                    }
                },
                failure: function(response, options){
                    StoreKeeper.loadMask.hide();
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
        }
	},{
    	text:"关闭", iconCls:"closeIcon", handler:function(){
    		WorkStationWorker.win.hide();
    	}
    }],
	fields: [{
		header:"人员主键", dataIndex:"empid", hidden: true
	},{
		header:"人员编码", dataIndex:"empcode"	
	},{
		header:"人员名称", dataIndex:"empname"
	}]
});
//移除侦听器
WorkStationWorker.empGrid.un('rowdblclick', WorkStationWorker.empGrid.toEditFn, WorkStationWorker.empGrid);
WorkStationWorker.empGrid.store.on("beforeload", function(){
	var empname = Ext.getCmp("empname_Id").getValue();
	this.baseParams.empname = empname;
	this.baseParams.workStationIDX = workStationIDX;
	this.baseParams.orgseq = orgseq;
});
//新增编辑窗口
WorkStationWorker.win = new Ext.Window({
    title:"选择人员信息", maximizable:true, width:520, height:280, 
    plain:true, closeAction:"hide", modal:true,layout:"fit",
    items:WorkStationWorker.empGrid
});
//作业人员列表
WorkStationWorker.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/workStationWorker!pageList.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/workStationWorker!logicDelete.action',            //删除数据的请求URL
    storeAutoLoad: false,
    tbar: [{
	    	text:"作业人员选择", iconCls:"addIcon", handler:function(){
	        WorkStationWorker.win.show();
	        WorkStationWorker.empGrid.store.load();
    	}
    },'delete',{
    	text:"关闭", iconCls:"closeIcon", handler:function(){
    		Gzforeman.win.hide();
    	}
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'工位及班组主键', dataIndex:'workStationIDX', hidden:true, editor:{  xtype:'hidden' }
	},{
		header:'工作人员ID', dataIndex:'workerID', hidden:true, editor:{ xtype:'hidden' }
	},{
		header:'人员编码', dataIndex:'workerCode', editor:{  }
	},{
		header:'人员名称', dataIndex:'workerName', editor:{  }
	},{
		header:'状态', dataIndex:'status', hidden: true,
		//renderer : function(v){if(v == workerStatus_new)return "新增";else if(v == workerStatus_use) return "启用";else return "作废";},
		editor:{ xtype:'hidden', value: workerStatus_new }
	}],
	/**
     * 执行删除操作后触发的函数，
     * 该函数依赖deleteButtonFn，如果deleteButtonFn被覆盖则失效
     * @return {Boolean}
     */
    afterDeleteFn: function(){
    	WorkStationWorker.empGrid.store.reload();
    	Gzforeman.grid.store.load();
    }
});
//移除侦听器
WorkStationWorker.grid.un('rowdblclick', WorkStationWorker.grid.toEditFn, WorkStationWorker.grid);
WorkStationWorker.grid.store.on("beforeload", function(){
	WorkStationWorker.searchParams.workStationIDX = workStationIDX;
	this.baseParams.entityJson = Ext.util.JSON.encode(WorkStationWorker.searchParams);
});
});