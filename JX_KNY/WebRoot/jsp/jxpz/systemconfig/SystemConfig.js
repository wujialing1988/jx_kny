/**
 * 系统配置项 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
//定义命名空间
Ext.namespace("SystemConfig");
SystemConfig.parentKey = "ROOT_0";   //父级键
SystemConfig.searcheParams = {};    //查询条件
SystemConfig.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/systemConfig!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/systemConfig!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/systemConfig!logicDelete.action',            //删除数据的请求URL
    storeId:'key',viewConfig: {forceFit: false, markDirty: false},
	saveFormColNum:2,fieldWidth:200,
	fields: [{
		header:'编码', dataIndex:'id',hidden:true,editor:{id:"ID",allowBlank:false, maxLength:5},searcher:{disabled:true}
	},{
		header:'名称', dataIndex:'configName', editor:{ allowBlank:false, maxLength:50 }
	},{
		header:'键', dataIndex:'key', editor:{ allowBlank:false, maxLength:50 },width:150
	},{
		header:'键值', dataIndex:'keyValue', editor:{ allowBlank:false, maxLength:50 },searcher:{disabled:true}
	},{
		header:'配置说明', dataIndex:'configDeclare', editor:{ xtype:"textarea",allowBlank:false, maxLength:20 },searcher:{disabled:true}
	},{
		header:'配置描述', dataIndex:'configDesc', editor:{ xtype:"textarea",allowBlank:false, maxLength:20 },searcher:{disabled:true}
	}],
	tbar: ['search','add','delete','refresh'],
	searchFn: function(searchParam){ 
    	SystemConfig.searcheParams = searchParam ;
        this.store.load();
	},
//	beforeGetFormData : function(){
//		Ext.getCmp("i_key").enable();
//	},
//	afterGetFormData: function(){
//		Ext.getCmp("i_key").disable();
//	},    
	beforeSaveFn: function(data){
		if(data.parentKey == "" || data.parentKey == null){
			SystemConfig.parentKey = SystemConfig.parentKey == "" ? "ROOT_0" : SystemConfig.parentKey;
			data.parentKey = SystemConfig.parentKey;
		}
		return true; 
	},
	afterDeleteFn: function(){ 
//		this.store.reload();
//        alertSuccess();
        SystemConfig.tree.root.reload();
        SystemConfig.tree.getRootNode().expand();
	},
	afterSaveSuccessFn: function(result, response, options){
        this.store.reload();
        SystemConfig.grid.saveWin.hide();
        alertSuccess();
        SystemConfig.tree.root.reload();
        SystemConfig.tree.getRootNode().expand();
    }
});
//移除侦听器
SystemConfig.grid.un('rowdblclick', SystemConfig.grid.toEditFn, SystemConfig.grid);
SystemConfig.grid.store.on("beforeload",function(){
	var searchParam = SystemConfig.searcheParams;
	SystemConfig.parentKey = SystemConfig.parentKey == "ROOT_0" ? "" : SystemConfig.parentKey;
	searchParam.parentKey = SystemConfig.parentKey;
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam); 
})
//当前点击的树节点id
SystemConfig.currentNodeId = "ROOT_0";
//系统配置树 
SystemConfig.tree = new Ext.tree.TreePanel( {
    loader : new Ext.tree.TreeLoader( {
        dataUrl : ctx + "/systemConfig!tree.action"
    }),
    root: new Ext.tree.AsyncTreeNode({
       	text:"系统配置树",
		id:"ROOT_0",
		isLastLevel: 0,
		classID : "",
		parentIDX : "",
		status : 0,
		leaf:false
    }),
    rootVisible: true,
    autoScroll : false,
    animate : false,
    useArrows : false,
    border : false,
    listeners: {
        click: function(node, e) {
        	SystemConfig.parentKey = node.id;
        	if(SystemConfig.parentKey != "ROOT_0"){
        		var data = node.attributes.record;
        		var record=new Ext.data.Record();
				for(var i in data){
					record.set(i,data[i]);
				}
				SystemConfig.baseForm.getForm().reset();
        		SystemConfig.baseForm.getForm().loadRecord(record);
        	}
        	SystemConfig.grid.store.load();
        }
    }    
});
SystemConfig.tree.on('beforeload', function(node){
    SystemConfig.tree.loader.dataUrl = ctx + '/systemConfig!tree.action?parentKey=' + node.id;
});
SystemConfig.tree.getRootNode().expand();

//信息表单
SystemConfig.grid.saveForm = new Ext.form.FormPanel({
   layout: "form",     border: false,      style: "padding:10px",      labelWidth: this.labelWidth,
    baseCls: "x-plain", align: "center",    defaultType: "textfield",   defaults: { anchor: "95%" },
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", 
            columnWidth: 1, 
            items: [
	            { id:"i_id",name:"id", fieldLabel:"编码",maxLength:50 ,width:400,
					validator:function(v){	
						if(v == ''){
							return "必填项";
						}
	//					var configName = Ext.getCmp("i_configName").getValue();
						
						if(SystemConfig.parentKey != '' && SystemConfig.parentKey != 'ROOT_0'){
							Ext.getCmp("i_key").setValue(SystemConfig.parentKey+"."+v);
						}else Ext.getCmp("i_key").setValue(v);
				}},
				{ id:"i_key",name:"key", fieldLabel:"键",maxLength:500 ,style:"background:#CEF;color:#00F;", readOnly:true, allowBlank:false,width:400},
				{ id:"i_configName",name:"configName", fieldLabel:"名称",maxLength:100 ,allowBlank:false,width:400
	//				validator:function(v){	
	//					if(v == ''){
	//						return "必填项";
	//					}
	//					var id = Ext.getCmp("i_id").getValue();
	//					if(id != ''){
	//						Ext.getCmp("i_key").setValue(id+"-"+v);
	//					}
				},
				{ name:"keyValue", fieldLabel:"键值",maxLength:100 , width:400},
				{ name:"configDeclare", fieldLabel:"配置说明",maxLength:500 ,xtype:"textarea",width:450},
				{ name:"configDesc", fieldLabel:"描述",maxLength:100 ,xtype:"textarea",width:450},
				{ name:"parentKey", fieldLabel:"父级键",xtype:"hidden"}
			]
        }
        ]
    }]
});
SystemConfig.baseForm = new Ext.form.FormPanel({
   layout: "form",     border: false,      style: "padding:14px",      labelWidth: this.labelWidth,
    baseCls: "x-plain", align: "center",    defaultType: "textfield",   defaults: { anchor: "95%" },
    buttonAlign:"center",
	buttons:[
    { 
    	id:"submitBaseBtn", text:"保存", iconCls:"saveIcon",
    	handler:function(){
            var form = SystemConfig.baseForm.getForm();
            if (!form.isValid()) return;
            var url = ctx + "/systemConfig!saveOrUpdate.action";
//            SystemConfig.enableColums();
            var data = form.getValues();
//            SystemConfig.disableColums();
            Ext.Ajax.request({
                url: url,
                jsonData: data,
                success: function(response, options){
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        alertSuccess();
                        SystemConfig.tree.root.reload();
                        SystemConfig.tree.getRootNode().expand();
                    } else {
                        alertFail(result.errMsg);
                    }
                },
                failure: function(response, options){
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
    	}
    }],
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", 
            columnWidth: 1, 
            items: [
            	{ id:"b_id",name:"id", fieldLabel:"编码",maxLength:100 , style:"background:#CEF;color:#00F;", readOnly:true,width:320},
				{ id:"b_key",name:"key", fieldLabel:"键",maxLength:100 , style:"background:#CEF;color:#00F;", readOnly:true,width:320},
            	{ name:"configName", fieldLabel:"名称",maxLength:100 , allowBlank:false,width:320},
				{ name:"keyValue", fieldLabel:"键值",maxLength:100 , width:320},
				{ id:"b_configDeclare",name:"configDeclare", fieldLabel:"配置说明",maxLength:500 ,xtype:"textarea",style:"background:#CEF;color:#00F;", readOnly:true,width:360},
				{ id:"b_configDesc",name:"configDesc", fieldLabel:"描述",maxLength:100 ,xtype:"textarea",style:"background:#CEF;color:#00F;", readOnly:true,width:360},
				{ name:"parentKey", fieldLabel:"父级键",xtype:"hidden"}
			]
        }
        ]
    }]
});
//tab选项卡
SystemConfig.tabs = new Ext.TabPanel({
    activeTab: 0,
    frame:true,
    items:[{  
           id: "baseinfoTab",
           title: '配置信息',
           layout:'fit',
           frame:true,
           items: [SystemConfig.baseForm]
        },{
           id: "propertyTab",
           title: '下级配置',
           layout:'fit',
           items: [SystemConfig.grid]
        }]
});
SystemConfig.enableColums = function(){
	Ext.getCmp("b_id").enable();
	Ext.getCmp("b_key").enable();
	Ext.getCmp("b_configDeclare").enable();
	Ext.getCmp("b_configDesc").enable();
}
SystemConfig.disableColums = function(){
	Ext.getCmp("b_id").disable();
	Ext.getCmp("b_key").disable();
	Ext.getCmp("b_configDeclare").disable();
	Ext.getCmp("b_configDesc").disable();
}
//页面布局
var viewport = new Ext.Viewport({
    layout:"fit",
    items: {
    	layout:"border",
	    items : [ {
	        title : '<span style="font-weight:normal">系统配置树</span>',
	        iconCls : 'chart_organisationIcon',
	        tools : [ {
	            id : 'refresh',
	            handler : function() {
	                SystemConfig.tree.root.reload();
	                SystemConfig.tree.getRootNode().expand();
	            }
	        } ],
	        collapsible : true,
	        width : 210,
	        minSize : 160,
	        maxSize : 280,
	        split : true,
	        region : 'west',
	        bodyBorder: false,
	        autoScroll : true,
	        items : [ SystemConfig.tree ]
	    }, {
	        region : 'center',
	        layout : 'fit',
	        bodyBorder: false,
	        items : [ SystemConfig.tabs ]
	    } ]
    }

});
});