/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {
	Ext.namespace('WorkStationSearcher');
	
	/** ************* 定义全局变量开始 ************* */
	WorkStationSearcher.searchParams = {};
	WorkStationSearcher.wsGroupIDX = "";											// 检修作业流程节点主键
	WorkStationSearcher.labelWidth = 80,
	WorkStationSearcher.fieldWidth = 120,
	
	WorkStationSearcher.repairLineIdx = "";
	WorkStationSearcher.parentIDX = "";
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 【确定】按钮触发的函数处理
	WorkStationSearcher.addFn = function() {
		var ids = [];
		RepairLine.getHasChkedNode(RepairLine.tree.getRootNode(), ids);
		if (Ext.isEmpty(ids)) {
			MyExt.Msg.alert('尚未选择任何记录！');
			return;
		}
		
		var datas = [];
		
		for (var i = 0; i < ids.length; i++) {
			var wsGroupItem = {};
			wsGroupItem.wsGroupIDX = WorkStationSearcher.wsGroupIDX;
			wsGroupItem.wsIDX = ids[i];
			datas.push(wsGroupItem);
		}
    	// Ajax后台数据处理
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
            url: ctx + '/wSGroupItem!save.action',
            jsonData: datas,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	alertSuccess();
                	WSGroupItem.grid.store.reload();
                	// 移除已经添加了的工位节点
                	for (var i = 0; i < ids.length; i++) {
                		var node = RepairLine.tree.getNodeById(ids[i]);
                		if (node) node.remove();
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
//				columnWidth: .5, layout: 'form',
//				defaults: {
//					xtype: 'textfield', width: WorkStationSearcher.fieldWidth
//				}
//			},
//			items:[
//			// Modified by hetao at 2015-09-08 调整台位以父子节点方式显示
//			/*{
//				items: [{
//					fieldLabel: '流水线名称', name: 'repairLineName'
//				}]
//			}, */{									
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
//				RepairLine.repairLineName = WorkStationSearcher.searchParams.repairLineName;
//				// Modified by hetao at 2015-09-08 调整台位以父子节点方式显示
////				RepairLine.grid.store.load();
//			}
//		}, {
//			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
//				// 重新加载工位表格
//				WorkStationSearcher.searchForm.getForm().reset();
//				WorkStationSearcher.repairLineIdx = "";
//				WorkStationSearcher.searchParams = {};
//				WorkStationSearcher.grid.store.load();
//				// 重新加载流水线表格
//				// Modified by hetao at 2015-09-08 调整台位以父子节点方式显示
//				/*RepairLine.repairLineName = "";
//				RepairLine.grid.store.load({
//					// 数据加载完成后，清空流水线表格的选区
//					callback: function(records) {
//						if (records.length > 0) {
//							RepairLine.grid.getSelectionModel().clearSelections();
//						}
//					}
//				});*/
//			}
//		}]
//	});
//	/** ************* 定义查询表单结束 ************* */
//	
//	/** ************* 定义候选表格开始 ************* */
//	WorkStationSearcher.grid = new Ext.yunda.Grid({
//	    loadURL: ctx + '/workStationForJob!pageQuery.action',                 //装载列表数据的请求URL
//	    tbar:null,
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
//	
//	// 以工位名称升序排序
//	WorkStationSearcher.grid.store.setDefaultSort('workStationName', 'ASC');
//	
//	// 查询前添加过滤条件
//	WorkStationSearcher.grid.store.on('beforeload' , function(){
//		// 状态 - 启用
//		WorkStationSearcher.searchParams.status = USE_STATUS;
//		WorkStationSearcher.searchParams.parentIDX = WorkStationSearcher.parentIDX;
//		// 检修流水线主键
//		WorkStationSearcher.searchParams.repairLineIdx = WorkStationSearcher.repairLineIdx;
//		var searchParams = MyJson.deleteBlankProp(WorkStationSearcher.searchParams);
//		var whereList = [];
//		for(prop in searchParams){
//			whereList.push({propName:prop, propValue:searchParams[prop]});
//		}
//		// 过滤掉当前工位组已经存在的工位
//		var sql = "IDX NOT IN(SELECT WS_IDX FROM JXGC_WS_GROUP_ITEM WHERE WS_Group_IDX = '"+ WorkStationSearcher.wsGroupIDX +"')";
//		whereList.push({sql: sql, compare:Condition.SQL});
//		if (Ext.isEmpty(WorkStationSearcher.parentIDX)) {
//			sql = "(PARENT_IDX IS NULL OR PARENT_IDX = 'ROOT_0')";
//			whereList.push({sql: sql, compare:Condition.SQL});
//		}
//		// 只查询修车的流水线工位
//		sql = [];
//		sql.push("IDX IN (");
//		sql.push("SELECT S.IDX FROM JXGC_REPAIR_LINE R, JXGC_WORK_STATION S WHERE R.RECORD_STATUS = 0 AND R.IDX = S.REPAIR_LINE_IDX AND R.REPAIR_LINE_TYPE ='");
//		sql.push(TYPE_TRAIN);			// 修车的流水线
//		sql.push("' AND R.STATUS ='");
//		sql.push(status_use); 			// 状态为启用的流水线
//		sql.push("' AND S.STATUS ='");
//		sql.push(USE_STATUS); 			// 状态为启用的流水线
//		sql.push("')");
//		whereList.push({sql: sql.join(""), compare:Condition.SQL});
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