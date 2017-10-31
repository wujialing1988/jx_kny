

Ext.onReady(function() {
	Ext.namespace('TecForWPSelect');
	
	/** ************* 定义全局变量开始 ************* */
	TecForWPSelect.searchParams = {};
	TecForWPSelect.wPIDX = "###";
	TecForWPSelect.labelWidth = 80,
	TecForWPSelect.fieldWidth = 120,
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 【确定】按钮触发的函数处理
	TecForWPSelect.addFn = function() {
		if (!$yd.isSelectedRecord(TecForWPSelect.grid)) {
			return;
		}
		var ids = $yd.getSelectedIdx(TecForWPSelect.grid);
		
		var datas = [];
		
		for (var i = 0; i < ids.length; i++) {
			var wpUnionTec = {};
			wpUnionTec.wPIDX = TecForWPSelect.wPIDX;
			wpUnionTec.tecIDX = ids[i];
			datas.push(wpUnionTec);
		}
    	// Ajax后台数据处理
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
            url: ctx + '/wPUnionTec!save.action',
            jsonData: datas,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	// 重新加载 【候选表格】
                    TecForWPSelect.grid.store.reload();
                	// 重新加载 【作业流程所用工艺表格】
                    TecForWP.grid.store.reload();
                } else {
                    alertFail(result.errMsg);
                }
            }
        }));
		
	}
	/** ************* 定义全局函数结束 ************* */
	
	/** ************* 定义查询表单开始 ************* */
	TecForWPSelect.searchForm = new Ext.form.FormPanel({
		labelWidth: TecForWPSelect.labelWidth,
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
					name: 'tecNo',
					xtype: 'textfield',
					width: TecForWPSelect.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .3,
				layout: 'form',
				items: [{
					fieldLabel: '名称',
					name: 'tecName',
					xtype: 'textfield',
					width: TecForWPSelect.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .3,
				layout: 'form',
				items: [{
					fieldLabel: '描述',
					name: 'tecDesc',
					xtype: 'textfield',
					width: TecForWPSelect.fieldWidth
				}]
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
				// 重新加载表格
				TecForWPSelect.grid.store.load();
			}
		}, {
			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
				TecForWPSelect.searchForm.getForm().reset();
				// 重新加载表格
				TecForWPSelect.grid.store.load();
			}
		}]
	});
	/** ************* 定义查询表单结束 ************* */
	
	/** ************* 定义候选表格开始 ************* */
	TecForWPSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/tec!findPageListForWPSelect.action',                 //装载列表数据的请求URL
	    tbar: null,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden: true, width: 15
		},{
			header:'编号', dataIndex:'tecNo', width: 20
		},{
			header:'名称', dataIndex:'tecName', width: 30
		},{
			header:'描述', dataIndex:'tecDesc', width: 50
		}],
		storeAutoLoad: false
	});
	TecForWPSelect.grid.un('rowdblclick', TecForWPSelect.grid.toEditFn, TecForWPSelect.grid);
	TecForWPSelect.grid.store.setDefaultSort("tecNo", "ASC");
	//查询前添加过滤条件
	TecForWPSelect.grid.store.on('beforeload' , function(){
		TecForWPSelect.searchParams = TecForWPSelect.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(TecForWPSelect.searchParams);
		this.baseParams.wPIDX = TecForWPSelect.wPIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************* 定义候选表格结束 ************* */
	
	TecForWPSelect.win = new Ext.Window({
		title:"选择检修工艺",
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
			items: [TecForWPSelect.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			items: [TecForWPSelect.grid]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '添加', iconCls: 'yesIcon', handler: TecForWPSelect.addFn
		}, {
			xtype: "button", text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
});