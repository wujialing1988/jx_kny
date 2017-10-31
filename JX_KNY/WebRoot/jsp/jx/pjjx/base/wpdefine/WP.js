/**
 * 检修作业流程 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('WP');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	WP.labelWidth = 60;
	WP.fieldWidth = 140;
	WP.searchParams = {};
	WP.saveWinWidth = 1200;
	WP.saveWinHeight = 700;
	WP.idx = "###";						// 全局的【检修作业流程】idx主键
	WP.wPDesc = "###";					// 全局的【检修作业流程】描述
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 自动设置【作业流程编号】字段值
	WP.setWPNo = function() {
		Ext.Ajax.request({
			url: ctx + "/codeRuleConfig!getConfigRule.action",
			params: {ruleFunction: "PJJX_WP_NO"},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
					WP.grid.saveForm.find("name", "wPNo")[0].setValue(result.rule);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	
	// 【设置工艺顺序】按钮触发的函数处理
	WP.setTecSeqFn = function() {
		if(!$yd.isSelectedRecord(WP.grid)) {
			return;
		}
		// 如果编辑[新增]窗口被打开，则隐藏
		if(WP.grid.saveWin)    WP.grid.saveWin.hide();
		MyExt.Msg.alert("功能完善中");
	}
	
	// 页面tabPanel显示与隐藏方法
	WP.hideTabPanelMethod = function(args) {
		
		// 点击“树干节点”时TabPanel的隐藏与显示
		if (0 == args) {
			Ext.getCmp('saveWinTab_2').unhideTabStripItem(0);
			Ext.getCmp('saveWinTab_2').unhideTabStripItem(5);
			Ext.getCmp('saveWinTab_2').hideTabStripItem(1);
			Ext.getCmp('saveWinTab_2').hideTabStripItem(2);
			Ext.getCmp('saveWinTab_2').hideTabStripItem(3);
			Ext.getCmp('saveWinTab_2').hideTabStripItem(4);
			Ext.getCmp('saveWinTab_2').setActiveTab(0);
	//		Ext.getCmp('saveWinTab_2').setActiveTab(5);
			
		// 点击“叶子节点”时TabPanel的隐藏与显示
		} else if (1 == args) {
			Ext.getCmp('saveWinTab_2').hideTabStripItem(0);
			Ext.getCmp('saveWinTab_2').hideTabStripItem(5);
			Ext.getCmp('saveWinTab_2').unhideTabStripItem(1);
			Ext.getCmp('saveWinTab_2').unhideTabStripItem(2);
			Ext.getCmp('saveWinTab_2').unhideTabStripItem(3);
			Ext.getCmp('saveWinTab_2').unhideTabStripItem(4);
			
		// 其它时TabPanel的隐藏与显示
		} else {
			Ext.getCmp('saveWinTab_2').unhideTabStripItem(0);
			Ext.getCmp('saveWinTab_2').unhideTabStripItem(5);
			Ext.getCmp('saveWinTab_2').hideTabStripItem(1);
			Ext.getCmp('saveWinTab_2').hideTabStripItem(2);
			Ext.getCmp('saveWinTab_2').hideTabStripItem(3);
			Ext.getCmp('saveWinTab_2').hideTabStripItem(4);
			Ext.getCmp('saveWinTab_2').setActiveTab(0);
	//		Ext.getCmp('saveWinTab_2').setActiveTab(5);
		}
	}
	// 对工时字段（分钟）按#天#小时#分钟进行单位显示
	WP.formatTime = function(ratedPeriod) {
		if (Ext.isEmpty(ratedPeriod) || ratedPeriod == 0) {
			return "";
		}
		var ratedPeriod_d = Math.floor(ratedPeriod/(24*60));			// 天
		var ratedPeriod_h = Math.floor((ratedPeriod%(24*60))/60);		// 时
		var ratedPeriod_m = ratedPeriod%60;								// 分
		var displayValue = "";
		if (ratedPeriod_d != 0) {
			displayValue += ratedPeriod_d + "天";
			if (ratedPeriod_h != 0) {
				displayValue += ratedPeriod_h + "小时";
				if (ratedPeriod_m != 0) {
					displayValue += ratedPeriod_m + "分";
				}
			} else {
				if (ratedPeriod_m != 0) {
					displayValue += ratedPeriod_m + "分";
				}
			}
		} else {
			if (ratedPeriod_h != 0) {
				displayValue += ratedPeriod_h + "小时";
				if (ratedPeriod_m != 0) {
					displayValue += ratedPeriod_m + "分";
				}
			} else {
				if (ratedPeriod_m != 0) {
					displayValue += ratedPeriod_m + "分";
				}
			}
		}
		return displayValue + "&nbsp;-&nbsp;(" + ratedPeriod + "分钟)";
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义查询表单开始 ************** */
	WP.searchForm = new Ext.form.FormPanel({
		labelWidth: WP.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 10px',
		layout: 'column',
		defaults: {
			columnWidth: .3, layout: 'form', 
			defaults: {
				xtype: 'textfield', width: WP.fieldWidth
			}
		},
		items:[{												// 第1行第1列
			items: [{
				fieldLabel: '编号', name: 'wPNo'
			}]
		}, {													// 第1行第3列
			items: [{
				fieldLabel: '描述', name: 'wPDesc'
			}]
		}, {													// 第1行第3列
			columnWidth: .4,
			labelWidth: 120,
			items: [{
				xtype: 'compositefield', fieldLabel: '额定工期（分钟）', combineErrors: false, anchor: '100%',
				items: [{
					xtype: 'numberfield', name: 'ratedPeriodFrom', width: 70
				}, {
					xtype: 'label',
					text: '至',
					style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}, {
					xtype: 'numberfield', name: 'ratedPeriodTo', width: 70
				}]
			}]
		}],
		buttons: [{
			xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
				// 重新加载表格
				WP.grid.store.load();
			}
		}, {
			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
				WP.searchForm.getForm().reset();
				// 重新加载表格
				WP.grid.store.load();
			}
		}],
		buttonAlign: 'center'
	});
	/** ************** 定义查询表单结束 ************** */
	
	/** ************** 定义保存表单开始 ************** */
	WP.saveForm = new Ext.form.FormPanel({
		frame:true,
		labelWidth:WP.labelWidth,
		layout:"form",
		padding:"0 15px",
		items:[{
			xtype:"textfield",
			fieldLabel:"编号",
			allowBlank:false,
			maxLength:30, width: WP.fieldWidth,
			name:"wPNo"
		}, {
			xtype:"textarea",
			fieldLabel:"描述",
			allowBlank:false,
			maxLength:500, anchor: '90%',
			name:"wPDesc"
		}, {
			xtype:"container",
			layout:"column",
			items:[{
				xtype:"container",
				layout:"form",
				columnWidth:0.5,
				items:[{
					xtype: 'compositefield', fieldLabel: '额定工期', combineErrors: false, allowBlank:false,
					items: [{
						xtype: 'numberfield', id: 'ratedPeriod_h', name: 'ratedPeriod_h', width: 60, validator: function(value) {
							if (parseInt(value) < 0) {
								return "请输入正整数";
							}
							var mValue = Ext.getCmp('ratedPeriod_m').getValue();
							if (Ext.isEmpty(value) && Ext.isEmpty(mValue)) {
								return '额定工期不能为空'
							} else {
								if (value.length > 2) {
									return "该输入项最大长度为2";
								} else if (Ext.isEmpty(mValue) || parseInt(mValue) < 60){
									Ext.getCmp('ratedPeriod_h').clearInvalid();
									Ext.getCmp('ratedPeriod_m').clearInvalid();
								}
							}
						}
					}, {
						xtype: 'label',
						text: '时',
						style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
					}, {
						xtype: 'numberfield', id: 'ratedPeriod_m', name: 'ratedPeriod_m', width: 60, validator: function(value) {
							if (parseInt(value) < 0) {
								return "请输入正整数";
							}
							var hValue = Ext.getCmp('ratedPeriod_h').getValue();
							if (Ext.isEmpty(value) && Ext.isEmpty(hValue)) {
								return '额定工期不能为空'
							} else {
								if (parseInt(value) >= 60) {
									return "不能超过60分钟";
								} else if (hValue.length <= 2){
									Ext.getCmp('ratedPeriod_h').clearInvalid();
									Ext.getCmp('ratedPeriod_m').clearInvalid();
								}
							}
						}
					}, {
						xtype: 'label',
						text: '分',
						style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
					}]
				}]
			}, {
				xtype:"container",
				layout:"form",
				columnWidth:0.5,
				items:[
					{
						xtype:"textfield",
						fieldLabel:"额定工时", 
						vtype:'positiveInt',
						maxLength: 4, width: WP.fieldWidth,
						name:"ratedWorkTime"
					}
				]
			}]
		}, {
			xtype:"textarea",
			name:"remarks",
			fieldLabel:"备注", anchor: '90%',
			maxLength:500
		}, {
			fieldLabel: "idx主键", name: "idx", xtype: "hidden"
		}],
		buttonAlign: "center",
		buttons: [{
            text: "保存", iconCls: "saveIcon", handler: function() {WP.grid.saveFn()}
        }]
	})
	/** ************** 定义保存表单结束 ************** */
	
	/** ************** 定义保存窗口开始 ************** */
	WP.saveWin = new Ext.Window({
		width:WP.saveWinWidth,
		height:WP.saveWinHeight,
		maximized:true,
//		modal: true,
		layout:"fit",
		closeAction:'hide',
		items:[{
			xtype:"tabpanel",
			id:"saveWinTab",
			activeTab:0,
			height:564,
			border:false,
			items:[{
				title:"检修需求",
				layout:"border",
				items:[{
					collapsible: true,
					title:"基本信息",
					region:"north",
					height:265,
					layout:"fit",
					items:WP.saveForm
				}, {
					title:"检修配件",
					region:"center",
					layout:"fit",
					items:PartsTypeForWP.grid
				}],
				listeners: {
					activate: function() {
						PartsTypeForWP.grid.store.reload();
					}
				}
			}, {
				title:"检修需求详情",
				layout:"ux.row",
				defaults:{
					layout:"fit", rowHeight:'.5'
				},
				items:[{
					items:TecForWP.grid
				}, {
					items:RecordForWP.grid
				}/*, {
					items:EquipForWP.grid
				}*/],
				listeners: {
					activate: function() {
						TecForWP.grid.store.reload();
						RecordForWP.grid.store.reload();
					// EquipForWP.grid.store.reload();
					}
				}
			}, {
				title:"作业流程节点",
				layout:"border",
				items:[{
					title : '<span style="font-weight:normal">作业节点树</span>',
			        iconCls : 'icon-expand-all',
			        tools : [ {
			            id : 'refresh',
			            handler: function() {
			            	WPNode_tree.reload(WPNode_tree.treePath);
			            }
			        } ],
					region:"west",
					layout:"fit",
					items:WPNode_tree.tree,
					width:268
				}, {
					xtype:"tabpanel",
					id:"saveWinTab_2",
					region:"center",
					defaults: {layout:"fit"},
					items:[{
						title:"下级作业节点", id:"saveWinTab_2_0", items:WPNode.grid
					}, {
						title:"检修工艺卡", id:"saveWinTab_2_1", items:TecCardForWPNode.grid
					}, {
						title:"检修记录卡", id:"saveWinTab_2_2", items:RecordCardForWPNode.grid
					}, {
						title:"检修作业工位", id:"saveWinTab_2_3", items:WpNodeStationDef.grid
					}/*, {
						title:"配件检修用料", id:"saveWinTab_2_4", items:WPMat.grid
					}, {
						title:"配件检修用料", id:"saveWinTab_2_5", items:WPMatSearch.grid
					}*/],
					listeners : {
						render : function(){
							WP.hideTabPanelMethod();
						}
					}
				}],
				listeners : {
					activate : function(){
						WPNode.grid.store.reload();
					}
				}
			}]
		}],
		buttonAlign: "center",
		buttons: [{
            text: "关闭", iconCls: "closeIcon", handler: function(){ this.findParentByType('window').hide(); }
        }]
	})
	/** ************** 定义保存窗口结束 ************** */
	
	/** ************** 定义【检修作业流程】表格开始 ************** */
	WP.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/wP!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/wP!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/wP!logicDelete.action',            //删除数据的请求URL
	    saveForm: WP.saveForm,
	    saveWin: WP.saveWin,
	    tbar:['add', 'delete', {
	    	text:"设置工艺顺序", iconCls:"configIcon", handler: WP.setTecSeqFn
	    }, 'refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, width: 15, editor: { xtype:'hidden' }
		},{
			header:'编号', dataIndex:'wPNo', width: 10,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			html = "<span><a href='#' onclick='WP.grid.toEditFn(\""+ WP.grid + "\",\""+ rowIndex +"\")'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'描述', dataIndex:'wPDesc', width: 30
		},{
			header:'额定工期', dataIndex:'ratedPeriod', width: 10, renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				return WP.formatTime(value);
			}
		},{
			header:'额定工时', dataIndex:'ratedWorkTime', width: 10
		},{
			header:'备注', dataIndex:'remarks', width: 20
		}],
		afterShowSaveWin: function(){
			// 自动设置【作业流程编号】字段值
			WP.setWPNo();
			
			// 加载【作业流程适用配件】表格数据
			PartsTypeForWP.wPIDX = "###";
			PartsTypeForWP.grid.store.load();
			PartsTypeForWP.grid.getTopToolbar().disable();
			
//			// 加载【作业流程检修工艺】表格数据
//			TecForWP.wPIDX = "###";
//			TecForWP.grid.store.load();
//			TecForWP.grid.getTopToolbar().disable();
//			
//			// 加载【作业流程记录单】表格数据
//			RecordForWP.wPIDX = "###";
//			RecordForWP.grid.store.load();
//			RecordForWP.grid.getTopToolbar().disable();
			
			// 因为{检修需求详情}和{作业流程节点}要保存了基本信息后才可编辑，因此新增窗口打开后，隐藏掉{检修需求详情}和{作业流程节点}这两个tab选项卡
			Ext.getCmp('saveWinTab').hideTabStripItem(1);		// 隐藏{检修需求详情}tab选项卡
			Ext.getCmp('saveWinTab').hideTabStripItem(2);		// 隐藏{作业流程节点}tab选项卡
			Ext.getCmp('saveWinTab').setActiveTab(0);			// 隐藏{检修需求}tab选项卡
			
		},
		
		// 重新编辑函数，打开编辑窗口后，通过计算“额定工期（分钟）”字段值对编辑表单的'额定工期'组合字段进行赋值
		afterShowEditWin: function(record, rowIndex){
			WP.idx = record.get('idx');
			WP.wPDesc = record.get('wPDesc');
			
			var ratedPeriod = record.get('ratedPeriod');
			var ratedPeriod_h = Math.floor(ratedPeriod/60);
			var ratedPeriod_m = ratedPeriod%60;
			// 设置 额定工期_时
			Ext.getCmp("ratedPeriod_h").setValue(ratedPeriod_h);
			// 设置 额定工期_分
			Ext.getCmp("ratedPeriod_m").setValue(ratedPeriod_m);
			
			// 加载【作业流程适用配件】表格数据
			PartsTypeForWP.wPIDX = WP.idx;
			PartsTypeForWP.grid.store.load();
			PartsTypeForWP.grid.getTopToolbar().enable();
			
			// 加载【作业流程检修工艺】表格数据
			TecForWP.wPIDX = WP.idx;
			TecForWP.grid.store.load();
//			TecForWP.grid.getTopToolbar().enable();
			
			// 加载【作业流程记录单】表格数据
			RecordForWP.wPIDX = WP.idx;
			RecordForWP.grid.store.load();
//			RecordForWP.grid.getTopToolbar().enable();
			
			// 加载【作业流程机务设备工单】表格数据
			EquipForWP.wPIDX = WP.idx;
			EquipForWP.grid.store.load();
			
			// 加载【作业节点】树列表
			WPNode_tree.wPIDX = WP.idx;
			WPNode_tree.tree.root.setText(WP.wPDesc);
			if (WPNode_tree.isRender) {
				WPNode_tree.reload();
			}
			
			// 加载【作业节点】表格
			WPNode.wPIDX = WP.idx;
			WPNode.parentWPNodeIDX = "ROOT_0";
			WPNode.grid.store.load();
			// 加载【检修用料】表格
			WPMat.wPIDX = WP.idx;
			WPMat.grid.store.load();
			WPMatSearch.grid.store.load();
			
			// 显示因执行了新增（但未成功保存）而隐藏的{检修需求详情}和{作业流程节点}这两个tab选项卡，用于后续编辑
			Ext.getCmp('saveWinTab').unhideTabStripItem(1);	 	// 显示{检修需求详情}tab选项卡
			Ext.getCmp('saveWinTab').unhideTabStripItem(2);	 	// 显示{作业流程节点}tab选项卡
			
			// 编辑记录时初始化{作业流程节点}下属选项卡[{下级作业节点},{检修工艺卡},{检修记录卡},{作业工位}]的显示
			Ext.getCmp('saveWinTab_2').hideTabStripItem(1);			// 隐藏{检修记录卡}tab选项卡
			Ext.getCmp('saveWinTab_2').hideTabStripItem(2); 			// 隐藏{检修工艺卡}tab选项卡
			Ext.getCmp('saveWinTab_2').hideTabStripItem(3); 			// 隐藏{作业工位}tab选项卡
			Ext.getCmp('saveWinTab_2').hideTabStripItem(4); 			// 隐藏{检修用料}tab选项卡
			Ext.getCmp('saveWinTab_2').unhideTabStripItem(0);  		// 显示{下级作业节点}tab选项卡
			Ext.getCmp('saveWinTab_2').unhideTabStripItem(5);  		// 显示{检修用料}tab选项卡
			Ext.getCmp('saveWinTab_2').setActiveTab(0);				// 激活{下级作业节点}tab选项卡
	//		Ext.getCmp('saveWinTab_2').setActiveTab(5);				// 激活{检修用料}tab选项卡
			
			// 重命名Tab
    		Ext.getCmp('saveWinTab_2_0').setTitle(WP.wPDesc + " - 作业节点")
		},
		
		// 重新保存方法，完善对“额定工期（分钟）”字段保存时的特殊处理
		beforeSaveFn: function(data){ 
			var ratedPeriod_h = data.ratedPeriod_h;
			var ratedPeriod_m = data.ratedPeriod_m;
			data.ratedPeriod = parseInt(ratedPeriod_h * 60);
			if (!Ext.isEmpty(ratedPeriod_m)) {
				 data.ratedPeriod += parseInt(ratedPeriod_m);
			}
			delete data.ratedPeriod_h;
			delete data.ratedPeriod_m;
			return true; 
		},
		
		// 重写保存成功后的处理函数
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        // 对idx字段值进行回显
	        this.saveForm.find('name', 'idx')[0].setValue(result.entity.idx);
	        WP.idx = result.entity.idx;
			WP.wPDesc =  result.entity.wPDesc;
	        
	        // 设置【作业流程适用配件】表格基础信息
	        PartsTypeForWP.wPIDX = WP.idx;
			PartsTypeForWP.grid.getTopToolbar().enable();
			
	        // 设置【作业流程检修工艺】表格基础信息
	        TecForWP.wPIDX = WP.idx;
