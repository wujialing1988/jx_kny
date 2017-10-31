Ext.namespace("form")

/*
 * 基本信息标题（显示车型车号）界面表单
 */
form.titleForm =  new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "99%" },
    layout: "form",		border: false,	labelWidth: 50,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, 
            items: [
        		{ fieldLabel:"车型车号", name:"trainTypeTrainNo", width:150, style:"border:none;background:none;", readOnly:true, id:"train_Id"},
        		{ fieldLabel:"车型",    name:"trainTypeIdx", id:"train_Type", xtype:"hidden"},
        		{ fieldLabel:"车号",    name:"trainNo", id:"train_no", xtype:"hidden"},
        		{ fieldLabel:"车型简称", name:"trainSortName", id:"train_sort_Type", xtype:"hidden"},
        		{ fieldLabel:"修程",    name:"repairClassIdx", id:"repairClass_Idx", xtype:"hidden"},
        		{ fieldLabel:"修次",    name:"repairTimeIdx", id:"repairTime_Idx", xtype:"hidden"},
            	{ fieldLabel:"计划开始时间", name:"transinTimeStr", width:150, style:"border:none;background:none;", readOnly:true}                
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, 
            items: [
            	{ fieldLabel:"修程修次", name:"repairClassRepairTime", id:"xcxc", width:150, style:"border:none;background:none;", readOnly:true},
                { fieldLabel:"计划完成时间", name:"planTrainTimeStr", width:150, style:"border:none;background:none;", readOnly:true}                
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, 
            items: [
            	{ fieldLabel:"流程节点", name:"nodeCaseName", width:150, style:"border:none;background:none;", readOnly:true}  
            ]
        }]
    }]
});

/*
 * 基本信息主要界面表单
 */
form.labelWidth = 80;
form.baseForm = new Ext.form.FormPanel({
	layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: form.labelWidth, 
    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "100%" }, buttonAlign: "center",
    buttonAlign: "center",autoScroll:true,
    items:[{
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
            items: [
                { name: "workCardName", fieldLabel: "作业名称",    anchor: "90%" , readOnly:true, style:"color:gray" }
            ]
       },{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
            items: [
				{ name: "ratedWorkHours", fieldLabel: "工时（分）",  maxLength: 50,  anchor: "90%", readOnly:true, style:"color:gray"  }
            ]
       }]
    },{
	    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	    items: [{
	        baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	        labelWidth: form.labelWidth, 	 columnWidth: 1, 
	        items: [{ name: "fixPlaceFullName", fieldLabel: "位置", anchor: "95%", readOnly:true, style:"color:gray"  },
	                { name: "fixPlaceIDX", fieldLabel: "检修位置ID", xtype:'hidden'},
	                { name: "fixPlaceFullCode", fieldLabel: "检修位置CODE", xtype:'hidden'}]
       }]
    },{
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
            items: [
                { name: "workScope", fieldLabel: "描述", height:130,   anchor: "90%" , xtype:"textarea", readOnly:true, style:"color:gray" }
            ]
       },{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
            items: [
				{ name: "safeAnnouncements", fieldLabel: "安全注意事项", height:130,  xtype:"textarea",  anchor: "90%", readOnly:true, style:"color:gray"  }
            ]
       }]
    },{
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
            items: [{
            	name: "realBeginTime", fieldLabel: "开工时间", anchor: "90%", xtype:"my97date",format: "Y-m-d H:i",
			    width:200, initNow:false, my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
            },{ name: "remarks", fieldLabel: "备注", anchor: "90%", height:50, xtype:"textarea", maxLength:1000 }]
       },{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
            items: [
            	{ name: "realEndTime", fieldLabel: "完工时间", anchor: "90%", xtype:"my97date",format: "Y-m-d H:i",
		          width:200, my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
		        },
        		{ name: "idx", xtype:"hidden",fieldLabel: "主键"},
        		{ name:"fixPlaceIdx",fieldLabel: "安装位置主键", xtype:"hidden" },
        		{ name: "buildUpTypeIDX",fieldLabel: "组成型号主键", xtype:"hidden"},
        		{ name: "buildUpTypeCode",fieldLabel: "组成型号代码", xtype:"hidden"},	
        		{ fieldLabel: "安装位置全路径编码", xtype: "hidden"},
        		{ name: "specificationModel", fieldLabel: "配件规格型号", xtype:"hidden" },
        		{ name: "rdpIDX", fieldLabel: "rdpIdx", xtype:"hidden" }
            ]
       }]
    }]
});

