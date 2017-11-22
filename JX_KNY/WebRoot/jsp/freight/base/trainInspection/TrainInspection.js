/**
 * 列检所维护
 */
Ext.onReady(function(){
	
Ext.namespace('TrainInspection');                       //定义命名空间

//定义全局变量保存查询条件
TrainInspection.searchParam = {} ;
TrainInspection.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainInspection!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainInspection!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainInspection!logicDelete.action',            //删除数据的请求URL
    singleSelect: true, saveFormColNum:1,
	fields: [
     {
		header:i18n.TrainInspection.inspectionName, dataIndex:'inspectionName',width: 120,editor: {allowBlank:false}
	},{
		header:i18n.TrainInspection.inspectionCode, dataIndex:'inspectionCode',width: 120,editor: {allowBlank:false}
	},{
		header:i18n.TrainInspection.levelName, dataIndex:'levelName',width: 120,editor:{
				id:'inspectionLevel_combo',
				xtype: 'EosDictEntry_combo',
				hiddenName: 'levelName',
				dicttypeid:'INSPECTION_LEVEL',
				displayField:'dictname',valueField:'dictname',
				allowBlank:false,
				hasEmpty:"false",
				returnField: [{widgetId:"levelCode",propertyName:"dictid"}]
	        },searcher: { hidden: true }
	},{
		header:'等级编码', dataIndex:'levelCode',hidden:true,width: 120,editor: {xtype:"hidden",id:"levelCode"},searcher: { hidden: true }
	},{
		header:i18n.TrainInspection.remark, dataIndex:'remark',width: 120,editor:{ xtype:'textarea', maxLength:1000 },searcher: { hidden: true }
	},{
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" },searcher: { hidden: true }
	}],
	searchFn: function(searchParam){ 
		TrainInspection.searchParam = searchParam ;
        TrainInspection.grid.store.load();
	},
	afterSaveSuccessFn: function(result, response, options){
		TrainInspection.grid.saveWin.hide();
        TrainInspection.grid.store.reload();
        alertSuccess();
    }	
});



//页面自适应布局
var viewport = new Ext.Viewport({
			layout:'fit',
			items:[{
			layout: 'border',
			items: [{
	        region: 'center', bodyBorder: false,title: i18n.TrainInspection.gdTitle,
	        layout: 'fit', items : [ StationTrack.grid ]
	    	},{ 		
		     	 region: 'west', layout: 'fit',  title: i18n.TrainInspection.ljsTitle,  bodyBorder: false, split: true,width: 600,minSize: 400, maxSize: 800, 
		     	 collapsible : true,   items:[TrainInspection.grid]
			}]
		}] 
});


	
//查询前添加过滤条件
TrainInspection.grid.store.on('beforeload' , function(){
		var searchParam = TrainInspection.searchParam ;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

// 添加加载结束事件
TrainInspection.grid.getStore().addListener('load',function(me, records, options ){
	if(records.length > 0){
		TrainInspection.grid.getSelectionModel().selectFirstRow();
		TrainInspection.loadStationTrack();
	}else{
		StationTrack.inspectionIdx = "###"; ;
		StationTrack.grid.getStore().reload();	
	}
});
	
/**
 * 单击刷新基本信息页面和车辆列表
 */
TrainInspection.grid.on("rowclick", function(grid, rowIndex, e){
	TrainInspection.loadStationTrack();
});

// 刷新股道页面
TrainInspection.loadStationTrack = function(){
    var sm = TrainInspection.grid.getSelectionModel();
   	var records = sm.getSelections();
   	StationTrack.inspectionIdx = records[0].data.idx ;
	StationTrack.grid.getStore().reload();		
}

});