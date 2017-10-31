/**
 * 配件检修结果信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace("PartsRdpInfo");				// 定义命名空间
	/** **************** 定义全局变量开始 **************** */
	PartsRdpInfo.record = null;					// 配件检修作业记录
	PartsRdpInfo.labelWidth = 100;
	PartsRdpInfo.fieldWidth = 140;
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 获取配件检修兑现单信息显示模板
	 */
	PartsRdpInfo.getPartsRdpInfoTpl = function() {
		if (Ext.isEmpty(PartsRdpInfo.tpl)) {
			PartsRdpInfo.tpl = new Ext.XTemplate(
			'<table class="pjjx-show-info-table">',
				'<tr>',
	                '<td class="pjjx-show-info-table-label" width="12%">识别码：</td><td width="13%">{identificationCode}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">配件编号：</td><td width="13%">{partsNo}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">配件名称：</td><td width="13%">{partsName}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">规格型号：</td><td width="13%">{specificationModel}</td>',
	            '</tr>',
				'<tr>',
	                '<td class="pjjx-show-info-table-label" width="12%">下车车型：</td><td width="13%">{unloadTrainType}{unloadTrainNo}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">下车修程：</td><td width="13%">{unloadRepairClass}{unloadRepairTime}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">开始时间：</td><td width="13%">{realStartTime}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">结束时间：</td><td width="13%">{realEndTime}</td>',
	            '</tr>',
				'<tr>',
	                '<td class="pjjx-show-info-table-label" width="12%">检修班组：</td><td width="13%">{repairOrgName}</td>',
	                '<td class="pjjx-show-info-table-label" width="12%">检修需求单：</td><td width="13%" colspan="5">{wpDesc}</td>',
	            '</tr>',
			'</table>'
		/*, {
	        comipled: true,
	        *//**
	         * 格式化日期
	         * @param value 日期毫秒数
	         * @param fmt 日期格式
	         * @returns {String} 格式化后的日期字符串
	         *//*
	        format: function(value) {
				if (Ext.isEmpty(value)) return '';
		        var date = new Date(value);
		        return date.format('Y-m-d');
	        }
	    }*/);
		}
	    return PartsRdpInfo.tpl;
	}
	
	/**
	 * 根据配件检修作业任务实体初始化详情查看页面
	 * @param data 配件检修作业任务实体
	 */
	PartsRdpInfo.initFn = function(data) {
		var values = {};
		// 格式化“开始时间”和“结束时间”
		if (!Ext.isEmpty(data.realStartTime)) {
			if (Ext.isString(data.realStartTime)) {
				values.realStartTime = data.realStartTime;
			} else {
				values.realStartTime = new Date(data.realStartTime).format('Y-m-d H:i');
			}
		}
		if (!Ext.isEmpty(data.realEndTime)) {
			if (Ext.isString(data.realStartTime)) {
				values.realEndTime = data.realEndTime;
			} else {
				values.realEndTime = new Date(data.realEndTime).format('Y-m-d H:i');
			}
		}
		Ext.applyIf(values, data);
		// 获取配件检修兑现单信息显示模板
		var tpl = PartsRdpInfo.getPartsRdpInfoTpl();
		tpl.overwrite(Ext.get('id_parts_rdp_info_base'), values);
		
		// -------------->> 加载“配件检修作业任务”树
		var text = [data.partsName, "（", data.specificationModel, "_", data.partsNo, "）"];
//		PartsRdpInfo.tree.getRootNode().setText(text.join(''));
//		PartsRdpInfo.tree.getRootNode().setId(data.idx);
//		PartsRdpInfo.tree.getRootNode().reload();
//		PartsRdpInfo.tree.getRootNode().expand();
//		PartsRdpInfo.tree.getRootNode().attributes["entity"] = Ext.encode(data);
		
		// -------------->> 加载“拆卸安装配件”列表
		var parentPartsAccountIDX = data.partsAccountIDX;
		// 上级配件idx主键
		PartsDismantleHis.parentPartsAccountIDX = parentPartsAccountIDX;
		// 根据上级配件idx主键查询该配件已拆卸的配件
		PartsDismantleHis.grid.store.load();
		
		// 上级配件idx主键
		PartsInstallHis.parentPartsAccountIDX = parentPartsAccountIDX;
		// 根据上级配件idx主键查询该配件已拆卸的配件
		PartsInstallHis.grid.store.load();
		
		// -------------->> 加载“检修记录工单”列表
		PartsRdpRecordCard.rdpIDX = data.idx;
		PartsRdpRecordCard.grid.store.load();
		
		// -------------->> 加载“检修作业工单”列表
		PartsRdpTecCard.rdpIDX = data.idx;
		PartsRdpTecCard.grid.store.load();
		
		// -------------->> 加载“提票工单”列表
		PartsRdpNotice.rdpIDX = data.idx;
		PartsRdpNotice.grid.store.load();
		
		// -------------->> 加载“物料消耗情况”列表
		PartsRdpExpendMatQuery.rdpIDX = data.idx;
		PartsRdpExpendMatQuery.grid.store.load();
	}
	/** **************** 定义全局函数开始 **************** */
	
	/** **************** 定义配件检修作业任务树开始 **************** */
