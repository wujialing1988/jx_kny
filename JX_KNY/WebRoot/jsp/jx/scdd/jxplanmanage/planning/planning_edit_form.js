Ext.onReady(function(){
	
	if(typeof(PlanningEdit) === 'undefined')
		Ext.ns("PlanningEdit");
	
	var form;
	
	function createForm(){
		form = defineFormPanel({
			labelWidth: 80,
			rows:[{
				cw: .5,
				cols: [{
					fieldLabel: "计划名称",
					name: "trainNo"
				},{
					fieldLabel: "编制日期",
					name: "plan_begin_gt",
					xtype: "my97date"
				},{
					fieldLabel: "计划开始日期",
					name: "plan_begin_lt",
					xtype: "my97date"
				},{
					fieldLabel: "计划结束日期",
					name: "plan_end_gt",
					xtype: "my97date"
				},{
					fieldLabel: "编制人",
					name: "plan_end_gt",
					xtype: "displayfield"
				},{
					fieldLabel: "编制部门",
					name: "plan_end_gt",
					xtype: "displayfield"
				}]
			}]
		}, {
			labelAlign: "right",
			style: "padding: 50px",
			buttonAlign: "center",
			buttons:[{
				text: "保存",
				iconCls: "saveIcon",
				handler: function(){
				
				}
			},{
				text: "关闭",
				iconCls: "closeIcon",
				handler: function(){
					this.findParentByType("window").hide();
				}
			}]
		});
		return form;
	}
	
	PlanningEdit.createForm = createForm;
});