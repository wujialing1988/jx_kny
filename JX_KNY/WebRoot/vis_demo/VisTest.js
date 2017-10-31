Ext.onReady(function() {

	Ext.ns('VisTest');

	/** **************** 定义全局函数开始 **************** */
	// 工作项新增时的函数处理，用于存储信息到数据库
	VisTest.itemsAddFn = function(item) {
		// 声明一个"机车台位任务"对象JSON，用于封装被更新的信息
		var platformTaskItem = {};
		platformTaskItem.idx = item.id;
		platformTaskItem.status = item.status;
		platformTaskItem.platformTaskIdx = item.group;
		platformTaskItem.startTime = item.start;
		platformTaskItem.endTime = item.end;
		platformTaskItem.displayInfo = item.content;

		console.log(platformTaskItem);

		// Ajax请求
		if (self.loadMask) self.loadMask.show();
		Ext.Ajax.request({
			url : ctx + '/platformTaskItem!add.action',
			jsonData : Ext.util.JSON.encode(platformTaskItem),
			// 请求成功后的回调函数
			success : function(response, options) {
				if (self.loadMask) self.loadMask.hide();
				var result = Ext.util.JSON.decode(response.responseText);
				if (result.errMsg == null) { // 操作成功
					alertSuccess("新增成功");
					 VisTest.store.reload();
				} else { // 操作失败
					alertFail(result.errMsg);
				}
			},
			// 请求失败后的回调函数
			failure : function(response, options) {
				if (self.loadMask) self.loadMask.hide();
				Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}

	// 工作项删除时的函数处理
	VisTest.itemsDeleteFn = function(ids) {
		// var items = VisTest.items.get(ids);
		// for (var i = 0; i < items.length; i++) {
		// if (items[i].status = STATUS_WKG) {
		// MyExt.Msg.alert('未开工的工作项不能删除！');
		// }
		// return;
		// }
		// Ajax请求
		if (self.loadMask) self.loadMask.show();
		Ext.Ajax.request({
			url : ctx + '/platformTaskItem!logicDelete.action',
			params : {
				ids : ids
			},
			// 请求成功后的回调函数
			success : function(response, options) {
				if (self.loadMask) self.loadMask.hide();
				var result = Ext.util.JSON.decode(response.responseText);
				if (result.errMsg == null) { // 操作成功
					alertSuccess("删除成功");
				} else { // 操作失败
					alertFail(result.errMsg);
				}
			},
			// 请求失败后的回调函数
			failure : function(response, options) {
				if (self.loadMask) self.loadMask.hide();
				Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}

	// 工作项更新时的函数处理，用于存储信息到数据库
	VisTest.itemsUpdateEvent = function(event, properties) {
		// VisTest.itemsUpdateFn(properties.data);
		var items = properties.data
		var array = [];
		for (var i = 0; i < items.length; i++) {
			// 声明一个"机车台位任务"对象JSON，用于封装被更新的信息
			console.log(items[i]);
			var platformTaskItem = {};
			platformTaskItem.idx = items[i].id;
			platformTaskItem.displayInfo = items[i].content;
			platformTaskItem.platformTaskIdx = items[i].group;
			platformTaskItem.startTime = items[i].start;
			platformTaskItem.endTime = items[i].end;
			platformTaskItem.status = items[i].status;
			array.push(platformTaskItem);
		}
		// Ajax请求
		if (self.loadMask)
			self.loadMask.show();
		Ext.Ajax.request({
			scope: VisTest.store,
			url : ctx + '/platformTaskItem!update.action',
			jsonData : Ext.util.JSON.encode(array),
			// 请求成功后的回调函数
			success : function(response, options) {
				if (self.loadMask)
					self.loadMask.hide();
				var result = Ext.util.JSON
						.decode(response.responseText);
				if (result.errMsg == null) { // 操作成功
					alertSuccess("更新成功");
					this.reload();
				} else { // 操作失败
					alertFail(result.errMsg);
				}
			},
			// 请求失败后的回调函数
			failure : function(response, options) {
				if (self.loadMask) self.loadMask.hide();
				Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});

	}

	// 批量更新机车台位任务状态【批量开工】，【批量完工】
	VisTest.updateStatusFn = function(ids, status) {
		// Ajax请求
		if (self.loadMask)
			self.loadMask.show();
		Ext.Ajax.request({
					url : ctx + '/platformTaskItem!updateStatus.action',
					params : {
						ids : ids,
						status : status
					},
					// 请求成功后的回调函数
					success : function(response, options) {
						if (self.loadMask)
							self.loadMask.hide();
						var result = Ext.util.JSON
								.decode(response.responseText);
						if (result.errMsg == null) { // 操作成功
							alertSuccess();
							VisTest.store.reload();
						} else { // 操作失败
							alertFail(result.errMsg);
						}
					},
					// 请求失败后的回调函数
					failure : function(response, options) {
						if (self.loadMask)
							self.loadMask.hide();
						Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status
										+ "\n" + response.responseText);
					}
				});

	}
	
	// 显示详情
	VisTest.showDetailFn = function(idx) {
		var win = new Ext.Window({
			title: '任务详情',
			padding: 10,
			height: 200,
			width: 300,
			html: ['idx: ', idx].join(''),
			buttonAlign: 'center',
			buttons: [{
				text: '关闭', handler: function() {
					this.findParentByType('window').hide();
				}
			}]
//			closeAction: 'hide'
		});
		win.show();
	}
	/** **************** 定义全局函数结束 **************** */

	/** **************** 定义全局变量开始 **************** */
	VisTest.items = new vis.DataSet();
	VisTest.groups = new vis.DataSet();
	VisTest.timeline = null;
	VisTest.options = {
		// template: function (item) {
		// if (!item) {
		// return "新任务";
		// }
		// return '<h1>' + item.status + '</h1><p>' + item.content + '</p>';
		// },

//		groupOrder: 'content',
		// 新增工作项的事件监听
		onAdd : function(item, callback) {
			Ext.Msg.prompt('提示', '请输入任务名称', function(btn, value) {
						if ('ok' == btn) {
							item.className = 'wkg';
							item.content = value;
							item.status = STATUS_WKG; // 增加扩展字段【status】，并赋初值为：未开工，否则在更新时会因获取不到任务的状态值而发生异常
							item.id = item.id.replace(/\-/g, '');
							callback(item); // send back adjusted new item
							// 执行数据库新增
							VisTest.itemsAddFn(item);
						} else {
							callback(null);
						}
					}, null, null, '新任务');
		},

		// 更新工作项的事件监听
		onUpdate : function(item, callback) {
			PlatformTaskItem.item = item;
			PlatformTaskItem.saveWin.setTitle('编辑');
			PlatformTaskItem.saveWin.show();
			// Ext.Msg.prompt('提示', '请输入任务名称', function(btn, value){
			// if ('ok' != btn) {
			// return;
			// }
			// item.content = value;
			// callback(item); // send back adjusted new item
			// }, null, null, item.content);
		},

		// 删除工作项的事件监听
		onRemove : function(item, callback) {
			Ext.Msg.confirm('提示', '是否确认删除？', function(btn) {
						if ('yes' != btn) {
							return;
						}
						callback(item);
						var ids = [];
						ids.push(item.id);
						// 执行数据库删除
						VisTest.itemsDeleteFn(ids);
					});
		}

	};
	/** **************** 定义全局变量结束 **************** */

	/** **************** 定义数据加载的函数处理开始 **************** */
	VisTest.storeLoadFn = function() {
		var count = this.getCount();
		for (var i = 0; i < count; i++) {
			var record = this.getAt(i);
			var groupId = record.get('idx');
			// 添加分组信息
//			var groupContent = "<a href='#' style='color:#fff;' onclick='VisTest.showDetailFn(\""+ groupId + "\")'>" + record.get('taskName') + "</a>";
			VisTest.groups.update({
						id : groupId,
						taskName : record.get('taskName'),
						content :  i + 1 + "、<a href='#' onclick='VisTest.showDetailFn(\""+ groupId + "\")'>" + record.get('taskName') + "</a>", 
						title : 'hetao'
					});

			var items = record.get('taskItems');
			for (var j = 0; j < items.length; j++) {
				var item = items[j];
				// 根据任务完成状态设置不同的显示样式
				var className = "";
				if (item.status == STATUS_WKG) {
					className = 'wkg';
				} else if (item.status == STATUS_YKG) {
					className = 'ykg';
				} else if (item.status == STATUS_YYQ) {
					className = 'yyq';
				} else {
					className = 'ywg';
				}
				var time = null;
				if (!Ext.isEmpty(item.endTime)) {
					var start = new Date(item.startTime).getTime();
					var end = new Date(item.endTime).getTime();
					time = (end - start) / 1000 / 60;
				}
				var content = Ext.isEmpty(item.displayInfo)
									? item.trainType + " " + item.trainNo
									: item.displayInfo;
				content = "<a href='#' style='color:#fff;' onclick='VisTest.showDetailFn(\""+ item.idx + "\")'>" + content + "</a>";
				if (null != time) {
					content += "<br/><span class='peroid'>历时：" + time + "分钟</span>"
				}
				var s = VisTest.items.update({
							id : item.idx,
							title : item.name,
							className : className,
							group : groupId,
							content : content,
							title : "车型：" + item.trainType + "\r\n车号："
									+ item.trainNo,
							start : item.startTime,
							end : item.endTime,
							status : item.status,
							displayInfo : item.displayInfo,
							trainTypeIdx : item.trainTypeIdx,
							trainType : item.trainType,
							trainNo : item.trainNo
						});
			}

		}
		// /////////
		if (!VisTest.timeline) {
			VisTest.timeline = new vis.Timeline(
				document.getElementById('visualization'), 
				VisTest.items, 
				VisTest.groups, 
				Ext.apply({}, VisTest.options, VisUtil.options)
			);
			// 数据项选择的事件监听
			 VisTest.timeline.on('select', function(properties){
			 	var items = properties.items;
			 	console.log(items.length);
			 	for (var i = 0; i < items.length; i++) {
//			 		MyExt.Msg.alert(VisTest.items.get(items[i]));
			 		console.log(VisTest.items.get(items[i]));
			 	}
			 });
		}

		// 数据加载成功后，添加工作项的更新事件监听
		VisTest.items.on('update', VisTest.itemsUpdateEvent);
	}
	/** **************** 定义数据加载的函数处理结束 **************** */

	/** **************** 定义数据容器开始 **************** */
	VisTest.store = new Ext.data.JsonStore({
		id : 'idx',
		root : "root",
		totalProperty : "totalProperty",
		autoLoad : true,
		remoteSort : false,
		url : ctx + '/platformTask!findPageList.action',
		fields : ["idx", "taskName", "taskItems", "seqNo"],
		sortInfo : {
			field : 'seqNo',
			direction : 'ASC'
		},
		listeners : {
			beforeload : function() {
				// 因为数据实时更新应用的是DataSet的update方法，因为在数据加载时，要撤销工作项的update事件监听
				VisTest.items.off('update', VisTest.itemsUpdateEvent);
			},
			// 数据加载完成后的函数处理
			load : VisTest.storeLoadFn
		}
	});
	/** **************** 定义数据容器结束 **************** */

	/** **************** 更新时间轴的定时器定义开始 **************** */
	// 页面初始化完成后延迟3秒启动定时器
	var delay = new Ext.util.DelayedTask(function() {
		Ext.TaskMgr.start({
			run : function() {
//				MyExt.Msg.alert('数据加载成功!');
				// 加载数据
				VisTest.store.reload();
			},
			interval : 1000 * 30
		});
	});
	// 延迟3秒
	delay.delay(3000);
	/** **************** 更新时间轴的定时器定义结束 **************** */

	// 页面自适应布局
	new Ext.Viewport({
		layout : 'fit',
		items : [{
			autoScroll : true,
			tbar : [{
				text : '批量删除',
				iconCls : 'deleteIcon',
				handler : function() {
					var ids = VisTest.timeline.getSelection();
					if (ids.length <= 0) {
						MyExt.Msg
								.alert('请先选择要删除的工作项，多选请配合使用[<span style="color:red; font-weight:bold;">Ctrl</span>]或者[<span style="color:red; font-weight:bold;">Shift</span>]键！');
						return;
					}
					Ext.Msg.confirm('提示', '是否确认删除？', function(btn) {
						if ('yes' != btn) {
							return;
						}
						// 数据库逻辑删除
						VisTest.itemsDeleteFn(ids);
						// 界面删除
						VisTest.items.remove(ids);
					});
				}
			}, {
				text : '批量开工',
				iconCls : 'startIcon',
				handler : function() {
					var ids = VisTest.timeline.getSelection();
					if (ids.length <= 0) {
						MyExt.Msg.alert('请先选择工作项，多选请配合使用[<span style="color:red; font-weight:bold;">Ctrl</span>]或者[<span style="color:red; font-weight:bold;">Shift</span>]键！');
						return;
					}
					VisTest.updateStatusFn(ids, STATUS_YKG);
				}
			}, {
				text : '批量完工',
				iconCls : 'yesIcon',
				handler : function() {
					var ids = VisTest.timeline.getSelection();
					if (ids.length <= 0) {
						MyExt.Msg.alert('请先选择工作项，多选请配合使用[<span style="color:red; font-weight:bold;">Ctrl</span>]或者[<span style="color:red; font-weight:bold;">Shift</span>]键！');
						return;
					}
					VisTest.updateStatusFn(ids, STATUS_YWG);
				}
			}, {
				text : '刷新',
				iconCls : 'refreshIcon',
				handler : function() {
					self.location.reload();
				}
			}, '->', '语言：', {
				xtype : 'combo',
				width : 70,
				store : new Ext.data.ArrayStore({
							fields : ['K', 'V'],
							data : [['en', "英文"], ["zh-cn", "中文"]]
						}),
				valueField : 'K',
				displayField : 'V',
				triggerAction : 'all',
				value : "zh-cn",
				mode : 'local',
				listeners : {
					select : function(cmp, record, index) {
						var v = cmp.getValue();
						VisTest.timeline.setOptions({
									locale : v
								});
					}
				}
			}, '-', '状态说明：', '-', {
				xtype : 'label',
				text : '未开工',
				style : 'background-color:#999999;',
				height : 30
			}, '-', {
				xtype : 'label',
				text : '已开工',
				style : 'background-color:#fec80c;'
			}, '-', {
				xtype : 'label',
				text : '已延期',
				style : 'background-color:#ff0000;'
			}, '-', {
				xtype : 'label',
				text : '已完工',
				style : 'background-color:#008000;'
			}],
			html : ['<div id="visualization"></div>'].join(""),

			// 数据分页的特殊处理
			// bbar: $yd.createPagingToolbar({pageSize:50, store:
			// VisTest.store})
			bbar: function(){
			 	cfg = {pageSize:50, store: VisTest.store};
			    //配置分页工具栏，表格默认每页显示记录数
			    var pageSize = cfg.pageSize || 50;  
			    //每页显示条数下拉选择框
			    var pageComboBox = new Ext.form.ComboBox({
			        name: 'pagesize',     triggerAction: 'all',  mode : 'local',   width: 75,
			        valueField: 'value',  displayField: 'text',  value: pageSize,  editable: false,
			        store: new Ext.data.ArrayStore({
			            fields: ['value', 'text'],
			            data: [[10, '10条/页'], [20, '20条/页'], [50, '50条/页'], [100, '100条/页']]
			        })
			    });
			    // 改变每页显示条数reload数据
			    pageComboBox.on("select", function(comboBox) {
			        pagingToolbar.pageSize = parseInt(comboBox.getValue());
			        pagingToolbar.store.reload({
			            params: {
			                start: 0,    limit: pagingToolbar.pageSize
			            }
			        });
			    });
			    //一个新实例化表格的分页工具栏
			    var pagingToolbar = new Ext.PagingToolbar({
			        pageSize: pageSize,   emptyMsg: "没有符合条件的记录",
			        displayInfo: true,    displayMsg: '显示 {0} 条到 {1} 条,共 {2} 条',    
//			            plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
			        items: ['-', '&nbsp;&nbsp;', pageComboBox, '-', 
			        "&nbsp;时间轴控制：", {
						text: '自适应', iconCls: 'expandIcon', handler: function() {
							VisTest.timeline.fit();
						}
					}, {
						text: '选择居中', iconCls: 'centerIcon', handler: function() {
							var selections = VisTest.timeline.getSelection();
							if (selections.length <= 0) {
								MyExt.Msg.alert('尚未选择任何记录！');
								return;
							}
							VisTest.timeline.focus(selections);
						}
					}, {
						text: '当前日期', iconCls: 'returnIcon', handler: function() {
							// 时间轴显示到当前日期
							VisTest.timeline.setWindow(getDayStartTime(), getDayEndTime());
						}
					}, '-', {
						xtype: 'label', text: '显示到：', 
						style: [
							'padding-left:18px;',
							'background-positionX:5px;',
							'background-repeat: no-repeat;',
							'background-image: url("images/location.png");'
						].join('')
					} ,{
						xtype: 'combo', editable: false,
						width: 80,
						triggerAction: 'all',  mode : 'local',   
						valueField: 'value',  displayField: 'text', 
						store: new Ext.data.ArrayStore({
				            fields: ['value', 'text'],
				            data: [['week', '本周'], ['month', '本月'], ['quarter', '本季度'], ['year', '本年']]
				        }),
				        value: 'week',
				        listeners: {
						    // 改变每页显示条数reload数据
				        	select: function(comboBox) {
				        		if ('week' == comboBox.getValue()) {
									VisTest.timeline.setWindow(getWeekStartDate(), getWeekEndDate());
				        		}
				        		if ('month' == comboBox.getValue()) {
									VisTest.timeline.setWindow(getMonthStartDate(), getMonthEndDate());
				        		}
				        		if ('quarter' == comboBox.getValue()) {
									VisTest.timeline.setWindow(getQuarterStartDate(), getQuarterEndDate());
				        		}
				        		if ('year' == comboBox.getValue()) {
									VisTest.timeline.setWindow(getYearStartDate(), getYearEndDate());
				        		}
				        	}
				        }
					}],
			        listeners: {
			        	beforechange : function() {
			        		// 数据分页的特殊处理
						 VisTest.groups.clear();
						 VisTest.items.clear();
			        	}
			        }
			    });
			    pagingToolbar.pageComboBox = pageComboBox;
			    //分页工具栏绑定数据源
			    if(cfg.store != null) {
			        pagingToolbar.bind(cfg.store);
			        cfg.store.on('beforeload', function(store, options){
			            store.baseParams.limit = pagingToolbar.pageSize;
			        });
			    }
			    return pagingToolbar;
			 }()
		}]
	});

});
