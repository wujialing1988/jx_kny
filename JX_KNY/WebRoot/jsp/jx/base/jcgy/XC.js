/**
 * Jcgy_xc 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('XC');                       //定义命名空间
XC.searchParam = {} ;					   // 查询条件
XC.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/xC!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/xC!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/xC!logicDelete.action',            //删除数据的请求URL
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'修程编码', dataIndex:'xcID', editor:{   }
	},{
		header:'修程名称', dataIndex:'xcName', editor:{   }
	},{
		header:'客货类型', dataIndex:'vehicleType', hidden:true, editor:{xtype:'hidden', value:vehicleType}
	}
	]
});

//查询前添加过滤条件
XC.grid.store.on('beforeload' , function(){
		var whereList = [] ;
		whereList.push({propName:"vehicleType", propValue: vehicleType, stringLike: false}) ;
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:XC.grid });
});