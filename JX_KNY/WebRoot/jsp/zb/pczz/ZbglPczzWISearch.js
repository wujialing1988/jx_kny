/**
 * 普查整治任务单--机车整备交验 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglPczzWISearch');//定义命名空间
 /* ************* 定义全局变量开始 ************* */
	ZbglPczzWISearch.searchParam = {};

ZbglPczzWISearch.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglPczzWI!pageList.action',                 //装载列表数据的请求URL
    storeAutoLoad : false,
	viewConfig:{},
	singleSelect: true,
	tbar:[],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX',hidden:true, editor:{  maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeShortName',hidden:true, editor:{  maxLength:8 }
	},{
		header:'车号', dataIndex:'trainNo', hidden:true,editor:{  maxLength:50 }
	},{
		header:'普查整治单主键', dataIndex:'zbglPczzIDX', hidden:true,editor:{  maxLength:50 }
	},{
		header:'普查整治项主键', dataIndex:'zbglPczzItemIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'项目名称', dataIndex:'itemName', editor:{  maxLength:50 }
	},{
		header:'项目内容', dataIndex:'itemContent',editor:{  maxLength:300 }
	},{
		header:'作业机构代码', dataIndex:'handleOrgID',hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'作业机构', dataIndex:'handleOrgName', editor:{  maxLength:200 }
	},{
		header:'作业人编码', dataIndex:'handlePersonID', hidden:true,editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'作业人名称', dataIndex:'handlePersonName', editor:{  maxLength:25 }
	},{
		header:'领活时间', dataIndex:'fetchTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width: 150
	},{
		header:'销活时间', dataIndex:'handleTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width: 150
	},{
		header:'任务单状态', dataIndex:'wIStatus',editor:{  maxLength:20 },
        renderer: function(v){
            switch(v){
            	case RDPWI_STATUS_TODO:
                    return "未处理";
                case RDPWI_STATUS_HANDLING:
                    return "处理中";
                case RDPWI_STATUS_HANDLED:
                    return "已处理";
                default:
                    return v;
            }
        }
	},{
		header:'整备任务单', dataIndex:'rdpIdx', hidden:true,editor:{  maxLength:20 }
	}]
});
ZbglPczzWISearch.grid.store.on("beforeload", function(){
		this.baseParams.entityJson = Ext.util.JSON.encode(ZbglPczzWISearch.searchParam);
	});
	//移除事件
ZbglPczzWISearch.grid.un('rowdblclick',ZbglPczzWISearch.grid.toEditFn,ZbglPczzWISearch.grid);
});