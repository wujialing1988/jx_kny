/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){

	Ext.ns('QCResult');
	
	/** **************** 定义全局变量开始 **************** */
	QCResult.STATUS_WKF = 0;		// 质量检验项状态——未开放
	QCResult.STATUS_DCL = 1;		// 质量检验项状态——待处理开放
	QCResult.STATUS_YCL = 2;		// 质量检验项状态——已处理
	QCResult.STATUS_YZZ = 3;		// 质量检验项状态——已终止
	
	/**
	 * 显示质量检查项查看窗口
	 * @param workCardIDX 机车检修作业工单idx主键
	 */
	QCResult.showWin = function(workCardIDX) {
		QCResult.workCardIDX = workCardIDX;
		QCResult.grid.store.setBaseParam('entityJson', Ext.encode({
			workCardIDX: workCardIDX
		}));
		QCResult.win.show();
	}
	/** **************** 定义全局变量结束 **************** */
	
	
	/** **************** 定义质量检查项列表开始 **************** */
	QCResult.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/qCResult!acquirePageList.action', 	//装载列表数据的请求URL
	    tbar:null,
	    storeAutoLoad: false,
	    viewConfig: null,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden: true
		},{
			header:'质量检查项', dataIndex:'checkItemName'/*, renderer: function(value, metaData, record) {
				var status = record.get('status');
				if (0 == status) return value + "（未开放）";
				if (1 == status) return value + "（待处理）";
				if (2 == status) return value + "（已处理）";
				if (3 == status) return value + "（已终止）";
			}*/
		},{
			header:'状态', dataIndex:'status', hidden: false, renderer: function(v) {
				if (QCResult.STATUS_WKF == v) return "未开放";
				if (QCResult.STATUS_DCL == v) return "待处理";
				if (QCResult.STATUS_YCL == v) return "已处理";
				if (QCResult.STATUS_YZZ == v) return "已终止";
				return "错误！未知的状态";
			}
		},{
			header:'质量检查人', dataIndex:'qcEmpName'
		},{
			header:'质量可检查人', dataIndex:'qcParticipants', width: 400
		}],
		//不需要双击显示
		toEditFn : function(){}
	});
	/** **************** 定义质量检查项列表结束 **************** */
	
	QCResult.win = new Ext.Window({
		title: '质量检查情况',
		height: 300, width: 800,
		closeAction: 'hide',
		layout: 'fit',
		items: [ QCResult.grid ],
		modal: true,
		
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}],
		
		listeners: {
			show: function() {
				QCResult.grid.store.load();
			}
		}
	});
	
});