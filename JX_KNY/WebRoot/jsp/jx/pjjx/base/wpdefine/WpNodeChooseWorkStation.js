/**
 * 配件检修工位 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsWorkStation');                       //定义命名空间
PartsWorkStation.searchParam = {};
PartsWorkStation.repairLineIdx = "" ;//流水线主键
PartsWorkStation.repairLineName = "" ;//流水线名称
PartsWorkStation.workStationName = "" ;//工位名称
PartsWorkStation.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsWorkStation!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsWorkStation!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsWorkStation!logicDelete.action',            //删除数据的请求URL
    storeAutoLoad: false,
    tbar : ['工位名称',{	            
            xtype:"textfield",								                
            name : "workStationName",
            id:"workStationNameId"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				PartsWorkStation.workStationName = Ext.getCmp("workStationNameId").getValue();				
				PartsWorkStation.grid.getStore().load();
				
			},
			title : "按输入框条件查询",
			scope : this
		}],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'工位编码', dataIndex:'workStationCode', width:80, editor:{  maxLength:50 }
	},{
		header:'工位名称', dataIndex:'workStationName',width:100,  editor:{  maxLength:100 }
	},{
		header:'流水线主键', dataIndex:'repairLineIdx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'流水线名称', dataIndex:'repairLineName'
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 }
	}]
});
PartsWorkStation.grid.un('rowdblclick', PartsWorkStation.grid.toEditFn, PartsWorkStation.grid);
PartsWorkStation.grid.store.on('beforeload',function(){
	     var sqlStr = " idx not in (select Work_Station_IDX from PJJX_WP_Node_Station_Def where Record_Status=0 and Node_IDX='"+WpNodeStationDef.nodeIDX+"')";
		 var whereList = [
		          		{sql: sqlStr, compare: Condition.SQL} 
					]
		if(PartsWorkStation.workStationName){
			whereList.push({propName:'workStationName',propValue:PartsWorkStation.workStationName});
		}
	    this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
	});
//定义选择窗口
PartsWorkStation.selectWin = new Ext.Window({
	title:"工位选择", width:600, height:400, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
	maximizable:false, items:[ PartsWorkStation.grid ], modal:true,
	buttons: [{
		text : "确定",iconCls : "saveIcon", handler: function(){
			PartsWorkStation.submit(); 
		}
	},{
        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ PartsWorkStation.selectWin.hide(); }
	}]
});
//工位选择提交方法
PartsWorkStation.submit = function(){
	var station = PartsWorkStation.grid.selModel.getSelections();
	if(station.length == 0){
		MyExt.Msg.alert("尚未选择一条记录！")
		return;
	}
	var datas = new Array();
	for(var i=0;i<station.length;i++){
		var data = {} ;
		data.nodeIDX = WpNodeStationDef.nodeIDX ;
		data.workStationIDX = station[i].get("idx");
		datas.push(data);
	}
	PartsWorkStation.grid.loadMask.show();
	Ext.Ajax.request({
		url: ctx + "/wpNodeStationDef!saveStationDefs.action",
		jsonData: datas,
		success: function(response, options){
			PartsWorkStation.grid.loadMask.hide();
			var result = Ext.util.JSON.decode(response.responseText);
			if(result.success){
				alertSuccess();
				WpNodeStationDef.grid.store.load();
				PartsWorkStation.selectWin.hide();
			}else{
				alertFail(result.errMsg);
			}
		},
		failure: function(response, options){
			PartsWorkStation.grid.loadMask.hide();
			MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		}
	});
}
});