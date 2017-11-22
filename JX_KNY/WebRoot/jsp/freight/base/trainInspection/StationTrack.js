/**
 * 股道维护
 */
Ext.onReady(function(){
	
Ext.namespace('StationTrack');                       //定义命名空间

StationTrack.inspectionIdx = '###' ;				// 列检所维护ID

//定义全局变量保存查询条件
StationTrack.searchParam = {} ;
StationTrack.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/stationTrack!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/stationTrack!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/stationTrack!logicDelete.action',            //删除数据的请求URL
    singleSelect: false, saveFormColNum:1,
	tbar:['add','delete'],
	fields: [
     	{
		header:'列检所', dataIndex:'inspectionIdx', hidden:true,width: 120,editor: {xtype:'hidden'}
	},
     	{
		header:i18n.TrainInspection.trackName, dataIndex:'trackName',width: 120,editor: {allowBlank:false}
	},
     	{
		header:i18n.TrainInspection.trackCode, dataIndex:'trackCode',width: 120,editor: {allowBlank:false}
	},
     	{
		header:i18n.TrainInspection.seqNo, dataIndex:'seqNo',width: 120,editor: {
					xtype:'numberfield',
			        allowDecimals:false,
			        allowNegative:false,
			        minValue:1,
			        maxValue:99,
			        allowBlank:false}
	},
		{
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}],
	// 必须要选中列检所才能添加
	beforeShowSaveWin: function(){
		if(StationTrack.inspectionIdx == '###'){
			MyExt.Msg.alert("请先选择列检所！");
			return false;
		}
	},  
	// 保存之前存状态
	beforeSaveFn: function(data){ 
		if(Ext.isEmpty(data.inspectionIdx)){
			data.inspectionIdx = StationTrack.inspectionIdx ;
		}
		return true; 
	},
	searchFn: function(searchParam){ 
		StationTrack.searchParam = searchParam ;
        StationTrack.grid.store.load();
	},
	afterSaveSuccessFn: function(result, response, options){
		StationTrack.grid.saveWin.hide();
        StationTrack.grid.store.reload();
        alertSuccess();
    }
});

// 默认排序		
StationTrack.grid.store.setDefaultSort("seqNo", "ASC");


//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:StationTrack.grid });
	
//查询前添加过滤条件
StationTrack.grid.store.on('beforeload' , function(){
		var searchParam = StationTrack.searchParam ;
		searchParam.inspectionIdx = StationTrack.inspectionIdx ;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

});