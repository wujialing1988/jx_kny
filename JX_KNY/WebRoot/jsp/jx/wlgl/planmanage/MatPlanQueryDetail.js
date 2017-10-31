/**
 * 
 * 消耗配件入库查询详情
 * 
 */
 
Ext.onReady(function() {
	Ext.namespace('MatPlanQueryDetail');
	
	/** ************ 定义全局变量开始 ************ */
	MatPlanQueryDetail.fieldWidth = 100;
	MatPlanQueryDetail.labelWidth = 70;
	MatPlanQueryDetail.matPlanIdx = '';
	/** ************ 定义全局变量结束 ************ */
	
	// 【单据信息】表单
	MatPlanQueryDetail.baseForm = new Ext.form.FormPanel({
		labelWidth:MatPlanQueryDetail.labelWidth,
		layout:"column", border: false,
		padding:"10px",
		items:[{
				layout:"form",
				columnWidth:0.4,
				items:[{
					fieldLabel:"单据编号", 
					name:"billNo", 
					xtype:"textfield",
					disabled: true,
					width: MatPlanQueryDetail.fieldWidth + 80
				}]
			},
			{
				layout:"form",
				columnWidth:0.3,
				items:[{
					xtype:'textfield',
					name:"applyEmp", 
					fieldLabel : "申请人",
					width:MatPlanQueryDetail.fieldWidth,
					disabled:true
				}]
			},
			{
				layout:"form",
				columnWidth:0.3,
				items:[{
					name: "applyDate", 
					fieldLabel: "申请日期",
					xtype: "my97date",
					format: "Y-m-d",  
					width: MatPlanQueryDetail.fieldWidth,
					disabled:true
				}]
			},
			{
				columnWidth: 0.4,
				layout:"form",
				items:[{
					xtype:"textfield", 
					name:"planName", 
					fieldLabel:"计划名称",
					width: MatPlanQueryDetail.fieldWidth + 80,
					disabled:true
				}]
			},
			{
				columnWidth:0.6,
				layout:"form",
				items:[{
					xtype:"textfield", 
					name:"useTeam", 
					fieldLabel:"使用班组",
					width: MatPlanQueryDetail.fieldWidth + 80,
					disabled:true
				}]
			},
			{
				layout:"form",
				columnWidth:0.25,
				items:[{
					fieldLabel : "制单人",
					name:"makeBillEmp", 
					xtype:"textfield",
					width: MatPlanQueryDetail.fieldWidth,
					disabled:true
				}]
			},
			{
				layout:"form",
				columnWidth:0.25,
				items:[{
					name: "makeBillDate", 
					fieldLabel: "制单日期",
					xtype: "my97date",
					format: "Y-m-d", 
					width: MatPlanQueryDetail.fieldWidth,
					disabled:true
				}]
			},
			{
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"registEmp_k",
					fieldLabel : "提交人",
					name:"registEmp", 
					xtype:"textfield", 
					width: MatPlanQueryDetail.fieldWidth,
					disabled:true
				}]
			},
			{
				layout:"form",
				columnWidth:0.25,
				items:[{
					id:"registDate_k",
					name: "registDate", 
					fieldLabel: "提交日期",
					xtype: "my97date",format: "Y-m-d",  
					width: MatPlanQueryDetail.fieldWidth,
					disabled:true
				}]
			}
		]
	});
	
    // 单据明细表格
    MatPlanQueryDetail.grid = new Ext.yunda.Grid({
    	title: '单据明细',
    	loadURL: ctx + '/matPlanDetail!pageList.action',
    	saveURL: ctx + '/matPlanDetail!saveOrUpdate.action',
    	deleteURL: ctx + '/matPlanDetail!logicDelete.action',
    	storeAutoLoad: false,
    	tbar: [], singleSelect: true,
    	fields: [{
	        	dataIndex:'idx', hidden:true, header:'idx'
	        },{
	        	dataIndex:'matCode', header:'物料编码', width: 30
	        },{
	        	dataIndex:'matPlanIdx', hidden:true, header:'用料计划单idx主键'
	        },{	
	        	dataIndex:'matDesc', header:'物料描述' 
	        },{	
	        	dataIndex:'price', header:'计划单价', width: 20
	        },{	
	        	dataIndex:'unit', header:'计量单位', width: 20
	        },{
	        	dataIndex:'qty', header:'数量', width: 20
	        },{
	        	dataIndex:'supplier', header:'生产产家及供应商', width: 50
	        }]
    });
    MatPlanQueryDetail.grid.un('rowdblclick', MatPlanQueryDetail.grid.toEditFn, MatPlanQueryDetail.grid);
	MatPlanQueryDetail.grid.store.on("beforeload", function() {
		var searchParams = {
			matPlanIdx : MatPlanQueryDetail.matPlanIdx
		};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	// 消耗配件入库详情显示窗口
	MatPlanQueryDetail.batchWin = new Ext.Window({
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
				height:110,
				items: MatPlanQueryDetail.baseForm
			},
			{
				xtype:"panel",
				border: false,
				region:"center",
				layout:"fit",
				items: MatPlanQueryDetail.grid
			}
		],
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				MatPlanQueryDetail.batchWin.hide();
			}
		}]
	})

});