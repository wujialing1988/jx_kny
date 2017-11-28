/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {
	Ext.namespace('WorkStationSearcher');
	
	/** ************* 定义全局变量开始 ************* */
	WorkStationSearcher.searchParams = {};
	WorkStationSearcher.nodeIDX = "";											// 检修作业流程节点主键
	WorkStationSearcher.labelWidth = 80,
	WorkStationSearcher.fieldWidth = 120,
	WorkStationSearcher.repairLineIdx = "",
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 【确定】按钮触发的函数处理
	WorkStationSearcher.addFn = function() {
		var ids = [];
//		RepairLine.getHasChkedNode(RepairLine.tree.getRootNode(), ids);
		// Modified by hetao at 2015-09-17 修改单次操作只能添加一个工位
		var selectedNodeIdx = RepairLine.getSelectedNode();
		if (!Ext.isEmpty(selectedNodeIdx)) {
			ids.push(selectedNodeIdx);
		}
		
		if (Ext.isEmpty(ids)) {
			MyExt.Msg.alert('尚未选择任何记录！');
			return;
		}
		
		var datas = [];
		
		for (var i = 0; i < ids.length; i++) {
			var jobNodeProjectDef = {};
			jobNodeProjectDef.nodeIDX = WorkStationSearcher.nodeIDX;
			jobNodeProjectDef.workStationIDX = ids[i];
			datas.push(jobNodeProjectDef);
		}
    	// Ajax后台数据处理
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
            url: ctx + '/jobNodeStationDef!save.action',
            jsonData: datas,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	alertSuccess();
                	WorkStation.grid.store.reload();
                	// 一个作业节点只能关联一条流水线上的一个工位，因为添加了一个工位后，从树上移除已经添加了的工位的流水线
                	for (var i = 0; i < ids.length; i++) {
                		var node = RepairLine.tree.getNodeById(ids[i]);
                		var repairLineIdx = node.attributes.repairLineIdx;
                		// 移除流水线
                		RepairLine.tree.getRootNode().removeChild(RepairLine.tree.getNodeById(repairLineIdx));
                	}
                } else {
                    alertFail(result.errMsg);
                }
            }
        }));
		
	}
	/** ************* 定义全局函数结束 ************* */
	