//			TecForWP.grid.getTopToolbar().enable();
			
	        // 设置【作业流程记录单】表格基础信息
	        RecordForWP.wPIDX = WP.idx;
//			RecordForWP.grid.getTopToolbar().enable();
			
	        // 设置【作业流程机务设备工单】表格基础信息
			EquipForWP.wPIDX = WP.idx;
			
			// 设置【作业节点】树列表基础信息
			WPNode_tree.wPIDX = WP.idx;
			WPNode_tree.tree.root.setText(WP.wPDesc);
			if (WPNode_tree.isRender) {
				WPNode_tree.reload();
			}
			// 设置【作业节点】表格基础信息
			WPNode.wPIDX = WP.idx;
			WPNode.parentWPNodeIDX = "ROOT_0";
			
			Ext.getCmp('saveWinTab').unhideTabStripItem(1);
			Ext.getCmp('saveWinTab').unhideTabStripItem(2);
			
			// 重命名Tab
    		Ext.getCmp('saveWinTab_2_0').setTitle(WP.wPDesc + " - 作业节点")
	    }
	});
	// 设置默认以“编号”字段进行升序排序
	WP.grid.store.setDefaultSort("wPNo", "ASC");
	WP.grid.store.on('beforeload', function() {
		WP.searchParams = WP.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(WP.searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************** 定义【检修作业流程】表格结束 ************** */
	
	// 页面自适应布局
	new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 110,
			border: true,
			collapsible: true,
			items: [WP.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			items: [WP.grid]
		}]
	});
});