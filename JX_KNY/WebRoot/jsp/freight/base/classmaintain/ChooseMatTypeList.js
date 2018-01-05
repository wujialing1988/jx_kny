/**
 * 物料 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('MatTypeList');                       //定义命名空间
//查询参数对象
MatTypeList.searchParam = {};
MatTypeList.matTypeItems = [] ;
// 自动生成“物料类型”radiogroup内容
MatTypeList.createMatTypeItemsFn = function() {
	for (var i = 0; i < objList.length; i++) {
        var field = objList[ i ];
        var editor = {};  			// 定义检查项
        editor.name = "matType"; 	
        editor.boxLabel = field.dictname;
        editor.inputValue = field.dictname;
        editor.width = 80;
        MatTypeList.matTypeItems.push(editor);
	}
}();
MatTypeList.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/matTypeList!findPageList.action',                 //装载列表数据的请求URL
    storeAutoLoad: false,storeId: 'matCode',
	fields: [{
			header:i18n.ChooseMatTypeList.matCode, dataIndex:'matCode', editor:{ id:'matCode', maxLength:50, allowBlank: false }, width: 120
		},{
			header:i18n.ChooseMatTypeList.matDesc, dataIndex:'matDesc', editor:{  maxLength:50, allowBlank: false }, width: 420
		},{
			header:i18n.ChooseMatTypeList.unit, dataIndex:'unit', editor:{  maxLength:20, allowBlank: false }, width: 80				
		}],
	tbar: [{
        xtype:"label", text:i18n.ChooseMatTypeList.matCode+"： " 
    },{
        xtype: "textfield",id:"MatTypeList_matCode",name:"matCode"
    },{
        xtype:"label", text:i18n.ChooseMatTypeList.matDesc+"： " 
    },{
        xtype: "textfield",id:"MatTypeList_matDesc",name:"matDesc"
    },{
        text:i18n.ChooseMatTypeList.search, iconCls:"searchIcon", handler: function(){
			MatTypeList.grid.store.load();
        }        
    }]
});
//移除组织机构列表的侦听器
MatTypeList.grid.un('rowdblclick', MatTypeList.grid.toEditFn, MatTypeList.grid);
MatTypeList.grid.store.on('beforeload',function(){
	var searchParam = MatTypeList.searchParam ;
	var matCode = Ext.get("MatTypeList_matCode").getValue();
	var matDesc = Ext.get("MatTypeList_matDesc").getValue();
	searchParam.matCode = matCode ;
	searchParam.matDesc = matDesc ;
	searchParam=MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson=Ext.util.JSON.encode(searchParam);
});
//选择物料窗口
MatTypeList.selectWin = new Ext.Window({
    title: i18n.ChooseMatTypeList.matChoice, layout: "border", width: 650, height: 460, modal: true, 
    closeAction: "hide", buttonAlign:'center', 
    items: [{
			layout: "fit",
			region : 'center',
	        bodyBorder: false,
	        autoScroll : true,
	        items : [ MatTypeList.grid ]
	},{
			xtype: 'form', height: 30, style: 'background:red;',
			id:"qcForm_0",
			labelWidth:100,
			labelAlign:"left",
			region : 'south',
			baseCls:"x-plain", border:false,
			height:30,
			style: 'padding:0px 10px 10px 10px;',
			items:[
				{
					id:"matTypeItems_0",xtype: 'radiogroup', fieldLabel: i18n.ChooseMatTypeList.matType, items: MatTypeList.matTypeItems, anchor:'80%',
	                listeners: {
	            		"render" : function(field){
	            			//将选中的数据标红
	            		}
	            	}
				}]
		}],
    	buttons: [{
	        text:i18n.ChooseMatTypeList.confirm, iconCls:"saveIcon", handler: function(){
	            var sm = MatTypeList.grid.getSelectionModel();
	            if (sm.getCount() < 1) {
	                MyExt.Msg.alert(i18n.ChooseMatTypeList.NoChoice);
	                return;
	            }
	            var data = sm.getSelections();
	            var datas = new Array();
	            var matType = Ext.getCmp("matTypeItems_0").getValue().inputValue ;
	            for (var i = 0; i < data.length; i++){
	            	var data_v = data[ i ].data ;
	            	data_v.matType = matType ;
	                datas.push(data_v);
	            }
	            Ext.Ajax.request({
	                url: ctx + "/whMatQuota!saveWhMatQuotaBatchForWorkPlace.action",
	                jsonData: datas,
	                params: {workplaceCode : WhMatQuota.workplaceCode , workplaceName : WhMatQuota.workplaceName},
	                success: function(response, options){
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        MatTypeList.grid.store.reload(); 
						    WhMatQuota.grid.store.load();
						    MatTypeList.selectWin.hide();
	                    } else {
	                        alertFail(result.errMsg);
	                    }
	                },
	                failure: function(response, options){
	                    MyExt.Msg.alert(i18n.ChooseMatTypeList.false+"\n" + response.status + "\n" + response.responseText);
	                }
	            });
	        }
	    }]
});
MatTypeList.init = function(){
		Ext.getCmp("matTypeItems_0").setValue(objList[ 0 ].dictname);
	};
	MatTypeList.init();
});