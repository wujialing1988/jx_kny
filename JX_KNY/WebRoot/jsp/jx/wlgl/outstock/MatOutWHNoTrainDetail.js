/**
 * 机车外用料详情js
 */
Ext.onReady(function(){
	
	Ext.namespace('MatOutWHNoTrainDetail');                       //定义命名空间
	
	/** ************* 定义全局变量开始 ************* */
	MatOutWHNoTrainDetail.matOutWHNoTrainIDX = "###";
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 【添加】按钮触发的函数处理
	MatOutWHNoTrainDetail.addFn = function() {
		var matCode = Ext.getCmp('matCode_s').getValue();
		// 验证物料编码是否为空
		if (Ext.isEmpty(matCode)) {
			MyExt.Msg.alert("请输入物料编码");
			return;
		}
		var whIdx = Ext.getCmp("whIdx_k").getValue();
		// 验证库房主键是否为空
		if (Ext.isEmpty(whIdx)) {
			MyExt.Msg.alert("请先选择领用库房");
			return;
		}
		Ext.Ajax.request({
			url : ctx + '/matTypeList!getModelByMatCode.action',
			params : { matCode: matCode, whIdx: whIdx },
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
						var count = MatOutWHNoTrainDetail.grid.store.getCount();
						if (count != 0) {
							for (var i = 0; i < count; i++) {
								var record = MatOutWHNoTrainDetail.grid.store.getAt(i);
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
						record_v.set("qty","1");
						MatOutWHNoTrainDetail.grid.store.insert(0, record_v); 
					    MatOutWHNoTrainDetail.grid.getView().refresh(); 
					    MatOutWHNoTrainDetail.grid.getSelectionModel().selectRow(0)
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
	MatOutWHNoTrainDetail.deleteFn = function() {
        var data = MatOutWHNoTrainDetail.grid.selModel.getSelections();
        if(data.length == 0 ){
        	MyExt.Msg.alert("尚未选择一条记录！");
            return;
        }
        var storeAt = MatOutWHNoTrainDetail.grid.store.indexOf(data[0]);
        var records = MatOutWHNoTrainDetail.store.getModifiedRecords();
        var count = records.length; 
        var j = storeAt + 1;
        if(count-1 == storeAt){
        	j = storeAt-1;
        }
	    MatOutWHNoTrainDetail.grid.getSelectionModel().selectRow(j);
	    for (var i = 0; i < data.length; i++){
	        MatOutWHNoTrainDetail.grid.store.remove(data[i]);
	    }
	    MatOutWHNoTrainDetail.grid.getView().refresh();
	}
	/** ************* 定义全局函数结束 ************* */

	MatOutWHNoTrainDetail.batchWin = new Ext.Window({
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
				var count = MatOutWHNoTrainDetail.grid.store.getCount();
				if (count != 0) {
					for (var i = 0; i < count; i++) {
						var record = MatOutWHNoTrainDetail.grid.store.getAt(i);
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
					record_v.set("qty","1");
					MatOutWHNoTrainDetail.grid.store.insert(0, record_v); 
				    MatOutWHNoTrainDetail.grid.getView().refresh(); 
				    MatOutWHNoTrainDetail.grid.getSelectionModel().selectRow(0)
				}
				// 添加成功后，隐藏【物料选择】 窗口
				MatOutWHNoTrainDetail.batchWin.hide();
			}
		}, {
			text:'关闭', iconCls:'closeIcon', handler: function() {
				MatOutWHNoTrainDetail.batchWin.hide();
			}
		}]
	})
	
	// 数据容器
	MatOutWHNoTrainDetail.store = new Ext.data.JsonStore({
		id:'idx', totalProperty:'totalProperty', autoLoad:true, pruneModifiedRecords: true,
		root: 'root',
		url:ctx+'/matOutWHNoTrainDetail!pageList.action',
		fields:['idx','matCode','matOutWHNoTrainIDX','matDesc','unit','qty']
	});
	// 操作掩码
    MatOutWHNoTrainDetail.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	// 行选择模式
    MatOutWHNoTrainDetail.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    // 单据明细表格
    MatOutWHNoTrainDetail.grid = new Ext.grid.EditorGridPanel({
		border: false, enableColumnMove: true, stripeRows: true, selModel: MatOutWHNoTrainDetail.sm, loadMask: true,
		clicksToEdit: 1,
		viewConfig: {forceFit: true},
        store: MatOutWHNoTrainDetail.store,
		colModel:new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
			{
	        	sortable:false, dataIndex:'idx', hidden:true, header:'idx'
	        },{
	        	sortable:false, dataIndex:'matCode', header:'物料编码'
	        },{
	        	sortable:false, dataIndex:'matOutWHNoTrainIDX', hidden:true, header:'机车外用料单主键'
	        },{	
	        	sortable:false, dataIndex:'matDesc', header:'物料描述'
	        },{	
	        	sortable:false, dataIndex:'unit', header:'计量单位'
	        },{
	        	sortable:false, dataIndex:'qty', header:'数量', editor:{
		        		maxLength: 4, 
		        		vtype: "nonNegativeInt", 
		        		allowBlank: false
					}
	        }]),
			tbar:[
				'物料编码：', {
					xtype:'textfield',id:'matCode_s',width:120,enableKeyEvents:true,listeners: {
//						keyup: function(filed, e) {
//							// 如果敲下Enter（回车键），则触发添加按钮的函数处理
//							if (e.getKey() == e.ENTER){
//								MatOutWHNoTrainDetail.addFn();
//							}
//						}
					}
				}, '&nbsp;&nbsp;', {
					text:'添加',iconCls:'addIcon',handler:MatOutWHNoTrainDetail.addFn
				}, '&nbsp;&nbsp;', {
					text:'批量添加',iconCls:'chart_attributeConfigIcon',handler:function(){
						var whIdx = Ext.getCmp("whIdx_k").getValue();
						// 验证库房主键是否为空
						if (Ext.isEmpty(whIdx)) {
							MyExt.Msg.alert("请先选择领用库房");
							return;
						}
						MatTypeList.whIdx = whIdx;
						// 打开【物料选择】 窗口
	              		MatOutWHNoTrainDetail.batchWin.show();
						// 重新加载 【物料选择】 窗口表格数据
						MatTypeList.grid.store.load();
					}
				}, '&nbsp;&nbsp;', {
				text:'删除', iconCls:'deleteIcon', handler:MatOutWHNoTrainDetail.deleteFn
				}, '&nbsp;&nbsp;', {
		           text:'刷新', iconCls:"refreshIcon", handler: function() {
		           		self.location.reload();
		           }
	           }]
    });
	MatOutWHNoTrainDetail.grid.store.on("beforeload", function(){
		var searchParams = {matOutWHNoTrainIDX: MatOutWHNoTrainDetail.matOutWHNoTrainIDX};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);  
	});
	
});