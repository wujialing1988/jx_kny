/**
 * Jcgy_train_type 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainType');                       //定义命名空间
TrainType.typeID = "" ;                              //车型主键
//定义全局变量保存查询条件
TrainType.searchParam = {} ;
TrainType.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainType!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainType!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainType!logicDelete.action',            //删除数据的请求URL
    singleSelect: true, saveFormColNum:3,
    labelWidth: 60,                                     //查询表单中的标签宽度
    fieldWidth: 130,
    tbar: ['search','refresh','-',{
		text: "双击行设置技术参数" , xtype: "label"
    }],
	fields: [{
		header:'车型代码', dataIndex:'typeID' ,searcher: { hidden: true }
	},{
		header:'车型名称', dataIndex:'typeName', editor:{   }
	},{
		header:'简称', dataIndex:'shortName', editor:{   }
	},{
		header:'拼音码', dataIndex:'spell', editor:{ hidden: true  }
	},{
		header:'别名', dataIndex:'alias', editor:{ hidden: true  }
	},{
		header:'小辅修公里', dataIndex:'kmlightRepair', editor:{ hidden: true }
	},{
		header:'功率系数', dataIndex:'powerQuotiety', editor:{ hidden: true  }
	}],
	afterShowEditWin: function(record, rowIndex){  //双击查看显示之后编辑页面之后的操作
		  TrainType.typeID = record.get("typeID");  //获取车型主键（车型代码）
		  this.saveWin.setTitle('技术参数设置');
		  this.disableAllColumns();
		  TrainTypeTechPara.grid.store.load();
	},
	searchFn: function(searchParam){ 
		TrainType.searchParam = searchParam ;
        TrainType.grid.store.load();
	}
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:TrainType.grid });
//移除侦听器
//TrainType.grid.un('rowdblclick', TrainType.grid.toEditFn, TrainType.grid);
//覆盖创建的窗口方法
TrainType.grid.createSaveWin = function(){
    if(TrainType.grid.saveForm == null) TrainType.grid.createSaveForm();
	TrainType.grid.saveWin = new Ext.Window({
		title: "技术参数设置", maximizable:false,maximized:true, layout: "fit", 
		closeAction: "hide", modal: true, buttonAlign: "center",
		items: {
			xtype: "panel", layout: "border",
			items:[{
	            region: 'north', layout: "fit",frame:true, height: 65, bodyBorder: false,items:[TrainType.grid.saveForm]
	        },{
	            region : 'center', layout : 'fit', bodyBorder: false, items : [ TrainTypeTechPara.grid ]
	        }]
		},
		buttons:[{
			text: "关闭", iconCls: "closeIcon", handler: function(){TrainType.grid.saveWin.hide();}
		}]
	});
}
	//查询前添加过滤条件
	TrainType.grid.store.on('beforeload' , function(){
		var searchParam = TrainType.searchParam ;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});

});