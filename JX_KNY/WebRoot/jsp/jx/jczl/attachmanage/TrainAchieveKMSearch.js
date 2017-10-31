/**
 * 机车走行公里 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 * 引用此JS文件需在页面上定义变量search=false，履历引用此JS文件search=true。
 */
Ext.onReady(function(){
Ext.namespace('TrainAchieveKM');                       //定义命名空间

TrainAchieveKM.searchParam = {};

TrainAchieveKM.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainAchieveKM!searchPageList.action?type=type',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainAchieveKM!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainAchieveKM!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:2, searchFormColNum:2,
    viewConfig: null,singleSelect:true,
    tbar: ["refresh"],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'trainTypeIDX', dataIndex:'trainTypeIDX', hidden:true, width:0, 
		editor:{
			allowBlank:false ,
			id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "车型",
			hiddenName: "trainTypeIDX", 
			returnField: [{widgetId:"trainTypeShortName",propertyName:"shortName"}],
			displayField: "shortName", valueField: "typeID",
			pageSize: 0, minListWidth: 200, 
			width: TrainAchieveKM.fieldWidth,
			editable:true,
			listeners : {   
	        	"select" : function() {   
	            	//重新加载车号下拉数据
	                var trainNo_comb = Ext.getCmp("trainNo_comb");   
	                trainNo_comb.reset();  
	                Ext.getCmp("trainNo_comb").clearValue(); 
	                trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
	                trainNo_comb.cascadeStore();	                
	        	}   
	    	}
		},
		searchor : { xtype:'textfield' }

	},{
		header:'车型', dataIndex:'trainTypeShortName', hidden:true, editor:{ 
			xtype:"hidden"
		}
	},{
		header:'车号', dataIndex:'trainNo', hidden:true,
		editor:{ 
			allowBlank:false ,
			id:"trainNo_comb",xtype: "TrainNo_combo",	fieldLabel: "车号",
			hiddenName: "trainNo", 
			displayField: "trainNo", valueField: "trainNo",
			pageSize: 20, minListWidth: 200, 
			width: TrainAchieveKM.fieldWidth
		}
	},{
		header:'走行年份', dataIndex:'achieveRegion', sortable:false, editor:{  maxLength:100 },searcher:{disabled:true }
	},{
		header:'使用别', dataIndex:'trainUseName', sortable:false, 
		editor:{
			id:"train_Use_KM",
			xtype: 'Base_combo',
			hiddenName: 'trainUse',
			entity:'com.yunda.jx.base.jcgy.entity.TrainUse',			
			fields: ["useID","useName"],displayField:'useName',valueField:'useID'
		}
	},{
		header:'年走行公里(KM)', dataIndex:'achieveTimeKM', width:150, sortable:false, 
		editor:{
			xtype:'numberfield',
			maxLength:6,
			validator:function(v){
				if(v < 0){
					return "走行公里不能为负数";
				}
			} 
		},searcher:{disabled:true }
	},{
		header:'累积走行公里(KM)', dataIndex:'achieveKM', width:150, sortable:false,
		editor:{
			xtype:'numberfield'			
		}
	}],	
	afterShowEditWin : function(record, rowIndex){
		//车型
		Ext.getCmp("trainType_comb").setDisplayValue(record.get("trainTypeIDX"),record.get("trainTypeShortName"));
		//车号
		Ext.getCmp("trainNo_comb").setDisplayValue(record.get("trainNo"),record.get("trainNo"));
		Ext.getCmp("train_Use_KM").setDisplayValue(record.get('trainUse'), record.get("trainUseName"));
		
		TrainAchieveKM.grid.saveWin.setTitle('查看');
		TrainAchieveKM.grid.saveWin.buttons[0].setVisible(false);
		TrainAchieveKM.grid.disableAllColumns();	
	}
});
//查询前添加过滤条件
TrainAchieveKM.grid.store.on('beforeload' , function(){		
	var searchParam = TrainAchieveKM.searchParam;
	searchParam.trainTypeIDX = JczlTrain.trainTypeIdx;
	searchParam.trainNo = JczlTrain.trainNo;
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
TrainAchieveKM.grid.un('rowdblclick', TrainAchieveKM.grid.toEditFn, TrainAchieveKM.grid);
if(!search){
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:TrainAchieveKM.grid });
}
});