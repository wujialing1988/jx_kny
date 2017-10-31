/**
 * 组织机构 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('org');                       //定义命名空间
//查询参数对象
org.searchParam = {};
org.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/omOrganizationSelect!pageQuery.action',                 //装载列表数据的请求URL
    storeAutoLoad: false,singleSelect:true,
	fields: [{
		header:'机构id', dataIndex:'orgid', hidden:true
	},{
		header:'机构编码', dataIndex:'orgcode', editor:{  maxLength:2 }
	},{
		header:'机构名称', dataIndex:'orgname', editor:{  maxLength:40 }
	}],
	tbar: [{
        xtype:"label", text:"机构名称： " 
    },{
        xtype: "textfield",id:"org_orgname",name:"orgname"
    },{
        text:"查询", iconCls:"searchIcon", handler: function(){
//            var orgname = Ext.get("org_orgname").getValue();
//		    org.searchParam = {'orgname':orgname};
//		    org.grid.store.on('beforeload',function(){
//				this.baseParams.entityJson = Ext.util.JSON.encode(org.searchParam); 
//			});
			org.grid.store.load();
        }        
    },"-",{
        text:"确定", iconCls:"saveIcon", handler: function(){
            var sm = org.grid.getSelectionModel();
            if (sm.getCount() < 1) {
                MyExt.Msg.alert("尚未选择一条记录！");
                return;
            }
            var data = sm.getSelections();
            var orgid = data[ 0 ].get("orgid");
            var data_v = WhOrg.grid.getSelectionModel().getSelections();
            var datas = new Array();
            for (var i = 0; i < data_v.length; i++){
                datas.push(data_v[ i ].data);
            }
            
            Ext.Ajax.request({
                url: ctx + "/whOrg!saveFromOrg.action",
                jsonData: datas,
                params: {orgid : orgid},
                success: function(response, options){
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        alertSuccess();
                        WhOrg.grid.store.reload(); 
//                        org.searchParam = {'orgname':Ext.get("org_orgname").getValue()};
					    org.grid.store.load();
					    WhOrg.selectWin.hide();
                    } else {
                        alertFail(result.errMsg);
                    }
                },
                failure: function(response, options){
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
        }
    }]
});
//移除组织机构列表的侦听器
org.grid.un('rowdblclick', org.grid.toEditFn, org.grid);
org.grid.store.on('beforeload',function(){
	var searchParam = org.searchParam ;
	var orgname = Ext.get("org_orgname").getValue();
	searchParam.orgname = orgname ;
	//等级。。。。
	searchParam=MyJson.deleteBlankProp(searchParam);
	var sqlStr = " orgid not in (select Org_ID from WLGL_WH_ORG where Record_Status=0)";
	var whereList = [{sql: sqlStr, compare: Condition.SQL}]
    for(prop in searchParam){
 		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.EQ});
 		continue;
     }
     this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
//	this.baseParams.entityJson=Ext.util.JSON.encode(searchParam);
});
});