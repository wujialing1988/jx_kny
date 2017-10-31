/**
 * 配件扩展编号 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsExtendNo');                       //定义命名空间
PartsExtendNo.partsTypeIDX = "";
PartsExtendNo.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
PartsExtendNo.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
PartsExtendNo.store = new Ext.data.JsonStore({
	id: "idx", root: "root", totalProperty: "totalProperty", autoLoad: false,
    url: ctx + "/partsExtendNo!getExtendNoList.action",
    fields: [ "idx","partsTypeIDX", "extendNoField","extendNoName", "isUsed"]
});
PartsExtendNo.grid = new Ext.grid.EditorGridPanel({	
	border: false, enableColumnMove: true, stripeRows: true, 
    viewConfig: {forceFit: true}, selModel: PartsExtendNo.sm,
    clicksToEdit: 1,
    store: PartsExtendNo.store, loadMask: PartsExtendNo.loadMask,
	hideRowNumberer: true,
	tbar:[{
		iconCls: 'saveIcon', text: '保存配件扩展编号',
		handler: function() {
			if(Ext.isEmpty(PartsExtendNo.partsTypeIDX)) {
				MyExt.Msg.alert("请先选择配件型号！");
				return;
			}
			var itemDataArray = [];
			for (var i = 0; i < PartsExtendNo.grid.store.getCount(); i++) {
				var data = PartsExtendNo.grid.store.getAt(i).data;
				if(Ext.isEmpty(data.extendNoName)){
					continue;
				}
				var itemData = {} ;
				itemData.idx = data.idx;
				itemData.partsTypeIDX = data.partsTypeIDX;
				itemData.extendNoField = data.extendNoField;
				itemData.extendNoName = data.extendNoName;
				itemData.isUsed = data.isUsed;
				itemDataArray.push(itemData);
			}
			if (itemDataArray.length == 0) {
				MyExt.Msg.alert("请输入配件扩展编号名称！");
				return;
			}
			Ext.Msg.confirm("提示", "确认处理完成", function(btn) {
				if (btn == 'yes') {
					PartsExtendNo.loadMask.show();
					Ext.Ajax.request({
						url : ctx + "/partsExtendNo!saveExtendNo.action",
						params : {
							itemDataArray : Ext.util.JSON.encode(itemDataArray)
						},
						success : function(response, options) {
							if (PartsExtendNo.loadMask)
								PartsExtendNo.loadMask.hide();
							var result = Ext.util.JSON.decode(response.responseText);
							if (result.errMsg) {
								alertFail(result.errMsg);
							} else {
								alertSuccess();
								PartsExtendNo.grid.store.reload();
							}
						}
					});
				}
			});
		}
	},{
		iconCls: 'deleteIcon', text: '删除配件扩展编号',
		handler: function() {
			if(Ext.isEmpty(PartsExtendNo.partsTypeIDX)) {
				MyExt.Msg.alert("请先选择配件型号！");
				return;
			}
			var grid = PartsExtendNo.grid;
			if (!$yd.isSelectedRecord(grid)) return;
			var data = grid.selModel.getSelections();
		    var ids = [];
		    for (var i = 0; i < data.length; i++){
		    	if(!Ext.isEmpty(data[ i ].get("idx")))
		        	ids.push(data[ i ].get("idx"));
		    }
		    if(ids.length == 0) {
		    	MyExt.Msg.alert("只能删除配件扩展编号名称不为空的记录！");
				return;
		    }
		    $yd.confirmAndDelete({
	            scope: grid, url: ctx + '/partsExtendNo!logicDelete.action', params: {ids: ids}
	        });
		}
	},{
		iconCls: 'refreshIcon', text: '刷新',
		handler: function() {
			if(Ext.isEmpty(PartsExtendNo.partsTypeIDX)) {
				MyExt.Msg.alert("请先选择配件型号！");
				return;
			}
			PartsExtendNo.grid.store.reload();
		}
	}],
	colModel: new Ext.grid.ColumnModel([
		PartsExtendNo.sm,
		{ header:'idx', dataIndex:'idx',hidden: true},
		{ header:'partsTypeIDX', dataIndex:'partsTypeIDX',hidden: true},
		{ header: "对应扩展编号字段",  dataIndex: "extendNoField",width:150},
		{ header: "扩展编号名称",  dataIndex: "extendNoName",width:200,
    	  editor:new Ext.form.TextField({
            maxLength:20
    	  })
    	},
    	{
    	  header: "是否启用",  dataIndex: "isUsed",
    	  editor: {
    	  		xtype: 'combo',
	            hiddenName:'isUsed',
	            store:new Ext.data.SimpleStore({
				    fields: ['v'],
					data : [[IS_USED],[NO_USED]]
				}),
				valueField:'v',
				displayField:'v',
				triggerAction:'all',
				mode:'local',
				editable: false,
				allowBlank: false

    	  }
    	}
	]),
	afterDeleteFn: function(){ }
});


PartsExtendNo.titleForm = new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,	labelWidth: 50,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.5, 
            items: [
        		{ fieldLabel:"规格型号编码", name:"specificationModelCode", width:300, style:"border:none;background:none;", readOnly:true},
        		{ fieldLabel:"配件名称",    name:"partsName",              width:300, style:"border:none;background:none;", readOnly:true}
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.5, 
            items: [
                { fieldLabel:"规格型号", name:"specificationModel", width:300, style:"border:none;background:none;", readOnly:true},
                { fieldLabel:"物料编码", name:"matCode", 			  width:300, style:"border:none;background:none;", readOnly:true}
            ]
        }]
    }]
});

PartsExtendNo.centerPanel = new Ext.Panel({
	layout: "border",
	items: [{
            region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',collapsible:true, height: 82, bodyBorder: false,items:[PartsExtendNo.titleForm]
        },{
            region : 'center', layout : 'fit', bodyBorder: false, items : [ PartsExtendNo.grid ]
        }
	]
});
PartsExtendNo.panel = new Ext.Panel({
	layout:"border",
    items : [ {        
        collapsible : true,
        width : 270,
        minSize : 160,
        maxSize : 280,
        split : true,
        region : 'west',
        bodyBorder : false,
        autoScroll : true,
		layout : 'fit',
        items : [ jx.pjwz.partbase.component.PartsTypeTree.panel ]
    }, {
        region : 'center',
        layout : 'fit',
        bodyBorder: false,
        items : [ PartsExtendNo.centerPanel ]
    }]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:PartsExtendNo.panel });
jx.pjwz.partbase.component.returnFn = function(node) {
	var form = PartsExtendNo.titleForm.getForm();
	form.reset();
	form.findField("specificationModelCode").setValue(node.attributes["specificationModelCode"]);
	form.findField("partsName").setValue(node.attributes["text"]);
	form.findField("specificationModel").setValue(node.attributes["specificationModel"]);
	form.findField("matCode").setValue(node.attributes["matCode"]);
	PartsExtendNo.partsTypeIDX = "";
	PartsExtendNo.partsTypeIDX = node.id;
	PartsExtendNo.grid.store.baseParams.partsTypeIDX = node.id;
	PartsExtendNo.grid.store.load();
}
});