/**
 * 
 * 机车外用料查询详情js
 * 
 */
 
Ext.onReady(function() {
	Ext.namespace('MatBackWHQueryDetail');
	
	/** ************ 定义全局变量开始 ************ */
	MatBackWHQueryDetail.fieldWidth = 100;
	MatBackWHQueryDetail.labelWidth = 70;
	MatBackWHQueryDetail.matBackWhIdx = '';
	/** ************ 定义全局变量结束 ************ */
	
	// 【单据信息】表单
	MatBackWHQueryDetail.baseForm = new Ext.form.FormPanel({
		labelWidth:MatBackWHQueryDetail.labelWidth,
		layout:"column",
		padding:"10px",
		items:[
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"billNo_k",
					fieldLabel:"单据编号", 
					name:"billNo", 
					xtype:"textfield",
					disabled: true,
					width: MatBackWHQueryDetail.fieldWidth + 80
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[
					{
						id:"whName_k",
						fieldLabel:"退库库房", 
						name:"whName", 
						xtype:"textfield",
						disabled: true,
						width: MatBackWHQueryDetail.fieldWidth + 40
					}
				]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"backDate_k",
					name: "backDate", 
					fieldLabel: "退库日期",
					disabled: true,
					xtype: "my97date",
					format: "Y-m-d",  
					width: MatBackWHQueryDetail.fieldWidth
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
						id:"backWhEmp_k",
						fieldLabel:"退库人", 
						name:"backWhEmp", 
						xtype:"textfield",
						disabled: true,
						width:MatBackWHQueryDetail.fieldWidth
					}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"backOrg_k",
					name: "backOrg", 
					fieldLabel: "退库班组",
					xtype: "textfield",
					disabled: true,
					width: MatBackWHQueryDetail.fieldWidth + 80
				}]
			},
			{
				xtype:"panel",
				columnWidth:1,
				layout:"form",
				items:[{
					xtype:"textfield", 
					id:"backReason_k", 
					name:"backReason", 
					fieldLabel:"退库原因",
					disabled: true,
					width:580
				}]
			},
			{
				xtype:"panel",
				columnWidth:1,
				layout:"form",
				items:[{
					xtype:"textfield", 
					id:"billSummary_k", 
					name:"billSummary", 
					fieldLabel:"单据摘要",
					disabled: true,
					width:580
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"makeBillEmp_k",
					fieldLabel : "制单人",
					name:"makeBillEmp", 
					xtype:"textfield",
					width: MatBackWHQueryDetail.fieldWidth,
					disabled:true
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"makeBillDate_k",
					name: "makeBillDate", 
					fieldLabel: "制单日期",
					xtype: "my97date",
					format: "Y-m-d", 
					width: MatBackWHQueryDetail.fieldWidth,
					disabled:true
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"registEmp_k",
					fieldLabel : "登账人",
					name:"registEmp", 
					xtype:"textfield", 
					width: MatBackWHQueryDetail.fieldWidth,
					disabled:true
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"registDate_k",
					name: "registDate", 
					fieldLabel: "登账日期",
					xtype: "my97date",format: "Y-m-d",  
					width: MatBackWHQueryDetail.fieldWidth,
					disabled:true
				}]
			}
		]
	});
	
    // 单据明细表格
    MatBackWHQueryDetail.grid = new Ext.yunda.Grid({
    	title: '单据明细',
    	loadURL: ctx + '/matBackWHDetail!pageList.action',
    	saveURL: ctx + '/matBackWHDetail!saveOrUpdate.action',
    	deleteURL: ctx + '/matBackWHDetail!logicDelete.action',
    	storeAutoLoad: false,
    	tbar: [], singleSelect: true,
    	fields: [{
	        	dataIndex:'idx', hidden:true, header:'idx主键'
	        },{
	        	dataIndex:'matBackWhIdx', hidden:true, header:'退库单idx主键'
	        },{
	        	dataIndex:'matOutWhDatailIdx', hidden:true, header:'出库单idx主键'
	        },{
	        	dataIndex:'matCode', header:'物料编码', width: 40
	        },{	
	        	dataIndex:'matDesc', header:'物料描述' 
	        },{	
	        	dataIndex:'unit', header:'计量单位', width: 20
	        },{
	        	dataIndex:'qty', header:'退库数量', width: 20
	        }]
    });
    MatBackWHQueryDetail.grid.un('rowdblclick', MatBackWHQueryDetail.grid.toEditFn, MatBackWHQueryDetail.grid);
	MatBackWHQueryDetail.grid.store.on("beforeload", function() {
		var searchParams = {
			matBackWhIdx : MatBackWHQueryDetail.matBackWhIdx
		};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	// 消耗配件入库详情显示窗口
	MatBackWHQueryDetail.batchWin = new Ext.Window({
		title:"入库信息",
		width:928,
		height:565,
		layout:"border", plain: true,
		closeAction:'hide',
		items:[
			{
				xtype:"panel",
				frame: true, border: false,
				region:"north",
				layout:"fit",
				height:160,
				items: MatBackWHQueryDetail.baseForm
			},
			{
				xtype:"panel",
				border: false,
				region:"center",
				layout:"fit",
				items: MatBackWHQueryDetail.grid
			}
		],
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				MatBackWHQueryDetail.batchWin.hide();
			}
		}]
	})

});