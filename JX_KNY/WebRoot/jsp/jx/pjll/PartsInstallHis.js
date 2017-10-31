/**
 * 配件检修结果信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace("PartsInstallHis");				// 定义命名空间
	
	/** **************** 定义拆卸配件查询结果列表开始 **************** */
	PartsInstallHis.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/partsInstallHis!pageList.action',                //装载列表数据的请求URL
		tbar: null,
		storeAutoLoad: false,
		
		viewConfig: null,
		
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		}, {
			header:'上级配件idx主键', dataIndex:'parentPartsAccountIDX', hidden:true
		}, {
			header:'识别码', dataIndex:'identificationCode'
		}, {
			header:'配件编号', dataIndex:'partsNo'
		},{
			header:'配件铭牌号', dataIndex:'nameplateNo', width: 140
		}, {
			header:'配件名称', dataIndex:'partsName', width: 110
		}, {
			header:'规格型号', dataIndex:'specificationModel', width: 140
		}, {
			header:'下车车型', dataIndex:'unloadTrainType', hidden:true
		}, {
			header:'下车车型编码', dataIndex:'unloadTrainTypeIdx', hidden:true
		}, {
			header:'下车车号', dataIndex:'unloadTrainNo', hidden:true
		}, {
			header:'下车修程', dataIndex:'unloadRepairClass', hidden:true
		}, {
			header:'下车修程编码', dataIndex:'unloadRepairClassIdx', hidden:true
		}, {
			header:'下车修次', dataIndex:'unloadRepairTime', hidden:true		
		}, {
			header:'位置', dataIndex:'aboardPlace'
		}, {
			header:'安装日期', dataIndex:'aboardDate', xtype: 'datecolumn', format: 'Y-m-d'
		}, {
			header:'检修需求主键', dataIndex:'wpIDX', hidden:true
		}, {
			header:'检修需求编号', dataIndex:'wpNo', hidden:true
		}, {
			header:'检修需求', dataIndex:'wpDesc'
		}, {
			header:'配件状态编码', dataIndex:'partsStatus', hidden:true
		}, {
			header:'配件状态', dataIndex:'partsStatusName'
			}, {
			header:'下车信息', dataIndex:'unloadRepairTimeIdx', width: 125,
			renderer: function(value, metadata, record, rowIndex, colIndex, store) {
				var unloadTrainInfo = "";
				// 车型
				if (!Ext.isEmpty(record.get('unloadTrainType'))) {
					unloadTrainInfo += record.get('unloadTrainType');
				}
				// 车号
				if (!Ext.isEmpty(record.get('unloadTrainNo'))) {
					unloadTrainInfo += record.get('unloadTrainNo');
				}
				if (!Ext.isEmpty(unloadTrainInfo)) {
					unloadTrainInfo += "&nbsp;"
				}
				// 修程
				if (!Ext.isEmpty(record.get('unloadRepairClass'))) {
					unloadTrainInfo += record.get('unloadRepairClass');
				}
				// 修次
				if (!Ext.isEmpty(record.get('unloadRepairTime'))) {
					unloadTrainInfo += record.get('unloadRepairTime');
				}
				return unloadTrainInfo;
			}
		}],
		
		toEditFn: Ext.emptyFn
	});
	
	PartsInstallHis.grid.store.on('beforeload', function() {
		this.baseParams.entityJson = Ext.encode({
			parentPartsAccountIDX: PartsInstallHis.parentPartsAccountIDX
		});
	});
	/** **************** 定义拆卸配件查询结果列表结束 **************** */
});