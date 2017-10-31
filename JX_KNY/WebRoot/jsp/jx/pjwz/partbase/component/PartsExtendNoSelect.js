/** 配件扩展编号设置控件 */
Ext.ns('jx.pjwz.partbase.component.PartsExtendNoSelect');
jx.pjwz.partbase.component.PartsExtendNoSelect = Ext.extend(
		Ext.form.TriggerField, {
			partsTypeIDX : "", // 前台传入的配件规格型号主键，必须设置
			form : null, // 新增编辑表单
			win : null, // 新增编辑窗口
			winWidth : null, // 新增编辑窗口宽度，默认null，可自定义配置，若为null将自动根据this.labelWidth + this.fieldWidth + 30进行计算
			winHeight : null, // 新增编辑窗口高度，默认null，可自定义配置，若为null新增编辑窗口自适应高度
			fields : null, // 表单动态json数组
			labelWidth : 90, // 表单中的标签宽度
			fieldWidth : 150, // 表单中的输入控件宽度
			formColNum : 1, // 新增编辑表单中的控件用几列进行布局，默认1（大于4会强制等于4），可自定义配置，当列数>=2时，表单采用列布局
			isEdit: true, //是否可编辑保存
			triggerClass : 'x-form-search-trigger', // 一个额外的CSS类，用来定义触发按钮样式
			initComponent : function(config) {
				jx.pjwz.partbase.component.PartsExtendNoSelect.superclass.initComponent.call(this);
				Ext.apply(this, config);
			},
			onTriggerClick : function() {
				if (this.disabled) return;
				this.init();
				if (this.win == null) this.createWin();
			},
			returnFn : function(value, jsonValue) { // 选择确定后触发函数，用于处理返回记录
			},
			init : function() {
				this.win = null;
				this.form = null;
			},
			saveFn : function() {
				var form = this.form.getForm();
				if (!form.isValid()) return;
				var data = form.getValues();
				var result = [];
				if(this.fields != null) {
					for (var i = 0; i < this.fields.length; i++) {
						var editor = this.fields[i];
						for (var prop in data) {
							if (prop == editor.extendNoField && !Ext.isEmpty(data[prop])) {
								editor.value = data[prop];
								break;
							}
						}
						result.push(editor);
					}
				}
				this.fields = result;
				var extendNo = "";
				for (var prop in data) {
					if (!Ext.isEmpty(data[prop])) {
						extendNo += data[prop] + "|";
					}
				}
				if (!Ext.isEmpty(extendNo)) {
					extendNo = extendNo.substring(0, extendNo.length - 1);
				}
				this.returnFn(extendNo, Ext.util.JSON.encode(this.fields));
				this.win.hide();
			},
			createForm : function() {
				if (Ext.isEmpty(this.fields)) {
					Ext.Ajax.request({
						url : ctx + "/partsExtendNo!getUsedJsonByPartsType.action",
						params : {
							partsTypeIDX : this.partsTypeIDX
						},
						scope : this,
						success : function(response, options) {
							var result = Ext.util.JSON.decode(response.responseText);
							if (!Ext.isEmpty(result)) {								
								this.createFormFields(result);
							} else {
								return;
							}
						}
					});
				} else {
					var result = this.fields;
					if (!Ext.isEmpty(result)) {
						this.createFormFields(result);
					} else {
						return;
					}
				}

			},
			createFormFields : function(result) {
				var fields = result;
				var saveFields = [];
				this.fields = result;
				for (var i = 0; i < fields.length; i++) {
					var field = fields[i];
					var editor = {};
					editor.name = field.extendNoField;
					editor.fieldLabel = field.extendNoName;
					if (!Ext.isEmpty(this.fields)) {
						editor.value = field.value;
					}
					editor.maxLength = 50;
					saveFields.push(editor);
				}
				
				var formItems = saveFields;
				
				var formRowNum = Math.round(saveFields.length / this.formColNum) ;
				if (this.formColNum >= 2) {
					// 当列数formColNum>=2时，表单采用列布局
					formItems = {
						xtype : "panel", border : false, baseCls : "x-plain",
						layout : "column", align : "center", items : []
					};
					// 每列百分比宽度
					var columnWidth = 1 / this.formColNum;
					// 创建每列panle的配置项
					for (var i = 0; i < this.formColNum; i++) {
						formItems.items.push({
							baseCls : "x-plain", align : "center", style : "padding:3px",
							layout : "form", defaultType : "textfield", labelWidth : this.labelWidth, 
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
				this.form = new Ext.form.FormPanel({
					layout : "form", border : false, style : "padding:10px",
					labelWidth : 90, baseCls : "x-plain", align : "center", defaultType : "textfield",
					defaults : { anchor : "98%" }, items : formItems
				});
				if (this.winWidth == null)
					this.winWidth = (this.labelWidth + this.fieldWidth + 8) * this.formColNum + 60;
				if (this.winWidthHeight == null)
					this.winHeight = 28 * formRowNum + 80;
				if (!this.isEdit) {
					this.win = new Ext.Window({
						title : '配件扩展编号', width : this.winWidth, height : this.winHeight,
						plain : true, closeAction : "hide", buttonAlign : 'center',
						maximizable : true, items : this.form, modal: true,
						buttons : [{
							text : "关闭", iconCls : "closeIcon", scope : this,
							handler : function() { this.win.hide(); }
						}]
					});
				} else {
					this.win = new Ext.Window({
						title : '配件扩展编号', width : this.winWidth, height : this.winHeight,
						plain : true, closeAction : "hide", buttonAlign : 'center',
						maximizable : true, items : this.form, modal: true,
						buttons : [{
							text : "保存", iconCls : "saveIcon", scope : this,
							handler : this.saveFn
						}, {
							text : "关闭", iconCls : "closeIcon", scope : this,
							handler : function() { this.win.hide(); }
						}]
					});
				}
				this.win.show();
			},
			// 创建弹出窗口
			createWin : function() {
				if (this.form == null) this.createForm();
			}
		});
// 注册为Ext容器组件
Ext.reg('PartsExtendNoSelect', jx.pjwz.partbase.component.PartsExtendNoSelect);