Ext.onReady(function(){
    Ext.namespace("PartsOutSourcingBack");
    PartsOutSourcingBack.searchParams = {};
    PartsOutSourcingBack.searchParams_back = {};
    PartsOutSourcingBack.labelWidth = 120;
	PartsOutSourcingBack.fieldWidth = 150;
   //未回段列表
   PartsOutSourcingBack.waitGrid=new Ext.yunda.Grid({
    loadURL: ctx + '/partsOutsourcing!pageQuery.action',
    saveFormColNum:2,singleSelect:true,
    labelWidth:PartsOutSourcingBack.labelWidth,
	fieldWidth:PartsOutSourcingBack.fieldWidth,
    tbar:[{
	    	text: '回段', iconCls: 'addIcon', handler: function() {
	    		var sm = PartsOutSourcingBack.waitGrid.getSelectionModel();
	    		if (sm.getCount() <= 0) {
	    			MyExt.Msg.alert('尚未选择任何记录！');
	    			return;
	    		}
	    		if (sm.getCount() > 1) {
	    			MyExt.Msg.alert('请只选择一条记录进行回段！');
	    			return;
	    		}
	    		var record = sm.getSelections()[0] ;
	    		if(PartsOutSourcingBack.waitGrid.saveWin == null)  PartsOutSourcingBack.waitGrid.createSaveWin();
       		    if(PartsOutSourcingBack.waitGrid.searchWin != null)  PartsOutSourcingBack.waitGrid.searchWin.hide(); 
	    		PartsOutSourcingBack.waitGrid.saveWin.setTitle('回段');
		        PartsOutSourcingBack.waitGrid.saveWin.show();
		        PartsOutSourcingBack.waitGrid.saveForm.getForm().reset();
		        PartsOutSourcingBack.waitGrid.saveForm.getForm().loadRecord(record);
	    	}
	    },'refresh'],
    viewConfig:{},
       fields: [
			{
				header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
			},{
				header:'配件委外编号', dataIndex:'partsOutNo',width:150, editor:{ disabled:true }
			},{
	        	dataIndex:'identificationCode',header:'配件识别码', editor:{ disabled:true }
	        },{
				header:'配件返回编号', dataIndex:'partsBackNo',width:150, hidden:true , editor:{id:"partsBackNo",allowBlank: false}
			},{
	        	dataIndex:'identificationCodeBack',header:'配件返回识别码', hidden:true, editor:{id:"identificationCodeBack",allowBlank: false}
	        },{
				header:'配件名称', dataIndex:'partsName',width:100, editor:{disabled:true }
			},{
				header:'规格型号', dataIndex:'specificationModel',width:100, editor:{ disabled:true}
			},{
				header:'委修厂家', dataIndex:'outsourcingFactory', width:100,editor:{ disabled:true }
			},{
				header:'委外日期', dataIndex:'outsourcingDate', width:80,editor:{ disabled:true }
			},{
				header:'返回日期', dataIndex:'backDate', hidden:true, editor:{ xtype:'my97date',format:'Y-m-d',allowBlank: false,initNow:true }
			},{
				header:'经办人', dataIndex:'outEmp', width:70,editor:{xtype:'hidden',disabled:true }
			},{
				header:'接收库房', dataIndex:'takeOverOrg', width:70,editor:{ 
							id:"whName_comb", fieldLabel:"接收库房",
				        	hiddenName:"takeOverOrg",width:150,
							xtype:"Base_combo",allowBlank: false,
							entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
							queryHql:"from Warehouse where recordStatus=0 and status = 1 and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='"+empId+"')",	
							fields:["wareHouseID","wareHouseName","idx"],
							displayField:"wareHouseName",valueField:"wareHouseName",
							returnField:[{widgetId:"takeOverOrgId",propertyName:"idx"}]   }
			},{
				header:'接收库房', dataIndex:'takeOverOrgId',hidden:true,editor:{ id:"takeOverOrgId",xtype:'hidden'  }
			},{
				header:'委外检修原因', dataIndex:'outsourcingReasion',width:200, editor:{ xtype:'textarea',disabled:true }
			},{
				header:'修理内容', dataIndex:'repairContent',width:300, editor:{ xtype:'textarea',disabled:true }
			}],
			afterShowEditWin: function(record, rowIndex){
				Ext.getCmp("partsBackNo").setValue(record.get("partsOutNo"));
				Ext.getCmp("identificationCodeBack").setValue(record.get("identificationCode"));
			},
			saveFn: function(){
		        //表单验证是否通过
		        var form = this.saveForm.getForm(); 
		        if (!form.isValid()) return;
		        
		        //获取表单数据前触发函数
		        this.beforeGetFormData();
		        var data = form.getValues();
		        //获取表单数据后触发函数
		        this.afterGetFormData();
		        data.takeOverType = takeOverType ; //接收部门为库房
		        //调用保存前触发函数，如果返回fasle将不保存记录
		        if(!this.beforeSaveFn(data)) return;
		        
		        var cfg = {
			        scope: this, url: ctx + '/partsOutsourcing!updatePartsOutsourcingForBack.action',
			        params: {outsourcingData: Ext.util.JSON.encode(data)},
			        success: function(response, options){
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {
			            	alertSuccess();
			            	PartsOutSourcingBack.waitGrid.saveWin.hide();
			                PartsOutSourcingBack.waitGrid.store.load();
			                PartsOutSourcingBack.backGrid.store.load();
			            }else {
		                        alertFail(result.errMsg);
		                    }
			        }
			    };
			    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		    }
    
	 });
	 PartsOutSourcingBack.waitForm=new Ext.form.FormPanel({
	 	labelWidth: PartsOutSourcingBack.labelWidth ,
		labelAlign:"left",
		layout:"column",
		padding:'10px', 
		defaults:{
			layout:"form",
			columnWidth:0.33,
			defaults:{
				xtype:"textfield", width: PartsOutSourcingBack.fieldWidth
			}
		},
		items:[{
			items:[{xtype:'textfield',name:'partsName',fieldLabel:'配件名称'},
			{xtype:'textfield',name:'specificationModel',fieldLabel:'规格型号'}]
		}, {
			items:[{xtype:'textfield',name:'partsOutNo',fieldLabel:'委外配件编号'},
			{xtype:'textfield',name:'identificationCode',fieldLabel:'配件识别码'}]
		},{

		  	items:[{xtype:'textfield',name:'outsourcingFactory',id:"outsourcingFactory",fieldLabel:'委外厂家',hidden:true},
	               {id:"outsourcingFactory_wait",xtype:'PartsOutSourcingFactory_combo',fieldLabel:'委外厂家',allowBlank:true,
          	        displayField:'factoryName',valueField:'id',
          	        hiddenName:'outsourcingFactoryId',editable:false,
          	        returnField:[{widgetId:'outsourcingFactory',propertyName:'factoryName'}]}]
		}], 
		buttonAlign: 'center',
		buttons:[{
			text:'查询', iconCls:'searchIcon', handler: function() {
					PartsOutSourcingBack.waitGrid.store.load();
			}
		},{
			text:'重置', iconCls:'resetIcon', handler: function() {
				    PartsOutSourcingBack.waitForm.getForm().reset();
				    Ext.getCmp("outsourcingFactory_wait").clearValue();
				    // 重新加载表格
				    PartsOutSourcingBack.waitGrid.store.load();
			}
		}]
	 });
  PartsOutSourcingBack.waitGrid.store.setDefaultSort('outsourcingDate', 'asc');
   //页面加载和数据模糊查询
  PartsOutSourcingBack.waitGrid.store.on('beforeload', function() {
		PartsOutSourcingBack.searchParams = PartsOutSourcingBack.waitForm.getForm().getValues();
		var searchParam = PartsOutSourcingBack.searchParams;
		searchParam = MyJson.deleteBlankProp(searchParam);
		var whereList = [] ;
		for (prop in searchParam) {			
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		whereList.push({propName:'status', propValue: out}) ;//查询委外、未回段的数据
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	})
   //已回段列表
   PartsOutSourcingBack.backGrid=new Ext.yunda.Grid({
    loadURL: ctx + '/partsOutsourcing!pageQuery.action',
    tbar:[{
	    	text: '撤销', iconCls: 'deleteIcon', handler: function() {
		        var data = PartsOutSourcingBack.backGrid.selModel.getSelections();
		     	if(data.length<1){
		       	  MyExt.Msg.alert("尚未选择一条记录！");
		       	  return ;
		       }
		       var ids = new Array();
			   for(var i=0;i<data.length;i++){
			 		ids.push(data[i].get('idx'));
			    }
			 	Ext.Msg.confirm('提示', "是否确认撤销？", function(btn){
					if (btn == 'yes') {
					 	Ext.Ajax.request({
					        url: ctx + "/partsOutsourcing!updateForCancelBackBatch.action",
					        params: {ids : ids},
					        success: function(response, options){
					            var result = Ext.util.JSON.decode(response.responseText);
					            if (result.errMsg == null) {
					                alertSuccess();
					                PartsOutSourcingBack.waitGrid.store.reload(); 
					                PartsOutSourcingBack.backGrid.store.reload(); 
					            } else {
					                alertFail(result.errMsg);
					            }
					        },
					        failure: function(response, options){
					            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					        }
						});
					}
			 	});
		 	
	    	}
	    },'refresh'],
    viewConfig:{},
       fields: [
			{
				header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
			},{
				header:'配件委外编号', dataIndex:'partsOutNo',width:150
			},{
				header:'配件返回编号', dataIndex:'partsBackNo',width:150
			},{
				header:'配件名称', dataIndex:'partsName',width:100
			},{
	        	dataIndex:'identificationCode',header:'配件识别码'
	        },{
	        	dataIndex:'identificationCodeBack',header:'配件返回识别码'
	        },{
				header:'规格型号', dataIndex:'specificationModel',width:100
			},{
				header:'委外检修原因', dataIndex:'outsourcingReasion',width:200
			},{
				header:'委修厂家', dataIndex:'outsourcingFactory', width:100
			},{
				header:'委修厂家主键', dataIndex:'outsourcingFactoryId',hidden:true
			},{
				header:'委外日期', dataIndex:'outsourcingDate', width:80
			},{
				header:'经办人', dataIndex:'outEmp', width:70
			},{
				header:'修理内容', dataIndex:'repairContent',width:300
			},{
				header:'配件信息主键', dataIndex:'partsAccountIDX',hidden:true
			},{
				header:'规格型号主键', dataIndex:'partsTypeIdx', hidden:true
			}]
    
	 });
	 PartsOutSourcingBack.backGrid.un("rowdblclick",PartsOutSourcingBack.backGrid.toEditFn,PartsOutSourcingBack.backGrid);
	 PartsOutSourcingBack.backForm=new Ext.form.FormPanel({
	 	labelWidth: PartsOutSourcingBack.labelWidth ,
		labelAlign:"left",
		layout:"column",
		padding:'10px', 
		defaults:{
			layout:"form",
			columnWidth:0.33,
			defaults:{
				xtype:"textfield", width: PartsOutSourcingBack.fieldWidth
			}
		},
		items:[{
			items:[{xtype:'textfield',name:'partsName',fieldLabel:'配件名称'},
			{xtype:'textfield',name:'specificationModel',fieldLabel:'规格型号'}]
		}, {
			items:[{xtype:'textfield',name:'partsBackNo',fieldLabel:'配件返回编号'},
			{xtype:'textfield',name:'identificationCodeBack',fieldLabel:'配件返回识别码'}]
		},{

		  	items:[{xtype:'textfield',name:'outsourcingFactory',id:"outsourcingFactory_back",fieldLabel:'委外厂家',hidden:true},
	               {id:"outsourcingFactory_back",xtype:'PartsOutSourcingFactory_combo',fieldLabel:'委外厂家',allowBlank:true,
          	        displayField:'factoryName',valueField:'id',
          	        hiddenName:'outsourcingFactoryId',editable:false,
          	        returnField:[{widgetId:'outsourcingFactory_back',propertyName:'factoryName'}]}]
		}], 
		buttonAlign: 'center',
		buttons:[{
			text:'查询', iconCls:'searchIcon', handler: function() {
					PartsOutSourcingBack.backGrid.store.load();
			}
		},{
			text:'重置', iconCls:'resetIcon', handler: function() {
				    PartsOutSourcingBack.backForm.getForm().reset();
				    Ext.getCmp("outsourcingFactory_back").clearValue();
				    // 重新加载表格
				    PartsOutSourcingBack.backGrid.store.load();
			}
		}]
	 });
	 //页面加载和数据模糊查询
  PartsOutSourcingBack.backGrid.store.on('beforeload', function() {
		PartsOutSourcingBack.searchParams_back = PartsOutSourcingBack.backForm.getForm().getValues();
		var searchParams_back = PartsOutSourcingBack.searchParams_back;
		searchParams_back = MyJson.deleteBlankProp(searchParams_back);
		var whereList = [] ;
		for (prop in searchParams_back) {			
	        whereList.push({propName:prop, propValue: searchParams_back[prop]}) ;
		}
		whereList.push({propName:'status', propValue: back}) ;//查询已回段的数据
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	})
	new Ext.Viewport({
		layout:"border", defaults: {layout:"fit"},
		items:[{
			xtype:"container", region:"center",
			items:[{
				xtype:"tabpanel",
				id:"id_tabpanel",
				activeTab:0,
				defaults: {layout:"fit"},
				items:[{
						xtype:"panel",
						title:"未回段",
						layout:"border",
						items: [{
							xtype:"panel",
							region:"center",
							layout:"fit",
							items: PartsOutSourcingBack.waitGrid
						}, {
							title:"查询",
							region:"north", style:"padding:10px;",
							height:180,
							layout:"fit",frame:true, collapsible: true,
							items:PartsOutSourcingBack.waitForm
						}]
					},{
							xtype:"panel",
							title:"已回段",
							layout:"border",
							items: [{
								xtype:"panel",
								region:"center",
								layout:"fit",
								items: PartsOutSourcingBack.backGrid
							}, {
								title:"查询",
								region:"north", style:"padding:10px;",
								height:180,
								layout:"fit",frame:true, collapsible: true,
								items:PartsOutSourcingBack.backForm
							}],
							listeners: {
								activate: function() {
									PartsOutSourcingBack.backGrid.store.reload();
								}
							}
						}]
			}]
		}]
	})
});