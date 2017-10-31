/**
 * 物料入库明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace('MatPlanDetail');                       //定义命名空间
	
	/** ************* 定义全局变量开始 ************* */
	MatPlanDetail.matPlanIdx = "###";
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 【添加】按钮触发的函数处理
	MatPlanDetail.addFn = function() {
		var matCode = Ext.getCmp('matCode_s').getValue();
		// 验证物料编码是否为空
		if (Ext.isEmpty(matCode)) {
			MyExt.Msg.alert("请输入物料编码");
			return;
		}
		Ext.Ajax.request({
			url : ctx + '/matTypeList!getModelByMatCode.action',
			params : { matCode: matCode },
			success : function(response, options) {
				var result = Ext.util.JSON.decode(response.responseText);
				if (result.errMsg == null) {
					var list = result.list;
					if (list.length <= 0) {
						return;
					}
					// 判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
					for (var i = 0; i < list.length; i++) {
						var entity = list[i];
						var count = MatPlanDetail.grid.store.getCount();
						if (count != 0) {
							for (var i = 0; i < count; i++) {
								var record = MatPlanDetail.grid.store.getAt(i);
								if (entity.matCode == record.get('matCode')) {
									MyExt.Msg.alert("物料编码<font color='red'>【" + record.get('matCode') + "】</font>已存在列表中，请不要重复添加");
	   	          					return ;
	   	          				}
							}
						}
					}
					var record_v = null;
					for (var j = 0; j < list.length; j++) {
						var entity = list[j];
						record_v = new Ext.data.Record();
						record_v.set("matCode",entity.matCode);
						record_v.set("matDesc",entity.matDesc);
						record_v.set("unit",entity.unit);
						record_v.set("price",entity.price);
						record_v.set("qty","1");
						MatPlanDetail.grid.store.insert(0, record_v); 
					    MatPlanDetail.grid.getView().refresh(); 
					    MatPlanDetail.grid.getSelectionModel().selectRow(0)
					}
				} else {
					alertFail(result.errMsg);
				}
			},
			failure : function(response, options) {
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	// 【删除】按钮触发的函数处理
	MatPlanDetail.deleteFn = function() {
        var data = MatPlanDetail.grid.selModel.getSelections();
        if(data.length == 0 ){
        	MyExt.Msg.alert("尚未选择一条记录！");
            return;
        }
        var storeAt = MatPlanDetail.grid.store.indexOf(data[0]);
        var records = MatPlanDetail.store.getModifiedRecords();
        var count = records.length; 
        var j = storeAt + 1;
        if(count-1 == storeAt){
        	j = storeAt-1;
        }
	    MatPlanDetail.grid.getSelectionModel().selectRow(j);
	    for (var i = 0; i < data.length; i++){
	        MatPlanDetail.grid.store.remove(data[i]);
	    }
	    MatPlanDetail.grid.getView().refresh();
	}
	/** ************* 定义全局函数结束 ************* */
	
	MatPlanDetail.batchWin = new Ext.Window({
		title:"物料选择",
		width:605, height:350,
		layout:"border",
		closeAction:"hide",
		plain:true,
		items:[
			{
				xtype:"panel",
				region:"center",
				layout:"fit",
				items: MatTypeList.grid
				
			},
			{
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
							items:[
								{
									xtype:"textfield", width:120, fieldLabel:"物料描述", id:"matDesc"
								}
							]
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
									MatTypeList.searchParam = MyJson.deleteBlankProp(searchParams);
									// 重新加载 【物料选择】 窗口表格数据
									MatTypeList.grid.store.load();
								}
							}]
						},
						{
							columnWidth:0.12, baseCls:"x-plain",
							layout:"form",
							items:[
								{
									xtype:"button", width:40,
									text:"重置", iconCls:"resetIcon", handler: function() {
										Ext.getCmp('searchForm').getForm().reset();
										// 重新加载 【物料选择】 窗口表格数据
										MatTypeList.searchParam = {};
										MatTypeList.grid.store.load();
									}
								}
							]
						}
					]
				}
			}
		],
		buttonAlign: 'center',
		buttons: [{
			text:'确认', iconCls:'yesIcon', handler: function() {
				var sm = MatTypeList.grid.getSelectionModel();
				if (sm.getCount() <= 0) {
					MyExt.Msg.alert("尚未选择一条记录");
					return;
				}
				// 已选择的待添加的物料信息
				var selectedRecrods = sm.getSelections();
				// 检验已选择的待添加的物料信息是否已经被添加
				var count = MatPlanDetail.grid.store.getCount();
				if (count != 0) {
					for (var i = 0; i < count; i++) {
						var record = MatPlanDetail.grid.store.getAt(i);
						for (var j = 0; j < selectedRecrods.length; j++) {
							if (record.get('matCode') == selectedRecrods[j].get('matCode')) {
								MyExt.Msg.alert("物料编码<font color='red'>【" + record.get('matCode') + "】</font>已存在列表中，请不要重复添加");
   	          					return ;
							}
						}
					}
				}
				// 声明一个Ext.data.Record变量
				var record_v = null;
				for (var k = 0; k < selectedRecrods.length; k++) {
					record_v = new Ext.data.Record();
					record_v.set("matCode",selectedRecrods[k].get('matCode'));
					record_v.set("matDesc",selectedRecrods[k].get('matDesc'));
					record_v.set("unit",selectedRecrods[k].get('unit'));
					record_v.set("price",selectedRecrods[k].get('price'));
					record_v.set("qty","1");
					MatPlanDetail.grid.store.insert(0, record_v); 
				    MatPlanDetail.grid.getView().refresh(); 
				    MatPlanDetail.grid.getSelectionModel().selectRow(0)
				}
				// 添加成功后，隐藏【物料选择】 窗口
				MatPlanDetail.batchWin.hide();
			}
		}, {
			text:'关闭', iconCls:'closeIcon', handler: function() {
				MatPlanDetail.batchWin.hide();
			}
		}]
	})
	
	// 数据容器
	MatPlanDetail.store = new Ext.data.JsonStore({
		id:'idx', totalProperty:'totalProperty', autoLoad:false, pruneModifiedRecords: true,
		root: 'root',
		url:ctx+'/matPlanDetail!pageList.action',
		fields:['idx','matCode','matPlanIdx','matDesc','unit','price', 'qty','supplier']
	});
	// 操作掩码
    MatPlanDetail.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	// 行选择模式
    MatPlanDetail.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    // 单据明细表格
    MatPlanDetail.grid = new Ext.grid.EditorGridPanel({
		border: false, enableColumnMove: true, stripeRows: true, selModel: MatPlanDetail.sm, loadMask: true,
		clicksToEdit: 1,
		viewConfig: {forceFit: true},
        store: MatPlanDetail.store,
		colModel:new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
			{
	        	sortable:false, dataIndex:'idx', hidden:true, header:'idx'
	        },{
	        	sortable:false, dataIndex:'matCode', header:'物料编码'
	        },{
	        	sortable:false, dataIndex:'matPlanIdx', hidden:true, header:'计划单idx主键'
	        },{	
	        	sortable:false, dataIndex:'matDesc', header:'物料描述'
	        },{	
	        	sortable:false, dataIndex:'unit', header:'计量单位'
	        },{	
	        	sortable:false, dataIndex:'price', header:'计量单价'
	        },{
	        	sortable:false, dataIndex:'qty', header:'数量', editor:{
		        		maxLength: 4, 
		        		vtype: "nonNegativeInt", 
		        		allowBlank: false
					}
	        },{	
	        	sortable:false, dataIndex:'supplier', header:'生产厂家及供应商', editor:{
//	        			xtype: 'textfield',
		        		maxLength: 100
					}
	        }]),
			tbar:[
				'物料编码：', {
					xtype:'textfield',id:'matCode_s',width:120,enableKeyEvents:true,listeners: {
//						keyup: function(filed, e) {
//							// 如果敲下Enter（回车键），则触发添加按钮的函数处理
//							if (e.getKey() == e.ENTER){
//								MatPlanDetail.addFn();
//							}
//						}
					}
				}, '&nbsp;&nbsp;', {
					text:'添加',iconCls:'addIcon',handler:MatPlanDetail.addFn
				}, '&nbsp;&nbsp;', {
					text:'批量添加',iconCls:'chart_attributeConfigIcon',handler:function(){
						// 打开【物料选择】 窗口
	              		MatPlanDetail.batchWin.show();
						// 重新加载 【物料选择】 窗口表格数据
						MatTypeList.grid.store.load();
					}
				}, '&nbsp;&nbsp;', {
				text:'删除', iconCls:'deleteIcon', handler:MatPlanDetail.deleteFn
				}, '&nbsp;&nbsp;', {
		           text:'刷新', iconCls:"refreshIcon", handler: function() {
		           		self.location.reload();
		           }
	           }]
    });
	MatPlanDetail.grid.store.on("beforeload", function(){
		var searchParams = {matPlanIdx: MatPlanDetail.matPlanIdx};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);  
	});
	
});