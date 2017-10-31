/**
 * 
 * 质量反馈单查询js
 * 
 */

Ext.onReady(function() {
	Ext.namespace('MatBackSupplyStation');
	
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
	MatBackSupplyStation.searchParams = {};
	MatBackSupplyStation.labelWidth = 100;
	MatBackSupplyStation.fieldWidth = 140;
	
	/** ************** 定义查询表单开始 ************** */
	MatBackSupplyStation.searchForm = new Ext.form.FormPanel({
		labelWidth: MatBackSupplyStation.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 15px',
		items:[{
			// 查询表单第1行
			baseCls: "x-plain",
			layout: 'column',
			items:[{												
				columnWidth: .25,
				layout: 'form',
				items: [{
					fieldLabel: '库房',
					hiddenName: 'whIdx',
					id: 'whIdx_s',
					width: MatBackSupplyStation.fieldWidth,
					xtype:"Base_combo", 
					entity:"com.yunda.jx.pjwz.stockbase.entity.Warehouse",						  
					queryHql:"from Warehouse where recordStatus=0 and status = 1",	
					fields:["wareHouseID","wareHouseName","idx"],
					displayField:"wareHouseName",valueField:"idx"
				}]
			},{												
				columnWidth: .25,
				layout: 'form',
				items: [{
					fieldLabel: '物料编码',
					name: 'matCode',
//					id: 'matCode_s',
					width: MatBackSupplyStation.fieldWidth,
					xtype:"textfield"
				}]
			},{													// 第1行第3列
				columnWidth: .5,
				layout: 'form',
				items: [{
					xtype: 'compositefield', fieldLabel: '反馈日期', combineErrors: false,
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
			},{												
				columnWidth: .5,
				layout: 'form',
				items: [{
					xtype:"textfield", 
					name:"matDesc", 
					fieldLabel:"物料描述",
					anchor: '98%'
				}]
			}, {													// 第1行第2列
				columnWidth: .5,
				layout: 'form',
				items: [{
					xtype: 'combo',	        	        
					fieldLabel : '状态',
					name : 'status',
			        hiddenName:'status',
			        store:new Ext.data.SimpleStore({
					    fields: ['K', 'V'],
						data : [[STATUS_ZC, "待登帐"], [STATUS_DZ, "已登帐"]]
					}),
					valueField:'K',
					displayField:'V',
					triggerAction:'all',
					value: STATUS_ZC,
					mode:'local',
					width: MatBackSupplyStation.fieldWidth
				}]
			}]
		}],
		buttons: [{
					xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
						var form = MatBackSupplyStation.searchForm.getForm();
						if (form.isValid()) {
							MatBackSupplyStation.grid.store.load();
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_d').clearInvalid();
							Ext.getCmp('endDate_d').clearInvalid();
						}
					}
				}, {
					xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
						MatBackSupplyStation.searchForm.getForm().reset();
						// 重置“入库库房”字段值
						Ext.getCmp('whIdx_s').clearValue();
						// 重新加载表格
						MatBackSupplyStation.grid.store.load();
					}
				}],
		buttonAlign: 'center'
	});
	/** ************** 定义查询表单结束 ************** */
	
	/** ************** 定义主体表格开始 ************** */
	MatBackSupplyStation.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matBackSupplyStation!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matBackSupplyStation!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matBackSupplyStation!logicDelete.action',            //删除数据的请求URL
	    tbar:null,
	    border: true,
	    singleSelect: true,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'库房主键', dataIndex:'whIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
		},{
			header:'库房名称', dataIndex:'whName', editor:{  maxLength:50 }
		},{
			header:'反馈人主键', dataIndex:'feedBackEmpID',hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'反馈人名称', dataIndex:'feedBackEmp', editor:{  maxLength:25 }
		},{
			header:'物料编码', dataIndex:'matCode', editor:{  maxLength:50 }
		},{
			header:'物料描述', dataIndex:'matDesc', editor:{  maxLength:100 }
		},{
			header:'单位', dataIndex:'unit', editor:{  maxLength:20 }
		},{
			header:'数量', dataIndex:'qty', editor:{ xtype:'numberfield' }
		},{
			header:'反馈日期', dataIndex:'feedBackDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'存在原因', dataIndex:'feedBackReason', editor:{  maxLength:200 }
		},{
			header:'单据状态', dataIndex:'status', editor:{  maxLength:20 },renderer: function(v) {
					if (v == STATUS_ZC) return "待登帐";
					if (v == STATUS_DZ) return "已登账";
					return "错误！未知状态";
				}
		},{
			header:'制单人', dataIndex:'makeBillEmp', hidden:true,editor:{  maxLength:25 }
		},{
			header:'制单日期', dataIndex:'makeBillDate',hidden:true, xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'登帐人', dataIndex:'registEmp',hidden:true, editor:{  maxLength:25 }
		},{
			header:'登帐日期', dataIndex:'registDate', hidden:true,xtype:'datecolumn', editor:{ xtype:'my97date' }
		}]
	});
	// 取消双击行出现“编辑”页面的事件监听
	MatBackSupplyStation.grid.un('rowdblclick', MatBackSupplyStation.grid.toEditFn, MatBackSupplyStation.grid);
	
	MatBackSupplyStation.grid.store.on('beforeload', function() {
		MatBackSupplyStation.searchParams = MatBackSupplyStation.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(MatBackSupplyStation.searchParams);
		MatBackSupplyStation.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	})
	/** ************** 定义主体表格结束 ************** */
	
	// 页面自适应布局
	MatBackSupplyStation.viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 150,
			border: true,
			collapsible: true,
//			collapseMode:'mini',
			split: true,
			items: [MatBackSupplyStation.searchForm]
		}, {
			// 结果显示列表区域
			region: 'center',
			layout: 'fit',
			height: 200,
			items: [MatBackSupplyStation.grid]
		}]
	});
});