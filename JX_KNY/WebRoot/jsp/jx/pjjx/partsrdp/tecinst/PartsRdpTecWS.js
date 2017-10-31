/**
 * 配件检修工序实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpTecWS');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpTecWS.labelWidth = 100;
	PartsRdpTecWS.fieldWidth = 100;
	
//	PartsRdpTecWS.rdpIDX = "###";					// 作业主键
//	PartsRdpTecWS.rdpNodeIDX = "###";				// 作业节点主键
	PartsRdpTecWS.rdpTecCardIDX = "###";			// 工艺卡主键
	PartsRdpTecWS.wsParentIDX = "ROOT_0";			// 上级工序主键
	PartsRdpTecWS.status = TEC_WP_STATUS_WCL;		// 状态
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	PartsRdpTecWS.finishBatchWPFn = function(){
		if (!$yd.isSelectedRecord(PartsRdpTecWS.grid)) {
			return;
		}
		Ext.Msg.confirm("提示  ", "是否确认提交？", function(btn){
	        if(btn == 'yes') {
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		        	scope: PartsRdpTecWS.grid,
		        	url: ctx + '/partsRdpTecWS!finishBatchWP.action',
					params: {ids: $yd.getSelectedIdx(PartsRdpTecWS.grid)}
		        }));
	        }
	    });  
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义工序树开始 ************** */
	PartsRdpTecWS.tree = new Ext.tree.TreePanel( {
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/partsRdpTecWS!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '工序树',
	        id: "ROOT_0",
	        leaf: false
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    animate : false,
	    useArrows : true,
	    border : false,
	    listeners: {
	        click: function(node, e) {
				PartsRdpTecWS.wsParentIDX = node.id;
	        	// 重新加载【配件检修工序】列表
	        	PartsRdpTecWS.grid.store.load();
//	        	// 设置工序列表表格的title
//	        	PartsRdpTecWS.grid.setTitle(node.text + "&nbsp;-&nbsp;下级工序");
	        }
	    }    
	});
	PartsRdpTecWS.tree.on('beforeload', function(node){
	    PartsRdpTecWS.tree.loader.dataUrl = ctx + '/partsRdpTecWS!tree.action?parentIDX=' + node.id + '&rdpTecCardIDX=' + PartsRdpTecWS.rdpTecCardIDX;
	});
	/** ************** 定义工序树结束 ************** */
	
	PartsRdpTecWS.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpTecWS!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/partsRdpTecWS!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/partsRdpTecWS!logicDelete.action',            //删除数据的请求URL
	    tbar:[{
	    	xtype:'label', text:'状态：'
    	}, {
    		xtype: 'radio', boxLabel: '未处理', checked: true, name: 'status_ws', inputValue: TEC_WP_STATUS_WCL, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpTecWS.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpTecWS.grid.store.load();
    			}
    		}
    	}, '&nbsp;', {
    		xtype: 'radio', boxLabel: '已处理', name: 'status_ws', inputValue: TEC_WP_STATUS_YCL, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpTecWS.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpTecWS.grid.store.load();
    			}
    		}
    	}, '&nbsp;', {
    		xtype: 'radio', boxLabel: '所有', name: 'status_ws', inputValue: -1, listeners: {
    			check: function(radio, checked) {
    				if (!checked) return;
    				PartsRdpTecWS.status = radio.getRawValue();
    				// 根据选择的状态重新加载表格数据 
					PartsRdpTecWS.grid.store.load();
    			}
    		}
    	}, '->', {
			text:'批量处理', iconCls:'configIcon', handler: PartsRdpTecWS.finishBatchWPFn
		}],
		storeAutoLoad: false,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'作业主键', dataIndex:'rdpIDX', hidden:true
		},{
			header:'作业节点主键', dataIndex:'rdpNodeIDX', hidden:true
		},{
			header:'工艺卡主键', dataIndex:'rdpTecCardIDX', hidden:true
		},{
			header:'上级工序主键', dataIndex:'wsParentIDX', hidden:true
		},{
			header:'工序主键', dataIndex:'wsIDX', hidden:true
		},{
			header:'顺序号', dataIndex:'seqNo', width: 15
		},{
			header:'工序编号', dataIndex:'wsNo', width: 30
		},{
			header:'工序名称', dataIndex:'wsName', width: 60
		},{
			header:'工序描述', dataIndex:'wsDesc'
		},{
			header:'状态', dataIndex:'status', width: 20,
			renderer: function(value, metadata, record, rowIndex, colIndex, store) {
				if (value == TEC_WP_STATUS_WCL) return "未处理";
				if (value == TEC_WP_STATUS_YCL) return "已处理";
				return "错误！未知状态";
			}
		}]
	});
	
	// 取消表格的行双击事件监听处理函数
	PartsRdpTecWS.grid.un('rowdblclick', PartsRdpTecWS.grid.toEditFn, PartsRdpTecWS.grid);
	PartsRdpTecWS.grid.store.setDefaultSort('seqNo', 'ASC');
	
	// 列表数据容器加载时的过滤条件设置
	PartsRdpTecWS.grid.store.on('beforeload', function() {
		var searchParams = {};
//		searchParams.rdpIDX = PartsRdpTecWS.rdpIDX;										// 作业主键
//		searchParams.rdpNodeIDX = PartsRdpTecWS.rdpNodeIDX;								// 作业节点主键
		searchParams.rdpTecCardIDX = PartsRdpTecWS.rdpTecCardIDX;						// 工艺卡主键
		searchParams.wsParentIDX = PartsRdpTecWS.wsParentIDX;							// 上级工序主键
		if (PartsRdpTecWS.status != -1) {
			
			searchParams.status = PartsRdpTecWS.status;												// 状态
		}
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
});