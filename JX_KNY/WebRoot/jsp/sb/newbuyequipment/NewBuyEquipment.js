/**
 * 新购设备（new） Created by hetao on 2016-09-23
 */
Ext.onReady(function() {
	Ext.ns('NewBuyEquipment');
	
	/** **************** 定义局部变量开始 **************** */
	var labelWidth = 100,
		fieldWidth = 140,
		ldsbPanelHeight = 58;

	var now = new Date();
	now.setMonth(now.getMonth() - 1);
	var lastMonth = now.format('Y-m-d');
	/** **************** 定义局部变量结束 **************** */
	/** **************** 定义局部函数开始 **************** */
	/**
	 * 验证设备是否为主设备，如果“电气系数”大于5、或者“机械系数”大于5、或者“固资原值大”于5000则表示为主设备
	 */
	function validIsPrimary() {
		// 电气系数
		var mechanicalCoefficient  = NewBuyEquipment.grid.saveForm.find("name", "mechanicalCoefficient")[0].getValue();
		// 机械系统
		var electricCoefficient = NewBuyEquipment.grid.saveForm.find("name", "electricCoefficient")[0].getValue();
		// 固资源值
		var fixedAssetValue = NewBuyEquipment.grid.saveForm.find("id", "fixedAssetValue")[0].getValue();
		// 如果“电气系数”大于5、或者“机械系数”大于5、或者“固资原值大”于5000则表示为主设备
		if (mechanicalCoefficient >= 5 
				|| electricCoefficient >= 5
				|| fixedAssetValue >= fixed_asset_value) {
			Ext.getCmp("isPrimaryDevice").setValue(1);
		} else {
			Ext.getCmp("isPrimaryDevice").setValue(0);
		}
	}
	/** **************** 定义局部函数结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	NewBuyEquipment.searchForm = new Ext.form.FormPanel({
		frame: true,
		collapsible: true,
		region: 'north', height: 110, padding: 10,
		title: '查询&nbsp;<span style="color:gray;font-weight:normal;">输入完成后按回车键查询</span>', 
		labelWidth: labelWidth,
		layout: 'column', 
		defaults: {
			columnWidth: .25, layout: 'form', defaultType: 'textfield',
			defaults: {
				width: fieldWidth, enableKeyEvents: true,
				listeners: {
					keyup: function(me, e) {
						if (e.ENTER == e.keyCode) {
							NewBuyEquipment.grid.store.load();
						}
					}	
				}
			}
		},
		items: [{
			items: [{
				 xtype: 'OmOrganizationCustom_comboTree', fieldLabel: '单位',
				 hiddenName: 'orgId', selectNodeModel:'exceptRoot' , width: 140, editable: false,
				 listeners : {
					select: function () {
						NewBuyEquipment.grid.store.load();
					}
       			 }
			}]
		}, {
			items: [{
				fieldLabel: '设备类别（编号）', name: 'classCode', labelWidth: 360
			}]
		}, {
			items: [{
				fieldLabel: '设备名称（编号）', name: 'equipmentCode'
			}]
		}, {
			items: [{
				xtype: 'compositefield', fieldLabel: '新购日期', combineErrors: false, anchor:'100%',
				items: [{
					xtype:'my97date', id: 'startDate', format:'Y-m-d', width: 100, initNow: false, //value:lastMonth,
					// 日期校验器
					vtype:'dateRange',
					dateRange:{startDate: 'startDate', endDate: 'endDate'}
				}, {
					xtype: 'label',
					text: '至',
					style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
				}, {							
					xtype:'my97date', id: 'endDate', format:'Y-m-d', width: 100, initNow: false,
					// 日期校验器
					vtype:'dateRange',
					dateRange:{startDate: 'startDate', endDate: 'endDate'}
				}]
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			text: '查询', iconCls: 'searchIcon', handler: function() {
				NewBuyEquipment.grid.store.load();
			}
		}, {
			text: '重置', iconCls: 'resetIcon', handler: function() {
				NewBuyEquipment.searchForm.getForm().reset();
				NewBuyEquipment.searchForm.findByType('OmOrganizationCustom_comboTree')[0].clearValue();
				NewBuyEquipment.grid.store.load();
			}
		}]
	});
	/** **************** 定义查询表单结束 **************** */
	
	/** **************** 定义保存表单开始 **************** */
	NewBuyEquipment.saveForm = new Ext.form.FormPanel({
		baseCls: 'plain',
		items: [{
			xtype: 'fieldset', title: '设备信息', layout: 'column', 
			defaults: {
				columnWidth: .33, layout: 'form', baseCls: 'plain', defaults: {
					width: fieldWidth, xtype: 'textfield'
				}
			},
			items: [{
				items: [{
					allowBlank: false,
					fieldLabel: '设备类别', xtype: 'classificationSelect',  hiddenName: 'classCode',
					returnField: [{
						widgetId: 'className', propertyName: 'className'
					}],
					listeners: {
						select: function(node) {
							// 自动设置设备编码
							Ext.Ajax.request({
								url: ctx + "/equipmentPrimaryInfo!maxEquipmentCode.action",
								params: {"classCode" : node.data.id},
								success: function(r){
									Ext.getCmp("equipmentCode").setValue(r.responseText.replace(/\"/g, ""));
								}
							});
						}
					}
				}, {
					allowBlank: false,
					fieldLabel: '设备类别名称', id: 'className', xtype: 'hidden'
				}, {
					allowBlank: false,
					fieldLabel: '机械系数', name: 'mechanicalCoefficient', vtype: "nonNegativeFloat",
					validator : validIsPrimary,
					maxLength: 5
				}, {
					allowBlank: false,
					fieldLabel: '电气系数', name: 'electricCoefficient', vtype: "nonNegativeFloat",
					validator : validIsPrimary,
					maxLength: 5
				}, {
					allowBlank: false,
					fieldLabel: '使用年月', name: 'useDate',
					xtype: 'my97date', format: 'Y-m', my97cfg: {dateFmt: "yyyy-MM"}
				}, RunShift({hiddenName:'runingShifts', allowBlank: false, fieldLabel: '设备运行班制', value: 0}), {
					fieldLabel: "电气总功率(kw)", name: 'eletricTotalPower', maxLength:10
				}, {
					style: 'background: rgb(199,237,204);padding-top: 5px;', xtype: 'panel', height: ldsbPanelHeight, anchor: '100%', baseCls: 'plain',
					layout: 'form', defaultType: 'textfield', defaults: {width: fieldWidth},
					items: [{
						  xtype: 'OmOrganization_Win', fieldLabel: '使用车间',
						  hiddenName: 'useWorkshopId', valueField: 'id',
						  rootId: 0, rootText: '铁路总公司', displayField: 'text',
						  width: fieldWidth, editable: true, 
						  returnField: [{widgetId:"useWorkshop",propertyName:"text"}],
						  listeners:{
						  	afterrender: function() {
								this.clearValue();
							},
							select : function(tree, node){
								Ext.getCmp("usePersonId").setSelectOrg(node.getPath("id"));
							}
						}
					}, {
						fieldLabel: '使用车间名称', id: 'useWorkshop', xtype: 'hidden'
					}, {
						  id: 'usePersonId', xtype: 'OmEmployee_MultSelectWin', fieldLabel: '使用人',
						  displayField:'empname', valueField: 'empid',
						  editable: false, width: fieldWidth, hiddenName: 'usePersonId',
						  returnField : [{widgetId: "usePerson", propertyName: "empname"}]
					}, {
						fieldLabel: '使用人名称', id: 'usePerson', xtype: 'hidden'
					}]
				}, ManageLevel({hiddenName:'manageLevel', value: '段', fieldLabel: '管理级别'})
					, YesOrNo({hiddenName:'isDedicated', fieldLabel: '是否专用设备', value: 0})
					, YesOrNo({hiddenName:'isPrimaryDevice',id:'isPrimaryDevice', value: 0, fieldLabel: '是否主设备', disabled: true})
					, YesOrNo({hiddenName:'xzState',id:'e_xzState_id', value: 0, fieldLabel: '闲置状态', disabled: true})
				]
			}, {
				items: [{
					allowBlank: false,
					fieldLabel: '设备编号', id: 'equipmentCode', maxLength: 20
				}, {
					allowBlank: false,
					xtype:'numberfield',
					fieldLabel: "固资原值(元)",
					vType:'positiveFloat',
					id: "fixedAssetValue",
					maxLength: 10,
					validator: function() {
						validIsPrimary();
						Ext.getCmp("fixedAssetNo").allowBlank = this.getValue() < fixed_asset_value;
						return true;
					}
				}, {
					xtype: "compositefield",
					items: [{
						xtype: "textfield", id: "specification", maxLength: 20, fieldLabel: "规格", width: fieldWidth - 35
					}, {
						xtype: "button", text:"Φ", handler: function() {
							var cmp = Ext.getCmp('specification');
							var v = cmp.getValue();
							if (Ext.isEmpty(v)) {
								cmp.setValue(this.getText());
							} else if (v.indexOf(this.getText()) < 0) {
								cmp.setValue(this.getText() + v);
							}
							cmp.focus();
						}, width: 30
					}]
				}, {
					fieldLabel: '制造工厂', name: 'makeFactory',
					xtype: "singlefieldcombo",
					entity: 'com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo',
					xfield: 'makeFactory'
				}, {
					fieldLabel: '设置地点', name: 'usePlace',
					xtype: "singlefieldcombo",
					entity: 'com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo',
					xfield: 'usePlace'
				}, {
					fieldLabel: "外形尺寸(mm)", name: 'shapeSize', maxLength: 30
				}, {
					style: 'background: rgb(199,237,204);padding-top: 5px;', xtype: 'panel', height: ldsbPanelHeight, anchor: '100%', baseCls: 'plain',
					layout: 'form', defaultType: 'textfield', defaults: {width: fieldWidth},
					items: [{
						  xtype: 'OmOrganization_Win', fieldLabel: '电气维修班组',
						  hiddenName: 'electricRepairTeamId', valueField: 'id',
						  rootId: 0, rootText: '铁路总公司', displayField: 'text',
						  width: fieldWidth, 
						  returnField: [{widgetId:"electricRepairTeam",propertyName:"text"}],
						  listeners:{
							  	afterrender: function() {
									this.clearValue();
								},
								select : function(tree, node){
									Ext.getCmp("electricRepairPersonId").setSelectOrg(node.getPath("id"));
								}
							}
					}, {
						fieldLabel: '电气维修班组名称', id: 'electricRepairTeam', xtype: 'hidden'
					}, {
						  id: 'electricRepairPersonId', xtype: 'OmEmployee_MultSelectWin', fieldLabel: '电气维修人',
						  displayField:'empname', valueField: 'empid',
						  editable: false, width: fieldWidth, hiddenName: 'electricRepairPersonId',
						  returnField :[{widgetId: "electricRepairPerson", propertyName: "empname"}]
					}, {
						fieldLabel: '电气维修班人名称', id: 'electricRepairPerson', xtype: 'hidden'
					}]
				}, ManageClass({hiddenName:'manageClass', value: 'A', fieldLabel: '管理类别'})
					, YesOrNo({hiddenName:'isSpecialType', fieldLabel: '是否特种设备', value: 0})
					, DynamicCombo({ hiddenName:'dynamic',id:'e_dynamic_id', value: 3,  fieldLabel: '设备动态', disabled: true})
					, YesOrNo({hiddenName:'czState',id:'e_czState_id', value: 0,  fieldLabel: '出租状态', disabled: true})
				]
			}, {
				items: [{
					allowBlank: false,
					fieldLabel: '设备名称', name: 'equipmentName', 
					maxLength: 30
				}, {
					allowBlank: false,
					maxLength:15,
					fieldLabel: '固资编号',
					id: "fixedAssetNo",
					validator: function(){
						if(Ext.getCmp("fixedAssetValue").getValue() < fixed_asset_value){
							return true;
						}
						if(this.getValue().replace(/^\s*$/g, "") === ""){
							return "该项为必填项";
						}
						return true;
					}
				}, {
					fieldLabel: '型号', name: 'model', maxLength: 30
				}, {
					fieldLabel: '制造年月', name: 'makeDate', 
					xtype: 'my97date', format: 'Y-m', my97cfg: {dateFmt: "yyyy-MM"}
				}, {
					fieldLabel: '出厂编号', name: 'leaveFactoryNo',  maxLength: 20
				}, {
					fieldLabel: "重量(t)", name: 'weight', xtype:'numberfield', maxLength: 30
				}, {
					style: 'background: rgb(199,237,204);padding-top: 5px;', xtype: 'panel', height: ldsbPanelHeight, anchor: '100%', baseCls: 'plain',
					layout: 'form', defaultType: 'textfield', defaults: {width: fieldWidth},
					items: [{
						  xtype: 'OmOrganization_Win', fieldLabel: '机械维修班组',
						  hiddenName: 'mechanicalRepairTeamId', valueField: 'id',
						  rootId: 0, rootText: '铁路总公司', displayField: 'text',
						  width: fieldWidth, 
						  returnField: [{widgetId:"mechanicalRepairTeam",propertyName:"text"}],
						  listeners:{
						  		afterrender: function() {
									this.clearValue();
								},
								select : function(tree, node){
									Ext.getCmp("mechanicalRepairPersonId").setSelectOrg(node.getPath("id"));
								}
							}
					}, {
						fieldLabel: '机械维修班组名称', id: 'mechanicalRepairTeam', xtype: 'hidden'
					}, {
						  id: 'mechanicalRepairPersonId', xtype: 'OmEmployee_MultSelectWin', fieldLabel: '机械维修人',
						  displayField:'empname', valueField: 'empid',
						  editable: false, width: fieldWidth, hiddenName: 'mechanicalRepairPersonId',
						  returnField :[{widgetId: "mechanicalRepairPerson", propertyName: "empname"}]
					}, {
						fieldLabel: '机械维修班人名称', id: 'mechanicalRepairPerson', xtype: 'hidden'
					}]
				}, Level({hiddenName:'tecLevel', id:'tecLevel', fieldLabel: '技术等级'}, 'edit')
					, YesOrNo({hiddenName:'isFrock', fieldLabel: '是否工装设备', value: 0})
					, YesOrNo({hiddenName:'isExactness', fieldLabel: '是否大精设备', value: 0})
					, YesOrNo({hiddenName:'fcState',id:'e_fcState_id', value: 0, fieldLabel: '封存状态', disabled: true})
				]
			}]
		}, {
			xtype: 'fieldset', title: '新购信息', layout: 'column', 
			defaults: {
				columnWidth: .33, layout: 'form', baseCls: 'plain', defaults: {
					width: fieldWidth, xtype: 'textfield'
				}
			},
			items: [{
				items: [{
						  xtype: 'OmOrganization_Win', fieldLabel: '单位',
						  hiddenName: 'orgId', rootId: 0, rootText: '铁路总公司',
						  width: fieldWidth, valueField: 'id', displayField: 'text',
						  returnField: [{widgetId:"orgName",propertyName:"text"}]
				}, {
					fieldLabel: '单位名称', id: 'orgName', xtype: 'hidden'
				}, {
						  xtype: 'OmOrganization_Win', fieldLabel: '批准单位',
						  hiddenName: 'ratifyOrgid', valueField: 'id', 
						  rootId: 0, rootText: '铁路总公司', displayField: 'text',
						  width: fieldWidth, 
						  returnField: [{widgetId:"ratifyOrgname",propertyName:"text"}]
				}, {
					fieldLabel: '批准单位名称', id: 'ratifyOrgname', xtype: 'hidden'
				}]
			}, {
				items: [{
						  xtype: 'OmEmployee_MultSelectWin', fieldLabel: '经办人',
						  displayField:'empname', valueField: 'empid',
						  editable: false, width: fieldWidth, hiddenName: 'responsiblePersonId',
						  returnField :[{widgetId: "responsiblePerson", propertyName: "empname"}]
				}, {
					fieldLabel: '经办人名称', xtype: 'hidden', id: 'responsiblePerson'
				}, {
					fieldLabel: '新购批号', name: 'buyBatchNum', maxLength: 100
				}]
			}, {
				items: [{
					fieldLabel: '新购日期', name: 'buyDate', xtype: 'my97date', format: 'Y-m-d', allowBlank: false
				}, {
					fieldLabel: '备注', name: 'remark', maxLength: 100
				}]
			}]
		}, {
			/** **************** 保存表单隐藏字段开始 **************** */
			xtype: 'container', layout: 'form', defaultType: 'textfield', hidden: true, items: [{
				fieldLabel: 'idx主键', name: 'idx'
			}, {
				fieldLabel: '主设备idx主键', name: 'equipmentIdx'
			}, {
				fieldLabel: '所属单位', name: 'affiliatedOrgname'
			}, {
				fieldLabel: '所属单位id', name: 'affiliatedOrgid'
			}]
			/** **************** 保存表单隐藏字段结束 **************** */
		}]
	});
	/** **************** 定义保存表单结束 **************** */
	
	/** **************** 定义设备列表开始 **************** */
	NewBuyEquipment.grid = new Ext.yunda.Grid({
		region: 'center',
		viewConfig: null,
		saveURL: ctx + '/newBuyEquipment!save.action',
		loadURL: ctx + '/newBuyEquipment!queryPageList.action',
		deleteURL: ctx + '/newBuyEquipment!logicDelete.action',
		/* 自定义保存表单 */
		saveForm: NewBuyEquipment.saveForm,
		saveWinWidth: 885,
		/* 自定义保存表单 */
		 tbar:['add', 'delete', {
			 	text: "附属设备", iconCls: "pluginIcon", handler: function(){
		    		var record;
		    		if(record = $yd.getSingleRecord(NewBuyEquipment.grid)){
		    			if(typeof(AffiliatedEquipment) != 'undefined'){
		    				AffiliatedEquipment.showWin(record.get("equipmentIdx"), true);
		    			}
		    		}
		    	}
		    },'refresh'],
		fields: [Attachment.createColModeJson({
			attachmentKeyName:'primary_equipment_info',
			attachmentKeyIDX: 'equipmentIdx'
		}),{
			header:'idx主键', dataIndex:'idx', hidden:true 
		},{
			header:'单位', dataIndex:'orgId', hidden:true
		},{
			header:'单位名称', dataIndex:'orgName'
		},{
			header:'主设备idx主键 ', dataIndex:'equipmentIdx', hidden:true
		},{
			header:'设备名称', dataIndex: 'equipmentName', width: 200
		},{
			header:'设备编号', dataIndex: 'equipmentCode', width: 140
		},{
			header:'设备类别编码', dataIndex: 'classCode'
		},{
			header:'设备类别名称', dataIndex: 'className'
		},{
			header:'新购日期', dataIndex:'buyDate'
		},{
			header:'规格', dataIndex:'specification', width: 60
		},{
			header:'型号', dataIndex:'model', width: 60
		},{
			header:'机械系数', dataIndex:'mechanicalCoefficient'
		},{
			header:'电气系数', dataIndex:'electricCoefficient'
		},{
			header:'固资编号', dataIndex:'fixedAssetNo'
		},{
			header:'固资原值', dataIndex:'fixedAssetValue'
		},{
			header:'制造工厂', dataIndex:'makeFactory'
		},{
			header:'制造年月', dataIndex:'makeDate', xtype:'datecolumn', format: 'Y-m'
		},{
			header:'出厂编号', dataIndex:'leaveFactoryNo', hidden:true
		},{
			header:'使用年月', dataIndex:'useDate', xtype:'datecolumn', format: 'Y-m'
		},{
			header:'设置地点', dataIndex:'usePlace'
		},{
			header:'管理级别', dataIndex:'manageLevel', hidden:true 
		},{
			header:'管理类别', dataIndex:'manageClass', hidden:true 
		},{
			header:'重量', dataIndex:'weight', hidden:true
		},{
			header:'最大修年度', dataIndex:'maxRepairYear', hidden:true
		},{
			header:'电气总功率', dataIndex:'eletricTotalPower', hidden:true
		},{
			header:'技术等级', dataIndex:'tecLevel', renderer: getLevel
		},{
			header:'外形尺寸', dataIndex:'shapeSize', hidden:true
		},{
			header:'是否主设备', dataIndex:'isPrimaryDevice', renderer:getYesOrNo
		},{
			header:'是否专用设备', dataIndex:'isDedicated', renderer:getYesOrNo
		},{
			header:'是否特种设备', dataIndex:'isSpecialType', renderer:getYesOrNo
		},{
			header:'是否大精设备', dataIndex:'isExactness', renderer:getYesOrNo
		},{
			header:'是否工装设备', dataIndex:'isFrock', renderer:getYesOrNo
		},{ 
			// getDynamic定义于staticCombo.js源文件
			header:'设备动态', dataIndex:'dynamic', renderer: getDynamic
		},{
			header:'封存状态', dataIndex:'fcState', renderer:getYesOrNo
		},{
			header:'闲置状态', dataIndex:'xzState', renderer:getYesOrNo
		},{
			header:'出租状态', dataIndex:'czState', renderer:getYesOrNo
		},{
			header:'设备运行班制', dataIndex:'runingShifts', hidden:true
		},{
			header:'使用车间', dataIndex:'useWorkshopId', hidden:true
		},{
			header:'使用车间', dataIndex:'useWorkshop', hidden:true
		},{
			header:'使用人', dataIndex:'usePersonId', hidden:true
		},{
			header:'使用人', dataIndex:'usePerson', hidden:true
		},{
			header:'机械维修班组', dataIndex:'mechanicalRepairTeamId', hidden:true
		},{
			header:'机械维修班组', dataIndex:'mechanicalRepairTeam', hidden:true
		},{
			header:'机械维修人', dataIndex:'mechanicalRepairPersonId', hidden: true
		},{
			header:'机械维修人', dataIndex:'mechanicalRepairPerson', hidden:true
		},{
			header:'电气维修班组', dataIndex:'electricRepairTeamId', hidden:true
		},{
			header:'电气维修人', dataIndex:'electricRepairPersonId', hidden:true
		},{
			header:'电气维修人', dataIndex:'electricRepairPerson', hidden:true
		},{
			header:'电气维修班组', dataIndex:'electricRepairTeam', hidden:true
		},{
			header:'新购批号', dataIndex:'buyBatchNum',hidden:true
		},{
			header:'批准单位ID', dataIndex:'ratifyOrgid', hidden:true
		},{
			header:'批准单位 ', dataIndex:'ratifyOrgname', hidden:true
		},{
			header:'所属单位 ', dataIndex:'affiliatedOrgname', hidden:true
		},{
			header:'所属单位id ', dataIndex:'affiliatedOrgid', hidden:true
		},{
			header:'经办人', dataIndex:'responsiblePersonId',hidden:true
		},{
			header:'经办人', dataIndex:'responsiblePerson', hidden:true
		},{
			header:'备注', dataIndex:'remark'
		}],
		
		beforeSaveFn: function(data){ 
			data.useDate = data.useDate + "-01"
			data.makeDate = data.makeDate + "-01";
			return true; 
		},
		
		beforeGetFormData: function(){
			// 是否主设备
			this.saveForm.find('hiddenName', 'isPrimaryDevice')[0].enable();
			// 设备动态
			this.saveForm.find('hiddenName', 'dynamic')[0].enable();
			// 闲置状态
			this.saveForm.find('hiddenName', 'xzState')[0].enable();
			// 出租状态
			this.saveForm.find('hiddenName', 'czState')[0].enable();
			// 封存状态
			this.saveForm.find('hiddenName', 'fcState')[0].enable();
			// 技术等级
			this.saveForm.find('hiddenName', 'tecLevel')[0].enable();
		},
	    afterGetFormData: function(){
			// 是否主设备
			this.saveForm.find('hiddenName', 'isPrimaryDevice')[0].disable();
			// 设备动态
			this.saveForm.find('hiddenName', 'dynamic')[0].disable();
			// 闲置状态
			this.saveForm.find('hiddenName', 'xzState')[0].disable();
			// 出租状态
			this.saveForm.find('hiddenName', 'czState')[0].disable();
			// 封存状态
			this.saveForm.find('hiddenName', 'fcState')[0].disable();
			// 技术等级
			this.saveForm.find('hiddenName', 'tecLevel')[0].disable();
	    },    
		afterShowSaveWin: function(){
			// 重置设备类别字段
			this.saveForm.find('hiddenName', 'classCode')[0].clearValue();
			// 设置经办人默认为系统当前操作人员
			this.saveForm.find('hiddenName', 'responsiblePersonId')[0].setDisplayValue(empid, uname);
			this.saveForm.find('id', 'responsiblePerson')[0].setValue(uname);
			
			// 单位
			this.saveForm.find('hiddenName', 'orgId')[0].setDisplayValue(teamOrgId, teamOrgName);
			this.saveForm.find('id', 'orgName')[0].setValue(teamOrgName);
			
			var cmp = Ext.getCmp("tecLevel");
			cmp.hasValue = false;
			cmp.enable();
		},    
		afterShowEditWin: function(record, rowIndex){
			// 回显经办人
			this.saveForm.find('hiddenName', 'responsiblePersonId')[0].setDisplayValue(record.get('responsiblePersonId'), record.get('responsiblePerson'));
			// 回显设备类别
			this.saveForm.find('hiddenName', 'classCode')[0].setDisplayValue(record.get('classCode'), record.get('className'));
			// 回显使用车间
			this.saveForm.find('hiddenName', 'useWorkshopId')[0].setDisplayValue(record.get('useWorkshopId'), record.get('useWorkshop'));
			// 回显使用人
			this.saveForm.find('hiddenName', 'usePersonId')[0].setDisplayValue(record.get('usePersonId'), record.get('usePerson'));
			// 回显电气维修班组、维修人
			this.saveForm.find('hiddenName', 'electricRepairTeamId')[0].setDisplayValue(record.get('electricRepairTeamId'), record.get('electricRepairTeam'));
			this.saveForm.find('hiddenName', 'electricRepairPersonId')[0].setDisplayValue(record.get('electricRepairPersonId'), record.get('electricRepairPerson'));
			// 回显电机械修班组、维修人
			this.saveForm.find('hiddenName', 'mechanicalRepairTeamId')[0].setDisplayValue(record.get('mechanicalRepairTeamId'), record.get('mechanicalRepairTeam'));
			this.saveForm.find('hiddenName', 'mechanicalRepairPersonId')[0].setDisplayValue(record.get('mechanicalRepairPersonId'), record.get('mechanicalRepairPerson'));
			// 回显批准单位
			this.saveForm.find('hiddenName', 'ratifyOrgid')[0].setDisplayValue(record.get('ratifyOrgid'), record.get('ratifyOrgname'));
			// 回显单位
			this.saveForm.find('hiddenName', 'orgId')[0].setDisplayValue(record.get('orgId'), record.get('orgName'));
			// 技术等级
			var tecLevel = record.get("tecLevel");
			if (Ext.isEmpty(tecLevel)) {
				Ext.getCmp("tecLevel").enable();
			} else {
				Ext.getCmp("tecLevel").disable();
			}
			// 验证设备是否为主设备
			validIsPrimary();
		},
		afterSaveSuccessFn: function(result, response, options){
	    	// 如果是【保存】则默认回显数据保存成功后的实体idx主键
	    	this.saveForm.find('name', 'idx')[0].setValue(result.entity.idx);
	    	this.saveForm.find('name', 'equipmentIdx')[0].setValue(result.entity.equipmentIdx);
	        this.store.reload();
	        alertSuccess();
	    }
	});
	NewBuyEquipment.grid.store.on('beforeload', function(){
		var form = NewBuyEquipment.searchForm.getForm();
		if (!form.isValid()) {
			return;
		}
		var searchParams = form.getValues();
		MyJson.deleteBlankProp(searchParams);
		this.baseParams.entityJson = Ext.encode(searchParams);
	});
	// 按不同颜色区分不同类型的设备
	NewBuyEquipment.grid.store.on('load', function(store, records, options ){
		var flag = true;
		for (var i = 0; i < store.getCount(); i++) {
			var row = NewBuyEquipment.grid.getView().getRow(i);
			if (flag) {
				row.childNodes[0].style.color = 'rgb(108,166,45)';
			} else {
				row.childNodes[0].style.color = 'rgb(56,140,255)';
			}
			var r0 = store.getAt(i);
			var r1 = store.getAt(i + 1);
			if (null != r1 && (r0.data.classCode) != (r1.data.classCode)) {
				flag = !flag;
			}
		}
	});
	/** **************** 定义设备列表结束 **************** */
	
	// 页面自适应布局
	new Ext.Viewport({
		layout: 'border',
		items: [NewBuyEquipment.searchForm, NewBuyEquipment.grid]
	});
	
});