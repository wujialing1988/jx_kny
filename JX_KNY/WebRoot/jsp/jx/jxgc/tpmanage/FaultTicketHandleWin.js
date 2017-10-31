Ext.onReady(function(){
	Ext.namespace('FaultTicketHandleWin');                       //定义命名空间
	FaultTicketHandleWin.labelWidth = 110;
	FaultTicketHandleWin.titleForm =  new Ext.form.FormPanel({
		baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "99%" },
	    layout: "form",		border: false,	labelWidth: 50,
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	        		{ fieldLabel:"车型车号", name:"trainTypeTrainNo", width:150, style:"border:none;background:none;", readOnly:true} ,                
	            	{ fieldLabel:"故障描述", name:"faultDesc", width:150, style:"border:none;background:none;", readOnly:true}        
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [
	            	{ fieldLabel:"提票类型", name:"type", width:150, style:"border:none;background:none;", readOnly:true} ,
	                { fieldLabel:"故障发生日期", name:"faultOccurDate", width:150, style:"border:none;background:none;", readOnly:true}                
	            ]
	        },{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
	            columnWidth: 0.33, 
	            items: [	            	
	            	{ fieldLabel:"故障提报人", name:"ticketEmp", width:150, style:"border:none;background:none;", readOnly:true} 
	            ]
	        }]
	    }]
	});
	
	FaultTicketHandleWin.baseForm = new Ext.form.FormPanel({
		layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: FaultTicketHandleWin.labelWidth, 
	    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "100%" }, buttonAlign: "center",
	    buttonAlign: "center",autoScroll:true,
	    items:[{
			        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center",     		
			        items: [{
			            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
			            labelWidth: FaultTicketHandleWin.labelWidth, 	 columnWidth: 0.5, 
			            items: [
			                {fieldLabel:"故障位置", name:"fixPlaceFullName", anchor: "90%", maxLength: 500, allowBlank: false},
			                { xtype: 'Base_combo',	fieldLabel: '处理方法',
							  hiddenName: 'methodID', returnField: [{widgetId:"methodName_Id",propertyName:"methodName"}],
							  displayField: 'methodName', valueField: 'methodID',
							  entity: 'com.yunda.jx.base.jcgy.entity.FaultMethod', 
							  fields: ["methodID","methodName"], anchor:"90%"},
							{ id: "methodName_Id", name: "methodName", xtype: "hidden"  },
							{ name: "methodDesc", fieldLabel: "处理方法描述",  maxLength: 500,  anchor: "90%", xtype:"textarea"  },
							{
				            	name: "completeTime", fieldLabel: "修竣时间", anchor: "90%", xtype:"my97date",format: "Y-m-d H:i",
							    width:200, my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
				            },{ name: "idx", xtype:"hidden",fieldLabel: "主键"}
			            ]
			       },{
			            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
			            labelWidth: FaultTicketHandleWin.labelWidth, 	 columnWidth: 0.5, 
			            items: [
			            	{ fieldLabel:"故障现象", name:"faultName", anchor: "90%", maxLength: 500, allowBlank: false },
							{
								anchor: "90%",
					            xtype: "ProfessionalType_comboTree", 
					            fieldLabel: "专业类型",
					            hiddenName: "professionalTypeIdx", 
					            returnField: [
					            	{widgetId : "professionalTypeName_1", propertyName : "text"}		            	
					            ], 
					            selectNodeModel: "all"
			                },{ 
			                	id : 'professionalTypeName_1', name: "professionalTypeName", fieldLabel: "专业类型名称",  maxLength: 50, style:"color:gray", hidden :true
			                },
			                { name: "repairResult", fieldLabel: "处理结果", maxLength: 500,  anchor: "90%" , xtype:"textarea", allowBlank:false}
			            ]
			       }]}]
	});
	
	FaultTicketHandleWin.createForm = function(qcList, workerList) {
		FaultTicketHandleWin.baseForm = new Ext.form.FormPanel({
			layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: FaultTicketHandleWin.labelWidth, 
		    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "100%" }, buttonAlign: "center",
		    buttonAlign: "center",autoScroll:true,
		    items:[{
			        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center",     		
			        items: [{
			            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
			            labelWidth: FaultTicketHandleWin.labelWidth, 	 columnWidth: 0.5, 
			            items: [
			                {fieldLabel:"故障位置", name:"fixPlaceFullName", anchor: "90%", maxLength: 500, allowBlank: false},
			                { xtype: 'Base_combo',	fieldLabel: '处理方法',
							  hiddenName: 'methodID', returnField: [{widgetId:"methodName_Id1",propertyName:"methodName"}],
							  displayField: 'methodName', valueField: 'methodID',
							  entity: 'com.yunda.jx.base.jcgy.entity.FaultMethod', 
							  fields: ["methodID","methodName"], anchor:"90%"},
							{ id: "methodName_Id1", name: "methodName", xtype: "hidden"  },
							{ name: "methodDesc", fieldLabel: "处理方法描述",  maxLength: 500,  anchor: "90%", xtype:"textarea"  },
							{
				            	name: "completeTime", fieldLabel: "修竣时间", anchor: "90%", xtype:"my97date",format: "Y-m-d H:i",
							    width:200, my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
				            },{ name: "idx", xtype:"hidden",fieldLabel: "主键"}
			            ]
			       },{
			            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
			            labelWidth: FaultTicketHandleWin.labelWidth, 	 columnWidth: 0.5, 
			            items: [
			            	{ fieldLabel:"故障现象", name:"faultName", anchor: "90%", maxLength: 500, allowBlank: false },
							{
								anchor: "90%",
					            xtype: "ProfessionalType_comboTree", 
					            fieldLabel: "专业类型",
					            hiddenName: "professionalTypeIdx", 
					            returnField: [
					            	{widgetId : "professionalTypeName_2", propertyName : "text"}		            	
					            ], 
					            selectNodeModel: "all"
			                },{ 
			                	id : 'professionalTypeName_2', name: "professionalTypeName", fieldLabel: "专业类型名称",  maxLength: 50, style:"color:gray", hidden :true
			                },
			                { name: "repairResult", fieldLabel: "处理结果", maxLength: 500,  anchor: "90%" , xtype:"textarea", allowBlank:false}
			            ]
			       }]}]
		});
		
		var formItems = [];
		var workerFormItems = null;
		if (workerList != null && workerList.length > 0) {
			var saveFields = [];
			for (var i = 0; i < workerList.length; i++) {
		        var field = workerList[ i ];
		        var editor = {};  //定义检验项
		        editor.xtype = "checkbox";
		        editor.name = "completeEmp"; //定义检验项名称规则
		        editor.boxLabel = field.workerName ;
		        editor.inputValue  = field.workerID ;
		        editor.autoWidth = true;
		        saveFields.push(editor);	       
		    }	    
		    workerFormItems = { xtype:'checkboxgroup', name: "workerID", fieldLabel: '其他处理人员', items: saveFields, anchor: "98%"}; 
		}
		if (workerFormItems != null)
			formItems.push(workerFormItems);
		if(qcList != null && qcList.length > 0) {	    
		    for (var i = 0; i < qcList.length; i++) {
		        var field = qcList[ i ];
		        var editor = {};  //定义检验项
		        editor.xtype = "TeamEmployee_SelectWin";
		        editor.fieldLabel = field.checkItemName + "指派"; //定义检验项名称规则
		        editor.hiddenName  = field.checkItemCode ;
		        editor.orgid = teamOrgId;
		        editor.editable = false;
		        editor.width = 200 ;  
				editor.allowBlank = false;
				editor.anchor = "40%";
		        formItems.push(editor);	        
		    } 
		}
		if (formItems.length > 0) {
			FaultTicketHandleWin.baseForm = new Ext.form.FormPanel({
				layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: FaultTicketHandleWin.labelWidth, 
			    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "100%" }, buttonAlign: "center",
			    buttonAlign: "center",autoScroll:true,
			    items:[{
			        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center",     		
			        items: [{
			            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
			            labelWidth: FaultTicketHandleWin.labelWidth, 	 columnWidth: 0.5, 
			            items: [
			                {fieldLabel:"故障位置", name:"fixPlaceFullName", anchor: "90%", maxLength: 500, allowBlank: false},
			                { xtype: 'Base_combo',	fieldLabel: '处理方法',
							  hiddenName: 'methodID', returnField: [{widgetId:"methodName_Id2",propertyName:"methodName"}],
							  displayField: 'methodName', valueField: 'methodID',
							  entity: 'com.yunda.jx.base.jcgy.entity.FaultMethod', 
							  fields: ["methodID","methodName"], anchor:"90%"},
							{ id: "methodName_Id2", name: "methodName", xtype: "hidden"  },
							{ name: "methodDesc", fieldLabel: "处理方法描述",  maxLength: 500,  anchor: "90%", xtype:"textarea"  },
							{
				            	name: "completeTime", fieldLabel: "修竣时间", anchor: "90%", xtype:"my97date",format: "Y-m-d H:i",
							    width:200, my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
				            },{ name: "idx", xtype:"hidden",fieldLabel: "主键"}
			            ]
			       },{
			            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
			            labelWidth: FaultTicketHandleWin.labelWidth, 	 columnWidth: 0.5, 
			            items: [
			            	{ fieldLabel:"故障现象", name:"faultName", anchor: "90%", maxLength: 500, allowBlank: false },
							{
								anchor: "90%",
					            xtype: "ProfessionalType_comboTree", 
					            fieldLabel: "专业类型",
					            hiddenName: "professionalTypeIdx", 
					            returnField: [
					            	{widgetId : "professionalTypeName_3", propertyName : "text"}		            	
					            ], 
					            selectNodeModel: "all"
			                },{ 
			                	id : 'professionalTypeName_3', name: "professionalTypeName", fieldLabel: "专业类型名称",  maxLength: 50, style:"color:gray", hidden :true
			                },
			                { name: "repairResult", fieldLabel: "处理结果", maxLength: 500,  anchor: "90%" , xtype:"textarea", allowBlank:false}
			            ]
			       }]},
			        {
			        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
			        items: [{
			            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
			            labelWidth: FaultTicketHandleWin.labelWidth, 	 columnWidth: 0.5, 
			            items: [
			                formItems
			            ]
			       }]
			    }]
			});
		}
		FaultTicketHandleWin.baseForm.getForm().findField("methodID").on("select", function(combo, record){
			var methodDesc = FaultTicketHandleWin.baseForm.getForm().findField("methodDesc");
			methodDesc.setValue(record.get("methodName"));				
		})
	}
	
	FaultTicketHandleWin.win = null;
	FaultTicketHandleWin.createWin = function() {
		FaultTicketHandleWin.panel = new Ext.Panel({
			frame:true, autoScroll:true,layout: "border",buttonAlign:"center",
			items:[{
	            region: 'north', title:'提报信息',
	            layout: "fit",frame:true,collapsible:true, 
	            height: 150, bodyBorder: false,
	            items:[FaultTicketHandleWin.titleForm]
	        },{
	            region : 'center',autoScroll:true,frame:true, layout : 'fit', bodyBorder: false, items : [ FaultTicketHandleWin.baseForm ]
	        }],
        	buttons: [{
        		text: "确认销票", iconCls: "saveIcon", handler: FaultTicketHandle.submit
        	},{
				text: "关闭", iconCls:"closeIcon", handler:function(){ FaultTicketHandleWin.win.hide();}
			}]
        });
		FaultTicketHandleWin.win = new Ext.Window({
			title:"故障提票处理", width:800, height:500, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
			items: FaultTicketHandleWin.panel , modal:true ,maximized : true
		});
	}
});