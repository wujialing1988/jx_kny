Ext.onReady(function(){
	Ext.ns('PointCheckScopeSelect');
	
	/** **************** 定义全局变量开始 **************** */
	PointCheckScopeSelect.checked = true;		// 默认只显示同类型的点检内容
	
	PointCheckScopeSelect.saveFn = function(classCode, datas) {
		if(self.loadMask)    self.loadMask.show();
		Ext.Ajax.request({
			url: ctx + '/pointCheckScope!save.action',
			params: {classCode: classCode, jsonData: Ext.encode(datas)},
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            PointCheckScopeSelect.grid.store.reload({
		            	// 添加完成后重新加载设备点检范围表格
		            	callback: function() {
				            PointCheckScope.grid.store.reload();
		            	}
		            });
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + ('' || response.responseText));
		    }
		});
	}
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义点检范围列表开始 **************** */
	PointCheckScopeSelect.grid = new Ext.yunda.Grid({
		loadURL : ctx + '/pointCheckScope!pageSelectQuery.action',
		pageSize: 50,
		storeAutoLoad : false,
		tbar : ['快捷查询：', {
			xtype : 'textfield', emptyText : '支持设备类别首拼快捷检索', width : 180,
			enableKeyEvents : true, listeners : {
				keyup : function( me, e ) {
					PointCheckScopeSelect.classNamePY = me.getValue();
					PointCheckScopeSelect.grid.store.load();
				}
			}
		}, '->', {
			xtype : 'checkbox', boxLabel : '只显示同类型的设备点检内容', checked : true,
			listeners : {
				check : function( me, checked ) {
					PointCheckScopeSelect.checked = checked
					PointCheckScopeSelect.grid.store.load();
				}
			}
		}],
		fields : [ {
			dataIndex : 'seqNo', header : '顺序号', hidden : true
		}, {
			dataIndex : 'classCode', header : '设备类别编码', hidden : true
		}, {
			dataIndex : 'className', header : '设备类别名称', width: 200, renderer: function(v, m, r) {
				return v + '(' + r.get('classCode') + ')';
			}
		}, {
			dataIndex : 'classNamePY', header : '设备类别名称首拼', hidden : true
		}, {
			dataIndex : 'checkContent', header : '点检内容', width : 280
		}, {
			dataIndex : 'checkContentPY', header : '点检内容首拼', hidden : true
		}],
		// 双击进行快速新增
		toEditFn : function(grid, rowIndex, e) {
			var record = this.store.getAt(rowIndex);
			PointCheckScopeSelect.saveFn(PointCheckScopeSelect.classCode, [record.data]);
		}
	});
	PointCheckScopeSelect.grid.store.on('beforeload', function(){
		this.baseParams.checked = PointCheckScopeSelect.checked;
		this.baseParams.entityJson = Ext.encode({
			classCode : PointCheckScopeSelect.classCode,
			classNamePY : PointCheckScopeSelect.classNamePY
		});
	});
	/** **************** 定义点检范围列表结束 **************** */

	PointCheckScopeSelect.win = new Ext.Window({
		title : '点检内容选择<span style = "color:green; font-weight:normal;">（可双击一条记录进行快速添加！）</span>',
		width : 750, height : 450,
		closeAction : 'hide',
		modal : true,
		layout : 'fit', 
		items : PointCheckScopeSelect.grid,
		listeners : {
			beforeshow : function(me) {
				var sm = PointCheckScope.tree.getSelectionModel();
				var node = sm.getSelectedNode();
				if (Ext.isEmpty(node)) {
					MyExt.Msg.alert('请先选择要进行维护的设备类别！');
					return false;
				}
				PointCheckScopeSelect.classCode = node.id;
				// 隐藏行编辑插件
				PointCheckScope.grid.rowEditor.slideHide();
				return true;
			},
			show : function() {
				PointCheckScopeSelect.grid.store.load();
			}
		},
		buttonAlign : 'center',
		buttons : [{
			text : '添加', iconCls : 'addIcon', handler : function() {
				var sm = PointCheckScopeSelect.grid.getSelectionModel();
				var selections = sm.getSelections();
				if (0 >= selections.length) {
					MyExt.Msg.alert('尚未选择任何记录！');
					return;
				}
				var datas = [];
				for (var i = 0; i < selections.length; i++) {
					datas.push(selections[i].data);
				}
				PointCheckScopeSelect.saveFn(PointCheckScopeSelect.classCode, datas);
			}
		}, {
			text : '关闭', iconCls : 'closeIcon', handler : function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
});