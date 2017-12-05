/**
 * 列检_故障提票
 */
Ext.onReady(function(){
	Ext.namespace('GztpclTicket');                       //定义命名空间
	
	GztpclTicket.labelWidth = 100;                        //表单中的标签名称宽度
	GztpclTicket.fieldWidth = 130;                       //表单中的标签宽度
	
	GztpclTicket.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: i18n.common.tip.loading });
	GztpclTicket.currRec = {};
	
	// 故障登记表单
	GztpclTicket.saveForm = new Ext.form.FormPanel({
		labelAlign:"right",
        layout:"column",
        bodyStyle: 'background-color: #DFE8F6; padding:5px;',
        defaults: { xtype:"container", layout:"form", columnWidth:1,
        	defaults: {
        		xtype:"textfield", 
    		    labelWidth:GztpclTicket.labelWidth, anchor:"98%",
    		    defaults: {
    		    	width: GztpclTicket.fieldWidth
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
        {id: "faultTypeValue", xtype: "hidden", name: "faultTypeValue"}, // 故障类型字典值
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
        {
    		//columnWidth:.5,
        	items:[
           	{
           		xtype: 'fieldset',
           		title: '故障信息',
           		layout: 'column',
                autoHeight:true,
                defaults: {
                	columnWidth:.5,
					layout: 'form',
					border: false,
					bodyStyle: 'background-color: #DFE8F6;',
					defaults: {
		        		xtype:"textfield", 
		    		    labelWidth:GztpclTicket.labelWidth, anchor:"98%",
		    		    defaults: {
		    		    	width: GztpclTicket.fieldWidth
		    		    }
		        	}
                },
           		items :[{
                	columnWidth:.5,
                	items:[{
               			id: 'gzdj_type',
    					allowBlank:false,
    					xtype: 'EosDictEntry_combo',
    					hiddenName: 'type',
    					dicttypeid:'GZDJ_TYPE',
    					displayField:'dictname',
    					valueField:'dictname',
    					fieldLabel: "登记类型"
                    }
                    ]
            	},{
                	columnWidth:.5,
                	items:[{
                		fieldLabel: '车次',
                        xtype:'displayfield',
                        name: "railwayTimeShow"
                    }
                    ]
            	}, {
                	columnWidth:.5,
                	items:[{
                		fieldLabel: '车型',
                        xtype:'displayfield',
                        name: "vehicleTypeCodeShow"
                    }
                    ]
            	}, {
                	columnWidth:.5,
                	items:[{
                		fieldLabel: '车号',
                        xtype:'displayfield',
                        name: "trainNoShow"
                    }
                    ]
            	}, {
	            	items:[{ id: 'scope_work', xtype: 'Base_comboTree', fieldLabel: '专业类型', rootId:'ROOT_0',
           				  hiddenName: 'scopeWorkIdx', returnField: [{widgetId:"scopeWorkFullname",propertyName:"text"}], selectNodeModel: 'exceptRoot',
           				  business: 'zbglRdpPlanRecord', valueField:'id',displayField:'text',
           				  width: GztpclTicket.fieldWidth }]
               	}, {
                   	items:[{ id: 'vehicle_component', xtype: 'Base_comboTree', fieldLabel: '故障部件', rootId:'0',
           				  hiddenName: 'vehicleComponentFlbm', returnField: [{widgetId:"vehicleComponentFullname",propertyName:"text"}], selectNodeModel: 'exceptRoot',
           				  business: 'jcgxBuild', valueField:'flbm',displayField:'text',
           				  width: GztpclTicket.fieldWidth }]
               	}, {
               		columnWidth:1,
                   	items:[
                   	{
                   		xtype: 'textarea',
                   		fieldLabel: '故障描述',
                   		allowBlank:false,
                   		maxLength:500,
                   		height: 60,
                   		name: 'faultDesc'
                   	}]
               	}, {
                   	items:[{ id: 'fault_type', xtype: 'Base_comboTree',hiddenName: 'faultTypeKey', //isExpandAll: true,
						fieldLabel:'故障类型',returnField:[{widgetId:"faultTypeValue",propertyName:"text"}],selectNodeModel:'exceptRoot',
						treeUrl: ctx + '/eosDictEntrySelect!tree.action', queryParams: {'dicttypeid':'FAULT_TYPE'},
						valueField:'id',displayField:'text',
						rootId: 'ROOT_0', rootText: '故障类型', width: GztpclTicket.fieldWidth
					}]
             	}, {
                   	items:[
                   	{
                   		xtype: 'radiogroup',
                   		fieldLabel: '处理类型',
                   		allowBlank:false,
                   		hidden: true,
                   		anchor:"50%",
                   		items: [
                   		    {
                   		    	xtype: 'radio',
                   		    	boxLabel: '登记',
                   		    	name: 'handleType',
                   		    	checked: true,
                   		    	inputValue: HANDLE_TYPE_REG
                   		    },
                   		    {
                   		    	xtype: 'radio',
                   		    	boxLabel: '上报',
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
    		//columnWidth:.5,
        	items:[
           	{
           		xtype: 'fieldset',
           		title: '处理结果',
           		layout: 'column',
                autoHeight:true,
                defaults: {
                	columnWidth:.5,
					layout: 'form',
					border: false,
					bodyStyle: 'background-color: #DFE8F6;',
					defaults: {
		        		xtype:"textfield", 
		    		    labelWidth:GztpclTicket.labelWidth, anchor:"98%",
		    		    defaults: {
		    		    	width: GztpclTicket.fieldWidth
		    		    }
		        	}
                },
           		items :[
		       		{
						columnWidth:1,
						items:[
						{   id:'faultDealType',xtype: 'Base_comboTree',hiddenName: 'handleWay',
							fieldLabel:'处理方式',returnField:[{widgetId:"handleWayValue",propertyName:"text"}],selectNodeModel:'exceptRoot',
							treeUrl: ctx + '/eosDictEntrySelect!tree.action', queryParams: {'dicttypeid':'FAULT_DEAL_TYPE'},
							valueField:'id',displayField:'text',
							rootId: 'ROOT_0', rootText: '处理方式', width: GztpclTicket.fieldWidth
						}				   	
					    ]
					}, {
		       			columnWidth:1,
						items:[{
					   		xtype: 'textarea',
					   		fieldLabel: '处理地点',
					   		maxLength:500,
					   		height: 40,
					   		name: 'handleSite'
					   	}]
					}, {
					columnWidth:1,
					height:60,
					xtype: 'panel',
					buttonAlign: 'center',
					buttons:[
					    {
					    	id: 'saveBtn',
					    	text: '保存',
					    	height:26,
					    	iconCls: 'saveIcon',
					    	disabled: true,
					    	handler:function(){
					    		if (GztpclTicket.currRec && HANDLE_TYPE_REP == GztpclTicket.currRec.handleType 
					    				&& STATUS_CHECKED == GztpclTicket.currRec.faultNoticeStatus) {
					    			alertFail('质量检验已完成，不能修改！');
					    			return;
					    		}
					    		
					    		//表单验证是否通过
					            var form = GztpclTicket.saveForm.getForm();
					            if (!form.isValid()) return;
					            
					            var data = form.getValues();
					            data.faultNoticeStatus = STATUS_OVER;
					            
					            // 获取物料数据
					            var matUses = GztpclTicket.getMatUses();					            
					            
					            if(GztpclTicket.loadMask)   GztpclTicket.loadMask.show();
					            var cfg = {
					                scope: this,
					                url: ctx + '/gztp!saveGztps.action',
					                jsonData: matUses,
            						params : {gztpData : Ext.util.JSON.encode(data)},
					                success: function(response, options){
					                    if(GztpclTicket.loadMask)   GztpclTicket.loadMask.hide();
					                    var result = Ext.util.JSON.decode(response.responseText);
					                    if (result.errMsg == null) {
					                        alertSuccess();
					                        GztpclTicket.resetForm();
					                        MatTypeUseList.grid.store.removeAll();
					                        GztpclTicket.grid.store.reload();
					                    } else {
					                    	alertFail(result.errMsg);
					                    }
					                }
					            };
					            Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
					    	}
					    }
					]
				}]
           	}
            ]
       	}]
	});
	
		// 作业范围显示隐藏
	GztpclTicket.saveForm.addListener('afterrender',function(me){
		if(vehicleType == '10'){
		    Ext.getCmp('scope_work').hide();
		}else{
			Ext.getCmp('scope_work').show();
		}
	});
	
	// 获取物料明细
	GztpclTicket.getMatUses = function(){
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
	GztpclTicket.resetForm = function(){
		var form = GztpclTicket.saveForm.getForm();
		form.reset();
		Ext.getCmp('scope_work').clearValue();
		Ext.getCmp('vehicle_component').clearValue();
		Ext.getCmp('fault_type').clearValue();
		Ext.getCmp('faultDealType').clearValue();
		Ext.getCmp('saveBtn').setDisabled(true);
	}
	
	GztpclTicket.queryTimeout;
	
	GztpclTicket.grid = new Ext.yunda.Grid({
		title : '故障上报列表',
		border: true,
		region: 'center',
	    loadURL: ctx + '/gztp!pageQuery.action',                 //装载列表数据的请求URL
	    viewConfig: {forceFit: false , markDirty: false },
	    selModel : new Ext.grid.CheckboxSelectionModel({singleSelect:true}),
	    tbar : ['refresh','&nbsp;&nbsp;',
	    {
	    	xtype:'textfield', id:'query_input', enableKeyEvents:true, emptyText:'快速检索（车次/车型/车号/提报人）', width:200,
	    	listeners: {
	    		keyup: function(filed, e) {
	    			if (GztpclTicket.queryTimeout) {
	    				clearTimeout(GztpclTicket.queryTimeout);
	    			}
	    			
	    			GztpclTicket.queryTimeout = setTimeout(function(){
						GztpclTicket.grid.store.load();
	    			}, 1000);
	    		}
			}
	    },'->','<span style="color:grey;">选中一条登记数据后在“故障上报处理”中进行处理。&nbsp;&nbsp;</span>'],
		fields: [
	     	{
				header:'列检计划主键', dataIndex:'rdpPlanIdx',width: 120,hidden:true
			},
	     	{
				header:'登记类型', dataIndex:'type',width: 120
			},
	     	{
				header:'登记单号', dataIndex:'faultNoticeCode',width: 120
			},
	     	{
				header:'列车车次', dataIndex:'railwayTime',width: 80
			},
	     	{
				header:'车型主键', dataIndex:'vehicleTypeIdx',width: 120,hidden:true
			},
	     	{
				header:'车型代码', dataIndex:'vehicleTypeCode',width: 100
			},
	     	{
				header:'车辆计划主键', dataIndex:'rdpRecordPlanIdx',width: 120,hidden:true
			},
	     	{
				header:'车号', dataIndex:'trainNo',width: 100
			},
	     	{
				header:'列检车辆数量', dataIndex:'rdpNum',width: 120,hidden:true
			},
	     	{
				header:'作业范围主键', dataIndex:'scopeWorkIdx',width: 120,hidden:true
			},
	     	{
				header:'专业类型', dataIndex:'scopeWorkFullname',width: 160
			},
	     	{
				header:'故障部件分类编码', dataIndex:'vehicleComponentFlbm',hidden:true
			},
	     	{
				header:'故障部件', dataIndex:'vehicleComponentFullname',width: 160
			},
	     	{
				header:'故障描述', dataIndex:'faultDesc',width: 160
			},
			{
				header:'故障类型字典KEY', dataIndex:'faultTypeKey',width: 120,hidden:true
			},
	     	{
				header:'故障类型', dataIndex:'faultTypeValue',width: 100
			},
	     	{
				header:'提报人ID', dataIndex:'noticePersonId',hidden:true
			},
	     	{
				header:'提报人', dataIndex:'noticePersonName',width: 80
			},
	        {
				header:'故障登记时间', dataIndex:'noticeTime', xtype:'datecolumn', format:'Y-m-d H:i:s', width:100, xtype:'datecolumn',width: 170
			},
	     	{
				header:'提票站场', dataIndex:'siteId',width: 120,hidden:true
			},
	     	{
				header:'提票站场名称', dataIndex:'siteName',width: 120,hidden:true
			},
	     	{
				header:'销票人ID', dataIndex:'handlePersonId',width: 120,hidden:true
			},
	     	{
				header:'销票人名称', dataIndex:'handlePersonName',width: 120,hidden:true
			},
	        {
				header:'销票时间', dataIndex:'handleTime', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn', hidden:true
			},
	     	{
				header:'处理地点', dataIndex:'handleSite',width: 120
			},
	     	{
				header:'处理方式', dataIndex:'handleWay',width: 120
			},
	     	{
				header:'整备任务单ID', dataIndex:'rdpIdx',width: 120,hidden:true
			},
	     	{
				header:'处理类型', dataIndex:'handleType',width: 70,hidden:true,
				renderer:function(value, metaData, record, rowIndex, colIndex, store){
					if (HANDLE_TYPE_REG == value) {
						return HANDLE_TYPE_REG_CH;
					}
					return '<span style="color:green;">' + HANDLE_TYPE_REP_CH + '</span>';
				}
			},
	     	{
				header:'状态', dataIndex:'faultNoticeStatus',width: 70,
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
				header:'主键ID', dataIndex:'idx',hidden:true
			}],
			beforeShowEditWin: function(){
				return false;
			}
	});
	
	// 作业范围显示隐藏
	GztpclTicket.grid.addListener('afterrender',function(me){
		if(vehicleType == '10'){
			GztpclTicket.grid.getColumnModel().setHidden(12,true);
		}else{
			GztpclTicket.grid.getColumnModel().setHidden(12,false);
		}
	});
	
	/**
	 * 单击车辆列表后，置到该行数据到form中以便进行修改
	 */
	GztpclTicket.grid.on("rowclick", function(grid, rowIndex, e){
		GztpclTicket.resetForm();
		
		// 获取当前选中行数据，并设置到form中
		var sm = GztpclTicket.grid.getSelectionModel().getSelections();
		if (sm.length <= 0) {
			return;
		}
		
		Gztpcl.westPanel.expand(true);
		
		
		var rowRec = sm[0].data;
		rowRec.railwayTimeShow = rowRec.railwayTime;
		rowRec.vehicleTypeCodeShow = rowRec.vehicleTypeCode;
		rowRec.trainNoShow = rowRec.trainNo;
		
		// 故障部件下拉树参数设置
		var vehicleComponent = Ext.getCmp("vehicle_component");
		vehicleComponent.rootText = rowRec.vehicleTypeCode ? rowRec.vehicleTypeCode : '无';
		vehicleComponent.queryParams = {shortName : rowRec.vehicleTypeCode};
		
		// 作业范围下拉树参数设置
		var scopeWork = Ext.getCmp("scope_work");
		scopeWork.rootText = ("专业类型");
		scopeWork.queryParams = {planIdx : rowRec.rdpPlanIdx};
		
		GztpclTicket.saveForm.getForm().setValues(rowRec);
		Ext.getCmp('scope_work').setDisplayValue(rowRec.scopeWorkIdx, rowRec.scopeWorkFullname);
		Ext.getCmp('vehicle_component').setDisplayValue(rowRec.vehicleComponentFlbm, rowRec.vehicleComponentFullname);
		Ext.getCmp('fault_type').setDisplayValue(rowRec.faultTypeKey, rowRec.faultTypeValue);
		Ext.getCmp('faultDealType').setDisplayValue(rowRec.handleWay, rowRec.handleWayValue);
		Ext.getCmp('gzdj_type').setDisplayValue(rowRec.type, rowRec.type);
		Ext.getCmp('saveBtn').setDisabled(false);
		MatTypeUseList.gztpIdx = rowRec.idx ;
		MatTypeUseList.grid.store.reload();
		GztpclTicket.currRec = rowRec;
	});

	//查询前添加过滤条件
	GztpclTicket.whereList = [];
	GztpclTicket.grid.store.on('beforeload' , function(){
		// 清空查询条件
		GztpclTicket.whereList.length = 0;
		
		GztpclTicket.whereList.push({ propName: 'handleType', propValue:HANDLE_TYPE_REP, compare: Condition.EQ});
		GztpclTicket.whereList.push({ propName: 'siteId', propValue:siteId, compare: Condition.EQ});
		GztpclTicket.whereList.push({ propName: 'vehicleType', propValue:vehicleType, compare: Condition.EQ});
		var queryKey = Ext.getCmp('query_input').getValue();
		if (!Ext.isEmpty(queryKey)) {
			var sql = " (RAILWAY_TIME LIKE '%" + queryKey + "%'"
			        + " OR VEHICLE_TYPE_CODE LIKE '%" + queryKey + "%'"
					+ " OR TRAIN_NO LIKE '%" + queryKey + "%'"
					+ " OR NOTICE_PERSON_NAME LIKE '%" + queryKey + "%') ";
			GztpclTicket.whereList.push({ sql: sql, compare: Condition.SQL});
		}
		
		this.baseParams.whereListJson = Ext.util.JSON.encode(GztpclTicket.whereList);
		this.baseParams.sort = 'faultNoticeStatus';
		this.baseParams.dir = 'DESC';
	});
});