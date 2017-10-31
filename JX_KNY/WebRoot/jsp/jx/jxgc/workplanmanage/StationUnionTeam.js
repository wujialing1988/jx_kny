/**
 * 工位对应的班组 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('StationUnionTeam');                       //定义命名空间
//选择作业班组列表
StationUnionTeam.orgGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/omOrganizationSelect!orgPageList.action',                 //装载列表数据的请求URL
    storeId:'orgID', storeAutoLoad: false, hideRowNumberer:true, page: false, tbar: null,
    fields: [{
        header:'orgid主键', dataIndex:'orgid', hidden:true
    },{
        header:'部门名称', dataIndex:'orgname'
    },{
        header:'部门序列', dataIndex:'orgseq', hidden: true
    }]
});
StationUnionTeam.orgGrid.store.on("beforeload", function(){
	this.baseParams.workStationIDX = WorkStation.idx;
});
StationUnionTeam.orgGrid.un('rowdblclick', StationUnionTeam.orgGrid.toEditFn, StationUnionTeam.orgGrid);
StationUnionTeam.orgWin = new Ext.Window({
        title:"选择作业班组", width:300, height:350, closeAction:"hide", buttonAlign:'center', maximizable:false, 
        layout:'fit', modal:true, items:StationUnionTeam.orgGrid, 
	    buttons: [{
	        text: "确定", iconCls: "saveIcon", handler: function(){
				if(!$yd.isSelectedRecord(StationUnionTeam.orgGrid, true))    return;
				var orgData = StationUnionTeam.orgGrid.selModel.getSelections();
				var dataAry = new Array();
	            for (var i = 0; i < orgData.length; i++){
	            	var data = {};
	                data.workStationIDX = WorkStation.idx;
	                data.teamOrgId = orgData[ i ].get("orgid");
	                data.teamOrgName = orgData[ i ].get("orgname");
	                data.teamOrgSeq = orgData[ i ].get("orgseq");
	                dataAry.push(data);
	            }
	            if(StationUnionTeam.orgGrid.loadMask) StationUnionTeam.orgGrid.loadMask.show();
	            Ext.Ajax.request({
	                url:  ctx + "/stationUnionTeam!saveOrUpdateList.action",
	                jsonData: dataAry,
	                success: function(response, options){
	                  	if(StationUnionTeam.orgGrid.loadMask) StationUnionTeam.orgGrid.loadMask.hide();
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null && result.success == true) {
	                        alertSuccess();
							StationUnionTeam.grid.store.reload();
							StationUnionTeam.orgWin.hide();
	                    } else {
	                        alertFail(result.errMsg);
	                    }
	                },
	                failure: function(response, options){	
	                	if(StationUnionTeam.orgGrid.loadMask) StationUnionTeam.orgGrid.loadMask.hide();
	                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	                }
               });
				
	        }
	    }, {
	        text: "关闭", iconCls: "closeIcon",handler: function(){ StationUnionTeam.orgWin.hide(); }
	    }]
    });
StationUnionTeam.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/stationUnionTeam!pageList.action',                 //装载列表数据的请求URL
    deleteURL: ctx + '/stationUnionTeam!deleteCascade.action',            //删除数据的请求URL
    storeAutoLoad: false,
    tbar:[{
    	text:"作业班组选择", iconCls:"addIcon",
        handler: function(){ 
        	StationUnionTeam.orgWin.show();
        	StationUnionTeam.orgGrid.store.load();
        	}
    	},'delete',{
	        text: "关闭", iconCls: "closeIcon",handler: function(){ WorkStation.grid.saveWin.hide(); }
	    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'工位主键', dataIndex:'workStationIDX', hidden: true, editor:{  xtype: 'hidden' }
	},{
		header:'工作班组编码', dataIndex:'teamOrgId', hidden: true, editor:{ xtype: 'hidden' }
	},{
		header:'工作班组全名', dataIndex:'teamOrgName'
	},{
		header:'工作班组序列', dataIndex:'teamOrgSeq', hidden: true, editor:{  xtype: 'hidden' }
	}]
});
StationUnionTeam.grid.un('rowdblclick', StationUnionTeam.grid.toEditFn, StationUnionTeam.grid);
StationUnionTeam.grid.store.on("beforeload", function(){
	var searchParam = {};
	searchParam.workStationIDX = WorkStation.idx;
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
});