/**
 * 
 * 机车检修用料查询js
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatOutWHTrainQuery');
	
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
	MatOutWHTrainQuery.searchParams = {};
	MatOutWHTrainQuery.labelWidth = 80;
	MatOutWHTrainQuery.fieldWidth = 120;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 显示入库单明细
	MatOutWHTrainQuery.showDetail = function(rowIndex) {
		// 重新加载入库单基本信息
		var record = MatOutWHTrainQuery.grid.store.getAt(rowIndex);
		MatOutWHTrainQueryDetail.baseForm.getForm().loadRecord(record);
		// 根据入库单主键，重新加载入库单明细
		MatOutWHTrainQueryDetail.matOutWHTrainIDX = record.get('idx');
		MatOutWHTrainQueryDetail.grid.store.load();
		if (record.get('status') == STATUS_ZC) {
			MatOutWHTrainQueryDetail.batchWin.setTitle("单据信息<font color='green'>(暂存)</font>");
		} else if (record.get('status') == STATUS_DZ) {
			MatOutWHTrainQueryDetail.batchWin.setTitle("单据信息<font color='red'>(已登账)</font>");
		}
		// 显示入库单明细窗口
		MatOutWHTrainQueryDetail.batchWin.show();
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义查询表单开始 ************** */
	MatOutWHTrainQuery.searchForm = new Ext.form.FormPanel({
		labelWidth: MatOutWHTrainQuery.labelWidth,
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
					fieldLabel: '领用库房',
					hiddenName: 'whIdx',
					id: 'whIdx_s',
					width: MatOutWHTrainQuery.fieldWidth,
					xtype:"Base_combo", 
					entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
					queryHql:"from Warehouse where recordStatus=0 and status = 1",	
					fields:["wareHouseID","wareHouseName","idx"],
					displayField:"wareHouseName",valueField:"idx"
				}]
			}, {													// 第1行第2列
				columnWidth: .25,
				layout: 'form',
				items: [{
					xtype: 'combo',	        	        
					fieldLabel : '状态',
					name : 'status',
			        hiddenName:'status',
			        store:new Ext.data.SimpleStore({
					    fields: ['K', 'V'],
						data : [[STATUS_SY, "所有"], [STATUS_ZC, "暂存"], [STATUS_DZ, "已登账"]]
					}),
					valueField:'K',
					displayField:'V',
					triggerAction:'all',
					value: STATUS_SY,
					mode:'local',
					width: MatOutWHTrainQuery.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .5,
				layout: 'form',
				items: [{
					xtype: 'compositefield', fieldLabel: '领用日期', combineErrors: false,
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
			// 查询表单第2行
			baseCls: "x-plain",
			layout: 'column',
			items:[{												// 第2行第1列
				columnWidth: .25,
				layout: 'form',
				items: [{
					id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "车型",
					hiddenName: "trainTypeIDX",
					displayField: "shortName", valueField: "typeID", width:MatOutWHTrainQuery.fieldWidth,
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
			}, {													// 第2行第2列
				columnWidth: .25,
				layout: 'form',
				items: [{
					id:"trainNo_comb",xtype: "TrainNo_combo",	fieldLabel: "车号",
					hiddenName: "trainNo",width:MatOutWHTrainQuery.fieldWidth,
					displayField: "trainNo", valueField: "trainNo",
					pageSize: 20, minListWidth: 200, 
					editable:true,
					listeners: {
						"beforequery" : function(){
	    					//选择修次前先选车型
							var trainTypeId =  Ext.getCmp("trainType_comb").getValue();
	    					if(trainTypeId == "" || trainTypeId == null){
	    						MyExt.Msg.alert("请先选择上车车型！");
	    						return false;
	    					}
	                	}
	    			}
				}]
			}, {													// 第2行第3列
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
        			width: MatOutWHTrainQuery.fieldWidth,
        			listeners : {  
        				"beforequery" : function(){
	    					//选择修次前先选车型
	                		var trainTypeId =  Ext.getCmp("trainType_comb").getValue();
	    					if(trainTypeId == "" || trainTypeId == null){
	    						MyExt.Msg.alert("请先选择上车车型！");
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
			}, {													// 第2行第4列
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
	    						MyExt.Msg.alert("请先选择上车修程！");
	    						return false;
	    					}
	                	}
	    			},
	    			width: MatOutWHTrainQuery.fieldWidth
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
					name: 'getEmp',
					fieldLabel: '领用人',
					xtype: 'textfield',
					width: MatOutWHTrainQuery.fieldWidth
				}]
			}, {													// 第3行第2列
				columnWidth: .25,
				layout: 'form',
				items: [{
					name: 'getOrg',
					fieldLabel: '领用班组',
					xtype: 'textfield',
					width: MatOutWHTrainQuery.fieldWidth
				}]
			}]
		}],
		buttons: [{
					xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
						var form = MatOutWHTrainQuery.searchForm.getForm();
						if (form.isValid()) {
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_d').clearInvalid();
							Ext.getCmp('endDate_d').clearInvalid();
							MatOutWHTrainQuery.grid.store.load();
						}
					}
				}, {
					xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
						MatOutWHTrainQuery.searchForm.getForm().reset();
						// 重置“入库库房”字段值
						Ext.getCmp('whIdx_s').clearValue();
						Ext.getCmp('trainType_comb').clearValue();
						Ext.getCmp('trainNo_comb').clearValue();
						Ext.getCmp('rc_comb').clearValue();
						Ext.getCmp('rt_comb').clearValue();
						// 重新加载表格
						MatOutWHTrainQuery.grid.store.load();
					}
				}],
		buttonAlign: 'center'
	});
	/** ************** 定义查询表单结束 ************** */
	
	/** ************** 定义主体表格开始 ************** */
	MatOutWHTrainQuery.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matOutWHTrain!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matOutWHTrain!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matOutWHTrain!logicDelete.action',            //删除数据的请求URL
	    tbar:null,
	    border: true,
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'单据编号', dataIndex:'billNo', width: 80,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	            var html = "";
	    		html = "<span><a href='#' onclick='MatOutWHTrainQuery.showDetail(" + rowIndex + ")'>"+value+"</a></span>";
	            return html;
			}
		},{
			header:'单据摘要', dataIndex:'billSummary', width: 170
		},{
			header:'领用库房主键', dataIndex:'whIdx', hidden:true
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
			header:'领用库房', dataIndex:'whName', width: 40
		},{
			header:'出库人主键', dataIndex:'exWhEmpId', hidden:true
		},{
			header:'出库人', dataIndex:'exWhEmp', hidden:true
		},{
			header:'领用人主键', dataIndex:'getEmpId', hidden:true
		},{
			header:'领用人', dataIndex:'getEmp', width: 30
		},{
			header:'领用机构id', dataIndex:'getOrgId', hidden:true
		},{
			header:'领用班组', dataIndex:'getOrg', width: 80
		},{
			header:'领用机构序列', dataIndex:'getOrgSeq', hidden:true
		},{
			header:'领用日期', dataIndex:'getDate', xtype:'datecolumn', width: 40
		},{
			header:'制单人', dataIndex:'makeBillEmp', hidden:true, editor:{  maxLength:25 }
		},{
			header:'制单日期', dataIndex:'makeBillDate', hidden:true, xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'登账人', dataIndex:'registEmp', hidden:true, editor:{  maxLength:25 }
		},{
			header:'登账日期', dataIndex:'registDate', hidden:true,  xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'状态', dataIndex:'status', width: 30, renderer: function(v) {
				if (v == STATUS_ZC) return "暂存";
				if (v == STATUS_DZ) return "已登账";
				return "错误！未知状态";
			}
		}]
	});
	// 取消双击行出现“编辑”页面的事件监听
	MatOutWHTrainQuery.grid.un('rowdblclick', MatOutWHTrainQuery.grid.toEditFn, MatOutWHTrainQuery.grid);
	
	MatOutWHTrainQuery.grid.store.on('beforeload', function() {
		MatOutWHTrainQuery.searchParams = MatOutWHTrainQuery.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(MatOutWHTrainQuery.searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
		this.baseParams.isQueryPage = true;
	})
	/** ************** 定义主体表格结束 ************** */
	
	// 页面自适应布局
	MatOutWHTrainQuery.viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 180,
			border: true,
			collapsible: true,
//			collapseMode:'mini',
			split: true,
			items: [MatOutWHTrainQuery.searchForm]
		}, {
			// 结果显示列表区域
			region: 'center',
			layout: 'fit',
			height: 200,
			items: [MatOutWHTrainQuery.grid]
		}]
	});
});