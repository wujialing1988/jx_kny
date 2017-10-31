//TODO 待删除

/**
 * 机车施修任务兑现单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
function appendx(str1, str2){
	if(str1 && str2)
		return str1 + "|" + str2;
	else if(str1)
		return str1;
	else if(str2)
		return str2;
	return "";
}
var action = '';
var trainX = '/trainEnforcePlanRdp';
var partsX = '/partsEnforcePlanRdp';
 function operate(idx){
 	var record = activityInfo.workCardGrid.store.getAt(idx);
 	activityInfo.workCardWin.show();
 	RQWorkTask.workTaskIDX = "-kill007" ;
 	RQWorkCard.workCardIDX = record.get("idx");
	RQWorkTask.grid.store.load(); //刷新作业任务结果信息
	QRWorker.grid.store.load();   //刷新作业人员信息（作业人员对应着作业工单）
	QRDetectResult.grid.store.load(); //刷新检测结果表格数据。
	if(flag != "ONTASK"){
		//质量控制信息
		RQWorkCard.grid.store.on("beforeload",function(){
			this.baseParams.workCardId = RQWorkCard.workCardIDX;
		});
		RQWorkCard.grid.store.load();
	}
    RQWorkCard.attachmentKeyIDX = record.get("idx");
   
    if(record.get("rdpType") == trainRdp){
    	action = trainX;
    }else if(record.get("rdpType") == partsRdp){
    	action = partsX;
    }else {
    	action = "/zbRdp";
    }
    if(action){
	    jQuery.ajax({
			url: ctx + action +"!getBeseInfo.action",
			data:{rdpId: rdpId},
			dataType:"json",
			type:"post",
			success:function(data){
				
				var recordx = new Ext.data.Record();
				for(var i in data){
					if(i == 'transInTime' || i == 'planTrainTime' || i == 'planBeginTime' || i == 'planEndTime'){	
						if(data[i]){
							recordx.set(i, new Date(data[i]).format('Y-m-d H:i'));
						}					
						continue;
					}
					recordx.set(i, data[i]);
				}
				if(record.get("rdpType") == trainRdp){
					recordx.set("trainTypeTrainNo", appendx(recordx.get("trainTypeShortName"), recordx.get("trainNo"))) ; //车型|车号
				    recordx.set("repairClassRepairTime", appendx(recordx.get("repairClassName"), recordx.get("repairtimeName"))) ; //车型|车号
				    //兑现单信息    
				    RQWorkCard.titleForm.getForm().loadRecord(recordx);
				    RQWorkCard.titleForm_parts.hide();
					RQWorkCard.titleForm.show();
			    }else if(record.get("rdpType") == partsRdp){
			    	
			    	recordx.set('repairClassRepairTime', appendx(recordx.get("repairClassName"), recordx.get("repairtimeName")));
					recordx.set('trainTypeTrainNo', appendx(recordx.get("unloadTrainTypeShortName"), recordx.get("unloadTrainNo")));
					RQWorkCard.titleForm_parts.getForm().loadRecord(recordx);
					RQWorkCard.titleForm.hide();
					RQWorkCard.titleForm_parts.show();
			    }
			},
			error: function(){
				alert(1);
			}
		});
    }
    if(record.get("realBeginTime")){
    	record.set("realBeginTime", record.get("realBeginTime").format('Y-m-d H:i'));
    }
    if(record.get("realEndTime")){
    	record.set("realEndTime", record.get("realEndTime").format('Y-m-d H:i'));
    }
    if(record.get("workSeqType"))
    	record.set("workSeqType", dict[record.get("workSeqType")]);
    
    //作业工单基本信息
    RQWorkCard.baseForm.getForm().reset();
    RQWorkCard.baseForm.getForm().loadRecord(record);
}
Ext.onReady(function(){
Ext.namespace('activityInfo');                       //定义命名空间
//机车生产信息
activityInfo.titleForm =  new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "99%" },
    layout: "form",		border: false,	labelWidth: 50, style: "padding-left:20px;",
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, 
            items: [
        		{ fieldLabel:"车型车号", name:"trainTypeTrainNo", width:150, style:"border:none;background:none;", readOnly:true},
                { fieldLabel:"修程修次", name:"repairClassRepairTime", id:"xcxc", width:150, style:"border:none;background:none;", readOnly:true}
            ]
        },                {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, 
            items: [
            	{ fieldLabel:"检修开始时间", name:"transInTime", width:150, style:"border:none;background:none;", readOnly:true},
                { fieldLabel:"承修部门", name:"undertakeDepName", width:150, style:"border:none;background:none;", readOnly:true}
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, 
            items: [
                { fieldLabel:"检修结束时间", name:"planTrainTime", width:150, style:"border:none;background:none;", readOnly:true}  
            ]
        }]
    }]
});

/**
 * 显示配件基本信息
 */
