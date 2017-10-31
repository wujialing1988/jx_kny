/**
 * 机车检修项目 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('RepairProject');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	RepairProject.nodeIDX = "";
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 删除（批量删除）函数
	RepairProject.deleteFn = function(idx) {
		var projectIdxs = null;
		if (idx instanceof Array) {
			projectIdxs = idx;
		} else {
			projectIdxs = [idx];
		}
		
		 //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
        $yd.confirmAndDelete({
        	jsonData: Ext.util.JSON.encode(projectIdxs),
            scope: RepairProject.grid, url: ctx + '/jobNodeProjectDef!delete.action', params: {
				nodeIDX: RepairProject.nodeIDX
			}
        });
	}
	// 查看作业工单详情
	RepairProject.showDetailFn = function(idx) {
		TrainQR.repairProjectIDX = idx;
		TrainQR.grid.store.load();
	}
	/** **************** 定义全局函数结束 **************** */
	
	RepairProject.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/repairProject!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/repairProject!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/repairProject!logicDelete.action',            //删除数据的请求URL
	    tbar:[{
	    	text:'选择作业项目', iconCls: 'addIcon', handler: function(){
	        	RepairProjectSearcher.nodeIDX = RepairProject.nodeIDX;	
	    		RepairProjectSearcher.win.show();
	    	}
	    }, 'delete', '->', '作业项目名称：', {
	    	xtype:'textfield', width:180, id:'repairProjectName', enableKeyEvents:true, emptyText:'输入项目名称快速检索', listeners: {
//	    		change: function(field, newValue, oldValue ){
//	    			if (newValue != oldValue) {
//	    				RepairProject.grid.store.load();
//	    			}
//	    		},
	    		keyup: function(filed, e) {
					// 如果敲下Enter（回车键），则触发添加按钮的函数处理
					if (e.getKey() == e.ENTER){
	    				RepairProject.grid.store.load();
					}
				}
	    	}
	    	
	    }, {
	    	text:"查询", iconCls:'searchIcon', handler:function(){
	    		RepairProject.grid.store.load();
	    	}
	    }, {
	    	text:"重置", iconCls:'resetIcon', handler:function(){
	    		Ext.getCmp('repairProjectName').reset();
	    		RepairProject.grid.store.load();
	    	}
	    }],
	    storeAutoLoad:false,
		fields: [{
			header:'编码', dataIndex:'repairProjectCode', width: 120//, renderer: function(value, metaData, record){
//				var html = "";
//	  			html = "<span><a href='#' onclick='RepairProject.showDetailFn(\""+ record.get('idx') + "\")'>"+value+"</a></span>";
//	      		return html;
//			}
		},{
			header:'车型主键', dataIndex:'pTrainTypeIdx', hidden:true
		},{
			header:'车型', dataIndex:'pTrainTypeShortname', hidden:true
		},{
			header:'名称', dataIndex:'repairProjectName', width: 200
		},{
			header:'检修项目类型', dataIndex:'repairProjectType', hidden:true
		},{
			header:'状态', dataIndex:'status', hidden:true, width:60
		},{
			header:'备注', dataIndex:'remark', width: 380
		},{
			header:'组成型号类型', dataIndex:'buildUpType', hidden:true
		},{
			header:'组成型号主键', dataIndex:'buildUpTypeIDX', hidden:true
		},{
			header:'组成型号编码', dataIndex:'buildUpTypeCode', hidden:true
		},{
			header:'组成型号', dataIndex:'buildUpTypeName', hidden:true
		},{
			header:'操作', dataIndex:'idx', align:'center', width: 60, renderer: function(value){
				return "<img src='" + deleteIcon + "' alt='删除' style='cursor:pointer' onclick='RepairProject.deleteFn(\""+ value +"\")'/>";
			}
		}],
		deleteButtonFn: function(){                         //点击删除按钮触发的函数，默认执行删除操作
	        //未选择记录，直接返回
	        if(!$yd.isSelectedRecord(this)) return;
	        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	        if(!this.beforeDeleteFn()) return;
	        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
	        RepairProject.deleteFn($yd.getSelectedIdx(this));
	    },
	    
	    listeners: {
		    rowclick: function(grid, rowIndex, e) {
//		    	var record = grid.store.getAt(rowIndex);
//		    	RepairProject.showDetailFn(record.get('idx'));
		    }
	    }
	});
	// 设置默认排序
	RepairProject.grid.store.setDefaultSort('repairProjectName', 'ASC');
	// 取消行双击编辑事件
	RepairProject.grid.un('rowdblclick', RepairProject.grid.toEditFn, RepairProject.grid);
	
	// 选择一条记录时，自动加载作业工单数据供查看
	RepairProject.grid.getSelectionModel().on('rowselect', function(sm, rowIndex, record){
		RepairProject.showDetailFn(record.get('idx'));
	});
	
	// 表格数据加载前的参数设置
	RepairProject.grid.store.on('beforeload', function(){
		var whereList = [];
		if (!Ext.isEmpty(RepairProject.nodeIDX)) {
			sql = "IDX IN (SELECT b.record_card_idx FROM JXGC_JobNode_Union_WorkSeq b WHERE b.record_card_idx = '"+ RepairProject.nodeIDX +"')"
			whereList.push({sql: sql, compare:Condition.SQL});
		}
		var repairProjectName = Ext.getCmp('repairProjectName').getValue();
		if (!Ext.isEmpty(repairProjectName)) {
			whereList.push({propName:'repairProjectName', propValue:repairProjectName, compare:Condition.EQ});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	// 作业项目表格数据加载成功后，自动选择第一条记录
	RepairProject.grid.store.on('load', function(store, records, options){ 
		if (store.getCount() > 0) {
			RepairProject.grid.getSelectionModel().selectRow(0);
		// 如果没有加载数据，清空作业工单表格
		} else if (TrainQR.grid.store.getCount() > 0){
			TrainQR.grid.store.removeAll();
		}
	});
	
});