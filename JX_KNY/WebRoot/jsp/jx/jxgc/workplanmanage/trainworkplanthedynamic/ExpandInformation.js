/**
 * 配件检修记录卡实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('ExpandInformation');                       // 定义命名空间
	ExpandInformation.idx = '';
	ExpandInformation.labelWidth = 110;
	ExpandInformation.fieldWidth = 60;
	ExpandInformation.searchParam ="";
	/*** 定义全局函数开始 ***/
	//保存前触发函数
	ExpandInformation.beforeSaveFn = function(data){		
		var form = TrainWorkPlanTheDynamic.searchForm.getForm();		
        var planGenerateDate = new Date().format('Y-m-d');
        if(null != form) planGenerateDate = form.getValues().planGenerateDate;
		if(Ext.isEmpty(data.idx)) data.planGenerateDate = planGenerateDate;		
		return true;
	}

   // 定义表格
	ExpandInformation.grid = new Ext.yunda.Grid({	
		loadURL: ctx + "/expandInformation!pageQuery.action",
		saveURL: ctx + "/expandInformation!saveOrUpdate.action",
	    deleteURL: ctx + "/expandInformation!logicDelete.action",                  //删除数据的请求URL
		storeAutoLoad: false,
		beforeSaveFn: ExpandInformation.beforeSaveFn,  
		tbar: [ 'add','delete'],
		fields: [{
			header: '主键', dataIndex: 'idx', hidden: true, editor: {xtype:'hidden' }
		}, {
			header: '类型', dataIndex: 'dictName',   width: 80, editor: { id:"dictName",maxLength:50 }
		}, {
			header: '内容', dataIndex: 'defineValue',	width: 200, 
			editor:{allowBlank: false, xtype:'textarea',height:100, maxLength:500 }
		}, {
			header: 'planGenerateDate', dataIndex: 'planGenerateDate', hidden: true, editor: {xtype:'hidden' }
		}, {
			header: 'saveStatus', dataIndex: 'saveStatus', hidden: true, editor: {  xtype:'hidden' }
		}, {
			header: 'dictId', dataIndex: 'dictId', hidden: true, editor: {  xtype:'hidden' }
		}],
	    searchFn: function(searchParam){
	    	ExpandInformation.searchParam = searchParam;
	    	this.store.load();
	    },
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        this.saveWin.hide();
	        alertSuccess();
	    }
	});
	
	ExpandInformation.grid.store.on('beforeload', function() {
		var searchParam = ExpandInformation.searchParam;
		var whereList = [] ;
		for (prop in searchParam) {			
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		this.baseParams.sort = "dictName";
		this.baseParams.dir = "ASC";
	});

	
});