/**
 * 提票单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TrainWorkTicketDetail');                       //定义命名空间
                    // 定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	TrainWorkTicketDetail.fieldWidth = 140
//    TrainWorkTicketDetail.index = -1;							 // 【检修记录工单】列表索引
    TrainWorkTicketDetail.record = null;							 // 【检修记录工单】记录对象
	/** **************** 定义全局变量结束 **************** */
    
    /** **************** 定义全局函数开始 **************** */

    //检修记录单显示模板
	TrainWorkTicketDetail.getDetailTpl = function() {
		if (Ext.isEmpty(TrainWorkTicketDetail.tpl)) {
			TrainWorkTicketDetail.tpl = new Ext.XTemplate(
				'<table class="pjjx-show-info-table">',
					'<tr>',
		                '<td class="pjjx-show-info-table-label" width="12%">车型车号：</td><td width="13%">{cxch}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">修程修次：</td><td width="13%">{xcxc}</td>',
		            
		                '<td class="pjjx-show-info-table-label" width="12%">计划开始时间：</td><td width="13%">{planBeginTime}</td>',
		               		            
		            '</tr>',
					'<tr>',
		                '<td class="pjjx-show-info-table-label" width="12%">提票单号：</td><td width="13%">{ticketCode}</td>',
		                '<td class="pjjx-show-info-table-label" width="12%">提报类型：</td><td width="13%">{type}</td>',            
		                '<td class="pjjx-show-info-table-label" width="12%">计划结束时间：</td><td width="13%">{planEndTime}</td>',
		            '</tr>',
					'<tr>',
						 '<td class="pjjx-show-info-table-label" width="12%">提报人：</td><td width="13%">{ticketEmp}</td>',
						 '<td class="pjjx-show-info-table-label" width="12%">提报时间：</td><td width="13%">{ticketTime}</td>',
		               '<td class="pjjx-show-info-table-label" width="12%">故障位置：</td><td width="13%" colspan="3">{fixPlaceFullName}</td>',          
		            '</tr>',
		            
				'</table>'
			);
		}
		return TrainWorkTicketDetail.tpl;
	}
 
	TrainWorkTicketDetail.baseForm = new Ext.form.FormPanel({
		padding: 10, 
		layout: 'column', defaults: {
			columnWidth: .25, layout: 'form',
			defaults: {
				width: TrainWorkTicketDetail.fieldWidth
			}
		},
		items: [{
			columnWidth: 1,
			items: [{
				fieldLabel: '不良状态描述',	xtype : "textarea", name: 'faultDesc', disabled:true, anchor: '50%'
			},{
				fieldLabel: '施修方案',	xtype : "textarea", name: 'faultReason', disabled:true, anchor: '50%'
			},{
				fieldLabel: '标签',	xtype : "textarea", name: 'reasonAnalysis', disabled:true, anchor: '50%'
			}]
		},{
			items: [{
				fieldLabel: '处理人', xtype:'textfield',	name: 'repairEmp',disabled:true
			}]
		}, {
			items: [{
				fieldLabel: '处理完成时间', name:'completeTime',xtype: 'my97date', format:'Y-m-d H:i',disabled:true
			}]

		}]
	});
	

	/**
	 * 重新加载一个新的记录单详情
	 */ 
	TrainWorkTicketDetail.initFn = function() {
		// 获取提票单
		TrainWorkTicketDetail.record = TrainWorkTicket.grid.store.getAt(TrainWorkTicketDetail.index);
		var values = {},
		data = TrainWorkTicketDetail.record.data;
		data.cxch = TrainWorkTicketDetail.cxch; 
        data.xcxc = TrainWorkTicketDetail.xcxc;
        data.planBeginTime = TrainWorkTicketDetail.planBeginTime; 
        data.planEndTime = TrainWorkTicketDetail.planEndTime;
        if (!Ext.isEmpty(data.ticketTime)) {
			if (Ext.isString(data.ticketTime)) {
				values.ticketTime = data.ticketTime;
			} else {
				values.ticketTime = new Date(data.ticketTime).format('Y-m-d H:i');
			}
		}
		Ext.applyIf(values, data);						// 赋值机车检修属性
		// 获取提票单信息显示模板
		var tpl = TrainWorkTicketDetail.getDetailTpl();
		tpl.overwrite(Ext.get('id_notice_detail'), values);
		TrainWorkTicketDetail.win.find("name","repairEmp")[0].setValue(data.repairEmp);
		TrainWorkTicketDetail.win.find("name","completeTime")[0].setValue(data.completeTime);
		TrainWorkTicketDetail.win.find("name","faultDesc")[0].setValue(data.faultDesc);
		TrainWorkTicketDetail.win.find("name","faultReason")[0].setValue(data.faultReason);
		TrainWorkTicketDetail.win.find("name","reasonAnalysis")[0].setValue(data.reasonAnalysis);
		
	}

	/** **************** 定义全局函数开始 **************** */
	
	TrainWorkTicketDetail.win = new Ext.Window({
		title: '故障提票详情',
		maximized: true,
		closeAction: 'hide',
		tbar: [{
			text: '关闭', iconCls: 'closeIcon', handler: function(){
				this.findParentByType('window').hide();
			}
		}, '->', {
			text:'上一工单', iconCls:'moveUpIcon', id:'id_r_move_up_btn', handler: function(){
				if (0 == TrainWorkTicketDetail.index) {
					MyExt.Msg.alert('已经是第一条详情单！');
					return;
				}
				TrainWorkTicketDetail.index--;
				TrainWorkTicketDetail.initFn();
			}
		}, {
			text:'下一工单', iconCls:'movedownIcon', id:'id_r_move_down_btn', handler: function(){
				if (TrainWorkTicket.grid.store.getCount() - 1 == TrainWorkTicketDetail.index) {
					MyExt.Msg.alert('已经是最后一条详情单！');
					return;
				}
				TrainWorkTicketDetail.index++;
				TrainWorkTicketDetail.initFn();
			}
		}],		
		layout: 'border',
		items: [{
			region: 'north', height: 118, layout: 'fit', frame: true,
			items: {
				xtype: 'fieldset', title: '故障提票情况',collapsible: true,
				html: '<div id="id_notice_detail"></div>'
			}
		}, {
			region: 'center',frame: true,
			items: [TrainWorkTicketDetail.baseForm]			
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}	
		}],
		listeners: {
			show: function(){
				TrainWorkTicketDetail.initFn();
			}
		}		
	});
	
});