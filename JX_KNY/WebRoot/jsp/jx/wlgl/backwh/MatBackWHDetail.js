/**
 * 退库详情js
 */
Ext.onReady(function(){
	
	Ext.namespace('MatBackWHDetail');                       //定义命名空间
	
	/** ************* 定义全局变量开始 ************* */
	MatBackWHDetail.matBackWhIdx = "###";
	MatBackWHDetail.labelWidth = 80;
	MatBackWHDetail.fieldWidth = 140;
	/** ************* 定义全局变量结束 ************* */
	
	MatBackWHDetail.batchWin = new Ext.Window({
		title:"物料选择",
		width:1000, height:650,
		layout:"border",
		closeAction:"hide",
		plain:true,
		items:[
			{
				xtype:"panel",
				region:"center",
				layout:"fit",
				items: MatBackWHSelect.grid
				
			},
			{
				region:"north", baseCls:"x-plain", style:"padding:10px;",
				height:108,
				layout:"fit",
				border: false,
				items:{
					xtype:"form",
					id: "searchForm",
					layout:"column",
					baseCls:"x-plain", 
					labelWidth: MatBackWHDetail.labelWidth, 
					items:[
						{
							columnWidth: .33,
							layout:"form", baseCls:"x-plain",
							items:[
								{
									xtype:"textfield", width:MatBackWHDetail.fieldWidth, fieldLabel:"出库单编号", name:"billNo"
								}
							]
						},
						{
							columnWidth: .33,
							layout:"form", baseCls:"x-plain",
							items:[
								{
									xtype:"textfield", width:MatBackWHDetail.fieldWidth, fieldLabel:"出库用途", name:"purpose"
								}
							]
						},
						{
							columnWidth: .33,
							layout:"form", baseCls:"x-plain",
							items:[
								{
									xtype:"textfield", width:MatBackWHDetail.fieldWidth, fieldLabel:"领用人", name:"getEmp"
								}
							]
						},
						{
							columnWidth: .33,
							layout:"form", baseCls:"x-plain",
							items:[
								{
									xtype:"textfield", width:MatBackWHDetail.fieldWidth, fieldLabel:"物料编码", name:"matCode"
								}
							]
						},
						{
							columnWidth: .33,
							layout:"form", baseCls:"x-plain",
							items:[
								{
									xtype:"textfield", width:MatBackWHDetail.fieldWidth, fieldLabel:"物料描述", name:"matDesc"
								}
							]
						},
						{
							columnWidth: .33,
							layout:"form", baseCls:"x-plain",
							items:[
								{
									xtype: 'compositefield', fieldLabel: '领用日期', combineErrors: false,
									items: [{
										xtype:'my97date', name: 'startDate', id: 'startDate_k', format:'Y-m-d', value: lastMonth, width: 100, allowBlank: false,
										// 日期校验器
										validator: function() {
											var startDate =new Date(Ext.getCmp('startDate_k').getValue());
											var endDate = new Date(Ext.getCmp('endDate_k').getValue());
											if (startDate > endDate) {
												return "开始日期不能大于结束日期";
											}
										}
									}, {
										xtype: 'label',
										text: '至',
										style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
									}, {							
										xtype:'my97date', name: 'endDate', id: 'endDate_k', format:'Y-m-d', width: 100, allowBlank: false,
										// 日期校验器
										validator: function() {
											var startDate =new Date(Ext.getCmp('startDate_k').getValue());
											var endDate = new Date(Ext.getCmp('endDate_k').getValue());
											if (startDate > endDate) {
												return "结束日期不能小于开始日期";
											}
										}
									}]
								}
							]
						}],
						buttonAlign: 'center',
						buttons: [{
							text:'查询', iconCls:"searchIcon", handler: function() {
								var form = Ext.getCmp('searchForm').getForm();
								var searchParams = form.getValues();
								MatBackWHSelect.searchParam = MyJson.deleteBlankProp(searchParams);
								// 重新加载 【物料选择】 窗口表格数据
								MatBackWHSelect.grid.store.load();
							}
						}, {
							text:"重置", iconCls:"resetIcon", handler: function() {
								var form = Ext.getCmp('searchForm').getForm();
								form.reset();
								var searchParams = form.getValues();
								// 重新加载 【物料选择】 窗口表格数据
								MatBackWHSelect.searchParam = MyJson.deleteBlankProp(searchParams);
								MatBackWHSelect.grid.store.load();
							}
						}]
				}
			}
		],
		buttonAlign: 'center',
		buttons: [{
			text:'确认', iconCls:'yesIcon', handler: function() {
				var sm = MatBackWHSelect.grid.getSelectionModel();
				if (sm.getCount() <= 0) {
					MyExt.Msg.alert("尚未选择一条记录");
					return;
				}
				// 已选择的待添加的物料信息
				var selectedRecrods = sm.getSelections();
				// 检验已选择的待添加的物料信息是否已经被添加
				var count = MatBackWHDetail.grid.store.getCount();
				if (count != 0) {
					for (var i = 0; i < count; i++) {
						var record = MatBackWHDetail.grid.store.getAt(i);
						for (var j = 0; j < selectedRecrods.length; j++) {
							if (record.get('matOutWhDatailIdx') == selectedRecrods[j].get('idx')) {
//								MyExt.Msg.alert("物料编码<font color='red'>【" + record.get('matCode') + "】</font>已存在列表中，请不要重复添加");
								MyExt.Msg.alert("您选择的记录已存在列表中，请不要重复添加");
   	          					return ;
							}
						}
					}
				}
				// 声明一个Ext.data.Record变量
				var record_v = null;
				for (var k = 0; k < selectedRecrods.length; k++) {
					record_v = new Ext.data.Record();
					record_v.set("matOutWhDatailIdx",selectedRecrods[k].get('idx'));
					record_v.set("matCode",selectedRecrods[k].get('matCode'));
					record_v.set("matDesc",selectedRecrods[k].get('matDesc'));
					record_v.set("unit",selectedRecrods[k].get('unit'));
					record_v.set("qty","1");
					MatBackWHDetail.grid.store.insert(0, record_v); 
				    MatBackWHDetail.grid.getView().refresh(); 
				    MatBackWHDetail.grid.getSelectionModel().selectRow(0)
				}
				// 添加成功后，隐藏【物料选择】 窗口
				MatBackWHDetail.batchWin.hide();
				// 添加成功后，不能修改“退库库房”、“退库人”、“退库班组”字段
				Ext.getCmp('whName_k').disable();
				Ext.getCmp('backWhEmpId_k').disable();
				Ext.getCmp('backOrg_k').disable();
			}
		}, {
			text:'关闭', iconCls:'closeIcon', handler: function() {
				MatBackWHDetail.batchWin.hide();
			}
		}]
	})
	
	// 数据容器
	MatBackWHDetail.store = new Ext.data.JsonStore({
		id:'idx', totalProperty:'totalProperty', autoLoad:true, pruneModifiedRecords: true,
		root: 'root',
		url:ctx+'/matBackWHDetail!pageList.action',
		fields:['idx','matBackWhIdx','matCode','matDesc','unit','qty','matOutWhDatailIdx']
	});
	// 操作掩码
    MatBackWHDetail.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	// 行选择模式
    MatBackWHDetail.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    // 单据明细表格
    MatBackWHDetail.grid = new Ext.grid.EditorGridPanel({
		border: false, enableColumnMove: true, stripeRows: true, selModel: MatBackWHDetail.sm, loadMask: true,
		clicksToEdit: 1,
		viewConfig: {forceFit: true},
        store: MatBackWHDetail.store,
		colModel:new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
			{
	        	sortable:false, dataIndex:'idx', hidden:true, header:'idx'
	        },{
	        	sortable:false, dataIndex:'matBackWhIdx', hidden:true, header:'退库单主键'
	        },{
	        	sortable:false, dataIndex:'matCode', header:'物料编码'
	        },{	
	        	sortable:false, dataIndex:'matDesc', header:'物料描述'
	        },{	
	        	sortable:false, dataIndex:'unit', header:'计量单位'
	        },{
	        	sortable:false, dataIndex:'qty', header:'退库数量', editor: {
		        		maxLength: 4, 
		        		vtype: "nonNegativeInt", 
		        		allowBlank: false
					}
	        },{	
	        	sortable:false, dataIndex:'matOutWhDatailIdx', header:'出库明细主键', hidden: true
	        }]),
			tbar:['&nbsp;&nbsp;', {
					text:'批量添加',iconCls:'chart_attributeConfigIcon',handler:function(){
						var whIdx = Ext.getCmp("whIdx_k").getValue();
						// 验证退库库房idx主键是否为空
						if (Ext.isEmpty(whIdx)) {
							MyExt.Msg.alert("请先选择退库库房");
							return;
						}
						var getOrgId = Ext.getCmp("backOrgId_k").getValue();
						// 验证退库人所在班组id,即：出库时的领用人所在班组id是否为空
						if (Ext.isEmpty(getOrgId)) {
							MyExt.Msg.alert("请先选择退库人所在班组");
							return;
						}
						MatBackWHSelect.whIdx = whIdx;
						MatBackWHSelect.getOrgId = getOrgId;
						// 打开【物料选择】 窗口
	              		MatBackWHDetail.batchWin.show();
	              		// 初始化查询条件
						MatBackWHSelect.searchParam = Ext.getCmp('searchForm').getForm().getValues();
						// 重新加载 【物料选择】 窗口表格数据
						MatBackWHSelect.grid.store.load();
					}
				}, '&nbsp;&nbsp;', {
				text:'删除', iconCls:'deleteIcon', handler:function() {
	                var data = MatBackWHDetail.grid.selModel.getSelections();
			        if(data.length == 0 ){
			        	MyExt.Msg.alert("尚未选择一条记录！");
			            return;
			        }
			        var storeAt = MatBackWHDetail.grid.store.indexOf(data[0]);
			        var records = MatBackWHDetail.store.getModifiedRecords();
			        var count = records.length; 
			        var j = storeAt + 1;
			        if(count-1 == storeAt){
			        	j = storeAt-1;
			        }
				    MatBackWHDetail.grid.getSelectionModel().selectRow(j);
				    for (var i = 0; i < data.length; i++){
				        MatBackWHDetail.grid.store.remove(data[i]);
				    }
				    MatBackWHDetail.grid.getView().refresh();
				    // 如果删除了所有已添加的数据，则可以修改“退库库房”、“退库人”、“退库班组”字段
				    var sm = MatBackWHDetail.grid.selModel;
				    if (sm.getCount() == 0) {
						// 暂存后的记录不能修改“退库库房”、“退库人”、“退库班组”字段
						Ext.getCmp('whName_k').enable();
						Ext.getCmp('backWhEmpId_k').enable();
						Ext.getCmp('backOrg_k').enable();
				    }
	           }}, '&nbsp;&nbsp;', {
		           text:'刷新', iconCls:"refreshIcon", handler: function() {
		           		self.location.reload();
		           }
	           }]
    });
	MatBackWHDetail.grid.store.on("beforeload", function(){
		var searchParams = {matBackWhIdx: MatBackWHDetail.matBackWhIdx};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);  
	});
	
});