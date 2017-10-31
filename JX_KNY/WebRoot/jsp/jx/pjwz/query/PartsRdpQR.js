/**
 * 质量检查结果 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpQR');                       // 定义命名空间
	
	PartsRdpQR.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpQR!pageList.action',                 //装载列表数据的请求URL
	    
	    tbar: null,
	    storeAutoLoad: false,
	    
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'关联主键', dataIndex:'relIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'记录卡实例主键', dataIndex:'rdpRecordCardIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'质量检查项主键', dataIndex:'qCItemIDX', hidden:true, editor:{  maxLength:50 }
		},{
			header:'检查项编码', dataIndex:'qCItemNo', hidden:true, editor:{  maxLength:50 }
		},{
			header:'检查项名称', dataIndex:'qCItemName', editor:{  maxLength:50 }
		},{
			header:'是否指派', dataIndex:'isAssign', hidden:true, editor:{ xtype:'numberfield', maxLength:1 }
		},{
			header:'顺序号', dataIndex:'seqNo', hidden:true, editor:{ xtype:'numberfield', maxLength:3 }
		},{
			header:'检验人员', dataIndex:'qREmpID', hidden:true, editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'检验人员名称', dataIndex:'qREmpName', editor:{  maxLength:25 }
		},{
			header:'检验结果', dataIndex:'qRResult', editor:{  maxLength:50 }
		},{
			header:'检验时间', dataIndex:'qRTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'检验方式', dataIndex:'checkWay', renderer: function(v){
				if ('1' == v) return '抽检'
				if ('2' == v) return '必检'
			}
		},{
			header:'状态', dataIndex:'status', editor:{  maxLength:20 }, 
			renderer: function(v){
				if ('01' == v) return '未处理'
				if ('02' == v) return '已处理'
			}
		}],
		
		toEditFn: Ext.emptyFn
	});
	
	PartsRdpQR.grid.store.on('beforeload', function(){
		this.baseParams.entityJson = Ext.encode({
			rdpRecordCardIDX: PartsRdpQR.rdpRecordCardIDX
		});
	});
});