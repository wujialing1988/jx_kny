/**
 * 视频监控信息维护 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('SiteVideoNvrChanel');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	SiteVideoNvrChanel.labelWidth = 100;
	SiteVideoNvrChanel.fieldWidth = 140;
	
	SiteVideoNvrChanel.videoNvrIDX = "";					// 网络录像机idx主键
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 获取当前NVR下已配置的最大通道号
	SiteVideoNvrChanel.maxChanelIDFn = function() {
		var maxChanelID = 0
		SiteVideoNvrChanel.grid.store.each(function(record){
			if (record.get('chanelID') > maxChanelID) {
				maxChanelID = record.get('chanelID');
			}
		});
		return maxChanelID + 1;
	}
	 // 删除（批量删除）函数
	SiteVideoNvrChanel.deleteFn = function(idx) {
		var ids = null;
		if (idx instanceof Array) {
			ids = idx;
		} else {
			ids = [idx];
		}
		
		 //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
        $yd.confirmAndDelete({
            scope: SiteVideoNvrChanel.grid, 
            url: ctx + '/siteVideoNvrChanel!delete.action',
			params: {
				ids: ids
			}
        });
	}
	// 解除绑定
	SiteVideoNvrChanel.unBindFn = function() {
		var sm = SiteVideoNvrChanel.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			MyExt.Msg.alert('尚未选择任何记录！');
			return;
		}
		var ids = [];
		var records = sm.getSelections();
		Ext.each(records, function(record){
			if (!Ext.isEmpty(record.get('videoCode'))) {
				ids.push(record.get('idx'));
			}
		});
		if (ids.length <= 0) {
			MyExt.Msg.alert('已选择的记录中没有绑定信息，请重新选择!');
			return;
		}
		Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
        if(btn != 'yes')    return;
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
	        	url: ctx + '/siteVideoNvrChanel!unBind.action',
	        	params: {ids : ids},
	        	scope: SiteVideoNvrChanel.grid
	        }));
	    });    
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义视频监控NVR表格开始 **************** */
	SiteVideoNvrChanel.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + '/siteVideoNvrChanel!pageList.action',                 // 装载列表数据的请求URL
	    saveURL: ctx + '/siteVideoNvrChanel!saveOrUpdate.action',             // 保存数据的请求URL
	    deleteURL: ctx + '/siteVideoNvrChanel!delete.action',            		// 删除数据的请求URL
	    storeAutoLoad: false,
	    tbar: [{
	    	text:'批量新增', iconCls: 'addIcon', handler: function() {
	    		Ext.Msg.prompt('初始化通道', '请输入要初始化的通道数量:<br/>', function(btn, value, dialog){
	    			if ('ok' != btn) {
	    				return;
	    			}
	    			var inputValue = parseInt(value);
	    			if (inputValue && inputValue > 0) {
	    				Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
	    					scope: SiteVideoNvrChanel.grid,
	    					url: ctx + "/siteVideoNvrChanel!init.action",
	    					params: {
	    						initNum: inputValue,
	    						videoNvrIDX: SiteVideoNvrChanel.videoNvrIDX
	    					}
	    				}));
	    			} else {
	    				MyExt.Msg.alert("请输入合法的正整数！");
	    			}
	    		}, this, false, 16);
	    	}
	    }, 'add', 'delete', '->', {
			text: '解除绑定', id: 'id_unbind_btn', iconCls: 'disconnectIcon', handler: SiteVideoNvrChanel.unBindFn	    	
	    }],
		fields: [{
			header:'操作', dataIndex:'idx', hidden:true, editor: {xtype:'hidden' }
		},{
			header:'网络录像机idx主键', dataIndex:'videoNvrIDX', hidden:true, editor: {id:'id_videoNvrIDX', xtype:'hidden' }
		},{
			header:'通道号', dataIndex:'chanelID', editor:{ id:'id_chanelID', maxLength:5, allowBlank: false, vtype:'positiveInt1To100' }
		},{
			header:'通道名称', dataIndex:'chanelName', editor:{ id:'id_chanelName', maxLength:50, allowBlank: false }
		},{
			header:'站场', dataIndex:'siteID', hidden:true, editor:{ xtype:'hidden' }
		},{
			header:'视频点编码', dataIndex:'videoCode', editor:{ disabled:true }
		},{
			header:'视频点名称', dataIndex:'videoName', editor:{ disabled:true }
		},{
			header:'操作', align:'center', width: 60, hidden:true, renderer: function(value, metaData, record){
				return "<img src='" + deleteIcon + "' alt='删除' style='cursor:pointer' onclick='SiteVideoNvrChanel.deleteFn(\""+ record.get('idx') +"\")'/>";
			}
		}],
		afterAddButtonFn: function(){
			var maxChanelID = SiteVideoNvrChanel.maxChanelIDFn();
			Ext.getCmp('id_chanelID').setValue(maxChanelID);
			Ext.getCmp('id_chanelName').setValue("通道" + maxChanelID);
			Ext.getCmp('id_videoNvrIDX').setValue(SiteVideoNvrChanel.videoNvrIDX);
		},
		deleteButtonFn: function(){                         //点击删除按钮触发的函数，默认执行删除操作
	        //未选择记录，直接返回
	        if(!$yd.isSelectedRecord(this)) return;
	        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
	        if(!this.beforeDeleteFn()) return;
	        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
	        SiteVideoNvrChanel.deleteFn($yd.getSelectedIdx(this));
	    }
	});
	
	// 默认以NVR名称升序排序
	SiteVideoNvrChanel.grid.store.setDefaultSort('chanelID', 'ASC');
	
	// 查询条件传递
	SiteVideoNvrChanel.grid.store.on('beforeload', function(){
		var searchParams = {};
		searchParams.videoNvrIDX = SiteVideoNvrChanel.videoNvrIDX;
		searchParams = MyJson.deleteBlankProp(searchParams);
		this.baseParams.entityJson = Ext.encode(searchParams);
	});
		
	// 数据加载完成后，自动选择第一条记录
	SiteVideoNvrChanel.grid.store.on('load', function(store, records, options){
		if (records.length <= 0) {
			return;
		}
		SiteVideoNvrChanel.grid.getSelectionModel().selectFirstRow();
	});
		
	/** **************** 定义视频监控NVR表格结束 **************** */
});