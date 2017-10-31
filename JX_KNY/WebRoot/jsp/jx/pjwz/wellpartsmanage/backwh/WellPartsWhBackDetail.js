Ext.onReady(function(){
   	Ext.namespace('WellPartsWhBackDetail'); 
	
	WellPartsWhBackDetail.store=new Ext.data.JsonStore({
       id:'idx',totalProperty:"totalProperty", autoLoad:true, pruneModifiedRecords: true,
        url: ctx + "/wellPartsWhBack!pageList.action",
	    fields: [  "idx","partsName","partsNo","specificationModel" ]
    });
    
    WellPartsWhBackDetail.loadMask=new Ext.LoadMask(Ext.getBody(),{msg:'正在处理，请稍后...'});
    //配件报废行选择模式，多选
    WellPartsWhBackDetail.sm=new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    WellPartsWhBackDetail.pagingToolbar = Ext.yunda.createPagingToolbar({store: WellPartsWhBackDetail.store});
    WellPartsWhBackDetail.grid=new Ext.grid.GridPanel({
      border: false, 
      enableColumnMove: true, 
      stripeRows: true,
      selModel: WellPartsWhBackDetail.sm,
      loadMask:true,
      viewConfig: {forceFit: true},
       colModel: new Ext.grid.ColumnModel( [
                  new Ext.grid.RowNumberer(),
       	         {
	             	sortable:false,header:'配件信息主键',dataIndex:'idx',hidden:true,editor:{}
	           },{
	             	sortable:false,header:'配件编号',dataIndex:'partsNo',editor:{}
	           },{
	         		sortable:false,header:'配件名称',dataIndex:'partsName',editor:{}
	           },{  
	                sortable:false,header:'配件识别码',dataIndex:'identificationCode',editor:{} 
	           },{
	          		sortable:false,header:'详细配置',dataIndex:'configDetail',editor:{}
	           },{
	         	 	sortable:false,header:'存放地点',dataIndex:'location',editor:{}
	           },{
	         	 	sortable:false,header:'上车车型',dataIndex:'aboardTrainType',editor:{}
	           },{
	         	 	sortable:false,header:'上车车号',dataIndex:'aboardTrainNo',editor:{}
	           },{
	         	 	sortable:false,header:'上车修程',dataIndex:'aboardRepairClass',editor:{}
	           },{
	         	 	sortable:false,header:'上车修次',dataIndex:'aboardRepairTime',editor:{}
	           },{
	         	 	sortable:false,header:'配件状态',dataIndex:'partsStatusName',editor:{}
	           },{
	         		sortable:false,header:'是否新品',dataIndex:'isNewParts',editor:{}
	           },{
	         		sortable:false,header:'配件规格型号主键',dataIndex:'partsTypeIDX',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'规格型号',dataIndex:'specificationModel',editor:{}
	           },{ 
	           	    sortable:false,header:'生产厂家主键',dataIndex:'madeFactoryIdx',hidden:true,editor:{}
	           },{
	          		sortable:false,header:'生产厂家',dataIndex:'madeFactoryName',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'出厂日期',dataIndex:'factoryDate',hidden:true,editor:{}
	           }]),
	           store:WellPartsWhBackDetail.store,
	           tbar:['配件编号:',{xtype:'textfield',width:150,id:'billNo_bill'},'&nbsp;&nbsp;',
	           	     {text:'添加',iconCls:'addIcon',handler:function(){
	           	       var partsNo = Ext.getCmp("billNo_bill").getValue();
	           	       if(partsNo==""){
	           	       	  MyExt.Msg.alert("请输入配件编号!");
	           	       	  return ;
	           	       }
	           	       var searchJson = {identificationCode : partsNo};
	           	       Ext.Ajax.request({
	           	          url:ctx+'/wellPartsWhBack!getPartsAccount.action',
	           	          params:{ searchJson: Ext.util.JSON.encode(searchJson)},
	           	          success:function(response,options){
	           	          	var result=Ext.util.JSON.decode(response.responseText);
	           	          	if(result.success){
	           	          		var entity=result.account;
	           	          		var count=WellPartsWhBackDetail.grid.store.getCount();
	           	          		//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
	           	          		if(count!=0){
	           	          			for(var i=0;i<count;i++){
	           	          				var record=WellPartsWhBackDetail.grid.store.getAt(i);
	           	          				if(entity.partsNo==record.get('partsNo')){
	           	          					MyExt.Msg.alert("配件编号为<font color='red'>【"+record.get('partsNo')+"】</font>已存在列表中，请不要重复添加");
	           	          					return ;
	           	          				}
	           	          			}
	           	          		}
	           	          		var defaultData = entity;
								var initData = Ext.apply({}, defaultData); 
								var record = new Ext.data.Record(defaultData);
						        WellPartsWhBackDetail.grid.store.insert(0, record); 
						        WellPartsWhBackDetail.grid.getView().refresh(); 
						        WellPartsWhBackDetail.grid.getSelectionModel().selectRow(0);
	           	          	}else{
	           	          		MyExt.Msg.alert(result.errMsg);
	           	          	}
	           	          },
	           	          failure: function(response, options){
                   			 MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                          }
	           	       });
	           	     }},'&nbsp;&nbsp;',
	           	     {text:'批量添加明细',iconCls:'chart_attributeConfigIcon',handler:function(){
	           	     	PartsAccountForWellPartsWhBack.grid.store.load();
	           	     	WellPartsWhBackDetail.batchWin.show();
	           	     	
	           	     }},'&nbsp;&nbsp;',
	           	     {text:'删除明细',iconCls:'deleteIcon',handler:function(){
	           	     
	           	     	var data=WellPartsWhBackDetail.grid.selModel.getSelections();
	           	     	if(data.length<1){
	                   	  MyExt.Msg.alert("尚未选择一条记录！");
	                   	  return ;
	                   }
	                   var storeAt = WellPartsWhBackDetail.grid.store.indexOf(data[0]);
				       var records = WellPartsWhBackDetail.store.getModifiedRecords();
				       var count = records.length; 
				       var j = storeAt + 1;
				       if(count-1 == storeAt){
				       		j = storeAt-1;
				       }
					   WellPartsWhBackDetail.grid.getSelectionModel().selectRow(j);
	                   for(var i=0;i<data.length;i++){
	                   	var record=data[i];
	                     WellPartsWhBackDetail.grid.store.remove(record); 
	                   }
	                   WellPartsWhBackDetail.grid.getView().refresh();
	           	     }}
	           	    ],
	           bbar: []
    });
    
	//移除事件
	WellPartsWhBackDetail.grid.un('rowdblclick',WellPartsWhBackDetail.grid.toEditFn,WellPartsWhBackDetail.grid);
	//登账并新增
	WellPartsWhBackDetail.saveFun = function(){
		var store = WellPartsWhBackDetail.grid.store;
		var recCount = store.getCount();
		if(recCount == 0){
			MyExt.Msg.alert("请添加明细！");
			return ;
		}
		var datas = new Array();
		for(var i = 0 ;i< recCount; i++){
			var data={};
    		data= store.getAt(i).data;
    		datas.push(data);
		}
		var form = WellPartsWhBack.form.getForm();
	    if (!form.isValid()) return;
	    var formData = form.getValues();
        Ext.Ajax.request({
            url: ctx + '/wellPartsWhBack!saveWellWhPartsAccount.action',
            jsonData:datas,
    	 	params:{formData:Ext.util.JSON.encode(formData)},
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    WellPartsWhBackDetail.grid.store.removeAll();
                    WellPartsWhBackDetail.grid.store.load();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	}
	WellPartsWhBackDetail.batchWin=new Ext.Window({
     title:'配件选择', width:1000, height:600, plain:true, closeAction:"hide", buttonAlign:'center', layout:'border',
    	maximizable:false,  modal:true,
    	items:[{
    	   region:'west',
    	   width:280,
    	   layout:'fit',
    	   border:false,
    	   items:[PartsTypeTreeSelect.panel]
    	},{
    	   region:'center',
    	   layout:'border',
    	   border:false,
    	   items:[{
    	      region:'north',
    	      collapsible :true,
    	      layout:'fit',
    	      frame:true,
    	      height:150,
    	      title:'查询',
    	      items:[PartsAccountForWellPartsWhBack.batchForm]
    	   },{
    	      region:'center',
    	      layout:'fit',
    	      border:false,
    	      items:[PartsAccountForWellPartsWhBack.grid]
    	   }]
    	}]
	});
});