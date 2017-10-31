/**
 * 物料消耗记录 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('MatTypeList');        		              // 定义命名空间
	Ext.namespace('PartsRdpExpendMat');                       // 定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpExpendMat.searchParams = {};
	PartsRdpExpendMat.rdpIDX = "###";				// 作业主键
	PartsRdpExpendMat.rdpTecCardIDX = "####";		// 工艺单主键
	PartsRdpExpendMat.tecCardIDX = "####";			// 工艺卡主键
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 确认添加物料的函数处理
	MatTypeList.addExpendMatFn =  function() {
		var sm = MatTypeList.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			MyExt.Msg.alert("尚未选择一条记录");
			return;
		}
		// 已选择的待添加的物料信息
		var records = sm.getSelections();
		// 检验已选择的待添加的物料信息是否已经被添加
		var count = PartsRdpExpendMat.grid.store.getCount();
		if (count != 0) {
			for (var i = 0; i < count; i++) {
				var record = PartsRdpExpendMat.grid.store.getAt(i);
				for (var j = 0; j < records.length; j++) {
					if (record.get('matCode') == records[j].get('matCode')) {
						MyExt.Msg.alert("物料<font color='red'>【" + record.get('matCode') + "】</font>已存在列表中，请不要重复添加！");
   	          			return ;
					}
				}
			}
		}
		// 遍历数组，完善对特征字段的值设置
		var datas = [];
		for (var k = 0; k < records.length; k++) {
			// 设置【物料消耗记录】的“作业主键”字段值
			records[k].data.rdpIDX = PartsRdpExpendMat.rdpIDX;
			records[k].data.rdpTecCardIDX = PartsRdpExpendMat.rdpTecCardIDX;
			delete records[k].data.idx;
			delete records[k].data.tecCardIDX;
			datas.push(records[k].data);
		}
		
		// Ajax后台数据处理
		self.loadMask.show();
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: PartsRdpExpendMat.grid,
        	url: ctx + '/partsRdpExpendMat!saveExpendMats.action',
        	jsonData:datas
        }));
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义物料选择表格开始 ************** */
	
	MatTypeList.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/tecCardMat!queryPageList.action',                 // 装载列表数据的请求URL
	    tbar: null,
	    storeAutoLoad:false,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'检修工艺卡主键', dataIndex:'tecCardIDX', hidden:true
		},{
			header:'物料编码', dataIndex:'matCode'
		},{
			header:'物料描述', dataIndex:'matDesc', width: 240
		},{
			header:'数量', dataIndex:'qty'
		},{
			header:'单价', dataIndex:'price'
		},{
			header:'计量单位', dataIndex:'unit'
		}],
		
		// 双击表格行进行快速添加消耗物料
		toEditFn: MatTypeList.addExpendMatFn
	});
	
	// 列表数据容器加载时的过滤条件设置
	MatTypeList.grid.store.on('beforeload', function() {
		var searchParams = Ext.getCmp('searchForm_Mat').getForm().getValues();
		searchParams.tecCardIDX = PartsRdpExpendMat.tecCardIDX;					// 工艺卡主键
		searchParams = MyJson.deleteBlankProp(searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	/** ************** 定义物料选择表格结束 ************** */
	
	/** ************** 定义物料选择窗口开始 ************** */
	PartsRdpExpendMat.batchWin = new Ext.Window({
		title:"物料选择",
		width:605, height:350,
		layout:"border",
		closeAction:"hide",
		plain:true,
		defaults:{layout:"fit"},
		items:[{
			region:"center",
			items: MatTypeList.grid
			
		}, {
			region:"north", baseCls:"x-plain", style:"padding:10px;",
			height:43,
			items:[{
				// 查询表单
				xtype:"form", id: "searchForm_Mat", layout:"column", baseCls:"x-plain", labelWidth: 60, 
				defaults:{
					columnWidth:0.355, layout:"form", baseCls:"x-plain",
					defaults: {xtype:"textfield", width:120}
				},
				items:[{
					items:[{
						fieldLabel:"物料编码", id:"matCode"
					}]
				}, {
					items:[{
						fieldLabel:"物料描述", id:"matDesc"
					}]
				}, {
					columnWidth:0.05
				}, {
					columnWidth:0.12,
					autoWidth:false,
					bodyStyle:"",
					items:[{
						xtype:"button", width:40,
						text:"查询", iconCls:"searchIcon", handler: function() {
							// 重新加载 【物料选择】 窗口表格数据
							MatTypeList.grid.store.load();
						}
					}]
				}, {
					columnWidth:0.12,
					items:[{
						xtype:"button", width:40,
						text:"重置", iconCls:"resetIcon", handler: function() {
							// 重置查询表单
							Ext.getCmp('searchForm_Mat').getForm().reset();
							// 重新加载 【物料选择】 窗口表格数据
							MatTypeList.grid.store.load();
						}
					}]
				}]
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text:'添加', iconCls:'addIcon', handler: MatTypeList.addExpendMatFn
		}, {
			text:'关闭', iconCls:'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	})
	/** ************** 定义物料选择窗口结束 ************** */
	
	/** ************** 定义物料消耗表格开始 ************** */
	PartsRdpExpendMat.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + '/partsRdpExpendMat!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/partsRdpExpendMat!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/partsRdpExpendMat!logicDelete.action',            //删除数据的请求URL
	    storeAutoLoad: false,
	    tbar:['add', 'delete'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'作业主键', dataIndex:'rdpIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'工艺卡主键', dataIndex:'rdpTecCardIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'物料编码', dataIndex:'matCode',width: 30, editor:{  maxLength:50, disabled: true }
		},{
			header:'物料描述', dataIndex:'matDesc',width: 100, editor:{  maxLength:100, disabled: true }
		},{
			header:'消耗数量', dataIndex:'qty',width: 30, editor:{ xtype:'numberfield', vtype:'positiveInt', maxLength:6}
		},{
			header:'单位', dataIndex:'unit',width: 20, editor:{  maxLength:20, disabled: true }
		},{
			header:'单价', dataIndex:'price',width: 20, editor:{ xtype:'numberfield', maxLength:6, disabled: true }
		},{
			header:'消耗人ID', dataIndex:'handleEmpId', hidden:true, editor:{disabled: true }
		},{
			header:'消耗人', dataIndex:'handleEmpName',width: 30, editor:{disabled: true }
		},{
			header:'消耗班组', dataIndex:'handleOrgId', hidden:true, editor:{disabled: true }
		},{
			header:'消耗班组名称', dataIndex:'handleOrgName', hidden:true, editor:{disabled: true }
		},{
			header:'消耗班组序列', dataIndex:'handleOrgSeq', hidden:true, editor:{disabled: true }
		}],
		// 新增按钮的事件函数处理
		addButtonFn: function() {
			// 显示物料选择窗口
			PartsRdpExpendMat.batchWin.show();
			// 重新加载 【物料选择】 窗口表格数据
			MatTypeList.grid.store.load();
		}
	});
	
	PartsRdpExpendMat.grid.store.setDefaultSort('matCode', 'ASC');
	// 列表数据容器加载时的过滤条件设置
	PartsRdpExpendMat.grid.store.on('beforeload', function() {
		var searchParams = PartsRdpExpendMat.searchParams;
		searchParams.rdpIDX = PartsRdpExpendMat.rdpIDX;							// 作业主键
		searchParams.rdpTecCardIDX = PartsRdpExpendMat.rdpTecCardIDX;					// 工艺卡主键
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************** 定义物料消耗表格结束 ************** */
	
});