//	PartsRdpInfo.tree =  new Ext.tree.TreePanel({
//		title: '配件检修作业任务',
//		tbar :new Ext.Toolbar(),
//		plugins: ['multifilter'],
//	    loader : new Ext.tree.TreeLoader( {
//	        dataUrl : ctx + "/partsRdp!tree.action"
//	    }),
//	    root: new Ext.tree.AsyncTreeNode({
//	       	text: '配件检修作业任务',
//	        id: "ROOT_0",
//	        leaf: false
//	    }),
//	    rootVisible: true,
//	    autoScroll : true,
//	    autoShow: true,
//	    useArrows : true,
//	    border : false,
//	    listeners: {
//	        beforeload:  function(node){
//			    PartsRdpInfo.tree.loader.dataUrl = ctx + '/partsRdp!tree.action?parentIDX=' + node.id;
//			},
//			render: function() {
//				// 渲染成功后，自动加载第一节节点数据
////				this.getRootNode().expand();
//				// 展开所有节点数据
////				this.expandAll();
//			},
//			click: function( node, e ) {
////				var entity = Ext.decode(node.attributes['entity']);
////				PartsRdpInfo.initFn(entity);
//			}
//	    }    
//	});
//	// 树节点选择模式变更后，根据当前选择的节点（配件检修作业任务）重新加载配件检修作业详情
//	PartsRdpInfo.tree.getSelectionModel().on('selectionchange', function( sm, node ){
//		var entity = Ext.decode(node.attributes['entity']);
//		PartsRdpInfo.initFn(entity);
//	});
	/** **************** 定义配件检修作业任务树结束 **************** */
	
	PartsRdpInfo.win = new Ext.Window({
		title: '作业计划单查看',
		maximized: true,
		layout: 'border',
		closeAction: 'hide',
		items: [{
				region: 'north', height: 118, layout: 'fit', frame: true,
				items: {
					xtype: 'fieldset', title: '配件检修兑现单信息',
					html: '<div id="id_parts_rdp_info_base"></div>'
				}
			}, {
				region: 'center', layout: 'fit',
				items: {
					xtype: 'tabpanel', activeItem: 0, border: false,
					items: [/*{
						title: '拆卸安装配件',
						layout: 'ux.row',
						defaults: { rowHeight: .5 },
						items: [{
							title: '拆卸配件', layout: 'fit',
							items: [PartsDismantleHis.grid]
						}, {
							title: '安装配件', layout: 'fit',
							items: [PartsInstallHis.grid]
						}]
	//				},{
						// TODO 使用甘特图方式展示流程节点信息
	//					title: '流程节点'
					},*/{
						title: '检修记录工单', layout: 'fit',
						items: [PartsRdpRecordCard.grid]
					},{
						title: '检修作业工单', layout: 'fit',
						items: [PartsRdpTecCard.grid]
					},{
						title: '提票工单', layout: 'fit',
						items: [PartsRdpNotice.grid]
					},{
						title: '物料消耗情况', layout: 'fit',
						items: [PartsRdpExpendMatQuery.grid]
					}],
					
					listeners: {
						tabchange: function( me, tab ) {/*
							if ('拆卸安装配件' === tab.title) {
								// 重新加载拆卸安装配件列表
								PartsDismantleHis.grid.store.reload();
								PartsInstallHis.grid.store.reload();
							}
							if ('检修记录工单' === tab.title) {
								PartsRdpRecordCard.grid.store.reload();
							}
							if ('检修作业工单' === tab.title) {
								PartsRdpTecCard.grid.store.reload();
							}
							if ('提票工单' === tab.title) {
								PartsRdpNotice.grid.store.reload();
							}
							if ('物料消耗情况' === tab.title) {
								PartsRdpExpendMatQuery.grid.store.reload();
							}
						*/}
					}
				}
			}],
		
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}],
		
		listeners: {
			show: function() {
				// 配件检修作业任务实体
				var data = PartsRdpInfo.record.json;
				if (typeof(data) == "undefine" || !data)
					data = PartsRdpInfo.record.data;
				// 初始化配件检修作业任务查看详情页面
				PartsRdpInfo.initFn(data);
			},
			hide: function() {
				// 详情查看窗口隐藏时，取消配件检修作业任务树的选择模式
//				PartsRdpInfo.tree.getSelectionModel().clearSelections(true);
			}
				
		}
	});
});
