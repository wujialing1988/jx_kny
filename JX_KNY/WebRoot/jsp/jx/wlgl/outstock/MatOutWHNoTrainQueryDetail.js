/**
 * 
 * 机车外用料查询详情js
 * 
 */
 
Ext.onReady(function() {
	Ext.namespace('MatOutWHNoTrainQueryDetail');
	
	/** ************ 定义全局变量开始 ************ */
	MatOutWHNoTrainQueryDetail.fieldWidth = 100;
	MatOutWHNoTrainQueryDetail.labelWidth = 70;
	MatOutWHNoTrainQueryDetail.matOutWHNoTrainIDX = '';
	/** ************ 定义全局变量结束 ************ */
	
	// 【单据信息】表单
	MatOutWHNoTrainQueryDetail.baseForm = new Ext.form.FormPanel({
		labelWidth:MatOutWHNoTrainQueryDetail.labelWidth,
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
					width: MatOutWHNoTrainQueryDetail.fieldWidth + 80
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[
					{
						id:"whName_k",
						fieldLabel:"领用库房", 
						name:"whName", 
						xtype:"textfield",
						disabled: true,
						width: MatOutWHNoTrainQueryDetail.fieldWidth + 40
					}
				]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"getDate_k",
					name: "getDate", 
					fieldLabel: "领用日期",
					disabled: true,
					xtype: "my97date",
					format: "Y-m-d",  
					width: MatOutWHNoTrainQueryDetail.fieldWidth
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
						id:"getEmp_k",
						fieldLabel:"领用人", 
						name:"getEmp", 
						xtype:"textfield",
						disabled: true,
						width:MatOutWHNoTrainQueryDetail.fieldWidth
					}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"getOrg_k",
					name: "getOrg", 
					fieldLabel: "领用班组",
					xtype: "textfield",
					disabled: true,
					width: MatOutWHNoTrainQueryDetail.fieldWidth + 80
				}]
			},
			{
				xtype:"panel",
				columnWidth:1,
				layout:"form",
				items:[{
					xtype:"textfield", 
					id:"purpose_k", 
					name:"purpose", 
					fieldLabel:"用途",
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
					width: MatOutWHNoTrainQueryDetail.fieldWidth,
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
					width: MatOutWHNoTrainQueryDetail.fieldWidth,
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
					width: MatOutWHNoTrainQueryDetail.fieldWidth,
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
					width: MatOutWHNoTrainQueryDetail.fieldWidth,
					disabled:true
				}]
			}
		]
	});
	
    // 单据明细表格
    MatOutWHNoTrainQueryDetail.grid = new Ext.yunda.Grid({
    	title: '单据明细',
    	loadURL: ctx + '/matOutWHNoTrainDetail!pageList.action',
    	saveURL: ctx + '/matOutWHNoTrainDetail!saveOrUpdate.action',
    	deleteURL: ctx + '/matOutWHNoTrainDetail!logicDelete.action',
    	storeAutoLoad: false,
    	tbar: [], singleSelect: true,
    	fields: [{
	        	dataIndex:'idx', hidden:true, header:'idx'
	        },{
	        	dataIndex:'matCode', header:'物料编码', width: 40
	        },{
	        	dataIndex:'matOutWHNoTrainIDX', hidden:true, header:'机车外用料单idx主键'
	        },{	
	        	dataIndex:'matDesc', header:'物料描述' 
	        },{	
	        	dataIndex:'unit', header:'计量单位', width: 20
	        },{
	        	dataIndex:'qty', header:'数量', width: 20
	        },{
	        	dataIndex:'backQty', header:'退库数量', width: 20
	        }]
    });
    MatOutWHNoTrainQueryDetail.grid.un('rowdblclick', MatOutWHNoTrainQueryDetail.grid.toEditFn, MatOutWHNoTrainQueryDetail.grid);
	MatOutWHNoTrainQueryDetail.grid.store.on("beforeload", function() {
		var searchParams = {
			matOutWHNoTrainIDX : MatOutWHNoTrainQueryDetail.matOutWHNoTrainIDX
		};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	// 消耗配件入库详情显示窗口
	MatOutWHNoTrainQueryDetail.batchWin = new Ext.Window({
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
				items: MatOutWHNoTrainQueryDetail.baseForm
			},
			{
				xtype:"panel",
				border: false,
				region:"center",
				layout:"fit",
				items: MatOutWHNoTrainQueryDetail.grid
			}
		],
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				MatOutWHNoTrainQueryDetail.batchWin.hide();
			}
		}]
	})

});