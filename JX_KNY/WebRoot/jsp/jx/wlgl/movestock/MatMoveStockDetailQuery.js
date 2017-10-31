/**
 * 
 * 消耗配件移库查询详情
 * 
 */
 
Ext.onReady(function() {
	Ext.namespace('MatMoveStockDetail');
	
	/** ************ 定义全局变量开始 ************ */
	MatMoveStockDetail.fieldWidth = 100;
	MatMoveStockDetail.labelWidth = 70;
	MatMoveStockDetail.matMoveStockIdx = '';
	/** ************ 定义全局变量结束 ************ */
	
	// 【单据信息】表单
	MatMoveStockDetail.baseForm = new Ext.form.FormPanel({
		labelWidth:MatMoveStockDetail.labelWidth,
		layout:"column", border: false,
		padding:"10px",
		items:[
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"billNo_k",
					fieldLabel:"单据编号", 
					name:"billNo", 
					xtype:"textfield",
					disabled: true,
					width:140
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[
			        {
						id:"exWhName_k",
						fieldLabel:"移出库房", 
						name:"exWhName", 
						xtype:"textfield",
						disabled: true,
						width:MatMoveStockDetail.fieldWidth
			        },
                    {id:"exWhIdx_k",xtype:"hidden", name:"exWhIdx"}
				]
				},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[
					{
						id:"exWhEmpId_k",
						fieldLabel:"移出人", 
						name:"exWhEmpId", 
						xtype:"textfield",
						disabled: true,
						width:MatMoveStockDetail.fieldWidth
			        }, 
			        {
					id:"exWhEmp_k",xtype:"hidden", name:"exWhEmp"
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"exWhDate_k",
					name: "exWhDate", 
					fieldLabel: "移出日期",
					disabled: true, 
					xtype: "my97date",
					format: "Y-m-d",  
					width: MatMoveStockDetail.fieldWidth
				}]
			},{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[
					{
						id:"inWhName_k",
						fieldLabel:"移入库房", 
						name:"inWhName", 
						xtype:"textfield",
						disabled: true,
						width:MatMoveStockDetail.fieldWidth
			        },
                    {id:"inWhIdx_k",xtype:"hidden", name:"inWhIdx"}
				]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
						id:"getEmpID_k",
						fieldLabel:"移料人", 
						name:"getEmpID", 
						xtype:"textfield",
						disabled: true,
						width:MatMoveStockDetail.fieldWidth
			        }, {
					id:"getEmp_k",xtype:"hidden", name:"getEmp"
				}]
			},{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"makeBillEmp_k",
					fieldLabel : "制单人",
					name:"makeBillEmp", 
					xtype:"textfield",
					width: MatMoveStockDetail.fieldWidth,
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
					width: MatMoveStockDetail.fieldWidth,
					disabled:true
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
					disabled: true,
					fieldLabel:"单据摘要",
					width:580
				}]
			}
		]
	});
	
    // 单据明细表格
    MatMoveStockDetail.grid = new Ext.yunda.Grid({
    	title: '单据明细',
    	loadURL: ctx + '/matMoveStockDetail!findPageList.action',
    	saveURL: ctx + '/matMoveStockDetail!saveOrUpdate.action',
    	deleteURL: ctx + '/matMoveStockDetail!logicDelete.action',
    	storeAutoLoad: false,
    	tbar: [], singleSelect: true,
    	fields: [{
	        	dataIndex:'idx', hidden:true, header:'idx'
	        },{
	        	dataIndex:'matCode', header:'物料编码', width: 40
	        },{
	        	sortable:false, dataIndex:'matMoveStockIdx', hidden:true, header:'移库单主键'
	        },{	
	        	dataIndex:'matDesc', header:'物料描述' 
	        },{	
	        	dataIndex:'unit', header:'计量单位', width: 20
	        },{
	        	dataIndex:'qty', header:'数量', width: 20
	        }]
    });
    MatMoveStockDetail.grid.un('rowdblclick', MatMoveStockDetail.grid.toEditFn, MatMoveStockDetail.grid);
	MatMoveStockDetail.grid.store.on("beforeload", function() {
		var searchParams = {
			matMoveStockIdx : MatMoveStockDetail.matMoveStockIdx
		};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	// 消耗配件入库详情显示窗口
	MatMoveStockDetail.batchWin = new Ext.Window({
		title:"移库信息",
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
				height:108,
				items: MatMoveStockDetail.baseForm
			},
			{
				xtype:"panel",
				border: false,
				region:"center",
				layout:"fit",
				items: MatMoveStockDetail.grid
			}
		],
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				MatMoveStockDetail.batchWin.hide();
			}
		}]
	})

});