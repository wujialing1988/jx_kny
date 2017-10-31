/**
 * 照片查看窗口
 */
Ext.onReady(function(){
	
	Ext.ns('ImageView');
	
	/** **************** 定义私有变量开始 **************** */
	var idx,			// 照片所属记录idx主键
		picIndex,
	    win;			// 照片查看窗口
	/** **************** 定义私有变量结束 **************** */
	
	/** **************** 定义私有函数开始 **************** */
	/**
	 * 获取（创建）找查看窗口
	 * @param winConfig : Object 照片查看窗口配置项
	 */
	function acquireWin(winConfig) {
		if (null != win) {
			return win;
		}
		win = new Ext.Window(Ext.apply({
			title: '照片查看',
	//		height: 520, width: 680,
			maximized: true,
			closeAction: 'hide',
			modal: true,
			html: ["<iframe frameborder='0' id='img_page' width='100%' height='100%'></iframe>"],
			buttonAlign: 'center',
			buttons: [{
				text: '关闭', iconCls: 'closeIcon', handler: function() {
					this.findParentByType('window').hide();
				}
			}],
			listeners: {
				show: function() {
					// 重定向iframe页面地址
					var url = ctx + "/attachment!images.action";
					url = Ext.urlAppend(url, "key=" + idx )
					url = Ext.urlAppend(url, "slideToStart=" + picIndex);
					if (this.businessName) {
						url = Ext.urlAppend(url, "businessName=" + this.businessName);
					}
					console.log(url);
					Ext.get('img_page').dom.src = url;
				}
			}
		}, winConfig));
		return win;
	}
	/** **************** 定义私有函数结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	/**
	 * 显示照片查看窗口
	 * @param grid 照片所属记录表格
	 */
	ImageView.show = function(grid, winConfig) {
		if (null == grid) {
			alertFail("未指定记录表格对象！");
			return;
		}
		var selections = grid.getSelectionModel().getSelections();
		if (0 >= selections.length) {
			MyExt.Msg.alert('尚未选择任何记录！');
			return;
		}
		if (1 != selections.length) {
			MyExt.Msg.alert('请只选择一条记录！');
			return;
		}
		// 设置照片查看idx主键
		idx = selections[0].id;
		acquireWin(winConfig).show();
	}
	
	/**
	 * 显示照片查看窗口
	 * @param key 附件组主键
	 * @param index 默认显示图片的下标
	 */
	ImageView.showByKey = function(key, index, winConfig) {
		if (!key) {
			alertFail("未指定记录表格对象！");
			return;
		}
		
		// 设置照片查看idx主键
		idx = key;
		picIndex = index ? index : 0;
		acquireWin(winConfig).show();
	}
	/** **************** 定义全局函数结束 **************** */
	
});