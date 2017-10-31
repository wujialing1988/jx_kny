Ext.onReady(function(){
    Ext.namespace("BindsensorStationSelect");
    BindsensorStationSelect.searchParam={};
    BindsensorStationSelect.fieldWidth = 110;
	BindsensorStationSelect.labelWidth = 70;

	/** ***定义查询表单开始** */
	  BindsensorStationSelect.batchForm=new Ext.form.FormPanel({
	  layout:'column',baeCls:'x-plain',frame:true,labelWidth: BindsensorStationSelect.labelWidth,
	  items:[{
	     columnWidth:.3,labelWidth: BindsensorStationSelect.labelWidth,
	     layout:'form',
	     baseCls:'x-plain',
	     items:[
	     	  {xtype:'textfield',fieldLabel:'集线盒编号',name:"boxCode",width:BindsensorStationSelect.fieldWidth}
	        ]},{
		     columnWidth:.3,
		     layout:'form',labelWidth: BindsensorStationSelect.labelWidth,
		     baseCls:'x-plain',
		     items:[
		     		{xtype:'textfield',fieldLabel:'传感器编号',name:"sensorCode",width:BindsensorStationSelect.fieldWidth}
		   	 	
			]},{
		     columnWidth:.3,
		     layout:'form',labelWidth: BindsensorStationSelect.labelWidth,
		     baseCls:'x-plain',
		     items:[
		     		{xtype:'textfield',fieldLabel:'安装位置',name:"location",width:BindsensorStationSelect.fieldWidth}
		   	 	
			]},{
		     columnWidth:.1,
		     layout:'form',labelWidth: BindsensorStationSelect.labelWidth,
		     baseCls:'x-plain',
		     items:[
		         {
			   	 	xtype : 'button',text : '查询',
					handler : function() {
							BindsensorStationSelect.searchParam = BindsensorStationSelect.batchForm.getForm().getValues();
							BindsensorStationSelect.grid.searchFn(BindsensorStationSelect.searchParam);
						}
		     }]
	  }]
	});
	
	/** ***定义查询表单结束** */
	
	/** ***定义修改集线盒号*** */
	    BindsensorStationSelect.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/tWTSensor!pageQuery.action',                 //装载列表数据的请求URL
		storeAutoLoad : true,
		viewConfig:{},		// 设置显示grid组件的滚动条
	    tbar:[],
	    fields: [{
					header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
				},{
					header:'集线盒编号', dataIndex:'boxCode', editor:{  maxLength:20 }
				},{
					header:'传感器编号', dataIndex:'sensorCode', editor:{  maxLength:20 }
				},{
					header:'最小感应门限', dataIndex:'minLimit', editor:{ xtype:'numberfield', maxLength:10 }
				},{
					header:'最高感应门限', dataIndex:'maxLimit', editor:{ xtype:'numberfield', maxLength:10 }
				},{
					header:'检测周期', dataIndex:'checkCycle', editor:{ xtype:'numberfield', maxLength:10 }			
				}	
				,{
					header:'安装位置', dataIndex:'location', editor:{  maxLength:50 }
				}],
				toEditFn: function(grid, rowIndex, e){},
				toEditFn: function(grid, rowIndex, e){
					BindsensorStation.addBindSensor();			
				}
		});
		// 筛选数据，显示没有被绑定的数据
		BindsensorStationSelect.grid.store.on('beforeload',function(){   
			var searchParams = BindsensorStationSelect.batchForm.getForm().getValues();
		    searchParams = MyJson.deleteBlankProp(searchParams);
			var whereList = [];
			for(prop in searchParams){
				whereList.push({propName:prop, propValue:searchParams[prop]});
			}
			var sql = "STATION_CODE IS NULL";
			whereList.push({sql: sql, compare:Condition.SQL});
			this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		});
});