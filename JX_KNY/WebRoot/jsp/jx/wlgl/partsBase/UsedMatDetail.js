/**
 * 常用物料清单明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('UsedMatDetail');                       //定义命名空间
	
	/** ****************** 定义全局变量开始 ****************** */
	UsedMatDetail.labelWidth = 100;
	UsedMatDetail.fieldWidth = 240;
	UsedMatDetail.usedMatIdx = "###",
	/** ****************** 定义全局变量结束 ****************** */
	
	/** **************** 定义清单信息表单开始 ***************** */
	UsedMatDetail.form = new Ext.form.FormPanel({
		align: "center", defaultType: "textfield",
		border: false, style: "padding:10px", labelWidth: UsedMatDetail.labelWidth,
		items: [{
			id:"usedMatName_d",
			name:"usedMatName",
			fieldLabel:"清单名称", 
			allowBlank: false, 
			maxLength: 50, 
			width: UsedMatDetail.fieldWidth 
		},{
			id:"usedMatDesc_d",
			name:"usedMatDesc",
			fieldLabel:"清单描述",
			maxLength: 200, 
			anchor: '80%'
		},{
			id:"idx_d",
			name:'idx',
			fieldLabel:"idx主键",
			xtype:'textfield'
		}]
	});
	/** ***************** 定义清单信息表单结束 **************** */
	
	/** ****************** 定义全局函数开始 ****************** */
	// 【添加】按钮触发的函数处理
	UsedMatDetail.addFn = function() {
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
						var count = UsedMatDetail.grid.store.getCount();
						if (count != 0) {
							for (var i = 0; i < count; i++) {
								var record = UsedMatDetail.grid.store.getAt(i);
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
						UsedMatDetail.grid.store.insert(0, record_v); 
					    UsedMatDetail.grid.getView().refresh(); 
					    UsedMatDetail.grid.getSelectionModel().selectRow(0)
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
	UsedMatDetail.deleteFn = function() {
	    var data = UsedMatDetail.grid.selModel.getSelections();
	    if(data.length == 0 ){
	    	MyExt.Msg.alert("尚未选择一条记录！");
	        return;
	    }
	    // 第一个删除项的索引号
	    var storeAt = UsedMatDetail.grid.store.indexOf(data[0]);
	    // 从数据容器中移除已选择的记录项
	    for (var i = 0; i < data.length; i++){
	        UsedMatDetail.grid.store.remove(data[i]);
	    }
	    // 获取删除后数据容器的最大索引值
	    var maxIndex = UsedMatDetail.grid.store.getCount() - 1;
	    if (maxIndex >= 0) {
	    	if (maxIndex >= storeAt) {
			    UsedMatDetail.grid.getSelectionModel().selectRow(storeAt);
	    	} else {
			    UsedMatDetail.grid.getSelectionModel().selectRow(maxIndex);
	    	}
	    }
	    // 刷新视图
	    UsedMatDetail.grid.getView().refresh();
	}
	/** ****************** 定义全局函数结束 ****************** */
	
	/** ****************** 定义物料选择窗口开始 ****************** */
	UsedMatDetail.batchWin = new Ext.Window({
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
				var count = UsedMatDetail.grid.store.getCount();
				if (count != 0) {
					for (var i = 0; i < count; i++) {
						var record = UsedMatDetail.grid.store.getAt(i);
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
					UsedMatDetail.grid.store.insert(0, record_v); 
				    UsedMatDetail.grid.getView().refresh(); 
				    UsedMatDetail.grid.getSelectionModel().selectRow(0)
				}
				// 添加成功后，隐藏【物料选择】 窗口
				UsedMatDetail.batchWin.hide();
			}
		}, {
			text:'关闭', iconCls:'closeIcon', handler: function() {
				UsedMatDetail.batchWin.hide();
			}
		}]
	})
	/** ****************** 定义物料选择窗口结束 ****************** */
	
	/** ****************** 定义清单明细表格结束 ****************** */
	// 数据容器
	UsedMatDetail.store = new Ext.data.JsonStore({
		storeId:'idx', totalProperty:'totalProperty', autoLoad:false,
		pruneModifiedRecords: true,  // True表示为，每次Store加载后，清除所有修改过的记录信息
		root: 'root',
		url:ctx+'/usedMatDetail!pageList.action',
		fields:['idx','usedMatIdx','matCode','matDesc','unit'],
		listeners: {
			"beforeload": function(){
				this.baseParams.entityJson = Ext.util.JSON.encode({usedMatIdx: UsedMatDetail.usedMatIdx});
			}
		}
	});
	// 默认以“物料描述”升序排序
	UsedMatDetail.store.setDefaultSort('matCode', 'ASC');
	// 行选择模式
    UsedMatDetail.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    // 单据明细表格
    UsedMatDetail.grid = new Ext.grid.EditorGridPanel({
		border: false, enableColumnMove: true, stripeRows: true, selModel: UsedMatDetail.sm, loadMask: true,
		viewConfig: {forceFit: true},
        store: UsedMatDetail.store,
		colModel:new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
			UsedMatDetail.sm,
			{
	        	sortable:false, dataIndex:'idx', hidden:true, header:'idx'
	        },{
	        	sortable:false, dataIndex:'usedMatIdx', hidden:true, header:'清单idx主键'
	        },{
	        	sortable:false, dataIndex:'matCode', header:'物料编码'
	        },{	
	        	sortable:false, dataIndex:'matDesc', header:'物料描述'
	        },{	
	        	sortable:false, dataIndex:'unit', header:'计量单位'
	        }]),
			tbar:[
				'物料编码：', {
				xtype:'textfield',id:'matCode_s',width:120
			}, '&nbsp;&nbsp;', {
				text:'添加',iconCls:'addIcon',handler:UsedMatDetail.addFn
			}, '&nbsp;&nbsp;', {
				text:'批量添加',iconCls:'chart_attributeConfigIcon',handler:function(){
					// 打开【物料选择】 窗口
	          		UsedMatDetail.batchWin.show();
					// 重新加载 【物料选择】 窗口表格数据
					MatTypeList.grid.store.load();
				}
			}, '&nbsp;&nbsp;', {
			text:'删除', iconCls:'deleteIcon', handler:UsedMatDetail.deleteFn
			}, '&nbsp;&nbsp;']
    });
	/** ****************** 定义清单明细表格开始 ****************** */
	
	/** ****************** 定义清单维护窗口开始 ****************** */
	UsedMatDetail.detailWin = new Ext.Window({
		title:"新增",
		width:748,
		height:549,
		layout:"border",
		closeAction:"hide",
		items:[
			{
				region : 'center',
				title: '清单明细',
				bodyBorder: false,
		        layout: "fit",
		        items:[ UsedMatDetail.grid ]
			},
			{
				height: 105,layout: "fit",
				region : 'north',frame:true,
				title: "清单信息",
		        bodyBorder: false,
		        autoScroll : true,
		        items:[ UsedMatDetail.form ]
			}
		], 
		buttonAlign:"center",
		buttons: [{  
			text:"保存", iconCls: "saveIcon", handler: function(){
				UsedMatDetail.saveFun();
			}
		},{
			text: "关闭", iconCls: "closeIcon",handler: function(){
             	UsedMatDetail.detailWin.hide(); 
			}
		}]
	});
	// 【保存】按钮触发的函数处理
	UsedMatDetail.saveFun = function(){
		var form = UsedMatDetail.form.getForm();
	    if (!form.isValid()) return;
	    var matData = form.getValues();
	    matData = MyJson.deleteBlankProp(matData);
	    var store = UsedMatDetail.grid.store;
		var count = store.getCount();
		var datas = new Array();
		if(count == 0){
			MyExt.Msg.alert("请添加明细！");
			return ;
		}
		for (var i = 0; i < count; i++) {
			var record=UsedMatDetail.grid.store.getAt(i);
			datas.push(record.data);
		}
        Ext.Ajax.request({
            url: ctx + '/usedMat!saveUsedMatAndDetail.action',
            jsonData: datas,
            params : {matData : Ext.util.JSON.encode(matData)},
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    UsedMatDetail.detailWin.hide();
                    UsedMat.grid.store.reload();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	}
	/** ****************** 定义清单维护窗口结束 ****************** */
	
});