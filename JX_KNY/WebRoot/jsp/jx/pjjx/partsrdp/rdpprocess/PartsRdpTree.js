/**
 * 检修作业流程 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace('PartsRdpTree'); 
	
	/** ************** 定义全局变量开始 ************** */
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 重新加载【作业节点树】
	PartsRdpTree.reload = function(path) {
        PartsRdpTree.tree.root.reload();
        if (path == undefined || path == "" || path == "###") {
			PartsRdpTree.tree.root.expand();
        } else {
        	// 展开树到指定节点
	        PartsRdpTree.tree.expandPath(path);
        }
	}
	
	// 设置配件检修作业计划单信息表单信息
	PartsRdpTree.setPartBaseForm = function(form, node) {
		form.find('name', 'partsNo')[0].setValue(node.attributes["partsNo"]),
		form.find('name', 'partsName')[0].setValue(node.attributes["partsName"]),
		form.find('name', 'specificationModel')[0].setValue(node.attributes["specificationModel"]),
		form.find('name', 'extendNo')[0].setValue(node.attributes["extendNo"]),
		form.find('name', 'trainType')[0].setValue(node.attributes["trainType"]),
		form.find('name', 'repair')[0].setValue(node.attributes["repair"]),
		form.find('name', 'planStartTime')[0].setValue(node.attributes["planStartTime"]),
		form.find('name', 'planEndTime')[0].setValue(node.attributes["planEndTime"])
	}
	// 页面初始化处理
	PartsRdpTree.initFn = function(node) {
		if (node == null || node.id == null || node.id == "ROOT_0") {
			PartsRdpProcess.rdpIdx = null;
			// 清空检修作业基本信息
			PartsRdpProcess.baseForm.getForm().reset();
            // 清空工单列表
            PartsRdpRecordCard.grid.store.removeAll();					// 记录工单
            PartsRdpTecCard.grid.store.removeAll();						// 工艺工单
            PartsRdpNotice.grid.store.removeAll();						// 提票工单
            PartsRdpExpendMatQuery.grid.store.removeAll();				// 物料消耗清空
		} else {
			// 获取作业主键
			var rdpIDX = node.id;
			
			// 设置“其他处理人员”控件的作业主键
			Ext.getCmp('workerEmpName_TecCard').queryParams.rdpIDX = rdpIDX;			// 控件位置：检修作业工单处理窗口
			Ext.getCmp('workerEmpName_RecordCard').queryParams.rdpIDX = rdpIDX;			// 控件位置：检修记录工单处理窗口
			Ext.getCmp('workerEmpName_Notice').queryParams.rdpIDX = rdpIDX;				// 控件位置：提票工单处理窗口
			Ext.getCmp('workerEmpName_assign').queryParams.rdpIDX = rdpIDX;				// 控件位置：有指派批量销活
			Ext.getCmp('workerEmpName_noAssign').queryParams.rdpIDX = rdpIDX;			// 控件位置：无指派批量销活
			
			// 设置配件检修作业计划单信息表单信息
			PartsRdpTree.setPartBaseForm(PartsRdpProcess.baseForm, node);
			
			PartsRdpProcess.rdpIdx = rdpIDX;
			
			// 加载【检修作业工单】表格
			PartsRdpTecCard.rdpIDX = rdpIDX;
			PartsRdpTecCard.grid.store.load();
			// 加载【检修记录工单】表格
			PartsRdpRecordCard.rdpIDX = rdpIDX;
			PartsRdpRecordCard.grid.store.load();
			// 加载【提票工单】表格
			PartsRdpNotice.rdpIDX = rdpIDX;
			PartsRdpNotice.grid.store.load();
			// 加载【物料消耗情况】表格
			PartsRdpExpendMatQuery.rdpIDX = rdpIDX;
			PartsRdpExpendMatQuery.grid.store.load();
			
			// 清空“其他处理人员”字段值
			Ext.getCmp('workEmpID_noAssign').reset();
			Ext.getCmp('workerEmpName_noAssign').clearValue();
			Ext.getCmp('workEmpID_assign').reset();
			Ext.getCmp('workerEmpName_assign').clearValue();
			
		}
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义作业节点树开始 ************** */
	PartsRdpTree.tree = new Ext.tree.TreePanel( {
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/partsRdp!partsTree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '配件信息',
	        id: "ROOT_0",
	        leaf: false,
	        listeners: {
	        	load: function() {
//	        		var sm = PartsRdpTree.tree.getSelectionModel();
	        	}
	        }
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    autoShow: true,
	    useArrows : true,
	    border : false,
	    listeners: {
	    	render: function() {
	    		PartsRdpTree.reload();
	    	},
	        click: function(node, e) {
	        	if (node.leaf) {
	        		PartsRdpTree.initFn(node);
	        	}
	        },
	        load: function() {
	        	PartsRdpTree.initFn();
	        }
	    }    
	});
	
	// 选中的树节点变化时的事件监听函数
	PartsRdpTree.tree.getSelectionModel().on('selectionchange', function(dsm, node) {
		if (node == null || node.id == null || node.id == "ROOT_0") {
			return;
		}
		// 设置配件检修作业计划单信息表单信息
		PartsRdpTree.setPartBaseForm(PartsRdpProcess.baseForm, node);
	});
	/** ************** 定义作业节点树结束 ************** */
	
})