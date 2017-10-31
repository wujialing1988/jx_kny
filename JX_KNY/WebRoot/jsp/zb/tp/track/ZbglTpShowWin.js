Ext.onReady(function(){
	Ext.namespace('ZbglTpShowWin');                       //定义命名空间
	ZbglTpShowWin.searchParam = {};
	/*** 查询表单 start ***/
	ZbglTpShowWin.searchLabelWidth = 90;
	ZbglTpShowWin.searchAnchor = '95%';
	ZbglTpShowWin.searchFieldWidth = 270;
	
	/*** 提票明细列表***/
	ZbglTpShowWin.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/zbglTpTrackRdpRecordCenter!findZbglTpByTrackRdpIDX.action',//装载列表数据的请求URL
	    singleSelect: true,
	    viewConfig: null,
	    tbar:[],
		fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true
	},{
		header:'提票单号', dataIndex:'faultNoticeCode'
	},{
		header:'车型编码', dataIndex:'trainTypeIDX', hidden:true
	},{
		header:'车型', dataIndex:'trainTypeShortName',  width : 55
	},{
		header:'车号', dataIndex:'trainNo',  width : 50
	},{
		header:'提票人', dataIndex:'noticePersonName', width : 80
	},{
		header:'提票时间', dataIndex:'noticeTime', xtype:'datecolumn', format: "Y-m-d H:i:s", width : 150
	},{
		header:'发现人', dataIndex:'discover',  width : 80
	},{
		header:'故障部件', dataIndex:'faultFixFullName',  width : 300
	},{
		header:'故障现象', dataIndex:'faultName'
	},{
		header:'故障描述', dataIndex:'faultDesc'
	},{
		header:'故障原因', dataIndex:'faultReason',width : 200
	},{
		header:'故障发生日期', dataIndex:'faultOccurDate', xtype:'datecolumn', format: "Y-m-d", width : 120
	},{
		header:'提票状态', dataIndex:'faultNoticeStatus', 
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
		header:'配属段编码', dataIndex:'dID', hidden:true
	},{
		header:'配属段名称', dataIndex:'dName', hidden:true
	},{
		header:'故障部件编码', dataIndex:'faultFixFullCode', hidden:true
	},{
		header:'故障ID', dataIndex:'faultID', hidden:true
	},{
		header:'专业类型ID', dataIndex:'professionalTypeIdx', hidden:true
	},{
		header:'专业类型名称', dataIndex:'professionalTypeName', hidden:true
	},{
		header:'提票来源', dataIndex:'noticeSource', hidden:true
	},{
		header:'提票人编码', dataIndex:'noticePersonId', hidden:true
	},{
		header:'提票站场名称', dataIndex:'siteName', hidden:true
	},{
		header:'处理班组编码', dataIndex:'revOrgID', hidden:true
	},{
		header:'处理班组名称', dataIndex:'revOrgName', hidden:true
	},{
		header:'处理班组序列', dataIndex:'revOrgSeq', hidden:true
	},{
		header:'接票人编码', dataIndex:'revPersonId', hidden:true
	},{
		header:'接票人', dataIndex:'revPersonName'
	},{
		header:'接票时间', dataIndex:'revTime', xtype:'datecolumn', format: "Y-m-d H:i:s", width : 150
	},{
		header:'施修方法', dataIndex:'methodDesc'
	},{
		header:'处理结果', dataIndex:'repairResult',
		renderer: function(value, metaData, record, rowIndex, colIndex, store){
			if (Ext.isEmpty(value))
				return "";
            if (record.get("repairClass") == REPAIRCLASS_SX)
            	return EosDictEntry.getDictname("JCZB_TP_REPAIRRESULT",value);
            else if (record.get("repairClass") == REPAIRCLASS_lX)
            	return EosDictEntry.getDictname("JCZB_LXTP_REPAIRRESULT",value);
        }
	},{
		header:'处理结果描述', dataIndex:'repairDesc',width : 250
	},{
		header:'销票人编码', dataIndex:'handlePersonId', hidden:true
	},{
		header:'销票人', dataIndex:'handlePersonName'
	},{
		header:'销票时间', dataIndex:'handleTime', xtype:'datecolumn', format: "Y-m-d H:i:s", width : 150
	},{
		header:'处理人', dataIndex:'repairEmp'
	},{
		header:'销票站场', dataIndex:'handleSiteID', hidden:true
	},{
		header:'验收人编码', dataIndex:'accPersonId', hidden:true
	},{
		header:'验收人名称', dataIndex:'accPersonName', hidden:true
	},{
		header:'验收时间', dataIndex:'accTime', xtype:'datecolumn', hidden:true
	},{
		header:'整备单ID', dataIndex:'rdpIDX', hidden:true
	},{
		header:'检修类型', dataIndex:'repairClass',
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
	},{
		header:'是否跟踪', dataIndex:'isTracked', hidden:true,
        renderer: function(v){
            switch(v){
                case ISTRACKED_YES:
                    return "跟踪";
                case ISTRACKED_NO:
                    return "未跟踪";
                default:
                    return v;
            }
        }
	},{
		header:'返修次数', dataIndex:'repairTimes', width: 140
	}],
		toEditFn : function(){}
	});
	
	ZbglTpShowWin.grid.store.on("beforeload", function(){	
        var searchParam = {};
        searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
		this.baseParams.zbglTpTrackIDX = ZbglTpTrackThisTime.zbglTpTrackIDX;
		//this.baseParams.recordIDX = ZbglTpTrack.recordIDX;
	});
	
	ZbglTpShowWin.win = new Ext.Window({
	    title:"提票跟踪单", 
	    layout: 'fit',
		modal: true, maximized: true ,
		items:ZbglTpShowWin.grid,
		closable : true,
		plain:true,
		closeAction:"hide",
		buttonAlign: 'center'
	});
	
	ZbglTpShowWin.showWin = function() {
		ZbglTpShowWin.win.show();
		ZbglTpShowWin.grid.store.load();
	}
});
