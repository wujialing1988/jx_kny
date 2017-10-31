/**
 * 
 * 机车用料消耗记录查询js
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatTrainExpendAccountQuery');
	
	/** 获取最近的一个月 */
	var dateNow = new Date();
	var month = dateNow.getMonth();
	var year = dateNow.getFullYear();
	if (month == 0) {
		dateNow.setFullYear(year - 1);
		dateNow.setMonth(11);
	} else {
		dateNow.setMonth(month-1);
	}
	var lastMonth = dateNow.format('Y-m-d');
	
	/** ************** 定义全局变量开始 ************** */
	MatTrainExpendAccountQuery.searchParams = {};
	MatTrainExpendAccountQuery.labelWidth = 80;
	MatTrainExpendAccountQuery.fieldWidth = 100;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义查询表单开始 ************** */
	MatTrainExpendAccountQuery.searchForm = new Ext.form.FormPanel({
		labelWidth: MatTrainExpendAccountQuery.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 15px',
		items:[{
			// 查询表单第1行
			baseCls: "x-plain",
			layout: 'column',
			items:[{												// 第1行第1列
				columnWidth: .25,
				layout: 'form',
				items: [{
					id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "车型",
					hiddenName: "trainTypeIDX",
					displayField: "shortName", valueField: "typeID", width:MatTrainExpendAccountQuery.fieldWidth,
					pageSize: 20,
					editable:true,
					listeners : {   
			        	"select" : function() {   
			            	//重新加载车号下拉数据
			                var trainNo_comb = Ext.getCmp("trainNo_comb");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
			                trainNo_comb.cascadeStore();
			                //重新加载修程下拉数据
			                var rc_comb = Ext.getCmp("rc_comb");
			                rc_comb.reset();
			                rc_comb.clearValue();
			                rc_comb.getStore().removeAll();
			                rc_comb.queryParams = {"TrainTypeIdx":this.getValue()};
			                rc_comb.cascadeStore();
							//重新加载修次数据
    	                	var rt_comb = Ext.getCmp("rt_comb");
    	                	rt_comb.clearValue();
    	                 	rt_comb.reset();
    	                 	rt_comb.getStore().removeAll();
    	                 	rt_comb.cascadeStore();
			        	}   
			    	}
				}]
			}, {													// 第1行第2列
				columnWidth: .25,
				layout: 'form',
				items: [{
					id:"trainNo_comb",xtype: "TrainNo_combo",	fieldLabel: "车号",
					hiddenName: "trainNo",width:MatTrainExpendAccountQuery.fieldWidth,
					displayField: "trainNo", valueField: "trainNo",
					pageSize: 20, minListWidth: 200, 
					editable:true,
					listeners: {
						"beforequery" : function(){
	    					//选择修次前先选车型
							var trainTypeId =  Ext.getCmp("trainType_comb").getValue();
	    					if(trainTypeId == "" || trainTypeId == null){
	    						MyExt.Msg.alert("请先选择车型！");
	    						return false;
	    					}
	                	}
	    			}
				}]
			}, {													// 第1行第3列
				columnWidth: .25,
				layout: 'form',
				items: [{
					id:"rc_comb",
        			xtype: "Base_combo",
        			business: 'trainRC',
					entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
					fields:['xcID','xcName'],
        			fieldLabel: "修程",
        			hiddenName: "xcId", 
        			displayField: "xcName",
        			valueField: "xcID",
        			pageSize: 20, minListWidth: 200,
        			queryHql: 'from UndertakeRc',
        			width: MatTrainExpendAccountQuery.fieldWidth,
        			listeners : {  
        				"beforequery" : function(){
	    					//选择修次前先选车型
	                		var trainTypeId =  Ext.getCmp("trainType_comb").getValue();
	    					if(trainTypeId == "" || trainTypeId == null){
	    						MyExt.Msg.alert("请先选择车型！");
	    						return false;
	    					}
	                	},
                    	"select" : function() {   
                    		//重新加载修次数据
                        	var rt_comb = Ext.getCmp("rt_comb");
                        	rt_comb.clearValue();
                         	rt_comb.reset();
                            rt_comb.queryParams = {"rcIDX":this.getValue()};
                            rt_comb.cascadeStore();  
                    	}
                    }
        		}]
			}, {													// 第1行第4列
				columnWidth: .25,
				layout: 'form',
				items: [{
	    			id:"rt_comb",
	    			xtype: "Base_combo",
	    			fieldLabel: "修次",
	    			hiddenName: "rtId", 
	    			displayField: "repairtimeName",
	    			valueField: "repairtimeIDX",
	    			pageSize: 0,
	    			minListWidth: 200,
	    			fields: ["repairtimeIDX","repairtimeName"],
    				business: 'rcRt',
	    			listeners : {
	    				"beforequery" : function(){
	    					//选择修次前先选修程
	                		var rcIdx =  Ext.getCmp("rc_comb").getValue();
	    					if(rcIdx == "" || rcIdx == null){
	    						MyExt.Msg.alert("请先选择修程！");
	    						return false;
	    					}
	                	}
	    			},
	    			width: MatTrainExpendAccountQuery.fieldWidth
	    		}]
			}]
		}, {
			// 查询表单第2行
			baseCls: "x-plain",
			layout: 'column',
			items:[{												// 第2行第1列
				columnWidth: .25,
				layout: 'form',
				items: [{
					id: 'expendOrg_k',
					name: 'expendOrg',
					xtype: 'textfield',
					fieldLabel: '消耗班组',
					width: MatTrainExpendAccountQuery.fieldWidth
				}]
			}, {
				columnWidth: .5,
				layout: 'form',
				items: [{
					xtype: 'compositefield', fieldLabel: '消耗日期', combineErrors: false,
					items: [{
						xtype:'my97date', name: 'startDate', id: 'startDate_d', format:'Y-m-d', value: lastMonth, width: 100, allowBlank: false,
						// 日期校验器
						validator: function() {
							var startDate =new Date(Ext.getCmp('startDate_d').getValue());
							var endDate = new Date(Ext.getCmp('endDate_d').getValue());
							if (startDate > endDate) {
								return "开始日期不能大于结束日期";
							}
						}
					}, {
						xtype: 'label',
						text: '至',
						style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
					}, {							
						xtype:'my97date', name: 'endDate', id: 'endDate_d', format:'Y-m-d', width: 100, allowBlank: false,
						// 日期校验器
						validator: function() {
							var startDate =new Date(Ext.getCmp('startDate_d').getValue());
							var endDate = new Date(Ext.getCmp('endDate_d').getValue());
							if (startDate > endDate) {
								return "结束日期不能小于开始日期";
							}
						}
					}]
				}]
			}]
		}, {
			// 查询表单第3行
			baseCls: "x-plain",
			layout: 'column',
			items:[{												// 第3行第1列
				columnWidth: .25,
				layout: 'form',
				items: [{
					name: 'matCode',
					fieldLabel: '物料编码',
					xtype: 'textfield',
					width: MatTrainExpendAccountQuery.fieldWidth
				}]
			}, {													// 第3行第2列
				columnWidth: .25,
				layout: 'form',
				items: [{
					name: 'matDesc',
					fieldLabel: '物料描述',
					xtype: 'textfield',
					anchor: '95%'
				}]
			}]
		}],
		buttons: [{
					xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
						var form = MatTrainExpendAccountQuery.searchForm.getForm();
						if (form.isValid()) {
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_d').clearInvalid();
							Ext.getCmp('endDate_d').clearInvalid();
							MatTrainExpendAccountQuery.grid.store.load();
						}
					}
				}, {
					xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
						MatTrainExpendAccountQuery.searchForm.getForm().reset();
						// 重置“入库库房”字段值
						Ext.getCmp('trainType_comb').clearValue();
						Ext.getCmp('trainNo_comb').clearValue();
						Ext.getCmp('rc_comb').clearValue();
						Ext.getCmp('rt_comb').clearValue();
						// 重新加载表格
						MatTrainExpendAccountQuery.grid.store.load();
					}
				}],
		buttonAlign: 'center'
	});
	/** ************** 定义查询表单结束 ************** */
	
	/** ************** 定义主体表格开始 ************** */
	MatTrainExpendAccountQuery.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matTrainExpendAccount!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matTrainExpendAccount!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matTrainExpendAccount!logicDelete.action',            //删除数据的请求URL
	    tbar:null,
	    border: true,
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'车型编码', dataIndex:'trainTypeIDX', hidden:true
		},{
			header:'车型', dataIndex:'trainTypeShortName', width: 30
		},{
			header:'车号', dataIndex:'trainNo', width: 30
		},{
			header:'修程编码', dataIndex:'xcId', hidden:true
		},{
			header:'修程', dataIndex:'xcName', width: 30
		},{
			header:'修次编码', dataIndex:'rtId', hidden:true
		},{
			header:'修次', dataIndex:'rtName', width: 30
		},{
			header: '消耗机构ID', dataIndex: 'expendOrgId', hidden:true
		},{
			header: '消耗班组', dataIndex: 'expendOrg', width: 60
		},{
			header: '消耗机构序列', dataIndex: 'expendOrgSeq', hidden:true
		},{
			header: '消耗时间', dataIndex: 'expendDate', xtype: 'datecolumn', format: 'Y-m-d', width: 40
		},{
			header: '物料编码', dataIndex: 'matCode', width: 40
		},{
			header: '物料描述', dataIndex: 'matDesc', width: 120
		},{
			header: '数量', dataIndex: 'qty', width: 20
		},{
			header: '计量单位', dataIndex: 'unit', width: 30
		},{
			header: '计划单价（元）', dataIndex: 'price', width: 30
		},{
			header: '登账人', dataIndex: 'registEmp', hidden:true
		},{
			header: '登账日期', dataIndex: 'registDate', xtype: 'datecolumn', format: 'Y-m-d', hidden:true
		},{
			header: '数据来源', dataIndex: 'dataSource', hidden:true
		}]
	});
	// 取消双击行出现“编辑”页面的事件监听
	MatTrainExpendAccountQuery.grid.un('rowdblclick', MatTrainExpendAccountQuery.grid.toEditFn, MatTrainExpendAccountQuery.grid);
	
	MatTrainExpendAccountQuery.grid.store.on('beforeload', function() {
		MatTrainExpendAccountQuery.searchParams = MatTrainExpendAccountQuery.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(MatTrainExpendAccountQuery.searchParams);
		MatTrainExpendAccountQuery.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	})
	/** ************** 定义主体表格结束 ************** */
	
	// 页面自适应布局
	MatTrainExpendAccountQuery.viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 173,
			border: true,
			collapsible: true,
//			collapseMode:'mini',
			split: true,
			items: [MatTrainExpendAccountQuery.searchForm]
		}, {
			// 结果显示列表区域
			region: 'center',
			layout: 'fit',
			height: 200,
			items: [MatTrainExpendAccountQuery.grid]
		}]
	});
});