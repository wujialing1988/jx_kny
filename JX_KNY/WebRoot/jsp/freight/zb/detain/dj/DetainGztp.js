/**
 * 扣车管理
 */
Ext.onReady(function(){
	
Ext.namespace('DetainGztp');                       //定义命名空间

DetainGztp.detainIdx = "###" ; // 扣车登记id

//定义全局变量保存查询条件
DetainGztp.searchParam = {} ;

DetainGztp.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/detainGztp!pageQuery.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/detainGztp!delete.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/detainGztp!saveOrUpdate.action',             //保存数据的请求URL
    singleSelect: false, 
    saveFormColNum:1,
    saveForm:DetainGztp.saveForm,
    tbar : ['add','delete'],    
	fields: [
    {
		header:'故障名称', dataIndex:'gztpName',width: 120,editor: { allowBlank:false }
	},{
		header:'故障描述', dataIndex:'gztpDesc',width: 120,editor: {
       		xtype: 'textarea',
       		fieldLabel: '故障描述',
       		allowBlank:false,
       		maxLength:500,
       		height: 60,
       		name: 'gztpDesc'
		}
	},{
		header:'扣车id', dataIndex:'detainIdx',width: 120,hidden:true ,editor: {
			xtype:'hidden'
		}
	},{
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}],
	searchFn: function(searchParam){ 
		DetainGztp.searchParam = searchParam ;
        DetainGztp.grid.store.load();
	},
	beforeShowEditWin: function(record, rowIndex){
		return true ;
	},
	beforeSaveFn: function(data){
		data.detainIdx = DetainGztp.detainIdx ;
		return true; 
	}
});

	
// 查询前添加过滤条件
DetainGztp.grid.store.on('beforeload' , function(){
		var whereList = [];
		whereList.push({ propName: 'detainIdx', propValue:DetainGztp.detainIdx, compare: Condition.EQ, stringLike: false});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

});