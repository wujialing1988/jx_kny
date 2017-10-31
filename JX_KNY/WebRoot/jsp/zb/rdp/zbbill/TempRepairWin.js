Ext.onReady(function(){
Ext.namespace('TempRepairWin');
TempRepairWin.labelWidth = 90;
TempRepairWin.fieldWidth = 150;
TempRepairWin.rdpIDX = '';
TempRepairWin.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
TempRepairWin.baseInfoForm =  new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false, frame: true,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:50,
            columnWidth: 0.2, 
            items: [
        		{ fieldLabel:"车型", name:"trainTypeShortName", width:60, style:"border:none;background:none;", readOnly:true}
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:50,
            columnWidth: 0.2, 
            items: [
            	{ fieldLabel:"车号", name:"trainNo", width:60, style:"border:none;background:none;", readOnly:true}
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:50,
            columnWidth: 0.25, 
            items: [
                { fieldLabel:"配属段", name:"dname", width:80, style:"border:none;background:none;", readOnly:true}
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:60,
            columnWidth: 0.35, 
            items: [
                { fieldLabel:"入段时间", name:"inTime", width:150, style:"border:none;background:none;", readOnly:true}
            ]
        }]
    }]
});
TempRepairWin.toDoGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglTp!pageQuery.action',                 //装载列表数据的请求URL
    viewConfig: null,
    storeAutoLoad: false,
    page: false,
    hideRowNumberer: true,
    tbar:[{
    	text:'转入临修活', iconCls:'application_goIcon', handler:function(){
    		var grid = TempRepairWin.toDoGrid;
    		if(!$yd.isSelectedRecord(grid)) return;//未选择记录，直接返回
        	var data = grid.selModel.getSelections();
        	TempRepairWin.LXGrid.store.add(data);
        	for(var i = 0; i < data.length; i++){
        		grid.store.remove(data[i]);
        	}
    	}
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},
	Attachment.createColModeJson({ attachmentKeyName:UPLOADPATH_TP, attachmentKeyIDX:'idx'}),
	{
		header:'提票单号', dataIndex:'faultNoticeCode', editor:{  maxLength:50 }
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
		header:'提票状态', dataIndex:'faultNoticeStatus', editor:{  maxLength:20 },
        renderer: function(v){
            switch(v){
                case STATUS_DRAFT:
                    return STATUS_DRAFT_CH;
                case STATUS_OPEN:
                    return STATUS_OPEN_CH;
                case STATUS_OVER:
                    return STATUS_OVER_CH;
                default:
                    return v;
            }
        }
	},{
		header:'处理结果', dataIndex:'repairResult',
        renderer: function(v){
        	return EosDictEntry.getDictname('JCZB_TP_REPAIRRESULT',v);
        }
	}],
	toEditFn: function(grid, rowIndex, e) {}
});
TempRepairWin.toDoGrid.store.on("beforeload", function(){
	var whereList = [] ;	
	whereList.push({propName:'rdpIDX', propValue: TempRepairWin.rdpIDX ,compare : Condition.EQ}) ;
	//未领活或已处理且处理结果为【转临修】的票
	var sqlStr = " ( Fault_Notice_Status = '" + STATUS_DRAFT + 
				"' or (Fault_Notice_Status = '" + STATUS_OVER + "' and Repair_Result = " + REPAIRRESULT_ZLX + "))" ;		
	whereList.push({sql: sqlStr, compare: Condition.SQL});
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});
TempRepairWin.LXGrid = new Ext.yunda.Grid({
    loadURL: '',                 
    viewConfig: null,
    storeAutoLoad: false,
    page: false,
    hideRowNumberer: true,
    tbar:[{
    	text:'移除临修票活', iconCls:'deleteIcon', handler:function(){
    		var grid = TempRepairWin.LXGrid;
    		if(!$yd.isSelectedRecord(grid)) return;//未选择记录，直接返回
        	var data = grid.selModel.getSelections();
        	for(var i = 0; i < data.length; i++){
        		grid.store.remove(data[i]);   
        		TempRepairWin.toDoGrid.store.add(data[i]);
        	}
//        	TempRepairWin.toDoGrid.store.load(); 
    	}
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},
	{
		header:'提票单号', dataIndex:'faultNoticeCode', editor:{  maxLength:50 }
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
		header:'提票状态', dataIndex:'faultNoticeStatus', editor:{  maxLength:20 },
        renderer: function(v){
            switch(v){
                case STATUS_DRAFT:
                    return STATUS_DRAFT_CH;
                case STATUS_OPEN:
                    return STATUS_OPEN_CH;
                case STATUS_OVER:
                    return STATUS_OVER_CH;
                default:
                    return v;
            }
        }
	},{
		header:'处理结果', dataIndex:'repairResult',
        renderer: function(v){
        	return EosDictEntry.getDictname('JCZB_TP_REPAIRRESULT',v);
        }
	}],
	toEditFn: function(grid, rowIndex, e) {}
});
TempRepairWin.LXInfoForm = new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false, frame: true,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:70,
            columnWidth: 0.22, 
            items: [
        		{ fieldLabel:"转临修人", name:"handlePersonName", value: empname, width:60, style:"border:none;background:none;", readOnly:true},
        		{ xtype: "hidden", name: "handlePersonID", value: empid}
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.38, 
            items: [
            	{ fieldLabel:"转临修时间", name:"handleTime", value: new Date().format('Y-m-d H:i:s'),width:150, style:"border:none;background:none;", readOnly:true}
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:90,
            columnWidth: 0.4, 
            items: [
                {
                  fieldLabel:"临修处理班组", xtype: 'OmOrganizationCustom_comboTree',hiddenName: 'handleOrgID',
                  returnField:[{widgetId:"handleOrgName_ID",propertyName:"text"},{widgetId:"handleOrgSeq_ID",propertyName:"orgseq"}],
          		  forDictHql:"[onlyFirstLevel]",
		  		  queryHql:" and orgid in (select orgid from WorkPlaceToOrg where workPlaceCode = '" + siteID + "') and orgdegree = 'tream' ", 
		  		  selectNodeModel: 'leaf', width: 170
                },
                { id: "handleOrgName_ID", xtype: "hidden", name: "handleOrgName"},
                { id: "handleOrgSeq_ID", xtype: "hidden", name: "handleOrgSeq"}
            ]
        }]
    },{    	
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", anchor: "98%" ,
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", 
            labelWidth: 80,      columnWidth: 1, 
            items: [{ xtype: "textarea", fieldLabel:"转临修原因", name:"handleReason", width:500, maxLength: 300}]
       }]
    }]
});
//转临修主界面
TempRepairWin.panel = new Ext.Panel({
	layout: "form",	border: false, style: "padding:2px", labelWidth: TempRepairWin.labelWidth,
	align: "center",defaults: { anchor: "98%" }, frame: true,
	items:[{
    		xtype: "fieldset", title: "基本信息",  autoHeight: true, collapsible : true, 
    		items: {
				layout: "form", items: TempRepairWin.baseInfoForm
			}
		},{
			height: 120, layout: "form",  items: TempRepairWin.LXInfoForm
		},{
    		xtype: "fieldset", title: "未完成票活信息",  height: 200, collapsible : true, 
    		items: {
				layout: "fit", items: TempRepairWin.toDoGrid,  height: 170
			}
		},{			
    		xtype: "fieldset", title: "临修票活信息",  height: 200, collapsible : true, 
    		items: {
				layout: "fit", items: TempRepairWin.LXGrid,  height: 170
			}
		}]
});
TempRepairWin.saveFn = function() {
	var LXInfoForm = TempRepairWin.LXInfoForm.getForm();
    if (!LXInfoForm.isValid()) return;
	if (TempRepairWin.LXGrid.store.getCount() <= 0) {
		Ext.Msg.confirm('提示','未选择临修票活，是否确认转临修?', function(btn) {
			if(btn == 'yes') {
				TempRepairWin.submitFn();
			}
		});
	} else {
		TempRepairWin.submitFn();
	}
}
TempRepairWin.submitFn = function() {
	var tpIDXAry = [];
	for(var i = 0; i < TempRepairWin.LXGrid.store.getCount(); i++){
        tpIDXAry.push(TempRepairWin.LXGrid.store.getAt(i).get("idx"));
    }  
    var LXInfoForm = TempRepairWin.LXInfoForm.getForm();
    var zlxData = LXInfoForm.getValues();
    var params = {
    	idx: TempRepairWin.rdpIDX,
    	tpIDXAry : Ext.util.JSON.encode(tpIDXAry),
    	zlxData : Ext.util.JSON.encode(zlxData)    	
    };
    var cfg = {
        url: ctx + "/zbglRdp!updateForZLX.action", 
        params: params,
        success: function(response, options){
            if(TempRepairWin.loadMask)   TempRepairWin.loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.success == true ) {
                ZbglRdp.SXGrid.store.reload();
                ZbglRdp.LXGrid.store.reload();
                alertSuccess();
                TempRepairWin.win.hide();				
            } else {
                MyExt.TopMsg.msg('提示', result.errMsg, true, 10);
                TempRepairWin.win.hide();
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
}
//转临修主窗口
TempRepairWin.win = new Ext.Window({
    title:"转临修", width: (TempRepairWin.labelWidth + TempRepairWin.fieldWidth + 8) * 2 + 60, 
    plain:true, closeAction:"hide", buttonAlign:'center', maximized:true, 
    items:TempRepairWin.panel, autoScroll : true,
    buttons: [{
        text: "确认", iconCls: "saveIcon", handler: TempRepairWin.saveFn
    }, {
        text: "关闭", iconCls: "closeIcon", handler: function(){ TempRepairWin.win.hide(); }
    }]
});
TempRepairWin.showWin = function(data) {
	var form = TempRepairWin.baseInfoForm.getForm();
	form.reset();
	form.findField("trainTypeShortName").setValue(data.trainTypeShortName);
	form.findField("trainNo").setValue(data.trainNo);
	form.findField("dname").setValue(data.dname);
	form.findField("inTime").setValue(new Date(data.inTime).format('Y-m-d H:i:s'));
	TempRepairWin.rdpIDX = '';
	TempRepairWin.rdpIDX = data.idx;
	TempRepairWin.win.show();
	TempRepairWin.toDoGrid.store.load();
	TempRepairWin.LXGrid.store.removeAll();
	TempRepairWin.LXInfoForm.getForm().reset();
	TempRepairWin.LXInfoForm.getForm().findField("handleOrgID").clearValue();
}
});