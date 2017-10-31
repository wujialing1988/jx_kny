

Ext.onReady(function() {
	Ext.namespace('RecordCardForWPNodeSelect');
	
	/** ************* 定义全局变量开始 ************* */
	RecordCardForWPNodeSelect.searchParams = {};
	RecordCardForWPNodeSelect.wPIDX = "###";
	RecordCardForWPNodeSelect.wPNodeIDX = "###";
	RecordCardForWPNodeSelect.labelWidth = 80,
	RecordCardForWPNodeSelect.fieldWidth = 120,
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 【确定】按钮触发的函数处理
	RecordCardForWPNodeSelect.addFn = function() {
		if (!$yd.isSelectedRecord(RecordCardForWPNodeSelect.grid)) {
			return;
		}
		var ids = $yd.getSelectedIdx(RecordCardForWPNodeSelect.grid);
		
		var datas = [];
		
		for (var i = 0; i < ids.length; i++) {
			var wPNodeUnionRecordCard = {};
			wPNodeUnionRecordCard.wPNodeIDX = RecordCardForWPNodeSelect.wPNodeIDX;
			wPNodeUnionRecordCard.recordCardIDX = ids[i];
			datas.push(wPNodeUnionRecordCard);
		}
    	// Ajax后台数据处理
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
            url: ctx + '/wPNodeUnionRecordCard!save.action',
            jsonData: datas,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	// 重新加载 【候选表格】
                    RecordCardForWPNodeSelect.grid.store.reload();
                	// 重新加载 【作业节点所挂记录卡】
                    RecordCardForWPNode.grid.store.reload();
                } else {
                    alertFail(result.errMsg);
                }
            }
        }));
		
	}
	/** ************* 定义全局函数结束 ************* */
	
	/** ************* 定义查询表单开始 ************* */
	RecordCardForWPNodeSelect.searchForm = new Ext.form.FormPanel({
		labelWidth: RecordCardForWPNodeSelect.labelWidth,
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
					fieldLabel: '记录单名称',
					name: 'recordName',
					xtype: 'textfield',
					width: RecordCardForWPNodeSelect.fieldWidth
				}]
			}, {
				columnWidth: .3,
				layout: 'form',
				items: [{
					fieldLabel: '编号',
					name: 'recordCardNo',
					xtype: 'textfield',
					width: RecordCardForWPNodeSelect.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .3,
				layout: 'form',
				items: [{
					fieldLabel: '描述',
					name: 'recordCardDesc',
					xtype: 'textfield',
					width: RecordCardForWPNodeSelect.fieldWidth
				}]
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
				RecordCardForWPNodeSelect.searchParams = RecordCardForWPNodeSelect.searchForm.getForm().getValues();
				// 重新加载表格
				RecordCardForWPNodeSelect.grid.store.load();
			}
		}, {
			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
				RecordCardForWPNodeSelect.searchForm.getForm().reset();
				RecordCardForWPNodeSelect.searchParams = {};
				// 重新加载表格
				RecordCardForWPNodeSelect.grid.store.load();
			}
		}]
	});
	/** ************* 定义查询表单结束 ************* */
	
	/** ************* 定义候选表格开始 ************* */
	RecordCardForWPNodeSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/recordCard!findPageListForWPNodeSelect.action',                 //装载列表数据的请求URL
	    tbar: null,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden: true, width: 15
		},{
			header:'记录单名称 - 顺序', dataIndex:'seqNo', width: 40, hidden: true, renderer: function(value, metadata, record, rowIndex, colIndex, store) {
				return record.data.recordName + " - " + value;
			}
		},{
			header:'记录卡编号', dataIndex:'recordCardNo', width: 15
		},{
			header:'记录卡描述', dataIndex:'recordCardDesc', width: 40
		},{
			header:'记录单名称', dataIndex:'recordName', width: 40, hidden: false
		}],
		storeAutoLoad: false
	});
	RecordCardForWPNodeSelect.grid.un('rowdblclick', RecordCardForWPNodeSelect.grid.toEditFn, RecordCardForWPNodeSelect.grid);
	RecordCardForWPNodeSelect.grid.store.setDefaultSort("seqNo", "ASC");
	//查询前添加过滤条件
	RecordCardForWPNodeSelect.grid.store.on('beforeload' , function(){
		var searchParams = MyJson.deleteBlankProp(RecordCardForWPNodeSelect.searchParams);
		this.baseParams.wPIDX = RecordCardForWPNodeSelect.wPIDX;
		this.baseParams.wPNodeIDX = RecordCardForWPNodeSelect.wPNodeIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************* 定义候选表格结束 ************* */
	
	RecordCardForWPNodeSelect.win = new Ext.Window({
		title:"选择检修记录卡",
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
			items: [RecordCardForWPNodeSelect.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			items: [RecordCardForWPNodeSelect.grid]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '添加', iconCls: 'yesIcon', handler: RecordCardForWPNodeSelect.addFn
		}, {
			xtype: "button", text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
});