/**
 * 工位对应的班组 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('StationUnionWorker');                       //定义命名空间
	
	// 人员选择列表
	StationUnionWorker.empGrid = new Ext.yunda.Grid({
		loadURL: ctx + "/omEmployeeSelect!pageListForWorkStation.action",
		storeAutoLoad: false,
		tbar: [{
			xtype:"label", text:"人员名称"
	    },{
			xtype: "textfield" , id:"empname_Id" , name:"empname"
	    },{
	        text:"查询", iconCls:"searchIcon", handler: function(){        	
	           StationUnionWorker.empGrid.store.load();
	        }
	    }, "-", {
	       text:"确定", iconCls:"saveIcon", handler: function(){
	        	//未选择记录，直接返回
				if(!$yd.isSelectedRecord(StationUnionWorker.empGrid)) return;
	    		var empData = StationUnionWorker.empGrid.selModel.getSelections();
	    		var dataAry = new Array();
	            for (var i = 0; i < empData.length; i++){
	            	var data = {};
	                data.workerID = empData[ i ].get("empid");
	                data.workerCode = empData[ i ].get("empcode");
	                data.workerName = empData[ i ].get("empname");
	                data.workStationIDX = WorkStation.idx ;
	                data.workStationName = WorkStation.workStationName ;
					data.status = workerStatus_new;
	                dataAry.push(data);
	            }
	            Ext.Ajax.request({
	                url:  ctx + "/workStationWorker!saveOrUpdateBatch.action",
	                jsonData: dataAry,
	                success: function(response, options){
	                  	StationUnionWorker.empGrid.loadMask.hide();
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.success == true) {
	                        alertSuccess();
	                        StationUnionWorker.win.hide();
	                        StationUnionWorker.grid.store.reload();
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
	    		StationUnionWorker.win.hide();
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
	StationUnionWorker.empGrid.un('rowdblclick', StationUnionWorker.empGrid.toEditFn, StationUnionWorker.empGrid);
	StationUnionWorker.empGrid.store.on("beforeload", function(){
		var empname = Ext.getCmp("empname_Id").getValue();
		this.baseParams.empname = empname;
		this.baseParams.workStationIDX = WorkStation.idx;
		this.baseParams.orgseq = WorkStation.orgseq;
	});
	//新增编辑窗口
	StationUnionWorker.win = new Ext.Window({
	    title:"选择人员信息", maximizable:true, width:520, height:280, 
	    plain:true, closeAction:"hide", modal:true,layout:"fit",
	    items:StationUnionWorker.empGrid
	});
    
	StationUnionWorker.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workStationWorker!pageList.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/workStationWorker!logicDelete.action',             //删除数据的请求URL
	    storeAutoLoad: false,
	    tbar:[{
	    	text:"作业人员选择", iconCls:"addIcon", handler: function(){	    		
	    		if (Ext.isEmpty(WorkStation.orgseq)) {
	    			MyExt.Msg.alert("请先设置工位的作业班组！");
	    			return;
	    		}
				StationUnionWorker.win.show();
	        	StationUnionWorker.empGrid.store.load();
	    	}
		},'delete',{
		        text: "关闭", iconCls: "closeIcon",handler: function(){ WorkStation.grid.saveWin.hide(); }
	    }],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'工位主键', dataIndex:'workStationIDX', hidden:true
		},{
			header:'工位名称', dataIndex:'workStationName'
		},{
			header:'工作人员ID', dataIndex:'workerID', hidden:true
		},{
			header:'人员编码', dataIndex:'workerCode'
		},{
			header:'人员名称', dataIndex:'workerName'
		},{
			header:'状态', dataIndex:'status', hidden: true
		}]
	});
	StationUnionWorker.grid.un('rowdblclick', StationUnionWorker.grid.toEditFn, StationUnionWorker.grid);
	StationUnionWorker.grid.store.on("beforeload", function(){
		var searchParam = {};
		searchParam.workStationIDX = WorkStation.idx;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	
});