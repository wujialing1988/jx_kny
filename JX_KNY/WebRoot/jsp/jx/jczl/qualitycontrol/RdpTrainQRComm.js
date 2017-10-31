/**质量记录单公用界面*/
Ext.onReady(function(){
Ext.namespace('QR');                       //定义命名空间
QR.rpdIdx = "" ;						//兑现单全局变量
/**
 * Form定义
 */
QR.titleForm =  new Ext.form.FormPanel({
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
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, 
            items: [
                
                { fieldLabel:"检修部门", name:"undertakeDepName", width:150, style:"border:none;background:none;", readOnly:true}    
            ]
        }]
    }]
});


//质量记录单查询结果窗口
QR.saveWin = new Ext.Window({
	title: "质量记录单结果列表", maximizable:false, layout: "fit", 
	closeAction: "hide", modal: true, maximized: true , buttonAlign:"center",
	items: {
		xtype: "panel", layout: "border",
		items:[{
            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',collapsible:true, height: 82, bodyBorder: false,items:[QR.titleForm]
        },{
            region : 'center', layout : 'fit', bodyBorder: false, items : [RQWorkCard.grid]
        }]
	},
	buttons:[{
		text: "关闭", iconCls: "closeIcon", handler: function(){QR.saveWin.hide();}
	}]
});


//机车质量记录单结果双击显示界面
QR.toEditFn = function(grid, rowIndex, e){
	if(this.searchWin != null)  this.searchWin.hide();  
	var record = grid.store.getAt(rowIndex);
	QR.rpdIdx = record.get("idx") ;  //施修任务兑现单主键
	RQWorkCard.grid.store.load();  //刷新作业工单信息
    QR.saveWin.show();
    //质量记录单界面显示信息
    QR.titleForm.getForm().reset();
    QR.titleForm.getForm().loadRecord(record);
    Ext.getCmp("transInTime_Id").setValue(new Date(record.get("transInTime")).format('Y-m-d H:i'));
    Ext.getCmp("planTrainTime_Id").setValue(new Date(record.get("planTrainTime")).format('Y-m-d H:i'));
	//QR.titleForm.find("name","undertakeDepName")[0].setValue(record.get("undertakeDepName"))
    var cxch = record.get("trainTypeShortName") +" | " +record.get("trainNo") ; //车型|车号
    var xcxc = record.get("repairClassName") +" | " +record.get("repairtimeName") ; //车型|车号
    Ext.getCmp("train_Id").setValue(cxch); 
    Ext.getCmp("xcxc").setValue(xcxc); 
	//作业工单界面显示信息    
    RQWorkCard.titleForm.getForm().reset();
    RQWorkCard.titleForm.getForm().loadRecord(record);
    RQWorkCard.titleForm.find("name","trainTypeTrainNo")[0].setValue(cxch);
    RQWorkCard.titleForm.find("name","repairClassRepairTime")[0].setValue(xcxc);
    RQWorkCard.titleForm.find("name","transInTime")[0].setValue(new Date(record.get("transInTime")).format('Y-m-d H:i'));
    RQWorkCard.titleForm.find("name","planTrainTime")[0].setValue(new Date(record.get("planTrainTime")).format('Y-m-d H:i'));
}
	
});