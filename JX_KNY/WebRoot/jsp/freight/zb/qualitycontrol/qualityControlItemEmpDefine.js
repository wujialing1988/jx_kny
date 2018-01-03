/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('QualityControlItemEmpDefine');
	
	/** ************** 定义全局变量开始 ************** */
	QualityControlItemEmpDefine.searchParams = {};
	QualityControlItemEmpDefine.qcItemIDX = "###";
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 【设置人员】函数处理
	QualityControlItemEmpDefine.configEmpFn = function() {
		// 隐藏【质量检查项】表格的行编辑控件
		if (QualityControlItemDefine.grid.rowEditor) {
			QualityControlItemDefine.grid.rowEditor.slideHide(); 
		}
		ZbglItemEmpDefine.QualityControlItemEmpDefine = QualityControlItemEmpDefine;
		ZbglItemEmpDefine.qcItemIDX = QualityControlItemEmpDefine.qcItemIDX,
		ZbglItemEmpDefine.grid.store.load();
		ZbglItemEmpDefine.win.show();
	}
	
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义质量检查人员表格开始 ************** */
	QualityControlItemEmpDefine.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/zbglQualityControlItemEmpDefine!pageList.action',                 //装载列表数据的请求URL
	    //loadURL: ctx + '/zbglQualityControlItemEmpDefine!queryPageList.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/zbglQualityControlItemEmpDefine!delete.action',                 //删除列表数据的请求URL
	    storeAutoLoad: false,
	    title: i18n.QualityInspectionConfig.qualityInsPerson,
	    tbar:[{
	    	text:i18n.QualityInspectionConfig.setPerson, iconCls:'configIcon', handler: QualityControlItemEmpDefine.configEmpFn
    	},'delete'],
		fields: [{
			header:i18n.QualityInspectionConfig.idx, dataIndex:'idx', hidden:true
		},{
			header:i18n.QualityInspectionConfig.qualityCtrIdx, dataIndex:'qcItemIDX', hidden: true
		},{
			header:i18n.QualityInspectionConfig.checkPerson, dataIndex:'checkEmpID', hidden: true
		},{
			header:i18n.QualityInspectionConfig.checkEmpName, dataIndex:'checkEmpName', width:20, editor:{  maxLength:25 }
		},{
			header:i18n.QualityInspectionConfig.siteID, dataIndex:'siteID', hidden: true
		}]
	});
	// 默认以“检查人员名称”进行升序排序
	QualityControlItemEmpDefine.grid.store.setDefaultSort('checkEmpName', 'ASC');
	// 取消表格双击进行编辑的事件监听
	QualityControlItemEmpDefine.grid.un('rowdblclick', QualityControlItemEmpDefine.grid.toEditFn, QualityControlItemEmpDefine.grid);
	// 数据加载时的参数设置
	QualityControlItemEmpDefine.grid.store.on('beforeload', function(){
		var searchParams = QualityControlItemEmpDefine.searchParams;
		searchParams.qcItemIDX = QualityControlItemEmpDefine.qcItemIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************** 定义质量检查人员表格结束 ************** */
});