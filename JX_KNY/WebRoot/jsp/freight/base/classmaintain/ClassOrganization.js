/**
 * 班次班组维护
 */
Ext.onReady(function(){
	
Ext.namespace('ClassOrganization');                       //定义命名空间

// 班次
ClassOrganization.classIdx = "###" ; // 班次
ClassOrganization.workplaceCode = "###" ; // 站点

//定义全局变量保存查询条件
ClassOrganization.searchParam = {} ;
ClassOrganization.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/classOrganization!pageList.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/classOrganization!delete.action',            //删除数据的请求URL
    singleSelect: false, saveFormColNum:1,
    labelWidth: 60,                                     //查询表单中的标签宽度
    fieldWidth: 130,
    isEdit: false,
    page: false,
	tbar : [
	{
		text : '选择班组',		
		disabled:false,
		iconCls:"chart_organisationIcon",
    	handler: function(){
    		if(ClassOrganization.classIdx == "###"){
    			MyExt.Msg.alert("请先选择班次！");
				return;
    		}
    		ClassOrganizationSelectWin.whereList = [] ;
    		var sqlStr = "orgid in (select orgid from JXPZ_WorkPlace_TO_ORG where workplace_code = '"+ClassOrganization.workplaceCode+"')" ;
    		sqlStr += " and orgid not in (select orgid from K_CLASS_ORGANIZATION where CLASS_IDX = '"+ClassOrganization.classIdx+"')" ;
    		ClassOrganizationSelectWin.whereList.push({sql: sqlStr, compare:Condition.SQL}); //排除已选择过的数据
    		ClassOrganizationSelectWin.orgdegree = "tream" ;
    		ClassOrganizationSelectWin.grid.store.reload();
    		ClassOrganizationSelectWin.selectWin.show();
    	}
	},'-','delete'
	],
	fields: [
     	{
		header:'班次主键', dataIndex:'classIdx',hidden:true,width: 120,editor: {}
	},
     	{
		header:'班组ID', dataIndex:'orgid',width: 120,hidden:true,editor: {}
	},
     	{
		header:'班组名称', dataIndex:'orgname',width: 120,editor: {}
	},
     	{
		header:'班组序列', dataIndex:'orgseq',width: 120,hidden:true,editor: {}
	},
		{
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}],
	searchFn: function(searchParam){ 
		ClassOrganization.searchParam = searchParam ;
        ClassOrganization.grid.store.load();
	},
	beforeShowEditWin: function(record, rowIndex){ 
		return false ;
	}	
});


/**
 * 双击事件 
 */
ClassOrganization.grid.on("rowclick", function(grid, rowIndex, e){
		var records = ClassOrganization.grid.getSelectionModel().getSelections();
		ClassOrganizationUser.orgIdx = records[0].data.orgid ; // 班组
		ClassOrganizationUser.classOrgIdx = records[0].data.classIdx ; // 班组班次对应ID
		ClassOrganizationUser.grid.getStore().reload();
});

// 添加加载结束事件
ClassOrganization.grid.getStore().addListener('load',function(me, records, options ){
	if(records.length > 0){
		ClassOrganization.grid.getSelectionModel().selectFirstRow();
		var records = ClassOrganization.grid.getSelectionModel().getSelections();
		ClassOrganizationUser.orgIdx = records[0].data.orgid ; // 班组
		ClassOrganizationUser.classOrgIdx = records[0].data.classIdx ; // 班组班次对应ID
		ClassOrganizationUser.grid.getStore().reload();
	}else{
		ClassOrganizationUser.orgIdx = "" ; // 班组
		ClassOrganizationUser.classOrgIdx = "" ; // 班组班次对应ID
		ClassOrganizationUser.grid.getStore().reload();
	}
});

// 班组选择方法
ClassOrganizationSelectWin.submit = function(){
	var org = ClassOrganizationSelectWin.grid.selModel.getSelections();
	if(org.length == 0){
		MyExt.Msg.alert("尚未选择一条记录！")
		return;
	}
	var datas = new Array();
	for(var i=0;i<org.length;i++){
		var data = {} ;
		data.orgid = org[i].get("orgid");
		data.orgseq = org[i].get("orgseq");
		data.orgname = org[i].get("orgname");
		data.classIdx = ClassOrganization.classIdx ;
		datas.push(data);
	}
	ClassOrganization.grid.loadMask.show();
	Ext.Ajax.request({
		url: ctx + "/classOrganization!saveOrganizations.action",
		jsonData: datas,
		success: function(response, options){
			ClassOrganization.grid.loadMask.hide();
			var result = Ext.util.JSON.decode(response.responseText);
			if(result.success){
				alertSuccess();
				ClassOrganization.grid.store.reload();
				ClassOrganizationSelectWin.selectWin.hide();
			}else{
				alertFail(result.errMsg);
			}
		},
		failure: function(response, options){
			ClassOrganization.grid.loadMask.hide();
			MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		}
	});
}

//查询前添加过滤条件
ClassOrganization.grid.store.on('beforeload' , function(){
		var searchParam = ClassOrganization.searchParam ;
		searchParam.classIdx = ClassOrganization.classIdx ;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

});