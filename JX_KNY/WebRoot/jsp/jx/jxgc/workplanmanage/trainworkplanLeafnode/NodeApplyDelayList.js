/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('NodeApplyDelayList');
	
	/** **************** 定义全局变量开始 **************** */
	NodeApplyDelayList.labelWidth = 100;
	NodeApplyDelayList.fieldWidth = 140;
	NodeApplyDelayList.nodeIDX = '';
	NodeApplyDelayList.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"请稍候..."});	
	/** **************** 定义全局变量结束 **************** */
  	   
	NodeApplyDelayList.cancel = function(idx, nodeIDX,type){
		data = {};
    	data.idx = idx;
    	data.nodeIDX = nodeIDX;
    	data.editStatus = type;
	   	var cfg = {
	        url: ctx + "/jobProcessNodeUpdateApply!updateConfirmApply.action",
			jsonData: data,
	        timeout: 600000,
	        success: function(response, options){
				if(NodeApplyDelayList.loadMask) NodeApplyDelayList.loadMask.hide();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                var nodeFormEditWin = LeafNodeAndWorkCardEditWin.form.getForm();
                    nodeFormEditWin.findField("planBeginTime").enable();
	                nodeFormEditWin.findField("planEndTime").enable();
	            	NodeApplyDelayList.grid.store.reload(); // 重新加载
	            	TrainWorkPlanForLeafNode.store.reload(); // 重新加载
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){	
	        	if(NodeApplyDelayList.loadMask) NodeApplyDelayList.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
   }
   
   	/** **************** 定义grid开始 **************** */  
	NodeApplyDelayList.grid = new Ext.yunda.Grid({
		toEditFn: Ext.emptyFn,
		storeAutoLoad: false,
		loadURL: ctx + '/jobProcessNodeUpdateApply!findNodeApplyList.action',
		viewConfig: null,
		page:false, singleSelect:true, remoteSort: false,
		tbar:[{	text : '刷新',
					iconCls : 'refreshIcon',
					handler : function() {
						NodeApplyDelayList.grid.store.reload();
					}							
				}],
		fields: [{
  		  header: '操作',dataIndex:"editStatus",id:"oprator",hidden:false,
		  renderer: function(value, metaData, record, rowIndex, colIndex, store){
  		 	var idx = record.get('idx');
  		 	var nodeIDX = record.get('nodeIDX');
			var html = "";
			if(value == EDIT_STATUS_WAIT){
    			html = "<span><a href='#' onclick = 'NodeApplyDelayList.cancel(\"" + idx +													// [0]
					"\",\""+ nodeIDX + "\",\""+ EDIT_STATUS_UN + "\")'>取消申请</a></span> &nbsp&nbsp ";	    
			}
			return html;
    	   }	
		},{
			dataIndex: 'idx', header: 'idx主键', hidden:true
		}, {
			dataIndex: 'nodeName', header: '节点名称', width: 110
		}, {
			dataIndex: 'nodeIDX', header: '节点idx', hidden:true	
		}, {
			dataIndex: 'planBeginTime', header: '原计划开始', xtype: "datecolumn", format:"m-d H:i", width: 90
		}, {
			dataIndex: 'planEndTime', header: '原计划结束', xtype: "datecolumn", format:"m-d H:i", width: 90
		}, {
			dataIndex: 'newPlanBeginTime', header: '申请开始', xtype: "datecolumn", format:"m-d H:i", width: 90
		}, {
			dataIndex: 'newPlanEndTime', header: '申请结束', xtype: "datecolumn", format:"m-d H:i", width: 90
		}, {	
			dataIndex: 'reason', header: '延期原因', width: 170,renderer: function(value, metaData, record, rowIndex, colIndex, store){
				metaData.attr = 'style="white-space:normal;"';
				return value;
			}
		}, {	
			dataIndex: 'opinions', header: '审批意见', width: 170,renderer: function(value, metaData, record, rowIndex, colIndex, store){
				metaData.attr = 'style="white-space:normal;"';
				return value;
			}
		}, {	
			dataIndex: 'editStatus', header: '审批状态', width: 90,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				if(value == EDIT_STATUS_ON ) {
					return "确认";
				}else if(value == EDIT_STATUS_WAIT ) {
//						var colModels = NodeApplyDelayList.grid.colModel;
//						colModels.setHidden(11,true);
						return "待确认";
					}
					else  {					
						return "取消";
					}
	    	   }
	    }]
	});
	
	// Grid数据加载前的查询条件传递 
	NodeApplyDelayList.grid.store.on('beforeload', function(){
		searchParams = {};
		searchParams.nodeIDX = NodeApplyDelayList.nodeIDX;
//		searchParams.editStatusStr = EDIT_STATUS_WAIT+ "," + EDIT_STATUS_UN ;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	// 上方表格【工序延误记录表格】定义
	NodeApplyDelayList.gridSearch = new Ext.yunda.Grid({
		toEditFn: Ext.emptyFn,
		storeAutoLoad: false,
		loadURL: ctx + '/jobProcessNodeUpdateApply!findNodeApplyList.action',
		viewConfig: null,
		page:false, singleSelect:true, remoteSort: false,
		tbar:[{	text : '刷新',
					iconCls : 'refreshIcon',
					handler : function() {
						NodeApplyDelayList.gridSearch.store.reload();
					}							
				}],
		fields: [{
			dataIndex: 'idx', header: 'idx主键', hidden:true
		}, {
			dataIndex: 'nodeName', header: '节点名称', width: 150
		}, {
			dataIndex: 'nodeIDX', header: '节点idx', hidden:true	
		}, {
			dataIndex: 'planBeginTime', header: '申请前开始时间', xtype: "datecolumn", format:"Y-m-d H:i", width: 120
		}, {
			dataIndex: 'planEndTime', header: '申请前结束时间', xtype: "datecolumn", format:"Y-m-d H:i", width: 120
		}, {
			dataIndex: 'newPlanBeginTime', header: '申请修改开始时间', xtype: "datecolumn", format:"Y-m-d H:i", width: 120
		}, {
			dataIndex: 'newPlanEndTime', header: '申请修改结束时间', xtype: "datecolumn", format:"Y-m-d H:i", width: 120
		}, {	
			dataIndex: 'reason', header: '延期原因', width: 200
		}, {	
			dataIndex: 'opinions', header: '审批意见', width: 200
		}, {	
			dataIndex: 'editStatus', header: '审批状态', width: 200,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){
				if(value == EDIT_STATUS_ON ) return "确认";
				else if(value == EDIT_STATUS_WAIT ) return "待确认";
				else return "取消";
    	   }	
		}]
	});
	
	// Grid数据加载前的查询条件传递 
	NodeApplyDelayList.gridSearch.store.on('beforeload', function(){
		searchParams = {};
		searchParams.nodeIDX = NodeApplyDelayList.nodeIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
//	// 申请确认窗口
//	NodeApplyDelayList.showWin = new Ext.Window({
//		title:"申请延期确认",
//		modal: true,
//		closeAction: 'hide',
//		width:1100, height:350,
//		layout:"fit",
//		items:[{
//			layout: 'fit',
//			items: [NodeApplyDelayList.grid]
//		}],
//		listeners: {
//			show: function(me, eOpts) {
//				NodeApplyDelayList.grid.store.load();
//			}
//		},
//		buttonAlign: 'center',
//		buttons: [{
//			text: '关闭', iconCls: 'closeIcon', handler: function() {
//				this.findParentByType('window').hide();
//			}
//		}]
//	});
//	
//	NodeApplyDelayList.showApplyWin = function(parentIDX){
//		NodeApplyDelayList.nodeIDX = nodeIDX;
//		NodeApplyDelayList.showWin.show();
//	}
	
})