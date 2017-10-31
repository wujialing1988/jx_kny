/**兑现单信息公用界面*/
Ext.onReady(function(){
	Ext.namespace('TrainComm');                       //定义命名空间
	TrainComm.rpdIdx = "" ;						//兑现单全局变量
	/**
	 * 检修基本信息
	 */
	TrainComm.titleForm =  new Ext.form.FormPanel({
		baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,	labelWidth: 50,
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	        		{ fieldLabel:"车型车号", name:"trainTypeTrainNo", width:150, style:"border:none;background:none;", readOnly:true, id:"train_Id"},
	        		{ fieldLabel:"车型", name:"trainTypeIdx", id:"train_Type", xtype:"hidden"},
	        		{ fieldLabel:"车号", name:"trainNo", id:"train_no", xtype:"hidden"},
	        		{ fieldLabel:"车型简称", name:"trainTypeShortName", id:"train_sort_Type", xtype:"hidden"},
	        		{ fieldLabel:"进车时间", id:'transInTime_Id', name:"transInTime", width:150, style:"border:none;background:none;", readOnly:true}
	            ]
	        },                {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	            	{ fieldLabel:"修程", name:"repairClassIDX", id:"repairClass_Idx", xtype:"hidden"},
	        		{ fieldLabel:"修次", name:"repairTimeIdx", id:"repairTime_Idx", xtype:"hidden"},
	                { fieldLabel:"修程修次", name:"repairClassRepairTime", id:"xcxc", width:150, style:"border:none;background:none;", readOnly:true},
	                { fieldLabel:"计划完成时间", id:'planTrainTime_Id',name:"planTrainTime", width:150, style:"border:none;background:none;", readOnly:true}
	            ]
	        }]
	    }]
	});
	
	
	//质量记录单查询结果窗口
	TrainComm.saveWin = new Ext.Window({
		title: "材料消耗编辑", maximizable:false, layout: "fit", 
		closeAction: "hide", modal: true, maximized: true , buttonAlign:"center",
		items: {
			xtype: "panel", layout: "border",
			items:[{
	            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',collapsible:true, height: 82, bodyBorder: false,items:[TrainComm.titleForm]
	        },{
	            region : 'center', layout : 'fit', bodyBorder: false, items : [ MatConsumeInfo.grid ]
	        }]
		},
		buttons:[{
			text: "关闭", iconCls: "closeIcon", handler: function(){TrainComm.saveWin.hide();}
		}]
	});
	
	
	//材料消耗编辑显示界面
	TrainComm.toEditFn = function(record){
		//if(this.searchWin != null)  this.searchWin.hide();	
		dataParam = {};
	    dataParam.rdpIDX = record.get("idx");
	    dataParam.trainTypeIDX = record.get("trainTypeIDX");
	    dataParam.trainNo = record.get("trainNo");
	    dataParam.trainTypeShortName = record.get("trainTypeShortName");
	    dataParam.repairClassIDX = record.get("repairClassIDX");
	    dataParam.repairClassName = record.get("repairClassName");
	    dataParam.repairtimeIDX = record.get("repairtimeIDX");
	    dataParam.repairtimeName = record.get("repairtimeName");
	    dataParam.repairOrgID = teamOrgId;
		MatConsumeInfo.grid.store.load();  //刷新作业工单信息
	    TrainComm.saveWin.show();
	    //质量记录单界面显示信息
	    TrainComm.titleForm.getForm().reset();
	    TrainComm.titleForm.getForm().loadRecord(record);
	    Ext.getCmp("transInTime_Id").setValue(new Date(record.get("transInTime")).format('Y-m-d H:i'));
	    Ext.getCmp("planTrainTime_Id").setValue(new Date(record.get("planTrainTime")).format('Y-m-d H:i'));
	    var cxch = record.get("trainTypeShortName") +" | " +record.get("trainNo") ; //车型|车号
	    var xcxc = record.get("repairClassName") +" | " +record.get("repairtimeName") ; //车型|车号
	    Ext.getCmp("train_Id").setValue(cxch); 
	    Ext.getCmp("xcxc").setValue(xcxc);	
	    
	}
});