/**
 * 故障提票form页面js
 */
function showtip(){
	processTips = new Ext.LoadMask(document.body,{
		msg:"正在处理你的操作，请稍后...",
		removeMask:true
	});
	processTips.show();
}

function hidetip(){
	processTips.hide();
}

Ext.onReady(function(){
	Ext.namespace('FaultTicketInfoWin');                       //定义命名空间
	FaultTicketInfoWin.fieldWidth = 180;
	FaultTicketInfoWin.labelWidth = 80;
	
	
	
	//故障提票编辑页面form
	FaultTicketInfoWin.baseInfoForm =  new Ext.form.FormPanel({
		baseCls: "x-plain", align: "center",	style: "padding:15px", defaultType: "textfield",	defaults: { anchor: "99%" },
	    layout: "form",		border: false,	labelWidth: 50,
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	        		{ fieldLabel:"车型车号", name:"trainTypeTrainNo", width:150,  readOnly:true},
	        	    { fieldLabel:"提报人", name:"ticketEmp", width:150, readOnly:true}         	            	            
	            ]	      
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [	        	
	        		{ fieldLabel:"提票类型", name:"type", width:150, readOnly:true},
	        		{ fieldLabel:"提报时间", name:"ticketTime", width:150, xtype:"my97date",my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, readOnly:true}
	                       	            	            
	            ]	      
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [	        	
	            	{ fieldLabel:"当前状态", name:"status", width:150,  readOnly:true}                	            	            
	            ]	      
          },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textarea", labelWidth:80,
	            columnWidth: 1, 
	            items: [	            
	              { fieldLabel:"不良状态描述", name:"faultDesc", width:640, height:100, xtype:"textarea", readOnly:true},                   
	            	{ fieldLabel:"施修方案", name:"faultReason", width:640,height:100,  readOnly:true},
	            	{ fieldLabel:"处理方法", name:"methodName", width:640,height:100, readOnly:true},
	                { fieldLabel:"标签", name:"reasonAnalysis", width:640, height:50, readOnly:true}                
	            ]	 
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	            	{ fieldLabel:"责任部门", name:"repairTeamName", width:150, readOnly:true}

	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	
	                { fieldLabel:"处理人", name:"completeEmp", width:150,  readOnly:true} 
 
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [ 
	                { fieldLabel:"完成时间", name:"completeTime", width:150, xtype:"my97date",my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, readOnly:true}  
	            ]
	        }]
	    }]
	});
	
	FaultTicketInfoWin.showWin = function(record) {
		var baseInfoForm = FaultTicketInfoWin.baseInfoForm.getForm();
		baseInfoForm.reset();
		baseInfoForm.loadRecord(record);
		baseInfoForm.findField("trainTypeTrainNo").setValue(record.get("trainTypeShortName") + " " + record.get("trainNo"));
		var methodName = record.get("methodName");
		var methodDesc = record.get("methodDesc");
		if(null == methodName){
			var methodName ="";
		}
		if(null == methodDesc){
			var methodDesc ="";
		}
		baseInfoForm.findField("methodName").setValue(methodName + " " + methodDesc);
		switch(record.get("status")){            	
                case STATUS_DRAFT:
                   baseInfoForm.findField("status").setValue(STATUS_DRAFT_CH);break;
                case STATUS_OPEN:
                     baseInfoForm.findField("status").setValue(STATUS_OPEN_CH);break;
                case STATUS_OVER:
                     baseInfoForm.findField("status").setValue(STATUS_OVER_CH);break;
            }
		FaultTicketInfoWin.win.show();
	}
	
	FaultTicketInfoWin.win = new Ext.Window({
	    title:"提票详情", width: 800, height:550, //maximized:true,
	    plain:true, closeAction:"hide", buttonAlign:'center',  layout: 'fit',
	    items:FaultTicketInfoWin.baseInfoForm, autoScroll : true
	});
});