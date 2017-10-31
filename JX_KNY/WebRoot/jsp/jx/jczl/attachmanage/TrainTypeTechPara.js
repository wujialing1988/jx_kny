/**
 * 车型的技术参数 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainTypeTechPara');                       //定义命名空间
//定义全局变量保存查询条件
TrainTypeTechPara.searchParam = {} ;
//保存前触发函数
TrainTypeTechPara.beforeSaveFn = function(rowEditor, changes, record, rowIndex){
    record.data.trainTypeIDX = TrainType.typeID ; //设置车型主键
    return true;
}
TrainTypeTechPara.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/trainTypeTechPara!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainTypeTechPara!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainTypeTechPara!logicDelete.action',            //删除数据的请求URL
    beforeSaveFn: TrainTypeTechPara.beforeSaveFn,                              //保存记录前触发的函数
//    beforeEditFn: TrainTypeTechPara.beforeEditFn,                              //编辑记录前触发的函数
    tbar: ['search','add','delete'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'技术参数编码', dataIndex:'techParaID', getEditor:function(){  return null; }, searcher:{disabled:true}
	},{
		header:'技术参数名称', dataIndex:'techParaName', editor:{  maxLength:100 , allowBlank:false}
	},{
		header:'技术参数值', dataIndex:'techParaValue', editor:{  maxLength:100 ,allowBlank:false}
	},{
		header:'技术参数描述', dataIndex:'techParaDesc', editor:{  maxLength:1000}
	},{
		header:'录入人主键', dataIndex:'registerPerson', hidden:true
	},{
		header:'录入人名称', dataIndex:'registerPersonName',  hidden:true
	},{
		header:'录入时间', dataIndex:'registerTime',  hidden:true
	},{
		header:'车型主键', dataIndex:'trainTypeIDX',  hidden:true
	}],
	defaultData: {techParaID: "" , trainTypeIDX: "", registerPerson:"" ,registerPersonName:"", registerTime: null},
	addButtonFn: function(){                            //点击新增按钮触发的函数，重构点击新增按钮触发的方法
		Ext.Ajax.request({
	        url: ctx + "/codeRuleConfig!getConfigRule.action",
	        params: {ruleFunction : "JCZL_TRAIN_TYPE_TECH_PARA_TECH_PARA_ID"},
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            TrainTypeTechPara.grid.defaultData.techParaID = result.rule ;
	            TrainTypeTechPara.grid.rowEditor.stopEditing(false); 
		        //点击新增按钮前，操作事件，主要针对默认数据的动态赋值
		        var initData = Ext.apply({}, TrainTypeTechPara.grid.defaultData); 
		        var record = new TrainTypeTechPara.grid.recordConstructor(initData);
		        TrainTypeTechPara.grid.store.insert(0, record); 
		        TrainTypeTechPara.grid.getView().refresh(); 
		        TrainTypeTechPara.grid.getSelectionModel().selectRow(0); 
		        TrainTypeTechPara.grid.rowEditor.startEditing(0);  
	        }
    	});
    },
	searchFn: function(searchParam){ 
		TrainTypeTechPara.searchParam = searchParam ;
        TrainTypeTechPara.grid.store.load();
	}
});
//查询前添加过滤条件
TrainTypeTechPara.grid.store.on('beforeload' , function(){
	var searchParam = TrainTypeTechPara.searchParam ;
	searchParam.trainTypeIDX = TrainType.typeID ;
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});


});