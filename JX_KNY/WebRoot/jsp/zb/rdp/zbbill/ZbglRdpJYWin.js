/**
 * 机车整备合格交验窗口 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglRdpJYWin');
ZbglRdpJYWin.labelWidth = 90;
ZbglRdpJYWin.fieldWidth = 150;
ZbglRdpJYWin.rdpIDX = '';
ZbglRdpJYWin.trainAccessAccountIDX = '';
ZbglRdpJYWin.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
ZbglRdpJYWin.baseInfoForm =  new Ext.form.FormPanel({
	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", frame: true,    
	defaults: { xtype:"container", autoEl:"div", layout:"form", baseCls:"x-plain", align:"center",  
				defaults: {xtype:"textfield", style:"border:none;background:none;", readOnly:true, anchor: "98%" }},     
        items: [
        {
            labelWidth:50, columnWidth: 0.2, 
            items: [
        		{ fieldLabel:"车型", name:"trainTypeShortName", width:60}
            ]
        },{
            labelWidth:50, columnWidth: 0.2, 
            items: [
            	{ fieldLabel:"车号", name:"trainNo", width:60}
            ]
        },{
            labelWidth:50, columnWidth: 0.25, 
            items: [
                { fieldLabel:"配属段", name:"dname", width:80}
            ]
        },{
            labelWidth:60, columnWidth: 0.35, 
            items: [
                { fieldLabel:"入段时间", name:"inTime", width:150}
            ]
        }]
    
});

ZbglRdpJYWin.saveFn = function() {
	var form = ZbglRdpJYWin.JYInfoForm.getForm(); 
    if (!form.isValid()) return;
    var data = form.getValues();
    var cfg = {
        url: ctx + '/zbglRdpJY!validateForJY.action', 
        params: {rdpIDX: ZbglRdpJYWin.rdpIDX},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            var msg = result.errMsg || "该操作不能恢复";
            Ext.Msg.confirm("提示  ", msg + ",是否继续？  ", function(btn){
		        if(btn != 'yes')    return;			        
		        Ext.Ajax.request({
		        	scope: this, url: ctx + '/zbglRdpJY!updateForJY.action', 
    				params: {rdpIDX: ZbglRdpJYWin.rdpIDX},
    				jsonData: data,
    				success: function(response, options){
		                if(ZbglRdpJYWin.loadMask)   ZbglRdpJYWin.loadMask.hide();
		                var result = Ext.util.JSON.decode(response.responseText);
		                if (result.errMsg == null) {
		                	ZbglRdpJY.ongoingGrid.store.load();
		                	ZbglRdpJY.completeGrid.store.load();
		                	ZbglRdpJYWin.win.hide();
		                	alertSuccess();
		                } else {
		                	alertFail(result.errMsg);
		                }
    				}
		        });
		    }); 
            
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));  
}
ZbglRdpJYWin.JYInfoForm = new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	
    border: false, frame: true, buttonAlign: 'center',
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        defaults: { baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80, defaults:{anchor: "95%" }},
        items: [
        {
            columnWidth: 0.3, 
            items: [
        		{ fieldLabel:"交车人", name:"fromPersonName", value: empname, width:60, style:"border:none;background:none;", readOnly:true},
        		{ xtype: "hidden", name: "fromPersonId", value: empid},
        		// { fieldLabel:"接车人", name:"toPersonName", xtype: 'textfield', allowBlank: false, width:80, maxLength: 50},
        		//需求修改，接车人字段修改为非必填
        		{ fieldLabel:"接车人", name:"toPersonName", xtype: 'textfield', width:80, maxLength: 50},
        		{ xtype: "hidden", name: "toPersonId"}
            ]
        },{
            columnWidth: 0.4, 
            items: [
            	{ fieldLabel:"交验时间", name:"rdpEndTime", xtype: 'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, width: 150},
        		{ fieldLabel:"出段车次", name:"outOrder", xtype: 'textfield', width: 150, maxLength: 20}
            ]
        },{
            columnWidth: 0.3, 
            items: [            	
                {
                	fieldLabel:"整备后去向", xtype: 'EosDictEntry_combo',hiddenName: 'toGo', allowBlank: false, 
					displayField:'dictname',valueField:'dictname', dicttypeid: 'JCZB_RDP_TOGO', width:80
                }
            ]
        }]
    },{    	
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", 
            labelWidth: 80,      columnWidth: 1, 
            items: [{ xtype: "textarea", fieldLabel:"交验情况描述", name:"remarks", width:500, maxLength: 300, anchor: "95%" }]
       }]
    }],
    buttons: [{
        text: "确认交验", iconCls: "saveIcon", handler: ZbglRdpJYWin.saveFn
    }, {
        text: "关闭", iconCls: "closeIcon", handler: function(){ ZbglRdpJYWin.win.hide(); }
    }]
    
});

ZbglRdpJYWin.grid = new Ext.yunda.Grid({
	loadURL: ctx + "/zbglRdpJY!findTaskList.action",
	singleSelect: true,
	storeAutoLoad: false,
	tbar: [],
	hideRowNumberer: true,
	fields: [{
		header:'任务', dataIndex:'taskName'
	},{
		header:'任务类型', hidden: true, dataIndex:'taskType'
	}],
	toEditFn: function(grid, rowIndex, e) {}
});
ZbglRdpJYWin.searchParam = {};
ZbglRdpJYWin.grid.store.on('beforeload', function() {
	this.baseParams.entityJson = Ext.util.JSON.encode(ZbglRdpJYWin.searchParam);
});
ZbglRdpJYWin.showSandingTab = function() {
	ZbglSandingSearch.form.getForm().reset();
	var cfg = {
        scope: this, url: ctx + '/zbglSanding!getEntityByAccountIDX.action', 
        params: {trainAccessAccountIDX: ZbglRdpJYWin.trainAccessAccountIDX},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.success == true && result.entity != null) {	            	
            	var form = ZbglSandingSearch.form.getForm();
				form.reset();
				form.findField("dutyPersonName").setValue(result.entity.dutyPersonName);
				if (result.entity.startTime)
					form.findField("startTime").setValue(new Date(result.entity.startTime).format('Y-m-d H:i:s'));
				var sandingTime = "";
				if(!Ext.isEmpty(result.entity.sandingTime)) {
					sandingTime = (result.entity.sandingTime/60).toFixed(2);
				}
				form.findField("sandingTime").setValue(sandingTime);
				if (result.entity.endTime)
					form.findField("endTime").setValue(new Date(result.entity.endTime).format('Y-m-d H:i:s'));
				var standardSandingTime = "";
				if(!Ext.isEmpty(result.entity.standardSandingTime)) {
					standardSandingTime = (result.entity.standardSandingTime/60).toFixed(2);
				}
				form.findField("standardSandingTime").setValue(standardSandingTime);
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));    
    ZbglRdpJYWin.isHideTab(true,false,false,false,false,false,false);
	ZbglRdpJYWin.tabs.activate(TASKTYPE_SANDING + "tab");
}
ZbglRdpJYWin.showHandoverTab = function() {
	ZbglHoCaseItem.caseForm.getForm().reset();
	ZbglHoCaseItem.grid.store.removeAll();
	var cfg = {
        scope: this, url: ctx + '/zbglHoCase!getEntityByRdp.action', 
        params: {rdpIDX: ZbglRdpJYWin.rdpIDX},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.success == true && result.entity != null) {	            	
            	var form = ZbglHoCaseItem.caseForm.getForm();
				form.reset();
				form.findField("fromPersonName").setValue(result.entity.fromPersonName);
				if (result.entity.handOverTime)
					form.findField("handOverTime").setValue(new Date(result.entity.handOverTime).format('Y-m-d H:i:s'));
				form.findField("remarks").setValue(result.entity.remarks);
				form.findField("toPersonName").setValue(result.entity.toPersonName);
				ZbglHoCaseItem.idx = result.entity.idx;
				ZbglHoCaseItem.grid.store.load();
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));    
	ZbglRdpJYWin.isHideTab(false,true,false,false,false,false,false);
	ZbglRdpJYWin.tabs.activate(TASKTYPE_HANDOVER + "tab");
}
ZbglRdpJYWin.showCleanTab = function() {
	ZbglCleanFormJY.form.getForm().reset();
	var cfg = {
        scope: this, url: ctx + '/zbglCleaning!getEntityByRdp.action', 
        params: {rdpIDX: ZbglRdpJYWin.rdpIDX},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.success == true && result.entity != null) {	            	
            	var form = ZbglCleanFormJY.form.getForm();
				form.reset();
				form.findField("dutyPersonName").setValue(result.entity.dutyPersonName);
				if (result.entity.cleaningTime)
					form.findField("cleaningTime").setValue(new Date(result.entity.cleaningTime).format('Y-m-d H:i:s'));
				form.findField("trainLevel").setValue(result.entity.trainLevel);
				form.findField("cleaningLevel").setValue(result.entity.cleaningLevel);
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));  
    ZbglRdpJYWin.isHideTab(false,false,false,false,false,false,true);
	ZbglRdpJYWin.tabs.activate(TASKTYPE_CLEAN + "tab");
}
ZbglRdpJYWin.grid.on("rowclick", function(grid, rowIndex, e){
	var record = grid.getStore().getAt(rowIndex);
	if (record.get("taskType") == TASKTYPE_SANDING) {		
		ZbglRdpJYWin.showSandingTab();
	}
	if (record.get("taskType") == TASKTYPE_HANDOVER) {
		ZbglRdpJYWin.showHandoverTab();
	}
	if (record.get("taskType") == TASKTYPE_RDP) {
		ZbglRdpJYWin.isHideTab(false,false,true,false,false,false,false);
		ZbglRdpJYWin.tabs.activate(record.get("taskType") + "tab");
		ZbglRdpWi.searchParam.rdpIDX = ZbglRdpJYWin.rdpIDX;
		ZbglRdpWi.grid.store.load();
	}
	if (record.get("taskType") == TASKTYPE_TP) {
		ZbglRdpJYWin.isHideTab(false,false,false,true,false,false,false);
		ZbglRdpJYWin.tabs.activate(record.get("taskType") + "tab");
		ZbglTpJY.searchParam.rdpIDX = ZbglRdpJYWin.rdpIDX;
		ZbglTpJY.grid.store.load();
	}
	if (record.get("taskType") == TASKTYPE_LWFX) {
		ZbglRdpJYWin.isHideTab(false,false,false,false,true,false,false);
		ZbglRdpJYWin.tabs.activate(record.get("taskType") + "tab");
		ZbglTpException.searchParam.rdpIDX = ZbglRdpJYWin.rdpIDX;
		ZbglTpException.grid.store.load();
	}
	if (record.get("taskType") == TASKTYPE_PCZZ) {
		ZbglRdpJYWin.isHideTab(false,false,false,false,false,true,false);
		ZbglRdpJYWin.tabs.activate(record.get("taskType") + "tab");
		ZbglPczzWISearch.searchParam.rdpIdx = ZbglRdpJYWin.rdpIDX; 
		ZbglPczzWISearch.grid.store.load();
	}
	if (record.get("taskType") == TASKTYPE_CLEAN) {
		ZbglRdpJYWin.showCleanTab();
	}
});
ZbglRdpJYWin.tabs = new Ext.TabPanel({
    activeTab: 0,
    frame:true,
    singleSelect: true,
    items:[{  
           id: TASKTYPE_SANDING + "tab",
           title: '机车上砂',
           layout:'fit',
           frame:true,
           items: [ZbglSandingSearch.form]
        },{  
           id: TASKTYPE_HANDOVER + "tab",
           title: '机车交接',
           layout:'fit',
           frame:true,
           items: [ZbglHoCaseItem.panel]
        },{  
           id: TASKTYPE_RDP + "tab",
           title: '整备任务单',
           layout:'fit',
           frame:true,
           items: [ZbglRdpWi.grid]
        },{ 
          id: TASKTYPE_TP + "tab",
          title: '提票活',
          layout: 'fit' ,
          items: [ZbglTpJY.grid]
       },{ 
          id: TASKTYPE_LWFX + "tab",
          title: '遗留活',
          layout: 'fit' ,
          hidden: true,
          items: [ZbglTpException.grid]
       },{ 
          id: TASKTYPE_PCZZ + "tab",
          title: '普查整治',
          frame:true,
          layout: 'fit' ,
          items: [ZbglPczzWISearch.grid]
       },{ 
          id: TASKTYPE_CLEAN + "tab",
          title: '机车保洁',
          frame:true,
          layout: 'fit' ,
          items: [ZbglCleanFormJY.form ]
       }]
});
//参数：【机车上砂】【机车交接】【整备任务】【提票】【遗留活】【普查整治】【机车保洁】
ZbglRdpJYWin.isHideTab = function(tab1,tab2,tab3,tab4,tab5,tab6,tab7){
	if(tab1){
		ZbglRdpJYWin.tabs.unhideTabStripItem(TASKTYPE_SANDING + "tab");
	}else{
		ZbglRdpJYWin.tabs.hideTabStripItem(TASKTYPE_SANDING + "tab");
	}
	if(tab2){
		ZbglRdpJYWin.tabs.unhideTabStripItem(TASKTYPE_HANDOVER + "tab");
	}else{
		ZbglRdpJYWin.tabs.hideTabStripItem(TASKTYPE_HANDOVER + "tab");
	}
	if(tab3){
		ZbglRdpJYWin.tabs.unhideTabStripItem(TASKTYPE_RDP + "tab");
	}else{
		ZbglRdpJYWin.tabs.hideTabStripItem(TASKTYPE_RDP + "tab");
	}
	if(tab4){
		ZbglRdpJYWin.tabs.unhideTabStripItem( TASKTYPE_TP + "tab");
	}else{
		ZbglRdpJYWin.tabs.hideTabStripItem( TASKTYPE_TP + "tab");
	}
	if(tab5){
		ZbglRdpJYWin.tabs.unhideTabStripItem(TASKTYPE_LWFX + "tab");
	}else{
		ZbglRdpJYWin.tabs.hideTabStripItem(TASKTYPE_LWFX + "tab");
	}
	if(tab6){
		ZbglRdpJYWin.tabs.unhideTabStripItem(TASKTYPE_PCZZ + "tab");
	}else{
		ZbglRdpJYWin.tabs.hideTabStripItem(TASKTYPE_PCZZ + "tab");
	}
	if(tab7){
		ZbglRdpJYWin.tabs.unhideTabStripItem(TASKTYPE_CLEAN + "tab");
	}else{
		ZbglRdpJYWin.tabs.hideTabStripItem(TASKTYPE_CLEAN + "tab");
	}
}
//主界面
ZbglRdpJYWin.panel = new Ext.Panel({
	layout: "form",	border: false, style: "padding:2px", labelWidth: ZbglRdpJYWin.labelWidth,
	align: "center",defaults: { anchor: "100%" }, frame: true,
	items:[{
    		xtype: "fieldset", title: "基本信息",  autoHeight: true, collapsible : true, 
    		items: {
				layout: "form", items: ZbglRdpJYWin.baseInfoForm
			}
		},{
			xtype: "fieldset", title: "交验信息",  autoHeight: true, collapsible : true, 
    		items: {
				layout: "form", items: ZbglRdpJYWin.JYInfoForm
			}
		},{
			title: '整备详情', layout : 'border', height: 500,
		    items : [ {
		        title: '详情', width: 200, minSize: 100, maxSize: 400, split: true, region: 'west', bodyBorder: false,
		        collapsible : true,
		        autoScroll: true, layout: 'fit', items : [ ZbglRdpJYWin.grid ]
		    }, {
		        region : 'center', layout: 'fit', bodyBorder: false, items: [ ZbglRdpJYWin.tabs ]
		    } ]
		}]
});


ZbglRdpJYWin.win = new Ext.Window({
    title:"机车整备交验", width: (ZbglRdpJYWin.labelWidth + ZbglRdpJYWin.fieldWidth + 8) * 2 + 60, height: 700,
    plain:true, closeAction:"hide", maximized:true, 
    items:ZbglRdpJYWin.panel, autoScroll : true
});
ZbglRdpJYWin.showWin = function(data) {
	var form = ZbglRdpJYWin.baseInfoForm.getForm();
	form.reset();
	form.findField("trainTypeShortName").setValue(data.trainTypeShortName);
	form.findField("trainNo").setValue(data.trainNo);
	form.findField("dname").setValue(data.dname);
	form.findField("inTime").setValue(new Date(data.inTime).format('Y-m-d H:i:s'));
	
	ZbglRdpJYWin.searchParam.idx = data.idx;
	ZbglRdpJYWin.rdpIDX = data.idx;
    ZbglRdpJYWin.searchParam.trainAccessAccountIDX = data.trainAccessAccountIDX;
    ZbglRdpJYWin.trainAccessAccountIDX = data.trainAccessAccountIDX;
    ZbglRdpJYWin.grid.store.load();
	ZbglRdpJYWin.win.show();
	Ext.getCmp("releaseBut").hide();
	ZbglRdpJYWin.showSandingTab();
}

ZbglRdpJYWin.editWin = function(data) {
	ZbglRdpJYWin.JYInfoForm.buttons[0].setVisible(true);
	var form = ZbglRdpJYWin.JYInfoForm.getForm();
	form.reset();
	form.findField("fromPersonName").setValue(empname);
	form.findField("fromPersonId").setValue(empid);
	form.findField("rdpEndTime").setValue(new Date().format('Y-m-d H:i:s'));
	form.findField("toGo").clearValue();
	ZbglTpJY.grid.getTopToolbar().get(0).setVisible(true);
	ZbglTpException.grid.getTopToolbar().get(0).setVisible(true);
	ZbglRdpJYWin.showWin(data);
}
ZbglRdpJYWin.searchWin = function(data) {
	ZbglRdpJYWin.JYInfoForm.buttons[0].setVisible(false);
	var form = ZbglRdpJYWin.JYInfoForm.getForm();
	form.reset();
	form.findField("fromPersonName").setValue(data.fromPersonName);
	form.findField("fromPersonId").setValue(data.fromPersonId);
	form.findField("toPersonName").setValue(data.toPersonName);
	form.findField("toPersonId").setValue(data.toPersonId);
	form.findField("outOrder").setValue(data.outOrder);
	form.findField("toGo").setDisplayValue(data.afterToGo, data.afterToGo);
	form.findField("rdpEndTime").setValue(new Date(data.rdpEndTime).format('Y-m-d H:i:s'));
	ZbglTpJY.grid.getTopToolbar().get(0).setVisible(false);
	ZbglTpException.grid.getTopToolbar().get(0).setVisible(false);
	ZbglRdpJYWin.showWin(data);
}
});