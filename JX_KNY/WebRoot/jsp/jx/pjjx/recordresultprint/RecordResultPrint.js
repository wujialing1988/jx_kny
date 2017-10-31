/**
 * 
 * 记录单结果打印 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 * 
 */

Ext.onReady(function() {
	Ext.namespace('RecordResultPrint');
	
	/** **************** 定义全局变量开始 **************** */
	RecordResultPrint.searchParams = {};
	RecordResultPrint.labelWidth = 100;
	RecordResultPrint.fieldWidth = 140;
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	RecordResultPrint.searchForm = new Ext.form.FormPanel({
		labelWidth: RecordResultPrint.labelWidth,
		style: 'padding: 10px 10px 0 10px',
		layout: 'column',
		defaults:{
			columnWidth: .33, layout: 'form',
			defaults:{ xtype: 'textfield', width: RecordResultPrint.fieldWidth }
		},
		items:[{												// 第1行第1列
			items: [{ fieldLabel: '编号', name: 'recordNo' }]
		}, {													// 第1行第2列
			items: [{ fieldLabel: '名称', name: 'recordName' }]
		}, {													// 第1行第3列
			items: [{ fieldLabel: '描述', name: 'recordDesc' }]
		}],
		buttons: [{
			xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
				// 重新加载表格
				RecordResultPrint.grid.store.load();
			}
		}, {
			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
				RecordResultPrint.searchForm.getForm().reset();
				// 重新加载表格
				RecordResultPrint.grid.store.load();
			}
		}],
		buttonAlign: 'center'
	});
	/** **************** 定义查询表单结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 【打印】按钮触发的函数处理
	RecordResultPrint.printFn = function() {
		var sm = RecordResultPrint.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			MyExt.Msg.alert('尚未选择任何记录！');
			return;
		}
		var record = sm.getSelections()[0];
		RecordResultPrint.printerPreviewFn(record.get('idx'));
	}
	
	// 打印预览
	RecordResultPrint.printerPreviewFn = function(idx) {
		// Ajax请求
		Ext.Ajax.request({
			url: ctx + '/printerModule!getModelForPreview.action',
			params:{businessIDX: idx},
			//请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		        	var entity = result.entity
		        	var deployCatalog = entity.deployCatalog;		// 报表部署目录
					var displayName = entity.displayName;			// 报表显示名称
					var deployName = entity.deployName;				// 报表部署名称
					while(deployCatalog.indexOf('.') >= 0) {
						deployCatalog = deployCatalog.replace('.', '/');
					}
					var reportUrl = "/" + deployCatalog + "/" + deployName;
					
					var url = reportUrl + "?ctx=" + ctx.substring(1);
					window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(url)+"&title=" + encodeURI(displayName));
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义查询主体开始 **************** */
	RecordResultPrint.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/record!pageList.action',         //装载列表数据的请求URL
		saveURL: ctx + '/record!saveOrUpdate.action',       //保存数据的请求URL
		deleteURL: ctx + '/record!logicDelete.action',      //删除数据的请求URL
		saveWinWidth: 520,
		singleSelect: true,
		tbar:[{
			text: '打印', iconCls: 'printerIcon', handler: RecordResultPrint.printFn
		}, 'refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, width:30
		},{
			header:'编号', dataIndex:'recordNo', width:15,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	     		var html = "";
	  			html = "<span><a href='#' onclick='RecordResultPrint.printerPreviewFn(\""+ record.get('idx') +"\")'>"+value+"</a></span>";
	      		return html;
			}
		},{
			header:'名称', dataIndex:'recordName', width:40
		},{
			header:'描述', dataIndex:'recordDesc'
		}]
	});
	
	// 取消双击行出现“编辑”页面的事件监听
	RecordResultPrint.grid.un('rowdblclick', RecordResultPrint.grid.toEditFn, RecordResultPrint.grid);
	
	RecordResultPrint.grid.store.on('beforeload', function() {
		RecordResultPrint.searchParams = RecordResultPrint.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(RecordResultPrint.searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** **************** 定义查询主体结束 **************** */
	
	// 页面自适应布局
	new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			layout:'fit',
			region: 'north',
			height: 110,
			collapsible: true,
			items: [RecordResultPrint.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			height: 200,
			items: [RecordResultPrint.grid]
		}]
	});
	
});