form.createWorkerForm = function(qcList, workerList) {
	form.baseForm = new Ext.form.FormPanel({
		layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: form.labelWidth, 
	    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "100%" }, buttonAlign: "center",
	    buttonAlign: "center",autoScroll:true,
	    items:[{
	        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	        items: [{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
	            items: [
	                { name: "workCardName", fieldLabel: "作业名称",    anchor: "90%" , readOnly:true, style:"color:gray" }
	            ]
	       },{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
	            items: [
					{ name: "ratedWorkHours", fieldLabel: "工时（分）",  maxLength: 50,  anchor: "90%", readOnly:true, style:"color:gray"  }
	            ]
	       }]
	    },{
		    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
		    items: [{
		        baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		        labelWidth: form.labelWidth, 	 columnWidth: 1, 
		        items: [{ name: "fixPlaceFullName", fieldLabel: "位置", anchor: "95%", readOnly:true, style:"color:gray"  },
		                { name: "fixPlaceIDX", fieldLabel: "检修位置ID", xtype:'hidden'},
		                { name: "fixPlaceFullCode", fieldLabel: "检修位置CODE", xtype:'hidden'}]
	       }]
	    },{
	        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	        items: [{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
	            items: [
	                { name: "workScope", fieldLabel: "描述", height:130,   anchor: "90%" , xtype:"textarea", readOnly:true, style:"color:gray" }
	            ]
	       },{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
	            items: [
					{ name: "safeAnnouncements", fieldLabel: "安全注意事项", height:130,  xtype:"textarea",  anchor: "90%", readOnly:true, style:"color:gray"  }
	            ]
	       }]
	    },{
	        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	        items: [{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
	            items: [{
	            	name: "realBeginTime", fieldLabel: "开工时间", anchor: "90%", xtype:"my97date",format: "Y-m-d H:i",
				    width:200, initNow:false, my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
	            },{ name: "remarks", fieldLabel: "备注", anchor: "90%", height:50, xtype:"textarea", maxLength:1000 }]
	       },{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
	            items: [
	            	{ name: "realEndTime", fieldLabel: "完工时间", anchor: "90%", xtype:"my97date",format: "Y-m-d H:i",
			          width:200, my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
			        },					
	        		{ name: "idx", xtype:"hidden",fieldLabel: "主键"},
	        		{ name:"fixPlaceIdx",fieldLabel: "安装位置主键", xtype:"hidden" },
	        		{ name: "buildUpTypeIDX",fieldLabel: "组成型号主键", xtype:"hidden"},
	        		{ name: "buildUpTypeCode",fieldLabel: "组成型号代码", xtype:"hidden"},	
	        		{ name: "fixPlaceFullCode",fieldLabel: "安装位置全路径编码", xtype: "hidden"},
	        		{ name: "specificationModel", fieldLabel: "配件规格型号", xtype:"hidden" },
	        		{ name: "rdpIDX", fieldLabel: "rdpIdx", xtype:"hidden" }
	            ]
	       }]
	    }]
	});
	var formItems = [];
	var workerFormItems = null;
	if (workerList != null && workerList.length > 0) {
		var saveFields = [];
		for (var i = 0; i < workerList.length; i++) {
	        var field = workerList[ i ];
	        var editor = {};  //定义检验项
	        editor.xtype = "checkbox";
	        editor.name = "workerID"; //定义检验项名称规则
	        editor.boxLabel = field.workerName ;
	        editor.inputValue  = field.workerID ;
	        editor.autoWidth = true;
	        editor.checked = true;
	        saveFields.push(editor);	       
	    }
	    var workerCount = workerList.length;
	    if (Ext.getCmp("workerID_Id"))
	    	Ext.getCmp("workerID_Id").destroy();
	    workerFormItems = { xtype: 'compositefield', fieldLabel : '其他处理人员', combineErrors: false, anchor: "100%",
        					items: [{ id: 'workerID_Id', xtype:'checkboxgroup', name: "workerID", fieldLabel: '其他处理人员', items: saveFields, anchor: "95%", width: 500},
           							{ xtype: 'button', text: '全选', width: 40, scope: this,
           							  handler: function() {
           							  		var all = new Array();
           							  		for (var i = 0; i < workerCount; i++) {
										        all[i] = true;
										    }	
										    Ext.getCmp("workerID_Id").setValue(all);
           							  }
           							},
           							  { xtype: 'button', text: '全取消', width: 40, 
           							  	handler: function() {
           							  		var all = new Array();
           							  		for (var i = 0; i < workerCount; i++) {
										        all[i] = false;
										    }
										    Ext.getCmp("workerID_Id").setValue(all);
           							  }}
           						   ]}
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
	    form.baseForm = new Ext.form.FormPanel({
			layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: form.labelWidth, 
		    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "100%" }, buttonAlign: "center",
		    buttonAlign: "center",autoScroll:true,
		    items:[{
		        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
		        items: [{
		            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
		            items: [
		                { name: "workCardName", fieldLabel: "作业名称",    anchor: "90%" , readOnly:true, style:"color:gray" }
		            ]
		       },{
		            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
		            items: [
						{ name: "ratedWorkHours", fieldLabel: "工时（分）",  maxLength: 50,  anchor: "90%", readOnly:true, style:"color:gray"  }
		            ]
		       }]
		    },{
			    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
			    items: [{
			        baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
			        labelWidth: form.labelWidth, 	 columnWidth: 1, 
			        items: [{ name: "fixPlaceFullName", fieldLabel: "位置", anchor: "95%", readOnly:true, style:"color:gray"  },
			                { name: "fixPlaceIDX", fieldLabel: "检修位置ID", xtype:'hidden'},
			                { name: "fixPlaceFullCode", fieldLabel: "检修位置CODE", xtype:'hidden'}]
		       }]
		    },{
		        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
		        items: [{
		            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
		            items: [
		                { name: "workScope", fieldLabel: "描述", height:130,   anchor: "90%" , xtype:"textarea", readOnly:true, style:"color:gray" }
		            ]
		       },{
		            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
		            items: [
						{ name: "safeAnnouncements", fieldLabel: "安全注意事项", height:130,  xtype:"textarea",  anchor: "90%", readOnly:true, style:"color:gray"  }
		            ]
		       }]
		    },{
		        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
		        items: [{
		            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
		            items: [{
		            	name: "realBeginTime", fieldLabel: "开工时间", anchor: "90%", xtype:"my97date",format: "Y-m-d H:i",
					    width:200, initNow:false, my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
		            },{ name: "remarks", fieldLabel: "备注", anchor: "90%", height:50, xtype:"textarea", maxLength:1000 }]
		       },{
		            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		            labelWidth: form.labelWidth, 	 columnWidth: 0.5, 
		            items: [
		            	{ name: "realEndTime", fieldLabel: "完工时间", anchor: "90%", xtype:"my97date",format: "Y-m-d H:i",
				          width:200, my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
				        },						
		        		{ name: "idx", xtype:"hidden",fieldLabel: "主键"},
		        		{ name:"fixPlaceIdx",fieldLabel: "安装位置主键", xtype:"hidden" },
		        		{ name: "buildUpTypeIDX",fieldLabel: "组成型号主键", xtype:"hidden"},
		        		{ name: "buildUpTypeCode",fieldLabel: "组成型号代码", xtype:"hidden"},	
		        		{ name: "fixPlaceFullCode",fieldLabel: "安装位置全路径编码", xtype: "hidden"},
		        		{ name: "specificationModel", fieldLabel: "配件规格型号", xtype:"hidden" },
		        		{ name: "rdpIDX", fieldLabel: "rdpIdx", xtype:"hidden" }
		            ]
		       }]
		    },{
		        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
		        items: [{
		            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		            labelWidth: form.labelWidth, 	 columnWidth: 1, 
		            items: [
		                formItems
		            ]
		       }]
		    }]
		});
	}
}

