//主设备查询页面表单
Ext.onReady(function(){
	Ext.namespace('EprimarySearchForm');   //定义命名空间
	
	var whereList = [{propName: 'orgId', propValue: systemOrgseq, StringLike: true}, 
					 {propName: 'fixedAssetValue', propValue: fixed_asset_value, compare: Condition.GE}, 
					 {propName: 'dynamic', propValues: primary_dynamic, compare: Condition.IN}]	
	//查询表单
	EprimarySearchForm.searchForm = new Ext.form.FormPanel({
		layout:"column", padding: 10, frame:true, labelWidth:80,
		defaults:{
			layout:"form",
			width:260,
			defaults:{
				anchor:"90%",
				xtype: "singlefieldcombo",
				entity: 'com.yunda.sb.equipmentprimaryinfo.entity.EquipmentPrimaryInfo',
				editable: true, 
				typeAhead:false
			}
		},
		items:[{
			items:[{
				 id: 's_orgName_id', xtype: 'OmOrganizationCustom_comboTree', fieldLabel: '单位',
				 hiddenName: 'orgId', selectNodeModel:'exceptRoot' , width: 140, editable: false,
				 listeners : {
					select: function () {
						EquipmentPrimaryInfo.grid.store.load();
						// 重新加载查询条件控件数据源
						var cmps = EprimarySearchForm.searchForm .findByType('singlefieldcombo');
						for (var i = 0; i < cmps.length; i++) {
							cmps[i].reset();
							cmps[i].whereList[0].propValue = this.getValue();
							cmps[i].reload();
						}
					}
       			 }
			},{
				fieldLabel:"制造年月", 
				name:"makeDate",
				xtype: "rangetext", 
				type: 'date', 
				id: "s_makedate_id", 
				clientWidth: 100
			}]
		},{
			items:[{
				fieldLabel: "设备名称",
				xfield: "equipmentName",
				whereList: whereList	
			},YesOrNo({fieldLabel:"是否主设备",hiddenName:'isPrimaryDevice'},true)]
		},{
			items:[{
				fieldLabel: "设备编号",
				xfield: "equipmentCode",
				whereList: whereList
			},YesOrNo({fieldLabel:"是否工装设备",hiddenName: 'isFrock'}, true)]
		},{
			items:[{
				fieldLabel:"机械系数", 
				name:"mechanicalCoefficient",
				xtype: "rangetext", 
				type: 'number', 
				id: "s_mef_id", 
				clientWidth: 100
			},
			YesOrNo({fieldLabel:"是否特种设备",hiddenName:'isSpecialType'},true )]
		},{
			items:[{
				fieldLabel:"电气系数", 
				name:"electricCoefficient",
				xtype: "rangetext", 
				type: 'number', 
				id: "s_elf_id",
				clientWidth: 100
			},YesOrNo({fieldLabel:"是否专用设备",hiddenName:'isDedicated'}, true)]
		},{
			items:[{
				fieldLabel:"使用年月", 
				name:"useDate",
				xtype: "rangetext", 
				type: 'date', 
				id: "s_usedate_id", 
				clientWidth: 100
			},YesOrNo({fieldLabel:"是否大精设备",hiddenName: 'isExactness'},true )]
		}],
		buttonAlign: 'center',
		buttons:[{
			text:'查询', iconCls:'searchIcon', handler: function() { search(); }
		},{
			text:'重置', iconCls:'resetIcon',  handler: function() { reset(); }
		}]
	 });
	
});

	var search = function(){}
	var reset = function(){}
	
