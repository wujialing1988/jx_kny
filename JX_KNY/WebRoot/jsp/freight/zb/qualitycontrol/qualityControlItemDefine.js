/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){

	Ext.namespace('QualityControlItemDefine');
	
	QualityControlItemDefine.searchParam = {} ;
	
	/** ************** 定义全局变量开始 ************** */
	QualityControlItemDefine.firstLoad = true;						// 定义一个标识变量，该变量用以标识QCItem.grid.store的load事件仅在页面初始化时才生效，详见QCItem.grid.store.on('load', function() {})
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 该方法用于根据【质量检查项表格】的表记录，对【质量检查人员】表格的显示（或隐藏）和数据容器的加载
	QualityControlItemDefine.showQCEmpViewFn = function(data) {
		QualityControlItemEmpDefine.grid.show();
		if(data && data.idx){
			QualityControlItemEmpDefine.qcItemIDX = data.idx;
		}
		QualityControlItemEmpDefine.grid.store.load();
	}
	
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义质量检查项表格开始 ************** */
	QualityControlItemDefine.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + '/zbglQualityControlItemDefine!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/zbglQualityControlItemDefine!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/zbglQualityControlItemDefine!delete.action',            //删除数据的请求URL
	    tbar: ['add', 'delete', 'refresh'],
	    title: '质量检查项',
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'客货类型', dataIndex:'vehicleType',hidden:true, editor: { xtype:"hidden",value:vehicleType }
		},{
			header:'顺序号', dataIndex:'seqNo', hidden:true, editor:{ disabled: false }
		},{
			header:'检查项编码', dataIndex:'qcItemNo', editor:{ allowBlank: false, maxLength: 50 }
		},{
			header:'检查项名称', dataIndex:'qcItemName', editor:{ allowBlank: false, maxLength: 50 }
		},{
			header:'站点标示', dataIndex:'siteID', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'业务编码', dataIndex:'businessCode', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'是否默认', dataIndex:'isDefault', editor:{ 
				xtype:'combo', 
				store: new Ext.data.SimpleStore({
		            fields: ["k", "v"],
		            data: [[IS_DEFAULT_NO, "否"], [IS_DEFAULT_YES, "是"]]
				}),
				valueField:'k',
				displayField:'v',
				triggerAction:'all',
				mode:'local',
				value:IS_DEFAULT_NO,
 				allowBlank: false 
        	}, renderer: function(v) {
        		if (v == IS_DEFAULT_NO) return "否";
        		if (v == IS_DEFAULT_YES) return "是";
        	}
		}], 
	    saveFn: function(rowEditor, changes, record, rowIndex){
	    
	    	//判断业务编码是否选择，如果没有默认赋值为提票
			if(!record.data.businessCode){
				record.data.businessCode = '质量检验';
			}
			record.data.siteID = siteID;//站点标示
	    	record.data.vehicleType = vehicleType; //客货类型
	    	
			if(self.loadMask)   self.loadMask.show();
			var cfg = {
	            scope: QualityControlItemDefine.grid, url: this.saveURL, jsonData: record.data,
	            success: function(response, options){
	                if(this.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null) {
	                    this.afterSaveSuccessFn(result, response, options);
	                } else {
	                    this.afterSaveFailFn(result, response, options);
	                }
	            }
	        };
			Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    	
	    },
		listeners:{
			// 行单击的事件监听函数
			rowclick: function (grid, rowIndex ) {
				QualityControlItemDefine.showQCEmpViewFn(this.store.getAt(rowIndex).data);
			}
		},
		// 新增时启用“质量检查项编码”和“质量检查项名称”
		afterAddButtonFn: function(){
			this.enableColumns(['qcItemNo', 'qcItemName']);
			QualityControlItemDefine.isAddFn = true;
			QualityControlItemDefine.showQCEmpViewFn();
	    },
		// “质量检查项编码”和“质量检查项名称”不可以编辑
		beforeEditFn: function(rowEditor, rowIndex){
			this.disableColumns(['qcItemNo', 'qcItemName']);
			QualityControlItemDefine.isAddFn = false;
	        return true;
	    }
	});
	
	//查询前添加过滤条件
	QualityControlItemDefine.grid.store.on('beforeload' , function(){
			var searchParam = QualityControlItemDefine.searchParam ;
			searchParam.vehicleType = vehicleType ;
			searchParam.siteID = siteID ;
			searchParam = MyJson.deleteBlankProp(searchParam);
			this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	
	// 默认以“顺序号”升序排序
	QualityControlItemDefine.grid.store.setDefaultSort('seqNo', 'ASC');
	// 数据加载完成后的页面初始化处理
	QualityControlItemDefine.grid.store.on('load', function(store, records, options) {
		if (this.getCount() > 0) {
			var sm = QualityControlItemDefine.grid.getSelectionModel();
			if (!QualityControlItemDefine.isAddFn) {
				// 如果不是新增操作触发的数据加载，则默认选择第一条记录
				sm.selectRow(0);
			} else {
				// 选择新增的几率
				sm.selectRow(records.length - 1);
			}
			QualityControlItemDefine.showQCEmpViewFn(sm.getSelections()[0].data);
		}		
	});
	/** ************** 定义质量检查项表格结束 ************** */
	
});