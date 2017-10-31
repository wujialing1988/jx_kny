/**
 * 
 * 机车用料消耗记录js
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatTrainExpendAccount');
	
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
	MatTrainExpendAccount.searchParams = {};
	MatTrainExpendAccount.labelWidth = 80;
	MatTrainExpendAccount.fieldWidth = 100;
	MatTrainExpendAccount.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 【新增】按钮触发的函数处理
	MatTrainExpendAccount.addFn = function() {
		// 隐藏“修改”窗口
		if (MatTrainExpendAccountModify.win.isVisible()) {
			MatTrainExpendAccountModify.win.hide();
		}
		MatTrainExpendAccountAdd.win.show();
//		MatTrainExpendAccountAdd.form.getForm().reset();
		MatTrainExpendAccountDetail.store.removeAll();
		// 首次打开“新增”窗口时，设置“消耗班组”为当前登录用户所在班组
		if (Ext.isEmpty(Ext.getCmp('expendOrgId_a').getValue())) {
			Ext.getCmp('expendOrgId_a').setDisplayValue(orgId, orgName);
		};
	}
	// 【修改】按钮触发的函数处理
	MatTrainExpendAccount.modifyFn = function() {
		// 隐藏“新增”窗口
		if (MatTrainExpendAccountAdd.win.isVisible()) {
			MatTrainExpendAccountAdd.win.hide();
		}
		var sm = MatTrainExpendAccount.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			MyExt.Msg.alert('尚未选择任何记录！');
			return;
		}
		var records = sm.getSelections();
		var record = records[0];
		var data = record.data;
		if (data.status == STATUS_DZ) {
			MyExt.Msg.alert('已登账的记录不能进行修改！');
			return;
		}
		// 显示【修改消耗记录】窗口
		MatTrainExpendAccountModify.win.show();
		// 加载源数据
		MatTrainExpendAccountModify.form.getForm().loadRecord(record);
		// 回显“车型”
		Ext.getCmp("trainType_comb").setDisplayValue(data.trainTypeIDX, data.trainTypeShortName);
		// 回显“车号”
		Ext.getCmp("trainNo_comb").setDisplayValue(data.trainNo, data.trainNo);
		// 回显“修程”
		Ext.getCmp("rc_comb").setDisplayValue(data.xcId, data.xcName);
		// 回显“修次”
		Ext.getCmp("rt_comb").setDisplayValue(data.rtId, data.rtName);
		// 回显“消耗班组”
		Ext.getCmp("OmOrganizationCustom_comboTree_Id").setDisplayValue(data.expendOrgId, data.expendOrg);
		// 回显“物料编码”
		Ext.getCmp("matCode_k").setDisplayValue(data.matCode, data.matCode);
		// 重新加载“物料信息选择组件”数据
		MatTypeListSelect.orgId = data.expendOrgId;
		MatTypeListSelect.grid.store.load();
	}
	
	// 【登账】按钮触发的函数处理
	MatTrainExpendAccount.saveEntryAccountFn = function() {
		// 隐藏“修改”窗口
		if (MatTrainExpendAccountModify.win.isVisible()) {
			MatTrainExpendAccountModify.win.hide();
		}
		// 隐藏“新增”窗口
		if (MatTrainExpendAccountAdd.win.isVisible()) {
			MatTrainExpendAccountAdd.win.hide();
		}
		var sm = MatTrainExpendAccount.grid.getSelectionModel();
		if (0 >= sm.getCount()) {
			MyExt.Msg.alert('尚未选择任何记录！');
			return;
		}
		var record = sm.getSelections()[0];
		if (STATUS_DZ  == record.get('status')) {
			MyExt.Msg.alert('已登账的记录不能再次登账！');
			return;
		}
		Ext.Msg.confirm("提示", "是否确认登账？", function(btn){
			if(btn == 'yes'){
				var ids = new Array();
				ids.push(record.get('idx'));
				Ext.Ajax.request({
		            url: ctx + '/matTrainExpendAccount!saveEntryAccount.action',
		            params: {ids: ids},
		            success: function(response, options){
		              	MatTrainExpendAccountModify.loadMask.hide();
		                var result = Ext.util.JSON.decode(response.responseText);
		                if (result.errMsg == null) {
		                    alertSuccess();
		                    // 重新加载主界面表格数据
		                    MatTrainExpendAccount.grid.store.reload();
		                } else {
		                    alertFail(result.errMsg);
		                }
		            },
		            failure: function(response, options){
		                MatTrainExpendAccountModify.loadMask.hide();
		                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		            }
				});
			}
		})
	}
	// 【回滚】触发的函数操作
	MatTrainExpendAccount.rollBackFn = function() {
		var sm = MatTrainExpendAccount.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
				MyExt.Msg.alert("尚未选择任何记录！");
				return;
		}
		var data = sm.getSelections()[0].data;
		if (data.status == STATUS_ZC) {
			MyExt.Msg.alert("只能回滚已登账的记录！");
			return;
		} else {
			Ext.Msg.confirm("提示  ", "是否确认回滚？  ", function(btn){
		        if(btn == 'yes') {
	        		MatTrainExpendAccount.loadMask.show();
	        		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
			        	scope: MatTrainExpendAccount.grid,
			        	url: ctx + '/matTrainExpendAccount!updateRollBack.action',
						params: {ids: $yd.getSelectedIdx(MatTrainExpendAccount.grid, MatTrainExpendAccount.grid.storeId)},
						success: function(response, options){
			              	MatTrainExpendAccount.loadMask.hide();
			                var result = Ext.util.JSON.decode(response.responseText);
			                if (result.errMsg == null) {
			                    alertSuccess();
						        // 撤销成功后的一些页面初始化方法
								MatTrainExpendAccount.grid.store.reload();
			                } else {
			                    alertFail(result.errMsg);
			                }
			            }
			        }));
		        }
		    });    
		}
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义查询表单开始 ************** */
	MatTrainExpendAccount.searchForm = new Ext.form.FormPanel({
		labelWidth: MatTrainExpendAccount.labelWidth,
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
					id:"trainType_comb_s",xtype: "TrainType_combo",	fieldLabel: "车型",
					hiddenName: "trainTypeIDX",
					displayField: "shortName", valueField: "typeID", width:MatTrainExpendAccount.fieldWidth,
					pageSize: 20,
					editable:true,
					listeners : {   
			        	"select" : function() {   
			            	//重新加载车号下拉数据
			                var trainNo_comb_s = Ext.getCmp("trainNo_comb_s");   
			                trainNo_comb_s.reset();  
			                trainNo_comb_s.clearValue(); 
			                trainNo_comb_s.queryParams = {"trainTypeIDX":this.getValue()};
			                trainNo_comb_s.cascadeStore();
			                //重新加载修程下拉数据
			                var rc_comb_s = Ext.getCmp("rc_comb_s");
			                rc_comb_s.reset();
			                rc_comb_s.clearValue();
			                rc_comb_s.getStore().removeAll();
			                rc_comb_s.queryParams = {"TrainTypeIdx":this.getValue()};
			                rc_comb_s.cascadeStore();
							//重新加载修次数据
    	                	var rt_comb_s = Ext.getCmp("rt_comb_s");
    	                	rt_comb_s.clearValue();
    	                 	rt_comb_s.reset();
    	                 	rt_comb_s.getStore().removeAll();
    	                 	rt_comb_s.cascadeStore();
			        	}   
			    	}
				}]
			}, {													// 第1行第2列
				columnWidth: .25,
				layout: 'form',
				items: [{
					id:"trainNo_comb_s",xtype: "TrainNo_combo",	fieldLabel: "车号",
					hiddenName: "trainNo",width:MatTrainExpendAccount.fieldWidth,
					displayField: "trainNo", valueField: "trainNo",
					pageSize: 20, minListWidth: 200, 
					editable:true,
					listeners: {
						"beforequery" : function(){
	    					//选择修次前先选车型
							var trainTypeId =  Ext.getCmp("trainType_comb_s").getValue();
	    					if(trainTypeId == "" || trainTypeId == null){
	    						MyExt.Msg.alert("请先选择上车车型！");
	    						return false;
	    					}
	                	}
	    			}
				}]
			}, {													// 第1行第3列
				columnWidth: .25,
				layout: 'form',
				items: [{
					id:"rc_comb_s",
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
        			width: MatTrainExpendAccount.fieldWidth,
        			listeners : {  
        				"beforequery" : function(){
	    					//选择修次前先选车型
	                		var trainTypeId =  Ext.getCmp("trainType_comb_s").getValue();
	    					if(trainTypeId == "" || trainTypeId == null){
	    						MyExt.Msg.alert("请先选择上车车型！");
	    						return false;
	    					}
	                	},
                    	"select" : function() {   
                    		//重新加载修次数据
                        	var rt_comb_s = Ext.getCmp("rt_comb_s");
                        	rt_comb_s.clearValue();
                         	rt_comb_s.reset();
                            rt_comb_s.queryParams = {"rcIDX":this.getValue()};
                            rt_comb_s.cascadeStore();  
                    	}
                    }
        		}]
			}, {													// 第1行第4列
				columnWidth: .25,
				layout: 'form',
				items: [{
	    			id:"rt_comb_s",
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
	                		var rcIdx =  Ext.getCmp("rc_comb_s").getValue();
	    					if(rcIdx == "" || rcIdx == null){
	    						MyExt.Msg.alert("请先选择上车修程！");
	    						return false;
	    					}
	                	}
	    			},
	    			width: MatTrainExpendAccount.fieldWidth
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
					name: 'expendOrg',
					xtype: 'textfield',
					fieldLabel: '消耗班组',
					width: MatTrainExpendAccount.fieldWidth
				}]
			}, {													// 第2行第2列
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
					width: MatTrainExpendAccount.fieldWidth
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
					width: MatTrainExpendAccount.fieldWidth
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
						var form = MatTrainExpendAccount.searchForm.getForm();
						if (form.isValid()) {
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_d').clearInvalid();
							Ext.getCmp('endDate_d').clearInvalid();
							MatTrainExpendAccount.grid.store.load();
						}
					}
				}, {
					xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
						MatTrainExpendAccount.searchForm.getForm().reset();
						// 重置“入库库房”字段值
						Ext.getCmp('trainType_comb_s').clearValue();
						Ext.getCmp('trainNo_comb_s').clearValue();
						Ext.getCmp('rc_comb_s').clearValue();
						Ext.getCmp('rt_comb_s').clearValue();
						// 重新加载表格
						MatTrainExpendAccount.grid.store.load();
					}
				}],
		buttonAlign: 'center'
	});
	/** ************** 定义查询表单结束 ************** */
	
	/** ************** 定义主体表格开始 ************** */
	MatTrainExpendAccount.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matTrainExpendAccount!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matTrainExpendAccount!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matTrainExpendAccount!logicDelete.action',            //删除数据的请求URL
	    tbar: [{
	    	text: '新增', iconCls: 'addIcon', handler: MatTrainExpendAccount.addFn
	    }, {
	    	text: '修改', iconCls: 'editIcon', handler: MatTrainExpendAccount.modifyFn
	    }, {
	    	text: '登账', iconCls: 'uploadIcon', handler: MatTrainExpendAccount.saveEntryAccountFn
	    }, 'delete', {
	    	text: '回滚', iconCls: 'resetIcon', handler: MatTrainExpendAccount.rollBackFn
	    }, 'refresh'],
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
			header: '消耗日期', dataIndex: 'expendDate', xtype: 'datecolumn', format: 'Y-m-d', width: 40
		},{
			header: '物料编码', dataIndex: 'matCode', width: 40
		},{
			header: '物料描述', dataIndex: 'matDesc', width: 120
		},{
			header: '数量', dataIndex: 'qty', width: 20
		},{
			header: '计量单位', dataIndex: 'unit', width: 30
		},{
			header: '计划单价', dataIndex: 'price', width: 30
		},{
			header: '登账人', dataIndex: 'registEmp', hidden:true
		},{
			header: '登账日期', dataIndex: 'registDate', xtype: 'datecolumn', format: 'Y-m-d', hidden:true
		},{
			header: '数据来源', dataIndex: 'dataSource', hidden:true
		},{
			header: '状态', dataIndex: 'status', hidden:false, width: 30, renderer: function(v) {
				if (v == STATUS_ZC) return "暂存";
				if (v == STATUS_DZ) return "已登账";
				return "错误！未知状态";
			}
		}],
		// 【删除】按钮触发后，执行删除之前的验证操作
		beforeDeleteFn: function() {
			// 隐藏“修改”窗口
			if (MatTrainExpendAccountModify.win.isVisible()) {
				MatTrainExpendAccountModify.win.hide();
			}
			// 隐藏“新增”窗口
			if (MatTrainExpendAccountAdd.win.isVisible()) {
				MatTrainExpendAccountAdd.win.hide();
			}
			var sm = this.getSelectionModel();
			var record = sm.getSelections()[0];
			// 已登账的记录不能删除
			if (STATUS_DZ == record.get('status')) {
				MyExt.Msg.alert('已登账的记录不能删除！');
				return false;
			}
			return true;
		},
		// 双击“暂存”的记录进行修改
		toEditFn: MatTrainExpendAccount.modifyFn
	});
	
	MatTrainExpendAccount.grid.store.on('beforeload', function() {
		MatTrainExpendAccount.searchParams = MatTrainExpendAccount.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(MatTrainExpendAccount.searchParams);
		searchParams.dataSource = DATA_SOURCE;
		MatTrainExpendAccount.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	})
	/** ************** 定义主体表格结束 ************** */
	
	// 页面自适应布局
	MatTrainExpendAccount.viewport = new Ext.Viewport({
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
			items: [MatTrainExpendAccount.searchForm]
		}, {
			// 结果显示列表区域
			region: 'center',
			layout: 'fit',
			height: 200,
			items: [MatTrainExpendAccount.grid]
		}]
	});
});