activityInfo.titleForm_parts = new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "99%" },
    layout: "form",		border: false,	labelWidth: 50, style: "padding-left:20px;",
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.33, 
            items: [
                 //用lastTimeWorker存储的配件名称
        		{ fieldLabel:"配件名称", name:"partsName", width:150, style:"border:none;background:none;", readOnly:true, id:"parts_name_id"},
                { fieldLabel:"修程修次", name:"repairClassRepairTime", id:"pxcxc", width:150, style:"border:none;background:none;", readOnly:true},
        		{ fieldLabel:"下车车型车号", name:"trainTypeTrainNo", id:"ptrainTypeTrainNo", width:150, style:"border:none;background:none;", readOnly:true}
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
                { fieldLabel:"检修结束时间", name:"planEndTime", width:150, style:"border:none;background:none;", readOnly:true}
            ]
        }]
    }]
});

//作业卡grid
activityInfo.workCardGrid = new Ext.yunda.Grid({
		loadURL: ctx + '/workCard!findListForJK.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/workCard!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/workCard!logicDelete.action',            //删除数据的请求URL
	    tbar:[],
	    fields: [{
			header:'IDX', dataIndex:'idx', editor: { xtype:"hidden"}, hidden:true
		},{
			header:'操作', dataIndex:'idx', editor: { xtype:'hidden' }, width:30, sortable:false,
			renderer:function(value, metaData, record, rowIndex, colIndex, store){			
				return "<a href='#' onclick='operate(\"" + rowIndex + "\")'>查看</a>";
			}
		},{
			header:'作业名称', dataIndex:'workCardName', width:250, editor: { }
		},{
			header:'施修工位', dataIndex:'workStationName', hidden:true, editor: { }
		},{
			header:'计划开工时间', dataIndex:'planBeginTime',xtype:'datecolumn', format: 'Y-m-d H:i', editor: { }
		},{
			header:'计划完工时间', dataIndex:'planEndTime', xtype:'datecolumn', format: 'Y-m-d H:i',editor: { }
		},{
			header:'实际开工时间', dataIndex:'realBeginTime',xtype:'datecolumn', format: 'Y-m-d H:i', editor: { },hidden:true
		},{
			header:'实际完工时间', dataIndex:'realEndTime',xtype:'datecolumn', format: 'Y-m-d H:i', editor: { },hidden:true
		},{
			header:'作业卡编码', dataIndex:'workCardCode', editor: { },hidden:true
		},{
			header:'车型', dataIndex:'trainSortName', width:50, editor: { },hidden:true
		},{
			header:'车型', dataIndex:'trainTypeIdx', width:50, editor: { },hidden:true
		},{
			header:'车号', dataIndex:'trainNo', width:50, editor: { },hidden:true
		},{
			header:'修程', dataIndex:'repairClassName', editor: { },hidden:true
		},{
			header:'修次', dataIndex:'repairTimeIdx', editor: { }, hidden:true
		},{
			header:'修程', dataIndex:'repairClassIdx', editor: { }, hidden:true
		},{
			header:'工序卡', dataIndex:'workSeqCardIDX', width:200, hidden:true, editor: { }
		},{
			header:'作业卡名称', dataIndex:'workCardName', width:200, editor: { },hidden:true
		},{
			header:'配件名称', dataIndex:'partsName', editor: { },hidden:true
		},{
			header:'规格型号', dataIndex:'specificationModel', editor: { },hidden:true
		},{
			header:'铭牌号', dataIndex:'nameplateNo', editor: { },hidden:true
		},{
			header:'配件编号', dataIndex:'partsNo', editor: { },hidden:true
		},{
			header:'安装位置全名', dataIndex:'fixPlaceFullName', width:250, editor: { },hidden:true
		},{
			header:'rdpType', dataIndex:'rdpType', width:250, editor: { },hidden:true
		},{
			header:'状态', dataIndex:'status', editor: { },searcher:{disabled:true},
			renderer: function(v){ 
				if(v == STATUS_NEW) return "待开放";
				if(v == STATUS_OPEN) return "已开放";
				if(v == STATUS_HANDLING) return "处理中";
				if(v == STATUS_HANDLED) return "已处理";
				if(v == STATUS_FINISHED) return "质量检查中";
				if(v == STATUS_TERMINATED) return "终止";
		}
		},{
			header:'备注', dataIndex:'remarks', hidden:true, editor: { }
		},{
			header:'修程修次', dataIndex:'repairClassRepairTime', hidden:true, editor: { }
		},{
			header:'车型车号', dataIndex:'trainTypeTrainNo', hidden:true, editor: { }
		},{
			header:'转入时间', dataIndex:'transInTimeStr', hidden:true, editor: { }
		},{
			header:'计划交车时间', dataIndex:'planTrainTimeStr', hidden:true, editor: { }
		},{
			header:'工位名称', dataIndex:'workStationName', hidden:true, editor: { }
		},{
			header:'工位负责人名称', dataIndex:'dutyPersonName', hidden:true, editor: { }
		},{
			header:'额定工时', dataIndex:'ratedWorkHours', hidden:true, editor: { }
		},{
			header:'注意事项', dataIndex:'safeAnnouncements', hidden:true, editor: { }
		},{
			header:'互换配件信息主键', dataIndex:'partsAccountIDX', hidden:true, editor: { }
		},{
			header:'互换配件型号主键', dataIndex:'partsTypeIDX', hidden:true, editor: { }
		},{
			header:'配件名称', dataIndex:'partsName', hidden:true, editor: { }
		},{
			header:'规格型号', dataIndex:'specificationModel', hidden:true, editor: { }
		},{
			header:'铭牌号', dataIndex:'nameplateNo', hidden:true, editor: { }
		},{
			header:'fixPlaceIDX', dataIndex:'fixPlaceIDX', hidden:true, editor: { }
		},{
			header:'fixPlaceFullCode', dataIndex:'fixPlaceFullCode', hidden:true, editor: { }
		},{
			header:'检修类型', dataIndex:'workSeqType',  hidden:true, editor: { }
		},{
			header:'零部件名称', dataIndex:'partsName', hidden:true, editor: { }
		},{
			header:'零部件型号', dataIndex:'specificationModel', hidden:true, editor: { }
		},{
			header:'零部件图号', dataIndex:'chartNo', hidden:true, editor: { }
		},{
			header:'零部件编号', dataIndex:'nameplateNo', hidden:true, editor: { }
		}]
	});
