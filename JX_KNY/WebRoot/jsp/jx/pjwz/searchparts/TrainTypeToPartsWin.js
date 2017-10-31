Ext.onReady(function(){
//修程定义
Ext.namespace('TrainTypeToParts');                       //定义命名空间
TrainTypeToParts.partsTypeIDX = "";
//定义一个FORM表单
TrainTypeToParts.form = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,		style: "padding:2px",labelWidth:80,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", columnWidth: 0.5,
            items: [{ name:"partsName", fieldLabel:"配件名称",disabled: true},
            { name:"unit", fieldLabel:"计量单位",disabled: true}
            		]
        },{
        	baseCls:"x-plain", align:"right", layout:"form", defaultType:"textfield", columnWidth: 0.5, 
            items: [
					{ name:"specificationModel", fieldLabel:"规格型号",disabled: true},
					{ name:"matCode", fieldLabel:"物料编码",disabled: true}
            	]
        }]
    }]
});
//定义试验模板grid
TrainTypeToParts.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainTypeToParts!findPageListForPartsTypeSearch.action',                 //装载列表数据的请求URL
    singleSelect: true,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型', dataIndex:'trainTypeShortName',width:70
	},{
		header:'数量', dataIndex:'standardQty',width:70
	},{
		header:'修程及分类', dataIndex:'RCandCLASS',width:200
	}],
	tbar: []
});

//设置试验内容窗口
TrainTypeToParts.win = new Ext.Window({
	title: "适用车型", maximizable:false, layout: "fit", width :580,height :400,
	closeAction: "hide", modal: true, buttonAlign:"center",
	items: {
		xtype: "panel", layout: "border",
		items:[{
            region: 'north', layout: "fit",  height :78 ,frame: true, bodyBorder: false,items:[ TrainTypeToParts.form ]
        },{
            region : 'center', layout : 'fit', bodyBorder: false, items : TrainTypeToParts.grid
        }]
	},
	buttons: [{
		text: "关闭", iconCls:"closeIcon" ,handler:function(){TrainTypeToParts.win.hide();}
	}]
});
//移除监听
TrainTypeToParts.grid.un('rowdblclick', TrainTypeToParts.grid.toEditFn, TrainTypeToParts.grid);
TrainTypeToParts.grid.store.on("beforeload", function(){
	this.baseParams.partsTypeIDX = TrainTypeToParts.partsTypeIDX;  
});
});