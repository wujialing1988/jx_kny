Ext.onReady(function(){
	
	if(typeof(Planning) === 'undefined')
		Ext.ns("Planning");
	
	var form = defineFormPanel({
		defaultType: "textfield",
		labelWidth: 60,
		rows:[{
			cw: [200, 140, 140, 140, 140, 70, 70],
			cols: [{
				fieldLabel: "计划名称",
				name: "trainNo"
			},{
				fieldLabel: "计划开始日期",
				name: "plan_begin_gt",
				xtype: "my97date",
				initNow: false
			},{
				fieldLabel: "-",
				labelSeparator: "",
				name: "plan_begin_lt",
				xtype: "my97date",
				initNow: false
			},{
				fieldLabel: "计划结束日期",
				name: "plan_end_gt",
				xtype: "my97date",
				initNow: false
			},{
				fieldLabel: "-",
				labelSeparator: "",
				name: "plan_end_lt",
				xtype: "my97date",
				initNow: false
			},{
				xtype: "button",
				style: "margin-left: 10px",
	            text: "查询",
	            iconCls: "searchIcon",
	            handler: function(){
					var sp = form.getForm().getValues();
					Planning.grid.searchFn(sp);
				}
			},{
				xtype: "button",
				text: "重置",
				style: "margin-left: 10px",
				iconCls: "resetIcon",
				handler: function(){
					form.getForm().reset();
				}
			}]
		}]
	}, {labelAlign: "right"});
	form.on("render", function(){
		var cmp = form.find("name", "plan_begin_gt")[0].ownerCt;
		cmp.labelWidth = 80;
		cmp.setWidth(cmp.width + 40);
		cmp = form.find("name", "plan_end_gt")[0].ownerCt;
		cmp.labelWidth = 80;
		cmp.setWidth(cmp.width + 40);
		cmp = form.find("name", "plan_begin_lt")[0].ownerCt;
		cmp.labelWidth = 10;
		cmp.setWidth(cmp.width - 40);
		cmp = form.find("name", "plan_end_lt")[0].ownerCt;
		cmp.labelWidth = 10;
		cmp.setWidth(cmp.width - 40);
	});
	Planning.form = form;
});