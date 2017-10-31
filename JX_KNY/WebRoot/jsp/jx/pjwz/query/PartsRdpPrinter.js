/**
 * 检修记录单打印 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace("PartsRdpPrinter");	
	
	/** **************** 定义全局变量开始 **************** */
	PartsRdpPrinter.rdpIDX = null; 				// 配件检修作业主键
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 打印预览
	 * @param args 配件检修记录单、与配件检修基础维护记录单主键
	 * 形如: 'E6A08C66526941119AE25135F66A1982'-'8a8284f250e9e6430150ea1ac3d00005'
	 */
	PartsRdpPrinter.printFn = function(args) {
		var partsRdpRecordIDX = args.split('-')[0];			// 配件检修记录单实例idx主键
		var recordIDX = args.split('-')[1];					// 记录单idx主键
		// Ajax请求
		Ext.Ajax.request({
			url: ctx + '/printerModule!getModelForPreview.action',
			params:{
				businessIDX: recordIDX
			},
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
					var dataUrl = reportUrl + "&idx=" + partsRdpRecordIDX;		// 配件检修记录单实例idx主键
                	window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI(displayName));
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
	
	/** **************** 定义配件检修记录单表格开始 **************** */
	PartsRdpPrinter.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpRecord!queryPageList.action',                 //装载列表数据的请求URL
	    tbar: null,
	    viewConfig: null,
		fields: [{
			header:'打印', dataIndex:'idx', width: 40, renderer: function(value, metaData, record, rowIndex, colIndex, store) {	
				var recordIDX = record.get('recordIDX')
				var args = [value, recordIDX].join('-');
				return "<img src='" + printerImg + "' alt='打印' style='cursor:pointer' onclick='PartsRdpPrinter.printFn(\"" + args + "\")'/>";
			}
		},{
			header:'作业主键', dataIndex:'rdpIDX', hidden:true
		},{
			header:'记录单主键', dataIndex:'recordIDX', hidden:true
		},{
			header:'记录单编号', dataIndex:'recordNo'
		},{
			header:'记录单名称', dataIndex:'recordName', width: 240
		},{
			header:'记录单描述', dataIndex:'recordDesc', hidden:true
		},{
			header:'报表主键', dataIndex:'reportTmplManageIDX', hidden:true
		},{
			header:'配件名称', dataIndex:'partsName', width: 180
		},{
			header:'配件编号', dataIndex:'partsNo', width: 130
		},{
			header:'识别码', dataIndex:'identificationCode', width: 130
		},{
			header:'完成百分比', dataIndex:'completPercent', width: 130,
			renderer :function(a,b,c,d){
				return Math.round(parseInt(a)) + "%";
			}
		}],
		toEditFn: Ext.emptyFn
	});
	PartsRdpPrinter.grid.store.on('beforeload', function(){
		this.baseParams.entityJson = Ext.encode({
			rdpIDX: PartsRdpPrinter.rdpIDX
		});
	});
	/** **************** 定义配件检修记录单表格结束 **************** */
	
	PartsRdpPrinter.win = new Ext.Window({
		title: '检修记录单打印',
		height: 500, width: 1000,
		closeAction: 'hide',
		buttonAlign: 'center',
		layout: 'fit',
		items: PartsRdpPrinter.grid,
		modal: true,
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}],
		
		listeners: {
			show: function() {
				PartsRdpPrinter.grid.store.load();
			}
		}
	});
});