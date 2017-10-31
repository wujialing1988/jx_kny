/**
 * 机务设备工单定义 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('EquipCard');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	EquipCard.labelWidth = 100;
	EquipCard.fieldWidth = 160;
	EquipCard.searchParams = {};
	/** **************** 定义全局变量结束 **************** */
		
	/** **************** 定义查询表单开始 **************** */
	EquipCard.searchForm = new Ext.form.FormPanel({
		labelWidth: EquipCard.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 15px',
		// 查询表单第1行
		layout: 'column',
		defaults:{columnWidth: .33, layout: 'form', defaults:{
			width: EquipCard.fieldWidth,
			xtype:'textfield'
		}},
		items:[{												// 第1行第1列
			items: [{
				id:"deviceType_comb", editable:false,selectOnFocus:false,forceSelection:false,typeAhead:false,
				xtype: 'Base_combo',hiddenName: "deviceTypeName",fieldLabel:"设备类别",
				entity:"com.yunda.jxpz.equipinfo.entity.DeviceType",displayField:"deviceTypeName",valueField:"deviceTypeName",
				fields:["deviceTypeCode","deviceTypeName"],
				minListWidth: EquipCard.fieldWidth
			}]
		}, {													// 第1行第2列
			items: [{
				fieldLabel: '工单编码',
				name: 'equipCardNo'
			}]
		}, {													// 第1行第3列
			items: [{
				fieldLabel: '工单描述',
				name: 'equipCardDesc'
			}]
		}],
		buttons: [{
			xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
				// 重新加载表格
				EquipCard.grid.store.load();
			}
		}, {
			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
				EquipCard.searchForm.getForm().reset();
				Ext.getCmp("deviceType_comb").clearValue();
				// 重新加载表格
				EquipCard.grid.store.load();
			}
		}],
		buttonAlign: 'center'
	});
	/** **************** 定义查询表单结束 **************** */
	EquipCard.setCardFn = function() {
		var sm = EquipCard.grid.getSelectionModel();
		if (sm.getCount() <= 0) {
			MyExt.Msg.alert("尚未选择任何记录！");
			return;
		}
		var data = sm.getSelections()[0];
		// 加载【设备工单信息】
		EquipDI.cardForm.getForm().loadRecord(data);
		EquipDI.equipCardIDX = data.get('idx');
		EquipDI.editWin.show();
		EquipDI.grid.store.load();
		EquipDI.grid.getTopToolbar().enable();
		Ext.getCmp("deviceTypeName_comb").setDisplayValue(data.get("deviceTypeName"),data.get("deviceTypeName"));
	}	
	
	EquipCard.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/equipCard!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/equipCard!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/equipCard!logicDelete.action',            //删除数据的请求URL
	    tbar: [{
				text: '新增', iconCls: 'addIcon', handler: function(){
					
					EquipDI.equipCardIDX = "####";
					EquipDI.editWin.show();
					EquipDI.cardForm.getForm().reset();
					Ext.getCmp("deviceTypeName_comb").clearValue();
					Ext.getCmp("equipCardDesc").setValue("");
					Ext.getCmp("cardId").setValue("");
					Ext.Ajax.request({
				        url: ctx + "/codeRuleConfig!getConfigRule.action",
				        params: {ruleFunction : "PJJX_EQUIP_CARD_CARDNO"},
				        success: function(response, options){
				            var result = Ext.util.JSON.decode(response.responseText);
				            Ext.getCmp("equipCardNo").setValue(result.rule);
				        }
					});
					EquipDI.grid.store.load();
					EquipDI.grid.getTopToolbar().disable();
				}
			},'refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'工单编码', dataIndex:'equipCardNo', editor:{  maxLength:30 },
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
		     		var html = "";
		  			html = "<span><a href='#' onclick='EquipCard.setCardFn()'>"+value+"</a></span>";
		      		return html;
				}
		},{
			header:'工单描述', dataIndex:'equipCardDesc', editor:{  maxLength:500 }
		},{
			header:'设备类别编码', dataIndex:'deviceTypeCode', editor:{  maxLength:50 }
		},{
			header:'设备类别', dataIndex:'deviceTypeName', editor:{  maxLength:50 }
		}]
	});
	EquipCard.grid.store.on('beforeload', function() {
		EquipCard.searchParams = EquipCard.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(EquipCard.searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	EquipCard.grid.un("rowdblclick",EquipCard.grid.toEditFn,EquipCard.grid);
	EquipCard.grid.on("rowdblclick",EquipCard.setCardFn,EquipCard.grid);
	
	//页面自适应布局
	new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 120,
			border: true,
			collapsible: true,
//			split: true,
			items: [EquipCard.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			items: [EquipCard.grid]
		}]
	});
});