/**
 * 机车检修项目 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('RepairProject');                       //定义命名空间
RepairProject.searchParam = {} ;                    //定义查询条件
var loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });
RepairProject.copy = function() {
	var cfg = Ext.apply($yd.cfgAjaxRequest(), {
        url: ctx + '/projectToRecordCard!updateProject.action',
        timeout : 600000000,
        success: function(response, options){
        	if(loadMask)   loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                //操作成功            
                alertSuccess();
            } else {
                //操作失败
                alertFail(result.errMsg);
            }
        }               
    }); 
    Ext.Msg.confirm("提示  ", "此操作不能恢复，确认操作？  ", function(btn){
        if(btn != 'yes')    return;
        if(loadMask)   loadMask.show();
        Ext.Ajax.request(cfg);
    });
}
RepairProject.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/repairProject!pageQuery.action',                 //装载列表数据的请求URL
    tbar:['refresh',{
        text:"机车检修作业工单处理优化", iconCls:"configIcon", tooltip:'机车检修作业工单处理优化', tooltipType:'title', handler:function(){
            RepairProject.copy();
        }        
	}],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: {id:"entityID", xtype:'hidden' }
	},{
		header:'编码', dataIndex:'repairProjectCode', editor:{id:"repairProjectCodeId", maxLength:20 }
	},{
		header:'车型主键', dataIndex:'pTrainTypeIdx',hidden:true, editor:{  
			id:"pTrainTypeIdx_comb",
			allowBlank:false,
			fieldLabel: "车型",
			xtype: "Base_combo",
        	business: 'trainType',
			fields:['typeID','shortName'],
			queryParams: {'isCx':'yes'},
			hiddenName: 'pTrainTypeIdx',
			returnField: [{widgetId:"pTrainTypeShortname",propertyName:"shortName"}],
			displayField: "shortName", valueField: "typeID"
		}
	},{
		header:'额定工时(分)', dataIndex:'ratedWorkHours', editor:{  maxLength:50 ,vtype: "positiveInt"},searcher: {disabled: true}
	},{
		header:'车型', dataIndex:'pTrainTypeShortname',editor:{ xtype:'hidden', id:'pTrainTypeShortname', maxLength:50 },searcher: {disabled: false}
	},{
		header:'名称', dataIndex:'repairProjectName', editor:{ allowBlank:false, maxLength:50 }
	},{
		header:'备注', dataIndex:'remark', editor:{ xtype:'textarea',height:80,  maxLength:1000 },searcher: {disabled: true}
	}],
    searchFn: function(searchParam){ 
		RepairProject.searchParam = searchParam ;
        this.store.load();
	},
	toEditFn: function(grid, rowIndex, e){        
    }
});

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items: RepairProject.grid });
});