/**
 * 互换配件型号表 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsType');                       //定义命名空间
PartsType.searchParam = {} ;                    //定义查询条件
PartsType.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsType!pageQuery.action',                 //装载列表数据的请求URL
    saveFormColNum: 2,	searchFormColNum: 2,
    storeAutoLoad:false,
    tbar:['search',{
    	text:'确认',iconCls:'saveIcon',handler:function(){
    		PartsType.submit();
    	}
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
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
	},{	
		header:'状态', dataIndex:'status', editor:{ hidden:true },
		renderer:function(v){
		   if(v==0) return "新增";
		   if(v==1) return "启用";
		   if(v==2) return "作废";
		}
	}],
	searchFn: function(searchParam){ 
		PartsType.searchParam = searchParam ;
        this.store.load();
	}
});

//取消编辑监听
PartsType.grid.un('rowdblclick', PartsType.grid.toEditFn, PartsType.grid); 

PartsType.selectWin = new Ext.Window({
	title:"规格型号选择", maximizable:true,width:700, height:325, closeAction:"hide", modal:true, 
	layout:"fit", items:PartsType.grid
});
//确认提交方法，后面可覆盖此方法完成查询
PartsType.submit = function(){alert("请覆盖，TechReformProject.submit 方法完成自己操作业务！");};

});