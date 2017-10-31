Ext.onReady(function(){
    Ext.namespace("WorkStationSelect");
    WorkStationSelect.searchParam={};
    WorkStationSelect.fieldWidth = 110;
	WorkStationSelect.labelWidth = 70;
    WorkStationSelect.batchForm=new Ext.form.FormPanel({
	  layout:'column',baeCls:'x-plain',frame:true,labelWidth: WorkStationSelect.labelWidth,
	  items:[{
	     columnWidth:.3,labelWidth: WorkStationSelect.labelWidth,
	     layout:'form',
	     baseCls:'x-plain',
	     items:[
	     	  {xtype:'textfield',fieldLabel:'工位编号',name:"workStationCode",width:WorkStationSelect.fieldWidth}
	        ]},{
		     columnWidth:.3,
		     layout:'form',labelWidth: WorkStationSelect.labelWidth,
		     baseCls:'x-plain',
		     items:[
		     		{xtype:'textfield',fieldLabel:'工位名称',name:"workStationName",width:WorkStationSelect.fieldWidth}
		   	 	
			]},{
		     columnWidth:.3,
		     layout:'form',labelWidth: WorkStationSelect.labelWidth,
		     baseCls:'x-plain',
		     items:[
		     		{xtype:'textfield',fieldLabel:'所属流水线',name:"repairLineName",width:WorkStationSelect.fieldWidth}
		   	 	
			]},{
		     columnWidth:.1,
		     layout:'form',labelWidth: WorkStationSelect.labelWidth,
		     baseCls:'x-plain',
		     items:[
		         {
			   	 	xtype:'button',text:'查询',handler:function(){
			        WorkStationSelect.searchParam = WorkStationSelect.batchForm.getForm().getValues();
			     	WorkStationSelect.grid.searchFn(WorkStationSelect.searchParam);
		     }}
		     ]
	  }]
	});
    WorkStationSelect.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/workStation!findListForBindWorkStation.action',                 //装载列表数据的请求URL
		storeAutoLoad : false,
		viewConfig:{},		// 设置显示grid组件的滚动条
	    tbar:[],
	    fields: [{
					header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
				},{
					header:'工位编码', dataIndex:'workStationCode', editor:{ allowBlank:false, maxLength:50 }
				},{
					header:'工位名称', dataIndex:'workStationName', editor:{ allowBlank:false, maxLength:100 }
				},{
					header:'流水线主键', dataIndex:'repairLineIdx', hidden:true, editor:{ maxLength:50 }
				},{
					header:'所属流水线', dataIndex:'repairLineName', editor:{ maxLength:100 }
				},{
					header:'状态', dataIndex:'status', 
					renderer : function(v){if(v == stationStatus_new)return "新增";else if(v == stationStatus_use) return "启用";else return "作废";},
					editor:{ xtype:'hidden', value: stationStatus_new },
					searcher:{ disabled: true }			
				}	
				,{
					header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 },
					searcher:{disabled: true}
				},{
					header:'机务设备主键', dataIndex:'equipIDX', hidden:true, editor:{  xtype:'hidden'}
				},{
					header:'机务设备', dataIndex:'equipName',hidden:true, searcher:{disabled: true}
				}],
	           searchFn: function(searchParam){
	                WorkStationSelect.searchParam=searchParam;
	           		this.store.load();	
               }
	});
	WorkStationSelect.grid.un("rowdblclick",WorkStationSelect.grid.toEditFn,WorkStationSelect.grid);
	WorkStationSelect.grid.store.setDefaultSort("repairLineName","ASC");
	WorkStationSelect.grid.store.on('beforeload',function(){
	     var searchParam=WorkStationSelect.searchParam;
	     searchParam=MyJson.deleteBlankProp(searchParam);
	     searchParam.lineType = type ; //流水线类型为【修车】
	     this.baseParams.entityJson=Ext.util.JSON.encode(searchParam);
	});
});