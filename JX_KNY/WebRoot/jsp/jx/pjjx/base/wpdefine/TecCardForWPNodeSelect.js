

Ext.onReady(function() {
	Ext.namespace('TecCardForWPNodeSelect');
	
	/** ************* 定义全局变量开始 ************* */
	TecCardForWPNodeSelect.searchParams = {};
	TecCardForWPNodeSelect.wPIDX = "###";
	TecCardForWPNodeSelect.wPNodeIDX = "###";
	TecCardForWPNodeSelect.labelWidth = 80,
	TecCardForWPNodeSelect.fieldWidth = 120,
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 【确定】按钮触发的函数处理
	TecCardForWPNodeSelect.addFn = function() {
		if (!$yd.isSelectedRecord(TecCardForWPNodeSelect.grid)) {
			return;
		}
		var ids = $yd.getSelectedIdx(TecCardForWPNodeSelect.grid);
		
		var datas = [];
		
		for (var i = 0; i < ids.length; i++) {
			var wPNodeUnionTecCard = {};
			wPNodeUnionTecCard.wPNodeIDX = TecCardForWPNodeSelect.wPNodeIDX;
			wPNodeUnionTecCard.tecCardIDX = ids[i];
			datas.push(wPNodeUnionTecCard);
		}
    	// Ajax后台数据处理
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
            url: ctx + '/wPNodeUnionTecCard!save.action',
            jsonData: datas,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	// 重新加载 【候选表格】
                    TecCardForWPNodeSelect.grid.store.reload();
                	// 重新加载 【作业节点所挂工艺卡】
                    TecCardForWPNode.grid.store.reload();
                } else {
                    alertFail(result.errMsg);
                }
            }
        }));
		
	}
	/** ************* 定义全局函数结束 ************* */
	
	/** ************* 定义查询表单开始 ************* */
	TecCardForWPNodeSelect.searchForm = new Ext.form.FormPanel({
		labelWidth: TecCardForWPNodeSelect.labelWidth,
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
					fieldLabel: '工艺名称',
					name: 'tecName',
					xtype: 'textfield',
					width: TecCardForWPNodeSelect.fieldWidth
				}]
			}, {
				columnWidth: .3,
				layout: 'form',
				items: [{
					fieldLabel: '编号',
					name: 'tecCardNo',
					xtype: 'textfield',
					width: TecCardForWPNodeSelect.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .3,
				layout: 'form',
				items: [{
					fieldLabel: '描述',
					name: 'tecCardDesc',
					xtype: 'textfield',
					width: TecCardForWPNodeSelect.fieldWidth
				}]
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
				TecCardForWPNodeSelect.searchParams = TecCardForWPNodeSelect.searchForm.getForm().getValues();
				// 重新加载表格
				TecCardForWPNodeSelect.grid.store.load();
			}
		}, {
			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
				TecCardForWPNodeSelect.searchForm.getForm().reset();
				TecCardForWPNodeSelect.searchParams = {};
				// 重新加载表格
				TecCardForWPNodeSelect.grid.store.load();
			}
		}]
	});
	/** ************* 定义查询表单结束 ************* */
	
	/** ************* 定义候选表格开始 ************* */
	TecCardForWPNodeSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/tecCard!findPageListForWPNodeSelect.action',                 //装载列表数据的请求URL
	    tbar: null,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden: true, width: 15
		},{
			header:'工艺名称 - 顺序', dataIndex:'seqNo', width: 40, hidden: true, renderer: function(value, metadata, record, rowIndex, colIndex, store) {
				return record.data.tecName + " - " + value;
			}
		},{
			header:'工艺卡编号', dataIndex:'tecCardNo', width: 10
		},{
			header:'工艺卡描述', dataIndex:'tecCardDesc', width: 50
		},{
			header:'工艺名称', dataIndex:'tecName', width: 40, hidden: false
		}],
		storeAutoLoad: false
	});
	TecCardForWPNodeSelect.grid.un('rowdblclick', TecCardForWPNodeSelect.grid.toEditFn, TecCardForWPNodeSelect.grid);
	TecCardForWPNodeSelect.grid.store.setDefaultSort("seqNo", "ASC");
	//查询前添加过滤条件
	TecCardForWPNodeSelect.grid.store.on('beforeload' , function(){
		var searchParams = MyJson.deleteBlankProp(TecCardForWPNodeSelect.searchParams);
		this.baseParams.wPIDX = TecCardForWPNodeSelect.wPIDX;
		this.baseParams.wPNodeIDX = TecCardForWPNodeSelect.wPNodeIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************* 定义候选表格结束 ************* */
	
	TecCardForWPNodeSelect.win = new Ext.Window({
		title:"选择检修工艺卡",
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
			items: [TecCardForWPNodeSelect.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			items: [TecCardForWPNodeSelect.grid]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '添加', iconCls: 'yesIcon', handler: TecCardForWPNodeSelect.addFn
		}, {
			xtype: "button", text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
});