/**
 * 机车整备作业进度查看窗口 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglRdpJDJKWin');
ZbglRdpJDJKWin.labelWidth = 90;
ZbglRdpJDJKWin.fieldWidth = 150;
ZbglRdpJDJKWin.rdpIDX = '';
ZbglRdpJDJKWin.trainAccessAccountIDX = '';
ZbglRdpJDJKWin.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
ZbglRdpJDJKWin.baseInfoForm =  new Ext.form.FormPanel({
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

ZbglRdpJDJKWin.grid = new Ext.yunda.Grid({
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
ZbglRdpJDJKWin.searchParam = {};
ZbglRdpJDJKWin.grid.store.on('beforeload', function() {
	this.baseParams.entityJson = Ext.util.JSON.encode(ZbglRdpJDJKWin.searchParam);
});
ZbglRdpJDJKWin.showSandingTab = function() {
	ZbglSandingSearch.form.getForm().reset();
	var cfg = {
        scope: this, url: ctx + '/zbglSanding!getEntityByAccountIDX.action', 
        params: {trainAccessAccountIDX: ZbglRdpJDJKWin.trainAccessAccountIDX},
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
    ZbglRdpJDJKWin.isHideTab(true,false,false,false,false,false,false);
	ZbglRdpJDJKWin.tabs.activate(TASKTYPE_SANDING + "tab");
}
ZbglRdpJDJKWin.showHandoverTab = function() {
	ZbglHoCaseItem.caseForm.getForm().reset();
	ZbglHoCaseItem.grid.store.removeAll();
	var cfg = {
        scope: this, url: ctx + '/zbglHoCase!getEntityByRdp.action', 
        params: {rdpIDX: ZbglRdpJDJKWin.rdpIDX},
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
	ZbglRdpJDJKWin.isHideTab(false,true,false,false,false,false,false);
	ZbglRdpJDJKWin.tabs.activate(TASKTYPE_HANDOVER + "tab");
}
ZbglRdpJDJKWin.showCleanTab = function() {
	ZbglCleanFormJY.form.getForm().reset();
	var cfg = {
        scope: this, url: ctx + '/zbglCleaning!getEntityByRdp.action', 
        params: {rdpIDX: ZbglRdpJDJKWin.rdpIDX},
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
    ZbglRdpJDJKWin.isHideTab(false,false,false,false,false,false,true);
	ZbglRdpJDJKWin.tabs.activate(TASKTYPE_CLEAN + "tab");
}
ZbglRdpJDJKWin.grid.on("rowclick", function(grid, rowIndex, e){
	var record = grid.getStore().getAt(rowIndex);
	if (record.get("taskType") == TASKTYPE_SANDING) {		
		ZbglRdpJDJKWin.showSandingTab();
	}
	if (record.get("taskType") == TASKTYPE_HANDOVER) {
		ZbglRdpJDJKWin.showHandoverTab();
	}
	if (record.get("taskType") == TASKTYPE_RDP) {
		ZbglRdpJDJKWin.isHideTab(false,false,true,false,false,false,false);
		ZbglRdpJDJKWin.tabs.activate(record.get("taskType") + "tab");
		ZbglRdpWi.searchParam.rdpIDX = ZbglRdpJDJKWin.rdpIDX;
		ZbglRdpWi.grid.store.load();
	}
	if (record.get("taskType") == TASKTYPE_TP) {
		ZbglRdpJDJKWin.isHideTab(false,false,false,true,false,false,false);
		ZbglRdpJDJKWin.tabs.activate(record.get("taskType") + "tab");
		ZbglTpJDJK.searchParam.rdpIDX = ZbglRdpJDJKWin.rdpIDX;
		ZbglTpJDJK.grid.store.load();
	}
	if (record.get("taskType") == TASKTYPE_LWFX) {
		ZbglRdpJDJKWin.isHideTab(false,false,false,false,true,false,false);
		ZbglRdpJDJKWin.tabs.activate(record.get("taskType") + "tab");
		ZbglTpExceptionJDJK.searchParam.rdpIDX = ZbglRdpJDJKWin.rdpIDX;
		ZbglTpExceptionJDJK.grid.store.load();
	}
	if (record.get("taskType") == TASKTYPE_PCZZ) {
		ZbglRdpJDJKWin.isHideTab(false,false,false,false,false,true,false);
		ZbglRdpJDJKWin.tabs.activate(record.get("taskType") + "tab");
		ZbglPczzWISearch.searchParam.rdpIdx = ZbglRdpJDJKWin.rdpIDX; 
		ZbglPczzWISearch.grid.store.load();
	}
	if (record.get("taskType") == TASKTYPE_CLEAN) {
		ZbglRdpJDJKWin.showCleanTab();
	}
});
ZbglRdpJDJKWin.tabs = new Ext.TabPanel({
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
          items: [ZbglTpJDJK.grid]
       },{ 
          id: TASKTYPE_LWFX + "tab",
          title: '例外放行',
          layout: 'fit' ,
          hidden: true,
          items: [ZbglTpExceptionJDJK.grid]
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
//参数：【机车上砂】【机车交接】【整备任务】【提票】【例外放行】【普查整治】【机车保洁】
ZbglRdpJDJKWin.isHideTab = function(tab1,tab2,tab3,tab4,tab5,tab6,tab7){
	if(tab1){
		ZbglRdpJDJKWin.tabs.unhideTabStripItem(TASKTYPE_SANDING + "tab");
	}else{
		ZbglRdpJDJKWin.tabs.hideTabStripItem(TASKTYPE_SANDING + "tab");
	}
	if(tab2){
		ZbglRdpJDJKWin.tabs.unhideTabStripItem(TASKTYPE_HANDOVER + "tab");
	}else{
		ZbglRdpJDJKWin.tabs.hideTabStripItem(TASKTYPE_HANDOVER + "tab");
	}
	if(tab3){
		ZbglRdpJDJKWin.tabs.unhideTabStripItem(TASKTYPE_RDP + "tab");
	}else{
		ZbglRdpJDJKWin.tabs.hideTabStripItem(TASKTYPE_RDP + "tab");
	}
	if(tab4){
		ZbglRdpJDJKWin.tabs.unhideTabStripItem( TASKTYPE_TP + "tab");
	}else{
		ZbglRdpJDJKWin.tabs.hideTabStripItem( TASKTYPE_TP + "tab");
	}
	if(tab5){
		ZbglRdpJDJKWin.tabs.unhideTabStripItem(TASKTYPE_LWFX + "tab");
	}else{
		ZbglRdpJDJKWin.tabs.hideTabStripItem(TASKTYPE_LWFX + "tab");
	}
	if(tab6){
		ZbglRdpJDJKWin.tabs.unhideTabStripItem(TASKTYPE_PCZZ + "tab");
	}else{
		ZbglRdpJDJKWin.tabs.hideTabStripItem(TASKTYPE_PCZZ + "tab");
	}
	if(tab7){
		ZbglRdpJDJKWin.tabs.unhideTabStripItem(TASKTYPE_CLEAN + "tab");
	}else{
		ZbglRdpJDJKWin.tabs.hideTabStripItem(TASKTYPE_CLEAN + "tab");
	}
}
//主界面
ZbglRdpJDJKWin.panel = new Ext.Container({
	layout: 'border',
	defaults: { layout: 'fit' },
	items: [{
		region: 'north', height: 200,
		xtype: 'fieldset', title: "基本信息",  autoHeight: true, collapsible : true, 
		items: ZbglRdpJDJKWin.baseInfoForm
	}, {
		region: 'center', layout: 'border', title: '整备详情', 
		defaults: { layout: 'fit', bodyBorder: false, border: false },
		items: [ {
			region: 'west', 
			width: 200, minSize: 100, maxSize: 400, 
	        title: '详情', split: true,// collapsible : true, autoScroll: true, 
	        items : [ ZbglRdpJDJKWin.grid ]
	    }, {
	        region : 'center', 
	        items: [ ZbglRdpJDJKWin.tabs ]
	    }]
	}]
});


ZbglRdpJDJKWin.win = new Ext.Window({
    title:"机车整备作业进度", width: (ZbglRdpJDJKWin.labelWidth + ZbglRdpJDJKWin.fieldWidth + 8) * 2 + 60, height: 700,
    plain:true, closeAction:"hide", maximized:true, buttonAlign:'center',
    layout: 'fit',
    items:ZbglRdpJDJKWin.panel, autoScroll : true,
    buttons: [ {
        text: "关闭", iconCls: "closeIcon", handler: function(){ ZbglRdpJDJKWin.win.hide(); }
    }]
});
ZbglRdpJDJKWin.showWin = function(data) {
	var form = ZbglRdpJDJKWin.baseInfoForm.getForm();
	form.reset();
	form.findField("trainTypeShortName").setValue(data.trainTypeShortName);
	form.findField("trainNo").setValue(data.trainNo);
	form.findField("dname").setValue(data.dname);
	form.findField("inTime").setValue(new Date(data.inTime).format('Y-m-d H:i:s'));
	
	ZbglRdpJDJKWin.searchParam.idx = data.idx;
	ZbglRdpJDJKWin.rdpIDX = data.idx;
    ZbglRdpJDJKWin.searchParam.trainAccessAccountIDX = data.trainAccessAccountIDX;
    ZbglRdpJDJKWin.trainAccessAccountIDX = data.trainAccessAccountIDX;
    ZbglRdpJDJKWin.grid.store.load();
	ZbglRdpJDJKWin.win.show();
	ZbglRdpJDJKWin.showSandingTab();
}

ZbglRdpJDJKWin.editWin = function(data) {
	
	ZbglTpJDJK.grid.getTopToolbar().get(0).setVisible(true);
	ZbglRdpJDJKWin.showWin(data);
}
ZbglRdpJDJKWin.searchWin = function(data) {
	ZbglTpJDJK.grid.getTopToolbar().get(0).setVisible(false);
	ZbglRdpJDJKWin.showWin(data);
}
});