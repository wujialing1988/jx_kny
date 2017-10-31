/**
 * JT6提票--机车整备合格交验 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglTpJDJK');                       //定义命名空间
ZbglTpJDJK.searchParam = {};
ZbglTpJDJK.labelWidth = 90;
ZbglTpJDJK.fieldWidth = 150;
ZbglTpJDJK.saveForm = new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false, frame: true, labelWidth: ZbglTpJDJK.labelWidth,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:ZbglTpJDJK.labelWidth,
            columnWidth: 0.5, 
            items: [
        		{ fieldLabel:"放行人", name:"handlePersonName", value: empname, width:ZbglTpJDJK.fieldWidth, style:"border:none;background:none;", readOnly:true},
        		{ xtype: "hidden", name: "handlePersonId", value: empid}
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:ZbglTpJDJK.labelWidth,
            columnWidth: 0.5, 
            items: [
            	{ fieldLabel:"放行时间", name:"handleTime", xtype: 'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, width: ZbglTpJDJK.fieldWidth, allowBlank: false}
            ]
        }]
    },{    	
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", anchor: "98%" ,
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", 
            labelWidth: ZbglTpJDJK.labelWidth,      columnWidth: 1, 
            items: [{ xtype: "textarea", fieldLabel:"放行原因", name:"exceptionReason", width:ZbglTpJDJK.labelWidth + ZbglTpJDJK.fieldWidth + 8, maxLength: 300}]
       }]
    }]
});
ZbglTpJDJK.saveFn = function() {
    var form = ZbglTpJDJK.saveForm.getForm(); 
    if (!form.isValid()) return;
    var data = form.getValues();   
    
    var datas = ZbglTpJDJK.grid.selModel.getSelections();
    if (datas.length < 1) {
    	MyExt.Msg.alert("请选择提票单记录");
	    return;
    }
    var tpExceptionAry = [];
	for(var i = 0; i < datas.length; i++){
		var tpException = {};
		tpException.rdpIDX = datas[i].get("rdpIDX");
		tpException.tpIDX = datas[i].get("idx");
		tpException.exceptionReason = data.exceptionReason;
		tpException.handlePersonId = data.handlePersonId;
		tpException.handlePersonName = data.handlePersonName;
		tpException.handleTime = data.handleTime;
        tpExceptionAry.push(tpException);
    }  
    var params = {
    	tpExceptionAry : Ext.util.JSON.encode(tpExceptionAry)
    };
    
    if(ZbglTpJDJK.grid.loadMask)   ZbglTpJDJK.grid.loadMask.show();
    var cfg = {
        scope: this, url: ctx + '/zbglTpException!saveForLwfx.action', 
        params: params,
        success: function(response, options){
            if(ZbglTpJDJK.grid.loadMask)   ZbglTpJDJK.grid.loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.success == true) {
            	ZbglTpJDJK.grid.store.load();
            	ZbglRdpJYWin.grid.store.load();
            	ZbglTpJDJK.saveWin.hide();
            	alertSuccess();                
            } else {
                alertFail(result.errMsg);
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));    
}

ZbglTpJDJK.saveWin = new Ext.Window({
    title:"放行原因填写", width: (ZbglTpJDJK.labelWidth + ZbglTpJDJK.fieldWidth + 8) * 2 + 60, 
    plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, items:ZbglTpJDJK.saveForm, 
    buttons: [{
        text: "确认", iconCls: "saveIcon", handler: ZbglTpJDJK.saveFn
    }, {
        text: "关闭", iconCls: "closeIcon", handler: function(){ ZbglTpJDJK.saveWin.hide(); }
    }]
});

ZbglTpJDJK.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglTp!pageQuery.action',                 //装载列表数据的请求URL
    viewConfig: null,
    storeAutoLoad: false,
    tbar:[{
    	    xtype:'label', text: '票活状态: '
    },{   
	        xtype:'checkbox', name:'faultNoticeStatus', boxLabel: STATUS_DRAFT_CH + '&nbsp;&nbsp;&nbsp;&nbsp;', 
	        inputValue:STATUS_DRAFT, checked:true,
		    handler: function(){
		    	ZbglTpJDJK.checkQuery();
		    }
	    },{   
	        xtype:'checkbox', name:'faultNoticeStatus', boxLabel: STATUS_OPEN_CH + '&nbsp;&nbsp;&nbsp;&nbsp;', 
	        inputValue:STATUS_OPEN, checked:true,
		    handler: function(){
		    	ZbglTpJDJK.checkQuery();
		    }
	    },{   
	        xtype:'checkbox', name:'faultNoticeStatus', boxLabel: STATUS_OVER_CH + '&nbsp;&nbsp;&nbsp;&nbsp;', 
	        inputValue:STATUS_OVER, checked:true,
		    handler: function(){
		    	ZbglTpJDJK.checkQuery();
		    }
	    },{   
	        xtype:'checkbox', name:'faultNoticeStatus', boxLabel: STATUS_CHECK_CH + '&nbsp;&nbsp;&nbsp;&nbsp;', 
	        inputValue:STATUS_CHECK, checked:true,
		    handler: function(){
		    	ZbglTpJDJK.checkQuery();
		    }
	    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},
	Attachment.createColModeJson({ attachmentKeyName:UPLOADPATH_TP, attachmentKeyIDX:'idx'}),
	{
		header:'提票单号', dataIndex:'faultNoticeCode', editor:{  maxLength:50 }
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
		header:'故障发生日期', dataIndex:'faultOccurDate', xtype:'datecolumn', format: "Y-m-d", editor:{ xtype:'my97date' }, width : 120
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
            else if (record.get("repairClass") == REPAIRCLASS_LX)
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
		header:'检修类型', dataIndex:'repairClass',
        renderer: function(v){
            switch(v){
            	case REPAIRCLASS_SX:
                    return "碎修";
                case REPAIRCLASS_LX:
                    return "临修";
                default:
                    return v;
            }
        }
	}],
	toEditFn: function(grid, rowIndex, e) {},
    searchFn: function(searchParam){
    	ZbglTpJDJK.searchParam = searchParam;
    	this.store.load();
    }
});
ZbglTpJDJK.faultNoticeStatus = STATUS_DRAFT + "," + STATUS_OPEN + "," + STATUS_OVER + "," + STATUS_CHECK;
ZbglTpJDJK.checkQuery = function() {
	ZbglTpJDJK.faultNoticeStatus = "-1";
	var checkBoxGroup = ZbglTpJDJK.grid.getTopToolbar().findByType("checkbox");
	for(var i = 0; i < checkBoxGroup.length; i++) {
		if(checkBoxGroup[i].checked) {
			ZbglTpJDJK.faultNoticeStatus = ZbglTpJDJK.faultNoticeStatus + "," + checkBoxGroup[i].inputValue;
		}
	}
	ZbglTpJDJK.grid.store.load();
}
ZbglTpJDJK.grid.store.on("beforeload", function(){	
	var searchParam = ZbglTpJDJK.searchParam;
	var whereList = [] ;
	for (prop in searchParam) {			
        if(prop == 'rdpIDX'){
			whereList.push({propName:prop, propValue: searchParam[prop], stringLike: false}) ;
		}else{
        	whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
	}
	whereList.push({propName:'faultNoticeStatus', propValues: ZbglTpJDJK.faultNoticeStatus.split(",") ,compare : Condition.IN}) ;
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});
});