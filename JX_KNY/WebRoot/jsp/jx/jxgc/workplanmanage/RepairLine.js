/**
 * 机车检修流水线 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('RepairLine');                      	// 定义命名空间
	
	/** ************* 定义全局变量开始 ************* */
	RepairLine.repairLineName = "";						// 流水线名称 
	/** ************* 定义全局变量结束 ************* */
	
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
			    RepairLine.tree.loader.dataUrl = ctx + '/repairLine!tree.action?parentIDX=' + node.id;
			},
			load: function(node) {
				if (node.getDepth() == 0) {
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
			// 树节点checked状态更新时的事件监听
			checkchange: function( node, checked ) {
				// 递归更新当前节点的子节点的checked状态
				RepairLine.setCheckStatus(node, checked);
			}
	    }    
	});
	
	/**
	 * 递归更新当前节点的子节点的checked状态
	 * @param node 当前节点
	 * @param checked 是否被选中，true：是，false：否
	 */
	RepairLine.setCheckStatus = function (node, checked) {
		node.eachChild( function(node){
			node.attributes.checked = checked;
			node.getUI().checkbox.checked = checked;
			// 递归
			RepairLine.setCheckStatus(node, checked);
		})
	}
	
//	// 选中的树节点变化时的事件监听函数(暂未使用)
//	RepairLine.tree.getSelectionModel().on('selectionchange', function(dsm, node) {
//		if (Ext.isEmpty(node)) return;
//		var depth = node.getDepth();
//		if (depth <= 1) {
//			WorkStationSearcher.repairLineIdx = node.id;
//			WorkStationSearcher.parentIDX = "";
//		} else {
//			WorkStationSearcher.parentIDX = node.id;
//		}
//		WorkStationSearcher.grid.store.load();
//	});
	
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
	
//	// 递归禁用节点(暂未使用)
//	RepairLine.disableNode = function(node) {
//		node.disable();
//		var childNodes = node.childNodes;
//		if (Ext.isEmpty(childNodes)) {
//			return;
//		}
//		for (var i = 0; i < childNodes.length; i ++) {
//			arguments.callee(childNodes[i]);
//		}
//	}
	
	// Modified by hetao at 2015-09-08 调整台位以父子节点方式显示
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
//		var searchParams = MyJson.deleteBlankProp(searchParams);
//		var whereList = [];
//		for(prop in searchParams){
//			whereList.push({propName:prop, propValue:searchParams[prop]});
//		}
//		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
//	});

});