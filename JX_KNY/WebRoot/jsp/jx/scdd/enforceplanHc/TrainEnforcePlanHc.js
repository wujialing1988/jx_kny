/**
 * 货车检修计划
 */
Ext.onReady(function(){
	
Ext.namespace('TrainEnforcePlanHc');                       //定义命名空间

//定义全局变量保存查询条件
TrainEnforcePlanHc.searchParam = {} ;
TrainEnforcePlanHc.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainEnforcePlanHc!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainEnforcePlanHc!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainEnforcePlanHc!logicDelete.action',            //删除数据的请求URL
    singleSelect: true, saveFormColNum:1,
    tbar:[
          {
        	  width: 30, xtype: 'label', text: '年份：', style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
          },'-',{
				id: 'plan_yearSearch_combo', xtype: 'combo',
				width: 90,
		        store: function() {
		        	var data = [];
		        	var year = new Date().getFullYear();
		        	data.push([year + 1, year + 1]);			// 下一年
		        	for (var i = 0; i < 2; i++) {
		        		data.push([year - i, year - i]);
		        	}
		        	return new Ext.data.SimpleStore({
					    fields: ['k', 'v'],
						data: data
					})
		        }(),
				valueField:'k', displayField:'v',
				editable: false,
				triggerAction:'all',
				mode:'local',
				listeners: {
					select: function( combo, record, index ) {
						TrainEnforcePlanHc.grid.store.load();
					}
				}
			},'-','add','-','delete','-','refresh'
    ], 
	fields: [
    {
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" },searcher: { hidden: true }
	},{
		header:'年份',dataIndex:'planYear',editor:{
			hiddenName: 'planYear', id: 'plan_year_combo', xtype: 'combo',
			allowBlank:false,
	        store: function() {
	        	var data = [];
	        	var year = new Date().getFullYear();
	        	data.push([year + 1, (year + 1) + '年']);			// 下一年
	        	for (var i = 0; i < 2; i++) {
	        		data.push([year - i, (year - i) + '年']);
	        	}
	        	return new Ext.data.SimpleStore({
				    fields: ['k', 'v'],
					data: data
				})
	        }(),
			valueField:'k', displayField:'v',
			value: new Date().getFullYear(),
			editable: false,
			triggerAction:'all',
			mode:'local'
		},renderer: function(value, metaData, record) {
			return value + '年' ;
		}
	},{
		header:'月份',dataIndex:'planMorth',editor:{
			hiddenName: 'planMorth', id: 'plan_month_combo', xtype: 'combo',
			allowBlank:false,
			store: function() {
				var data = [];
				var year = new Date().getFullYear();
				for (var i = 1; i <= 12; i++) {
					data.push([i, i+'月']);
				}
				return new Ext.data.SimpleStore({
					fields: ['k', 'v'],
					data: data
				})
			}(),
			valueField:'k', displayField:'v',
			value: new Date().getMonth() + 1,
			editable: false,
			triggerAction:'all',
			mode:'local'
		},renderer: function(value, metaData, record) {
			return value + '月' ;
		}
	}],
	searchFn: function(searchParam){ 
		TrainEnforcePlanHc.searchParam = searchParam ;
        TrainEnforcePlanHc.grid.store.load();
	},
	afterSaveSuccessFn: function(result, response, options){
		TrainEnforcePlanHc.grid.saveWin.hide();
        TrainEnforcePlanHc.grid.store.reload();
        alertSuccess();
    }	
});



//页面自适应布局
var viewport = new Ext.Viewport({
			layout:'fit',
			items:[{
			layout: 'border',
			items: [{
	        region: 'center', bodyBorder: false,title: '计划详情',
	        layout: 'fit', items : [ TrainEnforcePlanDetailHc.grid ]
	    	},{ 		
		     	 region: 'west', layout: 'fit',  title: '货车月检修计划',  bodyBorder: false, split: true,width: 400,minSize: 400, maxSize: 800, 
		     	 collapsible : true,   items:[TrainEnforcePlanHc.grid]
			}]
		}] 
});


	
//查询前添加过滤条件
TrainEnforcePlanHc.grid.store.on('beforeload' , function(){
		var searchParam = TrainEnforcePlanHc.searchParam ;
		var planYear = Ext.getCmp('plan_yearSearch_combo').getValue();
		searchParam.planYear = planYear ;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

// 添加加载结束事件
TrainEnforcePlanHc.grid.getStore().addListener('load',function(me, records, options ){
	if(records.length > 0){
		TrainEnforcePlanHc.grid.getSelectionModel().selectFirstRow();
		TrainEnforcePlanHc.loadDetails();
	}else{
		TrainEnforcePlanDetailHc.planIdx = "###"; ;
		TrainEnforcePlanDetailHc.grid.getStore().reload();	
	}
});
	
/**
 * 单击刷新基本信息页面和车辆列表
 */
TrainEnforcePlanHc.grid.on("rowclick", function(grid, rowIndex, e){
	TrainEnforcePlanHc.loadDetails();
});

// 刷新详细信息
TrainEnforcePlanHc.loadDetails = function(){
    var sm = TrainEnforcePlanHc.grid.getSelectionModel();
   	var records = sm.getSelections();
   	TrainEnforcePlanDetailHc.planIdx = records[0].data.idx ;
   	TrainEnforcePlanDetailHc.grid.getStore().reload();		
}

});