/**
 * 设备主要信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('EquipmentPrimaryInfo');   //定义命名空间
	
	/** **************** 定义私有变量开始 **************** */
	var labelWidth = 100,
		fieldWidth = 140,
		ldsbPanelHeight = 58;
	/** **************** 定义私有变量结束 **************** */
	
	/** **************** 定义私有函数开始 **************** */
	/**
	 * 验证设备是否为主设备，如果“电气系数”大于5、或者“机械系数”大于5、或者“固资原值大”于5000则表示为主设备
	 */
	function validIsPrimary() {
		// 电气系数
		var mechanicalCoefficient  = EquipmentPrimaryInfo.grid.saveForm.find("name", "mechanicalCoefficient")[0].getValue();
		// 机械系统
		var electricCoefficient = EquipmentPrimaryInfo.grid.saveForm.find("name", "electricCoefficient")[0].getValue();
		// 固资源值
		var fixedAssetValue = EquipmentPrimaryInfo.grid.saveForm.find("id", "fixedAssetValue")[0].getValue();
		// 如果“电气系数”大于5、或者“机械系数”大于5、或者“固资原值大”于5000则表示为主设备
		if (mechanicalCoefficient >= 5 
				|| electricCoefficient >= 5
				|| fixedAssetValue >= fixed_asset_value) {
			Ext.getCmp("isPrimaryDevice").setValue(1);
		} else {
			Ext.getCmp("isPrimaryDevice").setValue(0);
		}
	}
	/** **************** 定义私有函数结束 **************** */
	
	/** **************** 定义保存表单开始 **************** */
	EquipmentPrimaryInfo.saveForm = new Ext.form.FormPanel({
		padding: 10,
		baseCls: 'plain', 
		items: [{
			xtype: 'container', layout: 'column', 
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
									Ext.getCmp("equipmentCode").setValue(r.responseText);
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
						  returnField :[{widgetId: "usePerson", propertyName: "empname"}]
					}, {
						fieldLabel: '使用人名称', id: 'usePerson', xtype: 'hidden'
					}]
				}, ManageLevel({hiddenName:'manageLevel', value: '段', fieldLabel: '管理级别'})
				 , YesOrNo({hiddenName:'isDedicated', fieldLabel: '是否专用设备', value: 0})
				 , YesOrNo({hiddenName:'isPrimaryDevice',id:'isPrimaryDevice', value: 0, fieldLabel: '是否主设备', disabled: true})
				 , YesOrNo({hiddenName:'xzState',id:'e_xzState_id', value: 0, fieldLabel: '闲置状态', disabled: true})
				 , {
					  xtype: 'OmOrganization_Win', fieldLabel: '单位',
					  hiddenName: 'orgId', valueField: 'id',
					  rootId: 0, rootText: '铁路总公司', displayField: 'text',
					  width: fieldWidth, 
					  returnField: [{widgetId:"orgName",propertyName:"text"}]
				}, {
					fieldLabel: '单位名称', id: 'orgName', xtype: 'hidden'
				}]
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
					xtype: 'singlefieldcombo',
					fieldLabel: '制造工厂', name: 'makeFactory',
					entity: 'com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo', xfield: 'makeFactory',
					maxLength: '50'
				}, {
					xtype: 'singlefieldcombo',
					fieldLabel: '设置地点', name: 'usePlace',
					entity: 'com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo', xfield: 'usePlace',
					maxLength: '20'
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
				 , {
					fieldLabel: '最大修年度', name: 'maxRepairYear', vtype: "nonNegativeInt", maxLength: 4
				}]
			}, {
				items: [{
					allowBlank: false,
					fieldLabel: '设备名称', name: 'equipmentName', 
					maxLength: 30
				}, {
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
				 , {
					fieldLabel: '购入日期', name: 'buyDate', xtype: 'my97date', format: 'Y-m-d'
				}]
			}]
		}, {
			fieldLabel: '备注', name: 'remark', maxLength: 100, xtype: 'textarea', width: 420, height: 40
		}, {
			/** **************** 保存表单隐藏字段开始 **************** */
			xtype: 'container', layout: 'form', defaultType: 'textfield', hidden: true, items: [{
				fieldLabel: 'idx主键', name: 'idx'
			}, {
				fieldLabel: '技术状态', name: 'tecStatus'
			}]
			/** **************** 保存表单隐藏字段结束 **************** */
		}]
		
	});
	/** **************** 定义保存表单结束 **************** */

	EquipmentPrimaryInfo.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/equipmentPrimaryInfo!pageQuery.action',
	    saveURL: ctx + '/equipmentPrimaryInfo!saveOrUpdate.action',
	    deleteURL: ctx + '/equipmentPrimaryInfo!logicDelete.action',
	    /* 自定义保存表单 */
		saveForm: EquipmentPrimaryInfo.saveForm,
		saveWinWidth: 885,
		/* 自定义保存表单 */
	    viewConfig:true, singleSelect:true,
	    tbar: [{
	    	text:"查看", iconCls:"tableIcon", handler:function(){
	        	var record = null;
	        	if((record = $yd.getSingleRecord(EquipmentPrimaryInfo.grid))){	        		
	        		SR.show(record);
	        	}
	    	}
	    },{
	    	text:"附属设备", iconCls:"pluginIcon", handler:function(){
	        	var record;
	    		if(record = $yd.getSingleRecord(EquipmentPrimaryInfo.grid)){
	    			if(typeof(AffiliatedEquipment) != 'undefined'){
	    				AffiliatedEquipment.showWin(record.get("idx"), true);
	    			}
	    		}
		    }
	    },{
	    	text: "机械动力设备台帐", iconCls: "pluginIcon", handler:function(){
    	    	var sp  = EprimarySearchForm.searchForm.getForm().getValues();
        	   	RangeText.setReportValue("s_elf_id",sp);
        	   	RangeText.setReportValue("s_mef_id",sp);
        	   	RangeText.setReportValue("s_makedate_id",sp);
        	   	RangeText.setReportValue("s_usedate_id",sp);
        	    var cpt = "eprimaryInfo/mp_statistics.cpt";
        	   	report.view(cpt, "机械动力设备台账", sp);
    	}
    },{
    	text:"附属设备台帐",
    	iconCls:"cmpIcon",handler:function(){
    		var sp = EprimarySearchForm.searchForm.getForm().getValues();
    		var records = EquipmentPrimaryInfo.grid.selModel.getSelections();
             if(records.length<1){
             	  MyExt.Msg.alert("尚未选择一条记录！");
             	  return ;
             }
            var cpt = "eprimaryInfo/affiliateEquipment.cpt";
            report.view(cpt, "附属设备台帐", {orgId : sp.orgId,equipmentIdx:records[0].data.idx});
    	}
    },{
    	text:"导出全部设备",
    	iconCls:"pluginIcon",handler:function(){
    		var sp = EprimarySearchForm.searchForm.getForm().getValues();
      	   	RangeText.setReportValue("s_elf_id",sp);
    	   	RangeText.setReportValue("s_mef_id",sp);
    	   	RangeText.setReportValue("s_makedate_id",sp);
    	   	RangeText.setReportValue("s_usedate_id",sp);
    	    var cpt = "eprimaryInfo/all_eprimaryInfo.cpt";
            report.view(cpt, "全部主要设备台账", sp);
    	 }
    },'refresh'],
	fields: [
         Attachment.createColModeJson({
			attachmentKeyName:'primary_equipment_info',
			attachmentKeyIDX: 'idx'
		}),{
			header:'idx主键', dataIndex: 'idx', hidden: true
		},{
			header:'单位名称', dataIndex: 'orgName'
		},{
			header:'单位ID', dataIndex: 'orgId', hidden: true
		},{
			header:'设备名称', dataIndex: 'equipmentName', width: 200
		},{
			header:'设备编号', dataIndex: 'equipmentCode', width: 140
		},{
			header:'设备类别编码', dataIndex: 'classCode'
		},{
			header:'设备类别名称', dataIndex: 'className'
		},{
			header:'固资编号', dataIndex: 'fixedAssetNo'
		},{
			header:'固资原值', dataIndex: 'fixedAssetValue'
		},{
			header:'型号', dataIndex: 'model'
		},{
			header:'规格', dataIndex: 'specification'
		},{
			header:'机械系数', dataIndex: 'mechanicalCoefficient'
		},{
			header:'电气系数', dataIndex: 'electricCoefficient'
		},{
			header:'制造工厂', dataIndex: 'makeFactory'
		},{
			header:'制造年月', dataIndex: 'makeDate', xtype:'datecolumn', format: 'Y-m'
		},{
			header:'使用年月', dataIndex: 'useDate', xtype:'datecolumn', format: 'Y-m'
		},{
			header:'设置地点', dataIndex: 'usePlace'
		},{
			header:'管理级别', dataIndex: 'manageLevel'
		},{
			header:'管理类别', dataIndex: 'manageClass'
		},{
			header:'重量', dataIndex: 'weight'
		},{
			header:'最大修年度', dataIndex: 'maxRepairYear'
		},{
			header:'出厂编号', dataIndex: 'leaveFactoryNo'
		},{
			header:'电气总功率', dataIndex: 'eletricTotalPower'
		},{
			header:'技术等级', dataIndex: 'tecLevel', renderer: getLevel
		},{
			header:'外形尺寸', dataIndex: 'shapeSize'
		},{
			header:'是否主设备', dataIndex: 'isPrimaryDevice', renderer: getYesOrNo
		},{
			header:'是否专用设备', dataIndex: 'isDedicated', renderer: getYesOrNo
		},{
			header:'是否特种设备', dataIndex: 'isSpecialType', renderer: getYesOrNo
		},{
			header:'是否大精设备', dataIndex: 'isExactness', renderer: getYesOrNo
		},{
			header:'是否工装设备', dataIndex: 'isFrock', renderer: getYesOrNo
		},{ 
			header:'设备动态', dataIndex: 'dynamic', renderer: getDynamic
		},{
			header:'封存状态', dataIndex: 'fcState', renderer: getYesOrNo
		},{
			header:'闲置状态', dataIndex: 'xzState', renderer: getYesOrNo
		},{
			header:'出租状态', dataIndex: 'czState', renderer: getYesOrNo
		},{
			header:'设备运行班制', dataIndex: 'runingShifts', hidden: true
		},{
			header:'使用车间', dataIndex: 'useWorkshop', hidden: true
		},{
			header:'使用车间', dataIndex: 'useWorkshopId', hidden: true
		},{
			header:'使用人', dataIndex: 'usePersonId', hidden: true
		},{
			header:'使用人', dataIndex: 'usePerson', hidden: true
		},{
			header:'机械维修班组', dataIndex: 'mechanicalRepairTeamId', hidden: true
		},{
			header:'机械维修班组', dataIndex: 'mechanicalRepairTeam', hidden: true
		},{
			header:'机械维修人', dataIndex: 'mechanicalRepairPersonId', hidden: true
		},{
			header:'机械维修人', dataIndex: 'mechanicalRepairPerson', hidden: true
		},{
			header:'电气维修班组', dataIndex: 'electricRepairTeamId', hidden: true
		},{
			header:'电气维修班组', dataIndex: 'electricRepairTeam', hidden: true
		},{
			header:'电气维修人', dataIndex: 'electricRepairPersonId', hidden: true
		},{
			header:'电气维修人', dataIndex: 'electricRepairPerson', hidden: true
		},{
			header:'备注', dataIndex: 'remark'
		},{
			header:'购置日期', dataIndex: 'buyDate', xtype: "datecolumn", format: 'Y-m-d'
		},{
			header:'技术状态', dataIndex: 'tecStatus', hidden: true
		}],
		
		beforeSaveFn: function(data){ 
			data.useDate = data.useDate + "-01"
			data.makeDate = data.makeDate + "-01";
			return true; 
		},
		
		afterShowEditWin: function(record, rowIndex) {
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
	    }
	});
	EquipmentPrimaryInfo.grid.store.on('beforeload',function(){
		var sp  = EprimarySearchForm.searchForm.getForm().getValues();
		sp = MyJson.deleteBlankProp(sp);
		var whereList = [];
		//var sp = this.sp || {};
		for(var i in sp){
			whereList.push({propName: i, propValue:sp[i]});
		}
		whereList.push({propName: "dynamic", propValues:primary_dynamic, compare: Condition.IN});
		whereList.push({propName: "fixedAssetValue", propValue:fixed_asset_value, compare: Condition.GE});
		RangeText.setPageQuery('s_fdv_id',whereList);
		RangeText.setPageQuery('s_mef_id',whereList);
		RangeText.setPageQuery('s_elf_id',whereList);
		RangeText.setPageQuery('s_makedate_id',whereList);
		RangeText.setPageQuery('s_usedate_id',whereList);
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	//设备主要信息单条记录查看Form
	EquipmentPrimaryInfo.viewForm = new Ext.form.FormPanel({
			layout: "column",
			labelWidth:labelWidth,
			border: false,
			frame:true,
			defaults:{
				layout:'form', 
				columnWidth: 0.33,
				defaultType:'textfield',
				defaults:{
					style:"border:none;background:none;", readOnly:true, anchor:'100%'
				}
			},
			items:[{
	            items: [
	            	{ name:"idx", xtype: "hidden"},
	            	{fieldLabel:"设备编号", name:"equipmentCode",style: "color:blue"},
	            	{fieldLabel:"型号", name:"model",style: "color:blue"},
	            	{fieldLabel:"制造年月", name:"makeDate",style: "color:blue"},
	            	{fieldLabel:"固资编号", name:"fixedAssetNo",style: "color:blue"},
	            	{fieldLabel:"电气总功率", name:"eletricTotalPower",style: "color:blue"},
	            	{fieldLabel:"技术等级", name:"tecLevel",style: "color:blue"},
	            	{fieldLabel:"主设备", name:"isPrimaryDevice",style: "color:blue"},
	            	{fieldLabel:"设备动态", name:"dynamic",style: "color:blue"},
	            	{fieldLabel:"管理类别", name:"manageClass",style: "color:blue"},
	            	{fieldLabel:"使用人", name:"usePerson",style: "color:blue"},
	            	{fieldLabel:"使用车间", name:"useWorkshop",style: "color:blue"}
	            ]
			},{
	            items: [
	            	{fieldLabel:"设备名称", name:"equipmentName",style: "color:blue"},
	            	{fieldLabel:"规格", name:"specification",style: "color:blue"},
	            	{fieldLabel:"使用年月", xtype:'my97date',name:"useDate",style: "color:blue"},
	            	{fieldLabel:"出厂编号", name:"leaveFactoryNo",style: "color:blue"},
	            	{fieldLabel:"机械系数", name:"mechanicalCoefficient",style: "color:blue"},
	            	{fieldLabel:"设置地点", name:"usePlace",style: "color:blue"},
	            	{fieldLabel:"专用设备", name:"isDedicated",style: "color:blue"},
	            	{fieldLabel:"管理级别", name:"manageLevel",style: "color:blue"},
	            	{fieldLabel:"电气维修人", name:"electricRepariPeson",style: "color:blue"},
	            	{fieldLabel:"电气维修班组", name:"electricRepairTeam",style: "color:blue"},
	            	{fieldLabel:"单位", name:"orgName",style: "color:blue"}
	            ]
			}, {
	            items: [
	            	{fieldLabel:"设备类别", name:"classCode", style: "color:blue"},
	            	{fieldLabel:"重量", name:"weight",style: "color:blue"},
	            	{fieldLabel:"固资原值", name:"fixedAssetValue",style: "color:blue"},
	            	{fieldLabel:"制造工厂", name:"makeFactory",style: "color:blue"},
	            	{fieldLabel:"电气系数", name:"electricCoefficient",style: "color:blue"},
	            	{fieldLabel:"外形尺寸", name:"shapeSize",style: "color:blue"},
	            	{fieldLabel:"特种设备", name:"isSpecialType",style: "color:blue"},
	            	{fieldLabel:"机械维修人", name:"mechanicalRepariPeson",style: "color:blue"},
	            	{fieldLabel:"机械维修班组", name:"mechanicalRepairTeam",style: "color:blue"},
	            	{fieldLabel:"设备运行班制", name:"runingShifts",style: "color:blue"}
	            	
	            ]
			}]
	});
	
	//设备主要信息单条记录查看Win
	EquipmentPrimaryInfo.viewWin = new Ext.Window({
	   	title: "设备主要详细信息",width:700, height:400, maximizable:false, layout: "fit", 
		closeAction: "hide", modal: true, maximized: false , buttonAlign:"center",
		items:[ EquipmentPrimaryInfo.viewForm ],
		buttons:[{
	      text: "关闭", iconCls: "closeIcon", 
	      handler: function(){ 
	           EquipmentPrimaryInfo.grid.store.reload();
	           EquipmentPrimaryInfo.viewWin.hide();
	         }
	     }]
	});
	
	// 设备主要信息查看数据
	EquipmentPrimaryInfo.showWin = function(record){
			EquipmentPrimaryInfo.viewForm.getForm().loadRecord(record);
			EquipmentPrimaryInfo.viewWin.show();
	 }	
	
	//页面自适应布局
	EquipmentPrimaryInfo.viewport = new Ext.Viewport({
		layout: "fit",
		items:[{
			layout: "border",
			border: false, defaults: {
				border: false
			},
			items:[{
				title: '查询',
				region: "north",
//				baseCls:"x-plain",
				collapsible: true,
				items: [EprimarySearchForm.searchForm],
				autoHeight: true,
				listeners: {
					resize: function(){
					this.doLayout();
					}
				}
			},{
				region: "center",
				layout: "fit",
				id: "x",
				bodyStyle:'padding-left:0px;', 
				bodyBorder: true,
				items: [EquipmentPrimaryInfo.grid]
			}],
			listeners: {
				resize: function(){
					this.doLayout();
				}
			}
		}]
	});
	
	//重写EprimarySearchForm中的search和reset方法
	search = function(){
		EquipmentPrimaryInfo.grid.store.load();
	}
	reset = function(){
		var form =  EprimarySearchForm.searchForm;
		form.findByType('OmOrganizationCustom_comboTree')[0].clearValue();
		form.getForm().reset();
		EquipmentPrimaryInfo.grid.store.load();
	}
});