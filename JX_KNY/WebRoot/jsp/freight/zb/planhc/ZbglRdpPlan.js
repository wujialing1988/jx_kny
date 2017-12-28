/**
 * 货车列检
 */
Ext.onReady(function(){
	
	Ext.ns('ZbglRdpPlan');
	
	/** **************** 定义全局变量开始 **************** */
	ZbglRdpPlan.labelWidth = 80;
	
	ZbglRdpPlan.statusArrays = {"UNRELEASED":"未启动","ONGOING":"已启动","INTERRUPT":"中断","DELAY":"延期","COMPLETE":"已完成"} ;
	ZbglRdpPlan.statusColorArrays = {"UNRELEASED":"#999999","ONGOING":"#00BFFF","INTERRUPT":"red","DELAY":"yellow","COMPLETE":"#008000"} ;
	
	/**
	 * 启动计划
	 */
	ZbglRdpPlan.startPlan = function() {
		
		//判断是否选择了数据
		var grid = ZbglRdpPlan.ZbglRdpPlanGrid;
		if(!$yd.isSelectedRecord(grid)) {
			MyExt.Msg.alert(i18n.TrainInspectionPlan.alertMes1);
			return;
		}
		var ids = $yd.getSelectedIdx(grid);	
		
		var record = ZbglRdpPlan.ZbglRdpPlanGrid.store.getById(ids[0]);
		var idx = record.get("idx");
		
		var rdpPlanStatus = record.get("rdpPlanStatus");
		if(rdpPlanStatus != STATUS_UNRELEASED){
			MyExt.Msg.alert(i18n.TrainInspectionPlan.alertMes4);
			return;			
		}
		
		var cfg = {
	        url: ctx + "/zbglRdpPlan!startPlan.action", 
			params: {id: idx},
	        timeout: 600000,
	        success: function(response, options){
	        	if(processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                ZbglRdpPlan.ZbglRdpPlanGrid.store.load();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){
	        	if(processTips) hidetip();
		        Ext.Msg.alert(i18n.TrainInspectionPlan.alertRemaind, i18n.TrainInspectionPlan.alertRemaindMes + "\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Msg.confirm(i18n.TrainInspectionPlan.alertRemaind, i18n.TrainInspectionPlan.confirmMes2, function(btn){
	        if(btn != 'yes')    return;
	        showtip();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });
	}
	
	
	/**
	 * 中断计划
	 */
	ZbglRdpPlan.interruptPlan = function() {
		
		//判断是否选择了数据
		var grid = ZbglRdpPlan.ZbglRdpPlanGrid;
		if(!$yd.isSelectedRecord(grid)) {
			MyExt.Msg.alert(i18n.TrainInspectionPlan.alertMes1);
			return;
		}
		var ids = $yd.getSelectedIdx(grid);	
		
		var record = ZbglRdpPlan.ZbglRdpPlanGrid.store.getById(ids[0]);
		var idx = record.get("idx");
		
		var rdpPlanStatus = record.get("rdpPlanStatus");
		if(rdpPlanStatus != STATUS_HANDLING){
			MyExt.Msg.alert(i18n.TrainInspectionPlan.alertMes2);
			return;			
		}
		
		var cfg = {
	        url: ctx + "/zbglRdpPlan!interruptPlan.action", 
			params: {id: idx},
	        timeout: 600000,
	        success: function(response, options){
	        	if(processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                ZbglRdpPlan.ZbglRdpPlanGrid.store.load();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){
	        	if(processTips) hidetip();
		        Ext.Msg.alert(i18n.TrainInspectionPlan.alertRemaind, i18n.TrainInspectionPlan.alertRemaindMes + "\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Msg.confirm(i18n.TrainInspectionPlan.alertRemaind, i18n.TrainInspectionPlan.confirmMes3, function(btn){
	        if(btn != 'yes')    return;
	        showtip();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });
	}
	
	/**
	 * 恢复计划
	 */
	ZbglRdpPlan.regainPlan = function() {
		
		//判断是否选择了数据
		var grid = ZbglRdpPlan.ZbglRdpPlanGrid;
		if(!$yd.isSelectedRecord(grid)) {
			MyExt.Msg.alert(i18n.TrainInspectionPlan.alertMes1);
			return;
		}
		var ids = $yd.getSelectedIdx(grid);	
		
		var record = ZbglRdpPlan.ZbglRdpPlanGrid.store.getById(ids[0]);
		var idx = record.get("idx");
		
		var rdpPlanStatus = record.get("rdpPlanStatus");
		if(rdpPlanStatus != STATUS_INTERRUPT){
			MyExt.Msg.alert(i18n.TrainInspectionPlan.alertMes5);
			return;			
		}
		
		var cfg = {
	        url: ctx + "/zbglRdpPlan!regainPlan.action", 
			params: {id: idx},
	        timeout: 600000,
	        success: function(response, options){
	        	if(processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                ZbglRdpPlan.ZbglRdpPlanGrid.store.load();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){
	        	if(processTips) hidetip();
		        Ext.Msg.alert(i18n.TrainInspectionPlan.alertRemaind, i18n.TrainInspectionPlan.alertRemaindMes + "\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Msg.confirm(i18n.TrainInspectionPlan.alertRemaind, i18n.TrainInspectionPlan.confirmMes4, function(btn){
	        if(btn != 'yes')    return;
	        showtip();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });
	}
	
	
	/**
	 * 终止计划
	 */
	ZbglRdpPlan.stopPlan = function() {
		
		//判断是否选择了数据
		var grid = ZbglRdpPlan.ZbglRdpPlanGrid;
		if(!$yd.isSelectedRecord(grid)) {
			MyExt.Msg.alert(i18n.TrainInspectionPlan.alertMes1);
			return;
		}
		var ids = $yd.getSelectedIdx(grid);	
		
		var record = ZbglRdpPlan.ZbglRdpPlanGrid.store.getById(ids[0]);
		
		var rdpPlanStatus = record.get("rdpPlanStatus");
		if(rdpPlanStatus == STATUS_HANDLED){
			MyExt.Msg.alert(i18n.TrainInspectionPlan.alertMes3);
			return;			
		}
		
		var cfg = {
	        url: ctx + "/zbglRdpPlan!stopPlan.action", 
			params: {ids: ids},
	        timeout: 600000,
	        success: function(response, options){
	        	if(processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                ZbglRdpPlan.ZbglRdpPlanGrid.store.load();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){
	        	if(processTips) hidetip();
		        Ext.Msg.alert(i18n.TrainInspectionPlan.alertRemaind, i18n.TrainInspectionPlan.alertRemaindMes + "\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Msg.confirm(i18n.TrainInspectionPlan.alertRemaind, i18n.TrainInspectionPlan.confirmMes1, function(btn){
	        if(btn != 'yes')    return;
	        showtip();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });
	}
	
	/************** 完成填写实际时间 ****************/
	ZbglRdpPlan.CompletedForm = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",
		layout: "form",		border: false,	labelWidth: 120,
		defaults:{
			anchor: "99%" ,
			defaults: {
				defaults: {				
					anchor: "95%"
				}
			}
		},
		items:[{
			xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	    	items: [
		        {
		            baseCls:"x-plain", align:"center", layout:"form", columnWidth: 1,
			        items: [{				
						name: "realBeginTime", fieldLabel: i18n.TrainInspectionPlan.actualStartTime, xtype:"my97date",format: "Y-m-d H:i",   //实际开始时间
			        	my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
					},{				
						name: "realEndTime", fieldLabel: i18n.TrainInspectionPlan.actualEndTime, xtype:"my97date",format: "Y-m-d H:i",   //实际完成时间
			        	my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
					}]
				}]
		}]
	});
	
	
	ZbglRdpPlan.CompletedWin = new Ext.Window({
		title:i18n.TrainInspectionPlan.titleFinPlan, width:400, height:150, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
		labelWidth: 120,
		maximizable:false, 
		items:ZbglRdpPlan.CompletedForm,
		modal:true,
		buttons: [{
			text : i18n.TrainInspectionPlan.buttSave,iconCls : "saveIcon", handler: function(){
				var form = ZbglRdpPlan.CompletedForm.getForm(); 
		        if (!form.isValid()) return;
		        var data = form.getValues();
		        var ids = $yd.getSelectedIdx(ZbglRdpPlan.ZbglRdpPlanGrid);	
				var cfg = {
				        url: ctx + "/zbglRdpPlan!cmpPlan.action", 
						params: {id: ids[0],"realBeginTime":data.realBeginTime,"realEndTime":data.realEndTime},
				        timeout: 600000,
				        success: function(response, options){
				        	if(processTips) hidetip();
				            var result = Ext.util.JSON.decode(response.responseText);
				            if (result.errMsg == null && result.success == true) {
				                alertSuccess();
				                ZbglRdpPlan.CompletedWin.hide(); 
				                ZbglRdpPlan.ZbglRdpPlanGrid.store.load();
				            } else {
				                alertFail(result.errMsg);
				            }
				        },
			        failure: function(response, options){
			        	if(processTips) hidetip();
				        Ext.Msg.alert(i18n.TrainInspectionPlan.alertRemaind, i18n.TrainInspectionPlan.alertRemaindMes + "\n" + response.status + "\n" + response.responseText);
				    }
			    };
			    Ext.Msg.confirm(i18n.TrainInspectionPlan.alertRemaind, i18n.TrainInspectionPlan.confirmMes, function(btn){
			        if(btn != 'yes')    return;
			        showtip();
			        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
			    });
				
			}
		},{
	        text: i18n.TrainInspectionPlan.textClose, iconCls: "closeIcon", scope: this, handler: function(){ ZbglRdpPlan.CompletedWin.hide(); }
		}],
		listeners:{
			"show":function(){
				
			}
		}
	});
	
	/************** 完成填写实际时间 ****************/
	
	/**
	 * 完成计划
	 */
	ZbglRdpPlan.cmpPlan = function() {
		
		//判断是否选择了数据
		var grid = ZbglRdpPlan.ZbglRdpPlanGrid;
		if(!$yd.isSelectedRecord(grid)) {
			MyExt.Msg.alert(i18n.TrainInspectionPlan.alertMes1);
			return;
		}
		var ids = $yd.getSelectedIdx(grid);	
		
		var record = ZbglRdpPlan.ZbglRdpPlanGrid.store.getById(ids[0]);
		var idx = record.get("idx");
		
		var rdpPlanStatus = record.get("rdpPlanStatus");
		if(rdpPlanStatus != STATUS_HANDLING){
			MyExt.Msg.alert(i18n.TrainInspectionPlan.alertMes2);
			return;			
		}
		
		ZbglRdpPlan.CompletedWin.show();
	}
	
	/**
	 * 第一行的tbar
	 */
	ZbglRdpPlan.oneBar = new Ext.Toolbar({
		items: [
		{
    	text: i18n.TrainInspectionPlan.add ,iconCls:"addIcon", handler: function(){   //新增
    		ZbglRdpPlan.ZbglRdpPlanGrid.addButtonFn();
    		}
    	},
		{
    	text: i18n.TrainInspectionPlan.start ,iconCls:"beginIcon", handler: function(){   //启动
    		ZbglRdpPlan.startPlan();
    		}
    	},
    	{
    	text: i18n.TrainInspectionPlan.stop ,iconCls:"deleteIcon", handler: function(){   //终止
    		ZbglRdpPlan.stopPlan();
    		}
    	},
    	{
    	text: i18n.TrainInspectionPlan.finish ,iconCls:"cmpIcon", handler: function(){   //完成
    		ZbglRdpPlan.cmpPlan();
    		}
    	},
		{
    	text: i18n.TrainInspectionPlan.interrupt ,iconCls:"cancelIcon", handler: function(){   //中断
    		ZbglRdpPlan.interruptPlan();
    		}
	    },
		{
    	text: i18n.TrainInspectionPlan.recover ,iconCls:"yesIcon", handler: function(){   //恢复
    		ZbglRdpPlan.regainPlan();
    		}
	    }	    
		]
	});
	
	/**
	 * 列检计划列表 未完成
	 */
	ZbglRdpPlan.ZbglRdpPlanGrid = new Ext.yunda.Grid({
		loadURL: ctx + "/zbglRdpPlan!pageQuery.action",
		saveURL: ctx + '/zbglRdpPlan!saveZbglRdpPlan.action',             //保存数据的请求URL
    	deleteURL: ctx + '/zbglRdpPlan!logicDelete.action',            //删除数据的请求URL
		singleSelect: true, 
		saveFormColNum:2,
		labelWidth:120,
	    tbar:[
	    	{	
				xtype:'textfield', id:'rail_way_Time', enableKeyEvents:true, emptyText:i18n.TrainInspectionPlan.quickSearch, listeners: {   //输入车次快速检索
		    		keyup: function(filed, e) {
							ZbglRdpPlan.ZbglRdpPlanGrid.store.load();
		    		},
		    		render:function(){
						ZbglRdpPlan.oneBar.render(ZbglRdpPlan.ZbglRdpPlanGrid.tbar);
					}
				}	
			},'-','<div><span style="color:green;">'+ i18n.TrainInspectionPlan.remaind +'</span></div>'   //双击编辑【未启动】的计划
	    ],
		fields: [{
	   			header:i18n.TrainInspectionPlan.idx, dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.TrainInspectionPlan.train, dataIndex:'railwayTime', editor:{  
					maxLength:50,
					allowBlank:false,
					listeners : { 
						"change":function(me,newValue, oldValue){

						 }
					}
				}, searcher: {anchor:'98%'}
			},{
				header:'<div>' + i18n.TrainInspectionPlan.track + '<span style="color:green;">' + i18n.TrainInspectionPlan.VehicleNumber + '</span></div>', dataIndex:'rdpNum', editor:{    
					fieldLabel:i18n.TrainInspectionPlan.VehicleNumber2,
			        xtype:'numberfield',
			        allowDecimals:false,
			        allowNegative:false,
			        minValue:1,
			        maxValue:99,
			        allowBlank:false
			        }, 
			        renderer: function(value, metaData, record, rowIndex, colIndex, store) {
						var trackName = Ext.isEmpty(record.get('trackName'))?'':record.get('trackName');
						return trackName+":【"+value+"】";
					},
			        searcher: {anchor:'98%'}
			},{
	   			header:i18n.TrainInspectionPlan.classCode, dataIndex:'classNo', hidden:true, editor: { xtype:'hidden',id:'classNo' }   //班次编码
			},{
				header:i18n.TrainInspectionPlan.workClass, dataIndex:'className',hidden:true, editor:{   //作业班次
	    		        id:"class_combo",	
	    				hiddenName: "className",
	    				xtype: "Base_combo",
	    				queryParams: {'vehicleType':vehicleType}, 
	    			    entity:'com.yunda.freight.base.classMaintain.entity.ClassMaintain',
						business: 'classMaintain',	
	                    returnField: [{widgetId:"classNo",propertyName:"classNo"}],
	                    fields:["classNo","className","idx"],
	        		    displayField: "className", valueField: "className",
	                    pageSize: 0, minListWidth: 200,
	                    allowBlank:false,editable:false,
						listeners : {   
			                "select" : function(me, record, index) {   
			                    // 重新加载作业班组数据
			                    var workTeam_combo = Ext.getCmp("workTeam_comb");
			                    workTeam_combo.reset();
			                    workTeam_combo.clearValue();
			                    Ext.getCmp("workTeamName").setValue("");
			                    Ext.getCmp("workTeamSeq").setValue("");
			                    workTeam_combo.queryParams = {"classIdx":record.data.idx};
			                    workTeam_combo.cascadeStore();
			                }   
			            }
			        }, searcher: {anchor:'98%'}
			},{
				header:i18n.TrainInspectionPlan.workGroup, dataIndex:'workTeamID',hidden:true, editor:{  //作业班组
					id:"workTeam_comb",	
    				hiddenName: "workTeamID",
    				xtype: "Base_combo",
    			    entity:'com.yunda.freight.base.classOrganization.entity.ClassOrganization',
					business: 'classOrganization',	
                    returnField: [						
                    		{widgetId:"workTeamName",propertyName:"orgname"},
				  		 {widgetId:"workTeamSeq",propertyName:"orgseq"}
				  	],
                    fields:['orgid','orgseq','orgname'],
        		    displayField: "orgname", valueField: "orgid",
                    pageSize: 0, minListWidth: 200,
                    allowBlank:false,editable:false
				}, searcher: {anchor:'98%'}
			},{
	   			header:i18n.TrainInspectionPlan.workGroupName, dataIndex:'workTeamName', hidden:true, editor: {id:'workTeamName', xtype:'hidden' }  //作业班组名称
			},{
	   			header:i18n.TrainInspectionPlan.workGroupSequence, dataIndex:'workTeamSeq', hidden:true, editor: {id:'workTeamSeq', xtype:'hidden' }   //作业班组序列
			},{
				header:i18n.TrainInspectionPlan.checkTimeMin, dataIndex:'checkTime',hidden:true, editor:{    //技检时间
			        xtype:'numberfield',
			        allowDecimals:false,
			        allowNegative:false,
			        minValue:1,
			        maxValue:999,
			        allowBlank:false,
					listeners : {   
				        	"change" : function(me, newValue, oldValue ) {   
				        		
				        	}
					}
			        }, searcher: {anchor:'98%'}
			},{
				header:i18n.TrainInspectionPlan.plannedStartTime, dataIndex:'planStartTime', xtype:'datecolumn', hidden:true, editor:{allowBlank:false, xtype:'my97date',format: 'Y-m-d H:i' }
			},{
				header:i18n.TrainInspectionPlan.plannedEndTime, dataIndex:'planEndTime', xtype:'datecolumn',  hidden:true,editor:{hidden:true,xtype:'my97date',format: 'Y-m-d H:i'}
			},{
	   			header:i18n.TrainInspectionPlan.upDownStreamCode, dataIndex:'toDirectionNo', hidden:true, editor: { xtype:'hidden',id:'toDirectionNo' }
			},{
				header:i18n.TrainInspectionPlan.upstream_downstream, dataIndex:'toDirectionName',hidden:true, editor:{
						id:'toDirection_combo',
						xtype: 'EosDictEntry_combo',
						hiddenName: 'toDirectionName',
						dicttypeid:'FREIGHT_DIRECT',
						displayField:'dictname',valueField:'dictname',
						hasEmpty:"false",
						hidden:true,
						returnField: [{widgetId:"toDirectionNo",propertyName:"dictid"}]
			        }, searcher: {anchor:'98%'}
			},{
	   			header:i18n.TrainInspectionPlan.trackName, dataIndex:'trackName', hidden:true, editor: { xtype:'hidden',id:'trackName' }
			},{
				header:i18n.TrainInspectionPlan.track2, dataIndex:'trackNo',hidden:true, editor:{
					id:"trackNo_comb",
		        	xtype: "Base_combo",
		        	fieldLabel: i18n.TrainInspectionPlan.track2,
					fields:['idx','trackCode','trackName'],
					hiddenName: "trackNo",
					business: 'stationTrack',
					returnField:[
						 {widgetId:"trackName",propertyName:"trackName"}
				  	], 
				  	idProperty: 'idx',
					displayField: "trackName", 
					valueField: "trackCode",
					pageSize: 20, 
					minListWidth: 200, 
					editable:false,
					allowBlank: false,
					isAll:true
				}, searcher: {anchor:'98%'}
			},/*{
	   			header:'白夜班编码', dataIndex:'dayNightTypeNo', hidden:true, editor: { xtype:'hidden',id:'dayNightTypeNo' }
			},{
				header:'白夜班', dataIndex:'dayNightTypeName',hidden:true, editor:{
						id:'dayNightType_combo',
						xtype: 'EosDictEntry_combo',
						hiddenName: 'dayNightTypeName',
						dicttypeid:'DAYNIGHT_TYPE',
						displayField:'dictname',valueField:'dictname',
						hasEmpty:"false",
						returnField: [{widgetId:"dayNightTypeNo",propertyName:"dictid"}]
			        }, searcher: {anchor:'98%'}
			},*/
				{
				header:i18n.TrainInspectionPlan.status, dataIndex:'rdpPlanStatus',editor:{ xtype:'hidden'},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
					return '<div style="background:'+ ZbglRdpPlan.statusColorArrays[value] +';color:white;width:48px;height:18px;line-height:18px;text-align:center;border-radius:8px;margin-left:10px;">' + ZbglRdpPlan.statusArrays[value] + '</div>';
				}
			},{
				header:i18n.TrainInspectionPlan.trainType, dataIndex:'vehicleType',hidden:true, editor: { xtype:"hidden",value:vehicleType }
			},{
				header:i18n.TrainInspectionPlan.stationID, dataIndex:'siteID',hidden:true, editor: { xtype:"hidden"}
			},{
				header:i18n.TrainInspectionPlan.stationName, dataIndex:'siteName',hidden:true, editor: { xtype:"hidden" }
			},{
				header:i18n.TrainInspectionPlan.actualStartTime, dataIndex:'realStartTime',hidden:true, editor: { xtype:"hidden" }
			},{
				header:i18n.TrainInspectionPlan.actualEndTime, dataIndex:'realEndTime',hidden:true, editor: { xtype:"hidden" }
			}],
			afterShowSaveWin: function(){
				 // 设置字段只读
				 // this.setReadOnlyColumns(['railwayTime','rdpNum','trackNo'],false);
			},  
			afterShowEditWin: function(record, rowIndex){
				 // 解除字段只读
				 // this.setReadOnlyColumns(['railwayTime','rdpNum','trackNo'],true);
				Ext.getCmp("workTeam_comb").setDisplayValue(record.get("workTeamID"),record.get("workTeamName"));
				Ext.getCmp("trackNo_comb").setDisplayValue(record.get("trackNo"),record.get("trackName"));
			},
			// 保存之前存状态
			beforeSaveFn: function(data){ 
				if(Ext.isEmpty(data.idx)){
					data.rdpPlanStatus = STATUS_UNRELEASED ;
				}
				if(Ext.isEmpty(data.vehicleType)){
					data.vehicleType = vehicleType ;
				}
				var newValue = data.railwayTime ;
				if(newValue.length != 0){
					var lastChar = '' ;
					for(i = newValue.length - 1 ;  i >= 0 ;i--){
						lastChar = newValue.charAt(i)
						if(/^\d+$/.test(lastChar))  
						{  
							if(lastChar % 2 == 0){
								// 偶数
								data.toDirectionNo = 20  ;
								data.toDirectionName = '下行'  ;
							}else{
								// 奇数
								data.toDirectionNo = 10  ;
								data.toDirectionName = '上行'  ;
							}
							break ;
						} 
					}
				}
				
				return true; 
			},
			beforeShowEditWin: function(record, rowIndex){  
				var rdpPlanStatus = record.get('rdpPlanStatus');
				if(rdpPlanStatus != STATUS_UNRELEASED){
					MyExt.Msg.alert(i18n.TrainInspectionPlan.alertMes6);
					return false;
				}else{
					return true;
				}
					
			}
	});
	
	
	/**
	 * 列检计划列表 未完成
	 */
	ZbglRdpPlan.ZbglRdpPlanCompletedGrid = new Ext.yunda.Grid({
		loadURL: ctx + "/zbglRdpPlan!pageQuery.action",
		saveURL: ctx + '/zbglRdpPlan!saveZbglRdpPlan.action',             //保存数据的请求URL
    	deleteURL: ctx + '/zbglRdpPlan!logicDelete.action',            //删除数据的请求URL
    	storeAutoLoad:false,
		singleSelect: true, 
		isEdit:false,
		saveFormColNum:2,
		labelWidth:120,
		tbar:[{	
			xtype:'textfield', id:'rail_way_Time_Completed', enableKeyEvents:true, emptyText:i18n.TrainInspectionPlan.quickSearch, listeners: {
	    		keyup: function(filed, e) {
						ZbglRdpPlan.ZbglRdpPlanCompletedGrid.store.load();
	    		}
			}	
		}],
		fields: [{
	   			header:i18n.TrainInspectionPlan.idx, dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
			},{
				header:i18n.TrainInspectionPlan.train, dataIndex:'railwayTime', editor:{  maxLength:50,allowBlank:false }, searcher: {anchor:'98%'}
			},{
				header:'<div>' + i18n.TrainInspectionPlan.track + '<span style="color:green;">' + i18n.TrainInspectionPlan.VehicleNumber + '</span></div>', dataIndex:'rdpNum', editor:{  
					fieldLabel:i18n.TrainInspectionPlan.VehicleNumber2,
			        xtype:'numberfield',
			        allowDecimals:false,
			        allowNegative:false,
			        minValue:1,
			        maxValue:99,
			        allowBlank:false
			        }, 
			        renderer: function(value, metaData, record, rowIndex, colIndex, store) {
						var trackName = Ext.isEmpty(record.get('trackName'))?'':record.get('trackName');
						return trackName+":【"+value+"】";
					},
			        searcher: {anchor:'98%'}
			},{
				header:i18n.TrainInspectionPlan.workGroup, dataIndex:'workTeamID',hidden:true, editor:{ 
					id:"workTeam_comb",
		        	xtype: "Base_combo",
		        	fieldLabel: i18n.TrainInspectionPlan.workGroup,
					fields:['orgid','orgSeq','orgName'],
					hiddenName: "workTeamID",
					business: 'orgDicItem',
					queryParams: {dictTypeId: 'zbglrdpplan'},
					returnField:[
						 {widgetId:"workTeamName",propertyName:"orgName"},
				  		 {widgetId:"workTeamSeq",propertyName:"orgSeq"}
				  	], 
				  	idProperty: 'orgid',
					displayField: "orgName", 
					valueField: "orgid",
					pageSize: 20, 
					minListWidth: 200, 
					editable:false,
					allowBlank: false,
					isAll:true
				}, searcher: {anchor:'98%'}
			},{
	   			header:i18n.TrainInspectionPlan.upDownStreamCode, dataIndex:'toDirectionNo', hidden:true, editor: { xtype:'hidden',id:'toDirectionNo' }
			},{
				header:i18n.TrainInspectionPlan.upstream_downstream, dataIndex:'toDirectionName',hidden:true, editor:{
						id:'toDirection_combo',
						xtype: 'EosDictEntry_combo',
						hiddenName: 'toDirectionName',
						dicttypeid:'FREIGHT_DIRECT',
						displayField:'dictname',valueField:'dictname',
						hasEmpty:"false",
						allowBlank: false,
						returnField: [{widgetId:"toDirectionNo",propertyName:"dictid"}]
			        }, searcher: {anchor:'98%'}
			},{
				header:i18n.TrainInspectionPlan.track2, dataIndex:'trackNo',hidden:true, editor:{
					id:"trackNo_comb",
		        	xtype: "Base_combo",
		        	fieldLabel: i18n.TrainInspectionPlan.track2,
					fields:['idx','trackCode','trackName'],
					hiddenName: "trackNo",
					business: 'stationTrack',
					returnField:[
						 {widgetId:"trackName",propertyName:"trackName"}
				  	], 
				  	idProperty: 'idx',
					displayField: "trackName", 
					valueField: "trackCode",
					pageSize: 20, 
					minListWidth: 200, 
					editable:false,
					allowBlank: false,
					isAll:true
				}, searcher: {anchor:'98%'}
			},{
	   			header:i18n.TrainInspectionPlan.trackName, dataIndex:'trackName', hidden:true, editor: { xtype:'hidden',id:'trackName' }
			},{
				header:i18n.TrainInspectionPlan.checkTimeMin, dataIndex:'checkTime',hidden:true, editor:{    
			        xtype:'numberfield',
			        allowDecimals:false,
			        allowNegative:false,
			        minValue:1,
			        maxValue:999,
			        allowBlank:false
			        }, searcher: {anchor:'98%'}
			},{
				header:i18n.TrainInspectionPlan.plannedStartTime, dataIndex:'planStartTime', xtype:'datecolumn', hidden:true, editor:{allowBlank:false, xtype:'my97date',format: 'Y-m-d H:i' }
			},{
				header:i18n.TrainInspectionPlan.plannedEndTime, dataIndex:'planEndTime', xtype:'datecolumn',  hidden:true,editor:{hidden:true ,xtype:'my97date',format: 'Y-m-d H:i' }
			},{
	   			header:i18n.TrainInspectionPlan.classCode, dataIndex:'classNo', hidden:true, editor: { xtype:'hidden',id:'classNo' }
			},{
				header:i18n.TrainInspectionPlan.className, dataIndex:'className',hidden:true, editor:{
						id:'class_combo',
						xtype: 'EosDictEntry_combo',
						hiddenName: 'className',
						dicttypeid:'CLASS_TYPE',
						displayField:'dictname',valueField:'dictname',
						hasEmpty:"false",
						returnField: [{widgetId:"classNo",propertyName:"dictid"}]
			        }, searcher: {anchor:'98%'}
			},{
				header:i18n.TrainInspectionPlan.status, dataIndex:'rdpPlanStatus',editor:{ xtype:'hidden'},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
					return '<div style="background:'+ ZbglRdpPlan.statusColorArrays[value] +';color:white;width:48px;height:18px;line-height:18px;text-align:center;border-radius:8px;margin-left:10px;">' + ZbglRdpPlan.statusArrays[value] + '</div>';
				}
			},{
				header:i18n.TrainInspectionPlan.trainType, dataIndex:'vehicleType',hidden:true, editor: { xtype:"hidden",value:vehicleType }
			},{
				header:i18n.TrainInspectionPlan.actualStartTime, dataIndex:'realStartTime',hidden:true, editor: { xtype:"hidden" }
			},{
				header:i18n.TrainInspectionPlan.actualEndTime, dataIndex:'realEndTime',hidden:true, editor: { xtype:"hidden" }
			}],
			afterShowSaveWin: function(){
				 // 设置字段只读
				 // this.setReadOnlyColumns(['railwayTime','rdpNum','trackNo'],false);
			},  
			afterShowEditWin: function(record, rowIndex){
				 // 解除字段只读
				 // this.setReadOnlyColumns(['railwayTime','rdpNum','trackNo'],true);
				Ext.getCmp("workTeam_comb").setDisplayValue(record.get("workTeamID"),record.get("workTeamName"));
				Ext.getCmp("trackNo_comb").setDisplayValue(record.get("trackNo"),record.get("trackName"));
			},
			beforeShowEditWin: function(record, rowIndex){  
					return false;
			}
	});
	
	
	// 数据加载前
	ZbglRdpPlan.ZbglRdpPlanGrid.store.on('beforeload', function() {
		var whereList = [];
		if(!Ext.isEmpty(Ext.getCmp('rail_way_Time').getValue())){
			whereList.push({ propName: 'railwayTime', propValue: Ext.getCmp('rail_way_Time').getValue(), compare: Condition.EQ, stringLike: true });
		}
		whereList.push({ propName: 'siteID', propValue:siteID , compare: Condition.EQ, stringLike: false });
		whereList.push({ propName: 'vehicleType', propValue:vehicleType , compare: Condition.EQ, stringLike: false });
		whereList.push({ propName: 'rdpPlanStatus', propValue:STATUS_HANDLED, compare: Condition.NE});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	// 添加加载结束事件
	ZbglRdpPlan.ZbglRdpPlanGrid.getStore().addListener('load',function(me, records, options ){
		if(records.length > 0){
			ZbglRdpPlan.ZbglRdpPlanGrid.getSelectionModel().selectFirstRow();
	       	var sm = ZbglRdpPlan.ZbglRdpPlanGrid.getSelectionModel();
	       	ZbglRdpPlan.setInfoForm(sm);
			ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.getStore().reload();	
		}else{
			ZbglRdpPlan.infoForm.getForm().reset();
			ZbglRdpPlanRecord.rdpPlanIdx = "###" ;
			ZbglRdpPlanRecord.rdpPlanStatus = "###";
			ZbglRdpPlanRecord.railwayTime = "###";
			ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.getStore().reload();
		}
	});
	
	/**
	 * 单击刷新基本信息页面和车辆列表
	 */
	ZbglRdpPlan.ZbglRdpPlanGrid.on("rowclick", function(grid, rowIndex, e){
			var sm = ZbglRdpPlan.ZbglRdpPlanGrid.getSelectionModel();
	       	ZbglRdpPlan.setInfoForm(sm);
			ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.getStore().reload();
	});
	
	
	/** 完成tab页面 start **/
	// 数据加载前
	ZbglRdpPlan.ZbglRdpPlanCompletedGrid.store.on('beforeload', function() {
		var whereList = [];
		if(!Ext.isEmpty(Ext.getCmp('rail_way_Time_Completed').getValue())){
			whereList.push({ propName: 'railwayTime', propValue: Ext.getCmp('rail_way_Time_Completed').getValue(), compare: Condition.EQ, stringLike: true });
		}
		whereList.push({ propName: 'siteID', propValue:siteID , compare: Condition.EQ, stringLike: false });
		whereList.push({ propName: 'vehicleType', propValue:vehicleType , compare: Condition.EQ, stringLike: false });
		whereList.push({ propName: 'rdpPlanStatus', propValue:STATUS_HANDLED , compare: Condition.EQ, stringLike: false });
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	// 添加加载结束事件
	ZbglRdpPlan.ZbglRdpPlanCompletedGrid.getStore().addListener('load',function(me, records, options ){
		if(records.length > 0){
			ZbglRdpPlan.ZbglRdpPlanCompletedGrid.getSelectionModel().selectFirstRow();
	       	var sm = ZbglRdpPlan.ZbglRdpPlanCompletedGrid.getSelectionModel();
	       	ZbglRdpPlan.setInfoForm(sm);
			ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.getStore().reload();	
		}else{
			ZbglRdpPlan.infoForm.getForm().reset();
			ZbglRdpPlanRecord.rdpPlanIdx = "###" ;
			ZbglRdpPlanRecord.rdpPlanStatus = "###";
			ZbglRdpPlanRecord.railwayTime = "###" ;
			ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.getStore().reload();	
		}
	});
	
	/**
	 * 单击刷新基本信息页面和车辆列表
	 */
	ZbglRdpPlan.ZbglRdpPlanCompletedGrid.on("rowclick", function(grid, rowIndex, e){
	       	var sm = ZbglRdpPlan.ZbglRdpPlanCompletedGrid.getSelectionModel();
	       	ZbglRdpPlan.setInfoForm(sm);
			ZbglRdpPlanRecord.ZbglRdpPlanRecordGrid.getStore().reload();
	});
	
	/** 完成tab页面 end **/
		
	/**
	 * 设置基本信息
	 */
	ZbglRdpPlan.setInfoForm = function(sm){
		var form = ZbglRdpPlan.infoForm.getForm();		
		if (sm.getCount() > 0) {	
			var records = sm.getSelections();
			// 设置车辆列表中的计划ID及状态
			ZbglRdpPlanRecord.rdpPlanIdx = records[0].data.idx;
			ZbglRdpPlanRecord.rdpPlanStatus = records[0].data.rdpPlanStatus;
			ZbglRdpPlanRecord.railwayTime = records[0].data.railwayTime;
			// 设置基本信息
			var records = sm.getSelections();
			var idx = records[0].data.idx;
			form.reset();
			// 实际时间
			var realStartTime = '' ;
			var realEndTime = '' ;
			if(realStartTime){
				realStartTime = new Date(records[0].data.realStartTime).format('Y-m-d H:i');
				realEndTime = new Date(records[0].data.realEndTime).format('Y-m-d H:i');
			}
			form.findField("planStartTime").setValue(realStartTime + "~ " + realEndTime);
			form.findField("workTeamName").setValue(records[0].data.workTeamName);
			form.findField("className").setValue(records[0].data.className);
			form.findField("checkTime").setValue(records[0].data.checkTime+"(分钟)");
			form.findField("toDirectionName").setValue(records[0].data.toDirectionName);
			
		}		
	};
	
	/**
	 * 列检信息
	 */
	ZbglRdpPlan.infoForm = new Ext.form.FormPanel({
			labelWidth:ZbglRdpPlan.labelWidth, border: false,
			labelAlign:"left", layout:"column",
			bodyStyle:"padding:10px;",
			defaults:{
				xtype:"container", autoEl:"div", layout:"form", columnWidth:0.5, 
				defaults:{
					style: 'border:none; background:none;', 
					xtype:"textfield", readOnly: true,
					anchor:"100%"
				}
			},
			items:[{
				columnWidth:1,
		        items: [
		        	{ fieldLabel:i18n.TrainInspectionPlan.checkTime, name:"checkTime"}
		        ]
			},{
				columnWidth:1,
		        items: [
		        	{ fieldLabel:i18n.TrainInspectionPlan.realTime, name:"planStartTime"}
		        ]
			},{
				columnWidth:1,
		        items: [
		        	{ fieldLabel:i18n.TrainInspectionPlan.workClass, name:"className" }
		        ]
			},{
				columnWidth:1,
		        items: [
		        	{ fieldLabel:i18n.TrainInspectionPlan.workGroup, name:"workTeamName"}
		        ]
			},{
				columnWidth:1,
		        items: [
			        	{ fieldLabel:i18n.TrainInspectionPlan.upstream_downstream, name:"toDirectionName"}
			        ]
			}]
		});

});