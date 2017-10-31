/**
 * 作业项目详情
 */
Ext.onReady(function(){
	
	Ext.ns('ZbglRdpWiDi');
	
	ZbglRdpWiDi.rdpIdx = "" ; // 作业单ID
    
    /**
	 * 任务项详情
	 */
	ZbglRdpWiDi.ZbglRdpWiDiGrid = new Ext.yunda.Grid({
				loadURL : ctx + "/zbglRdpWidi!findZbglRdpWidisByRdpIdx.action",
				singleSelect : true,
				isEdit:false,
				viewConfig: {
		          forceFit: true
		        },
		        tbar : null ,
				fields : [
				{
	   			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
				},
				{header : '专业分类',dataIndex : 'nodeName',width: 150, editor : { xtype:"hidden" }},
				{header : '专项修项目',dataIndex : 'wiName',width: 150,editor : { xtype:"hidden" }},
				{header : '状态',dataIndex : 'wiStatus',width: 90,editor : { xtype:"hidden" }},
				{header : '作业人',dataIndex : 'handlePersonName',width: 90,editor : { xtype:"hidden" }},
				{header : '签到时间',dataIndex : 'handleTime', width: 120,editor : { xtype:"hidden" }},
				{header : '是否合格',dataIndex : 'isHg',hidden:true,width: 90,editor : { xtype:"hidden" }},
				{header : '数据项',dataIndex : 'diName',hidden:true,width: 150,editor : { xtype:"hidden" }},
				{header : '标准',dataIndex : 'diStandard',hidden:true,width: 150,editor : { xtype:"hidden" }},
				{header : '结果',dataIndex : 'diResult',hidden:true,width: 150,editor : { xtype:"hidden" }}
				],
				beforeShowEditWin: function(record, rowIndex){  
					return false;
				}
			});
    
				// 数据加载前
	ZbglRdpWiDi.ZbglRdpWiDiGrid.store.on('beforeload', function() {
		this.baseParams.rdpIdx = ZbglRdpWiDi.rdpIdx;
	});
			
    ZbglRdpWiDi.ZbglRdpWiDiGrid.store.on("load",function(){
    	gridSpan(ZbglRdpWiDi.ZbglRdpWiDiGrid,"row","[nodeName],[wiName],[wiStatus],[handlePersonName],[handleTime],[isHg]");
    });
	
	ZbglRdpWiDi.win = new Ext.Window({
		title:"作业详情", modal:true,width:1200, height:800, plain:true,
		layout:"fit", closeAction:"hide",
		items:ZbglRdpWiDi.ZbglRdpWiDiGrid,
		buttonAlign:'center',
		buttons:[{
			text:'关闭', iconCls:'closeIcon', handler:function(){
				this.findParentByType('window').hide();
			}
		}]
	});

});