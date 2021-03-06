/**
 * 
 * 消耗配件盘点查询详情
 * 
 */
 
Ext.onReady(function() {
	Ext.namespace('MatCheckLossQueryDetail');
	
	/** ************ 定义全局变量开始 ************ */
	MatCheckLossQueryDetail.fieldWidth = 100;
	MatCheckLossQueryDetail.labelWidth = 70;
	MatCheckLossQueryDetail.matCheckLossIDX = '';
	/** ************ 定义全局变量结束 ************ */
	
	// 【单据信息】表单
	MatCheckLossQueryDetail.baseForm = new Ext.form.FormPanel({
		labelWidth:MatCheckLossQueryDetail.labelWidth,
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
						fieldLabel:"库房", 
						name:"whName", 
						xtype:"textfield",
						disabled: true,
						width:MatCheckLossQueryDetail.fieldWidth
			        },
                    {id:"whIdx_k",xtype:"hidden", name:"whIdx"}
				]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"checkEmp_k",
					xtype:"textfield", 
					name:"checkEmp", 
					fieldLabel: "经手人",
					disabled: true,
					width:MatCheckLossQueryDetail.fieldWidth
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"lossDate_k",
					name: "lossDate", 
					fieldLabel: "损耗日期", initNow: false,
					disabled: true,
					xtype: "my97date",
					format: "Y-m-d",  
					width: MatCheckLossQueryDetail.fieldWidth
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
					width: MatCheckLossQueryDetail.fieldWidth,
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
					width: MatCheckLossQueryDetail.fieldWidth,
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
					width: MatCheckLossQueryDetail.fieldWidth,
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
					width: MatCheckLossQueryDetail.fieldWidth,
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
    MatCheckLossQueryDetail.grid = new Ext.yunda.Grid({
    	title: '单据明细',
    	loadURL: ctx + '/matCheckLossDetail!pageList.action',
    	saveURL: ctx + '/matCheckLossDetail!saveOrUpdate.action',
    	deleteURL: ctx + '/matCheckLossDetail!logicDelete.action',
    	storeAutoLoad: false,
    	tbar: [], singleSelect: true,
    	fields: [{
	        	dataIndex:'idx', hidden:true, header:'idx'
	        },{
	        	dataIndex:'matCode', header:'物料编码', width: 40
	        },{
	        	dataIndex:'matCheckLossIDX', hidden:true, header:'盘点单idx主键'
	        },{	
	        	dataIndex:'matDesc', header:'物料描述' 
	        },{	
	        	dataIndex:'unit', header:'计量单位', width: 20
	        },{
	        	dataIndex:'qty', header:'数量', width: 20
	        }]
    });
    MatCheckLossQueryDetail.grid.un('rowdblclick', MatCheckLossQueryDetail.grid.toEditFn, MatCheckLossQueryDetail.grid);
	MatCheckLossQueryDetail.grid.store.on("beforeload", function() {
		var searchParams = {
			matCheckLossIDX : MatCheckLossQueryDetail.matCheckLossIDX
		};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	// 消耗配件盘点详情显示窗口
	MatCheckLossQueryDetail.batchWin = new Ext.Window({
		title:"盘点信息",
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
				items: MatCheckLossQueryDetail.baseForm
			},
			{
				xtype:"panel",
				border: false,
				region:"center",
				layout:"fit",
				items: MatCheckLossQueryDetail.grid
			}
		],
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				MatCheckLossQueryDetail.batchWin.hide();
			}
		}]
	})

});