//移除侦听器
activityInfo.workCardGrid.un('rowdblclick', activityInfo.workCardGrid.toEditFn, activityInfo.workCardGrid);	
activityInfo.workCardGrid.store.on('beforeload',function(){
	this.baseParams.processDefID = processDefID;
	this.baseParams.activityID = activitydefId;
	this.baseParams.rdpIDX = rdpId;
})
//作业工单编辑选项卡列表
activityInfo.editTabs = new Ext.TabPanel({
    activeTab: 0, frame:true,
    items:[{
            title: "基本信息", layout: "fit", border: false, items: [ RQWorkCard.layoutPanel ]
        },{
            title: "检测/修项目", layout: "fit", layout: "border",border: false, 
            items: [{
        		 region: 'west', layout: "fit",title:'检测/修项目',
        		 width: 700,minSize: 500, maxSize: 800, split: true, bodyBorder: false, items:[ RQWorkTask.grid ]
            },{
        		 region: 'center', layout: "fit",title:'检测结果',  bodyBorder: false, items : [ QRDetectResult.grid ]
            }]
        },{
            title: "作业人员", layout: "fit", border: false, items: [ QRWorker.grid ]            
        }
//        ,{
//            title: "质量检查员", layout: "fit", border: false, items: [  ]            
//        }
        ]
});

//作业工单信息查询结果窗口
activityInfo.workCardWin = new Ext.Window({
	title: "作业工单查看", maximizable:false, layout: "fit", 
	closeAction: "hide", modal: true, maximized: true , buttonAlign:"center",
	items: [activityInfo.editTabs],
	buttons:[{
		text: "关闭", iconCls: "closeIcon", handler: function(){activityInfo.workCardWin.hide();}
	}]
});

if(typeof(isparts) == "undefined" || !isparts){
	action = trainX;
}else{	
	action = partsX;
}

jQuery.ajax({
	url: ctx + action + "!getBeseInfo.action",
	data: {rdpId: rdpId},
	type: "post",
	dataType:"json",
	success: function(data){		
		var record = new Ext.data.Record();
		for(var i in data){
			if(i == 'transInTime' || i == 'planTrainTime' || i == 'planBeginTime' || i == 'planEndTime'){					
				record.set(i, new Date(data[i]).format("Y-m-d H:i"));					
				continue;
			}
			record.set(i,data[i]);
		}
		if(action == partsX){
			record.set('repairClassRepairTime', appendx(record.get("repairClassName"), record.get("repairtimeName")));
			record.set('trainTypeTrainNo', appendx(record.get("unloadTrainTypeShortName"), record.get("unloadTrainNo")));
			activityInfo.titleForm_parts.getForm().loadRecord(record);
			activityInfo.titleForm.hide();
			activityInfo.titleForm_parts.show();
		}else{
			record.set('trainTypeTrainNo', appendx(record.get("trainTypeShortName"), record.get("trainNo")));
			record.set('repairClassRepairTime', appendx(record.get("repairClassName"), record.get("repairtimeName")));
			activityInfo.titleForm.getForm().loadRecord(record);
			activityInfo.titleForm_parts.hide();
			activityInfo.titleForm.show();
		}
	},
	error: function(a,b ,c){
		alert(a);
		alert(b);
		alert(c);
	}
});

//页面自适应布局
var viewport = new Ext.Viewport({ 
	layout:'border', 
	items:[{
			region:"north",
			layout:"fit",
			height:100,
			split:true,
			maxSize:300,
			minSize:100,			
			items:[activityInfo.titleForm, activityInfo.titleForm_parts]}
				,{
					title:"作业工单信息",
					region:"center",
					layout:"fit",
					items:[activityInfo.workCardGrid]
				}
	]
});
});	

