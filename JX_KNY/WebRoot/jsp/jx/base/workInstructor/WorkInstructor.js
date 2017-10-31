/**
 * 作业指导书
 */
Ext.onReady(function(){
	
Ext.namespace('WorkInstructor');                       //定义命名空间

//定义全局变量保存查询条件
WorkInstructor.searchParam = {} ;
WorkInstructor.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/workInstructor!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/workInstructor!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/workInstructor!delete.action',            //删除数据的请求URL
    singleSelect: false, saveFormColNum:1,
    labelWidth: 90,                                     //查询表单中的标签宽度
    fieldWidth: 180,
	fields: [
     	{
		header:'标题名', dataIndex:'title',width: 120,editor: {
				maxLength:500,
				name:"title",
				allowBlank:false
		}
	},
     	{
		header:'开始页码', dataIndex:'startPage',width: 120,editor: {
				fieldLabel:'开始页码',
		        xtype:'numberfield',
		        allowDecimals:false,
		        allowNegative:false,
		        minValue:1,
		        maxValue:99999,
		        allowBlank:false
		},searcher : {hidden:true}
	},
     	{
		header:'结束页码', dataIndex:'endPage',width: 120,editor: {
				fieldLabel:'结束页码',
		        xtype:'numberfield',
		        allowDecimals:false,
		        allowNegative:false,
		        minValue:1,
		        maxValue:99999,
		        allowBlank:false
		},searcher : {hidden:true}
	},
     	{
		header:'排序', dataIndex:'seqNo',width: 120,editor: {
				id:"seq_no",
				fieldLabel:'排序',
		        xtype:'numberfield',
		        allowDecimals:false,
		        allowNegative:false,
		        minValue:1,
		        maxValue:99,
		        allowBlank:false
			},searcher : {hidden:true}
	},{
		header:'概要内容', dataIndex:'content',width: 120,editor: {
				fieldLabel:"概要内容", 
				maxLength:200,
				xtype:"textarea",
				name:"content"
		},searcher : {hidden:true}
	},
		{
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}],
	searchFn: function(searchParam){ 
		WorkInstructor.searchParam = searchParam ;
        WorkInstructor.grid.store.load();
	},
	afterShowSaveWin: function(){
		var count = WorkInstructor.grid.getStore().getCount();
		if(Ext.isEmpty(Ext.getCmp('seq_no').getValue())){
			Ext.getCmp('seq_no').setValue(count+1);
		}
	},
    /**
     * 保存记录之前的触发动作，如果返回fasle将不保存记录，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * @param {Object} data 要保存的数据记录，json格式
     * @return {Boolean} 如果返回fasle将不保存记录
     */
    beforeSaveFn: function(data){ 
    	if(parseInt(data.endPage) < parseInt(data.startPage)){
    		MyExt.Msg.alert("结束页码需大于开始页码！");
    		return false ;
    	}
    	return true; 
    },
    afterSaveSuccessFn: function(result, response, options){
    	WorkInstructor.grid.saveWin.hide();
        WorkInstructor.grid.store.reload();
        alertSuccess();
    }    
});

// 默认排序		
WorkInstructor.grid.store.setDefaultSort("seqNo", "ASC");

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:WorkInstructor.grid });
	
//查询前添加过滤条件
WorkInstructor.grid.store.on('beforeload' , function(){
		var searchParam = WorkInstructor.searchParam ;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

});