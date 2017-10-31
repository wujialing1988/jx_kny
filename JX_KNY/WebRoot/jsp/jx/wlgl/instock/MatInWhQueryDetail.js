/**
 * 
 * 消耗配件入库查询详情
 * 
 */
 
Ext.onReady(function() {
	Ext.namespace('MatInWhQueryDetail');
	
	/** ************ 定义全局变量开始 ************ */
	MatInWhQueryDetail.fieldWidth = 100;
	MatInWhQueryDetail.labelWidth = 70;
	MatInWhQueryDetail.matInWhIdx = '';
	/** ************ 定义全局变量结束 ************ */
	
	// 【单据信息】表单
	MatInWhQueryDetail.baseForm = new Ext.form.FormPanel({
		labelWidth:MatInWhQueryDetail.labelWidth,
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
						id:"whName_k",
						fieldLabel:"入库库房", 
						name:"whName", 
						xtype:"textfield",
						disabled: true,
						width:MatInWhQueryDetail.fieldWidth
			        },
                    {id:"whIdx_k",xtype:"hidden", name:"whIdx"}
				]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"inWhEmp_k",
					xtype:"textfield", 
					name:"inWhEmp", 
					fieldLabel: "入库人",
					disabled: true,
					width:MatInWhQueryDetail.fieldWidth
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"inWhDate_k",
					name: "inWhDate", 
					fieldLabel: "入库日期", initNow: false,
					disabled: true,
					xtype: "my97date",
					format: "Y-m-d",  
					width: MatInWhQueryDetail.fieldWidth
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
					width: MatInWhQueryDetail.fieldWidth,
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
					fieldLabel: "制单日期", initNow: false,
					xtype: "my97date",
					format: "Y-m-d", 
					width: MatInWhQueryDetail.fieldWidth,
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
					width: MatInWhQueryDetail.fieldWidth,
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
					fieldLabel: "登账日期", initNow: false,
					xtype: "my97date",format: "Y-m-d",  
					width: MatInWhQueryDetail.fieldWidth,
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
    MatInWhQueryDetail.grid = new Ext.yunda.Grid({
    	title: '单据明细',
    	loadURL: ctx + '/matInWhDetail!pageList.action',
    	saveURL: ctx + '/matInWhDetail!saveOrUpdate.action',
    	deleteURL: ctx + '/matInWhDetail!logicDelete.action',
    	storeAutoLoad: false,
    	tbar: [], singleSelect: true,
    	fields: [{
	        	dataIndex:'idx', hidden:true, header:'idx'
	        },{
	        	dataIndex:'matCode', header:'物料编码', width: 40
	        },{
	        	dataIndex:'matInWhIdx', hidden:true, header:'入库单idx主键'
	        },{	
	        	dataIndex:'matDesc', header:'物料描述' 
	        },{	
	        	dataIndex:'unit', header:'计量单位', width: 20
	        },{
	        	dataIndex:'qty', header:'数量', width: 20
	        }]
    });
    MatInWhQueryDetail.grid.un('rowdblclick', MatInWhQueryDetail.grid.toEditFn, MatInWhQueryDetail.grid);
	MatInWhQueryDetail.grid.store.on("beforeload", function() {
		var searchParams = {
			matInWhIdx : MatInWhQueryDetail.matInWhIdx
		};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	// 消耗配件入库详情显示窗口
	MatInWhQueryDetail.batchWin = new Ext.Window({
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
				height:108,
				items: MatInWhQueryDetail.baseForm
			},
			{
				xtype:"panel",
				border: false,
				region:"center",
				layout:"fit",
				items: MatInWhQueryDetail.grid
			}
		],
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				MatInWhQueryDetail.batchWin.hide();
			}
		}]
	})

});