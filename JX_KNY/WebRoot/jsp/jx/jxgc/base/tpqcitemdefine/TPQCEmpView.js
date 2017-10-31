/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TPQCEmpView');                       //定义命名空间

	/** ************** 定义全局变量开始 ************** */
	TPQCEmpView.searchParams = {};
	TPQCEmpView.qCItemIDX = "###";
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 【设置人员】函数处理
	TPQCEmpView.configEmpFn = function() {
		// 隐藏【质量检查项】表格的行编辑控件
		if (TPQCItemDefine.grid.rowEditor) {
			TPQCItemDefine.grid.rowEditor.slideHide(); 
		}
		TPQCItemEmpDefine.qCItemIDX = TPQCEmpView.qCItemIDX,
		TPQCItemEmpDefine.grid.store.load();
		TPQCItemEmpDefine.win.show();
	}
	
	// 【设置机构】函数处理
	TPQCEmpView.configFn = function(idx) {
		// 设置QCEmpOrg对象的“质量检查人员idx主键”
		TPQCItemEmpOrgDefine.qcEmpIDX = idx;
		// 重新加载【检查人员所辖机构表格】
		TPQCItemEmpOrgDefine.grid.store.load();
		// 显示【质量检查机构维护窗】
		TPQCItemEmpOrgDefine.win.show();
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义质量检查人员表格开始 ************** */
	TPQCEmpView.grid = new Ext.yunda.Grid({
//	    loadURL: ctx + '/tPQCEmpView!pageList.action',                 //装载列表数据的请求URL
		/**
		 * Modified by HeTao in 2015-08-15 （用于解决unix系统下，WN_CONCAT函数不能使用的错误）
		 */
	    loadURL: ctx + '/tPQCItemEmpDefine!queryPageList.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/tPQCItemEmpDefine!logicDelete.action',                 //删除列表数据的请求URL
	    storeAutoLoad: false,
	    title: '质量检查人员',
	    tbar:[{
	    	text:'设置人员', iconCls:'configIcon', handler: TPQCEmpView.configEmpFn
    	},'delete'],
		fields: [{
			header:'质量检查项主键', dataIndex:'qCItemIDX', hidden: true
		},{
			header:'检查人员', dataIndex:'checkEmpID', hidden: true
		},{
			header:'检查人员名称', dataIndex:'checkEmpName', width:20, editor:{  maxLength:25 }
		},{
			header:'检查机构', dataIndex:'checkOrg'
		},{
			header:'设置机构', dataIndex:'idx', width:20, renderer:function(value, metaData, record, rowIndex, colIndex, store){			
				return "<img src='" + imgpathx + "' alt='操作' style='cursor:pointer' onclick='TPQCEmpView.configFn(\"" + value + "\")'/>";
			}, sortable:false
		}], 
		beforeDeleteFn: function(){              
			if (TPQCItemDefine.grid.rowEditor) {
				TPQCItemDefine.grid.rowEditor.slideHide(); 
			}             
	        return true;
	    },
		listeners: {
			// 增加“双击”进行组织机构维护的事件监听
			rowdblclick: function(grid, rowIndex, e) {
				var record = this.store.getAt(rowIndex);
				TPQCEmpView.configFn(record.data.idx);
			}
		}
	});
	// 默认以“检查人员名称”进行升序排序
	TPQCEmpView.grid.store.setDefaultSort('checkEmpName', 'ASC');
	// 取消表格双击进行编辑的事件监听
	TPQCEmpView.grid.un('rowdblclick', TPQCEmpView.grid.toEditFn, TPQCEmpView.grid);
	// 数据加载时的参数设置
	TPQCEmpView.grid.store.on('beforeload', function(){
		var searchParams = TPQCEmpView.searchParams;
		searchParams.qCItemIDX = TPQCEmpView.qCItemIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************** 定义质量检查人员表格结束 ************** */
	
});