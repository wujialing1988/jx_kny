/**
 * 机车履历 主要技术参数
 */
Ext.onReady(function(){
	
	Ext.namespace('TrainMainTechParam');  
	TrainMainTechParam.searchParam = {};
	TrainMainTechParam.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/trainTypeTechPara!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/trainTypeTechPara!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/trainTypeTechPara!logicDelete.action',            //删除数据的请求URL
	    saveFormColNum:2,  searchFormColNum:1,
	    viewConfig: null,  singleSelect:true,
	    tbar: ['search', 'refresh'],
	    fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'技术参数编码', dataIndex:'techParaID', width:0,editor:{},searcher:{disabled:true}
		},{
			header:'技术参数名称', dataIndex:'techParaName', width:200, editor:{  maxLength:100 , allowBlank:false, vtype:"validChar"}
		},{
			header:'技术参数值', dataIndex:'techParaValue', width:150, editor:{  maxLength:100 ,allowBlank:false, vtype:"validChar"}
		},{
			header:'技术参数描述', dataIndex:'techParaDesc', width:350, editor:{  maxLength:1000 , vtype:"validChar"}
		},{
			header:'录入人主键', dataIndex:'registerPerson', hidden:true
		},{
			header:'录入人名称', dataIndex:'registerPersonName',  hidden:true
		},{
			header:'录入时间', dataIndex:'registerTime',  hidden:true
		},{
			header:'车型主键', dataIndex:'trainTypeIDX',  hidden:true,
			searcher:{ xtype:'textfield', value:TrainMainTechParam.trainTypeIdx }
		}],
		searchFn: function(searchParam){ 
			TrainMainTechParam.searchParam = searchParam ;
			TrainMainTechParam.grid.store.load();
		}
	});
	//移除侦听
	TrainMainTechParam.grid.un('rowdblclick', TrainMainTechParam.grid.toEditFn, TrainMainTechParam.grid);
	//查询前添加过滤条件
	TrainMainTechParam.grid.store.on('beforeload' , function(){		
		var searchParam = TrainMainTechParam.searchParam ;
		searchParam.trainTypeIDX = JczlTrain.trainTypeIdx;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
});