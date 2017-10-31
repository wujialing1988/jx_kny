
Ext.onReady(function(){
	
	Ext.namespace("OutsourcingCatalog");
	OutsourcingCatalog.grid = new Ext.yunda.RowEditorGrid({
		loadURL: ctx + '/partsOutsourcingCatalog!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/partsOutsourcingCatalog!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/partsOutsourcingCatalog!delete.action',            //删除数据的请求URL
	    tbar:[{
	    	text: '新增',
	    	iconCls: 'addIcon',
	    	handler: function(){
	    		JcpjzdBuildSelect.selectWin.show();
	    		JcpjzdBuildSelect.grid.getStore().load();
	    	}
	    },{
	    	text: '设置委修厂家',
	    	iconCls: 'editIcon',
	    	handler: function(){
	    		if(!$yd.isSelectedRecord(OutsourcingCatalog.grid)) return;
	    		OutSoucingFactorySelect.grid.store.load();
	    		OutSoucingFactorySelect.selectWin.show();
	    	}
	    },{
	    	text: '设置下车送出周期',
	    	iconCls: 'editIcon',
	    	handler: function(){
	    		if(!$yd.isSelectedRecord(OutsourcingCatalog.grid)) return;
	    		OutSourcingCatalogForm.outWin.show();
	    	}
	    },{
	    	text: '设置检修周期',
	    	iconCls: 'editIcon',
	    	handler: function(){
	    		if(!$yd.isSelectedRecord(OutsourcingCatalog.grid)) return;
	    		OutSourcingCatalogForm.repairWin.show();
	    	}
	    },'delete','refresh'],
	    fields: [{
			header:'IDX', dataIndex:'idx', editor: { xtype:"hidden"}, hidden:true
		},{
			header:'零部件编码', dataIndex:'jcpjbm', editor: {disabled:true}, searcher:{}
		},{
			header:'零部件名称', dataIndex:'jcpjmc', editor: { disabled:true }, searcher: {}
		},{
			header:'委修厂家', dataIndex:'madeFactoryName', 
			editor: /*{ 
					 id:"madeFactory_Idx", editable:true,forceSelection:false,selectOnFocus:false,typeAhead:false,
		    		 xtype: 'Base_combo',hiddenName: "madeFactoryName",fieldLabel:"生产厂家",
	 				 entity:"com.yunda.jx.pjwz.partsBase.madefactory.entity.PartsMadeFactory",displayField:"madeFactoryName",valueField:"madeFactoryName",fields:["id","madeFactoryName"],			 
	 				 returnField:[{widgetId:"madeFactoryIdx",propertyName:"id"}], 
	 				 allowBlank: false
			}*/
			{xtype:'PartsOutSourcingFactory_combo',fieldLabel:'委外厂家*',allowBlank:false,
          	        displayField:'factoryName',valueField:'factoryName',fields:["id","factoryName"],
          	        hiddenName:'madeFactoryName',editable:false,id:'madeFactory_Idx',//outsourcingFactoryId
          	        returnField:[{widgetId:'madeFactoryIdx',propertyName:'id'}]
          	     }
			
		},{
			header:'委修厂家', dataIndex:'madeFactoryIdx',hidden:true, editor: { id:"madeFactoryIdx",xtype: "hidden" }, searcher:{}
		},{
			header:'下车送出周期(小时)', dataIndex:'outCycle', editor: { vtype: "positiveInt" }, searcher:{},
			renderer: function(v){
	            if (Ext.isEmpty(v) || v == 0) {
					return "";
				}
				var h = v%24 ;
		        var d = (v-h)/24 ;
		        return d+"天"+h+"小时";
	        }
		},{
			header:'检修周期(小时)', dataIndex:'repairCycle', editor: { vtype: "positiveInt" }, searcher:{},
			renderer: function(v){
	            if (Ext.isEmpty(v) || v == 0) {
					return "";
				}
				var h = v%24 ;
		        var d = (v-h)/24 ;
		        return d+"天"+h+"小时";
	        }
		}],
		afterShowEditWin: function(r, rowIndex){
			//Ext.getCmp("partsAccount_IDX").setDisplayValue(r.get("partsTypeIDX"), r.get("specificationModel"));
			Ext.getCmp("madeFactory_Idx").setDisplayValue(r.get("madeFactoryID"), r.get("factoryName"));
		},
		afterShowSaveWin: function(){
			Ext.getCmp("madeFactory_Idx").clearValue();
			Ext.getCmp("madeFactory_Idx").clearInvalid();
		}
	});
	//新增
	JcpjzdBuildSelect.submit = function(){
		var records=JcpjzdBuildSelect.grid.selModel.getSelections();
        if(records.length<1){
       	   MyExt.Msg.alert("尚未选择一条记录！");
       	   return ;
        }
        var datas = new Array();
		for(var i = 0 ;i < records.length; i++){
			var data={};
    		data.jcpjbm = records[i].data.jcpjbm;
    		data.jcpjmc = records[i].data.jcpjmc;
    		datas.push(data);
		}
		JcpjzdBuildSelect.grid.loadMask.show();
		Ext.Ajax.request({
			url: ctx + "/partsOutsourcingCatalog!newList.action",
			jsonData:datas,
			success: function(response, options){
	            JcpjzdBuildSelect.grid.loadMask.hide();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if(result.success){
	            	alertSuccess();
	            	OutsourcingCatalog.grid.store.reload();
	            	JcpjzdBuildSelect.selectWin.hide();
	            }else{
	            	alertFail(result.errMsg);
	            }
			},
	        failure: function(response, options){
				JcpjzdBuildSelect.grid.loadMask.hide();
	            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	        }
		});
	}
	//设置委修厂家
	OutSoucingFactorySelect.submit = function(){
		var factory = OutSoucingFactorySelect.grid.selModel.getSelections();
		if(factory.length == 0){
			MyExt.Msg.alert("尚未选择一条记录！")
			return;
		}
		var idxs = $yd.getSelectedIdx(OutsourcingCatalog.grid);
		OutSoucingFactorySelect.grid.loadMask.show();
		Ext.Ajax.request({
			url: ctx + "/partsOutsourcingCatalog!setMadeFactory.action",
			params: {idxs: idxs + "", factoryId: factory[0].get("id"),factoryName: factory[0].get("factoryName")},
			success: function(response, options){
				OutSoucingFactorySelect.grid.loadMask.hide();
				var result = Ext.util.JSON.decode(response.responseText);
				if(result.success){
					alertSuccess();
					OutsourcingCatalog.grid.store.reload();
					OutSoucingFactorySelect.selectWin.hide();
				}else{
					alertFail(result.errMsg);
				}
			},
			failure: function(response, options){
				OutSoucingFactorySelect.grid.loadMask.hide();
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	var viewport = new Ext.Viewport({ layout:'fit', items: OutsourcingCatalog.grid });
});