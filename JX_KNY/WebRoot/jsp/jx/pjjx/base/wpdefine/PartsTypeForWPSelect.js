

Ext.onReady(function() {
	Ext.namespace('PartsTypeForWPSelect');
	
	/** ************* 定义全局变量开始 ************* */
	PartsTypeForWPSelect.searchParams = {};
	PartsTypeForWPSelect.wPIDX = "###";
	PartsTypeForWPSelect.labelWidth = 80,
	PartsTypeForWPSelect.fieldWidth = 120,
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 【确定】按钮触发的函数处理
	PartsTypeForWPSelect.addFn = function() {
		if (!$yd.isSelectedRecord(PartsTypeForWPSelect.grid)) {
			return;
		}
		var ids = $yd.getSelectedIdx(PartsTypeForWPSelect.grid);
		
		var datas = [];
		
		for (var i = 0; i < ids.length; i++) {
			var wpUnionParts = {};
			wpUnionParts.wPIDX = PartsTypeForWPSelect.wPIDX;
			wpUnionParts.partsTypeIDX = ids[i];
			datas.push(wpUnionParts);
		}
    	// Ajax后台数据处理
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
            url: ctx + '/wPUnionParts!save.action',
            jsonData: datas,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	// 重新加载 【候选表格】
                    PartsTypeForWPSelect.grid.store.reload();
                	// 重新加载 【作业流程适用配件表格】
                    PartsTypeForWP.grid.store.reload();
                } else {
                    alertFail(result.errMsg);
                }
            }
        }));
		
	}
	/** ************* 定义全局函数结束 ************* */
	
	/** ************* 定义查询表单开始 ************* */
	PartsTypeForWPSelect.searchForm = new Ext.form.FormPanel({
		labelWidth: PartsTypeForWPSelect.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 15px',
		items:[{
			// 查询表单第1行
			baseCls: "x-plain",
			layout: 'column',
			items:[{												// 第1行第1列
				columnWidth: .3,
				layout: 'form',
				items: [{
					fieldLabel: '编号',
					name: 'specificationModelCode',
					xtype: 'textfield',
					width: PartsTypeForWPSelect.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .3,
				layout: 'form',
				items: [{
					fieldLabel: '配件名称',
					name: 'partsName',
					xtype: 'textfield',
					width: PartsTypeForWPSelect.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .3,
				layout: 'form',
				items: [{
					fieldLabel: '规格型号',
					name: 'specificationModel',
					xtype: 'textfield',
					width: PartsTypeForWPSelect.fieldWidth
				}]
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
				// 重新加载表格
				PartsTypeForWPSelect.grid.store.load();
			}
		}, {
			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
				PartsTypeForWPSelect.searchForm.getForm().reset();
				// 重新加载表格
				PartsTypeForWPSelect.grid.store.load();
			}
		}]
	});
	/** ************* 定义查询表单结束 ************* */
	
	/** ************* 定义候选表格开始 ************* */
	PartsTypeForWPSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsType!findPageListForWPSelect.action',                 //装载列表数据的请求URL
	    tbar: null,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, width: 15
		},{
			header:'编号', dataIndex:'specificationModelCode', width: 15
		},{
			header:'配件名称', dataIndex:'partsName', width: 30
		},{
			header:'规格型号', dataIndex:'specificationModel', width: 40
		},{
			header:'物料编码', dataIndex:'matCode', width: 25
		}],
		storeAutoLoad: false
	});
	PartsTypeForWPSelect.grid.un('rowdblclick', PartsTypeForWPSelect.grid.toEditFn, PartsTypeForWPSelect.grid);
	PartsTypeForWPSelect.grid.store.setDefaultSort("specificationModelCode", "ASC");
	//查询前添加过滤条件
	PartsTypeForWPSelect.grid.store.on('beforeload' , function(){
		PartsTypeForWPSelect.searchParams = PartsTypeForWPSelect.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(PartsTypeForWPSelect.searchParams);
		this.baseParams.wPIDX = PartsTypeForWPSelect.wPIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************* 定义候选表格结束 ************* */
	
	PartsTypeForWPSelect.win = new Ext.Window({
		title:"选择规格型号",
		width: 800,
		height: 500,
		modal: true,
		layout: 'border',
		closeAction: 'hide',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 120,
			border: true,
			collapsible: true,
			split: true,
			items: [PartsTypeForWPSelect.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			items: [PartsTypeForWPSelect.grid]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '添加', iconCls: 'yesIcon', handler: PartsTypeForWPSelect.addFn
		}, {
			xtype: "button", text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
});