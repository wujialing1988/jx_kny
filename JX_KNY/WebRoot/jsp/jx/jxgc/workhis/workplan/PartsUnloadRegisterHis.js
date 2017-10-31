/**
 *  表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsUnloadRegisterHis');                       //定义命名空间
	PartsUnloadRegisterHis.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsUnloadRegisterHis!pageList.action',                 //装载列表数据的请求URL
	    singleSelect: true,
	    storeAutoLoad:false,
	    viewConfig: null,
	    tbar:[],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'兑现单主键', dataIndex:'rdpIdx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'配件名称', dataIndex:'partsName', width:150			
		},{
			header:'规格型号', dataIndex:'specificationModel', width:150
		},{
			header:'位置', dataIndex:'unloadPlace', width:120
		},{
			header:'配件编号', dataIndex:'partsNo', width:120,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
		     		var html = "";	 
		  			html = "<span><a href='#' onclick='TrainWorkPlanCommHis.showPartsRdpInfoFn(\""+ record.get('partsRdpIDX') + "\",\""+ record.get('partsAccountIDX') +"\")'>"+value+"</a></span>";
		      		return html;
				}
		},{
			header:'识别码', dataIndex:'identificationCode', width: 120
		},{
			header:'配件铭牌号', dataIndex:'nameplateNo', width: 140	, hidden:true
		},{
			header:'下车日期', dataIndex:'unloadDate',xtype:'datecolumn'
		},{
			header:'下车原因', dataIndex:'unloadReason', width:150
		},{
			header:'检修需求单', dataIndex:'wpDesc', width:150, hidden:true
		},{
			header:'配件信息主鍵', dataIndex:'partsAccountIDX', width:150, hidden:true
		},{
			header:'状态', dataIndex:'partsStatusName', width:60
		},{
			header:'配件兑现单主键', dataIndex:'partsRdpIDX', hidden:true, editor: { xtype:'hidden' }
		}]
	});
	// 默认按识别码正序排序
	PartsUnloadRegisterHis.grid.store.setDefaultSort('partsName', "ASC");
	PartsUnloadRegisterHis.grid.store.on('beforeload' , function(){
		var searchParam = {};
		searchParam.rdpIdx = TrainWorkPlanCommHis.rpdIdx; //兑现单主键
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	PartsUnloadRegisterHis.grid.un('rowdblclick', PartsUnloadRegisterHis.grid.toEditFn, PartsUnloadRegisterHis.grid); //取消编辑监听

});