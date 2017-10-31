/**
 * 列检_故障提票
 */
Ext.onReady(function(){
	Ext.namespace('GztpTicket');                       //定义命名空间
	
	GztpTicket.labelWidth = 60;                        //表单中的标签名称宽度
	GztpTicket.fieldWidth = 130;                       //表单中的标签宽度
	
	//定义全局变量保存查询条件
	GztpTicket.searchParam = {};
	
	GztpTicket.rdpPlanIdx ; // 计划ID
	
	GztpTicket.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: i18n.common.tip.loading });
	GztpTicket.formValues = {};
	GztpTicket.currRec = {};
	
	// 故障登记表单
	GztpTicket.saveForm = new Ext.form.FormPanel({
		title : '故障登记维护',
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
    		columnWidth:.5,
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
		    		    labelWidth:GztpTicket.labelWidth, anchor:"98%",
		    		    defaults: {
		    		    	width: GztpTicket.fieldWidth
		    		    }
		        	}
                },
           		items :[{
                	columnWidth:.32,
                	items:[{
                		fieldLabel: '车次',
                        xtype:'displayfield',
                        name: "railwayTimeShow"
                    }
                    ]
            	}, {
                	columnWidth:.32,
                	items:[{
                		fieldLabel: '车型',
                        xtype:'displayfield',
                        name: "vehicleTypeCodeShow"
                    }
                    ]
            	}, {
                	columnWidth:.36,
                	items:[{
                		fieldLabel: '车号',
                        xtype:'displayfield',
                        name: "trainNoShow"
                    }
                    ]
            	}, {
	            	items:[{ id: 'scope_work', xtype: 'Base_comboTree', fieldLabel: '专业类型', rootId:'ROOT_0',hidden:false,
           				  hiddenName: 'scopeWorkIdx', returnField: [{widgetId:"scopeWorkFullname",propertyName:"text"}], selectNodeModel: 'exceptRoot',
           				  business: 'zbglRdpPlanRecord', valueField:'id',displayField:'text',queryParams: {'planIdx':GztpTicket.rdpPlanIdx},
           				  width: GztpTicket.fieldWidth }]
               	}, {
                   	items:[{ id: 'vehicle_component', xtype: 'Base_comboTree', fieldLabel: '故障部件', rootId:'0',
           				  hiddenName: 'vehicleComponentFlbm', returnField: [{widgetId:"vehicleComponentFullname",propertyName:"text"}], selectNodeModel: 'exceptRoot',
           				  business: 'jcgxBuild', valueField:'flbm',displayField:'text',
           				  width: GztpTicket.fieldWidth }]
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
						rootId: 'ROOT_0', rootText: '故障类型', width: GztpTicket.fieldWidth
					}]
             	}, {
                   	items:[
                   	{
                   		xtype: 'radiogroup',
                   		fieldLabel: '处理类型',
                   		//allowBlank:false,
                   		//anchor:"50%",
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
    		columnWidth:.5,
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
						fieldLabel:'处理方式',returnField:[{widgetId:"handleWayValue",propertyName:"text"}],selectNodeModel:'exceptRoot',
						treeUrl: ctx + '/eosDictEntrySelect!tree.action', queryParams: {'dicttypeid':'FAULT_DEAL_TYPE'},
						valueField:'id',displayField:'text',
						rootId: 'ROOT_0', rootText: '处理方式', width: GztpTicket.fieldWidth
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
				} , {
					columnWidth:1,
					height:70,
					xtype: 'panel',
					buttonAlign: 'center',
					buttons:[
					    {
					    	text: '保存',
					    	height:26,
					    	iconCls: 'saveIcon',
					    	handler:function(){
					    		if (GztpTicket.currRec && HANDLE_TYPE_REP == GztpTicket.currRec.handleType 
					            		&& STATUS_OVER == GztpTicket.currRec.faultNoticeStatus) {
					    			alertFail('上报数据已处理，不能修改！');
					            	return;
					            }
					    		if (GztpTicket.currRec && HANDLE_TYPE_REP == GztpTicket.currRec.handleType 
					    				&& STATUS_CHECKED == GztpTicket.currRec.faultNoticeStatus) {
					    			alertFail('质量检验已完成，不能修改！');
					    			return;
					    		}
					    		
					    		//表单验证是否通过
					            var form = GztpTicket.saveForm.getForm();
					            if (!form.isValid()) return;
					            
					            var data = form.getValues();
					            
					            // 获取物料数据
					            var matUses = GztpTicket.getMatUses();
					            
					            if(GztpTicket.loadMask)   GztpTicket.loadMask.show();
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
					            Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
					    	}
					    }, {
					    	text: '重置',
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
		Ext.getCmp('fault_type').clearValue();
		Ext.getCmp('faultDealType').clearValue();
		if (!skip) {
			form.setValues(GztpTicket.formValues);
		}
	}
	
	GztpTicket.grid = new Ext.yunda.Grid({
		title : '故障登记列表',
		region : 'center',
	    loadURL: ctx + '/gztp!pageList.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/gztp!logicDelete.action',            //删除数据的请求URL
	    //singleSelect: true,
	    storeAutoLoad: false,
	    selModel : new Ext.grid.CheckboxSelectionModel({singleSelect:true}),
	    tbar : ['refresh', 'delete', '<span style="color:grey;">&nbsp;&nbsp;选中一条登记数据后在“故障登记维护”中进行修改。</span>'],
		fields: [
	     	{
				header:'列检计划主键', dataIndex:'rdpPlanIdx',width: 120,hidden:true
			},
	     	{
				header:'登记单号', dataIndex:'faultNoticeCode',width: 120
			},
	     	{
				header:'列车车次', dataIndex:'railwayTime',width: 100
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
				header:'专业类型', dataIndex:'scopeWorkFullname',width: 160,hidden:false
			},
	     	{
				header:'故障部件分类编码', dataIndex:'vehicleComponentFlbm',width: 120,hidden:true
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
				header:'提票人ID', dataIndex:'noticePersonId',width: 120,hidden:true
			},
	     	{
				header:'提票人名称', dataIndex:'noticePersonName',width: 100
			},
	        {
				header:'故障登记时间', dataIndex:'noticeTime', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn',width: 160
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
				header:'处理方式值', dataIndex:'handleWay',width: 120,hidden:true
			},
	     	{
				header:'处理方式', dataIndex:'handleWayValue',width: 120
			},			
	     	{
				header:'整备任务单ID', dataIndex:'rdpIdx',width: 120,hidden:true
			},
	     	{
				header:'处理类型', dataIndex:'handleType',width: 80,
				renderer:function(value, metaData, record, rowIndex, colIndex, store){
					if (HANDLE_TYPE_REG == value) {
						return HANDLE_TYPE_REG_CH;
					}
					return '<span style="color:green;">' + HANDLE_TYPE_REP_CH + '</span>';
				}
			},
	     	{
				header:'状态', dataIndex:'faultNoticeStatus',width: 80,
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
			    			alertFail('上报数据已处理，不能删除！');
			            	return false;
			            }
			    		if (HANDLE_TYPE_REP == rec.handleType && STATUS_CHECKED == rec.faultNoticeStatus) {
			    			alertFail('质量检验已完成，不能删除！');
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
			GztpTicket.grid.getColumnModel().setHidden(11,true);
		}else{
			GztpTicket.grid.getColumnModel().setHidden(11,false);
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
		Ext.getCmp('fault_type').setDisplayValue(rowRec.faultTypeKey, rowRec.faultTypeValue);
		Ext.getCmp('faultDealType').setDisplayValue(rowRec.handleWay, rowRec.handleWayValue);
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
});