/*
 * 作业任务（检测/修项目）界面表单
 */
form.taskForm = new Ext.form.FormPanel({
	layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: form.labelWidth, 
    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "100%" }, buttonAlign: "center",
    buttonAlign: "center",autoScroll:true,
    items:[{
	    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	    items: [{
	        baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	        labelWidth: form.labelWidth, 	 columnWidth: 1, 
	        items: [{ name: "workTaskName", fieldLabel: "检测/修项目", anchor: "95%", readOnly:true, style:"color:gray"  },
	                { name: "workTaskCode", fieldLabel: "作业项编码", xtype:"hidden" },
	                { name: "idx", fieldLabel: "作业项IDX", xtype:"hidden", id:"workTaskIdx"},
	                { name: "workStepIDX", fieldLabel: "workStepIDX", xtype:"hidden"},
	                { name: "workCardIDX", fieldLabel: "workCardIDX", xtype:"hidden"},
	                { name: "status", fieldLabel: "status", xtype:"hidden", id:"status"},
	                { name: "repairContent", fieldLabel: "作业内容", xtype:"hidden" },
	            	{ name: "repairMethod", fieldLabel: "repairMethod",  xtype:"hidden" },
	            	{ name: "repairResultIdx", fieldLabel: "repairResultIdx",  xtype:"hidden" },
	            	{ name: "workTaskSeq", fieldLabel: "workTaskSeq",  xtype:"hidden" }]
       }]
    },{
	    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	    items: [{
	        baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	        labelWidth: form.labelWidth, 	 columnWidth: 1, 
	        items: [{ name: "repairStandard", fieldLabel: "技术要求或标准规定", anchor: "95%", height:80, xtype:"textarea", readOnly:true, style:"color:gray"  }]
       }]
    },{
	    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	    items: [{
	        baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	        labelWidth: form.labelWidth, 	 columnWidth: 1, 
	        items: [{ anchor: "95%",
	            fieldLabel: '结果',
	            xtype: 'Base_combo',
	            name: 'repairResult',
	            entity:'com.yunda.jx.jxgc.repairrequirement.entity.WorkStepResult', 
	        	fields: ["resultName"],
	        	minChars:50,
	        	maxLength:50,
	        	displayField:'resultName',
	        	valueField:'resultName',
	        	forceSelection:false,
	        	editable:true,
	        	allowBlank:false
	        }]
       }]
    },{
	    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	    items: [{
	        baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	        labelWidth: form.labelWidth, 	 columnWidth: 1, 
	        items: [{ id:"itemReamrks", name: "remarks", fieldLabel: "备注", anchor: "95%", height:50, xtype:"textarea", maxLength:1000 }]
       }]
    }]
});
/*
 * 完工确认界面表单
 */
