/**
 * 作业工单查看界面布局，公用JS页面，此页面提供布局显示和界面信息
 * 基本信息、检测/修项目、作业人员查看唯一入口JS页面
 * 需要在调用界面动态覆盖grid方法，方法如下：
 * toEditFn: function(grid, rowIndex, e){
 * 		var record = grid.store.getAt(rowIndex);
		RQWorkCard.workCardIDX = record.get("idx");  //设置作业卡主键
        RQWorkCard.toWorkCardEditFn(grid, rowIndex, e);  
    //注意grid上面需要有车型、车号、等信息
    (
		车型、trainTypeShortName
		车号、trainNo
		修程、repairClassName
		修次、repairtimeName
		检修部门（承修部门）、undertakeDepName
		进车时间（转入时间），transInTime
		计划完成时间	）planTrainTime,
		作业卡主键 ***** （作业任务、作业人员等信息）RQWorkCard.workCardIDX = record.get("idx");  //设置作业卡主键
	）
    }
 */

//统一命名空间
RQWorkCard = {};

RQWorkCard.workCardIDX = "" ;					  //作业工单主键（作业卡）（内部使用）
RQWorkCard.attachmentKeyIDX = "" ; //作业工单主键(用于报表查看)（内部使用）

//作业工单基本信息TitleForm
RQWorkCard.titleForm =  new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,	labelWidth: 50,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, 
            items: [
        		{ fieldLabel:"车型车号", name:"trainTypeTrainNo", width:150, style:"border:none;background:none;", readOnly:true},
        		{ fieldLabel:"车型", name:"trainTypeIdx", xtype:"hidden"},
        		{ fieldLabel:"车号", name:"trainNo", xtype:"hidden"},
        		{ fieldLabel:"车型简称", name:"trainTypeShortName",  xtype:"hidden"},
        		{ fieldLabel:"进车时间", name:"transInTime", width:150, style:"border:none;background:none;", readOnly:true}
            ]
        },                {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, 
            items: [
            	{ fieldLabel:"修程", name:"repairClassIDX", xtype:"hidden"},
        		{ fieldLabel:"修次", name:"repairTimeIdx", xtype:"hidden"},
                { fieldLabel:"修程修次", name:"repairClassRepairTime", width:150, style:"border:none;background:none;", readOnly:true},
                { fieldLabel:"计划完成时间", name:"planTrainTime", width:150, style:"border:none;background:none;", readOnly:true}
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, 
            items: [
                
                { fieldLabel:"检修部门", name:"undertakeDepName", width:150, style:"border:none;background:none;", readOnly:true},    
                {xtype:'label', hideLabel:true, width:150, style:"border:none;", readOnly:true ,
                	html : "<span><a href='#' onclick='RQWorkCard.showAttachment()'>查看附件</a></span>"
                }    
            ]
        }]
    }]
});
RQWorkCard.titleForm_parts =  new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,	labelWidth: 50,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, 
            items: [
        		{ fieldLabel:"配件名称", name:"partsName", width:150, style:"border:none;background:none;", readOnly:true},
                { fieldLabel:"修程修次", name:"repairClassRepairTime", width:150, style:"border:none;background:none;", readOnly:true},
        		{ fieldLabel:"下车车型车号", name:"trainTypeTrainNo" , width:150, style:"border:none;background:none;", readOnly:true}
            ]
        },                {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, 
            items: [
            	{ fieldLabel:"配件编号", name:"nameplateNo", width:150, style:"border:none;background:none;", readOnly:true},
            	{ fieldLabel:"检修开始时间", name:"planBeginTime", width:150, style:"border:none;background:none;", readOnly:true},
                { fieldLabel:"承修部门", name:"undertakeDepName", width:150, style:"border:none;background:none;", readOnly:true}
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, 
            items: [
				{ fieldLabel:"配件型号", name:"specificationModel", width:150, style:"border:none;background:none;", readOnly:true},
				{ fieldLabel:"检修结束时间", name:"planEndTime", width:150, style:"border:none;background:none;", readOnly:true},
                {xtype:'label', hideLabel:true, width:150, style:"border:none;", readOnly:true ,
                	html : "<span><a href='#' onclick='RQWorkCard.showAttachment()'>查看附件</a></span>"
                }    
            ]
        }]
    }]
});
//作业工单基本信息baseForm
RQWorkCard.labelWidth = 80;
RQWorkCard.anchor = '70%';
RQWorkCard.baseForm = new Ext.form.FormPanel({
	layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: RQWorkCard.labelWidth, 
    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "100%" }, buttonAlign: "center",
    buttonAlign: "center",autoScroll:true,
    items: [{
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: RQWorkCard.labelWidth, 	 columnWidth: 0.5, defaults: { anchor: RQWorkCard.anchor },
            items: [
                { name: "workCardName", fieldLabel: "作业工单名称",  maxLength: 50,  width: RQWorkCard.fieldWidth, disabled:true, style:"color:gray" }
            ]
       },{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: RQWorkCard.labelWidth, 	 columnWidth: 0.5,  defaults: { anchor: RQWorkCard.anchor },
            items: [
				{ name: "ratedWorkHours", fieldLabel: "额定工时(分)",  maxLength: 50,  width: RQWorkCard.fieldWidth, disabled:true, style:"color:gray"  }
            ]
       }]
    },{
	    xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	    items: [{
	        baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
	        labelWidth: RQWorkCard.labelWidth, 	 columnWidth: 1, 
	        items: [ 
	        	{ name: "fixPlaceFullName", fieldLabel: "位置",  anchor: "85%", disabled:true, style:"color:gray"  }
	        ]
       }]
    },{
   		xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center",
   		items: [{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: RQWorkCard.labelWidth, 	 columnWidth: 0.5,  defaults: { anchor: RQWorkCard.anchor },
            items: [
            { name: "workScope", fieldLabel: "描述",  maxLength: 50,height:110, xtype:'textarea', width: RQWorkCard.fieldWidth, disabled:true, style:"color:gray" },
                { name: "workSeqType", fieldLabel: "检修类型",    width: RQWorkCard.fieldWidth , disabled:true, style:"color:gray" },
                { name: "specificationModel", fieldLabel: "零部件型号",  maxLength: 50,  width: RQWorkCard.fieldWidth, disabled:true, style:"color:gray"  },
                { name: "realBeginTime", fieldLabel: "开工时间",  maxLength: 50,  width: RQWorkCard.fieldWidth, disabled:true, style:"color:gray"  }
            ]
       },{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: RQWorkCard.labelWidth, 	 columnWidth: 0.5,  defaults: { anchor: RQWorkCard.anchor },
            items: [
            	{ name: "safeAnnouncements", fieldLabel: "安全注意事项",height:110,xtype:'textarea',  maxLength: 50,  width: RQWorkCard.fieldWidth, disabled:true, style:"color:gray"  },
				{ name: "partsName", fieldLabel: "零部件名称",  maxLength: 50,  width: RQWorkCard.fieldWidth, disabled:true, style:"color:gray"  },
				{name: "nameplateNo", fieldLabel: "零部件编号", maxLength: 50,  width: RQWorkCard.fieldWidth, disabled:true, style:"color:gray"  },
				{name: "realEndTime", fieldLabel: "完工时间", maxLength: 50,  width: RQWorkCard.fieldWidth, disabled:true, style:"color:gray"  }
          ]
   	  }]
    }]
});
RQWorkCard.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/qualityControl!pageListForCardQR.action',                 //装载列表数据的请求URL
//    loadURL: ctx + '/workTaskResultView!pageList.action',                 //装载列表数据的请求URL
    singleSelect: true,
    storeAutoLoad:false,
    tbar:null,
	fields: quaFields, //jsp页面上动态组装
	searchFn: function(searchParam){
		RQWorkTask.searchParams = searchParam ;
		RQWorkTask.grid.store.load();
	}
});
//移除侦听器
RQWorkCard.grid.un('rowdblclick', RQWorkCard.grid.toEditFn, RQWorkCard.grid);
//作业工单界面布局信息显示
RQWorkCard.layoutPanel = new Ext.Panel({
	xtype: "panel", layout: "border",
	items:[{
        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
        collapsible:true, height: 110, bodyBorder: false,
        items:[RQWorkCard.titleForm, RQWorkCard.titleForm_parts]
    },{
        region : 'center', layout : 'fit',frame:true, bodyBorder: false, items : [ RQWorkCard.baseForm ]
    },{
        region : 'south', layout : 'fit',height: 150, bodyBorder: false, items : [ RQWorkCard.grid ]
    }]
});

