

Ext.onReady(function() {
	Ext.namespace('PartsTypeForSelect');
	
	/** ************* 定义全局变量开始 ************* */
	PartsTypeForSelect.searchParams = {};
	PartsTypeForSelect.labelWidth = 80,
	PartsTypeForSelect.fieldWidth = 120,
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 【确定】按钮触发的函数处理
	PartsTypeForSelect.addFn = function() {
		if (!$yd.isSelectedRecord(PartsTypeForSelect.grid)) {
			return;
		}
		var ids = $yd.getSelectedIdx(PartsTypeForSelect.grid);
		
		var datas = [];
		
		for (var i = 0; i < ids.length; i++) {
			var partsListV = {};
			partsListV.relationIDX = RecordCard.idx;
			partsListV.partsTypeIDX = ids[i];
			datas.push(partsListV);
		}
    	// Ajax后台数据处理
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
            url: ctx + '/partsFwList!save.action',
            jsonData: datas,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	alertSuccess();
                	// 重新加载 【配件清单】
                    PartsFwList.grid.store.reload();
                	// 重新加载 【配件规格型号】
                    PartsTypeForSelect.grid.store.reload();
                } else {
                    alertFail(result.errMsg);
                }
            }
        }));
		
	}
	/** ************* 定义全局函数结束 ************* */
	
	/** ************* 定义查询表单开始 ************* */
	PartsTypeForSelect.searchForm = new Ext.form.FormPanel({
		labelWidth: PartsTypeForSelect.labelWidth,
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
					width: PartsTypeForSelect.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .3,
				layout: 'form',
				items: [{
					fieldLabel: '配件名称',
					name: 'partsName',
					xtype: 'textfield',
					width: PartsTypeForSelect.fieldWidth
				}]
			}, {													// 第1行第3列
				columnWidth: .3,
				layout: 'form',
				items: [{
					fieldLabel: '规格型号',
					name: 'specificationModel',
					xtype: 'textfield',
					width: PartsTypeForSelect.fieldWidth
				}]
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
				// 重新加载表格
				PartsTypeForSelect.grid.store.load();
			}
		}, {
			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
				PartsTypeForSelect.searchForm.getForm().reset();
				// 重新加载表格
				PartsTypeForSelect.grid.store.load();
			}
		}]
	});
	/** ************* 定义查询表单结束 ************* */
	
	/** ************* 定义候选表格开始 ************* */
	PartsTypeForSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsType!pageQuery.action',                 //装载列表数据的请求URL
	    storeAutoLoad : false,
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
	PartsTypeForSelect.grid.un('rowdblclick', PartsTypeForSelect.grid.toEditFn, PartsTypeForSelect.grid);
	PartsTypeForSelect.grid.store.setDefaultSort("specificationModelCode", "ASC");
	//查询前添加过滤条件
	PartsTypeForSelect.grid.store.on('beforeload' , function(){
		PartsTypeForSelect.searchParams = PartsTypeForSelect.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(PartsTypeForSelect.searchParams);
		
		var sqlStr = " status=1 " ;
		var whereList = [
		          		{sql: sqlStr, compare: Condition.SQL} //过滤已选择的配件规格型号
					]
		for(prop in searchParams){
	     	whereList.push({propName:prop,propValue:searchParams[prop],compare:Condition.EQ});
	     }
	    this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
	});
	/** ************* 定义候选表格结束 ************* */
	
	PartsTypeForSelect.win = new Ext.Window({
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
			items: [PartsTypeForSelect.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			items: [PartsTypeForSelect.grid]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '添加', iconCls: 'yesIcon', handler: PartsTypeForSelect.addFn
		}, {
			xtype: "button", text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
});