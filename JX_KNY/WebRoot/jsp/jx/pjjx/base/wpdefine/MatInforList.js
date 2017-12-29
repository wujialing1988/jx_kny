

Ext.onReady(function() {
	Ext.namespace('MatInforList');
	
	/** ************* 定义全局变量开始 ************* */
	MatInforList.IDX = "";
	MatInforList.searchParam = {};
	/** ************* 定义全局变量结束 ************* */
	
	MatInforList.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matTypeList!pageQuery.action',                 //装载列表数据的请求URL	  
	    tbar:null,
		fields: [{
			header:i18n.TruckFaultReg.materialCode, dataIndex:'matCode'
		},{
			header:i18n.TruckFaultReg.materialDescription, dataIndex:'matDesc', width: 200
		},{
			header:i18n.TruckFaultReg.MeasureUnit, dataIndex:'unit', width: 60			
		},{
			header:i18n.TruckFaultReg.expectUnitPrice, dataIndex:'price', width: 60	
		}],
		storeId: 'matCode',
		storeAutoLoad: true
	});
	MatInforList.grid.un('rowdblclick', MatInforList.grid.toEditFn, MatInforList.grid);
	
	MatInforList.batchWin = new Ext.Window({
		title:i18n.TruckFaultReg.addMaterial,
		width:605, height:350,
		layout:"border",
		closeAction:"hide",
		plain:true,
		items:[{
			xtype:"panel",
			region:"center",
			layout:"fit",
			items: MatInforList.grid
		},{
			region:"north", baseCls:"x-plain", style:"padding:10px;",
			height:43,
			layout:"fit",
			items:{
				xtype:"form",
				id: "searchForm",
				layout:"column",
				baseCls:"x-plain", 
				labelWidth: 60, 
				items:[
					{
						columnWidth:0.355,
						layout:"form", baseCls:"x-plain",
						items:[
							{
								xtype:"textfield", width:120, fieldLabel:i18n.TruckFaultReg.materialCode, id:"matCode"
							}
						]
					},
					{
						columnWidth:0.355,
						layout:"form", baseCls:"x-plain",
						items:[{
								xtype:"textfield", width:120, fieldLabel:i18n.TruckFaultReg.materialDescription, id:"matDesc"
							}]
					},
					{
						columnWidth:0.05, baseCls:"x-plain"
					},
					{
						columnWidth:0.12,
						layout:"form", baseCls:"x-plain",
						autoWidth:false,
						bodyStyle:"",
						items:[{
							xtype:"button", width:40,
							text:i18n.TruckFaultReg.search, iconCls:"searchIcon", handler: function() {
								var form = Ext.getCmp('searchForm').getForm();
								var searchParams = form.getValues();
								MatInforList.searchParam = MyJson.deleteBlankProp(searchParams);
								// 重新加载 【物料选择】 窗口表格数据
								MatInforList.grid.store.load();
							}
						}]
					},
					{
						columnWidth:0.12, baseCls:"x-plain",
						layout:"form",
						items:[{
								xtype:"button", width:40,
								text:i18n.TruckFaultReg.reset, iconCls:"resetIcon", handler: function() {
									Ext.getCmp('searchForm').getForm().reset();
									// 重新加载 【物料选择】 窗口表格数据
									MatInforList.searchParam = {};
									MatInforList.grid.store.load();
								}
							}]
					}]
				}
			}],
		buttonAlign: 'center',
		buttons: [{
			text:i18n.TruckFaultReg.add, iconCls:'addIcon', handler: function() {
				MatInforList.submit();
			}	
		}, {
			text:i18n.TruckFaultReg.close, iconCls:'closeIcon', handler: function() {
				MatInforList.batchWin.hide();
			}
		}]
		
	});	
	//查询前添加过滤条件
	MatInforList.grid.store.on('beforeload' , function(){
		var whereList = [];
		for (var prop in MatInforList.searchParam) {
		   whereList.push({propName:prop, compare:Condition.LIKE, propValue:MatInforList.searchParam[ prop ]});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	//  确认提交方法，后面可覆盖此方法完成查询
	MatInforList.submit = function(){alert("请覆盖，MatInforList.submit 方法完成自己操作业务！");}
});