form.completeConfirm = null;


form.qcForm = null;
//创建质量控制表单
form.createForm = function(qcList){
	form.completeConfirm = new Ext.form.FormPanel({
			layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: form.labelWidth + 10, 
		    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "100%" }, buttonAlign: "center",
		    buttonAlign: "center", autoScroll:true,
		    items:[{
			    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
			    items: [{
			        baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
			        labelWidth: form.labelWidth + 10, 	 columnWidth: 1, 
			        items: [{ 
			        	name: "realBeginTime", fieldLabel: "开工时间", anchor: "93%", xtype:"my97date",format: "Y-m-d H:i",
			        	width:200, initNow:false, my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
			        }]
		       }]
		    },{
			    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
			    items: [{
			        baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
			        labelWidth: form.labelWidth + 10, 	 columnWidth: 1, 
			        items: [{
			        	 id:"endTime", name: "realEndTime", fieldLabel: "完工时间", anchor: "93%", xtype:"my97date",format: "Y-m-d H:i",
			        	width:200, my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false			        
			        }]
		       }]
		    },{
		    	xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
		    	items: [{
		    		baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		    		labelWidth: form.labelWidth + 10, 	 columnWidth: 1, 
		    		items: [{ name: "remarks", fieldLabel: "检修过程及更换件说明", anchor: "93%", xtype:"textarea", maxLength:500  }]
		    	}]
		    },{
			    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
			    items: [{
			        baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
			        labelWidth: form.labelWidth + 10, 	 columnWidth: 1, 
			        items: [{ name: "user", fieldLabel: "处理人", anchor: "93%", readOnly:true, style:"color:gray", value:uname  }]
		       }]
		    }/*,{
			    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
			    items: [{
			        baseCls: "x-plain", align: "center", layout: "form", defaultType: "numberfield", 
			        labelWidth: form.labelWidth + 10, 	 columnWidth: 1, 
			        items: [{ id: "overtime_Id", name: "overtime", fieldLabel: "加班时间(分钟)", anchor: "93%", maxLength : 5, vtype:'nonNegativeInt'  }]
		       }]
		    }*/]
		});
	if(qcList.length > 0) {
	    var formItems = [];
	    for (var i = 0; i < qcList.length; i++) {
	        var field = qcList[ i ];
	        var editor = {};  //定义检验项
	        editor.id = "checkItemId" + i;
	        editor.xtype = "TeamEmployee_SelectWin";
	        editor.fieldLabel = field.checkItemName + "指派"; //定义检验项名称规则
	        editor.hiddenName  = field.checkItemCode ;
	        editor.queryHql = "empid <> " + empId ;
	        editor.orgid = teamOrgId;
	        editor.editable = false;
	        editor.width = 200 ;  
			editor.allowBlank = false;
			editor.anchor = "93%";
	        formItems.push(editor);
	        
	    }     
	
	    form.qcForm = {
		    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
		    items: [{
		        baseCls: "x-plain", align: "center", layout: "form",  
		        labelWidth: form.labelWidth + 10, 	 columnWidth: 1, 
		        items: formItems
	       }]
	    };
	    form.completeConfirm = new Ext.form.FormPanel({
			layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: form.labelWidth + 10, 
		    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "100%" }, buttonAlign: "center",
		    buttonAlign: "center", autoScroll:true,
		    items:[{
			    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
			    items: [{
			        baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
			        labelWidth: form.labelWidth + 10, 	 columnWidth: 1, 
			        items: [{ name: "realBeginTime", fieldLabel: "开工时间", anchor: "93%", xtype:"my97date",format: "Y-m-d H:i",
			        	width:200, initNow:false, my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
			        }]
		       }]
		    },{
			    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
			    items: [{
			        baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
			        labelWidth: form.labelWidth + 10, 	 columnWidth: 1, 
			        items: [{ id:"endTime", name: "realEndTime", fieldLabel: "完工时间", anchor: "93%", xtype:"my97date",format: "Y-m-d H:i",
			        	width:200, my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
			        }]
		       }]
		    },{
		    	xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
		    	items: [{
		    		baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		    		labelWidth: form.labelWidth + 10, 	 columnWidth: 1, 
		    		items: [{ name: "remarks", fieldLabel: "检修过程及更换件说明", anchor: "93%", xtype:"textarea", maxLength:500  }]
		    	}]
		    },{
			    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
			    items: [{
			        baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
			        labelWidth: form.labelWidth + 10, 	 columnWidth: 1, 
			        items: [{ name: "user", fieldLabel: "处理人", anchor: "93%", readOnly:true, style:"color:gray", value:uname  }]
		       }]
		    }/*,{
			    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
			    items: [{
			        baseCls: "x-plain", align: "center", layout: "form", defaultType: "numberfield", 
			        labelWidth: form.labelWidth + 10, 	 columnWidth: 1, 
			        items: [{ id: "overtime_Id", name: "overtime", fieldLabel: "加班时间(分钟)", anchor: "93%", maxLength : 5, vtype:'nonNegativeInt'  }]
		       }]
		    }*/, form.qcForm]
		});	 
	}	
}
