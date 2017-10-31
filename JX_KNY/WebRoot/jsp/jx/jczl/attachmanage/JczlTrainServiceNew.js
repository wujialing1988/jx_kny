/**
 * 机车信息维护 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('JczlTrainService');                       //定义命名空间
JczlTrainService.searchParam = {} ;
JczlTrainService.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/jczlTrain!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/jczlTrain!saveOrUpdateTransfer.action',             //保存数据的请求URL
    deleteURL: ctx + '/jczlTrain!logicDelete.action',            //删除数据的请求URL
    saveFormColNum: 2 , searchFormColNum:2,
//    viewConfig: null,    
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', hidden:true, editor:{
			fieldLabel: "车辆车型", id:"trainType_comb", 
			allowBlank:false ,
			hiddenName: "trainTypeIDX",
			xtype: "Base_combo",
		    business: 'trainVehicleType',
		    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
            fields:['idx','typeName','typeCode'],
            queryParams: {'vehicleType':vehicleType},// 根据车辆类型查询所对应的车辆种类
		    displayField: "typeCode", valueField: "idx",
		    returnField: [{widgetId:"trainTypeShortNameId",propertyName:"typeCode"}],
            pageSize: 0, minListWidth: 150,
            editable:false,
			listeners : {   
                "select" : function() {   
                	
                }   
            }
		}
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{ 			
			id:'trainTypeShortNameId', xtype:"hidden"
		},searcher:{}
	},{
		header:'车号', dataIndex:'trainNo', editor:{ minLength:4 , maxLength:5 , allowBlank: false},
		searcher: {xtype: 'textfield'}
	},{
		header:'制造厂家主键', dataIndex:'makeFactoryIDX',hidden: true,  editor:{ 
			 id:"Factory_Select",xtype:'GyjcFactory_SelectWin',fieldLabel: "制造厂家",
			 hiddenName:"makeFactoryIDX",editable:false,
			 queryHql:"From GyjcFactory where fcID='A'", 
			 returnField:[{widgetId:"makeFactoryNameId",propertyName:"fName"}]
		}
	},{
		header:'制造厂家', dataIndex:'makeFactoryName',  editor:{id:"makeFactoryNameId", xtype:'hidden' }
	},{
		header:'出厂日期', dataIndex:'leaveDate', xtype:'datecolumn', 
		editor:{ xtype:'my97date',  my97cfg: {dateFmt:'yyyy-MM-dd'}, initNow: false,allowBlank:false  },
		searcher:{disabled: true}
	},{
		header:'登记人', dataIndex:'registerPersonName',
		editor:{xtype: "hidden"},searcher:{disabled: true}
	},{
		header:'登记时间', dataIndex:'registerTime', xtype:'datecolumn',
		editor:{xtype: "hidden"},searcher:{disabled: true}
	},{
		header:'车辆状态', dataIndex:'trainState',renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			var trainState = record.get('trainState');
			if(trainState == 10){
				return "检修";
			}else if(trainState == 20){
				return "运用";
			}else if(trainState == 30){
				return "列检";
			}else if(trainState == 40){
				return "扣车";				
			}
		},
		editor:{xtype: "hidden"},searcher:{disabled: true}
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 },
		searcher:{ disabled:true }
	},{
		header:'客货类型', dataIndex:'vehicleType',hidden:true, editor: { xtype:"hidden",value:vehicleType }
	}],
	searchFn: function(searchParam){ 
		JczlTrainService.searchParam = searchParam ;
        this.store.load();
	},
	/**
     * 选中记录加载到编辑表单，显示编辑窗口后触发该函数
     * 该函数依赖toEditFn，若默认toEditFn被覆盖则失效
     * @param {Ext.data.Record} record 当前选中记录
     * @param {Number} rowIndex 选中行下标
     */
    afterShowEditWin: function(record, rowIndex){    	
    	Ext.getCmp("trainType_comb").setDisplayValue(record.get("trainTypeIDX"),record.get("trainTypeShortName"));
    	Ext.getCmp("Factory_Select").setDisplayValue(record.get("makeFactoryIDX"),record.get("makeFactoryName"));
    },
    
     /**
     * 保存记录之前的触发动作，如果返回fasle将不保存记录，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * @param {Object} data 要保存的数据记录，json格式
     * @return {Boolean} 如果返回fasle将不保存记录
     */
    beforeSaveFn: function(data){ 
    	delete data.registerTime;
    	delete data.bShortName;
    	delete data.dShortName;
    	return true; 
    }
});
//查询前添加过滤条件
JczlTrainService.grid.store.on('beforeload' , function(){
	var searchParam = JczlTrainService.searchParam;
	searchParam.vehicleType = vehicleType ;
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:JczlTrainService.grid });

});