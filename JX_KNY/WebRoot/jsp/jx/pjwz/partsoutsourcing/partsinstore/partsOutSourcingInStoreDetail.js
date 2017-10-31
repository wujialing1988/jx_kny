Ext.onReady(function(){
	Ext.namespace("RepairedPartsWHDetail");
	
	// 显示扩展编号详情的函数
	RepairedPartsWHDetail.searchExtendNo = function(rowIndex, formColNum) {
		var extendNoJson = RepairedPartsWHDetail.grid.store.getAt(rowIndex).get("extendNoJson");
		jx.pjwz.partbase.component.PartsExtendNoWin.createWin(extendNoJson, formColNum);
	}
	
	RepairedPartsWHDetail.store=new Ext.data.JsonStore({
	   id:'idx',totalProperty:'totalProperty',autoLoad:true, pruneModifiedRecords: true,
      url:ctx+'/repairedPartsWHDetail!pageList.action',
      fields:['idx','partsNo','extendNoJson','whLocationName','whLocationIdx','configDetail',
                'partsName','specificationModel','partsAccountIdx','repairedPartsWhIdx']
	});
	
    RepairedPartsWHDetail.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
      //行选择模式
    RepairedPartsWHDetail.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    RepairedPartsWHDetail.grid=new Ext.grid.EditorGridPanel({
       border: false, enableColumnMove: true, stripeRows: true, selModel: RepairedPartsWHDetail.sm,loadMask:true,
	    clicksToEdit: 1,
	    viewConfig: {forceFit: true},
	     colModel:new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
	      {
	        	sortable:false,dataIndex:'idx',hidden:true,header:'idx'
	        },{
	        	sortable:false,dataIndex:'partsNo',header:'配件编号',editor:{xtype:'textfield', allowBlank: false}
	        },{
				sortable:false,header:'扩展编号', dataIndex:'extendNoJson', searcher : { disabled : true }, width: 150, 
				renderer: function(v,metadata, record, rowIndex, colIndex, store) {
					var extendNo = jx.pjwz.partbase.component.PartsExtendNoWin.getExtendNo(v);		
					if (Ext.isEmpty(extendNo)) {
						return "";
					}
					var html = "";
		    		html = "<span><a href='#' onclick='RepairedPartsWHDetail.searchExtendNo(" + rowIndex + ", 1)'>"+extendNo+"</a></span>";
		            return html;
				}
			},{
	        	sortable:false,dataIndex:'whLocationName',header:'存放库位',
	        	editor:{id:"location_comb", editable:true,selectOnFocus:false,forceSelection:false,typeAhead:false,
	    	    		 xtype: 'Base_combo',hiddenName: "whLocationIdx",fieldLabel:"存放库位",
		 				 entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.WarehouseLocation",displayField:"locationName",
		 				 valueField:"locationName",fields:["idx","locationName"],			 
		 				// returnField:[{widgetId:"whLocationIdx",propertyName:"idx"}], 
		 				 width: 120,allowBlank: false}
	        },{
	        	sortable:false,dataIndex:'whLocationIdx',header:'存放库位',hidden:true
	        	//editor:{id:'whLocationIdx'}
	        	 
	        },{
	        	sortable:false,dataIndex:'configDetail',header:'详细配置',editor:{xtype:'textarea',maxLangh:200}
	        },{
	       		sortable:false,dataIndex:'partsName',header:'配件名称'//,editor:{disabled:true}
	        },{
	           	sortable:false,dataIndex:'specificationModel',header:'规格型号'//,editor:{disabled:true}
	        },{	
	        	sortable:false,dataIndex:'partsAccountIdx',header:'配件信息主键',hidden:true
	        },{	
	        	sortable:false,dataIndex:'partsTypeIDX',header:'配件型号表主键',hidden:true
	        },{
	        	sortable:false,dataIndex:'repairedPartsWhIdx',header:'修竣配件入库主键',hidden:true
	        },{
	        	sortable:false,dataIndex:'partsOutNo',header:'老配件编号',hidden:true
	        }]),
	        listeners: {
				cellclick:function(grid, rowIndex, columnIndex, e) {
				    var whIdx = Ext.getCmp("whIdx").getValue();
				    var location_comb =  Ext.getCmp("location_comb");
	                location_comb.reset();
	                location_comb.clearValue();
	                location_comb.getStore().removeAll();
	                location_comb.queryParams = {"wareHouseIDX":whIdx};
	                location_comb.cascadeStore();
			}},
	        store:RepairedPartsWHDetail.store,
	        tbar:['配件编号：',{xtype:'textfield',id:'partsId',width:120},
	             {text:'添加',iconCls:'addIcon',handler:function(){
	              var billId=Ext.getCmp("partsId").getValue();
	           	       if(billId==""){
	           	       	  MyExt.Msg.alert("请输入配件编号!");
	           	       	  return ;
	           	       }
	           	       Ext.Ajax.request({
	           	          url:ctx+'/partsOutsourcing!findPartsOutforNo.action',
	           	          params:{billId:billId,status:"('"+out+"')"},
	           	          success:function(response,options){
	           	          	var result=Ext.util.JSON.decode(response.responseText);
	           	          	if(result.success){
	           	          		var entity=result.entity;
	           	          		var partsAccountIDX=entity[0];//配件信息主键
	           	          		var partsTypeIdx=entity[1];//配件规格型号主键
	           	          		var partsNo=entity[4];//配件编号
	           	          		var partsName=entity[2];//配件名称
	           	          		var specificationModel=entity[3];//规格型号
	           	          		var configDetail=entity[5];
	           	          		var extendNoJson=entity[6]; // 扩展编号
	           	          		var count=RepairedPartsWHDetail.grid.store.getCount();
	           	          		//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
	           	          		if(count!=0){
	           	          			for(var i=0;i<count;i++){
	           	          				var record=RepairedPartsWHDetail.grid.store.getAt(i);
	           	          				if(entity[4]==record.get('partsOutNo')){
	           	          					MyExt.Msg.alert("配件编号为<font color='red'>【"+record.get('partsOutNo')+"】</font>已存在列表中，请不要重复添加");
	           	          					return ;
	           	          				}
	           	          			} 
	           	          		}
	           	          		var defaultData = {partsAccountIdx:partsAccountIDX,partsNo:partsNo,partsOutNo:partsNo,extendNoJson:extendNoJson,partsName:partsName,specificationModel:specificationModel,partsTypeIDX:partsTypeIdx};
								var initData = Ext.apply({}, defaultData); 
								var record = new Ext.data.Record(defaultData);
						        RepairedPartsWHDetail.grid.store.insert(0, record); 
						        RepairedPartsWHDetail.grid.getView().refresh(); 
						        RepairedPartsWHDetail.grid.getSelectionModel().selectRow(0);
						        var count_1=RepairedPartsWHDetail.grid.store.getCount();
						        var count_1=RepairedPartsWHDetail.grid.store.getCount();
				                  if(count_1>0){
				                	  Ext.getCmp("whName_comb").disable();
				                   }else{
				                     Ext.getCmp("whName_comb").enable();
				                   }
	           	          	}else{
	           	          		MyExt.Msg.alert("<font color='red'>未找到配件编号为【"+billId+"】的配件或该配件已入库！</font>");
	           	          	}
	           	          },
	           	          failure: function(response, options){
                   			 MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                          }
	           	       });
	           	
	           } },'&nbsp;&nbsp;',
	           {text:'批量添加明细',iconCls:'chart_attributeConfigIcon',handler:function(){
	           	  var madeFactoryId = "ALL"; //查询委外修目录下所有的规格型号
	           	   PartsOutSourcingTypeTreeSelect.loadOutsSourcingTree(madeFactoryId);
	              PartsOutSourcSelect.grid.store.load();
	           	   RepairedPartsWHDetail.win.show();
	           }},'&nbsp;&nbsp;',
	           {text:'删除明细',iconCls:'deleteIcon',handler:function(){
	           	  
	                var records=RepairedPartsWHDetail.grid.selModel.getSelections();
	                if(records.length<1){
	                  MyExt.Msg.alert("尚未选择一条记录！");
	                 return ;
	                }
	                for(var i=0;i<records.length;i++){
	                    var record=records[i];
	                    RepairedPartsWHDetail.grid.store.remove(record);
	                }
	                 RepairedPartsWHDetail.grid.getView().refresh();
	                  var count_1=RepairedPartsWHDetail.grid.store.getCount();
	                  if(count_1>0){
	                	  Ext.getCmp("whName_comb").disable();
	                   }else{
	                     Ext.getCmp("whName_comb").enable();
	                   }
	                 //RepairedPartsWHDetail.grid.store.load();
	           }}]
    });
    RepairedPartsWHDetail.store.on('beforeload',function(){
        var beforeloadParam = {repairedPartsWhIdx: "-11111111"};
		this.baseParams.entityJson = Ext.util.JSON.encode(beforeloadParam);
    });
   
    RepairedPartsWHDetail.win=new Ext.Window({
       title:'委外配件选择', width:1000, height:600, plain:true, closeAction:"hide", buttonAlign:'center', layout:'border',frame:true,
    	maximizable:false,  modal:true,
    	items:[
    		{region:'west',frame:true,width:280,layout:'fit',items:[PartsOutSourcingTypeTreeSelect.panel]},
    		{region:'center',frame:true,layout:'fit',items:[PartsOutSourcSelect.grid]}
    	 ]
    });
});