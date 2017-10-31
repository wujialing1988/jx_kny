function append(v1, v2){
	var rtn = '';
	var appened = false;	//
	if(v1){
		rtn = v1;
		appened = true;
	}
	if(v2){
		if(appened){
			rtn += " | ";
		}
		rtn += v2;
	}
	return rtn;
}


Ext.onReady(function(){
	Ext.namespace('Base');                       //定义命名空间	
	RQWorkCard.workCardIDX = "";
	
//任务基本信息表单
	Base.baseForm = new Ext.form.FormPanel({
		align: 'center',
		layout : 'form',
		baseCls: 'x-plain',
		style : 'padding : 10px',
		defaults : { anchor : '100%'},
		labelWidth:90,
		buttonAlign : 'center', 
		items: [{
			align : 'center',
			layout : 'column',
			baseCls : 'x-plain',
			items:[{
				align : 'center',
				layout : 'form',
				defaultType : 'textfield',
				baseCls : 'x-plain',
				columnWidth : .4,
				items : [{
					name : 'workItemName', 
					fieldLabel : '质量检验项', 
					width : "99%",
					style:"border:none;background:none;",
					readOnly:true
				}]
			},{
				align : 'center',
				layout : 'form',
				defaultType : 'textfield',
				baseCls : 'x-plain',
				columnWidth : .3,
				items : [{
					name : 'trainTypeNo', 
					fieldLabel : '车型车号', 
					width : "99%",
					style:"border:none;background:none;",
					readOnly:true
				}]
			},{
				align : 'center',
				layout : 'form',
				defaultType : 'textfield',
				baseCls : 'x-plain',
				columnWidth : .3,
				items : [{
					name : 'rcrt', 
					fieldLabel : '修程修次', 
					width : "99%",
					style:"border:none;background:none;",
					readOnly:true
				}]
			}]
		},{
			align : 'center',
			layout : 'column',
			baseCls : 'x-plain',
			items:[{
				align : 'center',
				layout : 'form',
				defaultType : 'textfield',
				baseCls : 'x-plain',
				columnWidth : .4,
				items : [{
					name : 'workCardName', 
					fieldLabel : '作业工单名称', 
					width : "99%",
					style:"border:none;background:none;",
					readOnly:true
				}]
			},{
				align : 'center',
				layout : 'form',
				defaultType : 'textfield',
				baseCls : 'x-plain',
				columnWidth : .3,
				items : [{
					name : 'fixPlaceFullName', 
					fieldLabel : '位置', 
					width : "99%",
					style:"border:none;background:none;",
					readOnly:true
				}]
			}]
		}]
	});
	//检验信息表单
		Base.checkForm = new Ext.form.FormPanel({
		    layout: "form", 	border: false, 		style: "padding:2px", 		labelWidth: Base.labelWidth,
		    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "98%" },
		    items: [{
		        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
		        items: [
		    	{
		            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		            labelWidth: Base.labelWidth, 	 columnWidth: 0.5, 
		            items: [
						{ id:"checkPersonName",name: "qcEmpName", fieldLabel: "检验人员", readOnly:true, width: Base.fieldWidth }
		            ]
		    	},
		    	{
		            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		            labelWidth: Base.labelWidth, 	 columnWidth: 0.5, 
		            items: [
						{ id:'checkTime_Id', name: "qcTime", fieldLabel: "检验日期",xtype:"my97date",my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"},width:150 }
		            ]
		    	}
		        ]
		    },{
		        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
		        items: [
		    	{
		            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
		            labelWidth: Base.labelWidth, 	 columnWidth: 1, 
		            items: [
						{ name: "remarks", fieldLabel: "备注",xtype:"textarea",width: 400,maxLength:1000 }
		            ]
		    	}
		        ]
		    },{ id:"checkPersonId",name: "qcEmpID", fieldLabel: "检验人员id",xtype:"hidden"}]
		});
	//定义'fieldset'列表
	Base.fieldset = [{
		bodyStyle: 'padding-right:2px;', frame: true, border: false,
		items:{
			xtype: "fieldset",
			title: "基本信息",
			autoHeight: true,
			width : 900,
			items: Base.baseForm
		}
	},{
		bodyStyle: 'padding-left:3px;', frame: true, border: false,
		items:{
			xtype: "fieldset",
			title: "检验信息",
			autoHeight: true,
			width : 900,
			items: Base.checkForm
		}
	}];
	
	
	Base.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
            title:"基本信息", border:false, frame: true, items: Base.fieldset
        },{
            title:"作业工单信息", border:false, layout:"fit", items: [ RQWorkCard.layoutPanel ]
        },{
	        title: "检测/修项目", layout: "border", border: false, 
	        items: [{
	    		 region: 'west', layout: "fit",title:'检测/修项目',
	    		 width: 700,minSize: 500, maxSize: 800, split: true, bodyBorder: false, items:[ RQWorkTask.grid ]
	        },{
	    		 region: 'center', layout: "fit",title:'检测结果',  bodyBorder: false, items : [ QRDetectResult.grid ]
	        }]
        },{
            title: "作业人员", layout: "fit", border: false, items: [ QRWorker.grid ]            
        },{
            title: "质量检验", layout: "fit", border: false, items: [ QualityControlResult.grid ]            
        }]
	});
	
	Base.loadWorkCard = function(workcardIdx, workPlanIDX){
		jQuery.ajax({
			url: ctx + "/workCard!getCardInfo.action",
			data:{workcardIdx: workcardIdx},
			dataType:"json",
			type:"post",
			success:function(data){
				var record=new Ext.data.Record();
				for(var i in data){
					record.set(i,data[i]);
				}
				RQWorkCard.workCardIDX = record.get("idx");
				var cfg = {
			        scope: this, url: ctx + '/trainWorkPlan!getEntityByIDX.action',
			        params: {workPlanIDX: workPlanIDX},
			        success: function(response, options){
			            var result = Ext.util.JSON.decode(response.responseText);
			            var workPlanRecord = new Ext.data.Record();
			            if (result.entity != null) {		            	
			                var workPlanEntity = result.entity;
			                for(var i in result.entity){
			                	if(i == 'planBeginTime' || i == 'planEndTime'){
			                		workPlanRecord.set(i, new Date(result.entity[i]).format('Y-m-d H:i'));
			                		continue;	                    		
			                	}
			                	workPlanRecord.set(i, result.entity[i]);
			                }
			                var xcxc = append(workPlanRecord.get("repairClassName"), workPlanRecord.get("repairtimeName")) ; //修程|修次
							var cxch = append(workPlanRecord.get("trainTypeShortName"), workPlanRecord.get("trainNo")); //车型|车号
							RQWorkCard.titleForm.find("name","trainTypeTrainNo")[0].setValue(cxch);
					        RQWorkCard.titleForm.find("name","repairClassRepairTime")[0].setValue(xcxc);
						    RQWorkCard.titleForm.getForm().loadRecord(workPlanRecord);
							RQWorkCard.titleForm.show();
			            }
			        }
			    };
			    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
				//作业工单基本信息
			    RQWorkCard.baseForm.getForm().reset();
			    RQWorkCard.baseForm.getForm().loadRecord(record);
			    if(record.get("realBeginTime") != "" && record.get("realBeginTime") != null && record.get("realBeginTime") != 'undefined'){
			    	RQWorkCard.baseForm.find("name","realBeginTime")[0].setValue(new Date(record.get("realBeginTime")).format('Y-m-d H:i'));
			    }
			    if(record.get("realEndTime") != "" && record.get("realEndTime") != null && record.get("realEndTime") != 'undefined'){
			    	RQWorkCard.baseForm.find("name","realEndTime")[0].setValue(new Date(record.get("realEndTime")).format('Y-m-d H:i'));
			    }
				
				QRWorker.grid.store.load();
				QualityControlResult.grid.store.load();
				RQWorkCard.grid.store.baseParams.workCardId = workcardIdx;
				RQWorkCard.grid.store.load();
				//刷新检测/修项目Grid
				RQWorkTask.grid.store.load();
				QRDetectResult.grid.store.removeAll();
				Ext.getCmp("checkTime_Id").setValue(new Date());
			}
		});
	}
	
	Base.loadBaseInfo = function(record){
		Base.checkForm.getForm().reset();
		Base.baseForm.getForm().loadRecord(record);
		Base.baseForm.getForm().findField("trainTypeNo").setValue(append(record.get("trainType"), record.get("trainNo")));
		Base.baseForm.getForm().findField("rcrt").setValue(append(record.get("repairClassName"), record.get("repairtimeName")));
	}
	
	Ext.getCmp("checkPersonName").setValue(uname);
	Ext.getCmp("checkPersonId").setValue(empid);
});
