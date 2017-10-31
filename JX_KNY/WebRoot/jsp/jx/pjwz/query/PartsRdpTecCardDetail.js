/**
 * 配件检修记录卡实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpTecCardDetail');                       // 定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
    PartsRdpTecCardDetail.index = -1;							 // 【检修记录工单】列表索引
    PartsRdpTecCardDetail.record = null;							 // 【检修记录工单】记录对象
	/** **************** 定义全局变量结束 **************** */
    
    /** **************** 定义全局函数开始 **************** */
	/**
	 * 获取配件检修记录工单详情显示模板
	 */
	PartsRdpTecCardDetail.getDetailTpl = function() {
		if (Ext.isEmpty(PartsRdpTecCardDetail.tpl)) {
			PartsRdpTecCardDetail.tpl = new Ext.XTemplate(
				'<table class="pjjx-show-info-table">',
					'<tr>',
		                '<td class="pjjx-show-info-table-label" width="12%">识别码：</td><td width="15%">{identificationCode}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">配件编号：</td><td width="15%">{partsNo}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">配件名称：</td><td width="15%">{partsName}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">规格型号：</td><td width="15%">{specificationModel}</td>',
		            '</tr>',
					'<tr>',
		                '<td class="pjjx-show-info-table-label" width="12%">工单编号：</td><td width="15%">{tecCardNo}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">工单描述：</td><td width="15%" colspan="5">{tecCardDesc}</td>',
		            '</tr>',
				'</table>'
			);
		}
		return PartsRdpTecCardDetail.tpl;
	}
	
	/**
	 * 重新加载一个新的工单详情
	 */ 
	PartsRdpTecCardDetail.initFn = function() {
		// 获取检修记录工单
		PartsRdpTecCardDetail.record = PartsRdpTecCard.grid.store.getAt(PartsRdpTecCardDetail.index);
		var values = {},
			data = PartsRdpTecCardDetail.record.data;
		Ext.applyIf(values, data);						// 赋值检修记录工单属性
		Ext.applyIf(values, PartsRdpInfo.record.data);	// 赋值配件检修作业属性
		
		// 获取配件检修兑现单信息显示模板
		var tpl = PartsRdpTecCardDetail.getDetailTpl();
		tpl.overwrite(Ext.get('id_tec_card_detail'), values);
		
		// -------------->> 加载“作业任务”列表
		PartsRdpTecWS.rdpTecCardIDX = data.idx;
		PartsRdpTecWS.grid.store.load();
		
	}
	
	/**
	 * 动态生成检修检测项列表（含嵌套列表）
	 */
	PartsRdpTecCardDetail.loadRi = function(rdpTecCardIDX) {
		var dom = Ext.get('id_tec_card_ri_detail').dom;
		
//		if(self.loadMask)    self.loadMask.show();
		Ext.Ajax.request({
			url: ctx + '/partsRdpTecRI!queryInHTML.action',
			params:{ rdpTecCardIDX: rdpTecCardIDX },
		    //请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (!Ext.isEmpty(result.errMsg)) {       //操作成功     
		            dom.innerHTML = result.errMsg;
		        } else {                           //操作失败
		            dom.innerHTML = result.html;
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});
		
		
	}
	/** **************** 定义全局函数开始 **************** */
	
	PartsRdpTecCardDetail.win = new Ext.Window({
		title: '检修记录处理详情',
		maximized: true,
		closeAction: 'hide',
		tbar: [{
			text: '关闭', iconCls: 'closeIcon', handler: function(){
				this.findParentByType('window').hide();
			}
		}, '->', {
			text:'上一工单', iconCls:'moveUpIcon', id:'id_t_move_up_btn', handler: function(){
				if (0 == PartsRdpTecCardDetail.index) {
					MyExt.Msg.alert('已经是第一条工单！');
					return;
				}
				PartsRdpTecCardDetail.index--;
				PartsRdpTecCardDetail.initFn();
			}
		}, {
			text:'下一工单', iconCls:'movedownIcon', id:'id_t_move_down_btn', handler: function(){
				if (PartsRdpTecCard.grid.store.getCount() - 1 == PartsRdpTecCardDetail.index) {
					MyExt.Msg.alert('已经是最后一条工单！');
					return;
				}
				PartsRdpTecCardDetail.index++;
				PartsRdpTecCardDetail.initFn();
			}
		}],
		
		layout: 'border',
		items: [{
			region: 'north', height: 95, layout: 'fit', frame: true,
			items: {
				xtype: 'fieldset', title: '检修作业工单基本信息',
				html: '<div id="id_tec_card_detail"></div>'
			}
		}, {
			region: 'center', layout: 'fit',
			items: [PartsRdpTecWS.grid]
		}],
		listeners: {
			show: function(){
				PartsRdpTecCardDetail.initFn();
			}
		}
	});
});