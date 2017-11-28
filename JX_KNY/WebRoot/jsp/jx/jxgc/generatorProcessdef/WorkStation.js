/**
 * 机车检修项目 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('WorkStation');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	WorkStation.nodeIDX = "";
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 删除（批量删除）函数
	WorkStation.deleteFn = function(idx) {
		var workStationIdxs = null;
		if (idx instanceof Array) {
			workStationIdxs = idx;
		} else {
			workStationIdxs = [idx];
		}
		
		 //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
        $yd.confirmAndDelete({
        	jsonData: Ext.util.JSON.encode(workStationIdxs),
            scope: WorkStation.grid, url: ctx + '/jobNodeStationDef!delete.action', params: {
				nodeIDX: WorkStation.nodeIDX
			}
        });
	}
	/** **************** 定义全局函数结束 **************** */
	
	WorkStation.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workStationForJob!pageQuery.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/workStationForJob!logicDelete.action',
	    tbar:[{
	    	text:'选择作业工位', iconCls: 'addIcon', handler: function(){
	        	WorkStationSearcher.nodeIDX = WorkStation.nodeIDX;	
	        	RepairLine.nodeIDX = WorkStation.nodeIDX;
	        	// 获取已选择的工位主键，用于过滤工位树
	        	var store = WorkStation.grid.store;
	        	var selectedIds = [];
	        	store.each(function(record){
	        		this.push(record.get('repairLineIdx'));
	        	}, selectedIds);
	        	WorkStationSearcher.selectedIds = selectedIds;
	    		WorkStationSearcher.win.show()
	    	}
	    }, 'delete', '->', '作业工位名称：', {
	    	xtype:'textfield', width:180, id:'workStationName', enableKeyEvents:true, emptyText:'输入工位名称快速检索', listeners: {
//	    		change: function(field, newValue, oldValue ){
//	    			if (newValue != oldValue) {
//	    				WorkStation.grid.store.load();
//	    			}
//	    		},
	    		keyup: function(filed, e) {
					// 如果敲下Enter（回车键），则触发添加按钮的函数处理
					if (e.getKey() == e.ENTER){
	    				WorkStation.grid.store.load();
					}
				}
	    	}
	    	
	    }, {
	    	text:"查询", iconCls:'searchIcon', handler:function(){
	    		WorkStation.grid.store.load();
	    	}
	    }, {
	    	text:"重置", iconCls:'resetIcon', handler:function(){
	    		Ext.getCmp('workStationName').reset();
	    		WorkStation.grid.store.load();
	    	}
	    }],
	    storeAutoLoad:false,
		fields: [{
			header:'编码', dataIndex:'workStationCode', width: 120
		},{
			header:'名称', dataIndex:'workStationName', width: 200
		},{
			header:'流水线主键', dataIndex:'repairLineIdx', hidden:true
		},{
			header:'所属流水线', dataIndex:'repairLineName'
		},{
			header:'所属台位编码', dataIndex:'deskCode', hidden:true
		},{
			header:'所属图', dataIndex:'ownerMap', hidden:true
		},{
			header:'所属台位', dataIndex:'deskName'
		},{
			header:'机务设备主键', dataIndex:'equipIDX', hidden:true
		},{
			header:'机务设备', dataIndex:'equipName', width: 120
		},{
			header:'状态', dataIndex:'status', hidden:true, renderer:function(value){
				if (NEW_STATUS == value) return '新增';
				if (USE_STATUS == value) return '启用';
				if (NULLIFY_STATUS == value) return '作废';
				return '错误！未知状态';
			}
		},{
			header:'备注', dataIndex:'remarks', width: 380
		},{
			header:'操作', dataIndex:'idx', align:'center', width: 60, renderer: function(value){
				return "<img src='" + deleteIcon + "' alt='删除' style='cursor:pointer' onclick='WorkStation.deleteFn(\""+ value +"\")'/>";
			}
		}],
		deleteButtonFn: function(){                         //点击删除按钮触发的函数，默认执行删除操作
	        //未选择记录，直接返回
	        if(!$yd.isSelectedRecord(this)) return;
	        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	        if(!this.beforeDeleteFn()) return;
	        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
	        WorkStation.deleteFn($yd.getSelectedIdx(this));
	    }
	});
	// 设置默认排序
	WorkStation.grid.store.setDefaultSort('workStationName', 'ASC');
	WorkStation.grid.un('rowdblclick', WorkStation.grid.toEditFn, WorkStation.grid);
	
	WorkStation.grid.store.on('beforeload', function(){
		var whereList = [];
		if (!Ext.isEmpty(WorkStation.nodeIDX)) {
			sql = "IDX IN (SELECT Work_Station_IDX FROM JXGC_JobNode_Station_Def WHERE RECORD_STATUS = 0 AND NODE_IDX = '"+ WorkStation.nodeIDX +"')"
			whereList.push({sql: sql, compare:Condition.SQL});
		}
		var workStationName = Ext.getCmp('workStationName').getValue();
		if (!Ext.isEmpty(workStationName)) {
			whereList.push({propName:'workStationName', propValue:workStationName, compare:Condition.EQ});
		}
		whereList.push({propName:'status', propValue:USE_STATUS, compare:Condition.EQ});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
});