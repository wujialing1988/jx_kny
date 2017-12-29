/**
 * 列检_故障提票
 */
Ext.onReady(function(){
	Ext.namespace('GztpTicket');                       //定义命名空间
	
	GztpTicket.labelWidth = 100;                        //表单中的标签名称宽度
	GztpTicket.fieldWidth = 130;                       //表单中的标签宽度
	
	//定义全局变量保存查询条件
	GztpTicket.searchParam = {};
	
	GztpTicket.rdpPlanIdx ; // 计划ID
	
	GztpTicket.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: i18n.common.tip.loading });
	GztpTicket.formValues = {};
	GztpTicket.currRec = {};
	
	// 故障登记表单
	GztpTicket.saveForm = new Ext.form.FormPanel({
		title : i18n.TruckFaultReg.faultReMaintain,
		region : 'north',
		labelAlign:"right",
		align: "center",
        layout:"column",
        frame:true,
        border:false,
        defaults: { xtype:"container", layout:"form", columnWidth:1,
        	defaults: {
        		xtype:"textfield", 
    		    labelWidth:GztpTicket.labelWidth, anchor:"98%",
    		    defaults: {
    		    	width: GztpTicket.fieldWidth
    		    }
        	}
        },
        items:[
        {xtype: "hidden", name: "idx"}, // idx主键
        {xtype: "hidden", name: "rdpIdx"}, // 整备单主键
        {xtype: "hidden", name: "rdpPlanIdx"}, // 列检计划主键
        {xtype: "hidden", name: "vehicleTypeIdx"}, // 车型主键
        {xtype: "hidden", name: "rdpRecordPlanIdx"}, // 车辆计划主键
        {xtype: "hidden", name: "railwayTime"}, // 车次
        {xtype: "hidden", name: "vehicleTypeCode"}, // 车型
        {xtype: "hidden", name: "trainNo"}, // 车型
        {xtype: "hidden", name: "rdpNum"}, // 列检车辆数量
        {id: "handleWayValue", xtype: "hidden", name: "handleWayValue"}, // 处理方式ID
        
        {xtype: "hidden", name: "faultNoticeCode"}, // 提票单号
        {xtype: "hidden", name: "noticePersonId"}, // 提票人ID
        {xtype: "hidden", name: "noticePersonName"}, // 提票人名称
        //{xtype: "hidden", name: "noticeTime"}, // 提票时间
        {hidden:true, xtype:'my97date', format: 'Y-m-d H:i:s', name: "noticeTime"}, // 提票时间
        {xtype: "hidden", name: "siteId"}, // 提票站场
        {xtype: "hidden", name: "siteName"}, // 提票站场名称
        
        {id: 'faultNoticeStatus', xtype: "hidden", name: "faultNoticeStatus"}, // 票活状态
        {id: 'scopeWorkFullname', xtype: "hidden", name: "scopeWorkFullname"}, // 作业范围全称
        {id: 'vehicleComponentFullname', xtype: "hidden", name: "vehicleComponentFullname"}, // 故障部件全称
        {id: 'vehicleType', xtype: "hidden", name: "vehicleType",value:vehicleType}, // 客货类型
        {id: 'faultId', xtype: "hidden", name: "faultId"}, // 故障现象编码
        {
    		columnWidth:.5,
        	items:[
           	{
           		xtype: 'fieldset',
           		title: i18n.TruckFaultReg.faultInformation,
           		layout: 'column',
//                autoHeight:true,
           		height:230,
                defaults: {
                	columnWidth:.5,
					layout: 'form',
					border: false,
					bodyStyle: 'background-color: #DFE8F6;',
					defaults: {
		        		xtype:"textfield", 
		    		    labelWidth:GztpTicket.labelWidth, anchor:"98%",
		    		    defaults: {
		    		    	width: GztpTicket.fieldWidth
		    		    }
		        	}
                },
           		items :[{
           			items:[{
           			id: 'gzdj_type',
					allowBlank:false,
					xtype: 'EosDictEntry_combo',
					hiddenName: 'type',
					dicttypeid:'GZDJ_TYPE'+vehicleType,
					displayField:'dictname',
					valueField:'dictname',
					fieldLabel: i18n.TruckFaultReg.registerType
                    }
                    ]
	    		},{
                	items:[{
                		fieldLabel: i18n.TruckFaultReg.trainName,
                        xtype:'displayfield',
                        name: "railwayTimeShow"
                    }
                    ]
            	}, {
                	items:[{
                		fieldLabel: i18n.TruckFaultReg.trainType,
                        xtype:'displayfield',
                        name: "vehicleTypeCodeShow"
                    }
                    ]
            	}, {
                	items:[{
                		fieldLabel: i18n.TruckFaultReg.trainNum,
                        xtype:'displayfield',
                        name: "trainNoShow"
                    }
                    ]
            	}, {
            		columnWidth:1,
	            	items:[{ id: 'scope_work', xtype: 'Base_comboTree', fieldLabel: i18n.TruckFaultReg.professionType, rootId:'ROOT_0',hidden:false,
           				  hiddenName: 'scopeWorkIdx', returnField: [{widgetId:"scopeWorkFullname",propertyName:"text"}], selectNodeModel: 'exceptRoot',
           				  business: 'zbglRdpPlanRecord', valueField:'id',displayField:'text',queryParams: {'planIdx':GztpTicket.rdpPlanIdx},
           				  width: GztpTicket.fieldWidth }]
               	}, {
                   	items:[{ 
                   			id: 'vehicle_component', 
                   			xtype: 'Base_comboTree', 
                   			fieldLabel: i18n.TruckFaultReg.faultPart, 
                   			rootId:'0',
                   			allowBlank:false,
                   			hiddenName: 'vehicleComponentFlbm', 
                   			returnField: [{widgetId:"vehicleComponentFullname",propertyName:"text"}], 
                   			selectNodeModel: 'exceptRoot',
                   			business: 'jcgxBuild', 
                   			valueField:'flbm',
                   			displayField:'text',
                   			width: GztpTicket.fieldWidth,
        					listeners : { 
        						"select":function(combo, record, index){
        							Ext.getCmp('fault_Type_Key').setValue("");
        							Ext.getCmp('fault_Type_Value').setValue("");
        							Ext.getCmp('faultId').setValue("");
        							Ext.getCmp('faultName').setValue("");
        						 }
        					}
                   		}]
               	},{
					xtype: 'compositefield', fieldLabel : i18n.TruckFaultReg.faultLibrary, combineErrors: false,
			        items: [{
			               		xtype: 'button',
			               		text: i18n.TruckFaultReg.faultLibrary,
			               		tooltip:i18n.TruckFaultReg.selectFaultLibrary,
			              		width: 90,
			              		handler: function(){
			              			var component = Ext.getCmp('vehicle_component').getValue();
			              			if(Ext.isEmpty(component)){
			              				MyExt.Msg.alert(i18n.TruckFaultReg.Msg4);
			              				return;
			              			}
			              			// 弹出故障库选择窗口
			              			JcxtflFaultSelect.flbm = component ;
			              			JcxtflFaultSelect.faultSelectGrid.store.load();
			              			JcxtflFaultSelect.win.show();
			           	   		}
		           		}]
				}, {
               		columnWidth:1,
                   	items:[
                   	{
                   		id:'faultName',
                   		fieldLabel: i18n.TruckFaultReg.faultPhenomenon,
                   		xtype:'displayfield',
                   		name: 'faultDesc'
                   	}]
               	},{
                   	items:[{
                   		id:'fault_Type_Key',
                   		fieldLabel: i18n.TruckFaultReg.faultId,
                   		hidden:true,
                   		xtype:'displayfield',
                   		name: 'faultTypeKey'
                   	}]
             	}, {
                   	items:[{
                   		id:'fault_Type_Value',
                   		fieldLabel: i18n.TruckFaultReg.faultType,
                   		xtype:'displayfield',
                   		name: 'faultTypeValue'
                   	}]
             	}, {
                   	items:[
                   	{
                   		xtype: 'radiogroup',
                   		fieldLabel: i18n.TruckFaultReg.handleType,
                   		hidden:true,
                   		//allowBlank:false,
                   		//anchor:"50%",
                   		items: [
                   		    {
                   		    	xtype: 'radio',
                   		    	boxLabel: i18n.TruckFaultReg.registe,
                   		    	name: 'handleType',
                   		    	checked: true,
                   		    	inputValue: HANDLE_TYPE_REG
                   		    },
                   		    {
                   		    	xtype: 'radio',
                   		    	boxLabel: i18n.TruckFaultReg.report,
                   		    	name: 'handleType',
                   		    	inputValue: HANDLE_TYPE_REP
                   		    }
                   		],
                   		listeners: {
                   			change : function(self, checkedRadio) {
                   				var faultNoticeStatusHidden = Ext.getCmp('faultNoticeStatus');
                   				if (HANDLE_TYPE_REG == checkedRadio.inputValue) {
                   					faultNoticeStatusHidden.setValue(STATUS_OVER);
                   				} else {
                   					faultNoticeStatusHidden.setValue(STATUS_INIT);
                   				}
                   			}
                   		}
                   	}]
               	}]
           	}
            ]
       	}, {
    		columnWidth:.5,
        	items:[
           	{
           		xtype: 'fieldset',
           		title: i18n.TruckFaultReg.handleResult,
           		layout: 'column',
//                autoHeight:true,
           		height:230,
                defaults: {
                	columnWidth:.5,
					layout: 'form',
					border: false,
					bodyStyle: 'background-color: #DFE8F6;',
					defaults: {
		        		xtype:"textfield", 
		    		    labelWidth:GztpTicket.labelWidth, anchor:"98%",
		    		    defaults: {
		    		    	width: GztpTicket.fieldWidth
		    		    }
		        	}
                },
           		items :[{
					columnWidth:1,
					items:[
					{   id:'faultDealType',xtype: 'Base_comboTree',hiddenName: 'handleWay',
						fieldLabel:i18n.TruckFaultReg.handleMethod,returnField:[{widgetId:"handleWayValue",propertyName:"text"}],selectNodeModel:'exceptRoot',
						treeUrl: ctx + '/eosDictEntrySelect!tree.action', queryParams: {'dicttypeid':'FAULT_DEAL_TYPE'},
						valueField:'id',displayField:'text',
						rootId: 'ROOT_0', rootText: '处理方式', width: GztpTicket.fieldWidth
					}				   	
				    ]
				}, {
           			columnWidth:1,
					items:[{
				   		xtype: 'textarea',
				   		fieldLabel: i18n.TruckFaultReg.handleSite,
				   		maxLength:500,
				   		height: 40,
				   		name: 'handleSite'
				   	}]
				} , {
					columnWidth:1,
					height:70,
					xtype: 'panel',
					buttonAlign: 'center',
					buttons:[
					    {
					    	text: i18n.TruckFaultReg.save,
					    	height:26,
					    	iconCls: 'saveIcon',
					    	handler:function(){
					    		//表单验证是否通过
					            var form = GztpTicket.saveForm.getForm();
					            if (!form.isValid()) return;
					            
					            var faultDesc = Ext.getCmp('faultName').getValue();
					            if(Ext.isEmpty(faultDesc)){
					        		MyExt.Msg.alert(i18n.TruckFaultReg.Msg5);
					        		return;
					            }
					            
					            var data = form.getValues();
					            
					            // 故障类型
					    		var faultTypeKey = Ext.getCmp('fault_Type_Key').getValue();
					    		var faultTypeValue = Ext.getCmp('fault_Type_Value').getValue();

					    		data.faultTypeKey = faultTypeKey ;
					    		data.faultTypeValue = faultTypeValue ;
					    		data.faultDesc = faultDesc ;
					            // 获取物料数据
					            var matUses = GztpTicket.getMatUses();
					            
					            var cfg = {
					                scope: this,
					                url: ctx + '/gztp!saveGztps.action',
					                jsonData: matUses,
            						params : {gztpData : Ext.util.JSON.encode(data)},
					                success: function(response, options){
					                    if(GztpTicket.loadMask)   GztpTicket.loadMask.hide();
					                    var result = Ext.util.JSON.decode(response.responseText);
					                    if (result.errMsg == null) {
					                        alertSuccess();
					                        GztpTicket.resetForm();
					                        GztpTicket.grid.store.reload();
					                        MatTypeUseList.grid.store.removeAll();
					                    } else {
					                    	alertFail(result.errMsg);
					                    }
					                }
					            };
					            
					            // 上报生成扣车记录提醒
					            if('20' == data.handleWay){
					        	    Ext.Msg.confirm(i18n.TruckFaultReg.Msg6, i18n.TruckFaultReg.Msg7, function(btn){
					        	        if(btn != 'yes')    return;
					        	        if(GztpTicket.loadMask)   GztpTicket.loadMask.show();
					        	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
					        	    });
					            }else{
					            	if(GztpTicket.loadMask)   GztpTicket.loadMask.show();
					            	Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
					            }
					            
					    	}
					    }, {
					    	text: i18n.TruckFaultReg.reset,
					    	height:26,
					    	iconCls: 'resetIcon',
					    	handler:function(){
					    		GztpTicket.resetForm();
					    	}
					    }
					]
				}]
           	}
            ]
       	}]
	});
	
	// 作业范围显示隐藏
	GztpTicket.saveForm.addListener('afterrender',function(me){
		if(vehicleType == '10'){
		    Ext.getCmp('scope_work').hide();
		}else{
			Ext.getCmp('scope_work').show();
		}
	});
	
	// 获取物料明细
	GztpTicket.getMatUses = function(){
		var length = MatTypeUseList.store.getCount();
		var record = MatTypeUseList.store.getRange(0,length);
		var datas = new Array();
		if(record.length == 0){
			return datas;
		}
		for (var i = 0; i < record.length; i++) {
			var data = {} ;
			data = record[i].data;
			// 只保存数量不为空的数据
			if(!Ext.isEmpty(data.matCount)){
				datas.push(data);
			}
		}
		return datas ;
	}
	
	// 重置表单
	GztpTicket.resetForm = function(skip){
		var form = GztpTicket.saveForm.getForm();
		form.reset();
		Ext.getCmp('scope_work').clearValue();
		Ext.getCmp('vehicle_component').clearValue();
		Ext.getCmp('fault_Type_Key').setValue("");
		Ext.getCmp('fault_Type_Value').setValue("");
		Ext.getCmp('faultDealType').clearValue();
		Ext.getCmp('gzdj_type').clearValue();
		if (!skip) {
			form.setValues(GztpTicket.formValues);
		}
	}
	
	GztpTicket.grid = new Ext.yunda.Grid({
		title : i18n.TruckFaultReg.faultReList,
		region : 'center',
	    loadURL: ctx + '/gztp!pageList.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/gztp!logicDelete.action',            //删除数据的请求URL
	    //singleSelect: true,
	    storeAutoLoad: false,
	    viewConfig: {forceFit: false , markDirty: false },
	    selModel : new Ext.grid.CheckboxSelectionModel({singleSelect:true}),
	    tbar : ['refresh', 'delete', '<span style="color:grey;">&nbsp;&nbsp;' + i18n.TruckFaultReg.Pro + '</span>'],
		fields: [
	     	{
				header:i18n.TruckFaultReg.inspectionPlanId, dataIndex:'rdpPlanIdx',width: 120,hidden:true
			},
	     	{
				header:i18n.TruckFaultReg.registerType, dataIndex:'type',width: 120
			},
	     	{
				header:i18n.TruckFaultReg.registerNumber, dataIndex:'faultNoticeCode',width: 120
			},
	     	{
				header:i18n.TruckFaultReg.trainNumber, dataIndex:'railwayTime',width: 100
			},
	     	{
				header:i18n.TruckFaultReg.trainTypeIdx, dataIndex:'vehicleTypeIdx',width: 120,hidden:true
			},
	     	{
				header:i18n.TruckFaultReg.trainCode, dataIndex:'vehicleTypeCode',width: 100
			},
	     	{
				header:i18n.TruckFaultReg.trainPlanIdx, dataIndex:'rdpRecordPlanIdx',width: 120,hidden:true
			},
	     	{
				header:i18n.TruckFaultReg.trainNum, dataIndex:'trainNo',width: 100
			},
	     	{
				header:i18n.TruckFaultReg.inspectionNumber, dataIndex:'rdpNum',width: 120,hidden:true
			},
	     	{
				header:i18n.TruckFaultReg.workRangeIdx, dataIndex:'scopeWorkIdx',width: 120,hidden:true
			},
	     	{
				header:i18n.TruckFaultReg.professionType, dataIndex:'scopeWorkFullname',width: 160,hidden:false
			},
	     	{
				header:i18n.TruckFaultReg.faultPartCode, dataIndex:'vehicleComponentFlbm',width: 120,hidden:true
			},
	     	{
				header:i18n.TruckFaultReg.faultPart, dataIndex:'vehicleComponentFullname',width: 160
			},
	     	{
				header:i18n.TruckFaultReg.faultPhenomenon, dataIndex:'faultDesc',width: 160
			},
			{
				header:i18n.TruckFaultReg.faultTypeKey, dataIndex:'faultTypeKey',width: 120,hidden:true
			},
	     	{
				header:i18n.TruckFaultReg.faultType, dataIndex:'faultTypeValue',width: 100
			},
	     	{
				header:i18n.TruckFaultReg.noticePersonId, dataIndex:'noticePersonId',width: 120,hidden:true
			},
	     	{
				header:i18n.TruckFaultReg.noticePersonName, dataIndex:'noticePersonName',width: 100
			},
	        {
				header:i18n.TruckFaultReg.faultReDate, dataIndex:'noticeTime', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn',width: 160
			},
	     	{
				header:i18n.TruckFaultReg.noticeSite, dataIndex:'siteId',width: 120,hidden:true
			},
	     	{
				header:i18n.TruckFaultReg.siteName, dataIndex:'siteName',width: 120,hidden:true
			},
	     	{
				header:i18n.TruckFaultReg.handlePersonId, dataIndex:'handlePersonId',width: 120,hidden:true
			},
	     	{
				header:i18n.TruckFaultReg.handlePersonName, dataIndex:'handlePersonName',width: 120,hidden:true
			},
	        {
				header:i18n.TruckFaultReg.handleDate, dataIndex:'handleTime', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn', hidden:true
			},
	     	{
				header:i18n.TruckFaultReg.handleSite, dataIndex:'handleSite',width: 120
			},
	     	{
				header:i18n.TruckFaultReg.handleMethodValue, dataIndex:'handleWay',width: 120,hidden:true
			},
	     	{
				header:i18n.TruckFaultReg.handleMethod, dataIndex:'handleWayValue',width: 120
			},			
	     	{
				header:i18n.TruckFaultReg.taskListId, dataIndex:'rdpIdx',width: 120,hidden:true
			},
	     	{
				header:i18n.TruckFaultReg.handleType, dataIndex:'handleType',width: 80,hidden:true,
				renderer:function(value, metaData, record, rowIndex, colIndex, store){
					if (HANDLE_TYPE_REG == value) {
						return HANDLE_TYPE_REG_CH;
					}
					return '<span style="color:green;">' + HANDLE_TYPE_REP_CH + '</span>';
				}
			},
	     	{
				header:i18n.TruckFaultReg.status, dataIndex:'faultNoticeStatus',width: 80,hidden:true,
				renderer:function(value, metaData, record, rowIndex, colIndex, store){
					if (STATUS_INIT == value) {
						return '<span style="color:red;">' + STATUS_INIT_CH + '</span>';
					}
					if (STATUS_CHECKED == value) {
						return '<span style="color:green;">' + STATUS_CHECKED_CH + '</span>';
					}
					return STATUS_OVER_CH;
				}
			},
			{
				header:i18n.TruckFaultReg.idx, dataIndex:'idx',hidden:true
			}],
			searchFn: function(searchParam){
				GztpTicket.searchParam = searchParam ;
		        GztpTicket.grid.store.load();
			},
			beforeShowEditWin: function(){
				return false;
			},
			beforeDeleteFn: function(){
				var sm = GztpTicket.grid.getSelectionModel().getSelections();
				if (sm.length > 0) {
					var rec = null;
					for (var i = 0; i < sm.length; i++) {
						rec = sm[i].data;
						if (HANDLE_TYPE_REP == rec.handleType && STATUS_OVER == rec.faultNoticeStatus) {
			    			alertFail(i18n.TruckFaultReg.alertFail1);
			            	return false;
			            }
			    		if (HANDLE_TYPE_REP == rec.handleType && STATUS_CHECKED == rec.faultNoticeStatus) {
			    			alertFail(i18n.TruckFaultReg.alertFail2);
			    			return false;
			    		}
					}
				}
	    		
	    		return true;
			}
	});
	
		// 作业范围显示隐藏
	GztpTicket.grid.addListener('afterrender',function(me){
		if(vehicleType == '10'){
			GztpTicket.grid.getColumnModel().setHidden(12,true);
		}else{
			GztpTicket.grid.getColumnModel().setHidden(12,false);
		}
	});
	
	/**
	 * 单击车辆列表后，置到该行数据到form中以便进行修改
	 */
	GztpTicket.grid.on("rowclick", function(grid, rowIndex, e){
		// 获取当前选中行数据，并设置到form中
		var sm = GztpTicket.grid.getSelectionModel().getSelections();
		if (sm.length <= 0) {
			GztpTicket.resetForm();
			return;
		}
		
		GztpTicket.resetForm(true);
		
		var rowRec = sm[0].data;
		rowRec.railwayTimeShow = rowRec.railwayTime;
		rowRec.vehicleTypeCodeShow = rowRec.vehicleTypeCode;
		rowRec.trainNoShow = rowRec.trainNo;
		
		GztpTicket.saveForm.getForm().setValues(rowRec);
		Ext.getCmp('scope_work').setDisplayValue(rowRec.scopeWorkIdx, rowRec.scopeWorkFullname);
		Ext.getCmp('vehicle_component').setDisplayValue(rowRec.vehicleComponentFlbm, rowRec.vehicleComponentFullname);
		Ext.getCmp('fault_Type_Key').setValue(rowRec.faultTypeKey);
		Ext.getCmp('fault_Type_Value').setValue(rowRec.faultTypeValue);
		Ext.getCmp('faultDealType').setDisplayValue(rowRec.handleWay, rowRec.handleWayValue);
		Ext.getCmp('gzdj_type').setDisplayValue(rowRec.type, rowRec.type);
		GztpTicket.currRec = rowRec;
		MatTypeUseList.gztpIdx = rowRec.idx ;
		MatTypeUseList.grid.store.reload();
		
	});

	//查询前添加过滤条件
	GztpTicket.grid.store.on('beforeload' , function(){
			var searchParam = GztpTicket.searchParam;
			searchParam.siteId = siteId;
			searchParam.vehicleType = vehicleType;
			searchParam = MyJson.deleteBlankProp(searchParam);
			this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	
	// 故障库方法
	JcxtflFaultSelect.submit = function(data){
		Ext.getCmp('faultId').setValue(data.faultId);
		Ext.getCmp('faultName').setValue(data.faultName);
		Ext.getCmp('fault_Type_Key').setValue(data.faultTypeID);
		Ext.getCmp('fault_Type_Value').setValue(data.faultTypeName);
		JcxtflFaultSelect.win.hide();
	};
});