Ext.onReady(function(){
//修程定义
Ext.namespace('TestItem');                       //定义命名空间
TestItem.modelIDX = "-1" ; //试验模板主键
TestItem.repairClassIDX=null;//修程
TestItem.isNewlyMade=null;//是否新造
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
TestItem.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
//定义一个FORM表单
TestItem.form = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,		style: "padding:2px",labelWidth:80,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", columnWidth: 0.5,
            items: [{ name:"trainTypeShortName", fieldLabel:"车型" ,disabled: true},
            		{ name:"specificationModel", fieldLabel:"规格型号",disabled: true, width:100}]
        },{
        	baseCls:"x-plain", align:"right", layout:"form", defaultType:"textfield", columnWidth: 0.5, 
            items: [
					{ name:"partsName", fieldLabel:"配件名称",disabled: true, width:100}
            	]
        },{
        	baseCls:"x-plain", align:"right", layout:"form", defaultType:"combo", columnWidth:0.5, 
            items: [{
	            	xtype: 'radiogroup',
	                fieldLabel: '类型',
	                width:150,
	                items: [
	                    { boxLabel: '全部', name: 'typex', inputValue: 1 },
	                    { boxLabel: '新造', name: 'typex', inputValue: 2 },
	                    { boxLabel: '修程', name: 'typex', inputValue: 3, checked : true }
	                ] ,
	                listeners: {
	            		"change" : function(field,newValue,oldValue){
            				var val = newValue.getGroupValue();
	    					if(val == 3){
	    						Ext.getCmp("train_RC").enable();
	    						TestItem.repairClassIDX = Ext.getCmp("train_RC").getValue();
	    						TestItem.isNewlyMade = 0;
								TestItem.grid.store.load();
	    					}else if(val == 2){
	    						Ext.getCmp("train_RC").disable();
	    						TestItem.repairClassIDX = null;
	    						TestItem.isNewlyMade = 1;
	    						TestItem.grid.store.load({params:{isNewlyMade:1}});
	    					}else{
	    						Ext.getCmp("train_RC").disable();
	    						TestItem.repairClassIDX = null;
	    						TestItem.isNewlyMade = null;
	    						TestItem.grid.store.load();
	    					}
	            		}
	            	}
				}]
        },{
        	baseCls:"x-plain", align:"right", layout:"form", defaultType:"combo", columnWidth: 0.5, 
            items: [{
					xtype: "TrainRC_combo",
					id:"train_RC",
					fieldLabel: "修程",
					hiddenName: "repairClassIDX", 
					returnField: [{widgetId:"repairClassName",propertyName:"xcName"}],
					displayField: "xcName",
					valueField: "xcID",
					pageSize: 0, minListWidth: 200, width: 150,
					editable:false,
					listeners : {   
			        	"select" : function() {
        					TestItem.repairClassIDX = this.getValue();
							TestItem.grid.store.load();								
			        	}   
			    	}
				}]
        }]
    }]
});
//定义试验模板grid
TestItem.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/testItem!pageList.action',                 //装载列表数据的请求URL
    tbar:[{ text: "试验模板", xtype:"label"}],
    singleSelect: true,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'父级试验项目', dataIndex:'parentIDX', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'试验模板名称', dataIndex:'testItemName', editor:{ allowBlank: false, maxLength:100 }
	},{
		header:'试验类型', dataIndex:'testType',hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX',hidden:true, editor:{ xtype:'hidden', maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeShortName',hidden:true, editor:{ xtype:'hidden', maxLength:8 }
	},{
		header:'修程主键', dataIndex:'repairClassIDX', hidden:true,editor:{ xtype:'hidden', maxLength:8 }
	},{
		header:'修程名称', dataIndex:'repairClassName',hidden:true, editor:{ xtype:'hidden', maxLength:50 } 
	},{
		header:'是否新造', dataIndex:'isNewlyMade',hidden:true, editor:{ xtype:'hidden', maxLength:1 }
	},{
		header:'试验项目顺序', dataIndex:'testItemSeq',hidden:true, editor:{allowBlank: false, xtype:'numberfield', maxLength:1 }
	},{
		header:'备注', dataIndex:'remarks',hidden:true, editor:{ maxLength:1000 }
	},{
		header:'是否试验模板', dataIndex:'isTestTemplate', hidden:true, editor: { xtype:'hidden' }
	}],
	searchFn: function(searchParam){ 
        TestItem.grid.store.load();
	}
});
//定义试验项目grid
TestItem.itemGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/testItem!pageListItem.action',                 //装载列表数据的请求URL
    tbar:[{ text: "试验项目", xtype:"label"}],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'父级试验项目', dataIndex:'parentIDX', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'试验项目', dataIndex:'testItemName', editor:{ allowBlank: false, maxLength:100 }
	},{
		header:'试验类型', dataIndex:'testType',hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX',hidden:true, editor:{ xtype:'hidden', maxLength:8 }
	},{
		header:'车型', dataIndex:'trainTypeShortName',hidden:true, editor:{ xtype:'hidden', maxLength:8 }
	},{
		header:'修程主键', dataIndex:'repairClassIDX', hidden:true,editor:{ xtype:'hidden', maxLength:8 }
	},{
		header:'修程名称', dataIndex:'repairClassName',hidden:true, editor:{ xtype:'hidden', maxLength:50 } 
	},{
		header:'是否新造', dataIndex:'isNewlyMade',hidden:true, editor:{ xtype:'hidden', maxLength:1 }
	},{
		header:'试验项目顺序', dataIndex:'testItemSeq',hidden:true, editor:{allowBlank: false, xtype:'numberfield', maxLength:1 }
	},{
		header:'备注', dataIndex:'remarks',hidden:true, editor:{ maxLength:1000 }
	},{
		header:'是否试验模板', dataIndex:'isTestTemplate', hidden:true, editor: { xtype:'hidden' }
	}],
	searchFn: function(searchParam){ 
        TestItem.itemGrid.store.load();
	}
});
//添加事件
TestItem.grid.on("rowclick",function(grid,rowIndex){
	var record = this.store.getAt(rowIndex); //获取行记录的值
	TestItem.modelIDX= record.get("idx");  //试验项目主键
	TestItem.itemGrid.store.load();
	TestItem.itemGrid.selModel.selectAll();  //选择所有的行
});
//设置试验内容窗口
TestItem.selectWin = new Ext.Window({
	title: "试验模板选择", maximizable:false, layout: "fit", width :580,height :400,
	closeAction: "hide", modal: true, buttonAlign:"center",
	items: {
		xtype: "panel", layout: "border",
		items:[{
            region: 'north', layout: "fit",  height :78 ,frame: true, bodyBorder: false,items:[ TestItem.form ]
        },{
            region : 'center', layout : 'fit', bodyBorder: false, items : {
            	xtype: "panel", layout: "border" ,
            	items:[{
            		region: 'west', layout: "fit", width :280, minSize : 200, split : true, maxSize : 320, bodyBorder: false,items: [ TestItem.grid ]
            	},{
            		 region : 'center', layout : 'fit', bodyBorder: false, items : [ TestItem.itemGrid ]
            	}]
            }
        }]
	},
	buttons: [{
		text: "确定", iconCls: "saveIcon" , handler: function(){TestItem.submit();}
	},{
		text: "关闭", iconCls:"closeIcon" ,handler:function(){TestItem.selectWin.hide();}
	}]
});
//移除监听
TestItem.itemGrid.un('rowdblclick', TestItem.itemGrid.toEditFn, TestItem.itemGrid);
//移除监听
TestItem.grid.un('rowdblclick', TestItem.grid.toEditFn, TestItem.grid);
//确认提交方法，后面可覆盖此方法完成查询
TestItem.submit = function(){alert("请覆盖，TestItem.submit 方法完成自己操作业务！");};
//显示窗口前隐藏修次，需显示修次在点击选择试验模板按钮的事件中添加
TestItem.selectWin.on("show",function(){	
	TestItem.form.findByType("radiogroup")[0].setVisible(false);
	TestItem.form.findByType("textfield")[3].setVisible(false);
},TestItem.selectWin);
});