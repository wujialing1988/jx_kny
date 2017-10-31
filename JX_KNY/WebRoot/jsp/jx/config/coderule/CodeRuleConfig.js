/**
 * 业务编码规则配置 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
//定义命名空间
Ext.namespace("CodeRuleConfig");
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
CodeRuleConfig.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
//表单组件高宽等设置
CodeRuleConfig.labelWidth = 100;
CodeRuleConfig.fieldWidth = 180;

//新增编辑窗口
CodeRuleConfig.win = new Ext.Window({
    title: "业务编码规则属性",	maximizable: true, 		width: 530, 	height: 400,
    plain: true,   	closeAction: "hide", 	layout: "fit",items:CodeRuleConfigProp.grid
//    items: {
//        xtype:"tabpanel",id:"CodeRuleConfig_win", activeTab:0, enableTabScroll:true, border:false,
//        items: [{
//            title:"业务编码规则", border:false, layout:"fit", frame: true, items: CodeRuleConfig.form
//        },{
//            title:"业务编码规则属性", border:false, layout:"fit", items: CodeRuleConfigProp.grid
//        }]
//    }
});
//编辑记录前触发的函数
CodeRuleConfig.beforeeditFn = function(rowEditor, rowIndex){
	return true;
}
//保存前触发函数
CodeRuleConfig.beforeSaveFn = function(rowEditor, changes, record, rowIndex){
	delete record.data["operate"];
	return true;
}
//删除前触发函数
CodeRuleConfig.deleteButtonFn = function(rowEditor, changes, record, rowIndex){
	return true;
}
////列表
CodeRuleConfig.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + "/codeRuleConfig!pageList.action",                           //装载列表数据的请求URL
    saveURL: ctx + "/codeRuleConfig!saveOrUpdate.action",                   //保存数据的请求URL
    deleteURL: ctx + "/codeRuleConfig!deleteConfigByIds.action",                  //删除数据的请求URL
    beforeSaveFn: CodeRuleConfig.beforeSaveFn,                              //保存记录前触发的函数
    beforeeditFn: CodeRuleConfig.beforeeditFn,                              //编辑记录前触发的函数
//    deleteButtonFn:CodeRuleConfigProp.deleteButtonFn,
    fields: [{                                                                //列、store、编辑控件，必须配置
        header: '规则名称', dataIndex: 'ruleName', editor: { allowBlank: false }
    },{
        header: '功能点', dataIndex: 'ruleFunction',  editor: { allowBlank: false }     
    },{ header: "操作", dataIndex:"operate",renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
        	var html = "";
    		html = "<span><a href='#' onclick='CodeRuleConfig.showProWin(" + rowIndex + ")'>属性配置</a></span>";
            return html;
        },searcher:{disabled:true}},{
        header: 'idx主键', dataIndex: 'idx', hidden: true
    }],
    defaultData: {idx: null},                 //新增时默认Record记录值，必须配置
  	searchFn: function(searchParam){
  		CodeRuleConfig.searchParam = searchParam;
        this.store.load();	
    }
});
CodeRuleConfig.showProWin = function(idx){
			if(CodeRuleConfig.grid.searchWin != null) CodeRuleConfig.grid.searchWin.hide();
            var r = CodeRuleConfig.grid.store.getAt(idx);
//                CodeRuleConfig.win.setIconClass("edit1Icon");
            CodeRuleConfig.win.setTitle("业务编码规则属性");
            CodeRuleConfig.win.show();
        	CodeRuleConfigProp.ruleIDX = r.get("idx");
        	CodeRuleConfigProp.grid.store.on("beforeload", function(){
				var param = {ruleIDX: CodeRuleConfigProp.ruleIDX};
				this.baseParams.entityJson = Ext.util.JSON.encode(param);  
			});
			CodeRuleConfigProp.grid.store.load();
}
//查询参数对象
CodeRuleConfig.searchParam = {};
CodeRuleConfig.grid.store.on('beforeload',function(){
	CodeRuleConfig.grid.store.baseParams.entityJson = Ext.util.JSON.encode(CodeRuleConfig.searchParam);
})
//查询参数表单
//CodeRuleConfig.searchForm = new Ext.form.FormPanel({
//    layout: "form", 	border: false,  	style: "padding:10px", 		labelWidth: CodeRuleConfig.labelWidth,
//    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },    
//    items: [{
//        xtype: "panel", border: false, baseCls: "x-plain", layout: "column", align: "center", 
//        items: [
//        {
//            baseCls: "x-plain", align: "center", layout: "form", defaultType: "textfield", 
//            labelWidth: CodeRuleConfig.labelWidth, 	 columnWidth: 1, 
//            items: [
//				{ name: "ruleName", fieldLabel: "规则名称", width: CodeRuleConfig.fieldWidth },
//				{ name: "ruleFunction", fieldLabel: "功能点", width: CodeRuleConfig.fieldWidth }
//            ]
//        }
//        ]
//    }]
//});

////查询窗口
//CodeRuleConfig.searchWin = new Ext.Window({
//    title: "查询", 	width: 600, 	height: 400, 
//    plain: true, 		closeAction: "hide", 	items: CodeRuleConfig.searchForm, 
//    buttonAlign:"center",
//    buttons: [{
//        text: "查询", iconCls: "searchIcon", handler: function(){ 
//		    CodeRuleConfig.searchParam = CodeRuleConfig.searchForm.getForm().getValues();
//		    var searchParam = CodeRuleConfig.searchForm.getForm().getValues();
//		    searchParam = MyJson.deleteBlankProp(searchParam);
//		    CodeRuleConfig.store.load({
//		        params: {
//                    start: 0,    limit: CodeRuleConfig.pagingToolbar.pageSize,
//                    entityJson: Ext.util.JSON.encode(searchParam)
//                }	    
//		    });
//        }
//    }, {
//        text: "重置", iconCls: "resetIcon", handler: function(){ CodeRuleConfig.searchForm.getForm().reset(); }
//    }, {
//        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ CodeRuleConfig.searchWin.hide(); }                
//    }]
//});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:"fit", items:CodeRuleConfig.grid });


});