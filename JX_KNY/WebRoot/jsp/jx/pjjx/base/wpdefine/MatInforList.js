

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
			header:'物料编码', dataIndex:'matCode'
		},{
			header:'物料描述', dataIndex:'matDesc', width: 200
		},{
			header:'计量单位', dataIndex:'unit', width: 60			
		},{
			header:'计划单价', dataIndex:'price', width: 60	
		}],
		storeId: 'matCode',
		storeAutoLoad: true
	});
	MatInforList.grid.un('rowdblclick', MatInforList.grid.toEditFn, MatInforList.grid);
	
	MatInforList.batchWin = new Ext.Window({
		title:"物料选择",
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
								xtype:"textfield", width:120, fieldLabel:"物料编码", id:"matCode"
							}
						]
					},
					{
						columnWidth:0.355,
						layout:"form", baseCls:"x-plain",
						items:[{
								xtype:"textfield", width:120, fieldLabel:"物料描述", id:"matDesc"
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
							text:"查询", iconCls:"searchIcon", handler: function() {
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
								text:"重置", iconCls:"resetIcon", handler: function() {
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
			text:'添加', iconCls:'addIcon', handler: function() {
				MatInforList.submit();
			}	
		}, {
			text:'关闭', iconCls:'closeIcon', handler: function() {
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