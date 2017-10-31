/**
 * 设置送出周期和检修周期form
 */
Ext.onReady(function(){
	Ext.namespace('OutSourcingCatalogForm');                       //定义命名空间
	//送出周期form
	OutSourcingCatalogForm.outForm = new Ext.form.FormPanel({
	    layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: 100,
	    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "98%" },
	    items: [{
	        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	        items: [
	        	{
		            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		            labelWidth: 100, 	 columnWidth: 1, 
							items: [{
								xtype: 'compositefield', fieldLabel: '下车送出周期', combineErrors: false, anchor: '100%',
								items: [{
									xtype: 'numberfield', name: 'outDay', vtype: "positiveInt", width: 70
								}, {
									xtype: 'label',
									text: '天',
									style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
								}, {
									xtype: 'numberfield', name: 'outHour',vtype: "positiveInt", width: 70
								}, {
									xtype: 'label',
									text: '小时',
									style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
								}]
							}]
	        	}
	        ]
	    }]
	});
	//定义点击确定按钮的操作
	OutSourcingCatalogForm.outSubmit = function(){
		var form = OutSourcingCatalogForm.outForm.getForm(); 
        if (!form.isValid()) return;
        var data = form.getValues();
        if("" == data.outDay && "" == data.outHour){
       	   MyExt.Msg.alert("没有可保存的数据！");
       	   return ;
        }
        var outCycle = 0 ;
        if("" != data.outDay) outCycle = outCycle + parseInt(data.outDay * 24) ;
        if("" != data.outHour) outCycle = outCycle + parseInt(data.outHour) ;
		var idxs = $yd.getSelectedIdx(OutsourcingCatalog.grid);
		Ext.Ajax.request({
			url: ctx + "/partsOutsourcingCatalog!setOutCycle.action",
			params: {idxs: idxs + "", outCycle: outCycle},
			success: function(response, options){
				var result = Ext.util.JSON.decode(response.responseText);
				if(result.success){
					alertSuccess();
					OutsourcingCatalog.grid.store.reload();
					OutSourcingCatalogForm.outWin.hide();
				}else{
					alertFail(result.errMsg);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	
	//设置送出周期窗口
	OutSourcingCatalogForm.outWin = new Ext.Window({
		title:"下车送出周期", width:400, height:150, plain:true, closeAction:"hide", buttonAlign:'center',
    	maximizable:false, items:[ OutSourcingCatalogForm.outForm ], modal:true,
    	buttons: [{
			text : "确定",iconCls : "saveIcon", handler: function(){
				OutSourcingCatalogForm.outSubmit(); 
			}
		},{
	        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ OutSourcingCatalogForm.outWin.hide(); }
		}]
	});
	
	//检修周期form
	OutSourcingCatalogForm.repairForm = new Ext.form.FormPanel({
	    layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: 100,
	    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "98%" },
	    items: [{
	        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	        items: [
	        	{
		            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		            labelWidth: 100, 	 columnWidth: 1, 
							items: [{
								xtype: 'compositefield', fieldLabel: '检修周期', combineErrors: false, anchor: '100%',
								items: [{
									xtype: 'numberfield', name: 'repairDay', vtype: "positiveInt", width: 70
								}, {
									xtype: 'label',
									text: '天',
									style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
								}, {
									xtype: 'numberfield', name: 'repairHour',vtype: "positiveInt", width: 70
								}, {
									xtype: 'label',
									text: '小时',
									style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
								}]
							}]
	        	}
	        ]
	    }]
	});
	//定义点击确定按钮的操作
	OutSourcingCatalogForm.repairSubmit = function(){
		var form = OutSourcingCatalogForm.repairForm.getForm(); 
        if (!form.isValid()) return;
        var data = form.getValues();
        if("" == data.repairDay && "" == data.repairHour){
       	   MyExt.Msg.alert("没有可保存的数据！");
       	   return ;
        }
        var repairCycle = 0 ;
        if("" != data.repairDay) repairCycle = repairCycle + parseInt(data.repairDay * 24) ;
        if("" != data.repairHour) repairCycle = repairCycle + parseInt(data.repairHour) ;
		var idxs = $yd.getSelectedIdx(OutsourcingCatalog.grid);
		Ext.Ajax.request({
			url: ctx + "/partsOutsourcingCatalog!setRepairCycle.action",
			params: {idxs: idxs + "", repairCycle: repairCycle},
			success: function(response, options){
				var result = Ext.util.JSON.decode(response.responseText);
				if(result.success){
					alertSuccess();
					OutsourcingCatalog.grid.store.reload();
					OutSourcingCatalogForm.repairWin.hide();
				}else{
					alertFail(result.errMsg);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	
	//设置检修周期窗口
	OutSourcingCatalogForm.repairWin = new Ext.Window({
		title:"检修周期", width:400, height:150, plain:true, closeAction:"hide", buttonAlign:'center',
    	maximizable:false, items:[ OutSourcingCatalogForm.repairForm ], modal:true,
    	buttons: [{
			text : "确定",iconCls : "saveIcon", handler: function(){
				OutSourcingCatalogForm.repairSubmit(); 
			}
		},{
	        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ OutSourcingCatalogForm.repairWin.hide(); }
		}]
	});
});