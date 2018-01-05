/**
 * 业务编码规则配置属性 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
//定义命名空间
Ext.namespace("CodeRuleConfigProp");
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
CodeRuleConfigProp.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: i18n.CodeRuleConfigProp.wait });
//表单组件高宽等设置
CodeRuleConfigProp.labelWidth = 100;
CodeRuleConfigProp.fieldWidth = 180;
CodeRuleConfigProp.ruleIDX;
//记录属性类型的数据容器
CodeRuleConfigProp.propertyTypeStore = new Ext.data.SimpleStore({
    fields: ['value', 'text'],
    data : i18n.CodeRuleConfigProp.textArray
});

//属性类型为流水号时的属性值选择容器
CodeRuleConfigProp.type_1 = new Ext.data.SimpleStore({
    fields: ['value', 'text'],
    data :i18n.CodeRuleConfigProp.numArray
});
//属性类型为日期格式时的属性值选择容器
CodeRuleConfigProp.type_2 = new Ext.data.SimpleStore({
    fields: ['value', 'text'],
    data : i18n.CodeRuleConfigProp.dateArray
});
//信息表单
CodeRuleConfigProp.form = new Ext.form.FormPanel({
    layout: "form", 	border: false, 		style: "padding:10px", 		labelWidth: CodeRuleConfigProp.labelWidth,
    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "98%" },
    items: [{
        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
        items: [
    	{
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: CodeRuleConfigProp.labelWidth, 	 columnWidth: 1, 
            items: [
				{ name: "propertyName", fieldLabel: i18n.CodeRuleConfigProp.propertyName,  maxLength: 50,  allowBlank: false, width: CodeRuleConfigProp.fieldWidth },
				new Ext.form.ComboBox({
						id:"propertyTypecombo",
                        fieldLabel: i18n.CodeRuleConfigProp.propertyTypecombo,
                        hiddenName:'propertyType',
                        store: CodeRuleConfigProp.propertyTypeStore,
                        valueField:'value',
                        displayField:'text',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'',
                        allowBlank:false,
                        selectOnFocus:true,
                        width:180
                    }),
				{id:"type_0", fieldLabel: i18n.CodeRuleConfigProp.propertyValue,  maxLength: 50, width: CodeRuleConfigProp.fieldWidth },
				new Ext.form.ComboBox({
						id:"type_1",
                        fieldLabel: i18n.CodeRuleConfigProp.propertyValue,
                        store: CodeRuleConfigProp.type_1,
                        valueField:'value',
                        displayField:'text',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'',
                        selectOnFocus:true,
                        width:180,
                        hidden : true
                    }),
                    new Ext.form.ComboBox({
						id:"type_2",
                        fieldLabel: i18n.CodeRuleConfigProp.propertyValue,
                        store: CodeRuleConfigProp.type_2,
                        valueField:'value',
                        displayField:'text',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'',
                        selectOnFocus:true,
                        width:180,
                        hidden : true
                    }),
				{ name: "orderNo", fieldLabel: i18n.CodeRuleConfigProp.orderNo, xtype: "numberfield", maxLength: 8,  allowBlank: false, width: CodeRuleConfigProp.fieldWidth }
            ]
    	},
        {id:"idx",xtype: "hidden", name: "idx"},
        {id:"ruleIDX",xtype: "hidden", name: "ruleIDX"},
        {id:"propertyValue",xtype: "hidden", name: "propertyValue"}
        ]
    }]
});
//根据属性类型的不同，显示不同的属性值输入控件
Ext.getCmp("propertyTypecombo").on('select',function(){
	var propertyTypecombo = Ext.getCmp("propertyTypecombo").getValue();
	if(propertyTypecombo == 0 || propertyTypecombo == 3){
		 Ext.getCmp("type_0").setValue("");
         Ext.getCmp("type_0").show();
         Ext.getCmp("type_1").hide();
         Ext.getCmp("type_2").hide();
	}else if(propertyTypecombo == 1){
	         Ext.getCmp("type_0").hide();
	         Ext.getCmp("type_1").show();
	         Ext.getCmp("type_2").hide();
	}else if(propertyTypecombo == 2){
	         Ext.getCmp("type_0").hide();
	         Ext.getCmp("type_1").hide();
	         Ext.getCmp("type_2").show();
	}
});
//新增编辑窗口
CodeRuleConfigProp.win = new Ext.Window({
    title: i18n.CodeRuleConfigProp.add,	maximizable: true, 		width: 350, 	height: 230,
    plain: true,   	closeAction: "hide", 	items: CodeRuleConfigProp.form,
    buttonAlign: "center",
    buttons: [{
        text: i18n.CodeRuleConfigProp.save, iconCls: "saveIcon",
        handler: function(){
            var form = CodeRuleConfigProp.form.getForm();
            if (!form.isValid()) return;
            var url = ctx + "/codeRuleConfigProp!saveOrUpdate.action";
            var propertyTypecombo = Ext.getCmp("propertyTypecombo").getValue();
			if(propertyTypecombo == 0 || propertyTypecombo == 3){
		         Ext.getCmp("propertyValue").setValue(Ext.getCmp("type_0").getValue());
			}else if(propertyTypecombo == 1){
		        	 Ext.getCmp("propertyValue").setValue(Ext.getCmp("type_1").getValue());
			}else if(propertyTypecombo == 2){
		         	 Ext.getCmp("propertyValue").setValue(Ext.getCmp("type_2").getValue());
			}
            var data = form.getValues();
            for(var i in data){
            	if(i.indexOf("_") > 0) delete data[i]
            }
            CodeRuleConfigProp.loadMask.show();
            Ext.Ajax.request({
                url: url,
                jsonData: data,
                success: function(response, options){
                  	CodeRuleConfigProp.loadMask.hide();
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        alertSuccess();
                        CodeRuleConfigProp.store.reload();
                    } else {
                        alertFail(result.errMsg);
                    }
                },
                failure: function(response, options){
                    CodeRuleConfigProp.loadMask.hide();
                    MyExt.Msg.alert(i18n.CodeRuleConfigProp.false+"\n" + response.status + "\n" + response.responseText);
                }
            });
        }
    },{
    	 text:i18n.CodeRuleConfigProp.close, iconCls:"closeIcon",handler:function(){
    	 	CodeRuleConfigProp.win.hide();
    	 }
    }]
});

//数据容器
CodeRuleConfigProp.store = new Ext.data.JsonStore({
	id: "idx", root: "root", totalProperty: "totalProperty", remoteSort: true,
    url: ctx + "/codeRuleConfigProp!pageList.action",
    fields: [ "propertyName","propertyValue","propertyType","orderNo","ruleIDX","idx" ]
});
////选择模式，勾选框可多选
CodeRuleConfigProp.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
////分页工具
CodeRuleConfigProp.pagingToolbar = Ext.yunda.createPagingToolbar({store: CodeRuleConfigProp.store});
//页面列表
CodeRuleConfigProp.grid = new Ext.grid.GridPanel({
    border: false, enableColumnMove: true, stripeRows: true, loadMask: {msg:i18n.CodeRuleConfigProp.loading}, selModel: CodeRuleConfigProp.sm, viewConfig: {forceFit: true},
    colModel: new Ext.grid.ColumnModel({
    	defaultSortable: true,
    	columns: [
	        CodeRuleConfigProp.sm,
	        new Ext.grid.RowNumberer(),
	        { header: i18n.CodeRuleConfigProp.propertyName,  dataIndex: "propertyName" },			
	        { header: i18n.CodeRuleConfigProp.propertyValue,  dataIndex: "propertyValue",
	        renderer : function(value, metaData, record, rowIndex, colIndex, store){
	        	if(value=="yyyyMMdd") return i18n.CodeRuleConfigProp.returnYMD;
	        	if(value=="yyyyMMddHHmmss") return i18n.CodeRuleConfigProp.returnYMDHMS;
	        	if(value=="6") return i18n.CodeRuleConfigProp.returnSix;
	        	if(value=="8") return i18n.CodeRuleConfigProp.returnEight;
	            else return value;
	       	} },			
	        { header: i18n.CodeRuleConfigProp.propertyTypecombo,  dataIndex: "propertyType" ,
	        renderer : function(value, metaData, record, rowIndex, colIndex, store){
	        	if(value==0) return i18n.CodeRuleConfigProp.returnText;
	            if(value==1) return i18n.CodeRuleConfigProp.returnNum;
	            if(value==2) return i18n.CodeRuleConfigProp.returnDate;
	            if(value==3) return i18n.CodeRuleConfigProp.returnSepar;
	       	}
          	},			
	        { header: i18n.CodeRuleConfigProp.orderNo,  dataIndex: "orderNo" }			
    ]}),
    store: CodeRuleConfigProp.store,					//数据容器
    tbar: [{								//工具栏
        text: i18n.CodeRuleConfigProp.add, iconCls: "addIcon", handler: function(){
        	CodeRuleConfigProp.searchWin.hide();
            CodeRuleConfigProp.win.setTitle(i18n.CodeRuleConfigProp.add);
            CodeRuleConfigProp.win.show();
            CodeRuleConfigProp.form.getForm().reset();
            Ext.getCmp("propertyTypecombo").setValue(0);
            Ext.getCmp("type_0").setValue("");
	        Ext.getCmp("type_0").show();
	        Ext.getCmp("type_1").hide();
	        Ext.getCmp("type_2").hide();
            Ext.getCmp("ruleIDX").setValue(CodeRuleConfigProp.ruleIDX);
        }
    },{
        text: i18n.CodeRuleConfigProp.delete, iconCls: "deleteIcon",
        handler: function(){
            var sm = CodeRuleConfigProp.grid.getSelectionModel();
            if (sm.getCount() < 1) {
                MyExt.Msg.alert(i18n.CodeRuleConfigProp.NoChoice);
                return;
            }
            CodeRuleConfigProp.win.hide();
            Ext.Msg.confirm(i18n.CodeRuleConfigProp.prompt, i18n.CodeRuleConfigProp.YN, function(btn){
                if(btn == "yes"){
                    var data = sm.getSelections();
                    var ids = new Array();
                    for (var i = 0; i < data.length; i++){
                        ids.push(data[ i ].get("idx"));
                    }
                    CodeRuleConfigProp.win.hide();
                    Ext.Ajax.request({
                        url: ctx + "/codeRuleConfigProp!deleteConfigByIds.action",
                        params: {ids: ids},
                        success: function(response, options){
                            var result = Ext.util.JSON.decode(response.responseText);
                            if (result.errMsg == null) {
                                alertSuccess();
                                CodeRuleConfigProp.store.reload();    
                            } else {
                                alertFail(result.errMsg);
                            }
                        },
                        failure: function(response, options){
                            MyExt.Msg.alert(i18n.CodeRuleConfigProp.false+"\n" + response.status + "\n" + response.responseText);
                        }
                    });
                }
            });
        }	
    },"-",{ xtype:"label",text: i18n.CodeRuleConfigProp.eg},{id:"demo",xtype:"tbtext",width:200}],
    bbar: CodeRuleConfigProp.pagingToolbar,
    listeners: {
        "rowdblclick": {
            fn: function(grid, idx, e){
            	CodeRuleConfigProp.searchWin.hide();
                var r = grid.store.getAt(idx);
//                CodeRuleConfigProp.win.setIconClass("edit1Icon");
                CodeRuleConfigProp.win.setTitle(i18n.CodeRuleConfigProp.edit);
                CodeRuleConfigProp.win.show();
                CodeRuleConfigProp.form.getForm().reset();
                CodeRuleConfigProp.form.getForm().loadRecord(r);
                if(r.get("propertyType") == 0 || r.get("propertyType") == 3){
			         Ext.getCmp("type_0").show();
			         Ext.getCmp("type_1").hide();
			         Ext.getCmp("type_2").hide();
			         Ext.getCmp("type_0").setValue(r.get("propertyValue"));
				}else if(r.get("propertyType") == 1){
				         Ext.getCmp("type_0").hide();
				         Ext.getCmp("type_1").show();
				         Ext.getCmp("type_2").hide();
			        	 Ext.getCmp("type_1").setValue(r.get("propertyValue"));
				}else if(r.get("propertyType") == 2){
				         Ext.getCmp("type_0").hide();
				         Ext.getCmp("type_1").hide();
				         Ext.getCmp("type_2").show();
			         	 Ext.getCmp("type_2").setValue(r.get("propertyValue"));
				}
            }
        }
    }       
});
CodeRuleConfigProp.grid.store.on("load", function(){
			var count = CodeRuleConfigProp.store.getCount();
			var str="";
			for(var i=0;i<count;i++){
				//属性类型为日期格式
				if(CodeRuleConfigProp.store.getAt(i).data["propertyType"]==2){
					var fmt = "Ymd";
					if(CodeRuleConfigProp.store.getAt(i).data["propertyValue"]=="yyyyMMddHHmmss"){
						fmt = "YmdHis";
					}else if(CodeRuleConfigProp.store.getAt(i).data["propertyValue"]=="yyyyMMdd"){
						fmt = "Ymd";
					}else if(CodeRuleConfigProp.store.getAt(i).data["propertyValue"]=="yyyyMM"){
						fmt = "Ym";
					}else if(CodeRuleConfigProp.store.getAt(i).data["propertyValue"]=="yyMM"){
						fmt = "ym";
					}else if(CodeRuleConfigProp.store.getAt(i).data["propertyValue"]=="yyMMdd"){
						fmt = "ymd";
					}
					var sysdate = new Date().format(fmt);
					str = str+sysdate;
				}else if(CodeRuleConfigProp.store.getAt(i).data["propertyType"]==1){  //流水号
					var ran = "1";
					var random_num = CodeRuleConfigProp.store.getAt(i).data["propertyValue"];
					for(var j=1;j<random_num;j++){
						ran = "0"+ran;
					}
					str = str+ran;
				}
				else str = str+CodeRuleConfigProp.store.getAt(i).data["propertyValue"]
			}
			Ext.getCmp("demo").setText(str);
		});
//查询参数表单
CodeRuleConfigProp.searchForm = new Ext.form.FormPanel({
    layout: "form", 	border: false,  	style: "padding:10px", 		labelWidth: CodeRuleConfigProp.labelWidth,
    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },    
    items: [{
        xtype: "panel", border: false, baseCls: "x-plain", layout: "column", align: "center", 
        items: [
        {
            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
            labelWidth: CodeRuleConfigProp.labelWidth, 	 columnWidth: 1, 
            items: [
				{ name: "propertyName", fieldLabel: i18n.CodeRuleConfigProp.propertyName, width: CodeRuleConfigProp.fieldWidth },
				{ name: "propertyValue", fieldLabel: i18n.CodeRuleConfigProp.propertyValue, width: CodeRuleConfigProp.fieldWidth },
				{ name: "propertyType", fieldLabel: i18n.CodeRuleConfigProp.propertyTypecombo,xtype: "numberfield", width: CodeRuleConfigProp.fieldWidth },
				{ name: "orderNo", fieldLabel: i18n.CodeRuleConfigProp.orderNo,xtype: "numberfield", width: CodeRuleConfigProp.fieldWidth }
            ]
        }
        ]
    }]
});
//查询参数对象
CodeRuleConfigProp.searchParam = {};
//查询窗口
CodeRuleConfigProp.searchWin = new Ext.Window({
    title: i18n.CodeRuleConfigProp.search, 	width: 600, 	height: 400, 
    plain: true, 		closeAction: "hide", 	items: CodeRuleConfigProp.searchForm, 
    buttonAlign:"center",
    buttons: [{
        text: i18n.CodeRuleConfigProp.search, iconCls: "searchIcon", handler: function(){ 
		    CodeRuleConfigProp.searchParam = CodeRuleConfigProp.searchForm.getForm().getValues();
		    var searchParam = CodeRuleConfigProp.searchForm.getForm().getValues();
		    searchParam = MyJson.deleteBlankProp(searchParam);
		    CodeRuleConfigProp.store.load({
		        params: {
                    start: 0,    limit: CodeRuleConfigProp.pagingToolbar.pageSize,
                    entityJson: Ext.util.JSON.encode(searchParam)
                }	    
		    });
        }
    }, {
        text: i18n.CodeRuleConfigProp.reset, iconCls: "resetIcon", handler: function(){ CodeRuleConfigProp.searchForm.getForm().reset(); }
    }, {
        text: i18n.CodeRuleConfigProp.close, iconCls: "closeIcon", scope: this, handler: function(){ CodeRuleConfigProp.searchWin.hide(); }                
    }]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:"fit", items:CodeRuleConfigProp.grid });
});