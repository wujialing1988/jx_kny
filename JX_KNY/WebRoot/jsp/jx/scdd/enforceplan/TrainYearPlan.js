/*
 * 机车检修年计划表示层，基于Ext.js构造显示页面，ajax进行数据交互
 * */
Ext.onReady(function(){
Ext.namespace('TrainYearPlan');
TrainYearPlan.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainYearPlan!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainYearPlan!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainYearPlan!logicDelete.action',
    searchFormColNum: 2,            //查询窗口数据展示占两列
    saveFormColNum: 2,              //新增或修改窗口数据展示成两列
    tbar:['search','add','delete','refresh'], //工具栏显示按钮
    fields:[{
        header:'idx主键',dataIndex:'idx',hidden:true,editor:{ xtype:'hidden' }
    },{
        header:'车型主键',dataIndex:'trainTypeIdx',hidden:true,editor:{id:"trainTypeIdx",xtype:'hidden' }
    },{
        header:'车型',dataIndex:'trainTypeShortName',editor:{
					id:"trainType_comb",
					xtype:'Base_combo',
					business: 'trainType',													
					fields:['typeID','shortName','repairType'],
					queryParams: {'isCx':'yes'},
					entity:'com.yunda.jx.base.jcgy.entity.TrainType',
					hiddenName:'trainTypeShortName',
					returnField:[{widgetId:"trainTypeIdx",propertyName:"typeID"}],
					displayField: "shortName", 
					valueField: "shortName",
					pageSize: 20, 
					minListWidth: 200,
					emptyText:"- 请选择 -",
					disabled:false,
					editable:true,
					allowBlank: false
        }
    },{
        header:'修程编码',dataIndex:'repairClassIdx',hidden:true,editor:{id:'repairClassIdx', xtype:'hidden' }
    },{
        header:'修程',dataIndex:'repairClassName',editor:{
        	        id:"rc_comb",
					disabled:false,
					xtype: "Base_combo",
					hiddenName: "repairClassName",
					displayField: "xcName",
					valueField: "xcName",
					business: 'trainRC',
					entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
					fields:['xcID','xcName'],
					emptyText:"- 请选择 -",
					pageSize: 0, 
					minListWidth: 200,
					allowBlank: false,
					returnField: [{widgetId:"repairClassIdx",propertyName:"xcID"}]
        }          
    },{
        header:'年份',dataIndex:'dateYear',editor:
        	{    
        xtype:'numberfield',
        allowDecimals:false,
        allowNegative:false,
        minValue:2016,
        maxValue:9999,
        allowBlank:false
        }
    },{
        header:'年计划（台）',dataIndex:'planCount',editor:{xtype:'hidden'}
    },{
        header:'一季度（台）',dataIndex:'firstQuarter',editor:{xtype:'numberfield',allowDecimals:false,allowNegative:false,allowBlank:false}
    },{
        header:'二季度（台）',dataIndex:'secondQuarter',editor:{xtype:'numberfield',allowDecimals:false,allowNegative:false,allowBlank:false}
    },{
        header:'三季度（台）',dataIndex:'thirdQuarter',editor:{xtype:'numberfield',allowDecimals:false,allowNegative:false,allowBlank:false}
    },{
        header:'四季度（台）',dataIndex:'fourthQuarter',editor:{xtype:'numberfield',allowDecimals:false,allowNegative:false,allowBlank:false}
    },{
        header:'初始值',dataIndex:'initialValue',editor:{xtype:'numberfield',allowDecimals:false,allowNegative:false}
    }
    ],                                                //定义grid数据显示字段
    beforeSaveFn: function(data){ 
    	var a = data.dateYear;
    	var firstQuarter=parseInt(data.firstQuarter);
    	var secondQuarter=parseInt(data.secondQuarter);
    	var thirdQuarter=parseInt(data.thirdQuarter);
    	var fourthQuarter=parseInt(data.fourthQuarter);
    	data.planCount=firstQuarter+secondQuarter+thirdQuarter+fourthQuarter;
    	return true; 
    }  
});
var viewport=new Ext.Viewport({layout:'fit',items:TrainYearPlan.grid});
});