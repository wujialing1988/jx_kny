/**
 * 
 * 记录单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 * 
 */

Ext.onReady(function() {
	Ext.namespace('Record');
	
	/** ************** 定义全局变量开始 ************** */
	Record.searchParams = {};
	Record.labelWidth = 100;
	Record.fieldWidth = 140;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 自动设置【记录单】字段值
	Record.setTecNo = function() {
		Ext.Ajax.request({
			url: ctx + "/codeRuleConfig!getConfigRule.action",
			params: {ruleFunction: "PJJX_RECORD_NO"},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
					Ext.getCmp("recordNo_k").setValue(result.rule);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	// 按钮【设置记录卡】触发的函数处理
	Record.setRecordFn = function() {
		var sm = Record.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			MyExt.Msg.alert("尚未选择任何记录！");
			return;
		}
		var data = sm.getSelections()[0];
		// 加载【工艺信息】
		RecordCard.form.getForm().loadRecord(data);
		// 设置“配件检修工艺卡”的“检修工艺主键”字段值
		RecordCard.recordIDX = data.get('idx');
		RecordCard.win.show();
		RecordCard.grid.store.load();
	}
	// 按钮【定制打印模块】触发的函数处理
	Record.definePrinterFn = function() {
		var sm = Record.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			MyExt.Msg.alert("尚未选择任何记录！");
			return;
		}
		// 设置报表业务关联 - 业务主键
		ReportMgr.businessIDX = sm.getSelections()[0].get('idx');
		// 设置报表打印模板 - 报表部署目录
		ReportMgr.deployCatalog = "pjjx.record";
		// 显示打印模板定制窗口
		ReportMgr.win.show();
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义查询表单开始 ************** */
	Record.searchForm = new Ext.form.FormPanel({
		labelWidth: Record.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 15px',
		items:[{
			// 查询表单第1行
			baseCls: "x-plain",
			layout: 'column',
			items:[{												// 第1行第1列
				columnWidth: .33,
				layout: 'form',
				items: [{
					fieldLabel: '编号',
					maxLength:30,
					name: 'recordNo',
					xtype: 'textfield',
					width: Record.fieldWidth
				}]
			}, {													// 第1行第2列
				columnWidth: .33,
				layout: 'form',
				items: [{
					fieldLabel: '名称',
					maxLength:50,
					name: 'recordName',
					xtype: 'textfield',
					width: Record.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .33,
				layout: 'form',
				items: [{
					fieldLabel: '描述',
					maxLength:500,
					name: 'recordDesc',
					xtype: 'textfield',
					width: Record.fieldWidth
				}]
			}]
		}],
		buttons: [{
			xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
				var form = Record.searchForm.getForm();
				// 重新加载表格
				Record.grid.store.load();
			}
		}, {
			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
				Record.searchForm.getForm().reset();
				// 重新加载表格
				Record.grid.store.load();
			}
		}],
		buttonAlign: 'center'
	});
	/** ************** 定义查询表单结束 ************** */
	
	/** ************** 定义查询主体开始 ************** */
	Record.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/record!pageList.action',         //装载列表数据的请求URL
		saveURL: ctx + '/record!saveOrUpdate.action',       //保存数据的请求URL
		deleteURL: ctx + '/record!logicDelete.action',      //删除数据的请求URL
		saveWinWidth: 520,
		singleSelect: true,
		tbar:['add', {
			text: '设置记录卡', iconCls: 'configIcon', handler: Record.setRecordFn
		}, 'delete', {
			text: '定制打印模板', iconCls: 'page_edit_1Icon', handler: Record.definePrinterFn
		}, 'refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, width:30, editor: { xtype:'hidden' }
		},{
			header:'编号', dataIndex:'recordNo', width:15, editor:{ maxLength:30, width: Record.fieldWidth, id: 'recordNo_k', allowBlank: false},
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			html = "<span><a href='#' onclick='Record.setRecordFn()'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'名称', dataIndex:'recordName', width:40, editor:{ maxLength:50, width: Record.fieldWidth + 220, allowBlank: false}
		},{
			header:'描述', dataIndex:'recordDesc', editor:{ maxLength:500, xtype: 'textarea', width: Record.fieldWidth + 220, height: 80 }
		}],
		afterShowSaveWin: function(record, rowIndex){
			Record.setTecNo();
		},
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        // 隐藏编辑窗口
	        this.saveWin.hide();
	    }
	});
	// 取消双击行出现“编辑”页面的事件监听
//	 Record.grid.un('rowdblclick', Record.grid.toEditFn, Record.grid);
	
	Record.grid.store.on('beforeload', function() {
		Record.searchParams = Record.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(Record.searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************** 定义查询主体结束 ************** */
	
	// 页面自适应布局
	new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 120,
			border: true,
			collapsible: true,
			items: [Record.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			height: 200,
			items: [Record.grid]
		}]
	});
	
});