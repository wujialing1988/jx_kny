/**
 * 
 * 检修工艺维护js
 * 
 */

Ext.onReady(function() {
	Ext.namespace('TecDefine');
	
	/** ************** 定义全局变量开始 ************** */
	TecDefine.searchParams = {};
	TecDefine.labelWidth = 100;
	TecDefine.fieldWidth = 140;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 自动设置【工艺编号】字段值
	TecDefine.setTecNo = function() {
		Ext.Ajax.request({
			url: ctx + "/codeRuleConfig!getConfigRule.action",
			params: {ruleFunction: "PJJX_TEC_NO"},
			success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
					Ext.getCmp("tecNo_k").setValue(result.rule);
				}
			},
			failure: function(response, options){
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	// 按钮【设置工艺卡】触发的函数处理
	TecDefine.setTecCardFn = function() {
		var sm = TecDefine.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			MyExt.Msg.alert("尚未选择任何记录！");
			return;
		}
		var data = sm.getSelections()[0];
		// 加载【工艺信息】
		TecCard.form.getForm().loadRecord(data);
		// 设置“配件检修工艺卡”的“检修工艺主键”字段值
		TecCard.tecIDX = data.get('idx');
		TecCard.win.show();
		TecCard.grid.store.load();
	}
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义查询表单开始 ************** */
	TecDefine.searchForm = new Ext.form.FormPanel({
		labelWidth: TecDefine.labelWidth,
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
					fieldLabel: '工艺编号',
					maxLength:30,
					name: 'tecNo',
					xtype: 'textfield',
					width: TecDefine.fieldWidth
				}]
			}, {													// 第1行第2列
				columnWidth: .33,
				layout: 'form',
				items: [{
					fieldLabel: '工艺名称',
					maxLength:50,
					name: 'tecName',
					xtype: 'textfield',
					width: TecDefine.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .33,
				layout: 'form',
				items: [{
					fieldLabel: '工艺描述',
					maxLength:500,
					name: 'tecDesc',
					xtype: 'textfield',
					width: TecDefine.fieldWidth
				}]
			}]
		}],
		buttons: [{
			xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
				// 重新加载表格
				TecDefine.grid.store.load();
			}
		}, {
			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
				TecDefine.searchForm.getForm().reset();
				// 重新加载表格
				TecDefine.grid.store.load();
			}
		}],
		buttonAlign: 'center'
	});
	/** ************** 定义查询表单结束 ************** */
	
	/** ************** 定义查询主体开始 ************** */
	TecDefine.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/tec!pageList.action',         //装载列表数据的请求URL
		saveURL: ctx + '/tec!saveOrUpdate.action',       //保存数据的请求URL
		deleteURL: ctx + '/tec!logicDelete.action',      //删除数据的请求URL
		saveWinWidth: 520,
		singleSelect: true,
		tbar:['add', {
			text: '设置工艺卡', iconCls: 'configIcon', handler: TecDefine.setTecCardFn
		}, 'delete', 'refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, width: 20, editor: { xtype:'hidden' }
		},{
			header:'工艺编号', dataIndex:'tecNo', width:10, editor:{ maxLength:30, width: TecDefine.fieldWidth, id: 'tecNo_k', allowBlank: false},
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			html = "<span><a href='#' onclick='TecDefine.setTecCardFn()'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'工艺名称', dataIndex:'tecName', width:30, editor:{ maxLength:50, width: TecDefine.fieldWidth + 220, allowBlank: false}
		},{
			header:'工艺描述', dataIndex:'tecDesc', width:60, editor:{ maxLength:500, xtype: 'textarea', width: TecDefine.fieldWidth + 220, height: 80 }
		}],
		afterShowSaveWin: function(record, rowIndex){
			TecDefine.setTecNo();
		},
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        // 隐藏编辑窗口
	        this.saveWin.hide();
	    }
	});
	// 取消双击行出现“编辑”页面的事件监听
	 TecDefine.grid.un('rowdblclick', TecDefine.grid.toEditFn, TecDefine.grid);
	
	TecDefine.grid.store.on('beforeload', function() {
		TecDefine.searchParams = TecDefine.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(TecDefine.searchParams);
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
			items: [TecDefine.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			items: [TecDefine.grid]
		}]
	});
	
});