/**
 * 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TecCardMat');                       // 定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	TecCardMat.tecCardIDX = "###";
	TecCardMat.labelWidth = 80;
	TecCardMat.fieldWidth = 140;
	TecCardMat.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	/** ************** 定义全局变量结束 ************** */
	
	TecCardMat.batchWin = new Ext.Window({
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
			text:'添加', iconCls:'addIcon', handler: function() {
				var sm = MatTypeList.grid.getSelectionModel();
				if (sm.getCount() <= 0) {
					MyExt.Msg.alert("尚未选择一条记录");
					return;
				}
				// 已选择的待添加的物料信息
				var selectedRecrods = sm.getSelections();
				// 检验已选择的待添加的物料信息是否已经被添加
				var count = TecCardMat.grid.store.getCount();
				if (count != 0) {
					for (var i = 0; i < count; i++) {
						var record = TecCardMat.grid.store.getAt(i);
						for (var j = 0; j < selectedRecrods.length; j++) {
							if (record.get('matCode') == selectedRecrods[j].get('matCode')) {
								MyExt.Msg.alert("物料编码<font color='red'>【" + record.get('matCode') + "】</font>已存在列表中，请不要重复添加");
   	          					return ;
							}
						}
					}
				}
				var datas = [];
				for (var k = 0; k < selectedRecrods.length; k++) {
					var data = {};
					data.tecCardIDX = Ext.getCmp('idx_c').getValue();		// 检修工艺卡idx主键
					data.matCode = selectedRecrods[k].get('matCode');		// 物料编码
					data.matDesc = selectedRecrods[k].get('matDesc');		// 物料描述
					data.unit = selectedRecrods[k].get('unit');				// 计量单位
					data.qty = 1;											// 数量默认为”1“
					datas.push(data);
				}
				TecCardMat.loadMask.show();
		        Ext.Ajax.request({
		            url: ctx + '/tecCardMat!saveTecCardMats.action',
		            jsonData: datas,
		            success: function(response, options){
		              	TecCardMat.loadMask.hide();
		                var result = Ext.util.JSON.decode(response.responseText);
		                if (result.errMsg == null) {
		                    alertSuccess();
							// 添加成功后，隐藏【物料选择】 窗口
							// TecCardMat.batchWin.hide();
							// 重新加载 【物料选择】 窗口表格数据
							MatTypeList.grid.store.reload();
							// 重新加载表格数据
							TecCardMat.grid.store.reload();
		                } else {
		                    alertFail(result.errMsg);
		                }
		            },
		            failure: function(response, options){
		                TecCardMat.loadMask.hide();
		                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		            }
		        });
			}
		}, {
			text:'关闭', iconCls:'closeIcon', handler: function() {
				TecCardMat.batchWin.hide();
			}
		}],
		listeners: {
			show: function(window) {
				MatTypeList.tecCardIDX = TecCardMat.tecCardIDX;
				// 重新加载 【物料选择】 窗口表格数据
				MatTypeList.grid.store.load();
			}
		}
	})
	
	TecCardMat.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + '/tecCardMat!pageList.action',                 // 装载列表数据的请求URL
	    saveURL: ctx + '/tecCardMat!saveOrUpdate.action',             // 保存数据的请求URL
	    deleteURL: ctx + '/tecCardMat!logicDelete.action',            // 删除数据的请求URL
	    tbar: ['search', 'add', 'delete'],
	    storeAutoLoad:false,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'检修工艺卡主键', dataIndex:'tecCardIDX', hidden:true, editor:{disabled: true}, searcher: {hidden: true}
		},{
			header:'物料编码', dataIndex:'matCode', editor:{disabled: true}
		},{
			header:'物料描述', dataIndex:'matDesc', editor:{disabled: true}, width: 240
		},{
			header:'数量', dataIndex:'qty', editor:{ maxLength:4, allowBlank:false, vtype:'positiveInt' }, searcher: {hidden: true}
		},{
			header:'计量单位', dataIndex:'unit', editor:{disabled: true}, searcher: {hidden: true}
		}],
		addButtonFn: function() {
			// 打开【物料选择】 窗口
      		TecCardMat.batchWin.show();
		},
		searchFn: function(searchParam){
			searchParam.tecCardIDX = TecCardMat.tecCardIDX;
	        this.store.load({
	            params: { entityJson: Ext.util.JSON.encode(searchParam) }       
	        });	
	    }
	});
	// 设置默认排序
	TecCardMat.grid.store.setDefaultSort('matCode', 'ASC');
	TecCardMat.grid.store.on('beforeload', function(){
		var searchParams = {tecCardIDX: TecCardMat.tecCardIDX};
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
});