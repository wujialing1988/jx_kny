/**
 * 互换配件型号表 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsTypeTrain');                       //定义命名空间
PartsTypeTrain.searchParam = {} ;                    //定义查询条件
PartsTypeTrain.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainTypeToParts!findPartsTypeForRcClass.action',                 //装载列表数据的请求URL
    saveFormColNum: 2,	searchFormColNum: 2,
    tbar:['search',{
    	text:'确认',iconCls:'saveIcon',handler:function(){
    		PartsTypeTrain.submit();
    	}
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型', dataIndex:'trainTypeShortName',width:60, editor:{  maxLength:100 },searcher:{disabled:true}
	},{
		header:'规格型号编码', dataIndex:'specificationModelCode', editor:{  maxLength:100 }
	},{
		header:'规格型号', dataIndex:'specificationModel', editor:{  maxLength:100 }
	},{
		header:'配件名称', dataIndex:'partsName', editor:{  maxLength:100 }
	},{
		header:'物料编码', dataIndex:'matCode', editor:{  maxLength:50 }
	},{
		header:'计量单位', dataIndex:'unit',width:60, editor:{  maxLength:20 },searcher:{disabled:true}
	},{
		header:'业务状态', dataIndex:'status', hidden:true, editor:{ xtype:'numberfield', maxLength:1 }
	},{
		header:'图号', dataIndex:'chartNo',hidden:true,  editor:{  maxLength:50 },searcher:{disabled:true}
	},{
		header:'是否高价配件', dataIndex:'isHighterPricedParts',width:70, editor:{ xtype:'numberfield', maxLength:1 },
		renderer:function(v){
			switch(v){
				case isHeight:
					return "是";
				case noHeight:
					return "否";
				default:
					return v;
			}
		},searcher:{disabled:true}
	},{	
		header:'是否带序列号', dataIndex:'isHasSeq',hidden:true,  editor:{ xtype:'numberfield', maxLength:1 }
	}],
	searchFn: function(searchParam){ 
		PartsTypeTrain.searchParam = searchParam ;
        this.store.load();
	}
});

//取消编辑监听
PartsTypeTrain.grid.un('rowdblclick', PartsTypeTrain.grid.toEditFn, PartsTypeTrain.grid); 

PartsTypeTrain.selectWin = new Ext.Window({
	title:"规格型号选择", maximizable:true,width:700, height:325, closeAction:"hide", modal:true, 
	layout:"fit", items:PartsTypeTrain.grid
});
//确认提交方法，后面可覆盖此方法完成查询
PartsTypeTrain.submit = function(){alert("请覆盖，TechReformProject.submit 方法完成自己操作业务！");};

});