Ext.onReady(function(){
	
	Ext.namespace('CheckFault');
	
	CheckFault.searchParam = [];
	
	CheckFault.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
	
	CheckFault.jt_6 = 10;
	
	CheckFault.jt_28 = 20;
	
	CheckFault.searchForm = new Ext.form.FormPanel({
		align: 'center',
		layout : 'form',
		baseCls: 'x-plain',
		autoScroll : true,
		style : 'padding : 10px',
		defaults : { anchor : '98%'},
		items: [
			{
		        align: 'center', 
		        layout: 'column',
		        labelWidth : 60,
		        baseCls: 'x-plain',
		        items: [
			        {
			            align : 'center', 
			            layout : 'form', 
			            defaultType : 'textfield', 
			            baseCls : 'x-plain',
			            columnWidth : 1, 
			            items: [
							{ 	
								xtype: "TrainType_combo", 
								name:'trainTypeIDX', 
								fieldLabel:'车型', 
								width: 190, 
								hiddenName: "trainTypeIDX", 
								allowBlank: true,
								returnField: [{widgetId:"trainTypeShortNameId",propertyName:"shortName"}],
								displayField: "shortName", valueField: "typeID",
								pageSize: 20, 
								minListWidth: 200, 
								editable:true,
								isCx:'yes',
								listeners : {   
					                "collapse" : function() {   
					                	var trainNo_combo = Ext.getCmp("trainNo_combo");
					                    trainNo_combo.reset();
					                    trainNo_combo.clearValue();
					                    trainNo_combo.queryParams = {"trainTypeIDX":this.getValue()};
					                    trainNo_combo.cascadeStore();
					                }   
					            }
							},
							{ 
								id : 'trainNo_combo',
								xtype: "TrainNo_combo",	
								name:'trainNo', 
								fieldLabel:'车号',	
								width: 190 ,
								allowBlank:true ,
								hiddenName: "trainNo", 
								displayField: "trainNo", valueField: "trainNo",
								pageSize: 20, minListWidth: 200, editable:true
							},
							{ 
								xtype: 'EosDictEntry_combo',
								name:'type', 
								fieldLabel:'提票类型',	
								width: 190, 
								allowBlank:true,
								hiddenName: 'type',
								dicttypeid:'JCZL_FAULT_TYPE',
								displayField:'dictname',valueField:'dictid'
							 },
							{ 
								name:'fixPlaceFullName',	
								vtype:'validChar', 
								fieldLabel:'故障位置',	
								width: 190 
							},
							
							{
								xtype:'fieldset',
								title: '基本信息',
								collapsible: false,
								autoHeight:true,
								defaults: {anchor: '-20'},
								defaultType: 'textfield',
								items :[
									{
								        fieldLabel: '车型'
								    }, {
								        fieldLabel: '车号'
								    }, {
								        fieldLabel: '干你妹儿3000'
							        }
							    ]
							}
							
							
			            ]
			        }
		        ]
			}
		]
	});
	
	CheckFault.searchWin = new Ext.Window({
	    title : '查询', 
	    //iconCls : 'searchIcon',
	    resizable : false,
	    maximizable : true,
	    layout : 'fit',
	    width : 330, 
	    autoHeight : true,
	    plain : true, 
	    closeAction : 'hide', 
	    modal : true,
	    items : CheckFault.searchForm, 
	    buttonAlign : 'center',
	    buttons: [
	    	{
		        text: '查询', 
		        iconCls: 'searchIcon',
		        handler: function(){
		        	//获取到表单的值
				    var formValue = CheckFault.searchForm.getForm().getValues();
				    for(prop in formValue){  
				    	if(formValue[prop] != ''){
				    		CheckFault.searchParam.push({propName:prop, propValue:formValue[prop], compare: 1});
				    	}
					}
				    CheckFault.store.load({
				        params : {
		                       whereListJson : Ext.util.JSON.encode(CheckFault.searchParam)
		                }
				    });
				    CheckFault.searchParam.length = 0;
		        }
		    }, 
		    {
		        text :'重置', 
		        iconCls:'resetIcon',
		        handler:function(){
		        	//重置表单
		        	CheckFault.searchForm.getForm().reset(); 
		        	//清空查询条件
		        	CheckFault.searchParam.length = 0;
		        	//加载数据
		        	CheckFault.store.load();
		        	//隐藏窗口
		        	CheckFault.searchWin.hide();
		        }
		    }
	   ]
	});
	
	CheckFault.editForm = new Ext.form.FormPanel({
		align : 'center',
		layout : 'form',
		baseCls : 'x-plain',
		autoScroll : true,
		labelWidth : 85,
		style : 'padding : 10px',
		defaults : { anchor : '98%'},
		items : [
			{
				align : 'center',
				layout : 'form',
				defaultType : 'textfield',
				baseCls : 'x-plain',
				items : [
							{name : 'idx', hidden : true}
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
								name : 'faultNoticeCode', 
								fieldLabel : '提票单号', 
								allowBlank : false, 
								width : 165
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
								id : 'EosDictEntry_combo_view',
								xtype: 'EosDictEntry_combo',
								name:'type', 
								fieldLabel:'提票类型',	
								allowBlank:true,
								hiddenName: 'type',
								dicttypeid:'JCZL_FAULT_TYPE',
								displayField:'dictname',
								valueField:'dictid'
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
								id : 'TrainType_combo_view',
								xtype: "TrainType_combo", 
								name:'trainTypeIDX', 
								fieldLabel:'车&emsp;&emsp;型', 
								width: 165, 
								hiddenName: "trainTypeIDX", 
								allowBlank: true,
								displayField: "shortName", valueField: "typeID",
								pageSize: 20, 
								//minListWidth: 180, 
								editable:false,
								isCx:'yes',
								listeners : {   
					                "collapse" : function() {   
					                	var trainNo_combo = Ext.getCmp("trainNo_combo_view");
					                    trainNo_combo.reset();
					                    trainNo_combo.clearValue();
					                    trainNo_combo.queryParams = {"trainTypeIDX":this.getValue()};
					                    trainNo_combo.cascadeStore();
					                }   
					            }
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
								id : 'trainNo_combo_view',
								xtype: "TrainNo_combo",	
								name:'trainNo', 
								fieldLabel:'车&emsp;&emsp;号',	
								width: 172, 
								allowBlank:true ,
								hiddenName: "trainNo", 
								displayField: "trainNo", 
								valueField: "trainNo",
								pageSize: 20, 
								//minListWidth: 180, 
								editable:true
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
								name : 'noticePersonName', 
								fieldLabel : '提 票 人', 
								allowBlank : false, 
								width : 165
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
								xtype: 'combo',	        	        
								fieldLabel : '处理状态',
								name : 'status',
						        hiddenName:'status',
						        store:new Ext.data.SimpleStore({
								    fields: ['v', 't'],
									data : [[STATUS_DRAFT, "未处理"], [STATUS_OPEN, "处理中"], [STATUS_OVER, "已处理"]]
								}),
								valueField:'v',
								displayField:'t',
								triggerAction:'all',
								mode:'local'
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
								name : 'faultNoticeTime', 
								xtype : 'my97date', 
								my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, 
								fieldLabel : '提票时间', 
								width : 165
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
								name : 'faultOccurDate', 
								xtype : 'my97date', 
								my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, 
								fieldLabel : '故障发生时间', 
								width : 172
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
								name : 'faultName', 
								fieldLabel : '故障现象', 
								allowBlank : false, 
								width : 165
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
						columnWidth : 1,
						items : [
							{
								xtype : 'textarea',
								name : 'faultDesc', 
								fieldLabel : '故障描述', 
								allowBlank : false, 
								width : 450
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
						columnWidth : 1,
						items : [
							{
								xtype : 'textarea',
								name : 'fixPlaceFullName', 
								fieldLabel : '故障位置', 
								allowBlank : false, 
								width : 450
							}
						]
					}
				]
			},
			{
				align : 'center',
				layout : 'column',
				baseCls : 'x-plain',
				items:[{
						align : 'center',
						layout : 'form',
						defaultType : 'textfield',
						baseCls : 'x-plain',
						columnWidth : 1,
						items : [
							{
								name : 'partsName', 
								fieldLabel : '配件名称', 
								allowBlank : false, 
								width : 450
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
						columnWidth : 1,
						items : [
							{
								name : 'specificationModel', 
								fieldLabel : '规格型号', 
								allowBlank : false, 
								width : 450
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
						columnWidth : 1,
						items : [
							{
								name : 'nameplateNo', 
								fieldLabel : '铭 牌 号', 
								allowBlank : false, 
								width : 450
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
						columnWidth : 1,
						items : [
							{
								name : 'partsNo', 
								fieldLabel : '配件编号', 
								allowBlank : false, 
								width : 450
							}
						]
					}
				]
			}
		],
		listeners : {
			'render' : function(form){
				disableOrEnableAllColumns(this, CheckFault.store, true);
			}
		}
	});
	
	CheckFault.editWin = new Ext.Window({
		title : '编辑',
		//iconCls : 'editIcon',
		resizable : false,
		layout : 'fit',
		autoHeight : true,
		width : 600,
		plain : true,
		closeAction : 'hide',
		maximizable : false,
		modal : true,
		items : CheckFault.editForm,
		buttonAlign : 'center',
		buttons : [
			/*
			//被引用了这个页面, 只需要关闭按钮就行
			{
				id : '_submitBtm',
				text : '审核确认',
				iconCls : 'acceptIcon',
				handler : confirmSelected
			},*/
			{
				id : '_cancelBtn',
				text : '关闭',
				iconCls : 'closeIcon',
				handler : function(btn){
					CheckFault.editWin.hide();
				}
			}
		]
	});
	
	CheckFault.store = new Ext.data.JsonStore({
		id : 'idx',
		url : ctx + '/faultNotice!pageQuery.action',
	    root : 'root',
	    totalProperty : 'totalProperty', 
	    autoLoad : false,
	    fields: [
	    		'idx',
				'faultNoticeCode',
				'trainTypeIDX',
				'trainNo',
				'trainTypeShortName',
				'realFaultID',
				'faultID',
				'realFaultName',
				'faultName',
				'faultDesc',
				'faultFixPlaceIDX',
				'realFixPlaceIDX',
				'fixPlaceFullCode',
				'realFixPlaceFullCode',
				'fixPlaceFullName',
				'realFixPlaceFullName',
				'faultNoticeOrgseq',
				'faultNoticeOrgname',
				'partsAccountIDX',
				'realPartsAccountIDX',
				'partsTypeIDX',
				'realPartsTypeIDX',
				'partsName',
				'realPartsName',
				'specificationModel',
				'realSpecificationModel',
				'nameplateNo',
				'realNameplateNo',
				'partsNo',
				'realPartsNo',
				'noticePersonName',
				'methodId',
				'methodName',
				'methodDesc',
				'repairResult',
				'dropPartsNo',
				'dropConfigInfoIdx',
				'fixPartsNo',
				'fixConfigInfoIdx',
				'siteID',
				'status',
				'type',
				'isDrop',
				'recordStatus',
				'noticePersonId',
				'creator',
				'updator',
				'faultNoticeOrgid',
				{name:'createTime', type: 'date', dateFormat : 'time'},
				{name:'completeTime', type: 'date', dateFormat : 'time'},
				{name:'updateTime', type: 'date', dateFormat : 'time'},
				{name:'faultNoticeTime', type: 'date', dateFormat : 'time'},
				{name:'faultOccurDate', type: 'date', dateFormat : 'time'}
			]/*,
	    		//张凡的值班员信息确认页面引用这个grid, 这里就不用分辨JT-6和JT-28了
		    listeners : {
		    	  'beforeload' : function(store, options){
	    	  			this.baseParams.whereListJson = Ext.util.JSON.encode([{propName : 'type', propValue : Ext.getCmp('_faultTypeRadioGroup').getValue().inputValue, compare : Condition.EQ }]);
		    	  }  
		    }*/
	});
	
	CheckFault.pageTbar = Ext.yunda.createPagingToolbar({store : CheckFault.store});
	
	CheckFault.selModel = new Ext.grid.CheckboxSelectionModel({
		singleSelect : false
	});
	
	CheckFault.colModel = new Ext.grid.ColumnModel([
		//CheckFault.selModel,
		new Ext.grid.RowNumberer(),
		{
			header:'idx主键', dataIndex:'idx', hidden:true
		},
		Attachment.createColModeJson({ attachmentKeyName:'faultAtt', attachmentKeyIDX:'idx'}),{
			header:'提票单号', dataIndex:'faultNoticeCode', width: 85, sortable: true, menuDisabled : true
		},{
			header:'提票类型', dataIndex:'type', width: 70, sortable: true, menuDisabled : true,
			renderer: function(v){
				return EosDictEntry.getDictname('JCZL_FAULT_TYPE',v);
			}
		},{
			header:'车型', dataIndex:'trainTypeIDX', width: 60, sortable: true, menuDisabled : true,
			renderer:function(v,x,r){
				//使用record中的车型简称字段来作为显示
				return r.get("trainTypeShortName");
			}
		},{
			header:'车型', dataIndex:'trainTypeShortName', hidden:true
		},{
			header:'车号', dataIndex:'trainNo', width: 60, sortable: true, menuDisabled : true
		},{
			header:'提票状态', dataIndex:'status', width: 60, sortable: true, menuDisabled : true,
			renderer: function(v){
				switch(v){
					case STATUS_DRAFT:
						return STATUS_DRAFT_CH;
					case STATUS_OPEN:
						return STATUS_OPEN_CH;
					case STATUS_OVER:
						return STATUS_OVER_CH;
					case STATUS_ZLX:
						return STATUS_ZLX_CH;
					case STATUS_LWFX:
						return STATUS_LWFX_CH;
					case STATUS_CANCEL:
						return STATUS_CANCEL_CH;
					default:
						return v;
				}
			}
		},{
			header:'提票人ID', dataIndex:'noticePersonId',hidden:true
		},{
			header:'提票人名称', dataIndex:'noticePersonName', width: 80, sortable: true, menuDisabled : true
		},{
			header:'提票时间', dataIndex:'faultNoticeTime', xtype:'datecolumn', format : 'Y-m-d H:i', width: 120, sortable: true, menuDisabled : true
		},{
			header:'故障ID', dataIndex:'faultID',hidden:true
		},{
			header:'故障现象', dataIndex:'faultName', width: 110, sortable: true, menuDisabled : true
		},{
			header:'故障描述', dataIndex:'faultDesc', width: 110, sortable: true, menuDisabled : true
		},{
			header:'故障位置主键', dataIndex:'faultFixPlaceIDX',hidden:true
		},{
			header:'故障编码全名', dataIndex:'fixPlaceFullCode',hidden:true
		},{
			header:'故障位置', dataIndex:'fixPlaceFullName', width: 250, sortable: true, menuDisabled : true
		},{
			header:'故障发生日期', dataIndex:'faultOccurDate', xtype:'datecolumn', format : 'Y-m-d H:i', width: 120, sortable: true, menuDisabled : true
		},{
			header:'提票班组ID', dataIndex:'faultNoticeOrgid', hidden:true
		},{
			header:'提票班组序列', dataIndex:'faultNoticeOrgseq',hidden:true
		},{
			header:'提票班组名称', dataIndex:'faultNoticeOrgname', width: 80, sortable: true, menuDisabled : true
		},{
			header:'互换配件信息主键', dataIndex:'partsAccountIDX',hidden:true
		},{
			header:'互换配件型号主键', dataIndex:'partsTypeIDX',hidden:true
		},{
			header:'配件名称', dataIndex:'partsName', width: 100, sortable: true, menuDisabled : true
		},{
			header:'规格型号', dataIndex:'specificationModel', width: 100, sortable: true, menuDisabled : true
		},{
			header:'铭牌号', dataIndex:'nameplateNo', width: 110, sortable: true, menuDisabled : true
		},{
			header:'配件编号', dataIndex:'partsNo',width: 300, sortable: true, menuDisabled : true
		},{
			header:'提票类型名称', dataIndex:'typeName',hidden:true
		},{
			header:'组成型号主键', dataIndex:'buildUpTypeIDX',hidden:true
		},{
			header:'组成型号', dataIndex:'buildUpTypeName',hidden:true
		},{
			header:'组成型号代码', dataIndex:'buildUpTypeCode',hidden:true
		}
		]);
	
	//grid双击事件
	CheckFault.rowdblclick = function(grid, rowIndex, event){
		CheckFault.editWin.show();
		CheckFault.editWin.setTitle('提票详情');
		var record = grid.store.getAt(rowIndex);
		if(record){
			CheckFault.editForm.getForm().reset();
			CheckFault.editForm.getForm().loadRecord(record);
			Ext.getCmp('TrainType_combo_view').setDisplayValue(record.get('trainTypeIDX'),record.get('trainTypeShortName'));
			Ext.getCmp('trainNo_combo_view').setDisplayValue(record.get('trainNo'),record.get('trainNo'));
			Ext.getCmp('EosDictEntry_combo_view').setDisplayValue(record.get('type'),EosDictEntry.getDictname('JCZL_FAULT_TYPE',record.get('type')));
		}
	}
		
	CheckFault.grid = new Ext.grid.GridPanel({
		//viewConfig : {forceFit : true}
		store : CheckFault.store,
		selModel : CheckFault.selModel,
		colModel : CheckFault.colModel,
		stripeRows : true,
		bbar : CheckFault.pageTbar,
		tbar : [
				{
					text : '查询',
					iconCls : 'searchIcon',
					handler : function(btn){
						CheckFault.searchWin.show();
					}
				},
				{
				 	text: "审核确认", 
				 	iconCls: "acceptIcon", 
				 	handler: confirmSelected
				},
				{
					text : '刷新',
					iconCls : 'refreshIcon',
					handler : function(btn){
						self.location.reload();
					}
				},'-',
				{
					xtype : 'label',
					text : '提票类型：'
				},
				{ 
					id : '_faultTypeRadioGroup',
					xtype: 'radiogroup',
		            height:20,
		            value : CheckFault.jt_6,
		            items: [
		                {
		                	id:"_jt_6_radio", 
		                	name: 'type', 
		                	boxLabel: EosDictEntry.getDictname('JCZL_FAULT_TYPE', CheckFault.jt_6), 
		                	inputValue: CheckFault.jt_6
		                },
		                {
		                	id:"_jt_28_radio", 
		                	name: 'type', 
		                	boxLabel: EosDictEntry.getDictname('JCZL_FAULT_TYPE',CheckFault.jt_28), 
		                	inputValue: CheckFault.jt_28
		                }
		            ],
		            listeners : {
		            	'change' : function(field, newValue, oldValue){
		            		CheckFault.store.load();
		            	} 
		            }
				}
				/*,'-',
				{
					xtype : 'label',
					text : '提票类型：'
				},
				{
					xtype: 'EosDictEntry_combo',
					hiddenName: 'type',
					dicttypeid:'JCZL_FAULT_TYPE',
					displayField:'dictname',valueField:'dictid',
					allowBlank:true,
					listeners : {
						'select' : function(combo, record, index){
							var dictid = record.get('dictid');
							if(dictid){
							    CheckFault.searchParam.push({propName : 'type', propValue : dictid, compare: 1});
							    CheckFault.store.load({
							        params : {
					                       whereListJson : Ext.util.JSON.encode(CheckFault.searchParam)
					                }
							    });
							} else {
								CheckFault.store.load();
							}
						    CheckFault.searchParam.length = 0;
						}
					}
				}*/
			],
			listeners : {
				'rowdblclick' : CheckFault.rowdblclick
			}
	});
	
	
	
	var disableOrEnableAllColumns = function(form, store, isDisble){
        if(Ext.isEmpty(form)){
            return;
        }
        store.fields.eachKey(function(key,keyObj,index,length){
        	var field = form.find('name',key)[0];
        	if(field){
	        	field.setDisabled(isDisble);
        	}
        });
    }
    
    var confirmSelected = function(){
	 		var selections = CheckFault.grid.getSelectionModel().getSelections();
			if(selections.length == 0){
				MyExt.Msg.alert('请选择要操作的记录!');
				return ;
			}
			var submitIds = new Array();
			for (var i=0; i < selections.length; i++){
				submitIds.push(selections[i].get('idx'));
			}
			Ext.Msg.confirm('提示!','是否确认提交所选择的数据!', function(btn){
				if(btn == 'yes'){
					CheckFault.loadMask.show();
					Ext.Ajax.request({
		                url: ctx + '/faultNotice!confirmFault.action',
		                params: {ids: submitIds},
		                success: function(response, options){
		                  	CheckFault.loadMask.hide();
		                    var result = Ext.util.JSON.decode(response.responseText);
		                    if (result.errMsg == null) {
		                        alertSuccess();
		                        CheckFault.store.reload();
		                    } else {
		                        alertFail(result.errMsg);
		                    }
		                },
		                failure: function(response, options){
		                    CheckFault.loadMask.hide();
		                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		                }
		            });
				}
			})
    }
	
	var viewport = new Ext.Viewport({
		layout : 'fit',
		items : CheckFault.grid
	});
});