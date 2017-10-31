/**
 * 临碎修提票查询 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainTpInfo');                       //定义命名空间
/*** 机车信息列表 start ***/
TrainTpInfo.trainGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainAccessAccount!pageQuery.action',                 //装载列表数据的请求URL
    viewConfig: null,
    singleSelect: true,
    page:false,
    tbar: ['refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型', dataIndex:'trainTypeShortName', 
		editor:{id:'trainTypeShortName', name:'trainTypeShortName',xtype:'hidden' },
		searcher:{xtype: 'textfield'}, width: 60
	},{
		header:'车号', dataIndex:'trainNo', editor:{},
		searcher:{xtype: 'textfield'}, width: 60
	},{
		header:'机车别名', dataIndex:'trainAliasName', editor:{ xtype:'hidden'},
		searcher:{disabled: true}, width: 80
	},{
		header:'配属段', dataIndex:'dName', editor:{id:'dNAME',maxLength:50,disabled:true},
		searcher:{xtype: 'textfield'}, width: 100
	},{
		header:'入段时间', dataIndex:'inTime', xtype:'datecolumn', format: "Y-m-d H:i:s", 
		editor:{ xtype:'hidden' },
		searcher:{disabled: true}, width: 150
	},{
		header:'入段去向', dataIndex:'toGo', editor:{},
		renderer : function(v){
			if (Ext.isEmpty(v))
				return "";
			return EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",v);
		},
		searcher:{xtype: 'textfield'}, width: 100
	},{
		header:'到达车次', dataIndex:'arriveOrder', editor:{  maxLength:20 },
		searcher:{disabled: true}, width: 80
	},{
		header:'入段司机', dataIndex:'inDriver', editor:{  maxLength:20 },
		searcher:{disabled: true}, width: 80
	},{
		header:'计划出段时间', dataIndex:'planOutTime', xtype:'datecolumn', format: "Y-m-d H:i:s", 
		editor:{  },
		searcher:{disabled: true}, width: 150
	},{
		header:'实际出段时间', dataIndex:'outTime', xtype:'datecolumn', format: "Y-m-d H:i:s", 
		editor:{  },
		searcher:{disabled: true}, width: 150
	},{
		header:'出段车次', dataIndex:'outOrder', editor:{  maxLength:20 },
		searcher:{disabled: true}, width: 80
	},{
		header:'出段司机', dataIndex:'outDriver', editor:{  maxLength:20 },
		searcher:{disabled: true}, width: 80
	},{
		header:'站场', dataIndex:'siteName', editor:{ },
		searcher:{disabled: true}, width: 80
	}],
    toEditFn: function(grid, rowIndex, e) {}
});
TrainTpInfo.trainGrid.store.on("beforeload", function(){
	var whereList = [] ;
	whereList.push({propName:"idx", propValue: idx, compare:Condition.EQ, stringLike: false}) ;
	var sqlStr = " Out_Time is null" ;		
	whereList.push({sql: sqlStr, compare: Condition.SQL});
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

/*** 机车信息列表 end ***/

/*** 提票列表 start ***/
TrainTpInfo.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglTp!pageQuery.action',                 //装载列表数据的请求URL
    viewConfig: null,
    tbar:['refresh'],
    singleSelect: true,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型编码', dataIndex:'trainTypeIDX', hidden:true, editor:{  maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }, width : 55
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:8 }, width : 50
	},{
		header:'提票人', dataIndex:'noticePersonName', editor:{  maxLength:25 }, width : 80
	},{
		header:'提票时间', dataIndex:'noticeTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width : 150
	},{
		header:'故障部件', dataIndex:'faultFixFullName', editor:{  maxLength:500 }, width : 300
	},{
		header:'故障现象', dataIndex:'faultName', editor:{  maxLength:50 }
	},{
		header:'故障描述', dataIndex:'faultDesc', editor:{  maxLength:500 }
	},{
		header:'故障原因', dataIndex:'faultReason', editor:{  maxLength:500 }
	},{
		header:'故障发生日期', dataIndex:'faultOccurDate', xtype:'datecolumn', format: "Y-m-d", editor:{ xtype:'my97date' }, width : 120
	},{
		header:'提票状态', dataIndex:'faultNoticeStatus', editor:{  maxLength:20 },
        renderer: function(v){
            switch(v){
            	case STATUS_INIT:
                    return STATUS_INIT_CH;
                case STATUS_DRAFT:
                    return STATUS_DRAFT_CH;
                case STATUS_OPEN:
                    return STATUS_OPEN_CH;
                case STATUS_OVER:
                    return STATUS_OVER_CH;
                case STATUS_CHECK:
                    return STATUS_CHECK_CH;
                default:
                    return v;
            }
        }
	},{
		header:'配属段编码', dataIndex:'dID', hidden:true, editor:{  maxLength:20 }
	},{
		header:'配属段名称', dataIndex:'dName', hidden:true, editor:{  maxLength:50 }
	},{
		header:'故障部件编码', dataIndex:'faultFixFullCode', hidden:true, editor:{  maxLength:200 }
	},{
		header:'故障ID', dataIndex:'faultID', hidden:true, editor:{  maxLength:8 }
	},{
		header:'专业类型ID', dataIndex:'professionalTypeIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
	},{
		header:'专业类型名称', dataIndex:'professionalTypeName', hidden:true, editor:{  maxLength:100 }
	},{
		header:'提票来源', dataIndex:'noticeSource', hidden:true, editor:{  maxLength:50 }
	},{
		header:'提票人编码', dataIndex:'noticePersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'提票站场名称', dataIndex:'siteName', hidden:true, editor:{  maxLength:50 }
	},{
		header:'处理班组编码', dataIndex:'revOrgID', hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'处理班组名称', dataIndex:'revOrgName', hidden:true, editor:{  maxLength:50 }
	},{
		header:'处理班组序列', dataIndex:'revOrgSeq', hidden:true, editor:{  maxLength:300 }
	},{
		header:'接票人编码', dataIndex:'revPersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'接票人', dataIndex:'revPersonName', editor:{  maxLength:25 }
	},{
		header:'接票时间', dataIndex:'revTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width : 150
	},{
		header:'施修方法', dataIndex:'methodDesc', editor:{  maxLength:200 }
	},{
		header:'处理结果', dataIndex:'repairResult', editor:{ xtype:'numberfield', maxLength:2 },
		renderer: function(value, metaData, record, rowIndex, colIndex, store){
			if (Ext.isEmpty(value))
				return "";
            if (record.get("repairClass") == REPAIRCLASS_SX)
            	return EosDictEntry.getDictname("JCZB_TP_REPAIRRESULT",value);
            else if (record.get("repairClass") == REPAIRCLASS_lX)
            	return EosDictEntry.getDictname("JCZB_LXTP_REPAIRRESULT",value);
        }
	},{
		header:'销票人编码', dataIndex:'handlePersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'销票人', dataIndex:'handlePersonName', editor:{  maxLength:25 }
	},{
		header:'销票时间', dataIndex:'handleTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }, width : 150
	},{
		header:'销票站场', dataIndex:'handleSiteID', hidden:true, editor:{  maxLength:50 }
	},{
		header:'验收人编码', dataIndex:'accPersonId', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'验收人名称', dataIndex:'accPersonName', hidden:true, editor:{  maxLength:25 }
	},{
		header:'验收时间', dataIndex:'accTime', xtype:'datecolumn', hidden:true, editor:{ xtype:'my97date' }
	},{
		header:'整备单ID', dataIndex:'rdpIDX', hidden:true, editor:{  maxLength:50 }
	},{
		header:'检修类型', dataIndex:'repairClass', editor:{  maxLength:20 },
        renderer: function(v){
            switch(v){
                case REPAIRCLASS_SX:
                    return "碎修";
                case REPAIRCLASS_lX:
                    return "临修";
                default:
                    return v;
            }
        }
	}],
	toEditFn: function(grid, rowIndex, e) {}
});
TrainTpInfo.grid.store.on("beforeload", function(){
	var whereList = [] ;
	whereList.push({propName:'trainTypeIDX',propValue:trainTypeIDX,compare:Condition.EQ,stringLike: false});
	whereList.push({propName:'trainTypeShortName',propValue:trainTypeShortName,compare:Condition.EQ,stringLike: false});
	whereList.push({propName:'trainNo',propValue:trainNo,compare:Condition.EQ,stringLike: false});
	var sqlStr = " RDP_IDX is not null and RDP_IDX in (select idx from zb_zbgl_rdp where record_status = 0 and TRAIN_ACCESS_ACCOUNT_IDX = '" + idx + "')" ;		
	whereList.push({sql: sqlStr, compare: Condition.SQL});
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

/*** 提票列表 end ***/

/*** 界面布局 start ***/
TrainTpInfo.panel = {
    xtype: "panel", layout: "border", 
    items: [{
        region: 'north', layout: "fit",
        collapsible:true, height: 200, bodyBorder: false,
        items:[TrainTpInfo.trainGrid], frame: true, title: "机车信息"
    },{
        region : 'center', layout : 'fit', bodyBorder: false, items : [ TrainTpInfo.grid ]
    }]
};

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:TrainTpInfo.panel });
});