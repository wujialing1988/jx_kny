/**
 * 
 * 机车检修用料查询详情js
 * 
 */
 
Ext.onReady(function() {
	Ext.namespace('MatOutWHTrainQueryDetail');
	
	/** ************ 定义全局变量开始 ************ */
	MatOutWHTrainQueryDetail.fieldWidth = 100;
	MatOutWHTrainQueryDetail.labelWidth = 70;
	MatOutWHTrainQueryDetail.matOutWHTrainIDX = '';
	/** ************ 定义全局变量结束 ************ */
	
	// 【单据信息】表单
	MatOutWHTrainQueryDetail.baseForm = new Ext.form.FormPanel({
		labelWidth:MatOutWHTrainQueryDetail.labelWidth,
		layout:"column",
		padding:"10px",
		items:[
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"billNo_k",
					name:"billNo", 
					fieldLabel:"单据编号", 
					xtype:"textfield",
					disabled: true,
					width: MatOutWHTrainQueryDetail.fieldWidth + 80
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"whName_k",
					name:"whName", 
					fieldLabel:"领用库房", 
					xtype:"textfield",
					disabled: true,
					width:MatOutWHTrainQueryDetail.fieldWidth
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"getDate_k",
					name: "getDate", 
					fieldLabel: "领用日期",
					xtype: "my97date",
					format: "Y-m-d",  
					disabled: true,
					width: MatOutWHTrainQueryDetail.fieldWidth
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"getEmp_k",
					name:"getEmp", 
					fieldLabel:"领用人", 
					xtype:"textfield",
					disabled: true,
					width:MatOutWHTrainQueryDetail.fieldWidth
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
					width: MatOutWHTrainQueryDetail.fieldWidth + 80
				}]
			},
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.33,
				items:[{
					id:"exWhEmp_k",
					name: "exWhEmp", 
					fieldLabel: "出库人",
					xtype: "textfield",
					disabled: true,
					width: MatOutWHTrainQueryDetail.fieldWidth
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
				columnWidth:.25,
				layout:"form",
				items:[{
					id:"trainTypeShortName_k",
					name: "trainTypeShortName", 
					fieldLabel: "车型",
					xtype: "textfield",
					disabled: true,
					width: MatOutWHTrainQueryDetail.fieldWidth
				}]
			},
			{
				xtype:"panel",
				columnWidth:.25,
				layout:"form",
				items:[{
					id:"trainNo_k",
					name: "trainNo", 
					fieldLabel: "车号",
					xtype: "textfield",
					disabled: true,
					width: MatOutWHTrainQueryDetail.fieldWidth
				}]
			},
			{
				xtype:"panel",
				columnWidth:.25,
				layout:"form",
				items:[{
					id:"xcName_k",
					name: "xcName", 
					fieldLabel: "修程",
					xtype: "textfield",
					disabled: true,
					width: MatOutWHTrainQueryDetail.fieldWidth
				}]
			},
			{
				xtype:"panel",
				columnWidth:.25,
				layout:"form",
				items:[{
					id:"rtName_k",
					name: "rtName", 
					fieldLabel: "修次",
					xtype: "textfield",
					disabled: true,
					width: MatOutWHTrainQueryDetail.fieldWidth
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
					width: MatOutWHTrainQueryDetail.fieldWidth,
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
					width: MatOutWHTrainQueryDetail.fieldWidth,
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
					width: MatOutWHTrainQueryDetail.fieldWidth,
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
					width: MatOutWHTrainQueryDetail.fieldWidth,
					disabled:true
				}]
			}
		]
	});
	
    // 单据明细表格
    MatOutWHTrainQueryDetail.grid = new Ext.yunda.Grid({
    	title: '单据明细',
    	loadURL: ctx + '/matOutWHTrainDetail!pageList.action',
    	saveURL: ctx + '/matOutWHTrainDetail!saveOrUpdate.action',
    	deleteURL: ctx + '/matOutWHTrainDetail!logicDelete.action',
    	storeAutoLoad: false,
    	tbar: [], singleSelect: true,
    	fields: [{
	        	dataIndex:'idx', hidden:true, header:'idx'
	        },{
	        	dataIndex:'matCode', header:'物料编码', width: 40
	        },{
	        	dataIndex:'matOutWHTrainIDX', hidden:true, header:'机车用料单idx主键'
	        },{
	        	dataIndex:'matTrainExpendAccountIDX', hidden:true, header:'机车用料消耗记录主键'
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
    MatOutWHTrainQueryDetail.grid.un('rowdblclick', MatOutWHTrainQueryDetail.grid.toEditFn, MatOutWHTrainQueryDetail.grid);
	MatOutWHTrainQueryDetail.grid.store.on("beforeload", function() {
		var searchParams = {
			matOutWHTrainIDX : MatOutWHTrainQueryDetail.matOutWHTrainIDX
		};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	// 消耗配件入库详情显示窗口
	MatOutWHTrainQueryDetail.batchWin = new Ext.Window({
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
				items: MatOutWHTrainQueryDetail.baseForm
			},
			{
				xtype:"panel",
				border: false,
				region:"center",
				layout:"fit",
				items: MatOutWHTrainQueryDetail.grid
			}
		],
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				MatOutWHTrainQueryDetail.batchWin.hide();
			}
		}]
	})

});