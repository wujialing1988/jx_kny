/**
 * 机车零部件 规格型号生成二维码
 */
 Ext.onReady(function(){
 	Ext.namespace("JcpjzdBuildCode");
 
	JcpjzdBuildCode.insertRow = function(records){
			JcpjzdBuildCode.grid.store.removeAll();
			for(var i=0;i<records.length;i++){
				var r = records[i];
				var specificationModelCode = r.get('specificationModelCode');
				var specificationModel = r.get('specificationModel');
				var partsName = r.get('partsName');
				var defaultData = {
					specificationModelCode:specificationModelCode,
					specificationModel:specificationModel,
					nodepjbm:JcpjzdBuild.nodepjbm,
					partsName:partsName,
					xcwzbm:'',
					xcwzmc:''
					};
				var initData = Ext.apply({}, defaultData); 
				var record = new Ext.data.Record(defaultData);
		        JcpjzdBuildCode.grid.store.insert(0, record); 
			}
	}
	
	// 复制数据
	JcpjzdBuildCode.copyRows = function(records,addnumber){
			for(var i=0;i<records.length;i++){
				var r = records[i];
				var specificationModelCode = r.get('specificationModelCode');
				var specificationModel = r.get('specificationModel');
				var partsName = r.get('partsName');
				var defaultData = {
					specificationModelCode:specificationModelCode,
					specificationModel:specificationModel,
					nodepjbm:JcpjzdBuild.nodepjbm,
					partsName:partsName,
					xcwzbm:'',
					xcwzmc:''
				};
				var initData = Ext.apply({}, defaultData); 
				var record = new Ext.data.Record(defaultData);
				for(var n = 0 ; n < addnumber ; n++){
			        JcpjzdBuildCode.grid.store.insert(0, record); 
				}
			}
			JcpjzdBuildCode.grid.getView().refresh();
	}
	 
	JcpjzdBuildCode.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	 
	 
	JcpjzdBuildCode.store = new Ext.data.ArrayStore({
	    autoDestroy: true,
	    storeId: 'jcpjzdBuildCodeStore',
	    pruneModifiedRecords:true,
	    idIndex: 0,  
	    fields: [
	       {name: 'specificationModelCode'},
	       {name: 'specificationModel'},
	       {name: 'partsName'},
	       {name: 'xcwzbm'},
	       {name: 'nodepjbm'},
	       {name: 'xcwzmc'}
	    ]
	});
	 
	JcpjzdBuildCode.grid = new Ext.grid.EditorGridPanel({
		    border: false, 
		    enableColumnMove: true, 
		    stripeRows: true, 
		    singleSelect : true,
		    selModel: JcpjzdBuildCode.sm ,
		    loadMask:true,
		    clicksToEdit: 1,
		    height:200,
		    viewConfig: {forceFit: true},
		    colModel: new Ext.grid.ColumnModel([
		    	  JcpjzdBuildCode.sm,
		    	  new Ext.grid.RowNumberer(),
		    	 { sortable:false, header: "零部件编码",  dataIndex: "nodepjbm",width:100 },
		    	 { sortable:false, header: "配件名称",  dataIndex: "partsName",width:100 },
		    	 { sortable:true, header: "规格型号",  dataIndex: "specificationModel",width:100 },
		         { sortable:false, header: "规格型号编码",  dataIndex: "specificationModelCode",width:100 },
		    	 { sortable:true, header: "下车位置",  dataIndex: "xcwzmc",
		    		editor:{
		    			id: "JcpjzdBuildCode_xcwzmc_id",
						xtype: "Base_combo",
						fieldLabel: "位置",
						hiddenName: "xcwzmc",
						returnField: [{ widgetId: "JcpjzdBuildCode_xcwzbm_id", propertyName: "partId" }],
						displayField: "partName", valueField: "partName",
						entity: "com.yunda.jx.component.entity.EquipPart",
						fields: ["partId", "partName"],
						pageSize: 20,
						minListWidth: 200,
						editable: false
			 	 	}
			 	},
			 	{
					sortable:false, header: '下车位置编码', dataIndex: 'xcwzbm', hidden:true,
					editor: { id: "JcpjzdBuildCode_xcwzbm_id",xtype:"textfield",hiddenName:'xcwzbm' }    	  
		        }
		    ]),
			listeners: {
				cellclick:function(grid, rowIndex, columnIndex, e) {
					
				},
				afteredit:function(e){
					var xcwzbm = Ext.getCmp('JcpjzdBuildCode_xcwzbm_id').getValue();
					e.record.data.xcwzbm = xcwzbm;
					JcpjzdBuildCode.grid.getView().refresh();
				}
			},
		    store: JcpjzdBuildCode.store,
		    tbar: [/*
		    '复制数量：',
	    	 {xtype:'textfield',id:"addnumber", name:"addnumber",maxLength:2 ,vtype: "positiveInt", allowBlank:false,width:50},
	    	 '&nbsp;&nbsp;<span id="spUnit"><span>&nbsp;&nbsp;',*/
			{
				id: 'copy_data', text:'复制数据', iconCls:'addIcon', 
				handler: function(){
					var sm = JcpjzdBuildCode.grid.getSelectionModel();
            		var data = sm.getSelections();
            		//var addnumber = Ext.getCmp('addnumber').getValue();
            		if (sm.getCount() < 1) {
		                MyExt.Msg.alert("尚未选择一条记录！");
		                return;
	            	}
//	            	if(!addnumber){
//	            		MyExt.Msg.alert("请输入复制数量！");
//		                return;
//	            	}
	            	// 复制数量
	            	JcpjzdBuildCode.copyRows(data,1);
				}
			},{
				id: 'delete_data', text:'删除数据', iconCls:'addIcon', 
				handler: function(){
					var sm = JcpjzdBuildCode.grid.getSelectionModel();
            		var data = sm.getSelections();
            		if (sm.getCount() < 1) {
		                MyExt.Msg.alert("尚未选择一条记录！");
		                return;
	            	}
	            	for (var i = 0; i < data.length; i++){
			        	JcpjzdBuildCode.grid.store.remove(data[i]);
			    	}
			    	JcpjzdBuildCode.grid.getView().refresh(); 
				}
			},{
				id: 'QRcode', text:'生成二维码', iconCls:'addIcon', 
				handler: Generate
			},{
	            id: 'print', text: "打印", iconCls: "printerIcon", handler: SetPrint, disabled: true
			}
	        ]
	 });
	 
	JcpjzdBuildCode.win = new Ext.Window({
	        title:"二维码生成",
	        width:800, 
	        height:600, 
	        plain:true, 
	        closeAction:"hide", 
	        maximizable:true,
	        modal:true,
	        layout:'border',
	        defaults:{border:false},
	        items:[{
		        region: 'north', layout: "fit",bodyStyle:'padding-left:0px;',height: 300, bodyBorder: false,
		        items:[JcpjzdBuildCode.grid], frame: true, title: "", xtype: "panel"
    		},{
	         region : 'center', layout : 'border', bodyBorder: false, autoScoll: true, items : [{
			        id: 'qrcodelist', region: 'center', layout: "fit",bodyStyle:'padding-left:100px; padding-right:100px;', bodyBorder: false,
			        items:[], frame: true,  xtype: "panel"
			    },{
			        id: 'qrcode', region: 'east', layout: "fit", bodyBorder: false, 
			        items:[], frame: true, title: "", xtype: "panel"
			    }
	        ]
	     }],
	     listeners: {
	     	show:function(){
	     		document.getElementById("qrcodelist").innerHTML = "";
	     		JcpjzdBuildCode.grid.getView().refresh(); 
	     	}
	     }
	});

 });
 
 
 
 
 
 
 
 