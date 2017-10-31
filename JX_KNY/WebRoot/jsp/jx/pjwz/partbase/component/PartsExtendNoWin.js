/** 配件扩展编号详情显示通用js */
Ext.onReady(function() {
	Ext.ns('jx.pjwz.partbase.component.PartsExtendNoWin');
	jx.pjwz.partbase.component.PartsExtendNoWin.createWin = function(extendNoJson, formColNum) {
		var fields = Ext.util.JSON.decode(extendNoJson);
		var saveFields = [];
		this.fields = fields;
		for (var i = 0; i < fields.length; i++) {
			var field = fields[i];
			var editor = {};
			editor.name = field.extendNoField;
			editor.fieldLabel = field.extendNoName;
			editor.value = field.value;
			saveFields.push(editor);
		}
		var formRowNum = Math.round(fields.length / formColNum) ;
		
		var formItems = saveFields;
		if (formColNum >= 2) {
			// 当列数formColNum>=2时，表单采用列布局
			formItems = {
				xtype : "panel", border : false, baseCls : "x-plain",
				layout : "column", align : "center", items : []
			};
			// 每列百分比宽度
			var columnWidth = 1 / formColNum;
			// 创建每列panle的配置项
			for (var i = 0; i < formColNum; i++) {
				formItems.items.push({
					baseCls : "x-plain", align : "center", style : "padding:3px",
					layout : "form", defaultType : "textfield", labelWidth : 90,
					columnWidth : columnWidth, items : []
				});
			}
			var j = 0;
			for (var i = 0; i < saveFields.length; i++) {
				formItems.items[j].items.push(saveFields[i]);
				j++;
				if (j >= formItems.items.length) j = 0;
			}
		}
		// 生成FormPanel
		var form = new Ext.form.FormPanel({
			layout : "form", border : false, style : "padding:10px", labelWidth : 90,
			baseCls : "x-plain", align : "center", defaultType : "textfield",
			defaults : { anchor : "98%" }, items : formItems
		});
		
		var winWidth = (90 + 150 + 8) * formColNum + 60;
		var winHeight = 28 * formRowNum + 80;
		var win = new Ext.Window({
			title : '配件扩展编号', width : winWidth, height : winHeight, plain : true,
			closeAction : "hide", buttonAlign : 'center', maximizable : true,
			items : form, modal: true,
			buttons : [{
				text : "关闭", iconCls : "closeIcon", scope : this,
				handler : function() { win.hide(); }
			}]
		});
		win.show();
	}
	
	jx.pjwz.partbase.component.PartsExtendNoWin.getExtendNo = function(v) {
		// 如果”extendNoJson“字段内容为空，则不进行回显
		if (Ext.isEmpty(v)) {
			return "";
		}
		// 遍历”extendNoJson“字段内容，构造扩展编号的缩略显示字符串
		var jsonData = eval(v);
		var extendNo = '';
		if (jsonData.length <= 0) {
			return "";
		}
		for (var i = 0; i < jsonData.length; i++) {
			var value = jsonData[i].value;
			if (Ext.isEmpty(value) || null == value || undefined == value) {
				continue;
			}
			extendNo += "|";			// 扩展编号分隔符
			extendNo += value;			// 扩展编号值[i]
		}
		return extendNo.substr(1);
	}
	
});