Ext.onReady(function(){

	Ext.namespace('TecCardConfig');
	/** ************** 定义全局变量开始 ************** */
	TecCardConfig.labelWidth = 80;
	TecCardConfig.fieldWidth = 140;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义上方表单开始 ************** */
	TecCardConfig.form = new Ext.form.FormPanel({
		style: 'padding: 10px;',
		layout:"column",
		defaults:{
			defaults:{readOnly: true}
		},
		items:[{
			columnWidth:0.5,
			labelWidth: TecCardConfig.labelWidth,
			layout:"form",
			items:[
				{
					xtype:"textfield",
					fieldLabel:"工艺卡编号",
					name: "tecCardNo", id: "tecCardNo_c",
					anchor: '95%',
					style: 'background:none; border: none;'
				}
			]
		},
		{
			columnWidth:0.5,
			layout:"form",
			labelWidth: TecCardConfig.labelWidth,
			items:[
				{
					xtype:"textfield",
					fieldLabel:"顺序号",
					name: "seqNo", id: "seqNo_c",
					anchor: '95%',
					style: 'background:none; border: none;'
				}
			]
		},
		{
			layout:"form",
			columnWidth:1,
			labelWidth: TecCardConfig.labelWidth,
			items:[
				{
					xtype:"textfield",
					fieldLabel:"工艺卡描述",
					name:"tecCardDesc", id: "tecCardDesc_c",
					anchor: '95%',
					style: 'background:none; border: none;'
				}
			]
		},
		{
			layout:"form",
			columnWidth:1,
			items:[
				{
					xtype:"hidden", fieldLabel:"idx主键", name:"idx", id: "idx_c"
				}, {
					xtype:"hidden", fieldLabel:"检修工艺idx主键", name:"tecIDX", id: "tecIDX_c"
				}
			]
		}]
	
	});
	/** ************** 定义上方表单结束 ************** */
	
	//tab选项卡
	TecCardConfig.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    border: false,
	    frame:true,
	    items:[{  
				id: "TecCardTab",
				title: '工艺卡',
				layout:'fit',
				items: [{
	           		layout: 'border',		
	           		items:[{
	           			region: 'center',
	           			layout: 'fit',
	           			items:[TecCardWS.grid]
	           		}, {
				        region : 'west',
				        title : '<span style="font-weight:normal">工序树</span>',
				        iconCls : 'icon-expand-all',
				        tools : [ {
				            id : 'refresh',
				            handler : function() {
				                TecCardWS.tree.root.reload();
				                TecCardWS.tree.getRootNode().expand();
				            }
				        } ],
//				        collapsible : true,
				        width : 210,
				        minSize : 160,
				        maxSize : 280,
				        split : true,
				        autoScroll : true,
				        items : [ TecCardWS.tree ]
				    }]
				}]
	        }/*,{
				id: "TecCardMatTab",
				title: '所需物料',
				layout:'fit',
				items: [TecCardMat.grid]
	        }*/]
	});
	
	TecCardConfig.win = new Ext.Window({
		title:"工艺卡维护",
		width:811,
		height:683,
		layout:"border",
		closeAction:"hide",
		maximized:true,
		items:[
			{
				region:"center", border: false,
				layout:"fit",
				items:[TecCardConfig.tabs]
			},
			{
				region:"north", border: false,
				height:110,
				layout:"fit",
				title:"工艺卡信息",
				collapsible: true,
				frame: true,
				items:[TecCardConfig.form]
			}
		],
		buttonAlign: 'center',
		buttons: [{
			text:'关闭', iconCls:'closeIcon', handler: function() {
				TecCardConfig.win.hide();
			}
		}]
});


})