//机车质量记录单结果双击显示界面（接口方法）
//需要对外的接口方法
RQWorkCard.toWorkCardEditFn = function(grid, rowIndex, e){
	if(this.searchWin != null)  this.searchWin.hide();  
	var record = grid.store.getAt(rowIndex);
	RQWorkTask.workTaskIDX = "-kill007" ;
	RQWorkTask.grid.store.load(); //刷新作业任务结果信息
	QRWorker.grid.store.load();   //刷新作业人员信息（作业人员对应着作业工单）
	QRDetectResult.grid.store.load(); //刷新检测结果表格数据。
    RQWorkCard.workCardWin.show();
    RQWorkCard.attachmentKeyIDX = record.get("idx");
    //作业工单基本信息
    RQWorkCard.baseForm.getForm().reset();
    RQWorkCard.baseForm.getForm().loadRecord(record);
    var workSeqType = EosDictEntry.getDictname('JXGC_WORK_STEP_REPAIR_TYPE',record.get("workSeqType"));
    RQWorkCard.baseForm.find("name","realBeginTime")[0].setValue(new Date(record.get("realBeginTime")).format('Y-m-d H:i'));
    RQWorkCard.baseForm.find("name","realEndTime")[0].setValue(new Date(record.get("realEndTime")).format('Y-m-d H:i'));
    RQWorkCard.baseForm.find("name","workSeqType")[0].setValue(workSeqType);
}
//内部方法---查了附件方法
RQWorkCard.showAttachment = function(){
//	alert(RQWorkCard.attachmentKeyIDX); //八门神器
	Attachment.showInWin({ attachmentKeyName:'JXGC_Work_Card',attachmentKeyIDX:RQWorkCard.attachmentKeyIDX,disableButton:['删除',"新增"] });
}

