

Ext.onReady(function() {
	Ext.namespace('RecordForWPSelect');
	
	/** ************* 定义全局变量开始 ************* */
	RecordForWPSelect.searchParams = {};
	RecordForWPSelect.wPIDX = "###";
	RecordForWPSelect.labelWidth = 80,
	RecordForWPSelect.fieldWidth = 120,
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 【确定】按钮触发的函数处理
	RecordForWPSelect.addFn = function() {
		if (!$yd.isSelectedRecord(RecordForWPSelect.grid)) {
			return;
		}
		var ids = $yd.getSelectedIdx(RecordForWPSelect.grid);
		
		var datas = [];
		
		for (var i = 0; i < ids.length; i++) {
			var wpUnionRecord = {};
			wpUnionRecord.wPIDX = RecordForWPSelect.wPIDX;
			wpUnionRecord.recordIDX = ids[i];
			datas.push(wpUnionRecord);
		}
    	// Ajax后台数据处理
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
            url: ctx + '/wPUnionRecord!save.action',
            jsonData: datas,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	// 重新加载 【候选表格】
                    RecordForWPSelect.grid.store.reload();
                	// 重新加载 【作业流程所用记录单表格】
                    RecordForWP.grid.store.reload();
                } else {
                    alertFail(result.errMsg);
                }
            }
        }));
		
	}
	/** ************* 定义全局函数结束 ************* */
	
	/** ************* 定义查询表单开始 ************* */
	RecordForWPSelect.searchForm = new Ext.form.FormPanel({
		labelWidth: RecordForWPSelect.labelWidth,
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
					name: 'recordNo',
					xtype: 'textfield',
					width: RecordForWPSelect.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .3,
				layout: 'form',
				items: [{
					fieldLabel: '名称',
					name: 'recordName',
					xtype: 'textfield',
					width: RecordForWPSelect.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .3,
				layout: 'form',
				items: [{
					fieldLabel: '描述',
					name: 'recordDesc',
					xtype: 'textfield',
					width: RecordForWPSelect.fieldWidth
				}]
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
				// 重新加载表格
				RecordForWPSelect.grid.store.load();
			}
		}, {
			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
				RecordForWPSelect.searchForm.getForm().reset();
				// 重新加载表格
				RecordForWPSelect.grid.store.load();
			}
		}]
	});
	/** ************* 定义查询表单结束 ************* */
	
	/** ************* 定义候选表格开始 ************* */
	RecordForWPSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/record!findPageListForWPSelect.action',                 //装载列表数据的请求URL
	    tbar: null,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden: true, width: 15
		},{
			header:'编号', dataIndex:'recordNo', width: 20
		},{
			header:'名称', dataIndex:'recordName', width: 30
		},{
			header:'描述', dataIndex:'recordDesc', width: 50
		}],
		storeAutoLoad: false
	});
	RecordForWPSelect.grid.un('rowdblclick', RecordForWPSelect.grid.toEditFn, RecordForWPSelect.grid);
	RecordForWPSelect.grid.store.setDefaultSort("recordNo", "ASC");
	//查询前添加过滤条件
	RecordForWPSelect.grid.store.on('beforeload' , function(){
		RecordForWPSelect.searchParams = RecordForWPSelect.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(RecordForWPSelect.searchParams);
		this.baseParams.wPIDX = RecordForWPSelect.wPIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************* 定义候选表格结束 ************* */
	
	RecordForWPSelect.win = new Ext.Window({
		title:"选择检修记录单",
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
			items: [RecordForWPSelect.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			items: [RecordForWPSelect.grid]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '添加', iconCls: 'yesIcon', handler: RecordForWPSelect.addFn
		}, {
			xtype: "button", text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
});