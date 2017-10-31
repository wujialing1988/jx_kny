
Ext.onReady(function(){
	
	Ext.namespace("Dir");
	
	Dir.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/partsOutsourcingList!findPageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/partsOutsourcingList!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/partsOutsourcingList!logicDelete.action',            //删除数据的请求URL
	   // saveFormColNum:2,
	   // searchFormColNum:2,
	    tbar:['search',{
	    	text: i18n.common.button.add,
	    	iconCls: 'addIcon',
	    	handler: function(){
	    		PartsTypeSelect.selectWin.show();
	    	}
	    },{
	    	text: i18n.OutsourcingDir.setOutFactoryBtn,
	    	iconCls: 'editIcon',
	    	handler: function(){
	    		if(!$yd.isSelectedRecord(Dir.grid)) return;
	    		OutSoucingFactorySelect.grid.store.load();
	    		OutSoucingFactorySelect.selectWin.show();
	    	}
	    },'delete','refresh'],
	    fields: [{
			header:'IDX', dataIndex:'idx', editor: { xtype:"hidden"}, hidden:true
		},{
			header:'配件型号', dataIndex:'partsTypeIDX', 
			editor: {
				hidden:true
//				id: "partsAccount_IDX", xtype: "PartsType_SelectWin",
//				hiddenName: "partsTypeIDX",  displayField:"specificationModel",
//				valueField:"idx", allowBlank:false, editable:false,
//				returnField:[{widgetId:"specificationModel",propertyName:"specificationModel"},
//				             {widgetId:"partsName",propertyName:"partsName"}]
        }, hidden:true
		},{
			header:i18n.OutsourcingDir.specificationModel, dataIndex:'specificationModel', editor: {readOnly:true, style:"color:gray" }, searcher:{}
		},{
			header:i18n.OutsourcingDir.partsName, dataIndex:'partsName', editor: { allowBlank:false, xtype:"textfield", readOnly:true, style:"color:gray" }, searcher: {}
		},{
			header:i18n.OutsourcingDir.outsourcingFactory, dataIndex:'madeFactoryID', hidden:true,
			editor: { id:'madeFactory_Idx',  
				hiddenName: 'madeFactoryID', 
				xtype:'PartsOutSourcingFactory_SelectWin' ,
				returnField:[{widgetId:"factoryName",propertyName:"factoryName"}],
				editable:false, allowBlank:false
			}
		},{
			header:i18n.OutsourcingDir.outsourcingFactory, dataIndex:'factoryName', editor: { xtype: "hidden" }, searcher:{}
		}],
		afterShowEditWin: function(r, rowIndex){
			//Ext.getCmp("partsAccount_IDX").setDisplayValue(r.get("partsTypeIDX"), r.get("specificationModel"));
			Ext.getCmp("madeFactory_Idx").setDisplayValue(r.get("madeFactoryID"), r.get("factoryName"));
		},
		afterShowSaveWin: function(){
			Ext.getCmp("partsAccount_IDX").clearValue();
			Ext.getCmp("partsAccount_IDX").clearInvalid();
			Ext.getCmp("madeFactory_Idx").clearValue();
			Ext.getCmp("madeFactory_Idx").clearInvalid();
		}
	});
	PartsTypeSelect.submit = function(){
		
		var idxs = $yd.getSelectedIdx(PartsTypeSelect.grid);
		if(idxs.length == 0){
			MyExt.Msg.alert("尚未选择一条记录！")
			return;
		}
		PartsTypeSelect.grid.loadMask.show();
		Ext.Ajax.request({
			url: ctx + "/partsOutsourcingList!newList.action",
			params: {idxs: idxs + ""},
			success: function(response, options){
	            PartsTypeSelect.grid.loadMask.hide();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if(result.success){
	            	alertSuccess();
	            	Dir.grid.store.reload();
	            	PartsTypeSelect.selectWin.hide();
	            }else{
	            	alertFail(result.errMsg);
	            }
			},
	        failure: function(response, options){
				PartsTypeSelect.grid.loadMask.hide();
	            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	        }
		});
	}
	OutSoucingFactorySelect.submit = function(){
		var factory = OutSoucingFactorySelect.grid.selModel.getSelections();
		if(factory.length == 0){
			MyExt.Msg.alert("尚未选择一条记录！")
			return;
		}
		var idxs = $yd.getSelectedIdx(Dir.grid);
		OutSoucingFactorySelect.grid.loadMask.show();
		Ext.Ajax.request({
			url: ctx + "/partsOutsourcingList!setMadeFactory.action",
			params: {idxs: idxs + "", factory: factory[0].get("id")},
			success: function(response, options){
				OutSoucingFactorySelect.grid.loadMask.hide();
				var result = Ext.util.JSON.decode(response.responseText);
				if(result.success){
					alertSuccess();
					Dir.grid.store.reload();
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
	var viewport = new Ext.Viewport({ layout:'fit', items: Dir.grid });
});