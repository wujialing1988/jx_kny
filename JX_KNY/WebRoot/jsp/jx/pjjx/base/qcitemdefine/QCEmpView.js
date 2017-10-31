/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('QCEmpView');                       //定义命名空间

	/** ************** 定义全局变量开始 ************** */
	QCEmpView.searchParams = {};
	QCEmpView.qCItemIDX = "###";
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 【设置人员】函数处理
	QCEmpView.configEmpFn = function() {
		// 隐藏【质量检查项】表格的行编辑控件
		if (QCItem.grid.rowEditor) {
			QCItem.grid.rowEditor.slideHide(); 
		}
		QCEmp.qCItemIDX = QCEmpView.qCItemIDX,
		QCEmp.grid.store.load();
		QCEmp.win.show();
	}
	
	// 【设置机构】函数处理
	QCEmpView.configFn = function(idx) {
		// 设置QCEmpOrg对象的“质量检查人员idx主键”
		QCEmpOrg.qcEmpIDX = idx;
		// 重新加载【检查人员所辖机构表格】
		QCEmpOrg.grid.store.load();
		// 显示【质量检查机构维护窗】
		QCEmpOrg.win.show();
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义质量检查人员表格开始 ************** */
	QCEmpView.grid = new Ext.yunda.Grid({
//	    loadURL: ctx + '/qCEmpView!pageList.action',                 //装载列表数据的请求URL
		/**
		 * Modified by HeTao in 2015-08-15 （用于解决unix系统下，WN_CONCAT函数不能使用的错误）
		 */
	    loadURL: ctx + '/qCEmp!queryPageList.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/qCEmp!logicDelete.action',                 //删除列表数据的请求URL
	    storeAutoLoad: false,
	    title: '质量检查人员',
	    tbar:[{
	    	text:'设置人员', iconCls:'configIcon', handler: QCEmpView.configEmpFn
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
				return "<img src='" + imgpathx + "' alt='操作' style='cursor:pointer' onclick='QCEmpView.configFn(\"" + value + "\")'/>";
			}, sortable:false
		}], 
		beforeDeleteFn: function(){              
			if (QCItem.grid.rowEditor) {
				QCItem.grid.rowEditor.slideHide(); 
			}             
	        return true;
	    },
		listeners: {
			// 增加“双击”进行组织机构维护的事件监听
			rowdblclick: function(grid, rowIndex, e) {
				var record = this.store.getAt(rowIndex);
				QCEmpView.configFn(record.data.idx);
			}
		}
	});
	// 默认以“检查人员名称”进行升序排序
	QCEmpView.grid.store.setDefaultSort('checkEmpName', 'ASC');
	// 取消表格双击进行编辑的事件监听
	QCEmpView.grid.un('rowdblclick', QCEmpView.grid.toEditFn, QCEmpView.grid);
	// 数据加载时的参数设置
	QCEmpView.grid.store.on('beforeload', function(){
		var searchParams = QCEmpView.searchParams;
		searchParams.qCItemIDX = QCEmpView.qCItemIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************** 定义质量检查人员表格结束 ************** */
	
});