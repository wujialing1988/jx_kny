/**
 * 配件检修记录卡实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpRecordCardDetail');                       // 定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
    PartsRdpRecordCardDetail.index = -1;							 // 【检修记录工单】列表索引
    PartsRdpRecordCardDetail.record = null;							 // 【检修记录工单】记录对象
	/** **************** 定义全局变量结束 **************** */
    
    /** **************** 定义全局函数开始 **************** */
	/**
	 * 获取配件检修记录工单详情显示模板
	 */
	PartsRdpRecordCardDetail.getDetailTpl = function() {
		if (Ext.isEmpty(PartsRdpRecordCardDetail.tpl)) {
			PartsRdpRecordCardDetail.tpl = new Ext.XTemplate(
				'<table class="pjjx-show-info-table">',
					'<tr>',
		                '<td class="pjjx-show-info-table-label" width="12%">识别码：</td><td width="13%">{identificationCode}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">配件编号：</td><td width="13%">{partsNo}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">配件名称：</td><td width="13%">{partsName}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">规格型号：</td><td width="13%">{specificationModel}</td>',
		            '</tr>',
					'<tr>',
		                '<td class="pjjx-show-info-table-label" width="12%">记录卡编号：</td><td width="13%">{recordCardNo}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">工单名称：</td><td width="13%" colspan="5">{recordCardDesc}</td>',
		            '</tr>',
					'<tr>',
		                '<td class="pjjx-show-info-table-label" width="12%">检修情况描述：</td><td width="13%" colspan="7">{remarks}</td>',
		            '</tr>',
				'</table>'
			);
		}
		return PartsRdpRecordCardDetail.tpl;
	}
	
	/**
	 * 重新加载一个新的工单详情
	 */ 
	PartsRdpRecordCardDetail.initFn = function() {
		// 获取检修记录工单
		PartsRdpRecordCardDetail.record = PartsRdpRecordCard.grid.store.getAt(PartsRdpRecordCardDetail.index);
		var values = {},
			data = PartsRdpRecordCardDetail.record.data;
		Ext.applyIf(values, data);						// 赋值检修记录工单属性
		Ext.applyIf(values, PartsRdpInfo.record.data);	// 赋值配件检修作业属性
		
		// 获取配件检修兑现单信息显示模板
		var tpl = PartsRdpRecordCardDetail.getDetailTpl();
		tpl.overwrite(Ext.get('id_record_card_detail'), values);
		
		// -------------->> 加载“检修检测项”列表
		PartsRdpRecordCardDetail.loadRi(data.idx);
		
		// -------------->> 加载“质量检查”列表
		PartsRdpQR.rdpRecordCardIDX = data.idx;
		PartsRdpQR.grid.store.load();
	}
	
	/**
	 * 动态生成检修检测项列表（含嵌套列表）
	 */
	PartsRdpRecordCardDetail.loadRi = function(rdpRecordCardIDX) {
		var dom = Ext.get('id_record_card_ri_detail').dom;
		
//		if(self.loadMask)    self.loadMask.show();
		Ext.Ajax.request({
			url: ctx + '/partsRdpRecordRI!queryInHTML.action',
			params:{ rdpRecordCardIDX: rdpRecordCardIDX },
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
	
	PartsRdpRecordCardDetail.win = new Ext.Window({
		title: '检修记录处理详情',
		maximized: true,
		closeAction: 'hide',
		tbar: [{
			text: '关闭', iconCls: 'closeIcon', handler: function(){
				this.findParentByType('window').hide();
			}
		}, '->', {
			text:'上一工单', iconCls:'moveUpIcon', id:'id_r_move_up_btn', handler: function(){
				if (0 == PartsRdpRecordCardDetail.index) {
					MyExt.Msg.alert('已经是第一条工单！');
					return;
				}
				PartsRdpRecordCardDetail.index--;
				PartsRdpRecordCardDetail.initFn();
			}
		}, {
			text:'下一工单', iconCls:'movedownIcon', id:'id_r_move_down_btn', handler: function(){
				if (PartsRdpRecordCard.grid.store.getCount() - 1 == PartsRdpRecordCardDetail.index) {
					MyExt.Msg.alert('已经是最后一条工单！');
					return;
				}
				PartsRdpRecordCardDetail.index++;
				PartsRdpRecordCardDetail.initFn();
			}
		}],
		
		layout: 'border',
		items: [{
			region: 'north', height: 118, layout: 'fit', frame: true,
			items: {
				xtype: 'fieldset', title: '配件检修兑现单信息',
				html: '<div id="id_record_card_detail"></div>'
			}
		}, {
			region: 'center', layout: 'fit',
			items: {
				xtype: 'tabpanel', activeItem: 0, border: false,
				items: [{
					title: '检修检测项', layout: 'fit',
					items: [{
						xtype: 'panel', html: 'hello', autoScroll: true,
						html: '<div id="id_record_card_ri_detail"></div>'
					}]
				}, {
					title: '质量检查', layout: 'fit',
					items: [PartsRdpQR.grid]
				}]
			}
		}],
		listeners: {
			show: function(){
				PartsRdpRecordCardDetail.initFn();
			}
		}
	});
});