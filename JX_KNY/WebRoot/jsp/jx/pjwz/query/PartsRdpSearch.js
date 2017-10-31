/**
 * 配件检修结果查询 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace("PartsRdpSearch");				// 定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	PartsRdpSearch.labelWidth = 100;
	PartsRdpSearch.fieldWidth = 140;
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 规格型号选择控件赋值函数
	 * @param node 配件规格型号树节点
	 * @param e
	 */
	PartsRdpSearch.callReturnFn = function(node, e){
		// 规格型号显示字段赋值
	  	PartsRdpSearch.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
		// 规格型号主键隐藏字段赋值
	  	PartsRdpSearch.searchForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	}
	/**
	 * 打印
	 * @param idx 配件检修作业idx主键 
	 */
	PartsRdpSearch.printerFn = function(idx) {
		PartsRdpPrinter.rdpIDX = idx;
		PartsRdpPrinter.win.show();
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	PartsRdpSearch.searchForm = new Ext.form.FormPanel({
		padding: 10, labelWidth: PartsRdpSearch.labelWidth,
		layout: 'column', defaults: {
			columnWidth: .25, layout: 'form', defaultType: 'textfield',
			defaults: {
				width: PartsRdpSearch.fieldWidth
			}
		},
		items: [{
			items: [{
				fieldLabel: "下车车型",
				xtype: "Base_combo",
				hiddenName: "unloadTrainTypeIdx",
			    business: 'trainVehicleType',
			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
                fields:['idx','typeName','typeCode','vehicleType'],
                queryParams: {},// 表示客货类型
    		    displayField: "typeCode", valueField: "idx",
                pageSize: 20, minListWidth: 200,
                editable:false,				
				listeners : {
					"select" : function(combo, record, index) {
						// 重新加载修程下拉数据
						var vehicleType = record.data.vehicleType ;
						var xcCombo = Ext.getCmp("repairClassCombo");
						xcCombo.reset();
						xcCombo.clearValue();
						xcCombo.getStore().removeAll();
						xcCombo.queryParams = {
							"TrainTypeIdx" : this.getValue(),
							"vehicleType": vehicleType
						};
						xcCombo.cascadeStore();
					}
				}
			}, {
				fieldLabel: '规格型号',
				xtype : "PartsTypeTreeSelect",
				name: 'specificationModel',
				editable : false,
				returnFn: PartsRdpSearch.callReturnFn
			}, {
				fieldLabel: '规格型号idx', name: 'partsTypeIDX', xtype: 'hidden'
			}]
		},{
			items: [{
				fieldLabel: '下车车号', name: 'unloadTrainNo'
			}, {
				fieldLabel: '识别码', name: 'identificationCode'
			}]
		},{
			items: [{
				fieldLabel: '下车修程',
				id : "repairClassCombo",
				xtype : "Base_combo",
				business : 'trainRC',
				entity : 'com.yunda.jx.base.jcgy.entity.TrainRC',
				fields : ['xcID', 'xcName'],
				hiddenName : "unloadRepairClassIdx",
				displayField : "xcName",
				valueField : "xcID",
				pageSize : 20,
				minListWidth : 200,
				queryHql : 'from UndertakeRc',
				width : 140,
				editable : false
			}, {
				fieldLabel: '配件编号', name: 'partsNo'
			}]
		},{
			items: [{
				fieldLabel: '下车位置', name: 'unloadPlace'
			}]
		}],
		
		buttonAlign: 'center',
		buttons: [{
			text: '查询', iconCls: 'searchIcon', handler: function() {
				// 查询功能
				PartsRdpSearch.grid.store.load();
			}
		}, {
			text: '重置', iconCls: 'resetIcon', handler: function() {
				var formPanel = this.findParentByType('form');
				formPanel.getForm().reset();
				// 清空“下车修程”字段
				formPanel.findByType("Base_combo")[0].clearValue();
				
				// 重新查询
				PartsRdpSearch.grid.store.load();
			}
		}]
	});
	/** **************** 定义查询表单结束 **************** */
	
	/** **************** 定义配件检修作业查询结果列表开始 **************** */
	PartsRdpSearch.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/partsRdp!pagePartsRdpRdpInfoQuery.action',                //装载列表数据的请求URL
		tbar: null,
		viewConfig: null,
		fields: [{
			header:'打印', dataIndex:'idx', width: 40, renderer: function(value, metaData, record, rowIndex, colIndex, store) {			
				return "<img src='" + printerImg + "' alt='打印' style='cursor:pointer' onclick='PartsRdpSearch.printerFn(\"" + value + "\")'/>";
			}
		},{
			header:'配件idx主键', dataIndex:'partsAccountIDX', hidden: true
		},{
			header:'识别码', dataIndex:'identificationCode', width: 140 
		},{
			header:'配件编号', dataIndex:'partsNo', width: 140
		},{
			header:'配件铭牌号', dataIndex:'nameplateNo', width: 140
		},{
			header:'配件名称', dataIndex:'partsName', width: 140
		},{
			header:'规格型号', dataIndex:'specificationModel', width: 180
		},{
			header:'物料编码', dataIndex:'matCode', width: 60
		},{
			header:'检修班组', dataIndex:'repairOrgName'
		},{
			header:'下车车型', dataIndex:'unloadTrainType', hidden: true
		},{
			header:'下车车号', dataIndex:'unloadTrainNo', width: 90,
			renderer: function(value, metadata, record, rowIndex, colIndex, store) {
				var unloadTrainType = record.get('unloadTrainType');
				var text = Ext.isEmpty(unloadTrainType) ? "" : unloadTrainType;
				return text + (Ext.isEmpty(value) ? "" : value);
			}
		},{
			header:'下车修程', dataIndex:'unloadRepairClass', hidden: true
		},{
			header:'下车修次', dataIndex:'unloadRepairTime', width: 60,
			renderer: function(value, metadata, record, rowIndex, colIndex, store) {
				var unloadRepairClass = record.get('unloadRepairClass');
				var text = Ext.isEmpty(unloadRepairClass) ? "" : unloadRepairClass;
				return text + (Ext.isEmpty(value) ? "" : value)
			}
		},{
			header:'检修开始时间', dataIndex:'realStartTime', xtype: "datecolumn", format:"Y-m-d"
		},{
			header:'检修结束时间', dataIndex:'realEndTime', xtype: "datecolumn", format:"Y-m-d"
		},{
			header:'检修状态', dataIndex:'status', width: 60,
			renderer: function(value, metadata, record, rowIndex, colIndex, store) {
				if (STATUS_WQD == value) return '未启动';
				if (STATUS_JXZ == value) return '检修中';
				if (STATUS_DYS == value) return '待验收';
				if (STATUS_YZZ == value) return '已终止';
				if (STATUS_WFXF == value) return '无法修复';
				if (STATUS_JXHG == value) return '检修合格';
				return '错误！未知的状态';
			}
		}, {
			header:'检修需求单', dataIndex: 'wpDesc', hidden: true
		},{
			header:'下车位置', dataIndex:'unloadPlace'
		}],
		
		toEditFn: function(grid, rowIndex, e) {
			// 配件检修作业记录
			PartsRdpInfo.record = grid.store.getAt(rowIndex);
			PartsRdpInfo.win.show();
		}
	});
	
	// 默认按识别码正序排序
	PartsRdpSearch.grid.store.setDefaultSort('identificationCode', "ASC");
	
	// 列表数据源加载前的查询条件过滤
	PartsRdpSearch.grid.store.on('beforeload', function(){
		/*
		var form = PartsRdpSearch.searchForm.getForm();
		var values = form.getValues();
		var searchParams = MyJson.deleteBlankProp(values);
		var whereList = [];
		for (var prop in searchParams) {
			whereList.push({propName: prop, propValue: searchParams[prop]});
		}
	    this.baseParams.whereListJson = Ext.encode(whereList);*/
	    
	    var form = PartsRdpSearch.searchForm.getForm();
		var values = form.getValues();
		var searchParams = MyJson.deleteBlankProp(values);
		
		PartsRdpSearch.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	    
	});
	
	/** **************** 定义配件检修作业查询结果列表结束 **************** */
	
	//页面自适应布局
	var viewport = new Ext.Viewport({
		layout: 'border',
		items: [{
			title: '查询',
			region: 'north', height: 140,
			collapsible: true, frame: true,
			layout: 'fit',
			items: PartsRdpSearch.searchForm
		}, {
			region: 'center',
			layout: 'fit',
			items: PartsRdpSearch.grid
		}]
	});
	
});
