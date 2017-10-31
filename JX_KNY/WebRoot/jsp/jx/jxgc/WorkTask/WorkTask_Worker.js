Ext.namespace('Worker');                       //定义命名空间	
	
	/* ***********
	 * 作业人员
	 */ 
	Worker.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/worker!pageQuery.action',                 //装载列表数据的请求URL
	    saveFormColNum:3, searchFormColNum:2,
	    storeAutoLoad:false,
	    tbar:[], singleSelect:true,
	    fields: [{
			header:'IDX', dataIndex:'idx', editor: { xtype:"hidden"}, hidden:true
		},{
			header:'作业卡主键', dataIndex:'workCardIDX', editor: { xtype:"hidden"}, hidden:true
		},{
			header:'worker_id', dataIndex:'workerId', editor: { xtype:"hidden"},hidden:true
		},{
			header:'人员编码', dataIndex:'workerCode', editor: { xtype:"hidden"}
		},{
			header:'人员姓名', dataIndex:'workerName', editor: { xtype:"hidden"}
		},{
			header:'人员班组', dataIndex:'workerTreamName', editor: { xtype:"hidden"}
		}],
		toEditFn:function(){}
	});
	
	Worker.grid.store.on("beforeload",function(){
		var whereList = [];		
		whereList.push({propName:"workCardIDX",propValue:handler.currentIdx});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});