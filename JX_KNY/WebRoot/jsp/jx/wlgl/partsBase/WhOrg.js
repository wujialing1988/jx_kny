/**
 * 库房与班组关系维护 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('WhOrg');                       //定义命名空间
WhOrg.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/whOrg!findWhOrgList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/whOrg!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/whOrg!logicDelete.action',            //删除数据的请求URL
    storeId:'whIdx',
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'whIdx', dataIndex:'whIdx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'库房名称', dataIndex:'wareHouseName', editor:{  maxLength:50 }
	},{
		header:'机构名称', dataIndex:'orgname', editor:{ maxLength:10 }
	},{
		header:'维护日期', dataIndex:'updateTime',xtype:'datecolumn', editor:{maxLength:10 }
	}],
	tbar: [{
        text:"设置", iconCls:"addIcon", handler: function(){
        	var sm = WhOrg.grid.getSelectionModel();
            if (sm.getCount() < 1) {
                MyExt.Msg.alert("尚未选择一条记录！");
                return;
            }
        	WhOrg.selectWin.show();
            org.grid.store.load();
            Ext.getCmp("org_orgname").setValue("");
        }        
   		},{text:"取消设置", iconCls:"deleteIcon", handler: function(){
   				var sm = WhOrg.grid.getSelectionModel();
			    if (sm.getCount() < 1) {
			        MyExt.Msg.alert("尚未选择一条记录！");
			        return;
			    }
			    var data = sm.getSelections();
			    var ids = new Array();
			    for (var i = 0; i < data.length; i++){
			    	if(data[ i ].get("idx") == "" || data[i].get("idx") == null){
			    		MyExt.Msg.alert("库房【"+data[i].get("wareHouseName")+"】没有维护库房与班组的关系，不能取消设置！");
			        	return;
			    	}
		    		ids.push(data[ i ].get("idx"));
			    }
				Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
			        if(btn == 'yes') {
				        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
				        	scope: WhOrg.grid,
				        	url: WhOrg.grid.deleteURL,
							params: {ids: ids}
				        }));
				        WhOrg.grid.store.load();
			        }
			    }); 
   		}}]
});
//移除列表的侦听器
WhOrg.grid.un('rowdblclick', WhOrg.grid.toEditFn, WhOrg.grid);
//选择组织机构窗口
WhOrg.selectWin = new Ext.Window({
    title: "组织机构选择", layout: "fit", width: 500, height: 360, modal: true, 
    closeAction: "hide", items: org.grid,layout: "fit"
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:WhOrg.grid });
});