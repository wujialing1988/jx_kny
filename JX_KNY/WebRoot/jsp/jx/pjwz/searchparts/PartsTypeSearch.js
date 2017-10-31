Ext.onReady(function(){
	//定义命名空间
	Ext.namespace("PartsType");
	//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
	PartsType.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	//配件分类的状态
	PartsType.status = "";
	//表单组件高宽等设置
	PartsType.labelWidth = 130;
	PartsType.fieldWidth = 180;
	//信息表单
	PartsType.form = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: PartsType.labelWidth,
	    buttonAlign: "center",
	    buttons: [{
    	 text:"关闭", iconCls:"closeIcon",handler:function(){
    	 	PartsType.win.hide();
    	 }
    }
	    ],
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:PartsType.labelWidth,
	            columnWidth: 1, 
	            items: [
					{ id:"specificationModelCode", name:"specificationModelCode", fieldLabel:"规格型号编码",maxLength:100 , allowBlank:false,width:PartsType.fieldWidth },
					{ id:"specificationModel", name:"specificationModel", fieldLabel:"规格型号",maxLength:100 , allowBlank:false,width:PartsType.fieldWidth },
					{ id:"partsName", name:"partsName", fieldLabel:"配件名称",maxLength:100 , allowBlank:false,width:PartsType.fieldWidth },
					{ 
						id:"isHasSeq",
						xtype: 'radiogroup',
			            fieldLabel: '是否自带编号',
			            width:120,
			            items: [
			                {boxLabel: '是', name: 'isHasSeq', inputValue: yes},
			                {boxLabel: '否', name: 'isHasSeq', inputValue: no, checked : true}
			            ]
				},
				{ 
						id:"isHighterPricedParts",
						xtype: 'radiogroup',
			            fieldLabel: '是否高价互换配件',
			            width:120,
			            items: [
			                {boxLabel: '是', name: 'isHighterPricedParts', inputValue: yes},
			                {boxLabel: '否', name: 'isHighterPricedParts', inputValue: no, checked : true}
			            ]
				},
					{ id:"matCode", name:"matCode", fieldLabel:"物料编码",maxLength:50 , width:PartsType.fieldWidth },
                    {id:"unitCmbo",xtype:"EosDictEntry_combo",fieldLabel:"计量单位",hiddenName:"unit",
				 	displayField:"dictname",valueField:"dictname",dicttypeid:"PJWZ_Parts_TYPE_UNIT",width:PartsType.fieldWidth }
	            ]
	        },
	        {xtype:"hidden", name:"recordStatus"},
	        {xtype:"hidden", name:"idx"},
	        //暂时设置的值
	        {xtype:"hidden", id:"status", name:"status"}
	        ]
	    }]
	});
	PartsType.win = new Ext.Window({
	    title:"新增", maximizable:true, width:400, height:300,
	    plain:true,  layout:"fit", closeAction:"hide",
	    items: PartsType.form
	});
	//数据容器
	PartsType.store = new Ext.data.JsonStore({
		id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:true,
	    url: ctx + "/partsType!findpageList.action",
	    fields: [ "specificationModel","specificationModelCode","partsName","unit","timeLimit","limitKm","limitYears","status","recordStatus",
	    "siteID","creator","createTime","updator","updateTime","idx","partsClassIdx","className","professionalTypeIdx",
	    "professionalTypeName","matCode","isHighterPricedParts","isHasSeq" ]
	});
	//选择模式，勾选框可多选
	PartsType.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	//分页工具
	PartsType.pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsType.store});
	PartsType.grid = new Ext.grid.GridPanel({
	    border: false,
	    //随着窗口（父容器）大小自动调整,true表示不出现滚动条，列宽会自动压缩
	//    viewConfig: {forceFit: true},
	    //该高度设置在IE、Google浏览器显示正常，在Opera显示不正常
	    height: document.documentElement.scrollHeight,
	    //可移动列
	    enableColumnMove: true,
	//    loadMask: {msg:"正在加载表格数据，请稍等..."},
	    //偶数行变色
	    stripeRows: true,
	    //多选行
	    selModel: PartsType.sm,
	    colModel: new Ext.grid.ColumnModel([
	        PartsType.sm,
	        new Ext.grid.RowNumberer(),
	        { sortable:true, header:"规格型号编码", dataIndex:"specificationModelCode",width:130 },	
	        { sortable:true, header:"规格型号", dataIndex:"specificationModel" },		
	        { sortable:true, header:"配件名称", dataIndex:"partsName" },			
	        { sortable:true, header:"计量单位", dataIndex:"unit" },		
        	{ sortable:true, header:"物料编码", dataIndex:"matCode" },
	        { sortable:true, header:"是否自带编号", dataIndex:"isHasSeq" ,width:130,
	        	renderer : function(value, metaData, record, rowIndex, colIndex, store){
		          	if(value==yes) return "是";
		          	if(value==no) return "否";
		          }
	        	},			
	        { sortable:true, header:"是否高价互换配件", dataIndex:"isHighterPricedParts",width:130,
	        	renderer : function(value, metaData, record, rowIndex, colIndex, store){
		          	if(value==yes) return "是";
		          	if(value==no) return "否";
		          } },		
	        { sortable:true, header:"最大库存限额(月)", dataIndex:"timeLimit" }	
//	        { sortable:true, header:"状态", 
//	          dataIndex:"status", renderer:JX.getBizStatus
//	        }		
	    ]),
	    store: PartsType.store,
	    //工具栏
	    tbar: [{
	        text:"查询", iconCls:"searchIcon", handler: function(){
	        	PartsType.win.hide();
        	    PartsType.searchWin.show(); 
	       	}
	    },{
	        text:"适用车型查看", iconCls:"application_view_listIcon",
	        handler: function(){
	        	var sm = PartsType.grid.getSelectionModel();
	            var data = sm.getSelections();
	            if (sm.getCount() < 1) {
	                MyExt.Msg.alert("尚未选择一条记录！");
	                return;
	            }
	            if (sm.getCount() > 1) {
	                MyExt.Msg.alert("只能选择一条记录！");
	                return;
	            }
	            var record = data[0];
	            TrainTypeToParts.partsTypeIDX = record.get("idx");
	        	TrainTypeToParts.win.show();
	        	TrainTypeToParts.form.getForm().loadRecord(record);
				TrainTypeToParts.grid.store.load();
	        }
	    },{
	        text:"刷新", iconCls:"refreshIcon",
	        handler: function(){
	        	self.location.reload();
	        }
	        }],
	    bbar: PartsType.pagingToolbar,
	    listeners: {
	        "rowdblclick": {
	            fn: function(grid, idx, e){
	            	PartsType.searchWin.hide();
	                var record = grid.store.getAt(idx);
		            TrainTypeToParts.partsTypeIDX = record.get("idx");
		        	TrainTypeToParts.win.show();
		        	TrainTypeToParts.form.getForm().loadRecord(record);
					TrainTypeToParts.grid.store.load();	
	            }
	        }
	    }
	});
	//查询参数表单
	PartsType.searchForm = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: PartsType.labelWidth,
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:PartsType.labelWidth,
	            columnWidth: 1, 
	            items: [
					{ name:"specificationModel", fieldLabel:"规格型号",width: PartsType.fieldWidth },
					{ name:"partsName", fieldLabel:"配件名称",width: PartsType.fieldWidth },
					{ name:"matCode", fieldLabel:"物料编码",width: PartsType.fieldWidth },
					new Ext.form.ComboBox({
                        fieldLabel: '是否自带编号',
                        hiddenName:'isHasSeq',
                        store: new Ext.data.ArrayStore({
                            fields: ['val', 'state'],
                            data : [
                            	['','全部'],
                            	[yes,'是'],
                            	[no,'否']
                            ] 
                        }),
                        valueField:'val',
                        displayField:'state',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'请选择...',
                        selectOnFocus:true,
                        width:PartsType.fieldWidth,
                        editable :false
                    }),
                    new Ext.form.ComboBox({
                        fieldLabel: '是否高价互换配件',
                        hiddenName:'isHighterPricedParts',
                        store: new Ext.data.ArrayStore({
                            fields: ['val', 'state'],
                            data : [
                            	['','全部'],
                            	[yes,'是'],
                            	[no,'否']
                            ] 
                        }),
                        valueField:'val',
                        displayField:'state',
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        emptyText:'请选择...',
                        selectOnFocus:true,
                        width:PartsType.fieldWidth,
                        editable :false
                    })
	            ]
	        }
	        ]
	    }]
	});
	//查询参数对象
	PartsType.searchParam = {};
	//查询窗口
	PartsType.searchWin = new Ext.Window({
	    title:"查询", items:PartsType.searchForm,
	    width: 370, height: 250, plain: true, closeAction: "hide",buttonAlign:'center',
	    buttons: [{
	        text: "查询", iconCls: "searchIcon",
	        handler: function(){  
			    PartsType.searchParam = PartsType.searchForm.getForm().getValues();
			    var searchParam = PartsType.searchForm.getForm().getValues();
//			    for(prop in searchParam){
//			    	if(searchParam[prop] == "")	delete searchParam[prop];
//			    }
			    searchParam = MyJson.deleteBlankProp(searchParam);
			    PartsType.store.load({
			        params: {
			        start: 0,    limit: PartsType.pagingToolbar.pageSize,
			        entityJson: Ext.util.JSON.encode(searchParam),statue:PartsType.statue}
			    });
	        }
	    }, {
	        text:"重置", iconCls:"resetIcon",
	        handler:function(){	
		        PartsType.searchForm.getForm().reset();
		        PartsType.searchParam = {} ;
                PartsType.store.load();
	        }
	    },{
    	 text:"关闭", iconCls:"closeIcon",handler:function(){
    	 	PartsType.searchWin.hide();
    	 }
    }]
	});
		//页面自适应布局
	var viewport = new Ext.Viewport({ 
		layout:"fit", 
		items: [PartsType.grid
//	    }
	    ] 
	});
	//只查询启用状态的配件规格型号
	PartsType.statue = status_use;
	PartsType.store.on("beforeload", function(){
//		var beforeloadParam = {partsClassIdx: PartsType.partsClassIdx};
		this.baseParams.statue = PartsType.statue;  
		this.baseParams.entityJson = Ext.util.JSON.encode(PartsType.searchParam);  
	});
});
