//设备概况
Ext.onReady(function(){
	
	var id = "Info", form, idx;
	Ext.ns(id);
	
	window[id].load = function(_idx){
		idx = _idx;
		var record = $yd.getSingleRecord(EquipmentPrimaryInfo.grid);
		form.getForm().loadRecord(record);
		setDateTextForForm(form, record, "useDate", "Y-m");
		setDateTextForForm(form, record, "makeDate", "Y-m");
		setDateTextForForm(form, record, "buyDate", "Y-m-d");
		setDictConvertForForm(form, record, "tecLevel", getLevel);
		setDictConvertForForm(form, record, "isPrimaryDevice", getYesOrNo);
		setDictConvertForForm(form, record, "dynamic", getDynamic);
	};
	
	form = getDsiplayForm({fields: EquipmentPrimaryInfo.grid.fields, cols: [.5, .5], labelWidth: 120}, {style: "padding:15px", labelAlign: "right"});
	SR.addContent(id, form);
});