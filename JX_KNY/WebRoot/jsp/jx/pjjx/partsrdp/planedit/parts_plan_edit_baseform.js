Ext.onReady(function(){
	if(typeof(PartsPlanEdit) === 'undefined')
		Ext.ns("PartsPlanEdit");
	
	var form, store;
	
	function submit(){
		Ext.Ajax.request({
			url: ctx + "/partsRdp!editPartsRdpPlan.action",
			jsonData: form.getForm().getValues(),
			success: function(r){
				var rlt = Ext.util.JSON.decode(r.responseText);
				if(rlt.success){
					alertSuccess();
					if(store){
						store.reload();
						form.findParentByType("window").hide();
					}
				}else{
					alertFail(rlt.errMsg);
				}
			}
		});
	}
	
	function createBaseForm(){
		form = defineFormPanel({
			defaultType: "textfield",
			labelWidth: 85,
			rows:[{
				cw: .33,
				cols: [{
		         	fieldLabel: '规格型号',
		         	name: "specificationModel",
		         	disabled: true
	         	},{
					fieldLabel: "配件名称",
		         	name: "partsName",
		         	disabled: true
				},{
					fieldLabel: "物料编码",
		         	name: "matCode",
		         	disabled: true
				},{
					fieldLabel: "配件编号",
		         	name: "partsNo",
		         	disabled: true
				},{
					fieldLabel: "扩展编号",
		         	name: "extendNo",
		         	disabled: true
				},{
					fieldLabel: "下车车型",
		         	name: "unloadTrainType",
		         	disabled: true
				},{
					fieldLabel: "下车车号",
		         	name: "unloadTrainNo",
		         	disabled: true
				},{
					fieldLabel: "下车修程",
		         	name: "unloadRepairClass",
		         	disabled: true
				},{
					fieldLabel: "下车修次",
		         	name: "unloadRepairTime",
		         	disabled: true
				}]
			},{
				cw: [.33, .66],
				cols:[{
					fieldLabel: "承修班组",
					xtype: "Base_combo",
					id: "ppeb_repairTeam_id",
					allowBlank: false,
					action: ctx + "/partsRepairList!repairOrg.action",
					fields:['orgname', 'repairOrgID', 'orgseq'],
					hiddenName: "repairOrgID", 
					returnField: [{widgetId:"ppeb_orgseq_id",propertyName:"orgseq"}],
					displayField: "orgname",
					valueField: "repairOrgID",
					pageSize: 20, minListWidth: 200,
					width: 140,
					editable:false,
					listeners: {
						render: setTextName("repairOrgName")
					}
				},{
					fieldLabel: "检修需求单",
					name: "wpDesc",
					disabled: true
				}]
			},{
				cw: .33,
				cols: [{
					fieldLabel: "开始时间",
					name: "planStartTime",
					id: "ppeb_begin",
					xtype:"my97date",
					allowBlank: false,
					my97cfg: {dateFmt: "yyyy-MM-dd HH:mm"},
					format: "Y-m-d H:i"
				},{
					xtype: 'Base_combo',hiddenName: 'calendarIdx', fieldLabel: '日历', id: "ppeb_calendar_id",
					entity:'com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo',
					allowBlank: false,
					fields: ["idx","calendarName"],
					displayField:'calendarName',valueField:'idx',
					queryHql: 'from WorkCalendarInfo where recordStatus = 0',
					listeners: {
						render: setTextName("calendarName")
					}
				}]
			},{
				cw: .33,
				cols: [{
					fieldLabel: "结束时间",
					name: "planEndTime",
					id: "ppeb_end",
					xtype:"my97date",
					my97cfg: {dateFmt: "yyyy-MM-dd HH:mm", minDate: '#F{$dp.$D(\'ppeb_begin\')}'},
					format: "Y-m-d H:i",
		         	disabled: true
				},{
					xtype: "hidden", name: "idx"
				},{
					xtype: "hidden", name: "repairOrgSeq", id: "ppeb_orgseq_id"
				}]
			}]
		}, {
			labelAlign: "right",
			buttonAlign: "center",
			buttons:[{
				text: "保存", iconCls: "saveIcon",
				handler: function(){
					submit();
				}
			}, {
				text: "关闭", iconCls: "closeIcon", 
				handler: function(){
					form.findParentByType("window").hide();
		        }
		    }]
		});
		
		PartsPlanEdit.baseForm = form;
	}
	
	PartsPlanEdit.createBaseForm = createBaseForm;
	
	PartsPlanEdit.setForm = function(_record){
		PartsPlanEdit.baseForm.getForm().reset();
		PartsPlanEdit.baseForm.getForm().loadRecord(_record);
		var repairOrgID = _record.get("repairOrgID");
		var repairOrgName = _record.get("repairOrgName");
		
		var team = Ext.getCmp("ppeb_repairTeam_id");
		team.setDisplayValue(repairOrgID, repairOrgName);
		team.queryName = _record.get("partsTypeIDX");
		team.cascadeStore();
		
		var calendar = Ext.getCmp("ppeb_calendar_id");
		calendar.setDisplayValue(_record.get("calendarIdx"), _record.get("calendarName"));
		store = _record.store;
	}
});