//	/** ************* 定义查询表单开始 ************* */
//	WorkStationSearcher.searchForm = new Ext.form.FormPanel({
//		labelWidth: WorkStationSearcher.labelWidth,
//		baseCls: "x-plain", border: false,
//		style: 'padding: 15px',
//		items:[{
//			// 查询表单第1行
//			baseCls: "x-plain",
//			layout: 'column',
//			defaults:{
//				columnWidth: .3, layout: 'form',
//				defaults: {
//					xtype: 'textfield', width: WorkStationSearcher.fieldWidth
//				}
//			},
//			items:[{			
//				items: [{
//					fieldLabel: '流水线名称', name: 'repairLineName'
//				}]
//			}, {									
//				items: [{
//					fieldLabel: '工位编码', name: 'workStationCode'
//				}]
//			}, {
//				items: [{
//					fieldLabel: '工位名称', name: 'workStationName'
//				}]
//			}]
//		}],
//		buttonAlign: 'center',
//		buttons: [{
//			xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
//				// 重新加载工位表格
//				WorkStationSearcher.searchParams = WorkStationSearcher.searchForm.getForm().getValues();
//				WorkStationSearcher.grid.store.load();
//				// 重新加载流水线表格
//				if (!Ext.isEmpty(WorkStationSearcher.searchParams["repairLineName"])) {
//					RepairLine.repairLineName = WorkStationSearcher.searchParams["repairLineName"];
//				}
//				RepairLine.grid.store.load();
//			}
//		}, {
//			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
//				// 重新加载工位表格
//				WorkStationSearcher.searchForm.getForm().reset();
//				WorkStationSearcher.repairLineIdx = "";
//				WorkStationSearcher.searchParams = {};
//				WorkStationSearcher.grid.store.load();
//				// 重新加载流水线表格
//				RepairLine.repairLineName = "";
//				RepairLine.grid.store.load({
//					// 数据加载完成后，清空流水线表格的选区
//					callback: function(records) {
//						if (records.length > 0) {
//							RepairLine.grid.getSelectionModel().clearSelections();
//						}
//					}
//				});
//			}
//		}]
//	});
//	/** ************* 定义查询表单结束 ************* */
//	
//	/** ************* 定义候选表格开始 ************* */
//	WorkStationSearcher.grid = new Ext.yunda.Grid({
//	    loadURL: ctx + '/workStationForJob!pageQuery.action',                 //装载列表数据的请求URL
//	    tbar: null,
//	    storeAutoLoad:false,
//		fields: [{
//			header:'编码', dataIndex:'workStationCode', width: 80
//		},{
//			header:'名称', dataIndex:'workStationName', width: 160
//		},{
//			header:'流水线主键', dataIndex:'repairLineIdx', hidden:true
//		},{
//			header:'所属流水线', dataIndex:'repairLineName', hidden:true
//		},{
//			header:'所属台位编码', dataIndex:'deskCode', hidden:true
//		},{
//			header:'所属图', dataIndex:'ownerMap', hidden:true
//		},{
//			header:'所属台位', dataIndex:'deskName', width: 100
//		},{
//			header:'机务设备主键', dataIndex:'equipIDX', hidden:true
//		},{
//			header:'机务设备', dataIndex:'equipName', width: 100
//		},{
//			header:'状态', dataIndex:'status', hidden:true, renderer:function(value){
//				if (NEW_STATUS == value) return '新增';
//				if (USE_STATUS == value) return '启用';
//				if (NULLIFY_STATUS == value) return '作废';
//				return '错误！未知状态';
//			}
//		},{
//			header:'备注', dataIndex:'remarks', width: 220
//		},{
//			header:'操作', dataIndex:'idx', align:'center', width: 40, renderer: function(value){
//				return "<img src='" + addIcon + "' alt='添加' style='cursor:pointer' onclick='WorkStationSearcher.addFn()'/>";
//			}
//		}], 
//		
//		// 重新编辑函数，双击表格行是，执行添加操作
//		toEditFn: function(grid, rowIndex, e){
//			WorkStationSearcher.addFn();
//		}
//		
//	});
//	// 设置默认排序
//	WorkStationSearcher.grid.store.setDefaultSort('workStationName', 'ASC');
//	//查询前添加过滤条件
//	WorkStationSearcher.grid.store.on('beforeload' , function(){
//		// 状态 - 启用
//		WorkStationSearcher.searchParams.status = USE_STATUS;
//		WorkStationSearcher.searchParams.repairLineIdx = WorkStationSearcher.repairLineIdx;			// 流水线主键
//		var searchParams = MyJson.deleteBlankProp(WorkStationSearcher.searchParams);
//		var whereList = [];
//		for(prop in searchParams){
//			whereList.push({propName:prop, propValue:searchParams[prop]});
//		}
//		// 一个作业流程节点在同一个流水线上只能选择一个工位
//		var sql = [];
//		sql.push('IDX NOT IN(');
//		sql.push('SELECT IDX FROM JXGC_WORK_STATION WHERE IDX IN (');
//		sql.push('SELECT IDX FROM JXGC_WORK_STATION WHERE REPAIR_LINE_IDX IN(');
//		sql.push('SELECT DISTINCT REPAIR_LINE_IDX FROM JXGC_WORK_STATION WHERE IDX IN (');
//		sql.push("SELECT Work_Station_IDX FROM JXGC_JobNode_Station_Def WHERE Record_Status = 0 AND Node_IDX='");
//		sql.push(WorkStationSearcher.nodeIDX);
//		sql.push("'))))");
//		whereList.push({sql: sql.join(''), compare:Condition.SQL});
//		sql = [];
//		sql.push('IDX IN(');
//		sql.push('SELECT S.IDX FROM JXGC_WORK_STATION S, JXGC_REPAIR_LINE R WHERE S.REPAIR_LINE_IDX = R.IDX AND R.STATUS =');
//		sql.push(status_use);			// 状态为启用的流水线
//		sql.push(" And R.REPAIR_LINE_TYPE = '")
//		sql.push(TYPE_TRAIN)			// 修车的流水线
//		sql.push("')");
//		whereList.push({sql: sql.join(''), compare:Condition.SQL});
//		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
//	});
//	/** ************* 定义候选表格结束 ************* */
	
	WorkStationSearcher.win = new Ext.Window({
		title:"选择工位",
		width: 300,
		height: 600,
		modal: true,
		layout: 'fit',
		closeAction: 'hide',
		items:[RepairLine.tree],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '添加', iconCls: 'yesIcon', handler: WorkStationSearcher.addFn
		}, {
			xtype: "button", text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}], 
		listeners: {
			show: function(window){
				// Modified by hetao at 2015-09-08 调整台位以父子节点方式显示
				// 加载流水线树
				RepairLine.tree.getRootNode().reload();
				RepairLine.tree.getRootNode().expand();
				// 加载工位表格
//				WorkStationSearcher.grid.store.load();
			},
			hide: function(){
			}
		}
	});
	
});