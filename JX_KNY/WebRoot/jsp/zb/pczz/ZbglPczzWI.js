/**
 * 普查整治任务单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglPczzWI');//定义命名空间

 /* ************* 定义全局变量开始 ************* */
	ZbglPczzWI.fieldWidth = 120;
	ZbglPczzWI.labelWidth = 80;
	ZbglPczzWI.searchParams = {};
 /* ************* 定义全局变量结束 ************* */

	//任务项目执行情况
	ZbglPczzWI.form = new Ext.form.FormPanel({
		layout: "column",
		labelWidth:ZbglPczzWI.labelWidth,
		border: false,
		defaults:{
			columnWidth: 0.5, 
			layout:'form', 
			defaultType:'textfield',
			defaults:{
				style:"border:none;background:none;", readOnly:true, anchor:'100%'
			}
		},
		items:[{
            items: [
            	{ fieldLabel:"项目名称", name:"itemName"}
            ]
		},{
            items: [
            	{ fieldLabel:"项目内容", name:"itemContent"}
            ]
		}, {
			columnWidth: 1,
            items: [
            	{ fieldLabel:"车型", name:"trainTypeShortNameList"}
            ]
		}]
  });

ZbglPczzWI.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglPczzWI!pageList.action',                 //装载列表数据的请求URL
    storeAutoLoad : false,
	viewConfig:{},
	tbar:[],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX',hidden:true, editor:{  maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},{
		header:'普查整治单主键', dataIndex:'zbglPczzIDX', hidden:true,editor:{  maxLength:50 }
	},{
		header:'普查整治项主键', dataIndex:'zbglPczzItemIDX',hidden:true, editor:{  maxLength:50 }
	},{
		header:'项目名称', dataIndex:'itemName',hidden:true, editor:{  maxLength:50 }
	},{
		header:'项目内容', dataIndex:'itemContent', hidden:true,editor:{  maxLength:300 },width: 200
	},{
		header:'作业机构代码', dataIndex:'handleOrgID',hidden:true, editor:{ xtype:'numberfield', maxLength:10 }
	},{
		header:'作业机构', dataIndex:'handleOrgName', editor:{  maxLength:200 }
	},{
		header:'作业人编码', dataIndex:'handlePersonID', hidden:true,editor:{ xtype:'numberfield', maxLength:18 }
	},{
		header:'作业人名称', dataIndex:'handlePersonName', editor:{  maxLength:25 }
	},{
		header:'领活时间', dataIndex:'fetchTime', xtype:'datecolumn', format:'Y-m-d H:i:s', editor:{ xtype:'my97date' },width: 150
	},{
		header:'销活时间', dataIndex:'handleTime', xtype:'datecolumn', format:'Y-m-d H:i:s', editor:{ xtype:'my97date' },width: 150
	},{
		header:'任务单状态', dataIndex:'wIStatus', editor:{  maxLength:20 },
        renderer: function(v){
            switch(v){
                case RDPWI_STATUS_HANDLING:
                    return RDPWI_STATUS_HANDLING_CH;
                case RDPWI_STATUS_HANDLED:
                    return RDPWI_STATUS_HANDLED_CH;
                default:
                    return RDPWI_STATUS_TODO_CH;
            }
        }
	}]
});
ZbglPczzWI.grid.store.on("beforeload", function(){
		ZbglPczzWI.searchParams.zbglPczzItemIDX = ZbglPczzItemSearch.zbglPczzItemIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(ZbglPczzWI.searchParams);
	});
	//移除事件
ZbglPczzWI.grid.un('rowdblclick',ZbglPczzWI.grid.toEditFn,ZbglPczzWI.grid);
ZbglPczzWI.detailWin = new Ext.Window({
		title: "任务项目执行情况",width: 720, height: 500, maximizable:false, layout: "fit", 
		closeAction: "hide", modal: true, maximized: false , buttonAlign:"center",
		items: {
			xtype: "panel", layout: "border",
			items:[{
						height: 100,layout: "fit",
						region : 'north',split:true,
				        bodyBorder: false,frame: true,
				        autoScroll : true,
				        items : [ ZbglPczzWI.form ]
					},{
						region : 'center',
				        layout: "fit",
				        items:[ ZbglPczzWI.grid ]
					}
	    
	]},
	buttons: [{
            text: "关闭", iconCls: "closeIcon", handler: function(){ ZbglPczzWI.detailWin.hide(); }
        }]});
});