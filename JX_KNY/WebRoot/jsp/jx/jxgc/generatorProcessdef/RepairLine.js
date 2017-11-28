/**
 * 机车检修流水线 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('RepairLine');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	RepairLine.nodeIDX = "";							// 检修作业流程节点主键
	RepairLine.repairLineName = "";						// 流水线名称 
	/** **************** 定义全局变量结束 **************** */
	
	// 机车检修作业流程节点树型列表
	// Modified by hetao at 2015-09-08 调整台位以父子节点方式显示
	RepairLine.tree =  new Ext.tree.TreePanel({
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/repairLine!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '流水线',
	        id: "ROOT_0",
	        leaf: false
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    autoShow: true,
	    useArrows : true,
	    border : false,
	    listeners: {
	    	render: function() {
	    	},
	        beforeload:  function(node){
			    RepairLine.tree.loader.dataUrl = ctx + '/repairLine!tree.action?parentIDX=' + node.id + '&checkedTree=false';
			},
			load: function(node) {
				if (node.getDepth() != 0) {
					return;
				}
				// 过滤树节点，移除已经添加到工位组的工位
				if (!Ext.isEmpty(WorkStationSearcher.selectedIds)) {
					for (var i in WorkStationSearcher.selectedIds) {
						var childNodes = node.childNodes;
						for (var j in childNodes) {
							if (WorkStationSearcher.selectedIds[i] == childNodes[j].id) {
								node.removeChild(childNodes[j]);
							}
						}
					}
				}
			},
			dblclick: function( node, e ) {
				// 通过双击进行工位关联
        		if (node.getDepth() <= 1) {
        			return;
        		}
        		WorkStationSearcher.addFn();
        	}
	    }    
	});
	
	// 递归获取勾选的树节点
	RepairLine.getHasChkedNode = function(node, array) {
		if(typeof(node) == 'undefined') return false;
		if(node.attributes.checked == true && node.disabled != true) {
			array.push(node.id);
		}
		var childs = node.childNodes; //找到该节点的所有子节点
		for(var i = 0; i < childs.length; i++){
			arguments.callee(childs[i], array);
		}
	}
	
	// 获取已选择的树节点
	RepairLine.getSelectedNode = function() {
		var sm = RepairLine.tree.getSelectionModel();
		var node = sm.getSelectedNode();
		if (Ext.isEmpty(node) || node.getDepth() <= 1) {
			return null;
		}
		return node.id;
	}
	
//	RepairLine.grid = new Ext.yunda.Grid({
//	    loadURL: ctx + '/repairLineSearcher!pageQuery.action',                 //装载列表数据的请求URL
//	    tbar:null,
//	    singleSelect:true,
//		fields: [{
//			header:'idx主键', dataIndex:'idx', hidden:true
//		},{
//			header:'流水线编码', dataIndex:'repairLineCode', width: 80, hidden:true
//		},{
//			header:'流水线名称', dataIndex:'repairLineName'
//		},{
//			header:'流水线类型', dataIndex:'repairLineType', hidden: true
//		},{
//			header:'所属车间', dataIndex:'plantOrgId', hidden: true
//		},{
//			header:'所属车间', dataIndex:'plantOrgName', width: 150,  hidden:true
//		},{
//			header:'所属车间序列', dataIndex:'plantOrgSeq', hidden: true
//		},{
//			header:'所属股道编码', dataIndex:'trackCode', hidden: true
//		},{
//			header:'所属股道', dataIndex:'trackName', width: 80, hidden:true
//		},{
//			header:'状态', dataIndex:'status', hidden:true,
//			renderer : function(v){if(v == status_new)return "新增";else if(v == status_use) return "启用";else return "作废";}
//		},{
//			header:'备注', dataIndex:'remarks', width: 180, hidden:true
//		}]
//	});
//	// 取消函双击进行编辑的事件监听
//	RepairLine.grid.un('rowdblclick', RepairLine.grid.toEditFn, RepairLine.grid);
//	
//	RepairLine.grid.selModel.on('rowselect', function(sm, rowIndex, record){
//		WorkStationSearcher.repairLineIdx = record.get('idx');
//		WorkStationSearcher.grid.store.load();
//	});
//	
//	// 设置默认排序
//	RepairLine.grid.store.setDefaultSort('repairLineName', 'ASC');
//	//查询前添加过滤条件
//	RepairLine.grid.store.on('beforeload' , function(){
//		var searchParams = {};
//		// 状态 - 启用
//		searchParams.status = status_use;
//		searchParams.repairLineType = TYPE_TRAIN;
//		searchParams.repairLineName = RepairLine.repairLineName;
//		
//		searchParams = MyJson.deleteBlankProp(searchParams);
//		var whereList = [];
//		for(prop in searchParams){
//			whereList.push({propName:prop, propValue:searchParams[prop]});
//		}
//		// 一个作业流程节点在同一个流水线上只能选择一个工位
//		var sql = [];
//		sql.push("IDX NOT IN(");
//		sql.push("SELECT REPAIR_LINE_IDX FROM JXGC_WORK_STATION WHERE IDX IN (");
//		sql.push("SELECT WORK_STATION_IDX FROM JXGC_JOBNODE_STATION_DEF WHERE RECORD_STATUS = 0 AND NODE_IDX ='");
//		sql.push(RepairLine.nodeIDX);
//		sql.push("'))");
//		whereList.push({sql: sql.join(''), compare:Condition.SQL});
//		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
//	});

});