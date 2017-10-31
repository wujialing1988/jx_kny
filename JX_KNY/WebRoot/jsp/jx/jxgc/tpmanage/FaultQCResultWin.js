/**
 * 提票质量检查参与者 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('FaultQCResultWin');                       //定义命名空间
	FaultQCResultWin.labelWidth = 90;
	FaultQCResultWin.fieldWidth = 150;
	FaultQCResultWin.baseInfoForm =  new Ext.form.FormPanel({
		baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "99%" },
	    layout: "form",		border: false,	labelWidth: 50,
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	        		{ fieldLabel:"车型车号", name:"trainTypeTrainNo", width:150, style:"border:none;background:none;", readOnly:true},
	            	{ fieldLabel:"故障现象", name:"faultName", width:150, style:"border:none;background:none;", readOnly:true},                
	            	{ fieldLabel:"故障描述", name:"faultDesc", width:150, style:"border:none;background:none;", readOnly:true}                
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	            	{ fieldLabel:"提票类型", name:"type", width:150, style:"border:none;background:none;", readOnly:true},
	                { fieldLabel:"故障发生日期", name:"faultOccurDate", width:150, style:"border:none;background:none;", readOnly:true}                
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	            	{ fieldLabel:"故障位置", name:"fixPlaceFullName", width:150, style:"border:none;background:none;", readOnly:true},  
	            	{ fieldLabel:"故障提报人", name:"ticketEmp", width:150, style:"border:none;background:none;", readOnly:true}  
	            ]
	        }]
	    }]
	});
	FaultQCResultWin.handleForm =  new Ext.form.FormPanel({
		baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "99%" },
	    layout: "form",		border: false,	labelWidth: 50,
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	        		{ fieldLabel:"处理方法", name:"methodName", width:150, style:"border:none;background:none;", readOnly:true},
	            	{ fieldLabel:"修竣时间", name:"completeTime", width:150, style:"border:none;background:none;", readOnly:true}                
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	            	{ fieldLabel:"处理方法描述", name:"methodDesc", width:150, style:"border:none;background:none;", readOnly:true},
	                { fieldLabel:"处理人", name:"completeEmp", width:150, style:"border:none;background:none;", readOnly:true}                
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	            	{ name: "idx", xtype:"hidden"},
	            	{ name: "tpIDX", xtype:"hidden"},
	            	{ name: "checkItemCode", xtype:"hidden"}
	            ]
	        }]
	    }]
	});
	FaultQCResultWin.qcForm =  new Ext.form.FormPanel({
		baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "99%" },
	    layout: "form",		border: false,	labelWidth: 50,
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	        		{ fieldLabel:"检验项", name:"checkItemName", width:150, style:"border:none;background:none;", readOnly:true}
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	            	{ fieldLabel:"检验人员", name:"qcEmpName", width:150, style:"border:none;background:none;", readOnly:true },
	            	{ name: "qcEmpID", fieldLabel: "检验人员id",xtype:"hidden"}
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	            	{ name: "qcTime", fieldLabel: "检验时间",xtype:"my97date",my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"},width:150, allowBlank: false }  
	            ]
	        }]
	    },{
		        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
		        items: [
		    	{
		            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		            labelWidth: FaultQCResultWin.labelWidth, 	 columnWidth: 1, 
		            items: [
						{ name: "remarks", fieldLabel: "备注",xtype:"textarea",width: 400,maxLength:200 }
		            ]
		    	}]
		}]
	});
	
	FaultQCResultWin.showWin = function(record) {
		var baseInfoForm = FaultQCResultWin.baseInfoForm.getForm();
		baseInfoForm.reset();
		baseInfoForm.loadRecord(record);
		baseInfoForm.findField("trainTypeTrainNo").setValue(record.get("trainTypeShortName") + "|" + record.get("trainNo"));
		var handleForm = FaultQCResultWin.handleForm.getForm();
		handleForm.reset();
		handleForm.loadRecord(record);
		
		var qcForm = FaultQCResultWin.qcForm.getForm();
		qcForm.loadRecord(record);
		qcForm.findField("qcEmpID").setValue(empid);
		qcForm.findField("qcEmpName").setValue(uname);
		qcForm.findField("remarks").setValue("");
		FaultQCResultWin.win.show();
	}
	FaultQCResultWin.saveFn = function() {
		var qcForm = FaultQCResultWin.qcForm.getForm();
		if (!qcForm.isValid()) return;		
		Ext.Msg.confirm("提示","确认提交", function(btn){
			if(btn == "yes"){				
				var data = qcForm.getValues();
				var qcDatas = [];
				var handleForm = FaultQCResultWin.handleForm.getForm();
				data.idx = handleForm.findField("idx").getValue();
				data.tpIDX = handleForm.findField("tpIDX").getValue();
				data.checkItemCode = handleForm.findField("checkItemCode").getValue();
				delete data.checkItemName;
				qcDatas.push(data);
				 
				Ext.Ajax.request({
			    	url: ctx +"/faultQCResult!updateFinishQCResult.action",
			        jsonData: qcDatas,
			        success: function(response, options){
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.success) {
			            	alertSuccess();
			            	FaultQCResultWin.win.hide();
			            	FaultQCResultWin.reloadGrid();
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
	FaultQCResultWin.reloadGrid = function(){
		FaultQCResult.BJGrid.store.reload();
		FaultQCResult.CJGrid.store.reload();
	}
	FaultQCResultWin.panel = new Ext.Panel({
		layout: "fit",	border: false, style: "padding:2px", labelWidth: FaultQCResultWin.labelWidth,
		align: "center",defaults: { anchor: "98%" }, frame: true, buttonAlign:"center", autoScroll: true,
		items:[{
	    		xtype: "fieldset", title: "提报信息",  autoHeight: true, collapsible : true, 
	    		items: {
					layout: "form", items: FaultQCResultWin.baseInfoForm
				}
			},{
	    		xtype: "fieldset", title: "处理信息",  autoHeight: true, collapsible : true, 
	    		items: {
					layout: "form", items: FaultQCResultWin.handleForm
				}
			},{			
	    		xtype: "fieldset", title: "质检信息",  autoHeight: true, collapsible : true, 
	    		items: {
					layout: "form", items: FaultQCResultWin.qcForm
				}
			}],
	    buttons: [{
	        text: "确认任务", iconCls: "saveIcon", handler: FaultQCResultWin.saveFn
	    }, {
	        text: "关闭", iconCls: "closeIcon", handler: function(){ FaultQCResultWin.win.hide(); }
	    }]
	});
	FaultQCResultWin.win = new Ext.Window({
	    title:"故障提票质量检验", width: (FaultQCResultWin.labelWidth + FaultQCResultWin.fieldWidth + 8) * 2 + 60, 
	    plain:true, closeAction:"hide", buttonAlign:'center', maximized:true, layout: 'fit',
	    items:FaultQCResultWin.panel, autoScroll : true
	});
});