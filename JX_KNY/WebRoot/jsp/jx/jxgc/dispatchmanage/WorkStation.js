/**
 * 工位 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function() {
	Ext.namespace('WorkStation'); // 定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	WorkStation.searchParams = {}; 		// 全局查询参数集
	WorkStation.idx = ''; 				// 工位主键全局变量
	WorkStation.orgseq = '';			// 工位作业班组全局变量
	WorkStation.workStationName = ''; 	// 工位名称全局变量
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数量开始 **************** */
	// 根据业务编码生成规则生成工位编码
	WorkStation.getWorkStationCode = function() {
		var url = ctx + "/codeRuleConfig!getConfigRule.action";
		Ext.Ajax.request({
			url : url,
			params : { ruleFunction : "JXGC_WORK_STATION_WORK_STATION_CODE" },
			success : function(response, options) {
				var result = Ext.util.JSON.decode(response.responseText);
				if (result.errMsg == null) {
					Ext.getCmp("workStationCode_Id").setValue(result.rule);
				}
			},
			failure : function(response, options) {
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	
	/**
	 * 判断选中的记录是否可以执行启用、作废、删除等操作，返回提示信息
	 * 
	 * @param infoArray  遍历的数组
	 * @param msg 显示的字符串说明
	 * @param status
	 * @return 提示信息字符串
	 */
	WorkStation.alertOperate = function(infoArray, msg, status) {
		if (infoArray == null || infoArray.length <= 0) return msg;
		var info = "";
		var msgInfo = "";
		var operInfo = ""; // 启用消息
		var invalidInfo = ""; // 作废消息
		var titleInfo = "";
		if (status == 'del') {
			msgInfo = "该操作将不能恢复，是否继续【删除】？";
			operInfo = "不能删除";
			invalidInfo = "不能删除";
			titleInfo = '只能删除状态为【新增】的记录！';
		}
		if (status == 'start') {
			msgInfo = "确定【启用】选择的项？";
			operInfo = "";
			invalidInfo = "不能启用";
			titleInfo = '只能启用状态为【新增】的记录！';
		}
		if (status == 'invalid') {
			msgInfo = "确定【作废】选择的项？";
			operInfo = "不能作废";
			invalidInfo = "";
			titleInfo = '只能作废状态为【启用】的记录！';
		}

		if (infoArray instanceof Array) {
			for (var i = 0; i < infoArray.length; i++) {
				if (infoArray[i].get("status") == stationStatus_new) {
					info += (i + 1) + ". 【"
							+ infoArray[i].get("workStationCode") + "】为新增"
							+ operInfo + "！</br>";
					msgInfo = msg;
				}
				if (infoArray[i].get("status") == stationstationStatus_use) {
					info += (i + 1) + ". 【"
							+ infoArray[i].get("workStationCode") + "】已经启用"
							+ operInfo + "！</br>";
					msgInfo = msg;
				}
				if (infoArray[i].get("status") == stationStatus_nullify) {
					info += (i + 1) + ". 【"
							+ infoArray[i].get("workStationCode") + "】已经作废"
							+ invalidInfo + "！</br>";
					msgInfo = msg;
				}
			}
		} else {
			info = infoArray;
		}
		return titleInfo + '</br>' + info + '</br>' + msgInfo;
	}
	// 更新流水线记录业务状态，启用或作废
	WorkStation.updateStatus = function(validStatus, _grid, _operate) {
		// 未选择记录，直接返回
		if (!$yd.isSelectedRecord(_grid))
			return;

		var data = _grid.selModel.getSelections();
		var ids = new Array();
		var flag = new Array(); // 标记选择的项目
		for (var i = 0; i < data.length; i++) {
			if (data[i].get('status') == validStatus) {
				ids.push(data[i].get("idx"));
			} else {
				flag.push(data[i]);
			}
		}
		// 提示信息，请求参数
		var action, msgOnly, url, params;
		switch (_operate) {
			case 'del' :
				action = '删除';
				msgOnly = '只能【删除】新增状态的记录！';
				url = WorkStation.grid.deleteURL;
				params = {
					ids : ids
				};
				break;
			case 'start' :
				action = '启用';
				msgOnly = '只能【启用】新增状态的记录！';
				url = ctx + '/workStation!updateStatus.action';
				params = {
					ids : ids,
					status : stationStatus_use
				};
				break;
			case 'invalid' :
				action = '作废';
				msgOnly = '只能【作废】启用状态的记录！';
				url = ctx + '/workStation!updateStatus.action';
				params = {
					ids : ids,
					status : stationStatus_nullify
				};
				break;
		}
		if (ids.length <= 0) {
			MyExt.Msg.alert(msgOnly);
			return;
		}
		// 弹出信息确认框，根据用户确认后执行操作
		Ext.Msg.confirm('提示', WorkStation.alertOperate(flag, '是否执行' + action + '操作，该操作将不能恢复！', _operate), function(btn) {
			if (btn != 'yes')
				return;
			var cfg = Ext.apply($yd.cfgAjaxRequest(), {
				url : url,
				params : params,
				success : function(response, options) {
					var result = Ext.util.JSON.decode(response.responseText);
					if (result.errMsg == null) {
						// 操作成功
						alertSuccess();
						_grid.store.reload();
						_grid.afterDeleteFn();
						// 重新加载工位树
						WorkStation.reloadTree(WorkStation.treePath);
					} else {
						// 操作失败
						alertFail(result.errMsg);
					}
				}
			});
			Ext.Ajax.request(cfg);
		});
	}
	// 删除流水线函数，点击删除按钮触发的函数
	WorkStation.deleteFn = function() {
		if (this.saveWin)
			this.saveWin.hide();
		if (this.searchWin)
			this.searchWin.hide();
		// 未选择记录，直接返回
		if (!$yd.isSelectedRecord(this))
			return;
		// 执行删除前触发函数，根据返回结果觉得是否执行删除动作
		if (!this.beforeDeleteFn())
			return;
		WorkStation.updateStatus(stationStatus_new, this, 'del');
	}
	/** **************** 定义全局函数量结束 **************** */
	
	// 机车检修作业流程节点树型列表
	// TODO
	WorkStation.tree =  new Ext.tree.TreePanel( {
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/workStation!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '工位树',
	        id: "ROOT_0",
	        leaf: false
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    autoShow: true,
	    useArrows : true,
	    border : false,
	    listeners: {
	    	render: function() {
	    	},
	        beforeload:  function(node){
			    WorkStation.tree.loader.dataUrl = ctx + '/workStation!tree.action?parentIDX=' + node.id + '&repairLineIdx=' + RepairLine.idx;
			},
			load: function(node) {
				this.getSelectionModel().select(node);
			}
	    }    
	});
	
	// 选中的树节点变化时的事件监听函数
	WorkStation.tree.getSelectionModel().on('selectionchange', function(dsm, node) {
		if (Ext.isEmpty(node)) return;
		// TODO
		WorkStation.searchParams.parentIDX = node.id;
		WorkStation.parentIDX = node.id;
		WorkStation.treePath = node.getPath();
		WorkStation.grid.store.load();
	});
	
	// 重新加载【作业节点树】
	WorkStation.reloadTree = function(path) {
        WorkStation.tree.root.reload(function() {
			WorkStation.tree.getSelectionModel().select(WorkStation.tree.root);
    	});
        if (path == undefined || path == "" || path == "###") {
			WorkStation.tree.root.expand();
        } else {
        	// 展开树到指定节点
	        WorkStation.tree.expandPath(path);
	        WorkStation.tree.selectPath(path);
        }
	}
	
	WorkStation.grid = new Ext.yunda.Grid({
		loadURL : ctx + '/workStation!findPageList.action', // 装载列表数据的请求URL
		saveURL : ctx + '/workStation!saveOrUpdate.action', // 保存数据的请求URL
		// deleteURL: ctx + '/workStation!logicDelete.action', //删除数据的请求URL
		searchFormColNum : 1,
		saveFormColNum : 2,
		tbar : ['search', 'add', '-', /*{ 
			text:"启用", iconCls:"acceptIcon", handler:function(){
				WorkStation.updateStatus(stationStatus_new, WorkStation.grid, 'start'); 
			} 
	 	},*/ {
			text : "作废",
			iconCls : "dustbinIcon",
			handler : function() {
				WorkStation.updateStatus(stationStatus_use, WorkStation.grid, 'invalid');
			}
		}, '-', { xtype : 'label', text : '状态：' },  {
			xtype:'checkbox', name:'status', id:'stationStatus_new',
			boxLabel:'新增&nbsp;&nbsp;&nbsp;&nbsp;',
			inputValue:stationStatus_new, checked:true,
			handler:function(){ 
				WorkStation.checkQuery(status_new); 
			} 
		 }, {
			xtype : 'checkbox', name : 'status', id : 'stationStatus_use', boxLabel : '启用&nbsp;&nbsp;&nbsp;&nbsp;',
			inputValue : stationStatus_use, checked : true,
			handler : function() {
				WorkStation.checkQuery(status_use);
			}
		}, {
			xtype : 'checkbox', name : 'status', id : 'stationStatus_nullify', boxLabel : '作废',
			inputValue : stationStatus_nullify,
			handler : function() {
				WorkStation.checkQuery(status_nullify);
			}
		}, '-', {
			text : "关闭",
			iconCls : "closeIcon",
			handler : function() {
				RepairLine.grid.saveWin.hide();
			}
		}],
		fields : [{
			header : 'idx主键', dataIndex : 'idx', hidden : true, editor : {
				id : 'workStationIdx', xtype : 'hidden'
			}
		}, {
			header : '工位编码', dataIndex : 'workStationCode', editor : {
				id : 'workStationCode_Id', allowBlank : false, maxLength : 50
			}
		}, {
			header : '工位名称', dataIndex : 'workStationName', editor : {
				id : 'workStationName_Id', allowBlank : false, maxLength : 100
			}
		}, {
			header : '流水线主键', dataIndex : 'repairLineIdx', hidden : true, editor : {
				xtype : 'hidden', maxLength : 50
			}
		}, {
			header : '所属流水线', dataIndex : 'repairLineName', hidden : true, editor : {
				xtype : 'hidden', maxLength : 100
			}
		}, {
			header : '所属台位编码', dataIndex : 'deskCode', hidden : true, editor : {
				xtype : 'hidden', id : 'deskCode'
			}
		}, {
			header : '所属图', dataIndex : 'ownerMap', hidden : true, editor : {
				xtype : 'hidden', id : 'ownerMap'
			}
		}, {
			header : '所属台位', dataIndex : 'deskName', hidden : true,
			editor : {
				xtype : 'hidden'/*,
				id:"TWTStation_Combo",
				xtype: 'Base_combo',
				hiddenName: 'deskName',minListWidth:220,
				entity:'com.yunda.twt.twtinfo.entity.SiteStation',
				queryName:"stationName",
				editable:true,
				typeAhead : true,
				forceSelection : true,
				fields:
				["idx","stationCode","stationName","siteID"],displayField:'stationName',valueField:'stationName',
				returnField:[{widgetId:"deskCode",propertyName:"idx"},
				{widgetId:"ownerMap",propertyName:"siteID"}]*/
			},
			searcher : { disabled : true }
//				}, {
//					header : '机务设备主键', dataIndex : 'equipIDX', hidden : true, editor : {
//						xtype : 'hidden', id : "equipIDX"
//					}
//				}, {
//					header : '机务设备', dataIndex : 'equipName', editor : {
//						id : 'equipName_Id',
//						xtype : 'Base_combo',
//						hiddenName : 'equipName',
//						minListWidth : 220,
//						entity : 'com.yunda.jxpz.equipinfo.entity.EquipInfo',
//						editable : false,
//						typeAhead : true,
//						forceSelection : true,
//						queryHql : "from EquipInfo where equipType = 'train'",
//						fields : ["idx", "equipCode", "equipName", "equipType"],
//						displayField : 'equipName',
//						valueField : 'equipName',
//						returnField : [{
//									widgetId : "equipIDX",
//									propertyName : "idx"
//								}]
//					},
//					searcher : { disabled : true }
		}, {
			header : '状态', dataIndex : 'status', renderer : function(v) {
				if (v == stationStatus_new) return "新增";
				if (v == stationStatus_use) return "启用";
				return "作废";
			},
			editor : {
				xtype : 'hidden', value : stationStatus_new
			},
			searcher : { disabled : true }
		}, {
			header: '作业班组', dataIndex: 'teamOrgId', hidden: true, editor: {
				//allowBlank: false,
				id:'team_org_id',
				xtype: 'OmOrganizationCustom_comboTree', 
			    hiddenName: 'teamOrgId', 
			    returnField: [{
			    	widgetId:"teamorgname",propertyName:"orgname"
		    	}, {
			    	widgetId:"teamorgseq",propertyName:"orgseq"
		    	}], selectNodeModel:'all',							   
			  	queryHql: '[degree]tream',
			  	editable: true
			}
		}, {
			header: '作业班组名称', dataIndex: 'teamOrgName', editor: {
				id:'teamorgname', xtype: 'hidden'
			}
		}, {
			header: '作业班组序列', dataIndex: 'teamOrgSeq', hidden: true, editor: {
				id: 'teamorgseq', xtype: 'hidden'
			}
		}, {
			header : '备注', dataIndex : 'remarks',  hidden:true, editor : {
				xtype : 'textarea', maxLength : 1000
			},
			searcher : { disabled : true }
		}, {
			header : '上级主键', dataIndex : 'parentIDX', hidden: true, editor: {
				xtype:'hidden' 
			},
			searcher : { disabled : true }
		}],
		deleteButtonFn : WorkStation.deleteFn,
		/**
		 * 显示新增窗口后触发该函数，该函数依赖addButtonFn，若默认addButtonFn被覆盖则失效
		 */
		afterShowSaveWin : function() {
			Ext.getCmp("workStation_Tabs").activate("workStationForm_tabId");
			Ext.getCmp("workStation_Tabs").getItem("teamOrg_tabId").disable();
			// 设置上级主键字段值
			this.saveForm.find('name', 'parentIDX')[0].setValue("ROOT_0" == WorkStation.parentIDX ? "" : WorkStation.parentIDX);
			// TODO
//			Ext.getCmp("equipName_Id").clearValue();
//		 	Ext.getCmp("TWTStation_Combo").clearValue();
			// 清空“作业班组”字段值
		 	Ext.getCmp("team_org_id").clearValue();
		},
		/**
		 * 进入新增窗口之前触发的函数，如果返回false将不显示新增窗口 设置【工位作业人员】tab禁用，清空控件显示值
		 * 该函数依赖saveFn，若默认saveFn被覆盖则失效
		 */
		beforeShowSaveWin : function() {
			WorkStation.getWorkStationCode();
			this.enableAllColumns();
			return true;
		},
		/**
		 * 保存记录之前的触发动作，如果返回fasle将不保存记录，该函数依赖saveFn触发，如果saveFn被覆盖则失效 设置“流水线主键”
		 * 
		 * @param {Object}
		 *            data 要保存的数据记录，json格式
		 * @return {Boolean} 如果返回fasle将不保存记录
		 */
		beforeSaveFn : function(data) {
			data.repairLineIdx = RepairLine.idx;
			data.repairLineName = RepairLine.repairLineName;
			return true;
		},
		/**
		 * 获取表单数据前触发(覆盖该函数可用于修改表单字段enable、disabled等)，
		 * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
		 */
		beforeGetFormData : function() {
			this.enableColumns(['workStationCode', 'workStationName']);
		},

		/**
		 * 保存成功之后执行的函数，该函数依赖saveFn触发，如果saveFn被覆盖则失效
		 * 
		 * @param {}
		 *            result 服务器端返回的json对象
		 * @param {}
		 *            response 原生的服务器端返回响应对象
		 * @param {}
		 *            options 参数项
		 */
		afterSaveSuccessFn : function(result, response, options) {
			this.store.load();
			alertSuccess();
			var result = Ext.util.JSON.decode(response.responseText);
			// 获取保存成功后的“工位”主键并保存到全局变量
			WorkStation.idx = result.entity.idx;
			WorkStation.orgseq = result.entity.teamOrgSeq;
			RepairLine.workStationIDX = result.entity.idx; // 设置工位主键
			WorkStation.workStationName = result.entity.workStationName;
			WorkStation.grid.saveForm.getForm().getValues().idx = result.entity.idx;
			Ext.getCmp("workStationIdx").setValue(result.entity.idx);
			this.saveWin.setTitle('编辑');
			// 状态为启用状态
			if (result.entity.status == stationStatus_use) {
				// 禁用“工位编码”
				this.disableColumns(['workStationCode']);
				// 设置【默认作业人员】tab可用
				Ext.getCmp("workStation_Tabs").getItem("teamOrg_tabId").enable();
				Ext.getCmp("workStation_Tabs").getItem("workStationForm_tabId").enable();
			}
			
			// 重新加载工位树
			WorkStation.reloadTree(WorkStation.treePath);
		},
		
		/**
		 * 选中记录加载到编辑表单，显示编辑窗口后触发该函数 该函数依赖toEditFn，若默认toEditFn被覆盖则失效
		 * @param {Ext.data.Record} record 当前选中记录
		 * @param {Number} rowIndex 选中行下标
		 */
		afterShowEditWin : function(record, rowIndex) {
			// TODO
//			Ext.getCmp("TWTStation_Combo").setDisplayValue(record.get("deskName"),record.get("deskName"));
//			Ext.getCmp("equipName_Id").setDisplayValue(record.get("equipName"), record.get("equipName"));
			// 回显“作业班组”字段值
			Ext.getCmp("team_org_id").setDisplayValue(record.get("teamOrgId"),record.get("teamOrgName"));
			
			// 设置“工位主键”全局变量
			WorkStation.idx = record.get("idx");
			WorkStation.orgseq = record.get("teamOrgSeq");
			RepairLine.workStationIDX = record.get("idx"); 	// 工位主键
			StationUnionWorker.grid.store.load();				// 刷新作业班组grid

			WorkStation.workStationName = record.get("workStationName");
			// 状态为启用则不可编辑工位编码及工位名称并显示【工位作业人员】
			if (record.get("status") == stationStatus_use) {
				this.disableColumns(['workStationCode']);
				// 设置【默认作业人员】tab可用
				Ext.getCmp("workStation_Tabs").getItem("teamOrg_tabId").enable();
				Ext.getCmp("workStation_Tabs").activate("workStationForm_tabId");
			}
			// 状态为作废则不可编辑工位表单、禁用【工位作业人员】菜单
			else if (record.get("status") == stationStatus_nullify) {
				this.disableAllColumns();
				// 屏蔽“保存”“启用”按钮
				this.saveForm.buttons[0].setVisible(false);
				this.saveForm.buttons[1].setVisible(false);
				Ext.getCmp("workStation_Tabs").getItem("teamOrg_tabId").disable();
				Ext.getCmp("workStation_Tabs").activate("workStationForm_tabId");
			}
			return true;
		},
		
		/**
		 * 查询前获取查询表单参数，此函数依赖searchForm（查询窗口是否创建）（可覆盖此方法重构查询）
		 * @param searchParam 查询表单的Json对象
		 * @return {} 返回的Json数据格式对象,
		 */
		searchFn : function(searchParam) {
			// 清空查询条件参数
			WorkStation.searchParams = {};
			searchParam.repairLineIdx = RepairLine.idx;
			delete searchParam["status"];
			// 将查询表单数据设置到全局查询参数集
			for (prop in searchParam) {
				WorkStation.searchParams[prop] = searchParam[prop];
			}
			// 将全局查询参数集设置到baseParams，解决分页控件刷新
			this.store.baseParams.entityJson = Ext.encode(WorkStation.searchParams);
			// 访问后台，根据查询参数刷新列表
			this.store.load({
				params : {
					entityJson : Ext.util.JSON.encode(searchParam),
					status : WorkStation.status
				}
			});
		}
	});
	// 默认以工位名称进行升序排序
	WorkStation.grid.store.sort('workStationName', 'ASC');
	// 创建表单
	WorkStation.grid.createSaveForm();
	// 创建完成表单后给表单添加操作按钮
	WorkStation.grid.saveForm.buttonAlign = "center";
	WorkStation.grid.saveForm.addButton({
		text : "保存",
		iconCls : "saveIcon",
		handler : function() {
			// 表单验证是否通过
			var form = WorkStation.grid.saveForm.getForm();
			if (!form.isValid()) return;

			// 获取表单数据前触发函数
			WorkStation.grid.beforeGetFormData();
			var data = form.getValues();
			// 获取表单数据后触发函数
			WorkStation.grid.disableColumns(['workStationCode']);
			data.status = stationStatus_use;
			// 调用保存前触发函数，如果返回fasle将不保存记录
			if (!WorkStation.grid.beforeSaveFn(data)) return;

			if (self.loadMask)
				self.loadMask.show();
			var cfg = {
				url : WorkStation.grid.saveURL,
				jsonData : data,
				success : function(response, options) {
					if (self.loadMask) self.loadMask.hide();
					var result = Ext.decode(response.responseText);
					if (result.errMsg == null) {
						WorkStation.grid.afterSaveSuccessFn(result, response, options);
						RepairLine.workStationIDX = result.entity.idx; // 工位主键IDX
						StationUnionWorker.grid.store.load();
					} else {
						WorkStation.grid.afterSaveFailFn(result, response, options);
					}
				}
			};
			Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		}
	});
	WorkStation.grid.saveForm.addButton({
		text : "关闭", iconCls : "closeIcon", handler : function() {
			WorkStation.grid.saveWin.hide();
		}
	});
	WorkStation.grid.saveForm.style = "padding:30px",
	// 重构新增编辑窗口
	WorkStation.grid.saveWin = new Ext.Window({
		title : "新增", maximizable : true,
		width : 620, height : 380,
		layout : "fit", closeAction : "hide", 
		plain : true, modal : true,
		items : [{
			xtype : "tabpanel",
			id : "workStation_Tabs",
			border : false,
			activeTab : 0,
			enableTabScroll : true,
			items : [{
				title : "工位",  id : "workStationForm_tabId", layout : "fit", frame : true, border : false,
				items : WorkStation.grid.saveForm
			}, {
				title : "默认作业人员", id : "teamOrg_tabId", layout : "fit", border : false,
				items : StationUnionWorker.grid
			}]
		}]
	});
	// 设置查询窗口为模态窗口
	WorkStation.grid.createSearchWin();
	WorkStation.grid.searchWin.modal = true;
	// 状态全局变量
	WorkStation.status = stationStatus_use;
	// 状态多选按钮
	WorkStation.checkQuery = function(status) {
		WorkStation.status = "-1";
		if (Ext.getCmp("stationStatus_use").checked) {
			WorkStation.status = WorkStation.status + "," + stationStatus_use;
		}
		if (Ext.getCmp("stationStatus_nullify").checked) {
			WorkStation.status = WorkStation.status + "," + stationStatus_nullify;
		}
		WorkStation.grid.store.load({
			params : {
				entityJson : Ext.util.JSON
						.encode(WorkStation.searchParams)
			}
		});
	}
	
	// store载入前查询，为了分页查询时不至于出差错
	WorkStation.grid.store.on("beforeload", function() {
		WorkStation.searchParams.repairLineIdx = RepairLine.idx;
		this.baseParams.status = WorkStation.status;
		this.baseParams.entityJson = Ext.encode(WorkStation.searchParams);
	});
	
	// 清空查询参数集并刷新列表
	WorkStation.clearSearchParams = function(_grid) {
		_grid.store.load({
			params : {
				entityJson : Ext.encode({ repairLineIdx : RepairLine.idx }),
				status : ""
			}
		});
		_grid.store.baseParams.entityJson = Ext.encode({
			repairLineIdx : RepairLine.idx
		});
		_grid.store.baseParams.status = "";
		WorkStation.searchParams = {};
	};
	
});