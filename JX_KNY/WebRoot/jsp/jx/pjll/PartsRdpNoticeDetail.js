/**
 * 提票单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpNoticeDetail');                       //定义命名空间
                    // 定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	PartsRdpNoticeDetail.fieldWidth = 140
//    PartsRdpNoticeDetail.index = -1;							 // 【检修记录工单】列表索引
    PartsRdpNoticeDetail.record = null;							 // 【检修记录工单】记录对象
	/** **************** 定义全局变量结束 **************** */
    
    /** **************** 定义全局函数开始 **************** */

    //检修记录单显示模板
	PartsRdpNoticeDetail.getDetailTpl = function() {
		if (Ext.isEmpty(PartsRdpNoticeDetail.tpl)) {
			PartsRdpNoticeDetail.tpl = new Ext.XTemplate(
				'<table class="pjjx-show-info-table">',
					'<tr>',
		                '<td class="pjjx-show-info-table-label" width="12%">配件编号：</td><td width="13%">{partsNo}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">配件名称：</td><td width="13%">{partsName}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">规格型号：</td><td width="13%">{specificationModel}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">配件识别码：</td><td width="13%">{identificationCode}</td>',
		            '</tr>',
					'<tr>',
		                '<td class="pjjx-show-info-table-label" width="12%">提票单编号：</td><td width="13%">{noticeNo}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">提报人：</td><td width="13%" colspan="2">{noticeEmpName}</td>',
		            '</tr>',
					'<tr>',
		                '<td class="pjjx-show-info-table-label" width="12%">问题描述：</td><td width="13%" colspan="5">{noticeDesc}</td>',
		            '</tr>',
		            
				'</table>'
			);
		}
		return PartsRdpNoticeDetail.tpl;
	}
 
	PartsRdpNoticeDetail.baseForm = new Ext.form.FormPanel({
		padding: 10, 
		layout: 'column', defaults: {
			columnWidth: .25, layout: 'form',
			defaults: {
				width: PartsRdpNoticeDetail.fieldWidth
			}
		},
		items: [{
			columnWidth: 1,
			items: [{
				fieldLabel: '结果描述',	xtype : "textarea", name: 'solution', disabled:true, anchor: '50%'
			}]
		},{
			items: [{
				fieldLabel: '处理人', xtype:'textfield',	name: 'workEmpName',disabled:true
			}]
		}, {
			items: [{
				fieldLabel: '处理时间', name:'workStartTime',xtype: 'my97date', format:'Y-m-d H:i',disabled:true
			}]

		}]
	});
	

	/**
	 * 重新加载一个新的记录单详情
	 */ 
	PartsRdpNoticeDetail.initFn = function() {
		// 获取提票单
		PartsRdpNoticeDetail.record = PartsRdpNoticeNew.grid.store.getAt(PartsRdpNoticeDetail.index);
		var values = {},
		data = PartsRdpNoticeDetail.record.data;
		data.partsNo = PartsRdpNoticeDetail.partsNo; 
        data.partsName = PartsRdpNoticeDetail.partsName;
        data.specificationModel = PartsRdpNoticeDetail.specificationModel; 
        data.identificationCode = PartsRdpNoticeDetail.identificationCodes; 
		Ext.applyIf(values, data);						// 赋值检修记录单属性
		// 获取提票单信息显示模板
		var tpl = PartsRdpNoticeDetail.getDetailTpl();
		tpl.overwrite(Ext.get('id_notice_detail'), values);
		PartsRdpNoticeDetail.win.find("name","solution")[0].setValue(data.solution);
		PartsRdpNoticeDetail.win.find("name","workEmpName")[0].setValue(data.workEmpName);
		PartsRdpNoticeDetail.win.find("name","workStartTime")[0].setValue(data.workStartTime);
	}

	/** **************** 定义全局函数开始 **************** */
	
	PartsRdpNoticeDetail.win = new Ext.Window({
		title: '超范围或返工修情况详情',
		maximized: true,
		closeAction: 'hide',
		tbar: [{
			text: '关闭', iconCls: 'closeIcon', handler: function(){
				this.findParentByType('window').hide();
			}
		}, '->', {
			text:'上一工单', iconCls:'moveUpIcon', id:'id_r_move_up_btn', handler: function(){
				if (0 == PartsRdpNoticeDetail.index) {
					MyExt.Msg.alert('已经是第一条详情单！');
					return;
				}
				PartsRdpNoticeDetail.index--;
				PartsRdpNoticeDetail.initFn();
			}
		}, {
			text:'下一工单', iconCls:'movedownIcon', id:'id_r_move_down_btn', handler: function(){
				if (PartsRdpNoticeNew.grid.store.getCount() - 1 == PartsRdpNoticeDetail.index) {
					MyExt.Msg.alert('已经是最后一条详情单！');
					return;
				}
				PartsRdpNoticeDetail.index++;
				PartsRdpNoticeDetail.initFn();
			}
		}],		
		layout: 'border',
		items: [{
			region: 'north', height: 138, layout: 'fit', frame: true,
			items: {
				xtype: 'fieldset', title: '配件检修记录单信息',collapsible: true,
				html: '<div id="id_notice_detail"></div>'
			}
		}, {
			region: 'center',frame: true,
			items: [PartsRdpNoticeDetail.baseForm]			
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}	
		}],
		listeners: {
			show: function(){
				PartsRdpNoticeDetail.initFn();
			}
		}		
	});
	
});