/**
 * 机车施修任务兑现单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('activityInfo');                       //定义命名空间
	activityInfo.baseForm = new Ext.form.FormPanel({
		align: 'center',
		layout : 'form',
		baseCls: 'x-plain',
		style : 'padding : 5px',
		defaults : { anchor : '98%'},
		labelWidth:100,
		items: [
					{
						align : 'center',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										name : 'trainTypeShortName', 
										fieldLabel : '车型', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							},
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										name : 'trainNo', 
										fieldLabel : '车号', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							}
						]
					},
					{
						align : 'center',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										name : 'repairClassName', 
										fieldLabel : '修程', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							},
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										name : 'repairtimeName', 
										fieldLabel : '修次', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							}
						]
					},
					{
						align : 'center',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										id:"transInTime_v",
										name : 'transInTime', 
										fieldLabel : '计划开始时间', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							},
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										id:"planTrainTime_v",
										name : 'planTrainTime', 
										fieldLabel : '计划完成时间', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							}
						]
					},
					{
						align : 'center',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										id:"starttime",
										name : 'starttime', 
										fieldLabel : '活动开始时间', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							},
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										id:"endtime",
										name : 'endtime', 
										fieldLabel : '活动结束时间', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							}
						]
					},
					{
						align : 'center',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										name : 'activityinstname', 
										fieldLabel : '活动实例名称', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							},
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										name : 'activityinstdesc', 
										fieldLabel : '活动实例描述', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							}
						]
					}
		]
	});
	activityInfo.baseForm_parts = new Ext.form.FormPanel({
		align: 'center',
		layout : 'form',
		baseCls: 'x-plain',
		style : 'padding : 10px',
		defaults : { anchor : '98%'},
		labelWidth:100,
		items: [
					{
						align : 'center',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										name : 'partsName', 
										fieldLabel : '配件名称', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							},
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										name : 'nameplateNo', 
										fieldLabel : '配件编号', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							}
						]
					},
					{
						align : 'center',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										name : 'repairClassName', 
										fieldLabel : '修程', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							},
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										name : 'repairtimeName', 
										fieldLabel : '修次', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							}
						]
					},
					{
						align : 'center',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										id:"starttime_p",
										name : 'starttime', 
										fieldLabel : '活动开始时间', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							},
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										id:"endtime_p",
										name : 'endtime', 
										fieldLabel : '活动结束时间', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							}
						]
					},
					{
						align : 'center',
						layout : 'column',
						baseCls : 'x-plain',
						items:[
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										name : 'activityinstname', 
										fieldLabel : '活动实例名称', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							},
							{
								align : 'center',
								layout : 'form',
								defaultType : 'textfield',
								baseCls : 'x-plain',
								columnWidth : .5,
								items : [
									{
										name : 'activityinstdesc', 
										fieldLabel : '活动实例描述', 
										width : "99%",
										style:"border:none;background:none;",
										readOnly:true
									}
								]
							}
						]
					}
		]
	});
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
	
	activityInfo.createTrainWin = function(){
		
		activityInfo.Win = new Ext.Window({
		    title:"活动信息", maximizable:true, width:550, height:300, layout:"fit", closeAction:"hide",
		    items: activityInfo.baseForm,buttonAlign : 'center', 
		    buttons: [{
		    	text:"关闭", iconCls:"closeIcon",handler:function(){
					activityInfo.Win.hide();
				}
		    }]
		});
	}
	
	activityInfo.createPartsWin = function(){
	
		activityInfo.PartsWin = new Ext.Window({
		    title:"活动信息", maximizable:true, width:550, height:300, layout:"fit", closeAction:"hide",
		    items: activityInfo.baseForm_parts,buttonAlign : 'center', 
		    buttons: [{
		    	text:"关闭", iconCls:"closeIcon",handler:function(){
					activityInfo.PartsWin.hide();
				}
		    }]
		});
	}
	
});	
